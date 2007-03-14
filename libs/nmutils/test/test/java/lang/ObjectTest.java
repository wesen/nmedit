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
package test.java.lang;

import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test methods of {@link java.lang.Object}.
 * 
 * @author Christian Schneider
 */
public abstract class ObjectTest
{
    
    /**
     * Tests the {@link Object#equals(java.lang.Object)} method
     * and the {@link Object#hashCode()} contract for equal objects.
     */
    @Test
    public void testEquals()
    {
        Object[] testA = createEqualsTestInstances();
        Object[] testB = createEqualsTestInstances();

        // check wether the test should be done or not
        if (testA==null && testB==null) return ;
        
        final String MSG_INCOMPATIBLE = "incompatible test"; 

        if (testA==null || testB==null) Assert.assertTrue(MSG_INCOMPATIBLE, false);
        Assert.assertTrue(MSG_INCOMPATIBLE, testA.length == testB.length);
        
        int size = testA.length;
        for (int i=0;i<size;i++)
        {
            Assert.assertTrue("equals() failed", testA[i].equals(testB[i]));
            Assert.assertTrue("hashCode() not equal", testA[i].hashCode()==testB[i].hashCode());
        }
    }

    /**
     * Tests {@link Object#clone()} when this object implements
     * {@link Cloneable} and no {@link CloneNotSupportedException} is
     * thrown
     */
    @Test
    public void testClone()
    {
        Object[] test = createEqualsTestInstances();
        
        try
        {
            for (Object a : test)
            {
                Object b = createClone(a);
                Assert.assertEquals("equals() failed", a, b);
                Assert.assertTrue("hashCode() not equal", a.hashCode()==b.hashCode());
            }
        }
        catch (CloneNotSupportedException e)
        {
            // do not test since cloning is not supported
            return ;
        }
    }

    /**
     * Creates an array of instances of the tested object
     * that are used to test the {@link Object#equals(java.lang.Object)} method and
     * {@link Object#clone()} method (if the objects support cloning)
     * or <code>null</code> when the equals method should not be tested.
     * 
     * Two different calls must return arrays <code>a</code> and <code>b</code>
     * of the same size and for each integer 0&lt;=i%lt;size must a[i].equals(b[i]) return true. 
     */
    public abstract Object[] createEqualsTestInstances();

    /**
     * Creates a clone of the specified object using {@link Object#clone()}.
     * 
     * @param o
     * @return the cloned object or <code>null</code> when cloning the object failed.
     * @throws CloneNotSupportedException when the object does not support cloning
     */
    public static Object createClone(Object o) throws CloneNotSupportedException
    {
        // check if this object implements the Cloneable interface
        if (!(o instanceof Cloneable))
            throw new CloneNotSupportedException();
        
        Method cloneMethod;
        try
        {
            cloneMethod = o.getClass().getMethod("clone", new Class[0]);
            return cloneMethod.invoke(o, new Object[0]);
        }
        catch (Exception e)
        {
            if (e instanceof CloneNotSupportedException)
                throw (CloneNotSupportedException) e;
            return null;
        }
    }

}
