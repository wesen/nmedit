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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.NoSuchElementException;

import static junit.framework.Assert.*;

import net.sf.nmedit.jnmprotocol2.utils.QueueBuffer;

import org.junit.Test;

/**
 * JUnit tests for {@link net.sf.nmedit.jnmprotocol2.utils.QueueBuffer}
 * @author Christian Schneider
 */
public class QueueBufferTest
{

    /**
     * Tests creating a new QueueBuffer instance
     */
    @Test
    public void createQueueBufferTest()
    {
        QueueBuffer qb = new QueueBuffer<Integer>();
        
        // we do this only to avoid that the operation above
        // is removed due to optimizations.
        assertNotNull(qb);
    }

    /**
     * Tests if {@link QueueBuffer#addAll(Collection)}
     * throws an {@link UnsupportedOperationException}
     * as expected.
     */
    @Test(expected=UnsupportedOperationException.class)
    public void addAllUnsupported()
    {
        (new QueueBuffer<Integer>()).addAll(null);
    }

    /**
     * Tests if {@link QueueBuffer#removeAll(Collection)}
     * throws an {@link UnsupportedOperationException}
     * as expected.
     */
    @Test(expected=UnsupportedOperationException.class)
    public void removeAllUnsupported()
    {
        (new QueueBuffer<Integer>()).removeAll(null);
    }

    /**
     * Tests if {@link QueueBuffer#retainAll(Collection)}
     * throws an {@link UnsupportedOperationException}
     * as expected.
     */
    @Test(expected=UnsupportedOperationException.class)
    public void retainAllUnsupported()
    {
        (new QueueBuffer<Integer>()).retainAll(null);
    }

    /**
     * Tests if {@link QueueBuffer#offer(E)} returns
     * false if the argument is <code>null</code>.
     */
    @Test
    public void offerNullArgument()
    {
        assertFalse((new QueueBuffer<Integer>()).offer(null));
    }

    /**
     * Tests if {@link QueueBuffer#offer(E)} returns
     * true if the argument is valid.
     */
    @Test
    public void offerValidArgument()
    {
        assertTrue((new QueueBuffer<Integer>()).offer(0));
    }

    /**
     * Tests if {@link QueueBuffer#add(E)} 
     * throws an {@link NullPointerException}
     * like expected if the argument is <code>null</code>.
     */
    @Test(expected=NullPointerException.class)
    public void addNullArgument()
    {
        assertFalse((new QueueBuffer<Integer>()).add(null));
    }

    /**
     * Tests if {@link QueueBuffer#add(E)} 
     * returns true if the argument is valid.
     */
    @Test
    public void addValidArgument()
    {
        assertTrue((new QueueBuffer<Integer>()).add(0));
    }

    /**
     * Tests if {@link QueueBuffer#remove(Object)} 
     * throws an {@link NullPointerException}
     * like expected if the argument is <code>null</code>.
     */
    @Test(expected=NullPointerException.class)
    public void removeNullArgument()
    {
        (new QueueBuffer<Integer>()).remove(null);
    }

    /**
     * Tests if {@link QueueBuffer#remove(Object)} 
     * is true if an existing element is removed.
     */
    @Test
    public void removeExistentElement()
    {
        QueueBuffer<Integer> q = new QueueBuffer<Integer>();
        q.add(0);
        assertTrue(q.remove(0));
    }
    
    /**
     * Tests if {@link QueueBuffer#remove(Object)} 
     * is false if an element which is not in the collection
     * is removed.
     */
    @Test
    public void removeNotExistentElement()
    {
        QueueBuffer<Integer> q = new QueueBuffer<Integer>();
        q.add(0);
        assertFalse(q.remove(-1));
    }   

    /**
     * Tests if {@link QueueBuffer#contains(Object)} 
     * throws an {@link NullPointerException}
     * like expected if the argument is <code>null</code>.
     */
    @Test(expected=NullPointerException.class)
    public void containsNullArgument()
    {
        (new QueueBuffer<Integer>()).contains(null);
    }

    /**
     * Tests if {@link QueueBuffer#contains(Object)} 
     * is true if the argument exists in the collection.
     */
    @Test
    public void containsExistentElement()
    {
        QueueBuffer<Integer> q = new QueueBuffer<Integer>();
        q.add(0);
        assertTrue(q.contains(0));
    }
    
    /**
     * Tests if {@link QueueBuffer#contains(Object)} 
     * is false if the argument does not exists in the collection.
     */
    @Test
    public void containsNotExistentElement()
    {
        QueueBuffer<Integer> q = new QueueBuffer<Integer>();
        q.add(0);
        assertFalse(q.contains(-1));
    }   

    /**
     * Tests if {@link QueueBuffer#containsAll(Collection)} 
     * throws an {@link NullPointerException}
     * like expected if the argument is <code>null</code>.
     */
    @Test(expected=NullPointerException.class)
    public void containsAllNullArgument()
    {
        (new QueueBuffer<Integer>()).containsAll(null);
    }

    /**
     * Tests if {@link QueueBuffer#containsAll(Collection)} 
     * throws an {@link NullPointerException}
     * like expected if the argument contains elements
     * that are <code>null</code>.
     */
    @Test(expected=NullPointerException.class)
    public void containsAllCollectionHasNullElement()
    {
        (new QueueBuffer<Integer>()).containsAll(Arrays.<Object>asList(new Object[]{null}));
    }

    /**
     * Tests if {@link QueueBuffer#containsAll(Collection)} 
     * returns true like expected if the collection in the
     * argument is <code>empty</code>.
     */
    @Test
    public void containsAllInEmptyCollection()
    {
        assertTrue((new QueueBuffer<Integer>()).containsAll(Arrays.<Integer>asList(new Integer[0])));
    }

    /**
     * Tests if {@link QueueBuffer#containsAll(Collection)} 
     * returns false like expected if the collection in the
     * argument contains other elements than them in the queue.
     */
    @Test
    public void containsAll1()
    {
        assertFalse((new QueueBuffer<Integer>()).containsAll(Arrays.<Integer>asList(new Integer[]{0,1})));
    }

    /**
     * Tests if {@link QueueBuffer#containsAll(Collection)} 
     * returns true like expected if the collection in the
     * argument contains elements that are also elements of the queue.
     */
    @Test
    public void containsAll2()
    {
        final String[] elements = new String[]{"this", "is", "a", "test"};
        
        QueueBuffer<String> q = new QueueBuffer<String>();
        Collection<String> c = Arrays.<String>asList(elements);
        Collections.<String>addAll(q, elements);
        
        q.offer("element that is not part of c");
        
        assertTrue(q.containsAll(c));
    }

    /**
     * tests if {@link QueueBuffer#size()} is correct
     */
    @Test
    public void sizeTest()
    {
        QueueBuffer<Integer> q = new QueueBuffer<Integer>();
        
        int size = 0;
        
        do
        {
            assertEquals(q.size(), size);
            size++;
            q.offer(size);
        } 
        while (size<10);
        
        do
        {
            assertEquals(q.size(), size);
            size --;
            q.poll();
        }
        while (size>0);
        
        assertEquals(q.size(), size);
    }

    /**
     * tests if {@link QueueBuffer#isEmpty()} is correct
     */
    @Test
    public void isEmptyTest()
    {
        QueueBuffer<Integer> q = new QueueBuffer<Integer>();
        
        int size = 0;

        // we count how often the for loop bodies were executed
        // to be sure that the loops were executed
        int guarantee = 20;
        
        do
        {
            assertEquals(q.isEmpty(), size==0);
            size++;
            q.offer(size);
            guarantee--;
        } 
        while (size<10);
        
        do
        {
            assertEquals(q.isEmpty(), size==0);
            size --;
            q.poll();
            guarantee--;
        }
        while (size>0);

        assertEquals(q.isEmpty(), size==0);
        assertEquals("loops were not executed like expected", new Integer(guarantee), new Integer(0));
    }

    /**
     * tests if elements in the queue are removed in the same order
     * like they were added 
     */
    @Test
    public void testOfferPollOrder()
    {
        QueueBuffer<Integer> q = new QueueBuffer<Integer>();
        
        final int start = 1;
        final int end = 10;
        
        // we count how often the for loop bodies were executed
        // to be sure that the loops were executed
        int guarantee = 20;
        
        for (int i=start;i<=end;i++)
        {
            q.offer(i);
            guarantee --;
        }
        for (int i=start;i<=end;i++)
        {
            assertEquals(q.poll(), new Integer(i));
            guarantee --;
        }
        assertEquals("loops were not executed like expected", new Integer(guarantee), new Integer(0));
    }

    /**
     * Tests if {@link QueueBuffer#offerAll(QueueBuffer)} adds all
     * elements in the correct order and if the queue specified as 
     * argument is empty as result of the operation.
     */
    @Test
    public void testOfferAllElementTransfer()
    {
        QueueBuffer<Integer> a = new QueueBuffer<Integer>();
        QueueBuffer<Integer> b = new QueueBuffer<Integer>();

        Collections.<Integer>addAll(a, new Integer[]{0, 1, 2, 3, 4});
        Collections.<Integer>addAll(b, new Integer[]{5, 6, 7, 8, 9});
        
        a.offerAll(b);

        assertTrue("queue b is not empty", b.isEmpty());
        assertFalse("queue a is empty", a.isEmpty());
        assertEquals("queue a has not the correct number of elements (10): "+a.size(), a.size(), 10);

        // we count how often the for loop bodie was executed
        // to be sure that the loop was executed
        int guarantee = 10;
        
        for (int i=0;i<=9;i++)
        {
            assertEquals("elements are not in correct order", a.poll(), new Integer(i));
            guarantee--;
        }
        assertEquals("loop was not executed like expected", new Integer(guarantee), new Integer(0));
    }
    
    /**
     * Tests if clear works like expected
     */
    @Test
    public void clearTest()
    {
        QueueBuffer<Integer> q = new QueueBuffer<Integer>();
        Collections.<Integer>addAll(q, new Integer[]{0, 1, 2, 3, 4});
        q.clear();
        assertTrue(q.isEmpty());
        assertTrue(q.size()==0);
        assertEquals(q.peek(), null);
        assertEquals(q.poll(), null);
    }

    /**
     * Tests if {@link QueueBuffer#remove()} 
     * throws an {@link NoSuchElementException}
     * like expected if the queue is empty.
     */
    @Test(expected=NoSuchElementException.class)
    public void removeFromEmptyQueue()
    {
        (new QueueBuffer<Integer>()).remove();
    }

    /**
     * Tests if {@link QueueBuffer#remove()} 
     * returns the correct element.
     */
    @Test
    public void removeFromQueue()
    {
        QueueBuffer<Integer> q = new QueueBuffer<Integer>();
        Integer element = 0;
        q.offer(element);
        q.offer(element+1);
        assertEquals(q.remove(), element);
    }

    /**
     * Tests if {@link QueueBuffer#element()} 
     * throws an {@link NoSuchElementException}
     * like expected if the queue is empty.
     */
    @Test(expected=NoSuchElementException.class)
    public void elementFromEmptyQueue()
    {
        (new QueueBuffer<Integer>()).element();
    }

    /**
     * Tests if {@link QueueBuffer#element()} 
     * returns the correct element.
     */
    @Test
    public void elementFromQueue()
    {
        QueueBuffer<Integer> q = new QueueBuffer<Integer>();
        Integer element = 0;
        q.offer(element);
        q.offer(element+1);
        assertEquals(q.element(), element);
    }

}
