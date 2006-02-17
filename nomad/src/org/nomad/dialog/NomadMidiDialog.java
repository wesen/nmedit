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
package org.nomad.dialog;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiDevice.Info;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class NomadMidiDialog extends NomadDialog {
	
	private DeviceSelector in ;
	private DeviceSelector out;

	private Info choiceIn ;
	private Info choiceOut ;

	public NomadMidiDialog() {
		this(null, null);
	}
	
	public NomadMidiDialog(Info midiIn, Info midiOut) {
		super();

		choiceIn = midiIn;
		choiceOut = midiOut;
		
		setTitle("Midi Driver");
		setInfo("Info", 
				"Choose a Midi Driver"
		);
		
		setScrollbarEnabled(true);
		
		in = new DeviceSelector();
		out = new DeviceSelector();

		in.setChoice(midiIn);
		out.setChoice(midiOut);
		
		JLabel lbl ;
		
		add( lbl = new JLabel("Input") );
		lbl.setAlignmentX(0);
		
		add(in);
		add( lbl = new JLabel("Output") );
		lbl.setAlignmentX(0);
		
		add(out);
	}

	public void invoke() {
		super.invoke(new String[]{RESULT_OK, ":"+RESULT_CANCEL});
	}

	public MidiDevice.Info getInputDevice() {
		return getResult().equals(RESULT_OK) ? in.getChoice() : choiceIn;
	}
	
	public MidiDevice.Info getOutputDevice() {
		return getResult().equals(RESULT_OK) ? out.getChoice() : choiceOut;
	}

	public static NomadMidiDialog invokeDialog(Info midiIn, Info midiOut) {
		NomadMidiDialog dlg = new NomadMidiDialog(midiIn, midiOut);
		
		dlg.setVisible(true);
		
		return dlg;
	}

	private class DeviceSelector extends JPanel implements ActionListener {
		private JComboBox cbox;

		private JLabel vendor = new JLabel();
		private JLabel desc = new JLabel();
		private JLabel version = new JLabel(); 
		
		public DeviceSelector() {
			setLayout(new GridLayout(4,0));
			
			setBackground(Color.WHITE);
			setBorder(BorderFactory.createEtchedBorder());
			
			version.setAlignmentX(LEFT_ALIGNMENT);
			vendor.setAlignmentX(LEFT_ALIGNMENT);
			desc.setAlignmentX(LEFT_ALIGNMENT);
			cbox = new JComboBox(MidiSystem.getMidiDeviceInfo());
		
			Font font = new Font("SansSerif", Font.PLAIN, 11);
			cbox.setFont(font);
			version.setFont(font);
			vendor.setFont(font);
			desc.setFont(font);

			add(cbox);
			add(version);
			add(vendor);
			add(desc);
			
			setDevice(null);

			cbox.addActionListener(this);
			if (cbox.getItemCount()>0) cbox.setSelectedIndex(0);
		}
		
		public void setChoice(Info midiDev) {
			if (midiDev!=null) {
				cbox.setSelectedItem(midiDev);
			}
		}

		public void actionPerformed(ActionEvent event) {
			setDevice(getChoice());
		}
		
		public MidiDevice.Info getChoice() {
			return (MidiDevice.Info)cbox.getSelectedItem();
		}
		
		public void setDevice(MidiDevice.Info info) {
			if (info==null) {
				vendor.setText("-");
				desc.setText("-");
				version.setText("-");
				return;
			}
			
			vendor.setText(info.getVendor());
			version.setText(info.getVersion());
			desc.setText(info.getDescription());
		}
	}
	
}
