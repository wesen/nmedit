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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.JTImage;

public class JTImageUI extends JTComponentUI
{

    private static UIInstance<JTImageUI> uiInstance = new UIInstance<JTImageUI>(JTImageUI.class);
    
    public static JTImageUI createUI(JComponent c)
    {
        JTImageUI ui = uiInstance.getInstance(c);
        if (ui == null) uiInstance.setInstance(c, ui = new JTImageUI());
        return ui;
    }
    
    public Dimension getPreferredSize(JComponent c)
    {
        JTImage ic = (JTImage) c;
        ImageIcon icon = ic.getIcon();
        
        return icon == null ? ic.getSize() : new Dimension(icon.getIconWidth(), icon.getIconHeight()); 
    }

    @Override
    public void paintStaticLayer(Graphics2D g, JTComponent c)
    {
        ImageIcon icon = ((JTImage) c).getIcon();
        
        if (icon == null)
            paintNoIcon(g, c);
        else
        {
            // check if we have to scale
            int sw = icon.getIconWidth();
            int sh = icon.getIconHeight();
            
            if (sw<=0 || sh<=0)
            {
                paintNoIcon(g, c);
            }
            else
            {
                int dw = c.getWidth();
                int dh = c.getHeight();
                
                if (dw<=0 || dh<=0)
                    return;
                
                if (dw != sw || dh!=sh)
                {
                    g.scale(dw/(double)sw, dh/(double)sh);
                    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                    
                }
                icon.paintIcon(c, g, 0, 0);
            }
        }
    }

    private void paintNoIcon(Graphics2D g, JTComponent c)
    {
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, c.getWidth()-1, c.getHeight()-1);
        g.drawLine(0, 0, c.getWidth()-1, c.getHeight()-1);
        g.drawLine(c.getWidth()-1, 0, 0, c.getHeight()-1);
    }
    
}

