package net.sf.nmedit.nomad.util.graphics;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


/** 
 * A container for storing images and associating them with a key.
 * 
 * @author Christian Schneider
 */
public class ImageTracker {

	/**
	 * If a new image should be added and has the same key as an existing
	 * image it will be replace the old image. 
	 */
	public final static int IMAGE_TRACKER_ALLOW_REPLACE = 0;
	
	/**
	 * If a new image should be added and has the same key as an existing
	 * image it will be ignored. 
	 */
	public final static int IMAGE_TRACKER_DISALLOW_REPLACE = 1;
	
	// Pairs (String, Image)
	protected HashMap<String, Image> images = new HashMap<String, Image>(300);
	
	// one of IMAGE_TRACKER_ALLOW_REPLACE or IMAGE_TRACKER_DISALLOW_REPLACE
	private int policy = IMAGE_TRACKER_ALLOW_REPLACE;
	
	/**
	 * Creates a ImageTracker instance using IMAGE_TRACKER_ALLOW_REPLACE as
	 * policy.
	 * 
	 * @see #IMAGE_TRACKER_ALLOW_REPLACE
	 */
	public ImageTracker() {
		this(IMAGE_TRACKER_ALLOW_REPLACE);
	}

	/**
	 * Creates a ImageTracker instance using custom policy. 
	 * 
	 * @param policy one of IMAGE_TRACKER_ALLOW_REPLACE or IMAGE_TRACKER_DISALLOW_REPLACE
	 *  If police is none one of these the default policy, IMAGE_TRACKER_ALLOW_REPLACE
	 *  is used.  
	 * @see #IMAGE_TRACKER_ALLOW_REPLACE
	 * @see #IMAGE_TRACKER_DISALLOW_REPLACE
	 */
	public ImageTracker(int policy) {
		switch (policy) {
			case IMAGE_TRACKER_ALLOW_REPLACE:
			case IMAGE_TRACKER_DISALLOW_REPLACE:
				this.policy = policy;
				break;
			default:
				this.policy = IMAGE_TRACKER_ALLOW_REPLACE;
				break;
		}
	}
	
	/**
	 * Puts an image to the tracker according to the 
	 * policy.
	 * 
	 * @param key the key for the image
	 * @param image the image
	 * 
	 * @see #IMAGE_TRACKER_ALLOW_REPLACE
	 * @see #IMAGE_TRACKER_DISALLOW_REPLACE
	 */
	public void putImage(String key, Image image) {
		if (policy==IMAGE_TRACKER_ALLOW_REPLACE||!images.containsKey(key))
			images.put(key, image);
	}

	/**
	 * Returns the image with the given key or null if
	 * no image is associated with the key.
	 * 
	 * @param key the key
	 * @return the image
	 */
	public Image getImage(String key) {
		return images.get(key);
	}

	/**
	 * Returns an iterator that iterates over the 
	 * String objects that represent all valid keys,
	 * 
	 * @return iterator key-iterator
	 */
	public Iterator<String> getKeyIterator() {
		return images.keySet().iterator();
	}

	public Set<String> getKeys() {
		return images.keySet();
	}
	
	/**
	 * Adds all images contained in the given itracker parameter.
	 * The images are added according to the policy.
	 * @param itracker the source image tracker
	 */
	public void addFrom(ImageTracker itracker) {
		for (String key : itracker.images.keySet()) {
			putImage(key, itracker.getImage(key));
		}
	}
	
	/**
	 * Loads all files that are either an image or a slice from the given directory.
	 * If a file is an image, the name of the file without it's extension is used
	 * as key. If the image is a slice then the keys are loaded from the slice property
	 * file.
	 * 
	 * Images and slices are added according to the policy.
	 * 
	 * @param path the path where images are located
	 * @throws FileNotFoundException the path does not exists
	 * @see SliceImage
	 */
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
					if ((key.indexOf(File.separator)>=0))
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
