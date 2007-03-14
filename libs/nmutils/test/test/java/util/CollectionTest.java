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

import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import test.java.lang.ObjectTest;

/**
 * Tests {@link java.util.Collection}
 * @author Christian Schneider
 */
public abstract class CollectionTest<E> extends ObjectTest
{
    
    /**
     * Returns an empty instance of the tested collection.
     * Two calls must return two objects <code>a</code> and <code>b</code>
     * where <code>a!=b</code> is true.
     * 
     * @return empty instance of the tested collection
     */
    public abstract Collection<E> createEmptyCollection();
    
    /**
     * Creates an element that is not in the specified collection.
     * These elements are added to the collection. 
     * 
     * @param c the collection
     * @return a unique element
     * @throws NoSuchElementException limit of elements that can be added to the collection is reached
     */
    public abstract E createUniqueElement(Collection<E> c)
        throws NoSuchElementException;
    
    /**
     * Returns the maximum number of elements that can be created and added
     * to a collection for testing purposes. Returns <code>0</code>
     * when the number of elements is not limited.
     */
    public abstract int getElementLimit();

    /**
     * Returns the maximum number of elements that are created 
     * and added to a single list. The return value is in the range
     * [2..10].
     */
    public int getLimit()
    {
        int l = getElementLimit();
        return (l==0) ? 10 : Math.max(2, Math.min(10, l));
    }

    /**
     * returns an instance of the tested collection that contains different elements 
     * 
     * @return
     * @see #createEmptyCollection()
     * @see #createUniqueElement(Collection)
     * @see #getLimit()
     */
    public Collection<E> createCollection()
    {
        Collection<E> c = createEmptyCollection();
        for (int i=getLimit()-1;i>=0;i--)
            c.add(createUniqueElement(c));
        return c;
    }

    /**
     * returns a collection of objects that were not created before
     * and that are all different and for two elements a and b a.equals(b) is false 
     * 
     * @return
     * @see #createEmptyCollection()
     * @see #createUniqueElement(Collection)
     * @see #getLimit()
     */
    public Collection<Object> createUniqueCollection()
    {
        int size = getLimit();
        ArrayList<Object> list = new ArrayList<Object>(size);
        for (int i=getLimit()-1;i>=0;i--)
            list.add(new Object());
        return list;
    }
    
    /**
     * Tests {@link #createUniqueCollection()}
     */
    @Test
    public void createUniqueCollectionTest()
    {
        Collection<?> c = createUniqueCollection();
        Assert.assertTrue(c.size()>0);
        Object[] items = c.toArray();
        
        for (int i=0;i<items.length-1;i++)
        {
            Object a = items[i];
            for (int j=i+1;j<items.length;j++)
            {
                Object b = items[j];
                Assert.assertTrue(a!=b);
                Assert.assertTrue(!a.equals(b));
            }
        }
    }
    
    /**
     * Returns true when the collection supports adding equal elements twice
     */
    public abstract boolean supportsMultipleAdds();

    /**
     * Tests whether {@link #createCollection()} contains
     * at least two elements.
     */
    @Test
    public void testCreateCollection()
    {
        Assert.assertTrue(createCollection().size()>=2);
    }
    
    /**
     * Tests whether {@link #createEmptyCollection()} creates an
     * empty collection.
     */
    @Test
    public void testCreateEmptyCollection()
    {
        Collection<E> c = createEmptyCollection();
        Assert.assertTrue(c.size()==0);
    }
    
    /**
     * Tests whether {@link #getElementLimit()} returns a value
     * &gt;=0
     */
    @Test
    public void testGetElementLimit()
    {
        Assert.assertTrue(getElementLimit()>=0);
    }
    
    /**
     * Tests {@link #createUniqueElement(Collection)}.
     */
    @Test
    public void testCreateUniqueElement()
    {
        Collection<E> c = createEmptyCollection();
        for (int i=0;i<getLimit();i++)
        {
            E element = createUniqueElement(c);
            Assert.assertTrue(element!=null);
            Assert.assertTrue(!c.contains(element));
            c.add(element);
        }
    }
    
    /**
     * Tests {@link Collection#size()} and {@link Collection#isEmpty()}
     */
    @Test
    public void testSizeAndIsEmpty()
    {
        Collection<E> c = createEmptyCollection();
        int expectedSize = 0;
        for (;expectedSize<getLimit();expectedSize++)
        {
            Assert.assertTrue(expectedSize==c.size());
            Assert.assertTrue(c.isEmpty()==(expectedSize==0));
            c.add(createUniqueElement(c));
        }  
        Assert.assertTrue(expectedSize==c.size());
        Assert.assertTrue(c.isEmpty()==(expectedSize==0));
        
        while (c.size()>0)
        {
            E element = c.iterator().next();
            c.remove(element);
            expectedSize --;
            Assert.assertTrue(expectedSize==c.size());
            Assert.assertTrue(c.isEmpty()==(expectedSize==0));
        }
    }

    /**
     * When {@link #supportsMultipleAdds()} is true, tests
     * whether two elements a, b where a.equals(b)==true
     * can be added, otherwise tests if adding a and b fails.
     * 
     * Note: this test is not necessary when the {@link Object#equals(java.lang.Object)} method
     * is implemented correctly.
     */
    @Test
    public void equalElementTest()
    {
        if (supportsMultipleAdds())
        {
            Collection<E> c = createEmptyCollection();
            E e = createUniqueElement(c);
            Assert.assertTrue(c.add(e));
            Assert.assertTrue("adding twice the same element should be possible", 
                    c.add(e));
            Assert.assertTrue(c.size()==2);
        }
        else
        {
            Collection<E> c = createEmptyCollection();
            E e = createUniqueElement(c);
            Assert.assertTrue(c.add(e));
            Assert.assertTrue("adding twice the same element should not be possible", 
                    !c.add(e));
            Assert.assertTrue(c.size()==1);
        }
    }

    /**
     * Creates one empty collection and one with multiple elements added. 
     */
    @Override
    public Object[] createEqualsTestInstances()
    {
        return new Object[] {
                createEmptyCollection(),
                createCollection()
        };
    }

    /**
     * Tests {@link Collection#contains(java.lang.Object)}
     * TODO test exceptions
     */
    @Test
    public void testContains()
    {
        Collection<E> c = createCollection();
        c.remove(c.iterator().next()); // remove one element see getLimit()
        
        E unique = createUniqueElement(c);

        Assert.assertTrue (!c.contains(unique));
        for (E e : c)
            Assert.assertTrue(c.contains(e));
    }

    /**
     * Tests {@link Collection#containsAll(java.util.Collection)} 
     * TODO test exceptions
     */
    @Test
    public void testContainsAll()
    {
        Collection<E> c = createCollection();
        Assert.assertTrue (c.containsAll(c));
        Assert.assertTrue (c.containsAll(createEmptyCollection()));
        
        E unique = c.iterator().next();
        Assert.assertTrue(c.remove(unique)); // remove one element see getLimit()
        
        Collection<E> c2 = createEmptyCollection();
        c2.add(unique);        
        Assert.assertTrue(!c.containsAll(c2));

        // TODO requires c initially contains at least 2 elements        
        c2.add(c.iterator().next());        
        Assert.assertTrue(!c.containsAll(c2));
    }

    /**
     * Tests {@link Collection#toArray()} 
     */
    @Test
    public void testToArray()
    {
        Assert.assertTrue(createEmptyCollection().toArray().length==0);
        
        Collection<E> c = createCollection();
        Object[] array = c.toArray();

        Assert.assertTrue(c.size()==array.length);
        
        for (Object e : array)
        {
            Assert.assertTrue(c.contains(e));
        }
    }

    /**
     * Tests {@link Collection#toArray(T[])} 
     * TODO implement test
     */
    @Ignore("test not implemented: Collection.toArray(T[])")
    @Test
    public void testToArray2()
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Tests {@link Collection#add(E)} 
     * TODO implement test
     */
    @Ignore("test not implemented: Collection.add(E)")
    @Test
    public void testAdd()
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Tests {@link Collection#remove(java.lang.Object)} 
     * TODO implement test
     */
    @Ignore("test not implemented: Collection.remove(java.lang.Object)")
    @Test
    public void testRemove()
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Tests {@link Collection#addAll(java.util.Collection)} 
     * TODO implement test
     */
    @Ignore("test not implemented: Collection.addAll(java.util.Collection)")
    @Test
    public void testAddAll()
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Tests {@link Collection#removeAll(java.util.Collection)} 
     * TODO test exceptions
     */
    @Test
    public void testRemoveAll()
    {
        Collection<E> c = createCollection();
        
        Collection<E> copy = new ArrayList<E>();
        for (E e : c)
            copy.add(e);

        Assert.assertTrue (c.removeAll(copy));
        Assert.assertTrue (c.size()==0);
        
        c = createCollection();
        
        copy.clear();
        for (E e : c)
            copy.add(e);
        // remove one element
        copy.remove(copy.iterator().next());


        Assert.assertTrue (c.removeAll(copy));
        Assert.assertTrue (c.size()==1); // one element left
        
        Assert.assertTrue(!c.removeAll(createUniqueCollection()));
    }

    /**
     * Tests {@link Collection#retainAll(java.util.Collection)} 
     * TODO test exceptions
     */
    @Test
    public void testRetainAll()
    {
        Collection<E> c = createCollection();
        
        int sizeBefore = c.size();
        c.retainAll(c);
        Assert.assertTrue(c.size()==sizeBefore);
        
        c.retainAll(createEmptyCollection());
        Assert.assertTrue(c.size()==0);
    }

    /**
     * Tests {@link Collection#clear()} 
     */
    @Test
    public void testClear()
    {
        Collection<E> c;
        
        c = createEmptyCollection();
        c.clear();
        Assert.assertTrue(c.isEmpty());
        
        c = createCollection();
        c.clear();
        Assert.assertTrue(c.isEmpty());
    }

}
