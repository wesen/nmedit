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
