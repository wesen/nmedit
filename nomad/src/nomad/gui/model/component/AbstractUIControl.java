package nomad.gui.model.component;

import java.util.ArrayList;

import nomad.gui.model.UIFactory;

/**
 * This is the base class for any user interface component that manipulates or displays
 * one or more module parameters. The user interface uses {@link AbstractControlPort} as
 * link between each control and the parameter. 
 * 
 * @author Christian Schneider
 * @see AbstractControlPort
 * @composed 1 - n nomad.gui.model.component.AbstractControlPort
 */
public abstract class AbstractUIControl extends AbstractUIComponent {

	/** The list containing all ports */
	private ArrayList portList = new ArrayList();

	/**
	 * Creates a new parameter controlling or displaying user interface
	 * @param factory the factory that has created this instance
	 */
	public AbstractUIControl(UIFactory factory) {
		super(factory);
	}
	
	protected void installProperties(boolean install) {
		if (portList.size()==0)
			registerPorts();
		super.installProperties(install);
		for (int i=0;i<getControlPortCount();i++)
			registerPortProperties(getControlPort(i), install);
	}
	
	/**
	 * Override this method to register available all {@link AbstractControlPort} instances
	 */
	protected abstract void registerPorts();
	
	/**
	 * Override this method to install/uninstall the properties for the port.
	 * 
	 * @param port
	 * @param install
	 */
	protected void registerPortProperties(AbstractControlPort port, boolean install) {
		if (install)
			registerProperty(port.getParamPortProperty());
		else
			unregisterProperty(port.getParamPortProperty());
	}

	/**
	 * Returns the control port at given index
	 * @param index the index
	 * @return the port
	 */
	public AbstractControlPort getControlPort(int index) {
		return (AbstractControlPort) portList.get(index);
	}
	
	/**
	 * Returns the number of ports this component has
	 * @return the number of ports this component has
	 */
	public int getControlPortCount() {
		return portList.size();
	}

	/**
	 * Adds an control port to the list
	 * @param controlPort the port
	 */
	protected void registerControlPort(AbstractControlPort controlPort) {
		portList.add(controlPort);
		controlPort.setPortIndex(portList.size()-1);
	}
	
}
