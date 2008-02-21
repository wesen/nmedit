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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.nmedit.jpdl2.dom.PDLDocument;
import net.sf.nmedit.jpdl2.dom.PDLPacketDecl;
import net.sf.nmedit.jpdl2.dom.PDLPacketRef;

public class PDLDocumentImpl implements PDLDocument
{
    
    private Map<String, PDLPacketDecl> packetMap;
    private String startPacketName;
    
    public PDLDocumentImpl()
    {
        packetMap = new HashMap<String, PDLPacketDecl>();
        startPacketName = null;
    }
    
    public String getStartPacketName()
    {
        return startPacketName;
    }

    public void setStartPacketName(String packetName)
    {
        this.startPacketName = packetName;
    }
    
    public boolean containsPacket(String packetName)
    {
        return packetMap.containsKey(packetName);
    }

    public void add(PDLPacketDecl packet)
    {
        if (packetMap.containsKey(packet.getName()))
            throw new IllegalArgumentException("packet exists: "+packet.getName());
        packetMap.put(packet.getName(), packet);
    }

    public PDLPacketDecl getPacketDecl(String name)
    {
        return packetMap.get(name);
    }

    public PDLPacketDecl getPacketDecl(PDLPacketRef packetReference)
    {
        return getPacketDecl(packetReference.getPacketName());
    }

    public Iterator<PDLPacketDecl> iterator()
    {
        return packetMap.values().iterator();
    }

    public int getPacketCount()
    {
        return packetMap.size();
    }
    
}
