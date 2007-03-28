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
 * Created on Jun 22, 2006
 */
package net.sf.nmedit.nomad.core.swing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Stroke;

import javax.swing.border.Border;

public class DashBorder implements Border
{

    private final static Stroke dashLines = new BasicStroke( 1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1, new float[]{ 1, 2 }, 0 );
    
    private Color color;

    private boolean left;

    private boolean right;

    private boolean top;

    private boolean bottom;

    private static Insets insets = new Insets(1,1,1,1); 
    
    public DashBorder( Color color, boolean left, boolean right, boolean top, boolean bottom)
    {
        if (color == null) throw new NullPointerException();
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
        this.color = color;
    }

    public static Border create(boolean left, boolean right, boolean top, boolean bottom)
    {
        return new DashBorder(Color.DARK_GRAY, left, right, top, bottom);
    }
    
    public static Border create(Color color, boolean left, boolean right, boolean top, boolean bottom)
    {
        return new DashBorder(color, left, right, top, bottom);
    }

    public void paintBorder( Component c, Graphics g, int x, int y, int width,
            int height )
    {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(color);
        g2.setStroke(dashLines);
        
        int r = width-1;
        int b = height-1;

        if (top) g2.drawLine(0, 0, r, 0); // top
        if (bottom) g2.drawLine(0, b, r, b); // bottom

        if (left) g2.drawLine(0, 0, 0, b); // left
        if (right) g2.drawLine(r, 0, r, b); // right
        
        g2.dispose();
    }

    public Insets getBorderInsets( Component c )
    {
        return insets;
    }

    public boolean isBorderOpaque()
    {
        return false;
    }

}
