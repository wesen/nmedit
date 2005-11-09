package nomad.gui.model.component.builtin;

import nomad.gui.model.UIFactory;
import nomad.gui.model.component.AbstractControlPort;
import nomad.gui.model.component.AbstractUIControl;
import nomad.gui.model.component.builtin.implementation.VocoderBandChangeEvent;
import nomad.gui.model.component.builtin.implementation.VocoderBandChangeListener;
import nomad.gui.model.component.builtin.implementation.VocoderBandDisplay;
import nomad.gui.model.component.builtin.implementation.VocoderControl;
import nomad.model.descriptive.DParameter;

/**
 * The user interface containing the vocoder display and the ports controlling each of the 16 bands.
 * 
 * @author Christian Schneider
 */
public class VocoderUI extends AbstractUIControl {

	private VocoderControl vcontrol = null;
	private VocoderBandProperty[] vbands = new VocoderBandProperty[VocoderBandDisplay.NUM_BANDS];
	
	public VocoderUI(UIFactory factory) {
		super(factory);
		vcontrol = new VocoderControl(
				factory.getImageTracker().getImage("btn.arrow.up"),
				factory.getImageTracker().getImage("btn.arrow.down")
		);
		vcontrol.getVocoderBandDisplay().addBandChangeListener(new VocoderBandChangeListener(){

			public void vocoderBandChanged(VocoderBandChangeEvent event) {
				if (event.haveAllBandsChanged()) {
					for (int i=0;i<vbands.length;i++) // update all ports
						vbands[i].firePortValueUpdateEvent(); 
				} else {
					vbands[event.getBandIndex()].firePortValueUpdateEvent();
				}
			}});
		setComponent(vcontrol);
	}
	
	public VocoderControl getVocoderControl() {
		return vcontrol;
	}
	
	protected void registerPorts() {
		for (int i=0;i<VocoderBandDisplay.NUM_BANDS;i++) {
			vbands[i] = new VocoderBandProperty(i);
			registerControlPort(vbands[i]);
		}
	}

	public String getName() {
		return "Vocoder";
	}

	private class VocoderBandProperty extends AbstractControlPort {
		
		private DParameter param = null;
		private int band = 0;
		
		public VocoderBandProperty(int band) {
			super(VocoderUI.this);
			this.band = band;
		}

		public DParameter getParameterInfoAdapter() {
			return param;
		}

		public void setParameterInfoAdapter(DParameter parameterInfo) {
			param = parameterInfo;
		}

		public int getParameterValue() {
			return vcontrol.getVocoderBandDisplay().getBand(band);
		}

		public void setParameterValue(int value) {
			vcontrol.getVocoderBandDisplay().setBand(band, value);
		}
		
	}
}
