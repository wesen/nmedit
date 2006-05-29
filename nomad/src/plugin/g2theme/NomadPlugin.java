package plugin.g2theme; 

import net.sf.nmedit.nomad.plugin.NomadFactory;

/**
 * The classic theme plugin.
 * 
 * @author Christian Schneider
 */
public class NomadPlugin extends net.sf.nmedit.nomad.plugin.NomadPlugin {

	private final
	String[] author_list = 
		new String[]{"Christian Schneider"};
	
	public String getName() {
		return "G2 Theme";
	}

	public String[] getAuthors() {
		return author_list;
	}

	public String getDescription() {
		return "Theme of the official G2 editor.";
	}

	public int getFactoryType() {
		return net.sf.nmedit.nomad.plugin.NomadPlugin.NOMAD_FACTORY_TYPE_UI;
	}

	public NomadFactory getFactoryInstance() {
		return new G2ThemeFactory();
	}

	public boolean supportsCurrentPlatform() {
		// supports any platform
		return true;
	}

    @Override
    public String getHomepage()
    {
        return "http://nmedit.sourceforge.net";
    }
    
    @Override
    public String getVersion()
    {
        return "0.1 (Preview)";
    }

}
