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

import java.io.FileNotFoundException;
import java.util.HashMap;

import org.nomad.xml.dom.module.DModule;
import org.nomad.xml.dom.module.ModuleDescriptions;
import org.nomad.xml.dom.theme.NomadDOM;
import org.nomad.xml.dom.theme.NomadDOMModule;
import org.nomad.xml.pull.UIParser;
import org.xmlpull.v1.XmlPullParserException;

public class DomImpl extends DOMNodeImpl implements NomadDOM {

	private HashMap moduleMap = new HashMap();
	
	public DomImpl() {
		super();
	}

	public NomadDOMModule createModuleNode(DModule info) {
		DomModuleImpl mod = new DomModuleImpl(info);
		add(mod);
		moduleMap.put(info.getKey(), mod);
		return mod;
	}

	public NomadDOMModule createModuleNode(int id) { 
		DModule info = ModuleDescriptions.model.getModuleById(id);
		return createModuleNode(info);
	}

	public NomadDOMModule getModuleNodeById(int id) {
		return (NomadDOMModule) moduleMap.get(DModule.getKeyFromId(id));
	}

	public NomadDOMModule getModuleNode(int index) {
		return (NomadDOMModule) getNode(index);
	}

	public static void importDocument(DomImpl dom, String file) {
		UIParser parser = new UIParser(dom);
		try {
			parser.parse(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
	}

}
