package nomad.gui.model.property;

import java.util.ArrayList;
import java.util.Iterator;

public class PropertyList {

	private ArrayList properties = new ArrayList();
	
	public void add(Property p) {
		properties.add(p);
	}
	
	public Property get(int index) {
		return (Property) properties.get(index);
	}
	
	public int size() {
		return properties.size();
	}

	public boolean remove(Property p) {
		return properties.remove(p);
	}
	
	public Property remove(int index) {
		return (Property) properties.remove(index);
	}

	public Property findByDisplayName(String name) {
		Iterator iter = properties.iterator();
		while (iter.hasNext()) {
			Property candidate = (Property) iter.next();
			if (candidate.getDisplayName().equals(name))
				return candidate;
		}
		return null;
	}

	public Property findById(String id) {
		Iterator iter = properties.iterator();
		while (iter.hasNext()) {
			Property candidate = (Property) iter.next();
			if (candidate.getId().equals(id))
				return candidate;
		}
		return null;
	}

}
