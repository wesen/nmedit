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
package test.java.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import junit.framework.Assert;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

public abstract class AbstractIteratorTest<E>
{

    public abstract Collection<E> createCollection(boolean empty);
    
    /**
     * Creates the iterator that should be tested
     * @param empty <code>true</code> when the iterator should contain no elements or <code>false</code> otherwise.
     */
    public final Iterator<E> createIterator(boolean empty)
    {
        return createCollection(empty).iterator();
    }

    /**
     * Returns <code>true</code> when the {@link Iterator#remove()} function should be tested.
     */
    public abstract boolean doTestRemove();

    @Test
    public void createIteratorTest()
    {
        assertTrue(!createIterator(true).hasNext());
        assertTrue(createIterator(false).hasNext());
    }
    
    @Test(expected=NoSuchElementException.class)
    public void nextFailsTest()
    {
        Iterator i = createIterator(false);
        while (i.hasNext()) i.next();
        i.next();
    }

    @Test(expected=NoSuchElementException.class)
    public void nextFailsTestEmpty()
    {
        Iterator i = createIterator(true);
        i.next();
    }

    @Test(expected=IllegalStateException.class)
    public void removeFailsTest1()
    {
        createIterator(true).remove();
    }

    @Test
    public void removeNotImplementedTest() 
    {
        if (doTestRemove())
            return ;
        
        Iterator<E> i=createIterator(false);
        i.next();
        boolean exceptionThrown = false;
        try
        {
            i.remove();    
        }
        catch (UnsupportedOperationException e)
        {
            exceptionThrown = true;
        }
        
        assertTrue(exceptionThrown);
    }

    @Test(expected=IllegalStateException.class)
    public void removeFailsTest2() 
    {
        if (!doTestRemove())
            return ;
        
        Iterator<E> i=createIterator(false);
        while (i.hasNext())
        {
            i.next();
            i.remove();
        }
        i.remove();
    }

    @Test
    public void removeWorksTest() 
    {
        if (!doTestRemove())
            return ;
        
        Iterator<E> i=createIterator(false);
        while (i.hasNext())
        {
            i.next();
            i.remove();
        }
    }

    @Test
    public void removeTest2()
    {
        if (!doTestRemove())
            return ;
        
        Collection<E> c = createCollection(false);
        Iterator<E> i = c.iterator();
        
        final int initialSize = c.size();
        int removed = 0;

        while (i.hasNext())
        {
            E e = i.next();
            Assert.assertTrue("iteration contains element which is not part of the collection", c.contains(e));
            i.remove();
            removed ++;
            Assert.assertTrue("element <"+e+"> still in collection", !c.contains(e));
        }

        Assert.assertTrue("iterator should have removed all elements", removed == initialSize);
        Assert.assertTrue("collection should be empty", c.size()==0);
    }
    
}
