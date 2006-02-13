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
 * Created on Feb 3, 2006
 */
package org.nomad.theme.curve;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;

import javax.swing.JComponent;

import org.nomad.util.graphics.Segment;
import org.nomad.util.graphics.Segments;

public class CurveSegments extends Segments<Curve> {
	
	//public final static int PATH_SEGMENT_SIZE = CurveSegment.DEFAULT_SEGMENT_SIZE/2;
	private CurvePainter painter;	

	public CurveSegments(JComponent component, CurvePainter painter) {
		super(component);
		this.painter = painter;
	}
	
	public CurvePainter getPainter() {
		return painter;
	}

	protected Segment<Curve> newSegment(int xOffset, int yOffset) {
		return new CurveSegment<Curve>(this, xOffset, yOffset);
	}

	public void render(Graphics2D g2, Segment<Curve> segment) {
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		for (Curve curve : segment)
			getPainter().paint(g2, curve);
	}

	protected Segment<Curve>[] newArray(int size) {
		return new CurveSegment[size];
	}

	public void addToSegments(ArrayList<? extends Curve> curves) {
		for (Curve curve : curves)
			addToSegments(curve);
	}

	public void removeFromSegments(ArrayList<? extends Curve> curves) {
		for (Curve curve : curves)
			removeFromSegments(curve);
	}

	public void addToSegments(Curve curve) {
		addToSegments(curve, curve);
	}
	
	public void removeFromSegments(Curve curve) {
		removeFromSegments(curve, curve);
	}

	public void addDirtyRegion(Curve curve) {
		addDirtyRegion(getPathIterator(curve));
	}

}
