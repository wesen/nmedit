package nomad.gui;

import javax.swing.JLabel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nomad.gui.property.InvalidValueException;
import nomad.gui.property.Property;
import nomad.misc.FontInfo;

public class LabelUI extends BasicUI {

	private LabelTextProperty labelTextProperty = new LabelTextProperty();
	private TheLabel theLabel = new TheLabel();

	public LabelUI() {
		// public text property
		getProperties().putProperty("text", labelTextProperty);
		// set component to theLabel
		theLabel.setText(labelTextProperty.getText());
		labelTextProperty.addChangeListener(theLabel);
		setComponent(theLabel);
	}

	private class LabelTextProperty extends Property {
		private final static String defaultValue = "label";	
		public Object getDefaultValue() {
			return defaultValue;
		}
		public void setText(String text) {
			try {
				super.setValue(text);
			} catch (InvalidValueException e) {
				// should never occure
				e.printStackTrace();
			}
		}
		public String getText() {
			return (String) getValue();
		}
	}
	
	private class TheLabel extends JLabel implements ChangeListener {
		public TheLabel() {
			super(labelTextProperty.getText());
		}
		
		public void setText(String text) {
			super.setText(text);
			this.setSize(FontInfo.getTextRect(text, this.getFont(), this));
		}

		public void stateChanged(ChangeEvent event) {
			if (event.getSource()==labelTextProperty)
				this.setText(labelTextProperty.getText());
		}
	}

	public String getName() {
		return "label";
	}
	
}
