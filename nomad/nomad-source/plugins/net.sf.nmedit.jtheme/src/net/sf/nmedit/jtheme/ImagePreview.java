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
 * Created on Jun 29, 2006
 */
package net.sf.nmedit.jtheme;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import net.sf.nmedit.nmutils.graphics.GraphicsToolkit;

public class ImagePreview extends JPanel
{
   
    /**
     * 
     */
    private static final long serialVersionUID = -1152051978348388139L;
    private Image original = null;
    private BufferedImage scaled = null;
    private boolean autoResizeH = false;
    private boolean autoResizeV = false;
    private boolean onlyScaledDown = true;

    public ImagePreview()
    {
        addComponentListener(new PreviewUpdater());
    }

    public boolean isOnlyScaledDownEnabled()
    {
        return onlyScaledDown;
    }
    
    public void setOnlyScaleDownEnabled(boolean enable)
    {
        this.onlyScaledDown = enable;
    }
    
    public boolean isHorizontalAutoresizeEnabled()
    {
        return autoResizeH;
    }
    
    public boolean isVerticalAutoresizeEnabled()
    {
        return autoResizeH;
    }
    
    public void setHorizontalAutoresizeEnabled(boolean enable)
    {
        if (this.autoResizeH!=enable)
        {
            this.autoResizeH = enable;
            //updateScaledImage();
        }
    }
    
    public void setVerticalAutoresizeEnabled(boolean enable)
    {
        if (this.autoResizeV!=enable)
        {
            this.autoResizeV = enable;
            //updateScaledImage();
        }
    }

    public void setPreviewImage(Image image)
    {
        if (original != image)
        {
            this.original = image;
            updateScaledImage();
        }
    }
    
    private void updateScaledImage()
    {
        if (original!=null) SwingUtilities.invokeLater(new ScaledImageProducer(original));
        else setScaledImage(null);
    }
    
    public Image getPreviewImage()
    {
        return original;
    }

    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        if (scaled != null)
        {
            final int w = getWidth();
            final int h = getHeight();
            final int sw = scaled.getWidth(null);
            final int sh = scaled.getHeight(null);
            final int dx = Math.max(0, (w-sw)/2);
            final int dy = Math.max(0, (h-sh)/2);

            g.drawImage(scaled, dx, dy, null);
        }
    }
    
    private void setScaledImage(BufferedImage scaled)
    {
        if (this.scaled != scaled)
        {
            this.scaled = scaled;
            if (scaled!=null)
            {
                int w = scaled.getWidth();
                int h = scaled.getHeight();
                Insets insets = getInsets();
                if (insets!=null)
                {       
                    w+=  insets.left+insets.right;
                    h+=  insets.top+insets.bottom;
                }
                
                Dimension d = getMinimumSize();
                if (d!=null)
                {
                    w = Math.max(w, d.width);
                    h = Math.max(h, d.height);
                }
                
                setPreferredSize(new Dimension(w, h));
            }
            else
            {
                setPreferredSize(getMinimumSize());
            }
            revalidate();
            repaint();
        }
    }

    private class ScaledImageProducer implements Runnable
    {
        
        private final Image image;
        private final int maxw;
        private final int maxh;
        private boolean autoV ;
        private boolean autoH ;

        public ScaledImageProducer(Image image)
        {
            this.image = image;

            int insetsTotalX = 0;
            int insetsTotalY = 0;
            Insets insets = getInsets();
            if (insets!=null)
            {
                insetsTotalX = insets.left+insets.right;
                insetsTotalY = insets.top+insets.bottom;
            }
            
            this.maxw = getWidth() - insetsTotalX;
            this.maxh = getHeight() - insetsTotalY;
            this.autoH = autoResizeH;
            this.autoV = autoResizeV;
        }
        
        public void run()
        {
            if (!scaleAndSet()) setScaledImage(null);
            else  if (autoV || autoH) setSize(getPreferredSize());
        }
        
        public boolean scaleAndSet()
        {
            if (original != image || maxw<=0 || maxh<=0) return false; // image has changed in the meanwhile
            
            final int iw = image.getWidth(null);
            final int ih = image.getHeight(null);
            if (iw <= 0 || ih <= 0) return false;

            final double fw = maxw/(double)iw;
            final double fh = maxh/(double)ih;
            double f;
            if ((!autoH) && (!autoV)) f = Math.min(fw, fh);
            else if ((autoH) && (!autoV)) f = fh;
            else if ((!autoH) && (autoV)) f = fw;
            else {
                // no scaling required
                f = 1;
                
                if (image instanceof BufferedImage)
                {
                    setScaledImage((BufferedImage) image);
                    return true;
                }
            }
            
            f = Math.max(f, 0);
            if (isOnlyScaledDownEnabled())
                f = Math.min(f, 1);
            
            if (f==0) return false; // should not happen
            setScaledImage(GraphicsToolkit.getScaledImage(image, f));
            return true;
        }
        
    }
    
    private class PreviewUpdater extends ComponentAdapter
    {
        public void componentResized(ComponentEvent e)
        {
            updateScaledImage();
        }
    }

}
