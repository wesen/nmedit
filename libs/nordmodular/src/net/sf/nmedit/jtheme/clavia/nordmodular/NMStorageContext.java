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
package net.sf.nmedit.jtheme.clavia.nordmodular;

import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.clavia.nordmodular.store.EnvelopeStore;
import net.sf.nmedit.jtheme.clavia.nordmodular.store.ScrollbarSliderElement;
import net.sf.nmedit.jtheme.store.DefaultStorageContext;

public class NMStorageContext extends DefaultStorageContext
{

    public NMStorageContext(JTContext context, ClassLoader classLoader)
    {
        super(context, classLoader);
    }

    protected void installDefaults()
    {
        super.installDefaults();
        // using customized store class:
        installStore("adsr-envelope", EnvelopeStore.class);
        installStore("ahd-envelope", EnvelopeStore.class);
        installStore("adsr-mod-envelope", EnvelopeStore.class);
        installStore("ad-envelope", EnvelopeStore.class); // TODO use DefaultStore
        
        installStore("scrollbar", ScrollbarSliderElement.class);
    }
}
