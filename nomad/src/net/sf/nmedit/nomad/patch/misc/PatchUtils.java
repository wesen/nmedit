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
 * Created on Apr 19, 2006
 */
package net.sf.nmedit.nomad.patch.misc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import net.sf.nmedit.nomad.patch.Format;
import net.sf.nmedit.nomad.patch.parser.PatchFileParser;
import net.sf.nmedit.nomad.patch.parser.PatchParserException;

/**
 * Helper class that can extract some information out of a 
 * patch file.
 * 
 * @author Christian Schneider
 */
public class PatchUtils
{

    public static class PatchInfo
    {
        private String version;
        private String notes;

        public PatchInfo(String version, String notes)
        {
            this.version = version;
            this.notes = notes;
        }
        
        public String getNotes()
        {
            return notes;
        }
        
        public String getVersion()
        {
            return version;
        }
    }

    private static PatchInfo noInfo()
    {
        return new PatchInfo(null, "");
    }
    
    public static PatchInfo getPatchInfo(File patchFile)
    {
        FileReader in;
        try
        {
            in = new FileReader(patchFile);
        }
        catch (FileNotFoundException e1)
        {
            return noInfo();
        }
        
        
        PatchFileParser parser = new PatchFileParser(in);
        
        String version = null;
        StringBuffer notes = new StringBuffer();
        
        boolean hasVersion = false;
        boolean hasNotes = false;
        boolean done = false;
        
        try
        {
            while ((parser.nextToken()>=0)&&(!done))
            {
                switch (parser.getTokenType())
                {
                    case PatchFileParser.TK_SECTION_END:
                        {
                            if (hasVersion && hasNotes)
                            {
                                done = true;
                            }
                        }
                        break;
                        
                    case PatchFileParser.TK_RECORD:
                        {
                            switch (parser.getSectionID())
                            {
                                case Format.SEC_HEADER:
                                {
                                    String s = parser.getString();
                                    if (s.length()>0 && s.toLowerCase().startsWith("version"))
                                    {
                                        version = s;
                                        hasVersion = true;
                                    }
                                }
                                break;
                                case Format.SEC_NOTES:
                                {
                                    hasNotes = true;
                                    
                                    if (notes.length()>0)
                                    {
                                        notes.append("\r\n");
                                    }
   
                                    notes.append(parser.getString());
                                }
                                break;
                            }
                        }
                        break ;
                }
            }
        }
        catch (PatchParserException e)
        {
            return noInfo();
        }
        
        try
        {
            in.close();
        }
        catch (IOException e)
        {
        }
        
        return new PatchInfo(version, notes.toString());
        
    }
    
}
