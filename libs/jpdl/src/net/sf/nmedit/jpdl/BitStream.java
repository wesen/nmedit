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
 * A class for reading and writing bits. 
 * 
 * TODO position and size should be of type long
 * 
 * <h1>History</h1>
 * <h3>2006-06-14</h3>
 * <ul>
 *   <li>getInt(0) now returns 0 as expected</li>
 *   <li>getInt(X) now fails when X is larger than number of available bits or negative</li>
 * </ul>
 */
public class BitStream
{

    private final static int INITIAL_CAPACITY = 20; // => 32*INITIAL_CAPACITY bits
    private final static int OFFSET_MASK = 0x1F; // = 32-1 = ... 011111
    private final static int INDEX_SHIFT = 5; // 2^SHIFT = 32

    /**
     * number of written bits
     */
    private int size;
    
    /**
     * points behind the last written bit
     */
    private int position;
    
    /**
     * bits
     * 
     * bit sequences are aligned at the top most bit
     * 
     * <b>example</b>
     * <p>the example uses fields with 4-bit capacity
     * rather than 32-bits for readability</p>
     * <code>
     * a:append(binary(110), bits(3));
     * b:append(binary(110), bits(3));
     * c:append(binary(110), bits(3));
     * 
     * results in:
     * 1101 1011 0___
     * aaab bbcc c
     * </code>
     */
    private int[] bits;

    
    public BitStream()
    {
        // new array
        bits = new int[INITIAL_CAPACITY];
        // reset the bit stream
        clear();
    }
    
    public int getInt(int bits)
    {
        // pre-conditions
        // 1. 0<=bits<=32
        // 2. available(bits)
        
        if (bits<0 || bits>32)
        {
            throw new IllegalArgumentException
            (
              "Specified number of bits out of range [0..32]:"+bits
            );
        }
        else if (bits==0)
        {
            // note: when position+bits>size
            // the operation still returns 0
            return 0;
        }
        else if (position+bits>size)
        {
            throw new IllegalArgumentException
            (
               "Number of requested bits ("+bits+") exceeds "
              +"number of available bits ("+(size-position)+")"      
            );
        }
        
        
        /*    | position
         *    |
         *    |   -bits-
         *    |------------|
         *    |            |
         * [__(vhead][vtail]__]
         *    
         * [--fhead-][-ftail--]
         */
     
        /* caution:
         * 1) x >>> (32*i) == x
         * 2) x  << (32*i) == x
         */
        
        final int hIndex = position >>> INDEX_SHIFT;
        final int hOffset = position & OFFSET_MASK;
        final int hSpace = 32-hOffset; // available bits in head-field 
        
        final int vHead;
        {
            // move to msb - truncates bits before head
            int fHead = this.bits[hIndex] << hOffset;
            // note: see 'caution'
            // - bitsHead == 0 => (fhead >>> (32-bits)) == fhead
            vHead = (fHead >>> (32-bits))
            // truncate bits behind head not necessary:
            // 1. case: bit-count(tail)==0 => vhead is aligned at lsb
            // 2. case: bit-count(tail) >0 => vhead was in fHead aligned at lsb

            // there seems to be a bug:
            // maybe optimizations make the shift operations
            // behave like (this.bits[index]>>(32-bits-offset))
            // which does not remove bits
            // solution: remove bits with and-mask
            & (0xFFFFFFFF>>>(32-bits)) ;
        }
        
        // post-condition:
        // - update position
        position += bits;

        if (bits<=hSpace)
        {
            // all bits are in fHead
            // => bit-count(vtail) == 0
            // => (vtail == 0)
            // => ((vhead|vtail)==vhead) 
            return vHead;
        }
        else
        {
            // some bits remain in fTail
        
            // final int tIndex  = idxHead+1;
            // final int tOffset = 32;
            
            final int TAIL_SHIFT =
                // shifts tail that is alignat at msb
                // to its correct location (tail-1)...0
                (32-(hOffset+bits-32));
            
            final int vTail;
            {
                // move to lsb
                vTail = (    
                    this.bits[hIndex+1] >>> 
                    TAIL_SHIFT
                )
                // truncate bits before tail not necessary:
                // fTail is aligned at msb => no leading bits

                // this should not be necessary,
                // bit the >>> operator sometimes
                // does not set the upper bits
                // to zero
                & (0xFFFFFFFF>>>TAIL_SHIFT);
            }
            
            return (vHead|vTail);
        }
    }

    public void append( int data, int bits )
    {
        // pre-condition:
        // - specified number of bits must be in range [0..32]
        // - data must have only the specified number of bits set
        // - truncate additional bits in parameter data
        // - space for written bits has to be available
        if (bits<0||bits>32)
        {
            throw new IllegalArgumentException
            (
               "Specified number of bits out of range [0..32]: "+bits
            );
        }
        else if (bits==0)
        {   
            // no bits to write
            return ; 
        }
        else
        {
            // 1<= bits <= 32
            // note: this would fail if (bits==0)
            // because 0xFFFFFFFF>>>32 = 0xFFFFFFFF
            data &= (0xFFFFFFFF>>>(32-bits));
        }
        
        // check space
        if (((size + bits)>> INDEX_SHIFT)+1 >= this.bits.length)
        {
            // allocate space : double size
            int newSize = this.bits.length<<2;
            
            int[] newArray = new int[newSize];
            
            // copy the definied data to the new array
            for (int i = this.bits.length - 1; i >= 0; i--)
                newArray[i] = this.bits[i];
            // set the rest of the new array to zero
            for (int i = this.bits.length; i < newSize; i++)
                newArray[i] = 0;
            // finally replace the array with the larger version
            this.bits = newArray;
        }

        final int hIndex = size >>> INDEX_SHIFT;
        final int hOffset = size & OFFSET_MASK;
        final int hSpace= 32-hOffset;
        
        final int vHead = (
            // move to msb
            data << (32-bits)
        )
        // move to position in fHead
        >>> hOffset;
        
        this.bits[hIndex] |= vHead;
        
        if (bits>hSpace)
        {
            // add additional bits at msb of fTail
            this.bits[hIndex+1] 
              = data << //(bits-hSpace);

            (
                    -bits // highest bit to index 0
                    +32    // highest bit to index 31
                    +(32-hOffset) // tail to msb
            ) ;
        }
        
        // post-condition
        // - update size
        size += bits;
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
        if (bits.length>INITIAL_CAPACITY) 
            bits = new int[INITIAL_CAPACITY];
        
        // set each fields to zero
        Arrays.fill( bits, 0 ); 
    }

    /**
     * Sets the number of valid bits. The operation only removes the bits with
     * index >= size. It can not be used to make the bit stream larger.
     * 
     * @param size the new size (in bits)
     */
    public void setSize(int size)
    {
        // makes only smaller
        if (this.size > size)
        {
            int unset = (size>>INDEX_SHIFT);
            if (unset+1<bits.length)
            {
                // set fields unset+1 ... array.length-1 to zero
                Arrays.fill(bits, unset+1, bits.length, 0);
            }

            // we may have to remove some bits in the last field
            int validbits = size&OFFSET_MASK; // == size%32;
            // set invalid bits to zero
            bits[unset] &= ~(0xFFFFFFFF>>>validbits);

            this.size = size;
            if (position > size) position = size;
        }
    }

    /**
     * Returns true when the specified number of bits are available starting from
     * the current position.
     * 
     * @param amount number of bits
     * TODO throw IllegalArgumentException when (amount &lt; 0)
     */
    public boolean isAvailable( int amount )
    {
        return position + amount <= size;
    }

    /**
     * Returns the number of written bits.
     * @return the number of written bits
     */
    public int getSize()
    {
        return size;
    }

    /**
     * Returns the current bit position.
     * @return the current bit position.
     */
    public int getPosition()
    {
        return position;
    }

    /**
     * Sets the position to the given bit index.
     * @param position bit index
     */
    public void setPosition( int position )
    {
        this.position = position;
    }

}

