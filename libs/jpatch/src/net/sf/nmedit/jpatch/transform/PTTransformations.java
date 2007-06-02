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
package net.sf.nmedit.jpatch.transform;

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleDescriptor;

/**
 * Contains the mappings which describe how one module
 * can be transformed into another one without loosing the parameter values 
 * and connections. 
 * 
 * @author Christian Schneider
 */
public interface PTTransformations
{

    /**
     * Returns the possible transformation targets for the specified module. 
     * @param source the source module 
     * @return the possible transformation targets
     */
    PModuleDescriptor[] getTargets(PModuleDescriptor source);

    /**
     * Returns all transformations (mappings) for the specified module.
     * @param source the source module
     * @return all transformations (mappings) for the specified module
     */
    PTModuleMapping[] getMappings(PModuleDescriptor source);

    /**
     * Returns the mapping for the specified source and destination module
     * 
     * @param source the source module 
     * @param destination the destination module
     * @return the mapping of the specified modules or null of no such mapping exists
     */
    PTModuleMapping getMapping(PModuleDescriptor source, PModuleDescriptor destination);

    /**
     * Returns the mapping for the specified source and destination module
     * 
     * @param source the source module 
     * @param destination the destination module
     * @return the mapping of the specified modules or null of no such mapping exists
     */
    PTModuleMapping getMapping(PModule source, PModuleDescriptor destination);
    
}
