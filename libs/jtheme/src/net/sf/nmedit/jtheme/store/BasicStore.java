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
package net.sf.nmedit.jtheme.store;

import java.awt.Dimension;
import java.awt.Point;

import org.jdom.Attribute;
import org.jdom.Element;

public abstract class BasicStore extends Store
{

    private static final boolean DEBUG = false;

    protected transient int x;
    protected transient int y;
    protected transient int width;
    protected transient int height;
    protected transient String name;
    protected transient String componentId;

    public static int getIntAtt(Element e, String name)
    {
        return Integer.parseInt(e.getAttributeValue(name));
    }

    public static int getIntAtt(Element e, String name, int alt)
    {
        try
        {
            return getIntAtt(e, name);
        }
        catch (NumberFormatException nfe)
        {
            return alt;
        }
    }

    public static int getX(Element e)
    {
        return Math.max(0, getIntAtt(e, "x", 0));
    }

    public static int getY(Element e)
    {
        return Math.max(0, getIntAtt(e, "y", 0));
    }

    public static int getWidth(Element e)
    {
        return getIntAtt(e, "width", 0);
    }

    public static int getHeight(Element e)
    {
        return getIntAtt(e, "height", 0);
    }

    public static int getSize(Element e)
    {
        return getIntAtt(e, "size", 0);
    }
    
    public static String getName(Element e)
    {
        Attribute name = e.getAttribute("name");
        return name != null ? name.getValue() : null;
    }
    
    public Point getLocation(Element e)
    {
        return new Point(getX(e), getY(e));
    }
    
    public static Dimension getDimensions(Element e)
    {
        int size = getSize(e);
        if (size>0) return new Dimension(size, size);
        else return new Dimension(getWidth(e), getHeight(e));
    }
    
    protected static String lookupComponendId(Element e)
    {
        Attribute a = e.getAttribute("component-id");
        if (a != null)
            return a.getValue();
        else if (DEBUG)
            System.err.println("element "+e+" has no attribute:'component-id'");
        return null;
    }
    
    protected static String lookupChildElementComponentId(Element parent, String childElementName)
    {
        Element e = parent.getChild(childElementName);
        
        if (e != null)
            return lookupComponendId(e);
        else if (DEBUG)
            System.err.println("element "+parent+" has no such child element:'"+childElementName+"'");
        return null;
    }
    
}
