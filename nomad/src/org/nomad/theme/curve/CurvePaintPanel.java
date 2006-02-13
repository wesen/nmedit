		package org.nomad.theme.curve;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

import javax.swing.JLayeredPane;
import javax.swing.RepaintManager;

import org.nomad.util.misc.NomadUtilities;

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
 * Created on Feb 1, 2006
 */

public class CurvePaintPanel extends JLayeredPane {

	private ArrayList<Curve> curveList = new ArrayList<Curve>();
	private ArrayList<Curve> unmanagedCurveList = new ArrayList<Curve>();
//	private Curve draggedCurve = null;

	private CurvePainter painter = new CurvePainter();
	final static boolean debug = false;
	private CurveSegments segments = new CurveSegments(this, painter);
	private UnmanagedCurveModification ucModification = new UnmanagedCurveModification();
	private OwnedCurveModification ocModification = new OwnedCurveModification();
	private Curve draggedCurve = null;
	private boolean renderUnmanaged = true;
	private boolean renderUnmanagedAsLines = false;
	private boolean unmanagedHaveShadow = true;
	
	private Container owner ;
	
	public CurvePaintPanel(Container owner) {
		//setDoubleBuffered(false);
		this.owner = owner;
		setSize(owner.getSize());
		owner.addComponentListener(new ComponentAdapter(){
			public void componentResized(ComponentEvent event) {
				setSize(event.getComponent().getSize());
			}});
		setOpaque(false);
	}
	
	public Container getOwner() {
		return owner;
	}
	
	public void resetUnmanaged() {		
		if (!unmanagedCurveList.isEmpty()) {
			for (Curve curve:unmanagedCurveList) {
				curve.setCallback(ocModification);
				segments.addToSegments(curve);
			}
			unmanagedCurveList.clear();
		}
	}
	
	protected ArrayList<? extends Curve> getUnmanaged() {
		return unmanagedCurveList;
	}
	
	protected void setUnmanaged(ArrayList<? extends Curve> curves) {
		resetUnmanaged();
		unmanagedCurveList = new ArrayList<Curve>(curves.size());		
		for (Curve curve:curves) {
			segments.removeFromSegments(curve);
			unmanagedCurveList.add(curve);
			curve.setCallback(renderUnmanaged ? ucModification : null);
		}
	}
	
	public Curve getDraggedCurve() {
		return draggedCurve;
	}

	public boolean hasCurve(Curve curve) {
		return curveList.contains(curve);
	}
	
	public void removeCurve(Curve curve) {
		curve.setCallback(null);
		curveList.remove(curve);
		segments.removeFromSegments(curve);
	}
	
	public void addCurve(Curve curve) {
		curveList.add(curve);
		curve.setCallback(ocModification);
		segments.addToSegments(curve);
	}
	
	public void setDraggedCurve(Curve curve) {
		if (draggedCurve != null) {
			draggedCurve.setCallback(null); // remove callback
			Curve c = draggedCurve;
			draggedCurve = null;
			//markDirty(c);
			addDirtyRegion(c);
		}
		
		this.draggedCurve = curve;
		
		if (draggedCurve != null) {
			draggedCurve.setCallback(ucModification);
			addDirtyRegion(draggedCurve);
		} 
	}
	
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		segments.paint(g2);

		//g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		if (renderUnmanaged) {
			if (renderUnmanagedAsLines) {
				for (Curve curve : unmanagedCurveList) {
					painter.line(g2, curve);
				}
			} else {
				for (Curve curve : unmanagedCurveList) {
					painter.paint(g2, curve, unmanagedHaveShadow);
				}
			}
		}
		
		if (draggedCurve != null) {
			painter.paint(g2, draggedCurve, unmanagedHaveShadow); 
			
			if (debug) {

				g2.setColor(Color.BLACK);
				paintPoint(g2, draggedCurve.getP1());
				paintPoint(g2, draggedCurve.getP2());
				g2.setColor(Color.BLUE);
				paintPoint(g2, draggedCurve.getCtrlP1());
				paintPoint(g2, draggedCurve.getCtrlP2());
			}
		}
	}

	private void paintPoint(Graphics2D g2, int x, int y) {
		g2.drawRect(x-5, y-5, 10,10);
	}
	
	private void paintPoint(Graphics2D g2, Point p) {
		paintPoint(g2, p.x, p.y);
	}
	
	protected void addDirtyRegion(Curve curve) {
		
		Rectangle dirty = curve.getBounds();
		NomadUtilities.enlarge(dirty, 4);
		
		RepaintManager
			.currentManager(this)
			.addDirtyRegion(
				this,
				dirty.x, dirty.y,
				dirty.width,
				dirty.height
			);
	}
	
	private class UnmanagedCurveModification implements CurveEventCallback {
		public void beforeChange(Curve curve) 	{ addDirtyRegion(curve); } 
		public void afterChange(Curve curve) 	{ addDirtyRegion(curve); }
	}
	
	private class OwnedCurveModification implements CurveEventCallback {
		public void beforeChange(Curve curve) {
			segments.removeFromSegments(curve);
		}

		public void afterChange(Curve curve) {
			segments.addToSegments(curve);
		}
	}

	public CurvePainter getCurvePainter() {
		return painter;
	}
	
}
