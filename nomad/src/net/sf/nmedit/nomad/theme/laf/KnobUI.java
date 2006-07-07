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
 * Created on May 14, 2006
 */
package net.sf.nmedit.nomad.theme.laf;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.MouseInfo;
import java.awt.Paint;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;

import net.sf.nmedit.nomad.patch.ui.popup.ControlPopup;
import net.sf.nmedit.nomad.theme.NomadClassicColors;

public class KnobUI extends ComponentUI
{

    final static int BUTTON_NONE = 0;
    final static int BUTTON_1 = 1;
    final static int BUTTON_2 = 2;
    final int btnHeight = 8; 
    
    public Color clOutline1 = Color.decode("#7B7B7B");
    public Color clOutline2 = Color.BLACK;

    public Color clEdge1 = Color.decode("#C6C6C6");
    public Color clEdge2 = Color.decode("#707070");
    
    public Color clFill1 = Color.decode("#9D9D9D");
    public Color clFill2 = Color.decode("#949494");

    public Color btnBackgroundFocus = Color.CYAN;
    public Color btnBackgroundHover = NomadClassicColors.MODULE_BACKGROUND;
    
    public int borderSize = 1;
    public int edgeSize = 2;
    final double alpha = 225*Math.PI/180;

    private Border btnBorderRaised = BorderFactory.createRaisedBevelBorder();
    private Border btnBorderLowered = BorderFactory.createLoweredBevelBorder();
    
    private KnobUIFeel feel = null;
    
    private static Knob hoveredKnob = null;
    private static int hoveredButton = BUTTON_NONE;
    private static boolean mousePressed = false;

    public KnobUI()
    {
        feel = createFeel();
    }
    
    protected KnobUIFeel getFeel()
    {
        return feel;
    }
    
    protected KnobUIFeel createFeel()
    {
        return new KnobUIFeel(this);
    }

    public void installUI(JComponent c) 
    {
        c.setOpaque(false);
        c.setFocusable(true);
        Knob k = (Knob) c;
        getFeel().installFeel(k);
    }
    
    public void uninstallUI(JComponent c) 
    {
        getFeel().uninstallFeel((Knob)c);
    }
    
    public void paint(Graphics g, JComponent c) 
    {
        Knob knob = (Knob) c;
        int w = c.getWidth();
        int h = c.getHeight();

        paintKnob(g, knob, w, h);
    }
    
    private void paintKnob( Graphics g, Knob knob, int w, int h )
    {
        Graphics2D g2 = (Graphics2D) g;
        
        Object aaHint = g2.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double cx = w/2.0;
        double cy = h/2.0;
        double xradius = cx;
        double yradius = cy;
        
        int ax = getXPx(xradius, alpha, cx); // (int)((Math.cos(alpha)+1)*(w/2));
        int ay = getYPx(yradius, alpha, cy); //(int)((Math.sin(alpha)+1)*(h/2));
        
        int bx = w-ax;
        int by = h-ay;

        g2.setColor(Color.BLACK);
        g2.drawLine(w/2,h/2,getXPx(w, mapToRad(0),cx), getYPx(h, mapToRad(0),cy));
        g2.drawLine(w/2,h/2,getXPx(w, mapToRad(1),cx), getYPx(h, mapToRad(1),cy));
        
        if (borderSize>0)
        {
            Paint paOutline = new GradientPaint(ax,ay,clOutline1,bx,by,clOutline2, false);
            g2.setPaint(paOutline);
            g2.fillOval(0,0,w,h);
        }

        if (edgeSize>0)
        {
            Paint paEdge = new GradientPaint(ax+borderSize,ay+borderSize,clEdge1,bx-borderSize,by-borderSize,clEdge2, false);
            g2.setPaint(paEdge);
            g2.fillOval(borderSize,borderSize,w-borderSize*2,h-borderSize*2);
        }

        Paint paFill = new GradientPaint(ax,ay,clFill1,bx,by,clFill2, false);
        g2.setPaint(paFill);
        g2.fillOval(borderSize+edgeSize,borderSize+edgeSize,w-(borderSize+edgeSize)*2,h-(borderSize+edgeSize)*2);

        double fValue = (knob.getValue()-knob.getMinValue())/(double)(knob.getMaxValue()-knob.getMinValue());
        
        // morph
        Double morph = knob.getMorphValue();
        if (morph!=null)
        {
            g2.setColor(knob.getMorphBackground());
            g2.fillOval(0,0,w,h);
            
            // draw arc
            g2.setColor(knob.getMorphForeground());
            double startAngle = mapToRad(fValue);
            double stopAngle = mapToRad(fValue+morph.doubleValue());
            //stopAngle %= 360
            stopAngle = Math.max(mapToRad(0), Math.min(stopAngle, mapToRad(1)));

            startAngle*=180/Math.PI;
            stopAngle*=180/Math.PI;
            double arcAngle   = stopAngle-startAngle;// (int)(morph.doubleValue()*(360-90));
            g2.fillArc(0,0,w,h, -(int)startAngle, -(int)arcAngle);
        }
        
        // paint meter
        g2.setColor(Color.BLACK);

        xradius-=(borderSize+edgeSize+1);
        yradius-=(borderSize+edgeSize+1);
        
        g2.drawLine(w/2,h/2,getXPx(xradius, mapToRad(fValue),cx), getYPx(yradius, mapToRad(fValue),cy));
        
        if (knob.hasFocus() || knob==hoveredKnob)
        {
            paintButtons(g, knob, w, h);
        }
        
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, aaHint);
    }
    
    private void paintButtons( Graphics g, Knob knob, int w, int h )
    {
        Graphics2D g2 = (Graphics2D) g;
        
        
        g2.setColor(knob.hasFocus() ? btnBackgroundFocus : btnBackgroundHover);
        
        int y = h-btnHeight;

        Composite composite = g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        g2.fillRect(0, h-btnHeight, w, btnHeight);
        g2.setComposite(composite);
        
        PointerInfo pi=MouseInfo.getPointerInfo();
        Point p = pi.getLocation();
        SwingUtilities.convertPointFromScreen(p, knob);

        boolean btnLeftRaised = true;
        boolean btnRightRaised = true;
         
        if (mousePressed)
        switch (getHoveredButton(knob, p.x,p.y))
        {
            case BUTTON_NONE: 
                break ;
            case BUTTON_1:
                btnLeftRaised = false;
                btnRightRaised = true;
                break;
            case BUTTON_2:
                btnLeftRaised = true;
                btnRightRaised = false;
                break;
        }
        
        paintButton(g, knob, w, h, 0, y, w/2, btnHeight, btnLeftRaised, false);
        paintButton(g, knob, w, h, w/2, y, w/2, btnHeight, btnRightRaised, true);
    }

    private int getHoveredButton(Knob knob, int x, int y)
    {
        if (knob.contains(x, y) && y>=(knob.getHeight()-btnHeight))
        {
            return (x<knob.getWidth()/2) ? BUTTON_1 : BUTTON_2;
        }
        return BUTTON_NONE;
    }
    
    protected void paintButton(Graphics g, Knob knob, int w, int h, int bx, int by, int bw, int bh, boolean raised, boolean increase)
    {
        Border border = raised ? btnBorderRaised : btnBorderLowered; 
        border.paintBorder(knob, g, bx, by, bw, bh);
        
        int cx = bx+bw/2;
        
        Insets i = border.getBorderInsets(knob);
        bx+=i.left;
        by+=i.top;
        bw-=i.left+i.right+4;
        bh-=i.top+i.bottom;
        
        g.setColor(Color.BLACK);
        
        if (!raised)
        {
            bx++;
            by++;
            cx++;
        }
        
        bw = (int)Math.sqrt(bh*bh*2);
        
        int y1 = by;
        int y2 = by+bh;
        
        if (!increase)
        {
            int tmp = y2;
            y2 = y1;
            y1 = tmp;
        }

        g.fillPolygon(new Polygon(new int[]{cx, cx+bw/2, cx-bw/2}, new int[]{y1, y2, y2}, 3));
    }

    public int getXPx(double radius, double rad, double cx)
    {
        return (int)(Math.cos(rad)*radius+cx);
    }
    
    public int getYPx(double radius, double rad, double cy)
    {
        return (int)(Math.sin(rad)*radius+cy);
    }
    
    public double mapToRad(double fValue)
    {   // 90+45 --> 45
        return (90+45+fValue*(360-90))*Math.PI/180;
    }

    public Dimension getPreferredSize(JComponent c) 
    {
        return new Dimension(24,24);
    }
    
    public Dimension getMinimumSize(JComponent c) 
    {
        return null;        
    }
    
    public Dimension getMaximumSize(JComponent c) 
    {
        return null;
    }

    private static KnobUI knobUI = new KnobUI();  
    
    public static ComponentUI createUI(JComponent c) 
    {
        return knobUI;
    }
    
    public static class KnobUIFeel implements MouseListener, MouseMotionListener,
        KeyListener, ActionListener, FocusListener
    {
        
        private Point start = null;
        private int startValue = 0;
        private double startMorph = 0;
        private boolean setMorph = false;
        private KnobUI ui;
        private HoldTimer timer = new HoldTimer(this);
        
        public KnobUIFeel(KnobUI ui)
        {
            this.ui = ui;
        }
        
        public void installFeel(Knob knob)
        {
            knob.addMouseListener(this);
            knob.addMouseMotionListener(this);
            knob.addKeyListener(this);
            knob.addFocusListener(this);
        }
        
        public void uninstallFeel(Knob knob)
        {
            knob.removeMouseListener(this);
            knob.removeMouseMotionListener(this);
            knob.removeKeyListener(this);
            knob.removeFocusListener(this);
        }

        public void mouseClicked( MouseEvent e )
        {
            // TODO Auto-generated method stub
        }

        public void mousePressed( MouseEvent e )
        {
            mousePressed = true;
            
            if (timer.isRunning())
                return ;
            
            if (e.getComponent() instanceof Knob)
            {
                Knob knob = (Knob) e.getComponent();

                if (e.isPopupTrigger())
                {
                    ControlPopup popup = new ControlPopup(knob); 
                    popup.show(knob, e.getX(), e.getY());
                    return ;
                }
                
                hoveredButton = ui.getHoveredButton(knob, e.getX(), e.getY()); 
                    
                if (hoveredButton!=BUTTON_NONE)
                {
                    hoveredKnob = knob;
                    handleButtonPressed(hoveredKnob, hoveredButton);
                    timer.start();
                }
                else
                {
                    hoveredKnob = null;
                    start = new Point(e.getPoint());
                    startValue = knob.getValue();
                    Double m = knob.getMorphValue();
                    startMorph = m == null ? 0 : m.doubleValue();
                    setMorph = e.isControlDown() || e.getClickCount()>=2;
                    if (setMorph)
                    {
                        knob.setMorphValue(startValue);
                    }
                }
                knob.requestFocus();
                knob.repaint();
            }
        }

        public void mouseReleased( MouseEvent e )
        {
            start = null;
            mousePressed = false;
            if (timer.isRunning())
                timer.stop();
            
            if (hoveredKnob!=null)
                hoveredKnob.repaint();
        }

        public void mouseEntered( MouseEvent e )
        {
            if (e.getComponent() instanceof Knob)
            {
                hoveredKnob = (Knob) e.getComponent();
                hoveredButton = ui.getHoveredButton(hoveredKnob, e.getX(), e.getY());
                hoveredKnob.repaint();
            }
        }

        public void mouseExited( MouseEvent e )
        {
            if (hoveredKnob!=null)
            {
                Knob knob = hoveredKnob;
                hoveredKnob = null;
                knob.repaint();
            }
        }

        public void mouseDragged( MouseEvent e )
        {
            if (start!=null && e.getComponent() instanceof Knob)
            {
                Knob knob = (Knob) e.getComponent();
                int delta = e.getX()-start.x;
                
                if (!setMorph)
                {
                    int dragValue = startValue + (delta/3);
                    dragValue = Math.max(knob.getMinValue(), Math.min(dragValue, knob.getMaxValue()));
                    knob.setValue(dragValue);
                }
                else
                {
                    double dragValue = startMorph+(delta/3)/(double)knob.getRange();
                    double dragValueAbs = dragValue*knob.getRange()+knob.getValue();
                    dragValueAbs = Math.max(knob.getMinValue(), Math.min(dragValueAbs, knob.getMaxValue()));
                    dragValue = (dragValueAbs-knob.getValue())/knob.getRange();
                    
                    knob.setMorphValue(dragValue);                    
                }
            }
        }

        public void mouseMoved( MouseEvent e )
        {
            if (e.getComponent() instanceof Knob)
            {
                Knob knob = (Knob) e.getComponent();
                
                int hBtn = ui.getHoveredButton(knob, e.getX(), e.getY());
                
                if (hBtn!=hoveredButton)
                {
                    hoveredButton = hBtn;
                    knob.repaint();
                }
            }
        }

        public void keyTyped( KeyEvent e )
        {
        }

        public void keyPressed( KeyEvent e )
        {
            if (e.getComponent() instanceof Knob)
            {
                Knob knob = (Knob) e.getComponent();
                
                switch (e.getKeyCode())
                {
                    case KeyEvent.VK_ESCAPE:
                        if (start!=null)
                        {
                            if (!setMorph)
                            {
                                knob.setValue(startValue);
                            }
                            else
                            {
                                if (startMorph==0)
                                    knob.setMorphValue(null);
                                else
                                    knob.setMorphValue(startMorph);                    
                            }
                            
                            start = null;
                        }
                        break;
                    case KeyEvent.VK_UP:
                        
                        if (e.isControlDown())
                        {
                            knob.incMorph();
                        }
                        else
                        {
                            knob.incValue();
                        }
                        break;
                        
                    case KeyEvent.VK_DOWN:
                        if (e.isControlDown())
                        {
                            knob.decMorph();
                        }
                        else
                        {
                            knob.decValue();
                        }
                        break;
                }
            }
        }

        public void keyReleased( KeyEvent e )
        {
            // TODO Auto-generated method stub
            
        }

        public void actionPerformed( ActionEvent e )
        {
            if (hoveredKnob!=null)
                handleButtonPressed(hoveredKnob, hoveredButton);
        }
        
        public void handleButtonPressed(Knob knob, int btn)
        {
            switch (btn)
            {
                case BUTTON_1:
                    knob.decValue();
                    break;
                case BUTTON_2:
                    knob.incValue();
                    break;
            }
        }

        public void focusGained( FocusEvent e )
        {
            // TODO Auto-generated method stub
            
        }

        public void focusLost( FocusEvent e )
        {
            if (e.getComponent()==hoveredKnob)
            {
                Knob k = hoveredKnob;
                hoveredKnob = null;
                k.repaint();
            }
        }
        
    }
    
}
