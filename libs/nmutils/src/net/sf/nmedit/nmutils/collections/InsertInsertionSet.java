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
package net.sf.nmedit.nmutils.collections;

import java.io.IOException;
import java.io.Serializable;
import java.util.AbstractSet;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import net.sf.nmedit.nmutils.Arrays15;

public class InsertInsertionSet<E> extends AbstractSet<E> implements Set<E>, Serializable
{

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -8096374677355271025L;
    private transient Object[] elements;
    private transient int modCount;
    private int size = 0;
    protected boolean replaceEnabled = true;

    public InsertInsertionSet(int initialCapacity, boolean replaceEnabled)
    {
        elements = new Object[initialCapacity];
        this.replaceEnabled = replaceEnabled;
    }
    
    public boolean isOrderValueDefined(int ov)
    {
        return indexForOrderValue(ov, true) < size;
    }
    
    protected int orderValue(Object e)
    {
        return e.hashCode();
    }

    @SuppressWarnings("unchecked")
    private final E fastGet(int index)
    {
        return (E) elements[index];
    }
    
    public E getElementByOrderValue(int ov)
    {
        int index = indexForOrderValue(ov, true);
        return (index<size) ? fastGet(index) : null;
    }
    
    protected int indexForOrderValue(final int ov, final boolean find)
    {
        // O(ld(n)) complexity
        
        int l = 0;
        int r = size-1;
        int index = size;
        while (index<0)
        {
            if (l>r)
            {
                index = l;
                break;
            }
            int mid = l+(r-l)/2;
            int midov = orderValue(elements[mid]);
            if (midov<ov)
            {
                l = mid+1;
            }
            else if (midov>ov)
            { 
                r = mid-1;
            }
            else
            {
                if (!find) return -1;
                index = mid;
            }
        }
        return index;
    }


    public void ensureCapacity(int minCapacity) 
    {
        modCount++;
        int oldCapacity = elements.length;
        if (minCapacity > oldCapacity) 
        {
            // Object oldData[] = elements;
            int newCapacity = (oldCapacity * 3)/2 + 1;
            if (newCapacity < minCapacity)
                newCapacity = minCapacity;
            // minCapacity is usually close to size, so this is a win:
            // Java 6 only: elements = Arrays.copyOf(elements, newCapacity);
            elements = Arrays15.copyOf(elements, newCapacity);
            
        }
    }
    
    public boolean add(E e)
    {
        final int ov = orderValue(e);
        final int index = indexForOrderValue(ov, replaceEnabled);
        if (replaceEnabled)
        {
            if (index<size && ov==orderValue(elements[index]))
            {
                // replace
                modCount ++;
                elements[index] = e;
                return true;
            }
        }
        else if (index<0) return false; // element already defined
        
        ensureCapacity(size+1); // increments modcount !!
        
        if (index<size) // shift elements +1
            System.arraycopy(elements, index, elements, index+1, elements.length-index);
         elements[index] = e;
        size++;
        return true;
    }

    /*
     * Private remove method that skips bounds checking and does not
     * return the value removed.
     */
    private void fastRemove(int index) 
    {
        modCount++;
        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(elements, index+1, elements, index, numMoved);
        elements[--size] = null; // Let gc do its work
    }

    public E getElementAt(int index)
    {
        RangeCheck(index);
        return fastGet(index);
    }
    
    public E removeElementAt(int index)
    {
        RangeCheck(index);
        E e = fastGet(index);
        fastRemove(index);
        return e;
    }
    
    public void clear()
    {
        modCount++;
        // Let gc do its work
        for (int i = 0; i < size; i++)
            elements[i] = null;
        size = 0;
    }
    
    public int indexOf(Object o)
    {
        int index = indexForOrderValue(orderValue(o), true);
        if (index>=0)
        {
            Object e = elements[index];
            if (!(o == e || (o!=null && o.equals(e))))
                return -1;
        }
        return index;
    }

    public boolean contains(Object o)
    {
        return indexOf(o)>=0;
    }

    public boolean isEmpty()
    {
        return size==0;
    }

    public Iterator<E> iterator()
    {
        return new Itr();
    }

    public boolean remove(Object o)
    {
        int index = indexOf(o);
        if (index<0) return false;
        fastRemove(index);
        return true;
    }

    private void RangeCheck(int index) 
    {
        if (index >= size)
            throw new IndexOutOfBoundsException("Index: "+index+", Size: "+size);
    }
    
    public int size()
    {
        return size;
    }

    public Object[] toArray()
    {
        return Arrays15.copyOf(elements, size);
    }

    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a)
    {
        if (a.length < size)
            // Make a new array of a's runtime type, but my contents:
            return (T[]) Arrays15.copyOf(elements, size, a.getClass());
        System.arraycopy(elements, 0, a, 0, size);
        if (a.length > size)
            a[size] = null;
        return a;
    }
    
    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (o == null || (!(o instanceof InsertInsertionSet))) return false;

        Iterator<E> e1 = iterator();
        Iterator e2 = ((InsertInsertionSet) o).iterator();
        while(e1.hasNext() && e2.hasNext()) 
        {
            E o1 = e1.next();
            Object o2 = e2.next();
            if (!(o1==null ? o2==null : o1.equals(o2)))
                return false;
        }
        return !(e1.hasNext() || e2.hasNext());
    }

    
    private void writeObject(java.io.ObjectOutputStream out) throws IOException
    {
        // remember mod count to ensure the collection has not changed
        final int expectedModCount = modCount;
        
        // write size and replaceEnabled fields 
        out.defaultWriteObject();

        // write array length
        out.writeInt(elements.length);

        // write elements in the proper order.
        for (int i=0; i<size; i++)
            out.writeObject(elements[i]);

        // check for concurrent modification
        if (modCount != expectedModCount)
            throw new ConcurrentModificationException();
    }

    @SuppressWarnings("unchecked")
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        // read size and replaceEnabled fields
        in.defaultReadObject();

        // read in array length and allocate array
        int arrayLength = in.readInt();
        Object[] a = elements = new Object[arrayLength];

        // read elements in the proper order.
        for (int i=0; i<size; i++)
            a[i] = in.readObject();
    }
    
    private class Itr implements Iterator<E> 
    {
        /**
         * Index of element to be returned by subsequent call to next.
         */
        int cursor = 0;
    
        /**
         * Index of element returned by most recent call to next or
         * previous.  Reset to -1 if this element is deleted by a call
         * to remove.
         */
        int lastRet = -1;
    
        /**
         * The modCount value that the iterator believes that the backing
         * List should have.  If this expectation is violated, the iterator
         * has detected concurrent modification.
         */
        int expectedModCount = modCount;
    
        public boolean hasNext() 
        {
            return cursor != size();
        }
    
        public E next() 
        {
            checkForComodification();
            try 
            {
                E next = getElementAt(cursor);
                lastRet = cursor++;
                return next;
            } catch (IndexOutOfBoundsException e) 
            {
                checkForComodification();
                throw new NoSuchElementException();
            }
        }
    
        public void remove() 
        {
            if (lastRet == -1)
                throw new IllegalStateException();
            checkForComodification();
    
            try 
            {
                InsertInsertionSet.this.removeElementAt(lastRet);
                if (lastRet < cursor) cursor--;
                lastRet = -1;
                expectedModCount = modCount;
            } 
            catch (IndexOutOfBoundsException e) 
            {
                throw new ConcurrentModificationException();
            }
        }
    
        final void checkForComodification() 
        {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }
    
    
    
}
