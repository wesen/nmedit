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
package net.sf.nmedit.nordmodular;

import java.io.InputStream;
import java.net.URL;

import javax.swing.ImageIcon;

import net.sf.nmedit.jtheme.clavia.nordmodular.JTNM1Context;
import net.sf.nmedit.jtheme.clavia.nordmodular.NMStorageContext;
import net.sf.nmedit.jtheme.store.DefaultStorageContext;
import net.sf.nmedit.jtheme.util.RelativeClassLoader;
import net.sf.nmedit.nomad.core.jpf.TempDir;

import org.xml.sax.InputSource;

public class NMContextData 
{

    private JTNM1Context jtContext;
    private static NMContextData instance;
    private ImageIcon nmMicroIcon;
    private ImageIcon nmRackIcon;
    private ImageIcon nmModularIcon;
    private NMHelpHandler helpHandler = new NMHelpHandler();

    public static NMContextData sharedInstance()
    {
        if (instance == null)
            instance = new NMContextData();
        
        return instance;
    }

    {
        jtContext = NMContextData.this.initContextSavely();
        JTNM1Context.setCachedContext(jtContext);
        //cr.prepareData();
    }

    public NMHelpHandler getHelpHandler()
    {
        return helpHandler;
    }
    
    private ImageIcon loadIcon(String name)
    {
        if (name == null)
            return null;
        URL resource = getClass().getClassLoader().getResource(name);
        
        if (resource == null)
        {
            // System.err.println("resource not found: "+name);
            return null;
        }
        return new ImageIcon(resource);
    }

    public ImageIcon getMicroIcon()
    {
        if (nmMicroIcon == null)
            nmMicroIcon = loadIcon("icons/micro-modular-icon.png");
        return nmMicroIcon;
    }

    public ImageIcon getModularIcon()
    {
        if (nmModularIcon == null)
            nmModularIcon = loadIcon("icons/nord-modular-icon.png");
        
        return nmModularIcon;
    }

    public ImageIcon getModularRackIcon()
    {
        return getModularIcon();
    }
    
    public JTNM1Context getJTContext()
    {
        return jtContext;
    }
    
    /*
    private RelativeClassLoader getRelativeClassLoader(URL url)
    {
        String r = url.getPath();
        r = r.substring(0, r.lastIndexOf("/"))+"/";
        return new RelativeClassLoader(r, getClass().getClassLoader());
    }*/

    private JTNM1Context initContextSavely()
    {
        try
        {
            return initContext();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    private TempDir getTempDir()
    {
        return TempDir.forObject(this);
    }

    private JTNM1Context initContext() throws Exception
    {
        JTNM1Context jtcontext = new JTNM1Context(null);
        
        InputStream source;
        
        DefaultStorageContext storageContext;
        final String ct = "classic-theme/";
        final String ctf = ct+"classic-theme.xml";
        source = getResourceAsStream(ctf);
        try
        {
            URL relative = getClass().getClassLoader().getResource(ctf);
            
            storageContext = new NMStorageContext(jtcontext, RelativeClassLoader.fromPath(getClass().getClassLoader(), relative));

            ClassLoader loader = getClass().getClassLoader();

            Thread.currentThread().setContextClassLoader(loader);

            TempDir tmp = getTempDir();
            storageContext.setCacheDir(tmp.getTempFile("theme-cache"));
            
            storageContext.parseStore(new InputSource(source), loader);
        }
        finally
        {
            source.close();
        }
        
        jtcontext.setStorageContext(storageContext);
        
        jtcontext.setUIDefaultsClassLoader(getClass().getClassLoader());
        
        return jtcontext;
    }

    protected InputStream getResourceAsStream(String path)
    {
        return getClass().getClassLoader().getResourceAsStream(path);
    }    
    
}
