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
package net.sf.nmedit.nordmodular;

import net.sf.nmedit.jpatch.clavia.nordmodular.NM1ModuleDescriptions;
import net.sf.nmedit.jpatch.spec.ModuleDescriptions;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.store.DefaultStorageContext;

public class NMContext
{
    
    private NM1ModuleDescriptions md;
    private JTContext ct;
    private DefaultStorageContext dsc;

    public NMContext(ModuleDescriptions md, JTContext ct, DefaultStorageContext dsc)
    {
        this.md = (NM1ModuleDescriptions) md;
        this.ct = ct;
        this.dsc = dsc;
    }

    public NM1ModuleDescriptions getModuleDescriptions()
    {
        return md;
    }
    
    public JTContext getContext()
    {
        return ct;
    }
    
    public DefaultStorageContext getStorageContext()
    {
        return dsc;
    }
    
}

