/*
    Protocol Definition Language
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

/*
 * Created on Dec 18, 2006
 */
package net.sf.nmedit.jpdl;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit tests for {@link net.sf.nmedit.jpdl.BitStream}
 * 
 * @author Christian Schneider
 */
public class BitStreamTest
{

    @Test
    public void testZeroBitCase()
    {
        BitStream bs;
        
        // must not throw an exception
        
        bs = new BitStream();
        bs.getInt(0);
        bs.append(0, 0);
        
        bs = BitStream.wrap(new byte[10]);
        bs.setPosition(bs.getSize());
        bs.getInt(0);
        bs.append(0, 0);
    }
    
    /**
     * Test for {@link BitStream#unsignedByte(byte)}.
     */
    @Test
    public void testUnsignedByte()
    {
        // for each byte 
        for (byte b=Byte.MIN_VALUE;;b++)
        {
            // byte is in two's complement, we calculate
            // the expected return value of the tested function
            int expected = b>=0 ? (int) b : 256+(int)b;

            // test if result of function and expected value are equal
            assertTrue(BitStream.unsignedByte(b)==expected);
            
            // all possible values are tested
            if (b==Byte.MAX_VALUE)
                break;
        }
    }
    
    /**
     * Test for {@link BitStream#b2i(byte, byte, byte, byte)}.
     * Tests that <code>signum(b2i(b3,0,0,0))==signum(b3)</code> is always <code>true</code>.
     */
    @Test
    public void testB2iTestSignumCondition()
    {
        final byte p = (byte) 0;
        for (byte b=Byte.MIN_VALUE;;b++)
        {
            assertTrue(Integer.signum(BitStream.b2i(b,p,p,p))==Integer.signum(b));
            if (b==Byte.MAX_VALUE)
                break;
        }
    }
    
    /**
     * Test for {@link BitStream#b2i(byte, byte, byte, byte)}.
     */
    @Test
    public void testB2i()
    {
        final byte p = (byte) 0;
        byte a = (byte) 0xFF;
        assertTrue(BitStream.b2i(p,p,p,p)==0);
        assertTrue(BitStream.b2i(a,a,a,a)==0xFFFFFFFF);
    }

    /**
     * Checks valid arguments {@link BitStream#ensureBitRange0to32(int)}
     */
    @Test
    public void testEnsureBitRange0to32()
    {
        for (int i=0;i<=32;i++)
            BitStream.ensureBitRange0to32(i);
    }
    
    /**
     * Checks invalid arguments {@link BitStream#ensureBitRange0to32(int)}
     */
    @Test(expected=IllegalArgumentException.class)
    public void testEnsureBitRange0to32Invalid1()
    {
        BitStream.ensureBitRange0to32(-1);
    }

    /**
     * Checks invalid arguments {@link BitStream#ensureBitRange0to32(int)}
     */
    @Test(expected=IllegalArgumentException.class)
    public void testEnsureBitRange0to32Invalid2()
    {
        BitStream.ensureBitRange0to32(32+1);
    }
    
    /**
     * Tests {@link BitStream#indexof(int)} and {@link BitStream#bitsof(int)}.
     * 
     * Assures that the for positive or zero values the condition
     * <code>p == (BitStream.indexof(p)*32)+BitStream.bitsof(p)</code>
     * is <code>true</code>
     */
    @Test
    public void testIndexOfBitsOfTest()
    {
        for (int p=0;p<=100;p++)
        {
            assertTrue(p == (BitStream.indexof(p)*32)+BitStream.bitsof(p));
        }
    }
    
    /**
     * Tests {@link BitStream#bitsof(int)} 
     * Tests that for values p in [0..32-1] and k in [0..*]
     * the condition BitStream.bitsof((32*k)+p)==p is true
     */
    @Test
    public void testBitsOf()
    {        
        for (int k=0;k<100;k++)
            for (int p=0;p<32;p++)
                assertTrue(BitStream.bitsof((32*k)+p)==p);
    }
    
    /**
     * Tests {@link BitStream#indexof(int)}.
     * Tests that for values p in [0..32-1] and k in [0..*]
     * the condition BitStream.indexof((32*k)+p)==k is true
     */
    @Test
    public void testIndexOf()
    {
        for (int k=0;k<100;k++)
            for (int p=0;p<32;p++)
                assertTrue(BitStream.indexof((32*k)+p)==k);
    }

    /**
     * Tests {@link BitStream#unsetbits(int, int)}
     */
    @Test
    public void unsetBitsTest()
    {
        final int allbits = 0xFFFFFFFF;
        
        assertTrue("bitcount: 0",BitStream.unsetbits(allbits,0) == 0);
        
        for (int bitcount=1;bitcount<=32;bitcount++)
        {
            assertTrue("bitcount: "+bitcount,BitStream.unsetbits(allbits,bitcount) == ((allbits<<(32-bitcount))>>>(32-bitcount)));
        }
    }
    
    /**
     * Test for {@link BitStream#BitStream()}
     */
    @Test
    public void constructorTest1()
    {
        new BitStream();
    }
    
    /**
     * Test for {@link BitStream#BitStream(int)}
     */
    @Test
    public void constructorTest2()
    {
        new BitStream(0);
        new BitStream(1);
    }
    
    /**
     * Test for {@link BitStream#BitStream(int)}.
     * Tests invalid negative arguments.
     */
    @Test(expected=NegativeArraySizeException.class)
    public void constructorTest3()
    {
        new BitStream(-1);
    }
    
    /**
     * Tests {@link BitStream#getSize()}
     */
    @Test
    public void getSizeTest()
    {
        BitStream bs = new BitStream();
        // empty bitstream 
        assertTrue(bs.getSize()==0);
        // size after appending
        bs.append(0,0); 
        for (int i=0;i<32;i++)
        {
            assertTrue(bs.getSize()==i);
            bs.append(0,1); 
        }
        assertTrue(bs.getSize()==32);

        // size after wrap(byte[])
        assertTrue(BitStream.wrap(new byte[10]).getSize()==10*8);
        // size after wrap(byte[10], 1, 9)
        assertTrue(BitStream.wrap(new byte[10], 1, 9).getSize()==(9-1)*8);
        // size after wrap(int[])
        assertTrue(BitStream.wrap(new int[10]).getSize()==10*32);
        // size after wrap(byte[10], 99)
        assertTrue(BitStream.wrap(new int[10], 99).getSize()==99);
        // size after clear 1
        bs = BitStream.wrap(new byte[10]);
        bs.clear();
        assertTrue(bs.getSize()==0);
        // size after clear 2
        bs = new BitStream();
        bs.clear();
        assertTrue(bs.getSize()==0);
        // size after setSize 1
        bs = BitStream.wrap(new byte[10]);
        bs.setSize(32);
        assertTrue(bs.getSize()==32);
        // size after setSize 2
        bs = new BitStream();
        bs.setSize(32);
        assertTrue(bs.getSize()==0);
    }
    
    /**
     * Tests reading and writing
     */
    @Test
    public void testReadWrite()
    {
        BitStream bs = new BitStream();
        bs.append((byte)0xFF);
        assertTrue(bs.getPosition()==0);
        assertTrue(bs.getSize()==8);
        assertTrue(bs.getInt(bs.getSize())==0xFF);
        assertTrue(bs.getPosition()==bs.getSize());
        
        // test getByte()
        bs = new BitStream();
        bs.append((byte)0xFF);
        assertTrue(bs.getPosition()==0);
        assertTrue(bs.getSize()==8);
        assertTrue(bs.getByte()==(byte)0xFF);
        assertTrue(bs.getPosition()==bs.getSize());
        
        final int pattern = 0xAaAaAa; 
        testRW(pattern);     // (1010)+ pattern
        testRW(pattern>>>1); // (0101)+ pattern
        
        // writing free number of bits
        bs = new BitStream();
        int totalbits = 0;
        for(int b=0;b<=32;b++)
        {
            bs.append(0xFfFfFfFf, b);
            totalbits+=b;
        }
        // check number of bits
        assertTrue(bs.getSize()==totalbits);
        // read all bits and check if they are all set
        while (bs.isAvailable(1))
            assertTrue(bs.getInt(1)==1);
        
        // test getInt(0)

        bs = new BitStream();
        assertTrue(bs.getInt(0)==0);
        bs = new BitStream();
        bs.append(0xFfFfFfFf);
        assertTrue(bs.getInt(0)==0);
    }
    
    /**
     * @see #testReadWrite()
     */
    private void testRW(int pattern)
    {
        BitStream bs = new BitStream();
        bs.append(pattern);
        bs.append(pattern);
        assertTrue(bs.getSize()==32*2);
        assertTrue(bs.getPosition()==0);
        assertTrue(bs.getInt()==pattern);
        assertTrue(bs.getPosition()==32);
        assertTrue(bs.getInt()==pattern);
        assertTrue(bs.getPosition()==bs.getSize());
    }
    
    /**
     * Tests invalid argument in {@link BitStream#getInt()}
     */
    @Test(expected=IllegalArgumentException.class)
    public void testGetIntFails1()
    {
        BitStream bs = new BitStream();
        bs.getInt(-1);
    }
    
    /**
     * Tests invalid argument in {@link BitStream#getInt()}
     */
    @Test(expected=IllegalArgumentException.class)
    public void testGetIntFails2()
    {
        BitStream bs = new BitStream();
        bs.getInt(33);
    }
    
    /**
     * Tests invalid argument in {@link BitStream#getInt()}
     */
    @Test(expected=IllegalArgumentException.class)
    public void testGetIntFails3()
    {
        BitStream bs = new BitStream();
        bs.append(0);
        bs.append(0); 
        assertTrue(bs.getSize()==64);
        bs.getInt(33);
    }
    
    /**
     * Tests {@link BitStream#clone()}
     */
    @Test
    public void cloneTest()
    {
        BitStream bs = new BitStream();
        bs.append(0xAaAaAa,32);
        bs.getInt(2);
        // assert conditions
        checkCloneConditions(bs);
        
        // create clone
        BitStream cl = bs.clone();
        checkCloneConditions(cl);
        
        // check again
        assertTrue(bs.getSize()==cl.getSize());
        assertTrue(bs.getPosition()==cl.getPosition());
        // data check
        int remaining = bs.getSize()-bs.getPosition();
        assertTrue(bs.getInt(remaining)==cl.getInt(remaining));
    }

    /**
     * @see #cloneTest()
     */
    private void checkCloneConditions( BitStream bs )
    {
        assertTrue(bs.getSize()==32);
        assertTrue(bs.getPosition()==2);
    }
    
    /**
     * Tests {@link BitStream#isAvailable(int)}
     */
    @Test
    public void availableTest()
    {
        for (int i=0;i<=32+1;i++)
        {
            BitStream bs = BitStream.wrap(new int[2], i);
            assertTrue(bs.isAvailable(i));
            assertFalse(bs.isAvailable(i+1));
        }
        
        {
            final int start = 1;
            for (int i=start;i<=10;i++)
            {
                BitStream bs = BitStream.wrap(new byte[10], start, i);
                assertTrue(bs.isAvailable((i-start)*8));
                assertFalse(bs.isAvailable(((i-start)*8)+1));
            }
        }
        
        
        {
            byte[] data = new byte[10];
            BitStream bs = BitStream.wrap(data);
            assertTrue(bs.getSize() == data.length*8);
            assertTrue(bs.isAvailable(bs.getSize()));
            assertTrue(bs.isAvailable(data.length*8));
            assertFalse(bs.isAvailable((data.length*8)+1));
        }
    }

    /**
     * Tests {@link BitStream#toIntArray()}
     */
    @Test
    public void toIntArrayTest()
    {
        // ensure only complete integers are written
        int[] tmp = new int[] {0xAaAaAa, 0xAaAaAa};
        for (int i=0;i<=tmp.length*32;i++)
        {
            BitStream bs = BitStream.wrap(tmp, i);
            assertTrue(bs.toIntArray().length == i/32);
        }
        
        // check data
        int[] cpy ;
        BitStream bs;
        
        bs = BitStream.wrap(tmp);
        cpy = bs.toIntArray();
        assertTrue(cpy.length == tmp.length);
        assertTrue(cpy[0]==tmp[0]);
        assertTrue(cpy[1]==tmp[1]);
        
        bs = BitStream.wrap(tmp, 32*2-1);
        cpy = bs.toIntArray();
        assertTrue(cpy.length == 1);
        assertTrue(cpy[0]==(tmp[0]&~1));
    }

    /**
     * Tests {@link BitStream#toByteArray()}
     */
    @Test
    public void toByteArrayTest()
    {
        // ensure only complete integers are written
        final byte pattern = (byte) 0xAa;
        byte[] tmp = new byte[] {pattern, pattern, pattern, pattern, pattern};
        // check data
        for (int start=0;start<5;start++)
        {
            for (int end=start;end<=5;end++)
            {
                BitStream bs = BitStream.wrap(tmp, start, end);
                byte[] cpy = bs.toByteArray();
                assertTrue(cpy.length == end-start);
                for (int i=0;i<cpy.length;i++)
                    assertTrue(cpy[i]==tmp[i]);
            }
        }
    }

}
