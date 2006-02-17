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

import net.sf.nmedit.jnmprotocol.AckMessage;
import net.sf.nmedit.jnmprotocol.GetPatchListMessage;
import net.sf.nmedit.jnmprotocol.IAmMessage;
import net.sf.nmedit.jnmprotocol.LightMessage;
import net.sf.nmedit.jnmprotocol.MidiDriver;
import net.sf.nmedit.jnmprotocol.MidiMessage;
import net.sf.nmedit.jnmprotocol.NewPatchInSlotMessage;
import net.sf.nmedit.jnmprotocol.NmProtocol;
import net.sf.nmedit.jnmprotocol.NmProtocolListener;
import net.sf.nmedit.jnmprotocol.ParameterMessage;
import net.sf.nmedit.jnmprotocol.PatchListMessage;
import net.sf.nmedit.jnmprotocol.PatchMessage;
import net.sf.nmedit.jnmprotocol.RequestPatchMessage;
import net.sf.nmedit.jnmprotocol.SlotActivatedMessage;
import net.sf.nmedit.jnmprotocol.SlotsSelectedMessage;
import net.sf.nmedit.jnmprotocol.VoiceCountMessage;

import org.nomad.dialog.ExceptionNotificationDialog;
import org.nomad.dialog.NomadMidiDialog;
import org.nomad.patch.Patch;
import org.nomad.patch.format.PatchConstructionException;
import org.nomad.patch.format.PatchMessageDecoder;

// TODO if heartbeat fails, disconnect() should be called
public class SynthConnection implements HeartbeatErrorHandler {

	private Synth synth ;

	private MidiDevice.Info midiIn = null;
	private MidiDevice.Info midiOut = null;

	private MidiDriver driver = null;
	private NmProtocol protocol = null;
	private HeartbeatTask hbtask = null;
	private Listener listener = new Listener();
	
	private ArrayList<SynthConnectionStateListener>
		connectionListenerList ;
	
	public SynthConnection() {
		
		PatchMessageDecoder.init();
		
		synth = new Synth(this);
	    try {
			MidiMessage.usePdlFile("/usr/local/lib/nmprotocol/midi.pdl", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		connectionListenerList = new ArrayList<SynthConnectionStateListener>();
	}
	
	public Synth getSynth() {
		return synth;
	}
	
	public void addConnectionStateListener(SynthConnectionStateListener l) {
		if (!connectionListenerList.contains(l))
			connectionListenerList.add(l);
	}
	
	public void removeConnectionStateListener(SynthConnectionStateListener l) {
		connectionListenerList.remove(l);
	}
	
	public void fireConnectionStateChangedEvent() {
		for (SynthConnectionStateListener l : connectionListenerList)
			l.synthConnectionStateChanged(this);
	}
	
	public boolean areMidiDevicesSet() {
		return midiIn!=null && midiOut!=null;
	}

	public boolean setup() {
		NomadMidiDialog dlg = new NomadMidiDialog(midiIn, midiOut);
		dlg.invoke();
		midiIn = dlg.getInputDevice();
		midiOut = dlg.getOutputDevice();
		
		return dlg.isOkResult();
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
		protocol.addListener(listener);
		
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
	
	private class Listener extends NmProtocolListener {

		public void messageReceived(IAmMessage message)
		{
		    System.out.println("IAmMessage: " +
				       "sender:" + message.get("sender") + " " +
				       "versionHigh:" + message.get("versionHigh") + " " +
				       "versionLow:" + message.get("versionLow"));
		    if (message.get("sender") == IAmMessage.MODULAR) {
			System.out.println("IAmMessage: " +
					   "unknown1:" + message.get("unknown1") + " " +
					   "unknown2:" + message.get("unknown2") + " " +
					   "unknown3:" + message.get("unknown3") + " " +
					   "unknown4:" + message.get("unknown4"));
		    }
		    
		    RequestPatchMessage requestPatchMessage;
			try {
				requestPatchMessage = new RequestPatchMessage();
				requestPatchMessage.set("slot", 0);
				//for (int slot = 0; slot < 4; slot++) 
				//	requestPatchMessage.set("slot", slot);
				
			    	protocol.send(requestPatchMessage);
				//}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void messageReceived(LightMessage message)
		{/*
		    System.out.println("LightMessage: " +
				       "slot:" + message.get("slot") + " " +	
				       "pid:" + message.get("pid") + " " +
				       message.get("light0") +
				       message.get("light1") +
				       message.get("light2") +
				       message.get("light3") +
				       message.get("light4") +
				       message.get("light5") +
				       message.get("light6") +
				       message.get("light7") +
				       message.get("light8") +
				       message.get("light9") +
				       message.get("light10") +
				       message.get("light11") +
				       message.get("light12") +
				       message.get("light13") +
				       message.get("light14") +
				       message.get("light15") +
				       message.get("light16") +
				       message.get("light17") +
				       message.get("light18") +
				       message.get("light19"));*/
		}

		public void messageReceived(PatchMessage message)
		{
			System.out.print("Decoding patch...");
			
			try {
				Patch p = PatchMessageDecoder.decode(message);
				System.out.println("[Done]");
				synth.setSlot(0, p);
			} catch (PatchConstructionException e) {
				System.out.println("[Failed]");
				e.printStackTrace();
			}
		}

		public void messageReceived(AckMessage message)
		{
		    System.out.println("AckMessage: " +
				       "slot:" + message.get("slot") + " " +
				       "pid1:" + message.get("pid1") + " " +
				       "type:" + message.get("type") + " " +
				       "pid2:" + message.get("pid2"));

		}

		public void messageReceived(PatchListMessage message)
		{
			System.out.println(message);
		}
		
		public void messageReceived(NewPatchInSlotMessage message)
		{
		    System.out.println("NewPatchInSlotMessage: " +
				       "slot:" + message.get("slot") + " " +	
				       "pid:" + message.get("pid"));
		}
		
		public void messageReceived(VoiceCountMessage message)
		{
		    System.out.println("VoiceCountMessage: " +
				       "voiceCount0:" + message.get("voiceCount0") + " " +
				       "voiceCount1:" + message.get("voiceCount1") + " " +
				       "voiceCount2:" + message.get("voiceCount2") + " " +
				       "voiceCount3:" + message.get("voiceCount3"));	    
		}
		
		public void messageReceived(SlotsSelectedMessage message)
		{
		    System.out.println("SlotsSelectedMessage: " +
				       "slot0Selected:" + message.get("slot0Selected") + " " +
				       "slot1Selected:" + message.get("slot1Selected") + " " +
				       "slot2Selected:" + message.get("slot2Selected") + " " +
				       "slot3Selected:" + message.get("slot3Selected"));
		}
		
		public void messageReceived(SlotActivatedMessage message)
		{
		    System.out.println("SlotActivatedMessage: " +
				       "activeSlot:" + message.get("activeSlot"));
		}
		
		public void messageReceived(ParameterMessage message)
		{
		    System.out.println("ParameterMessage: " +
				       "slot:" + message.get("slot") + " " +
				       "pid:" + message.get("pid") + " " +
				       "section:" + message.get("section") + " " +
				       "module:" + message.get("module") + " " +
				       "parameter:" + message.get("parameter") + " " +
				       "value:" + message.get("value"));
		}
	}

	public void exceptionOccured(HeartbeatTaskExceptionMessage message) {
		message.emergencyStop();
		disconnect();
		ExceptionNotificationDialog dialog = new ExceptionNotificationDialog(message.getCause());
		dialog.invoke();
	}
	
}
