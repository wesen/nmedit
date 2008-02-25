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

package net.sf.nmedit.jnmprotocol2;

import net.sf.nmedit.jnmprotocol2.utils.NmCharacter;
import net.sf.nmedit.jpdl2.stream.BitStream;
import net.sf.nmedit.jpdl2.utils.PDLUtils;

public class MidiException extends Exception
{
    
    /**
     * 
     */
    private static final long serialVersionUID = 5967172605877128644L;
    public static final int INVALID_MIDI_DATA = -1000;
    public static final int TIMEOUT = -1001;
    public static final int MIDI_PARSE_ERROR = -1002;
    public static final int UNKNOWN_MIDI_MESSAGE = -1003;
    
    // the midi message which caused this exception
    private byte[] midiMessage;
    
    public MidiException(String message, int error)
    {
	this.message = message;
	this.error = error;
    }

    /**
     * Sets the midi message which caused this exception
     */
    public void setMidiMessage(byte[] message)
    {
        this.midiMessage = message;
    }

    /**
     * Returns the midi message which caused this exception
     */
    public byte[] getMidiMessage()
    {
        return midiMessage;
    }
    
    public String getMessage()
    {
        StringBuilder sb = new StringBuilder();
        if (message != null)
        {
            sb.append(message);
            sb.append(" ");
        }
        sb.append("[error=");
        sb.append(Integer.toString(getError()));
        if (midiMessage != null)
        {
            sb.append(",message={");
            sb.append(PDLUtils.toHexadecimal(midiMessage));
            sb.append("}");
            sb.append(",text={");
            sb.append(NmCharacter.toText(midiMessage));
            sb.append("}");
        }
        sb.append("]");
        return sb.toString();
    }

    public int getError()
    {
	return error;
    }

    private String message;
    private int error;

    public void setMidiMessage(BitStream bitStream)
    {
        setMidiMessage(bitStream.toByteArray());
    }
    
}
