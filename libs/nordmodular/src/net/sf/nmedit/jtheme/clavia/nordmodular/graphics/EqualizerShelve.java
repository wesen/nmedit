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

public class EqualizerShelve extends Curve {
	public EqualizerShelve() {
		super(7); 
		// we increase just a bit the coordinates so that the border 
		// of the shape is invisible
		points[0].setLocation(-0.01f, 1.01f);
		points[0].setPoint_type(PathIterator.SEG_MOVETO);
		points[1].setLocation(-0.01f, 0.3f);
		points[2].setLocation(0.4f, 0.3f);
		points[3].setLocation(0.6f, 0.5f);
		points[4].setLocation(1.1f, 0.5f);
		points[5].setLocation(1.01f, 1.01f);
		points[6].setLocation(-0.01f, 1.10f);
		
	}
	
	public void  setGain(float gain){
        setModified(true);
		points[1].setY(1 - gain);
		points[2].setY(1 - gain);
	}
	
	public float getGain(){
		return 1 - points[1].getY();
	}
	
	public float getFreq()
	{
		return points[2].getX();
	}
	
	public void setFreq(float f)
	{
        setModified(true);
		points[2].setX(f*0.7f);
		points[3].setX((f+0.2f)*.7f);
	}

}
