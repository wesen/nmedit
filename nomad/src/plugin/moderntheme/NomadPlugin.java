package plugin.moderntheme; 

import nomad.plugin.NomadFactory;

public class NomadPlugin extends nomad.plugin.NomadPlugin {

	private final
	String[] author_list = 
		new String[]{"Ian Hoogeboom", "Christian Schneider"};
	
	public String getName() {
		return "Modern Theme";
	}

	public String[] getAuthors() {
		return author_list;
	}

	public String getDescription() {
		return "Modern Theme.";
	}

	public int getFactoryType() {
		return nomad.plugin.NomadPlugin.NOMAD_FACTORY_TYPE_UI;
	}

	public NomadFactory getFactoryInstance() {
		return new ModernThemeFactory();
	}

	public boolean supportsCurrentPlatform() {
		// supports any platform
		return true;
	}

}
