package nomad.gui.property;

import java.util.HashMap;


public class PropertyMap {

	private HashMap map = new HashMap();

	public Property getProperty(String name) {
		return (Property) map.get(name);
	}

	public void putProperty(String name, Property p) {
		map.put(name, p);
	}

	public String[] getPropertyNames() {
		Object[] items = map.keySet().toArray();
		String[] result= new String[items.length];
		for (int i=0;i<result.length;i++)
			result[i] = (String) items[i];
		
		return result;
	}
	
	public void removeProperty(String name) {
		map.remove(name);
	}

}
