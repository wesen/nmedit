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
 * Created on Apr 21, 2006
 */
package net.sf.nmedit.nomad.patch.virtual.event;

import net.sf.nmedit.nomad.patch.virtual.Custom;

/**
 * Event sent by a {@link net.sf.nmedit.nomad.patch.virtual.Custom}.
 * 
 * @author Christian Schneider
 */
public class CustomEvent extends PatchEvent
{

    private Custom custom = null;

    public void valueChanged( Custom custom )
    {
        setID(CUSTOM_VALUE_CHANGED);
        this.custom = custom;
    }

    public Custom getCustom()
    {
        return custom;
    }

}
