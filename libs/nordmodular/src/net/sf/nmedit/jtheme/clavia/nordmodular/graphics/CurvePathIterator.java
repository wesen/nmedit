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
import java.awt.geom.PathIterator;



 public class CurvePathIterator implements PathIterator
    {
//    	 affine transformation
        protected AffineTransform at;
        
        // current index
        protected int index = 0;

        protected int maxpoint ;
        protected Point points[];
        protected Rectangle bounds;
        
        public CurvePathIterator(AffineTransform at, Point points[], Rectangle bounds)
        {
            this.at = at;
            maxpoint = points.length;
            this.points = points;
            this.bounds = bounds;
        }

        public int getWindingRule()
        {
            return WIND_NON_ZERO;
        }

        public boolean isDone()
        {
            return index>=maxpoint;
        }

        public void next()
        {
            index ++;            
        }
         
        protected void applytransform( float[] coords, int seg )
        {
            if (at != null) {
                at.transform(coords, 0, coords, 0, seg == SEG_CUBICTO ? 3 : 1);
            }
        }
        
        private void exp(float srcX, float srcY, float destX, float destY, float coords[])
        {
        	coords[0] = (destX-srcX)/2+srcX; // ctrl 1
            coords[1] = srcY;
            coords[2] = destX; // ctrl 2
            coords[3] = (destY-srcY)/2+srcY;
            coords[4] = destX; // destination
            coords[5] = destY;
        }
        
        private void log(float srcX, float srcY, float destX, float destY, float coords[])
        {
        	coords[0] = srcX; // ctrl 1
            coords[1] = (srcY-destY)/2+destY;
            coords[2] = (destX-srcX)/2+srcX; // ctrl 2
            coords[3] = destY;
            coords[4] = destX; // destination
            coords[5] = destY;
        }
       
        
        public int currentSegment( float[] fcoords )
        {
       	if(points[index].getCurve_type() == EqualizerMid.LIN) {
        		fcoords[0] = points[index].getX()*(bounds.width-1);
            	fcoords[1] = points[index].getY()*(bounds.height-1);
        	} else if(points[index].getCurve_type() == EqualizerMid.EXP) {
            	exp(points[index-1].getX(),points[index-1].getY(),
            			points[index].getX(),points[index].getY(),fcoords);
                        	fcoords[0] = fcoords[0]*(bounds.width-1);
            	fcoords[1] = fcoords[1]*(bounds.height-1);
            	fcoords[2] = fcoords[2]*(bounds.width-1);
            	fcoords[3] = fcoords[3]*(bounds.height-1);
            	fcoords[4] = fcoords[4]*(bounds.width-1);
            	fcoords[5] = fcoords[5]*(bounds.height-1);
        	} else if(points[index].getCurve_type() == EqualizerMid.LOG) {
            	log(points[index-1].getX(),points[index-1].getY(),
            			points[index].getX(),points[index].getY(),fcoords);            	
            	fcoords[0] = fcoords[0]*(bounds.width-1);
            	fcoords[1] = fcoords[1]*(bounds.height-1);
            	fcoords[2] = fcoords[2]*(bounds.width-1);
            	fcoords[3] = fcoords[3]*(bounds.height-1);
            	fcoords[4] = fcoords[4]*(bounds.width-1);
            	fcoords[5] = fcoords[5]*(bounds.height-1);
        	}
        	
        	applytransform(fcoords, points[index].getPoint_type());
        	return points[index].getPoint_type();
        }

        public int currentSegment( double[] dcoords )
        {
        	dcoords[0] = (double) points[index].getX();
        	dcoords[1] = (double) points[index].getY();
            //applytransform(dcoords, points[index].getCurve_type());
      	return points[index].getPoint_type();
        }
    }