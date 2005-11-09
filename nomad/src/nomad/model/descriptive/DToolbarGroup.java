package nomad.model.descriptive;

import java.util.ArrayList;

/**
 * A group containing sections of modules.
 * 
 * @author Christian Schneider
 * @composed 1 - n nomad.model.descriptive.DSection
 */
public class DToolbarGroup {

	/** A list containing DToolbarSection objects */
	private ArrayList dsections = new ArrayList();
	/** The name of this group */
	private String name = null;
	/** The short version of this groups name */
	private String shortname = null;
	
	/**
	 * Creates a new toolbar group with given name. Short name will be the same as name
	 * @param name the name of this group
	 */
	public DToolbarGroup(String name) {
		this(name, null);
	}

	/**
	 * Creates a new toolbar group with given name and a given shorter version of it's name
	 * @param name the name
	 * @param shortname the short name
	 */
	public DToolbarGroup(String name, String shortname) {
		this.name = name;
		if (name==null)
			throw new NullPointerException("'name' must not be null");
		this.shortname = shortname;
	}
	
	/**
	 * Returns a toolbar section at given index
	 * @param index the index
	 * @return the section
	 */
	public DToolbarSection getSection(int index) {
		return (DToolbarSection) dsections.get(index);
	}
	
	/**
	 * Returns the number of toolbar sections this group owns
	 * @return the number of toolbar sections this group owns
	 */
	public int getSectionCount() {
		return dsections.size();
	}
	
	/**
	 * Adds a new section to this toolbar group
	 * @param s the section
	 */
	void addSection(DToolbarSection s) {
		dsections.add(s);
	}
	
	/**
	 * Returns the name of this toolbar group
	 * @return the name of this toolbar group
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the short name of this toolbar group
	 * @return the short name of this toolbar group
	 */
	public String getShortName() {
		return shortname==null ? name : shortname;
	}
	
}
