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
 * Created on May 5, 2006
 */
package net.sf.nmedit.nomad.main.background;

import java.awt.Component;
import java.awt.Graphics;

public class CompoundBackground implements Background
{

    private Background a;
    private Background b;

    public CompoundBackground(Background a, Background b)
    {
        this.a = a;
        this.b = b;
    }

    public void paintBackground( Component c, Graphics g, int x, int y,
            int width, int height )
    {
        if (b.getTransparency()!=OPAQUE)
        {
            a.paintBackground(c, g, x, y, width, height);
        }
        b.paintBackground(c, g, x, y, width, height);
    }

    public int getTransparency()
    {
        int ta = a.getTransparency();
        int tb = b.getTransparency();
        
        if (ta == OPAQUE || tb==OPAQUE)
            return OPAQUE;
        else if (ta==TRANSLUCENT||tb==TRANSLUCENT)
            return TRANSLUCENT;
        else
            return BITMASK;
    }

}
