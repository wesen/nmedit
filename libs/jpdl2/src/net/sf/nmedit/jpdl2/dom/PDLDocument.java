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
 * The PDL document containing all packet declarations.
 */
public interface PDLDocument extends Iterable<PDLPacketDecl>
{

    /**
     * Returns the name of the packet which the parser use to start parsing.
     * If null is returned then no stack packet was specified.
     * 
     * @return the name of the start packet
     */
    String getStartPacketName();
    
    /**
     * Returns the packet declaration with the specified packet name.
     * Returns null if the packet name is undefined.
     * 
     * @param packetName name of the packet
     * @return the packet declaration with the specified packet name
     */
    PDLPacketDecl getPacketDecl(String packetName);
    
    /**
     * Returns the referenced packet or null if the referenced packet is undefined
     * 
     * @param packetReference the packet reference
     * @return the referenced packet
     */
    PDLPacketDecl getPacketDecl(PDLPacketRef packetReference);
    
    /**
     * Returns the number of packets in this document.
     * @return the number of packets in this document
     */
    int getPacketCount();
    
}
