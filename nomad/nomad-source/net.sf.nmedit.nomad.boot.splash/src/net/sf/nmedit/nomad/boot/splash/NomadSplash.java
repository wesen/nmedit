/* Copyright (C) 2007 Christian Schneider
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
package net.sf.nmedit.nomad.boot.splash;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.java.plugin.boot.SplashHandler;
import org.java.plugin.util.ExtendedProperties;

public class NomadSplash implements SplashHandler
{

    public static final String KEY_PREFIX = "";

    /**
     * default false
     */
    public static final String KEY_METER_ENABLED = KEY_PREFIX+"meter.enabled";
    public static final String KEY_METER_BOUNDS = KEY_PREFIX+"meter.bounds";
    public static final String KEY_METER_BOUNDS_ROUND_RECT = KEY_PREFIX+"meter.roundrect";
    public static final String KEY_METER_FILL = KEY_PREFIX+"meter.fill";
    public static final String KEY_TEXT = KEY_PREFIX+"text";
    public static final String KEY_TEXT_BOUNDS = KEY_PREFIX+"text.bounds";
    public static final String KEY_TEXT_ENABLED = KEY_PREFIX+"text.enabled";
    public static final String KEY_TEXT_FILL = KEY_PREFIX+"text.fill";
    public static final String KEY_TEXT_FONTNAME = KEY_PREFIX+"text.font.name";
    public static final String KEY_TIMEOUT = KEY_PREFIX+"timeout";
    public static final String KEY_CLOSE_ONE_CLICK = KEY_PREFIX+"closeonclick";
    public static final String KEY_BLOCK_UNTIL_VISIBLE = KEY_PREFIX+"blockuntilvisible";

    private static final int SP_IMAGE_LOADING = 0;
    private static final int SP_IMAGE_AVAILABLE = 1;
    private static final int SP_IMAGE_PAINTED_ONCE = 2;
    private static final int SP_ABORTED = 3;
    private static final int SP_VISIBLE = 4;
    private static final int SP_CLOSE_ON_CLICK = 5;
    private static final int SP_BLOCK_UNTIL_VISIBLE = 6;
    private static final int SP_DISPOSING_WINDOW = 7;
    
    private volatile int flags = 0;

    private void setFlag(int aFlag, boolean aValue) 
    {        
        if(aValue) {
            flags |= (1 << aFlag);
        } else {
            flags &= ~(1 << aFlag);
        }
        checkFlags();
    }
    
    private boolean getFlag(int aFlag) 
    {
        int mask = (1 << aFlag);
        return ((flags & mask) == mask);
    }

    // splash window properties
    private volatile int spSplashTimeout = 3000; 
    private SplashWindow spSplashWindow;

    // splash image properties
    private URL spSplashImageURL;
    private Image spSplashImage;
    private Dimension spSplashImageSize;
    private SplashImageObserver spImageObserver;
    
    // meter properties
    private volatile float spMeterProgress = 0f;
    private boolean spMeterEnabled = false;
    private Rectangle spMeterBounds;
    private boolean spMeterRoundRect = false;
    private Color spMeterFill;

    // text properties
    private boolean spTextEnabled = false;
    private volatile String spText = null;
    private Rectangle spTextBounds;
    private Color spTextFill;
    private Font spTextFont;
    
    // locks

    private final Object paintLock = new Object();
    private final Object windowLock = new Object();
    
    private volatile long splashPaintedTime = 0;
    
    private Color getColorProperty(ExtendedProperties p, String propertyName, Color defaultValue)
    {
        String pValue = p.getProperty(propertyName);
        if (pValue == null)
            return defaultValue;
        
        try
        {
            if (pValue.length() == 1+8)
            {   
                int i = Integer.decode(pValue).intValue();
                int a = (i>>24)&0xFF;
                if (a == 0)
                    return null;
                
                int r = (i>>16)&0xFF;
                int g = (i>>8)&0xFF;
                int b = (i)&0xFF;
                return new Color(r, g, b, a);
            }
            
            return Color.decode(pValue);
        }
        catch (NumberFormatException e)
        {
            // ignore
        }
        return defaultValue;
    }
    
    private boolean getBooleanProperty(ExtendedProperties p, String propertyName, boolean defaultValue)
    {
        String pValue = p.getProperty(propertyName);
        if (pValue == null)
            return defaultValue;
        
        try
        {
            return Boolean.parseBoolean(pValue);
        }
        catch (NumberFormatException e)
        {            
            // ignore
        }
        return defaultValue;
    }
    
    private static final String spRectanglePropertyRegex = "\\s*(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s*";
    private Pattern spRectanglePropertyPattern;

    private Rectangle getRectangleProperty(ExtendedProperties p, String propertyName)
    {
        String pValue = p.getProperty(propertyName);
        if (pValue == null)
            return null;
        
        if (spRectanglePropertyPattern == null)
            spRectanglePropertyPattern = Pattern.compile(spRectanglePropertyRegex);
        
        Matcher matcher = spRectanglePropertyPattern.matcher(pValue);
        if (!matcher.matches())
            return null;
        
        try
        {
            return new Rectangle( Integer.parseInt(matcher.group(1)),
                    Integer.parseInt(matcher.group(2)),
                    Integer.parseInt(matcher.group(3)),
                    Integer.parseInt(matcher.group(4)));
        }
        catch (NumberFormatException e)
        {
            // ignore
            return null;
        }
    }
    
    private int getIntegerProperty(ExtendedProperties p, String propertyName, int defaultValue)
    {
        try
        {
            return Integer.parseInt(p.getProperty(propertyName));
        }
        catch (RuntimeException e)
        {
            return defaultValue;
        }
    }

    public void configure(ExtendedProperties p)
    {
        // meter
        spMeterFill = getColorProperty(p, KEY_METER_FILL, null);
        spMeterBounds = getRectangleProperty(p, KEY_METER_BOUNDS);
        spMeterRoundRect = getBooleanProperty(p, KEY_METER_BOUNDS_ROUND_RECT, false);
        spMeterEnabled = 
            getBooleanProperty(p, KEY_METER_ENABLED, false) 
            && (spMeterFill != null) 
            && (spMeterBounds != null)
            && (spMeterBounds.width>0)
            && (spMeterBounds.height>0);

        // text
        spText = p.getProperty(KEY_TEXT);
        spTextFill = getColorProperty(p, KEY_TEXT_FILL, null);
        spTextBounds = getRectangleProperty(p, KEY_TEXT_BOUNDS);

        String fontname = p.getProperty(KEY_TEXT_FONTNAME);
        if (fontname == null)
            fontname = "sansserif";
        spTextFont = createFont(fontname, spTextBounds.height);
        
        spTextEnabled =
            getBooleanProperty(p, KEY_TEXT_ENABLED, false)
            && (spTextFill != null)
            && (spTextBounds != null)
            && (spTextBounds.width>0)
            && (spTextBounds.height>0)
            && (spTextFont != null);
        
        spSplashTimeout =
            getIntegerProperty(p, KEY_TIMEOUT, 3000);
        setFlag(SP_CLOSE_ON_CLICK, getBooleanProperty(p, KEY_CLOSE_ONE_CLICK, true));
        setFlag(SP_BLOCK_UNTIL_VISIBLE, getBooleanProperty(p, KEY_BLOCK_UNTIL_VISIBLE, false));
    }

    private static int pixelsToPoint(int pixels, int dpi) 
    {
        return (int) Math.floor((pixels * 72.0) / dpi);
    }
    
    private static Font createFont(String fontname, int heightInPixel)
    {
        int fontsize;
        try
        {
            int dpi = Toolkit.getDefaultToolkit().getScreenResolution();
            fontsize = pixelsToPoint(heightInPixel, dpi);
        }
        catch (HeadlessException e)
        {
            return null;
        }
        
        return fontsize>0 ? new Font(fontname, Font.PLAIN, fontsize) : null;
    }

    public URL getImage()
    {
        return spSplashImageURL;
    }

    public Object getImplementation()
    {
        return this;
    }

    public float getProgress()
    {
        return spMeterProgress;
    }

    public String getText()
    {
        return spText;
    }

    public boolean isVisible()
    {
        return getFlag(SP_VISIBLE);
    }

    public void setImage(URL url)
    {
        if (!eq(spSplashImageURL, url))
        {
            this.spSplashImageURL = url;   
        }
    }

    public void setProgress(float progress)
    {
        if (progress<0) progress = 0;
        else if (progress>1) progress = 1f;
        
        if (this.spMeterProgress != progress)
        {
            this.spMeterProgress = progress;
            
            if (spMeterEnabled)
                repaint(spMeterBounds);
        }
    }
    
    private void repaint(Rectangle r)
    {
        spSplashBufferDirty = true;
        synchronized (windowLock)
        {
            if (spSplashWindow != null)
                spSplashWindow.repaint(r.x, r.y, r.width, r.height);   
        }
    }
    
    private boolean spSplashBufferDirty = true;
    private BufferedImage spSplashBuffer;
    private void checkSplashBuffer()
    {
        boolean fullRepaint = false;
        
        if (spSplashBuffer == null && spSplashImageSize != null)
        {
            spSplashBuffer = new BufferedImage(
                    spSplashImageSize.width,
                    spSplashImageSize.height,
                    BufferedImage.TYPE_INT_RGB);
            spSplashBufferDirty = true;
            fullRepaint = true;
        }
        if (!spSplashBufferDirty)
            return;
        
        Graphics2D g = spSplashBuffer.createGraphics();
        try
        {
            if (fullRepaint)
                g.drawImage(spSplashImage, 0, 0, null);

            if (spMeterEnabled)
            {
                if (!fullRepaint)
                    paintRegion(g, spSplashImage, spMeterBounds);
                
                g.setColor(spMeterFill);
                
                if (spMeterRoundRect)
                {
                    if (spMeterProgress>0)
                    {
                        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        
                        int arc = Math.min(spMeterBounds.width, spMeterBounds.height);
                        g.fillRoundRect(spMeterBounds.x, spMeterBounds.y, 
                                (int) Math.round(spMeterBounds.width * spMeterProgress), 
                                spMeterBounds.height, arc, arc);
    
                        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                    }
                }
                else
                {
                    if (spMeterProgress>0)
                    {
                        g.fillRect(spMeterBounds.x, spMeterBounds.y, 
                                (int) Math.round(spMeterBounds.width * spMeterProgress), 
                                spMeterBounds.height);
                    }
                }
            }

            if (spTextEnabled)
            {
                if (!fullRepaint)
                    paintRegion(g, spSplashImage, spTextBounds);
                
                if (spText != null)
                {
                    FontMetrics fm = g.getFontMetrics();
                    g.setFont(spTextFont);
                    g.setColor(spTextFill);
                    g.setClip(spTextBounds.x, spTextBounds.y, spTextBounds.width, spTextBounds.height);
                    g.drawString(spText, spTextBounds.x, spTextBounds.y+fm.getAscent()-fm.getDescent());
                }
            }
        }
        finally
        {
            g.dispose();
        }
        
        spSplashBufferDirty = false;
    }

    private void paintRegion(Graphics2D g, Image img, Rectangle rect)
    {
        int r = rect.x+rect.width;
        int b = rect.y+rect.height;
        g.drawImage(img, rect.x, rect.y, r, b, rect.x, rect.y, r, b, null);
    }

    public void setText(String text)
    {
        if (!eq(this.spText, text))
        {
            this.spText = text;
            
            if (spTextEnabled)
                repaint(spTextBounds);
        }
    }

    public void setVisible(boolean visible)
    {
        if (getFlag(SP_VISIBLE) != visible)
        {
            setFlag(SP_VISIBLE, visible);
        }
    }
    
    private void mouseClicked()
    {
        if (getFlag(SP_CLOSE_ON_CLICK))
        {
            disposeWindow();
            setVisible(false);
        }
    }

    private void checkFlags()
    {
        if (getFlag(SP_IMAGE_PAINTED_ONCE))
        {
            if (splashPaintedTime == 0)
                splashPaintedTime = System.currentTimeMillis();
        }
        
        if (getFlag(SP_ABORTED))
        {
            setFlag(SP_VISIBLE, false);
            return;
        }

        if (getFlag(SP_VISIBLE))
        {
            if (!getFlag(SP_IMAGE_LOADING))
            {
                setFlag(SP_IMAGE_LOADING, true);
                spImageObserver = new SplashImageObserver();
                spImageObserver.prepareImage();
                return ;
            }
            
            if (getFlag(SP_IMAGE_AVAILABLE))
            {
                synchronized (windowLock)
                {
                    if (spSplashWindow == null)
                    {
                        checkSplashBuffer(); // render buffer for the first time
        
                        spSplashWindow = new SplashWindow();
                        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
                        spSplashWindow.setBounds(
                                (screen.width-spSplashImageSize.width)/2,
                                (screen.height-spSplashImageSize.height)/2,
                                spSplashImageSize.width, spSplashImageSize.height);
                        spSplashWindow.setVisible(true);
                        
                        if (getFlag(SP_BLOCK_UNTIL_VISIBLE))
                        {
                            while (getFlag(SP_VISIBLE)
                                    && (!(getFlag(SP_ABORTED)||getFlag(SP_IMAGE_PAINTED_ONCE))))
                            {
                                synchronized (paintLock)
                                {
                                    try
                                    {
                                        paintLock.wait(250);
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
            }
        }
        else
        {
            synchronized (windowLock)
            {
                if (spSplashWindow == null || getFlag(SP_DISPOSING_WINDOW))
                    return ;
            }

            if (spSplashTimeout <= 0 || ((splashPaintedTime+
                    spSplashTimeout)<System.currentTimeMillis()))
                disposeWindow();
            else 
            {
                setFlag(SP_DISPOSING_WINDOW, true);
                
                (new Thread()
                {
                    public void run()
                    {
                        while ((!Thread.interrupted())
                                && (!getFlag(SP_ABORTED))
                                && ((splashPaintedTime+
                                        spSplashTimeout)>System.currentTimeMillis()))
                        {
                            synchronized(windowLock)
                            {
                                if (spSplashWindow == null)
                                    break;
                                
                                try
                                {
                                    windowLock.wait(Math.max(0,spSplashTimeout-(System.currentTimeMillis()
                                            -splashPaintedTime)));
                                }
                                catch (InterruptedException e)
                                {
                                    // ignore
                                }
                            }
                        }
                        disposeWindow();
                    }
                }
                ) .start();
            }
        }
    }

    private void disposeWindow()
    {   
        Runnable r = new Runnable() { public void run()
        {
            synchronized (windowLock)
            {
                if (spSplashWindow != null)
                {
                    spSplashWindow.dispose();
                    spSplashWindow = null;
                    setFlag(SP_VISIBLE, false);
                }
                setFlag(SP_DISPOSING_WINDOW, false);
                windowLock.notifyAll();
            }
            }}; 
     
        if (EventQueue.isDispatchThread())
            EventQueue.invokeLater(r);
        else
            r.run();
    }
    
    private static boolean eq(Object a, Object b)
    {
        return a == b || (a != null && a.equals(b));
    }
    
    private class SplashImageObserver implements ImageObserver
    {
        
        public void prepareImage()
        {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            
            URL url = spSplashImageURL;
            if (url == null)
                io_abort();
            // get the image
            spSplashImage = toolkit.getImage(url);
            // monitor the image loading process
            if (toolkit.prepareImage(spSplashImage, -1, -1, this))
            {
                io_complete();
            }
        }

        private void io_complete()
        {
            spImageObserver = null;
            int w = spSplashImage.getWidth(null);
            int h = spSplashImage.getHeight(null);
            
            if (w>0 && h>0)
                spSplashImageSize = new Dimension(w, h);
            setFlag(SP_IMAGE_AVAILABLE, true);
        }

        private boolean io_isAbortRequested()
        {
            return getFlag(SP_ABORTED);
        }

        private void io_abort()
        {
            spImageObserver = null;
            setFlag(SP_ABORTED, true);
        }

        public boolean imageUpdate( Image image, int infoflags, int x, int y, int width, int height )
        {
            // return value in case we want to abort loading the image
            final boolean IO_ABORT_LOADING = false;
            // return value in case we still want to construct the splash image
            final boolean IO_RETRIEVE_DATA = true;

            // check for errors or if there is a request to abort loading 
            if (((infoflags & (ERROR|ABORT)) != 0) || io_isAbortRequested())
            {
                io_abort();
                return IO_ABORT_LOADING;
            }

            // check if image is complete
            if ((infoflags & ALLBITS) != 0)
            {
                io_complete();
                // image is complete - we are done
                return IO_ABORT_LOADING;
            }

            // not done yet - get more data
            return IO_RETRIEVE_DATA;
        }
        
    }
    
    private void paintSplash(Graphics g)
    {
        checkSplashBuffer();
        Image img = spSplashBuffer == null ? spSplashImage : spSplashBuffer;
        g.drawImage(img, 0, 0, null);
        
        if (!getFlag(SP_IMAGE_PAINTED_ONCE))
        {
            synchronized (paintLock)
            {
                setFlag(SP_IMAGE_PAINTED_ONCE, true);
                paintLock.notifyAll();
            }
        }
    }
    
    private class SplashWindow extends Window
    {
        /**
         * 
         */
        private static final long serialVersionUID = -1330056937401666849L;

        public SplashWindow()
        {
            super(new SplashFrame());
            enableEvents(MouseEvent.MOUSE_EVENT_MASK);
        }
        
        public void update(Graphics g)
        {
            // do not erase background because we fill it completely with the image
            paint(g);
        }
        
        public void paint(Graphics g)
        {
            paintSplash(g);
        }
        
        public void processMouseEvent(MouseEvent e)
        {
            if (e.getID() == MouseEvent.MOUSE_CLICKED)
                mouseClicked();
            super.processMouseEvent(e);
        }
    }
    
    private static class SplashFrame extends Frame
    {
        /**
         * 
         */
        private static final long serialVersionUID = -8574332776737247773L;

        public void update(Graphics g)
        {
            // do not erase background because we fill it completely with the image
            paint(g);
        }
    }
    
}
