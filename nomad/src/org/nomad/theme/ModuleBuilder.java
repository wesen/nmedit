/* Copyright (C) 2006 Christian Schneider
 * 
 * This file is part of Nomad.
 * 
 * Nomad is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Nomad is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Nomad; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

/*
 * Created on Jan 12, 2006
 */
package org.nomad.theme;


import java.util.Iterator;

import org.nomad.env.Environment;
import org.nomad.patch.Module;
import org.nomad.patch.ui.ModuleSectionUI;
import org.nomad.patch.ui.ModuleUI;
import org.nomad.theme.component.NomadComponent;
import org.nomad.theme.component.NomadContainerCacher;
import org.nomad.theme.property.Property;
import org.nomad.theme.property.PropertySet;
import org.nomad.theme.property.PropertyUtils;
import org.nomad.util.graphics.ImageBuffer;
import org.nomad.util.graphics.PersistenceManager;
import org.nomad.xml.XMLFileWriter;
import org.nomad.xml.dom.module.DModule;
import org.nomad.xml.dom.theme.ComponentNode;
import org.nomad.xml.dom.theme.ModuleNode;
import org.nomad.xml.dom.theme.PropertyNode;
import org.nomad.xml.dom.theme.ThemeNode;
import org.nomad.xml.dom.theme.impl.ThemeNodeImpl;

public class ModuleBuilder {

	private ThemeNode nomadDom = null;
	private UIFactory uifactory = null;
	private PersistenceManager backgroundManager = new PersistenceManager();
	
	private Environment env = null;
	
	public Environment getEnvironment() {
		return env;
	}
	
	public ModuleBuilder(Environment env) {
		this.env = env;
	}

	public void rewriteDOM(NomadComponent moduleContainer, DModule info) {
		
		if (nomadDom==null)
			throw new NullPointerException("No Dom");
		
		ModuleNode node = nomadDom.getModuleNodeById(info.getModuleID());
		if (node != null)
			node.removeChildren();
		else
			node = nomadDom.createModuleNode(info.getModuleID());
		
    	for (Iterator<NomadComponent> iter = moduleContainer.getExportableNomadComponents();iter.hasNext();) {
    		NomadComponent comp = iter.next();
    		ComponentNode compNode = node.createComponentNode(comp.getNameAlias());
    		PropertyUtils.exportToDOM(compNode, uifactory.getProperties(comp) ); 
    	}
    }
    
	public void exportDom(XMLFileWriter out) {
		out.beginTag("theme", true);
		
		for (ModuleNode mod : nomadDom) {
			out.beginTagStart("module");
			out.addAttribute("id", ""+mod.getModule().getModuleID());
			out.beginTagFinish(true);
			
			for (ComponentNode compNode : mod) {
				out.beginTagStart("component");
				out.addAttribute("name", compNode.getName()); // TODO use associations i.e. 'button', 'knob'
				out.beginTagFinish(true);

				for (PropertyNode pNode : compNode) {
					out.beginTagStart("property");
					out.addAttribute("name",pNode.getName());
					out.addAttribute("value", pNode.getValue());
					out.beginTagFinish(false);
					
				}
				
				out.endTag();
			}	
			out.endTag();
		}
		out.endTag();
	}

	public void load() {
		load(uifactory.getUIDescriptionFileName());
	}

	public void info() {
		
		PersistenceManager man = backgroundManager;
		
		int max = 0;
		int total = 0;
		for (Iterator iter=man.getKeys().iterator();iter.hasNext();){
			int r = man.getReferenceCount(iter.next());
			total+=r;
			max=Math.max(max,r);
		}
		System.out.println("** max(references)=#"+max);
		System.out.println("** total(references)=#"+total);
		
	}
	
	public void load(String file) {
		backgroundManager = new PersistenceManager(); // remove all backgrounds
		nomadDom = read(file);
	}

	public void setUIFactory(UIFactory uifactory) {
		this.uifactory = uifactory;
		load();
	}

	public ThemeNode getDom() {
		return nomadDom;
	}

	public ModuleUI compose(Module module, ModuleSectionUI moduleSection) {
		ModuleUI moduleGUI = compose(module.getInfo(), moduleSection);
		moduleGUI.setModule(module);
		return moduleGUI;
	}
	
	// TODO remove components border if cache is used
	
	public ModuleUI compose(DModule module, ModuleSectionUI moduleSection) {
		ModuleUI moduleGUI = uifactory.getModuleGUI(module);
		moduleGUI.setModuleSectionUI(moduleSection);
		createGUIComponents(moduleGUI, module, true);
		return moduleGUI;
	}
	
	public ModuleUI compose(DModule module) {
		ModuleUI moduleGUI = uifactory.getModuleGUI(module);
		createGUIComponents(moduleGUI, module, true);
		return moduleGUI;
	}

	private Object getCacheKey(DModule moduleInfo) {
		return new Integer(moduleInfo.getModuleID());
	}
	
	public void createGUIComponents(NomadComponent modulePane, DModule moduleInfo, boolean useCache) {
		boolean isRendering = false ;
		
		if (useCache) {
			Object cacheKey = getCacheKey(moduleInfo);
			ImageBuffer bgCache = new ImageBuffer(backgroundManager, cacheKey);

			// NomadContainerCacher.enable();
			if (bgCache.isValid()) {
				NomadContainerCacher.enableHook(modulePane, bgCache);
			} else {
				NomadContainerCacher.enableHook(modulePane, backgroundManager, cacheKey);
				isRendering = true;
			}
		} else {
			isRendering = true;
		}

		// get module ui information
		ModuleNode domModule = nomadDom.getModuleNodeById(moduleInfo.getModuleID());
		
		for (ComponentNode compNode : domModule) {
			String compName = compNode.getName();
			Class<? extends NomadComponent> compClass = uifactory.getNomadComponentClass(compName);

			if (compClass==null) {
				System.err.println("Cannot create component with name '"+compName+"'.");
			} else {
				if(isRendering || !uifactory.isDecoration(compClass)) {
					NomadComponent comp = uifactory.newComponentInstanceByClass(compClass);

					// setup component
					PropertySet properties = uifactory.getProperties(comp);
					for (PropertyNode propNode : compNode) {
						Property compProperty = properties.get(propNode.getName());

						try {
							compProperty.setValue(propNode.getValue());
						} catch (Throwable t) {
							System.err.println("** In component "+comp.getClass().getName()+": error setting property '"+propNode.getName()+"'.");
							System.err.println("** "+t);
						}
					}
					modulePane.add(comp);
				}
			} 
		}

		NomadContainerCacher.closeHook();
		//NomadContainerCacher.disable();
		
	}

	// -----------

	public ThemeNode read(String file) {
		ThemeNodeImpl dom = new ThemeNodeImpl();
		ThemeNodeImpl.importDocument(dom, file);
		return dom;
	}
	
}
