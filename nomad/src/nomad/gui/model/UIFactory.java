package nomad.gui.model;

import java.util.HashMap;

import nomad.gui.model.component.AbstractConnectorUI;
import nomad.gui.model.component.AbstractUIComponent;
import nomad.gui.model.component.AbstractUIControl;
import nomad.misc.ImageTracker;
import nomad.plugin.NomadFactory;

public abstract class UIFactory extends NomadFactory {
	
	private HashMap componentClasses = new HashMap();
	private String[] names = new String[] {};
	private Class DefaultLabel = null;
	private Class DefaultControl = null;
	private Class DefaultOptionControl = null;
	private Class DefaultConnector = null;
	private ImageTracker imageTracker = new ImageTracker();
	
	public void installUIClass(Class uiclass) {
		if (!AbstractUIComponent.class.isAssignableFrom(uiclass))
			throw new ClassCastException("Cannot install class "+uiclass+".");
		componentClasses.put(uiclass.getName(), uiclass);
		updateNameList();
	}
	
	public void installDefaultLabel(Class uiclass) {
		if (!AbstractUIComponent.class.isAssignableFrom(uiclass))
			throw new ClassCastException("Cannot install class "+uiclass+".");
		DefaultLabel = uiclass;
	}
	
	public void installDefaultConnector(Class uiclass) {
		if (!AbstractConnectorUI.class.isAssignableFrom(uiclass))
			throw new ClassCastException("Cannot install class "+uiclass+".");
		DefaultConnector = uiclass;
	}
	
	public void installDefaultControl(Class uiclass) {
		if (!AbstractUIControl.class.isAssignableFrom(uiclass))
			throw new ClassCastException("Cannot install class "+uiclass+".");
		DefaultControl = uiclass;
	}
	
	public void installDefaultOptionControl(Class uiclass) {
		if (!AbstractUIControl.class.isAssignableFrom(uiclass))
			throw new ClassCastException("Cannot install class "+uiclass+".");
		DefaultOptionControl = uiclass;
	}
	
	private void updateNameList() {
		Object[] objs = componentClasses.keySet().toArray();
		names = new String[objs.length];
		for (int i=0;i<objs.length;i++)
			names[i] = (String) objs[i];
	}
	
	public String[] getUIClassNames() {
		return names;
	}
	
	public AbstractUIComponent newUIInstance(String uiClassName) {
		Class uiclass = (Class) componentClasses.get(uiClassName);
		if (uiclass != null)
			try {
				return (AbstractUIComponent) uiclass.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		return null;
	}
	
	public AbstractUIControl newDefaultControlInstance() {
		Class uiclass = DefaultControl;
		if (uiclass != null)
			try {
				return (AbstractUIControl) uiclass.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		return null;
	}

	public AbstractUIControl newDefaultOptionControlInstance() {
		if (DefaultOptionControl==null)
			return newDefaultControlInstance();
		
		Class uiclass = DefaultOptionControl;
		if (uiclass != null)
			try {
				return (AbstractUIControl) uiclass.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		return null;
	}
	
	public AbstractConnectorUI newDefaultConnectorInstance() {
		Class uiclass = DefaultConnector;
		if (uiclass != null)
			try {
				return (AbstractConnectorUI) uiclass.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		return null;
	}
	
	public AbstractUIComponent newDefaultLabelInstance() {
		Class uiclass = DefaultLabel;
		if (uiclass != null)
			try {
				return (AbstractUIComponent) uiclass.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		return null;
	}
	
	public ImageTracker getImageTracker() {
		return imageTracker;
	}
	
	public abstract String getUIDescriptionFileName();
	
}
