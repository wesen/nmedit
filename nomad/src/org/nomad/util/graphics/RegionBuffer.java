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
 * Created on Jan 20, 2006
 */
package org.nomad.util.graphics;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

class RegionBuffer {
	
	private BufferedImage image = null;
	private Rectangle dirtyRegion = new Rectangle();
	private int row = -1;
	private int col = -1;
	private int wh = -1;
	private int x1 = 0;
	private int y1 = 0;
	private int x2 = 0;
	private int y2 = 0;
	private Rectangle tmpRect = new Rectangle();
	private Rectangle fullRegion = null;
	
	public RegionBuffer(int row, int col, int regionSize) {
		image = ImageToolkit.createCompatibleBuffer(regionSize, regionSize, Transparency.OPAQUE);
		this.row = row;
		this.col = col;
		this.wh = regionSize;
		x1=regionSize*row;
		y1=regionSize*col;
		x2=regionSize*(row+1);
		y2=regionSize*(col+1);
		fullRegion.setBounds(x1,y1,x2,y2);
		setDirty();
	}
	
	public void paint(Graphics2D g2) {
		ImageToolkit.paintRegion(g2, image, null, x1, y1);
		setClean();
	}
	
	public boolean isDirty() {
		return dirtyRegion.isEmpty();
	}
	
	public void setClean() {
		dirtyRegion.setBounds(x1,y1,0,0);
	}
	
	public void setDirty() {
		dirtyRegion.setBounds(x1,y1,wh,wh);
	}

	public void setDirty(int x, int y, int w, int h) {
		tmpRect.setBounds(x,y,w,h);
		setDirty(tmpRect);
	}

	public void setDirty(Rectangle region) {
		if (!isDirty()) {
			dirtyRegion=region;
		} else {
			dirtyRegion=dirtyRegion.union(region);
		}
	}

}