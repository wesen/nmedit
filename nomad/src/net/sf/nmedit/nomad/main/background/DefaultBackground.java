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
 * Created on May 1, 2006
 */
package net.sf.nmedit.nomad.main.background;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

public class DefaultBackground extends AbstractBackground
{

    public void paintBackground( Component c, Graphics g, int x, int y,
            int width, int height )
    {
        // previous color
        Color prevColor = g.getColor();
        // set color
        g.setColor(c.getBackground());
        // fill clip & bounds
        updateArea(g, x, y, width, height);
        g.fillRect(area.x, area.y, area.width, area.height);
        // restore color
        g.setColor(prevColor);
    }
    
    public int getTransparency()
    {
        return OPAQUE;
    }

}
