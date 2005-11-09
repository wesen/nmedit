package nomad.model.descriptive;

import java.awt.Image;
import java.util.ArrayList;

/**
 * A object describing the properties of a module
 * 
 * @author Christian Schneider
 * @composed 1 - n nomad.model.descriptive.DParameter
 * @composed 1 - n nomad.model.descriptive.DConnector
 */
public class DModule {
	
	/** An icon representation for this module */
	private Image icon;
	/** A list containing DParameter objects describing the parameters this module has */
	private ArrayList dparameters = new ArrayList();
	/** A list containing DConnector objects describing the connectors this module has */
	private ArrayList dconnectors = new ArrayList();
	/** The parent toolbar section this module belongs to */
	private DToolbarSection parent = null;
	/** The name of this module */
	private String name=null;
	/** A shortend version of the modules name */
	private String shortname=null;
	/** Id of this module */
	private int mdID=0;
	/** Module properties */
	private double mdCycles=0;
	/** Module properties */
	private double mdXmem=0;
	/** Module properties */
	private double mdYmem=0;
	/** Module properties */
	private double mdProgMem=0;
	/** Module properties */
	private double mdDynMem=0;
	/** Module properties */
	private double mdZeroPage=0;
	/** Module properties */
	private int mdHeight=1;

	/**
	 * Creates a new module descriptor that belongs to the given toolbar section
	 * with given name
	 * @param parent toolbar section  this module belongs to
	 * @param name name of this module
	 */
	public DModule(DToolbarSection parent, String name) {
		this.parent = parent;
		this.name = name;
		if (name==null)
			throw new NullPointerException("'name' must not be null");
	}
	
	/**
	 * Sets the icon of this module
	 * @param icon the icon of this module
	 */
	public void setIcon(Image icon) {
		this.icon = icon;
	}
	
	/**
	 * Returns the icon of this module
	 * @return the icon of this module
	 */
	public Image getIcon() {
		return icon;
	}
	
	/**
	 * Sets the parent toolbar section this module belongs to
	 * @param parent the parent toolbar section this module belongs to
	 */
	void setParent(DToolbarSection parent) {
		this.parent = parent;
	}

	/**
	 * Returns the number of parameters this module has.
	 * @return the number of parameters this module has.
	 */
	public int getParameterCount() {
		return dparameters.size();
	}
	
	/**
	 * Returns the parameter at the given index
	 * @param index the index
	 * @return the parameter
	 */
	public DParameter getParameter(int index) {
		return (DParameter) dparameters.get(index);
	}

	/**
	 * Adds a new parameter object to this module.
	 * @param d a parameter
	 */
	void addParameter(DParameter d) {
		dparameters.add(d);
	}
	
	/**
	 * Returns the number of connectors this module has
	 * @return the number of connectors this module has
	 */
	public int getConnectorCount() {
		return dconnectors.size();
	}

	/**
	 * Returns the connector at the given index
	 * @param index the index
	 * @return the connector
	 */
	public DConnector getConnector(int index) {
		return (DConnector) dconnectors.get(index);
	}
	
	/**
	 * Adds a new connector object to this module.
	 * @param c a connector
	 */
	void addConnector(DConnector c) {
		dconnectors.add(c);
	}
	
	/**
	 * Returns the toolbar section this module belongs to
	 * @return the toolbar section this module belongs to
	 */
	public DToolbarSection getParent() {
		return parent;
	}
	
	/**
	 * Returns the name of this module
	 * @return the name of this module
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns a short version of the name of this module.
	 * If the short name attribute is null than this method
	 * will return <code>getName()</code>
	 * @return the name of this module
	 */
	public String getShortName() {
		return shortname==null ? name : shortname;
	}

	/**
	 * Sets the short name attribute of this module containing
	 * a shorter version of it's name
	 * @param shortname
	 */
	void setShortName(String shortname) {
		this.shortname = shortname;
	}

	/**
	 * @return module property
	 */
	public double getCycles() {
		return mdCycles;
	}

	
	void setCycles(double mdCycles) {
		this.mdCycles = mdCycles;
	}

	/**
	 * @return module property
	 */
	public double getDynMem() {
		return mdDynMem;
	}

	void setDynMem(double mdDynMem) {
		this.mdDynMem = mdDynMem;
	}

	/**
	 * @return module property
	 */
	public int getModuleID() {
		return mdID;
	}

	public void setModuleID(int id) {
		mdID=id;
	}

	/**
	 * @return module property
	 */
	public double getProgMem() {
		return mdProgMem;
	}

	void setProgMem(double mdProgMem) {
		this.mdProgMem = mdProgMem;
	}

	/**
	 * @return the default module height
	 */
	public int getHeight() {
		return mdHeight;
	}

	void setHeight(int height) {
		this.mdHeight = height;
	}

	/**
	 * @return module property
	 */
	public double getXmem() {
		return mdXmem;
	}

	void setXmem(double mdXmem) {
		this.mdXmem = mdXmem;
	}

	/**
	 * @return module property
	 */
	public double getYmem() {
		return mdYmem;
	}

	void setYmem(double mdYmem) {
		this.mdYmem = mdYmem;
	}

	/**
	 * @return module property
	 */
	public double getZeroPage() {
		return mdZeroPage;
	}

	void setZeroPage(double mdZeroPage) {
		this.mdZeroPage = mdZeroPage;
	}

	/**
	 * Returns a unique key for this module
	 * @return a unique key for this module
	 */
	public String getKey() {
		return DModule.getKeyFromId(mdID);
	}
	
	public static String getKeyFromId(int id) {
		return Integer.toString(id);
	}
	
	/**
	 * Returns a parameter by it's id
	 * @param paramID the if for the searched parameter object
	 * @return the parameter or null if the parameter does not exist 
	 */
	public DParameter getParameterById(int paramID) {
		DParameter p = null;
		
		// look if id and index are the same. This should be true in most cases
		if ( 	(paramID>=0)
			&&	(paramID<getParameterCount())
			&& (p = (DParameter) getParameter(paramID)).getId()==paramID)
			return p;

		// not found then we have to check all candidates
		for (int i=0;i<getParameterCount();i++) {
			if ((p = getParameter(i)).getId()==paramID)
				return p;
		}

		return null; // bad luck, not found
	}
	
	/**
	 * Returns a connector by it's id. You must specify if the id belongs to an
	 * input connector or to an output connector.
	 * 
	 * @param connectorID the id of the connector
	 * @param isInput if the searched connector is an input connector
	 * @return the connector or null if the connector does not exist
	 */
	public DConnector getConnectorById(int connectorID, boolean isInput) {
		DConnector c = null;
		
		// look if id and index are the same. This should be true in most cases
		if ( 	(connectorID>=0)
			&&	(connectorID<getConnectorCount())
			&& (c = (DConnector) getConnector(connectorID)).getId()==connectorID)
			return c;

		// not found then we have to check all candidates
		for (int i=0;i<getConnectorCount();i++) {
			if ((c = getConnector(i)).getId()==connectorID) {
				if (isInput && c.getType()==DConnector.CONNECTOR_TYPE_INPUT)
					return c;
				else if (!isInput && c.getType()==DConnector.CONNECTOR_TYPE_OUTPUT)
					return c;
			}
		}
		
		return null; // bad luck, not found
	}
	
}
