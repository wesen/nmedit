package nomad.gui.property;

import java.util.HashMap;
import java.util.Iterator;


public class PropertyMap {

	private HashMap map = new HashMap();
	private String[] names = new String[] {};

	public Property getProperty(String name) {
		return (Property) map.get(name);
	}

	public void putProperty(String name, Property p) {
		map.put(name, p);
		updateNames();
	}
	
	private void updateNames() {
		Object[] items = map.keySet().toArray();
		names = new String[items.length];
		for (int i=0;i<names.length;i++)
			names[i] = (String) items[i];
	}

	public String[] getPropertyNames() {
		return names;
	}
	
	public int getCount() {
		return names.length;
	}
	
	public void removeProperty(String name) {
		map.remove(name);
		updateNames();
	}
	
	public Iterator getPropertyIterator() {
		return map.values().iterator();
	}
}
