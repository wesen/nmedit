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
 * Created on Feb 3, 2006
 */
package org.nomad.util.array;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.nomad.util.iterate.MakeIterable;

public abstract class TransitionMatrix<N, T> extends Array2D<T> {

	private ArrayList<N> nodeList = new ArrayList<N>();
	private ArrayList<TransitionChangeListener<N,T>> changeListener = 
		new ArrayList<TransitionChangeListener<N,T>>();
	//private boolean tableResize = false;

	public TransitionMatrix() {
		super();
	}

	protected void fireChangeEvent(N a, N b, T oldt, T newt) {
		for (TransitionChangeListener<N,T> l : changeListener)
			l.transitionChanged(this, a, b, oldt, newt);
	}
	
	public void addChangeListener(TransitionChangeListener<N,T> l) {
		if (!changeListener.contains(l))
			changeListener.add(l);
	}
	
	public void removeChangeListener(TransitionChangeListener<N,T> l) {
		changeListener.remove(l);
	}
	
	public int indexOf(N node) {
		return nodeList.indexOf(node);
	}
	
	public int getNodeCount() {
		return nodeList.size();
	}
	
	public N getNode(int index) {
		return nodeList.get(index);
	}
	
	public Iterable<N> iterableNodes() {
		return new MakeIterable<N>(nodes());
	}
	
	public Iterator<N> nodes() {
		return nodeList.iterator();
	}

	protected void addNodeU(N node) {
		nodeList.add(node);
	}
	/*
	public boolean isTableResizeEnabled() {
		return tableResize;
	}
	
	public void setTableResizeEnabled(boolean enabled) {
		if (tableResize!=enabled) {
			tableResize = enabled;
			if (tableResize)
				nodesChanged();
		}
	}*/
	
	protected void nodesChanged() {
		//if (isTableResizeEnabled())
			resize(nodeList.size());
	}
	
	public void addNode(N node) {
		addNodeU(node);
		nodesChanged();
	}
	
	public void removeNode(N node) {
		int index = nodeList.indexOf(node);
		if (index>=0) {
			nodeList.remove(node);
			removeCross(index);
		}
	}
	
	public int getSize() {
		return nodeList.size();
	}

	public T getCell(N a, N b) {
		return getCell(indexOf(a), indexOf(b));
	}
	
	public void setCell(N a, N b, T value) {
		setCell(indexOf(a), indexOf(b), value);
	}
	
	public boolean hasTransition(N a, N b) {
		return getCell(a, b)!=null;
	}
	
	public T getTransition(N a, N b) {
		return getCell(a, b);
	}
	
	public void setTransition(N a, N b, T t) {
		int ia = indexOf(a);
		int ib = indexOf(b);
		if (ia>=0 && ib>=0) {
			T old = getCell(ia, ib);
			
			setCell(ia, ib, t);
			setCell(ib, ia, t);
			
			fireChangeEvent(a, b, old, t); //
		}
	}

	public Iterator<N> traverseDirect(N start) {
		return traverseDirect(indexOf(start));
	}
	
	protected Iterator<N> traverseDirect(int nodeIndex) {
		return new DirectIterator(nodeIndex);
	}

	public ArrayList<T> getDirectTransitions(N start) {
		ArrayList<T> trans = new ArrayList<T>();
		int a = indexOf(start);
		for (Iterator<N> iter=traverseDirect(a);iter.hasNext();)
		{
			T t = getCell(a, indexOf(iter.next()));
			if (t!=null && !trans.contains(t))
				trans.add(t);
		}
		return trans;
	}

	public ArrayList<T> getTransitions(N start) {
		ArrayList<N> links = getLinked(start);
		ArrayList<T> trans = new ArrayList<T>();
		
		for (int i=links.size()-1;i>0;i--)
			for (int j=i-1;j>=0;j--)
			{
				T t = getCell(i, j);
				if (t!=null) trans.add(t);
			}
		
		return trans;
	}

	public ArrayList<N> getLinked(N start)
	{
		ArrayList<N> nodes = new ArrayList<N>();
		nodes.add(start);
		int index = 0;
		
		while (index<nodes.size()) 
		{
			for (Iterator<N> link=traverseDirect(nodes.get(index++));link.hasNext();)
			{
				N node = link.next();
				if (!nodes.contains(node)) nodes.add(node);
			}
		}
		
		return nodes;
	}
	
	private class DirectIterator implements Iterator<N> {

		private int index;
		private int successor = -1;

		public DirectIterator(int index) {
			this.index = index;
			align();
		}

		private void align() {
			successor++;
			while (hasNext()) {
				if (getCell(index, successor)!=null)
					break; // transition exists, done
				successor++;
			}
		}
		
		public boolean hasNext() {
			return successor<nodeList.size();
		}

		public N next() {
			if (!hasNext())
				throw new NoSuchElementException();

			int current = successor;
			align();
			return nodeList.get(current);
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
	
}
