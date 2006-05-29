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

/**
 * The base event message class for events sent by 
 * the elements of a patch. 
 * 
 * @author Christian Schneider
 */
public abstract class PatchEvent
{

    public final static int MODULE_MOVED = 0;
    public final static int MODULE_RENAMED = 30;
    

    public final static int CABLE_CONNECTED = 1;
    public final static int CABLE_DISCONNECTED = 2;
    public final static int CABLE_COLOR_CHANGED = 3;
    public final static int CABLE_VISIBILITY_CHANGED = 4;

    public final static int PARAMETER_VALUE_CHANGED = 5;
    public final static int PARAMETER_MORPHRANGE_CHANGED = 6;

    public final static int CUSTOM_VALUE_CHANGED = 7;

    public final static int CONNECTOR_CONNECTED_STATE_CHANGED = 8;

    public final static int KNOB_ASSIGNMENT = 9;
    public final static int MIDICONTROLLER_ASSIGNMENT = 10;
    public final static int MORPH_ASSIGNMENT = 11;
    
    public final static int MORPH_VALUE_CHANGED = 12;

    public static final int MORPH_KEYBOARD_ASSIGNMENT_CHANGED = 13;

    public final static int HEADER_VALUE_CHANGED = 14;
    public final static int PATCH_NAME_CHANGED = 15;

    public final static int VA_MODULE_ADDED = 16;
    public final static int VA_MODULE_REMOVED = 17;

    public static final int VA_CONNECTED = 18;
    public static final int VA_DISCONNECTED = 19;
    public static final int VA_RESIZED = 20;
    public static final int VA_UPDATE_CONNECTION = 21;
    public static final int VA_BEGIN_UPDATE = 23;
    public static final int VA_END_UPDATE = 24;
    
    /**
     * The current event ID
     */
    private int ID;

    public PatchEvent()
    {
        this.ID = -1;
    }

    /**
     * Returns the ID of the event
     * @return the ID of the event
     */
    public int getID()
    {
        return ID;
    }

    /**
     * Sets the event ID
     * @param ID the event ID
     */
    protected void setID( int ID )
    {
        this.ID = ID;
    }
    
}
