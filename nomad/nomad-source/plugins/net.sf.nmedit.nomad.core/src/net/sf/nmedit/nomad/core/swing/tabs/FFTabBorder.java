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
package net.sf.nmedit.nomad.core.swing.tabs;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.Border;

public class FFTabBorder implements Border
{

    private Color clFill;
    private Color clBottomLine;
    private int fillHeight;
    
    public FFTabBorder(Color bottomLineColor, Color fillColor, int fillHeight)
    {
        this.clBottomLine = bottomLineColor;
        this.clFill = fillColor;
        this.fillHeight = fillHeight;
    }
    
    public Insets getBorderInsets(Component c)
    {
        return new Insets(0, 0, 1+fillHeight, 0);
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width,
            int height)
    {
        int r = x+width-1;
        int b = y+height-1;
        
        Color prevColor = g.getColor();
        g.setColor(clFill);
        g.fillRect(x, b-fillHeight, width, fillHeight);
        g.setColor(clBottomLine);
        g.drawLine(x, b, r, b);
        
        g.setColor(prevColor);
    }

    public boolean isBorderOpaque()
    {
        return (clFill.getAlpha() & clBottomLine.getAlpha())==0xFF;
    }

}
