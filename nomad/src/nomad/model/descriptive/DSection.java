package nomad.model.descriptive;

import java.util.Vector;

/**
 * @author Christian Schneider
 * @composed 1 - n nomad.model.descriptive.DModule
 */
public class DSection {

	private Vector dmodules = new Vector();
	private DGroup parent = null;
	
	public DSection(DGroup parent) {
		this.parent = parent;
		if (parent==null)
			throw new NullPointerException("'parent' must not be null");
	}

	public int getModuleCount() {
		return dmodules.size();
	}
	
	public DModule getModule(int index) {
		return (DModule) dmodules.get(index);
	}
	
	public DGroup getParent() {
		return parent;
	}

	void addModule(DModule module) {
		dmodules.add(module);
	}

}
