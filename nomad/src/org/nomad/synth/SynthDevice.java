/* Copyright (C) 2006 Christian Schneider
 * 
 * This file is part of Nomad.
 * 
 * Nomad is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Nomad is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Nomad; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

/*
 * Created on Feb 14, 2006
 */
package org.nomad.synth;

import java.util.ArrayList;

import javax.sound.midi.MidiDevice;

import net.sf.nmedit.jnmprotocol.IAmMessage;
import net.sf.nmedit.jnmprotocol.MidiDriver;
import net.sf.nmedit.jnmprotocol.MidiMessage;
import net.sf.nmedit.jnmprotocol.NmProtocol;

import org.nomad.dialog.ExceptionNotificationDialog;
import org.nomad.dialog.NomadMidiDialog;
import org.nomad.env.Environment;
import org.nomad.patch.format.PatchMessageDecoder;

// TODO if heartbeat fails, disconnect() should be called
public class SynthDevice implements HeartbeatErrorHandler {

	private Synth synth ;

	private MidiDevice.Info midiIn = null;
	private MidiDevice.Info midiOut = null;

	private MidiDriver driver = null;
	private NmProtocol protocol = null;
	private HeartbeatTask hbtask = null;
	ProtocolListener protocolListener = null;
	
	private ArrayList<SynthDeviceStateListener>
		connectionListenerList ;
	
	public SynthDevice() {
		PatchMessageDecoder.init();
	    try {
			MidiMessage.usePdlFile("/usr/local/lib/nmprotocol/midi.pdl", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		synth = new Synth(this);
		protocolListener = new ProtocolListener(this);
		connectionListenerList = new ArrayList<SynthDeviceStateListener>();
	}
	
	public Synth getSynth() {
		return synth;
	}
	
	public void addStateListener(SynthDeviceStateListener l) {
		if (!connectionListenerList.contains(l))
			connectionListenerList.add(l);
	}
	
	public void removeStateListener(SynthDeviceStateListener l) {
		connectionListenerList.remove(l);
	}
	
	public void fireConnectionStateChangedEvent() {
		for (SynthDeviceStateListener l : connectionListenerList)
			l.synthConnectionStateChanged(this);
	}
	
	public boolean areMidiDevicesSet() {
		return midiIn!=null && midiOut!=null;
	}

	public boolean setup() {
		Environment env = Environment.sharedInstance();
		
		NomadMidiDialog dlg = new NomadMidiDialog(midiIn, midiOut);
		
		if (midiIn==null) dlg.setInputDevice(env.getProperty("synth.midi.in"));
		if (midiOut==null) dlg.setOutputDevice(env.getProperty("synth.midi.out"));
		
		dlg.invoke();
		
		if (dlg.isOkResult()) {
			midiIn = dlg.getInputDevice();
			midiOut = dlg.getOutputDevice();
	
			env.setProperty("synth.midi.in", midiIn.getName());
			env.setProperty("synth.midi.out", midiOut.getName());
			
			return true;
		} else {
			return false;
		}
	}
	
	public void connect() {
		if (isConnected()) {
			System.err.println("Reconnecting...");
			disconnect();
		}
		
		if (!areMidiDevicesSet()) {
			if (!setup())
				return ;
		
			if (!areMidiDevicesSet()) {

				System.err.println("No Mididevices are set");
				return;
			}
		}
		
		driver = new MidiDriver();
		try {
			driver.connect(midiIn, midiOut);
		} catch (Exception e) {
			e.printStackTrace();
			driver = null;
			return ;
		}
		
		protocol = new NmProtocol(driver);
		protocol.addListener(protocolListener);
		
		try {
			protocol.send(new IAmMessage());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		hbtask = new HeartbeatTask(protocol, this);
		try {
			hbtask.start();
		} catch (HeartbeatTaskException e) {
			e.printStackTrace();
		}
		fireConnectionStateChangedEvent();
	}
	
	public void disconnect() {
		if (isConnected()) {

			try {
				if (hbtask.isRunning())
					hbtask.stop();
			} catch (HeartbeatTaskException e) {
				e.printStackTrace();
			}
			driver.disconnect();
			driver = null;
			protocol = null;

			fireConnectionStateChangedEvent();
		} else {
			System.err.println("Not connected");
		}
	}
	
	public boolean isConnected() {
		return driver!=null;
	}
	
	public void exceptionOccured(HeartbeatTaskExceptionMessage message) {
		message.emergencyStop();
		disconnect();
		message.getCause().printStackTrace();
		ExceptionNotificationDialog dialog = new ExceptionNotificationDialog(message.getCause());
		dialog.invoke();
	}

	public int getActivePid(int slot) {
		return protocol.getActivePid(slot);
	}
	
	public void send(MidiMessage message) throws Exception {
		protocol.send(message);
	}
	
}
