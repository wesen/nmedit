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
 * Created on Jan 11, 2006
 */
package org.nomad.theme.component.model;

import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.nomad.theme.component.KnobToolkit;
import org.nomad.theme.component.NomadControl;

public class NomadClassicKnobBehaviour {

	private NomadControl component = null;
	public Metrics metrics = new Metrics();
	
	public NomadClassicKnobBehaviour(NomadControl component) {
		super();
		this.component = component;
		metrics.update();
		setup();
	}

	protected void setup() {
		component.
		getAccessibleProperties().byName("size").addChangeListener(
				new ChangeListener(){
					public void stateChanged(ChangeEvent event) {
						metrics.update();
					}}		
		);

		component.
		addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent event) {
				component.
				requestFocus();
			}});

		component.
		addMouseMotionListener(new MouseMotionAdapter(){
			public void mouseDragged(MouseEvent event) {
				if (SwingUtilities.isLeftMouseButton(event)) {
					int newValue = component.getMinValue() + 
						(int) (metrics.calcRangeFactor(
							event.getPoint())*component.getRange());
					component.setValue(newValue);
				}
			}});

		component.
		addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent event) {
				boolean ctrl_pressed = (event.getModifiers() & (KeyEvent.CTRL_DOWN_MASK|KeyEvent.CTRL_MASK))!=0;
				switch(event.getKeyCode()) {
					case KeyEvent.VK_UP: 
						if (ctrl_pressed) component.incMorph(); else component.incValue(); break;
					case KeyEvent.VK_DOWN:
						if (ctrl_pressed) component.decMorph(); else component.decValue(); break;
					case KeyEvent.VK_DELETE:
						if (ctrl_pressed) component.setMorphValue(null); break;
				}
			}});
	}
	

	public class Metrics {
		
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
		
		public Metrics() {
			update();
		}

		public void update(){
			
			if (component==null)
				return;
			
			if (component.getWidth()==w && component.getHeight()==h)
				return;
			
			w=component.getWidth();
			h=component.getHeight();
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
		
		public Arc2D getMorphArc() {
			Double morph = component.getMorphValue();
			if (morph==null)
				return null;

			double arcStartAngle = component.getValuePercentage()*270.0d; // [0..270]
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
	
	
}
