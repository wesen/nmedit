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
package net.sf.nmedit.jpatch.impl;

import net.sf.nmedit.jpatch.ModuleDescriptions;
import net.sf.nmedit.jpatch.PFactory;
import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleContainer;
import net.sf.nmedit.jpatch.PModuleDescriptor;
import net.sf.nmedit.jpatch.PModuleMetrics;
import net.sf.nmedit.jpatch.PPatch;
import net.sf.nmedit.jpatch.PSettings;
import net.sf.nmedit.jpatch.history.History;
import net.sf.nmedit.jpatch.history.HistoryImpl;

public class PBasicPatch implements PPatch
{

    private ModuleDescriptions moduleDescriptions;
    private PFactory pfactory;
    private History history;
    private String name;

    public PBasicPatch(ModuleDescriptions moduleDescriptions)
    {
        this(moduleDescriptions, PBasicFactory.getSharedInstance());
    }

    public PBasicPatch(ModuleDescriptions moduleDescriptions, PFactory pfactory)
    {
        this.moduleDescriptions = moduleDescriptions;
        this.pfactory = pfactory;
        this.history = createHistory();
    }

    protected History createHistory()
    {
        return new HistoryImpl();
    }

    public ModuleDescriptions getModuleDescriptions()
    {
        return moduleDescriptions;
    }
    
    public PModule createModule(PModuleDescriptor d)
    {
        return getComponentFactory().createModule(d);
    }

    public PFactory getComponentFactory()
    {
        return pfactory;
    }
    
    public History getHistory()
    {
        return history;
    }

    public PModuleContainer getModuleContainer(int index)
    {
        throw new UnsupportedOperationException("not implemented");
    }

    public int getModuleContainerCount()
    {
        throw new UnsupportedOperationException("not implemented");
    }

    public PModuleMetrics getModuleMetrics()
    {
        return getModuleDescriptions().getMetrics();
    }

    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }

    public PSettings getSettings()
    {
        throw new UnsupportedOperationException("not implemented");
    }

}
