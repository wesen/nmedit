/* Copyright (C) 2008 Christian Schneider
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
package net.sf.nmedit.jtheme.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.Border;

public class SimpleSelectionBorder implements Border
{

    private Border border;
    private Color color;
    
    public SimpleSelectionBorder(Border notSelectedBorder, Color color)
    {
        this.border = notSelectedBorder;
        this.color = color;
    }
    
    public Insets getBorderInsets(Component c)
    {
        Insets insets = border.getBorderInsets(c);
        insets.left = Math.max(1, insets.left);
        insets.right = Math.max(1, insets.right);
        insets.top = Math.max(1, insets.top);
        insets.bottom = Math.max(1, insets.bottom);
        return insets;
    }

    public boolean isBorderOpaque()
    {
        return border.isBorderOpaque();
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width,
            int height)
    {
        border.paintBorder(c, g, x, y, width, height);
        // paint selection
        g.setColor(color);
        g.drawRect(x, y, width-1, height-1);
    }

}
