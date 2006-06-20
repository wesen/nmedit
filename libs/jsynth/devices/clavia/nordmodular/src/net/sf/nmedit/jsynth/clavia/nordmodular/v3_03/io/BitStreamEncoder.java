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
import net.sf.nmedit.jpatch.io.PatchEncoder;
import net.sf.nmedit.jpatch.io.PatchEncoderException;
import net.sf.nmedit.jpatch.io.Target;
import net.sf.nmedit.jpatch.io.UnsupportedTargetException;
import net.sf.nmedit.jpatch.spi.PatchImplementation;

public class BitStreamEncoder implements PatchEncoder
{

    private Patch source = null;

    public BitStreamEncoder(PatchImplementation impl)
    { }

    public void setSource( net.sf.nmedit.jpatch.Patch patch ) throws PatchEncoderException
    {
        this.source = (Patch) patch;
    }

    public void encode( Target target ) throws PatchEncoderException, UnsupportedTargetException
    {
        if (! (target instanceof BitStreamTarget)) throw new UnsupportedTargetException(target);
        
        DirectBitStreamBuilder builder = new DirectBitStreamBuilder(source);
        builder.generate();

        ((BitStreamTarget)target).setBitStream(builder.getBitStream());
        ((BitStreamTarget)target).setSectionEndPositions(builder.getSectionEndPositions());
    }

}
