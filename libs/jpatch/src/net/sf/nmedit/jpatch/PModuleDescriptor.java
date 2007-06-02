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

/**
 * Descriptor for a {@link PModule module}.
 * @author Christian Schneider
 */
public interface PModuleDescriptor extends PDescriptor
{

    /**
     * Returns the number of lights of this module.
     * @return the number of lights
     */
    int getLightDescriptorCount();

    /**
     * Returns the light at the specified index.
     * @param index the light index
     * @return the light at the specified index
     */
    PLightDescriptor getLightDescriptor(int index);
    
    /**
     * Returns the number of connectors of this module.
     * @return the number of connectors
     */
    int getConnectorDescriptorCount();
    
    /**
     * Returns the connector at the specified index.
     * @param index the connector index
     * @return the connector at the specified index
     */
    PConnectorDescriptor getConnectorDescriptor(int index);

    /**
     * Returns the number of parameters of this module.
     * @return the number of parameters
     */
    int getParameterDescriptorCount();

    /**
     * Returns the parameters at the specified index.
     * @param index the parameters index
     * @return the parameters at the specified index
     */
    PParameterDescriptor getParameterDescriptor(int index);
    
    /**
     * Returns <code>null</code> if not stated otherwise.
     */
    PDescriptor getParentDescriptor();
    
    /**
     * Returns the number of modules which can be added to a module container
     * or <code>-1</code> if there is no such limit.
     * The limit is specified by the integer-attribute 'limit'.
     * 
     * @return the number of modules which can be added to a module container.
     * @see PDescriptor#getIntAttribute(String, int)
     */
    int getLimit();
    
    /**
     * Returns all module descriptors and other global data.
     * @return the module descriptors
     */
    ModuleDescriptions getModules();
    
    /**
     * Returns the category this module belongs to or null if not specified.
     * The category is specified by the string-attribute 'category'
     * @return return category this module belongs to
     * @see PDescriptor#getStringAttribute(String)
     */
    String getCategory();
    
    /**
     * Returns true if this module can not be added (by the user) to one of
     * the module containers. This can be used for module-like structures which
     * are always present in a patch. The value is specified by the boolean-attribute
     * 'instantiable'.
     * @return
     * @see PDescriptor#getBooleanAttribute(String, boolean)
     */
    boolean isInstanciable();

    /**
     * Returns the child connector with the specified component id, or null
     * if the id does not exist.
     * For a {@link PConnectorDescriptor} <code>c</code> the condition
     * <code>c.getParentComponent().equals(this)==true</code> implies
     * c == getConnectorByComponentId(c.getComponentId()).  
     * @param componentId the component id
     * @return the connector with the specified component id
     */
    PConnectorDescriptor getConnectorByComponentId(Object componentId);

    /**
     * Returns the child parameter with the specified component id, or null
     * if the id does not exist.
     * For a {@link PParameterDescriptor} <code>p</code> the condition
     * <code>p.getParentComponent().equals(this)==true</code> implies
     * p == getParameterByComponentId(p.getComponentId()).  
     * @param componentId the component id
     * @return the parameter with the specified component id
     */
    PParameterDescriptor getParameterByComponentId(Object componentId);

    /**
     * Returns the child light with the specified component id, or null
     * if the id does not exist.
     * For a {@link PLight} <code>l</code> the condition
     * <code>l.getParentComponent().equals(this)==true</code> implies
     * l == getParameterByComponentId(l.getComponentId()).  
     * @param componentId the component id
     * @return the light with the specified component id
     */
    PLightDescriptor getLightDescriptorByComponentId(Object componentId);
}
