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
package net.sf.nmedit.jpatch.clavia.nordmodular.event;

import net.sf.nmedit.jpatch.clavia.nordmodular.NMPatch;
import net.sf.nmedit.jpatch.event.PPatchEvent;

public class PPatchSettingsEvent extends PPatchEvent
{
    
    /**
     * 
     */
    private static final long serialVersionUID = -5701325511021253604L;
    public static final int PATCH_SETTINGS_CHANGED = CUSTOM_EVENT_START+10;

    public PPatchSettingsEvent(NMPatch patch)
    {
        super(patch, PATCH_SETTINGS_CHANGED, null);
    }

    public NMPatch getPatch()
    {
        return (NMPatch) target;
    }
    
}
