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

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class AbstractBackground implements Background
{
    /**
     * The clip bounds of the current graphics object
     */
    protected final Rectangle clip = new Rectangle();
    
    /**
     * The area to be filled. Intersection between
     * the clip bounds and the given bounds.
     */
    protected final Rectangle area = new Rectangle();

    protected void updateArea(Graphics g, int x, int y, int width, int height)
    {
        // read the graphics clip
        clip.setBounds(0,0,0,0);
        g.getClipBounds(clip); // if no clip bounds are set clip is still empty

        // set the area
        area.setLocation(x, y);
        area.setSize(width, height);
        
        // compute intersection between area and clip
        if (!clip.isEmpty())
        {
            Rectangle.intersect(area, clip, area);
        }
    }
    
}
