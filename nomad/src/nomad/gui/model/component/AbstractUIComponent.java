package nomad.gui.model.component;

import java.awt.Component;

import nomad.gui.model.property.ComponentLocationProperty;
import nomad.gui.model.property.ComponentSizeProperty;
import nomad.gui.model.property.Property;
import nomad.gui.model.property.PropertyList;

public abstract class AbstractUIComponent {

	private PropertyList properties = new PropertyList();
	private Component component = null;
	private ComponentLocationProperty clp = null;
	private ComponentSizeProperty csp = null;

	public AbstractUIComponent() {
		;
	}
	
	/**
	 * Installs property for component size.
	 * This operation can be overwritten for custom behaviour.
	 * @param install true if property will be installed else false
	 */
	protected void installSizeProperty(boolean install) {	
		if (install) {
			csp = new ComponentSizeProperty("size", this);
			registerProperty(csp);
			csp.installComponentListener(true);
		} else if (csp!=null) {
			csp.installComponentListener(false);
			unregisterProperty(csp);
			csp = null;
		}
	}
	
	/**
	 * Installs property for component location.
	 * This operation can be overwritten for custom behaviour.
	 * @param install true if property will be installed else false
	 */
	protected void installLocationProperty(boolean install) {
		if (install) {
			clp = new ComponentLocationProperty("location", this);
			registerProperty(clp);
			clp.installComponentListener(true);
		} else if (clp!=null) {
			unregisterProperty(clp);
			clp.installComponentListener(false);
			clp=null;
		}
	}
	
	/**
	 * This method is used internally to add/remove properties.
	 * It installs properties for size and location by default.
	 * This behaviour might be changed by a different installSizeProperty
	 * and installLocationProperty implementation
	 * @param install true means installing properties, false means removing properties
	 * @see AbstractUIComponent#installLocationProperty(boolean)
	 * @see AbstractUIComponent#installSizeProperty(boolean)
	 */
	protected void installProperties(boolean install) {
		installSizeProperty(install);
		installLocationProperty(install);
	}

	/**
	 * Sets the used component. Properties will be installed/removed internally.
	 * @param component
	 */
	protected void setComponent(Component component) {
		if (this.component==component)
			return ;
		
		if (this.component!=null)
			installProperties(false /*remove*/);
		
		this.component = component;
		
		if (this.component!=null)
			installProperties(true /*install*/);
	}
	
	/**
	 * Returns the component
	 * @return the component
	 */
	public Component getComponent() {
		return component;
	}

	/**
	 * Returns a property by given displayName
	 * @param displayName name of the property
	 * @return the property
	 */
	public Property getPropertyByDisplayName(String displayName) {
		return properties.findByDisplayName(displayName);
	}
	
	/**
	 * Returns a property by given id
	 * @param id id of the property
	 * @return the property
	 */
	public Property getPropertyById(String id) {
		return properties.findById(id);
	}

	/**
	 * Registers a property
	 * @param p the registered property
	 */
	protected void registerProperty(Property p) {
		properties.add(p);
	}
	
	/**
	 * Unregisters a property
	 * @param p the unregistered property
	 */
	protected void unregisterProperty(Property p) {
		properties.remove(p);
	}

	/**
	 * Returns the property at given index
	 * @param index the index
	 * @return the property
	 */
	public Property getProperty(int index) {
		return properties.get(index);
	}

	/**
	 * Returns the number of installed properties
	 * @return the number of installed properties
	 */
	public int getPropertyCount() {
		return properties.size();
	}

	/**
	 * Returns the name of the ui component
	 * @return the name of the ui component
	 */
	public abstract String getName();


	public String toString() {
		return getName() + " ("+getClass().getName()+")";
	}
	
}
