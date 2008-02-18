/*
    Nord Modular Midi Protocol 3.03 Library
    Copyright (C) 2003-2006 Marcus Andersson

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/


package net.sf.nmedit.jnmprotocol.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

import net.sf.nmedit.jnmprotocol.utils.QueueBuffer;

/**
 * A queue that is optimized for buffering and fast 
 * insert/remove operations of elements. 
 * 
 * The queue uses two different linked lists internally.
 * The first list contains the elements that have an object
 * associated with them. The second list to which we refer
 * with the term cache, contains the elements
 * that have no object associated with them anymore.
 * 
 * If elements that contain data are removed from the queue
 * (for example using poll()) the will be stored in the cache
 * (the reference to the data is set to null).
 * Later if a data is offered to the queue, it is not necessary
 * to create a new list element but instead an element from the
 * cache can be used.
 * 
 * The advantages of the cache are:
 * <ul>
 * <li>it is faster to reuse an element instead of allocating a new one</li>
 * <li>less in best case no garbage is created. The garbage collector
 * has less work to do</li>
 * <li>memory consumption is constant if the queue is used correctly.
 * A correct use means that the maximum number of elements is below
 * or equal a constant boundary. For example the queue contains always
 * &lt;=1000 elements. A queue that uses no such cache can produce
 * up to several MB of garbage. 
 *  </li>
 * </ul>
 * 
 * The queue uses a linked list as internal representation
 * and recycles unused list items to avoid unecessary garbage.
 * 
 * @author Christian Schneider
 */
public class QueueBuffer<E> implements Queue<E>
{
    
    /**
     * Element of a linked list. Contains the stored element data and
     * the next element in the list.
     */
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

    // head is the first element in the list (the front element in this queue).
    // always one of the following contitions is true:
    // - head == null 
    //     if and only if the queue is empty
    // - head == tail && head!=null
    //     if and only if the number of elements in the queue is 1
    // - head != tail 
    //     if and only if the queue contains more than one element
    private Element<E> head;
    // tail is the last element in the list (the last element in this queue).
    // always one of the following contitions is true:
    // - tail == null
    //     if and only if the queue is empty
    // - head == tail && head!=null
    //     if and only if the number of elements in the queue is 1
    // - head != tail
    //     if and only if the queue contains more than one element
    private Element<E> tail;
    // the head of the list of cached elements. This list contains the elements
    // that were previously used and removed from the queue. We recycle
    // them if new data is offered to the queue.
    // elements in the cache contain no data, thus the Element.data field is always null
    // always one of the following contitions is true:
    // - cacheHead == null
    //     if and only if the cache is empty
    // - cacheHead == cacheTail && cacheHead!=null
    //     if and only if the number of elements in the cache is 1
    // - cacheHead != cacheTail
    //     if and only if the cache contains more than one element
    private Element<E> cacheHead;
    // the tail of the list of cached elements
    // elements in the cache contain no data, thus the Element.data field is always null
    // always one of the following contitions is true:
    // - cacheTail == null
    //     if and only if the cache is empty
    // - cacheHead == cacheTail && cacheHead!=null
    //     if and only if the number of elements in the cache is 1
    // - cacheHead != cacheTail
    //     if and only if the cache contains more than one element
    private Element<E> cacheTail;
    
    // variable is changed if the collection is modified
    private transient int modcount;
    
    /**
     * Removes all elements from this queue and adds them to a new queue.
     * @return a new queue
     */
    public QueueBuffer<E> release()
    {
        QueueBuffer<E> q = new QueueBuffer<E>();
        q.head = head;
        q.tail = tail;
        head = null;
        tail = null;
        modcount++;
        return q;
    }
    
    /**
     * Inserts the specified element into this queue.
     * 
     * If possible the queue will not create new element
     * container but recycle one that is not used anymore.
     *
     * @param o the element to insert.
     * @return <tt>true</tt> if o is not null, otherwise false
     */
    public final boolean offer( E o )
    {
        if (o == null)
            return false;
        
        // the new element
        Element<E> e ;

        // we check if we can recycle the new element
        // or if we have to create a new instance 
        if (cacheHead != null)
        {
            // recycle the head of the cache
            e = cacheHead;
            
            // remove head element from cache
            cacheHead = cacheHead.next;
            if (cacheHead==null)
                cacheTail = null;
            
            // initialize element fields
            e.next = null;
            e.data = o;
        }
        else
        {
            // create a new instance
            e = new Element<E>(o);
        }

        // no we add the element it to the queue
        // we first have to check if the queue is empty
        // or if it already contains some elements
        if (tail != null)
        {
            // the queue is not empty
            
            // the last element will be e 
            tail.next = e;
            // and e will become the new tail
            tail = e;
        }
        else
        {
            // the queue is empty, thus e is both
            // head and tail
            head = tail = e;
        }
        
        // we have changed the queue
        modcount++;
        
        // always return true
        return true;
    }

    /**
     * Retrieves and removes the head of this queue, or <tt>null</tt>
     * if this queue is empty.
     *
     * The element container of the removed element will be 
     * added to a cache and recycled if further elements are added
     * to the qeue.
     *
     * @return the head of this queue, or <tt>null</tt> if this
     *         queue is empty.
     */
    public final E poll()
    {
        // first see if the queue is empty and return null if so
        if (head == null)
            return null;
        
        // the queue is not empty

        // the result element / data
        Element<E> e = head;
        E result = e.data;

        // if the head is removed then
        // - the next element in the queue will become the new head
        head = head.next;  
        // - if the next element is null then tail will become null, too
        if (head == null)
            tail = null;
        
        // now we can cache the unused element container
        // first disconnect it
        e.next = null;
        // then add it to the cache (e.data will be automatically set to null)
        cache(e);

        // we have changed the queue
        modcount++;
        
        // we are done
        return result;
    }

    /**
     * Retrieves and removes the head of this queue.  This method
     * differs from the <tt>poll</tt> method in that it throws an
     * exception if this queue is empty.
     *
     * @return the head of this queue.
     * @throws NoSuchElementException if this queue is empty.
     */
    public E remove()
    {
        E e = poll();
        if (e == null)
            throw new NoSuchElementException();
        return e;
    }

    /**
     * Retrieves, but does not remove, the head of this queue,
     * returning <tt>null</tt> if this queue is empty.
     *
     * @return the head of this queue, or <tt>null</tt> if this queue
     * is empty.
     */
    public final E peek()
    {
        return head != null ? head.data : null;
    }

    /**
     * Retrieves, but does not remove, the head of this queue.  This method
     * differs from the <tt>peek</tt> method only in that it throws an
     * exception if this queue is empty.
     *
     * @return the head of this queue.
     * @throws NoSuchElementException if this queue is empty.
     */
    public final E element()
    {
        E e = peek();
        if (e == null)
            throw new NoSuchElementException();
        return e;
    }

    /**
     * Returns the number of elements in this collection.  If this collection
     * contains more than <tt>Integer.MAX_VALUE</tt> elements, returns
     * <tt>Integer.MAX_VALUE</tt>.
     * 
     * Do not use this method if not absolutely necessary because it
     * has complexity O(n). 
     * 
     * @return the number of elements in this collection
     */
    public int size()
    {
        int size = 0;
        Element<E> pos = head;
        while (pos!=null && size!=Integer.MAX_VALUE)
        {
            size ++;
            pos = pos.next;
        }
        return size;
    }

    /**
     * Returns <tt>true</tt> if this collection contains no elements.
     *
     * @return <tt>true</tt> if this collection contains no elements
     */
    public final boolean isEmpty()
    {
        return head == null;
    }

    /**
     * Returns <tt>true</tt> if this collection contains the specified
     * element.  More formally, returns <tt>true</tt> if and only if this
     * collection contains at least one element <tt>e</tt> such that
     * <tt>(o==null ? e==null : o.equals(e))</tt>.
     *
     * @param o element whose presence in this collection is to be tested.
     * @return <tt>true</tt> if this collection contains the specified
     *         element
     * @throws NullPointerException if the specified element is null and this
     *         collection does not support null elements (optional).
     */
    public boolean contains( Object o )
    {
        if (o==null)
            throw new NullPointerException();
        
        Element<E> pos = head;
        while (pos!=null)
        {
            if (pos.data==o || pos.data.equals(o))
                return true;
            
            pos = pos.next;
        }
        return false;
    }

    /**
     * Returns an iterator over the elements in this collection. 
     * The elements are returned in the same order like they are
     * stored in this queue. 
     *  
     * @return an <tt>Iterator</tt> over the elements in this collection
     */
    public Iterator<E> iterator()
    {
        return new Iterator<E> ()
        {
            // the data of the element returned by next() 
            // or null if next() was not called or remove() was called 
            E data = null;
            // current position, starting at the head of the queue
            // if pos == null then the iteration is completed
            Element<E> pos = head;
            // remember modification counter so we can see if
            // concurrent modifications were performed
            int knownMod = modcount;

            public boolean hasNext()
            {
                return pos != null;
            }
            
            /** 
             * Checks if the modification counter has changed and
             * throws a ConcurrentModificationException if this is
             * the case.
             */
            private void checkMod()
            {
                if (knownMod != modcount)
                    throw new ConcurrentModificationException();
            }

            public E next()
            {
                // check for modifications
                checkMod();
                
                // see if element exists 
                if (!hasNext())
                    throw new NoSuchElementException();
                
                // remember data
                data = pos.data;
                // remember position
                pos = pos.next;
                return data;
            }

            public void remove()
            {
                // check for modifications
                checkMod();
                // check if data!=null what means that next() has been called
                if (data == null)
                    throw new IllegalStateException();
                // remove data
                QueueBuffer.this.remove(data);
                // set data to null since we also use it as state variable
                data = null;
                // update the known modification value
                knownMod = modcount;
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


    /**
     * Adds the specified element to the queue.
     *
     * @param o element whose presence in this collection is to be ensured.
     * @return <tt>true</tt> 
     * @throws NullPointerException if the specified element is null.
     */
    public boolean add( E o )
    {
        if (o == null)
            throw new NullPointerException();
        return offer(o);
    }

    /**
     * Removes a single instance of the specified element from this
     * collection, if it is present.  More formally,
     * removes an element <tt>e</tt> such that <tt>(o==null ?  e==null :
     * o.equals(e))</tt>, if this collection contains one or more such
     * elements.  Returns true if this collection contained the specified
     * element.
     *
     * @param o element to be removed from this collection, if present.
     * @return <tt>true</tt>  if this collection contained the specified element
     * @throws NullPointerException if the specified element is null.
     */
    public boolean remove( Object o )
    {
        // null-elements are not allowed
        if (o==null)
            throw new NullPointerException();
        // the element is not in the queue
        if (isEmpty())
            return false;

        // prev is the previous element of pos
        Element<E> prev = null;
        // the current position in the queue
        Element<E> pos = head;
        
        while (pos!=null)
        {
            // see if we have found the specified element
            if (pos.data == o || pos.data.equals(o))
            {
                // remove element
                
                // if we remove the head then we have to set head:=head.next
                if (head == pos)
                    head = head.next;
                // if we remove tail then we have to set tail:=previous of tail
                if (tail == pos)
                    tail = prev;
                // if either head or tail is null then the queue is empty and we have to
                // set both to null
                if (head == null ^ tail == null)
                    head = tail = null;
                
                // if the removed element (pos) has a previous element
                // than we have to link the previous element with the next element
                // to be sure that pos is not inside the list anymore
                if (prev != null)
                    prev.next = pos.next;

                // now we can cache the unused element container
                // first disconnect it
                pos.next = null;
                // then add it to the cache (e.data will be automatically set to null)
                cache(pos);

                // update modification counter
                modcount++;
                
                // the element has been removed
                return true;
            }
            
            // we have not found it yet
            // ensure that prev is previous element of pos
            prev = pos;
            pos  = prev.next;
        }
        
        // the element could not be found, thus it was not removed
        return false;
    }

    /**
     * Returns <tt>true</tt> if this collection contains all of the elements
     * in the specified collection.
     *
     * @param  c collection to be checked for containment in this collection.
     * @return <tt>true</tt> if this collection contains all of the elements
     *         in the specified collection
     * @throws NullPointerException if the specified collection is <tt>null</tt>.
     * @see    #contains(Object)
     */
    public boolean containsAll( Collection<?> c )
    {
        if (c == this || c.isEmpty())
            return true;

        for (Object o : c)
        {
            // we do not support null-elements
            if (o == null)
                throw new NullPointerException();
            
            if (!contains(o))
                return false;
        }
        return true;
    }

    /**
     * Offers each element of the specified queue to this queue. The specified
     * queue does not contain any elements after this operation.
     * 
     * To balance the transaction of elements, this queue will provide
     * all cached (unused) elements to the specified queue.
     * 
     * Note: The operation has complexity O(1) and not O(n).
     * 
     * @param queue the queue to offer to this queue
     * @return true if this collection changed as result of the
     *  operation. 
     * @throws NullPointerException if the specified queue is null
     */
    public boolean offerAll( QueueBuffer<E> queue )
    {
        // first see if the collection will be changed
        if (queue.isEmpty())
            return false;

        // first see if this collection is empty
        if (tail == null)
        {
            // this collection is empty and the head of
            // the specified queue will become the head of this queue
            head = queue.head;
        }
        else
        {
            // this collection is not empty, we add
            // the specified queue to the tail
            tail.next = queue.head;
        }
        // the tail of the specified queue will become the tail of this queue
        tail = queue.tail;
        // the queue has no element after this operation
        queue.head = queue.tail = null;
        
        // now we give the specified queue our cached elements as exchange
        if (cacheHead != null)
        {
            // see if the specified queue has no cached elements  
            if (queue.cacheTail == null)
            {
                // no it hasn't, thus we can copy them 1:1
                queue.cacheHead = cacheHead;
                queue.cacheTail = cacheTail;
            }
            else
            {
                // the specified queue has already elements in it's cache
                queue.cacheTail.next = cacheHead;
                queue.cacheTail = cacheTail;
            }
            // this cache is empty as result of this operation
            cacheHead = cacheTail = null;
        }
        
        // update the modification counters of both queues
        queue.modcount ++;
        modcount ++;
     
        // the collection has changed as result of this operation
        return true;
    }
    
    /**
     * The operation is not supported.
     * @throws UnsupportedOperationException 
     */
    public boolean addAll( Collection<? extends E> c )
    {
        throw new UnsupportedOperationException();
    }

    /**
     * The operation is not supported.
     * @throws UnsupportedOperationException 
     */
    public boolean removeAll( Collection<?> c )
    {
        throw new UnsupportedOperationException();
    }

    /**
     * The operation is not supported.
     * @throws UnsupportedOperationException 
     */
    public boolean retainAll( Collection<?> c )
    {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Clears the cache of unused elements.
     */
    public void clearCache()
    {
        cacheHead = cacheTail = null;
    }

    /**
     * Removes all of the elements from this collection.
     * This collection will be empty after this method.
     * 
     * If the collection was not empty then the element
     * containeres will be recycled.
     */
    public void clear()
    {
        if (isEmpty())
            return;

        // we do not set head.next to null here
        // so that each element (head,...,tail)
        // will be added to the cache
        cache(head);
        head = tail = null;
        modcount++;
    }
    
    /**
     * Adds the specified element and each of it's
     * successors to the cache. Set e.next=null if
     * only e should be added to the cache.
     * 
     * The operation will set the Element.data field
     * of e and it's successors to null.
     * 
     * @param e the element(s) that should be recycled
     */
    private final void cache(Element<E> e)
    {   
        // set Element.data fields to null
        Element<E> newTail = e;
        while (newTail.next!=null)
        {
            newTail.data = null;
            newTail = newTail.next;
        } 
        newTail.data = null;
        
        // see if the cache is empty or not
        if (cacheTail != null) 
            cacheTail.next = e;
        else
            cacheHead = e;
        cacheTail = newTail;
    }

}
