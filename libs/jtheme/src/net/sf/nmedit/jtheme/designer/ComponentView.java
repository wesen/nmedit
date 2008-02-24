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
package net.sf.nmedit.jtheme.designer;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JComponent;

/**
 * Shows the view component and allows to select the view's 
 * child components to modify their position / size. 
 * 
 * @author Christian Schneider
 */
public class ComponentView extends JComponent
{
    
    /**
     * 
     */
    private static final long serialVersionUID = 1038186567981622867L;
    private JComponent view;
    private ComponentBounds selectedComponent;

    public ComponentView()
    {
        super();
        setOpaque(false);
    }
    
    public JComponent getView()
    {
        return view;
    }
 
    public ComponentBounds getSelectedComponent()
    {
        return selectedComponent;
    }
    
    public void setSelectedComponent(ComponentBounds c)
    {
        this.selectedComponent = c;
    }
    
    public void setView(JComponent view)
    {
        JComponent oldView = this.view;
        if (oldView != view)
        {
            this.view = null;
            this.selectedComponent = null;
            if (oldView != null)
                uninstall(oldView);
            this.view = view;
            if (view != null)
                install(view);
        }
    }

    protected void uninstall(JComponent view)
    {
        setToolTipText(null);
        for (Component c: getComponents())
        {
            if (c instanceof ComponentBounds)
            {
                ComponentBounds cb = (ComponentBounds) c;
                cb.uninstall();
                remove(cb);
            }
        }
    }

    protected void install(JComponent view)
    {
        setToolTipText(view.getClass().getName());
        for (Component c: view.getComponents())
        {
            ComponentBounds cb = new ComponentBounds((JComponent)c);
            add(cb);
        }
    }
    
    protected void paintComponent(Graphics g)
    {
        JComponent c = this.view;
        if (c == null)
            return;
        
        Graphics gg = g.create();
        try
        {
            gg.translate(-c.getX(), -c.getY());
            
            try
            {
                super.add(c);
                c.paint(gg);
            }
            finally
            {
                super.remove(c);
            }
            
            ComponentBounds.drawOutline(gg, 0, 0, view.getWidth(), view.getHeight());
            
        }
        finally
        {
            gg.dispose();
        }
    }
    
    
}
