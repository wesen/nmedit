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
 * Created on Jun 15, 2006
 */
package net.sf.nmedit.jmisc.xml.builder.impl;

import java.util.HashMap;
import java.util.Map;

import net.sf.nmedit.jmisc.xml.builder.AttributeHandler;
import net.sf.nmedit.jmisc.xml.builder.ElementHandler;
import net.sf.nmedit.jmisc.xml.builder.XMLProcessingException;

import org.xmlpull.v1.XmlPullParser;

public class AbstractElementHandler implements ElementHandler
{
    
    private final AbstractElementHandler parent;
    private final String name;
    
    private Map<String, AbstractElementHandler> elementMap;
    private Map<String, AbstractAttributeHandler> attributeMap;
    private int requiredAttributes;

    public AbstractElementHandler(AbstractElementHandler parent,
            String name)
    {
        this.parent = parent;
        this.elementMap = null;
        this.name = name;
        this.requiredAttributes = 0;
        
        if (parent!=null) parent.registerContextElement(this);
    }
    public AbstractElementHandler(String name)
    {
        this(null, name);
    }

    public String getElementName()
    {
        return name;
    }

    public ElementHandler getParent()
    {
        return parent;
    }

    public ElementHandler getElementHandler( String name )
    {
        return elementMap == null ? null : elementMap.get(name);
    }

    protected void registerContextElement( AbstractElementHandler handler )
    {
        if (elementMap == null)
            elementMap = createElementMap();
        elementMap.put(handler.getElementName(), handler);
    }
    
    public void registerAttribute( AbstractAttributeHandler handler )
    {
        if (attributeMap == null)
            attributeMap = createAttributeMap();

        attributeMap.put(handler.getAttributeName(), handler);

        if (handler.isRequired())
        {
            requiredAttributes++;
        }
    }
    
    public boolean hasRequiredAttributes()
    {
        return requiredAttributes > 0;
    }
    
    protected Map<String, AbstractElementHandler> createElementMap()
    {
        return new HashMap<String, AbstractElementHandler>();
    }
    
    protected Map<String, AbstractAttributeHandler> createAttributeMap()
    {
        return new HashMap<String, AbstractAttributeHandler>();
    }
    
    public void element(XmlPullParser parser) throws XMLProcessingException
    {
        int missingAttributes = requiredAttributes;
        // check attributes
        for (int i=parser.getAttributeCount()-1;i>=0;i--)
        {
            AbstractAttributeHandler h = attributeMap.get(
                    parser.getAttributeName(i));
            if (h==null) undefinedAttribute(parser, i);
            else
            {
                if (h.isRequired()) missingAttributes --;
                h.attribute(parser, i);
            }
        }
        
        if (missingAttributes>0) missingAttributes(parser);
    }

    private void missingAttributes( XmlPullParser parser ) throws XMLProcessingException
    {
        for (AttributeHandler h : attributeMap.values())
        {
            if (h.isRequired())
            {
                int attributeIndex = parser.getAttributeCount()-1;
                for (; attributeIndex>=0;attributeIndex--)
                {
                    if (h.getAttributeName().equals(parser.getAttributeName(
                            attributeIndex)))
                    {
                        break; 
                    }
                }
                
                if (attributeIndex<0) missingAttribute(parser, h);
            }
        }
    }
    
    private void missingAttribute( XmlPullParser parser, AttributeHandler h ) throws XMLProcessingException
    { 
        throw new XMLProcessingException(parser, "missing attribute '"+h.getAttributeName()+"'");
    }
    
    private void undefinedAttribute( XmlPullParser parser, int attributeIndex ) throws XMLProcessingException
    { 
        throw new XMLProcessingException(parser, "undefined attribute '"+parser.getAttributeName(attributeIndex)+"'");
    }
    
}
