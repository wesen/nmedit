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
import org.junit.Test;

public class PBasicSignalTypeTest
{
    int id = 345;
    String name = "test.name";
    Color color = new Color(1,2,3);
    PSignal st = new PSignal(null, id, name, color);

    @Test
    public void createTest()
    {
        Assert.assertNull(st.getDefinedSignals());
        Assert.assertEquals(color, st.getColor());
        Assert.assertEquals(id, st.getId());
        Assert.assertEquals(name, st.getName());
    }
    
    @Test
    public void equalsTest()
    {
        Assert.assertTrue(st.equals(st));
        PSignal st2 = new PSignal(null, id, name, color);
        Assert.assertTrue(st.equals(st2));
        Assert.assertFalse(st.equals(new PSignal(null, id+1, name, color)));
        Assert.assertFalse(st.equals(new PSignal(null, id, name+" ", color)));
        Assert.assertFalse(st.equals(new PSignal(null, id, name, Color.RED)));
    }

    @Test
    public void testSerialization() throws Exception
    {
        SerializationTestHelper helper = new SerializationTestHelper();
        Assert.assertTrue(helper.testSerialization(st));
    }
    
}
