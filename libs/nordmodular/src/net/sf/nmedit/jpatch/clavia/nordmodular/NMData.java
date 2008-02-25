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

package net.sf.nmedit.jpatch.clavia.nordmodular;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

import org.xml.sax.InputSource;

import net.sf.nmedit.jpatch.clavia.nordmodular.NM1ModuleDescriptions;
import net.sf.nmedit.jpatch.transform.PTTransformationsBuillder;
import net.sf.nmedit.jpatch.transform.PTTransformations;
import net.sf.nmedit.jtheme.util.RelativeClassLoader;

public class NMData
{

    private NM1ModuleDescriptions moduleDescriptions;
    private static NMData instance;

    public static NMData sharedInstance()
    {
        if (instance == null)
            instance = new NMData();
        
        return instance;
    }

    private NMData() // no subclassing (caused multiple instances of module descriptions => errors)
    {
        moduleDescriptions = NMData.this.initModuleDescriptionsSavely();
    }
    
    public NM1ModuleDescriptions getModuleDescriptions()
    {
        return moduleDescriptions;
    }

    private NM1ModuleDescriptions initModuleDescriptions() throws Exception
    {
        InputStream source;
        
        URL mdURL = getClass().getClassLoader().getResource("module-descriptions/modules.xml");
        
        NM1ModuleDescriptions descriptions = null;

        if (descriptions == null)
        {
            source = new FileInputStream(new File(mdURL.toURI()));
            try
            {
                descriptions = NM1ModuleDescriptions.parse(RelativeClassLoader.fromPath(getClass().getClassLoader(), mdURL), source);
            }
            finally
            {
                source.close();
            }
        }
        URL transURL = getClass().getClassLoader().getResource("module-descriptions/transformations.xml");
        
        source = new FileInputStream(new File(transURL.toURI()));
        try
        {
            PTTransformations t = 
                PTTransformationsBuillder.build(new InputSource(source), descriptions);
            descriptions.setTransformations(t);
        }
        finally
        {
            source.close();
        }
        
        return descriptions;
    }

    private NM1ModuleDescriptions initModuleDescriptionsSavely()
    {
        try
        {
            return initModuleDescriptions();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /*
    private RelativeClassLoader getRelativeClassLoader(URL url)
    {
        String r = url.getPath();
        r = r.substring(0, r.lastIndexOf("/"))+"/";
        return new RelativeClassLoader(r, getClass().getClassLoader());
    }*/

    protected InputStream getResourceAsStream(String path)
    {
        return getClass().getClassLoader().getResourceAsStream(path);
    }    
}
