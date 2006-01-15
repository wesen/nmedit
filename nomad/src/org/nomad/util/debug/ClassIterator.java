package org.nomad.util.debug;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ClassIterator implements Iterator {

	private JavaFileIterator iter = null;
	private Class pending = null;
	private FileClassLoader cl = new FileClassLoader();
	
	public ClassIterator(JavaFileIterator iter) {
		this.iter = iter;
		pending = traverse();
	}

	protected Class traverse() {
		Class c = null;
		while (iter.hasNext() && c==null) {
			try {
				c = cl.loadClass(iter.nextJavaFile());
			} catch (ClassNotFoundException e) {
				//e.printStackTrace();
			}
		}
		return c;
	}

	public boolean hasNext() {
		return pending!=null;
	}
	
	public Class nextClass() {
		if (!hasNext())
			throw new NoSuchElementException();
		
		Class result = pending;
		pending = traverse();
		return result;
	}
	
	public Object next() {
		return nextClass();
	}
	
	public void remove() {
		throw new UnsupportedOperationException();
	}
}