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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class AWTSplashScreen extends SplashScreenControl implements SplashScreen
{

    public static final String KEY_METER = PREFIX+"meter";
    public static final String KEY_METER_BOUNDS = PREFIX+"meter.bounds";
    public static final String KEY_METER_COLOR = PREFIX+"meter.color";
    public static final String KEY_METER_ROUNDRECT = PREFIX+"meter.roundrect";
    public static final String KEY_TEXT = PREFIX+"text";
    public static final String KEY_TEXT_BOUNDS = PREFIX+"text.bounds";
    public static final String KEY_TEXT_COLOR = PREFIX+"text.color";
    
    private Map<Object, Object> propertyMap = new HashMap<Object, Object>();
    
    private final Object windowLock = new Object();
    
    private Window splashWindow = null;
    
    private String text = null;
    private float progress = 0f;

    // properties extracted from the property map - thus we need no synchronisation on them

    private Rectangle pmBounds = null;
    private Rectangle txtBounds = null;    
    private Color pmColor = null;
    private boolean pmRoundRect = false;
    private Color txtColor = null;

    private boolean textPropertiesValid = false;
    private boolean progressMeterPropertiesValid = false;
    
    private Font txtFont = null;
    private FontMetrics txtFontMetrics = null;
    
    private void loadAndValidateProperties(Component parent, int swidth, int sheight)
    {
        synchronized (propertyMap)
        {
            boolean showMeter = NMUtilities.getBooleanProperty(propertyMap,KEY_METER, true);
            
            if (showMeter)
            {
                pmBounds = NMUtilities.getRectangleProperty(propertyMap,KEY_METER_BOUNDS, null);
                pmColor = NMUtilities.getColorProperty(propertyMap,KEY_METER_COLOR, Color.BLACK);
                pmRoundRect = NMUtilities.getBooleanProperty(propertyMap,KEY_METER_ROUNDRECT, false);

                if (pmBounds != null)
                {
                    NMUtilities.fitRectangle(pmBounds, swidth, sheight);
                    progressMeterPropertiesValid = (!pmBounds.isEmpty());
                }
            }

            boolean showText = NMUtilities.getBooleanProperty(propertyMap,KEY_TEXT, true);
            if (showText)
            {
                txtBounds = NMUtilities.getRectangleProperty(propertyMap,KEY_TEXT_BOUNDS, null);
                txtColor = NMUtilities.getColorProperty(propertyMap,KEY_TEXT_COLOR, Color.BLACK);
                
                if (txtBounds!=null)
                {
                    NMUtilities.fitRectangle(txtBounds, swidth, sheight);
                    if (!txtBounds.isEmpty())
                    {
                        createFont(parent);
                        textPropertiesValid = (txtFont != null) && (txtFontMetrics != null);
                    }
                }
            }
        }
    }

    private void createFont(Component parent)
    {
        int size = 20;
        while (size>=2)
        {
            Font f = new Font("sansserif", Font.PLAIN, size);
            FontMetrics fm = parent.getFontMetrics(f);
            if ((fm != null) && (fm.getHeight()<=txtBounds.height))
            {
                this.txtFont = f;
                this.txtFontMetrics = fm;
                return ;
            }
            size --;
        }
        return ;
    }

    @Override
    protected URL getSplashImageURL() throws MalformedURLException
    {
        Object value = getClientProperty(LOCATION_KEY);
        
        if (value == null)
            return null;
        else if (value instanceof String)
        {
            String s = (String) value;
            
            File file = new File(s);
            
            if (file.exists())
                return file.toURI().toURL();

            return new URL((String) value);
        }
        else if (value instanceof URL)
            return (URL) value;
        else return null;
    }
    
    public void putClientProperty( Object key, Object value )
    {
        synchronized (propertyMap)
        {
            propertyMap.put(key, value);
        }
    }

    public Object getClientProperty( Object key )
    {
        synchronized (propertyMap)
        {
            return propertyMap.get(key);
        }
    }

    public void setProgress( float progress )
    {
        progress = Math.max(0, Math.min(1, progress));
        
        if (this.progress!=progress)
        {
            this.progress = progress;
    
            if (progressMeterPropertiesValid)
            {
                synchronized (windowLock)
                {
                    if (splashWindow != null)
                        splashWindow.repaint(pmBounds.x, pmBounds.y, pmBounds.width, pmBounds.height);
                }
            }
        }
    }

    public float getProgress()
    {
        return progress;
    }

    public void setText( String text )
    {
        String currentText;
        
        currentText = this.text;
        
        this.text = text;

        if (textPropertiesValid && ((currentText != text) || ((currentText!=null) && (!currentText.equals(text)))))
        {
            synchronized (windowLock)
            {
                if (splashWindow != null)
                    splashWindow.repaint(txtBounds.x, txtBounds.y, txtBounds.width, txtBounds.height);
            }
        }
    }

    public String getText()
    {
        return text;
    }

    @Override
    protected void closeSplashWindow()
    {
        synchronized (windowLock)
        {
            if (splashWindow != null)
            {
                Window w = splashWindow;
                splashWindow = null;
                Window o = w.getOwner();
                if (o!=null)
                    o.dispose(); // the frame
                else
                    w.dispose();
            }
        }
    }

    private BufferedImage bgbuffer = null;
    
    private void renderSplash(Image splashImage)
    {
        Graphics2D g2 = null;
        if (bgbuffer == null)
        {
            bgbuffer = new BufferedImage(splashImage.getWidth(null), splashImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
            g2 = bgbuffer.createGraphics();
            g2.drawImage(splashImage, 0, 0, null);
        }
        else
        {
            g2 = bgbuffer.createGraphics();
            g2.drawImage(splashImage, 0, 0, null);
        }

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (progressMeterPropertiesValid)
        {
            Rectangle r = pmBounds;
            g2.setColor(pmColor);
            float f = getProgress();
            
            if (pmRoundRect)
            {
                int arc = Math.min(r.width, r.height);
                g2.fillRoundRect(r.x, r.y, (int) (r.width*f), r.height, arc, arc);
            }
            else
            {
                g2.fillRect(r.x, r.y, (int) (r.width*f), r.height);   
            }
        }

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        
        String t = getText();
        
        if (textPropertiesValid && t!=null)
        {
            Rectangle r = txtBounds;
            g2.setFont(txtFont);
            g2.setColor(txtColor);

            g2.setClip(r.x, r.y, r.width, r.height);
            g2.drawString(t, r.x, r.y+txtFontMetrics.getHeight()-txtFontMetrics.getDescent());
        }
        
        g2.dispose();
    }
    
    // private Color inv = null;
    protected void paintSplash(Image splashImage, Component parent, Graphics g)
    {
        if (bgbuffer == null)
        {
            g.drawImage(splashImage, 0, 0, null);
        }
        else
        {
            renderSplash(splashImage);
            g.drawImage( bgbuffer, 0, 0, null );
        }

        notifySplashIsPainted();
    }
    
    @Override
    protected final void showSplashWindow( Image splashImage, int width, int height )
    {
        synchronized (windowLock)
        {
            SplashWindow w = new SplashWindow(splashImage);
    
            loadAndValidateProperties(w, width, height);
            if (textPropertiesValid && progressMeterPropertiesValid)
            {
                renderSplash(splashImage);
            }
            
            if (!isAbortRequested())
            {
                w.setSize(width, height);
                Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
                w.setLocation((screen.width-width)/2, (screen.height-height)/2);
    
                w.addMouseListener(new MouseAdapter(){
                    public void mouseClicked(MouseEvent e)
                    {
                        disposeImmediately();
                    }
                });
                
                w.setVisible(true);
                splashWindow = w;
            }
            else
            {
                setSplashScreenAborted();
            }
        }
    }
    
    private class SplashWindow extends Window
    {

        private Image splashImage;

        public SplashWindow(Image splashImage)
        {
            super( new SplashFrame() );
            this.splashImage = splashImage;
        }
        
        public void update(Graphics g)
        {
            // do not erase background because we fill it completely with the image
            paint(g);
        }
        
        public void paint(Graphics g)
        {
            paintSplash(splashImage, this, g);
        }
        
    }
    
    private static class SplashFrame extends Frame
    {
        public void update(Graphics g)
        {
            // do not erase background because we fill it completely with the image
            paint(g);
        }
        
    }

}
