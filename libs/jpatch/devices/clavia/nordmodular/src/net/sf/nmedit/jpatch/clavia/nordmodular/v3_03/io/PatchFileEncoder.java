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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import net.sf.nmedit.jpatch.Patch;
import net.sf.nmedit.jpatch.io.FileTarget;
import net.sf.nmedit.jpatch.io.PatchEncoder;
import net.sf.nmedit.jpatch.io.PatchEncoderException;
import net.sf.nmedit.jpatch.io.Target;
import net.sf.nmedit.jpatch.io.UnsupportedTargetException;
import net.sf.nmedit.jpatch.spi.PatchImplementation;

public class PatchFileEncoder implements PatchEncoder
{

    //private PatchImplementation impl;
    private Patch source = null;

    public PatchFileEncoder(PatchImplementation impl)
    {
        //this.impl = impl;
    }

    public void setSource( Patch patch ) throws PatchEncoderException
    {
        this.source = patch;
    }

    public void encode( Target target ) throws PatchEncoderException, UnsupportedTargetException
    {
        if (!(target instanceof FileTarget)) throw new UnsupportedTargetException(target);
        if (source==null) throw new PatchEncoderException("source not set");

        VirtualTranscoder t = new VirtualTranscoder();
        try
        {
            Writer writer = ((FileTarget)target).getWriter();
            t.transcode((net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Patch) source, 
                    new StreamBuilder(new PrintWriter(writer)));
            
            writer.flush();
        }
        catch (TranscoderException e)
        {
            throw new PatchEncoderException(e);
        }
        catch (IOException e)
        {
            throw new PatchEncoderException(e);
        }
    }

}
