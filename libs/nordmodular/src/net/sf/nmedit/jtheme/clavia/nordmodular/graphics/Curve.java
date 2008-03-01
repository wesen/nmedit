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
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.FlatteningPathIterator;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;


public class Curve implements Shape {
	public final static int LIN = 0;
    public final static int EXP = 1;
    public final static int LOG = 2;
	protected Point points[];
	
    protected Rectangle bounds = new Rectangle(0,0,1,1);

    private boolean modified = true;
    
    public void setModified(boolean modified)
    {
        this.modified = modified;
    }

    public boolean isModified()
    {
        return modified;
    }
	
    public Curve(int numPts) {
    	points = new Point[numPts];
    	for(int i = 0; i < points.length ; i++)
		{
			points[i] = new Point();
			points[i].setPoint_type(PathIterator.SEG_LINETO);
			points[i].setCurve_type(LIN);
		}    	
    }
	
	public Rectangle getBounds()
    {
        return new Rectangle(bounds);
    }
	
	public void setBounds(int x, int y, int width, int height)
    {
        bounds.setBounds(x, y, width, height);
    }
	
    public Rectangle2D getBounds2D()
    {
        return getBounds();
    }

    public boolean contains( double x, double y )
    {
        return bounds.contains(x, y);
    }

    public boolean contains( Point2D p )
    {
        return bounds.contains(p);
    }

    public boolean intersects( double x, double y, double w, double h )
    {
        return bounds.intersects(x, y, w, h); 
    }

    public boolean intersects( Rectangle2D r )
    {
        return bounds.intersects(r);
    }

    public boolean contains( double x, double y, double w, double h )
    {
        return bounds.contains(x, y, w, h);
    }

    public boolean contains( Rectangle2D r )
    {
        return bounds.contains(r);
    }
    
    public PathIterator getPathIterator( final AffineTransform at )
    {
        return new CurvePathIterator(at,points,bounds);
    }

    public PathIterator getPathIterator( AffineTransform at, double flatness )
    {
        return new FlatteningPathIterator(getPathIterator(at), flatness);
    }
}
