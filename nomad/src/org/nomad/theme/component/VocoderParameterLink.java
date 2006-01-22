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
 * Created on Jan 19, 2006
 */
package org.nomad.theme.component;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.nomad.patch.Module;
import org.nomad.patch.Parameter;
import org.nomad.xml.dom.module.DParameter;

public class VocoderParameterLink {

	private VocoderBandDisplay vbd = null;
	private Parameter[] pbands = null;
	
	public VocoderParameterLink(VocoderBandDisplay vbd) {
		this.vbd = vbd;
		pbands = new Parameter[VocoderBandDisplay.NUM_BANDS];
		for (int i=pbands.length-1;i>=0;i--)
			pbands[i] = null;
	}
	
	public VocoderBandDisplay getVocoderBandDisplay() {
		return vbd;
	}

	public DParameter getParameterInfo(int band) {
		return getVocoderBandDisplay().getInfo(band);
	}
	
	public void link() {
		Module module = getVocoderBandDisplay().getModule();
		if (module!=null) {
			for (int i=VocoderBandDisplay.NUM_BANDS-1;i>=0;i--) {
				pbands[i] = module.findParameter(getParameterInfo(i));
				if (pbands[i]!=null) {
					getVocoderBandDisplay().setBand(i, pbands[i].getValue());
					pbands[i].addChangeListener(new ParameterChangeListener(i));
					getVocoderBandDisplay().addBandChangeListener(new ParameterBroadcast());
				}
			}
		}
	}

	public void unlink() {
		//TODO revert link()
	}

	private class ParameterChangeListener implements ChangeListener {
		int band ;
		public ParameterChangeListener(int band) {
			this.band = band;
		}
		public void stateChanged(ChangeEvent event) {
			if (pbands[band]!=null)
				getVocoderBandDisplay().setBand(band, pbands[band].getValue());
		}
	}
	
	private class ParameterBroadcast implements VocoderBandChangeListener {
		public void vocoderBandChanged(VocoderBandChangeEvent event) {
			int band = event.getBandIndex();
			if (band==VocoderBandChangeEvent.ALL_BANDS) {
				for (int i=VocoderBandDisplay.NUM_BANDS-1;i>=0;i--) {
					Parameter receiver = pbands[i];
					if (receiver!=null)
						receiver.setValue(getVocoderBandDisplay().getBand(i));
				}
			} else {
				Parameter receiver = pbands[band];
				if (receiver!=null)
					receiver.setValue(getVocoderBandDisplay().getBand(band));
			}
		}
	}
	
}
