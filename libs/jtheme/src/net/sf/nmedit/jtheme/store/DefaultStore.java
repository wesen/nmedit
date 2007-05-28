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

import net.sf.nmedit.jtheme.component.JTComponent;

import org.jdom.Attribute;
import org.jdom.Element;

public abstract class DefaultStore extends Store
{
    
    private Element element;
    
    private static boolean DEBUG = false;

    protected DefaultStore(Element element)
    {
        this.element = element;
    }

    @Override
    public Element getElement()
    {
        return element;
    }

    protected int getIntAtt(Element e, String name)
    {
        return Integer.parseInt(e.getAttributeValue(name));
    }

    protected int getIntAtt(String name)
    {
        return getIntAtt(element, name);
    }

    protected int getIntAtt(String name, int alt)
    {
        return getIntAtt(element, name, alt);
    }
    
    protected int getIntAtt(Element e, String name, int alt)
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

    protected int getX()
    {
        return Math.max(0, getIntAtt("x", 0));
    }

    protected int getY()
    {
        return Math.max(0, getIntAtt("y", 0));
    }

    protected int getWidth()
    {
        return getIntAtt("width", -1);
    }

    protected int getHeight()
    {
        return getIntAtt("height", -1);
    }

    protected int getSize()
    {
        return getIntAtt("size", -1);
    }
    
    protected void applyName(JTComponent c)
    {
        if (name != null)
            c.setName(name);
    }
    
    protected void applyLocation(JTComponent c)
    {
        c.setLocation(getX(), getY());
    }
    
    protected void applySize(JTComponent c)
    {
        int s = getSize();
        int w, h;
        if (s>0)
        {
            w = h = s;
        }
        else
        {
            w = getWidth();
            h = getHeight();
        }
        
        c.setSize(w, h);
    }
    
    protected String lookupChildElementComponentId(String childElementName)
    {
        Element e = getElement().getChild(childElementName);
        
        if (e != null) 
        {
            Attribute a = e.getAttribute("component-id");
            if (a != null)
                return a.getValue();
            else if (DEBUG)
            {
                System.err.println("element "+getElement()+"/"+e+" has no attribute:'component-id'");
            }
        }
        else  if (DEBUG)
        {
            System.err.println("element "+getElement()+" has no such child element:'"+childElementName+"'");
        }
        
        return null;
    }
    
}

