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

import java.util.Iterator;

import net.sf.nmedit.nmutils.collections.InsertInsertionSet;
import net.sf.nmedit.nmutils.collections.UnmodifiableIterator;

/**
 * The reference implementation of interface {@link PTypes}.
 * @author Christian Schneider
 */
public class PTypes<T extends PType> implements Iterable<T>
{

    /**
     * the types
     */
    private InsertInsertionSet<T> types;
    
    private String name;
    
    public PTypes(String name)
    {
        this.name = name;
        types = new InsertInsertionSet<T>(10, false);
    }

    public String getName()
    {
        return name;
    }

    public boolean isIdDefined(int id)
    {
        return types.isOrderValueDefined(id);
    }
    
    protected void addType(T type)
    {
        if (!types.add(type))
            throw new IllegalArgumentException("type already defined: "+type);
    }

    /**
     * Returns the type at the specified index.
     * @param index the type index
     * @return the type at the specified index
     */
    public T getType(int index)
    {
        return types.getElementAt(index);
    }

    /**
     * Returns the type with the specified id.
     * @param id the type id
     * @return the type with the specified id
     */
    public T getTypeById(int id)
    {
        return types.getElementByOrderValue(id);
    }

    /**
     * Returns the type with the specified name.
     * @param name name of the signal
     * @return the type with the specified name
     */
    public T getTypeByName(String name)
    {
        if (name == null)
            throw new NullPointerException("name must not be null");
        for (int i=types.size()-1;i>=0;i--)
        {
            T t = types.getElementAt(i);
            String n = t.getName();
            if (name == n || (n!=null && name.equals(n)))
                return t;
        }
        return null;
    }

    /**
     * Returns the number of defined types.
     * @return the number of defined types
     */
    public int size()
    {
        return types.size();
    }

    public Iterator<T> iterator()
    {
        return new UnmodifiableIterator<T>(types.iterator());
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName());
        sb.append('[');
        int cnt = 0;
        final int size = types.size();
        for (int i=0;i<size;i++)
        {
            sb.append(types.getElementAt(i));
            if (++cnt<size)
                sb.append(",");
        }
        sb.append(']');
        return sb.toString();
    }

    public int hashCode()
    {
        return types.hashCode();
    }

    /**
     * Returns true if the specified type is defined.
     * 
     * @param type the type
     * @return true if the specified type is defined
     */
    public boolean contains(PType type)
    {
        return (type != null) && types.contains(type);
    }
    
    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (o == null || (!(o instanceof PTypes))) return false;
        
        PTypes<?> ts = (PTypes) o;
        if (size() != ts.size()) return false;

        for (int i=size()-1;i>=0;i--)
            if (!ts.contains(ts.getType(i)))
                return false;

        return true;
    }

}
