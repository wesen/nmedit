package org.nomad.main.run;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.nomad.main.Nomad;
import org.nomad.util.misc.NomadUtilities;

/**
 * @author Christian Schneider
 */
public class AppRunner implements Runnable 
{
	/**
	 * @param args The command line arguments
	 */
	public static void main(String[] args)
	{
		try {
			SwingUtilities.invokeAndWait(new AppRunner());
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

    public void run()
    {
	    try { 
	    	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
	    } catch (Exception e) { 
	    	e.printStackTrace();
	    }
	
	    Nomad nomad = new Nomad();
		NomadUtilities.setupAndShow(nomad, 0.75, 0.75);
	    
	    //nomad.toFront();
	    nomad.initialLoading();
    }
  
}
