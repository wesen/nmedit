package nomad.misc;

import java.awt.Image;
import java.util.HashMap;
import java.util.Iterator;

/** 
 * Simple container for storing images and associating them with a key
 * @author Christian Schneider
 */
public class ImageTracker {
	
	HashMap images = new HashMap();
	
	public ImageTracker() {
		;
	}
	
	/**
	 * Puts an image to the hashmap
	 * @param key key for the image
	 * @param image the image
	 */
	public void putImage(String key, Image image) {
		images.put(key, image);
	}
	
	/**
	 * Returns the image with the given key
	 * @param key the key
	 * @return the image
	 */
	public Image getImage(String key) {
		return (Image) images.get(key);
	}

	/**
	 * Returns an iterator that iterates over strings that are
	 * valid keys for the images
	 * @return
	 */
	public Iterator getKeys() {
		return images.keySet().iterator();
	}

	/**
	 * Adds all images contained in the given itracker parameter.
	 * Note that an image will be replaced if the same key exists
	 * in this object and in the itracker object.
	 * @param itracker the source image tracker
	 */
	public void addFrom(ImageTracker itracker) {
		Iterator keyIterator = itracker.getKeys();
		while (keyIterator.hasNext()) {
			String key = (String) keyIterator.next();
			putImage(key, itracker.getImage(key));
		}
	}
	
}
