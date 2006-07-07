/* Copyright (C) 2006 Christian Schneider
 * 
 * This file is part of Nomad.
 * 
 * Nomad is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Nomad is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Nomad; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

/*
 * Created on Mar 31, 2006
 */
package net.sf.nmedit.nomad.core.application;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.swing.JFrame;

import net.sf.nmedit.nomad.core.application.splash.SplashScreen;

public abstract class Application
{

    // ---- application ----

    /**
     * Command line arguments
     */
    private final String[] commandLineArgs;

    /**
     * Creates a new application instance. 
     * When the argument is <code>null</code>,
     * an empty String array is used. 
     * 
     * @param args the command line arguments.
     */
    public Application( String[] args )
    {
        commandLineArgs = args == null ? new String[0] : args;
    }

    /**
     * Returns the command line arguments.
     * 
     * @return the command line arguments
     */
    public String[] getCommandLineArgs()
    {
        return commandLineArgs.clone();
    }

    /**
     * Realises the initialisation of the application. This method must not be
     * called directly. It will be called only once, when the application is
     * started.
     * 
     * @param progress
     *            the progressmeter is used to display progress messages on the
     *            splash screen
     * @throws ApplicationInstantiationException
     *             if starting the application failed and should be stopped
     */
    protected abstract void startInternal( ProgressMeter progress )
            throws ApplicationInstantiationException;

    /**
     * Realises the freeing of the application. The method must not be called
     * directly. To stop the application use {@link #stop()}. It will be called
     * only once, when the application is stopped.
     * 
     * @see #stop()
     */
    protected abstract void stopInternal();

    /**
     * Realises a restart of the application. The method should never be called
     * directly. To restart the application use {@link #restart()}.
     * 
     * @throws UnsupportedOperationException
     *             if the operation is not supported
     * @see #restart()
     */
    protected void restartInternal()
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Modifies the frames default closing operation and adds a window listener.
     * If the window listener receives the event
     * {@link java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)}
     * it calls {@link #stop()}.
     * 
     * @param main
     *            the main window
     * @see JFrame#setDefaultCloseOperation(int)
     */
    protected void configureMainFrame( JFrame main )
    {
        main.addWindowListener( AppStopper.instance );
        main.setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
    }

    // ---- static ----

    /**
     * instance of the application
     */
    private static Application instance           = null;

    /**
     * the logger
     */
    private static Logger      log                = null;

    /**
     * the application's properties
     */
    private static Properties  properties         = new Properties();

    /**
     * true, if {@link #start()} has been called
     */
    private static boolean     applicationStarted = false;

    /**
     * true, if {@link #stop()} has been called
     */
    private static boolean     applicationStopped = false;

    /**
     * Sends a status message to the logger.
     * 
     * @param status
     *            the status message
     */
    public static void status( String status )
    {
        log.println( "[STATUS] " + status );
    }

    /**
     * Returns a property
     * 
     * @param key
     *            key of the property
     * @return a property
     */
    public static String getProperty( String key )
    {
        return properties.getProperty( key );
    }


    public static int getIntegerProperty( String key, int alternative )
    {
        String sint = properties.getProperty(key);
        if (sint == null)
            return alternative;
        
        try
        {
            return Integer.parseInt(sint);
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
            return alternative;
        }
    }

    /**
     * Sets the properties value. If key is null, the property will be removed.
     * 
     * @param key
     *            name of the property
     * @param value
     *            value of the property or null if the property should be
     *            removed
     */
    public static void setProperty( String key, String value )
    {
        if (value != null)
        {
            properties.setProperty( key, value );
        }
        else
        {
            properties.remove( key );
        }
    }

    /**
     * Returns the name of the application. The information is extracted from
     * the application properties using the key {@link Const#APPLICATION_NAME}
     * 
     * @return the name of the application
     */
    public static String getName()
    {
        return getProperty( Const.APPLICATION_NAME );
    }

    /**
     * Returns the version of the application. The information is extracted from
     * the application properties using the key
     * {@link Const#APPLICATION_VERSION}
     * 
     * @return the name of the application
     */
    public static String getVersion()
    {
        return getProperty( Const.APPLICATION_VERSION );
    }

    /**
     * Returns the instance of the current application.
     * 
     * @return the instance of the current application
     */
    public static Application getApplication()
    {
        return Application.instance;
    }

    /**
     * Returns the logger.
     * 
     * @return the logger
     */
    public static Logger getLog()
    {
        return log;
    }

    /**
     * Returns the application properties.
     * 
     * @return the application properties
     */
    public static Properties getProperties()
    {
        return properties;
    }

    public static void putPropertiesFromXML( String fileName )
            throws IOException
    {
        putPropertiesFromXML( new File( fileName ) );
    }

    public static void putPropertiesFromXML( File file ) throws IOException
    {
        FileInputStream in;
        try
        {
            in = new FileInputStream( file );
        }
        catch (FileNotFoundException e)
        {
            throw e;
        }

        try
        {
            properties.loadFromXML( in );
        }
        catch (InvalidPropertiesFormatException e)
        {
            throw e;
        }
        catch (IOException e)
        {
            throw e;
        }
        finally
        {
            try
            {
                in.close();
            }
            catch (Exception e)
            {

            }
        }
    }

    /**
     * @see #storeProperties(File)
     */
    public static void storeProperties( String fileName )
            throws FileNotFoundException, IOException
    {
        storeProperties( new File( fileName ) );
    }

    /**
     * Calls {@link #storeProperties(String, File)} using the regular expression
     * {@link Const#CUSTOM_PROPERTY_PREFIX_STRING}.
     * 
     * @see #storeProperties(String, File)
     */
    public static void storeProperties( File file )
            throws FileNotFoundException, IOException
    {
        storeProperties( Const.CUSTOM_PROPERTY_PREFIX_MATCHER, file );
    }

    /**
     * Stores a subset of the available properties. Only properties that match
     * the regular expression are stored.
     * 
     * @param keyRegExpression
     *            regular expression
     * @param file
     *            target file
     */
    public static void storeProperties( String keyRegExpression, File file )
            throws FileNotFoundException, IOException
    {
        Pattern pattern = Pattern.compile( keyRegExpression );
        Properties custom = new Properties();
        for (Object okey : properties.keySet())
        {
            String key = (String) okey;
            if (pattern.matcher( key ).matches())
            {
                custom.setProperty( key, properties.getProperty( key ) );
            }
        }

        FileOutputStream out;
        try
        {
            out = new FileOutputStream( file );
        }
        catch (FileNotFoundException e)
        {
            throw e;
        }

        try
        {
            custom.storeToXML( out, "custom properties" );
            out.flush();
        }
        finally
        {
            try
            {
                out.close();
            }
            catch (Exception e)
            {
                // ignore
            }
        }
    }

    /**
     * Starts the application.
     * 
     * @throws ApplicationInstantiationException
     *             if the application has not started successfully und must be
     *             stopped.
     * @throws IllegalStateException
     *             if an application has started before
     */
    private static void start( ProgressMeter progress )
            throws ApplicationInstantiationException
    {
        status( "Starting application." );

        if (Application.applicationStarted)
        {
            throw new IllegalStateException( "Application can only start once." );
        }
        else
        {
            Application.applicationStarted = true;

            synchronized (Application.instance)
            {
                Application.instance.startInternal( progress );
            }
        }
    }

    /**
     * Stops the application.
     * 
     * @throws IllegalStateException
     *             if no application instance is running
     */
    public static void stop()
    {
        status( "Stopping application..." );
        if (!Application.isRunning())
        {
            throw new IllegalStateException( "Application is not running." );
        }
        else
        {
            try
            {
                Application.instance.stopInternal();
                Application.applicationStopped = true;
            }
            finally
            {
                Application.instance = null;
                // logger.close();
                // System.exit(0);
            }
        }
    }

    /**
     * Restarts the application if the operation is supported.
     * 
     * @throws IllegalStateException
     *             if no application instance is running
     */
    public static void restart()
    {
        status( "restarting application..." );
        if (!Application.isRunning())
        {
            throw new IllegalStateException( "Application is not running." );
        }
        else
        {
            try
            {
                Application.instance.restartInternal();
            }
            catch (UnsupportedOperationException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns true if the application is running.
     * 
     * @return true if the application is running.
     */
    public static boolean isRunning()
    {
        return applicationStarted && ( !applicationStopped );
    }

    /**
     * Creates an instance of {@link Application}. This method must be called
     * only once. Before calling the application's properties must be set. The
     * logger will write the log the file obtained using the key
     * {@link Const#APPLICATION_LOGFILE}. If the file can not be created, or
     * the file name is <code>null</code>, the logger will write to
     * {@link System#out}. If the property {@link Const#KEY_SPLASH_IMAGE}
     * contains an URL or filename and the image file is loaded a splash screen
     * will display this image until the application is loaded. If no image can
     * be found, no splash screen will appear. The {@link Application} class
     * name is obtained using the key {@link Const#APPLICATION_CLASS_NAME}. The
     * application name is obtained using the key {@link Const#APPLICATION_NAME}.
     * The application version is obtained using the key
     * {@link Const#APPLICATION_VERSION}. If the property
     * {@link Const#APPLICATION_PROPERTYFILE} is set, it is tried to load the
     * application properties from the given XML file.
     * 
     * @param args
     *            the command line arguments
     * @throws ApplicationInstantiationException
     *             if the application could not be started
     * @see Const#APPLICATION_CLASS_NAME
     * @see Const#APPLICATION_NAME
     * @see Const#APPLICATION_VERSION
     * @see Const#KEY_SPLASH_IMAGE
     * @see Const#APPLICATION_LOGFILE
     * @see Const#APPLICATION_PROPERTYFILE
     */
    public static void create( String[] args )
            throws ApplicationInstantiationException
    {
        // check if an application instance already exists
        if (Application.instance != null)
        {
            throw new ApplicationInstantiationException(
                    "Only one application instance possible at the same time." );
        }

        // create log file
        Application.log = Logger
                .create( getProperty( Const.APPLICATION_LOGFILE ) );
        log.printHeader();

        // create splash

        log.print( "property file:" );
        String propertyFile = getProperty( Const.APPLICATION_PROPERTYFILE );
        if (getProperty( Const.APPLICATION_LOGFILE ) == null)
        {
            log.println();
        }
        else
        {
            log.println( propertyFile );

            try
            {
                putPropertiesFromXML( propertyFile );
            }
            catch (IOException e)
            {
                throw new ApplicationInstantiationException( e );
            }
        }

        SplashScreen splash = new SplashScreen(
                getProperty( Const.KEY_SPLASH_IMAGE ) );

        try
        {
            splash.advance();
        }
        catch (Exception e)
        {
            splash = null;
            e.printStackTrace();
        }

        // wether the splash should disposed immediately or not
        boolean disposeSplashImmediately = false;

        try
        {
            // create class
            try
            {
                String className = getProperty( Const.APPLICATION_CLASS_NAME );

                if (className == null)
                {
                    throw new ApplicationInstantiationException(
                            "Class name in application properties not set." );
                }

                Class clazz = Class.forName( className );
                Constructor constructor = clazz.getConstructor( new Class[]
                    { String[].class } );

                Application.instance = (Application) constructor
                        .newInstance( new Object[]
                            { args } );
            }
            catch (InvocationTargetException e)
            {
                throw new ApplicationInstantiationException( e
                        .getTargetException() );
            }
            catch (Exception e)
            {
                throw new ApplicationInstantiationException( e );
            }

            try
            {
                Application.start( new SplashProgressMeter( splash ) );
            }
            catch (ApplicationInstantiationException e)
            {
                disposeSplashImmediately = true;
                try
                {
                    Application.stop();
                }
                catch (Throwable t)
                {
                    // we don't show this error since the application might be
                    // in
                    // an illegal state
                }

                throw e;
            }

        }
        finally
        {
            if (splash != null)
            {
                try
                {
                    splash.dispose( disposeSplashImmediately );
                }
                catch (Throwable t)
                {
                    t.printStackTrace();
                }
            }
        }
    }

    private static class SplashProgressMeter implements ProgressMeter
    {
        private SplashScreen splashScreen;

        public SplashProgressMeter( SplashScreen splashScreen )
        {
            this.splashScreen = splashScreen;
        }

        public void increment( String progressMessage )
        {
            status( "[PROGRESS] " + progressMessage );
            if (splashScreen != null)
            {
                splashScreen.updateStatus( progressMessage );
            }
        }
    }

    /**
     * A {@link WindowAdapter} that will call the method
     * {@link Application#stop()} when it receives the event
     * {@link java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)}.
     * 
     * @author Christian Schneider
     */
    private static class AppStopper extends WindowAdapter
    {

        /**
         * instance of the class
         */
        public final static AppStopper instance = new AppStopper();

        public void windowClosing( WindowEvent event )
        {
            if (Application.isRunning())
            {
                Application.stop();
            }
        }
    }

}
