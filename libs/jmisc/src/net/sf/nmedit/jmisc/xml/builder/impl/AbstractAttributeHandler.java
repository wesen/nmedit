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

import net.sf.nmedit.jmisc.xml.builder.AttributeHandler;
import net.sf.nmedit.jmisc.xml.builder.ElementHandler;

public abstract class AbstractAttributeHandler implements AttributeHandler
{

    private AbstractElementHandler element;
    private final String name;
    private final boolean required;

    public AbstractAttributeHandler(AbstractElementHandler element,
            String name, boolean required)
    {
        this.element = element;
        this.name = name;
        this.required = required;
        
        element.registerAttribute(this);
    }

    public String getAttributeName()
    {
        return name;
    }

    public boolean isRequired()
    {
        return required;
    }

    public ElementHandler getElement()
    {
        return element;
    }

}
