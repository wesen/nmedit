package nomad.plugin;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

import nomad.com.NullComPortPlugin;
import nomad.gui.model.UIFactory;

/**
 * The plugin manager.
 * 
 * @author Christian Schneider
 */
public class PluginManager {
	
	// contains the NomadPlugin objects
	private static ArrayList plugins = new ArrayList();
	
	/**
	 * Loads all plugins contained int the 'plugin' directory.
	 * A plugin is either a jar file or a directory containing
	 * the NomadPlugin class.
	 */
	public static void init() {
		// reset plugin list
		plugins = new ArrayList();

		// load built in plugins
		loadBuiltinPlugins();
		
		ClassLoader cloader = PluginManager.class.getClassLoader();
		URL url = cloader.getResource("plugin");
		if (url == null) {
			System.err.println("PluginManager: Plugin Folder missing.");
			return;
		}

		File[] candidates = (new File(url.getFile())).listFiles();
		for (int i=0;i<candidates.length;i++) {
			String class_name = "plugin."+candidates[i].getName()+".NomadPlugin";
			try {
				Class plugin_class = cloader.loadClass(class_name);
				NomadPlugin plugin = (NomadPlugin) plugin_class.newInstance();
				plugin.setLocation(new File("plugin"+File.separator+candidates[i].getName()));
				plugins.add(plugin);
			} catch (Throwable e) {
				System.err.println("** PluginManager: Failed loading class '"+class_name+"'.");
				System.err.println("   "+e);
			}
		}
	}
	
	/**
	 * loads built in plugins
	 */
	private static void loadBuiltinPlugins() {
		plugins.add(new NullComPortPlugin());
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
		return (NomadPlugin) plugins.get(index);
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
