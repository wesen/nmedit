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
	int type =0; // 
	float cutOff =0.5f;
	
	float resAmplitude = 0.45f;			
	float resonance = 0;
	
	int slope = 1; // 0 = 12 db, 1 = 24 db
	int gainControl = 1;
    
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
        setModified(true);
		switch(type){
		//high pass
			case 2:
				float gainOffset = gainControl == 0 ? 1f:0.5f;
				points[1].setLocation(cutOff-0.5f+slope*0.25f, 1.1f+ resAmplitude*.5f*resonance*gainControl);
				
				points[2].setLocation(cutOff,resAmplitude - resonance*resAmplitude*gainOffset);
				points[2].setPoint_type(PathIterator.SEG_CUBICTO);
				points[2].setCurve_type(EXP);

				
				float angleSlope = -70+40*slope;						 									
				float angle = 180f  - 80f* resonance; 
				points[2].setBezier(points[1].getX(),points[1].getY(),0.05f,0.2f+0.1f*resonance,angleSlope,angle);
								
				points[3].setLocation(cutOff+0.1f+slope*.15f,resAmplitude + resAmplitude*.5f*resonance*gainControl);
				points[3].setPoint_type(PathIterator.SEG_CUBICTO);
				points[3].setCurve_type(EXP);

				angle =  80f * resonance;		 							
				points[3].setBezier(points[2].getX(),points[2].getY(),0.05f*resonance,0.05f+0.05f*resonance,angle,180f);
				
				points[4].setLocation(1.5f,resAmplitude + resAmplitude*.5f*resonance*gainControl);
				points[4].setPoint_type(PathIterator.SEG_LINETO);
				points[4].setCurve_type(LIN);
				
				points[5].setLocation(1.1f,1.1f);
				points[6].setLocation(-0.1f,1.1f);
				break;
			//low pass
			case 0:			
				gainOffset = gainControl == 0 ? 1f:0.5f;
				points[1].setLocation(-0.3f,resAmplitude + resAmplitude*.5f*resonance*gainControl);
				points[2].setLocation(-0.1f+cutOff-slope*.15f,resAmplitude + resAmplitude*.5f*resonance*gainControl);
				points[2].setPoint_type(PathIterator.SEG_LINETO);
				points[2].setCurve_type(LIN);
				
				points[3].setLocation(cutOff,resAmplitude - resonance*resAmplitude*gainOffset);
				points[3].setPoint_type(PathIterator.SEG_CUBICTO);
				points[3].setCurve_type(EXP);

				angle = 180f - 80f*resonance; 
				points[3].setBezier(points[2].getX(),points[2].getY(),0.05f+0.05f*resonance,0.05f*resonance,0f,angle);
				
				points[4].setLocation(0.5f+cutOff-slope*0.25f, 1.1f + resAmplitude*.5f*resonance*gainControl);
				points[4].setPoint_type(PathIterator.SEG_CUBICTO);		
				points[4].setCurve_type(LOG);
				angle = (85f-15f*slope)*resonance;
				
				angleSlope = -150+40*slope;						 		
				points[4].setBezier(points[3].getX(),points[3].getY(),0.2f+0.1f*resonance,0.05f,angle,angleSlope);
				points[5].setLocation(1.1f,1.1f);
				points[6].setLocation(-0.1f,1.1f);
				break;
			//band pass
			case 1:
				gainOffset = gainControl == 0 ? 1f:0.5f;
				//compute left end of the curve
				float leftEnd, rightEnd;
				if(slope == 0)
				{
					leftEnd = -0.65f+cutOff+0.12f*resonance;
					rightEnd = 0.65f+cutOff-0.12f*resonance;
				} else
				{
					leftEnd = -0.45f+ cutOff+0.06f*resonance;
					rightEnd = .45f+ cutOff-0.06f*resonance;
				}
				
				points[1].setLocation(-0.1f,1.1f+ resAmplitude*.5f*resonance*gainControl);
				points[2].setLocation(leftEnd,1.1f+ resAmplitude*.5f*resonance*gainControl);
				points[2].setPoint_type(PathIterator.SEG_LINETO);
				points[2].setCurve_type(LIN);
								
				points[3].setLocation(cutOff,resAmplitude - resonance*resAmplitude*gainOffset);
				points[3].setPoint_type(PathIterator.SEG_CUBICTO);
				points[3].setCurve_type(EXP);
				
				float lenSegOrig = 0.25f; 
				float lenSegEnd = slope == 1 ? 0.25f : 0.25f+0.15f*resonance;
				angle = -50f - 40f*resonance - 20*slope;				
				float angle2 = 180f - 80f*resonance;
				points[3].setBezier(points[2].getX(),points[2].getY(),lenSegOrig,lenSegEnd,angle,angle2);
				
				// curve is symetric w.r.t. cutoff = 0.5.
				points[4].setLocation(rightEnd, 1.1f+ resAmplitude*.5f*resonance*gainControl);
				points[4].setPoint_type(PathIterator.SEG_CUBICTO);
				points[4].setCurve_type(EXP);
								
				points[4].setBezier(points[3].getX(),points[3].getY(),lenSegEnd,lenSegOrig,180 - angle2, 180 - angle);

				points[5].setLocation(1.1f,1.1f);
				points[6].setLocation(-0.1f,1.1f);
				break;
			// band reject
			case 3:
				gainOffset = gainControl == 0 ? 0.35f:0f;
				
				points[1].setLocation(-0.5f,resAmplitude + resAmplitude*gainOffset*resonance);
				points[2].setLocation(-0.45f+(0.15f-0.1f*slope)*resonance+cutOff-slope*0.05f,resAmplitude + resAmplitude*gainOffset*resonance);
				points[2].setPoint_type(PathIterator.SEG_LINETO);
				points[2].setCurve_type(LIN);
				
				//adapting the beziez parameter with respect to the slop
				lenSegOrig = 0.2f;
				float res;
				//slope is 12db
				if(slope == 0){
					lenSegOrig = 0.3f; // length of the orinal control segment  
					lenSegEnd = 0.2f; //  length of the ending control segment
					res = 1 ;//+ resAmplitude/2*(1- resonance);
				// 24db: make to curve steeper => reduce length of segments  
				} else{
					lenSegOrig = 0.2f;
					lenSegEnd = 0.4f;
					res = 1+ resAmplitude*.8f*(1- resonance);
				}
				 
				 
				points[3].setLocation(cutOff,res);
				points[3].setPoint_type(PathIterator.SEG_CUBICTO);
				points[3].setCurve_type(EXP);
				
				points[3].setBezier(points[2].getX(),points[2].getY(),lenSegOrig,lenSegEnd,0,-90);
				
				points[4].setLocation(0.45f-(0.15f-0.1f*slope)*resonance+cutOff+slope*0.05f, resAmplitude + resAmplitude*gainOffset*resonance);
				points[4].setPoint_type(PathIterator.SEG_CUBICTO);
				points[4].setCurve_type(EXP);
				
				points[4].setBezier(points[3].getX(),points[3].getY(),lenSegEnd,lenSegOrig,-90,180);
				points[5].setLocation(1.1f,resAmplitude + resAmplitude*gainOffset*resonance);
				points[6].setLocation(1.1f,1.1f);
				break;
				
		}						
	}
	
	public float getResonance() {		
		return resonance;
	}
	
	public void setResonance(float resonance) {		
		this.resonance = resonance;
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
		
	public int getSlope() {
		return slope;
	}

	public void setSlope(int slope) {
		this.slope = slope;
		update();
	}
	
	public int getGainControl() {
		return gainControl;
	}

	public void setGainControl(int gainControl) {
		this.gainControl = gainControl;
		update();
	}
}
