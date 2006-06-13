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
 * Created on May 19, 2006
 */

package net.sf.nmedit.jpdl;


import java.util.Arrays;


/**
 * A class for reading and writing bits. TODO position and size should be of
 * type long
 */
public class BitStream
{

    /**
     * the array grows in blocks of the given size
     */
    private final static int INIT_SIZE = 20;

    /**
     * the array index is calculated by dividing (bit position p) (p/32) which
     * is equal to p>>SHIFT
     */
    private final static int SHIFT = 5; // 2^SHIFT = 32

    /**
     * the bit position inside an array field is calculated by the modulo
     * division (bit position p) (p % 32) which is equal to (p & MASK)
     */
    private final static int MASK = 0x1F; // binary 0...011111 = (32-1)

    /**
     * current bit position
     */
    private long position;

    /**
     * number of defined bits
     */
    private long size;

    /**
     * array with 32 bits per field fields are filled starting from the least
     * significant bit to the most significant bit
     */
    private long[] array;

    public BitStream()
    {
        // new array
        array = new long[INIT_SIZE];
        // reset the bit stream
        clear();
    }

    /**
     * Removes all bits. The position and the size are set to 0 (zero).
     */
    public void clear()
    {
        // reset position and size
        position = 0;
        size = 0;
        
        // use a smaller array when it become too large
        if (array.length>INIT_SIZE) 
            array = new long[INIT_SIZE];
        
        // set each fields to zero
        Arrays.fill( array, 0 ); 
    }

    /**
     * Appends a specified number of bits to the end of the bit stream.
     * Bits are appended starting from (bitcount-1) downto 0.
     * 
     * @param data the specified bits are appended starting from the index
     * (bitcount-1) downto 0
     * @param bitcount 0-32 bits are appended
     */
    public void append( int data, int bitcount )
    {        
        if (bitcount>32||bitcount<0)
            throw new IllegalArgumentException("Number of bits out of range (0-32):"+bitcount);
        //else if (bitcount==0) return;

        // check if there is enough space left in the array
        if (( ( size + bitcount ) >> SHIFT ) + 1 >= array.length)
        {
            // we have to create a larger array
            // the array should grow in blocks
            int asize = array.length << 2; // double array size
            long[] newarray = new long[asize];
            // copy the definied data to the new array
            for (int i = array.length - 1; i >= 0; i--)
                newarray[i] = array[i];
            // set the rest of the new array to zero
            for (int i = array.length; i < asize; i++)
                newarray[i] = 0;
            // finally replace the array with the larger version
            array = newarray;
        }

        /*
         * case 1: inside
         * 
         *         - bitcount -
         *        |-------------|
         * +----------------------------+
         * | .A.  (head     ... )   .B. | , A and B can have 0 bits
         * +----------------------------+
         *  31                         0
         *  
         * case 2: overlapping
         * 
         *          - bitcount -
         *        |--------------|
         * +--------------+-------------+
         * |      (head ..|. tail)      |
         * +--------------+-------------+
         *  31           0 31          0
         */

        // array index
        final int index = (int) (size >>> SHIFT);
        // offset from the msb
        final long offset = size & MASK;

        // values in long type
        final long lsize = (long) bitcount;
        final long ldata = (long) data;
        
        long head = ldata;
        head &= (0xFFFFFFFF>>>(32-lsize)); // remove unecessary bits 
        head <<= (32-lsize); // align at msb, this will remove overlapping bits
        head>>>= (offset);  // align at defined bits

        // store head without changing saved bits
        array[index] |= head;

        // check if there is a tail
        if (offset+lsize>32)
        {
            // yes there is a tail
            final long tail = ldata
                << // tail is aligned at msb 
                (
                        -lsize // highest bit to index 0
                        +32    // highest bit to index 31
                        +(32-offset) // tail to msb
                ) ;
                
            array[index+1] = tail;
        }

        size+=lsize;
    }
    
    /**
     * Returns a specified number of bits. The return value's bits are set
     * starting from the index (bitCount-1) downto 0
     * 
     * @param bitcount 0-32 bits
     * @return returns a specified number of bits
     */
    public int getInt( int bitcount )
    {
	if (bitcount == 0)
	    return 0;

        if (bitcount>32||bitcount<0)
            throw new IllegalArgumentException("Number of bits out of range (0-32):"+bitcount);
	
        // array index
        final int index = (int) (position >>> SHIFT);
        // offset from the msb
        final long offset = position & MASK;

        // values in long type
        final long lsize = bitcount;
        
        long head = array[index];
        // align at msb
        head <<= offset;
        // move at correct position
        head >>>= (32-lsize);
        
        // there seems to be a bug:
        // maybe optimizations make the shift operations
        // behave like (array[index]>>(32-lsize-offset))
        // which does not remove bits
        // solution: remove bits with and-mask
        head &= (0xFFFFFFFF>>>(32-bitcount)) ;
        
        // tail
        final long tail;
        // check if tail exits
        if (offset+lsize>32)
        {
            final long TAIL_SHIFT =
                // shifts tail that is alignat at msb
                // to its correct location (tail-1)...0
                (32-(offset+lsize-32));
            
            tail =
                (
                // tail at msb in next field
                array[index+1] 
                // move to correct location    
                >>>TAIL_SHIFT
                )
                // this should not be necessary,
                // bit the >>> operator sometimes
                // does not set the upper bits
                // to zero
                & (0xFFFFFFFF>>>TAIL_SHIFT);
        }
        else
        {
            // no tail
            tail = 0;
        }
            
        position+=lsize;
        
        return (int) (head|tail);
    }

    /**
     * Returns the current bit position
     * 
     * @return the current bit position.
     */
    public int getPosition()
    {
        return (int) position;
    }

    /**
     * Returns the number of defined bits.
     * 
     * @return the number of defined bits
     */
    public int getSize()
    {
        return (int) size;
    }

    /**
     * Sets the position to the given bit index.
     * 
     * @param position bit index
     */
    public void setPosition( int position )
    {
        this.position = position;
    }

    /**
     * Sets the number of valid bits. The operation only removes the bits with
     * index >= size. It can not be used to make the bit stream larger.
     * 
     * @param size the new size
     */
    public void setSize( int size )
    {
        // makes only smaller
        if (this.size > size)
        {
            int unset = (size>>SHIFT);
            if (unset+1<array.length)
            {
                // set fields unset+1 ... array.length-1 to zero
                Arrays.fill(array, unset+1, array.length, 0);
            }
            
	    // we may have to remove some bits in the last field
	    int validbits = size&MASK; // == size%32;
	    // set invalid bits to zero
	    array[unset] &= ~(0xFFFFFFFF>>>validbits);

            this.size = size;
            if (position > size) position = size;
        }
    }

    /**
     * Returns true when the specified number of bits are definied starting from
     * the current position.
     * 
     * @param amount number of bits
     * @return true when the specified number of bits are definied starting from
     * the current position.
     */
    public boolean isAvailable( int amount )
    {
        return position + amount <= size;
    }

}

