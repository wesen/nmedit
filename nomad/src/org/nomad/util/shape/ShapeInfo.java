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

import java.awt.Rectangle;
import java.awt.Shape;

import org.nomad.util.misc.NomadUtilities;

public class ShapeInfo<T extends Shape> {

	private final static int initial_coordcount = 20*2; 
	
	private T shape ;
	private Rectangle pixBounds ;
	private Rectangle segBounds ;
	private int[] coords = new int[initial_coordcount];
	private int segCount = 0;
	
	public ShapeInfo(T shape) {
		this.shape = shape;
		segBounds = new Rectangle();
	}
	
	public T getShape() {
		return shape;
	}
	
	public void update(ShapeRenderInfo info) {
		pixBounds = shape.getBounds();
		NomadUtilities.enlargeToGrid(pixBounds, info.getSegmentSize());
		segBounds.setBounds(pixBounds);
		info.scaleToSegment(segBounds);
		
		segCount = 0;
		int maxCoords = (segBounds.width+2)*(segBounds.height+2)*2;
		if (coords.length<maxCoords)
			coords = new int[maxCoords];

		int r = segBounds.x+(segBounds.width )-1; // right
		int b = segBounds.y+(segBounds.height)-1; // bottom
		
		int t = info.getSegmentSize()>>2; // - 1 / 4
		int ts= info.getSegmentSize()<<1; // + 2
		
		int index = 0;
		for (int i=segBounds.x;i<=r;i++) {
			for (int j=segBounds.y;j<=b;j++) {
				if (shape.intersects( info.scaleToPixel(i)-t, info.scaleToPixel(j)-t, ts, ts )) {
					coords[index  ] = i;
					coords[index+1] = j;
					segCount++;
					index  +=2;
				}
			}
		}
	}
	
	public Rectangle getPixBounds() {
		return pixBounds;
	}
	
	public Rectangle getSegBounds() {
		return segBounds;
	}
	
	/*public boolean equals(Object obj) {
		return shape.equals(obj);
	}*/
	
	public String toString() {
		return shape.toString();
	}

	public int[] getSegmentMask() {
		return coords;
	}
	
	public int getMaskSize() {
		return segCount;
	}
	
}
