package nomad.misc;

import java.util.Iterator;

/**
 * An iterator that links two iterators.
 *
 * @author Christian Schneider
 */
public class SequenceIterator implements Iterator {

	private Iterator ia = null;
	private Iterator ib = null;
	
	// the previously called iterator
	private Iterator lastCalled = null;
	
	/**
	 * An iterator that links two iterators
	 * 
	 * @param a first iterator
	 * @param b second iterator
	 */
	public SequenceIterator(Iterator a, Iterator b) {
		this.ia = a;
		this.ib = b;
	}
	
	public boolean hasNext() {
		return getCurrent().hasNext();
	}
	
	// returns the current iterator
	private Iterator getCurrent() {
		return lastCalled = ia.hasNext()?ia:ib;
	}

	public Object next() {
		return getCurrent().next();
	}

	public void remove() {
		lastCalled.remove();
	}
	
}
