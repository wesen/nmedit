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
 * The reference implementation of interface {@link PSignalTypes}.
 * @author Christian Schneider
 */
public class PSignalTypes extends PTypes<PSignal>
{
    
    /**
     * 
     */
    private static final long serialVersionUID = -7327837439471694263L;
    private PSignal noSignal = null;

    public PSignalTypes(String name)
    {
        super(name);
    }

    public PSignal create(int id, String name, boolean noSignal)
    {
        return create(id, name, noSignal ? Color.WHITE : Color.BLACK, noSignal);
    }

    public PSignal create(int id, String name, Color color, boolean noSignal)
    {
        PSignal signal = new PSignal(this, id, name, color);
        super.addType(signal);
        if (noSignal) this.noSignal = signal;
        return signal;
    }

    /**
     * Returns the signal definition for the no-signal situation.
     * @return the no-signal
     */
    public PSignal noSignal()
    {
        return noSignal;
    }

    /**
     * Returns true if the specified signal is defined,
     * false otherwise.
     * @param signal the signal
     * @return true if the specified signal is defined
     */
    public boolean contains(PType type)
    {
        return (type instanceof PSignal) && super.contains(type);
    }
    /*
    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (o == null || (!(o instanceof PSignalTypes))) return false;
        
        PSignalTypes st = (PSignalTypes) o;
        if (st.getSignalTypeCount() != getSignalTypeCount())
            return false;
        
        if (!(noSignal == st.noSignal() || (noSignal != null && noSignal.equals(st.noSignal()))))
            return false;
        
        for (int i=getSignalTypeCount()-1;i>=0;i--)
            if (!st.contains(signalList.get(i)))
                return false;
        return true;
    }
    */
}
