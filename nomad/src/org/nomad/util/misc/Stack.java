package org.nomad.util.misc;

import java.util.ArrayList;

public class Stack<T> extends ArrayList<T> {

	public Stack() { }

	public Stack(T top) {
		if (top!=null)
			push(top);
	}
	
	public void push(T obj) {
		add(obj);
	}
	
	public int getTopIndex() {
		return size()-1;
	}
	
	public T top() {
		return get(getTopIndex());
	}
	
	public void pop() {
		remove(getTopIndex());
	}

}
