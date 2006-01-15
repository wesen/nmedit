package org.nomad.util.debug;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;



public abstract class CopyOfTest {

	private ArrayList testList = new ArrayList();
	private int errorCount = 0;
	
	public void test() {
		populate();
		runTests();
	}
	
	protected abstract void populate();
	
	protected void putTest(TestCase testCase) {
		testList.add(testCase);
	}
	
	protected void runTests() {		
		if (testList.size()==0) {
			System.out.println("No tests available.");
			return ;
		}
		
		errorCount = 0;
		for (int i=0;i<testList.size();i++) {
			String prefix = (i+1)+": "; 
			
			TestCase tcase = (TestCase) testList.get(i);

			System.out.print(prefix+tcase.getContext()+"/'"+tcase.getDescription()+"'...");
			try {
				startTest();
				tcase.run();
				String text = stopTest();
				System.out.println("[ok]");
				if (text.length()>0) {
					String pprefix = prefix+"\t> ";
					String[] lines = text.split("\n");
					for (int j=0;j<lines.length;j++) {
						System.out.println(pprefix+lines[j]);
					}
					
				}
			} catch (Throwable t) {
				System.out.println("[failed]");

				errorCount++;
				System.out.println(prefix+"cause: "+(t.getMessage()==null?"unknown":t.getMessage()));
				prefix+="e: ";
				StackTraceElement[] ste = t.getStackTrace();
				for (int j=0;j<ste.length;j++) {
					StackTraceElement traceEl = ste[j];
					Class inClass = null;
					try {
						inClass = Class.forName(traceEl.getClassName());

						if (!(TestCase.class.isAssignableFrom(inClass)
						      ||CopyOfTest.class.isAssignableFrom(inClass)) ) {
							System.out.println(prefix+"line "+traceEl.getLineNumber()+": "+traceEl.getClassName()
									+"/"+traceEl.getMethodName());
						}

					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		}

		System.out.println("---------------------------");
		System.out.println("All tests done.");
		System.out.println("Tests : "+testList.size());
		System.out.println("Errors: "+errorCount);
	}

	private PrintStream oldOut = null;
	private ByteArrayOutputStream data = null;
	
	private void revert() {
		if (oldOut!=null) {
			System.setOut(oldOut);
			oldOut = null;
		}
	}
	
	private void startTest() {
		revert();
		oldOut = System.out;
		data = new ByteArrayOutputStream();
		System.setOut(new PrintStream(data));
	}

	private String stopTest() {
		revert();
		String result = "";
		if (data!=null) {
			result = data.toString();
			data = null;
		}
		return result;
	}
	
}
