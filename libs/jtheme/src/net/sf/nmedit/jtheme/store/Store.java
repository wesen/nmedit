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
package net.sf.nmedit.jtheme.store;

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.JTException;
import net.sf.nmedit.jtheme.component.JTComponent;

import org.jdom.Element;

public abstract class Store
{
    
    private boolean reducible = false;
    protected String name;
    
    public boolean isReducible()
    {
        return reducible; 
    }
    
    protected void setReducible(JTComponent c)
    {
        this.reducible = c.isReducible();
    }

    public static Store create(StorageContext context, Element xmlElement)
      throws JTException
    {
        throw new UnsupportedOperationException();
    }
    
    public abstract JTComponent createComponent(JTContext context)
      throws JTException;
    
    public JTComponent createComponent(JTContext context, PModule module)
      throws JTException
    {
        JTComponent component = createComponent(context);
        if (module != null)
            link(context, component, module);
        return component;
    }

    protected void link(JTContext context, JTComponent component, PModule module)
      throws JTException
    {
        // no such operation
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getName()
    {
        return name;
    }
    
}

