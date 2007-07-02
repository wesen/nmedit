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
 * Created on Dec 18, 2006
 */
package net.sf.nmedit.nmutils.collections;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayMap<E> implements Iterable<E>, Cloneable, Serializable
{

    /**
     * 
     */
    private static final long serialVersionUID = -3401325714509820645L;
    private E[] elements;
    private int size = 0;
    private int minkey = 0;
    private transient int modcount;

    @SuppressWarnings("unchecked")
    public ArrayMap()
    {
        elements = (E[])new Object[10];
    }

    public void clear()
    {
        size = 0;
        Arrays.fill(elements, null);
    }

    public int getMinKey()
    {
        return minkey;
    }
    
    public void setMinKey(int min)
    {
        if (min<0)
            throw new IllegalArgumentException("invalid minimum key:"+min);
        this.minkey = min;
    }
    
    public int generateIndex()
    {
        // prefer size rather than elements.length
        // because elements in range[size..elements.length]
        // are probably not set
        return generateIndex(size+getMinKey());
    }

    public int generateIndex(int start)
    {
        validateIndex(start);
        while (start<elements.length && elements[start]!=null)
            start++;
        return start;        
    }
    
    public int indexOf(Object o)
    {
        for (int i=0;i<elements.length;i++)
        {
            Object e = elements[i];
            if (e!= null &&(e==o || e.equals(o)))
                return i;
        }
        return -1;
    }

    public E get(int index)
    {
        validateIndex(index);
        return index<elements.length ? elements[index] : null;
    }
    
    public E put(IndexedElement<E> e)
    {
        return put(e.getIndex(), e.getElement());
    }
    
    protected void validateIndex(int index)
    {
        if (index<getMinKey())
            throw new IllegalArgumentException("invalid index: "+index);
    }

    @SuppressWarnings("unchecked")
    public E put(int index, E element)
    {
        validateIndex(index);
        E old = null;
        
        if (index<elements.length)
        {
            old = elements[index];
            elements[index] = element;
        }
        else if (element != null)
        {
            // IMPLIED: index>=elements.length
            // add capacity
            E[] na = (E[]) new Object[Math.max(index+1, elements.length*2)];
            System.arraycopy(elements, 0, na, 0, elements.length);
            elements = na;
            elements[index] = element;
        }

        if (old != element)
        {
            if (old!=null) size--;
            if (element!=null) size++;

            modcount++;
        }
        return old;
    }

    public boolean contains(Object o)
    {
        return indexOf(o) >= 0;
    }
    
    public int size()
    {
        return size;
    }
    
    public E remove(int index)
    {
        return put(index, null);
    }

    public Iterator<Integer> keyIterator()
    {
        return new Iterator<Integer>()
        {
            
            private int knownmod = modcount;
            private int index = align(-1);

            private int align(int index)
            {
                while (++index<elements.length && elements[index]==null)
                {
                    ;
                }
                return index;
            }

            public boolean hasNext()
            {
                return index<elements.length;
            }
            
            private void checkMod()
            {
                if (knownmod != modcount)
                    throw new ConcurrentModificationException();
            }

            public Integer next()
            {
                checkMod();
                if (!hasNext())
                    throw new NoSuchElementException();
                Integer result = new Integer(index);
                index = align(index);
                return result;
            }

            public void remove()
            {
                throw new UnsupportedOperationException();
            }
            
        };
    }

    public Iterator<E> iterator()
    {
        return new Iterator<E>()
        {
            
            private int knownmod = modcount;
            private int index = align(-1);
            private int storeindex = -1;

            private int align(int index)
            {
                while (++index<elements.length && elements[index]==null);
                return index;
            }

            public boolean hasNext()
            {
                return index<elements.length;
            }
            
            private void checkMod()
            {
                if (knownmod != modcount)
                    throw new ConcurrentModificationException();
            }

            public E next()
            {
                checkMod();
                if (!hasNext())
                    throw new NoSuchElementException();
                
                storeindex = index;
                E e = elements[index];
                index = align(index);
                return e;
            }

            public void remove()
            {
                checkMod();
                if (storeindex<0)
                    throw new IllegalStateException();

                put(storeindex, null);
                storeindex = -1;
                knownmod = modcount;
            }
            
        };
    }

    @SuppressWarnings("unchecked")
    public ArrayMap<E> clone()
    {
        ArrayMap<E> clone = null;
        try
        {
            clone = (ArrayMap<E>) super.clone();
            clone.size = size;
            if (clone.elements == null || clone.elements==elements)
                clone.elements = (E[]) elements.clone();
        }
        catch (CloneNotSupportedException e)
        {
            throw new InternalError(e.getMessage());
        }
        return clone;
    }
    
    public boolean equals(Object o)
    {
        if (o == this)
            return true;
        
        if (!(o instanceof ArrayMap))
            return false;

        ArrayMap am = (ArrayMap)o;
        if (size()!=am.size())
            return false;
        
        
        Object[] e2 = am.elements;
        int s = Math.min(elements.length, e2.length);
        for (int i=0;i<s;i++)
            if (!elements[i].equals(e2[i]))
                return false;
        return true;
    }
    
    /**
     * @serialData the smallest key (int), the size field (int), largest index (int), followed by the pairs index(int),elements
     */
    private void writeObject(java.io.ObjectOutputStream out) throws IOException
    {
         out.defaultWriteObject();

         int max = elements.length;
         while (--max>=0 && elements[max]==null);

         out.writeInt(minkey);
         out.writeInt(size);
         out.writeInt(max);
         for (int i=max;i>=0;i--)
         {
             Object o = elements[i];
             if (o!=null)
             {
                 out.writeInt(i);
                 out.writeObject(o);
             }
         }
    }
    /**
     * @serialData the smallest key (int), the size field (int), largest index (int), followed by the pairs index(int),elements
     */
    @SuppressWarnings("unchecked")
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();

        minkey = in.readInt();
        size = in.readInt();
        
        int max = in.readInt();
        Object[] e = new Object[max+1];
        
        for (int i=0;i<size;i++)
        {
            int key = in.readInt();
            e[key] = in.readObject();
        }

        elements = (E[]) e;
    }

    public boolean containsKey( int index )
    {
        return index>= 0 && index<elements.length && elements[index]!=null;
    }
    
    
}
