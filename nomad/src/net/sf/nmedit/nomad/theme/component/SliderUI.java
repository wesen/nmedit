package net.sf.nmedit.nomad.theme.component;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
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
 * Created on Aug 26, 2006
 */

public class SliderUI extends ComponentUI
{
    
    private final static SliderUI instance = new SliderUI();
    
    private final static Color DEFAULT_BG_COLOR = Color.decode("#DCDCDC");
    private final static Color GRIP_COLOR = Color.decode("#8A8A8A");

    private EventHandler eventHandler = new EventHandler();
    private Border DEFAULT_BORDER = BorderFactory.createLineBorder(GRIP_COLOR, 1);
    
    public SliderUI()
    {
    }
    
    public static ComponentUI createUI(JComponent c) 
    {
        return instance;
    }
    
    public void installUI(JComponent c) 
    {
        super.installUI(c);
        c.addMouseListener(eventHandler);
        c.addMouseMotionListener(eventHandler);
        c.addMouseWheelListener(eventHandler);
        c.addFocusListener(eventHandler);
        c.addKeyListener(eventHandler);
        c.setBorder(DEFAULT_BORDER);
    }
    
    public void uninstallUI(JComponent c) 
    {
        c.removeMouseListener(eventHandler);
        c.removeMouseMotionListener(eventHandler);
        c.removeMouseWheelListener(eventHandler);
        c.removeFocusListener(eventHandler);
        c.removeKeyListener(eventHandler);
        super.uninstallUI(c);
    }

    public void paint(Graphics g, JComponent c) 
    {
        Color bgColor = c.getBackground();
        /*if(bgColor == null) */bgColor = DEFAULT_BG_COLOR;
        g.setColor(bgColor);
        g.fillRect(0, 0, c.getWidth(), c.getHeight());
        
        final int sh = 3; // slider height in pixel
        Slider s = (Slider) c;
        
        int v = s.getValue()-s.getMinValue();
        double pv = v/(double)(s.getMaxValue()-s.getMinValue());

        Insets is = s.getInsets();
        final int padtop = is == null ? 0 : is.top;
        final int padbottom = is == null ? 0 : is.bottom;
        
        g.setColor(GRIP_COLOR);
        g.fillRect( 0, (int)((1-pv)*(s.getHeight()-sh-padtop-padbottom))+padtop, s.getWidth(), sh );
    }

    private class EventHandler implements MouseListener, MouseMotionListener, 
        KeyListener, FocusListener, MouseWheelListener
    {
        
        private Cursor cCursorOld = null;
        private Component c = null;
        private Point startLocation = null;
        private int startValue = 0;
        
        private void setComponent(Component c)
        {
            if (this.c!=null)
            {
                this.c.setCursor(cCursorOld);
                this.c = null;
                cCursorOld = null;
            }

            if (c!=null)
            {
                cCursorOld = c.getCursor();
                (this.c = c).setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            }
        }

        public void mouseDragged( MouseEvent e )
        {
            if (e.getComponent()==c) // necessary ???
            { 
                Slider s = (Slider) c;
                
                int dy = startLocation.y-e.getY();
                double py = Math.max(-100, Math.min(100, dy/2.0))/100.0; // better control by mouse+bounded value
                int vy = s.getMinValue()+(int)(py*(s.getMaxValue()-s.getMinValue()));
                s.setValue(Math.max(s.getMinValue(), Math.min(startValue+vy, s.getMaxValue())));    
            }
        }

        public void keyTyped( KeyEvent e )
        {
            // TODO Auto-generated method stub
            
        }

        public void keyPressed( KeyEvent e )
        {
            // TODO Auto-generated method stub
            
        }

        public void keyReleased( KeyEvent e )
        {
            // TODO Auto-generated method stub
            
        }

        public void focusGained( FocusEvent e )
        {
            // TODO Auto-generated method stub
            
        }

        public void focusLost( FocusEvent e )
        {
            // TODO Auto-generated method stub
            
        }

        public void mouseWheelMoved( MouseWheelEvent e )
        {
            // TODO Auto-generated method stub
            
        }

        public void mouseClicked( MouseEvent e )
        {
            // TODO Auto-generated method stub
            
        }

        public void mousePressed( MouseEvent e )
        {
            setComponent(e.getComponent());
            startLocation = new Point(e.getX(), e.getY());

            startValue = ((Slider) e.getComponent()).getValue();
        }

        public void mouseReleased( MouseEvent e )
        {
            setComponent(null);
            startLocation = null;
        }

        public void mouseMoved( MouseEvent e )
        {
        }

        public void mouseEntered( MouseEvent e )
        {
            // TODO Auto-generated method stub
            
        }

        public void mouseExited( MouseEvent e )
        {
            // TODO Auto-generated method stub
            
        }
        
    }
    
}
