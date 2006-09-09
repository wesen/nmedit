package net.sf.nmedit.nomad.theme;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.VoiceArea;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.DModule;
import net.sf.nmedit.jtheme.ThemeConfiguration;
import net.sf.nmedit.nomad.patch.ui.ModuleSectionUI;
import net.sf.nmedit.nomad.patch.ui.ModuleUI;
import net.sf.nmedit.nomad.theme.component.NomadComponent;
import net.sf.nmedit.nomad.theme.xml.dom.ThemeNode;
import net.sf.nmedit.nomad.util.graphics.ImageTracker;


public abstract class UIFactory  {
    
    
    public ThemeConfiguration configuration = new ThemeConfiguration();

	private HashMap <String,Class<? extends NomadComponent>> 
		componentClasses = new HashMap<String,Class<? extends NomadComponent>>();

	private HashMap <Class<? extends NomadComponent>, String>
		alias = new HashMap<Class<? extends NomadComponent>, String>();

	private ImageTracker imageTracker = 
		new ImageTracker(ImageTracker.IMAGE_TRACKER_DISALLOW_REPLACE);

	public UIFactory() { }

	public void installClass(Class<? extends NomadComponent> componentClass, String aliasName) {
		NomadComponent c = newComponentInstanceByClass(componentClass);
		
		componentClasses.put(aliasName, componentClass);
		componentClasses.put(componentClass.getName(), componentClass);
		alias.put(componentClass, aliasName);
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
			return componentClass.newInstance();
		} catch (Throwable e) {
			if (componentClass!=null)
				e.printStackTrace();
		}
		return null;
	}
	
	public ImageTracker getImageTracker() {
		return imageTracker;
	}
	
    public abstract ThemeNode getThemeSetup();

    protected ThemeNode loadThemeSetup( String name )
    {
        ThemeNode dom = new ThemeNode();
        InputStream in = getClass().getResourceAsStream(name);
        ThemeNode.importDocument( dom, in );
        return dom;
    }
    
//	public abstract String getUIDescriptionFileName();

	public ModuleUI getModuleGUI(DModule info) {
		return new ModuleUI(info);
	}
	
	public ModuleSectionUI getModuleSectionUI(VoiceArea moduleSection) {
		return new ModuleSectionUI(moduleSection);
	}

	public String getAlias(Class<? extends NomadComponent> clazz) {
		String salias = alias.get(clazz);
		return salias==null ? clazz.getName() : salias;
	}
	
}
