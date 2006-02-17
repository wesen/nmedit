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
import org.nomad.xml.dom.theme.ThemeNode;
import org.nomad.xml.dom.theme.ModuleNode;
import org.nomad.xml.pull.ThemeParser;
import org.xmlpull.v1.XmlPullParserException;

public class ThemeNodeImpl extends NodeImpl implements ThemeNode {

	private HashMap<String, ModuleNode> moduleMap = new HashMap<String, ModuleNode>();
	
	public ThemeNodeImpl() {
		super();
	}

	public ModuleNode createModuleNode(DModule info) {
		ModuleNodeImpl mod = new ModuleNodeImpl(info);
		add(mod);
		moduleMap.put(info.getKey(), mod);
		return mod;
	}

	public ModuleNode createModuleNode(int id) { 
		DModule info = ModuleDescriptions.sharedInstance().getModuleById(id);
		return createModuleNode(info);
	}

	public ModuleNode getModuleNodeById(int id) {
		return moduleMap.get(DModule.getKeyFromId(id));
	}

	public ModuleNode getModuleNode(int index) {
		return (ModuleNode) getNode(index);
	}

	public static void importDocument(ThemeNodeImpl dom, String file) {
		ThemeParser parser = new ThemeParser(dom);
		try {
			parser.parse(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
	}

}
