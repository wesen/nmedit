package nomad.model.descriptive;

import java.awt.Image;
import java.util.ArrayList;

/**
 * @author Christian Schneider
 * @composed 1 - n nomad.model.descriptive.DParameter
 * @composed 1 - n nomad.model.descriptive.DConnector
 *
 */
public class DModule {
	
	private Image icon;
	private ArrayList dparameters = new ArrayList();
	private ArrayList dconnectors = new ArrayList();
	private DToolbarSection parent = null;
	
	private String name=null;
	private String shortname=null;

	private int mdID=0;
	private double mdCycles=0;
	private double mdXmem=0;
	private double mdYmem=0;
	private double mdProgMem=0;
	private double mdDynMem=0;
	private double mdZeroPage=0;
	private int mdHeight=1;

	public DModule(DToolbarSection parent, String name) {
		this.parent = parent;
		this.name = name;
		if (name==null)
			throw new NullPointerException("'name' must not be null");
	}
	
	public void setIcon(Image icon) {
		this.icon = icon;
	}
	
	public Image getIcon() {
		return icon;
	}
	
	void setParent(DToolbarSection parent) {
		this.parent = parent;
	}

	public int getParameterCount() {
		return dparameters.size();
	}
	
	public DParameter getParameter(int index) {
		return (DParameter) dparameters.get(index);
	}

	void addParameter(DParameter d) {
		dparameters.add(d);
	}
	
	public int getConnectorCount() {
		return dconnectors.size();
	}
	
	public DConnector getConnector(int index) {
		return (DConnector) dconnectors.get(index);
	}
	
	void addConnector(DConnector c) {
		dconnectors.add(c);
	}
	
	public DToolbarSection getParent() {
		return parent;
	}
	
	public String getName() {
		return name;
	}
	
	public String getShortName() {
		return shortname==null ? name : shortname;
	}

	void setShortName(String shortname) {
		this.shortname = shortname;
	}

	public double getCycles() {
		return mdCycles;
	}

	void setCycles(double mdCycles) {
		this.mdCycles = mdCycles;
	}

	public double getDynMem() {
		return mdDynMem;
	}

	void setDynMem(double mdDynMem) {
		this.mdDynMem = mdDynMem;
	}

	public int getModuleID() {
		return mdID;
	}

	public void setModuleID(int id) {
		mdID=id;
	}

	public double getProgMem() {
		return mdProgMem;
	}

	void setProgMem(double mdProgMem) {
		this.mdProgMem = mdProgMem;
	}

	public int getHeight() {
		return mdHeight;
	}

	void setHeight(int height) {
		this.mdHeight = height;
	}

	public double getXmem() {
		return mdXmem;
	}

	void setXmem(double mdXmem) {
		this.mdXmem = mdXmem;
	}

	public double getYmem() {
		return mdYmem;
	}

	void setYmem(double mdYmem) {
		this.mdYmem = mdYmem;
	}

	public double getZeroPage() {
		return mdZeroPage;
	}

	void setZeroPage(double mdZeroPage) {
		this.mdZeroPage = mdZeroPage;
	}

	public String getKey() {
		return DModule.getKeyFromId(mdID);
	}
	
	public static String getKeyFromId(int id) {
		return Integer.toString(id);
	}
	
	public DParameter getParameterById(int paramID) {
		for (int i=0;i<getParameterCount();i++) {
			DParameter p = getParameter(i);
			if (p.getId()==paramID)
				return p;
		}
		return null;
	}
	
	public DConnector getConnectorById(int connectorID, boolean isInput) {
		for (int i=0;i<getConnectorCount();i++) {
			DConnector c = getConnector(i);
			if (c.getId()==connectorID) {
				if (isInput && c.getType()==DConnector.CONNECTOR_TYPE_INPUT)
					return c;
				else if (!isInput && c.getType()==DConnector.CONNECTOR_TYPE_OUTPUT)
					return c;
			}
		}
		return null;
	}
	
}
