package nomad.gui.model.property;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import nomad.gui.model.component.AbstractUIComponent;

public class ComponentSizeProperty extends PointProperty {

	private MyComponentListener theListener = null;

	public ComponentSizeProperty(AbstractUIComponent uicomponent) {
		super("size", uicomponent);
	}

	public ComponentSizeProperty(String displayName, AbstractUIComponent uicomponent) {
		super(displayName, uicomponent);
	}

	protected Object getInternalValue() {
		return getPointFromDimension(getComponent().getSize());
	}

	protected void setInternalValue(Object value) {
		getComponent().setSize(getDimensionFromPoint((Point)value));
	}

	public Dimension getSize() {
		return (Dimension) getValue();
	}

	protected void setSize(Dimension value) {
		setValue(value);
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
		public void componentResized(ComponentEvent event) {
			fireChangeEvent();
		}
		public void componentMoved(ComponentEvent event) {}
		public void componentShown(ComponentEvent event) {}
		public void componentHidden(ComponentEvent event) {}
	}

	public String getId() {
		return "component.size";
	}
}
