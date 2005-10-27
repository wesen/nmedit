package nomad.gui.model.component.builtin.implementation;

import java.awt.BorderLayout;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;

import nomad.misc.GridButtonComponent;
import nomad.misc.GridButtonModel;

public class VocoderControl extends JComponent {

	VocoderBandDisplay display = null;
	GridButtonComponent controls = null;
	GridButtonModel vocoderGridButtonModel = null;
	VocoderBandPresetControl vocoderBandPresetControl = null;
	
	public VocoderControl() {
		display = new VocoderBandDisplay();
		vocoderGridButtonModel = new VocoderGridButtonModel();
		controls = new GridButtonComponent();
		controls.setGridButtonModel(vocoderGridButtonModel);
		vocoderBandPresetControl = new VocoderBandPresetControl(this, true);
		setLayout(new BorderLayout());
		add(BorderLayout.CENTER, display);
		
		JPanel controlPane = new JPanel();
		JPanel centerPane = new JPanel();
		//centerPane.setLayout(new BoxLayout(centerPane, BoxLayout.LINE_AXIS));
		//centerPane.add(Box.createHorizontalGlue());
		centerPane.setLayout(new BorderLayout());
		//centerPane.add(BorderLayout.CENTER,controls);
		
		controlPane.setLayout(new BorderLayout());
		controlPane.add(BorderLayout.CENTER, controls);//centerPane);
		controlPane.add(BorderLayout.SOUTH, vocoderBandPresetControl);
		add(BorderLayout.SOUTH, controlPane);
	}
	
	public VocoderBandDisplay getVocoderBandDisplay() {
		return display;
	}
	
	private class VocoderGridButtonModel implements GridButtonModel {
		public int getButtonRowSize() {
			return 2;
		}
		public int getButtonColSize() {
			return 16;
		}
		public String getButtonLabel(int row, int col) {
			return (row==0?"+":"-");
		}
		public Icon getButtonIcon(int row, int col) {
			return null;
		}
		public void buttonClicked(int row, int col) {
			display.setBand(col, display.getBand(col)+(row==1?-1:+1));
			// update
		}
		public boolean getButtonHasAdvancedMouseHandling(int row, int col) {
			return true;
		}
	}
}
