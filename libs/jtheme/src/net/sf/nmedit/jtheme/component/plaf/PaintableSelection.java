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
package net.sf.nmedit.jtheme.component.plaf;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JComponent;

public class PaintableSelection
{

    public Point start = new Point(0,0);
    public Point origin = new Point(0,0);
    public Rectangle bounds = new Rectangle(0,0,0,0);

    public int getPaintX()
    {
        return bounds.x-origin.x; 
    }
    
    public int getPaintY()
    {
        return bounds.y-origin.y; 
    }
    
    public void paint(JComponent c, Graphics g)
    {
        if (!bounds.isEmpty())
        {
            SelectionPainter.paintSelectionBox(g, 
                    getPaintX(), 
                    getPaintY(),
                    bounds.width, 
                    bounds.height);
        }
    }
    
    public void repaint(JComponent c)
    {
        if (!bounds.isEmpty())
        {
            c.repaint( 
                    getPaintX(), 
                    getPaintY(),
                    bounds.width, 
                    bounds.height);
        }
    }
    
}
