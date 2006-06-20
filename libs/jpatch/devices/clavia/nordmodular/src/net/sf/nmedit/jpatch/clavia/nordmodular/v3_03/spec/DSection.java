package net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec;

import java.util.ArrayList;


/**
 * An list that contains modules belonging to the same toolbar section
 * 
 * @author Christian Schneider
 * @composed 1 - n nomad.model.descriptive.DModule
 */
public class DSection {

	/** A list containing the DModule objects */
	private ArrayList<DModule> dmodules = new ArrayList<DModule>();
	/** The toolbar group this section belongs to */
	private DGroup parent = null;
	
	/**
	 * Creates a new toolbar section that belongs to the given toolbar group
	 * @param parent
	 */
	public DSection(DGroup parent) {
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
		return dmodules.get(index);
	}
	
	/**
	 * Returns the toolbar group this section belongs to
	 * @return the toolbar group this section belongs to
	 */
	public DGroup getParent() {
		return parent;
	}

	/**
	 * Adds a module to this section
	 * @param module the module
	 */
	public void addModule(DModule module) {
		dmodules.add(module);
	}

}
