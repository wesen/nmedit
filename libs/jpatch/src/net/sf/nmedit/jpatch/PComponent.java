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
package net.sf.nmedit.jpatch;

import java.util.Iterator;


/**
 * A component of a patch.
 * 
 * @author Christian Schneider
 */
public interface PComponent
{

    /**
     * Returns the descriptor of this component.
     * @return the descriptor of this component
     */
    PDescriptor getDescriptor();
    
    /**
     * Returns the parent of this component.
     * @return the parent of this component
     */
    PComponent getParentComponent();

    /**
     * Returns the number of child components. 
     * @return the number of child components.
     */
    int getComponentCount();
    
    /**
     * Returns the child component at the specified index.
     * @param index the component index
     * @return child component at the specified index
     * @throws IndexOutOfBoundsException if the equation
     *  0&lt;=index&lt;=getComponentCount() is false. 
     */
    PComponent getComponent(int index);
    
    int getComponentIndex();
    
    Object getComponentId();

    PComponent getComponentByComponentId(Object id);

    PConnector getConnectorByComponentId(Object id);

    PParameter getParameterByComponentId(Object id);
    

    Object getAttribute(String name);
    
    void setAttribute(String name, Object value);
    
    int getIntAttribute(String name, int defaultValue);
    
    String getStringAttribute(String name);
    
    boolean getBooleanAttribute(String name, boolean defaultValue);
    
    int getAttributeCount();
    
    Iterator<String> attributeKeys();
    
}
