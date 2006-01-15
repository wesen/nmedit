package org.nomad.util.debug;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ClassDelegateIterator implements Iterator {

	private ClassIterator iter = null;
	private Class pending = null;
	private Class ancestor = null;
	private boolean discardAncestor;
	
	public ClassDelegateIterator(ClassIterator iter, Class ancestor) {
		this(iter, ancestor, false);
	}

	public ClassDelegateIterator(ClassIterator iter, Class ancestor, boolean discardAncestor) {
		this.iter = iter;
		this.ancestor = ancestor;
		this.discardAncestor = discardAncestor;
		pending = traverse();
	}
	
	private Class traverse() {
		while (iter.hasNext()) {
			Class t = iter.nextClass();
			if (ancestor.isAssignableFrom(t)) {
				if (!discardAncestor || !ancestor.equals(t))
					return t;
			}
		}
		return null;
	}
	
	public boolean hasNext() {
		return pending!=null;
	}

	public Class nextDelegate() {
		if (!hasNext())
			throw new NoSuchElementException();
		
		Class result = pending;
		pending = traverse();
		return result;
	}
	
	public Object next() {
		return nextDelegate();
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

}
