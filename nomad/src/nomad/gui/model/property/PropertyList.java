package nomad.gui.model.property;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A list of properties
 * @author Christian Schneider
 */
public class PropertyList {

	/** A list containing {@link Property} objects */
	private ArrayList properties = new ArrayList();
	
	/**
	 * Adds an property to the list
	 * @param p the property
	 */
	public void add(Property p) {
		properties.add(p);
	}
	
	/**
	 * Returns a property at given index
	 * @param index the index
	 * @return the property
	 */
	public Property get(int index) {
		return (Property) properties.get(index);
	}
	
	/**
	 * Returns the number of properties available in the list
	 * @return the number of properties available in the list
	 */
	public int size() {
		return properties.size();
	}

	/**
	 * Removes a property from the list and returns
	 * @param p the property
	 * @return true if the property was in the list
	 */
	public boolean remove(Property p) {
		return properties.remove(p);
	}
	
	/**
	 * Removes a property at given index
	 * @param index the index
	 * @return the property that was removed
	 */
	public Property remove(int index) {
		return (Property) properties.remove(index);
	}

	/**
	 * Searches the properties and returns the first with the same name
	 * @param name the name of the searched property
	 * @return the property or null if no property has the name
	 */
	public Property findByDisplayName(String name) {
		Iterator iter = properties.iterator();
		while (iter.hasNext()) {
			Property candidate = (Property) iter.next();
			if (candidate.getDisplayName().equals(name))
				return candidate;
		}
		return null;
	}

	/**
	 * Returns a property with same id
	 * @param id the id
	 * @return the property
	 */
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
