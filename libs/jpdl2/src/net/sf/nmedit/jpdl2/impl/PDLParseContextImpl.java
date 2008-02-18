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
import java.util.Map.Entry;

import net.sf.nmedit.jpdl2.PDLPacket;
import net.sf.nmedit.jpdl2.PDLParseContext;
import net.sf.nmedit.jpdl2.stream.BitStream;
import net.sf.nmedit.jpdl2.stream.PDLDataSource;

public class PDLParseContextImpl implements PDLParseContext
{

    public PDLDataSource input;
    public BitStream stream;
    public PDLPacket packet;
    private Map<String, Label> labelMap = new HashMap<String, Label>();
    private int maxAge = -1;

    public void clearLabels()
    {
        labelMap.clear();
        maxAge = -1;
    }
    
    public void setLabel(String name, int age, int value)
    {
        labelMap.put(name, new Label(age, value));
        maxAge = Math.max(maxAge, age);
    }
    
    public int getLabel(String name)
    {
        Label label = labelMap.get(name);
        if (label == null)
            throw new RuntimeException("label not defined: "+name);
        return label.value;
    }

    public PDLPacket getPacket()
    {
        return packet;
    }
    
    public void deleteLabelsOlderThan(int age)
    {
        if (age<maxAge) // only create iterator if necessary
        {
            for (Iterator<Entry<String, Label>> iter = labelMap.entrySet().iterator();iter.hasNext();)
            {
                if (iter.next().getValue().age>age)
                {
                    iter.remove();
                }
            }
            maxAge = age-1;
        }
    }

    public boolean hasLabel(String name)
    {
        return labelMap.get(name) != null; 
    }

    public BitStream getBitStream()
    {
        return stream;
    }
    
    private static class Label
    {
        private int value;
        private int age;

        public Label(int age, int value)
        {
            this.age = age;
            this.value = value;
        }
    }

}
