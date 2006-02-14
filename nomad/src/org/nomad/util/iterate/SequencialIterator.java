package org.nomad.util.iterate;

import java.util.Iterator;

/**
 * An iterator that links two iterators.
 *
 * @author Christian Schneider
 */
public class SequencialIterator<T> implements Iterator<T> {

	private Iterator<T> ia = null;
	private Iterator<T> ib = null;
	
	// the previously called iterator
	private Iterator<T> lastCalled = null;
	
	/**
	 * An iterator that links two iterators
	 * 
	 * @param a first iterator
	 * @param b second iterator
	 */
	public SequencialIterator(Iterator<T> a, Iterator<T> b) {
		this.ia = a;
		this.ib = b;
	}
	
	public boolean hasNext() {
		return getCurrent().hasNext();
	}
	
	// returns the current iterator
	private Iterator<T> getCurrent() {
		return lastCalled = ia.hasNext()?ia:ib;
	}

	public T next() {
		return getCurrent().next();
	}

	public void remove() {
		lastCalled.remove();
	}
	
}
