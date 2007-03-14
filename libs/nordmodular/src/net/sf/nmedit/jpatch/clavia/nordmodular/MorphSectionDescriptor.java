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

import java.util.Iterator;

import net.sf.nmedit.jpatch.ConnectorDescriptor;
import net.sf.nmedit.jpatch.ImageSource;
import net.sf.nmedit.jpatch.ModuleDescriptor;
import net.sf.nmedit.jpatch.ParameterDescriptor;
import net.sf.nmedit.nmutils.collections.EmptyIterator;

public class MorphSectionDescriptor implements ModuleDescriptor
{
    
    private MorphDescriptor[] morphs = new MorphDescriptor[4];
    private MorphKBAssignmentDescriptor[] kbassign = new MorphKBAssignmentDescriptor[4];
    
    private static final MorphSectionDescriptor instance = new MorphSectionDescriptor();

    public static MorphSectionDescriptor getInstance()
    {
        return instance;
    }

    public MorphSectionDescriptor()
    {
        for (int i=0;i<4;i++)
        {
            morphs[i] = new MorphDescriptor(this, i);
            kbassign[i] = new MorphKBAssignmentDescriptor(this, i);
        }
    }
    
    public int getConnectorCount()
    {
        return 0;
    }

    public ConnectorDescriptor getConnectorDescriptor( int index )
    {
        throw new IndexOutOfBoundsException();
    }

    public int getParameterCount()
    {
        return morphs.length+kbassign.length;
    }

    public MorphDescriptor getMorphDescriptor( int index )
    {
        return morphs[index];
    }
    
    public MorphKBAssignmentDescriptor getKeyboardAssignmentDescriptor( int index )
    {
        return kbassign[index];
    }

    public ParameterDescriptor getParameterDescriptor( int index )
    {
        return index<morphs.length ? morphs[index] : kbassign[index];
    }

    public ModuleDescriptor getSourceDescriptor()
    {
        return null;
    }

    public String getComponentName()
    {
        return "MorphSection";
    }

    public Object getAttribute( String name )
    {
        return null;
    }

    public Object[] getAttributes()
    {
        return MorphDescriptor.EMPTY;
    }

    public int getAttributeCount()
    {
        return 0;
    }

    public String getName()
    {
        return getClass().getName();
    }

    public String toString()
    {
        return "MorphSection";
    }

    public ParameterDescriptor[] getParameterDescriptorList( String parameterClass )
    {
        if (MorphDescriptor.PARAMETER_CLASS.equals(parameterClass))
            return morphs.clone();
        else if (MorphKBAssignmentDescriptor.PARAMETER_CLASS.equals(parameterClass))
            return kbassign.clone();
        else return NO_PARAMETER_DESCRIPTORS;
    }

    public ConnectorDescriptor[] getConnectorDescriptorList( String connectorClass )
    {
        return NO_CONNECTOR_DESCRIPTORS;
    }

    public String getDisplayName()
    {
        return "MorphSection";
    }

    public int getIndex()
    {
        return -1;
    }

    public ImageSource getImage( String key )
    {
        return null;
    }

    public Iterator<ImageSource> getImages()
    {
        return new EmptyIterator<ImageSource>();
    }

    public ParameterDescriptor getParameter( int index )
    {
        throw new IndexOutOfBoundsException();
    }

    public ParameterDescriptor getParameter( int index, String pclass )
    {
        /*if ("morph".equals(pclass))
            return getMorphDescriptor(index);
            kb parameter*/
        throw new IndexOutOfBoundsException();
    }

    public ConnectorDescriptor getConnector( int index )
    {
        throw new IndexOutOfBoundsException();
    }

    public ConnectorDescriptor getConnector( int index, String cclass )
    {
        throw new IndexOutOfBoundsException();
    }
    
    public ConnectorDescriptor getConnector( int index, boolean output )
    {
        throw new IndexOutOfBoundsException();
    }

    public String getCategory()
    {
        return "Morph";
    };
    
}
