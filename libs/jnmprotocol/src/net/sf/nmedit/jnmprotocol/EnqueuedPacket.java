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

import javax.sound.midi.InvalidMidiDataException;

import net.sf.nmedit.jnmprotocol.EnqueuedPacket;
import net.sf.nmedit.jnmprotocol.MidiException;
import net.sf.nmedit.jnmprotocol.utils.FastSysexMessage;

public class EnqueuedPacket extends FastSysexMessage
{
    public static EnqueuedPacket create(byte[] data, boolean expectsreply)
        throws MidiException
    {
        try
        {
            return new EnqueuedPacket(data, expectsreply);
        }
        catch (InvalidMidiDataException e)
        {
            throw new MidiException(e.getMessage(), 0);
        }
    }
    
    public EnqueuedPacket(byte[] data, boolean expectsreply) throws InvalidMidiDataException {
        super(data);
        this.expectsreply = expectsreply;
    }
    
    public boolean expectsReply() {
	return expectsreply;
    }
  
    private boolean expectsreply;

}
