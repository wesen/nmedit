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
 * Created on Feb 24, 2006
 */
package org.nomad.util.shape;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.JComponent;
import javax.swing.RepaintManager;

import org.nomad.util.graphics.Repainter;

public abstract class ShapeDisplay<T extends Shape> implements Repainter {

	private RepaintManager repaintManager;
	private ShapeRenderer<T> shapeRenderer;
	private ShapeRenderer<T> directRenderer;
	private ShapeRenderInfo renderInfo = new ShapeRenderInfo();
	private ShapeRenderManager<T> renderManager ;
	private JComponent component;
	
	public ShapeDisplay(JComponent component) {
		this.component = component;
		repaintManager = RepaintManager.currentManager(component);
		shapeRenderer = new Renderer<T>(this);
		directRenderer = new DirectRenderer<T>(this);
		renderManager = new ShapeRenderManager<T>(renderInfo, this, shapeRenderer, directRenderer);
	}
	
	public void paint(Graphics g) {
		renderManager.paint(g);
	}
	
	public void paintDirect(Graphics g) {
		renderManager.paintDirect(g);
	}

	public JComponent getComponent() {
		return component;
	}
	
	public ShapeDraggingTool<T> newShapeDraggingTool(T shape) {
		ArrayList<T> collection = new ArrayList<T>(1);
		collection.add(shape);
		return ShapeDraggingTool.<T>newTool(this, collection);
	}

	public ShapeDraggingTool<T> newShapeDraggingTool(Collection<T> shapes) {
		return ShapeDraggingTool.<T>newTool(this, shapes);
	}
	
	public void clear() {
		renderManager.clear();
	}
	
	public void beginUpdate() {
		renderManager.beginUpdate();
	}
	
	public void endUpdate() {
		renderManager.endUpdate();
	}
	
	public void add(T t) {
		renderManager.add(t);
	}

	public void add(T t, boolean directRendering) {
		if (directRendering)
			renderManager.addDirectRendered(t);
		else
			renderManager.add(t);
	}
	
	public boolean isDirectRendered(T t) {
		return renderManager.containsDirectRenderedShape(t);
	}

	public void remove(T t) {
		if (isDirectRendered(t))
			renderManager.removeDirectRendered(t);
		else
			renderManager.remove(t);
	}
	
	public void transformDirectRendering() {
		renderManager.transformDirectRendering();
	}
	
	public void setDirectRenderingEnabled(T t, boolean enable) {
		if (enable) {
			// check if shape exists - it would be added accidently otherwise
			if (renderManager.containsShape(t)) {
				renderManager.beginUpdate();
				renderManager.remove(t);
				renderManager.addDirectRendered(t);
				renderManager.endUpdate();
			}
		} else {
			if (renderManager.containsDirectRenderedShape(t)) {
				renderManager.beginUpdate();
				renderManager.add(t);
				renderManager.removeDirectRendered(t);
				renderManager.endUpdate();
			}
		}
	}
	
	public void update(T t) {
		renderManager.update(t);
	}
	
	public boolean hasDirectRendered() {
		return renderManager.hasDirectRendered();
	}
	
	public Iterator<T> getDirectRendered() {
		return renderManager.getDirectRendered();
	}
	
	public Iterator<T> getBuffered() {
		return renderManager.getBuffered();
	}
	
	public void addDirtyRegion(int x, int y, int w, int h) {
		repaintManager.addDirtyRegion(getComponent(), x, y, w, h);
	}
	
	private abstract static class ShapeDisplayRenderer<T extends Shape> implements ShapeRenderer<T> {
		ShapeDisplay<T> disp ;
		public ShapeDisplayRenderer(ShapeDisplay<T> disp) {
			this.disp = disp;
		}
	}
	
	private static class Renderer<T extends Shape> extends ShapeDisplayRenderer<T> {
		public Renderer(ShapeDisplay<T> disp) { super(disp); }
		public void paint(Graphics2D g2, T shape) {
			disp.renderShape(g2, shape);
		}
	}
	
	private static class DirectRenderer<T extends Shape> extends ShapeDisplayRenderer<T> {
		public DirectRenderer(ShapeDisplay<T> disp) { super(disp); }
		public void paint(Graphics2D g2, T shape) {
			disp.renderShapeDirect(g2, shape);
		}
	}
	
	protected abstract void renderShape(Graphics2D g2, T shape);
	protected abstract void renderShapeDirect(Graphics2D g2, T shape);
	
}
