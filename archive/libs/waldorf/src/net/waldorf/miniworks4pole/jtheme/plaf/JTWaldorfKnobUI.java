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
package net.waldorf.miniworks4pole.jtheme.plaf;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;

import javax.swing.JComponent;
import javax.swing.UIDefaults;

import net.sf.nmedit.jtheme.component.JTControl;
import net.sf.nmedit.jtheme.component.misc.KnobMetrics;
import net.sf.nmedit.jtheme.component.plaf.JTBasicKnobUI;

public class JTWaldorfKnobUI extends JTBasicKnobUI
{

    private static JTWaldorfKnobUI instance;
    
    public static final Color defaultBackgroundColor = new Color(0x777777);
    public static final Color defaultBorderColor = new Color(0xEEEEEE);
    public static final Color defaultGripColor = new Color(0xEEEEEE);
    
    public JTWaldorfKnobUI()
    {
        super();
    }

    public static JTWaldorfKnobUI createUI(JComponent c) 
    {
        if (instance == null)
            instance = new JTWaldorfKnobUI();
        
        return instance;
    }

    protected String getPrefix()
    {
        return "knob.waldorf.";
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
        
        GradientPaint gp = new GradientPaint(
                0, 0, backgroundColor.brighter(), 
                size, size, backgroundColor
        );

        Paint oldPaint = g.getPaint();
        g.setPaint(gp);
        g.fillOval(0, 0, size, size);
        
        gp = new GradientPaint(
                0, 0, borderColor, 
                size, size, Color.DARK_GRAY
        );

        g.setPaint(gp);
        g.drawOval(0, 0, size-1, size-1);

        g.setPaint(oldPaint);
    }

    protected void paintKnobForegroundUnderlay( Graphics2D g, JTControl control )
    {
        if (control.hasFocus())
        {
            Color focusColor = defaultFocusedBorderColor;
            int size = diameter(control);
            
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g.setColor(focusColor);
            g.drawOval(0, 0, size-1, size-1);
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

        int pos = size/2-5;
        int cxy = 1+size/2;
        
        // scale and translate value
        value = KnobMetrics.cw(gripStartValue+(value*(gripStopValue-gripStartValue))); 
        
        int gx = (int) (Math.sin(value*KnobMetrics.PI2)*pos);
        int gy = (int) (Math.cos(value*KnobMetrics.PI2)*pos);

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(gripColor);
        
        GradientPaint gp = new GradientPaint(
                cxy+gx-3, cxy+gy-3, gripColor, 
                cxy+gx-3+5, cxy+gy-3+5, Color.DARK_GRAY
        );
        
        Paint oldPaint = g.getPaint();
        g.setPaint(gp);
        g.fillOval(cxy+gx-3, cxy+gy-3, 5, 5);
        g.setPaint(oldPaint);
        
    }
}

