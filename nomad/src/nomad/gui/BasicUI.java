package nomad.gui;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nomad.gui.property.PointProperty;
import nomad.gui.property.PropertyMap;

public abstract class BasicUI {

	private PropertyMap properties = new PropertyMap();
	private PointProperty plocation = new PointProperty();
	private PointProperty psize = new PointProperty();
	private Component theComponent = null;
	private TheComponentListener theComponentListener = 
		new TheComponentListener();
	private PropertyLocationChanged propertyLocationChanged =
		new PropertyLocationChanged();
	private PropertySizeChanged propertySizeChanged =
		new PropertySizeChanged();

	public BasicUI() {
		plocation.addChangeListener(propertyLocationChanged);
		psize.addChangeListener(propertySizeChanged);
		installLocationProperty(true);
	}
	
	public PropertyMap getProperties() {
		return properties;
	}

	public abstract String getName() ;
	
	protected void setComponent(Component c) {
		if (this.theComponent!=null)
			// remove listener from previous component
			this.theComponent.removeComponentListener(theComponentListener);
		
		this.theComponent = c;
		// load initial value
		plocation.setValue(theComponent.getLocation(), propertyLocationChanged);
		psize.setValue(theComponent.getWidth(), theComponent.getHeight(), propertySizeChanged);
		
		if (this.theComponent!=null)
			// add listener to current component
			this.theComponent.addComponentListener(theComponentListener);
	}
	
	public Component getComponent() {
		return theComponent;
	}
	
	protected void installLocationProperty(boolean install) {
		if (install) {
			properties.putProperty("location", plocation);
		} else
			properties.removeProperty("location");
	}
	
	protected void installSizeProperty(boolean install) {
		if (install) {
			properties.putProperty("size", psize);
		} else
			properties.removeProperty("size");
	}
	
	private class PropertyLocationChanged implements ChangeListener {
		public void stateChanged(ChangeEvent event) {
			getComponent().setLocation(plocation.getPoint());
		}
	}
	
	private class PropertySizeChanged implements ChangeListener {
		public void stateChanged(ChangeEvent event) {
			Point p = plocation.getPoint();
			getComponent().setSize(p.x, p.y);
		}
	}
	
	private class TheComponentListener implements ComponentListener {
		public void componentMoved(ComponentEvent event) {
			PointProperty p = (PointProperty) properties.getProperty("location");
			if (p!=null) // if installed
				p.setValue(
						event.getComponent().getLocation(), 
						propertyLocationChanged // will not be notified again,
						// else that would cause a deadlock
				);
		}
		
		public void componentResized(ComponentEvent event) {
			PointProperty p = (PointProperty) properties.getProperty("size");
			if (p!=null) { // if installed
				p.setValue(
					event.getComponent().getWidth(),
					event.getComponent().getHeight(), 
					propertySizeChanged // will not be notified again,
					// else that would cause a deadlock
				);	
			}
		}

		public void componentShown(ComponentEvent event) {
			//
		}

		public void componentHidden(ComponentEvent event) {
			// 
		}
		
	}
	
	public String toString() {
		return getName() + " ("+getClass().getName()+")";
	}
	
}
