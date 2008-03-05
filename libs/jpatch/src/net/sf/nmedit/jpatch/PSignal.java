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

import java.awt.Color;

/**
 * Identifies a specific signal type for example 
 * an audio signal, a logic signal or a control signal. 
 */
public class PSignal extends PType
{

    private static final long serialVersionUID = -2637295506140546814L;
    
    private PSignalTypes parent;
    private Color color;

    public PSignal(PSignalTypes parent, int id, String name, Color color)
    {
        super(id, name);
        this.parent = parent;
        this.color = color;
    }

    /**
     * Returns the name of this signal. The return value
     * must not be <code>null</code>. The value might be
     * displayed to the user (used as fallback), 
     * thus it should be named carefully.
     * @return the name of this signal
     */
    public String getName()
    {
        return super.getName();
    }

    /**
     * Returns an identifier of this signal.
     * Each {@link #getDefinedSignals() signal} is unique.
     * @return the identifier of this signal
     */
    public int getId()
    {
        return super.getId();
    }

    /**
     * Returns the defined signal types.
     * @return the defined signal types
     */
    public PSignalTypes getDefinedSignals()
    {
        return parent;
    }

    public String toString()
    {
        return getClass().getName()+"[id="+getId()+",name="+getName()+",color="+color+"]";
    }

    /**
     * Returns the preferred color for this signal.
     * The value must not be null.
     * @return the preferred color for this signal
     */
    public Color getColor()
    {
        return color;
    }
    
    public int hashCode()
    {
         return super.hashCode();
    }
    
    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (o == null || (!(o instanceof PSignal))) return false;
        
        PSignal st = (PSignal) o;
        return st.getId() == getId() && eq(getName(), st.getName());
    }

}
