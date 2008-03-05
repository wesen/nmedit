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
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.UIDefaults;
import javax.swing.border.Border;

import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.JTControl;

public class JTBasicSliderUI extends JTBasicControlUI implements SwingConstants
{

    public static final Color DEFAULT_BACKGROUND = new Color(0xDCDCDC);
    public static final Color DEFAULT_GRIP_COLOR = new Color(0x000000);
    public static final Color defaultFocusedBorderColor = new Color(0x660000FF, true);

    public static final String sliderBackgroundColorKey = "sliderui.color.background";
    public static final String sliderGripColorKey = "sliderui.color.grip";

    public static final String sliderGripSizeKey = "sliderui.grip.size";
    public static final String borderKey = "sliderui.border";
    
    protected static final UIInstance<JTBasicSliderUI> uiInstance = new UIInstance<JTBasicSliderUI>(JTBasicSliderUI.class);
  
    public static JTBasicSliderUI createUI(JComponent c) 
    {
        JTBasicSliderUI ui = uiInstance.getInstance(c);
        if (ui == null) uiInstance.setInstance(c, ui = new JTBasicSliderUI());
        return ui;
    }

    protected Border createDefaultBorder()
    {
        return BorderFactory.createLineBorder(defaultGripColor);
    }
    
    protected Border defaultBorder;
    protected Color defaultBackground;
    protected int defaultGripSize=5;
    protected Color defaultGripColor;
    private boolean defaultsInitialized = false;

    protected void initDefaults(JTComponent c)
    {
        UIDefaults defaults = c.getContext().getUIDefaults();

        defaultBorder = defaults.getBorder(borderKey);
        if (defaultBorder == null)
            defaultBorder = createDefaultBorder();

        defaultBackground = defaults.getColor(sliderBackgroundColorKey);
        if (defaultBackground == null)
            defaultBackground = DEFAULT_BACKGROUND;

        defaultGripSize = defaults.getInt(sliderGripSizeKey);
        if (defaultGripSize<=0)
            defaultGripSize = 5;

        defaultGripColor = defaults.getColor(sliderGripColorKey);
        if (defaultGripColor == null)
            defaultGripColor = DEFAULT_GRIP_COLOR;
    }
    
    public void installUI(JComponent c)
    {
        super.installUI(c);
        
        JTComponent jtc = (JTComponent) c;
        
        if (!defaultsInitialized)
        {
            initDefaults(jtc);
            defaultsInitialized = true;
        }
        
        c.setBorder(defaultBorder);
        c.setBackground(defaultBackground);
        
        if (defaultBorder != null)
            c.setOpaque(defaultBorder.isBorderOpaque());
        else
            c.setOpaque(true);
    }

    public void uninstallUI(JComponent c)
    {
        super.uninstallUI(c);
        c.setBorder(null);
    }

    private transient Insets cachedBorderInsets;
    
    protected Insets getInsets(JTComponent c)
    {
        if (cachedBorderInsets == null)
        {
            cachedBorderInsets = c.getInsets(cachedBorderInsets);
        }
        return cachedBorderInsets;
    }

    public void paintStaticLayer(Graphics2D g, JTComponent c)
    {
        JTControl control = (JTControl) c;
        
        Insets insets = getInsets(c);
        paintSliderBackground(g, control, insets);
    }

    public void paintDynamicLayer(Graphics2D g, JTComponent c)
    {
        JTControl control = (JTControl) c;

        Insets insets = getInsets(c);
        paintSliderForegroundUnderlay(g, control, insets);
        paintSliderForeground(g, control, insets);
    }

    
    protected void paintSliderBackground( Graphics2D g, JTControl c, Insets insets )
    {
        Border border = c.getBorder();
        
        int x = 0;
        int y = 0;
        int w = c.getWidth();
        int h = c.getHeight();
        
        if (border != null  && (!border.isBorderOpaque()))
        {
            
            x+=insets.left;
            y+=insets.top;
            w-=insets.left+insets.right;
            h-=insets.top+insets.bottom;
        }
        
        Color bg = c.getBackground();
        if (c.hasFocus())
            bg = bg.brighter();
        
        g.setColor(bg);
        g.fillRect(x, y, w, h);
    }

    protected void paintSliderForegroundUnderlay( Graphics2D g, JTControl control, Insets insets )
    {
        
    }

    protected void paintSliderForeground( Graphics2D g, JTControl control, Insets insets )
    {
        double value = control.getNormalizedValue();
        paintSliderGrip(g, control, defaultGripColor, defaultGripSize, value, insets);
    }

    protected void paintSliderGrip(Graphics2D g, JTControl control, Color gripColor, 
            int gripSize, double value, Insets insets)
    {
        int orientation = ((JTControl)control).getOrientation();
        int start;
        int stop;
        int dx;
        int dy;
        int gw;
        int gh;
        if (orientation == VERTICAL)
        {
            start = insets.top;
            stop = control.getHeight()-insets.bottom-gripSize;
            dx = insets.left;
            dy = start+ (int) ((1-value)*(stop-start));
            gw = control.getWidth()-insets.left-insets.right;
            gh = gripSize;
        }
        else
        {
            start = insets.left;
            stop = control.getWidth()-insets.right-gripSize;
            dx = start+ (int) (value*(stop-start));
            dy = insets.top;
            gw = gripSize;
            gh = control.getHeight()-insets.top-insets.bottom;
        }
        
        paintSliderGrip(g, gripColor, dx, dy, gw, gh);
    }

    protected void paintSliderGrip(Graphics2D g, Color gripColor, int gx, int gy, int gw, int gh)
    {
        g.setColor(gripColor);
        g.fillRect(gx, gy, gw, gh);
    }

    public Dimension getPreferredSize(JComponent c) 
    {
        boolean hrz = ((JTControl) c).getOrientation() != VERTICAL;
        
        return hrz ? new Dimension(100, 20) : new Dimension(20, 100);
    }
    
    public Dimension getMinimumSize(JComponent c) 
    {
        return new Dimension(10, 10);
    }
    
    public Dimension getMaximumSize(JComponent c) 
    {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE); 
    }
    
}
