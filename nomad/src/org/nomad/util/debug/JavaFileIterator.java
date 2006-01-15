package org.nomad.util.debug;

import java.io.File;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class JavaFileIterator implements Iterator {

	private ResourceIterator iter = null;
	private File pending = null;
	
	public JavaFileIterator(ResourceIterator iter) {
		this.iter = iter;
		pending = traverse();
	}
	
	private File traverse() {
		File f = null;
		while (iter.hasNext()) {
			f = iter.nextFile();
			if (f.getName().toLowerCase().endsWith(".java"))
				break;
			else f = null;
		}
		return f;
	}

	public boolean hasNext() {
		return pending!=null;
	}

	public File nextJavaFile() {
		if (!hasNext())
			throw new NoSuchElementException();

		File result = pending; 
		pending = traverse();
		return result;
	}
	
	public Object next() {
		return nextJavaFile();
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}
}
