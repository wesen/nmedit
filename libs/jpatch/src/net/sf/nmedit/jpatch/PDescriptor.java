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
 * The descriptor contains immutable data of a component
 * and can be shared amongst different component instances.
 * 
 * @author Christian Schneider
 */
public interface PDescriptor
{

    /**
     * Attributes (key, value) where the key starts with
     * CACHE_KEY_PREFIX can be removed at any time.
     * 
     * Before serialization these attributes are be removed.
     */
    public static final String CACHE_KEY_PREFIX = "__cache.";
    
    /**
     * Returns the parent descriptor of this descriptor.
     * Generally <code>null</code> can be returned
     * but depending on the descriptor this method
     * must return a non-null value.
     * 
     * If two components c, p have the descriptors
     * dc, dp, c is a composite of p, then the condition
     * <code>dc.getParentDescriptor().equals(dp)</code> should be true.
     * 
     * @return the parent descriptor or <code>null</code> if no parent
     * descriptor exists
     */
    PDescriptor getParentDescriptor();

    /**
     * Returns the (default) name of the component the descriptor belongs to.
     * @return the name of the component the descriptor belongs to
     */
    String getName();
    
    /**
     * Returns the attribute with the specified name (key)
     * or <code>null</code> if the attribute was not specified.
     * @param name name of the attribute
     * @return the attribute value 
     */
    Object getAttribute(String name);
    
    /**
     * Sets the attribute with the specified name (key).
     * @param name name of the attribute 
     * @param value the attribute value
     */
    void setAttribute(String name, Object value);
    
    /**
     * Returns the attribute with the specified name.
     * If the attribute is not defined or not instance of {@link Integer}}
     * then the specified default value is returned instead.
     * 
     * @param name name of the attribute
     * @param defaultValue the default value of the attribute 
     * @return the integer attribute with the specified name
     */
    int getIntAttribute(String name, int defaultValue);

    /**
     * Returns the attribute with the specified name.
     * If the attribute is not defined or not instance of {@link Float}}
     * then the specified default value is returned instead.
     * 
     * @param name name of the attribute
     * @param defaultValue the default value of the attribute 
     * @return the float attribute with the specified name
     */
    float getFloatAttribute(String name, float defaultValue);

    /**
     * Returns the attribute with the specified name.
     * If the attribute is not defined or not instance of {@link Double}}
     * then the specified default value is returned instead.
     * 
     * @param name name of the attribute
     * @param defaultValue the default value of the attribute 
     * @return the double attribute with the specified name
     */
    double getDoubleAttribute(String name, double defaultValue);
    
    /**
     * Returns the attribute with the specified name.
     * If the attribute is not defined or not instance of {@link String}}
     * <code>null</code> is returned instead.
     * 
     * @param name name of the attribute 
     * @return the string attribute with the specified name
     */
    String getStringAttribute(String name);

    /**
     * Returns the attribute with the specified name.
     * If the attribute is not defined or not instance of {@link Boolean}}
     * then the specified default value is returned instead.
     * 
     * @param name name of the attribute
     * @param defaultValue the default value of the attribute 
     * @return the boolean attribute with the specified name
     */
    boolean getBooleanAttribute(String name, boolean defaultValue);
    
    /**
     * Returns the number of defined attributes.
     * @return the number of defined attributes
     */
    int getAttributeCount();
    
    /**
     * Returns the iteration over the names (keys) of the
     * defined attributes.  
     * @return iteration over the attribute names (keys)
     */
    Iterator<String> attributeKeys();

    /**
     * Returns the attribute with the name 'icon16x16' which is instance
     * of {@link ImageSource} or <code>null</code> if this attribute does not exist.
     * 
     * The resulting image must be a 16px * 16px or smaller icon for this component.
     * @return icon of this component 
     */
    ImageSource get16x16IconSource();

    /**
     * Returns the attribute with the name 'icon32x32' which is instance
     * of {@link ImageSource} or <code>null</code> if this attribute does not exist.
     * 
     * The resulting image must be a 32px * 32px or smaller icon for this component.
     * @return icon of this component 
     */
    ImageSource get32x32IconSource();

    /**
     * Returns the attribute with the specified name.
     * If the attribute is not defined or not instance of {@link ImageSource}}
     * then <code>null</code> is returned instead.
     * 
     * @param key name of the attribute 
     * @return the ImageSource attribute with the specified name
     */
    ImageSource getImageSource( String key );

    /**
     * Returnsall attribute values which are instance of {@link ImageSource}.
     * @return all ImageSource attributes
     */
    Iterator<ImageSource> getImageSources();

    /**
     * Returns a unique not-<code>null</code> identifier of this descriptor.
     * The uniqueness is limited to the parent descriptors child descriptors.
     * The return value is usually a string.
     * @return unique identifier of this descriptor
     * @see #getComponentByComponentId(Object)
     */
    Object getComponentId();

    /**
     * Returns the child descriptor with the specified component id.
     * Two descriptors <code>c</code>, <code>p</code> the equation
     * <code>c.getParentComponent().equals(p)==true</code> implies
     * <code>p.getComponentByComponentId(c.getComponentId())==c</code>.
     * If the specified component identifier does not exist then 
     * <code>null</code> is returned. 
     * 
     * @param componentId 
     * @return the child descriptor with the specified component id
     */
    PDescriptor getComponentByComponentId(Object componentId);

    /**
     * Returns the index property of this descriptor.
     * This method is used internally by the parent descriptor.
     * The condition <code>getParentDescriptor()==null</code>
     * implies <code>getDescriptorIndex()&lt;0</code>.
     * @return the index of this descriptor
     * @see #setDescriptorIndex(int)
     */
    int getDescriptorIndex();

    /**
     * Sets the index property of this descriptor.
     * This method is used internally by the parent descriptor.
     * Caution: do not change this property unless you know
     * what you are doing. Doing otherwise might cause exceptions.
     * @param index the new index 
     * @return the index of this descriptor
     */
    void setDescriptorIndex(int index);

    /**
     * The roles of this component.
     * @return set of roles
     */
    PRoles getRoles();
    
}
