package nomad.gui.model;

import java.awt.Graphics;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;

import nomad.gui.ModuleGUI;
import nomad.gui.ModuleSectionGUI;
import nomad.gui.model.component.AbstractConnectorUI;
import nomad.gui.model.component.AbstractUIComponent;
import nomad.gui.model.component.AbstractUIControl;
import nomad.misc.ImageTracker;
import nomad.patch.Module;
import nomad.patch.ModuleSection;
import nomad.plugin.NomadFactory;
import nomad.plugin.cache.XMLUICacheWriter;

public abstract class UIFactory extends NomadFactory {
	
	private HashMap componentClasses = new HashMap();
	private String[] names = new String[] {};
	private Class DefaultLabel = null;
	private Class DefaultControl = null;
	private Class DefaultOptionControl = null;
	private Class DefaultConnector = null;
	private ImageTracker imageTracker = 
		new ImageTracker(ImageTracker.IMAGE_TRACKER_DISALLOW_REPLACE);
	
	private boolean inEditMode = false;
	
	public UIFactory() {
		// create the ui cache file
		createUICache();
	}
	
	public void setEditing(boolean enable) {
		this.inEditMode = enable;
	}
	
	public boolean isEditing() {
		return inEditMode;
	}
	
	private void createUICache() {
		String filename = getUIDescriptionFileName();
		String cacheFile= filename;
		try {
			if (cacheFile.endsWith("xml")) {
				cacheFile = cacheFile.substring(0, cacheFile.length()-3)+"cache";
			}
			
			File cache = new File(cacheFile);
			
			if (cache.exists()) {
				Date dxml  = new Date((new File(filename)).lastModified());
				Date dcache= new Date(cache.lastModified());
				if (dcache.after(dxml))
					return ; // cache is newer, nothing to do
			}
			
			// cache file does not exist or is out of date
			XMLUICacheWriter.writeCache(filename, cacheFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
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
	
	private Object createUIInstanceFor(Class uiclass)  {
		try {
			return uiclass.getConstructors()[0].newInstance(new Object[]{this});
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.getTargetException().printStackTrace();
			e.printStackTrace();
		}
		return null;
	}
	
	public AbstractUIComponent newUIInstance(String uiClassName) {
		Class uiclass = (Class) componentClasses.get(uiClassName);
		if (uiclass != null)
			return (AbstractUIComponent) createUIInstanceFor(uiclass);
		return null;
	}
	
	public AbstractUIControl newDefaultControlInstance() {
		Class uiclass = DefaultControl;
		if (uiclass != null)
			return (AbstractUIControl) createUIInstanceFor(uiclass);
		return null;
	}

	public AbstractUIControl newDefaultOptionControlInstance() {
		if (DefaultOptionControl==null)
			return newDefaultControlInstance();
		
		Class uiclass = DefaultOptionControl;
		if (uiclass != null)
			return (AbstractUIControl) createUIInstanceFor(uiclass);
		return null;
	}
	
	public AbstractConnectorUI newDefaultConnectorInstance() {
		Class uiclass = DefaultConnector;
		if (uiclass != null)
			return (AbstractConnectorUI) createUIInstanceFor(uiclass);
		return null;
	}
	
	public AbstractUIComponent newDefaultLabelInstance() {
		Class uiclass = DefaultLabel;
		if (uiclass != null)
			return (AbstractUIComponent) createUIInstanceFor(uiclass);
		return null;
	}
	
	public ImageTracker getImageTracker() {
		return imageTracker;
	}
	
	public abstract String getUIDescriptionFileName();

	public ModuleGUI getModuleGUI(Module module, ModuleSectionGUI moduleSectionGUI) {
		return new ModuleGUI(this, module, moduleSectionGUI) {
			public void paintBuffer(Graphics g) {
				super.paintBuffer(g);
				drawOwnedComponents(g);
			}
		};
	}
	
	public ModuleSectionGUI getModuleSectionGUI(ModuleSection moduleSection) {
		return new ModuleSectionGUI(moduleSection);
	}
	
}
