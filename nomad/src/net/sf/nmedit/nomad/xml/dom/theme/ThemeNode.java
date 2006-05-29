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
 * Created on Feb 27, 2006
 */
package net.sf.nmedit.nomad.xml.dom.theme;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;

import net.sf.nmedit.nomad.theme.UIFactory;
import net.sf.nmedit.nomad.xml.pull.ThemeParser;

import org.xmlpull.v1.XmlPullParserException;


public class ThemeNode implements Iterable<ModuleNode> {

	private HashMap<Integer, ModuleNode> moduleNodeMap ;

	public ThemeNode() {
		moduleNodeMap = new HashMap<Integer, ModuleNode>();
	}

	public void putModuleNode(ModuleNode moduleNode) {
		moduleNodeMap.put(moduleNode.getId(), moduleNode);
	}

	public ModuleNode getModuleNode(int id) {
		return moduleNodeMap.get(id);
	}

	public Iterator<ModuleNode> iterator() {
		return moduleNodeMap.values().iterator();
	}

	public static void importDocument(ThemeNode dom, String file) {
		ThemeParser parser = new ThemeParser(dom);
		try {
			parser.parse(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
	}
	
	public void compile(UIFactory f) {
		for (ModuleNode m : this) {
			for (ComponentNode c : m) {
				c.compileProperties(f);
			}
		}
	}

}
