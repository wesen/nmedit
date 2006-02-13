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
package org.nomad.theme.curve;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Point2D;

public class Curve extends CubicCurve2D.Double {

	private final static int maxCX = 60;
	private final static int maxCY = 10;
	private final static int dyTreshold = 40;
	private final static int dxTreshold = 300; 	// in pixel
	private final static int gravityLimit = 100; 		// in pixel
	private final static int hrzGravityTreshold = dyTreshold/2; 	// in pixel

	private CurveEventCallback callback = null;

	private Color color = Color.WHITE;
	
	public Curve() {
		this(0, 0, 0, 0);
	}
	
	public Curve(double x1, double y1, double x2, double y2) {
		setCurve(x1, y1, x2, y2);
	}
	
	public Curve(Point2D p1, Point2D p2) {
		setCurve(p1.getX(), p1.getY(), p2.getX(), p2.getY());
	}
	
	protected void beforeChange() {
		if (callback!=null) callback.beforeChange(this);
	}

	protected void afterChange() {
		if (callback!=null) callback.afterChange(this);
	}
	
	CurveEventCallback getCallback() {
		return callback;
	}
	
	void setCallback(CurveEventCallback callback) {
		this.callback = callback;
	}

	public Color getColor() {
		return color;
	}
	
	public void setColor(Color c) {
		beforeChange(); 
		this.color = c;
		afterChange();
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
	
	public void setP1(double x, double y) { setCurve(x, y, x2, y2); }
	public void setP1(Point2D p) { setP1(p.getX(), p.getY()); }
	public void setP2(double x, double y) { setCurve(x1, y1, x, y); }
	public void setP2(Point2D p) { setP2(p.getX(), p.getY()); }
	
	public void setCurve(CubicCurve2D curve) {
		setCurve(curve.getCtrlX1(), curve.getCtrlY1(), curve.getCtrlX2(), curve.getCtrlY2());
	}

	public void setCurve(Point p1, Point p2) {
		setCurve(p1.x, p1.y, p2.x, p2.y);
	}
	
	public void setCurve(double x1, double y1, double ctrlx1, double ctrly1, double ctrlx2, double ctrly2, double x2, double y2) {
		setCurve(x1, y1, x2, y2);
	}
	
	public void setCurve(double x1, double y1, double x2, double y2) {
		beforeChange();
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		updateControlPoints();
		afterChange();
	}

	private void updateControlPoints() {
		final double dx = Math.abs(x1-x2); // distance x : p1, p2
		final double dy = Math.abs(y1-y2); // distance y : p1, p2

		// control point offset from p1/p2 (without gravity)
		double cx = Math.min(maxCX, dx);
		double cy = Math.min(maxCY, dy);

		// if p1 is at the left of p2 then -1, otherwise +1 
		int isLeft  = x1<x2 ? -1 : +1;
		// is p1 is above p2 then +1, otherwise -1
		int isAbove = y1<y2 ? +1 : -1;

		// if p1 and p2 are below dy treshold, the higher control point is lowered
		// so that the cable hangs down rather then it becomes a straight line
		double gravity1 = Math.max(0, dyTreshold-dy);
		// if distanceX(p1, p2) passes dx treshold the gravity has higher impact
		double infiniteGravity = Math.max(0, dx-dxTreshold);
		// => the cable will hang down more than before (limited by gravityLimit)
		double gravity2 = Math.min(infiniteGravity, gravityLimit);

		// assures that if p1 is exactly above p2 the curve is not a straight lines 
		double hrzGravity = Math.min(Math.max(0, hrzGravityTreshold-dx), hrzGravityTreshold);
		
		// offset with gravity
		double offsetX = ((cx-gravity1)*isLeft); 
		double offsetY = (cy*isAbove);
		double globalGravity = gravity1+gravity2;

		ctrly1 = y1-offsetY+globalGravity;
		ctrlx1 = x1-offsetX+hrzGravity;
		ctrly2 = y2+offsetY+globalGravity;
		ctrlx2 = x2+offsetX+hrzGravity;
	}
	
}
