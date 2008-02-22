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
import net.sf.nmedit.jpdl2.utils.PDLUtils;

public class PDLPacketRefImpl extends PDLItemImpl implements PDLPacketRef
{
    
    private PDLItemType type;
    private String packetName;
    private String binding;
    private PDLDocument document;
    private PDLMultiplicity multiplicity;

    public PDLPacketRefImpl(PDLDocument document, String packetName, String binding, boolean inline)
    {
        this.type = inline ? PDLItemType.InlinePacketRef : PDLItemType.PacketRef;
        
        if (type != PDLItemType.InlinePacketRef && binding == null)
            throw new NullPointerException("binding must not be null");
        
        this.packetName = packetName;
        this.binding = binding;
        this.document = document;
    }
    
    public PDLPacketRefImpl(PDLDocument document, PDLPacketRef ref, PDLMultiplicity m)
    {
        this(document, ref.getPacketName(), ref.getBinding(), false);
        this.type = PDLItemType.PacketRefList;
        this.multiplicity = m;
    }
    
    public String getBinding()
    {
        return binding;
    }
    
    public String getPacketName()
    {
        return packetName;
    }

    public PDLItemType getType()
    {
        return type;
    }

    public PDLPacketDecl getReferencedPacket()
    {
        return document.getPacketDecl(this);
    }

    public PDLMultiplicity getMultiplicity()
    {
        return multiplicity;
    }

    public String toString()
    {
        if (type == PDLItemType.InlinePacketRef)
            return packetName+"$$";
        else if (type == PDLItemType.PacketRefList)
            return multiplicity +"*"+ packetName+"$"+binding;
        else
            return packetName+"$"+binding;
    }

    public int getMinimumSize()
    {
        if (type == PDLItemType.PacketRefList)
            return PDLUtils.getMinMultiplicity(multiplicity) * getReferencedPacket().getMinimumSize();
        else
            return getReferencedPacket().getMinimumSize();
    }

    public int getMinimumCount()
    {
        if (type == PDLItemType.PacketRefList)
            return PDLUtils.getMinMultiplicity(multiplicity) * getReferencedPacket().getMinimumCount();
        else
            return getReferencedPacket().getMinimumCount();
    }
    
}
