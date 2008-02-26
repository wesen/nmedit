/*
    Protocol Definition Language
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
package net.sf.nmedit.jpdl2;

import net.sf.nmedit.jpdl2.stream.BitStream;

/**
 * The parse context object.
 */
public interface PDLParseContext
{
    
    /**
     * Returns the current bitstream. The bitstream
     * is either the data source which is parsed,
     * or it is currently generated.
     * @return the bitstream
     */
    BitStream getBitStream();
    
    /**
     * Returns the bit position of the label with the specified name.
     * @param name the label name
     * @return the bit position of the label
     */
    int getLabel(String name);
    
    /**
     * Returns true if the specified label exists.
     * @param name the label name
     * @return true if the specified label exists
     */
    boolean hasLabel(String name);
    
    /**
     * Returns the currently generated packet.
     * @return the currently generated packet
     */
    PDLPacket getPacket();
    
}
