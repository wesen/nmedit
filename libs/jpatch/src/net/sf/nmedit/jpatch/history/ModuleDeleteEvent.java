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

public class ModuleDeleteEvent implements Event
{

    private PModuleContainer container;
    private int moduleIndex;

    public ModuleDeleteEvent(PModule m)
    {
        this.container = m.getParentComponent();
        this.moduleIndex = m.getComponentIndex();
    }
    
    public void deleteModule()
    { 
        container.remove(container.getModule(moduleIndex));
    }

    public String getTitle()
    {
        return null;
    }

    public void perform()
    {
        deleteModule();
    }
    
}
