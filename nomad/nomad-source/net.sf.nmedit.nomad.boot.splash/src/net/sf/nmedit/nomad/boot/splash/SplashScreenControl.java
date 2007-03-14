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
 * Created on Nov 20, 2006
 */
package net.sf.nmedit.nomad.boot.splash;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A class implementing an advanced logic for controlling the splash screen window.
 * 
 * The splash screen is created asynchronously and does not block other operations.
 */
public abstract class SplashScreenControl implements ImageObserver
{

    // maximum time of the splash screen to stay visible in milliseconds
    // the minimum visibility time will never be larger than this value  
    public static final long MAX_VISIBILITY_TIME = 10000l; 
    
    // minimum time of the splash screen to stay visible in milliseconds
    private long minimumVisibilityTime;

    // lock used to synchronize advance() and dispose() calls
    private final Object lock = new Object();
    
    private final Object waitForPaintLock = new Object();
    
    // lock used by the thread and disposeImmediatelly()
    private Object threadLock = null;

    // indicates that advance() has already been called
    private boolean advanceCalledBefore = false;
    
    // indicates that dispose() has already been called
    private boolean disposeCalledBefore = false;
    
    // indicates that the splash screen should be closed without waiting
    private boolean disposeImmediately = false;
    
    // indicates that we don't have to close the splash screen
    private boolean splashScreenAborted = false;
    
    // absolute time when the splash was painted, in milliseconds
    private long splashPaintNotificationTime = 0l;
    
    // true when imageReady has been called
    private boolean imageReadyCalled = false;
    
    private boolean blockUntilImageAvailable = true;
    
    /**
     * Creates a splash screen.
     */
    public SplashScreenControl()
    {
        // use a default of 1.5 seconds as minimum visibility time
        setMinimumVisibilityTime(1500l);
    }

    /**
     * lock object used to synchronize advance() and dispose() calls
     */
    private final Object getLock()
    {
        return lock;
    }

    /**
     * Sets the minimum time in milliseconds for the splash screen to stay visible.
     * @param t the time in milliseconds
     */
    public final void setMinimumVisibilityTime(long t)
    {
        this.minimumVisibilityTime = Math.min(t, MAX_VISIBILITY_TIME);
    }
    
    /**
     * Returns the minimum time in milliseconds for the splash screen to stay visible.
     * @return the minimum time in milliseconds for the splash screen to stay visible
     */
    public long getMinimumVisibilityTime()
    {
        return minimumVisibilityTime;
    }
    
    /**
     * Creates and shows the splash screen.
     * 
     * The method is thread safe and subsequent calls are ignored.
     */
    public final void advance()
    {
        boolean validRequest = false;
        
        // avoid that the splash screen is created multiple times
        synchronized (getLock())
        {
            
            // assure advance()/dispose() was not called before
            if ((!advanceCalledBefore) && (!disposeCalledBefore))
            {
                // remember that advance() has been called
                advanceCalledBefore = true;
         
                validRequest = true;
            }
        }

        if (validRequest)
        {
            // retrive image location
            URL imageURL = null;
            try
            {
                imageURL = getSplashImageURL();
            }
            catch (MalformedURLException e)
            {
                // ignored - splash screen will not be created
            }
            if (imageURL != null)
            {
                Toolkit toolkit = Toolkit.getDefaultToolkit();
                // get the image
                Image splashImage = toolkit.getImage(imageURL);
                
                if (splashImage != null)
                {
                    // use the ImageObserver interface to monitor the image loading process
                    if (toolkit.prepareImage(splashImage, -1, -1, this))
                    {
                        // the image is already available - create splash now window
                        imageReady(splashImage);
                    }
                }
            }

            if (blockUntilImageAvailable)
            {
                while ((splashPaintNotificationTime==0) && (!isAbortRequested()))
                {
                    synchronized(waitForPaintLock)
                    {
                        try
                        {
                            waitForPaintLock.wait();
                        }
                        catch (InterruptedException e)
                        {
                            // ignore
                        }
                    }
                }
            }   
        }
    }

    public boolean imageUpdate( Image image, int infoflags, int x, int y, int width, int height )
    {
        // return value in case we want to abort loading the image
        final boolean IO_ABORT_LOADING = false;
        // return value in case we still want to construct the splash image
        final boolean IO_RETRIEVE_DATA = true;

        // check for errors or if there is a request to abort loading 
        if (((infoflags & (ERROR|ABORT)) != 0) || isAbortRequested())
        {
            setSplashScreenAborted();
            return IO_ABORT_LOADING;
        }

        // check if image is complete
        if ((infoflags & ALLBITS) != 0)
        {
            imageReady(image);
            
            // image is complete - we are done
            return IO_ABORT_LOADING;
        }

        // not done yet - get more data
        return IO_RETRIEVE_DATA;
    }
    
    private void imageReady(Image image)
    {
        if (!imageReadyCalled)
        {
            // in case advance() AND imageUpdate() send the image ready notification
            // this should not happen, but we have to be sure
            imageReadyCalled = true;
            
            // retrieve width and height of the image
            int w = image.getWidth(null);
            int h = image.getHeight(null);
            
            // dimensions should be >0, but we check them to be sure
            if ((w>0) && (h>0))
            {
                if (!isAbortRequested())
                  // create splash window
                  showSplashWindow(image, w, h);
                else
                    setSplashScreenAborted();
            }
            else
            {
                setSplashScreenAborted();
            }

        }
    }

    /**
     * Closes the splash screen if present.
     * The method is thread safe and subsequent calls are ignored.
     * 
     * When the splash screen is still loading, the loading process is
     * aborted and no splash screen will appear. 
     */
    public final void dispose()
    {
        boolean validRequest = false;
        
        synchronized (getLock())
        {
            // assure dispose() has not been called
            if (!disposeCalledBefore)
            {
                // remember that dispose() has been called
                disposeCalledBefore = true;
                
                validRequest = true;
            }
        }
        
        if (validRequest)
        {
            // now we have several conditions
            // 1. advance has not been called -> will not create splash screen when called later
            // 2. image/splash still loading -> will be aborted and no splash screen will shown up
            // 3. splash window created soon / will soon showup -> close splash
             
            if (advanceCalledBefore && (!splashScreenAborted))
            {
                // we have to stop the splash screen
                
                if (disposeImmediately || (System.currentTimeMillis()>=getSplashScreenTimeout()))
                {
                    // close it immediately because
                    // 1. it was requested, or
                    // 2. it was long enough visible
                    closeSplashWindow();
                }
                else
                {
                    // start a thread that will close the splash window after the timeout
                    (new Thread() 
                    {
                        public void run()
                        {
                            // set the thread lock object
                            threadLock = this;
                            
                            long timeout;   // splash screen timeout
                            long current;   // current time
                            // indicates that we can stop waiting and close the window now
                            boolean done = false;
                            
                            // the loop is stopped if
                            // 1. the thread was interrupted, or
                            // 2. done is true, or
                            // 3. disposeImmediately() has been called (for example when the user
                            // clicks on the splash window)
                            // 4. the splash screen has been aborted
                            while (!(Thread.interrupted()||done||disposeImmediately||splashScreenAborted))
                            {
                                timeout = getSplashScreenTimeout() ;
                                current = System.currentTimeMillis();

                                if (current>=timeout)
                                {
                                    done = true; // timeout reached - stop now
                                }
                                else
                                {
                                    synchronized (threadLock)
                                    {
                                        try
                                        {
                                            wait(timeout-current);
                                        }
                                        catch (InterruptedException e)
                                        {
                                            // save to ignore
                                            // this might be caused by a call
                                            // to disposeImmediately()
                                        }
                                    }
                                }
                            }

                            if (!splashScreenAborted)
                            {
                                // we are done and can finally close the splash window now
                                closeSplashWindow();
                            }
                        }
                    }       
                    ).start();
                }
            }
        }
    }

    /**
     * Closes the splash screen without waiting.
     * 
     * The method is thread safe and subsequent calls are ignored.
     */
    public final void disposeImmediately()
    {
        if (!disposeImmediately)
        {
            disposeImmediately = true;

            if (threadLock!=null)
            {
                synchronized (threadLock)
                {
                    // when the thread is waiting we have to wake it up
                    // otherwise the splash window might be open a while longer
                    threadLock.notifyAll();
                }
            }
            
            dispose();
        }
    }

    /**
     * Returns true when loading the splash screen should be aborted.
     * @return true when loading the splash screen should be aborted
     */
    protected final boolean isAbortRequested()
    {
        return disposeCalledBefore;
    }
    
    /**
     * Sets a flag, indicating that the splash screen loading was aborted.
     * closeSplashWindow may not be called when this flag is set. 
     */
    public final void setSplashScreenAborted()
    {
        splashScreenAborted = true;
        synchronized (waitForPaintLock)
        {
            waitForPaintLock.notifyAll();
        }
    }
    
    /**
     * Returns true when the splash screen loading has been aborted, and the splash
     * screen is already removed.
     * @return
     */
    public final boolean isSplashScreenAborted()
    {
        return splashScreenAborted;
    }
    
    /**
     * Notification that the splash window is painted.
     * Remembers the time of the first notification.
     * 
     * The time is used to calculate the timeout of the splash screen.
     * When this method is not called the value returned 
     * by {@link #getSplashScreenTimeout()} always indicates a timeout.
     * 
     * If this method is not called correctly this could cause the splash 
     * window to become visible and to be closed afterwards without giving
     * the user a chance to recognize it.
     */
    public final void notifySplashIsPainted()
    {
        if (splashPaintNotificationTime==0)
        {
            synchronized (waitForPaintLock)
            {
                // +1 in case System.currentTimeMillis() returns 0 (should not happen)
                splashPaintNotificationTime = System.currentTimeMillis()+1;
                
                waitForPaintLock.notifyAll();
            }
        }
    }
    
    /**
     * Returns the (absolute) time in milliseconds at which the splash
     * window should be closed. The method guarantees that the
     * difference of the return value and {@link System#currentTimeMillis()}
     * will be less or equal than {@link #MAX_VISIBILITY_TIME} 
     * 
     * @return timeout in milliseconds 
     */
    public final long getSplashScreenTimeout()
    {
        if (splashPaintNotificationTime==0)
        {
            return 0;
        }
        else
        {
            final long currentTime = System.currentTimeMillis();
            final long visibleTime = currentTime - splashPaintNotificationTime;
            final long remainingTime = minimumVisibilityTime>visibleTime ?
                    minimumVisibilityTime - visibleTime : 0;
            return currentTime + remainingTime;
        }
    }

    /**
     * When called the splash window must be closed.
     */
    protected abstract void closeSplashWindow();
    
    /**
     * Returns the location of the splash image.
     * @return the location of the splash image
     * @throws MalformedURLException
     */
    protected abstract URL getSplashImageURL() throws MalformedURLException;
    
    /**
     * Creates and shows the splash window with the specified image. 
     * 
     * @param splashImage the splash image
     * @param width width of the splash image (guaranteed to be larger than zero)
     * @param height height of the splash image (guaranteed to be larger than zero)
     */
    protected abstract void showSplashWindow( Image splashImage, int width, int height );
    
}
