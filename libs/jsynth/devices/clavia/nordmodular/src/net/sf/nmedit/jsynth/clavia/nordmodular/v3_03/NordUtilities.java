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
 * Created on May 17, 2006
 */
package net.sf.nmedit.jsynth.clavia.nordmodular.v3_03;

import net.sf.nmedit.jnmprotocol.PatchMessage;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Patch;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.io.BitstreamTranscoder;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.io.TranscoderException;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.io.VirtualBuilder;
import net.sf.nmedit.jpatch.io.PatchEncoder;
import net.sf.nmedit.jsynth.clavia.nordmodular.v3_03.io.BitStreamTarget;

public class NordUtilities
{

    public static PatchMessage generatePatchMessage(Patch patch, int slotID) throws Exception
    {/*
        DirectBitStreamBuilder builder = new 
            DirectBitStreamBuilder(patch);
        builder.generate();

        PatchMessage patchMessage =
            builder.generateMessage(slotID);
        
        return patchMessage; */

        PatchEncoder enc = patch.getPatchImplementation().createPatchEncoder(BitStreamTarget.class);
        enc.setSource(patch);
        
        BitStreamTarget target = new BitStreamTarget();
        enc.encode(target);
        
        return target.generateMessage(slotID);
        
        /*
        VirtualTranscoder transcoder = new VirtualTranscoder();
        BitStreamBuilder bitStreamBuilder = new BitStreamBuilder();
        transcoder.transcode(patch, bitStreamBuilder);
        
        PatchMessage patchMessage =
            bitStreamBuilder.generateMessage(slotID);
        
        return patchMessage;*/
    }

    public static void parsePatchMessage(PatchMessage message, Patch patch) throws TranscoderException
    {
        BitstreamTranscoder transcoder = new BitstreamTranscoder();
        VirtualBuilder patchBuilder = new VirtualBuilder(patch);
        transcoder.transcode(message.getPatchStream(), patchBuilder);
    }

}
