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
package net.sf.nmedit.jpatch.clavia.nordmodular;

import java.awt.Point;

public class NMModuleMetrics
{

    public static final int GRID_WIDTH_PX = 255;
    public static final int GRID_HEIGHT_PX = 15;
    
    private NMModuleMetrics()
    {
        // instances not allowed
    }
    
    public static Point computeInternalLocation(int sx, int sy)
    {
        return computeInternalLocation(null, sx, sy);
    }
    
    public static int computeScreenX(int ix)
    {
        return ix*GRID_WIDTH_PX;
    }
    
    public static int computeScreenY(int iy)
    {
        return iy*GRID_HEIGHT_PX;
    }
    
    public static int computeInternalX(int sx)
    {
        // x/255
        return (sx+255/2)/255;
    }
    
    public static int computeInternalY(int sy)
    {
        return sy/15;
    }
    
    public static Point computeInternalLocation(Point dst, int sx, int sy)
    {
        if (dst == null)
            dst = new Point();
        dst.setLocation(computeInternalX(sx), computeInternalY(sy));
        return dst;
    }
    
    public static Point computeScreenLocation(int x, int y)
    {
        return computeScreenLocation(null, x, y);
    }
    
    public static Point computeScreenLocation(Point src)
    {
        return computeScreenLocation(null, src.x, src.y);
    }
    
    public static Point computeScreenLocation(Point dst, int x, int y)
    {
        if (dst == null)
            dst = new Point();

        dst.setLocation(computeScreenX(x), computeScreenY(y));
        
        return dst;
    }
    

    
    
}
