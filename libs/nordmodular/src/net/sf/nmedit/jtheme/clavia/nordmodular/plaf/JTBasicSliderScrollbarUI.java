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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;

import javax.swing.JComponent;

import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.plaf.JTBasicSliderUI;
import net.sf.nmedit.jtheme.component.plaf.UIInstance;

public class JTBasicSliderScrollbarUI extends JTBasicSliderUI
{

    protected static final UIInstance<JTBasicSliderScrollbarUI> uiInstance = 
        new UIInstance<JTBasicSliderScrollbarUI>(JTBasicSliderScrollbarUI.class);
  
    public static JTBasicSliderScrollbarUI createUI(JComponent c) 
    {
        JTBasicSliderScrollbarUI ui = uiInstance.getInstance(c);
        if (ui == null) uiInstance.setInstance(c, ui = new JTBasicSliderScrollbarUI());
        return ui;
    }
   
    Color gripBackground = new Color(0xD0D0D0);
    Color gripBorderHighlight = new Color(0xF0F0F0);
    Color gripGrips = new Color(0x7e7e7e);

    protected void initDefaults(JTComponent c)
    {
        super.initDefaults(c);
        defaultGripSize = 11;
    }

    protected void paintSliderGrip(Graphics2D g, Color gripColor, int gx, int gy, int gw, int gh)
    {
        paintScrollbarGrip(g, gx, gy, gw, gh);
    }

    private void paintScrollbarGrip(Graphics2D g, int x, int y, int width, int height)
    {
        Color background = new Color(0xD0D0D0);
        Color borderHighlight = new Color(0xF0F0F0);
        Color grips = new Color(0x7e7e7e);
        
        g.setColor(background);
        g.fillRect(x, y, width, height);
        g.setColor(borderHighlight);
        g.drawRect(x, y, width-1, height-1);
        g.setColor(grips);
        for (int i=2;i<height-1;i+=2)
        {
            g.drawLine(x+1, y+i, x+width-2, y+i);
        }
        
        Composite composite = g.getComposite();
        g.setColor(grips.darker());
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f));
        g.drawLine(x+width-1, y, x+width-1, y+height-1);
        g.drawLine(x, y+height-1, x+width-2, y+height-1);

        g.drawLine(x+width-1, y+1, x+width-1, y+height-2);
        g.drawLine(x+1, y+height-1, x+width-2, y+height-1);
        
        g.setComposite(composite);
    }
    
}
