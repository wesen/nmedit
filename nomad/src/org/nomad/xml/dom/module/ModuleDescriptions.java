package org.nomad.xml.dom.module;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.nomad.util.misc.ImageTracker;
import org.nomad.xml.dom.substitution.Substitutions;
import org.nomad.xml.pull.ModuleDescriptionParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * A container for all module relevant information.
 * 
 * @author Christian Schneider
 * @composed 1 - n nomad.model.descriptive.DGroup
 */
public class ModuleDescriptions {

	/** Hashmap containing the pairs (DModule.getKey(), DModule) */
	private HashMap dmodules = new HashMap();
	/** The groups */
	private ArrayList dgroups = new ArrayList();
	/** the static data */
	public static ModuleDescriptions model = null;
	
	/**
	 * Initializes the static model by loading all data from the xml file 
	 * @param xmlfile the xml file
	 * @param subs the substitution xml file reader
	 */
	public static void init(String xmlfile, Substitutions subs) {
		model = new ModuleDescriptions(xmlfile, subs);
	}

	/**
	 * Loads the module icons
	 * @param imageTracker the image source
	 */
	public void loadImages(ImageTracker imageTracker) {
		DConnector.setImageTracker(imageTracker);
		//DConnector.loadImages(imageTracker);

		Iterator iter = dmodules.values().iterator();
		while (iter.hasNext()) {
			DModule module = (DModule) iter.next();
			module.setIcon(imageTracker.getImage(Integer.toString(module.getModuleID())));
		}
	}

	/**
	 * Retuns a module by it's key
	 * @param key the key
	 * @return the module
	 */
	public DModule getModuleByKey(String key) {
		return (DModule) dmodules.get(key);
	}

	/**
	 * Returns a module by it's id
	 * @param id the id
	 * @return the module
	 */
	public DModule getModuleById(int id) {
		return getModuleByKey(DModule.getKeyFromId(id));
	}
	
	/**
	 * Returns a toolbar group at given index
	 * @param index the index
	 * @return the group
	 */
	public DToolbarGroup getGroup(int index) {
		return (DToolbarGroup) dgroups.get(index);
	}
	
	/**
	 * Returns the number of groups
	 * @return the number of groups
	 */
	public int getGroupCount() {
		return dgroups.size();
	}
	
	/**
	 * Returns a collection containing all DModule objects
	 * @return a collection containing all DModule objects
	 */
	public Collection getModules() {
		return dmodules.values();
	}
	
	private Substitutions substitutions = null;
	
	public ModuleDescriptions(String xmlfile, Substitutions subs) {
		this.substitutions = subs;

		ModuleDescriptionParser parser = new ModuleDescriptionParser(this);
		try {
			parser.parse(xmlfile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}

	}
	
	public Substitutions getSubstitutions() {
		return substitutions;
	}
	
	public void add(DModule module) {
		dmodules.put(module.getKey(), module);
	}

	public void addToolbarGroup(DToolbarGroup group) {
		dgroups.add(group);
	}

}
