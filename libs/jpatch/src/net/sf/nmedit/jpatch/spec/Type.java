/* Copyright (C) 2006 Christian Schneider
 * 
 * This file is part of Nomad.
 * 
 * Nomad is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Nomad is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Nomad; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

/*
 * Created on Dec 10, 2006
 */
package net.sf.nmedit.jpatch.spec;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Type
{

    private Map<Integer,String> key_value = new HashMap<Integer,String>();
    private Map<String,Integer> value_key = new HashMap<String,Integer>();
    private String name;
    
    public Type( String name )
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    
    public Iterator<String> values()
    {
        return key_value.values().iterator();
    }
    
    public void putValue(int key, String name)
    {
        Integer i = new Integer(key);
        key_value.put(i, name);
        value_key.put(name, i);
    }
    
    public String getValue(int key)
    {
        return key_value.get(key);
    }
    
    public Integer getKey(String value)
    {
        return value_key.get(value);
    }

    public String toString()
    {
        return "type "+getName();
    }

}
