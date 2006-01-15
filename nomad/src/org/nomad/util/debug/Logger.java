package org.nomad.util.debug;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class Logger extends PrintStream {

	int stackElements = 0;

	public static Logger log = new Logger(System.out);

	public Logger(OutputStream out) {
		super(out);
	}
	
	public Logger(OutputStream out, boolean autoFlush) {
		super(out, autoFlush);
	}

	public Logger(OutputStream out, boolean autoFlush, String encoding)
			throws UnsupportedEncodingException {
		super(out, autoFlush, encoding);
	}

	public Logger(String file) throws FileNotFoundException {
		super(file);
	}

	public Logger(String fileName, String csn) throws FileNotFoundException,
			UnsupportedEncodingException {
		super(fileName, csn);
	}

	public Logger(File file) throws FileNotFoundException {
		super(file);
	}

	public Logger(File file, String csn) throws FileNotFoundException,
			UnsupportedEncodingException {
		super(file, csn);
	}

	private final static String cascade = "|  ";

	public void exitCascade() {
		if (stackElements<=0) {
			throw new IllegalStateException("Cascade stack is empty.");
		} else {
			stackElements --;
			
			CascadableOutputStream cascade = (CascadableOutputStream) out;

			if (cascade.hasUnfinishedLine())
				println();
			
			try {
				cascade.flush();
			} catch (IOException e) {
			}


			out = cascade.out();
			if (out instanceof CascadableOutputStream) 
				((CascadableOutputStream)out).resetLine();
		}
	}

	protected final void setupCascade(CascadeModel model) {
		stackElements ++;
		if (out instanceof CascadableOutputStream) {
			CascadableOutputStream cascade = (CascadableOutputStream) out;
			if (cascade.hasUnfinishedLine())
				println();
			
			cascade.resetLine();
		}
		
		out = model == null
			? new CascadableOutputStream(out, cascade)
			: new CascadableOutputStream(out, model);
	}

	public void enterCascade() {
		setupCascade(null);
	}

	public void enterCascade(CascadeModel model) {
		setupCascade(model);
	}

	public void enterCascade(Class theClass) {
		enterCascade(theClass.getName());
	}
	
	public void enterCascade(String str) {
		println("---["+str+"]");
		enterCascade();
	}
	
	public void enterCascade(String label, String text) {
		println("---["+label+"] - "+text);
		enterCascade();
	}
	
}
