package nomad.application;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.SwingUtilities;

import nomad.application.ui.Nomad;

import java.awt.*;
import java.lang.Math;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Christian Schneider
 */
public class AppRunner
{
	/**
	 * @param args The command line arguments
	 */
	public static void main(String[] args)
	{
		try {
			SwingUtilities.invokeAndWait(new App());
		}
		catch (InterruptedException e) {
			// Ignore: If this exception occurs, we return too early, which
			// makes the splash window go away too early.
			// Nothing to worry about. Maybe we should write a log message.
		}
		catch (InvocationTargetException e) {
			// Error: Startup has failed badly.
			// We can not continue running our application.
			InternalError error = new InternalError();
			error.initCause(e);
			throw error;
		}
	}

    final static class App implements Runnable {
	    public void run()
	    {
		    try { 
		    	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
		    } catch (Exception e) { 
		    	e.printStackTrace();
		    }
		
		    Nomad frame = null;
		
		    try {
		        frame = new Nomad();
		    }
		    catch (Exception e){
		    	e.printStackTrace();
		        System.exit(1);
		    }
		    frame.validate();
		
		    // center window
		    Dimension screensz  = Toolkit.getDefaultToolkit().getScreenSize();
		    Dimension framesz   = frame.getSize();
		
		    framesz.height = Math.min(framesz.height, screensz.height);
		    framesz.width  = Math.min(framesz.width,  screensz.width);
		
		    frame.setLocation(
		      (screensz.width-framesz.width)/2,
		      (screensz.height-framesz.height)/2
		    );
		
		    // set close operation, then show window
		    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		    frame.setVisible(true);
		    frame.toFront();
	    }
      }
  
}
