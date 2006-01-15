package org.nomad.util.misc;

import java.util.ArrayList;

public class Stack extends ArrayList {

	public Stack() {
		
	}
	
	public Stack(Object top) {
		if (top!=null)
			push(top);
	}
	
	public void push(Object obj) {
		add(obj);
	}
	
	public int getTopIndex() {
		return size()-1;
	}
	
	public Object top() {
		return get(getTopIndex());
	}
	
	public void pop() {
		remove(getTopIndex());
	}

}
