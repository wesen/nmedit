package editor;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Vector;

public class UIXMLFileWriter extends PrintStream {

	private int offset = 0;
	private boolean freshLine = true;
	private Vector elementStack = new Vector();

	public UIXMLFileWriter(String file) throws FileNotFoundException {
		super(new FileOutputStream(file));
		prolog();
	}
	
	public void enteredSection(boolean enter) {
		offset += enter?+1:-1;
		if (offset<0) {
			System.err.println("** Warning Negative offset in "+getClass().getName());
			offset = 0;
		}
	}

	private boolean closed = false;
	
	public void close() {
		if (!closed) {
			epilog();
			closed = true;
		}
		super.close();
	}
	
	private void push(String element) {
		elementStack.add(element);
	}
	
	private String top() {
		if (elementStack.size()==0)
			return null;
		else 
			return (String) elementStack.lastElement();
	}
	
	private String pop() {
		if (elementStack.size()==0)
			return null;
		else {
			String top = (String) elementStack.lastElement();
			elementStack.remove(top);
			return top;
		}
	}

	private void prolog() {
		println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		println("<!DOCTYPE ui-description SYSTEM \"ui-description.dtd\">");
		beginTag("ui-description", true);
	}

	private void epilog() {
		endTag();
	}

	private void addLineOffset() {
		if (freshLine) {
			for (int i=0;i<offset;i++)
				super.print("  ");
			freshLine = false;
		}
	}

	public void beginTag(String elementName, boolean hasChildren) {
		beginTagStart(elementName);
		beginTagFinish(hasChildren);
	}
	
	public void beginTagStart(String elementName) {
		print("<"+elementName);
		push(elementName);
	}

	public void addAttribute(String attributeName, String value) {
		print(" "+attributeName+"=\""+value+"\"");
	}

	public void beginTagFinish(boolean hasChildren) {
		if (hasChildren) {
			println(">");
			enteredSection(true);
		} else {
			println(" />");
			pop();
		}
	}

	public void endTag() {
		enteredSection(false);
		println("</"+pop()+">");
	}

	public void print(boolean b) {addLineOffset();super.print(b);}
	public void print(char c) {addLineOffset();super.print(c);}
	public void print(char[] s) {addLineOffset();super.print(s);}
	public void print(double d) {addLineOffset();super.print(d);}
	public void print(float f) {addLineOffset();super.print(f);}
	public void print(int i) {addLineOffset();super.print(i);}
	public void print(long l) {addLineOffset();super.print(l);}
	public void print(Object obj) {addLineOffset();super.print(obj);}
	public void print(String s) {addLineOffset();super.print(s);}
	 
	public void println() {addLineOffset();super.println();freshLine=true;}
	public void println(boolean x) {addLineOffset();super.println(x);freshLine=true;}
	public void println(char x) {addLineOffset();super.println(x);freshLine=true;}
	public void println(char[] x) {addLineOffset();super.println(x);freshLine=true;}
	public void println(double x) {addLineOffset();super.println(x);freshLine=true;}
	public void println(float x) {addLineOffset();super.println(x);freshLine=true;}
	public void println(int x) {addLineOffset();super.println(x);freshLine=true;}
	public void println(long x) {addLineOffset();super.println(x);freshLine=true;}
	public void println(Object x) {addLineOffset();super.println(x);freshLine=true;}
	public void println(String x) {addLineOffset();super.println(x);freshLine=true;}
	
	
}
