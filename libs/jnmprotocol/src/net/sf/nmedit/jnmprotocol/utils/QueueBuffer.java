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
 * Created on Jan 4, 2007
 */
package net.sf.nmedit.jnmprotocol.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * A queue that is optimized for buffering and fast 
 * insert/remove operations of elements. 
 * 
 * The queue uses a linked list as internal representation
 * and caches unused list items to avoid unecessary garbage.
 * 
 * @author Christian Schneider
 */
public class QueueBuffer<E> implements Queue<E>
{

    private Element<E> head;
    private Element<E> tail;
    private Element<E> cacheHead;
    private Element<E> cacheTail;
    private transient int modcount;
    
    public final boolean offer( E o )
    {
        Element<E> e ;
        if (cacheHead != null)
        {
            e = cacheHead;
            if (cacheHead.next != null)
                cacheHead = cacheHead.next;
            else
                cacheHead = cacheTail = null;
            e.next = null;
            e.data = o;
        }
        else
        {
            e = new Element<E>(o);
        }
        
        if (tail != null)
            tail.next = e;
        else
            head = tail = e;
        
        modcount++;
        return true;
    }

    public final E poll()
    {
        if (head != null)
        {
            // result element
            Element<E> e = head;
            
            // update head/tail
            head = (head.next != null) ? head.next : (tail=null);
            
            // result data
            E data = e.data;
            
            // cache e
            e.next = null;
            cache(e);

            modcount++;
            return data;
        }
        else
        {
            return null;
        }
    }

    public E remove()
    {
        if (isEmpty())
            throw new NoSuchElementException();
        return poll();
    }

    public final E peek()
    {
        return head != null ? head.data : null;
    }

    public final E element()
    {
        if (isEmpty())
            throw new NoSuchElementException();
        return peek();
    }

    public int size()
    {
        int size = 0;
        Element<E> pos = head;
        while (pos!=null)
        {
            size ++;
            pos = pos.next;
        }
        return size;
    }

    public final boolean isEmpty()
    {
        return head == null;
    }

    public boolean contains( Object o )
    {
        Element<E> pos = head;
        while (pos!=null)
        {
            if (pos.data==o || pos.data.equals(o))
                return true;
            
            pos = pos.next;
        }
        return false;
    }

    public Iterator<E> iterator()
    {
        return new Iterator<E> ()
        {
            
            E data = null;
            Element<E> pos = head;
            int knownMod = modcount;

            public boolean hasNext()
            {
                return pos != null;
            }
            
            private void checkMod()
            {
                if (knownMod != modcount)
                    throw new ConcurrentModificationException();
            }

            public E next()
            {
                checkMod();
                
                if (!hasNext())
                    throw new NoSuchElementException();
                
                data = pos.data;
                pos = pos.next;
                return data;
            }

            public void remove()
            {
                checkMod();
                
                if (data == null)
                    throw new IllegalStateException();
                QueueBuffer.this.remove(data);
                data = null;
            }
            
        };
    }

    public Object[] toArray()
    {
        List<Object> list = new ArrayList<Object>(size());
        list.addAll(this);
        return list.toArray();
    }

    public <T> T[] toArray( T[] a )
    {
        List<E> list = new ArrayList<E>(size());
        list.addAll(this);
        return list.toArray(a);
    }

    public boolean add( E o )
    {
        return offer(o);
    }

    public boolean remove( Object o )
    {
        if (isEmpty())
            return false;

        Element<E> prev = null;
        Element<E> pos = head;
        
        while (pos!=null)
        {
            
            if (pos.data == o || pos.data.equals(o))
            {
                if (head == pos)
                    head = head.next;
                if (tail == pos)
                    tail = prev;
                if (head == null ^ tail == null)
                    head = tail = null;
                if (prev != null)
                    prev.next = pos.next;

                modcount++;
                pos.next = null;
                cache(pos);
                
                return true;
            }
            
            prev = pos;
            pos = pos.next;
        }
        
        return false;
    }

    public boolean containsAll( Collection<?> c )
    {
        if (c == this || c.isEmpty())
            return true;

        for (Object o : c)
            if (!contains(o))
                return false;
        return true;
    }

    /**
     * Appends and removes each element of the specified queue to this queue.
     * The operation is done in O(1).
     * 
     * @param queue
     * @return
     */
    public boolean offerAll( QueueBuffer<E> queue )
    {
        if (!queue.isEmpty())
        {
            if (tail == null)
            {
                head = queue.head;
            }
            else
            {
                tail.next = queue.head;
            }
            tail = queue.tail;
            
            queue.head = queue.tail = null;
            
            if (cacheHead != null)
            {
                if (queue.cacheTail == null)
                    queue.cacheHead = queue.cacheTail = cacheHead;
                else
                    queue.cacheTail.next = cacheHead;
                cacheHead = cacheTail = null;
            }
            
            queue.modcount ++;
            modcount ++;
            
            return true;
        }
        return false;
    }
    
    public boolean addAll( Collection<? extends E> c )
    {
        throw new UnsupportedOperationException();
    }

    public boolean removeAll( Collection<?> c )
    {
        throw new UnsupportedOperationException();
    }

    public boolean retainAll( Collection<?> c )
    {
        throw new UnsupportedOperationException();
    }

    public void clear()
    {
        if (!isEmpty())
        {
            cache(head);
            head = tail = null;
            modcount++;
        }
    }
    
    private final void cache(Element<E> e)
    {   
        Element<E> pos = e;
        {
            pos.data = null;
            pos = pos.next;
        } 
        while (pos!=null);
        
        if (cacheTail != null)
            cacheTail.next = e;
        else
            cacheHead = cacheTail = e;
    }
    
    public void clearCache()
    {
        cacheHead = cacheTail = null;
    }
    
    private static class Element<E>
    {
        public E data;
        public Element<E> next;

        public Element(E data)
        {
            this.data = data;
            this.next = null;
        }
        
    }

}
