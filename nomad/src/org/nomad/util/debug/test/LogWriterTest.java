package org.nomad.util.debug.test;

import org.nomad.util.debug.Logger;
import org.nomad.util.debug.Test;
import org.nomad.util.debug.TestCase;

public class LogWriterTest extends Test {

	public LogWriterTest() {
		super();
	}

	protected void populate() {
		putTest(new TestCase("LogWriter", "Output test"){
			public void run() {
				Logger w = new Logger(System.out);
				w.println("a line");
				w.println("a line");
				w.print("no line, ");
				w.print("no line, but line separator "+System.getProperty("line.separator"));
				
				assertion(true, "hahah");
			}});
		putTest(new TestCase("LogWriter", "Contextual Output"){
			public void run() {
				Logger w = new Logger(System.out);
				w.println("a line");
				w.println("a line");
				w.print("no line, ");
				w.print("no line, but line separator "+System.getProperty("line.separator"));
				
				w.enterCascade(getClass());
				w.println("new context, a line");
				w.println("new context, a line");
				w.enterCascade();
				w.print("new2 context, no line, ");
				w.exitCascade();
				w.print("new2 context");
				w.print(", no line, but line separator "+System.getProperty("line.separator"));
				w.exitCascade();

				w.println("I'm done");
			}});
	}

}
