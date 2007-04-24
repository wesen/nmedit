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
package net.sf.nmedit.jpatch.history;

import net.sf.nmedit.jpatch.Module;
import net.sf.nmedit.jpatch.ModuleContainer;
import net.sf.nmedit.jpatch.ModuleDescriptor;

public class NewModuleEvent implements Event
{

    private ModuleDescriptor descriptor;
    private ModuleContainer container;
    private int x;
    private int y;
    private int uniqueId;
    private int[] parameters;
    private String title;

    public NewModuleEvent(Module m)
    {
        this.container = m.getParent();
        this.descriptor = m.getDescriptor();
        this.x = m.getScreenX();
        this.y = m.getScreenY();
        this.uniqueId = m.getUniqueId();
        this.title = m.getTitle();
        
        parameters = new int[descriptor.getParameterCount()];
        for (int i=descriptor.getParameterCount()-1;i>=0;i--)
            parameters[i] = m.getParameter(descriptor.getParameterDescriptor(i)).getValue();
    }
    
    public void newModule()
    {
        Module m = container.createModule(descriptor);
        m.setTitle(title);
        m.setUniqueId(uniqueId);        
        m.setScreenLocation(x, y);
        for (int i=descriptor.getParameterCount()-1;i>=0;i--)
            m.getParameter(descriptor.getParameterDescriptor(i)).setValue(parameters[i]);
        container.add(m);
    }

    public String getTitle()
    {
        return null;
    }

    public void perform()
    {
        newModule();
    }
    
}
