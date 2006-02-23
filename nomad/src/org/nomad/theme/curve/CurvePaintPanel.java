package org.nomad.theme.curve;

import java.awt.Container;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.RepaintManager;

import org.nomad.util.graphics.Repainter;
import org.nomad.util.graphics.Segments;
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

public class CurvePaintPanel extends JComponent implements Repainter {

	private ArrayList<Curve> curveList = new ArrayList<Curve>();
	private ArrayList<Curve> unmanagedCurveList = new ArrayList<Curve>();
//	private Curve draggedCurve = null;

	private CurvePainter painter = new CurvePainter();
	private CurveSegments segments = new CurveSegments(this, this, painter);
	private UnmanagedCurveModification ucModification = new UnmanagedCurveModification();
	private OwnedCurveModification ocModification = new OwnedCurveModification();
	private Curve draggedCurve = null;
	private final static boolean unmanagedHaveShadow = true;
	private RepaintManager repaintManager;
	private Container owner ;
	private boolean updating = false;
	
	public CurvePaintPanel(Container owner) {
		setDoubleBuffered(false);
		this.owner = owner;
		setSize(owner.getSize());
		owner.addComponentListener(new ComponentAdapter(){
			public void componentResized(ComponentEvent event) {
				setSize(event.getComponent().getSize());
			}});
		setOpaque(false);
		repaintManager = RepaintManager.currentManager(this);
	}
	
	protected void clearCurves() {
		resetUnmanaged();
		while (curveList.size()>0)
			removeCurve(curveList.get(0));
	}
	
	public Container getOwner() {
		return owner;
	}
	
	protected void resetUnmanaged() {		
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
			curve.setCallback(ucModification);
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
	
	private Cursor previousCursor = null;
	
	public void setDraggedCurve(Curve curve) {
		if (draggedCurve != null) {
			draggedCurve.setCallback(null); // remove callback
			Curve c = draggedCurve;
			draggedCurve = null;
			addDirtyRegion(c);
			
			if (curve==null && previousCursor!=null) {
				setCursor(previousCursor);
				previousCursor = null;
			}
		}
		
		this.draggedCurve = curve;
		
		if (draggedCurve != null) {
			if (previousCursor==null) {
				previousCursor = getCursor();
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
			
			draggedCurve.setCallback(ucModification);
			addDirtyRegion(draggedCurve);
		} 
	}
	
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		segments.paint(g2);

		if (!unmanagedCurveList.isEmpty())
			for (Curve curve : unmanagedCurveList)
				painter.paint(g2, curve);
		
		if (draggedCurve != null) 
			painter.paint(g2, draggedCurve, unmanagedHaveShadow);
	}
	
	public void setUpdatingEnabled(boolean enable) {
		updating = enable;
		segments.setUpdatingEnabled(enable);
		if (updating && !updateRegion.isEmpty()) {
			forceDirtyRegion(updateRegion);
			updateRegion.setBounds(0,0,0,0);
		}
	}
	
	public boolean isUpdatingEnabled() {
		return updating;
	}

	private final static int curve_bounds_enlargement=4;
	
	protected void addDirtyRegion(Curve curve) {
			// use bounds rather than path because repaintmanager builds always one clip rect
			// a more accurate resulution would only bring unecessary overhead
			Rectangle dirty = curve.getBounds();
			NomadUtilities.enlarge(dirty, curve_bounds_enlargement);
			addDirtyRegion(dirty);
	}
	
	private Rectangle updateRegion = new Rectangle(0,0,0,0);
	
	public void addDirtyRegion(int x, int y, int w, int h) {
		addDirtyRegion(new Rectangle(x, y, w, h));
	}
	
	protected void addDirtyRegion(Rectangle dirty) {
		if (updating) {
			updateRegion = updateRegion.union(dirty);
		} else {
			forceDirtyRegion(dirty);
		}
	}
	
	private void forceDirtyRegion(Rectangle dirty) {

		NomadUtilities.enlargeToGrid(dirty, Segments.SEGMENT_SIZE);
		
		repaintManager.addDirtyRegion(
				this,
				dirty.x, dirty.y,
				dirty.width,
				dirty.height
			);
	}
	
	private class UnmanagedCurveModification implements CurveEventCallback {
		
		Rectangle r = new Rectangle();
		
		public void beforeChange(Curve curve) 	{ r.setBounds(curve.getBounds()); } 
		public void afterChange(Curve curve) 	{ 
			r.union(curve.getBounds());
			NomadUtilities.enlarge(r, curve_bounds_enlargement);
			forceDirtyRegion(r);
		}
		
	}
	
	private class OwnedCurveModification implements CurveEventCallback {
		public void beforeChange(Curve curve)	{ segments.removeFromSegments(curve); }
		public void afterChange(Curve curve)	{ segments.addToSegments(curve); }
	}

	public CurvePainter getCurvePainter() {
		return painter;
	}
	
}
