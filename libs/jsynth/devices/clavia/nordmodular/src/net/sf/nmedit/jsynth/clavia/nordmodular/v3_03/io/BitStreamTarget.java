/* Copyright (C) 2006 Christian Schneider
 * 
 * This file is part of Nomad.
 * 
 * Nomad is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Nomad is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Nomad; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

/*
 * Created on Jun 20, 2006
 */
package net.sf.nmedit.jsynth.clavia.nordmodular.v3_03.io;

import java.util.List;

import net.sf.nmedit.jnmprotocol.PatchMessage;
import net.sf.nmedit.jpatch.io.Target;
import net.sf.nmedit.jpdl.BitStream;

public class BitStreamTarget implements Target
{

    private BitStream bitStream;
    private List<Integer> sectionEndPositions;

    public BitStreamTarget()
    {
        this.bitStream = null;
    }
    
    public BitStream getBitStream()
    {
        return bitStream;
    }
    
    public void setBitStream(BitStream bitStream)
    {
        this.bitStream = bitStream;
    }

    public void setSectionEndPositions(List<Integer> list)
    {
        this.sectionEndPositions = list;
    }
    
    public List<Integer> getSectionEndPositions()
    {
        return sectionEndPositions;
    }

    public PatchMessage generateMessage( int slotID ) throws Exception
    {
        return new PatchMessage(getBitStream(), getSectionEndPositions(), slotID);
    }
}
