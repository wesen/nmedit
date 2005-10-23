package nomad.plugin;

import java.io.File;

public abstract class NomadPlugin { 

	/**
	 * Must be returned by getFactoryType() if
	 * the plugin implements the ComPort interface. 
	 */
	public final static int NOMAD_FACTORY_TYPE_COMPORT = 0;
	
	/**
	 * Must be returned by getFactoryType() if
	 * the plugin implements the UIBuilder interface. 
	 */
	public final static int NOMAD_FACTORY_TYPE_UI = 1;
	
	/**
	 * @see NomadPlugin#getLocation()
	 */
	private File location = null;
	
	public NomadPlugin() {
		//
	}
	
	void setLocation(File f) {
		this.location = f;
	}
	
	/**
	 * Location of the plugin. The value is 
	 * 'plugin'+File.seperator+&lt;Pluginfolder&gt;
	 * @return location of the plugin 
	 */
	public File getLocation() {
		return this.location;
	}

	/**
	 * Returns the name of the plugin.
	 * @return the name of the plugin.
	 */
	public abstract String getName();
	
	/**
	 * Returns an array containing the author names.
	 * @return array containing the author names.
	 */
	public abstract String[] getAuthors();
	
	/**
	 * Returns an description on the plugin.
	 * @return an description on the plugin.
	 */
	public abstract String getDescription();
	
	/**
	 * Returns either NOMAD_FACTORY_TYPE_COMPORT if the factory
	 * creates a ComPort implementation or NOMAD_FACTORY_TYPE_UI
	 * if the factory is the ui factory.
	 * @return the factory type
	 */
	public abstract int getFactoryType();
	
	/**
	 * Returns an instance of the factory. 
	 * @return instance of the factory.
	 */
	public abstract NomadFactory getFactoryInstance();
	
	/**
	 * Returns true if the current platform is supported
	 * @return true if the current platform is supported
	 */
	public abstract boolean supportsCurrentPlatform();
}
