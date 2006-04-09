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

import java.awt.Shape;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class ShapeDraggingTool<T extends Shape> {

	private ShapeDisplay<T> display;
	private Collection<T> shapes;

	protected ShapeDraggingTool(ShapeDisplay<T> display, Collection<? extends T> shapes) {
		this.display = display;
		this.shapes = new ArrayList<T>(shapes);
	}

	public static <T extends Shape> ShapeDraggingTool<T> newTool(ShapeDisplay<T> display, Collection<? extends T> shapes){
		ShapeDraggingTool<T> tool = new ShapeDraggingTool<T>(display, shapes);
		tool.transferShapes();
		return tool;
	}

	protected void transferShapes() {
		display.beginUpdate();
		try {
			for (T t : shapes) {
				if (!display.isDirectRendered(t)) {
					display.remove(t);
					display.add(t, true); // add in direct rendering mode
				}
			}
		} catch (RuntimeException r) {
			display.endUpdate();
			throw r;
		}
		display.endUpdate();
	}

	public ShapeDisplay<T> getDisplay() {
		return display;
	}

	public Iterator<T> shapes() {
		return shapes.iterator();
	}

	// stores all curves 
	public void remainAll() {
		display.beginUpdate();
		try {
			for (T t : shapes) {
				display.setDirectRenderingEnabled(t, false);
			}
		} catch (RuntimeException r) {
			display.endUpdate();
			throw r;
		}
		display.endUpdate();
	}
	
	// removes all curves
	public void removeAll() {
		display.beginUpdate();
		try {
			for (T t : shapes) {
				display.remove(t);
			}
		} catch (RuntimeException r) {
			display.endUpdate();
			throw r;
		}
		display.endUpdate();
	}

	public void updateShapes() {
		display.beginUpdate();
		for (T t : shapes) {
			display.update(t);
		}
		display.endUpdate();
	}

}
