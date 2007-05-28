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


public interface PDescriptor
{

    public static final String CACHE_KEY_PREFIX = "__cache.";
    
    PDescriptor getParentDescriptor();

    String getName();
    
    Object getAttribute(String name);
    
    void setAttribute(String name, Object value);
    
    int getIntAttribute(String name, int defaultValue);
    
    String getStringAttribute(String name);
    
    boolean getBooleanAttribute(String name, boolean defaultValue);
    
    int getAttributeCount();
    
    Iterator<String> attributeKeys();
    
    int getDescriptorIndex();
    
    void setDescriptorIndex(int index);

    ImageSource get16x16Icon();
    
    ImageSource get32x32Icon();
    
    ImageSource getImage( String key );

    public Iterator<ImageSource> getImages();

    Object getComponentId();

    PDescriptor getComponentByComponentId(Object id);

    PConnectorDescriptor getConnectorByComponentId(Object id);

    PParameterDescriptor getParameterByComponentId(Object id);
    
}
