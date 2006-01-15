package org.nomad.theme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


import org.nomad.patch.Module;
import org.nomad.patch.ModuleSection;
import org.nomad.plugin.NomadFactory;
import org.nomad.theme.component.NomadComponent;
import org.nomad.util.misc.ImageTracker;

public abstract class UIFactory extends NomadFactory {
	
	private HashMap componentClasses = new HashMap();
	private String[] names = new String[] {};
	private ImageTracker imageTracker = 
		new ImageTracker(ImageTracker.IMAGE_TRACKER_DISALLOW_REPLACE);
	
	private boolean inEditMode = false;
	
	public UIFactory() {
	}
	
	public void setEditing(boolean enable) {
		this.inEditMode = enable;
	}
	
	public boolean isEditing() {
		return inEditMode;
	}
	
	public void installClass(Class uiClass, Object key) {
		if (!NomadComponent.class.isAssignableFrom(uiClass))
			throw new ClassCastException("Install Class: Incompatible class "+uiClass+".");
		
		componentClasses.put(key, uiClass);
		
		if (!componentClasses.containsKey(uiClass))
			componentClasses.put(uiClass, uiClass);
		
		String name = uiClass.getName();

		if (!componentClasses.containsKey(name))
			componentClasses.put(name, uiClass);
		
		updateNameList();
	}

	public void installClass(Class uiClass) {
		installClass(uiClass, uiClass);
	}
	
	private void updateNameList() {
		Object[] objs = componentClasses.values().toArray();
		names = new String[objs.length];
		for (int i=0;i<objs.length;i++) {
			names[i] = ((Class) objs[i]).getName();
			//System.out.println(names[i]);
		}
	}
	
	public String[] getUIClassNames() {
		return names;
	}
	
	public Class getNomadComponentClass(Object key) {
		return (Class) componentClasses.get(key);
	}
	
	public Class[] getInstalledClasses() {
		ArrayList items = new ArrayList();
		for (Iterator iter = componentClasses.values().iterator();iter.hasNext();) {
			Object c = iter.next();
			if (!items.contains(c)) items.add(c);
		}
		
		Class[] classList = new Class[items.size()];
		for (int i=items.size()-1;i>=0;i--)
			classList[i] = (Class) items.get(i);
		
		return classList;
	}

	public NomadComponent newComponentInstance(String componentNameString) {
		NomadComponent c = newComponentInstanceByClass((Class)componentClasses.get(componentNameString));
		c.setNameAlias(componentNameString);
		return c;
	}
	
	public NomadComponent newComponentInstanceByClass(Class componentClass) {
		
		if (componentClass==null)
			return null;
		
		try {
			return (NomadComponent) componentClass.newInstance();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ImageTracker getImageTracker() {
		return imageTracker;
	}
	
	public abstract String getUIDescriptionFileName();

	public ModuleGUI getModuleGUI(Module module, ModuleSectionGUI moduleSectionGUI) {
		return new ModuleGUI(this, module, moduleSectionGUI);
	}
	
	public ModuleSectionGUI getModuleSectionGUI(ModuleSection moduleSection) {
		return new ModuleSectionGUI(moduleSection);
	}
	
}
