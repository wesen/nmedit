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
package net.sf.nmedit.jtheme.reflect;

import java.util.HashSet;
import java.util.Set;

public class DefaultPropertyExclusionFilter implements PropertyExclusionFilter
{
    
    private Set<String> exclusiveSet;

    public DefaultPropertyExclusionFilter(Set<String> exclusiveSet)
    {
        this.exclusiveSet = exclusiveSet;
        if (this.exclusiveSet == null)
            this.exclusiveSet = new HashSet<String>();
    }

    public Set<String> getSet()
    {
        return exclusiveSet;
    }
    
    public boolean exlusive(String propertyName)
    {
        return exclusiveSet.contains(propertyName);
    }

}

