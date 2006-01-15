package org.nomad.xml.dom.module;

import java.util.ArrayList;


/**
 * An list that contains modules belonging to the same toolbar section
 * 
 * @author Christian Schneider
 * @composed 1 - n nomad.model.descriptive.DModule
 */
public class DToolbarSection {

	/** A list containing the DModule objects */
	private ArrayList dmodules = new ArrayList();
	/** The toolbar group this section belongs to */
	private DToolbarGroup parent = null;
	
	/**
	 * Creates a new toolbar section that belongs to the given toolbar group
	 * @param parent
	 */
	public DToolbarSection(DToolbarGroup parent) {
		this.parent = parent;
		if (parent==null)
			throw new NullPointerException("'parent' must not be null");
	}

	/**
	 * Returns the number of modules that belong to this section
	 * @return the number of modules that belong to this section
	 */
	public int getModuleCount() {
		return dmodules.size();
	}
	
	/**
	 * Returns the module at given index
	 * @param index the index
	 * @return the module
	 */
	public DModule getModule(int index) {
		return (DModule) dmodules.get(index);
	}
	
	/**
	 * Returns the toolbar group this section belongs to
	 * @return the toolbar group this section belongs to
	 */
	public DToolbarGroup getParent() {
		return parent;
	}

	/**
	 * Adds a module to this section
	 * @param module the module
	 */
	void addModule(DModule module) {
		dmodules.add(module);
	}

}
