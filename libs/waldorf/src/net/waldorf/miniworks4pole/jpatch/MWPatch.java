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
package net.waldorf.miniworks4pole.jpatch;

import java.util.Iterator;

import net.sf.nmedit.jpatch.ComponentDescriptor;
import net.sf.nmedit.jpatch.ConnectionManager;
import net.sf.nmedit.jpatch.DefaultModule;
import net.sf.nmedit.jpatch.InvalidDescriptorException;
import net.sf.nmedit.jpatch.Module;
import net.sf.nmedit.jpatch.ModuleContainer;
import net.sf.nmedit.jpatch.ModuleDescriptor;
import net.sf.nmedit.jpatch.Patch;
import net.sf.nmedit.jpatch.event.ModuleContainerListener;
import net.sf.nmedit.jpatch.spec.ModuleDescriptions;
import net.sf.nmedit.nmutils.iterator.ArrayIterator;

public class MWPatch implements Patch
{
    
    private ModuleDescriptions moduleDescriptions;
    private MWContainer container;
    private int programNumber = -1;

    public MWPatch(ModuleDescriptions moduleDescriptions)
    {
        this.moduleDescriptions = moduleDescriptions;
        container = new MWContainer(this, new DefaultModule(moduleDescriptions.get(0)));
    }

    public void setProgramNumber(int programNumber)
    {
        this.programNumber = programNumber;
    }

    public DefaultModule getMiniworksModule()
    {
        return container.miniworks;
    }
    
    public ModuleContainer getModuleContainer()
    {
        return container;
    }

    public String getName()
    {
        return (programNumber>=1&&programNumber<40)? "P."+programNumber:"P.?";
    }

    public String getVersion()
    {
        return null;
    }

    private static class MWContainer implements ModuleContainer
    {
        
        private DefaultModule miniworks;
        private MWPatch patch;
        
        public MWContainer(MWPatch patch, DefaultModule miniworks)
        {
            this.patch = patch;
            this.miniworks = miniworks;
        }

        public void add(Module module)
        {
            throw new UnsupportedOperationException();
        }

        public void addModuleContainerListener(ModuleContainerListener l)
        {
            // ignore
        }

        public boolean contains(Module module)
        {
            return miniworks == module;
        }

        public Module createModule(ModuleDescriptor descriptor) throws InvalidDescriptorException
        {
            throw new UnsupportedOperationException();
        }

        public ConnectionManager getConnectionManager()
        {
            return null;
        }

        public int getModuleCount()
        {
            return 1;
        }

        public MWPatch getPatch()
        {
            return patch;
        }

        public void remove(Module module)
        {
            throw new UnsupportedOperationException();
        }

        public void removeModuleContainerListener(ModuleContainerListener l)
        {
            // ignore
        }

        public ComponentDescriptor getDescriptor()
        {
            // TODO Auto-generated method stub
            return null;
        }

        public String getName()
        {
            // TODO Auto-generated method stub
            return null;
        }

        public Iterator<Module> iterator()
        {
            return new ArrayIterator<Module>(new Module[]{miniworks});
        }
        
    }
    
}
