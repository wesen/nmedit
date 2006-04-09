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
 * Created on Feb 24, 2006
 */
package org.nomad.util.shape;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;

import org.nomad.util.graphics.ImageToolkit;

public class ShapeContainer<T extends Shape> extends ArrayList<ShapeInfo<T>> {

	private static final boolean debug = false;
	private boolean modified = false;
	private BufferedImage image = null;
	private boolean highQualityRendering = true;
	private int pxX;
	private int pxY;
	
	public ShapeContainer(int pxX, int pxY) {
		this.pxX = pxX;
		this.pxY = pxY;
	}

	public int getPxX() {
		return pxX;
	}

	public int getPxY() {
		return pxY;
	}
	
	public void add(int index, ShapeInfo<T> element) {
    	setModified();
		super.add(index, element);
	}

    public boolean add(ShapeInfo<T> element) {
    	if (super.add(element)) {
    		setModified();
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public boolean addAll(Collection<? extends ShapeInfo<T>> c) {
    	if (super.addAll(c)) {
	    	setModified();
	    	return true;
    	} else {
    		return false;
    	}
    }
    
    public boolean addAll(int index, Collection<? extends ShapeInfo<T>> c) {
    	if (super.addAll(index, c)) {
	    	setModified();
	      	return true;
    	} else {
    		return false;
    	}
    }
    
    public void clear() {
    	if (!isEmpty())
    		setModified();
       	super.clear();
    }
    
    public boolean remove(Object element) {
    	if (super.remove(element)) {
    		setModified();
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public ShapeInfo<T> remove(int index) {
    	setModified();
       	return super.remove(index);
    }
    
    protected void removeRange(int fromIndex, int toIndex) {
    	setModified();
       	super.removeRange(fromIndex, toIndex);
    }
    
    public ShapeInfo<T> set(int index, ShapeInfo<T> element) {
    	setModified();
       	return super.set(index, element);
    }

	public void setModified() {
		modified = true;
	}

	public boolean isModified() {
		return modified;
	}

	public boolean isHighQualityRenderingEnabled() {
		return highQualityRendering;
	}

	public void setHighQualityRendering(boolean enable) {
		highQualityRendering = enable;
	}

	public void forceRendering(ShapeRenderInfo info , ShapeRenderer<T> renderer) {
		if (!isEmpty()) {
			Graphics2D g2;
			
			if (image==null) {
				image = ImageToolkit.createCompatibleBuffer(info.getSegmentSize(), info.getSegmentSize(), Transparency.TRANSLUCENT);
				g2 = image.createGraphics();
			} else {
				g2 = image.createGraphics();
				ImageToolkit.clearRegion(g2, 0, 0, info.getSegmentSize(), info.getSegmentSize());
			}
	
				// render
				
				if (highQualityRendering) {
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				}
		
				g2.translate(-pxX, -pxY);
				for (ShapeInfo<T> sinfo : this) {
					if (sinfo.isVisible())
						renderer.paint(g2, sinfo.getShape());
				}
				// g2.translate(x, y); not necessary
			
			g2.dispose();
		}
		modified = false;
	}
	
	public void paint(Graphics g, ShapeRenderInfo info , ShapeRenderer<T> renderer) {
		if (!isEmpty()) {
			
			if (isModified()) 
				forceRendering(info, renderer);
			
			// draw
			g.drawImage(image, pxX, pxY, null);
			
			if (debug)
				g.drawRect(pxX, pxY, info.getSegmentSize(), info.getSegmentSize());
			
		}
	}
	
}
