package net.sf.nmedit.jnmprotocol.utils;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;

/**
 * Sysex message which avoids copying the byte[] array passed to the constructor.
 */
public class FastSysexMessage extends SysexMessage
{
    // make the (byte[]) constructor public avoids
    // - new SysexMessage() creates a unused byte array
    // - setMessage creates another byte array where the data is copied to
    public FastSysexMessage(byte[] data)throws InvalidMidiDataException 
    {
        super(data);
        if (data.length==0 || (((data[0] & 0xFF) != 0xF0) && ((data[0] & 0xFF) != 0xF7))) 
        {
            super.setMessage(data, data.length); // will throw Exception
        }
    }

    // overwrite this method so that the original data array,
    // which is shared among all transmitters, cannot be modified
    public void setMessage(byte[] data, int length) throws InvalidMidiDataException 
    {
        if ((data.length == 0) || (((data[0] & 0xFF) != 0xF0) && ((data[0] & 0xFF) != 0xF7))) 
        {
            super.setMessage(data, data.length); // will throw Exception
        }
        this.length = length;
        this.data = new byte[this.length];
        System.arraycopy(data, 0, this.data, 0, length);
    }
}
