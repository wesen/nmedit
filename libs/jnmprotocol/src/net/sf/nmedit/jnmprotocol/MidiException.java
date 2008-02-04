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

package net.sf.nmedit.jnmprotocol;

import net.sf.nmedit.jnmprotocol.utils.StringUtils;

public class MidiException extends Exception
{
    
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
        sb.append("error=");
        sb.append(Integer.toString(getError()));
        if (midiMessage != null)
        {
            sb.append(",message={");
            sb.append(StringUtils.toHexadecimal(midiMessage));
            sb.append("}");
            sb.append(",text={");
            sb.append(StringUtils.toText(midiMessage));
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
    
}
