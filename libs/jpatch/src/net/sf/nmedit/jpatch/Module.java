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

import java.awt.Point;

import net.sf.nmedit.jpatch.event.ModuleListener;

/**
 * View of a module from the black box perspective
 * 
 * @author Christian Schneider
 */
public interface Module extends Component, ModuleContainer
{

    /**
     * Returns the parent container of this module.
     * The return value is <code>null</code> when the module
     * has not been added to a container.
     * 
     * @return the parent container of this module
     */
    ModuleContainer getParent();
    
    /**
     * @see Component#getDescriptor()
     */
    ModuleDescriptor getDescriptor();
    
    /**
     * Returns true if at least one connector of this module is
     * connected with any other connector.
     * @return true if a connector of this module is connected
     */
    boolean hasConnections();
    
    /**
     * Returns true if at least one connector of this module is
     * connected with a connector owned by another module
     * @return true if a connector of this module is connected with
     * a connector owned to a different module
     */
    boolean hasOutgoingConnections();
    
    /**
     * Removes all connections of the connectors of this module. 
     */
    void removeConnections();

    /**
     * Returns the connector of this module with the specified descriptor. 
     *   
     * @param descriptor the descriptor used to identify a connector of this module
     * @return
     * @throws InvalidDescriptorException if the descriptor does not belong
     *   to a connector of this module
     */
    Connector getConnector(ConnectorDescriptor descriptor)
        throws InvalidDescriptorException;
    
    /**
     * Returns the number of connectors this module has.
     * @return the number of connectors this module has
     */
    int getConnectorCount();
    
    /**
     * Returns the parameter of this module with the specified descriptor. 
     *   
     * @param descriptor the descriptor used to identify a parameter of this module
     * @return
     * @throws InvalidDescriptorException if the descriptor does not belong
     *   to a parameter of this module
     */
    Parameter getParameter(ParameterDescriptor descriptor)
    throws InvalidDescriptorException;
    
    /**
     * Returns the number of parameters this module has.
     * @return the number of parameters this module has
     */
    int getParameterCount();

    void setScreenLocation(int x, int y);
    
    void setScreenLocation(Point location);
    
    Point getScreenLocation();

    int getScreenX();
    int getScreenY();

    void addModuleListener(ModuleListener l);
    void removeModuleListener(ModuleListener l);

    String getTitle();
    
    void setTitle(String title);
    
    int getUniqueId();

    void setUniqueId(int uniqueId);
    
}
