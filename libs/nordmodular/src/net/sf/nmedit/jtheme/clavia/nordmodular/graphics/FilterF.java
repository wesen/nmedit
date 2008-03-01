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

import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;

public class FilterF extends Curve {
	float slope =0f; // 
	float cutOff =0.5f;
	int cutOffInt = 64;
	
	float resAmplitude = 0.45f;
	float resonance= resAmplitude; //compirsed between 0 and res
	public FilterF(){
		super(6);
		
		points[0].setPoint_type(PathIterator.SEG_MOVETO);
		points[0].setLocation(-0.1f,1.1f);
		points[1].setLocation(-0.1f,resAmplitude);
		points[2].setLocation(0.3f,resAmplitude);
		
		
		points[3].setPoint_type(PathIterator.SEG_CUBICTO);
		points[3].setCurve_type(EXP);
		
		points[4].setPoint_type(PathIterator.SEG_CUBICTO);		
		points[4].setCurve_type(LOG);
		
		points[5].setLocation(-0.1f,1.1f);
		
		update();
	}
		
	public PathIterator getPathIterator( final AffineTransform at )
    {
        return new FilterIterator(at,points,bounds);
    }
	
	
	
	private void update()
	{		
        setModified(true);
		points[1].setLocation(-0.1f,resAmplitude);
		points[2].setLocation(-0.2f+cutOff,resAmplitude);
		
		points[3].setLocation(cutOff,this.resonance);
		float angle = 110 + 70* this.resonance/resAmplitude;
		points[3].setBezier(points[2].getX(),points[2].getY(),0.1f,0.1f,0f,angle);
		
		points[4].setLocation(0.3f+cutOff+slope, 1.1f);
		//angle going from 70 to 0 with resonance going from 0 to resAmplitude
		angle = 70-70*this.resonance/resAmplitude;		 			
		points[4].setBezier(points[3].getX(),points[3].getY(),0.1f,0.1f,angle,-110);
				
							
	}
	
	public float getResonance() {		
		return resonance/resAmplitude;
	}
	
	public void setResonance(float resonance) {
		this.resonance = (1-resonance)*resAmplitude;
		update();				
	}
	
	public int getCutOff()
	{
		return cutOffInt;
	}
	
	public void setCutOff(int cutOff)
	{
		//cutoff comprised between .1 and .9 
		this.cutOffInt = cutOff;
		this.cutOff = cutOff/127f*.8f+.1f;
		update();
	}

	public int getSlope() {
		return (int)(slope/0.1f)-2;
	}

	//slope must be  0 for 12 db, 1 for 18db and 2 for 24db
	public void setSlope(int slope) {
		this.slope = (2-slope)*0.1f;
		update();
		
	}
		
}
