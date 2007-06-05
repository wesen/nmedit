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
package net.sf.nmedit.jtheme.component.plaf;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;

import javax.swing.JComponent;
import javax.swing.border.Border;

import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.JTLight;

public class JTBasicLightUI extends JTLightUI
{

    private final static Point2D.Float gradientStart = new Point2D.Float(1,0);
    private Point2D.Float gradientStop = new Point2D.Float(0,0);

    private static UIInstance<JTLightUI> uiInstance = new UIInstance<JTLightUI>(JTLightUI.class);
    
    public static JTLightUI createUI(JComponent c)
    {
        JTLightUI ui = uiInstance.getInstance(c);
        if (ui == null) uiInstance.setInstance(c, ui=new JTBasicLightUI());
        return ui;
    }
    
    private boolean borderSet = false;
    private transient Border border;

    public void installUI(JComponent c)
    {
        if (!borderSet)
        {
            border = ((JTComponent)c).getContext().getUIDefaults().getBorder(BORDER_KEY);
            borderSet = true;
        }
        if (border != null) c.setBorder(border);
    }
    
    public void uninstallUI(JComponent c)
    {
        c.setBorder(null);
    }
    
    public void paintStaticLayer(Graphics2D g, JTComponent c)
    {
        
    }

    public void paintDynamicLayer(Graphics2D g, JTComponent c)
    {
        JTLight l = (JTLight) c;

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        switch (l.getType())
        {
            case JTLight.METER:
                paintMeter(l, g);
                break;
            case JTLight.LED:
                paintLED(l, g);
                break;
            case JTLight.LED_ARRAY:
                paintLEDArray(l, g);
                break;
        }
    }
    
    
    protected void paintLEDArray(JTLight l, Graphics2D g)
    {
        // value = minValue means all disabled
        int min = l.getMinValue();
        int value = l.getValue();
        int leds = l.getMaxValue() - min;
        
        if (leds <= 0) return;
        
        int w = l.getWidth();
        int h = l.getHeight();
        int s = Math.min(w, h);
        
        float gx = w/(float)leds;
        float off = ((gx-s)/2f);
        
        for (int i=1;i<=leds;i++)
            paintLED(g, (int)(off+gx*(i-1)), 0, s, i==value);
    }

    protected void paintLED(JTLight l, Graphics2D g)
    {
        paintLED(g, 0, 0, Math.min(l.getWidth(), l.getHeight()), l.isLEDOn());
    }
    
    private static final Color LON = Color.GREEN;
    private static final Color LOFF = LON.darker().darker();
    private static final Color LOUTLINE = LOFF.darker().darker();
    
    protected void paintLED(Graphics2D g2, int x, int y, int s, boolean on)
    {
        g2.setColor(on ? LON:LOFF);
        g2.fillOval(x, y, s-1, s-1);
        g2.setColor(LOUTLINE);
        g2.drawOval(x, y, s-2, s-2);
    }

    public final static Color MODULE_BACKGROUND = Color.decode("#BFBFBF");

    public final static Color AUDIO_LEVEL_DISPLAY_LOW = Color.decode("#087309");
    public final static Color AUDIO_LEVEL_DISPLAY_HIGH = Color.decode("#767518");
    public final static Color AUDIO_LEVEL_DISPLAY_LIGHT = Color.decode("#00CC00");
    
    protected void paintMeter(JTLight l, Graphics2D g2) 
    {
        final int width = l.getWidth();
        final int height = l.getHeight();
        
        float range = l.getMaxValue()-l.getMinValue();
        float scale = range == 0 ? 0 :
            (l.getValue()-l.getMinValue())/range;
        
        gradientStop.x = width-1;
        GradientPaint gradient = new GradientPaint(gradientStart, AUDIO_LEVEL_DISPLAY_LOW,
                gradientStop, AUDIO_LEVEL_DISPLAY_HIGH);
        Paint p = g2.getPaint();
        g2.setPaint(gradient);
        g2.fillRect(1,1,width-2,height-2);
        g2.setPaint(p);

        g2.setColor(MODULE_BACKGROUND.darker());
        g2.drawLine(1, 0, width-3, 0); // top
        g2.setColor(MODULE_BACKGROUND.brighter());
        g2.drawLine(1, height-1, width-2, height-1); // bottom
        
        g2.setColor(MODULE_BACKGROUND.darker());
        g2.drawLine(0, 1, 0, height-2); // left
        g2.setColor(MODULE_BACKGROUND.brighter());
        g2.drawLine(width-1, 1, width-1, height-2); // right
        
        // dynamic
        g2.setColor(AUDIO_LEVEL_DISPLAY_LIGHT);
        int w = width-2;
        //final float scale = 0.25f; 
        
        int scaledWidth = (int) Math.round(w * scale);
        g2.fillRect(1,1,scaledWidth,height-2);
    }

}
