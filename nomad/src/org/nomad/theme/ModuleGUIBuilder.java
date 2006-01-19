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

import org.nomad.patch.Module;
import org.nomad.theme.component.NomadComponent;
import org.nomad.theme.component.NomadContainerCacher;
import org.nomad.theme.property.Property;
import org.nomad.theme.property.PropertySet;
import org.nomad.util.graphics.ImageBuffer;
import org.nomad.util.graphics.PersistenceManager;
import org.nomad.xml.XMLFileWriter;
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
	private HashMap decorationOnlyComponents = new HashMap();

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

	public void load() {
		load(uifactory.getUIDescriptionFileName());
	}

	public static void info() {
		
		PersistenceManager man = instance.backgroundManager;
		
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
		decorationOnlyComponents = new HashMap();
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
		ModuleGUI moduleGUI = uifactory.getModuleGUI(module.getDModule(), module, moduleSectionGUI);
		_createGUIComponents(moduleGUI, null, module.getDModule(), true);
		return moduleGUI;
	}
	
	public ModuleGUI _createGUI(DModule moduleInfo, ModuleSectionGUI moduleSectionGUI) {
		ModuleGUI moduleGUI = uifactory.getModuleGUI(moduleInfo, null,moduleSectionGUI);
		_createGUIComponents(moduleGUI, null, moduleInfo, true);
		return moduleGUI;
	}

	private Object getCacheKey(DModule moduleInfo) {
		return new Integer(moduleInfo.getModuleID());
	}
	
	public void _createGUIComponents(NomadComponent modulePane, Object object, DModule moduleInfo, boolean useCache) {
		
		if (useCache) {
			
			boolean keyNotFound = false;
			boolean isRendering = false ;
			
			Object cacheKey = getCacheKey(moduleInfo);
			ImageBuffer bgCache = new ImageBuffer(backgroundManager, cacheKey);

			// NomadContainerCacher.enable();
			if (bgCache.isValid()) {
				NomadContainerCacher.enableHook(modulePane, bgCache);
			} else {
				NomadContainerCacher.enableHook(modulePane, backgroundManager, cacheKey);
				isRendering = true;
			}

			// get module ui information
			NomadDOMModule domModule = nomadDom.getModuleNodeById(moduleInfo.getModuleID());
			
			for (Iterator itComp=domModule.iterator();itComp.hasNext();) {
				NomadDOMComponent compNode = (NomadDOMComponent) itComp.next();

				String compName = compNode.getName();
				Class compClass = uifactory.getNomadComponentClass(compName);

				keyNotFound = !decorationOnlyComponents.containsKey(compName);
				
				if(isRendering || keyNotFound) {
					
					NomadComponent comp = uifactory.newComponentInstanceByClass(compClass);
					comp.setSize(comp.getPreferredSize());
						
					if (comp==null) {
						System.err.println("Cannot create componenent with name '"+compName+"'.");
					} else {
						
						if (keyNotFound && !comp.hasDynamicOverlay()) {
							String key = new String(compName);
							decorationOnlyComponents.put(key,key);
						}
						
						// setup component
						comp.setEnvironment(uifactory);
						comp.setNameAlias(compName);
						PropertySet properties = comp.getAccessibleProperties();
						for (Iterator itProp=compNode.iterator();itProp.hasNext();) {
							NomadDOMProperty propNode = (NomadDOMProperty) itProp.next();
							Property compProperty = properties.byName(propNode.getName());

							try {
								compProperty.setValueFromString(propNode.getValue());
							} catch (Throwable t) {
								System.err.println("** In component "+comp.getClass().getName()+": error setting property '"+propNode.getName()+"'.");
								System.err.println("** "+t);
								//t.printStackTrace();
							}
						}
						comp.deleteOnScreenBuffer();
						comp.link();
						modulePane.add(comp);
					}
				}
			}


			NomadContainerCacher.closeHook();
			//NomadContainerCacher.disable();
			
		} else {

			// no caching

			// get module ui information
			NomadDOMModule domModule = nomadDom.getModuleNodeById(moduleInfo.getModuleID());
			
			for (Iterator itComp=domModule.iterator();itComp.hasNext();) {
				NomadDOMComponent compNode = (NomadDOMComponent) itComp.next();

				String compName = compNode.getName();
				Class compClass = uifactory.getNomadComponentClass(compName);

				NomadComponent comp = uifactory.newComponentInstanceByClass(compClass);
				comp.setSize(comp.getPreferredSize());
				
				if (comp==null) {

					System.err.println("Cannot create componenent with name '"+compName+"'.");

				} else {
					
					// setup component
					comp.setEnvironment(uifactory);
					comp.setNameAlias(compName);

					PropertySet properties = comp.getAccessibleProperties();

					for (Iterator itProp=compNode.iterator();itProp.hasNext();) {
						NomadDOMProperty propNode = (NomadDOMProperty) itProp.next();
						Property compProperty = properties.byName(propNode.getName());

						try {
							compProperty.setValueFromString(propNode.getValue());
						} catch (Throwable t) {
							System.err.println("** In component "+comp.getClass().getName()+": error setting property '"+propNode.getName()+"'.");
							System.err.println("** "+t);
							//t.printStackTrace();
						}
					}

					modulePane.add(comp);
					comp.deleteOnScreenBuffer();
					comp.link();
				}
			}
		}
		
		
		
		
		
		/*
		
		boolean createDecoration = true;
		
		ImageBuffer bgCache = null;
		Object cacheKey = null;
		
		if (useCache) {
			cacheKey = getCacheKey(moduleInfo);
			bgCache = new ImageBuffer(backgroundManager, cacheKey);
			createDecoration = !bgCache.isValid();
		}
		
		// get module ui information
		NomadDOMModule domModule = nomadDom.getModuleNodeById(moduleInfo.getModuleID());
		
		for (Iterator itComp=domModule.iterator();itComp.hasNext();) {
			NomadDOMComponent compNode = (NomadDOMComponent) itComp.next();

			String compName = compNode.getName();
			Class compClass = uifactory.getNomadComponentClass(compName);

			boolean notAsDecorationRegistered = !decorationOnlyComponents.containsKey(compName);
					
			if ( createDecoration || notAsDecorationRegistered) {
				
				NomadComponent comp = uifactory.newComponentInstanceByClass(compClass);
				comp.setSize(comp.getPreferredSize());
				
				if (comp==null) {

					System.err.println("Cannot create componenent with name '"+compName+"'.");

				} else {
					
					if (notAsDecorationRegistered && (!comp.hasDynamicOverlay()) ) {
						decorationOnlyComponents.put(compName, compName);
					}
					// setup component
					comp.setEnvironment(uifactory);
					comp.setNameAlias(compName);

					PropertySet properties = comp.getAccessibleProperties();

					for (Iterator itProp=compNode.iterator();itProp.hasNext();) {
						NomadDOMProperty propNode = (NomadDOMProperty) itProp.next();
						Property compProperty = properties.byName(propNode.getName());
						if (compProperty==null) {
							System.err.println("Component '"+comp.getClass().getName()+"' has no property "+propNode+".");
						} else {
							try {
								compProperty.setValueFromString(propNode.getValue());
							} catch (Throwable t) {
								System.err.println("In component "+comp.getClass().getName()+": error setting property "+t+".");
								t.printStackTrace();
							}
						}
					}

					modulePane.add(comp);
					comp.deleteOnScreenBuffer();
					comp.link();
				}
			}
		}

		if (useCache) {

			if (bgCache==null || !bgCache.isValid()) {
				bgCache = new ImageBuffer(backgroundManager, cacheKey, modulePane.renderBackground());
				modulePane.removeDecoration();
			}
			// broadcast
			modulePane.broadcastBackground(bgCache);
			bgCache.dispose();
		}
	*/	
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

	public final static NomadDOM read(String file) {
		DomImpl dom = new DomImpl();
		DomImpl.importDocument(dom, file);
		return dom;
	}
	
}
