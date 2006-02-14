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
package org.nomad.util.array;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Area;

import org.nomad.util.iterate.ConditionF;
import org.nomad.util.iterate.ConditionMask;
import org.nomad.util.misc.NomadUtilities;

/**
 * A class that describes a path through an one dimensional array that is
 * interpreted as a two dimensional array.
 *
 * Custom path implementations have to override one of the <code>getSuccessor()</code> methods.
 * In the base implementation each of the two methods calls the other one. 
 * 
 * The caller always has to pass <code>getSuccessor()</code> valid index (or x,y coordinates).
 * The <code>getSuccessor()</code> method will return either the successor for the given index
 * or <code>-1</code> if no further successor exists.
 * 
 * The parameter <code>xdim</code> always refers to the width of the array.
 * The parameter <code>ydim</code> always refers to the height of the array.
 * The parameter <code>xydim</code> refers to the size of the array. It
 * always fullfills the conditions <code>xydim==xdim*ydim</code> and <code>xydim==length(array)</code>.
 * 
 * @author Christian Schneider
 */
public abstract class Path2D {
	
	public int getSuccessor(int index, int xdim, int ydim, int xydim) {
		return getSuccessor(Array2D.getX(index, xdim), Array2D.getY(index, xdim), xdim, ydim, xydim);
	}

	public int getSuccessor(int x, int y, int xdim, int ydim, int xydim) {
		return getSuccessor(Array2D.getIndex(x, y, xdim, ydim), xdim, ydim, xydim);
	}
	
	public int getStartIndex(int xdim, int ydim, int xydim) {
		return 0;
	}
	
	/**
	 * A class that assures that the successor of the given path always points to a cell thats value fullfills the given condition 
	 */
	public static class ConditionalF<T> extends Path2D {
		private T[] array2d;
		private Path2D path;
		private ConditionF<T> condition;

		public ConditionalF(T[] array2d, Path2D path, ConditionF<T> condition) {
			this.array2d = array2d;
			this.path = path;
			this.condition = condition;
		}
		
		private int align(int index, int xdim, int ydim, int xydim) {
			while (index>=0 && !condition.isTrue(array2d[index])) {
				index = path.getSuccessor(index, xdim, ydim, xydim);
			}
			return index;
		}
		
		public int getStartIndex(int xdim, int ydim, int xydim) {
			return align( path.getStartIndex(xdim, ydim, xydim), xdim, ydim, xydim );
		}
		
		public int getSuccessor(int index, int xdim, int ydim, int xydim) {
			return align( path.getSuccessor(index, xdim, ydim, xydim), xdim, ydim, xydim );
		}
	}
	
	/**
	 * A class that assures that the successor of the given path always points to a cell in the array that is not null. 
	 */
	public static class MaskedPath extends Path2D {
		private Path2D path;
		private ConditionMask condition;

		public MaskedPath(Path2D path, ConditionMask condition) {
			this.path = path;
			this.condition = condition;
		}
		
		private int align(int index, int xdim, int ydim, int xydim) {
			while (index>=0 && !condition.isTrue(Array2D.getX(index, xdim), Array2D.getY(index, xdim), xdim, ydim)) {
				index = path.getSuccessor(index, xdim, ydim, xydim);
			}
			return index;
		}
		
		public int getStartIndex(int xdim, int ydim, int xydim) {
			return align( path.getStartIndex(xdim, ydim, xydim), xdim, ydim, xydim );
		}
		
		public int getSuccessor(int index, int xdim, int ydim, int xydim) {
			return align( path.getSuccessor(index, xdim, ydim, xydim), xdim, ydim, xydim );
		}
	}

	/**
	 * Scans each row from top to bottom, left to right.
	 */
	public static class Rows extends Path2D {
		public int getSuccessor(int index, int xdim, int ydim, int xydim) {
			return Path2D.rowscan(index, xdim, ydim, xydim);
		}
	}
	
	/** 
	 * Scans the given range of rows from top to bottom, left to right.
	 */
	public static class RowRange extends Path2D {
		private int start;
		private int stop;

		public RowRange(int startRow, int stopRow) {
			this.start = startRow;
			this.stop = stopRow;
		}
		
		public int getSuccessor(int index, int xdim, int ydim, int xydim) {
			return Path2D.rowrangescan(index, xdim, ydim, xydim, start, stop);
		}
	}

	/**
	 * Scans each column from left to right, top to bottom.
	 */
	public static class Cols extends Path2D {
		public int getSuccessor(int index, int xdim, int ydim, int xydim) {
			return Path2D.colscan(index, xydim);
		}
	}
	
	/** 
	 * Scans the given range of column from left to right, top to bottom.
	 */
	public static class ColRange extends Path2D {
		private int start;
		private int stop;

		public ColRange(int startCol, int stopCol) {
			this.start = startCol;
			this.stop = stopCol;
		}
		
		public int getSuccessor(int index, int xdim, int ydim, int xydim) {
			return Path2D.colrangescan(index, xdim, ydim, xydim, start, stop);
		}
	}
	
	/**
	 * Scans the inverse path.
	 */
	public static class Inverse extends Path2D {
		private Path2D path;
		public Inverse(Path2D path) {
			this.path = path;
		}
		
		public int getStartIndex(int xdim, int ydim, int xydim) {
			return (xydim-1)-path.getStartIndex(xdim, ydim, xydim);
		}
		
		public int getSuccessor(int index, int xdim, int ydim, int xydim) {
			return Path2D.inverse(index, xdim, ydim, xydim, path);
		}
	}
	
	/**
	 * Scans a snail shell like path.
	 * Starting from top left, scans counterclockwise (down, right, up, restart(newline))+. 
	 */
	public static class SnailShell extends Path2D {
		private int quadsize ;
		
		public SnailShell(int xdim, int ydim) {
			quadsize = Math.min(xdim, ydim);
			quadsize*= quadsize;
		}

		public int getSuccessor(int x, int y, int xdim, int ydim, int xydim) {
			return Path2D.snailshell(x, y, xdim, ydim, xydim, quadsize);
		}
	}
	
	/**
	 * Scans through the cells inside a rectangle.
	 */
	public static class Rectangular extends Path2D {
	
		private int dx;
		private int dy;
		private int dr;
		private int db;

		public Rectangular(Rectangle bounds, int xdim, int ydim) {
			this(bounds.x, bounds.y, bounds.width, bounds.height, xdim, ydim);
		}
		
		public Rectangular(int bx, int by, int bw, int bh, int xdim, int ydim) {
			this.dx = Math.max(0, bx);
			this.dy = Math.max(0, by);
			this.dr = Math.max(0, Math.min(this.dx+bw, xdim)-1);
			this.db = Math.max(0, Math.min(this.dy+bh, ydim)-1);
		}

		public int getStartIndex(int xdim, int ydim, int xydim) {
			return (dx>=xdim || dy>=ydim) ? -1 : Array2D.getIndex(dx, dy, xdim, ydim);
		}
		
		public int getSuccessor(int x, int y, int xdim, int ydim, int xydim) {
			if (++x<=dr) return Array2D.getIndex(x, y, xdim, ydim);
			// x>dr
			x = dx;
			if (++y<=db) return Array2D.getIndex(x, y, xdim, ydim);
			// y>db => done
			return -1;
		}
		
	}

	public static Path2D createShapePath(Shape shape, int xdim, int ydim, int rsize) {
		return createShapePath(shape, xdim, ydim, rsize, rsize);
	}

	public static Path2D createShapePath(Shape shape, int xdim, int ydim, int rw, int rh) {
		Rectangle bounds = shape.getBounds();
		NomadUtilities.scale(bounds, 1.0d/(double)rw, 1.0d/(double)rh);
		NomadUtilities.enlarge(bounds, 1);

		// now use mask on box
		return new MaskedPath(
			new Rectangular(bounds, xdim, ydim), 
			new AreaMask(new Area(shape), rw, rh)
		);
	}

	private static class AreaMask implements ConditionMask {
		private Area area;
		private int sx;
		private int sy;
		private int thresholdx;
		private int thresholdy;
		private int thresholdw;
		private int thresholdh;

		public AreaMask(Area area, int sx, int sy) {
			this.area = area;
			this.sx = sx;
			this.sy = sy;

			this.thresholdx = sx/2;
			this.thresholdy = sy/2;

			this.thresholdw = sx+(sx/2); // *2
			this.thresholdh = sy+(sy/2); // *2
		}

		public boolean isTrue(int x, int y, int xdim, int ydim) {
			return area.intersects((x*sx)-thresholdx, (y*sy)-thresholdy, thresholdw, thresholdh);
		}
	}
	
	/*
	public static Path2D createShapePath(Shape shape, int xdim, int ydim, int rw, int rh, int thresholdx, int thresholdy) {
		double sx = 1.0d/rw;
		double sy = 1.0d/rh;
		
		Rectangle bounds = shape.getBounds();
		NomadUtilities.scale(bounds, sx, sy);
		NomadUtilities.enlarge(bounds, 1);
				
		// use box path
		Rectangular boxPath = new Rectangular(bounds, xdim, ydim);

		// now use conditional path
		//Conditional
		Area area = new Area(shape);
		area.transform(AffineTransform.getScaleInstance(sx, sy));
		
		ConditionMask mask = new AreaMask(area, rw, rh, ((double)thresholdx)*sx, ((double)thresholdy)*sy);
		
		// now use mask on box
		MaskedPath maskedPath = new MaskedPath(boxPath, mask);
		
		return maskedPath;
	}

	private static class AreaMask implements ConditionMask {
		private Area area;
		private double offsetx;
		private double offsety;
		private double thresholdw;
		private double thresholdh;

		public AreaMask(Area area, double thresholdx, double thresholdy) {
			this.area = area;
			this.offsetx = -thresholdx-0.5d;
			this.offsety = -thresholdy-0.5d;
			this.thresholdw = (2.0d*thresholdx);
			this.thresholdh = (2.0d*thresholdy);
		}

		public boolean isTrue(int x, int y, int xdim, int ydim) {
			return area.intersects(((double)x)+offsetx, ((double)y)+offsety, thresholdw, thresholdh);
		}
	}
*/
	/**
     * Scans from top to bottom, left to right.
	 */
	public static int rowscan(int index, int xdim, int ydim, int xydim) {
		index+=xdim; 		// next line 
		if (index>=xydim) {
			index=(index%xydim)+1; // restart with offset+1
			if (index>=xdim) {
				return -1;  // out of bounds => done
			}
		}
		return index;
	}

	/**
     * Scans from top to bottom, left to right.
     * Starting from startRow until stopRow (inclusive)
	 */
	public static int rowrangescan(int index, int xdim, int ydim, int xydim, int startRow, int stopRow) {
		int s = rowscan(index, xdim, ydim, xydim);
		if (s>=0) {
			if (Array2D.getY(s, xdim)>stopRow) {
				// newcol
				s = Array2D.getIndex(Array2D.getX(index, xdim)+1, startRow, xdim, ydim);
				if (s>=xydim) return -1;
			}
		}
		return s;
	}

	/**
	 * Scans from left to right, top to bottom.
	 */
	public static int colscan(int index, int xydim) {
		return (++index>=xydim) ? -1 : index;
	}
	
	public static int colrangescan(int index, int xdim, int ydim, int xydim, int startCol, int stopCol) {
		int s = colscan(index, xydim);
		if (s>=0) {
			if (Array2D.getX(s, xdim)>stopCol) {
				// newline
				s = Array2D.getIndex(startCol, Array2D.getY(index, xdim)+1, xdim, ydim);
				if (s>=xydim) return -1;
			};
		}
		return s;
	}

	
	public static int inverse(int index, int xdim, int ydim, int xydim, Path2D path) {
		int s = path.getSuccessor((xydim-1)-index, xdim, ydim, xydim);
		return s < 0 ? -1 : (xydim-1)-s;
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param xdim
	 * @param ydim
	 * @param xydim
	 * @param quadsize quadsize = Math.min(xdim,ydim)^2
	 * @return
	 */
	public static int snailshell(int x, int y, int xdim, int ydim, int xydim, int quadsize) {
		int index ;
		if (y==0) {
			index = Array2D.getIndex(0, x+1, xdim, ydim);
		} else if (x<y) {
			index = Array2D.getIndex(x+1, y, xdim, ydim);
		} else { // x==y || x>y
			index = Array2D.getIndex(x, y-1, xdim, ydim);
		}
		
		if (index>quadsize) {		
			// handle irregular case
			if (xdim>ydim) {
				int sy = Array2D.getY(index, xdim);
				if (sy>=ydim) 
					index = Array2D.getIndex(sy, ydim-1, xdim, ydim);
			} else { // xdim<ydim
				index = Array2D.getIndex(x, y, xdim, ydim)+1;
			}
			if (index>=xydim) return -1;
		}
		return index;
	}
	
}
