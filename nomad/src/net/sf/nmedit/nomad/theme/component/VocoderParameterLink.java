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
package net.sf.nmedit.nomad.theme.component;

import net.sf.nmedit.nomad.patch.virtual.Module;
import net.sf.nmedit.nomad.patch.virtual.Parameter;
import net.sf.nmedit.nomad.patch.virtual.event.EventListener;
import net.sf.nmedit.nomad.patch.virtual.event.ParameterEvent;
import net.sf.nmedit.nomad.xml.dom.module.DParameter;


public class VocoderParameterLink implements EventListener<ParameterEvent> {

	private VocoderBandDisplay vbd = null;
	private Parameter[] pbands = null;
	private Module linkedModule = null;
	private ParameterBroadcast broadcast = new ParameterBroadcast();
	
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
		return getVocoderBandDisplay().getDefinition(band);
	}
	
	public void link() {
		linkedModule = getVocoderBandDisplay().getModule();
		if (linkedModule!=null) {
			for (int i=VocoderBandDisplay.NUM_BANDS-1;i>=0;i--) {
				pbands[i] = linkedModule.getParameter(getParameterInfo(i).getContextId());
				if (pbands[i]!=null) {
					getVocoderBandDisplay().setBand(i, pbands[i].getValue());
					pbands[i].addListener(this);
					getVocoderBandDisplay().addBandChangeListener(broadcast);
				}
			}
		}
	}

	public void unlink() {
		if (linkedModule!=null) {
			for (int i=VocoderBandDisplay.NUM_BANDS-1;i>=0;i--) {
				pbands[i].removeListener(this);
				pbands[i] = null;
				getVocoderBandDisplay().removeBandChangeListener(broadcast);
			}
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

    public void event( ParameterEvent event )
    {
        if (event.getID()==ParameterEvent.PARAMETER_VALUE_CHANGED)
        {
            int band = event.getParameter().getDefinition().getContextId();
            if (pbands[band]!=null)
                getVocoderBandDisplay().setBand(band, event.getParameter().getValue());
        }
    }
	
}
