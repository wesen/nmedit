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
package org.nomad.util.array;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Iterator;

import org.nomad.util.iterate.NotNullCondition;

public abstract class Array2D<T> implements Iterable<T> {

	private int xdim = 0;
	private int ydim = 0;
	private int size = 0;
	private T[] data = newArray(0);
	
	public Array2D() {
		super();
	}
	
	public T[] getArray() {
		return data;
	}

	protected abstract T[] newArray(int size);
	
	protected T newCell(int x, int y) {
		return null;
	}
	
	protected T newCell(int index) {
		return newCell(Array2D.getX(index, xdim), Array2D.getY(index, xdim));
	}
	
	protected int getIndex(int x, int y) {
		return Array2D.getIndex(x, y, xdim, ydim);
	}

	public int getWidth() {
		return xdim;
	}
	
	public int getHeight() {
		return ydim;
	}
	
	public T getCell(int index) {
		return data[index];
	}
	
	public T getCell(int x, int y) {
		return getCell(getIndex(x,y));
	}
	
	public int getSize() {
		return size;
	}

	
	public boolean hasCell(int index) {
		return (0<=index)&&(index<getSize());
	}
	
	public boolean hasCell(int x, int y) {
		return (0<=x)&&(x<xdim)&&(0<=y)&&(y<ydim);
	}
	
	public void setCell(int index, T value) {
		data[index] = value;
	}

	public void setCell(int x, int y, T value) {
		setCell(getIndex(x, y), value);
	}
	
	public void resetCell(int index) {
		data[index] = newCell(index);
	}

	public void resetCell(int x, int y) {
		resetCell(getIndex(x, y));
	}
	
	public Dimension getContentBounds() {
		Point cell = new Point();
		findMaxCell(cell);
		return new Dimension(cell.x+1, cell.y+1);
	}
	
	public void findMaxCell(Point cell) {
		Array2D.findMaxCell(cell, data, xdim, ydim);
	}
	
	public void fitToContent() {
		resize(getContentBounds());
	}

	public void resize(int size) {
		resize(size, size);
	}
	
	public void resize(Dimension size) {
		resize(size.width, size.height);
	}

	public void resize(int xdim, int ydim) {
		if (xdim == this.xdim && ydim == this.ydim) return;

		int oxdim = this.xdim;
		int oydim = this.ydim;
		
		T[] newa = newArray(Array2D.getSize(xdim, ydim));
		Array2D.transfer(newa, xdim, ydim, data, oxdim, oydim);
		data = newa;
		this.xdim = xdim;
		this.ydim = ydim;

		size = xdim*ydim;
		if (oxdim==0||oydim==0) {
			resetPath(getColScanPath());
		} else {
			// scan new rows/cols and reset their content
			if (oxdim<xdim) resetCols(Math.max(oxdim-1, 0), xdim-1);
			if (oydim<ydim) resetRows(Math.max(oydim-1, 0), ydim-1);
		}
	}
	
	public void printDebugPath() {
		for(Iterator i=getColScanPath();i.hasNext();) {
			System.out.print(i.next()==null?" ":"x");
		}
		System.out.println();
	}
	
	public void resetCols(int start, int stop) {
		resetPath(getColScanPath(start, stop));
	}

	public void resetRows(int start, int stop) {
		resetPath(getRowScanPath(start, stop));
	}
	
	public void resetCols(int start, int stop, T value) {
		resetPath(getColScanPath(start, stop), value);
	}

	public void resetRows(int start, int stop, T value) {
		resetPath(getRowScanPath(start, stop), value);
	}
	
	public void resetPath(CellIterator<T> iterator) {
		while (iterator.hasNext()) {
			if (iterator.next()==null)
				iterator.setCurrent(newCell(iterator.getCurrentIndex()));
		}
	}
	
	public void resetPath(CellIterator<T> iterator, T value) {
		while (iterator.hasNext()) {
			if (iterator.next()==null)
				iterator.setCurrent(value);
		}
	}
	
	public Iterator<T> iterator() {
		return getValidPath();
	}
	
	public CellIterator<T> getValidPath() {
		return getValidPath(new Path2D.Cols());
	}
	
	public CellIterator<T> getValidPath(Path2D path) {
		return getPath(new Path2D.ConditionalF<T>(getArray(), path, new NotNullCondition<T>()));
	}
	
	public CellIterator<T> getPath(Path2D path) {
		return new CellIterator<T>(this, path);
	}
	
	public CellIterator<T> getRowScanPath(int start, int stop) {
		return getPath(new Path2D.RowRange(start, stop));
	}
	
	public CellIterator<T> getColScanPath(int start, int stop) {
		return getPath(new Path2D.ColRange(start, stop));
	}
	
	public CellIterator<T> getRowScanPath() {
		return getPath(new Path2D.Rows());
	}
	
	public CellIterator<T> getColScanPath() {
		return getPath(new Path2D.Cols());
	}
	
	public CellIterator<T> getSnailShellPath() {
		return getPath(new Path2D.SnailShell(getWidth(), getHeight()));
	}
	
	public void removeCols(int start, int stop) {
		int len = stop-start+1;
		CellIterator<T> src = getColScanPath(start+len, getWidth()-1);
		CellIterator<T> dst = getColScanPath(start, getWidth()-len-1);
		copy(src, dst);
		resize(getWidth()-len+1, getHeight());
	}

	
	protected void copy(CellIterator<T> src, CellIterator<T> dst) {
		while (src.hasNext() && dst.hasNext()) {
			dst.next();
			dst.setCurrent(src.next());
		}
	}
	
	public void removeRows(int start, int stop) {
		int len = stop-start+1;

		CellIterator<T> src = getRowScanPath(start+len, getHeight()-1);
		CellIterator<T> dst = getRowScanPath(start, getHeight()-len-1);
		copy(src, dst);

		resize(getWidth(), getHeight()-len+1);
	}

	public void removeCol(int index) {
		removeCols(index, index);
	}
	
	public void removeRow(int index) {
		removeRows(index, index);
	}
	
	public void removeCross(int index) {
		removeCol(index);
		removeRow(index);
	}
	
	// ********* static methods

	/**
	 * Returns the index of the cell (x, y) for an array with given size 
	 * @param x 
	 * @param y  
	 * @param xdim array with
	 * @param ydim array height
	 * @return the index of the cell (x, y)
	 */
	public static int getIndex(int x, int y, int xdim, int ydim) {
		return x+(y*xdim);
	}
	
	/**
	 * Returns the column of index.  
	 * 
	 * @param index index in the array
	 * @param xdim width of the array
	 * @return column of index
	 */
	public static int getX(int index, int xdim) {
		return index % xdim;
	}

	/**
	 * Returns the row of index.  
	 * 
	 * @param index index in the array
	 * @param xdim width of the array
	 * @return row of index
	 */
	public static int getY(int index, int xdim) {
		return index / xdim;
	}
	
	/**
	 * Searches array for the cell so that any other cells which do not contain null have smaller indices then the found cell.
	 * If the array contains only null values, cell will be set to (-1,-1).
	 * 
	 * @param cell  stores the result cell indices
	 * @param array searched array
	 * @param xdim width of array
	 * @param ydim height of array
	 */
	public static void findMaxCell(Point cell, Object[] array, int xdim, int ydim) {
		cell.x = -1;
		cell.y = -1;
		CellIterator<Object> invScan = new CellIterator<Object> (array, xdim, ydim, new Path2D.Cols());
		
		while (invScan.hasNext()) {
			if (invScan.next()!=null) {
				if (cell.x<invScan.getCurrentX()) {
					cell.x=invScan.getCurrentX();
					if (cell.x==xdim-1)
						break;
				}
			}
		}

		invScan = new CellIterator<Object> (array, xdim, ydim, new Path2D.Cols());

		while (invScan.hasNext()) {
			if (invScan.next()!=null) {
				if (cell.y<invScan.getCurrentY()) {
					cell.y=invScan.getCurrentY();
					if (cell.y==ydim-1)
						break;
				}
			}
		}
	}
	
	/**
	 * Copies src to dst. Only <code>min(dxdim, sxdim) x min(dydim,sydim)</code> cells are copied.
	 *  
	 * @param dst copy target
	 * @param dxdim width of target array
	 * @param dydim height of target array
	 * @param src copy source
	 * @param sxdim width of source array
	 * @param sydim height of source array
	 */
	public static void transfer(Object[] dst, int dxdim, int dydim, Object[] src, int sxdim, int sydim) {
		int copycx = Math.min(sxdim, dxdim);
		int copycy = Math.min(sydim, dydim);

		int doffset = 0;
		int soffset = 0;
		for (int y=0;y<copycy;y++) {
			for (int x=0;x<copycx;x++) 
				dst[doffset+x] = src[soffset+x];
			doffset += dxdim;
			soffset += sxdim;
		}
	}
	
	/**
	 * Copies a row from src to dst. Only <code>min(dxdim, dydim)</code> cells are copied.
	 * 
	 * @param dst copy target
	 * @param drow index of target row
	 * @param dxdim width of target array
	 * @param dydim height of target array
	 * @param src copy source
	 * @param srow index of source row
	 * @param sxdim width of source array
	 * @param sydim height of source array
	 */
	public static void transferRow(Object[] dst, int drow, int dxdim, int dydim, Object[] src, int srow, int sxdim, int sydim) {
		int copyc = Math.min(dxdim, sxdim); // copy count
		
		int dstart = Array2D.getIndex(0, drow, dxdim, dydim);
		int sstart = Array2D.getIndex(0, srow, sxdim, sydim);
		
		transferRow(dst, dstart, src, sstart, copyc);
	}
	
	/**
	 * Copies a row from src to dst.
	 * 
	 * @param dst copy target
	 * @param dstart first cell index of target row
	 * @param src source data
	 * @param sstart first cell index of source row
	 * @param count number of fields to copy, dst and src must have larger or equal number of cells in a row
	 */
	public static void transferRow(Object[] dst, int dstart, Object[] src, int sstart, int count) {
		for (int i=0;i<count;i++)
			dst[dstart+i] = src[sstart+i];
	}

	public static int getSize(int xdim, int ydim) {
		return xdim*ydim;
	}
	
	
}
