package main;

public class Debug {
	static boolean on; 
	
	Debug(){
	}
	
	static void set(boolean on){
		Debug.on = on;
	}
	
      static void println(){
          if (on){
            System.out.println();
          }
        }


      static void println(boolean b){
          if (on){
              System.out.println(System.currentTimeMillis() + " " + (b?"true":"false"));
          }
        }

  static void println(String text){
    if (on){
      System.out.println(System.currentTimeMillis() + " " + text);
    }
  }

  static void println(int text){
    if (on){
      System.out.println(System.currentTimeMillis() + " " + text);
    }
  }
}
