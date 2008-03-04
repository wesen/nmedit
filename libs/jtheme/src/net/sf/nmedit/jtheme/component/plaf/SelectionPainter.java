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
package net.sf.nmedit.jtheme.component.plaf;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Collection;

import javax.swing.JComponent;

import net.sf.nmedit.jpatch.PModule;

/**
 * TODO replace class with a customizable approach.
 */
public class SelectionPainter 
{
    
    private static final Color SELECTION_COLOR = Color.decode("#995500");
    private static final float[] DASH = { 6, 3 };
    private static final BasicStroke DASH_STROKE = new BasicStroke(1,
            BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1, DASH, 0 );

	public static void paintPModuleSelectionBox(Graphics g,
			Collection<? extends PModule> modules, int tx, int ty) {
        if (modules.isEmpty()) return;
        g = g.create();
        try
        {
            ((Graphics2D)g).setStroke(DASH_STROKE);

            g.setXORMode(Color.BLACK);
            g.setColor(SELECTION_COLOR);
            for (PModule c: modules)
            {
                g.drawRect(tx+c.getScreenX(), ty+c.getScreenY(), c.getScreenWidth()-1, c.getScreenHeight()-1);        
            }
        }
        finally
        {
            g.dispose();
        }
}
    

    public static void paintSelectionBox(Graphics g, Collection<? extends JComponent> components, int tx, int ty)
    {
        if (components.isEmpty()) return;
        g = g.create();
        try
        {
            ((Graphics2D)g).setStroke(DASH_STROKE);

            g.setXORMode(Color.BLACK);
            g.setColor(SELECTION_COLOR);
            for (JComponent c: components)
            {
                g.drawRect(tx+c.getX(), ty+c.getY(), c.getWidth()-1, c.getHeight()-1);        
            }
        }
        finally
        {
            g.dispose();
        }
    }
    
    public static void paintSelectionBox(Graphics g, int x, int y, int width, int height)
    {
        g = g.create();
        try
        {
            ((Graphics2D)g).setStroke(DASH_STROKE);

            g.setXORMode(Color.BLACK);
            g.setColor(SELECTION_COLOR);
            g.drawRect(x, y, width-1, height-1);
        }
        finally
        {
            g.dispose();
        }
    }
    
    public static void paintSelection(Graphics g, int x, int y, int width, int height)
    {
        g = g.create();
        try
        {
            g.setColor(SELECTION_COLOR);
            g.drawRect(x, y, width-1, height-1);
        }
        finally
        {
            g.dispose();
        }
    }

}
