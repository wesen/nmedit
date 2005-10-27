package nomad.gui.model.component;

import java.util.ArrayList;

public abstract class AbstractUIControl extends AbstractUIComponent {

	private ArrayList portList = new ArrayList();
	
	public AbstractUIControl() {
		;
	}
	
	protected void installProperties(boolean install) {
		if (portList.size()==0)
			registerPorts();
		super.installProperties(install);
		for (int i=0;i<getControlPortCount();i++)
			registerPortProperties(getControlPort(i), install);
	}
	
	protected abstract void registerPorts();
	
	protected void registerPortProperties(AbstractControlPort port, boolean install) {
		if (install)
			registerProperty(port.getParamPortProperty());
		else
			unregisterProperty(port.getParamPortProperty());
	}

	public AbstractControlPort getControlPort(int index) {
		return (AbstractControlPort) portList.get(index);
	}
	
	public int getControlPortCount() {
		return portList.size();
	}
	
	protected void registerControlPort(AbstractControlPort controlPort) {
		portList.add(controlPort);
		controlPort.setPortIndex(portList.size()-1);
	}
	
}
