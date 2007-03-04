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

public class FilterE extends Curve {
	int type =1; // 
	float cutOff =0.5f;
	
	float resAmplitude = 0.4f;
	float resonance= resAmplitude; //compirsed between 0 and res
	public FilterE(){
		super(7);
		
		points[0].setPoint_type(PathIterator.SEG_MOVETO);
		points[0].setLocation(-0.1f,1.1f);
		points[1].setLocation(-0.1f,resAmplitude);
		points[2].setLocation(0.3f,resAmplitude);
		
		
		points[3].setPoint_type(PathIterator.SEG_CUBICTO);
		points[3].setCurve_type(EXP);
		
		points[4].setPoint_type(PathIterator.SEG_CUBICTO);		
		points[4].setCurve_type(LOG);
		
		points[5].setLocation(-0.1f,1.1f);
		points[6].setLocation(-0.1f,1.1f); // this point is used by the band reject only
		update();
	}
		
	public PathIterator getPathIterator( final AffineTransform at )
    {
        return new FilterIterator(at,points,bounds);
    }
	
	
	
	private void update()
	{
		switch(type){
		//high pass
			case 0:
				points[1].setLocation(cutOff-0.2f, 1.1f);
				//angle going from 70 to 0 with resonance going from 0 to resAmplitude
				
				points[2].setLocation(cutOff,this.resonance);
				points[2].setPoint_type(PathIterator.SEG_CUBICTO);
				points[2].setCurve_type(EXP);
				//angle going from 180 to 100 with resonance going from 1 to resAmplitude/2
				float angle = 180f+ (this.resonance - resAmplitude)/resAmplitude*160f;	
				points[2].setBezier(points[1].getX(),points[1].getY(),0.1f,0.05f,-70,angle);
								
				points[3].setLocation(cutOff+0.2f,resAmplitude*2-this.resonance);
				points[3].setPoint_type(PathIterator.SEG_CUBICTO);
				points[3].setCurve_type(EXP);
//				angle going from 0 to 70? with resonance going from 1 to resAmplitude/2
				angle =  (resAmplitude - this.resonance)/resAmplitude*140f;			 							
				points[3].setBezier(points[2].getX(),points[2].getY(),0.1f,0.05f,angle,180f);
				
				points[4].setLocation(1.5f,resAmplitude*2-this.resonance);
				points[4].setPoint_type(PathIterator.SEG_LINETO);
				points[4].setCurve_type(LIN);
				
				points[5].setLocation(1.1f,1.1f);
				break;
			//low pass
			case 1:												
				points[1].setLocation(-0.3f,resAmplitude*2-this.resonance);
				points[2].setLocation(-0.2f+cutOff,resAmplitude*2-this.resonance);
				points[2].setPoint_type(PathIterator.SEG_LINETO);
				points[2].setCurve_type(LIN);
				
				points[3].setLocation(cutOff,this.resonance);
				points[3].setPoint_type(PathIterator.SEG_CUBICTO);
				points[3].setCurve_type(EXP);
				//angle going from 180 to 100 with resonance going from 1 to resAmplitude/2
				angle = 180f+ (this.resonance - resAmplitude)/resAmplitude*160f;
				points[3].setBezier(points[2].getX(),points[2].getY(),0.1f,0.05f,0f,angle);
				
				points[4].setLocation(0.3f+cutOff, 1.1f);
				points[4].setPoint_type(PathIterator.SEG_CUBICTO);		
				points[4].setCurve_type(LOG);
//				angle going from 70 to 0 with resonance going from 1 to resAmplitude/2
				angle = (resAmplitude - this.resonance )/resAmplitude*140f;
				//angle going from 0 to 70 with resonance going from 0 to resAmplitude						 		
				points[4].setBezier(points[3].getX(),points[3].getY(),0.1f,0.1f,angle,-110);
				break;
			//band pass
			case 2:
				points[1].setLocation(-0.1f,1.1f);
				points[2].setLocation(-0.45f+cutOff,1.1f);
				points[2].setPoint_type(PathIterator.SEG_LINETO);
				points[2].setCurve_type(LIN);
				
				float l = 0.25f;
				points[3].setLocation(cutOff,this.resonance);
				points[3].setPoint_type(PathIterator.SEG_CUBICTO);
				points[3].setCurve_type(EXP);
				angle = -70 + (this.resonance - resAmplitude)/resAmplitude*40f;
//				angle going from 180 to 100 with resonance going from 1 to resAmplitude/2				
				float angle2 = 180f+ (this.resonance - resAmplitude)/resAmplitude*160f;
				points[3].setBezier(points[2].getX(),points[2].getY(),l,l,angle,angle2);
				
				points[4].setLocation(0.45f+cutOff, 1.1f);
				points[4].setPoint_type(PathIterator.SEG_CUBICTO);
				points[4].setCurve_type(EXP);
				//angle going from 0 to 80 with resonance going from 1 to resAmplitude/2
				angle = (this.resonance - resAmplitude)/resAmplitude*-160f;
				angle2 = -110 - (this.resonance - resAmplitude)/resAmplitude*40f;
				points[4].setBezier(points[3].getX(),points[3].getY(),l,l,angle,angle2);
				break;
			case 3:
				points[1].setLocation(-0.1f,resAmplitude);
				points[2].setLocation(-0.45f+cutOff,resAmplitude);
				points[2].setPoint_type(PathIterator.SEG_LINETO);
				points[2].setCurve_type(LIN);
				
				l = 0.25f;
				points[3].setLocation(cutOff,1 + resAmplitude - this.resonance);
				points[3].setPoint_type(PathIterator.SEG_CUBICTO);
				points[3].setCurve_type(EXP);
				angle = -70 + (this.resonance - resAmplitude)/resAmplitude*40f;
//				angle going from 180 to 100 with resonance going from 1 to resAmplitude/2				
				angle2 = 180f+ (this.resonance - resAmplitude)/resAmplitude*160f;
				points[3].setBezier(points[2].getX(),points[2].getY(),l,l,0,-90);
				
				points[4].setLocation(0.45f+cutOff, resAmplitude);
				points[4].setPoint_type(PathIterator.SEG_CUBICTO);
				points[4].setCurve_type(EXP);
				//angle going from 0 to 80 with resonance going from 1 to resAmplitude/2
				angle = (this.resonance - resAmplitude)/resAmplitude*-160f;
				angle2 = -110 - (this.resonance - resAmplitude)/resAmplitude*40f;
				points[4].setBezier(points[3].getX(),points[3].getY(),l,l,-90,180);
				points[5].setLocation(1.1f,resAmplitude);
				points[6].setLocation(1.1f,1.1f);
				break;
				
		}						
	}
	
	public float getResonance() {		
		return (this.resonance -resAmplitude) * 2/resAmplitude;
	}
	
	public void setResonance(float resonance) {
		// this.resonance goes from resAmplitute to resAmplitude/2 when resonance goes from 0 to 1
		this.resonance = resAmplitude  - resonance*resAmplitude*0.5f;
		
		update();				
	}
	
	public float getCutOff()
	{
		return cutOff/.8f-.1f;
	}
	
	public void setCutOff(float cutOff)
	{
		//cutoff comprised between .1 and .9 
		this.cutOff = cutOff*.8f+.1f;
		update();
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
		update();
		
	}
		
}
