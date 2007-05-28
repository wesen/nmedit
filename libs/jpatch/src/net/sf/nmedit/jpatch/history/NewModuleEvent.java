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

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleContainer;
import net.sf.nmedit.jpatch.PModuleDescriptor;
import net.sf.nmedit.jpatch.PatchUtils;

public class NewModuleEvent implements Event
{

    private PModuleDescriptor descriptor;
    private PModuleContainer container;
    private int x;
    private int y;
    private int moduleIndex;
    private int[] parameters;
    private String title;

    public NewModuleEvent(PModule m)
    {
        this.container = m.getParentComponent();
        this.descriptor = m.getDescriptor();
        this.x = m.getScreenX();
        this.y = m.getScreenY();
        this.moduleIndex = m.getComponentIndex();
        this.title = m.getTitle();
        parameters = PatchUtils.getParameterValues(m);
    }
    
    public void newModule()
    {
        PModule m = container.createModule(descriptor);
        m.setTitle(title);
        m.setScreenLocation(x, y);
        PatchUtils.setParameterValues(m, parameters);
        container.add(moduleIndex, m);
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
