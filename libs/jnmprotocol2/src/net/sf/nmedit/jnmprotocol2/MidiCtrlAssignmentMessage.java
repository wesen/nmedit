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
 * Notification about a changed midi controller assignment.
 * 
 * <p>There are three cases:</p>
 * <ol>
 * <li>a ctrl was assigned to a parameter which was not assigned to another ctrl</li>
 * <li>a ctrl was assigned to a parameter which was assigned to another ctrl</li>
 * <li>a ctrl was deassigned from a parameter</li>
 * </ol>
 * 
 * <p>The sc variable for each of the three cases:</p>
 * <ol>
 * <li>sc = 0x22</li>
 * <li>sc = 0x23</li>
 * <li>sc = 0x23</li>
 * </ol>
 * 
 * <p>Which parameters are accessible in which case ?</p>
 * 
 * <ol>
 * <li>PREV_MIDI_CTRL, SECTION, MODULE, PARAMETER, MIDI_CTRL</li>
 * <li>SECTION, MODULE, PARAMETER, MIDI_CTRL</li>
 * <li>PREV_MIDI_CTRL</li>
 * </ol>
 * 
 * @author Christian Schneider
 */
public class MidiCtrlAssignmentMessage extends MidiMessage
{
//PREV_MIDI_CTRL
    private static final String PREV_MIDI_CTRL = "prevmidictrl";
    private static final String SECTION = "section";
    private static final String MODULE = "module";
    private static final String PARAMETER = "parameter";
    private static final String MIDI_CTRL = "midictrl";

    public MidiCtrlAssignmentMessage()
    {
	super();

	addParameter("pid", "data:pid");
	addParameter("sc", "data:sc");
    addParameter(PREV_MIDI_CTRL, "data:data:prevmidictrl");
    
	set("cc", 0x14); // direction synth -> editor possible ???
    }
    
    private void addParameters(boolean NewMidiCtrlAssignmentPacket)
    {
        // select 'NewMidiCtrlAssignmentPacket' or 'MidiCtrlAssignment' packet
        String prefix = NewMidiCtrlAssignmentPacket ? "data:data:" : "";
        addParameter(SECTION, prefix+"data:data:section");
        addParameter(MODULE, prefix+"data:data:module");
        addParameter(PARAMETER, prefix+"data:data:parameter");
        addParameter(MIDI_CTRL, prefix+"data:data:midictrl");    
    }
    
    MidiCtrlAssignmentMessage(PDLPacket packet)
    {
	this();
    
    addParameters(packet.containsPacket("NewMidiCtrlAssignmentPacket"));
	setAll(packet);
    }
    
    /**
     * Deassignes a midi controller from a parameter.
     * 
     * @param slot slot index
     * @param pid patch id
     * @param prevMidiCtrl the midi controller which will be deassigned
     */
    public void deassign(int slot, int pid, int prevMidiCtrl)
    {
        assign(slot, pid, prevMidiCtrl, -1, -1, -1, -1);
    }
    
    /**
     * Assigns, deassigns, or re-assigns a midi controller.
     * 
     * @param slot slot index
     * @param pid patch id
     * @param prevMidiCtrl previous midi controller index or &lt;0 if no midi controller was assigned to the parameter before
     * @param newMidiCtrl index of the midi controller which should to be assigned to the parameter or &lt;0 if no such assignment should happen 
     * @param section if newMidiCtrl&gt;=0 the section of the parameter, otherwise the argument is ignored
     * @param module if newMidiCtrl&gt;=0 the module index of the parameter, otherwise the argument is ignored
     * @param parameter if newMidiCtrl&gt;=0 the parameter index, otherwise the argument is ignored
     */
    public void assign(int slot, int pid, int prevMidiCtrl, int newMidiCtrl, int section, int module, int parameter)
    {
        if (prevMidiCtrl<0 && newMidiCtrl<0)
            throw new IllegalArgumentException("previous and new midi controller can not be -1 at the same time");

        addParameters(prevMidiCtrl>=0 && newMidiCtrl>=0);
        
        // editor to synth
        set("cc", 0x17);
        set("slot", slot);
        set("pid", pid);
        
        if (prevMidiCtrl<0)
        {
            // assign new midi controller
            set("sc", 0x22);
        }   
        else
        {
            // deassign or reassign midi controller
            set("sc", 0x23);
            set(PREV_MIDI_CTRL, prevMidiCtrl);
        }

        if (newMidiCtrl>=0)
        {
            set(MIDI_CTRL, newMidiCtrl);
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
        // direction synth->editor not supported
    }
}
