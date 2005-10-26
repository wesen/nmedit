package nomad.misc;

import java.util.Iterator;

/**
 * @author Christian Schneider
 */
public class SequenceIterator implements Iterator {

	private Iterator ia = null;
	private Iterator ib = null;
	private Iterator lastCalled = null;
	
	/**
	 * Creates an iterator that links two iterators
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
	
	private Iterator getCurrent() {
		lastCalled = ia.hasNext()?ia:ib;
		return lastCalled;
	}

	public Object next() {
		return getCurrent().next();
	}

	public void remove() {
		lastCalled.remove();
	}
	
}
