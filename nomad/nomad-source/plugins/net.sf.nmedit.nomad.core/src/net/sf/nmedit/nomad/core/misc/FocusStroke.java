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
package net.sf.nmedit.nomad.core.misc;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Stroke;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.border.Border;

public class FocusStroke
{

    private static BasicStroke focusStroke = new BasicStroke(1, BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_MITER, 1, new float[] { 1, 1 }, 0);
    
    public static Stroke getFocusStroke()
    {
        return focusStroke;
    }

    public static Border getFocusStrokeBorder(Color strokeColor)
    {
        return new FocusStrokeBorder(strokeColor);
    }

    public static Border getFocusStrokeBorder(Component focusOwner, Color strokeColor)
    {
        FocusStrokeBorder border = new FocusStrokeBorder(strokeColor);
        focusOwner.addFocusListener(border);
        return border;
    }

    private static class FocusStrokeBorder implements Border, FocusListener
    {

        private Color strokeColor;

        public FocusStrokeBorder(Color strokeColor)
        {
            this.strokeColor = strokeColor;
        }

        public Insets getBorderInsets(Component c)
        {
            return new Insets(2,2,2,2);
        }

        public boolean isBorderOpaque()
        {
            return false;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height)
        {
            if (!c.hasFocus()) return;
            
            Graphics2D g2 = (Graphics2D) g;
            Color oldColor = g2.getColor();

            g2.setColor(strokeColor);
            Stroke prevStroke = g2.getStroke();
            g2.setStroke(getFocusStroke());
            g2.drawRect(x, y, width-1, height-1);
            
            g2.setColor(oldColor);
            g2.setStroke(prevStroke);
        }

        public void focusGained(FocusEvent e)
        {
            e.getComponent().repaint();
        }

        public void focusLost(FocusEvent e)
        {
            e.getComponent().repaint();
        }
        
    }
    
}
