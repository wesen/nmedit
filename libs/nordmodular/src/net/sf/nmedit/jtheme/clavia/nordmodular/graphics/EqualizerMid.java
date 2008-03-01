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

import java.awt.geom.PathIterator;


public class EqualizerMid extends Curve {

	public EqualizerMid(){
		super(8);
				
		for(int i = 0; i < points.length ; i++)
		{
			points[i] = new Point();
			points[i].setPoint_type(PathIterator.SEG_LINETO);
			points[i].setCurve_type(LIN);
		}
		points[0].setPoint_type(PathIterator.SEG_MOVETO);
		points[0].setLocation(-0.1f,1.1f);
		//points[1].setPoint_type(PathIterator.SEG_MOVETO);
		points[1].setLocation(-0.1f,0.5f);
		points[2].setLocation(0.4f,0.5f);
		points[3].setLocation(0.5f,0);
		points[3].setPoint_type(PathIterator.SEG_CUBICTO);
		points[3].setCurve_type(EXP);
		points[4].setLocation(0.6f,0.5f);
		points[4].setPoint_type(PathIterator.SEG_CUBICTO);
		points[4].setCurve_type(LOG);
		points[5].setLocation(1.1f,0.5f);
		//points[6].setPoint_type(PathIterator.SEG_MOVETO);
		points[6].setLocation(1.1f,1.1f);
		//points[7].setPoint_type(PathIterator.SEG_MOVETO);
		points[7].setLocation(-0.1f,1.1f);
	}
	
	public void setGain (float gain){
        setModified(true);
		points[3].setY(gain);
	}
	
	public float getGain (){
		return points[3].getY();
	}
	
	public void setBW( float bw){
        setModified(true);
		float  f = getFreq();
		points[2].setX(f - bw/2-0.05f);
		points[4].setX(f + bw/2+0.05f);
		points[3].setX(f);
	}
	
	public float getBW (){
		return points[4].getX() - points[2].getX();
	}
	
	public void setFreq( float f){
        setModified(true);
		float bw = getBW();
		points[2].setX(f - bw/2);
		points[4].setX(f + bw/2);
		points[3].setX(f);
	}
	
	public float getFreq(){
		return points[3].getX();
	}		
}
