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
package net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.io;

import net.sf.nmedit.jpatch.Patch;
import net.sf.nmedit.jpatch.io.FileSource;
import net.sf.nmedit.jpatch.io.PatchDecoder;
import net.sf.nmedit.jpatch.io.PatchDecoderException;
import net.sf.nmedit.jpatch.io.Source;
import net.sf.nmedit.jpatch.io.UnsupportedSourceException;
import net.sf.nmedit.jpatch.spi.PatchImplementation;

public class PatchFileDecoder implements PatchDecoder
{

    private PatchImplementation impl;
    private Patch patch = null;
    private boolean decoded = false;

    public PatchFileDecoder(PatchImplementation impl)
    {
        this.impl = impl;
    }

    public Patch getPatch() throws PatchDecoderException
    {
        // TODO better messages
        if (!decoded) throw new PatchDecoderException("Decoder has not been started");
        if (patch == null) throw new PatchDecoderException("Decoder failed");
        return patch;
    }

    public void decode( Source source ) throws PatchDecoderException,
            UnsupportedSourceException
    {
        if (!(source instanceof FileSource)) throw new UnsupportedSourceException(source);
        decoded = true;
   
        PatchFileParser parser = new PatchFileParser(((FileSource)source).getReader());
        VirtualBuilder builder = new VirtualBuilder(
                (net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Patch)impl.createPatch()
        );
        PatchParserTranscoder t = new PatchParserTranscoder();
        try
        {
            t.transcode(parser, builder);
            
            patch = builder.getPatch();
            // TODO disable history while building
            patch.getHistory().clear();
            patch.getHistory().setModified(false);
        }
        catch (TranscoderException e)
        {
            throw new PatchDecoderException(e);
        }
    }

}
