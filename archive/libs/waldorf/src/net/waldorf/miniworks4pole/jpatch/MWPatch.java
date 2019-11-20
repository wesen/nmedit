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

import net.sf.nmedit.jpatch.CopyOperation;
import net.sf.nmedit.jpatch.ModuleDescriptions;
import net.sf.nmedit.jpatch.MoveOperation;
import net.sf.nmedit.jpatch.PConnectionManager;
import net.sf.nmedit.jpatch.PFactory;
import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleContainer;
import net.sf.nmedit.jpatch.PModuleDescriptor;
import net.sf.nmedit.jpatch.PPatch;
import net.sf.nmedit.jpatch.PSettings;
import net.sf.nmedit.jpatch.event.PModuleContainerListener;
import net.sf.nmedit.jpatch.history.History;
import net.sf.nmedit.jpatch.impl.PBasicModule;
import net.sf.nmedit.jpatch.impl.PBasicModuleContainer;
import net.sf.nmedit.jpatch.impl.PBasicPatch;

public class MWPatch extends PBasicPatch implements PPatch
{
    
    private MWContainer container;
    private int programNumber = -1;

    public MWPatch(ModuleDescriptions moduleDescriptions)
    {
        super(moduleDescriptions);
        container = new MWContainer(this, new PBasicModule(moduleDescriptions.getModuleById(""+0)));
    }

    public void setProgramNumber(int programNumber)
    {
        this.programNumber = programNumber;
    }

    public PBasicModule getMiniworksModule()
    {
        return container.miniworks;
    }
    
    public PModuleContainer getModuleContainer()
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

    private static class MWContainer extends PBasicModuleContainer
    {
        
        private PBasicModule miniworks;
        private MWPatch patch;
        
        public MWContainer(MWPatch patch, PBasicModule miniworks)
        {
            super(patch, "module", 0);
            this.miniworks = miniworks;
        }

        public boolean add(PModule module)
        {
            throw new UnsupportedOperationException();
        }

        public boolean add(int index, PModule module)
        {
            throw new UnsupportedOperationException();
        }

        public void addModuleContainerListener(PModuleContainerListener l)
        {
            // ignore
        }


        public PModule createModule(PModuleDescriptor descriptor) 
        {
            throw new UnsupportedOperationException();
        }

        public PConnectionManager getConnectionManager()
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

        public boolean remove(PModule module)
        {
            throw new UnsupportedOperationException();
        }

        public void removeModuleContainerListener(PModuleContainerListener l)
        {
            // ignore
        }

        public String getName()
        {
            // TODO Auto-generated method stub
            return null;
        }

        public MoveOperation createMoveOperation()
        {
            throw new UnsupportedOperationException("operation 'move' not supported");
        }

        public CopyOperation createCopyOperation()
        {
            throw new UnsupportedOperationException("operation 'copy' not supported");
        }

    }

    public History getHistory()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public PModule createModule(PModuleDescriptor d)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public PFactory getComponentFactory()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public PModuleContainer getModuleContainer(int index)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public int getModuleContainerCount()
    {
        return 1;
    }

    public PSettings getSettings()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
}
