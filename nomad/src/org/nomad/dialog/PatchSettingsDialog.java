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
 * Created on Feb 15, 2006
 */
package org.nomad.dialog;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import org.nomad.patch.Header;
import org.nomad.patch.Patch;

public class PatchSettingsDialog extends NomadDialog {

	private Patch patch;

	public PatchSettingsDialog(Patch patch) {
		setTitle("Patch Settings");
		setInfo("Info","Modifying patch\n"+patch.getName());
		this.patch = patch;
		
		JPanel group;
		
		group = newGroup("Voices");
		addRow(group,"Requested", new JSpinner(new RequestedVoicesModel()));
		add(group);
		
		group = newGroup("Velocity Range");
		addRow(group,"Max", new JSpinner(new VelocityRangeModel(true)));
		addRow(group,"Min", new JSpinner(new VelocityRangeModel(false)));
		add(group);
		
		group = newGroup("Keyboard Range");
		addRow(group,"Max", new JSpinner(new KeyboardRangeModel(true)));
		addRow(group,"Min", new JSpinner(new KeyboardRangeModel(false)));
		add(group);
		
		ButtonGroup pedalMode = new ButtonGroup();
		JRadioButton radio1 ;
		JRadioButton radio2 ;
		group = newGroup("Pedal Mode");
		radio1=new JRadioButton("Sustain");
		radio2=new JRadioButton("On/Off");
		addRow(group,null, new Component[]{radio1, radio2});
		pedalMode.add(radio1);
		pedalMode.add(radio2);
		add(group);

		group = newGroup("Bend Range");
		addRow(group,"Semitones", new JSpinner(new BendRangeModel()));
		add(group);

		
		ButtonGroup portamentoMode = new ButtonGroup();
		group = newGroup("Portamento");
		radio1=new JRadioButton("Normal");
		radio2=new JRadioButton("Auto");
		portamentoMode.add(radio1);
		portamentoMode.add(radio2);
		addRow(group,null, new Component[]{radio1, radio2});
		addRow(group,"Time", new JSpinner(new PortamentoTimeModel()));
		add(group);

		group = newGroup("Octave shift");
		ButtonGroup octaveShift = new ButtonGroup();
		JRadioButton[] rbtns = new JRadioButton[5];
		for (int i=-2;i<=+2;i++) {
			rbtns[i+2]=new JRadioButton(Integer.toString(i));
			octaveShift.add(rbtns[i+2]);
		}
		addRow(group,null, rbtns);
		add(group);
		
		group =newGroup("Voice Retrigger");
		addRow(group, null, new Component[]{
				new JCheckBox("Poly"),
				new JCheckBox("Common")
		});
		add(group);
	}
	
	public Patch getPatch() {
		return patch;
	}
	
	public void invoke() {
		invoke(new String[]{RESULT_OK, ":"+RESULT_CANCEL});
	}

	private class MyNumberModel extends SpinnerNumberModel {
		public MyNumberModel() {
			setMaximumSize(new Dimension(100, 20));
		}
	}
	
	private class RequestedVoicesModel extends MyNumberModel {
		public RequestedVoicesModel() {
			Header h = getPatch().getHeader();
			setMinimum(new Integer(0));
			setMaximum(new Integer(32));
			setValue(new Integer(h.getRequestedVoices()));
		}
	}
	
	private class PortamentoTimeModel extends MyNumberModel {
		public PortamentoTimeModel() {
			Header h = getPatch().getHeader();
			setMinimum(new Integer(0));
			setMaximum(new Integer(127));
			setValue(new Integer(h.getPortamentoTime()));
		}
	}
	
	private class BendRangeModel extends MyNumberModel {
		public BendRangeModel() {
			setMinimum(new Integer(2));
			setMaximum(new Integer(24));
			Header h = getPatch().getHeader();
			setValue(new Integer(h.getBendRange()));
		}
	}

	private class VelocityRangeModel extends MyNumberModel {
		private boolean max;

		public VelocityRangeModel(boolean max) {
			this.max = max;
			setMinimum(new Integer(0));
			setMaximum(new Integer(127));
			Header h = getPatch().getHeader();
			setValue(new Integer(max?h.getVelocityRangeMax():
				h.getVelocityRangeMin()));
		}
	}

	private class KeyboardRangeModel extends MyNumberModel {
		private boolean max;

		public KeyboardRangeModel(boolean max) {
			this.max = max;
			setMinimum(new Integer(0));
			setMaximum(new Integer(127));
			Header h = getPatch().getHeader();
			setValue(new Integer(max?h.getKeyboardRangeMax():
				h.getKeyboardRangeMin()));
		}
	}
	
}
