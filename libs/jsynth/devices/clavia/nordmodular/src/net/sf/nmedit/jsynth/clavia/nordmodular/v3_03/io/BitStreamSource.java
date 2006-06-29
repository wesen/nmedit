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

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Patch;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.io.BitstreamTranscoder;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.io.TranscoderException;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.io.VirtualBuilder;
import net.sf.nmedit.jpatch.io.Source;
import net.sf.nmedit.jpatch.spi.PatchImplementation;
import net.sf.nmedit.jpdl.BitStream;

public class BitStreamSource implements Source
{

    private BitStream bitStream;
    int section = 0;
    int bitstreamSetCount = 0;
    
    Patch patch = null;
    VirtualBuilder builder = null;
    BitstreamTranscoder transcoder = null;
    

    public BitStreamSource()
    {
        this.bitStream = null;
    }
    
    void initialize(PatchImplementation impl)
    {
        patch = (Patch) impl.createPatch();
        builder = new VirtualBuilder(patch);
        transcoder = new BitstreamTranscoder();
    }
    
    public void setBitStream(BitStream bitStream)
    {
        this.bitStream = bitStream;
    }
    
    public BitStream getBitStream()
    {
        return bitStream;
    }
    
    public boolean isComplete()
    {
        return section >= 13;
    }
    
    void processBitStream() throws TranscoderException
    {
        transcoder.transcode(bitStream, builder);
    }
    
}
