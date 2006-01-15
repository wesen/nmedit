package org.nomad.util.debug;

import java.util.ArrayList;

public abstract class Test {

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
			TestCase tcase = (TestCase) testList.get(i);


			Logger.log.enterCascade(tcase.getContext(), tcase.getDescription());
			try {
				tcase.run();
			} catch (Throwable t) {
				Logger.log.enterCascade("error");
				
				t.printStackTrace(Logger.log);
				/*
				System.out.println((t.getMessage()==null?"unknown":t.getMessage()));
				{
					StackTraceElement[] ste = t.getStackTrace();
					for (int j=0;j<ste.length;j++) {
						StackTraceElement traceEl = ste[j];
						Class inClass = null;
						try {
							inClass = Class.forName(traceEl.getClassName());
	
							if (!(TestCase.class.isAssignableFrom(inClass)
							      ||Test.class.isAssignableFrom(inClass)) ) {
								System.out.println("line "+traceEl.getLineNumber()+": "+traceEl.getClassName()
										+"/"+traceEl.getMethodName());
							}
	
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
					}
				}
				*/
				Logger.log.exitCascade();
			}
			Logger.log.exitCascade();
		}
	}

	public int getErrorCount() {
		return errorCount;
	}
	
	public int getTestCount() {
		return testList.size();
	}
	
}
