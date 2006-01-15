package org.nomad.util.debug;



public class Tester {

	public static void main(String[] args) {
		System.setOut(Logger.log);
		(new Tester()).run();
	}

	public void run() {
		ClassDelegateIterator iter = new 
			ClassDelegateIterator (
				new ClassIterator (
					new JavaFileIterator (
						new ResourceIterator(".")
					)
				),
				Test.class, true
			);
		
		int testCount = 0;
		int errorCount = 0;
		
		while (iter.hasNext()) {
			try {
				Test test = (Test) iter.nextDelegate().newInstance(); 
				Logger.log.enterCascade("Testsuite", test.getClass().getName());
				test.test();

				testCount+=test.getTestCount();
				errorCount+=test.getErrorCount();
				
				Logger.log.exitCascade();
			} catch (InstantiationException e) {
				System.out.println(e);
			} catch (IllegalAccessException e) {
				System.out.println(e);
			}
		}
		
		System.out.println("----------------");
		System.out.println("Tests : "+testCount);
		System.out.println("Errors: "+errorCount);
		
	}
	
}

