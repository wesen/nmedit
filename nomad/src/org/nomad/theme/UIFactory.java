package org.nomad.theme;

import java.util.HashMap;

import org.nomad.patch.Module;
import org.nomad.patch.ModuleSection;
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
		return componentClasses.values().toArray(new Class[componentClasses.size()]);
		
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

	public ModuleGUI getModuleGUI(DModule info, Module module, ModuleSectionGUI moduleSectionGUI) {
		return new ModuleGUI(this, info, module, moduleSectionGUI);
	}
	
	public ModuleSectionGUI getModuleSectionGUI(ModuleSection moduleSection) {
		return new ModuleSectionGUI(moduleSection);
	}
	
}
