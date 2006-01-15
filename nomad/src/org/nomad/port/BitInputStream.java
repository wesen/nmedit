package org.nomad.port;

/**
 * Interface for a bit-stream capable of
 * reading segments of 0 to 32 bits.
 * @author Christian Schneider
 */
public abstract class BitInputStream {
	
	/**
	 * Returns true it numbits bits are available.
	 * @param numbits number of bits to look for
	 * @return true it numbits bits are available.
	 */
	public abstract boolean isAvailable(int numbits);
	
	/**
	 * Passes numberOfBits bits. If numberOfBits is greater
	 * than the number of available bits an IndexOutOfBoundsException
	 * is thrown.
	 * @param numberOfBits number of bits to mark read
	 * @throws IndexOutOfBoundsException numberOfBits is greater
	 * than the number of available bits
	 */
	public abstract void unread(int numberOfBits)
		throws IndexOutOfBoundsException;

	/**
	 * Reads numberOfBits bits and sets the stream position
	 * to the next available bit.
	 * If numberOfBits is greater than the number of available
	 * bits an IndexOutOfBoundsException is thrown.
	 * @param numberOfBits number of bits to read
	 * @return next numberOfBits bits in stream
	 * @throws IndexOutOfBoundsException numberOfBits is greater
	 * than the number of available bits
	 */
	public abstract int read(int numberOfBits)
		throws IndexOutOfBoundsException;

	/**
	 * Reads numberOfBits bits without changing the stream position.
	 * If numberOfBits is greater than the number of available
	 * bits an IndexOutOfBoundsException is thrown.
	 * @param numberOfBits number of bits to read
	 * @return next numberOfBits bits in stream
	 * @throws IndexOutOfBoundsException numberOfBits is greater
	 * than the number of available bits
	 */
	public abstract int peek(int numberOfBits)
		throws IndexOutOfBoundsException;
	
	/**
	 * Reads 1 bit and sets the stream position to the next available bit.
	 * If 1 is greater than the number of available
	 * bits an IndexOutOfBoundsException is thrown.
	 * @return next bit in stream
	 * @throws IndexOutOfBoundsException no more bit is available
	 */	
	public int readBit() 
		throws IndexOutOfBoundsException {
		return read(1);
	}
	
	/**
	 * Reads 1 bit and sets the stream position to the next available bit.
	 * The return value is true if the bit is 1, false otherwise.
	 * If 1 is greater than the number of available
	 * bits an IndexOutOfBoundsException is thrown.
	 * @throws IndexOutOfBoundsException no more bit is available
	 * @return next bit in stream, true if bit is 1, false if bit is 0
	 */	
	public boolean readBitB()
		throws IndexOutOfBoundsException {
		return readBit()!=0;
	}
	
	/**
	 * Reads 4 bit and sets the stream position to the next available bit.
	 * If 4 is greater than the number of available
	 * bits an IndexOutOfBoundsException is thrown.
	 * @return next 4 bits in stream
	 * @throws IndexOutOfBoundsException less then 4 bit are available
	 */
	public int read4() throws IndexOutOfBoundsException {
		return read(4);
	}
	
	/**
	 * Reads 8 bit and sets the stream position to the next available bit.
	 * If 8 is greater than the number of available
	 * bits an IndexOutOfBoundsException is thrown.
	 * @return next 8 bits in stream
	 * @throws IndexOutOfBoundsException less then 8 bit are available
	 */
	public int read8() throws IndexOutOfBoundsException {
		return read(8);
	}
	
	/**
	 * Reads 16 bit and sets the stream position to the next available bit.
	 * If 16 is greater than the number of available
	 * bits an IndexOutOfBoundsException is thrown.
	 * @return next 16 bits in stream
	 * @throws IndexOutOfBoundsException less then 16 bit are available
	 */
	public int read16() throws IndexOutOfBoundsException {
		return read(16);
	}
	
	/**
	 * Reads 32 bit and sets the stream position to the next available bit.
	 * If 32 is greater than the number of available
	 * bits an IndexOutOfBoundsException is thrown.
	 * @return next 32 bits in stream
	 * @throws IndexOutOfBoundsException less then 32 bit are available
	 */
	public int read32() throws IndexOutOfBoundsException {
		return read(32);
	}
}
