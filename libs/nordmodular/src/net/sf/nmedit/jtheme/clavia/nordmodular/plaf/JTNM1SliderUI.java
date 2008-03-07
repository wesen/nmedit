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
package net.sf.nmedit.jtheme.clavia.nordmodular.plaf;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.UIDefaults;

import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.JTControl;
import net.sf.nmedit.jtheme.component.JTControlAdapter;
import net.sf.nmedit.jtheme.component.plaf.JTBasicSliderUI;

public class JTNM1SliderUI extends JTBasicSliderUI implements SwingConstants
{
    public static final String sliderGripBorderLightColorKey = sliderGripColorKey+".light";
    public static final String sliderGripBorderDarkColorKey = sliderGripColorKey+".dark";

    public static JTBasicSliderUI createUI(JComponent c) 
    {
        return new JTNM1SliderUI();
        /*
        JTBasicSliderUI ui = uiInstance.getInstance(c);
        if (ui == null) uiInstance.setInstance(c, ui = new JTNM1SliderUI());
        return ui;*/
    }
    
    private Color sliderGripLight = null;
    private Color sliderGripDark = null;
    
    public void initDefaults(JTComponent c)
    {
        super.initDefaults(c);
        UIDefaults defaults = c.getContext().getUIDefaults();
        sliderGripLight = defaults.getColor(sliderGripBorderLightColorKey);
        sliderGripDark = defaults.getColor(sliderGripBorderDarkColorKey);

        if (sliderGripLight == null)
            sliderGripLight = Color.lightGray;
        if (sliderGripDark == null)
            sliderGripDark = Color.darkGray;
    }

    protected void paintSliderGrip(Graphics2D g, Color gripColor, int gx, int gy, int gw, int gh)
    {
        g.setColor(gripColor);
        g.fillRect(gx, gy, gw, gh);

        g.setColor(sliderGripLight);
        g.drawLine(gx+1, gy, gx+gw-1, gy);
        g.drawLine(gx, gy+1, gx, gy+gh-1);
        g.setColor(sliderGripDark);
        g.drawLine(gx+1, gy+gh-1, gx+gw-1, gy+gh-1);
        g.drawLine(gx+gw-1, gy+1, gx+gw-1, gy+gh-1);
    }

    protected void paintSliderForegroundUnderlay( Graphics2D g, JTControl control, Insets insets )
    {
        if (!control.isExtensionAdapterSet())
            return;
        
        JTControlAdapter valAdapter = control.getControlAdapter();
        JTControlAdapter extAdapter = control.getExtensiondapter();

        int e = control.getExtensionValue();
        if (e != 0)
        {
            int relative = control.getValue()+e;
            if (relative<control.getMinValue())
                relative = control.getMinValue();
            else if (relative>control.getMaxValue())
                relative = control.getMaxValue();
            
            float d = control.getMaxValue()-control.getMinValue();
            if (d>0)
            {
                float rnorm = (relative-control.getMinValue())/d;
                
                double value = valAdapter.getNormalizedValue();
                paintExtension(g, control, 
                        getExtensionColor(control.getControlAdapter().getParameter()), 
                        value, rnorm);
            }
        }
        
    }

    private void paintExtension(Graphics2D g, 
            JTControl control, 
            Color color, double start, double stop)
    {
        g.setColor(color);
        Insets insets = getInsets(control);
        
        int width = control.getWidth();
        int height = control.getHeight();
        
        if (control.getOrientation() == HORIZONTAL)
        {
            // TODO
        }
        else
        {
            // start from 0 to 1, from bottom to top
            
            // vertical
            int maxExtend = height-insets.top-insets.bottom;
            int fromPos = (int)(maxExtend*(1-Math.max(start, stop)));
            int extend = (int)(maxExtend*(Math.abs(start-stop)));
            
            g.fillRect(insets.left, insets.top+fromPos, 
                    width-insets.left-insets.right, extend);
        }
    }
    
}

