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

import javax.swing.JComponent;

import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.component.JTComponent;

public class UIInstance<T extends JTComponentUI>
{
    
    private Class<T> uiclass;
    
    private transient JTContext currentContext;
    private transient T currentUI;
    private transient String name;

    public UIInstance(Class<T> uiclass)
    {
        this.uiclass = uiclass;
    }
    
    public Class<T> getUIClass()
    {
        return uiclass;
    }
    
    public String getUIClassName()
    {
        if (name == null)
            name = uiclass.getName()+"$ui-instance";
        return name;
    }
    
    public T getInstance(JComponent c)
    {
        return getInstance(((JTComponent) c).getContext());
    }
    
    public T getInstance(JTComponent c)
    {
        return getInstance(c.getContext());
    }
    
    public T getInstance(JTContext context)
    {
        if (context != null)
        {
            if (context == currentContext && currentUI != null) return currentUI;
            
            Object ui = context.getUIDefaults().get(getUIClassName());
            
            if (ui != null && uiclass.isInstance(ui))
            {
                currentContext = context;
                currentUI = uiclass.cast(ui);
                return currentUI;
            }
        }
        return null;
    }
    
    public void setInstance(JTContext context, T instance)
    {
        if (context == null) return;
        context.getUIDefaults().put(getUIClassName(), instance);
        currentContext = context;
        currentUI = instance;
    }

    public void setInstance(JComponent c, T instance)
    {
        setInstance(((JTComponent)c).getContext(), instance);
    }
    
}
