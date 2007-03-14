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
package net.sf.nmedit.jtheme.editor;

import javax.swing.DefaultListModel;

import net.sf.nmedit.jtheme.JTContext;

public class ComponentListModel extends DefaultListModel
{
    
    private JTContext context;

    public ComponentListModel(JTContext context)
    {
        setContext(context);
    }
    
    public ComponentListModel()
    {
        this(null);
    }
    
    public void setContext(JTContext context)
    {
        JTContext oldContext = this.context;
        if (oldContext != context)
        {
            if (oldContext != null)
                uninstallContext(oldContext);
            this.context = context;
            if (context != null)
                installContext(context);
        }
    }

    protected void uninstallContext(JTContext context)
    {
        clear();
    }

    protected void installContext(JTContext context)
    {
        Class<?>[] clist = context.getComponentClasses();
        ensureCapacity(clist.length);
        for (Class<?> c : clist)
            addElement(c);
    }

    public JTContext getContext()
    {
        return context;
    }
    
}

