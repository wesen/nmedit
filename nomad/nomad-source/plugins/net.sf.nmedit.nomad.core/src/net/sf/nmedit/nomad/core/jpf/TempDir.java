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
import net.sf.nmedit.nmutils.Platform;

import org.java.plugin.Plugin;
import org.java.plugin.PluginManager;
import org.java.plugin.registry.PluginDescriptor;

public class TempDir
{

    private Plugin plugin;
   
    private File root;
    
    protected TempDir(Plugin plugin)
    {
        this.plugin = plugin;
    }
    
    public static File getBaseDir() {
    	File base = null;
        if (Platform.isFlavor(Platform.OS.MacOSFlavor)) {
        	String userPath = System.getProperty("user.home");
        	base = new File(userPath, "Library/Application Support/Nomad");
        } else if (Platform.isFlavor(Platform.OS.UnixFlavor)) {
            String userPath = System.getProperty("user.home");
            base = new File(userPath, ".nomad");
        }
        else {
        	base = new File("plugin-tmp");
        }
        
        if (!base.exists()) {
            boolean result = base.mkdirs();
        }
        return base;
    }
    public static TempDir forObject(Object o)
    {
        TempDir tmp = new TempDir(PluginManager.lookup(o).getPluginFor(o));
        return tmp;
    }
    
    public static TempDir generalTempDir() {
    	TempDir tmp = new TempDir(null);
    	tmp.root = getBaseDir();
    	return tmp;
    }
    
    public Plugin getPlugin()
    {
        return plugin;
    }
    
    private File getPluginTempFile()
    {
        if (root != null)
            return root;
        
        PluginDescriptor pd = plugin.getDescriptor();
        
        String name;
        
        String s = pd.getPluginClassName();
        if (s == null)
            throw new IllegalStateException("plugin class name not specified in plugin: "+pd);
        
        name = s+"-"+pd.getVersion();

        File base = TempDir.getBaseDir();
        // TODO version checking on temp dir
        
        root = new File(base,name);
        if (!root.exists())
            root.mkdir();

        return root;
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
