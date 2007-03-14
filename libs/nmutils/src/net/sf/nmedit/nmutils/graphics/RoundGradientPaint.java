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
 * Created on Jan 11, 2006
 */
package net.sf.nmedit.nmutils.graphics;

import java.awt.Color;
import java.awt.Paint;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public class RoundGradientPaint implements Paint {

    private Point2D mid, rad;
    private Color cinner, couter;
    

    public RoundGradientPaint(Ellipse2D ellipse, Color inner, Color outer) {
        this(ellipse.getCenterX(), ellipse.getCenterY(), ellipse.getCenterX(), ellipse.getCenterY(), inner, outer);
    }

    public RoundGradientPaint(double midx,double midy,double rad, Color inner,Color outer) {
        this(midx,midy,rad,rad,inner,outer);
    }

    public RoundGradientPaint(double midx,double midy,double radx,double rady, Color inner,Color outer) {
        this(new Point2D.Double(midx,midy),new Point2D.Double(radx,rady),inner,outer);
    }

    public RoundGradientPaint(Point2D mid, Point2D rad, Color inner,Color outer) {
        this.mid=mid;
        this.rad=rad;
        this.cinner=inner;this.couter=outer;
    }
    
    public void setInnerColor(Color color)
    {
        this.cinner = color;
    }
    
    public void setOuterColor(Color color)
    {
        this.couter = color;
    }

    public PaintContext createContext(ColorModel cm, 
            Rectangle deviceBounds, Rectangle2D userBounds, 
            AffineTransform xform, RenderingHints hints)  {
          Point2D transformedMid = xform.transform(mid, null);
          Point2D transformedRad = xform.deltaTransform(rad, null);
          return new RoundGradientPC(transformedMid, transformedRad);
    }

    public int getTransparency() {
          int a1 = cinner.getAlpha();
          int a2 = couter.getAlpha();
          return (((a1 & a2) == 0xff) ? OPAQUE : TRANSLUCENT);
    }
    
    private class RoundGradientPC implements PaintContext {

        private Point2D mid, rad;
        
        public RoundGradientPC(Point2D mid, Point2D rad) {
            this.mid=mid;
            this.rad=rad; 
        }

        public ColorModel getColorModel() {      
            return ColorModel.getRGBdefault();
        }

        public Raster getRaster(int x, int y, int w, int h) {
             WritableRaster raster = getColorModel().createCompatibleWritableRaster(w, h);
             
             // optimized
             float[] distx = new float[w];
             float dx = (float) mid.getX()-x;
             for (int i = 0; i < w; i++) {
                 distx[i] = dx-i;
                 distx[i] = distx[i]*distx[i];
             }
             
             float[] disty;
             float dy = (float) mid.getY()-y;
             if (w==h && dx==dy) {
                 disty = distx;
             } else {
                 disty = new float[h];
                 for (int i = 0; i < h; i++) {
                     disty[i] = dy-i;
                     disty[i] = disty[i]*disty[i];
                 }
             }
             
             // for loop
             int c1r=cinner.getRed();
             int c1g=cinner.getGreen();
             int c1b=cinner.getBlue();
             int c1a=cinner.getAlpha();

             int c2r=couter.getRed();
             int c2g=couter.getGreen();
             int c2b=couter.getBlue();
             int c2a=couter.getAlpha();

             int difr=c2r-c1r;
             int difg=c2g-c1g;
             int difb=c2b-c1b;
             int difa=c2a-c1a;
             
             float radius = (float) rad.distance(0,0);
             float distance;
             float ratio;
             int base;
             
             //int fixpoint = radius;

             int[] data = new int[w * h * 4];
             for (int j = 0; j < h; j++) {
                 for (int i = 0; i < w; i++) {
                     distance = (float) Math.sqrt(distx[i]+disty[j]);
                     ratio = Math.min(distance / radius, 1);
                     base = (j * w + i) * 4;
                     data[base + 0] = (int) (c1r + ratio*difr);
                     data[base + 1] = (int) (c1g + ratio*difg);
                     data[base + 2] = (int) (c1b + ratio*difb);
                     data[base + 3] = (int) (c1a + ratio*difa);
                 }
             }

             /*
             // working::
             for (int j = 0; j < h; j++) {
                 for (int i = 0; i < w; i++) {
                      double distance = mid.distance(x + i, y + j);
                      double radius = rad.distance(0, 0);
                      double ratio = distance / radius;
                      if (ratio > 1.0)
                        ratio = 1.0;

                      int base = (j * w + i) * 4;
                      data[base + 0] = (int) (c1r + ratio* (c2r - c1r));
                      data[base + 1] = (int) (c1g + ratio* (c2g - c1g));
                      data[base + 2] = (int) (c1b + ratio* (c2b - c1b));
                      data[base + 3] = (int) (c1a + ratio* (c2a - c1a));
                 }
             } */
             raster.setPixels(0, 0, w, h, data);
             return raster;
        }

        public void dispose() { }
        
    }
    
}
