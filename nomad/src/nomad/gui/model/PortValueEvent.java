package nomad.gui.model;

import nomad.gui.model.component.AbstractControlPort;

public class PortValueEvent extends Object {

	private AbstractControlPort port = null;
	
	public PortValueEvent(AbstractControlPort port) {
		this.port = port;
	}
	
	public AbstractControlPort getSource() {
		return port;
	}
	
}
