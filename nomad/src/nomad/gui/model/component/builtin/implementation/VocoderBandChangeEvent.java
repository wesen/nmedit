package nomad.gui.model.component.builtin.implementation;


public class VocoderBandChangeEvent {

	private boolean allBandsChanged = false;
	private VocoderBandDisplay source = null;
	private int band = -1;
	
	public VocoderBandChangeEvent(VocoderBandDisplay source) {
		this.source = source;
		allBandsChanged = true;
	}
	
	public VocoderBandChangeEvent(VocoderBandDisplay source, int band) {
		this.source = source;
		this.band = band;
	}
	
	public VocoderBandDisplay getSource() {
		return source;
	}
	
	public int getBandIndex() {
		return band;
	}
	
	public boolean haveAllBandsChanged() {
		return allBandsChanged;
	}
	
}
