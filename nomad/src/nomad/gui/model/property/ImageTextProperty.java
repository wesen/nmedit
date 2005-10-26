package nomad.gui.model.property;

import java.awt.Image;

import nomad.gui.model.component.AbstractUIComponent;
import nomad.misc.ImageTracker;

/**
 * @author Christian Schneider
 * 
 * Uses String as transfer type.
 */
public abstract class ImageTextProperty extends Property {

	private ImageTracker imageTracker = null;

	public ImageTextProperty(String displayName, AbstractUIComponent uicomponent, ImageTracker imageTracker) {
		super(displayName, uicomponent);
		this.imageTracker = imageTracker;
	}

	public void setValue(Object value) {
		super.setValue((String) value);
	}
	
	public void setImageTracker(ImageTracker imageTracker) {
		this.imageTracker = imageTracker;
	}
	
	public ImageTracker getImageTracker() {
		return this.imageTracker;
	}
	
	/**
	 * Returns the image that is associated with the current value in the imageTracker.
	 * If imageTracker is null or the image is not found, null is returned 
	 * @return the image
	 */
	public Image getImage() {
		if (imageTracker==null)
			return null;

		String key = (String) getValue();
		if (key==null)
			return null;
		
		if (! key.matches("\\{@.*}"))
			return null;
		
		if (key.length()<=3)
			return null;

		// now key not null and valid		
		key = key.substring(2, key.length()-1);
		return imageTracker.getImage(key);
	}

}
