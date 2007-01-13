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

package net.sf.nmedit.jnmprotocol;

import java.util.*;
import net.sf.nmedit.jpdl.*;

/**
 * A message containing the names of consecutive patches
 * stored in the synthesizer.
 * 
 * The message has two parameters section and position
 * describing the location of the first patch.
 * 
 * Subsequent patches are at the next position (position+1).
 * 
 * If the patch name starts with the byte 0x01 then the second
 * byte (supplementary value) contains the absolute position
 * (position=getSupplementaryValue(name)), in the current section.
 * 
 * If the patch name starts with the byte 0x02 then the current
 * location is unused, thus the patch name does not exist.
 * 
 * If the patch name starts with the byte 0x03 then the 
 * second byte (supplementary value) contains the section
 * of the next patch name. (section=getSupplementaryValue(name),
 * position=0)
 */
public class PatchListMessage extends MidiMessage
{
    private LinkedList names;
    private boolean endoflist;

    public static final char END_OF_SECTION = 3;
    public static final char EMPTY_POSITION = 2;
    public static final char EMPTY_SEQUENCE = 1;

    public PatchListMessage()
	throws Exception
    {
	super();

	names = new LinkedList();

	addParameter("unknown", "data:patchList:unknown");
	addParameter("kind", "data:patchList:kind");
	addParameter("section", null);
	addParameter("position", null);
	set("cc", 0x17);
	set("slot", 0);

	isreply = true;
    }

    PatchListMessage(Packet packet)
	throws Exception
    {
	this();
	setAll(packet);
	
	Packet patchList = packet.getPacket("data:patchList:data");
	
	if (patchList != null) {
	    set("section", patchList.getVariable("section"));
	    set("position", patchList.getVariable("position"));

	    Packet list = patchList.getPacket("names");
	    while (list != null) {
		String name = "" + (char)list.getVariable("first");
		if (name.charAt(0) != 0x02) {
		    name += extractName(list.getPacket("name"));
		}
		names.add(name);
		list = list.getPacket("next");
	    }
	    endoflist = false;
	}
	else {
	    set("section", 0);
	    set("position", 0);
	    endoflist = true;
	}
    }

    public List getBitStream()
	throws Exception
    {
	throw new
	    MidiException("PatchListMessage::getBitStream not implemented.",
			  0);
    }
    
    public void notifyListener(NmProtocolListener listener)
	throws Exception
    {
	listener.messageReceived(this);
    }

    public List getNames()
    {
	return names;
    }

    public boolean isEndOfList()
    {
	return endoflist;
    }
    
    /**
     * Returns true if the specified name describes the section
     * of the next patch name.
     */
    public static boolean isEndOfSection(String name)
    {
	return name.length() == 2 && name.charAt(0) == END_OF_SECTION;
    }

    /**
     * Returns true if the patch name describes an unused location.
     */
    public static boolean isEmptyPosition(String name)
    {
	return name.length() == 1 && name.charAt(0) == EMPTY_POSITION;
    }

    /**
     * Returns true if the patch name contains info about the position.
     */
    public static boolean hasGapBefore(String name)
    {
	return name.length() >= 2 && name.charAt(0) == EMPTY_SEQUENCE;
    }
    
    /**
     * Removes the gap info from the specified name.
     * Use this only if hasGapBefore(name) returns true.
     */
    public static String removeGapInfoFromName(String name)
    {
        return name.substring(2);
    }

    /**
     * Returns the supplementary value.
     * The supplementary value is stored in the second character.
     * @throws IllegalArgumentException if isEndOfSection(name) and hasGapBefore(name) is false
     */
    public static int getSupplementaryValue(String name)
    {
        if (!(isEndOfSection(name)||hasGapBefore(name)))
          throw new IllegalArgumentException("no supplementary value:"+name);
	return (int)name.charAt(1);
    }
    
}
