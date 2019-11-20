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
package net.waldorf.miniworks4pole;

import net.sf.nmedit.jpatch.Formatter;
import net.sf.nmedit.jpatch.ModuleDescriptions;
import net.sf.nmedit.jpatch.PParameter;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.clavia.nordmodular.NMStorageContext;
import net.sf.nmedit.jtheme.store.DefaultStorageContext;
public class MWHelper
{

    public static ModuleDescriptions createModuleDescriptions()
    {
        ModuleDescriptions md = new ModuleDescriptions(null);
       //md.getFormatterRegistry().putFormatter("GateTimeFormatter", new GateTimeFormatter() );
     
        return md;
    }

    public static class GateTimeFormatter implements Formatter
    {

        public String getString(PParameter parameter, int value)
        {
            return Integer.toString(value);
        }

    }
    
    public static DefaultStorageContext createStorageContext(JTContext context, ClassLoader loader)
    {
        return new NMStorageContext(context, loader);
    }
    
}
