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
package net.sf.nmedit.jtheme;

import java.awt.Color;

public class JTUtils
{

    public static int getAlpha(Color color)
    {
        return color.getAlpha();
    }
    
    public static int getAlpha(int color)
    {
        return (color >> 24)&0xFF;
    }

    public static Color alpha(Color c, int alpha)
    {   
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
    }
    
    public static Color neighbour(Color c1, Color c2, double f) 
    {
        return new Color(
            c1.getRed()     + (int)((c2.getRed()    -c1.getRed()    )*f),
            c1.getGreen()   + (int)((c2.getGreen()  -c1.getGreen()  )*f),
            c1.getBlue()    + (int)((c2.getBlue()   -c1.getBlue()   )*f),
            c1.getAlpha()   + (int)((c2.getAlpha()  -c1.getAlpha()  )*f)
        );
    }

}

