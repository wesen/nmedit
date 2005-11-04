package nomad.gui;

import java.util.ArrayList;
import java.util.Iterator;

import nomad.gui.model.component.AbstractConnectorUI;
import nomad.gui.model.component.AbstractUIComponent;
import nomad.gui.model.component.AbstractUIControl;
import nomad.misc.SequenceIterator;

public class ModuleGUIComponents {

	ArrayList controls = new ArrayList();
	ArrayList connectors = new ArrayList();
	ArrayList other = new ArrayList();
	
	public int getConnectorCount() {
		return connectors.size();
	}
	
	public int getOtherCount() {
		return other.size();
	}
	
	public int getControlCount() {
		return controls.size();
	}
	
	protected void addConnector(AbstractConnectorUI connector) {
		connectors.add(connector);
	}
	
	public AbstractConnectorUI getConnector(int index) {
		return (AbstractConnectorUI)connectors.get(index);
	}
	
	protected void addControl(AbstractUIControl control) {
		controls.add(control);
	}
	
	public AbstractUIControl getControl(int index) {
		return (AbstractUIControl)controls.get(index);
	}
	
	protected void addOtherComponent(AbstractUIComponent component) {
		other.add(component);
	}
	
	public AbstractUIComponent getOtherComponent(int index) {
		return (AbstractUIComponent) other.get(index);
	}
	
	public void add(AbstractUIComponent component) {
		if (component instanceof AbstractConnectorUI)
			addConnector((AbstractConnectorUI)component);
		else if (component instanceof AbstractUIControl)
			addControl((AbstractUIControl)component);
		else
			addOtherComponent(component);
	}
	
	public void remove(AbstractUIComponent component) {
		if (component instanceof AbstractConnectorUI)
			connectors.remove(component);
		else if (component instanceof AbstractUIControl)
			controls.remove(component);
		else
			other.remove(component);
	}
	
	public Iterator getAllComponents(){
		return new SequenceIterator(new SequenceIterator(connectors.iterator(), controls.iterator()), other.iterator());
	}
	
}
