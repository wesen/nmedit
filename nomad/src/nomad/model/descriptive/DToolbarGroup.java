package nomad.model.descriptive;

import java.util.Vector;
/**
 * @author Christian Schneider
 * @composed 1 - n nomad.model.descriptive.DSection
 */
public class DToolbarGroup {

	private Vector dsections = new Vector();
	private String name = null;
	private String shortname = null;
	
	public DToolbarGroup(String name) {
		this(name, null);
	}
	
	public DToolbarGroup(String name, String shortname) {
		this.name = name;
		if (name==null)
			throw new NullPointerException("'name' must not be null");
		this.shortname = shortname;
	}
	
	public DToolbarSection getSection(int index) {
		return (DToolbarSection) dsections.get(index);
	}
	
	public int getSectionCount() {
		return dsections.size();
	}
	
	void addSection(DToolbarSection s) {
		dsections.add(s);
	}
	
	public String getName() {
		return name;
	}
	
	public String getShortName() {
		return shortname==null ? name : shortname;
	}
	
}
