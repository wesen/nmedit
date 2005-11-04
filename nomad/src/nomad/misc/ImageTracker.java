package nomad.misc;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;

/** 
 * Simple container for storing images and associating them with a key
 * @author Christian Schneider
 */
public class ImageTracker {

	public final static int IMAGE_TRACKER_ALLOW_REPLACE = 0;
	public final static int IMAGE_TRACKER_DISALLOW_REPLACE = 1;
	HashMap images = new HashMap();
	private int mode = -1;
	
	public ImageTracker() {
		this(IMAGE_TRACKER_ALLOW_REPLACE);
	}

	public ImageTracker(int mode) {
		this.mode = mode;
	}
	
	/**
	 * Puts an image to the hashmap
	 * @param key key for the image
	 * @param image the image
	 */
	public void putImage(String key, Image image) {
		if (mode==IMAGE_TRACKER_ALLOW_REPLACE||!images.containsKey(key))
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
	 * @return iterator
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
	
	public void loadFromDirectory(String path) throws FileNotFoundException {
		File dir = new File(path);
		if (!dir.exists()) {
			throw new FileNotFoundException("Directory '"+dir+"' not found.");
		}
		
		File[] files = dir.listFiles();
		for (int i=0;i<files.length;i++) {
			File f = files[i];
			if (!f.getName().endsWith("slice") && f.getName().indexOf(".")>=0) {
				String sliceName = f.getName();
				sliceName = sliceName.substring(0, sliceName.lastIndexOf("."))+".slice";
				
				File slice = new File(dir+File.separator+sliceName);
				if (slice.exists()) {
					// load slice
					SliceImage.createSliceImage(dir+File.separator+f.getName()).feedImageTracker(this);
				} else {
					// load single image
					Image image = Toolkit.getDefaultToolkit().getImage(dir+File.separator+f.getName());
					String key = f.getName().substring(0,f.getName().lastIndexOf("."));
					if (key.contains(File.separator))
						key = key.substring(key.lastIndexOf(File.separator));
					if (image!=null)
						putImage(key, image);
					else
						System.err.println("Could not load image "+dir+File.separator+f.getName());
				}
			}
		}
	}
	
}
