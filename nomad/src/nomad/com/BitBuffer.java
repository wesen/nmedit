package nomad.com;

import java.nio.BufferUnderflowException;

/**
 * Implementation of BitInputStream. Allows random
 * read/write operations.
 * @author Christian Schneider
 * @hidden
 */
public class BitBuffer extends BitInputStream {

	final static int BITS_BIT = 1;
	final static int BITS_BYTE = 8;
	final static int BITS_SHORT = 16;
	final static int BITS_INT = 32;
	final static int BITS_LONG = 32;
	final static int BITS_FLOAT = 32;
	final static int BITS_DOUBLE = 64;

	int[] buffer = null;
	int pos=0;
	int size=0;
	
	/**
	 * Create a new empty BitBuffer
	 */
	public BitBuffer() {
		buffer = new int[]{};
	}
	
	/**
	 * Create a new BitBuffer that wraps argument buffer.
	 * @param buffer the wrapped array
	 * @param bitCount number of valid bits in buffer 
	 * @throws IndexOutOfBoundsException if buffer contains less than bitCount bits.
	 */
	private BitBuffer(int[] buffer, int bitCount)
		throws IndexOutOfBoundsException {
		super();
		if (bitCount/32 > buffer.length)
			throw new IndexOutOfBoundsException("bitCount implies more data than available in buffer");
		
		this.buffer = buffer;
		this.size = bitCount;
	}
	
	/**
	 * Creates a new BitBuffer that uses buffer as initial data.
	 * @param buffer the wrapped array
	 * @param bitCount number of valid bits in buffer
	 * @return new BitBuffer
	 * @throws IndexOutOfBoundsException if buffer contains less than bitCount bits.
	 */
	public static BitBuffer wrap(int[] buffer, int bitCount)
		throws IndexOutOfBoundsException {
		return new BitBuffer(buffer, bitCount);
	}
	
	/**
	 * Creates a new BitBuffer using a copy of buffer as initial data.
	 * @param buffer the initial data
	 * @param bitCount number of valid bits in buffer
	 * @return new BitBuffer
	 * @throws IndexOutOfBoundsException if buffer contains less than bitCount bits.
	 */
	public static BitBuffer wrapclone(int[] buffer, int bitCount)
		throws IndexOutOfBoundsException {
		int[] clone = new int[buffer.length];
		for (int i=0;i<buffer.length;i++)
			clone[i] = buffer[i];
		return BitBuffer.wrap(clone, bitCount);
	}
	
	/**
	 * Returns a new BitInputStream initialized with a copy of the data of this object 
	 * @return BitInputStream initialized with a copy of the data of this object
	 */
	public BitInputStream getNewInputStream() {
		return BitBuffer.wrapclone(buffer, size);
	}
	
	/**
	 * Sets read position to the first bit.
	 */
	public void reset() {
		pos=0;
	}
	
	/**
	 * Writes 1 to the stream if bit is true otherwrise it writes
	 * 0 to the stream 
	 * @param bit bit that will be written to the stream
	 */
	public void write(boolean bit) {
		write(bit?1:0, 1);
	}
	
	// add a new bucket to the array with 0 as it's initial value
	private void allocateBuffer() {
		int[] tmp = new int[buffer.length+1];
		for (int i=0;i<buffer.length;i++)
			tmp[i]=buffer[i];
		tmp[buffer.length]=0;
		buffer=tmp;
	}
	
	// number of bits that will fit in the actual array
	private int capacity() {
		return buffer.length*BITS_INT;
	}
	
	/**
	 * Writes numberOfBits bits the the buffer
	 * @param bits the data
	 * @param numberOfBits number of bits
	 */
	public void write(int bits, int numberOfBits) {
		if (numberOfBits>32)
			throw new IndexOutOfBoundsException("can only write up to 32");

		int newpos = pos+numberOfBits;
		if (newpos>capacity())
			allocateBuffer();

		// mask bits of argument
		if (numberOfBits<32)
			bits &= ((1<<numberOfBits) -1);
		
		System.out.println(bits);
		
		// get position
		int index = pos / BITS_INT;
		int offset= pos % BITS_INT;
		int bufferremaining = (BITS_INT-offset);
		
		
		if (numberOfBits<=bufferremaining) {
			buffer[index]|=(bits<<(bufferremaining-numberOfBits));
		} else {
			buffer[index]|= (bits>>>(numberOfBits-bufferremaining));
			buffer[index+1]= bits>>>bufferremaining;
		}

		// update pos and size
		pos=newpos;
		size+=numberOfBits;
	}
	
	/**
	 * Returns number of available bits in stream
	 * @return number of available bits in stream
	 */
	public int available() {
		return size-pos;
	}

	/**
	 * Returns true if at lead numbits bits are available in stream
	 */
	public boolean isAvailable(int numbits) {
		return numbits<=available();
	}
	
	public void unread(int numberOfBits) {
		int newpos = pos+numberOfBits;
		if (newpos>size)
			throw new BufferUnderflowException();
		pos = newpos;
	}
	
	public int read(int numberOfBits) {
		return peek(numberOfBits, true);
	}
	
	public int peek(int numberOfBits) {
		return peek(numberOfBits, false);
	}
	
	private int peek(int numberOfBits, boolean unread) {		
		int newpos = pos+numberOfBits;
		if (newpos>size)
			throw new BufferUnderflowException();
		
		int index  = pos / BITS_INT;
		int offset = pos % BITS_INT;
		int bufferremaining = (BITS_INT-offset);
		
		// read remaining bits in current buffer and align right
		int result = buffer[index];
		if (bufferremaining<BITS_INT)
			result &= ((1<<bufferremaining) -1);
		
		if (numberOfBits<bufferremaining)
			// shift away unrequested bits
			result >>>= bufferremaining-numberOfBits;
		else if (numberOfBits>bufferremaining) {
			// number of bits in next buffer
			int notread = numberOfBits-bufferremaining;
			// shift result to make place for remaining bits
			result = result << notread;
			// load bits from next buffer
			result  |= buffer[index+1] >>> (32-notread);
		}

		if (unread)
			pos = newpos;
		return result;
	}

}
