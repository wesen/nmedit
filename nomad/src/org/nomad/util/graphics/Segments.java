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

import org.nomad.util.array.Array2D;
import org.nomad.util.array.Path2D;
import org.nomad.util.array.Path2DIterator;
import org.nomad.util.array.Path2D.Rectangular;
import org.nomad.util.misc.NomadUtilities;

public abstract class Segments<T> extends Array2D<Segment<T>> {

	private JComponent component = null;
	private Repainter m ;
	private boolean updating = false;
	
	public final static int SEGMENT_SIZE = 64; // pixel

	public Segments(JComponent component, Repainter repainter) {
		this.component = component;
		m = repainter;
	}
	
	public JComponent getComponent() {
		return component;
	}
	
	public int getSegmentSize() {
		return Segments.SEGMENT_SIZE;
	}
	
	public void setUpdatingEnabled(boolean enable) {
		updating = enable;
	}
	
	public boolean isUpdatingEnabled() {
		return updating;
	}

	protected void addDirtyRegion(int ix, int iy) {
		if (!updating)
			m.addDirtyRegion(ix*getSegmentSize(), iy*getSegmentSize(), getSegmentSize(), getSegmentSize());
	}

	protected void addDirtyRegion(Segment<T> segment) {
		if (!updating)
			m.addDirtyRegion(segment.getOffsetX(), segment.getOffsetY(), getSegmentSize(), getSegmentSize());
	}

	protected void addDirtyRegion(Path2DIterator iterator) {
		while (iterator.hasNext()) {
			iterator.nextCell();
			addDirtyRegion(iterator.getCurrentX(), iterator.getCurrentY());
		}
	}

	protected Path2DIterator getPathIterator(Shape shape) {
		return new Path2DIterator(getWidth(), getHeight(), 
			Path2D.createShapePath(shape, getWidth(), getHeight(), getSegmentSize())
		);
	}
	
	protected Path2D getPath(Shape shape) {
		return Path2D.createShapePath(shape, getWidth(), getHeight(), getSegmentSize());
	}
	
	public Path2D getPath(Shape shape, int xdim, int ydim) {
		return Path2D.createShapePath(shape, xdim, ydim, getSegmentSize());
	}

	public void addToSegments(T item, Shape itemShape) {
		Rectangle bounds = itemShape.getBounds();
		int neww = Math.max(getWidth(), 1+ (bounds.x+bounds.width)/getSegmentSize());
		int newh = Math.max(getHeight(),1+ (bounds.y+bounds.height)/getSegmentSize());
		resize(neww, newh); // assure that segments exist


		Path2DIterator path = getPathIterator(itemShape);
		while (path.hasNext()) {
			path.nextCell();
			if (hasCell(path.getCurrentIndex())) {
				Segment<T> segment = getCell(path.getCurrentIndex());
				if (!segment.contains(item))
					segment.add(item);
			}
		}
	}
	
	public void removeFromSegments(T item, Shape itemShape) {
		Rectangle bounds = itemShape.getBounds();
		NomadUtilities.scale(bounds, 1.0d/getSegmentSize());
		NomadUtilities.enlarge(bounds, 1);
		
		Path2DIterator path = new Path2DIterator(getWidth(), getHeight(), new Rectangular(bounds, getWidth(), getHeight()));
		
		/*
		Path2DIterator path = getPathIterator(itemShape);
		*/
		while (path.hasNext()) {
			path.nextCell();
			if (hasCell(path.getCurrentIndex())) {
				getCell(path.getCurrentIndex()).remove(item);
			}
		}
	}
	
	public void paint(Graphics2D g2) {
		Rectangle clip = g2.getClipBounds();		
		if (clip != null) {
			// paint clip region
			NomadUtilities.scale(clip, 1.0d/getSegmentSize());
			NomadUtilities.enlarge(clip, 1);

			clip.x = Math.max(0, clip.x);
			clip.y = Math.max(0, clip.y);
			int rlimit = Math.min(clip.x+clip.width , getWidth());
			int blimit = Math.min(clip.y+clip.height, getHeight());
			if (clip.x>0||clip.y>0||rlimit<getWidth()||blimit<getHeight())
			{
				// now paint the rect (top-left:(clip.x, clip.y), bottom-right:(rlimit, blimit))

				int line 		= Array2D.getIndex(0, clip.y, getWidth(), getHeight());
				int lineLimit 	= Array2D.getIndex(0, blimit, getWidth(), getHeight());
				int offset 		= Array2D.getIndex(clip.x, 0, getWidth(), getHeight());
				int offsetLimit = Array2D.getIndex(rlimit, 0, getWidth(), getHeight());
				int index ;
				int limit ;
				
				while (line<lineLimit) {
					index = line+offset;
					limit = line+offsetLimit;
					while (index<limit)
						getCell(index++).paint(g2);
					
					line += getWidth();
				}
				
				/* The above is same as this simple loop, but less costly  
				 
				for (int y=clip.y;y<blimit;y++)
					for (int x=clip.x;x<rlimit;x++)
						getCell(x,y).paint(g2);
				*/
				
				// we are done
				return ;
				
			} // else we have to repaint the whole component
		}
		
		// paint all
		for (int i=0;i<getSize();i++)
			getCell(i).paint(g2);
	}
	
/*
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
*/
	public void modified(final Segment<T> segment) {
		addDirtyRegion(segment);  // update region
	}

	protected Segment<T> newCell(int x, int y) {
		return newSegment(x*SEGMENT_SIZE, y*SEGMENT_SIZE); 
	}

	protected abstract Segment<T> newSegment(int xOffset, int yOffset);

	public abstract void render(Graphics2D g2, Segment<T> segment);

}
