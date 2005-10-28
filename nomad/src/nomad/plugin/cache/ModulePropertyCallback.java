package nomad.plugin.cache;

public interface ModulePropertyCallback {

	public void readComponent(String className)
		throws UICacheException;
	
	public void readComponentProperty(String propertyId, String value)
		throws UICacheException;
	
}
