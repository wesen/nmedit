/* Copyright (C) 2007 Julien Pauty
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

package net.sf.nmedit.jtheme.clavia.nordmodular.graphics;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

public class FilterIterator extends CurvePathIterator {
	public FilterIterator(AffineTransform at, Point points[], Rectangle bounds) {
		super(at, points, bounds);
	}
	
	public int currentSegment( float[] fcoords )
    {
   	    if(points[index].getCurve_type() == EqualizerMid.LIN) {
    		fcoords[0] = points[index].getX()*(bounds.width-1)+bounds.x;
        	fcoords[1] = points[index].getY()*(bounds.height-1)+bounds.y;
    	} else if(points[index].getCurve_type() == EqualizerMid.EXP || 
    			  points[index].getCurve_type() == EqualizerMid.LOG) {        	
        	fcoords[0] = points[index].getX1()*(bounds.width-1)+bounds.x;
        	fcoords[1] = points[index].getY1()*(bounds.height-1)+bounds.y;
        	fcoords[2] = points[index].getX2()*(bounds.width-1)+bounds.x;
        	fcoords[3] = points[index].getY2()*(bounds.height-1)+bounds.y;
        	fcoords[4] = points[index].getX()*(bounds.width-1)+bounds.x;
        	fcoords[5] = points[index].getY()*(bounds.height-1)+bounds.y;  
        	
    	}   
   	   
    	applytransform(fcoords, points[index].getPoint_type());
    	return points[index].getPoint_type();
    }
}
