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
 * Created on Dec 10, 2006
 */
package net.sf.nmedit.jpatch.spec;

import net.sf.nmedit.jpatch.ConnectorDescriptor;
import net.sf.nmedit.jpatch.ModuleDescriptor;

public class DefaultConnectorDescriptor extends BasedDescriptor implements ConnectorDescriptor
{
    
    private boolean isOutput = true;
    private ModuleDescriptor module;
    private String name;
    private int signalId = -1;
    private String className;
    private int index;

    public DefaultConnectorDescriptor(ModuleDescriptor module, String name, boolean isOutput, int index)
    {
        this.module = module;
        this.name = name;
        this.isOutput = isOutput;
        this.index = index;
    }
    
    public String toString()
    {
        return getClass().getName()+"[name="+name+",index="+index+",output="+isOutput+",module-index="+module.getIndex()+"]";
    }

    public boolean isOutput()
    {
        return isOutput;
    }

    public ConnectorDescriptor getSourceDescriptor()
    {
        return null;
    }

    public ModuleDescriptor getModuleDescriptor()
    {
        return module;
    }

    public String getComponentName()
    {
        return name;
    }

    public String getName()
    {
        return getClass().getName();
    }
    
    public void setSignalId(int sid)
    {
        this.signalId = sid;
    }

    public int getSignalId()
    {
        return signalId;
    }

    public void setConnectorClass( String className )
    {
        this.className = className;
    }
    
    public String getConnectorClass()
    {
        return className;
    }

    public int getIndex()
    {
        return index;
    }
    
}
