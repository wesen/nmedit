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
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.nomad.util.iterate.NullIterator;

public class TransitionMatrix<N, T extends Transition<N>> implements Iterable<T> {
	
	private final int DEFAULT_CAPACITY = 100;

	private ArrayList<N> nodeList 		= new ArrayList<N>(DEFAULT_CAPACITY);
	private ArrayList<T> all_transitions = new ArrayList<T>(DEFAULT_CAPACITY);
	private ArrayList<ArrayList<T>> transitionList = new ArrayList<ArrayList<T>>(DEFAULT_CAPACITY); 
	private ArrayList<TransitionChangeListener<T>> changeListenerList = 
		new ArrayList<TransitionChangeListener<T>>();
	
	private void addNodeIfNotExists(N node, T t) {
		int index = nodeList.indexOf(node);
		ArrayList<T> transitions;
		if (index<0) {
			nodeList.add(node);
			transitions = new ArrayList<T>(4);
			transitions.add(t);
			transitionList.add(transitions);
		} else {
			transitions = transitionList.get(index);
			if (!transitions.contains(t))
				transitions.add(t);
		}
	}
	
	public void addTransition(T t) {
		N n1 = t.getN1();
		N n2 = t.getN2();
		
		if (n1!=n2) {
			addNodeIfNotExists(n1, t);
			addNodeIfNotExists(n2, t);
			all_transitions.add(t);
			
			fireChangeEvent(t, true);
		}
	}

	public Iterator<N> traverseDirect(N node) {
		int index = nodeList.indexOf(node);
		ArrayList<T> list = null;
		if (index>=0) {
			list = transitionList.get(index);
			if (list!=null)
				return new TransitionToNodeIterator(list.iterator(), node);
		}
		return new NullIterator<N>();
	}
	
	public Iterator<T> iterator() {
		return all_transitions.iterator();
	}
	
	public Iterable<T> getTransitions(N node) {
		int index = nodeList.indexOf(node);
		ArrayList<T> list = null;
		
		if (index>=0)  list = transitionList.get(index);
		return list==null ? new ArrayList<T>() : list;
	}
	
	public Iterable<T> getLinkedT(N node) {
		ArrayList<T> trans = new ArrayList<T>(nodeList.size());
		ArrayList<N> nodes = new ArrayList<N>(nodeList.size());
		int visited = 0;
		nodes.add(node);
		
		while (visited<nodes.size()) {
			node = nodes.get(visited++);
			
			for (T t : getTransitions(node)) {
				N n1 = t.getN1();
				N n2 = t.getN2();

				if (!nodes.contains(n1)) nodes.add(n1);
				if (!nodes.contains(n2)) nodes.add(n2);
				if (!trans.contains(t)) trans.add(t);
			}
				
		}
		return trans;
	}
	
	public ArrayList<N> getLinked(N start) {
		ArrayList<N> nodes = new ArrayList<N>(nodeList.size());
		
		nodes.add(start);
		int index = 0;
		
		while (index<nodes.size())
			for (Iterator<N> link=traverseDirect(nodes.get(index++));link.hasNext();)
			{
				N node = link.next();
				if (!nodes.contains(node)) nodes.add(node);
			}
		
		return nodes;
	}
	
	private void removeTransition(N node, T t) {
		int index = nodeList.indexOf(node);
		ArrayList<T> tlist = transitionList.get(index);
		tlist.remove(t);
		if (tlist.size()<=0) {
			nodeList.remove(index);
			transitionList.remove(index);
		}
	}

	public void removeTransition(T t) {
		if (all_transitions.remove(t)) { // has transition
			
			N a = t.getN1();
			N b = t.getN2();

			removeTransition(a, t);
			removeTransition(b, t);
			fireChangeEvent(t, false);
		}
	}
	
	public void removeTransition(N a, N b) {
		removeTransition(getTransition(a,b));
	}

	public boolean hasTransition(N node) {
		return nodeList.contains(node);
	}
	
	public T getTransition(N a, N b) {
		int ai1 = nodeList.indexOf(a);
		if (ai1>=0) {
			for (T t : transitionList.get(ai1))
				if (t.getN1()==b||t.getN2()==b)
					return t;
		}
		return null;
	}

	public boolean hasTransition(N a, N b) {
		return getTransition(a, b)!=null;
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
	
	private class TransitionToNodeIterator implements Iterator<N> {

		private Iterator<T> transitions;
		private N neg_mask;

		public TransitionToNodeIterator(Iterator<T> transitions, N neg_mask) {
			this.transitions = transitions;
			this.neg_mask = neg_mask;
		}
		
		public boolean hasNext() {
			return transitions.hasNext();
		}

		public N next() {
			if (!hasNext()) throw new NoSuchElementException();
			T t = transitions.next();
			
			return t.getN1()!=neg_mask?t.getN1():t.getN2();
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
	
}
