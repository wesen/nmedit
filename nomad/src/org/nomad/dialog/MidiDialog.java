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

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiDevice.Info;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.nomad.util.misc.NomadUtilities;

import sun.awt.VerticalBagLayout;

public class MidiDialog extends JDialog {
	
	private DeviceSelector in ;
	private DeviceSelector out;
	
	public MidiDialog(Info midiIn, Info midiOut) {
		super();
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(300, 280);
		NomadUtilities.center(this);


		in = new DeviceSelector();
		out = new DeviceSelector();

		in.setChoice(midiIn);
		out.setChoice(midiOut);
		
		Container c = getContentPane();
		c.setLayout(new VerticalBagLayout());
		
		c.add( new JLabel("Input") );
		c.add(in);
		c.add( new JLabel("Output") );
		c.add(out);
		
		JButton ok = new JButton("Ok");
		
		c.add(ok);
		
		validate();
		
		ok.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent event) {
				setVisible(false);
			}});
		
		setModal(true);
		//setVisible(true);
	}

	public MidiDevice.Info getInputDevice() {
		return in.getChoice();
	}
	
	public MidiDevice.Info getOutputDevice() {
		return out.getChoice();
	}

	public static MidiDialog invokeDialog(Info midiIn, Info midiOut) {
		MidiDialog dlg = new MidiDialog(midiIn, midiOut);
		
		dlg.setVisible(true);
		
		return dlg;
	}

	private class DeviceSelector extends JPanel implements ActionListener {
		private JComboBox cbox;
		private MidiDeviceInfoView infoView = new MidiDeviceInfoView();
		
		public DeviceSelector() {
			
			setBorder(BorderFactory.createEtchedBorder());
			
			setLayout(new BorderLayout());
			cbox = new JComboBox(MidiSystem.getMidiDeviceInfo());
			cbox.addActionListener(this);
			if (cbox.getItemCount()>0) cbox.setSelectedIndex(0);

			add(cbox, BorderLayout.NORTH);
			add(infoView, BorderLayout.CENTER);
		}
		
		public void setChoice(Info midiDev) {
			if (midiDev!=null)
				cbox.setSelectedItem(midiDev);
		}

		public void actionPerformed(ActionEvent event) {
			infoView.setDevice(getChoice());
		}
		
		public MidiDevice.Info getChoice() {
			return (MidiDevice.Info)cbox.getSelectedItem();
		}
	}
	
	private class MidiDeviceInfoView extends JPanel {

		private JLabel vendor ;
		private JLabel desc ;
		private JLabel name ;
		private JLabel version ; 
		
		public MidiDeviceInfoView() {
			setLayout(new GridLayout(0, 1));

			add(name = new JLabel());
			add(version = new JLabel());
			add(vendor = new JLabel());
			add(desc = new JLabel());
			
			setDevice(null);
		}
		
		public void setDevice(MidiDevice.Info info) {
			if (info==null) {
				vendor.setText("-");
				desc.setText("-");
				name.setText("-");
				version.setText("-");
				return;
			}
			
			vendor.setText(info.getVendor());
			version.setText(info.getVersion());
			name.setText(info.getName());
			desc.setText(info.getDescription());
		}
	}

}
