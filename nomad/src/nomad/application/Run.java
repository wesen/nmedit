package nomad.application;

import java.awt.Point;

/**
 * @author Christian Schneider
 */
public class Run
{
  /**
   * Shows the splash screen, launches the application and then disposes
   * the splash screen.
   * @param args the command line arguments
   */
  public static void main(String[] args)
  {
    SplashWindow.splash(Run.class.getResource("/data/images/splash.gif"), new Point(76,272));
    SplashWindow.invokeMain("nomad.application.AppRunner", args);
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