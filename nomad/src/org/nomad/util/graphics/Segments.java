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
 * Created on Feb 9, 2006
 */
package org.nomad.util.graphics;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;

import javax.swing.JComponent;
import javax.swing.RepaintManager;

import org.nomad.util.array.Array2D;
import org.nomad.util.array.CellIterator;
import org.nomad.util.array.Path2D;
import org.nomad.util.array.Path2DIterator;
import org.nomad.util.misc.NomadUtilities;

public abstract class Segments<T> extends Array2D<Segment<T>> {

	private JComponent component = null;
	
	public final static int SEGMENT_SIZE = 50; // pixel

	public Segments(JComponent component) {
		this.component = component;
	}
	
	public JComponent getComponent() {
		return component;
	}
	
	public int getSegmentSize() {
		return Segments.SEGMENT_SIZE;
	}

	protected void addDirtyRegion(RepaintManager m, int ix, int iy) {
		m.addDirtyRegion(getComponent(), ix*getSegmentSize(), iy*getSegmentSize(), getSegmentSize(), getSegmentSize());
	}

	protected void addDirtyRegion(RepaintManager m, Segment<T> segment) {
		m.addDirtyRegion(getComponent(), segment.getOffsetX(), segment.getOffsetY(), getSegmentSize(), getSegmentSize());
	}

	protected void addDirtyRegion(RepaintManager m, Path2DIterator iterator) {
		while (iterator.hasNext()) {
			iterator.nextCell();
			addDirtyRegion(m, iterator.getCurrentX(), iterator.getCurrentY());
		}
	}

	protected void addDirtyRegion(int ix, int iy) {
		addDirtyRegion(RepaintManager.currentManager(getComponent()), ix, iy); 
	}

	protected void addDirtyRegion(Segment<T> segment) {
		addDirtyRegion(RepaintManager.currentManager(getComponent()), segment); 
	}
	
	protected void addDirtyRegion(Path2DIterator iterator) {
		addDirtyRegion(RepaintManager.currentManager(getComponent()), iterator); 
	}
	
	protected Path2DIterator getPathIterator(Shape shape) {
		return new Path2DIterator(getWidth(), getHeight(), getPath(shape));
	}
	
	protected Path2D getPath(Shape shape) {
		return getPath(shape, getWidth(), getHeight());
	}
	
	public Path2D getPath(Shape shape, int xdim, int ydim) {
		return Path2D.createShapePath(shape, xdim, ydim, getSegmentSize());
	}
	
	public void addToSegments(T item, Shape itemShape) {
		Rectangle bounds = itemShape.getBounds();
		int neww = Math.max(getWidth(),  1+ (bounds.x+bounds.width)/getSegmentSize());
		int newh = Math.max(getHeight(), 1+ (bounds.y+bounds.height)/getSegmentSize());
		resize(neww, newh); // assure that segments exist

		for (CellIterator<Segment<T>> iter=new CellIterator<Segment<T>>(this, getPath(itemShape));iter.hasNext();) {
			Segment<T> segment = iter.next();
			if (!segment.contains(item))
				segment.add(item);
		}
	}
	
	public void removeFromSegments(T item, Shape itemShape) {
		Path2DIterator path = getPathIterator(itemShape);
		while (path.hasNext()) {
			path.nextCell();
			if (hasCell(path.getCurrentIndex())) {
				getCell(path.getCurrentIndex()).remove(item);
			}
		}
	}
	
	private CellIterator<Segment<T>> clip(Graphics2D g2) {
		Rectangle clip = g2.getClipBounds();		
		if (clip == null) return getColScanPath();
		
		NomadUtilities.scale(clip, 1.0d/getSegmentSize());
		NomadUtilities.enlarge(clip, 1);
		
		return new CellIterator<Segment<T>>(this, new Path2D.Rectangular(clip, getWidth(), getHeight()));
	}
	
	public void paint(Graphics2D g2) {
		for (CellIterator<Segment<T>> iter = clip(g2); iter.hasNext();) {
			iter.next().paint(g2);
		}
	}

	public void modified(final Segment<T> segment) {
		addDirtyRegion(segment);  // update region
	}

	protected Segment<T> newCell(int x, int y) {
		return newSegment(x*SEGMENT_SIZE, y*SEGMENT_SIZE); 
	}

	protected abstract Segment<T> newSegment(int xOffset, int yOffset);

	public abstract void render(Graphics2D g2, Segment<T> segment);

}
