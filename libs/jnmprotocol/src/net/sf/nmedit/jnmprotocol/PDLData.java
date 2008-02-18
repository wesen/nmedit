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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

import net.sf.nmedit.jnmprotocol.PDLData;
import net.sf.nmedit.jpdl.PacketParser;
import net.sf.nmedit.jpdl.Protocol;

public final class PDLData
{

    public static final String DEFAULT_MIDI_PDL_LOCATION = "/midi.pdl";
    public static final String DEFAULT_PATCH_PDL_LOCATION = "/patch.pdl";
    
    private static String midiPDLLocation = DEFAULT_MIDI_PDL_LOCATION;
    private static String patchPDLLocation = DEFAULT_PATCH_PDL_LOCATION;

    private static Protocol midiProtocol;
    private static Protocol pdlProtocol;

    private static ClassLoader loader;
    
    private static PacketParser midiSysexParser;
    private static PacketParser patchParser;
    
    /**
     * Returns the midi protocol.
     * @throws RuntimeException if initializing the protocol failed
     */
    public static Protocol getMidiProtocol()
    {
        if (midiProtocol == null)
        {
            // protocol not parsed yet
            midiProtocol = parseProtocolFile(midiPDLLocation);
        }
        return midiProtocol;
    }

    /**
     * Returns the patch protocol.
     * @throws RuntimeException if initializing the protocol failed
     */
    public static Protocol getPatchProtocol()
    {
        if (pdlProtocol == null)
        {
            // protocol not parsed yet
            pdlProtocol = parseProtocolFile(patchPDLLocation);
        }
        return pdlProtocol;
    }

    /**
     * Returns the midi protocol's 'Sysex' parser.
     * @throws RuntimeException if initializing the protocol failed
     */
    public static PacketParser getMidiSysexParser()
    {
        if (midiSysexParser == null)
            midiSysexParser = getMidiProtocol().getPacketParser("Sysex");
        return midiSysexParser;
    }

    /**
     * Returns the patch protocol's 'Patch' parser.
     * @throws RuntimeException if initializing the protocol failed
     */
    public static PacketParser getPatchParser()
    {
        if (patchParser == null)
            patchParser = getPatchProtocol().getPacketParser("Patch");
        return patchParser;
    }
    
    /**
     * Sets the locations of the pdl files and the class loader which
     * is used to locate the files.
     * 
     * If the specified class loader is null, then the class loader of
     * PDLData will be used.
     * 
     * @param loader the class loader which locates  the pdl files
     * @param midiPDLLocation the location of the midi.pdl file
     * @param patchPDLLocation the location of the patch.pdl file
     */
    public static void setSource(ClassLoader loader, String midiPDLLocation, 
            String patchPDLLocation)
    {
        PDLData.loader = loader;
        PDLData.midiPDLLocation = midiPDLLocation;
        PDLData.patchPDLLocation = patchPDLLocation;
        
        reset();
    }
    
    /**
     * Deletes references to the used protocols.
     * The protocol variables will be automaticall reinitialized.  
     */
    public static void reset()
    {
        midiProtocol = null;
        pdlProtocol = null;
        
        midiSysexParser = null;
        patchParser = null;
    }
    
    /**
     * The class loader used to locate the pdl files.
     */
    public static ClassLoader getPDLClassLoader()
    {
        if (loader == null)
            loader = PDLData.class.getClassLoader();
        
        return loader;
    }
    
    /**
     * Returns the location of the midi pdl file.
     */
    public static String getMidiPDLLocation()
    {
        return midiPDLLocation;
    }

    /**
     * Returns the location of the patch pdl file.
     */
    public static String getPatchPDLLocation()
    {
        return patchPDLLocation;
    }

    /**
     * Returns the protocol file at the specified location.
     * @throws RuntimeException if the operation failed.
     */
    private static Protocol parseProtocolFile(String file)
    {
        ClassLoader loader = getPDLClassLoader();

        URL resource = null; 
            
        File abs = new File(file);
        if (abs.exists())
        {
            try
            {
                resource = abs.toURI().toURL();
            }
            catch (MalformedURLException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        if (resource == null)
            resource = loader.getResource(file);
        
        if (resource == null)
            throw new RuntimeException("could not initialize protocol: "+file);
        
        Reader reader;
        try
        {
            reader = new BufferedReader(new InputStreamReader(resource.openStream()));
        }
        catch (IOException e1)
        {
            throw new RuntimeException(e1);
        }

        Protocol protocol;
        try
        {
            try
            {
                protocol = new Protocol(reader);
            }
            catch (Exception e)
            {
                throw new RuntimeException("could not parse protocol: "+resource);
            }
        }
        finally
        {
            try
            {
                reader.close();
            }
            catch (IOException e)
            {
                // ignored / probably the stream was already closed
            }
        }
            
        return protocol;
    }

}
