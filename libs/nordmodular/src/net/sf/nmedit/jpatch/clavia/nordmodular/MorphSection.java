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
 * Created on Dec 11, 2006
 */
package net.sf.nmedit.jpatch.clavia.nordmodular;

import java.awt.Point;
import java.util.Iterator;

import net.sf.nmedit.jpatch.ConnectionManager;
import net.sf.nmedit.jpatch.Connector;
import net.sf.nmedit.jpatch.ConnectorDescriptor;
import net.sf.nmedit.jpatch.InvalidDescriptorException;
import net.sf.nmedit.jpatch.Module;
import net.sf.nmedit.jpatch.ModuleContainer;
import net.sf.nmedit.jpatch.ModuleDescriptor;
import net.sf.nmedit.jpatch.Parameter;
import net.sf.nmedit.jpatch.ParameterDescriptor;
import net.sf.nmedit.jpatch.Patch;
import net.sf.nmedit.jpatch.event.ModuleContainerListener;
import net.sf.nmedit.jpatch.event.ModuleListener;
import net.sf.nmedit.nmutils.collections.EmptyIterator;

public class MorphSection implements Module
{
    
    private MorphSectionDescriptor descriptor;
    private Morph[] morphs;
    private MorphKBAssignment[] keyboard;
    private NMPatch patch;

    public MorphSection(NMPatch patch)
    {
        this.patch = patch;
        descriptor = MorphSectionDescriptor.getInstance();
        
        morphs = new Morph[4];
        keyboard = new MorphKBAssignment[4];
        
        for (int i=0;i<4;i++)
        {
            morphs[i] = new Morph(this, descriptor.getMorphDescriptor(i));
            keyboard[i] = new MorphKBAssignment(this, descriptor.getKeyboardAssignmentDescriptor(i));
        }
    }
    
    public Morph getMorph(int mid)
    {
        return morphs[mid];
    }
    
    public MorphKBAssignment getKeyboardAssignment(int mid)
    {
        return keyboard[mid];
    }

    public ModuleContainer getParent()
    {
        return null;
    }

    public MorphSectionDescriptor getDescriptor()
    {
        return descriptor;
    }

    public boolean hasConnections()
    {
        return false;
    }

    public boolean hasOutgoingConnections()
    {
        return false;
    }

    public void removeConnections()
    {

    }

    public Connector getConnector( ConnectorDescriptor descriptor )
            throws InvalidDescriptorException
    {
        throw new InvalidDescriptorException("Module has no connectors");
    }

    public int getConnectorCount()
    {
        return 0;
    }

    public Parameter getParameter( ParameterDescriptor descriptor ) throws InvalidDescriptorException
    {
        
        if (descriptor instanceof MorphDescriptor)
            return morphs[((MorphDescriptor)descriptor).getMorphId()];
        else if (descriptor instanceof MorphKBAssignmentDescriptor)
            return keyboard[((MorphKBAssignmentDescriptor)descriptor).getMorphId()];
        else
            throw new InvalidDescriptorException();
    }

    public int getParameterCount()
    {
        return morphs.length+keyboard.length;
    }

    public String getName()
    {
        return getDescriptor().getComponentName();
    }

    public Patch getPatch()
    {
        return patch;
    }

    public Module createModule( ModuleDescriptor descriptor )
            throws InvalidDescriptorException
    {
        throw new InvalidDescriptorException();
    }

    public void add( Module module )
    {
        // todo exception
        // no op
    }

    public void remove( Module module )
    {
        // no op
    }

    public int getModuleCount()
    {
        return 0;
    }

    public boolean contains( Module module )
    {
        return false;
    }

    public ConnectionManager getConnectionManager()
    {
        // no op
        return null;
    }

    public void addModuleContainerListener( ModuleContainerListener l )
    {
        // no op
    }

    public void removeModuleContainerListener( ModuleContainerListener l )
    {
        // no op
    }

    public Iterator<Module> iterator()
    {
        return new EmptyIterator<Module>();
    }

    public void setScreenLocation( int x, int y )
    {
        // no op
    }

    public void setScreenLocation( Point location )
    {
        // no op
    }

    public Point getScreenLocation()
    {
        return new Point(getScreenX(), getScreenY());
    }

    public int getScreenX()
    {
        return -1;
    }

    public int getScreenY()
    {
        return -1;
    }

    public void addModuleListener( ModuleListener l )
    {
        // no op
    }

    public void removeModuleListener( ModuleListener l )
    {
        // no op
    }

    public int getMorphCount()
    {
        // TODO Auto-generated method stub
        return 4;
    }

}
