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

public class ShapeRenderInfo {

	private int segSize ;
	private int power2  ;
	
	public ShapeRenderInfo() {
		this(128);
	}
	
	public ShapeRenderInfo(int segmentSize) {
		setSegmentSize(segmentSize);
	}
	
	private double log2(double d) {
		return Math.log(d)/Math.log(2.0d);
	}
	
	protected void setSegmentSize(int segmentSize) {
		double p2 = log2(segmentSize);
		int floor = (int) Math.floor(p2);
		if (p2-floor > 10e-20)
			throw new IllegalArgumentException("Segment size must be power of 2.");
		
		power2 = floor; // is equal to p2	
		segSize = scaleToPixel(1);
	}

	public int getSegmentSize() {
		return segSize;
	}
	
	public int getPower2() {
		return power2;
	}
	
	public int scaleToPixel(int s) {
		return s<<power2;
	}
	
	public int scaleToSegment(int px) {
		return px>>power2;
	}
	
	public void scaleToPixel(Rectangle r) {
		r.x 	= r.x 		<<power2;
		r.y 	= r.y 		<<power2;
		r.width = r.width 	<<power2;
		r.height= r.height	<<power2;
	}
	
	public void scaleToSegment(Rectangle r) {
		r.x 	= r.x 		>>power2;
		r.y 	= r.y 		>>power2;
		r.width = r.width 	>>power2;
		r.height= r.height	>>power2;
	}
	
}
