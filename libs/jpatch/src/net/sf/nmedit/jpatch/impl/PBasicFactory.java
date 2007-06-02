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

import net.sf.nmedit.jpatch.PFactory;
import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleDescriptor;

/**
 * The reference implementation of interface {@link PFactory}.
 * @author Christian Schneider
 */
public class PBasicFactory implements PFactory
{
    
    private static final PBasicFactory instance = new PBasicFactory();
    
    public static PBasicFactory getSharedInstance()
    {
        return instance;
    }

    public PModule createModule(PModuleDescriptor descriptor)
    {
        if (descriptor.isInstanciable())
            return new PBasicModule(descriptor);
        else throw new IllegalArgumentException("module is not instanciable: "+descriptor);
    }

}
