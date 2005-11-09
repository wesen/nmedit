package nomad.gui.model.component;

import java.awt.Component;

import nomad.gui.model.UIFactory;
import nomad.gui.model.property.ComponentLocationProperty;
import nomad.gui.model.property.ComponentSizeProperty;
import nomad.gui.model.property.Property;
import nomad.gui.model.property.PropertyList;

/**
 * A class for setting up a user interface component that will be placed on the module. 
 * 
 * <p>
 * <ul>
 * <li>For setting up a connector component use AbstractConnectorUI that is derived from this class.</li>
 * <li>For setting up a parameter controlling or displaying component use AbstractUIControl that is derived from this class.</li>
 * <li>For any other decorating component use this class as base.</li>
 * </ul>
 * </p>
 * 
 * This class uses {@link nomad.gui.model.property.Property} to provide access to the components
 * properties by the module editor and finally to load and save their values to the
 * xml file so that they can have individual values.
 * 
 * @author Christian Schneider
 */
public abstract class AbstractUIComponent {

	/** The properties */
	private PropertyList properties = new PropertyList();
	/** The ui component */
	private Component component = null;
	/** The location property */
	private ComponentLocationProperty clp = null;
	/** The size property */
	private ComponentSizeProperty csp = null;
	/** true if the component does change it's behaviour */
	private boolean isDecorating = false;
	/** the ui factory that has created this instance */
	private UIFactory factory = null;

	/**
	 * Creates a new user interface component.
	 * @param factory the factory that has created this component
	 */
	public AbstractUIComponent(UIFactory factory) {
		this.factory = factory;
	}

	/**
	 * Returns the factory that has created this component
	 * @return the factory that has created this component
	 */
	public UIFactory getFactory() {
		return factory;
	}

	/**
	 * Returns true if this component can change it's look anytime. 
	 * @return true if this component can change it's look anytime.
	 */
	public boolean isDecoratingComponent() {
		return isDecorating;
	}
	
	/**
	 * Sets the decorating property
	 * @param enable true if component is a decorating component and does not change it's behaviour
	 * @see #isDecoratingComponent()
	 */
	public void setAsDecoratingDocument(boolean enable) {
		isDecorating = enable;
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
	 * Returns the name of the ui component.
	 * If this component is a label the method could return 'label'
	 * @return the name of the ui component
	 */
	public abstract String getName();


	public String toString() {
		return getName() + " ("+getClass().getName()+")";
	}
	
}
