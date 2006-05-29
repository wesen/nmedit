package net.sf.nmedit.nomad.theme.component;



/**
 * @author Christian Schneider
 * @hidden
 */
public class VocoderBandChangeEvent {

	private boolean allBandsChanged = false;
	private VocoderBandDisplay source = null;
	private int band = ALL_BANDS;
	public final static int ALL_BANDS = -1;
	
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
