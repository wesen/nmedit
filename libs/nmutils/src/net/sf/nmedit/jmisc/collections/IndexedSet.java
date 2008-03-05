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
 * Created on Sep 2, 2006
 */
package net.sf.nmedit.jmisc.collections;

import java.io.IOException;
import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

@SuppressWarnings("unchecked")
public class IndexedSet<E> extends AbstractSet<E> 
    implements Set<E>, Serializable, Cloneable
{
    
    /**
     * 
     */
    private static final long serialVersionUID = -6699327676964923532L;
    private transient int modCount;
    private int size = 0;
    private int max = -1;
    private transient Mapping mapping;
    private E[] elements;

    public IndexedSet(Mapping mapper)
    {
        elements = (E[]) new Object[10];
        this.mapping = mapper;
    }
    
    public Mapping getMapping()
    {
        return mapping;
    }
    
    public int getEmptyKey()
    {
        return max+1;
    }
    
    public int getSmallestEmptyKey()
    {
        int k = 0;
        while ((k<=max) && (elements[k]!=null)) k++;
        return k;
    }
    
    public int getNextEmptyKey(int index)
    {
        while ((++index<=max)&&(elements[index]!=null));
        return index;
    }
    
    public int size()
    {
        return size;
    }

    public boolean isEmpty()
    {
        return size<=0;
    }
    
    private boolean eq(E e, Object o)
    {
        if (e==null||o==null)
            return false;
        
        if (e.equals(o))
        {
            // this is not be necessary but we check it
            return mapping.getIndex(e) == mapping.getIndex(o);
        }
        return false;
    }

    public boolean contains( Object o )
    {
        if (o==null) return false;
        int index = mapping.getIndex(o);
        return (0<=index) && (index<=max) && eq(elements[index], o);
    }
    
    
    private void resize(int newsz)
    {
        E[] nElements = (E[]) new Object[newsz];
        System.arraycopy(elements, 0, nElements, 0, max);
        elements = nElements;
    }
    
    public boolean add( E o )
    {
        int index = mapping.getIndex(o);
        if (index>max)
        {
            max = index;
            if (index>=elements.length)
                resize(Math.max(index, elements.length+9)+1);
        }
        else if (index<0)
            throw new IndexOutOfBoundsException("invalid index:"+index);
        else if (elements[index]!=null)
            return false; // can't add two elements at same position
        
        elements[index] = o;
        size++;
        modCount ++;
        return true;
    }

    private void completeRemove(int index)
    {
        elements[index] = null;
        if (index==max)
            while ((--max>=0)&&(elements[max]==null));

        size --;
        modCount ++;

        if ((10<max)&&(max<(elements.length>>1)))
            resize(max);
    }
    
    public boolean remove( Object o )
    {
        int index = mapping.getIndex(o);
        if ((0<=index) && (index<=max) && eq(elements[index], o))
        {
            completeRemove(index);
            return true;
        }
        return false;
    }
    
    public E removeElementAt(int index)
    {
        E result = getElementAt(index);
        if (result!=null)
            completeRemove(index);
        return result;
    }

    public E getElementAt(int index)
    {
        return (0<=index && index<=max) ? elements[index] : null;
    }
    
    public boolean containsKey(int index)
    {
        return getElementAt(index)!=null;
    }
    
    public void clear()
    {
        Arrays.fill(elements, null);
        size = 0;
        max = -1;
        modCount ++;
    }

    public Iterator<E> iterator()
    {
        return new Iterator<E>()
        {
            
            int pos = find(-1);
            int cur = -1;
            int knownMod = modCount;

            private int find(int pos)
            {
                while ((++pos<=max) && (elements[pos]==null)) ;
                return pos ;
            }
            
            private void checkMod()
            {
                if (modCount!=knownMod)
                    throw new ConcurrentModificationException();
            }
            
            public boolean hasNext()
            {
                return pos<=max;
            }

            public E next()
            {
                checkMod();
                if (!hasNext())
                    throw new NoSuchElementException();
                E element = elements[cur = pos];
                pos = find(pos);
                return element;
            }

            public void remove()
            {
                checkMod();
                if ((0<=cur)&&(cur<=max))
                {
                    completeRemove(cur);
                    cur = -1;
                    knownMod = modCount;
                }
                else
                {
                    throw new IllegalStateException();
                }
            }
            
        };
    }
    
    /**
     * @serialData the mapping (object), the size field (int), followed by the elements
     */
    private void writeObject(java.io.ObjectOutputStream out) throws IOException
    {
         out.defaultWriteObject();
         out.writeObject(mapping);
         out.writeInt(size);
         for (E e : this)
         {
             out.writeObject(e);
         }
    }
    
    public Object clone()
    {
        IndexedSet<E> clone = null;
        try
        {
            clone = (IndexedSet<E>) super.clone();
            clone.elements = (E[]) elements.clone();
        }
        catch (CloneNotSupportedException e)
        {
            throw new InternalError(e.getMessage());
        }
        return clone;
    }

    /**
     * @serialData the mapping (object), the size field (int), followed by the elements
     */
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
        mapping = (Mapping) in.readObject();
        size = in.readInt();
        elements = (E[]) new Object[size];
        max = -1;
        for (int i=0;i<size;i++)
        {
            E e = (E) in.readObject();
            int index = mapping.getIndex(e);
            elements[index] = e;
            max = Math.max(index, max);
        }
    }
    
}
