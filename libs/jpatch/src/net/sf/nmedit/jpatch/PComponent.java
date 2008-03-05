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

import javax.swing.undo.UndoableEdit;

import net.sf.nmedit.jpatch.history.PUndoableEditSupport;

/**
 * A component in a patch.
 * 
 * @author Christian Schneider
 */
public interface PComponent
{

    PUndoableEditFactory getUndoableEditFactory();
    
    PUndoableEditSupport getEditSupport();
    
    /**
     * Posts an edit of this component.
     * The edit will only be recognized if 
     * {@link #isUndoableEditSupportEnabled()}
     * is true.
     * @param edit
     */
    void postEdit(UndoableEdit edit);
    
    /**
     * True if undo support is enabled.
     * @return true if undo support is enabled
     */
    boolean isUndoableEditSupportEnabled();
    
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
    
    /**
     * Returns the index of this component.
     * Generally this is equal to {@link PDescriptor#getDescriptorIndex() getDescriptor().getDescriptorIndex()}.
     * @return returns the index of this component
     * @see PDescriptor#getDescriptorIndex()
     */
    int getComponentIndex();
    
    /**
     * Returns a unique identifier of this component. The uniqueness is limited
     * to the {@link #getParentComponent() parent component}'s children.
     * Generally this returns {@link PDescriptor#getComponentId() getDescriptor().getComponentId()}.
     * @return a unique identifier of this component
     * @see PDescriptor#getComponentId()
     */
    Object getComponentId();

    /**
     * Returns the child component with the specified id or null if no
     * such component exists.
     * @param componentId the component id
     * @return returns the child component with the specified id
     */
    PComponent getComponentByComponentId(Object componentId);

    /**
     * Returns the child connector with the specified id or null if no
     * such component exists.
     * @param componentId the component id
     * @return returns the child connector with the specified id
     */
    PConnector getConnectorByComponentId(Object componentId);

    /**
     * Returns the child parameter with the specified id or null if no
     * such component exists.
     * @param componentId the component id
     * @return returns the child parameter with the specified id
     */
    PParameter getParameterByComponentId(Object componentId);

    /**
     * Returns the child light with the specified id or null if no
     * such component exists.
     * @param componentId the component id
     * @return returns the child light with the specified id
     */
    PLight getLightByComponentId(Object componentId);
    
    /**
     * Returns the attribute with the specified name or <code>null</code>
     * if the attribute does not exist. The value is obtained from the
     * component's {@link #getDescriptor() descriptor}. 
     * 
     * @param name name of the attribute
     * @return the attribute with the specified name
     * @see PDescriptor#getAttribute(String)
     */
    Object getAttribute(String name);

    /**
     * Returns the string-attribute with the specified name or <code>null</code>
     * if the attribute does not exist. The value is obtained from the
     * component's {@link #getDescriptor() descriptor}. 
     * 
     * @param name name of the attribute
     * @return the attribute with the specified name
     * @see PDescriptor#getAttribute(String)
     */
    String getStringAttribute(String name);

    /**
     * Returns the integer-attribute with the specified name or <code>defaultValue</code>
     * if the attribute does not exist. The value is obtained from the
     * component's {@link #getDescriptor() descriptor}. 
     * 
     * @param name name of the attribute
     * @param defaultValue the default value
     * @return the attribute with the specified name
     * @see PDescriptor#getIntAttribute(String, int)
     */
    int getIntAttribute(String name, int defaultValue);

    /**
     * Returns the float-attribute with the specified name or <code>defaultValue</code>
     * if the attribute does not exist. The value is obtained from the
     * component's {@link #getDescriptor() descriptor}. 
     * 
     * @param name name of the attribute
     * @param defaultValue the default value
     * @return the attribute with the specified name
     * @see PDescriptor#getFloatAttribute(String, float)
     */
    float getFloatAttribute(String name, float defaultValue);

    /**
     * Returns the double-attribute with the specified name or <code>defaultValue</code>
     * if the attribute does not exist. The value is obtained from the
     * component's {@link #getDescriptor() descriptor}. 
     * 
     * @param name name of the attribute
     * @param defaultValue the default value
     * @return the attribute with the specified name
     * @see PDescriptor#getDoubleAttribute(String, double)
     */
    double getDoubleAttribute(String name, double defaultValue);

    /**
     * Returns the boolean-attribute with the specified name or <code>null</code>
     * if the attribute does not exist. The value is obtained from the
     * component's {@link #getDescriptor() descriptor}. 
     * 
     * @param name name of the attribute
     * @param defaultValue the default value
     * @return the attribute with the specified name
     * @see PDescriptor#getBooleanAttribute(String, boolean)
     */
    boolean getBooleanAttribute(String name, boolean defaultValue);

    /**
     * Returns the number of attributes defined in the 
     * component's {@link #getDescriptor() descriptor}. 
     * 
     * @see PDescriptor#getAttributeCount()
     */
    int getAttributeCount();

    /**
     * Returns the names of the attributes which are defined in the
     * component's {@link #getDescriptor() descriptor}. 
     * 
     * @see PDescriptor#attributeKeys()
     */
    Iterator<String> attributeKeys();


    String getName();
    
    PRoles getRoles();
    
}
