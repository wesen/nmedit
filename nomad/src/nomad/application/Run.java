package nomad.application;

import java.awt.Point;

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
  
  public static void statusMessage(String message) {
	  SplashWindow.statusMessage(message);
  }
}