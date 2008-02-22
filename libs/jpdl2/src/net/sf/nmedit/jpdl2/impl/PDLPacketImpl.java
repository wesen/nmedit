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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.nmedit.jpdl2.PDLPacket;
import net.sf.nmedit.jpdl2.dom.PDLPacketDecl;

public class PDLPacketImpl implements PDLPacket
{
    
    private Map<String, PacketObject> packetObjects = new HashMap<String, PacketObject>();
    
    private int age = 0;

    private PDLPacketDecl decl;

    private String binding;

    public PDLPacketImpl(PDLPacketDecl decl, String binding)
    {
        this.decl = decl;
        this.binding = binding;
    }
    
    public String getBinding()
    {
        return binding;
    }
    
    public String getName()
    {
        return decl.getName();
    }

    public int getCurrentAge()
    {
        return age;
    }

    public final int incrementAge()
    {
        return age++;
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

    public boolean containsPacket(String packetName)
    {
    if (decl.getName().equals(packetName)) {
        return true;
    }
    else {
        for (PacketObject o: packetObjects.values())
        {
            if (o instanceof Packet)
            {
                PDLPacket p = ((Packet)o).packet;
                if (p.containsPacket(packetName))
                    return true;
            }
            else if (o instanceof PacketList)
            {
                PDLPacket[] list = ((PacketList)o).packets;
                for (int i=0;i<list.length;i++)
                    if (list[i].containsPacket(packetName))
                        return true;
            }
        }
    }
    return false;
    }

    protected boolean contains(String name)
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
        if (name != null && o != null && type == o.getType())
            return o;
        else
            return null;
    }

    public boolean hasVariable(String name)
    {
        return contains(name, PacketObjectType.Variable);
    }

    public boolean hasPacket(String name)
    {
        return contains(name, PacketObjectType.Packet);
    }

    public boolean hasVariableList(String name)
    {
        return contains(name, PacketObjectType.VariableList);
    }

    public boolean hasString(String name)
    {
        return contains(name, PacketObjectType.String);
    }

    public boolean hasPacketList(String name)
    {
        return contains(name, PacketObjectType.PacketList);
    }
    
    private void setPacketObject(String name, PacketObject o)
    {
        packetObjects.put(name, o);
    }

    public void setVariable(String name, int value)
    {
        setPacketObject(name, new Variable(name, value));
    }

    public void setVariableList(String name, int[] values)
    {
        if (values == null)
            throw new NullPointerException();
        setPacketObject(name, new VariableList(name, values));
    }

    public void setPacket(String name, PDLPacket packet)
    {
        if (packet == null)
            throw new NullPointerException();
        setPacketObject(name, new Packet(name, packet));
    }

    public void setPacketList(String name, PDLPacket[] packets)
    {
        if (packets == null)
            throw new NullPointerException();
        setPacketObject(name, new PacketList(name, packets));
    }
    
    public void setString(String name, String value)
    {
        if (value == null)
            throw new NullPointerException();
        setPacketObject(name, new POString(name, value));
    }

    public String getString(String name)
    {
        int pos = name.indexOf(":");
        if (pos >= 0) {
            return getPacket(name.substring(0, pos)).getString(name.substring(pos+1));
        }
        PacketObject o = getPacketObject(name, PacketObjectType.String);
        if (o == null) 
            throw new IllegalArgumentException("string not defined: "+name);
        return ((POString)o).value;
    }

    public int getVariable(String name)
    {
        int pos = name.indexOf(":");
        if (pos >= 0) {
            return getPacket(name.substring(0, pos)).getVariable(name.substring(pos+1));
        }
        PacketObject o = getPacketObject(name, PacketObjectType.Variable);
        if (o == null) 
            throw new IllegalArgumentException("variable not defined: "+name);
        return ((Variable)o).value;
    }

    public int getVariable(String name, int defaultValue)
    {
        int pos = name.indexOf(":");
        if (pos >= 0) {
            return getPacket(name.substring(0, pos)).getVariable(name.substring(pos+1), defaultValue);
        }
        PacketObject o = getPacketObject(name, PacketObjectType.Variable);
        if (o == null) return defaultValue;
        return ((Variable)o).value;
    }

    public int[] getVariableList(String name)
    {
        int pos = name.indexOf(":");
        if (pos >= 0) {
            return getPacket(name.substring(0, pos)).getVariableList(name.substring(pos+1));
        }
        PacketObject o = getPacketObject(name, PacketObjectType.VariableList);
        if (o == null) return null;
        int[] list = ((VariableList)o).values;
        int[] copy = new int[list.length];
        for (int i = 0; i < list.length; i++) 
        	copy[i] = list[i];
        return copy;
    }

    public PDLPacket getPacket(String name)
    {
        int pos = name.indexOf(":");
        if (pos >= 0) {
            String packetName = name.substring(0, pos); 

            PacketObject o = getPacketObject(packetName, PacketObjectType.Packet);
            return ((Packet)o).packet.getPacket(name.substring(pos+1));
        }
        PacketObject o = getPacketObject(name, PacketObjectType.Packet);
        if (o == null) return null;
        return ((Packet)o).packet;
    }

    public PDLPacket[] getPacketList(String name)
    {
        int pos = name.indexOf(":");
        if (pos >= 0) {
            return getPacket(name.substring(0, pos)).getPacketList(name.substring(pos+1));
        }
        PacketObject o = getPacketObject(name, PacketObjectType.PacketList);
        if (o == null) return null;
        
        PDLPacket[] list = ((PacketList)o).packets;
        PDLPacket[] copy = new PDLPacket[list.length];
        for (int i = 0; i < list.length; i++) 
        	copy[i] = list[i];
        return copy;
    }

    private static enum PacketObjectType
    {
        Variable,
        VariableList,
        Packet,
        PacketList,
        String
    }
    
    private List<String> getAllKeys(PacketObjectType type)
    {
        PacketObject[] items = new PacketObject[packetObjects.size()];
        int size = 0;
        for (PacketObject po:packetObjects.values())
            if (po.getType() == type)
            {
                items[size++] = po;
            }
        
        Arrays.sort(items, 0, size, AgeComparator.instance);
        
        List<String> names = new ArrayList<String>(size);
        for (int i=0;i<size;i++)
            names.add(items[i].name);
        names = Collections.unmodifiableList(names);
        return names;
    }

    public List<String> getAllVariables()
    {
        return getAllKeys(PacketObjectType.Variable);
    }
    
    private static class AgeComparator implements Comparator<PacketObject>
    {
        private static AgeComparator instance = new AgeComparator();
        public int compare(PacketObject a, PacketObject b)
        {
            return a.age-b.age;
        }
    }

    private abstract class PacketObject
    {
        int age = PDLPacketImpl.this.incrementAge();
        String name;
        public PacketObject(String name){this.name = name;}
        public abstract PacketObjectType getType();
    }
    
    private class POString extends PacketObject
    {
        String value;
        public POString(String name, String value) { super(name); this.value = value; }
        public PacketObjectType getType() { return PacketObjectType.String; }
    }
    
    private class Variable extends PacketObject
    {
        int value;
        public Variable(String name, int value) { super(name); this.value = value; }
        public PacketObjectType getType() { return PacketObjectType.Variable; }
    }

    private class VariableList extends PacketObject
    {
        int[] values;
        public VariableList(String name, int[] values) { super(name); this.values = values; }
        public PacketObjectType getType() { return PacketObjectType.VariableList; }
    }

    private class Packet extends PacketObject
    {
        PDLPacket packet;
        public Packet(String name, PDLPacket packet) { super(name); this.packet = packet; }
        public PacketObjectType getType() { return PacketObjectType.Packet; }
    }

    private class PacketList extends PacketObject
    {
        PDLPacket[] packets;
        public PacketList(String name, PDLPacket[] packets) { super(name); this.packets = packets; }
        public PacketObjectType getType() { return PacketObjectType.PacketList; }
    }
    
    public String toString()
    {
        return "packet "+decl.getName();
    }

    public List<String> getAllPacketLists()
    {
        return getAllKeys(PacketObjectType.PacketList);
    }

    public List<String> getAllPackets()
    {
        return getAllKeys(PacketObjectType.Packet);
    }

    public List<String> getAllVariableLists()
    {
        return getAllKeys(PacketObjectType.VariableList);
    }

    public List<String> getAllStrings()
    {
        return getAllKeys(PacketObjectType.String);
    }
    
}
