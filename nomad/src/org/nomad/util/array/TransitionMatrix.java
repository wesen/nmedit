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
 * Created on Feb 15, 2006
 */
package org.nomad.util.array;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class TransitionMatrix<N, T extends Transition<N>> implements Iterable<T> {
	
	// private final int DEFAULT_CAPACITY = 100;

	private HashMap<N,NodeWrapper<N, T>> nodes = new HashMap<N,NodeWrapper<N, T>>();
	private LinkedList<T> all_transitions = new LinkedList<T>(); 
	private LinkedList<TransitionChangeListener<T>> changeListenerList = 
		new LinkedList<TransitionChangeListener<T>>();
	
	public void addTransition(T t) {
		N n1 = t.getN1();
		N n2 = t.getN2();
		
		if (n1!=n2) {
			NodeWrapper<N, T> nw;

			nw = nodes.get(n1);
			if (nw!=null) nw.addUnique(t);
			else nodes.put(n1, new NodeWrapper<N, T>(n1, t));
			
			nw = nodes.get(n2);
			if (nw!=null) nw.addUnique(t);
			else nodes.put(n2, new NodeWrapper<N, T>(n2, t));

			all_transitions.add(t);
			fireChangeEvent(t, true);
		}
	}
/*
	public Iterator<N> traverseDirect(N node) {
		NodeWrapper<N, T> nw = nodes.get(node);
		if (nw!=null)
			return nw.directTransitions();
		else
			return new NullIterator<N>();
	}*/
	
	public Iterator<T> iterator() {
		return all_transitions.iterator();
	}
	
	public Iterable<T> getTransitions(N node) {
		NodeWrapper<N, T> nw = nodes.get(node);
		if (nw!=null) {
			return nw.values();
		} else
			return new LinkedList<T>();
	}
	
	public Collection<N> getLinked(N start) {
		ArrayList<N> nodeList = new ArrayList<N>(nodes.keySet().size());
		
		nodeList.add(start);
		int index = 0;
		
		boolean found;
		while (index<nodeList.size()) {
			NodeWrapper<N,T> nw = nodes.get(nodeList.get(index++));
			if (nw!=null) {
				for (N n : nw.keySet()) {
					found = false;
					for (int i=0;i<index;i++)
						if (nodeList.get(i)==n) {
							found = true;
							break;
						}
					if (!found) 
						nodeList.add(n);
				}
			}
		}
		return nodeList;
	}
	
	public Iterator<T> getLinkedT(N start) {
		ArrayList<N> nodeList = new ArrayList<N>(nodes.keySet().size());
		HashMap<T,T> trans = new HashMap<T,T>();
		
		nodeList.add(start);
		int index = 0;
		
		boolean found;
		while (index<nodeList.size()) {
			NodeWrapper<N,T> nw = nodes.get(nodeList.get(index++));
			if (nw!=null) {
				for (N n : nw.keySet()) {
					found = false;
					for (int i=0;i<index;i++)
						if (nodeList.get(i)==n) {
							found = true;
							break;
						}
					if (!found) {
						nodeList.add(n);
						
						for (T t : nodes.get(n).values()) {
							if (!trans.containsKey(t))
								trans.put(t,t);
						}
						
					}
				}
			}
		}
		return trans.keySet().iterator();
	}
	
	private void removeNode(NodeWrapper<N,T> nw) {
		LinkedList<NodeWrapper<N,T>> removeList = new LinkedList<NodeWrapper<N,T>>();
		LinkedList<T> transList = new LinkedList<T>();
		removeList.add(nw);
		
		while (removeList.size()>0) {
			nw = removeList.remove();
			nodes.remove(nw.node);
			
			for (N n : nw.keySet()) {
				NodeWrapper<N,T> nw2 = nodes.get(n);
				T t = nw2.remove(nw.node);
				if (!transList.contains(t)) {
					all_transitions.remove(t);
					transList.add(t);
				}
				
				if (nw2.isEmpty())
					removeList.add(nw2);
			}
		}
		
		for (T t : transList)
			fireChangeEvent(t, false);
	}
	
	public void removeNode(N node) {
		NodeWrapper<N,T> nw = nodes.get(node);
		if (nw!=null) removeNode(nw);
	}
	/*
	private void removeTransition(N node, T t) {
		NodeWrapper<N,T> nw = nodes.get(node);
		if (nw!=null) {
			nw.removeTransition(t);
			all_transitions.remove(t);
			if (nw.isEmpty()) {
				nodes.remove(node);
				nodeRemoved(nw);
			}
		}
	}*/

	public void removeTransition(T t) {
		if (all_transitions.remove(t)) { // has transition

			NodeWrapper<N,T> nw1 = nodes.get(t.getN1());
			NodeWrapper<N,T> nw2 = nodes.get(t.getN2());

			nw1.remove(nw2.node);
			nw2.remove(nw1.node);

			if (nw1.isEmpty()) removeNode(nw1);
			
			if (nw2.isEmpty()) removeNode(nw1);
			
			fireChangeEvent(t, false);
		}
	}
	
	public void removeTransition(N a, N b) {
		removeTransition(getTransition(a,b));
	}

	public boolean hasTransition(N node) {
		return nodes.containsKey(node);
	}
	
	public T getTransition(N a, N b) {
		NodeWrapper<N,T> nwa = nodes.get(a);
		if (nwa!=null) {
			return nwa.get(b);
		}
		return null;
	}

	public boolean hasTransition(N a, N b) {
		NodeWrapper<N,T> nwa = nodes.get(a);
		if (nwa!=null) {
			return nwa.containsKey(b);
		} else {
			return false;
		}
	}

	protected void fireChangeEvent(T t, boolean transition_added) {
		if (changeListenerList.size()>0)
			for (TransitionChangeListener<T> l : changeListenerList)
				l.transitionChanged(t, transition_added);
	}
	
	public void addChangeListener(TransitionChangeListener<T> l) {
		if (!changeListenerList.contains(l))
			changeListenerList.add(l);
	}
	
	public void removeChangeListener(TransitionChangeListener<T> l) {
		changeListenerList.remove(l);
	}

	private static class NodeWrapper<N, T extends Transition<N>> extends HashMap<N, T> {
		private N node;

		public NodeWrapper (N node, T t) {
			this.node = node;
			put(other(t), t);
		}
		
		public N other(T t) {
			return t.getN1()==node ? t.getN2() : t.getN1();
		}
		
		public Iterator<N> directTransitions() {
			return keySet().iterator();
		}	
		
		public boolean contains(T t) {
			return containsKey(other(t));
		}

		public void removeTransition(T t) {
			remove(other(t));
		}
		
		public void addUnique(T t) {
			N n = other(t);
			if (!containsKey(n)) put(n, t);
		}
		
	}	
}
