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
 * Created on Aug 29, 2006
 */
package net.sf.nmedit.nmutils.graphics;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;

/**
 * A class that waits until the dimensions of an image become
 * available and can be obtained by 
 * {@link java.awt.Image#getWidth(java.awt.image.ImageObserver)} 
 * and {@link java.awt.Image#getHeight(java.awt.image.ImageObserver)}.
 * 
 * @author Christian Schneider
 */
public final class ImageQuery implements ImageObserver
{
   
    // the image
    private final Image image;
    // true when width is available
    private boolean availableWidth  = false;
    // true when height is available
    private boolean availableHeight = false;
    // true when width and height are available or an error as occured
    private boolean done = false;
    // indicates that an error has occured - this does not imply that width and height are not available
    private boolean failed = false;
    // indicates whether an time out has occured or not
    private boolean timedOut = false;

    /**
     * Waits until the dimensions of the specified image are available (no time out).
     * 
     * @see #waitForDimensions(Image, long)
     */
    public static boolean waitForDimensions(Image image) 
    {
        // no timeout 
        return waitForDimensions(image, 0);
    }

    /**
     * Waits until the dimensions of the specified image are available or an timeout occures.
     * 
     * @param image the image
     * @param timeOut <code>0</code> means no time out otherwise it specifies the time out in milliseconds
     * 
     * @return <code>true</code> when the image dimensions are available
     * or <code>false</code> when the image is invalid or an timeOut has occured
     */
    public static boolean waitForDimensions(Image image, long timeOut) 
    {
        return (new ImageQuery(image)).waitForDimensions(timeOut);
    }
    
    private ImageQuery(Image image)
    {
        this.image = image;
    }

    /**
     * @param timeOut timeout in milliseconds. <code>0</code> means no time out
     */
    private synchronized boolean waitForDimensions(long timeOut)
    {
        // start loading
        if (!Toolkit.getDefaultToolkit().prepareImage(image, -1, -1, this))
        {        
            // the image is not loaded - wait until data becomes available
           
            // calculate time out point
            final long end = System.currentTimeMillis() + timeOut;
            while (!doAbort())
            {
                if (timeOut!=0)
                {
                    timeOut = end-System.currentTimeMillis();
                    if (timeOut<=0)
                    {
                        // timed out
                        timedOut = true;
                        return result();
                    }
                }
    
                try
                {
                    wait(timeOut);
                }
                catch (InterruptedException e)
                { }
            }   
        }
        else
        {
            // the image is already loaded
            availableWidth = availableHeight = true;
        }
        return result();
    }
    
    /**
     * @return <code>true</code> when width and height are available
     */
    private boolean result()
    {
        return availableWidth && availableHeight;
    }
    
    /**
     * @return <code>true</code> when an timeout has occured or data is available
     */
    private boolean doAbort()
    {
        return done || timedOut;
    }

    public synchronized boolean imageUpdate( Image img, int infoflags, int x, int y, int width, int height )
    {
        availableWidth |= (infoflags & ImageObserver.WIDTH) != 0;
        availableHeight|= (infoflags & ImageObserver.HEIGHT) != 0;
        failed |=  (infoflags & (ImageObserver.ERROR|ImageObserver.ABORT)) != 0;

        if ((infoflags & ALLBITS) != 0)
        {
            availableWidth = availableHeight = true;
        }
        
        if (result() || failed)
        {
            // we are done
            done = true;
            // wake up
            notifyAll();
            // no more updates required
            return false; 
        }

        // still waiting for the info (when not timed out and not done)
        return !doAbort();
    }

}
