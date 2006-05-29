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
package net.sf.nmedit.nomad.util.graphics;

import java.awt.Color;
import java.awt.Paint;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public class RasterPaint implements Paint
{

    public final static int TILE_SIZE = 4;
    
    //private Color color;
    private Color c1, c2, c3;
    private Paint paint;
    private float intensity;
    private Point2D origin = new Point2D.Double();

    public RasterPaint(Color color, Paint paint)
    {
        //this.color = color;
        c1 = color.darker();
        c2 = color.brighter();
        c3 = color;
        intensity = (float)color.getAlpha()/(float)0xFF;
        
        this.paint = paint;
    }

    public PaintContext createContext( ColorModel cm, Rectangle deviceBounds,
            Rectangle2D userBounds, AffineTransform xform, RenderingHints hints )
    {
        origin.setLocation(0,0);
        xform.transform(origin, origin);
        
        return new RasterPaintContext(
            paint.createContext(cm, deviceBounds, userBounds, xform, hints),
            origin,
            c1, c2, c3, intensity
        );
    }

    public int getTransparency()
    {
        return paint.getTransparency();
    }
    
    private static class RasterPaintContext implements PaintContext
    {

        private PaintContext context;
        private int ox;
        private int oy;
        private Color c1;
        private Color c2;
        private Color c3;
        private float intensity;

        public RasterPaintContext( PaintContext context, Point2D origin, Color c1, Color c2, Color c3, float intensity )
        {
            this.context = context;
            // origin might contain negative values            
            ox = ((int)(origin.getX()));
            oy = ((int)(origin.getY()));

            if (ox<0) ox = -ox;
            if (oy<0) oy = -oy;

            ox %= TILE_SIZE;
            oy %= TILE_SIZE; 
            
            this.c1 = c1;
            this.c2 = c2;
            this.c3 = c3;
            this.intensity = intensity;
        }

        public void dispose()
        {
            context.dispose();
        }

        public ColorModel getColorModel()
        {
            return context.getColorModel();
        }

        public Raster getRaster( int x, int y, int w, int h )
        {
            Raster raster = context.getRaster(x, y, w, h);
            
            if (intensity<=0) // invisible
                return raster;
            
            WritableRaster wraster;

            if (raster instanceof WritableRaster) 
            {
                wraster = (WritableRaster) raster;
            }
            else
            {
                wraster = context.getColorModel().createCompatibleWritableRaster(w, h);
                wraster.setDataElements(0, 0, raster);
                raster = wraster;
            }
            {   // post processing
                

                final int s = TILE_SIZE;
                final int o = 2;
                if (intensity>=1)
                {
                    updatePixel(c1);
                    fill(wraster, 0, 1, w, h, s);
                    updatePixel(c2);
                    fill(wraster, 0, 0, w, h, s);
                    updatePixel(c3);
                    fill(wraster, 0, 2, w, h, s);
    
                    updatePixel(c1);
                    fill(wraster, 0+o, 1+o, w, h, s);
                    updatePixel(c2);
                    fill(wraster, 0+o, 0+o, w, h, s);
                    updatePixel(c3);
                    fill(wraster, 0+o, 2+o, w, h, s);
                }
                else // intensity = [0,1]\{0,1}
                {
                    updatePixel(c1);
                    fillBlend(wraster, 0, 1, w, h, s);
                    updatePixel(c2);
                    fillBlend(wraster, 0, 0, w, h, s);
                    updatePixel(c3);
                    fillBlend(wraster, 0, 2, w, h, s);
    
                    updatePixel(c1);
                    fillBlend(wraster, 0+o, 1+o, w, h, s);
                    updatePixel(c2);
                    fillBlend(wraster, 0+o, 0+o, w, h, s);
                    updatePixel(c3);
                    fillBlend(wraster, 0+o, 2+o, w, h, s);
                }
            }
            return wraster;
        }
        
        private int[] pixel = new int[4];
        private int[] org = new int[4];

        private void updatePixel(Color c)
        {
            pixel[0] = c.getRed();
            pixel[1] = c.getGreen();
            pixel[2] = c.getBlue();
            pixel[3] = c.getAlpha();
        }

        private void fill(WritableRaster r, int dx, int dy, int w, int h, int step)
        {
            dx = (dx+ox) % TILE_SIZE;
            dy = (dy+oy) % TILE_SIZE;
            
            for (int x=dx;x<w;x+=step)
                for (int y=dy;y<h;y+=step)
                    r.setPixel(x, y, pixel);
        }
        
        private void fillBlend(WritableRaster r, int dx, int dy, int w, int h, int step)
        {
            dx = (dx+ox) % TILE_SIZE;
            dy = (dy+oy) % TILE_SIZE;
            
            for (int x=dx;x<w;x+=step)
                for (int y=dy;y<h;y+=step)
                {
                    // read pixel
                    r.getPixel(x, y, org);

                    // blend
                    org[0] += (int)((pixel[0]-org[0])*intensity);
                    org[1] += (int)((pixel[1]-org[1])*intensity);
                    org[2] += (int)((pixel[2]-org[2])*intensity);
                    // we leave alpha (org[3]) as is
                    
                    // set pixel
                    r.setPixel(x, y, org);
                }
        }
    }

}
