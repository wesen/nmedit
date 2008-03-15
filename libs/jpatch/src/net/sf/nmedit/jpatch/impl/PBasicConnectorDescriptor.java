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
package net.sf.nmedit.jpatch.impl;

import net.sf.nmedit.jpatch.PConnectorDescriptor;
import net.sf.nmedit.jpatch.PModuleDescriptor;
import net.sf.nmedit.jpatch.PSignal;
import net.sf.nmedit.jpatch.PSignalTypes;

/**
 * The reference implementation of interface {@link PConnectorDescriptor}.
 * @author Christian Schneider
 */
public class PBasicConnectorDescriptor extends PBasicDescriptor 
    implements PConnectorDescriptor//, Serializable
{

    private static final long serialVersionUID = -4016101017192946943L;
    private PSignal defaultSignal;
    private boolean output;
    private transient PModuleDescriptor parent;
    
    public PBasicConnectorDescriptor(PModuleDescriptor parent, String name, Object componentId, 
            PSignal defaultSignal, boolean output)
    {
        super(name, componentId);
        this.parent = parent;
        this.defaultSignal = defaultSignal;
        this.output = output;
    }
    
    void setParent(PModuleDescriptor parent)
    {
        this.parent = parent;
    }

    public PSignal getDefaultSignalType()
    {
        return defaultSignal;
    }

    public boolean isOutput()
    {
        return output;
    }
    
    public PModuleDescriptor getParentDescriptor()
    {
        return parent;
    }

    protected void toStringProperties(StringBuilder sb)
    {
        super.toStringProperties(sb);
        sb.append(",output="+output+",default-signal="+defaultSignal);
    }

    public PSignalTypes getDefinedSignals()
    {
        return getParentDescriptor().getModules().getDefinedSignals();
    }

    /*
    private void writeObject(java.io.ObjectOutputStream out) throws IOException
    {
        out.defaultWriteObject();
    }
    
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
    }*/
    

}
