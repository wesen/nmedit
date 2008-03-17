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
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Pseudo3DCableGeometrie extends CubicCurve2D.Float 
    implements CableGeometrie
{

    /**
     * 
     */
    private static final long serialVersionUID = 2686151817733494025L;
    private boolean boundaryChanged = true;
    private transient Rectangle2D cachedBounds;

	private final static int maxCX = 60;
	private final static int maxCY = 10;
	private final static int dyTreshold = 40;
	private final static int dxTreshold = 300; 	// in pixel
	private final static int gravityLimit = 100; 		// in pixel
	private final static int hrzGravityTreshold = dyTreshold/2; 	// in pixel
	//private ShapeGrid shapeGrid = new ShapeGrid() ;

	public Pseudo3DCableGeometrie() 
    {
		this(0, 0, 0, 0);
	}
	
	public Pseudo3DCableGeometrie(float x1, float y1, float x2, float y2) 
    {
		setCurve(x1, y1, x2, y2);
	}
	
	public Pseudo3DCableGeometrie(Point2D p1, Point2D p2) 
    {
		setCurve((float)p1.getX(), (float)p1.getY(), (float)p2.getX(), (float)p2.getY());
	}
	
	public Point getP1() { return new Point(getX1i(), getY1i()); }
	public Point getP2() { return new Point(getX2i(), getY2i()); } 
	public Point getCtrlP1() { return new Point(getCtrlX1i(), getCtrlY1i()); }
	public Point getCtrlP2() { return new Point(getCtrlX2i(), getCtrlY2i()); } 

	public int getX1i() { return (int) getX1(); }
	public int getY1i() { return (int) getY1(); }
	public int getX2i() { return (int) getX2(); }
	public int getY2i() { return (int) getY2(); }

	public int getCtrlX1i() { return (int) getCtrlX1(); }
	public int getCtrlY1i() { return (int) getCtrlY1(); }
	public int getCtrlX2i() { return (int) getCtrlX2(); }
	public int getCtrlY2i() { return (int) getCtrlY2(); }
	
	public void setP1(float x, float y) { setCurve(x, y, x2, y2); }
	public void setP1(Point2D p) { setP1((float)p.getX(), (float)p.getY()); }
	public void setP2(float x, float y) { setCurve(x1, y1, x, y); }
	public void setP2(Point2D p) { setP2((float)p.getX(), (float)p.getY()); }
	
	public void setCurve(CubicCurve2D curve) 
    {
		setCurve((float)curve.getCtrlX1(), (float)curve.getCtrlY1(), (float)curve.getCtrlX2(), (float)curve.getCtrlY2());
	}

	public void setCurve(Point p1, Point p2) 
    {
		setCurve(p1.x, p1.y, p2.x, p2.y);
	}
	
	public void setCurve(float x1, float y1, float ctrlx1, float ctrly1, float ctrlx2, float ctrly2, float x2, float y2) 
    {
		setCurve(x1, y1, x2, y2);
	}
	
	protected void swapPoints() 
    {
		float tmp;
		tmp = x1; x1=x2; x2=tmp;
		tmp = y1; y1=y2; y2=tmp;
		tmp = ctrlx1; ctrlx1=ctrlx2; ctrlx2=tmp;
		tmp = ctrly1; ctrly1=ctrly2; ctrly2=tmp;
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

	private void updateControlPoints() 
    {
		final float dx = Math.abs(x1-x2); // distance x : p1, p2
		final float dy = Math.abs(y1-y2); // distance y : p1, p2

		// control point offset from p1/p2 (without gravity)
		float cx = Math.min(maxCX, dx);
		float cy = Math.min(maxCY, dy);

		// if p1 is at the left of p2 then -1, otherwise +1 
		int isLeft  = x1<x2 ? -1 : +1;
		// is p1 is above p2 then +1, otherwise -1
		int isAbove = y1<y2 ? +1 : -1;

		// if p1 and p2 are below dy treshold, the higher control point is lowered
		// so that the cable hangs down rather then it becomes a straight line
		float gravity1 = Math.max(0, dyTreshold-dy);
		// if distanceX(p1, p2) passes dx treshold the gravity has higher impact
		float infiniteGravity = Math.max(0, dx-dxTreshold);
		// => the cable will hang down more than before (limited by gravityLimit)
		float gravity2 = Math.min(infiniteGravity, gravityLimit);

		// assures that if p1 is exactly above p2 the curve is not a straight lines 
		float hrzGravity = Math.min(Math.max(0, hrzGravityTreshold-dx), hrzGravityTreshold);
		
		// offset with gravity
		float offsetX = ((cx-gravity1)*isLeft); 
		float offsetY = (cy*isAbove);
		float globalGravity = gravity1+gravity2;

		ctrly1 = y1-offsetY+globalGravity;
		ctrlx1 = x1-offsetX+hrzGravity;
		ctrly2 = y2+offsetY+globalGravity;
		ctrlx2 = x2+offsetX+hrzGravity;
        
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
