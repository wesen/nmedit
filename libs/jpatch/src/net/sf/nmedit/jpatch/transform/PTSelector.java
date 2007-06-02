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
package net.sf.nmedit.jpatch.transform;

import net.sf.nmedit.jpatch.PConnectorDescriptor;
import net.sf.nmedit.jpatch.PDescriptor;
import net.sf.nmedit.jpatch.PParameterDescriptor;
import net.sf.nmedit.jpatch.PRuntimeException;

public class PTSelector
{

    public static final int CONNECTOR = 1;
    public static final int PARAMETER = 2;
    
    // the descriptor of the selected component
    private PDescriptor descriptor;
    // the selector id
    private int selectorId;

    private int type ;

    /**
     * Selects a parameter.
     * @param descriptor the descriptor of the selected parameter
     * @param selectorId the selector id
     */
    public PTSelector(PParameterDescriptor descriptor, int selectorId)
    {
        this((PDescriptor)descriptor, selectorId, PARAMETER);
    }
    
    /**
     * Selects a connector.
     * @param descriptor the descriptor of the selected connector
     * @param selectorId the selector id
     */
    public PTSelector(PConnectorDescriptor descriptor, int selectorId)
    {
        this((PDescriptor)descriptor, selectorId, CONNECTOR);
    }
    
    /**
     * Selects a component.
     * @param descriptor the descriptor of the selected connector
     * @param selectorId the selector id
     */
    protected PTSelector(PDescriptor descriptor, int selectorId, int type)
    {
        this.descriptor = descriptor;
        this.selectorId = selectorId;
        this.type = type;
    }
    
    public int getType()
    {
        return type;
    }
    
    public PConnectorDescriptor getConnector()
    {
        try
        {
            return (PConnectorDescriptor) descriptor;
        }
        catch (ClassCastException e)
        {
            throw new PRuntimeException("not a connector");   
        }
    }
    
    public PParameterDescriptor getParameter()
    {
        try
        {
            return (PParameterDescriptor) descriptor;
        }
        catch (ClassCastException e)
        {
            throw new PRuntimeException("not a parameter");   
        }
    }
    
    /**
     * Returns the descriptor of the selected connector.
     * @return the descriptor of the selected connector
     */
    public PDescriptor getDescriptor()
    {
        return descriptor;
    }
    
    /**
     * Returns the selector id.
     * @return the selector id
     */
    public int getSelectorId()
    {
        return selectorId;
    }
    
    /**
     * Returns the class name, selector id and the descriptor.
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return getClass().getSimpleName()+"[selector="+selectorId+",descriptor="+descriptor+"]";
    }

    void setSelectorId(int id)
    {
        this.selectorId = id;
    }
    
}
