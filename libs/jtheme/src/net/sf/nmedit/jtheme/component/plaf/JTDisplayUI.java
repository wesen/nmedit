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
 * Created on Jan 21, 2007
 */
package net.sf.nmedit.jtheme.component.plaf;

import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.JComponent;

import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.JTDisplay;

public class JTDisplayUI extends JTComponentUI
{

    public static final String BACKGROUND_KEY = "display.background";
    public static final Color DEFAULT_BACKGROUND = Color.DARK_GRAY;

    public static final String FOREGROUND_KEY = "display.foreground";
    public static final Color DEFAULT_FOREGROUND = Color.GREEN;

    protected Color getDefaultBackgroundColor()
    {
        return DEFAULT_BACKGROUND;
    }

    protected Color getDefaultForegroundColor()
    {
        return DEFAULT_FOREGROUND;
    }
    
    private static JTDisplayUI instance = new JTDisplayUI();

    public static JTDisplayUI createUI(JComponent c)
    {
        return instance; 
    }
    
    public void installUI(JComponent c)
    {
        JTDisplay display = (JTDisplay) c;
        Color bgColor = display.getContext().getUIDefaults().getColor(BACKGROUND_KEY);
        if (bgColor == null)
            bgColor = getDefaultBackgroundColor();
        display.setBackground(bgColor);

        Color fgColor = display.getContext().getUIDefaults().getColor(FOREGROUND_KEY);
        if (fgColor == null)
            fgColor = getDefaultForegroundColor();
        display.setForeground(fgColor);
    }
    
    public void paintStaticLayer(Graphics2D g, JTComponent c)
    {
        if (c.isOpaque())
        {
            Color bgColor = c.getBackground();
            if (bgColor != null)
            {
                Color oldColor = g.getColor();
                g.setColor(bgColor);
                g.fillRect(0, 0, c.getWidth(), c.getHeight());
                g.setColor(oldColor);
            }
        }
    }
    
}
