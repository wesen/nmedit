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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;

import javax.swing.InputMap;
import javax.swing.JComponent;

import net.sf.nmedit.jtheme.JTUtils;
import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.JTControl;
import net.sf.nmedit.jtheme.component.plaf.JTBasicControlUI;
import net.sf.nmedit.jtheme.component.plaf.JTControlUI;
import net.sf.nmedit.nmutils.swing.NMLazyActionMap;

public class JTNM1ResetButtonUI extends JTBasicControlUI
{

    private final static Color defaultBackground = Color.decode("#61A387");
    private final static Color defaultForeground = Color.decode("#74E25D");
    private final static Color defaultOutline = new Color(0, 0, 0, 0.4f);
    private final Color defaultHighlight = new Color(245, 245, 220, 180);
    private final static boolean upsidedown = true;
    //private boolean rememberState = false;
    
    private Color clOutline = defaultOutline;
    private Color clHighlight = defaultHighlight;
    
    private static Metrics metrics = new Metrics();
    private static JTNM1ResetButtonUI instance = new JTNM1ResetButtonUI();
    
    public static JTControlUI createUI(JComponent c)
    {
        return instance;
    }
    
    public boolean contains(JComponent c, int x, int y)
    {
        metrics.update(c.getWidth(), c.getHeight());
        return metrics.polygonFill.getBounds().contains(x, y);
    }

    public Dimension getPreferredSize(JComponent c)
    {
        return new Dimension(9, 6);
    }
    
    protected void configureGraphics(Graphics2D g2) 
    {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
    }
    
    public void paintStaticLayer(Graphics2D g, JTComponent c) 
    {
        configureGraphics(g);
        metrics.update(c.getWidth(), c.getHeight());
        g.setColor(defaultBackground);
        g.fill(metrics.polygonFill);
        g.setColor(clOutline);
        g.draw(metrics.polygonDraw);
    }
    
    private boolean isInDefaultState(JTControl c)
    {
        return c.getDefaultValue() == c.getValue();
    }
    
    public void paintDynamicLayer(Graphics2D g, JTComponent c) {

        JTControl control = (JTControl) c;
        
        if (isInDefaultState(control))
        {
            configureGraphics(g);
            metrics.update(c.getWidth(), c.getHeight());
            
            Color f = defaultForeground;
            g.setColor(JTUtils.alpha(f, 165));
            g.fill(metrics.polygonFill);

            g.setColor(c.hasFocus() ? clHighlight : clOutline);
            // outer outline
            g.draw(metrics.polygonDraw);
        }
        else if (control.hasFocus())
        {
            configureGraphics(g);
            metrics.update(c.getWidth(), c.getHeight());
            
            g.setColor(clHighlight);
            // outer outline
            g.draw(metrics.polygonDraw);
        }
    }

    private transient BasicControlListener rbControlListenerInstance;
    
    protected BasicControlListener createControlListener(JTControl control) 
    {
        if (rbControlListenerInstance == null)
            rbControlListenerInstance = new ResetButtonControlListener();
        return rbControlListenerInstance;
    }

    public static class ResetButtonControlListener extends BasicControlListener
    {

        public static void loadActionMap(NMLazyActionMap map) 
        {  
            map.put(new Actions(DEFAULTVALUE));
        }

        protected void fillInputMap(InputMap map)
        {
            addDefaultValueKS(map);
        }

        public void mousePressed( MouseEvent e )
        {
            JTControl control = controlFor(e);
            if (control == null)  return;
            if (!control.hasFocus())
                control.requestFocus();
        }
        
        public void mouseReleased( MouseEvent e )
        {
            JTControl control = controlFor(e);
            if (control == null)  return;
            control.setValue(control.getDefaultValue());
        }

    }
    
    private static class Metrics 
    {
        int w = 0;
        int h = 0;

        Polygon polygonFill = null;
        Polygon polygonDraw = null;
        
        public void update(int width, int height) {
            if (w==width&&h==height&&polygonFill!=null&&polygonDraw!=null)
                return;
            
            w = width; h = height;
            int s = (1-w%2); // is 0(uneven) or 1(even) , shift so that middle is not even
            int thypotenuse = 0;
            int theight = 0;
            int middle = w/2-s;

            // hypotenuse c, hight h : condition : c = 2*h-1
            theight = h;
            thypotenuse = w-s;
            if (thypotenuse!=2*h-1) {
                theight = h;
                thypotenuse = 2*theight-1;
            }

            int left  = middle - (int) Math.floor ( thypotenuse/2.0d );
            int right = middle + (int) Math.floor ( thypotenuse/2.0d );
            int top   = h/2 - (int) Math.floor ( theight/2.0d );
            int bottom= h/2 + (int) Math.floor ( theight/2.0d );

            if (upsidedown) {
                polygonFill = new Polygon();
                polygonFill.addPoint(left, top+1);
                polygonFill.addPoint(middle, bottom+1);
                polygonFill.addPoint(right+1, top+1);
                
                polygonDraw = new Polygon();
                polygonDraw.addPoint(left, top+1);
                polygonDraw.addPoint(middle, bottom-1);
                polygonDraw.addPoint(right, top+1);
            } else {
                polygonFill = new Polygon();
                polygonFill.addPoint(left, bottom-1);
                polygonFill.addPoint(middle, top+1);
                polygonFill.addPoint(right, bottom-1);
                
                polygonDraw = new Polygon();
                polygonDraw.addPoint(left, bottom-1);
                polygonDraw.addPoint(middle, top+1);
                polygonDraw.addPoint(right, bottom-1);
            }
        }
    }

}

