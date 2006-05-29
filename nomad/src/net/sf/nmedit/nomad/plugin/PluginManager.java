package net.sf.nmedit.nomad.plugin;

import java.util.ArrayList;

import net.sf.nmedit.nomad.theme.UIFactory;


/**
 * The plugin manager.
 * 
 * @author Christian Schneider
 */
public class PluginManager {
	
	// contains the NomadPlugin objects
	private static ArrayList<NomadPlugin> plugins = new ArrayList<NomadPlugin>();
	private static String[] pluginResources = new String[0];
	private static PluginClassLoader loader = new PluginClassLoader();
	/*
	static {
		// init ();
	}
	*/
	/**
	 * Loads all plugins contained int the 'plugin' directory.
	 * A plugin is either a jar file or a directory containing
	 * the NomadPlugin class.
	 */
	public static void init() {
		
		plugins.clear();
		pluginResources = loader.listPossiblePlugins();

		loadBuiltinPlugins();
		
		for (int i=0;i<pluginResources.length;i++) {

			Class pluginClass = null;
			
			try {
				pluginClass = loader.loadClass(pluginResources[i]);
			} catch (ClassNotFoundException e) {
				System.err.println("** PluginManager: Not a valid plugin: '"+pluginResources[i]+"'. Ignored.");
			}
			
			if (pluginClass != null) {
				
				NomadPlugin nomadPlugin = null;
				
				try {
					nomadPlugin = (NomadPlugin) pluginClass.newInstance();
				} catch (ClassCastException e) {
					System.err.println("** PluginManager: Incompatible plugin: '"+pluginClass.getName()+"'. Ignored.");
				} catch (Throwable e) {
					System.err.println("** PluginManager: Failed loading class '"+pluginClass.getName()+"'.");
				}
				
				if (nomadPlugin!=null) {
					plugins.add( nomadPlugin );
					System.out.println("** PluginManager: Plugin loaded: '"+pluginClass.getName()+"'.");
				}
			}
		}
	}

	/**
	 * loads built in plugins
	 */
	private static void loadBuiltinPlugins() {

	}
	
	/**
	 * Returns the number of available plugins
	 * @return the number of available plugins
	 */
	public static int getPluginCount() {
		return plugins.size();
	}
	
	/**
	 * Returns a plugin at the given index.
	 * @param index the index
	 * @return the plugin
	 */
	public static NomadPlugin getPlugin(int index) {
		return plugins.get(index);
	}
	
	/**
	 * Returns the default UIFactory instance.
	 * @return the default UIFactory instance.
	 */
	public static UIFactory getDefaultUIFactory() {
		for (int i=0;i<getPluginCount();i++) {
			NomadPlugin plugin = getPlugin(i);
			if (plugin.getFactoryType()==NomadPlugin.NOMAD_FACTORY_TYPE_UI
				&&plugin.getName().equals("Classic Theme"))
				return (UIFactory) plugin.getFactoryInstance();
		}
		return null;
	}
	
}
