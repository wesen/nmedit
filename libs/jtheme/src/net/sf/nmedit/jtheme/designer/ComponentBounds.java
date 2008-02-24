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
package net.sf.nmedit.jtheme.designer;

import java.awt.AWTEvent;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;

/**
 * Draws the boundaries of a child component and handles mouse events / actions.
 * 
 * @author Christian Schneider
 */
public class ComponentBounds extends JComponent
{

    /**
     * 
     */
    private static final long serialVersionUID = 8688680759876963575L;
    private static final float[] dash = { 6, 3 };
    private static final BasicStroke dashStroke = new BasicStroke(1,
            BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1, dash, 0 );
    
    private JComponent component;
    private boolean selected = false;
    
    public ComponentBounds(JComponent component)
    {
        this.component = component;
        setToolTipText(component.getClass().getName());
        setOpaque(false);
        install();
    }
    
    public boolean isSelected()
    {
        return selected;
    }
    
    public void setSelected(boolean selected)
    {
        if (this.selected != selected)
        {
            this.selected = selected;
            repaint();
        }
    }
    
    public ComponentView getView()
    {
        try
        {
            return (ComponentView) getParent();
        }
        catch (ClassCastException e)
        {
            return null;
        }
    }
    
    protected void install()
    {
        component.putClientProperty(ComponentBounds.class.getName(), this);
        BasicEventHandler.getInstance(this, true).install(this);
        updateBounds();
    }
    
    public void uninstall()
    {
        component.putClientProperty(ComponentBounds.class.getName(), null);
        BasicEventHandler.getInstance(this, false).uninstall(this);
    }

    public static ComponentBounds cbFor(JComponent c)
    {
        try
        {
            return (ComponentBounds) c.getClientProperty(ComponentBounds.class.getName());
        }
        catch (ClassCastException e)
        {
            return null;
        }
    }
    
    protected void updateBounds()
    {
        Rectangle newBounds = component.getBounds();
        Rectangle oldBounds = getBounds();

        if (!newBounds.equals(oldBounds))    
        {
            setBounds(newBounds);

        }
    }

    protected void paintComponent(Graphics g)
    {
        g.setColor(selected ? Color.RED : Color.BLACK);
        drawOutline(g, 0, 0, getWidth(), getHeight());
        if (selected)
        {
            g.drawRect(0, 0, 4, 4);
            g.drawRect(getWidth()-4-1, 0, 4, 4);
            g.drawRect(0, getHeight()-4-1, 4, 4);
            g.drawRect(getWidth()-4-1, getHeight()-4-1, 4, 4);
        }
    }
    
    protected void paintChildren(Graphics g)
    {
        // no op
    }
    
    /**
     * Draws a dash stroke outline.
     */
    public static void drawOutline(Graphics g, int x, int y, int width, int height)
    {
        Graphics2D g2 = (Graphics2D) g;
        Stroke oldStroke = g2.getStroke();
        g2.setStroke(dashStroke);
        g2.drawRect(0, 0, width-1, height-1);
        g2.setStroke(oldStroke);
    }
    
    /**
     * Handles all events.
     */
    private static class BasicEventHandler
    implements ComponentListener, MouseListener, MouseMotionListener
    {

        private Rectangle mousePressedComponentBounds;
        private Point mousePressedLocation;
        
        public static BasicEventHandler getInstance(ComponentBounds cb, boolean create)
        {
            BasicEventHandler eh = null;
            try
            {
                eh = (BasicEventHandler) cb.getClientProperty(BasicEventHandler.class.getName()); 
            }
            catch (ClassCastException e)
            {
                // no op
            }
            
            if (eh == null && create)
            {
                eh = new BasicEventHandler();
            }
            
            return eh;
        }
        
        public void install(ComponentBounds cb)
        {
            cb.putClientProperty(BasicEventHandler.class.getName(), this);
            cb.component.addComponentListener(this);

            cb.addMouseListener(this);
            cb.addMouseMotionListener(this);
        }
        
        public void uninstall(ComponentBounds cb)
        {
            cb.putClientProperty(BasicEventHandler.class.getName(), null);
            cb.component.removeComponentListener(this);
            
            cb.removeMouseListener(this);
            cb.removeMouseMotionListener(this);
        }

        public void componentMoved(ComponentEvent e)
        {
            ComponentBounds cb = cbSource(e);
            if (cb != null)
                cb.updateBounds();
        }

        public void componentResized(ComponentEvent e)
        {
            ComponentBounds cb = cbSource(e);
            if (cb != null)
                cb.updateBounds();
        }

        public void componentHidden(ComponentEvent e)
        {
            // no op
        }

        public void componentShown(ComponentEvent e)
        {
            // no op
        }
        
        private ComponentBounds cbSource(AWTEvent e)
        {
            try
            {
                return (ComponentBounds) e.getSource();
            }
            catch (ClassCastException ex)
            {
                Object o = e.getSource();
                if (o instanceof JComponent)
                    return cbFor((JComponent)o);
                return null;
            }
        }

        public void mouseClicked(MouseEvent e)
        {
            // no op
        }

        public void mouseEntered(MouseEvent e)
        {
            // no op
        }

        public void mouseExited(MouseEvent e)
        {
            // no op
        }

        public void mousePressed(MouseEvent e)
        {
            ComponentBounds cb = cbSource(e);
            if (cb == null)
            {
                mousePressedComponentBounds = null;
                mousePressedLocation = null;
            }
            else
            {
                ComponentView cv = cb.getView();
                if (cv != null)
                {
                    ComponentBounds currentSelection = cv.getSelectedComponent();
                    if (currentSelection != cb)
                    {
                        if (currentSelection!=null)
                            currentSelection.setSelected(false);
                        cv.setSelectedComponent(cb);
                        cb.setSelected(true);
                    }
                } 
                mousePressedComponentBounds = cb.getBounds();
                mousePressedLocation = e.getPoint();
            }
        }

        public void mouseReleased(MouseEvent e)
        {
            // no op
        }

        public void mouseDragged(MouseEvent e)
        {
            ComponentBounds cb = cbSource(e);
            Point p = mousePressedLocation;
            Rectangle r = mousePressedComponentBounds;
            if (p == null || r == null || cb == null)
                return;

            int moveX = -p.x+e.getX();
            int moveY = -p.y+e.getY();

            Rectangle newBounds = cb.component.getBounds();
            
            newBounds.setLocation(cb.component.getX()+moveX, 
                    cb.component.getY()+moveY);
            
            Container parent = cb.component.getParent();
            if (newBounds.x+newBounds.width>=parent.getWidth())
                newBounds.x = parent.getWidth()-newBounds.width;
            if (newBounds.x<0)
                newBounds.x = 0;
            if (newBounds.y+newBounds.height>=parent.getHeight())
                newBounds.y = parent.getHeight()-newBounds.height;
            if (newBounds.y<0)
                newBounds.y = 0;
            cb.component.setBounds(newBounds);
        }

        public void mouseMoved(MouseEvent e)
        {
            // no op
        }
        
    }
    
}
