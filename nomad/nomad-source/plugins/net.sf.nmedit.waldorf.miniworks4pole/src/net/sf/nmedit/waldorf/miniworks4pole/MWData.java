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
package net.sf.nmedit.waldorf.miniworks4pole;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import net.sf.nmedit.jpatch.ModuleDescriptions;
import net.sf.nmedit.jpatch.ModuleDescriptionsParser;
import net.sf.nmedit.jpdl.Protocol;
import net.sf.nmedit.jtheme.store.DefaultStorageContext;
import net.sf.nmedit.jtheme.util.RelativeClassLoader;
import net.waldorf.miniworks4pole.MWHelper;
import net.waldorf.miniworks4pole.jpatch.MWPatch;
import net.waldorf.miniworks4pole.jprotocol.WProtocol;
import net.waldorf.miniworks4pole.jsynth.Miniworks4Pole;
import net.waldorf.miniworks4pole.jtheme.JTMWPatch;
import net.waldorf.miniworks4pole.jtheme.WMContext;

import org.xml.sax.InputSource;

public class MWData
{
    
    public static final String THEME = "/theme/miniworks-ui.xml";
    public static final String MODULE_DESCRIPTIONS = "/4pole-example.xml";

    private static ModuleDescriptions md;
    private static DefaultStorageContext storageContext;
    private static WMContext jtcontext;
    private static boolean initialized = false;

    public static void init() throws Exception
    {
        if (initialized)
            return;
        initialized = true;
        
        md = MWHelper.createModuleDescriptions();

        InputStream in = new BufferedInputStream(MWData.class.getResourceAsStream(MODULE_DESCRIPTIONS));
        
        try
        {
            ModuleDescriptionsParser.parse(md, in);
        }
        finally
        {
            in.close();
        }

        URL themeURL = MWData.class.getResource(THEME);
        File themeFile = new File(themeURL.getPath());
   
        ClassLoader loader = new RelativeClassLoader(themeFile.getParentFile());

        jtcontext = new WMContext();
        storageContext = MWHelper.createStorageContext(jtcontext, loader);
        jtcontext.setStorageContext(storageContext);
        
        Reader reader;
        reader = new BufferedReader(new FileReader(themeFile));
        try
        {
            storageContext.parseStore(new InputSource(reader));
        }
        finally
        {
            reader.close();
        }

        jtcontext.setUIDefaultsClassLoader(MWData.class.getClassLoader());

        reader = new InputStreamReader(MWData.class.getResourceAsStream("/codecs/waldorf-miniworks-4pole.pdl")); 
        try
        {
            WProtocol.setProtocol(new Protocol(reader));
        }
        finally
        {
            reader.close();
        }
    }

    public static ModuleDescriptions getModuleDescriptions()
    {
        ensureInitialized();
        return md;
    }

    private static void ensureInitialized()
    {
        if (!initialized)
        {
            try
            {
                init();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public static DefaultStorageContext getStorageContext()
    {
        ensureInitialized();
        return storageContext;
    }

    public static WMContext getUIContext()
    {
        ensureInitialized();
        return jtcontext;
    }
    
    public static JTMWPatch createPatchUI(MWPatch patch)
    {
        ensureInitialized();

        return new JTMWPatch(storageContext, jtcontext, md, patch);
    }

    public static Miniworks4Pole createSynth()
    {
        ensureInitialized();
        
        return new Miniworks4Pole(md);
    }

    public static MWPatch createPatch()
    {
        return new MWPatch(md);
    }
    
}
