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
 * Created on Apr 20, 2006
 */
package net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.misc;

import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Module;

public class ModuleSet extends AbstractSet<Module>
{
    
    private final static int SEG_SIZE = 100;

    protected Module[] map;
    private int size;
    private int last;

    public ModuleSet()
    {
        map = new Module[SEG_SIZE];
        Arrays.fill(map, null);
        
        size = 0;
        last =-1;
    }
    
    public int size()
    {
        return size;
    }

    public boolean isEmpty()
    {
        return size==0;
    }

    public boolean contains( Object o )
    {
        int index = ((Module) o).getIndex() -1;
        return (1<=index) && (index<=last) && (map[index]==o);
    }

    /**
     * Returns a value i with 0<=i<=size so that
     * map.length==i || map[i] == null 
     * 
     * @return returns an available index
     */
    private int availableIndex()
    {
        // note:
        // when (last+1 == size) is true,
        // then all fields are set 
        // => i=0..last : map[i]!=null
        
        if (last+1!=size)
        {
            // map[last]!=null
            for (int i=0;i<last;i++)
            {
                if (map[i]==null)
                {
                    return i;
                }
            }
        }
        
        return size;
    }

    private int requiredSegments(int lastIndex)
    {
        return (lastIndex / SEG_SIZE)+1;
    }

    private void autoExpand(int index)
    {
        if (index>=map.length)
        {            
            Module[] newMap = new Module[requiredSegments(index)*SEG_SIZE];
            for (int i=0;i<map.length;i++)
            {
                newMap[i] = map[i];
            }
            for (int i=map.length;i<newMap.length;i++)
            {
                newMap[i] = null;
            }
            map = newMap;
        }
    }

    private void autoContract()
    {
        int newSize = requiredSegments(last)*SEG_SIZE;
        if (newSize<map.length)
        {
            Module[] newMap = new Module[newSize];
            for (int i=0;i<newSize;i++)
            {
                newMap[i] = map[i];
            }
            map = newMap;
        }
    }

    public Module get(int index)
    {
        int internalIndex = index-1;
        
        if (0<=internalIndex && internalIndex<map.length)
        {
            return map[internalIndex];
        }
        else
        {
            throw new NoSuchElementException(""+index);
        }
        
    }
    
    public boolean add( Module m )
    {
        int index = m.getIndex()-1;
        
        if (index<0)
        {
            index = availableIndex();
            m.setIndex(index+1);
        }
        
        if (index<map.length && map[index]!=null)
        {
            return false;
        }
        else
        {
            autoExpand(index);
            map[index] = m;
            size ++;
            last = Math.max(index, last);
            return true;
        }
    }

    public boolean remove( Object o )
    {
        Module m = (Module) o;
        int index = m.getIndex()-1;
        if (0<=index && index<map.length && map[index]==o)
        {
            map[index] = null;
            size --;
            if (index == last)
            {
                while (last>=0 && map[last]==null)
                {
                    last --;
                }
                autoContract();
            }
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public Iterator<Module> iterator()
    {
        return new ModuleIterator();
    }

    public void clear()
    {
        for (int i=0;i<size;i++)
        {
            Module m = map[i];
            if (m!=null)
            {
                remove(m);
            }
        }
    }

    private class ModuleIterator extends FilteringIterator<Module>
    {
        public ModuleIterator( )
        {
            super( map, new Filter.Assigned<Module>() );
        }

        public void remove()
        {
            if (element!=null)
            {
                ModuleSet.this.remove(element);
                element = null;
            }
            else
            {
                throw new IllegalStateException();
            }
        }
    }

}
