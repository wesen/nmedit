package nomad.com;

import nomad.plugin.NomadFactory;
import nomad.plugin.NomadPlugin;

public class NullComPortPlugin extends NomadPlugin {

	private final static String[] authors = new String[] {"Christian Schneider"};
	
	public String getName() {
		return "NullComPort";
	}

	public String[] getAuthors() {
		return authors;
	}

	public String getDescription() {
		// TODO Auto-generated method stub
		return "Fake ComPort implementation";
	}

	public int getFactoryType() {
		// TODO Auto-generated method stub
		return NomadPlugin.NOMAD_FACTORY_TYPE_COMPORT;
	}

	public NomadFactory getFactoryInstance() {
		// TODO Auto-generated method stub
		return new ComPortFactory() {
			public ComPort getInstance() {
				return new NullComPort();
			}
		};
	}

	public boolean supportsCurrentPlatform() {
		// supports any platform
		return true;
	}

}