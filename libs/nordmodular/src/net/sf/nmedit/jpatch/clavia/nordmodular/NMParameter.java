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
 * Created on Apr 10, 2006
 */
package net.sf.nmedit.jpatch.clavia.nordmodular;

import net.sf.nmedit.jpatch.DefaultParameter;
import net.sf.nmedit.jpatch.Parameter;
import net.sf.nmedit.jpatch.ParameterDescriptor;

public class NMParameter extends DefaultParameter implements Parameter
{

    private final ParameterDescriptor descriptor;
    
    /**
     * 
     * morph = -127..127bbm
     * 
     * WRONG:
     * 
     * The real morph value has the same range as 'value'.
     * The internal morph value is relative to 'value'
     * and is in the range -maxValue<=internalMorph<=maxValue.
     * A negative value means it is smaller than value.
     * A positive value means it is bigger than value.
     * A value of null (0) means it is equal than value.
     */
    // private int morphRange;
    private final NMModule module;

    public NMParameter( ParameterDescriptor descriptor, NMModule module )
    {
        this.module = module;
        this.descriptor = descriptor;
        initValue();
    }

    public final ParameterDescriptor getDescriptor()
    {
        return descriptor;
    }

    public String getName()
    {
        return getDescriptor().getName();
    }

    public NMModule getOwner()
    {
        return module;
    }

}
