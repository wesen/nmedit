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

import java.util.ArrayList;
import java.util.Iterator;

import org.nomad.xml.dom.theme.Node;

public abstract class NodeImpl<N> implements Node<N> {

	private ArrayList<N> children = new ArrayList<N>();

	public int getNodeCount() {
		return (children==null) ? 0 : children.size();
	}

	protected void add(N node) {
		children.add(node);
	}
	
	public NodeImpl getNode(int index) {
		return (NodeImpl) children.get(index);
	}
	
	public Iterator<N> iterator() {
		return children.iterator();
	}
	
	public void removeChildren() {
		children.clear();
	}
	
}
