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

import org.xml.sax.InputSource;

import net.sf.nmedit.jpatch.clavia.nordmodular.NM1ModuleDescriptions;
import net.sf.nmedit.jtheme.clavia.nordmodular.JTNM1Context;
import net.sf.nmedit.jtheme.clavia.nordmodular.NMStorageContext;
import net.sf.nmedit.jtheme.store.DefaultStorageContext;
import net.sf.nmedit.jtheme.util.RelativeClassLoader;
import net.sf.nmedit.nmutils.Timer;

public class Nordmodular
{
    
    private static Nordmodular instance = new Nordmodular();

    public static Nordmodular sharedInstance()
    {
        return instance;
    }
    
    public static synchronized NMContext sharedContext()
    {
        Nordmodular nm = sharedInstance();
        if (nm.context == null)
        {
            try
            {
                nm.init();
            }
            catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
        return nm.context;
    }
    
    
    private InputStream getResourceAsStream(String path)
    {
        return getClass().getClassLoader().getResourceAsStream(path);
    }
    
    private NMContext context;
     
    private void init() throws Exception
    {
        InputStream source;
        
        Timer timer = new Timer();
        
        timer.reset();
        
        NM1ModuleDescriptions descriptions;
        source = getResourceAsStream("module-descriptions/modules.xml");
        try
        {
            descriptions = NM1ModuleDescriptions.parse(source);
        }
        finally
        {
            source.close();
        }
        
        System.out.println("descriptions: "+timer);
        
        timer.reset();
        DefaultStorageContext storageContext;
        final String ct = "classic-theme/";
        final String ctf = ct+"classic-theme.xml";
        source = getResourceAsStream(ctf);
        try
        {
            URL relative = getClass().getClassLoader().getResource(ctf);
            
            String r = relative.getPath();
            r = r.substring(0, r.lastIndexOf("/"))+"/";
            
            RelativeClassLoader rcl = new RelativeClassLoader(r, getClass().getClassLoader());
            
            storageContext = new NMStorageContext(rcl);
            storageContext.parseStore(new InputSource(source));
        }
        finally
        {
            source.close();
        }
        System.out.println("theme: "+timer);
        
        JTNM1Context jtcontext = new JTNM1Context();
        
        jtcontext.setUIDefaultsClassLoader(getClass().getClassLoader());
        
        context = new NMContext(descriptions, jtcontext, storageContext);
    }
    
}

