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
package org.nomad.xml.dom.theme.impl;

import java.util.Iterator;


import org.nomad.xml.XMLAttributeReader;
import org.nomad.xml.XMLAttributeValidationException;
import org.nomad.xml.dom.module.DModule;
import org.nomad.xml.dom.module.ModuleDescriptions;
import org.nomad.xml.dom.theme.NomadDOM;
import org.nomad.xml.dom.theme.NomadDOMComponent;
import org.nomad.xml.dom.theme.NomadDOMModule;
import org.nomad.xml.dom.theme.NomadDOMProperty;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DomImpl extends DOMNodeImpl implements NomadDOM {

	public DomImpl() {
		super();
	}

	public NomadDOMModule createModuleNode(DModule info) {
		DomModuleImpl mod = new DomModuleImpl(info);
		add(mod);
		return mod;
	}

	public NomadDOMModule createModuleNode(int id) { 
		DModule info = ModuleDescriptions.model.getModuleById(id);
		return createModuleNode(info);
	}

	public NomadDOMModule getModuleNodeById(int id) {
		for (Iterator iter = iterator();iter.hasNext();) {
			NomadDOMModule candidate = (NomadDOMModule) iter.next();
			if (candidate.getInfo().getModuleID()==id)
				return candidate;
		}
		return null;
	}

	public NomadDOMModule getModuleNode(int index) {
		return (NomadDOMModule) getNode(index);
	}
	
	public static void importDocument(NomadDOM noDom, Document xmlDoc) throws XMLAttributeValidationException {
		NodeList nodeList = xmlDoc.getElementsByTagName("module");
		
		XMLAttributeReader atts;
		
		for (int i=0;i<nodeList.getLength();i++) {
			 Node node = nodeList.item(i);
			 atts = new XMLAttributeReader(node);
			 int moduleId = atts.getIntegerAttribute("id");
			 
			 NomadDOMModule noModule = noDom.createModuleNode(moduleId);
			 importModule(noModule, node);
		}
	}

	public static void importModule(NomadDOMModule noModule, Node xmlModule) throws XMLAttributeValidationException {
		NodeList nodeList = xmlModule.getChildNodes();

		XMLAttributeReader atts;
		
		for (int i=0;i<nodeList.getLength();i++) {
			Node node = nodeList.item(i);

			if (node.getNodeType()==Node.ELEMENT_NODE) { // component
				
				atts = new XMLAttributeReader(node);
				String name = atts.getAttribute("name", null);
				
				if (name!=null) {
					
					NomadDOMComponent noComponent = noModule.createComponentNode(name);
					importComponent(noComponent, node);
				
				} else {
					System.err.println("Element 'component' has no attribute 'name'");
				}

				
			} else {
				// name mismatch
			}
		}
	}

	public static void importComponent(NomadDOMComponent noComponent, Node xmlComponent) throws XMLAttributeValidationException {
		NodeList nodeList = xmlComponent.getChildNodes();

		XMLAttributeReader atts;
		
		for (int i=0;i<nodeList.getLength();i++) {
			Node node = nodeList.item(i);
			if (node.getNodeType()==Node.ELEMENT_NODE) { // property
				atts = new XMLAttributeReader(node);
				String name = atts.getAttribute("name", null);
				if (name==null)
					System.err.println(atts.locateElement("name")+" not found");
				else {
					String value = atts.getAttribute("value", null);
					if (value==null)
						System.err.println(atts.locateElement("value")+" not found");
					else {
						NomadDOMProperty noProperty = noComponent.createPropertyNode(name);
						noProperty.setValue(value);
					}
				}
			} else {
				// name mismatch
			}
		}
	}
	
}
