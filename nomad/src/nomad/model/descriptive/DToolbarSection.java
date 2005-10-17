package nomad.model.descriptive;

import java.util.Vector;

/**
 * @author Christian Schneider
 * @composed 1 - n nomad.model.descriptive.DModule
 */
public class DToolbarSection {

	private Vector dmodules = new Vector();
	private DToolbarGroup parent = null;
	
	public DToolbarSection(DToolbarGroup parent) {
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
	
	public DToolbarGroup getParent() {
		return parent;
	}

	void addModule(DModule module) {
		dmodules.add(module);
	}

}
