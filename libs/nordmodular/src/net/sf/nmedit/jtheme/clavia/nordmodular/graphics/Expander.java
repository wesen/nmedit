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

public class Expander extends Curve {
	
	float ratio = 1.0f,threshold, gate = 0;
	int ratioIndex = 0; // see _convert_ratio function 
	
	public Expander(){
		super(6);
		points[0].setPoint_type(PathIterator.SEG_MOVETO);
		points[0].setLocation(-0.1f,1f);
		update();
	}
	


	private void update(){
		setModified(true);
		if (gate<= threshold) {
			points[0].setLocation(gate,100000.1f);
			//y coordinate of the intersection of between the vertical
			// gate line and the expanding line
			
			float intersectGate = ratio*(-gate+threshold)-threshold+1f;			
			points[1].setLocation(gate,intersectGate); // gate inter
			points[2].setLocation(threshold,1f-threshold);
		}
		else {
			points[0].setLocation(gate,1.1f);
			points[1].setLocation(gate,1f-gate); // gate inter
			points[2].setLocation(gate,1f-gate);
		}
		
		
		points[3].setLocation(1.1f,-0.1f);
		
	}
	
	public float getGate() {
		return gate;///0.8f;
	}

	public void setGate(float gate) {
		this.gate = gate;//*.8f;
		update();
	}

	
	public float getRatio() {
		return ratioIndex;
	}

	public void setRatio(int ratio) {
		this.ratio = _convert_ratio(ratio);
		this.ratioIndex = ratio;
		
		update();
	}


	public float getThreshold() {
		return threshold;
	}

	public void setThreshold(float threshold) {
		this.threshold = threshold;
		update();
	}
	
	private float _convert_ratio(int ratio){
		float conv[] = {1.0f,1.1f,1.2f,1.3f,1.4f,1.5f,1.6f,1.7f,1.8f,1.9f,2.0f,2.2f,2.4f,2.6f,2.8f,3.0f,3.2f,3.4f,3.6f,3.8f,4.0f,4.2f,4.4f,4.6f,4.8f,5.0f,5.5f,6.0f,6.5f,7.0f,7.5f,8.0f,8.5f,9.0f,9.5f,10f,11f,12f,13f,14f,15f,16f,17f,18f,19f,20f,22f,24f,26f,28f,30f,32f,34f,36f,38f,40f,42f,44f,46f,48f,50f,55f,60f,65f,70f,75f,80f}; 
		
		return ratio < conv.length ? conv[ratio]: 1.0f;
	}
}
