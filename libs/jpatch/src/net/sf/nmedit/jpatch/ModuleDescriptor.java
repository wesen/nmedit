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
 * Created on Nov 30, 2006
 */
package net.sf.nmedit.jpatch;

import java.util.Iterator;

/**
 * Returns the descriptor of a module.
 * 
 * @author Christian Schneider
 */
public interface ModuleDescriptor extends ComponentDescriptor
{

    public static ParameterDescriptor[] NO_PARAMETER_DESCRIPTORS = new ParameterDescriptor[0];
    public static ConnectorDescriptor[] NO_CONNECTOR_DESCRIPTORS = new ConnectorDescriptor[0];

    /**
     * Returns the number of connectors the described module has.
     * @return the number of connectors the described module has
     */
    int getConnectorCount();
    
    /**
     * Returns the connector descriptor at the specified index.
     * 
     * @param index index of the descriptor
     * @return the connector descriptor at the specified index
     */
    ConnectorDescriptor getConnectorDescriptor(int index);
    
    /**
     * Returns the number of parameters the described module has.
     * @return the number of parameters the described module has
     */
    int getParameterCount();
    
    /**
     * Returns the parameter descriptor at the specified index.
     * 
     * @param index index of the descriptor
     * @return the parameter descriptor at the specified index
     */
    ParameterDescriptor getParameterDescriptor(int index);
    
    /**
     * @see Descriptor#getSourceDescriptor()
     */
    ModuleDescriptor getSourceDescriptor();

    String getCategory();

    String getDisplayName();
    
    ImageSource getImage(String key);
    Iterator<ImageSource> getImages();
    
    ParameterDescriptor[] getParameterDescriptorList(String parameterClass);
    ConnectorDescriptor[] getConnectorDescriptorList(String connectorClass);

    ParameterDescriptor getParameter(int index);
    ParameterDescriptor getParameter(int index, String pclass);
    ConnectorDescriptor getConnector(int index);
    ConnectorDescriptor getConnector(int index, String cclass);
    ConnectorDescriptor getConnector( int index, boolean output );
    
    int getIndex();
    
}
