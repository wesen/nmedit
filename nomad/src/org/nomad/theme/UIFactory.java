package org.nomad.theme;

import java.util.ArrayList;
import java.util.HashMap;

import org.nomad.patch.ModuleSection;
import org.nomad.patch.ui.ModuleSectionUI;
import org.nomad.patch.ui.ModuleUI;
import org.nomad.plugin.NomadFactory;
import org.nomad.theme.component.NomadComponent;
import org.nomad.util.graphics.ImageTracker;
import org.nomad.xml.dom.module.DModule;

public abstract class UIFactory extends NomadFactory {

	private HashMap <String,Class>
		componentClasses
		= new HashMap<String,Class>();

	private HashMap <Class, String>
		alias 
		= new HashMap<Class, String>();

	private ImageTracker imageTracker = 
		new ImageTracker(ImageTracker.IMAGE_TRACKER_DISALLOW_REPLACE);

	public UIFactory() {
	}

	public void installClass(Class componentClass) {
		installClass(componentClass, componentClass.getName());
	}
	
	public void installClass(Class componentClass, String aliasName) {
		if (!NomadComponent.class.isAssignableFrom(componentClass))
			throw new IllegalArgumentException(
				"Unsupported class "+componentClass
			);

		componentClasses.put(aliasName, componentClass);
		componentClasses.put(componentClass.getName(), componentClass);
		alias.put(componentClass, aliasName);
	}
	
	public Class getNomadComponentClass(Object key) {
		return componentClasses.get(key);
	}

	public Class[] getInstalledClasses() {
		// componentClasses.values() can contain duplicates
		ArrayList<Class> setList = new ArrayList<Class>();
		for (Class c : componentClasses.values()) {
			if (!setList.contains(c)) setList.add(c);
		}
		
		return setList.toArray(new Class[setList.size()]);
	}

	public NomadComponent newComponentInstance(String componentNameString) {
		return newComponentInstanceByClass(componentClasses.get(componentNameString));
	}
	
	public NomadComponent newComponentInstanceByClass(Class componentClass) {
		try {
			NomadComponent c = (NomadComponent) componentClass.newInstance();
			c.setNameAlias(alias.get(componentClass));
			return c;
		} catch (Throwable e) {
			if (componentClass!=null)
				e.printStackTrace();
		}
		return null;
	}
	
	public ImageTracker getImageTracker() {
		return imageTracker;
	}
	
	public abstract String getUIDescriptionFileName();

	public ModuleUI getModuleGUI(DModule info) {
		return new ModuleUI(info);
	}
	
	public ModuleSectionUI getModuleSectionUI(ModuleSection moduleSection) {
		return new ModuleSectionUI(moduleSection);
	}
	
}
