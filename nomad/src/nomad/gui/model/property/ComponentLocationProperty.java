package nomad.gui.model.property;

import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import nomad.gui.model.component.AbstractUIComponent;

public class ComponentLocationProperty extends PointProperty {

	private MyComponentListener theListener = null;

	public ComponentLocationProperty(AbstractUIComponent uicomponent) {
		super("location", uicomponent);
	}

	public ComponentLocationProperty(String displayName, AbstractUIComponent uicomponent) {
		super(displayName, uicomponent);
	}

	protected Object getInternalValue() {
		return getComponent().getLocation();
	}
	
	public Point getLocation() {
		return (Point) getValue();
	}

	protected void setLocation(Point value) {
		setValue(value);
	}

	protected void setInternalValue(Object value) {
		getComponent().setLocation((Point)value);
	}

	public void installComponentListener(boolean install) {
		if (install)
			getComponent().addComponentListener(theListener=new MyComponentListener());
		else if (theListener!=null) {
			getComponent().removeComponentListener(theListener);
			theListener = null;
		}
	}
	
	private class MyComponentListener implements ComponentListener {
		public void componentResized(ComponentEvent event) {}
		public void componentMoved(ComponentEvent event) {
			fireChangeEvent();
		}
		public void componentShown(ComponentEvent event) {}
		public void componentHidden(ComponentEvent event) {}
	}

	public String getId() {
		return "location";
	}
}
