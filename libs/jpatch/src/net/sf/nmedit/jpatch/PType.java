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


public class PType //implements Serializable
{
    private int id;
    private String name;

    public PType(int id, String name)
    {
        this.id = id;
        this.name = name;
    }

    /**
     * Returns the name of this type. The return value
     * must not be <code>null</code>.
     * @return the name of the type
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns an identifier for this type.
     * The identifier is unique in the {@link PTypes defined type set}.
     * @return itentifier for this type.
     */
    public int getId()
    {
        return id;
    }

    /**
     * Returns the {@link getId() id} of this type.
     */
    public int hashCode()
    {
        return id;
    }
    
    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (o == null || (!(o instanceof PType))) return false;
        
        PType t = (PType) o;
        return t.id == id && eq(t.name, name);
    }

    public String toString()
    {
        return getClass().getName()+"[id="+getId()+",name="+getName()+"]";
    }

    protected final boolean eq(Object a, Object b)
    {
        return a == b || (a!=null && a.equals(b));
    }

}
