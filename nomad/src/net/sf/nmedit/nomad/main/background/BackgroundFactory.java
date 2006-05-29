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
 * Created on May 1, 2006
 */
package net.sf.nmedit.nomad.main.background;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import net.sf.nmedit.nomad.util.cache.Cache;
import net.sf.nmedit.nomad.util.cache.WeakCache;
import net.sf.nmedit.nomad.util.graphics.RasterPaint;

public class BackgroundFactory
{
    
    // don't let anyone instantiate this class
    private BackgroundFactory()
    { }
    
    private static Background defaultBackground = new DefaultBackground();
    //private static WeakReference<Background> metallicBG = new WeakReference<Background>(null);

    public static Background createDefaultBackground()
    {
        return defaultBackground;
    }

    public static Background createCompoundBackground(Background a, Background b)
    {
        return new CompoundBackground(a,b);
    }
    
    public static Background createTiledBackground(Image image)
    {
        return new TiledBackground(image, 0, 0);
    }

    public static Background createTiledBackground(Image image, int dx, int dy)
    {
        return new TiledBackground(image, dx, dy);
    }
    
    public static Background createGradientBackground( Color start, Color stop, int orientation )
    {
        return new GradientBackground(start, stop, orientation);
    }

    public static Background createGradientBackground( Color start, Color stop, int orientation, float f )
    {
        return new GradientBackground(start, stop, null, orientation ,f);
    }

    /*
    public static Background createGradientBackground( Color start, Color stop, Color raster, int orientation )
    {
        return new GradientBackground(start, stop, raster, orientation);
    }*/
    
    private final static BufferedImage createRasterBackgroundImage( Color start, Color stop, Color raster, int size, int orientation, float f)
    {
        if (size<=0)
            throw new IllegalArgumentException("size<=0");

        GraphicsConfiguration gc =
            GraphicsEnvironment
            .getLocalGraphicsEnvironment()
            .getDefaultScreenDevice()
            .getDefaultConfiguration();
        
        int w, h;
        
        if (orientation == GradientBackground.HORIZONTAL)
        {
            w = size;
            h = RasterPaint.TILE_SIZE;
        }
        else
        {
            w = RasterPaint.TILE_SIZE;
            h = size;
        }
        
        Background b = new GradientBackground(start, stop, raster, orientation, f);
        
        int transparency = (start.getAlpha()!=0xFF||stop.getAlpha()!=0xFF)
            ?Transparency.TRANSLUCENT:Transparency.OPAQUE;

        BufferedImage bi = gc.createCompatibleImage(w, h, transparency);           
        Graphics2D g2 = bi.createGraphics();
        b.paintBackground(null, g2, 0, 0, w, h);                
        g2.dispose();                
        return bi;
    }
    
    public final static Background createRasterBackground( Color start, Color stop, Color raster, int size, int orientation, float f)
    {          
        return new TiledBackground(createRasterBackgroundImage(start, stop, raster, size, orientation, f), 0, 0);
    }

    private static Cache<Background> cachedMetallic = new WeakCache<Background>();
    private static int cachedSize = 0;
    
    public static Background createMetallicBackground(int size)
    {
        if (cachedSize == size)
        {
            Background b = cachedMetallic.get();
            if (b!=null)
                return b;
        }
        
        cachedSize = size;
        Background b = createRasterBackground(
                GradientBackground.METALLIC1_START,
                GradientBackground.METALLIC1_STOP,
                GradientBackground.METALLIC1_RASTER,
                size,
                GradientBackground.VERTICAL,
                0.8f
             );
        cachedMetallic.put(b);
        return b;
    }

    public static Background createMetallicBackground()
    {
        return createMetallicBackground(
        java.awt.Toolkit.getDefaultToolkit().getScreenSize().height
        );
    }

}
