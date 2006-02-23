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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Segment<T> extends ArrayList<T> {

	public final static boolean debug = false;
	
	private int offsetX;
	private int offsetY;
	private Segments<T> segments;
	private BufferedImage image = null;
	private boolean shouldRender = false;
	public boolean dirty = false;
	
	public Segment(Segments<T> segments) {
		this(segments, 0, 0);
	}

	public Segment(Segments<T> segments, Point offset) {
		this(segments, offset.x, offset.y);
	}
	
	public Segment(Segments<T> segments, int offsetX, int offsetY) {
		this.segments = segments;
		setOffset(offsetX, offsetY);
	}
	
	public Segments<T> getSegments() {
		return segments;
	}
	
	public int getOffsetX() {
		return offsetX;
	}

	public int getOffsetY() {
		return offsetY;
	}

	public void setOffset(Point offset) {
		setOffset(offset.x, offset.y);
	}

	public void setOffset(int offsetX, int offsetY) {
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}

	public void add(int index, T element) {
		super.add(index, element);
		setModified();
	}

	public boolean add(T element) {
		boolean result = super.add(element);
		setModified();
		return result;
	}

	public T set(int index, T element) {
		T t = super.set(index, element);;
		setModified();
		return t;
	}

	public T remove(int index) {
		T t = super.remove(index);
		setModified();
		return t;
	}

	public boolean remove(Object element) {
		boolean removed = super.remove(element);
		if (removed)
			setModified();
		return removed;
	}

	protected void setModified() {
		if (hasPaintData()) {
			shouldRender = true;
			if (image==null) {
				image = ImageToolkit.createCompatibleBuffer(
					getSegments().getSegmentSize(), 
					getSegments().getSegmentSize(), Transparency.TRANSLUCENT
				);
			}
		} else {
			this.image = null;
		}
		getSegments().modified(this);
	}

	public boolean hasPaintData() {
		return size()>0;
	}

	public void render() {
		Graphics2D g2 = image.createGraphics();
		if (shouldRender) {
			ImageToolkit.clearRegion(g2, 0, 0, image.getWidth(), image.getHeight());
		}
		g2.translate(-offsetX, -offsetY);
		getSegments().render(g2, this);
		// g2.translate(offsetX, offsetY);
		g2.dispose();
		
		shouldRender = false;
	}

	public void paint(Graphics2D g2) {
		if (hasPaintData()) {
			if (shouldRender) render();

			g2.drawImage(image, offsetX, offsetY, null);
			//dirty = modified;

			if (debug) {
				g2.setColor(Color.blue);
				g2.drawRect(offsetX, offsetY, image.getWidth(), image.getHeight());
			}
		} 
	}
	
}
