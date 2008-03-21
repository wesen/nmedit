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

/*
 * Created on Feb 10, 2006
 */
package net.sf.nmedit.jtheme.cable;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class SimpleCableGeometrie extends QuadCurve2D.Float 
    implements CableGeometrie
{

    /**
     * 
     */
    private static final long serialVersionUID = 2686151817733494025L;
    private boolean boundaryChanged = true;
    private transient Rectangle2D cachedBounds;
    
    private final static int hangingMin = 4;
    private final static int hangingMax = 55;
    private final static float hangingWeight = 0.3F; 

	public SimpleCableGeometrie() 
    {
		this(0, 0, 0, 0);
	}
	
	public SimpleCableGeometrie(float x1, float y1, float x2, float y2) 
    {
		setCurve(x1, y1, x2, y2);
	}
	
	public SimpleCableGeometrie(Point2D p1, Point2D p2) 
    {
		setCurve((float)p1.getX(), (float)p1.getY(), (float)p2.getX(), (float)p2.getY());
	}
	
	public Point getP1() { return new Point(getXi(), getYi()); }
	public Point getP2() { return new Point(getXi(), getYi()); } 
	public Point getCtrlP1() { return new Point(getCtrlXi(), getCtrlYi()); }

	public int getXi() { return (int) getX1(); }
	public int getYi() { return (int) getY1(); }

	public int getCtrlXi() { return (int) getCtrlX(); }
	public int getCtrlYi() { return (int) getCtrlY(); }
	
	public void setCurve(QuadCurve2D curve) 
    {
		setCurve((float)curve.getX1(), (float)curve.getY1(), (float)curve.getX2(), (float)curve.getY2());
	}

	public void setCurve(Point p1, Point p2) 
    {
		setCurve(p1.x, p1.y, p2.x, p2.y);
	}
	
	public void setCurve(float x1, float y1, float ctrlx1, float ctrly1, float ctrlx2, float ctrly2, float x2, float y2) 
    {
		setCurve(x1, y1, x2, y2);
	}
	
    public void setCurve(float x1, float y1, float x2, float y2) 
    {
		if (this.x1!=x1||this.y1!=y1||this.x2!=x2||this.y2!=y2) {
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
	        updateControlPoints();
		}

	}

    private double shake = 0; // default: no shaking 
        
	private void updateControlPoints() 
    {
		float length = (float) Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
		int isLeft = ((x1<x2 && y1<y2) || (x1>x2 && y1>y2)) ? -1 : +1;
		
		float shakeDepLen = (float) ((shake / 5) * (length / 2));
		
		ctrlx = ((x1+x2) / 2) + (hangingMin + shakeDepLen) * isLeft;
		ctrly = ((y1+y2) / 2) + Math.min(hangingMax, ((hangingWeight) * length) + shakeDepLen);
		
        boundaryChanged = true;
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

    public boolean intersects(int x, int y, int width, int height)
    {
        return super.intersects(x, y, width, height);
    }

    public boolean intersects(Rectangle r)
    {
        return super.intersects(r.x, r.y, r.width, r.height);
    }

    private final Rectangle2D getCachedBounds()
    {
        // cachedBounds==null => boundaryChanged = true 
        if (boundaryChanged || (cachedBounds == null))
        {
            cachedBounds = super.getBounds2D();
            boundaryChanged = false;
        }
        return cachedBounds;
    }

    public void setEndPoints(int x1, int y1, int x2, int y2)
    {
        setCurve(x1, y1, x2, y2);
    }
    
    public void setEndPoints(Point p1, Point p2)
    {
        setCurve(p1, p2);
    }

    public Shape getShape()
    {
        return this;
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
        return shake;
    }

    public void setShake(double shake)
    {
        this.shake = shake;
        updateControlPoints();
    }

    public void shake()
    {
        setShake(Math.random());
    }

}
