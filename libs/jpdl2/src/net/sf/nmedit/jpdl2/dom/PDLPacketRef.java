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
package net.sf.nmedit.jpdl2.dom;

/**
 * References another packet.
 */
public interface PDLPacketRef extends PDLItem
{

    /**
     * Returns the name of the referenced packet.
     * @return the name of the referenced packet
     */
    String getPacketName();
    
    /**
     * Returns the binding/identifier of the referenced packet.
     * @return the binding/identifier of the referenced packet
     */
    String getBinding();

    /**
     * Returns the referenced packet. 
     * @return the referenced packet
     */
    PDLPacketDecl getReferencedPacket();

    /**
     * Returns {@link PDLItemType#PacketRef},
     * {@link PDLItemType#PacketRefList} or
     * {@link PDLItemType#InlinePacketRef}
     * @return {@link PDLItemType#PacketRef},
     * {@link PDLItemType#PacketRefList} or
     * {@link PDLItemType#InlinePacketRef}
     */
    PDLItemType getType();

    public PDLMultiplicity getMultiplicity();
    
}
