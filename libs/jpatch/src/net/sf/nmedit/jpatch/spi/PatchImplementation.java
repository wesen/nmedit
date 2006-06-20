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
package net.sf.nmedit.jpatch.spi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import sun.misc.Service;

import net.sf.nmedit.jpatch.Patch;
import net.sf.nmedit.jpatch.io.PatchDecoder;
import net.sf.nmedit.jpatch.io.PatchDecoderException;
import net.sf.nmedit.jpatch.io.PatchEncoder;
import net.sf.nmedit.jpatch.io.PatchEncoderException;
import net.sf.nmedit.jpatch.io.Source;
import net.sf.nmedit.jpatch.io.Target;
import net.sf.nmedit.jpatch.io.UnsupportedSourceException;
import net.sf.nmedit.jpatch.io.UnsupportedTargetException;

/**
 * A patch implementation
 * 
 * @author Christian Schneider
 */
public abstract class PatchImplementation 
{

    /**
     * Returns the name of the implemented patch file format.
     * @return the name of the implemented patch file format
     */
    public abstract String getName();

    /**
     * Returns the version of the implementated patch file format.
     * @return the version of the implementated patch file format
     */
    public abstract String getVersion();

    /**
     * Returns the vendor of the implementation.
     * @return the vendor of the implementation
     */
    public abstract String getVendor();

    /**
     * Creates a new patch.
     * @return a new patch
     */
    public abstract Patch createPatch();

    /**
     * Creates a patch decoder for the specified source.
     * @param source the decoder source 
     * @return the patch decoder for the specified source
     * @throws PatchEncoderException 
     */
    public PatchDecoder createPatchDecoder(Class<? extends Source> source) 
    throws PatchDecoderException, UnsupportedSourceException
    {
        for (PatchDecoderProvider provider : getPatchDecoders())
        {
            if (provider.isSupported(source))
            {
                return provider.createDecoder(this);
            }
        }
        
        throw new UnsupportedSourceException(source, "decoder not available");
    }

    /**
     * Creates a patch encoder for the specified target.
     * @param target the encoder target
     * @return the patch encoder for the specified target
     */
    public PatchEncoder createPatchEncoder(Class<? extends Target> target) 
    throws PatchEncoderException, UnsupportedTargetException 
    {
        for (PatchEncoderProvider provider : getPatchEncoders())
        {
            if (provider.isSupported(target))
            {
                return provider.createEncoder(this);
            }
        }
        
        throw new UnsupportedTargetException(target, "encoder not available");
    }

    /**
     * Returns a list of available decoders. 
     * @return a list of available decoders
     */
    public PatchDecoderProvider[] getPatchDecoders()
    {
        List<PatchDecoderProvider> list = 
            new ArrayList<PatchDecoderProvider>();

        Iterator i =
        Service.providers(PatchDecoderProvider.class);
        
        while (i.hasNext()) 
        {
            Object candidate = i.next();
        
            if (candidate instanceof PatchDecoderProvider)
            {
                PatchDecoderProvider provider = 
                    (PatchDecoderProvider) candidate;
                if (provider.isSupported(this))
                {
                    list.add(provider);
                }
            }
        }
        
        return list.toArray(new PatchDecoderProvider[list.size()]);
    }

    /**
     * Returns a list of available encoders.
     * @return a list of available encoders
     */
    public PatchEncoderProvider[] getPatchEncoders()
    {
        List<PatchEncoderProvider> list = 
            new ArrayList<PatchEncoderProvider>();

        Iterator i =
        Service.providers(PatchEncoderProvider.class);
        
        while (i.hasNext()) 
        {
            Object candidate = i.next();
        
            if (candidate instanceof PatchEncoderProvider)
            {
                PatchEncoderProvider provider = 
                    (PatchEncoderProvider) candidate;
                if (provider.isSupported(this))
                {
                    list.add(provider);
                }
            }
        }
        
        return list.toArray(new PatchEncoderProvider[list.size()]);
    }

    /**
     * Returns the default file extension of the
     * implemented patch file format. 
     * @return the default file extension
     */
    public abstract String getDefaultFileExtension();

    /**
     * Returns a list of known file extensions
     * used by this patch file format.
     * @return a list of known file extensions
     */
    public String[] getKnownFileExtensions()
    {
        return new String [] {getDefaultFileExtension()};
    }

    public static PatchImplementation getImplementation(String name)
    {
        Iterator i = Service.providers(PatchImplementation.class);
        while (i.hasNext())
        {
            PatchImplementation candidate = (PatchImplementation) i.next();
            if (candidate.getName().equals(name))
                return candidate;
        }
        return null;
    }

    public static PatchImplementation getImplementation(String name, String version)
    {
        Iterator i = Service.providers(PatchImplementation.class);
        while (i.hasNext())
        {
            PatchImplementation candidate = (PatchImplementation) i.next();
            if (candidate.getName().equals(name) && candidate.getVersion().equals(version))
                return candidate;
        }
        return null;
    }
    
}
