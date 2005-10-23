package plugin.classictheme; 

import nomad.plugin.NomadFactory;

public class NomadPlugin extends nomad.plugin.NomadPlugin {

	private final
	String[] author_list = 
		new String[]{"Ian Hoogeboom", "Christian Schneider"};
	
	public String getName() {
		return "Classic Theme";
	}

	public String[] getAuthors() {
		return author_list;
	}

	public String getDescription() {
		return "Theme of the official editor.";
	}

	public int getFactoryType() {
		return nomad.plugin.NomadPlugin.NOMAD_FACTORY_TYPE_UI;
	}

	public NomadFactory getFactoryInstance() {
		return new ClassicThemeFactory();
	}

	public boolean supportsCurrentPlatform() {
		// supports any platform
		return true;
	}

}
