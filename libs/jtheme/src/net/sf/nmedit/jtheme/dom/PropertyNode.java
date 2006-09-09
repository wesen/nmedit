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
 * Created on Sep 9, 2006
 */
package net.sf.nmedit.jtheme.dom;

public class PropertyNode
{

    private String propertyName;
    private String propertyValue;
    private Object userObject;

    public PropertyNode(String propertyName, String propertyValue)
    {
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
    }

    public String getPropertyName()
    {
        return propertyName;
    }

    public void setPropertyName( String propertyName )
    {
        this.propertyName = propertyName;
    }

    public String getPropertyValue()
    {
        return propertyValue;
    }

    public void setPropertyValue( String propertyValue )
    {
        this.propertyValue = propertyValue;
    }
    
    public Object getUserObject()
    {
        return userObject;
    }
    
    public void setUserObject(Object userObject)
    {
        this.userObject = userObject;
    }
/*
    public int hashCode()
    {
        return propertyName.hashCode();
    }
    
    public boolean equals(Object o)
    {
        if (o!=null && o instanceof PropertyNode)
        {
            return ((PropertyNode)o).getPropertyName().equals(propertyName);
        }
        return false;
    }*/
}
