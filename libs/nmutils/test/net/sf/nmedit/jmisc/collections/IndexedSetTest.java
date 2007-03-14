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
 * Created on Sep 3, 2006
 */
package net.sf.nmedit.jmisc.collections;

import java.util.Collection;

import test.java.util.SetTest;

public class IndexedSetTest extends SetTest<Indexed>
{
    
    public static IndexedSet<Indexed> createSet(boolean empty)
    {
        IndexedSet<Indexed> set = new IndexedSet<Indexed>(new Mapping.IDMapping());
        if (!empty) 
        {
            for (Indexed item : createItems())
                set.add(item);
        }
        return set;
    }

    public static Indexed[] createItems()
    {
        return new Indexed[]
        {
                new IndexedObj(1),
                new IndexedObj(3),
                new IndexedObj(2),
                new IndexedObj(7),
                new IndexedObj(4),
        };
    }
    
    public static class IndexedObj implements Indexed 
    {
        final int index;
        
        public IndexedObj(int i)
        {
            this.index = i;
        }

        public int getIndex()
        {
            return index;
        }
        
        public boolean equals(Object o)
        {
            if (o!=null && o instanceof IndexedObj)
            {
                return ((IndexedObj)o).index == index;
            }
            return false;
        }
        
        public int hashCode()
        {
            return index;
        }
        
        public String toString()
        {
            return Integer.toString(index);
        }
        
    }

    @Override
    public Collection<Indexed> createEmptyCollection()
    {
        return createSet(true);
    }

    @Override
    public Indexed createUniqueElement( Collection<Indexed> c )
    {
        return new IndexedObj( ((IndexedSet<Indexed>) c).getSmallestEmptyKey() );
    }

    @Override
    public int getElementLimit()
    {
        return 0;
    }

}
