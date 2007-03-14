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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

public class ShadowCableRenderer extends SimpleCableRenderer
{

    private Stroke fatStroke = new BasicStroke(2f);
    private boolean shadows = true;
    private Color shadowColor = new Color(0x88000000, true);
    
    public void setShadowsEnabled(boolean enabled)
    {
        this.shadows = enabled;
    }
    
    public boolean isShadowsEnabled()
    {
        return shadows;
    }

    public void initRenderer(Graphics2D g2)
    {
        super.initRenderer(g2);
        if (shadows)
        {
            g2.setStroke(fatStroke);
        }
    }

    public void render(Graphics2D g2, Cable cable)
    {
        if (shadows)
        {
            final int dx = 1;
            
            g2.translate(dx, 0);
            g2.setColor(shadowColor);
            g2.draw(cable.getShape());
            g2.translate(-dx, 0);
        }
        
        super.render(g2, cable);
    }

    public int getCableDiameter()
    {
        return 4;
    }
}

