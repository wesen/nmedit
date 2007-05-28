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
 * Represents a connection between two connectors a and b.
 * There is no restriction whether one of the connectors 
 * (a and b) must be an input or an output. 
 */
public class PConnection 
{

    private PConnector ca;
    private PConnector cb;
    
    public PConnection(PConnector ca, PConnector cb)
    {
        if (ca == null || cb == null)
            throw new NullPointerException("connectors must not be null");
        
        this.ca = ca;
        this.cb = cb;
    }

    public PConnector getA()
    {
        return ca;
    }
    
    public PConnector getB()
    {
        return cb;
    }
    
    public PConnectorDescriptor getDescriptorA()
    {
        return ca.getDescriptor();
    }
    
    public PConnectorDescriptor getDescriptorB()
    {
        return cb.getDescriptor();
    }

    public PModule getModuleA()
    {
        return ca.getParentComponent();
    }
    
    public PModule getModuleB()
    {
        return cb.getParentComponent();
    }
    
    public boolean contains(PConnector connector)
    {
        return ca == connector || cb == connector;
    }
    
    public boolean contains(PModule module)
    {
        return getModuleA() == module || getModuleB() == module;
    }
    
    public String toString()
    {
        return ca+"<->"+cb;
    }
    
    public int hashCode()
    {
        return ca.hashCode()+cb.hashCode();
    }
    
    public boolean equals(Object o)
    {
        if (o == null || (!(o instanceof PConnection)))
            return false;
        
        PConnection po = (PConnection) o;
        return contains(po.getA()) && contains(po.getB());
    }
    
}
