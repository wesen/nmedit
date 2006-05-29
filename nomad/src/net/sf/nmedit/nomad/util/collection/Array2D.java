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
package net.sf.nmedit.nomad.util.collection;

import java.awt.Point;

public class Array2D {

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

    public static <T> T get( T[] array, int x, int y, int xdim, int ydim )
    {
        return array[getIndex(x, y, xdim, ydim)];
    }

    public static <T> void set( T[] array, int x, int y, int xdim, int ydim, T data )
    {
        array[getIndex(x, y, xdim, ydim)] = data;
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
        
        if (xdim==0||ydim==0)
            return ;
        
        int index = 0;
        for (int x=0;x<xdim;x++)
        {
            for (int y=0;y<ydim;y++)
            {
                if (array[index]!=null)
                {
                    if (cell.x<x) cell.x = x;
                    if (cell.y<y) 
                    {
                        cell.y = y;
                        if (cell.x==xdim && cell.y==ydim)
                            return;
                    }
                }
                index ++;
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
	public static <T> void transfer(T[] dst, int dxdim, int dydim, T[] src, int sxdim, int sydim) {
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
	public static <T> void transferRow(T[] dst, int drow, int dxdim, int dydim, T[] src, int srow, int sxdim, int sydim) {
		int copyc = Math.min(dxdim, sxdim); // copy count
		
		int dstart = Array2D.getIndex(0, drow, dxdim, dydim);
		int sstart = Array2D.getIndex(0, srow, sxdim, sydim);

        System.arraycopy(src, sstart, dst, dstart, copyc);
	}
	
	public static int getSize(int xdim, int ydim) {
		return xdim*ydim;
	}
	
	
}
