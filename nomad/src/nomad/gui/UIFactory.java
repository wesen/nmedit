package nomad.gui;

import java.util.HashMap;

import nomad.plugin.NomadFactory;

public abstract class UIFactory extends NomadFactory {
	
	private HashMap componentClasses = new HashMap();
	private String[] names = new String[] {};
	private Class DefaultLabel = null;
	private Class DefaultControl = null;
	private Class DefaultConnector = null;
	
	public void installUIClass(Class uiclass) {
		if (!BasicUI.class.isAssignableFrom(uiclass))
			throw new ClassCastException("Cannot install class "+uiclass+".");
		componentClasses.put(uiclass.getName(), uiclass);
		updateNameList();
	}
	
	public void installDefaultLabel(Class uiclass) {
		if (!BasicUI.class.isAssignableFrom(uiclass))
			throw new ClassCastException("Cannot install class "+uiclass+".");
		DefaultLabel = uiclass;
	}
	
	public void installDefaultConnector(Class uiclass) {
		if (!ConnectorUI.class.isAssignableFrom(uiclass))
			throw new ClassCastException("Cannot install class "+uiclass+".");
		DefaultConnector = uiclass;
	}
	
	public void installDefaultControl(Class uiclass) {
		if (!ControlUI.class.isAssignableFrom(uiclass))
			throw new ClassCastException("Cannot install class "+uiclass+".");
		DefaultControl = uiclass;
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
	
	public BasicUI newUIInstance(String uiClassName) {
		Class uiclass = (Class) componentClasses.get(uiClassName);
		if (uiclass != null)
			try {
				return (BasicUI) uiclass.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		return null;
	}
	
	public ControlUI newDefaultControlInstance() {
		Class uiclass = DefaultControl;
		if (uiclass != null)
			try {
				return (ControlUI) uiclass.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		return null;
	}
	
	public ConnectorUI newDefaultConnectorInstance() {
		Class uiclass = DefaultConnector;
		if (uiclass != null)
			try {
				return (ConnectorUI) uiclass.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		return null;
	}
	
	public BasicUI newDefaultLabelInstance() {
		Class uiclass = DefaultLabel;
		if (uiclass != null)
			try {
				return (BasicUI) uiclass.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		return null;
	}
}
