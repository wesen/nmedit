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
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.UIDefaults;
import javax.swing.border.Border;

import net.sf.nmedit.jtheme.component.JTComponent;

public class JTDisplayUI extends JTComponentUI
{

    public static final String BACKGROUND_KEY = "display.background";
    public static final Color DEFAULT_BACKGROUND = Color.DARK_GRAY;

    public static final String FOREGROUND_KEY = "display.foreground";
    public static final Color DEFAULT_FOREGROUND = Color.GREEN;
    public static final String BORDER_KEY = "display.border";

    protected Color getDefaultBackgroundColor()
    {
        return DEFAULT_BACKGROUND;
    }

    protected Color getDefaultForegroundColor()
    {
        return DEFAULT_FOREGROUND;
    }
    
    private static UIInstance<JTDisplayUI> uiInstance = new UIInstance<JTDisplayUI>(JTDisplayUI.class);

    public static JTDisplayUI createUI(JComponent c)
    {
        JTDisplayUI ui = uiInstance.getInstance(c);
        if (ui == null) uiInstance.setInstance(c, ui = new JTDisplayUI());
        return ui; 
    }
    
    private Color bgColor;
    private Color fgColor;
    private Border border;
    private boolean defaultsInitialized = false;
    
    public void installUI(JComponent c)
    {
        if (!defaultsInitialized)
        {
            UIDefaults uiDefaults = ((JTComponent) c).getContext().getUIDefaults();
            initDefaults(uiDefaults);
            defaultsInitialized = true;
        }
        
        c.setBackground(bgColor);
        c.setForeground(fgColor);
        if (border != null)
            c.setBorder(border);
    }
    
    private void initDefaults(UIDefaults defaults)
    {
        border = defaults.getBorder(BORDER_KEY);

        bgColor = defaults.getColor(BACKGROUND_KEY);
        if (bgColor == null)
            bgColor = getDefaultBackgroundColor();

        fgColor = defaults.getColor(FOREGROUND_KEY);
        if (fgColor == null)
            fgColor = getDefaultForegroundColor();
        
    }

    private static transient Insets cachedInsets;
    
    public void paintStaticLayer(Graphics2D g, JTComponent c)
    {
        if (c.isOpaque())
        {
            Color bgColor = c.getBackground();
            if (bgColor != null)
            {
                Insets i = cachedInsets = c.getInsets(cachedInsets);
                Color oldColor = g.getColor();
                g.setColor(bgColor);
                g.fillRect(i.left, i.top, 
                        c.getWidth()-i.left-i.right, 
                        c.getHeight()-i.top-i.bottom);
                g.setColor(oldColor);
            }
        }
    }
    
}
