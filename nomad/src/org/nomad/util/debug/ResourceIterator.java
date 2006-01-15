package org.nomad.util.debug;

import java.io.File;
import java.net.URL;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.nomad.util.misc.Stack;


public class ResourceIterator implements Iterator {

	/* a stack containing File objects */
	private Stack stack = new Stack();
	private File pending= null;
	public ResourceIterator(URL url) {
		this(url.toString());
	}

	public ResourceIterator(String dir) {
		this(new File(dir));
	}

	public ResourceIterator(File dir) {
		stack.push(dir);
		pending = traverse();
	}

	/*
	 * 1. If stack is empty, null will be returned.
	 * 2. Removes top item from stack. If it is a file,
	 *    it will be returned. 
	 * 3. If it is a directory, the directorys content
	 *    will be put on the stack. Go on with 1. 
	 */
	protected File traverse() {
		File f = null;

		while (!stack.isEmpty()) {
			f = (File) stack.top();
			stack.pop();
			
			if (f.isFile())
				break;

			File[] content = f.listFiles();
			if (content!=null) {
				for (int i=0;i<content.length;i++) {
					stack.push(content[i]);
				}
			}
		}
		
		return f;
	}

	public boolean hasNext() {
		return pending!=null;
	}

	public File nextFile() {
		if (!hasNext())
			throw new NoSuchElementException();
		
		File result = pending;
		pending = traverse();
		return result;
	}

	public Object next() {
		return nextFile();
	}
	
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
