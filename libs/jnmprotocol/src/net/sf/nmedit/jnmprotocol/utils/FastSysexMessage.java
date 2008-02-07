/*
    Nord Modular Midi Protocol 3.03 Library
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
