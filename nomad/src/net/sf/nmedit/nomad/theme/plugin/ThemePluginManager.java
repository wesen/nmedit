package net.sf.nmedit.nomad.theme.plugin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import sun.misc.Service;

import net.sf.nmedit.nomad.theme.NMTheme;


/**
 * The plugin manager.
 * 
 * @author Christian Schneider
 */
public class ThemePluginManager 
{
    
    private static ThemePluginProvider[] themes = null;
    
    public static ThemePluginProvider[] getThemes()
    {
        if (themes != null) return themes;
        
        List<ThemePluginProvider> list = new ArrayList<ThemePluginProvider>();
        Iterator i = Service.providers(ThemePluginProvider.class);
        while (i.hasNext())
        {
            ThemePluginProvider p = (ThemePluginProvider) i.next();
            list.add(p);
        }
        
        return themes = list.toArray(new ThemePluginProvider[list.size()]);
    }
    
	/**
	 * Returns the number of available plugins
	 * @return the number of available plugins
	 */
	public static int getPluginCount() 
    {
		return getThemes().length;
	}
	
	/**
	 * Returns a plugin at the given index.
	 * @param index the index
	 * @return the plugin
	 */
	public static ThemePluginProvider getPlugin(int index) 
    {
		return getThemes()[index];
	}
    
    public static ThemePluginProvider getDefaultProvider()
    {
        return getProvider("Classic Theme");
    }
    
    public static ThemePluginProvider getProvider(String name)
    {
        for (ThemePluginProvider p : getThemes())
        {
            if (p.getName().equals(name))
                return p;
        }
        
        return null;
    }
	
	/**
	 * Returns the default UIFactory instance.
	 * @return the default UIFactory instance.
	 */
	public static NMTheme getDefaultUIFactory() 
    {
        ThemePluginProvider p = getDefaultProvider();
        return p.getFactory();
	}
	
}
