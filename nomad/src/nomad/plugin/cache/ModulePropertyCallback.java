package nomad.plugin.cache;


/**
 * The callback class used by the UICache class to 
 * send the user interface properties. 
 * 
 * Each call to <code>readComponent(String)</code> is followed 
 * by 0 to n calls to <code>readComponentProperty(String, String)</code>.
 * 
 * @author Christian Schneider
 * @see nomad.plugin.cache.UICache#loadModule(int, ModulePropertyCallback)
 */
public interface ModulePropertyCallback {

	/**
	 * The class name for a component used on the current module
	 * has been found. 
	 * 
	 * @param className the name of the component
	 * @throws UICacheException if the process must be interrupted
	 */
	public void readComponent(String className)
		throws UICacheException;
	
	/**
	 * A property has been read for the component identified by the
	 * previous call to <code>readComponent(String)</code>
	 * 
	 * @param propertyId the id of the property
	 * @param value the value of the property
	 * @throws UICacheException if the process must be interrupted
	 */
	public void readComponentProperty(String propertyId, String value)
		throws UICacheException;
	
}
