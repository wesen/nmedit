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
 * Created on May 2, 2006
 */
package net.sf.nmedit.nmutils.graphics;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;

import javax.swing.ImageIcon;

public class GraphicsToolkit
{

    public static ImageIcon renderColorIcon(Color c)
    {
        return new ImageIcon(renderColorImage(c, 16, 16, Color.BLACK));
    }
    
    public static BufferedImage renderColorImage(Color c, int w, int h, Color border)
    {
        boolean opaque = c.getAlpha()==0xFF && (border==null || (border!= null && border.getAlpha()==0xFF));
        
        BufferedImage img = new BufferedImage(w, h, opaque ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        try
        {
            int fx, fy, fw, fh;
            if (border == null)
            {
                fx = 0;
                fy = 0;
                fw = w;
                fh = h;
            }
            else
            {
                fx = 1;
                fy = 1;
                fw = w-2;
                fh = h-2;
                g.setColor(border);
                g.drawRect(0, 0, w-1, h-1);
            }
            g.setColor(c);
            g.fillRect(fx, fy, fw, fh);
        }
        finally
        {
            g.dispose();
        }
        return img;
    }
    
    public static BufferedImage getScaledImage(Image source, double f)
    {
        return getScaledImage(source, null, f);
    }
    
    public static BufferedImage getScaledImage(Image source, BufferedImage reuse, double f)
    {
        if (f<=0) throw new IllegalArgumentException("invalid factor specified");
        final int iw = source.getWidth(null);
        final int ih = source.getHeight(null);
        final int sw = (int) (iw*f);
        final int sh = (int) (ih*f);
        
        final BufferedImage scaled;
        final int transparency = (source instanceof Transparency) ? ((Transparency)source).getTransparency() : Transparency.OPAQUE;
        
        Graphics2D g2 = null; 
        if (reuse!=null && reuse.getWidth()==sw && reuse.getHeight()==sh && transparency == reuse.getTransparency())
        {
            scaled = reuse;
            g2 = scaled.createGraphics();
            clearRegion(g2, 0, 0, sw, sh);
        }
        else
        {
            scaled = createCompatibleBuffer(sw, sh, transparency);
            g2 = scaled.createGraphics();
        }
        
        try
        {
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.scale(f, f);
            g2.drawImage(source, 0, 0, null);
            g2.dispose();
        }
        finally
        {
            g2.dispose();
        }
        
        return scaled;
    }

    /**
     * Returns <code>true</code> when the color is transparent,
     * thus when it has a alpha value not equal to <code>255</code>
     * (<code>0xFF</code>).
     * @param c the color
     * @return <code>true</code> when the color is transparent,
     */
    public static boolean isTransparent(Color c)
    {
        return c.getAlpha() != 0xFF ;
    }

    public static boolean isTransparent(Color c1, Color c2)
    {
        return (((c1.getAlpha() & c2.getAlpha()) == 0xFF) ? false : true);
    }

    /*
    public static boolean isTransparent(Color ... colors)
    {
        for (int i=0;i<colors.length;i++)
        {
            if (colors[i].getAlpha() != 0xFF)
                return true;
        }
        return false;
    }
    */

    public final static int getColorValue(Color c)
    {
        return  getColorValue(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());

    }

    public final static int getColorValue(int r, int g, int b)
    {
        return getColorValue(r, g, b, 0xFF);
    }
    
    public final static int getColorValue(int r, int g, int b, int a)
    {
        return   
             (((int) a << 24)&0xFF000000)
            |(((int) r << 16)&0x00FF0000)
            |(((int) g <<  8)&0x0000FF00)
            |( (int) b       &0x000000FF);
    }
    
    /**
     * Computes the intersection between two rectangles. The result is
     * stored in the specified rectangle.
     * @param r
     * @param ax
     * @param ay
     * @param aw
     * @param ah
     */
    public final static void intersect(Rectangle r, int ax, int ay, int aw, int ah)
    {
        intersect(r, r.x, r.y, r.width, r.height, ax, ay, aw, ah);
    }

    public final static void union(Rectangle r, int ax, int ay, int aw, int ah)
    {
        union(r, r.x, r.y, r.width, r.height, ax, ay, aw, ah);
    }
    
    public final static void union(Rectangle dest, int ax, int ay, int aw, int ah, int bx, int by, int bw, int bh)
    {
        if (aw<=0||ah<=0)
        {
            dest.x = bx;
            dest.y = by;
            dest.width = bw;
            dest.height = bh;
        }
        else if (bw<=0||bh<=0)
        {
            dest.x = ax;
            dest.y = ay;
            dest.width = aw;
            dest.height = ah;
        }
        else
        {
            int x1 = Math.min(ax, bx);
            int x2 = Math.max(ax + aw, bx + bw);
            int y1 = Math.min(ay, by);
            int y2 = Math.max(ay + ah, by + bh);
            dest.x = x1;
            dest.y = y1;
            dest.width = x2-x1;
            dest.height = y2-y1;
        }
    }
    
    /**
     * Computes the intersection of two rectangles <code>a</code> and <code>b</code>.
     * The intersection is stored in the rectangle <code>dest</code>.
     * @param dest The computed intersection
     * @param ax
     * @param ay
     * @param aw
     * @param ah
     * @param bx
     * @param by
     * @param bw
     * @param bh
     */
    public final static void intersect(Rectangle dest, int ax, int ay, int aw, int ah, int bx, int by, int bw, int bh)
    {
        // modified and ripped from Rectangle#intersection(Rectangle)
        long ax2 = ax; ax2 += aw;
        long ay2 = ay; ay2 += ah;
        long bx2 = bx; bx2 += bw;
        long by2 = by; by2 += bh;
        if (ax < bx) ax = bx;
        if (ay < by) ay = by;
        if (ax2 > bx2) ax2 = bx2;
        if (ay2 > by2) ay2 = by2;
        ax2 -= ax;
        ay2 -= ay;
        // ax2,ay2 will never overflow (they will never be
        // larger than the smallest of the two source w,h)
        // they might underflow, though...
        if (ax2 < Integer.MIN_VALUE) ax2 = Integer.MIN_VALUE;
        if (ay2 < Integer.MIN_VALUE) ay2 = Integer.MIN_VALUE;

        // store the computed intersection
        dest.x = ax;
        dest.y = ay;
        dest.width = (int) ax2;
        dest.height = (int) ay2;
    }

    /**
     * Fills the specified area with the image.
     * 
     * @param g the graphics object
     * @param tile the tile
     * @param iw width of the tile
     * @param ih height of the tile
     * @param dx translation of the tile relative to the origin (<0 means x>0 in the tile)
     * @param dy translation of the tile relative to the origin (<0 means y>0 in the tile)
     * @param x x coordinate of the filled area
     * @param y y coordinate of the filled area
     * @param w width of the filled area
     * @param h height of the filled area
     */
    public static void paintTiles(Graphics g, Image tile, int iw, int ih, int dx, int dy, int x, int y, int w, int h)
    {
        if (iw<=0||ih<=0)
            throw new IllegalArgumentException("invalid image size");
        
       // move (dx, dy) to the origin (x, y)
        
       dx -= x;
       dy -= y;

       Rectangle clip = new Rectangle();
       g.getClipBounds(clip);
       /*
       Shape saveClip = g.getClip(); 
       if (saveClip!=null)
       {
           Rectangle dummy = new Rectangle();
           {
               // get clip bounds
               g.getClipBounds(dummy);
               
               // intersect with area
               intersect(dummy, dummy.x, dummy.y, 
                       dummy.width, dummy.height, x, y, w, h);
        
               // shrink area to intersection
               x = dummy.x;
               y = dummy.y;
               w = dummy.width;
               h = dummy.height;
            }
       }
       */

       // intersect with area
       intersect(clip, clip.x, clip.y, clip.width, clip.height, x, y, w, h);

       // shrink area to intersection
       x = clip.x;
       y = clip.y;
       w = clip.width;
       h = clip.height;

       if (w<=0 || h<=0)
           return;
       
       // set clip rect to intersection with area
       g.clipRect(x,y,w,h);

       // distance from the painted (inner) area to the next larger area
       // in which the tiles fit
       int relx = (x+dx)%iw;
       int rely = (y+dy)%ih;
       if (relx<0) relx = iw-relx;
       if (rely<0) rely = ih-rely;

       x-=relx;
       y-=rely;
       w+=relx;
       h+=rely;

       // right, bottom
       int r = x+w-1;
       int b = y+h-1;
       
       // paint tiles
       
       int px, py;
       
       py = y;
       while (py<=b)
       {
           px = x;
           while (px<=r)
           {
               g.drawImage(tile, px, py, null);
               px+=iw;
           }
           py+=ih;
       }

       // restore clip
      // g.setClip(saveClip);
    }

    /**
     * Creates a image with the same size as the component using the components graphics configuration.
     * @see #createCompatibleBuffer(int, int, int, GraphicsConfiguration)
     */
    public static BufferedImage createCompatibleBuffer(Component c, int transparency) {
        GraphicsConfiguration gc = c.getGraphicsConfiguration();        
        return createCompatibleBuffer(c.getWidth(), c.getHeight(), transparency, 
                gc!=null?gc:getDefaultGraphicsConfiguration());
    }
    
    /**
     * Creates a image using the screen graphics configuration.
     * @see #createCompatibleBuffer(int, int, int, GraphicsConfiguration)
     */
    public static BufferedImage createCompatibleBuffer(Dimension size, int transparency) {
        return createCompatibleBuffer(size.width, size.height, transparency);
    }

    private static GraphicsConfiguration gc =
        GraphicsEnvironment
        .getLocalGraphicsEnvironment()
        .getDefaultScreenDevice()
        .getDefaultConfiguration();
    
    public static GraphicsConfiguration getDefaultGraphicsConfiguration() {
        return gc;
    }
    
    /**
     * Creates a image using the screen graphics configuration.
     * @see #createCompatibleBuffer(int, int, int, GraphicsConfiguration)
     */
    public static BufferedImage createCompatibleBuffer(int width, int height, int transparency) {
        return createCompatibleBuffer(width, height, transparency, getDefaultGraphicsConfiguration());
    }

    /**
     * Creates a compatible image using the components graphics configuration.
     * @see #createCompatibleBuffer(int, int, int, GraphicsConfiguration)
     */
    public static BufferedImage createCompatibleBuffer(Dimension size, int transparency, Component c) {
        return createCompatibleBuffer(size.width, size.height, transparency, c);
    }
    
    /**
     * Creates a compatible image using the components graphics configuration.
     * @see #createCompatibleBuffer(int, int, int, GraphicsConfiguration)
     */
    public static BufferedImage createCompatibleBuffer(int width, int height, int transparency, Component c) {
        
        if (c==null || c.getGraphicsConfiguration()==null) {
            return createCompatibleBuffer(width, height, transparency);
        }
        
        return createCompatibleBuffer(width, height, transparency, c.getGraphicsConfiguration());
    }

    /**
     * @see #createCompatibleBuffer(int, int, int, GraphicsConfiguration)
     */
    public static BufferedImage createCompatibleBuffer(Dimension size, int transparency, GraphicsConfiguration gc) {
        return createCompatibleBuffer(size.width, size.height, transparency, gc);
    }
    
    /**
     * Creates a compatible image.
     * @param width with of image
     * @param height height of image
     * @param transparency transparency of image
     * @param gc graphics configuration
     * @return a image compatible to the graphics configuration
     */
    public static BufferedImage createCompatibleBuffer(int width, int height, int transparency, GraphicsConfiguration gc) {
        return gc.createCompatibleImage(width, height, transparency);
    }

    /**
     * Returns true if the image has transparent pixels or the value
     * of alternativeResult if the transparency could not be determined.
     * 
     * @param image The image to check for transparent pixels
     * @return true if the image has transparent pixels.
     */
    public static boolean hasAlpha(Image image, boolean alternativeResult) {
        if (image instanceof BufferedImage) {
            BufferedImage bimage = (BufferedImage)image;
            return bimage.getColorModel().hasAlpha();
        }

        PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
        try {
           pg.grabPixels();
        } catch (InterruptedException e) {
            // ups
        }

        if ((pg.getStatus() & ImageObserver.ABORT) != 0) {
            // return alternativeResult
            return alternativeResult;
        }

        // Get the image's color model
        ColorModel cm = pg.getColorModel();
        return cm.hasAlpha();
    }

    /**
     * Returns true if the image has transparent pixels or the value
     * of true if the transparency could not be determined.
     * 
     * @param image The image to check for transparent pixels
     * @return true if the image has transparent pixels.
     */
    public static boolean hasAlpha(Image image) {
        return hasAlpha(image, true);
    }

    /*
    public static void paintRegion(Graphics g, Image image, Rectangle srcReg, int dx, int dy) {
        if (srcReg != null)
        {
            paintRegion(g, image, srcReg.x, srcReg.y, srcReg.width, srcReg.height, dx, dy);
        }
        else
        {
            Rectangle clip = g.getClipBounds(); // use clip bounds if necessary
            if (clip==null) {
                    g.drawImage(image, dx, dy, null);
            } else { // clip!=null
                    // intersect clip with image bounds
                    Rectangle ri = clip.intersection(new Rectangle(0, 0, image.getWidth(null), image.getHeight(null)));
                    if (ri.isEmpty()){
                        // no painting necessary
                    } else {
                        // paint intersection of clip and image
                        int r = ri.x+ri.width;
                        int b = ri.y+ri.height;
                        g.drawImage(image,
                                dx+ri.x, dy+ri.y, dx+r, dy+b,
                                ri.x, ri.y, r, b, null);
                    }
            }
        }
    }

    public static void paintRegion(Graphics g, Image image, int sx, int sy, int sw, int sh, int dx, int dy) {
        Rectangle clip = g.getClipBounds(); // use clip bounds if necessary
        if (clip==null) 
        {
              g.drawImage(image, dx, dy, dx+sw, dy+sh,
                    sx, sy,sx+sw,sy+ sh, null);
        } else 
        { // clip!=null
            Rectangle target = new Rectangle(0, 0, sw, sh); // move region to 0,0
            Rectangle ri = clip.intersection(target); // intersect region and component
            if (!ri.isEmpty()) 
            {

                int ix = sx+ri.x;
                int iy = sy+ri.y;
                
                g.drawImage(image,
                    dx+ri.x, dy+ri.y, dx+ri.x+ri.width, dy+ri.y+ri.height,
                    ix, iy, ix+ri.width, iy+ri.height, null);
            } 
            else 
            {
                // no painting necessary
            }
        }
    }
*/
    public static Dimension getImageSize(Image image)
    {
        int iw = image.getWidth(null);
        int ih = image.getHeight(null);
        if (iw<=0||ih<=0)
        {
            ImageQuery.waitForDimensions(image);
            iw = image.getWidth(null);
            ih = image.getHeight(null);
        }
        return new Dimension(iw, ih);
    }
    
    public static void clearRegion(Graphics2D g2, int x, int y, int w, int h) {
        Composite c = g2.getComposite();
        g2.setComposite(AlphaComposite.Clear);
        g2.fillRect(x, y, w, h);
        g2.setComposite(c);
    }
/*
    public static BufferedImage getBufferedImage( Image image )
    {
        if (image instanceof BufferedImage)
            return (BufferedImage) image;
        Dimension sz = getImageSize(image);
        
        int t = Transparency.OPAQUE;
        if (image instanceof Transparency)
            t = ((Transparency)image).getTransparency();
        
        BufferedImage bi = createCompatibleBuffer(sz.width, sz.height, t);
        Graphics2D g2 = bi.createGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        return bi;
    }*/

}
 