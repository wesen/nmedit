package nomad.plugin;

import java.io.File;
import java.net.URL;
import java.util.Vector;

import nomad.com.NullComPortPlugin;
import nomad.gui.model.UIFactory;

public class PluginManager {
	
	private static Vector plugins = new Vector();
	
	public static void init() {
		plugins = new Vector();
		
		loadDefaultPlugins();
		
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
	
	private static void loadDefaultPlugins() {
		plugins.add(new NullComPortPlugin());
	}
	
	public static int getPluginCount() {
		return plugins.size();
	}
	
	public static NomadPlugin getPlugin(int index) {
		return (NomadPlugin) plugins.get(index);
	}
	
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
