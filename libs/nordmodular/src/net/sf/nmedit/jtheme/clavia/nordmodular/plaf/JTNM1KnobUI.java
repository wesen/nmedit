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
package net.sf.nmedit.jtheme.clavia.nordmodular.plaf;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;

import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.plaf.JTBasicKnobUI;

public class JTNM1KnobUI extends JTBasicKnobUI
{

    private static JTNM1KnobUI instance = new JTNM1KnobUI();
/*
    private BasicStroke thinStroke = new BasicStroke(0.6f);
*/
    public static JTNM1KnobUI createUI(JComponent c)
    {
        return instance;
    }
    /*
    private GradientPaint gp = null;
    private int gps = 0;
    */
    private Map<Integer, BufferedImage> backgroundMap = new HashMap<Integer, BufferedImage>();
    
    public void paintStaticLayer(Graphics2D g, JTComponent c)
    {
        final int minBorder = Math.min(c.getWidth(), c.getHeight());
        final int s = getDiameter(c);
        
        final Integer key = new Integer(minBorder); // prevent duplicate auto boxing
        BufferedImage bi = backgroundMap.get(key);
        if (bi==null)
        {
            bi = new BufferedImage(minBorder, minBorder, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = bi.createGraphics();
            try
            {
                renderBackground(g2, s);
            }
            finally
            {
                g2.dispose();
            }
            
            backgroundMap.put(key, bi);
        }
        
        if (bi==null)
            renderBackground(g, s);
        else
            g.drawImage(bi, 0, 0, null);
    }

    private static final Color bgFill = new Color(0xB0B0B0/*0x9A9A9A*/);
    private static final Stroke stroke1 = new BasicStroke(1.75f);
    private static final Stroke stroke2 = new BasicStroke(1.00f);
    private static final Color stroke2color = new Color(0xBB000000, true);
    private static final Color gradColor1 = new Color(0x77FFFFFF, true);
    private static final Color gradColor2 = new Color(0x77000000, true);
    
    protected void renderBackground( Graphics2D g2, int s )
    {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        int rad = (int)Math.floor(s/2.0);//s>>1; // mid = s/2

        g2.setColor(Color.BLACK);
        g2.drawLine(rad, rad, 0, s);
        g2.drawLine(rad, rad, s, s);
        
        g2.setColor(bgFill);
        g2.fillOval(0, 0, s-1, s-1);

        g2.setStroke(stroke1);
        g2.setPaint(new GradientPaint(6, 6, gradColor1, s-6, s-6, gradColor2));
        g2.drawOval(1, 1, s-3, s-3);
        
        g2.setStroke(stroke2);
        g2.setColor(stroke2color);
        g2.drawOval(0, 0, s-1, s-1);
        
    }

    /*
    protected void renderBackground( Graphics2D g2, int s )
    {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int rad = s>>1; // mid = s/2

        g2.setColor(Color.BLACK);
        g2.drawLine(rad, rad, 0, s);
        g2.drawLine(rad, rad, s, s);
        
        if (gp==null || gps!=s)
        {
            gps = s;
            gp = new GradientPaint(0, 0, Color.WHITE, s, s, Color.LIGHT_GRAY);
        }
        
        final Paint oldPaint = g2.getPaint();
        g2.setPaint(gp);
        g2.fillOval(0, 0, s, s);
        g2.setPaint(oldPaint);
        
        paintOutline(g2, s, Color.BLACK);
    }

    protected void paintOutline(Graphics2D g2, int s, Color c)
    {
        g2.setColor(c);
        final Stroke oldStroke = g2.getStroke();
        g2.setStroke(thinStroke);
        g2.drawOval(0, 0, s-1, s-1);
        g2.setStroke(oldStroke);
    }
*/
}

