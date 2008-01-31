package net.sf.nmedit.jpdl2.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.nmedit.jpdl2.PDLPacket;
import net.sf.nmedit.jpdl2.PDLParseContext;
import net.sf.nmedit.jpdl2.bitstream.BitStream;

public class PDLParseContextImpl implements PDLParseContext
{
    
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
