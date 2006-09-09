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
 * Created on Sep 8, 2006
 */
package net.sf.nmedit.jtheme;

import java.awt.Component;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;

public class ComponentHook extends ComponentConfiguration implements ContainerListener
{
    
    private Map<JComponent, ComponentConfiguration> 
        hooks = new HashMap<JComponent, ComponentConfiguration>();
    private boolean installed = true;

    public ComponentHook( ThemeConfiguration config, JComponent component )
    {
        super( config, component );
        hooks.put(component, this);
        component.addContainerListener(this);
        installToChildren(component);
    }
    
    private void installToChildren(JComponent parent)
    {
        for (Component cc : parent.getComponents())
        {
            JComponent c = (JComponent) cc;
            hooks.put(c, new ComponentConfiguration(getThemeConfiguration(), c));
            installToChildren(c);
            c.addContainerListener(this);
        }
    }
    
    private void uninstall(JComponent c)
    {
        c.removeContainerListener(this);
        hooks.remove(c);
        for (Component cc : c.getComponents())
            uninstall((JComponent)cc);
    }

    public void uninstallHook()
    {
        if (installed)
        {
            uninstall(getComponent());
            installed = false;
        }
    }

    public void componentAdded( ContainerEvent e )
    {
        JComponent c = (JComponent) e.getComponent();
        hooks.put(c, new ComponentConfiguration(getThemeConfiguration(), c));
        installToChildren(c);
        c.addContainerListener(this);
    }

    public void componentRemoved( ContainerEvent e )
    {
        uninstall((JComponent)e.getComponent());
    }

    public boolean isChanged()
    {
        if (super.isChanged())
            return true;
        for (JComponent c : hooks.keySet())
            if (c!=getComponent() && hooks.get(c).isChanged())
                return true;
        return false;
    }

    public ComponentConfiguration getComponentConfiguration( JComponent c )
    {
        return hooks.get(c);
    }
    
}
