package net.sf.nmedit.nomad.xml;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Stack;

/**
 * A class that allows to write xml files.
 * 
 * @author Christian Schneider
 */
public class XMLFileWriter extends PrintStream {

	// line offset, or height of current node
	private int offset = 0;
	// if we are in a new line 
	private boolean freshLine = true;
	// stack containing the nodes from the current node to the top node
	private Stack<String> elementStack = new Stack<String>();

	// current tag is a closed tag
	private boolean closed = false;
	
	/**
	 * Creates a new xml file. xmlProlog will be inserted at the beginning of the file.
	 * @param file the file name
	 * @param xmlProlog the prolog
	 * @param docType the doc type declaration
	 * @throws FileNotFoundException
	 */
	public XMLFileWriter(String file, String xmlProlog, String docType) throws FileNotFoundException {
		super(new FileOutputStream(file));
		prolog(xmlProlog, docType);
	}

	/**
	 * Updates the offset. The offset is incremented if a new section has been entered,
	 * otherwise it is decremented.
	 * @param enter
	 */
	private void enteredSection(boolean enter) {
		offset += enter?+1:-1;
		if (offset<0) {
			System.err.println("** Warning Negative offset in "+getClass().getName());
			offset = 0;
		}
	}

	/**
	 * Closes the file. This method must be called, otherwise the xml file
	 * might not be valid or does not contain all information.
	 */
	public void close() {
		if (!closed) {
			epilog();
			closed = true;
		}
		super.close();
	}
	
	/**
	 * Pushes an element to the stack
	 * @param element the element
	 */
	private void push(String element) {
		elementStack.push(element);
	}
	
	/*
	private String top() {
		if (elementStack.size()==0)
			return null;
		else 
			return (String) elementStack.lastElement();
	}*/
	
	/**
	 * Removes an element from the stack.
	 * If the stack was empty null is returned, otherwise the name
	 * of the element is returned
	 * @return the element name
	 */
	private String pop() {
		if (elementStack.isEmpty())
			return null;
		else {
			String top = elementStack.pop();
			return top;
		}
	}

	/**
	 * Writes the prolog for the xml file.
	 * @param xmlProlog
	 * @param docType
	 */
	protected void prolog(String xmlProlog, String docType) {
		println(xmlProlog);
		println(docType);
	}

	/**
	 * Writes the epilog for the xml file.
	 */
	protected void epilog() {
		//
	}

	/**
	 * If the current line is a new line spaces are
	 * inserted according to the offset attribute.
	 */
	private void addLineOffset() {
		if (freshLine) {
			for (int i=0;i<offset;i++)
				super.print("  ");
			freshLine = false;
		}
	}

	/**
	 * Writes a new tag. If hasChildren is true endTag must be called later.
	 * @param elementName
	 * @param hasChildren true if the node has children
	 */
	public void beginTag(String elementName, boolean hasChildren) {
		beginTagStart(elementName);
		beginTagFinish(hasChildren);
	}
	
	/**
	 * Writes a new tag, but does not finish it for now, so that attributes
	 * can be written.
	 * @param elementName
	 */
	public void beginTagStart(String elementName) {
		print("<"+elementName);
		push(elementName);
	}

	/**
	 * Writes an attribute for the current element.
	 * @param attributeName
	 * @param value
	 */
	public void addAttribute(String attributeName, String value) {
		print(" "+attributeName+"=\""+strToXMLstr(value)+"\"");
	}

	/**
	 * Finishes the tag. If hasChildren is true endTag must be called later.
	 * @param hasChildren
	 */
	public void beginTagFinish(boolean hasChildren) {
		if (hasChildren) {
			println(">");
			enteredSection(true);
		} else {
			println(" />");
			pop();
		}
	}

	/**
	 * Writes the end tag for the current element.
	 */
	public void endTag() {
		enteredSection(false);
		println("</"+pop()+">");
	}
	
	/**
	 * Removes illegal characters from string.
	 * For now the '&' character is the only one replaced (by '&amp;amp;')
	 * @param str the string
	 * @return the xml conform string
	 */
	public String strToXMLstr(String str) {
		return str.replaceAll("&","&amp;");
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
