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
package net.sf.nmedit.jtheme.persistence;

public class Property
{

    private String name;
    private String stringValue;
    private Object value;
    
    public Property(String name, String stringValue)
    {
        this(name);
        this.stringValue = stringValue;
    }
    
    public Property(String name, Object value)
    {
        this(name);
        this.value = value;
    }
    
    public Property(String name)
    {
        this.name = name;
    }
    
    public String getStringValue()
    {
        return stringValue;
    }
    
    public Object getValue()
    {
        return value;
    }
    
    public void setStringValue(String stringValue)
    {
        this.stringValue = stringValue;
    }
    
    public void setValue(Object value)
    {
        this.value = value;
    }
    
    public int hashCode()
    {
        return name.hashCode();
    }
    
    public boolean equals(Object o)
    {
        return o != null && o instanceof Property && ((Property)o).name.equals(name);
    }
    
}

