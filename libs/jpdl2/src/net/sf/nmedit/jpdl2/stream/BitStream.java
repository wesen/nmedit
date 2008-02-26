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
 * Created on Dec 17, 2006
 */
package net.sf.nmedit.jpdl2.stream;

import java.util.Arrays;


/**
 * A stream for bitwise writing and reading.
 * 
 * <h1>History</h1>
 * <h3>2008-02-20</h3>
 * <ul>
 *   <li>implements PDLDataSource</li>
 * </ul>
 * <h3>2007-01-02</h3>
 * <ul>
 *   <li>getInt(0) and append(data, 0) cases are now working</li>
 * </ul>
 * <h3>2006-12-18</h3>
 * <ul>
 *   <li>simpler and more readable getInt()/append() implementation</li>
 *   <li>added wrap() methods to wrap existing arrays</li>
 *   <li>added toIntArray()/toByteArray() methods to extract the internal data quickly</li>
 *   <li>added JUnit tests see test/net.sf.nmedit.jpdl.BitStreamTest</li>
 * </ul>
 * <h3>2006-06-14</h3>
 * <ul>
 *   <li>getInt(0) now returns 0 as expected</li>
 *   <li>getInt(X) now fails when X is larger than number of available bits or negative</li>
 * </ul>
 * 
 * @author Christian Schneider
 */
public class BitStream implements Cloneable, PDLDataSource
{
    // empty byte[] array
    static final byte[] EMPTY_BYTES = new byte[0];
    // empty int[] array
    static final int[] EMPTY_INTS = new int[0];
    // write position/number of written bits
    private int size;
    // read position
    private int position;
    // array containing the bits 
    private int[] data;

    // ****** constructors ******
 
    /**
     * Creates a new bitstream for bitwise writing and reading.
     * The bitstream has an initial capacity of 32*initialCapacity bits.
     * @param initialCapacity initial capacity (32*initialCapacity bits)
     * @throws NegativeArraySizeException if the initialCapacity is negative 
     */
    public BitStream(int initialCapacity)
    {
        this(new int[initialCapacity], 0);
    }

    /**
     * Creates a new bitstream for bitwise writing and reading.
     */
    public BitStream()
    {
        this(10);
    }
    
    /**
     * Creates a new bitstream with the specfied initial data.
     * 
     * @param data initial data of the bitstream
     * @param bitcount number of valid bits - remaining bits are undefined
     * @throws IllegalArgumentException if bitcount>data.length*32
     */
    private BitStream(int[] data, int bitcount)
    {
        if (bitcount > data.length*32)
            throw new IllegalArgumentException("invalid number of bits: "+bitcount);
        
        this.data = data;
        this.size = bitcount;
        this.position = 0;
    }

    // ****** wrapping functions ******
    
    /**
     * Creates a bitstream with the specified initial data.
     * 
     * @param data initial data of the bitstream
     * @param bitcount number of valid bits - remaining bits are undefined
     * @throws IllegalArgumentException if bitcount>data.length*32
     */
    public final static BitStream wrap(int[] data, int bitcount)
    {
        return new BitStream(data, bitcount);
    }

    /**
     * Creates a bitstream with the specified initial data.
     * @param data initial data of the bitstream
     */
    public final static BitStream wrap(int[] data)
    {
        return new BitStream(data, data.length * 32);
    }

    /**
     * Creates a bitstream with the specified initial data.
     * @param data initial data of the bitstream
     */
    public final static BitStream wrap(byte[] data)
    {
        return BitStream.wrap(data, 0, data.length); 
    }

    /**
     * Creates a bitstream with the specified initial data.
     * As initial the complete bytes data[start..end-1] are used.
     * 
     * @param data initial data of the bitstream
     * @param start start index
     * @param end end index (exklusive)
     * @throws IndexOutOfBoundsException if start<0 or end<0 or start>end or end>data.length
     */
    public final static BitStream wrap(byte[] data, int start, int end)
    {
        if ((start < 0) || (end < 0) || (start > end) || (end > data.length))
            throw new IndexOutOfBoundsException ( "start " + start 
                    + ", end " + end + ", data.length " + data.length);
        
        final int bytes = end-start;
        final int[] idata = new int[(bytes+3)/4];
        
        int i=0;
        final int iend = idata.length-1;
        for (;i<iend;i++)
        {
            // byte index
            int b = start+(i<<2);
            idata[i] = b2i(data[b], data[b+1], data[b+2], data[b+3]);
        }
        if (i<idata.length)
        {
            int b = start+(i<<2);
            idata[i] = b2i(data[b],
                  b+1<end?data[b+1]:0,
                  b+2<end?data[b+2]:0,
                  b+3<end?data[b+3]:0);
        }
        return new BitStream(idata, bytes*8);
    }
    
    public static BitStream copyOf(BitStream src)
    {
        BitStream copy = new BitStream(src.data.length);
        System.arraycopy(src.data, 0, copy.data, 0, src.data.length);
        copy.position = src.position;
        copy.size = src.size;
        return copy;
    }

    // ****** read / write functions ******
    
    /**
     * Reads the specified number of bits and returns them.
     * 
     * @param bitcount number of bits to read [0..32]
     * @throws IllegalArgumentException if bitcount not in [0..32] or the specified number of bits is not available
     */
    public final int getInt(int bitcount)
    {
        if (bitcount == 0) return 0;
        // ensure bits in range [0..32]
        ensureBitRange0to32(bitcount);
        // ensure bits available
        if (!isAvailable(bitcount))
            throw new IllegalArgumentException("specified number of bits not available: "+bitcount);
        // get array index
        int idx = indexof(position);
        // calculate available bits in current field
        int available = 32-bitsof(position);
        // number of bits of right shift operation
        int rshift = available-bitcount;
        // get raw value
        int value = data[idx];
        if (rshift>=0)
        {
            value >>>= rshift;
        }
        else
        {
            value = (value<<-rshift)|(data[idx+1]>>>(32+rshift)); 
        }
        // move pointer
        position+=bitcount;
        // unset bits that do not belong to the value
        return unsetbits(value, bitcount);
    }
    
    /**
     * Reads the next 32-bit integer from this bitstream.
     * Using this method is equal to using <code>getInt(32)</code>.
     * @return the next integer
     */
    public final int getInt()
    {
        return getInt(32);
    }
    
    /**
     * Reads the next 8-bit byte from this bitstream.
     * Using this method is equal to using <code>(byte)getInt(8)</code>.
     * @return the next byte
     */
    public final byte getByte()
    {
        return (byte)getInt(8);
    }
    
    /**
     * Appends the specified number of (least significant) bits of the specified value.
     * 
     * @param value integer containing the bits to write aligned at the least significant bit
     * @param bitcount number of bits to write [0..32]
     * @throws IllegalArgumentException if bitcount not in [0..32]
     */
    public final void append( int value, int bitcount )
    {   
        if (bitcount == 0) return ;
        // ensure bits in range [0..32]
        ensureBitRange0to32(bitcount);
        // ensure only specified number of bits are set
        value = unsetbits(value, bitcount);
        // calculate remaining bits in current array field
        int remaining = 32-bitsof(size);
        // get array index
        int idx = indexof(size);
        // ensure array is big enough
        if (idx+2>data.length)
        {
            int[] expanded = new int[(idx+2)<<1];
            System.arraycopy(data, 0, expanded, 0, data.length);
            data = expanded;   
        }
        // number of bits of left shift operation 
        int lshift = remaining-bitcount;
        // store bits
        if (lshift>=0)
        {
            data[idx] |= (value << lshift);
        }
        else
        {
            data[idx] |= (value>>>-lshift);
            data[idx+1] = value << (32+lshift);
        }
        // add bits
        size += bitcount;
    }

    /**
     * Appends all 32 bits of the specified integer
     * @param data data to append
     */
    public final void append(int data)
    {
        append(data, 32);
    }

    /**
     * Appends the specified number of bits of the byte value.
     * The byte is interpreted as lowest significant byte of an integer
     * (and the other three bytes are set to zero).
     * @param data data to append
     * @param bitcount number of bits to append [0..32]
     */
    public final void append(byte data, int bitcount)
    {
        append(unsignedByte(data), bitcount);
    }

    /**
     * Appends all 8 bits of the specified byte
     * @param data data to append
     */
    public final void append(byte data)
    {
        append(unsignedByte(data), 8);
    }

    // ****** control functions ******
    
    /**
     * Clears all data and resets the read position to zero.
     */
    public void clear()
    {
        setSize(0); 
    }

    /**
     * Sets the size of the bitstream. This method only
     * makes the bitstream smaller.
     * @param size number of valid bits
     * 
     * TODO there seems to be a bug where the remaining bits are not cleared (set to 0)
     */
    public void setSize(int size)
    {
        // array can only be made smaller
        if (this.size > size)
        {
            int idx = indexof(size);
            // clear full fields 
            if (idx+1<data.length)
                Arrays.fill(data, idx+1, data.length, 0);
            // clear bits in last field
            data[idx] &= 0xFFFFFFFF << (32-bitsof(size));
            // update size and pointer
            this.size = size;
            position = Math.min(position, size);
        }
    }

    /**
     * Returns true if the specified number of bits is available for reading.
     * @param bitcount number of bits to check if they are available
     * @return true if the specified number of bits is available for reading
     */
    public final boolean isAvailable( int bitcount )
    {
        return position + bitcount <= size;
    }

    /**
     * Returns the number of valid bits in this bitstream.
     * @return the number of valid bits in this bitstream
     */
    public int getSize()
    {
        return size;
    }

    /**
     * Returns the current reading position of this bitstream.
     * @return the current reading position of this bitstream
     */
    public int getPosition()
    {
        return position;
    }

    /**
     * Sets the current reading position of this bitstream.
     * The position can be invalid but has to be &gt;=0.
     * @param position new position
     * @throws IllegalArgumentException if the new position is negative
     */
    public void setPosition( int position )
    {
        if (position<0)
            throw new IllegalArgumentException("invalid position: "+position);
        this.position = position;
    }

    // ****** misc functions ******
    
    /**
     * Returns a copy of this bitstream.
     */
    public BitStream clone()
    {
        try
        {
            return (BitStream) super.clone();
        }
        catch (CloneNotSupportedException e)
        {
            throw new InternalError(e.getMessage());
        }
    }
    


    // ****** toArray functions ******

    /**
     * Returns an array of byte containing all completed 8-bit blocks 
     * stored in this bitstream.
     */
    public byte[] toByteArray()
    {
        // return empty array
        if (size <= 0) return EMPTY_BYTES;
        // calculate number of complete byte blocks
        int bytes = size/8;
        // calculate number of complete integer blocks
        int blocks = size/32;
        // create array
        byte[] bdata = new byte[bytes];
        // for each byte b:
        int b = 0;
        for (;b<blocks;b+=4)
        {
            // write complete integer block
            int d = data[b>>>2];
            bdata[b]   = (byte) ((d>>24)&0xFF);
            bdata[b+1] = (byte) ((d>>16)&0xFF);
            bdata[b+2] = (byte) ((d>> 8)&0xFF);
            bdata[b+3] = (byte) ((d    )&0xFF);
        }
        // write remaining 0-3 bytes
        while (b<bytes)
        {
            bdata[b] = (byte) ((data[b>>>2]>>>(24-(8*(b%4))))&0xFF);
            b++;
        }
        // return data
        return bdata;
    }
    
    /**
     * Returns an array of byte containing all completed 32-bit blocks 
     * stored in this bitstream.
     */
    public int[] toIntArray()
    {
        // return empty array
        if (size <= 0) return EMPTY_INTS;
        // calculate number of complete integer blocks
        int ints = size/32;
        // create array
        int[] idata = new int[ints];
        // copy data
        System.arraycopy(data, 0, idata, 0, ints);
        // return data
        return idata;
    }
    
    // ****** helpers ******

    /**
     * Converts a signed byte to an unsigned integer.
     * 
     * @param b the byte value to convert
     * @return unsigned integer
     */
    static final int unsignedByte(byte b)
    {
        return (int) (b & 0xFF);
    }    

    /**
     * Creates an integer from the four specified bytes.
     * 
     * The sign of the return value is the sign of the specified byte b3.
     * Thus <code>signum(b2i(b3, 0, 0, 0)) == signum(b3)</code> is always <code>true</code>.
     * 
     * @param b3 sets the bits 31..24
     * @param b2 sets the bits 23..16
     * @param b1 sets the bits 15.. 8
     * @param b0 sets the bits  7.. 0
     * @return an integer created from four bytes 
     */
    static final int b2i(byte b3, byte b2, byte b1, byte b0)
    {
        return (unsignedByte(b3)<<24) | (unsignedByte(b2)<<16) | (unsignedByte(b1)<<8) | unsignedByte(b0);
    }

    /**
     * Ensures that the specified argument is in the range <code>[0..32]</code>.
     * @param bitcount checked if in range <code>[0..32]</code>
     * @throws IllegalArgumentException if the condition is violated
     */
    static final void ensureBitRange0to32(int bitcount) 
        throws IllegalArgumentException
    {
        if ((bitcount>32) || (bitcount<0))
            throw new IllegalArgumentException("invalid number of bits:"+bitcount);
    }
    
    /**
     * Extracts the index encoded in the specified integer.
     * The index is stored at the bit position 31..5.
     * The return value is equal to a division of the position by 32,
     * thus for positive or zero arguments following is <code>true</code>:
     * <code>p/32==indexof(p)</code>.
     * 
     * The following condition is <code>true</code> for positive or zero
     * arguments: <code>p==indexof(p)*32 +bitsof(p)</code>.
     * 
     * @param position contains the index
     * @return index encoded in the specified integer
     */
    static final int indexof(int position)
    {
        return position >>> 5; // == position / 32
    }

    /**
     * Extracts the bit-position encoded in the specified integer.
     * The value is stored at the bit position 4..0.
     *
     * The return value is equal to a modulo division by 32,
     * thus for positive or zero arguments following is <code>true</code>:
     * <code>p mod 32==bitsof(p)</code>.
     * 
     * The following condition is <code>true</code> for positive or zero
     * arguments: <code>p=indexof(p)*32 +bitsof(p)</code>.
     * 
     * @param position contains the index
     * @return index encoded in the specified integer
     */
    static final int bitsof(int position)
    {
        return position & 0x1F; // == position % 32
    }
    
    /**
     * Clears the 32-bitcount most significant bits.
     * 
     * @param value value of which the msb's should be cleared
     * @param bitcount number of least significant bits that are left unchanged 
     * @return value with 32-bitcount most significant bits cleared
     */
    static final int unsetbits(int value, int bitcount)
    {
        return (bitcount<32) ? (value & ~(0xFFFFFFFF << bitcount)) : value;
    }

    public int remaining()
    {
        return Math.max(0, size-position);
    }

}
