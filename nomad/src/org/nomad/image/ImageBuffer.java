/* Copyright (C) 2006 Christian Schneider
 * 
 * This file is part of Nomad.
 * 
 * Nomad is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Nomad is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Nomad; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

/*
 * Created on Jan 3, 2006
 */
package org.nomad.image;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

/**
 * A class that hosts an image until dispose() is called.
 * 
 * If PersistenceManager and a valid key are provided, a new ImageBuffer
 * will try to obtain a copy of the image identified by the PersistenceManager
 * and the key and register itself to the PersistenceManager. 
 * 
 * @author Christian Schneider
 */
public class ImageBuffer {
	
	/** The image. */
	private Image image = null;
	
	/** If manager, key and image are not null this ImageBuffer is registered to this manager. */
	private PersistenceManager manager = null;
	
	/** Identifier for the image. */
	private Object key = null;
	
	private Rectangle region = null;
	
	/** Creates an unmanaged ImageBuffer containing no image. */
	public ImageBuffer() { }

	/**
	 * Creates an unmanaged ImageBuffer containing the given image.
	 * @param image the image
	 */
	public ImageBuffer(Image image) {
		this.image = image;
	}

	/**
	 * If sharedBuffer is null this creates an unmanaged ImageBuffer containing no image.
	 * Otherwise the new ImageBuffer will be initialized with the same image
	 * and region, sharedBuffer provides (which can be null). 
	 * If sharedBuffer's isShared() property is true, the newly created ImageBuffer
	 * will use the sharedBuffer's key and PersistenceManager properties to register itself.
	 * 
	 * @param sharedBuffer
	 */
	public ImageBuffer(ImageBuffer sharedBuffer) {
		if (sharedBuffer!=null) {
			image = sharedBuffer.image;
			this.region = sharedBuffer.region;
			if (sharedBuffer.isShared()) {
				key = sharedBuffer.key;
				manager = sharedBuffer.manager;
				manager.register(this);
			}
		}
	}

	/**
	 * Uses manager and key to find a sharing ImageBuffer which will be used to
	 * initialize the new ImageBuffer.
	 * @see #ImageBuffer(ImageBuffer)
	 */
	public ImageBuffer(PersistenceManager manager, Object key) {
		if (manager.containsKey(key)) {
			this.image = manager.getUnmanaged(key);
			this.key = key;
			this.manager = manager;
			manager.register(this);
		}
	}

	/**
	 * Creates a ImageBuffer instance which automatically subscribes to manager to share its image.
	 * Use this only to register an ImageBuffer with a key that does not exist yet.
	 * 
	 * @param manager manager to subscribe to
	 * @param key Identifier to subscribe with
	 * @param image the shared image 
	 * @throws IllegalStateException The manager has already an ImageBuffer instance
	 * that is registered with the given key.
	 * @throws NullPointerException if image or manager or key are null
	 */
	public ImageBuffer(PersistenceManager manager, Object key, Image image) {
		if (image==null) throw new NullPointerException("Cannot register a ImageBuffer that has no image to share.");
		if (manager==null) throw new NullPointerException("Invalid PersistenceManager");
		if (key==null) throw new NullPointerException("Invalid key "+key);
		
		if (manager.containsKey(key))
			throw new IllegalStateException("Key collision.");

		this.manager = manager;
		this.key = key;
		this.image = image;
		manager.register(this);
	}
	
	/**
	 * Sets the region property.
	 * @param region A rectangle used by paint() to display only part
	 *  of the image that lays inside the rectangle. 
	 */
	public void setRegion(Rectangle region) {
		this.region=region==null?null:new Rectangle(region);
	}
	
	/**
	 * Returns the region to display within paint().
	 * If no region is set, this returns null.
	 * @return the region.
	 */
	public Rectangle getRegion() {
		return this.region;
	}
	
	/**
	 * Returns true if the region property is set.
	 * @return true if the region property is set.
	 */
	public boolean isRegionSet() {
		return region!=null;
	}

	/** @return the image (which can be null) */
	public Image getImage() {
		return this.image;
	}

	/** @return true if the image is not null */
	public boolean isValid() {
		return image!=null;
	}

	/** 
	 * @return true if the ImageBuffer is sharing it's image. This implies that
	 * none of the properties PersistenceManager, Key, Image is null.
	 */
	public boolean isShared() {
		return key!=null && manager!=null;
	}
	
	/**
	 * Calls unsubscribe() and sets the image properties to null. 
	 * @see #unsubscribe()
	 */
	public void dispose() {
		unsubscribe();
		this.image = null;
	}

	/**
	 * If this ImageBuffer shares it's image, it unregiters itself from the PersistenceManager.
	 * The properties PersistenceManager and Key are set to null.
	 */
	public void unsubscribe() {
		if (isShared()) {
			manager.unregister(this);
		}
		manager = null;
		key = null;
	}

	/**
	 * @return if isShared() returns true, this will return the key with that this
	 * ImageBuffer is registered to the PersistenceManager or otherwise null will be returned.
	 * @see #isShared()
	 */
	public Object getKey() {
		return key;
	}

	/**
	 * @return if isShared() returns true, this will return the PersistenceManager 
	 * this ImageBuffer is registered to. Otherwise null will be returned.
	 * @see #isShared()
	 */
	public PersistenceManager getPersistenceManager() {
		return manager;
	}

	/* /**
	 * Calls unsubscribe() and afterwards super.finalize()
	 * @see #unsubscribe()
	 *
	protected void finalize() throws Throwable {
		unsubscribe();
		super.finalize();
	}*/

	/**
	 * Paints the full image if the region property is not set.
	 * Otherwise only the part of the image within the region is painted.
	 * Both full image or region are painted at x=0 and y=0.
	 * The paint method checks the clip bounds of the graphics object
	 * and paints only the necessary parts.
	 *   
	 * @param g Graphics object used to paint the image.
	 * @throws IllegalStateException isValid() has returned false
	 * @see #isValid()
	 */
	public void paint(Graphics g) {
		if (!isValid()) 
			throw new IllegalStateException("ImageBuffer is not valid.");

		Rectangle clip = g.getClipBounds(); // use clip bounds if necessary
		if (clip==null) {
			if (isRegionSet()) {
				g.drawImage(image, 0, 0, region.width, region.height,
					region.x, region.y,region.x+region.width,region.y+ region.height, null);
			} else {
				g.drawImage(image, 0, 0, null);
			}
		} else { // clip!=null
			if (isRegionSet()) {	
				Rectangle target = new Rectangle(0, 0, region.width, region.height); // move region to 0,0
				Rectangle ri = clip.intersection(target); // intersect region and component
				if (ri.isEmpty()) {
					// no painting necessary
				} else {
					
					int ix = region.x+ri.x;
					int iy = region.y+ri.y;
					
					g.drawImage(image,
						ri.x, ri.y, ri.x+ri.width, ri.y+ri.height,
						ix, iy, ix+ri.width, iy+ri.height, null);
				}
			} else {
				// intersect clip with image bounds
				Rectangle ri = clip.intersection(new Rectangle(0, 0, image.getWidth(null), image.getHeight(null)));
				if (ri.isEmpty()){
					// no painting necessary
				} else {
					// paint intersection of clip and image
					int r = ri.x+ri.width;
					int b = ri.y+ri.height;
					g.drawImage(image,
							ri.x, ri.y, r, b,
							ri.x, ri.y, r, b, null);
				}
			}
		}
	}
	
	protected void finalize() throws Throwable {
		unsubscribe();
		super.finalize();
	}
	
}
