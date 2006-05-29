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
 * Created on Apr 30, 2006
 */
package net.sf.nmedit.nomad.util.graphics;

import java.awt.Color;
import java.awt.Paint;
import java.awt.PaintContext;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public class ButtonGlowPaint implements Paint
{
    
    private Color ctop;
    private Color cbottom;
    //private int w;
    private int h;
    private int cx;
    private int cy;
    private Point tmp = new Point();
    private Point2D ptop = new Point2D.Double();
    private Point2D pbottom = new Point2D.Double();

    /*
     *  |--                             ---
     *  |   ---                    ----
     *  |        ----- --- --- - 
     *  |----------------------------------
     *  |        ----- --- --- - 
     *  |   ---                    ----
     *  |--                             ---
     * 
     * 
     * 
     * 
     */

    public ButtonGlowPaint(Color c, int w, int h)
    {
        this.ctop = c.brighter();
        this.cbottom = c;
        //this.w=w;
        this.h=h;
        this.cx = w/2; // center of rect (0,0,w,h)
        this.cy = h/2;
    }

    public PaintContext createContext( ColorModel cm, Rectangle deviceBounds,
            Rectangle2D userBounds, AffineTransform xform, RenderingHints hints )
    {
        tmp.setLocation(cx, 0);
        xform.transform(tmp,ptop);
        tmp.setLocation(cx, h);
        xform.transform(tmp,pbottom);
        tmp.setLocation(cx,cy);
        double maxDist = tmp.distance(0,0);
        double hTransformed = xform.transform(tmp,null).getY();
        return new GlowPaintContext(ctop, cbottom,ptop, pbottom, maxDist,hTransformed);
    }

    public int getTransparency()
    {
        return OPAQUE;
    }

    private static class GlowPaintContext implements PaintContext
    {

        private Color ctop;
        private Color cbottom;
        private Color ctopbright;
        private Color cbottombright;
        private double maxDist;
        private int uh2;
        private Point2D centerTop;
        private Point2D centerBottom;

        public GlowPaintContext( Color ctop, Color cbottom, Point2D ptop, Point2D pbottom, double maxDist, double uh2 )
        {
            this.ctop = ctop;
            this.cbottom = cbottom;
            this.ctopbright = ctop.brighter().brighter();
            this.cbottombright = cbottom.brighter().brighter();
            centerTop = ptop;
            centerBottom = pbottom;
            this.maxDist = maxDist;
            this.uh2 = (int)uh2;
            
        }

        public void dispose()
        {
        }

        public ColorModel getColorModel()
        {
            return ColorModel.getRGBdefault();
        }

        public Raster getRaster( int x, int y, int w, int h )
        {
            WritableRaster raster = getColorModel().createCompatibleWritableRaster(
                    w, h);
                
                //Math.sqrt(w*w+h*h)/2;

            int[] data = new int[w * h * 4];

            int tb ;
            tb = uh2-y;
            tb = Math.min(tb, h);
            if (tb>0) // upper half
                fill(data, 0, tb, x, y, w, h, ctopbright, ctop, centerTop);
            
            tb = Math.max(0, tb);
            if (h-tb>0) // lower half
                fill(data, tb, h, x, y, w, h, cbottombright, cbottom, centerBottom);
            
            raster.setPixels(0, 0, w, h, data);
            return raster;
        }
        
        private void fill(int[]data, int ty, int tb, int x, int y, int w, int h, Color inner, Color outer, Point2D center)
        {
            double ratio;
            int base;
            int row;
            
            Point2D relCenter = new Point2D.Double(
                    center.getX()-x,
                    center.getY()-y
            );
            
            for (int j = ty; j < tb; j++) 
            {
                row = (j*w)<<2;
                for (int i = 0; i < w; i++) {
                  ratio = relCenter.distance(i,j) / maxDist;
                  if (ratio > 1.0)
                    ratio = 1.0;

                  base = row + (i << 2); // == (j * w + i) * 4
                  data[base + 0] = (int) (inner.getRed() + ratio
                      * (outer.getRed() - inner.getRed()));
                  data[base + 1] = (int) (inner.getGreen() + ratio
                      * (outer.getGreen() - inner.getGreen()));
                  data[base + 2] = (int) (inner.getBlue() + ratio
                      * (outer.getBlue() - inner.getBlue()));
                  data[base + 3] = (int) (inner.getAlpha() + ratio
                      * (outer.getAlpha() - inner.getAlpha()));
                }
              }
        }
        
    }
    
}
