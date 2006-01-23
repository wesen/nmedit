package org.nomad.main.run;

import java.awt.Point;
import java.io.File;
import java.net.MalformedURLException;


/**
 * @author Christian Schneider
 */
public class Run
{
  /**
   * Shows the splash screen, launches the application and then disposes
   * the splash screen.
   * @param args the command line arguments
 * @throws MalformedURLException 
   */
  public static void main(String[] args) throws MalformedURLException
  {
	  runMainIn("org.nomad.main.run.AppRunner", args);
  }
  
  public static void runMainIn(String classname, String[] args){
		File file = new File("data/images/splash.jpg");
		try {
			SplashWindow.splash(file.toURL(), new Point(76,272));
		} catch (MalformedURLException e) {
			// this will not happen
			if (!file.exists())
				System.err.println("Splash image not found.");
		}  
	  SplashWindow.invokeMain(classname, args);
	  SplashWindow.disposeSplash();
  }
  
  /**
   * Sets the statusMessage property of the splash window object.
   * @param message the status message 
   */
  public static void statusMessage(String message) {
	  SplashWindow.statusMessage(message);
  }
}