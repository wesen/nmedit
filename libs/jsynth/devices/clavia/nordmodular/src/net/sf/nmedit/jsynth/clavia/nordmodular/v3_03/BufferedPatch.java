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
 * Created on Jun 18, 2006
 */
package net.sf.nmedit.jsynth.clavia.nordmodular.v3_03;

import net.sf.nmedit.jnmprotocol.PatchMessage;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Patch;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.io.TranscoderException;

public class BufferedPatch
{
    
    public final static int SECTION_COUNT = 13;

    private Patch patch;
    private int sections = 0;

    private Slot target;

    public BufferedPatch(Slot target)
    {
        patch = (Patch) target.getDevice().getPatchImplementation().createPatch();
        this.target = target;
    }
    
    public boolean isComplete()
    {
        return sections>=SECTION_COUNT;
    }
    
    public Patch getPatch()
    {
        if (!isComplete())
            throw new IllegalStateException("Patch is not available yet.");
        
        return patch;
    }

    public Slot getTarget()
    {
        return target;
    }
    
    public void build(PatchMessage message) throws TranscoderException
    {
        sections ++;
        
        NordUtilities.parsePatchMessage(message, patch);
        
        if (isComplete())
        {
            target.setPatch(patch);
            target.getDevice().fireNewPatchInSlot(target);
        }
    }
    
}
