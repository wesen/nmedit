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

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Connector;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Header;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Knob;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.MidiController;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Module;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Morph;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Parameter;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Patch;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.VoiceArea;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.misc.Assignment;

public class EventBuilder
{
    
    private final static class VoiceAreaEvent extends Event
    {
        private VoiceArea va = null;
        public VoiceAreaEvent(VoiceArea va, int eventID)
        {
            super(eventID);
            this.va = va;
        }
        public VoiceArea getVoiceArea()
        {
            return va;
        }
        public Patch getPatch()
        {
            return va.getPatch();
        }
    }
    
    private final static class ModuleEvent extends Event
    {
        private Module module = null;
        private VoiceArea va;
        public ModuleEvent(Module module, int eventID)
        {
            this(module.getVoiceArea(), module, eventID);
        }
        public ModuleEvent( VoiceArea va, Module module, int eventID )
        {
            super(eventID);
            this.va = va;
            this.module = module;
        }
        public VoiceArea getVoiceArea()
        {
            return va;
        }
        public Module getModule()
        {
            return module;
        }
        public Patch getPatch()
        {
            return va.getPatch();
        }
    }
    
    private final static class ConnectorEvent extends Event
    {
        private Connector a, b;
        private VoiceArea va ;

        public ConnectorEvent(Connector a, Connector b, int eventID)
        {
            this(null, a, b, eventID);
        }

        public ConnectorEvent(VoiceArea va, Connector a, Connector b, int eventID)
        {
            super(eventID);
            this.va = va;
            this.a = a;
            this.b = b;
        }

        public Connector getConnector1()
        {
            return a;
        }

        public Connector getConnector2()
        {
            return b;
        }
        
        public VoiceArea getVoiceArea()
        {
            return va!= null ? va : (va=a.getModule().getVoiceArea());
        }
        public Patch getPatch()
        {
            return getVoiceArea().getPatch();
        }
    }
    
    private final static class ParameterEvent extends Event
    {
        private Parameter p;
        ParameterEvent(Parameter p, int eventID)
        {
            super(eventID);
            this.p = p;
        }
        
        public Parameter getParameter()
        {
            return p;
        }
        
        public Module getModule()
        {
            return p.getModule();
        }

        public Patch getPatch()
        {
            return getVoiceArea().getPatch();
        }
    }

    private final static class PatchEvent extends Event
    {
        
        private Patch patch;
        private String propertyName;
        private Object oldValue;
        private Object newValue;

        public PatchEvent( Patch patch, String propertyName, Object oldValue, Object newValue )
        {
            super( Event.ID_PATCH_PROPERTY_CHANGED );
            this.patch = patch ;
            this.propertyName = propertyName;
            this.oldValue = oldValue;
            this.newValue = newValue;
        }

        @Override
        public Patch getPatch()
        {
            return patch;
        }

        public String getPropertyName()
        {
            return propertyName;
        }
        
        public Object getOldValue()
        {
            return oldValue;
        }
        
        public Object getNewValue()
        {
            return newValue;
        }
        
    }

    private final static class AssignmentEvent extends Event
    {
        
        private Patch patch;
        private Knob knob = null;
        private MidiController mc = null;
        private Object oldValue;
        private Object newValue;

        public AssignmentEvent( Patch patch, Knob knob, Assignment oldValue, Assignment newValue )
        {
            super( Event.ID_KNOB_ASSIGNMENT_CHANGED );
            this.patch = patch ;
            this.knob = knob;
            this.oldValue = oldValue;
            this.newValue = newValue;
        }

        public AssignmentEvent( Patch patch, MidiController mc, Assignment oldValue, Assignment newValue )
        {
            super( Event.ID_MIDICTRL_ASSIGNMENT_CHANGED );
            this.patch = patch ;
            this.mc = mc;
            this.oldValue = oldValue;
            this.newValue = newValue;
        }
        
        public Knob getKnob()
        {
            return knob;
        }
        
        public MidiController getMidiController()
        {
            return mc;
        }

        @Override
        public Patch getPatch()
        {
            return patch;
        }

        public Object getOldValue()
        {
            return oldValue;
        }
        
        public Object getNewValue()
        {
            return newValue;
        }
        
    }
    
    private final static class HeaderEvent extends Event
    {
        
        private Header header;
        private int index;
        private int oldValue;
        private int newValue;

        public HeaderEvent( Header header, int index, int oldValue, int newValue )
        {
            super( Event.ID_HEADER_VALUE_CHANGED );
            this.header = header ;
            this.index = index;
            this.oldValue = oldValue;
            this.newValue = newValue;
        }

        @Override
        public Patch getPatch()
        {
            return header.getPatch();
        }

        public int getIndex()
        {
            return index;
        }
        
        public Object getOldValue()
        {
            return oldValue;
        }
        
        public Object getNewValue()
        {
            return newValue;
        }
        
    }
    
    private final static class MorphEvent extends Event
    {
        private Morph morph;

        public MorphEvent(int eventId, Morph m)
        {
            super(eventId);
            this.morph = m;
        }
        
        public Morph getMorph()
        {
            return morph;
        }

        @Override
        public Patch getPatch()
        {
            return morph.getPatch();
        }
    }

    public static Event moduleMoved(Module m)
    {
        return new ModuleEvent(m, Event.ID_MODULE_MOVED);
    }

    public static Event moduleRenamed(Module m)
    {
        return new ModuleEvent(m, Event.ID_MODULE_RENAMED);
    }

    public static Event moduleAdded(Module m)
    {
        return new ModuleEvent(m, Event.ID_MODULE_ADDED);
    }

    public static Event moduleRemoved(VoiceArea va, Module m)
    {
        return new ModuleEvent(va, m, Event.ID_MODULE_REMOVED);
    }

    public static Event voiceAreaResized(VoiceArea v)
    {
        return new VoiceAreaEvent(v, Event.ID_VOICE_AREA_RESIZED);
    }

    public static Event cableAdded(Connector a, Connector b)
    {
        return new ConnectorEvent(a, b, Event.ID_CABLES_ADDED);
    }

    public static Event cableRemoved(VoiceArea va, Connector a, Connector b)
    {
        return new ConnectorEvent(va, a, b, Event.ID_CABLES_REMOVED);
    }

    public static Event connectorStateChanged(Connector a)
    {
        return new ConnectorEvent(a, null, Event.ID_CONNECTOR_STATE_CHANGED);
    }
    
    public static Event parameterValueChanged(Parameter p)
    {
        return new ParameterEvent(p, Event.ID_PARAMETER_VALUE_CHANGED);
    }
    
    public static Event parameterMorphValueChanged(Parameter p)
    {
        return new ParameterEvent(p, Event.ID_PARAMETER_MORPHVALUE_CHANGED);
    }
    
    public static Event parameterMorphAssignmentChanged(Parameter p)
    {
        return new ParameterEvent(p, Event.ID_PARAMETER_MORPH_ASSIGNMENT_CHANGED);
    }
    
    public static Event parameterKnobAssignmentChanged(Parameter p)
    {
        return new ParameterEvent(p, Event.ID_PARAMETER_KNOB_ASSIGNMENT_CHANGED);
    }
    
    public static Event parameterMidiCtrlAssignmentChanged(Parameter p)
    {
        return new ParameterEvent(p, Event.ID_PARAMETER_MIDICONTROLLER_ASSIGNMENT_CHANGED);
    }
   
    public static Event patchPropertyChanged(Patch patch, String propertyName, Object oldValue, Object newValue)
    {
        return new PatchEvent(patch, propertyName, oldValue, newValue);
    }

    public static Event headerValueChanged( Header header, int index, int oldValue, int value )
    {
        return new HeaderEvent(header, index, oldValue, value);
    }

    public static Event morphAssigned( Morph morph )
    {
        return new MorphEvent(Event.ID_MORPH_ASSIGNED, morph);
    }

    public static Event morphDeassigned( Morph morph )
    {
        return new MorphEvent(Event.ID_MORPH_DEASSIGNED, morph);
    }

    public static Event morphValueChanged( Morph morph )
    {
        return new MorphEvent(Event.ID_MORPH_VALUE_CHANGED, morph);
    }

    public static Event morphKeyboardAssignmentChanged( Morph morph )
    {
        return new MorphEvent(Event.ID_MORPH_KEYBOARDASSIGNMENT_CHANGED, morph);
    }

    public static Event assignmentChanged(Patch patch, MidiController mctrl, Assignment oldValue, Assignment newValue )
    {
        return new AssignmentEvent(patch, mctrl, oldValue, newValue);
    }

    public static Event assignmentChanged(Patch patch, Knob knob, Assignment oldValue, Assignment newValue )
    {
        return new AssignmentEvent(patch, knob, oldValue, newValue);
    }

    public static Event cableGraphUpdate( Connector c )
    {
        return new ConnectorEvent(c, null , Event.ID_CABLE_GRAPH_UPDATE);
    }
    
}
