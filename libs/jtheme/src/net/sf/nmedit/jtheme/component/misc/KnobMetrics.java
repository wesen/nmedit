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
 * Created on Jan 7, 2006
 */
package net.sf.nmedit.jtheme.component.misc;

import java.awt.geom.Point2D;

/**
 * @author Christian Schneider
 */
public class KnobMetrics {

	public final static double PI2 = Math.PI*2.0d;
	public final static double PIdiv2 = Math.PI/2.0d;
	public final static double PIdiv4 = Math.PI/4.0d;

	/**
	 * Returns the norm of p. If (p = z+k) and z \in Z and 0<=k<=1 then normP(p)==k.
	 * (normP(p):=p-Math.floor(p))
	 * 
	 * @param p
	 * @return value k with 0&lt;=k&lt;=1
	 */
	public static double normP(double p) {
		return p==1 ? 1 : p-Math.floor(p);
	}

	/**
	 * Returns true if a and b have nearly the same value.
	 * (abs(a-b)<1e-20)
	 * 
	 * @return true if a and b have nearly the same value
	 */
	public static boolean areSimilar(double a, double b) {
		return Math.abs(a-b)<1e-20;
	}
	
	/**
	 * Returns true if a and 0 are similar
	 * @param a number to check if it is similar to zero
	 * @return true if a and 0 are similar
	 * @see #areSimilar(double, double)
	 */
	public static boolean isCloseToZero(double a) {
		return areSimilar(0, a);
	}
	
	/**
	 * <pre> Angles:
	 *          180
	 *           |
	 *      270 ---  90
	 *           |
	 *           0</pre>
	 * 
	 * @param mx ordinate (x-coordinate) of the point m(mx, my)
	 * @param my abscissa (y-coordinate) of the point m(mx, my)
	 * @param px ordinate (x-coordinate) of the point p(px, py)
	 * @param py abscissa (y-coordinate) of the point p(px, py)
	 * 
	 * @return the angle <code>a</code> of point <code>p</code> concering the coordinate systems center <code>m</code>.
	 * For the return value <code>a</code> the condition <code>0&lt;=a&lt;=1</code> is true if
	 * the equation (areSimilar(mx, px) and areSimilar(my,py)) is false. Otherwise
	 * an angle can not be returned. To indicate this, -1 will be returned. (Note -1 is equivalent to +1 or 0).
	 * @see #areSimilar(double, double)
	 */
	public static double getAngle(double mx, double my, double px, double py) {
		if (KnobMetrics.areSimilar(mx, px) && KnobMetrics.areSimilar(my,py))
			return -1; // no angle

		double pangle = PIdiv2-Math.atan((py-my)/(px-mx));
		if (px<mx) {
			pangle+=Math.PI;
		} 
		return normP(pangle/PI2);
	}

	public static double getAngle(double mx, double my, double px, double py, double arcStart, double arcStop) {
		double angle = KnobMetrics.getAngle(mx, my, px, py);
		double arcRange = arcStop-arcStart;
		return angle<=arcStart ? 0: angle >= arcStop ? 1 : (angle-arcStart)/arcRange;		
	}
	
	public static double getAngleCCW(double mx, double my, double px, double py, double arcStart, double arcStop) {
		double angle = KnobMetrics.cw(KnobMetrics.getAngle(mx, my, px, py));
		double arcRange = arcStop-arcStart;
		return angle<=arcStart ? 0: angle >= arcStop ? 1 : (angle-arcStart)/arcRange;		
	}

	
	/**
	 * Returns the inverse angle.
	 * 
	 * For example the angles
	 * <pre>
	 *          180
	 *           |
	 *      270 ---  90
	 *           |
	 *           0</pre>
	 * will be transformed to
	 * <pre>
	 *          180
	 *           |
	 *       90 --- 270
	 *           |
	 *           0</pre>
	 * 
	 * @param angle
	 * @return the value r:=normP(1-angle) (normP makes shure that the condition 0&lt;=r&lt;1 is true) 
	 */
	public static double cw(double angle) {
		return normP(1.0d-angle);
	}

	/**
	 * Returns the point with distance radius from origin (midx, midy) with pangle==getAngle(midx,midy, p.x,p.y).
	 * 
	 * @param midx
	 * @param midy
	 * @param radius
	 * @param pangle
	 * @return
	 */
	public static Point2D getPoint(double midx, double midy, double radius, double pangle) {
		if (isCloseToZero(radius))
			return new Point2D.Double(midx, midy);

		pangle = pangle*Math.PI*2;
		return new Point2D.Double(midx+(Math.sin(pangle)*radius), midy+(Math.cos(pangle)*radius));
	}

	/**
	 * Returns the point that lays on the arc (arcStart, arcStop). The arcs origin is (midx, midy).
	 * The distance of the arc (point) to the origin is radius. 
	 * 
	 * @see #getPoint(double, double, double, double)
	 */
	public static Point2D getPoint(double midx, double midy, double radius, double arcPos, double arcStart, double arcStop) {
		double arcRange = arcStop-arcStart;
		return KnobMetrics.getPoint(midx, midy, radius, arcStart+arcPos*arcRange);
	}
	
	public static Point2D getPointCCW(double midx, double midy, double radius, double arcPos, double arcStart, double arcStop) {
		double arcRange = arcStop-arcStart;
		return KnobMetrics.getPoint(midx, midy, radius, KnobMetrics.cw(arcStart+arcPos*arcRange));
	}
	
}
