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

import net.sf.nmedit.jpdl2.PDLPacket;

public class PDLPacketImpl implements PDLPacket
{
    
    private Map<String, PacketObject> packetObjects = new HashMap<String, PacketObject>();
    
    private int age = 0;

    public int getCurrentAge()
    {
        return age;
    }
    
    public void removeItemsOlderThan(int minAge)
    {
        if (this.age>minAge)
        {
            boolean itemsRemoved = false;
            for (Iterator<PacketObject> iter=packetObjects.values().iterator();iter.hasNext();)
            {
                if (iter.next().age>=minAge)
                {
                    iter.remove();
                    itemsRemoved = true;
                }
            }
            if (itemsRemoved)
                this.age = minAge+1;
        }
    }
    
    private boolean contains(String name)
    {
        return packetObjects.get(name) != null;
    }
    
    private boolean contains(String name, PacketObjectType type)
    {
        PacketObject o = packetObjects.get(name);
        return o != null && o.getType() == type;
    }
    
    private PacketObject getPacketObject(String name, PacketObjectType type)
    {
        PacketObject o = packetObjects.get(name);
        if (name != null && type == o.getType())
            return o;
        else
            return null;
    }

    public boolean hasVariable(String name)
    {
        return contains(name, PacketObjectType.Variable);
    }

    public boolean hasLabel(String name)
    {
        return contains(name, PacketObjectType.Label);
    }

    public boolean hasPacket(String name)
    {
        return contains(name, PacketObjectType.Packet);
    }

    public boolean hasVariableList(String name)
    {
        return contains(name, PacketObjectType.VariableList);
    }

    public boolean hasPacketList(String name)
    {
        return contains(name, PacketObjectType.PacketList);
    }
    
    private void setPacketObject(String name, PacketObject o)
    {
        packetObjects.put(name, o);
    }

    public void setLabel(String name, int value)
    {
        setPacketObject(name, new Label(value));
    }

    public void setVariable(String name, int value)
    {
        setPacketObject(name, new Variable(value));
    }

    public void setVariableList(String name, int[] values)
    {
        setPacketObject(name, new VariableList(values));
    }

    public void setPacket(String name, PDLPacket packet)
    {
        setPacketObject(name, new Packet(packet));
    }

    public void setPacketList(String name, PDLPacket[] packets)
    {
        setPacketObject(name, new PacketList(packets));
    }

    public int getLabel(String name)
    {
        PacketObject o = getPacketObject(name, PacketObjectType.Label);
        if (o == null) 
            throw new IllegalArgumentException("label not defined: "+name);
        return ((Label)o).value;
    }

    public int getVariable(String name)
    {
        PacketObject o = getPacketObject(name, PacketObjectType.Variable);
        if (o == null) 
            throw new IllegalArgumentException("variable not defined: "+name);
        return ((Variable)o).value;
    }

    public int getVariable(String name, int defaultValue)
    {
        PacketObject o = getPacketObject(name, PacketObjectType.Variable);
        if (o == null) return defaultValue;
        return ((Variable)o).value;
    }

    public int[] getVariableList(String name)
    {
        PacketObject o = getPacketObject(name, PacketObjectType.VariableList);
        if (o == null) return null;
        return ((VariableList)o).values;
    }

    public PDLPacket getPacket(String name)
    {
        PacketObject o = getPacketObject(name, PacketObjectType.Packet);
        if (o == null) return null;
        return ((Packet)o).packet;
    }

    public PDLPacket[] getPacketList(String name)
    {
        PacketObject o = getPacketObject(name, PacketObjectType.PacketList);
        if (o == null) return null;
        return ((PacketList)o).packets;
    }
    
    private static enum PacketObjectType
    {
        Variable,
        VariableList,
        Packet,
        PacketList,
        Label
    }

    private abstract class PacketObject
    {
        int age = PDLPacketImpl.this.age++;
        public abstract PacketObjectType getType();
    }
    
    private class Label extends PacketObject
    {
        int value;
        public Label(int value) { this.value = value; }
        public PacketObjectType getType() { return PacketObjectType.Label; }   
    }

    private class Variable extends PacketObject
    {
        int value;
        public Variable(int value) { this.value = value; }
        public PacketObjectType getType() { return PacketObjectType.Variable; }
    }

    private class VariableList extends PacketObject
    {
        int[] values;
        public VariableList(int[] values) { this.values = values; }
        public PacketObjectType getType() { return PacketObjectType.VariableList; }
    }

    private class Packet extends PacketObject
    {
        PDLPacket packet;
        public Packet(PDLPacket packet) { this.packet = packet; }
        public PacketObjectType getType() { return PacketObjectType.Packet; }
    }

    private class PacketList extends PacketObject
    {
        PDLPacket[] packets;
        public PacketList(PDLPacket[] packets) { this.packets = packets; }
        public PacketObjectType getType() { return PacketObjectType.PacketList; }
    }
    
}
