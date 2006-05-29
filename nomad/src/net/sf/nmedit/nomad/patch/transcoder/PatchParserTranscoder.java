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
 * Created on Apr 18, 2006
 */
package net.sf.nmedit.nomad.patch.transcoder;

import net.sf.nmedit.nomad.patch.builder.PatchBuilder;
import net.sf.nmedit.nomad.patch.parser.PatchParser;
import net.sf.nmedit.nomad.patch.parser.PatchParserException;

/**
 * Uses a {@link net.sf.nmedit.nomad.patch.parser.PatchParser} as source and
 * feeds the {@link net.sf.nmedit.nomad.patch.builder.PatchBuilder} with the data. 
 * 
 * @author Christian Schneider
 */
public class PatchParserTranscoder extends Transcoder<PatchParser, PatchBuilder>
{

    public void transcode( PatchParser parser, PatchBuilder callback ) throws TranscoderException
    {
        try
        {
            transcodeInternal(parser, callback);
        }
        catch (PatchParserException e)
        {
            throw new TranscoderException(e);
        }
    }

    protected void transcodeInternal( PatchParser parser, PatchBuilder callback ) throws PatchParserException
    {
        while (parser.nextToken()>=0)
        {
            switch (parser.getTokenType())
            {
                case PatchParser.TK_SECTION_START:
                    
                    callback.beginSection(parser.getSectionID());
                    break;
                    
                case PatchParser.TK_SECTION_END:
                    
                    callback.endSection(parser.getSectionID());
                    break;
                    
                case PatchParser.TK_RECORD:
                    
                    callback.record(parser);
                    break;
                default:
                    throw new IllegalStateException("unknown token type:"+parser.getTokenType());
            }
        }
    }

}
