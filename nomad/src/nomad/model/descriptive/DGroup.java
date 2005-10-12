package nomad.model.descriptive;

import java.util.Vector;

public class DGroup {

	private Vector dsections = new Vector();
	private String name = null;
	private String shortname = null;
	
	public DGroup(String name) {
		this(name, null);
	}
	
	public DGroup(String name, String shortname) {
		this.name = name;
		if (name==null)
			throw new NullPointerException("'name' must not be null");
		this.shortname = shortname;
	}
	
	public DSection getSection(int index) {
		return (DSection) dsections.get(index);
	}
	
	public int getSectionCount() {
		return dsections.size();
	}
	
	void addSection(DSection s) {
		dsections.add(s);
	}
	
	public String getName() {
		return name;
	}
	
	public String getShortName() {
		return shortname==null ? name : shortname;
	}
	
}
