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
package net.sf.nmedit.jpdl2.impl;

import net.sf.nmedit.jpdl2.dom.PDLDocument;
import net.sf.nmedit.jpdl2.dom.PDLItemType;
import net.sf.nmedit.jpdl2.dom.PDLMultiplicity;
import net.sf.nmedit.jpdl2.dom.PDLPacketDecl;
import net.sf.nmedit.jpdl2.dom.PDLPacketRef;
import net.sf.nmedit.jpdl2.dom.PDLPacketRefList;
import net.sf.nmedit.jpdl2.utils.PDLUtils;

public class PDLPacketRefListImpl extends PDLItemImpl implements PDLPacketRefList
{
    
    private PDLMultiplicity multiplicity;
    private PDLPacketRef packetRef;

    public PDLPacketRefListImpl(PDLPacketRef packetRef, PDLMultiplicity multiplicity)
    {
        this.packetRef = packetRef;
        this.multiplicity = multiplicity;
    }
    
    public PDLPacketRefListImpl(PDLDocument document, String packetName, String binding, PDLMultiplicity multiplicity)
    {
        this(new PDLPacketRefImpl(document, packetName, binding), multiplicity);
    }
    
    public PDLMultiplicity getMultiplicity()
    {
        return multiplicity;
    }

    public PDLItemType getType()
    {
        return PDLItemType.PacketRefList;
    }

    public PDLPacketRef getPacketRef()
    {
        return packetRef;
    }

    public String getBinding()
    {
        return packetRef.getBinding();
    }

    public String getPacketName()
    {
        return packetRef.getPacketName();
    }

    public PDLPacketDecl getReferencedPacket()
    {
        return packetRef.getReferencedPacket();
    }

    public int getMinimumSize()
    {
        return PDLUtils.getMinMultiplicity(multiplicity) * getReferencedPacket().getMinimumSize();
    }

    public int getMinimumCount()
    {
        return PDLUtils.getMinMultiplicity(multiplicity) * getReferencedPacket().getMinimumCount();
    }
    
    public String toString()
    {
        return multiplicity +"*"+ packetRef;
    }

}
