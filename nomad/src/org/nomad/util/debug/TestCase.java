package org.nomad.util.debug;

public abstract class TestCase {

	private String context = null;
	private String description = null;

	public TestCase(String context, String description) {;
		this.context = context;
		this.description = description;
	}

	public String getContext() {
		return context;
	}

	public String getDescription() {
		return description;
	}

	public void assertion(String message) {
		throw new AssertionError(message);
	}

	public void assertion(boolean condition, String description) {
		if (condition)
			throw new AssertionError("Condition '"+description+"' failed.");
	}
	
	public void assertion(boolean condition, String description, String message) {
		if (condition)
			throw new AssertionError("Condition '"+description+"' failed: "+message);
	}
	
	public abstract void run();
	
}
