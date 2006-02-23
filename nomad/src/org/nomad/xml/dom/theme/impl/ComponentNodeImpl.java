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

import org.nomad.xml.dom.theme.ComponentNode;
import org.nomad.xml.dom.theme.PropertyNode;

public class ComponentNodeImpl extends NodeImpl<PropertyNode> implements ComponentNode {

	private String name = null;
	
	public ComponentNodeImpl(String name) {
		this.name=name;
	}

	public PropertyNode createPropertyNode(String name) {
		PropertyNodeImpl p = new PropertyNodeImpl(name);
		add(p);
		return p;
	}

	public PropertyNode getPropertyNode(int index) {
		return  (PropertyNode) getNode(index);
	}

	public String getName() {
		return name;
	}

}
