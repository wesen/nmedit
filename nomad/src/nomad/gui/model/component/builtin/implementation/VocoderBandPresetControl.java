package nomad.gui.model.component.builtin.implementation;
import javax.swing.Icon;

import nomad.misc.GridButtonComponent;
import nomad.misc.GridButtonModel;

public class VocoderBandPresetControl extends GridButtonComponent {

	private VocoderControl vcontrol = null;
	private boolean horizontal=true;
	
	public VocoderBandPresetControl(VocoderControl vcontrol, boolean horizontal) {
		super ();
		this.vcontrol =vcontrol;
		this.horizontal = horizontal;
		
		setGridButtonModel(new GridButtonModel(){

			public int getButtonRowSize() {
				return VocoderBandPresetControl.this.horizontal?1:7;
			}
			
			public int getButtonColSize() {
				return VocoderBandPresetControl.this.horizontal?7:1;
			}
			
			public String getButtonLabel(int row, int col) {
				switch (VocoderBandPresetControl.this.horizontal?col:row) {
					case 0: return "-2";
					case 1: return "-1";
					case 2: return "0";
					case 3: return "+1";
					case 4: return "+2";
					case 5: return "Inv";
					case 6: return "Rnd";
					default: return "?";
				}
			}
			
			public Icon getButtonIcon(int row, int col) {
				return null;
			}
			
			public boolean getButtonHasAdvancedMouseHandling(int row, int col) {
				return false;
			}
			
			public void buttonClicked(int row, int col) {
				int buttonIndex = VocoderBandPresetControl.this.horizontal?col:row;
				switch (buttonIndex) {
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
						getVocoderControl().getVocoderBandDisplay().setBands(buttonIndex-2);
						break;
					case 5: 
						getVocoderControl().getVocoderBandDisplay().setBandsInv();
						break;
					case 6: 
						getVocoderControl().getVocoderBandDisplay().setBandsRnd();
						break;
				}
			}
			
		});
	}

	public VocoderControl getVocoderControl() {
		return vcontrol;
	}
	
}
