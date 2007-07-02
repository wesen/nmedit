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
package net.sf.nmedit.jpatch.event;

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PParameter;

/**
 * Event sent by a {@link PParameter parameter}.
 * 
 * @author Christian Schneider
 */
public class PParameterEvent extends PPatchEvent
{

    /**
     * 
     */
    private static final long serialVersionUID = -490310354383195515L;

    public PParameterEvent(PParameter parameter, int id)
    {
        super(parameter, id, null);
    }
    
    /**
     * Returns the module containing the {@link #getParameter() parameter}.
     * @return the module containing the parameter
     */
    public PModule getModule()
    {
        return getParameter().getParentComponent();
    }
    
    /**
     * Returns the parameter that changed.
     * @return the parameter
     */
    public PParameter getParameter()
    {
        return (PParameter) target;
    }

}
