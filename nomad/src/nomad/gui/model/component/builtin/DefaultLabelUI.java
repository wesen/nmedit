package nomad.gui.model.component.builtin;

import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import nomad.gui.model.UIFactory;
import nomad.gui.model.component.AbstractUIComponent;
import nomad.gui.model.property.ImageTextProperty;
import nomad.misc.ImageTracker;

/**
 * A label user interface that supports text containing line breaks or displaying icons.
 * Line breaks are insterted where the character sequency <code>'\n'</code> (without quotes) was found</li>.
 * To display an icon the component uses the factories image tracker property to receive images from.
 * You can select an icon using the syntax <code>'{' '@' image_key '}'</code> where <code>image_key</code>
 * is the key that will be used to look up the icon from the image tracker. For example if you want to show the
 * icon that is associated with the key <code>icon_smiley</code> the text property has to be set to
 * <code>{<span>@</span>icon_smiley}</code>.
 * 
 * This class uses an {@link javax.swing.JLabel} as displaying component and has the isDecoratingComponent()
 * property set to true. The size property is not installed because it depends on the text/icon that will be
 * displayed. The size will be adjusted automatically when the text/icon property is set. 
 * 
 * @author Christian Schneider
 */
public class DefaultLabelUI extends AbstractUIComponent {

	private JLabel theLabel = null;
	private ImageTextProperty litp = null;
	private String textValue = "";
	
	public DefaultLabelUI(UIFactory factory) {
		super(factory);
		theLabel = new JLabel(textValue=getName());
		theLabel.setHorizontalAlignment(SwingConstants.CENTER);
		theLabel.setFont(new Font("Dialog", Font.PLAIN, 9));
		litp = new MyImageTextProperty(factory.getImageTracker());
		updateLabelUI();
		setAsDecoratingDocument(true); // does not change
		setComponent(theLabel);
	}
	
	protected void installSizeProperty(boolean install) {	
		// don't want to install the size property because it is calculated internally
	}
	
	protected void installProperties(boolean install) {
		super.installProperties(install);
		if (install)
			registerProperty(litp);
		else
			unregisterProperty(litp);
	}
	
	public String getName() {
		return "label";
	}
	
	public String getTextValue() {
		return textValue;
	}
	
	public void setTextValue(String text) {
		this.textValue = text;
		litp.fireChangeEvent();
	}
	
	private void updateLabelUI() {
		Image image = litp.getImage();
		if (image!=null) {
			ImageIcon icon = new ImageIcon(image);
			theLabel.setIcon(icon);
			theLabel.setSize(icon.getIconWidth(), icon.getIconHeight());
			theLabel.setPreferredSize(theLabel.getSize());
			theLabel.setText("");
		} else {
			theLabel.setIcon(null);
			
			// check if linebreak is in text
			final String lineBreak = "\\\\n";
			if (textValue.matches(".*"+lineBreak+".*")) {
				theLabel.setText("<html><div align=\"center\">"+textValue.replaceAll(lineBreak, "<br>")+"</div></html>");
			} else {
				theLabel.setText(textValue);
			}
			theLabel.setSize(theLabel.getPreferredSize());
		}
	}
	
	private class MyImageTextProperty extends ImageTextProperty {

		public MyImageTextProperty(ImageTracker imageTracker) {
			super("text", DefaultLabelUI.this, imageTracker);
		}

		protected Object getInternalValue() {
			return textValue;
		}

		protected void setInternalValue(Object value) {
			if (value==textValue || textValue.equals(value))
				return;

			textValue = value==null ? "" : (String) value;
			updateLabelUI();
		}

		public String getId() {
			return "label.text";
		}
		
	}

}
