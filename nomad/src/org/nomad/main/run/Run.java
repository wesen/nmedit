package org.nomad.main.run;

import java.awt.Point;
import java.io.File;
import java.net.MalformedURLException;

import org.nomad.dialog.ExceptionNotificationDialog.NoamdExceptionHandler;


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
	public static void main(String[] args) throws MalformedURLException {
		runMainIn("org.nomad.main.run.AppRunner", args);
	}
	
	public static void runMainIn(String className, String[] args){
		  NoamdExceptionHandler.setAsDefaultHandler();
		  
		  File file = new File("data/images/splash.jpg");
			try {
				SplashWindow.splash(file.toURL(), new Point(76,272));
			} catch (MalformedURLException e) {
				// this will not happen
			}  

			 /*
			  * Invokes the main method of the provided class name.
			  * @param className The class name 
			  * @param args the command line arguments
			  */
		    try
		    {
		      Class.forName(className)
		        .getMethod("main", new Class[] {String[].class})
		        .invoke(null, new Object[] {args});
		    }
		    catch (Exception e)
		    {
		      InternalError error =
		        new InternalError("Failed to invoke main method");
		      error.initCause(e);
		      throw error;
		    }
		  
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