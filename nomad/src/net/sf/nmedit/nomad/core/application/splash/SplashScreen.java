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
 * Created on Mar 29, 2006
 */
package net.sf.nmedit.nomad.core.application.splash;

import java.awt.Frame;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;

import net.sf.nmedit.nomad.core.application.Application;

public class SplashScreen
{

    public final static int MINIMUM_DISPLAY_TIME = 1500;

    private URL             imageURL;

    private Image           splashImage;

    private SplashWindow    splashWindow;

    private boolean         disposed             = false;

    private long            splashDisplayTime    = 0;

    public SplashScreen( String imageLocation )
    {
        this( SplashScreen.decideURL( imageLocation ) );
    }

    public SplashScreen( URL imageURL )
    {
        this.imageURL = imageURL;
        this.splashImage = null;
    }

    public boolean isSplashDisplayed()
    {
        return splashWindow != null;
    }

    public void advance() throws FileNotFoundException
    {
        if (isSplashDisplayed())
        {
            throw new IllegalStateException();
        }

        Application.status( "Loading splash:" + imageURL );
        splashImage = SplashScreen.imageFromURL( imageURL );

        if (splashImage == null)
        {
            throw new FileNotFoundException( "image not found: " + imageURL );
        }

        splashWindow = new SplashWindow( new Frame(), splashImage );

        if (splashWindow.getWidth() <= 0 || splashWindow.getHeight() <= 0)
        {
            System.err.println( "image not found or corrupted: " + imageURL );
            disposed = true;
            splashWindow.dispose();
            splashWindow = null;
            splashImage.flush();
            splashImage = null;
            return;
        }
        else
        {
            splashWindow.setVisible( true );
            splashWindow.waitUntilVisible();

            // remember time before application has started
            splashDisplayTime = System.currentTimeMillis();
        }

    }

    public void updateStatus( String progressMessage )
    {
        if (splashWindow != null)
        {
            splashWindow.updateStatus( progressMessage );
        }
    }

    public void dispose( boolean immediately )
    {
        if (disposed)
        {
            return;
        }

        try
        {
            if (!immediately)
            {
                // measure application startup duration
                splashDisplayTime = System.currentTimeMillis()
                        - splashDisplayTime;

                // make sure the splash will be displayed for the minimum
                // duration
                if (splashDisplayTime < MINIMUM_DISPLAY_TIME)
                {
                    try
                    {
                        synchronized (splashWindow)
                        {
                            splashWindow
                                    .wait( SplashScreen.MINIMUM_DISPLAY_TIME
                                            - splashDisplayTime );
                        }
                    }
                    catch (InterruptedException e)
                    {
                        // The splash window will be disposed earlier than
                        // expected.
                        // We don't have a critical exception here.
                    }
                }
            }
        }
        finally
        {
            disposed = true;
            splashWindow.disposeSplash();
            splashWindow = null;
            splashImage.flush();
        }
        splashImage = null;
    }

    public static URL decideURL( String splashImageLocation )
    {
        URL url = null;

        if (splashImageLocation != null)
        {
            File file = new File( splashImageLocation );
            try
            {
                url = file.exists() ? file.toURL() : new URL(
                        splashImageLocation );
            }
            catch (MalformedURLException e)
            {
                url = null;
            }
        }

        return url;
    }

    public static Image imageFromURL( URL imageURL )
    {
        if (imageURL != null)
        {
            try
            {
                return Toolkit.getDefaultToolkit().createImage( imageURL );
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return null;
            }
        }
        else
        {
            return null;
        }
    }

}
