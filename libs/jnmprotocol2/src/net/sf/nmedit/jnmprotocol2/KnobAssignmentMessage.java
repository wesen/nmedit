/*
    Nord Modular Midi Protocol 3.03 Library
    Copyright (C) 2003-2006 Marcus Andersson

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package net.sf.nmedit.jnmprotocol2;

import java.util.*;

import net.sf.nmedit.jnmprotocol2.MidiException;
import net.sf.nmedit.jnmprotocol2.MidiMessage;
import net.sf.nmedit.jnmprotocol2.NmProtocolListener;
import net.sf.nmedit.jpdl2.*;
import net.sf.nmedit.jpdl2.stream.BitStream;
import net.sf.nmedit.jpdl2.stream.IntStream;

/**
 * Notification about a changed knob assignment.
 * 
 * <p>There are three cases:</p>
 * <ol>
 * <li>a knob was assigned to a parameter which was not assigned to another knob</li>
 * <li>a knob was assigned to a parameter which was assigned to another knob</li>
 * <li>a knob was deassigned from a parameter</li>
 * </ol>
 * 
 * <p>The sc variable for each of the three cases:</p>
 * <ol>
 * <li>sc = 0x25</li>
 * <li>sc = 0x26</li>
 * <li>sc = 0x26</li>
 * </ol>
 * 
 * <p>Which parameters are accessible in which case ?</p>
 * 
 * <ol>
 * <li>PREVKNOB, SECTION, MODULE, PARAMETER, KNOB</li>
 * <li>SECTION, MODULE, PARAMETER, KNOB</li>
 * <li>PREVKNOB</li>
 * </ol>
 * 
 * @author Christian Schneider
 */
public class KnobAssignmentMessage extends MidiMessage
{

    private static final String PREVKNOB = "prevknob";
    private static final String SECTION = "section";
    private static final String MODULE = "module";
    private static final String PARAMETER = "parameter";
    private static final String KNOB = "knob";

    public KnobAssignmentMessage()
    {
	super();

	addParameter("pid", "data:pid");
	addParameter("sc", "data:sc");
    addParameter(PREVKNOB, "data:data:prevknob");
    
	set("cc", 0x14);
    }
    
    private void addParameters(boolean NewKnobAssignmentPacket)
    {
        // select 'NewKnobAssignmentPacket' or 'KnobAssignment' packet
        String prefix = NewKnobAssignmentPacket ? "data:data:" : "";
        addParameter(MODULE, prefix+"data:data:module");
        addParameter(PARAMETER, prefix+"data:data:parameter");
        addParameter(SECTION, prefix+"data:data:section");
        addParameter(KNOB, prefix+"data:data:knob");    
    }
    
    public KnobAssignmentMessage(PDLPacket packet)
    {
	this();
    
    addParameters(packet.containsPacket("NewKnobAssignmentPacket"));
	setAll(packet);
    }
    
    /**
     * Deassignes a knob from a parameter.
     * 
     * @param slot slot index
     * @param pid patch id
     * @param knobId the knob which will be deassigned
     */
    public void deassign(int slot, int pid, int knobId)
    {
        assign(slot, pid, knobId, -1, -1, -1, -1);
    }
    
    /**
     * Assigns, deassigns, or re-assigns a knob.
     * 
     * @param slot slot index
     * @param pid patch id
     * @param prevKnob previous knob index or &lt;0 if no knob was assigned to the parameter before
     * @param newKnob index of the knob which should to be assigned to the parameter or &lt;0 if no such assignment should happen 
     * @param section if newKnob&gt;=0 the section of the parameter, otherwise the argument is ignored
     * @param module if newKnob&gt;=0 the module index of the parameter, otherwise the argument is ignored
     * @param parameter if newKnob&gt;=0 the parameter index, otherwise the argument is ignored
     */
    public void assign(int slot, int pid, int prevKnob, int newKnob, int section, int module, int parameter)
    {
        if (prevKnob<0 && newKnob<0)
            throw new IllegalArgumentException("previous and new knob can not be -1 at the same time");

        addParameters(prevKnob>=0 && newKnob>=0);
        
        // editor to synth
        set("cc", 0x17);
        set("slot", slot);
        set("pid", pid);
        
        if (prevKnob<0)
        {
            // assign new knob
            set("sc", 0x25);
        }   
        else
        {
            // deassign or reassign knob
            set("sc", 0x26);
            set(PREVKNOB, prevKnob);
        }

        if (newKnob>=0)
        {
            set(KNOB, newKnob);
            set(MODULE, module);
            set(PARAMETER, parameter);
            set(SECTION, section);
        }
    }

    public BitStream getBitStream()
    throws MidiException
    {
        IntStream intStream = new IntStream();
        for (Iterator i = parameterNames(); i.hasNext(); ) 
        {
            int value = get((String)i.next(), -1);
            if (value>=0)
                intStream.append(value);
        }
    	
    	
        return getBitStream(intStream);
    }
    
    public void notifyListener(NmProtocolListener listener)
    {
	listener.messageReceived(this);
    }
}
