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


import java.util.HashMap;
import java.util.Iterator;

import org.nomad.image.ImageBuffer;
import org.nomad.image.PersistenceManager;
import org.nomad.patch.Module;
import org.nomad.theme.component.NomadComponent;
import org.nomad.theme.property.Property;
import org.nomad.xml.XMLAttributeValidationException;
import org.nomad.xml.XMLFileWriter;
import org.nomad.xml.XMLReader;
import org.nomad.xml.dom.module.DModule;
import org.nomad.xml.dom.theme.NomadDOM;
import org.nomad.xml.dom.theme.NomadDOMComponent;
import org.nomad.xml.dom.theme.NomadDOMModule;
import org.nomad.xml.dom.theme.NomadDOMProperty;
import org.nomad.xml.dom.theme.impl.DomImpl;

public class ModuleGUIBuilder {

	public static ModuleGUIBuilder instance;
	private NomadDOM nomadDom = null;
	private UIFactory uifactory = null;
	private PersistenceManager backgroundManager = new PersistenceManager();
	private HashMap staticComponentMap = new HashMap();

	public ModuleGUIBuilder(UIFactory uifactory) {
		setUIFactory(uifactory);
	}

	public final static void rewriteDOM(NomadComponent moduleContainer, DModule info) {
		
		if (instance == null || instance.nomadDom==null)
			throw new NullPointerException();
		
		NomadDOMModule node = instance.nomadDom.getModuleNodeById(info.getModuleID());
		if (node != null)
			node.removeChildren();
		else
			node = instance.nomadDom.createModuleNode(info.getModuleID());
		
    	for (Iterator iter = moduleContainer.getExportableNomadComponents();iter.hasNext();) {
    		NomadComponent comp = (NomadComponent) iter.next();
    		
    		NomadDOMComponent compNode = node.createComponentNode(comp.getNameAlias());
    		comp.getAccessibleProperties().exportToDOM(compNode);
    	}
    }
    
	public void exportDom(XMLFileWriter out) {
		out.beginTag("ui-description", true);
		
		for (int i=0;i<nomadDom.getNodeCount();i++) {
			NomadDOMModule mod = nomadDom.getModuleNode(i);
			
			out.beginTagStart("module");
			out.addAttribute("id", ""+mod.getInfo().getModuleID());
			out.beginTagFinish(true);
			
			for (int j=0;j<mod.getNodeCount();j++) {
				NomadDOMComponent compNode = mod.getComponentNode(j);
				
				out.beginTagStart("component");
				out.addAttribute("name", compNode.getName()); // TODO use associations i.e. 'button', 'knob'
				out.beginTagFinish(true);

				for (int k=0;k<compNode.getNodeCount();k++) {
					NomadDOMProperty pNode = compNode.getPropertyNode(k);
					
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

	public boolean load() {
		return load(uifactory.getUIDescriptionFileName());
	}

	public boolean load(String file) {
		
		backgroundManager = new PersistenceManager(); // remove all backgrounds
		staticComponentMap = new HashMap();
		try {
			nomadDom = read(file);
		} catch (XMLAttributeValidationException e) {
			e.printStackTrace();
			nomadDom = new DomImpl();
			return false;
		}
		return true;
	}

	public void setUIFactory(UIFactory uifactory) {
		this.uifactory = uifactory;
		load();
	}

	public NomadDOM getDom() {
		return nomadDom;
	}

	public ModuleGUI _createGUI(Module module, ModuleSectionGUI moduleSectionGUI) {
		//ModuleGUI moduleGUI = new ModuleGUI(uifactory, module, moduleSectionGUI);
		ModuleGUI moduleGUI = uifactory.getModuleGUI(module, moduleSectionGUI);

		configureModuleGUI(moduleGUI, module);
		
		_createGUIComponents(moduleGUI, null, module.getDModule(), true);
		return moduleGUI;
	}

	public void configureModuleGUI(ModuleGUI moduleGUI, Module module) {
		
		moduleGUI.setLocation(module.getPixLocationX(), module.getPixLocationY());
		moduleGUI.setSize(module.getPixWidth(), module.getPixHeight());
		moduleGUI.setNameLabel(module.getModuleTitle(), module.getPixWidth());
		moduleGUI.setVisible(true);
		
	}
	
	public void _createGUIComponents(NomadComponent modulePane, Object object, DModule moduleInfo, boolean useCache) {
		
		
		ImageBuffer background;
		if (useCache)
			background = new ImageBuffer(backgroundManager, new Integer(moduleInfo.getModuleID()));
		else
			background = new ImageBuffer();
		
		if (moduleInfo==null) {
			
			System.err.println("Cannot determine module id. Skipping.");
			return ;
			
		}
		
		NomadDOMModule domModule = nomadDom.getModuleNodeById(moduleInfo.getModuleID());
		
		if (domModule==null) {
			
			System.err.println("DOM:No node for module "+moduleInfo);
			return ;
			
		}
		
		for (int i=0;i<domModule.getNodeCount();i++) {
			NomadDOMComponent compNode = domModule.getComponentNode(i);
			
			String compName = compNode.getName();
			Class compClass = uifactory.getNomadComponentClass(compName);

			if ((!staticComponentMap.containsKey(compClass))|| (!background.isValid())) {
				
				NomadComponent comp = uifactory.newComponentInstanceByClass(compClass);
				
				if (comp==null) {
					System.err.println("Cannot instantiate componenend with name '"+compName+"'.");
				} else {
					comp.setNameAlias(compName);
					comp.setEnvironment(uifactory);

					if (useCache & !comp.hasDynamicOverlay()) // remember component, that we do not need
						staticComponentMap.put(compClass,compClass);
		
//					if (!background.isValid() || comp.hasDynamicOverlay()) {
						// we must create this component
	
						comp.setSize(comp.getPreferredSize());
						
						for (int j=0;j<compNode.getNodeCount();j++) {
							NomadDOMProperty propNode = compNode.getPropertyNode(j);
							
							Property compProperty =
								comp.getAccessibleProperties().byName(propNode.getName());
							
							if (compProperty==null)
								System.err.println("In component "+comp.getClass().getName()+": no property "+propNode+".");
							else {
								try {
									compProperty.setValue(propNode.getValue());
								} catch (Throwable t) {
									System.err.println("In component "+comp.getClass().getName()+": error setting property "+t+".");
									t.printStackTrace();
								}
							}
						}
		
						//comp.deleteOnScreenBuffer();
						modulePane.add(comp);
						
					//	comp.deleteOnScreenBuffer(); // to make sure it is updated
					}
					
//				}
			}
		}
		
		// nearly finished
		
		if (useCache) {
			if (!background.isValid()) {
				//	modulePane.validate();
				background = new ImageBuffer(backgroundManager, new Integer(moduleInfo.getModuleID()) , modulePane.renderBackground());
				modulePane.removeDecoration();
			}

			// broadcast
			modulePane.broadcastBackground(background);
		}
	}
	
	
	
	
	
	
	
	
	
	// -----------

	public static ModuleGUI createGUI(Module module, ModuleSectionGUI moduleSectionGUI) {
		return instance._createGUI(module, moduleSectionGUI);
	}

	public static void createGUIBuilder(UIFactory uifactory) {
		instance = new ModuleGUIBuilder(uifactory);
	}

	public static void createGUIComponents(NomadComponent modulePane, Object object, DModule moduleInfo) {
		instance._createGUIComponents(modulePane, object, moduleInfo, true);
	}

	public final static NomadDOM read(String file) throws XMLAttributeValidationException {
		DomImpl dom = new DomImpl();
		DomImpl.importDocument(dom, XMLReader.readDocument(file));
		return dom;
	}
	
}
