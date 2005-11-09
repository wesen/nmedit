package nomad.gui.model.component.builtin.implementation;

import java.awt.BorderLayout;
import java.awt.Image;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

import nomad.misc.GridButtonComponent;
import nomad.misc.GridButtonModel;

/**
 * @author Christian Schneider
 * @hidden
 */
public class VocoderControl extends JComponent {

	private VocoderBandDisplay display = null;
	private GridButtonComponent controls = null;
	private GridButtonModel vocoderGridButtonModel = null;
	private VocoderBandPresetControl vocoderBandPresetControl = null;
	private Image upArrow = null;
	private Image downArrow = null;

	public VocoderControl() {
		this(null, null);
	}
	
	public VocoderControl(Image upArrow, Image downArrow) {
		this.upArrow = upArrow;
		this.downArrow = downArrow;
		
		display = new VocoderBandDisplay();
		vocoderGridButtonModel = new VocoderGridButtonModel();
		controls = new GridButtonComponent();
		controls.setGridButtonModel(vocoderGridButtonModel);
		vocoderBandPresetControl = new VocoderBandPresetControl(this, true);
		setLayout(new BorderLayout());
		add(display, BorderLayout.NORTH);
		display.setSize(100, 100);

		setOpaque(false);
		add(controls, BorderLayout.CENTER);
		add(vocoderBandPresetControl, BorderLayout.SOUTH);
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
			return getButtonImage(row, col)==null?(row==0?"+":"-"):null;
		}
		public Icon getButtonIcon(int row, int col) {
			Image image = getButtonImage(row, col);
			return image==null?null: new ImageIcon(image);
		}
		public Image getButtonImage(int row, int col) {
			return row==0?upArrow:downArrow;
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
