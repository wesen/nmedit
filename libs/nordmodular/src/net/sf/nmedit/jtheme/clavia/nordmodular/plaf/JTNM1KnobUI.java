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

/*
    private BasicStroke thinStroke = new BasicStroke(0.6f);
*/
	
	 
    public static JTBasicKnobUI createUI(JComponent c) 
    {
        JTBasicKnobUI ui = uiInstance.getInstance(c);
        if (ui == null) uiInstance.setInstance(c, ui = new JTNM1KnobUI());
        return ui;
    }
    
    public void installUI(JComponent c)
    {
        super.installUI(c);
        
        c.setBackground(bgFill);
    }
    
    /*
    private GradientPaint gp = null;
    private int gps = 0;
    */
    private Map<Integer, State> backgroundMap = new HashMap<Integer, State>();
    
    private static class State
    {
        
        int hashCode;
        Color bg;
        int size;
        BufferedImage image;
        
        public State(Color bg, int size, int hashCode, BufferedImage bi)
        {
            this.bg = bg;
            this.size = size;
            this.hashCode = hashCode;
            this.image = bi;
        }
        
        static int computeHash(Color bg, int size)
        {
            return (bg==null?0:(bg.hashCode()<<4))+size;
        }
        
        public int hashCode()
        {
            return hashCode;
        }
        
        public boolean equals(Object o)
        {
            if (o == this) return true;
            if (o == null||(!(o instanceof State))) return false;
            State st = (State)o;
            return isEqual(st.bg, st.size);
        }
        
        public boolean isEqual(Color bg, int size)
        {
            return this.bg.getRGB() == bg.getRGB() && this.size == size;
        }
    }
    
    public void paintStaticLayer(Graphics2D g, JTComponent c)
    {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //final int minBorder = Math.min(c.getWidth(), c.getHeight());
        final int s = diameter(c);
        
        final Color bg = c.getBackground();
        
        final int hashCode = State.computeHash(bg, s); 
        
        final Integer key = new Integer(hashCode); // prevent duplicate auto boxing
        BufferedImage bi;
        State st = backgroundMap.get(key);
        if (st==null)
        {
            bi = new BufferedImage(s, s, BufferedImage.TYPE_INT_ARGB);
            st = new State(bg, s, hashCode, bi);
            Graphics2D g2 = bi.createGraphics();
            try
            {
                renderBackground(g2, s, c.getBackground());
            }
            finally
            {
                g2.dispose();
            }
            
            backgroundMap.put(key, st);
        }
        else
        {
            bi = st.image;
        }
        
        if (bi==null)
            renderBackground(g, s, c.getBackground());
        else
            g.drawImage(bi, 0, 0, null);
    }

    private static final Color bgFill = new Color(/*0xB0B0B0/**/0xA0A0A0);
    private static final Stroke stroke1 = new BasicStroke(1.75f);
    private static final Stroke stroke2 = new BasicStroke(1.00f);
    //private static final Color stroke2color = new Color(0xBB000000, true);
    private static final Color gradColor1 = new Color(0x77FFFFFF, true);
    private static final Color gradColor2 = new Color(0x77000000, true);
    
    protected void renderBackground( Graphics2D g2, int s, Color background )
    {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
/*
        g2.setColor(Color.BLUE);
        g2.drawRect(0, 0, s-1, s-1);*/
        
        int rad = s/2;//(int)Math.floor((s)/2.0f);//s>>1; // mid = s/2

        g2.setColor(Color.BLACK);
        g2.drawLine(rad, rad, 0, s);
        g2.drawLine(rad, rad, s, s);
        
        if (background == null)
            background = bgFill;
        
        g2.setColor(background);
        g2.fillOval(0, 0, s-1, s-1);

        g2.setStroke(stroke1);
        g2.setPaint(new GradientPaint(6, 6, gradColor1, s-6, s-6, gradColor2));
        g2.drawOval(1, 1, s-3, s-3);
        
        g2.setStroke(stroke2);
        g2.setColor(background.darker());//stroke2color);
        g2.drawOval(0, 0, s-1, s-1);
        
    }

}

