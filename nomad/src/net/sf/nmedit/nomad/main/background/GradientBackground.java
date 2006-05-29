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
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;

import net.sf.nmedit.nomad.util.graphics.RasterPaint;

public class GradientBackground extends AbstractBackground
{

    public static final Color METALLIC1_RASTER = new Color(0x80C3C9D9, true);
    public final static Color METALLIC1_START  = new Color(0xC3C9D9, false);
    public final static Color METALLIC1_STOP   = new Color(0x657189, false);
    

    public final static int HORIZONTAL = 0;
    public final static int VERTICAL   = 1;
    private Color start;
    private Color stop;
    private int orientation;
    private Paint fill;
    private int gwidth = 0;
    private int gheight = 0;
    private Color rasterColor;
    private boolean paintRaster;
    private float f;

    public GradientBackground(Color start, Color stop, int orientation)
    {
        this(start, stop, null, orientation, 1);
    }

    public GradientBackground(Color start, Color stop, Color raster, int orientation)
    {
        this(start, stop, raster, orientation, 1);
    }
    
    public GradientBackground(Color start, Color stop, Color raster, int orientation, float f)
    {
        this.start = start;
        this.stop = stop;
        this.rasterColor = raster;
        this.paintRaster = rasterColor != null && rasterColor.getAlpha()!=0;
        this.orientation = orientation;
        this.f = f;
        if (orientation!=HORIZONTAL && orientation!=VERTICAL)
            throw new IllegalArgumentException("unknown orientation: "+orientation);
    }

    public void paintBackground( Component c, Graphics g, int x, int y,
            int width, int height )
    {
        if (fill==null || gwidth!=width || gheight!=height)
        {
            gwidth = width;
            gheight = height;
            if (orientation==HORIZONTAL)
                fill = new GradientPaint(x, y, start, x+(width*f)-1, y, stop, true);
            else // orientation==VERTICAL
                fill = new GradientPaint(x, y, start, x, y+(height*f)-1, stop, true);
            if (paintRaster)
                fill = new RasterPaint(rasterColor, fill);
            
        }

        Graphics2D g2 = (Graphics2D) g;
        Paint prevPaint = g2.getPaint();
        g2.setPaint(fill);
        updateArea(g, x, y, width, height);
        
        g2.fillRect(area.x, area.y, area.width, area.height);
        
        g2.setPaint(prevPaint);
    }

    public int getTransparency()
    {
        return ((start.getAlpha()&stop.getAlpha())==0xFF) ? OPAQUE : TRANSLUCENT;
    }

}
