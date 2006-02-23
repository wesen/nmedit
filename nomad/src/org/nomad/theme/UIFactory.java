package org.nomad.theme;

import java.util.ArrayList;
import java.util.HashMap;

import org.nomad.patch.ModuleSection;
import org.nomad.patch.ui.ModuleSectionUI;
import org.nomad.patch.ui.ModuleUI;
import org.nomad.plugin.NomadFactory;
import org.nomad.theme.component.NomadComponent;
import org.nomad.theme.property.Property;
import org.nomad.theme.property.PropertySet;
import org.nomad.util.graphics.ImageTracker;
import org.nomad.xml.dom.module.DModule;

public abstract class UIFactory extends NomadFactory {

	private HashMap <String,Class<? extends NomadComponent>> 
		componentClasses = new HashMap<String,Class<? extends NomadComponent>>();

	private HashMap <Class<? extends NomadComponent>, String>
		alias = new HashMap<Class<? extends NomadComponent>, String>();

	private ImageTracker imageTracker = 
		new ImageTracker(ImageTracker.IMAGE_TRACKER_DISALLOW_REPLACE);

	private HashMap<Class<? extends NomadComponent>, Boolean> decorationMap 
		= new HashMap<Class<? extends NomadComponent>, Boolean>();
	
	private HashMap<Class<? extends NomadComponent>, PropertySet>
		properties = new HashMap<Class<? extends NomadComponent>, PropertySet>(); 
	
	public UIFactory() { }

	public void installClass(Class<? extends NomadComponent> componentClass, String aliasName) {
		NomadComponent c = newComponentInstanceByClass(componentClass);
		
		if (!c.hasDynamicOverlay())
			decorationMap.put(componentClass, Boolean.TRUE);
		
		PropertySet set = new PropertySet();
		c.registerProperties(set);
		properties.put(componentClass, set);
		
		componentClasses.put(aliasName, componentClass);
		componentClasses.put(componentClass.getName(), componentClass);
		alias.put(componentClass, aliasName);
	}
	
	public PropertySet getProperties(NomadComponent component) {
		PropertySet set = properties.get(component.getClass());
		for (Property p : set)
			p.setComponent(component);
		
		return set;
	}
	
	public boolean isDecoration(Class<? extends NomadComponent> clazz) {
		return decorationMap.containsKey(clazz);
	}
	
	public Class<? extends NomadComponent> getNomadComponentClass(Object key) {
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
	
	public NomadComponent newComponentInstanceByClass(Class<? extends NomadComponent> componentClass) {
		try {
			NomadComponent c = componentClass.newInstance();
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
