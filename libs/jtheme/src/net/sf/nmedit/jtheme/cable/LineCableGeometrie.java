/* Copyright (C) 2006 Christian Schneider
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
package net.sf.nmedit.jtheme.cable;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Line2D.Float;

public class LineCableGeometrie extends Float implements CableGeometrie
{

    /**
     * 
     */
    private static final long serialVersionUID = -1531914634168898089L;
    private boolean boundaryChanged = true;
    private transient Rectangle2D cachedBounds;
    
    public void setLine(double x1, double y1, double x2, double y2) 
    {
        boundaryChanged = true;
        super.setLine(x1, y1, x2, y2);
    }

    public void setLine(float x1, float y1, float x2, float y2) 
    {
        boundaryChanged = true;
        super.setLine(x1, y1, x2, y2);
    }

    private final Rectangle2D getCachedBounds()
    {
        // cachedBounds==null => boundaryChanged = true 
        if (boundaryChanged || (cachedBounds == null))
        {
            cachedBounds = super.getBounds();
        }
        return cachedBounds;
    }

    public Rectangle getBounds()
    {
        return getBounds(null);
    }
    
    public Rectangle getBounds(Rectangle r)
    {
        if (r == null)
            r = new Rectangle();
        r.setFrame(getCachedBounds());
        return r;
    }

    public Shape getShape()
    {
        return this;
    }

    public boolean intersects(int x, int y, int width, int height)
    {
        return super.intersects(x, y, width, height);
    }

    public boolean intersects(Rectangle r)
    {
        return super.intersects(r);
    }

    public void setEndPoints(int x1, int y1, int x2, int y2)
    {
        setLine(x1, y1, x2, y2);
    }

    public void setEndPoints(Point p1, Point p2)
    {
        setLine(p1, p2);
    }

    public Point getStart()
    {
        return new Point((int)getX1(), (int)getY1());
    }

    public Point getStop()
    {
        return new Point((int)getX2(), (int)getY2());
    }

    public double getShake()
    {
        return 0;
    }

    public void setShake(double shake)
    {
        // not supported
    }

    public void shake()
    {
        // not supported
    }

    
    
}

