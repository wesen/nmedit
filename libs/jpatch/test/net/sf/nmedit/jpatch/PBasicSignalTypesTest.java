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
package net.sf.nmedit.jpatch;

import java.awt.Color;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PBasicSignalTypesTest
{
    PSignalTypes st;

    @Before
    public void initSignalTypes()
    {
        st = createSignalTypes();
    }
    
    public PSignalTypes createSignalTypes()
    {
        PSignalTypes bst = new PSignalTypes("signals");
        for (int i=0;i<10;i++)
            bst.create(i, ""+i, new Color(i,i,i), i==1);
        return bst;
    }
    
    @Test
    public void equalsTest()
    {
        Assert.assertEquals(st, createSignalTypes());
    }

    @Test
    public void testSerialization() throws Exception
    {
        SerializationTestHelper helper = new SerializationTestHelper();
        Assert.assertTrue(helper.testSerialization(st));
    }
    
}
