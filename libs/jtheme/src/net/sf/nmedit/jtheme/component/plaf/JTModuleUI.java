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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.border.Border;

import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.JTModule;

public class JTModuleUI extends JTComponentUI
{
    
    public static final String moduleBorder = "ModuleUI.Border";
    private static JTModuleUI instance = new JTModuleUI();
    
    public static JTModuleUI createUI(JComponent c)
    {
        return instance;
    }
    
    private Border border;

    public void installUI(JComponent c) 
    {
        if (border == null)
        {
            JTModule module = (JTModule) c;
            JTContext context = module.getContext();
            border = context.getUIDefaults().getBorder(moduleBorder);
        }

        if (border != null)
            c.setBorder(border);
    }
    
    private transient Insets cachedInsets;
    
    public void paintStaticLayer(Graphics2D g, JTComponent c)
    {
        if (c.isOpaque())
        {
            g.setColor(c.getBackground());
            
            Border border = c.getBorder();
            if (border == null || border.isBorderOpaque())
            {
                g.fillRect(0, 0, c.getWidth(), c.getHeight());
            }
            else
            {
                Insets i = cachedInsets = c.getInsets(cachedInsets);
                g.fillRect(i.left, i.top, c.getWidth()-(i.left+i.right-1), c.getHeight()-(i.top+i.bottom-1));
            }
        }
    }
    
    public void paintDynamicLayer(Graphics2D g, JTComponent c)
    {
        // no op
    }

    public void paintSelection(Graphics g, JTModule module)
    {
        
        if (module.isSelected())
        {
            // TODO lookup selection color
            
            final Color selection = Color.blue;
            g.setColor(selection);
            g.drawRect(0, 0, module.getWidth()-1, module.getHeight()-1);
        }
    }
}
