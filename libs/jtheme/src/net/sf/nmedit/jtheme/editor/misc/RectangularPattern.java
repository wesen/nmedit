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
package net.sf.nmedit.jtheme.editor.misc;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class RectangularPattern
{

    public static final Color COLOR_A = new Color(0x999999);
    public static final Color COLOR_B = new Color(0x666666);
    public static final int SIZE = 8;
    
    public static Paint create()
    {
        return create(COLOR_A, COLOR_B, SIZE);
    }
    
    public static Paint create(Color a, Color b, int size)
    {
        return create(a, b, size, false);
    }

    public static Paint create(Color a, Color b, int size, boolean useAlpha)
    {
        BufferedImage img = createPattern(a, b, size, useAlpha);
        
        return new TexturePaint(img, new Rectangle2D.Float(0,0, size*2, size*2));
    }
    
    private static BufferedImage createPattern(Color a, Color b, int size, boolean useAlpha)
    {
        
        boolean translucent = useAlpha  
            && (a.getAlpha()&0xFF)<0xFF 
            && (b.getAlpha()&0xFF)<0xFF;
        
        int ss = size*2;
        BufferedImage bi = new BufferedImage(ss, ss,
                translucent ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
        
        Graphics2D g = bi.createGraphics();
        try
        {
            paintPattern(bi, g, a, b, size);
        }
        finally
        {
            g.dispose();
        }
        
        return bi;
    }

    private static void paintPattern(BufferedImage bi, Graphics2D g, Color a, Color b, int size)
    {
        g.setColor(a);
        g.fillRect(0, 0, bi.getWidth(), bi.getHeight());
        g.setColor(b);
        g.fillRect(size, 0, size, size);
        g.fillRect(0, size, size, size);
    }
    
}

