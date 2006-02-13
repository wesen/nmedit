package org.nomad.theme.curve;

import java.awt.Point;

import org.nomad.util.graphics.Segment;
import org.nomad.util.graphics.Segments;


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

public class CurveSegment<T extends Curve> extends Segment<T> {

	//public final static int DEFAULT_SEGMENT_SIZE = 20;

	public CurveSegment(Segments<T> segments) {
		super(segments);
	}

	public CurveSegment(Segments<T> segments, Point offset) {
		super(segments, offset);
	}
	
	public CurveSegment(Segments<T> segments, int offsetX, int offsetY) {
		super(segments, offsetX, offsetY);
	}

}
