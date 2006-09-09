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
 * Created on Sep 8, 2006
 */
package net.sf.nmedit.jtheme;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

public class ComponentEditorView extends JComponent implements MouseListener, MouseMotionListener, ActionListener
{
    
    private ComponentHook componentHook;

    public ComponentEditorView(ThemeConfiguration config, JComponent c)
    {
        setOpaque(true);
        setBackground(Color.WHITE);
        setForeground(Color.BLACK);
        setFont(new Font("serif", Font.PLAIN, 12));
        componentHook = new ComponentHook(config, c);
        
        addMouseListener(this);
        addMouseMotionListener(this);
        
        setComponentPopupMenu(createPopup());
    }
    
    private JPopupMenu createPopup()
    {
        JPopupMenu pm = new JPopupMenu();
        pm.add("config").addActionListener(this);
        return pm;
    }

    public ThemeConfiguration getThemeConfiguration()
    {
        return componentHook.getThemeConfiguration();
    }
    
    public JComponent getView()
    {
        return componentHook.getComponent();
    }
    
    private Image bi = null;
    private int vw=0, vh=0;
    
    private Graphics2D getViewGraphics()
    {
        Dimension d = getView().getPreferredSize();
        if (d == null) d  =getView().getSize();
        else getView().setSize(d);
        int nw = d.width;
        int nh = d.height;
        
        
        if (bi==null||vw!=nw||vh!=nh)
        {
            vw = nw;
            vh=nh;
            if (vw<=0||vh<=0)
            {
                return null;
            }            
            bi = createImage(vw, vh);
            Graphics2D g2 = (Graphics2D) bi.getGraphics();
            
            g2.setBackground(getBackground());
            g2.setColor(getForeground());
            g2.setFont(getFont());
            
            return g2;
        }
        return null;
    }

    public void paintComponent(Graphics g)
    {
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        Graphics2D gview = getViewGraphics();
        if (gview!=null)
        {
            JComponent c = getView();
           // add(c);
            c.validate();
            c.printAll(gview);
           // remove(c);
            gview.dispose();
        }
        
        
        int x = (getWidth()-vw)/2;
        int y = (getHeight()-vh)/2;
        
        g.drawImage(bi, x, y, this);
        g.setColor(Color.BLACK);
        g.drawRect(x-1, y-1, vw+2, vh+2);
    }
    
    private JComponent locateComponentInView(int x, int y)
    {
        JComponent c = getView();
        c.setLocation((getWidth()-vw)/2, (getHeight()-vh)/2);
        add(c);
        Component cc = SwingUtilities.getDeepestComponentAt(this, x, y);
        if (cc==this) cc = null;
        remove(c);
        return (JComponent) cc;
    }

    public void mouseClicked( MouseEvent e )
    {
    }
    
    private JComponent dragged = null;
    private Point dragStart = null;
    private Point pos = null;

    public void mousePressed( MouseEvent e )
    {
        dragged = locateComponentInView(e.getX(), e.getY());
        if (dragged==getView())
            dragged = null;
        
        dragStart = e.getPoint();
    }

    public void mouseReleased( MouseEvent e )
    {
        dragged = null;
    }

    public void mouseEntered( MouseEvent e )
    {
        // TODO Auto-generated method stub
        
    }

    public void mouseExited( MouseEvent e )
    {
        // TODO Auto-generated method stub
        
    }

    public void mouseDragged( MouseEvent e )
    {
        pos = e.getPoint();
        if (dragged != null)
        {
            int dx = e.getX()-dragStart.x;
            int dy = e.getY()-dragStart.y;
            
            dragged.setLocation(dragged.getX()+dx, dragged.getY()+dy);
            dragStart = e.getPoint();
            bi=null; // TODO erase image
            repaint();
        }
    }

    public void mouseMoved( MouseEvent e )
    {
        pos = e.getPoint();
    }

    public void actionPerformed( ActionEvent e )
    {
        JComponent c = locateComponentInView(pos.x, pos.y);
        if (c!=getView() && c!=null)
        {
            ComponentConfiguration conf = componentHook.getComponentConfiguration(c);
            
            Map<String, Property> map = conf.getProperties();
            for (String p : map.keySet())
            {
                System.out.println(p+"="+map.get(p).getValue(c));
            }
        }
    }
    
}
