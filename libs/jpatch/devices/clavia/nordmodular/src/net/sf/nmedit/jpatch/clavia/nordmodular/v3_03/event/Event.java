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
 * Created on Sep 7, 2006
 */
package net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event;

import net.sf.nmedit.jnmprotocol.MidiMessage;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Connector;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Knob;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.MidiController;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Module;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Morph;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Parameter;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Patch;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.VoiceArea;

public abstract class Event
{
    
    // patch
    public final static int ID_PATCH_PROPERTY_CHANGED = 0;
    
    // voice area
    public final static int ID_MODULE_REMOVED = 10;
    public final static int ID_MODULE_ADDED = 11;
    public final static int ID_CABLES_ADDED = 12;
    public final static int ID_CABLES_REMOVED = 13;
    public final static int ID_VOICE_AREA_RESIZED = 14;
    public static final int ID_CABLE_GRAPH_UPDATE = 15;
    
    // module
    public final static int ID_MODULE_MOVED = 20;
    public final static int ID_MODULE_RENAMED = 21;

    // connector
    public final static int ID_CONNECTOR_STATE_CHANGED = 30;

    // parameter 
    public final static int ID_PARAMETER_VALUE_CHANGED = 40;
    public final static int ID_PARAMETER_MORPHVALUE_CHANGED = 41;
    public final static int ID_PARAMETER_KNOB_ASSIGNMENT_CHANGED = 42;
    public final static int ID_PARAMETER_MORPH_ASSIGNMENT_CHANGED = 43;
    public final static int ID_PARAMETER_MIDICONTROLLER_ASSIGNMENT_CHANGED = 44;

    // header
    public final static int ID_HEADER_VALUE_CHANGED = 50;
    
    // morphs
    public final static int ID_MORPH_ASSIGNED = 51;
    public final static int ID_MORPH_DEASSIGNED = 52;
    public final static int ID_MORPH_VALUE_CHANGED = 53;
    public final static int ID_MORPH_KEYBOARDASSIGNMENT_CHANGED = 54;

    public static final int ID_KNOB_ASSIGNMENT_CHANGED = 60;

    public static final int ID_MIDICTRL_ASSIGNMENT_CHANGED =61;
    
    // custom

    private int eventID = -1;
    
    public Event(int eventID)
    {
        this.eventID = eventID;
    }
    
    public abstract Patch getPatch();
    
    public int getID()
    {
        return eventID;
    }
    
    public Module getModule()
    {
        return null;
    }

    public VoiceArea getVoiceArea()
    {
        Module m = getModule();
        return m != null ? m.getVoiceArea() : null;
    }

    public Connector getConnector()
    {
        return getConnector1();
    }

    public Connector getConnector1()
    {
        return null;
    }
    
    public Connector getConnector2()
    {
        return null;
    }
    
    public Parameter getParameter()
    {
        return null;
    }
    
    public MidiMessage createMidiMessage()
    {
        throw new UnsupportedOperationException();
    }
    
    public String getPropertyName()
    {
        return null;
    }
    
    public Object getOldValue()
    {
        return null;
    }
    
    public Object getNewValue()
    {
        return null;
    }

    public int getIndex()
    {
        return -1;
    }
    
    public Morph getMorph()
    {
        return null;
    }

    public Knob getKnob()
    {
        return null;
    }
    
    public MidiController getMidiController()
    {
        return null;
    }

}
