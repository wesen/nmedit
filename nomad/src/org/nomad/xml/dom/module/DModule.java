package org.nomad.xml.dom.module;

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
	private ArrayList dparameters = null;
	/** A list containing DCustom objects */
	private ArrayList dcustoms = null;
	/** A list containing DConnector objects describing the connectors this module has */
	private ArrayList dconnectors = null;
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
	/** Module properties */
	private boolean cvaOnly=false;
	/** Module properties */
	private int limit=0;

	/**
	 * Creates a new module descriptor that belongs to the given toolbar section
	 * with given name
	 * @param parent toolbar section  this module belongs to
	 * @param name name of this module
	 */
	public DModule(DToolbarSection parent, String name) {
		this(parent);
		this.name = name;
		setName(name);
	}
	
	public DModule(DToolbarSection parent) {
		this.parent = parent;
	}
	
	public void setName(String name) {
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
	public void setParent(DToolbarSection parent) {
		this.parent = parent;
	}

	/**
	 * Returns the number of parameters this module has.
	 * @return the number of parameters this module has.
	 */
	public int getParameterCount() {
		return dparameters == null ? 0 : dparameters.size();
	}
	
	/**
	 * Returns the number of 'custom' parameters this module has.
	 * @return the number of 'custom' parameters this module has.
	 */
	public int getCustomParamCount() {
		return dcustoms == null ? 0 : dcustoms.size();
	}
	
	/**
	 * Returns the parameter at the given index
	 * @param index the index
	 * @return the parameter
	 */
	public DParameter getParameter(int index) {
		if (dparameters == null) return null;
		return (DParameter) dparameters.get(index);
	}
	
	/**
	 * Returns the 'custom' parameter at the given index
	 * @param index the index
	 * @return the parameter
	 */
	public DCustom getCustomParam(int index) {
		if (dcustoms == null) return null;
		return (DCustom) dcustoms.get(index);
	}

	/**
	 * Adds a new 'custom' parameter object to this module.
	 * @param d a parameter
	 */
	public void addCustomParam(DCustom d) {
		if (dcustoms == null) dcustoms = new ArrayList();
		dcustoms.add(d);
	}
	
	/**
	 * Adds a new parameter object to this module.
	 * @param d a parameter
	 */
	public void addParameter(DParameter d) {
		if (dparameters == null) dparameters = new ArrayList();
		dparameters.add(d);
	}
	
	/**
	 * Returns the number of connectors this module has
	 * @return the number of connectors this module has
	 */
	public int getConnectorCount() {
		return dconnectors == null ? 0 : dconnectors.size();
	}

	/**
	 * Returns the connector at the given index
	 * @param index the index
	 * @return the connector
	 */
	public DConnector getConnector(int index) {
		if (dconnectors == null) return null;
		return (DConnector) dconnectors.get(index);
	}
	
	/**
	 * Adds a new connector object to this module.
	 * @param c a connector
	 */
	public void addConnector(DConnector c) {
		if (dconnectors == null) dconnectors = new ArrayList();
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
	public void setShortName(String shortname) {
		this.shortname = shortname;
	}

	/**
	 * @return module property
	 */
	public double getCycles() {
		return mdCycles;
	}

	
	public void setCycles(double mdCycles) {
		this.mdCycles = mdCycles;
	}

	/**
	 * @return module property
	 */
	public double getDynMem() {
		return mdDynMem;
	}

	public void setDynMem(double mdDynMem) {
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

	public void setProgMem(double mdProgMem) {
		this.mdProgMem = mdProgMem;
	}

	/**
	 * @return the default module height
	 */
	public int getHeight() {
		return mdHeight;
	}

	public void setHeight(int height) {
		this.mdHeight = height;
	}

	/**
	 * @return module property
	 */
	public double getXmem() {
		return mdXmem;
	}

	public void setXmem(double mdXmem) {
		this.mdXmem = mdXmem;
	}

	/**
	 * @return module property
	 */
	public double getYmem() {
		return mdYmem;
	}

	public void setYmem(double mdYmem) {
		this.mdYmem = mdYmem;
	}

	/**
	 * @return module property
	 */
	public double getZeroPage() {
		return mdZeroPage;
	}

	public void setZeroPage(double mdZeroPage) {
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
	
	public String toString() {
		return "DModule[id="+getModuleID()+"]";
	}
	
	/**
	 * Returns a parameter by it's id
	 * @param paramID the if for the searched parameter object
	 * @return the parameter or null if the parameter does not exist 
	 */
	public DParameter getParameterById(int paramID) {
		if (dparameters==null) return null;
		
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
	 * Returns a 'custom' parameter by it's id
	 * @param paramID the if for the searched parameter object
	 * @return the parameter or null if the parameter does not exist 
	 */
	public DCustom getCustomParamById(int paramID) {
		if (dcustoms==null) return null;
		DCustom p = null;
		
		// look if id and index are the same. This should be true in most cases
		if ( 	(paramID>=0)
			&&	(paramID<getCustomParamCount())
			&& (p = (DCustom) getCustomParam(paramID)).getId()==paramID)
			return p;

		// not found then we have to check all candidates
		for (int i=0;i<getCustomParamCount();i++) {
			if ((p = getCustomParam(i)).getId()==paramID)
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
		if (dconnectors==null) return null;
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

	public boolean isCvaOnly() {
		return cvaOnly;
	}

	public void setCvaOnly(boolean cvaOnly) {
		this.cvaOnly = cvaOnly;
	}

	public boolean isLimited() {
		return getLimit()>0;
	}
	
	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
	
}
