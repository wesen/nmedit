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
 * Created on Jan 18, 2006
 */
package org.nomad.theme.component.model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import org.nomad.image.ImageToolkit;
import org.nomad.theme.component.KnobToolkit;
import org.nomad.theme.component.NomadControl;

public class NomadClassicKnobGraphics {

	public final static Color clFillDark = Color.decode("#969696");
	public final static Color clFillLight = Color.decode("#9D9D9D");
	public final static Color clHighlight = new Color(245, 245, 220, 180);

	private final static HashMap cacheMap = new HashMap();
	
	public static ControlCachedGraphics obtainGraphicsCache(NomadControl control, ControlCachedGraphics currentGraphics) {
		if ((currentGraphics==null)||(!currentGraphics.matches(control.getSize()))) {
			if (currentGraphics!=null) unregister(currentGraphics);			
			currentGraphics = searchCache(control.getSize());
			if (!currentGraphics.isValid) {
				currentGraphics.render();
			}
			register(currentGraphics);
		}
		return currentGraphics;
	}

	private static ControlCachedGraphics searchCache(Dimension size) {
		ControlCachedGraphics ccg = new ControlCachedGraphics(size);
		ControlCachedGraphics mappedCCg = (ControlCachedGraphics) cacheMap.get(ccg);
		return (mappedCCg==null) ? ccg : mappedCCg;
	}
	
	private static void register(ControlCachedGraphics ccg) {
		if (0==(ccg.referenceCount++)) {
			cacheMap.put(ccg,ccg);
		}
	}
	
	private static void unregister(ControlCachedGraphics ccg) {
		if ((--ccg.referenceCount)==0) {
			cacheMap.remove(ccg);
		}
	}

	public static class Metrics {
		
		public int w = -1;
		public int h = -1;
		// diameter
		public int diameter = -1;
		// radius
		public double radius = -1;
		// center point
		public double cx = -1;
		public double cy = -1;

		public Point lBar = new Point(0, 0);
		public Point rBar = new Point(0, 0);

		public final static double arcStart = 45.0d/360.0d;
		public final static double arcStop = (270.0d+45.0d)/360.0d;
		public final static double arcRange = (arcStop-arcStart);

		public Ellipse2D ellipse = null;
		public Ellipse2D ellipseSmall = null;
		public double shrink1 = 1.5;
		public double shrink2 = 2.5;
		public Point2D pointTL1 = null;
		public Point2D pointBR1 = null;
		
		public Metrics(Dimension size) {
			w=size.width;
			h=size.height;
			cx=((double) w)/2.0;
			cy=((double) h)/2.0;
			diameter = Math.min(w, h);
			radius = ((double)diameter) / 2.0;
			/*lBar = new Point(1, h-2);
			rBar = new Point(w-2, h-2);*/

			lBar = convert(calcPosition2D(0, h-2));
			rBar = convert(calcPosition2D(1, h-2));
			

			pointTL1 = getTopLeftMostPoint(1);
			pointBR1 = getRightBottomMostPoint(2.5);
			
			// the shape
			ellipse = new Ellipse2D.Double(
					pointTL1.getX(), pointTL1.getY(),
					pointBR1.getX(), pointBR1.getY()
			);
			ellipseSmall = new Ellipse2D.Double(
					pointTL1.getX()+shrink1, pointTL1.getY()+shrink1,
					pointBR1.getX()-shrink2, pointBR1.getY()-shrink2
			);
			
		}
		
		public Point2D getTopLeftMostPoint(double shrink) {
			double r = radius-shrink;
			return new Point2D.Double(cx-r, cy-r);
		}

		public Point2D getRightBottomMostPoint(double shrink) {
			double r = radius-shrink;
			return new Point2D.Double(cx+r, cy+r);
		}
		
		public Point2D calcPosition2D(double fact, double radius) {
			return KnobToolkit.getPointCCW(cx, cy, radius, fact, arcStart, arcStop);
		}
		
		public Point2D calcPosition2D(double fact) {
			return calcPosition2D(fact, radius-2);
		}
		
		public Point calcPosition(double fact) {
			return convert(calcPosition2D(fact));
		}
		
		public Point convert(Point2D p2) {
			return new Point(Math.round((float)p2.getX()), Math.round((float)p2.getY()));
		}
		
		public double calcRangeFactor(Point p) {
			return KnobToolkit.getAngleCCW(cx, cy, p.x, p.y, arcStart, arcStop);
		} 
		
		public Arc2D getMorphArc(NomadControl control) {
			Double morph = control.getMorphValue();
			if (morph==null)
				return null;

			double arcStartAngle = control.getValuePercentage()*270.0d; // [0..270]
			double arcAngleSize = morph.doubleValue()*270.0d; // [-270..270]
			double arcSum = arcStartAngle+arcAngleSize; // [270..2*270]

			if (arcSum>270)
				arcAngleSize = (270.0d-arcStartAngle);
			else if (arcSum<0)
				arcAngleSize = -arcStartAngle;
			
			arcStartAngle+=135.0d;
			
			return new Arc2D.Double(pointTL1.getX()+0.5, pointTL1.getY()+0.5, pointBR1.getX()-1, pointBR1.getY()-1,
					360.0d-arcStartAngle, -arcAngleSize, Arc2D.PIE);
		}
	}

	public static class ControlCachedGraphics {
		private BufferedImage cache = null;
		private boolean isValid = false;
		private int referenceCount = 0; // 0 also means not cached
		private Metrics metrics = null;
		private int hashCode = 0;
		private Dimension size=null;
		
		protected ControlCachedGraphics(Dimension size) {
			this.size = new Dimension(size);
			hashCode = size.width*size.height;
		}

		public int hashCode() {
			return hashCode;
		}

		protected void configureGraphics(Graphics2D g2) {
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		}

		protected boolean matches(Dimension size) {
			return this.size.equals(size);
		}
		
		public boolean equals(Object obj) {
			if (!(obj instanceof ControlCachedGraphics)) return false;
			return matches(((ControlCachedGraphics) obj).size);
		}
		
		protected void render() {
			if (size.width>0 && size.height>0) {
				metrics = new Metrics(size);
				cache 	= ImageToolkit.createCompatibleBuffer(new Dimension(this.metrics.w, this.metrics.h), Transparency.TRANSLUCENT);
				Graphics2D g2 = cache.createGraphics();
				paintDecoration(g2);
				g2.dispose();
				isValid = true;
			}
		}

		protected void enableQualityRendering(Graphics2D g2) {
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		
		public String toString() {
			return "ControlCachedGraphics[size="+size.width+","+size.height
			+",valid="+isValid
			+",references="+referenceCount
			+",hash="+hashCode()+"]";
		}

		public void dispose() {
			unregister(this);
		}

		public void paintDecorationCache(NomadControl control, Graphics2D g2) {
			g2.drawImage(cache, 0, 0, control);
		}

		public void paintDecoration(Graphics2D g2) {
			configureGraphics(g2);

			Metrics m = metrics;
			
			g2.setColor(Color.BLACK);
			// mid - left bottom corner
			g2.drawLine(Math.round((float)m.cx), Math.round((float)m.cy), m.lBar.x, m.lBar.y);
			// mid - right bottom corner
			g2.drawLine(Math.round((float)m.cx), Math.round((float)m.cy), m.rBar.x, m.rBar.y);

			// fill
			g2.setPaint(new GradientPaint(
				m.pointTL1, clFillLight,
				m.pointBR1, clFillDark, true
				//m.pointTL1, Color.LIGHT_GRAY, --- looks better
				//m.pointBR1, Color.GRAY, true
			));
			g2.fill(m.ellipse);

			// inner outline
			g2.setPaint(new GradientPaint(
				m.pointTL1, Color.WHITE,
				m.pointBR1, Color.BLACK, true
			));
			g2.setStroke(new BasicStroke(1.5f));
			g2.draw(m.ellipseSmall);

			// outer outline
			g2.setPaint(new GradientPaint(
				m.pointTL1, Color.LIGHT_GRAY,
				m.pointBR1, Color.BLACK, true
			));
			g2.setStroke(new BasicStroke(1.0f));
			g2.draw(m.ellipse);
				
		}
		
		public void paintDynamicOverlay(NomadControl control, Graphics2D g2) {
			configureGraphics(g2);
			Metrics m = metrics;

			if (control.hasFocus()) {
				// we highlight the button
				g2.setColor(clHighlight);
				// outer outline
				g2.setPaint(new GradientPaint(
						m.pointTL1, clFillLight,
						m.pointBR1, clHighlight, true
					));
					g2.fill(m.ellipseSmall);
				
			}
			
			Arc2D morph = m.getMorphArc(control);
			if (morph!=null) {
				// draw morph color overlay
				g2.setPaint(control.getMorphBackground());
				g2.fill(m.ellipse);

				// draw morph arc overlay
				g2.setPaint(control.getMorphForeground());
				g2.fill(morph);	
			}

			// draw marker
			Point p = m.calcPosition(control.getValuePercentage());
			g2.setColor(Color.BLACK);
			g2.drawLine(Math.round((float)m.cx), Math.round((float)m.cy), p.x, p.y);
		}

		public Metrics getMetrics() {
			return metrics;
		}
	}
}
