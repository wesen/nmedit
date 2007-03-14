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
package net.sf.nmedit.jtheme.cable;

import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class SimpleCableRenderer implements CableRenderer
{
    
    private boolean antialiasing = true;
    
    public SimpleCableRenderer()
    {
        this(false);
    }
    
    public SimpleCableRenderer(boolean antialiasing)
    {
        this.antialiasing = antialiasing;
    }
    
    public void setAntialiasingEnabled(boolean enabled)
    {
        this.antialiasing = enabled;
    }
    
    public boolean isAntialiasingEnabled()
    {
        return antialiasing;
    }

    public void initRenderer(Graphics2D g2)
    {
        if (antialiasing)
        {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                    RenderingHints.VALUE_ANTIALIAS_ON);
        }
    }

    public void render(Graphics2D g2, Cable cable)
    {        
        g2.setColor(cable.getColor());
        g2.draw(cable.getShape());
    }

    public int getCableDiameter()
    {
        return 2;
    }
    
}

