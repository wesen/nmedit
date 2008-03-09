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
package net.sf.nmedit.jtheme.store2;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.store.StorageContext;

import org.jdom.Attribute;
import org.jdom.Element;

public abstract class AbstractElement extends ComponentElement
    implements Serializable
{

    public static final String ATT_NAME = "name";
    public static final String ATT_X = "x";
    public static final String ATT_Y = "y";
    public static final String ATT_WIDTH = "width";
    public static final String ATT_HEIGHT= "height";
    public static final String ATT_SIZE  = "size";
    public static final String ATT_COMPONENT_ID  = "component-id";
    public static final String ATT_VALUE  = "value";
    public static final String ATT_STYLE  = "style";
    public static final String ATT_CLASS  = "class";

    protected int x = -1;
    protected int y = -1;
    protected int width = -1;
    protected int height = -1;
    protected String name;

    protected AbstractElement()
    {
        super();
    }
    
    protected void initElement(StorageContext context, Element e)
    {
        initFromAttributes(context, e);
        initializeElement(context);
    }

    protected void initFromAttributes(StorageContext context, Element e)
    {
        List attList = e.getAttributes();
        for (int i=attList.size()-1;i>=0;i--)
        {
            Attribute att = (Attribute) attList.get(i);
            initAttributes(context, att);
        }
    }

    static int IC = 0;
    
    protected void initAttributes(StorageContext context, Attribute att)
    {
        String name = att.getName();
     
        //System.out.println("->>"+(++IC));
        
        if (ATT_NAME.equals(name))
            this.name = att.getValue();
        else if (ATT_X.equals(name))
            this.x = parseInt(att.getValue(), -1);
        else if (ATT_Y.equals(name))
            this.y = parseInt(att.getValue(), -1);
        else if (ATT_WIDTH.equals(name))
            this.width = parseInt(att.getValue(), -1);
        else if (ATT_HEIGHT.equals(name))
            this.height = parseInt(att.getValue(), -1);
        else if (ATT_SIZE.equals(name))
            this.width = this.height = parseInt(att.getValue(), -1);
        else if (ATT_STYLE.equals(name))
            initCSSStyle(context, att.getValue());
        else if (ATT_CLASS.equals(name))
            initCSSClass(context, att.getValue());
    }

    protected void initCSSStyle(StorageContext context, String styleValue)
    {
        // no op
    }
    
    protected void initCSSClass(StorageContext context, String cssClass)
    {
        // no op
    }
    
    protected void checkDimensions()
    {
        // TODO use a JTRuntimeException
        if ((width<0)||(height<0))
            throw new IllegalStateException("attribute height|width|size missing or invalid");
    }

    protected void checkLocation()
    {
        if ((x<0)||(y<0))
            throw new IllegalStateException("attribute x|y missing or invalid");
    }

    protected static int parseInt(String attValue, String attName)
    {
        try
        {
            return Integer.parseInt(attValue);
        }
        catch (NumberFormatException e)
        {
            throw new NumberFormatException("attribute '"+attName+"' not an int, "+e.getMessage());
        }
    }

    protected static int parseInt(String attValue, int defaultValue)
    {
        try
        {
            return Integer.parseInt(attValue);
        }
        catch (NumberFormatException e)
        {
            return defaultValue;
        }
    }
    
    protected static String lookupChildComponentId(Element parent, String childName)
    {
        Element child = parent.getChild(childName);
        if (child != null)
        {
            Attribute att = child.getAttribute(ATT_COMPONENT_ID);
            if (att != null)
                return att.getValue();
        }
        return null;
    }
    
    protected void setLocation(JTComponent c)
    {
        c.setLocation(x, y);
    }
    
    protected void setSize(JTComponent c)
    {
        c.setSize(width, height);
    }
    
    protected void setBounds(JTComponent c)
    {
        c.setBounds(x, y, width, height);
    }
    
    protected void setName(JTComponent c)
    {
        if (name != null)
            c.setName(name);
    }

    private void writeObject(java.io.ObjectOutputStream out)
        throws IOException
    {
        out.defaultWriteObject(); // 
    }
    
    private void readObject(java.io.ObjectInputStream in)
        throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
    }
    
}
