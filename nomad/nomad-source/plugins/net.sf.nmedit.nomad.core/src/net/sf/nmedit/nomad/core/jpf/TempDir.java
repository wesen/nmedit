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
package net.sf.nmedit.nomad.core.jpf;

import java.io.File;
import java.net.URISyntaxException;

import org.java.plugin.Plugin;

public class TempDir
{

    private Plugin plugin;
   
    private transient File pluginTempFile;
    
    public TempDir(Plugin plugin)
    {
        this.plugin = plugin;
    }
    
    public Plugin getPlugin()
    {
        return plugin;
    }
    
    private File getPluginTempFile()
    {
        if (pluginTempFile != null)
            return pluginTempFile;
        
        File pluginFile = null;
        try
        {
            pluginFile = new File(plugin.getDescriptor().getLocation().toURI()); // the xml file
        }
        catch (URISyntaxException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        pluginTempFile = new File(pluginFile.getParentFile(), "temp/"+File.separatorChar);
        
        if (!pluginTempFile.exists())
            pluginTempFile.mkdir();
        
        return pluginTempFile;
    }
    
    public File getTempFile(String path)
    {
        if (path.startsWith("."+File.separatorChar+"..")||path.startsWith(".."+File.separatorChar))
            throw new RuntimeException("invalid path: "+path);
        
        while (path.startsWith(""+File.separatorChar))
            path = path.substring(1); 
        
        return new File( getPluginTempFile(), path);
    }
    
}
