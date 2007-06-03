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
import java.awt.RenderingHints;

import javax.swing.JComponent;
import javax.swing.UIDefaults;

import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.JTControl;
import net.sf.nmedit.jtheme.component.misc.KnobMetrics;

public class JTBasicKnobUI extends JTBasicControlUI
{

    public static final Color defaultBackgroundColor = new Color(0xE3E3E3);
    public static final Color defaultBorderColor = new Color(0x000000);
    public static final Color defaultFocusedBorderColor = new Color(0x660000FF, true);
    public static final Color defaultGripColor = new Color(0x000000);

    public static final String knobBackgroundColorKey = "color.background";
    public static final String knobBorderColorKey = "color.border";
    public static final String knobGripColorKey = "color.grip";

    public static final String knobGripStartValueKey = "grip.start";
    public static final String knobGripStopValueKey = "grip.stop";
    
    private static JTBasicKnobUI instance;
    
    public JTBasicKnobUI()
    {
        super();
    }


    public void installUI(JComponent c)
    {
        super.installUI(c);
        c.setOpaque(false);   
    }
    
    public void uninstallUI(JComponent c)
    {
        super.uninstallUI(c);
    }
    
    public static JTBasicKnobUI createUI(JComponent c) 
    {
        if (instance == null)
            instance = new JTBasicKnobUI();
        
        return instance;
    }

    protected String getPrefix()
    {
        return "knob.";
    }

    public void paintStaticLayer(Graphics2D g, JTComponent c)
    {
        JTControl control = (JTControl) c;
        
        paintKnobBackground(g, control);
    }

    public void paintDynamicLayer(Graphics2D g, JTComponent c)
    {
        JTControl control = (JTControl) c;
        
        paintKnobForegroundUnderlay(g, control);
        paintKnobForeground(g, control);
    }

    protected static int diameter(int w, int h)
    {
        // use uneven size to avoid bad looking circles
        int d = Math.min(w, h);
        return d - (d+1)%2;
    }
    
    protected static int diameter(JTComponent c)
    {
        return diameter(c.getWidth(), c.getHeight());
    }
    
    protected void paintKnobBackground( Graphics2D g, JTControl control )
    {
        UIDefaults defaults = control.getContext().getUIDefaults();
        
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int size = diameter(control);
        
        Color backgroundColor = defaults.getColor(getPrefix()+knobBackgroundColorKey);
        if (backgroundColor == null)
            backgroundColor = defaultBackgroundColor;

        Color borderColor = defaults.getColor(getPrefix()+knobBorderColorKey);
        if (borderColor == null)
            borderColor = defaultBorderColor;
        
        g.setColor(backgroundColor);
        g.fillOval(0, 0, size, size);

        g.setColor(borderColor);
        g.drawOval(0, 0, size-1, size-1);
    }

    protected void paintKnobForegroundUnderlay( Graphics2D g, JTControl control )
    {
        if (control.hasFocus())
        {
            Color focusColor = defaultFocusedBorderColor;
            int size = diameter(control);
            
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g.setColor(focusColor);
            g.drawOval(1, 1, size-3, size-3);
        }
    }

    protected void paintKnobForeground( Graphics2D g, JTControl control )
    {
        UIDefaults defaults = control.getContext().getUIDefaults();
        double value = control.getNormalizedValue();
        double gripStartValue = getDouble(defaults, getPrefix()+knobGripStartValueKey, 0.25/2);
        double gripStopValue = getDouble(defaults, getPrefix()+knobGripStopValueKey, 1-0.25/2);
        
        Color gripColor = defaults.getColor(getPrefix()+knobGripColorKey);
        if (gripColor == null)
            gripColor = defaultGripColor;
        
        paintKnobGrip(g, control, gripColor, gripStartValue, gripStopValue, value);
    }

    private void paintKnobGrip(Graphics2D g, JTControl control, Color gripColor, 
            double gripStartValue, double gripStopValue, double value)
    {
        int size = diameter(control);

        int len = size/2-1;
        int cxy = 1+len;
        
        // scale and translate value
        value = KnobMetrics.cw(gripStartValue+(value*(gripStopValue-gripStartValue))); 
        
        int gx = (int) (Math.sin(value*KnobMetrics.PI2)*len);
        int gy = (int) (Math.cos(value*KnobMetrics.PI2)*len);

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(gripColor);
        g.drawLine(cxy, cxy, cxy+gx, cxy+gy);
    }

}
