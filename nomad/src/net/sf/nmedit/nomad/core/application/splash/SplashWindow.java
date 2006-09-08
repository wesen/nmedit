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
package net.sf.nmedit.nomad.core.application.splash;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import net.sf.nmedit.jmisc.util.ImageQuery;

public class SplashWindow extends Window
{

    private Font      statusFont      = new Font( "Arial", Font.PLAIN, 10 );

    private boolean   disposed        = false;

    private boolean   splashVisible;

    private Image     splashImage;

    private String    progressMessage = "";

    private Point     statusLocation  = new Point( 76, 272 );

    public SplashWindow( Frame parent, Image splashImage )
    {
        super( parent );
        this.splashVisible = false;
        this.splashImage = splashImage;
        addMouseListener( new UserControlledDispose() );

        ImageQuery.waitForDimensions(splashImage, 5000);

        int iw = splashImage.getWidth( this );
        int ih = splashImage.getHeight( this );

        setSize( iw, ih );
        Dimension sc = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation( ( sc.width - iw ) / 2, ( sc.height - ih ) / 2 );
    }

    public void update( Graphics g )
    {
        // don't paint background
        paint( g );
    }

    public void updateStatus( String progressMessage )
    {
        this.progressMessage = progressMessage == null ? "" : progressMessage;
        Graphics g = this.getGraphics();
        if (g!=null)
        {
            this.update( g );   
        }
    }

    public void paint( Graphics g )
    {
        if (splashImage != null)
        {
            g.drawImage( splashImage, 0, 0, this );
        }

        g.setFont( statusFont );

        g.setColor(Color.BLACK);
        
        g.drawString( progressMessage, statusLocation.x, statusLocation.y );

        if (!splashVisible)
        {
            splashVisible = true;
            synchronized (this)
            {
                notifyAll();
            }
        }
    }

    public void disposeSplash()
    {
        if (!disposed)
        {
            disposed = true;
            getOwner().dispose();
        }
    }

    private class UserControlledDispose extends MouseAdapter
    {
        public void mouseClicked( MouseEvent event )
        {
            disposeSplash();
        }
    }

    public void waitUntilVisible()
    {
        // Note: To make sure the user gets a chance to see the
        // splash window we wait until its paint method has been
        // called at least once by the AWT event dispatcher thread.
        // If more than one processor is available, we don't wait,
        // and maximize CPU throughput instead.
        if (!EventQueue.isDispatchThread()
                && Runtime.getRuntime().availableProcessors() == 1)
        {
            synchronized (this)
            {
                if (splashVisible) return;

                while (!splashVisible)
                {
                    try
                    {
                        wait();
                    }
                    catch (InterruptedException e)
                    {
                    }
                }
            }
        }
    }

}
