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

import net.sf.nmedit.jpatch.PModuleContainerDescriptor;

/**
 * The reference implementation of interface {@link PModuleContainerDescriptor}.
 * @author Christian Schneider
 */
public class PBasicModuleContainerDescriptor extends PBasicDescriptor implements
        PModuleContainerDescriptor//, Serializable
{

    private static final long serialVersionUID = 3842506879258093343L;

    public PBasicModuleContainerDescriptor(String name, Object componentId)
    {
        super(name, componentId);
    }

    /*
    private void writeObject(java.io.ObjectOutputStream out) throws IOException
    {
        out.defaultWriteObject();
    }
    
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
    }
    */
}
