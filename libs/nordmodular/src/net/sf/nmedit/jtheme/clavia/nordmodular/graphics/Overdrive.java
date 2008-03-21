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

public class Overdrive extends Curve {
	
	float overdrive;
	
	public Overdrive(){
		super(6);
		points[0].setPoint_type(PathIterator.SEG_MOVETO);
		points[0].setLocation(-0.1f,1f);
		points[3].setLocation(1.1f, 0f);
		points[4].setLocation(1.1f, 1.1f);
		points[5].setLocation(-.1f, 1.1f);
		
		points[2].setPoint_type(PathIterator.SEG_CUBICTO);
		points[2].setCurve_type(LOG);
		update();
	}
	
	private void update(){
        setModified(true);
        
		points[1].setLocation(.45f*overdrive, 1f);
		
		
		points[2].setLocation(1-.45f*overdrive, 0f);
		
		// from -45° to 0 , and from 135 to 180 for bezier angles
		points[2].setBezier(points[1].getX(),points[1].getY(),.25f,.25f,-45*(1-overdrive),180f-45*(1-overdrive));
		}

	public int getOverdrive() {
		return (int)(overdrive);
	}

	public void setOverdrive(float overdrive) {
		this.overdrive = overdrive;
		update();
	}
	
	public PathIterator getPathIterator( final AffineTransform at )
    {
        return new FilterIterator(at,points,bounds);
    }
	
}
