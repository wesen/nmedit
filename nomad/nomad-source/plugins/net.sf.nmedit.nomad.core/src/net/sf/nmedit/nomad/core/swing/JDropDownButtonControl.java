/* Copyright (C) 2007 Christian Schneider
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
package net.sf.nmedit.nomad.core.swing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPopupMenu;
import javax.swing.Timer;

public class JDropDownButtonControl extends MouseAdapter implements ActionListener
{
    
    private JButton btn;
    private JPopupMenu popup;
    private ActionListener defaultAction;

    public JDropDownButtonControl(JButton btn, JPopupMenu popup)
    {
        this(btn, popup, null);
    }

    public JDropDownButtonControl(JButton btn, JPopupMenu popup, ActionListener defaultAction)
    {
        this.btn = btn;
        this.popup = popup;
        this.defaultAction = defaultAction;
        
        install();
        installDropDownIcon();
    }
    
    private void installDropDownIcon()
    {
        Icon icon = btn.getIcon();
        int w = 0;
        int h = 0;
        int ix = 0;
        if (icon != null)
        {
            w = icon.getIconWidth();
            h = icon.getIconHeight();
        }
        if (w>0||h>0)
        {
            ix = w+btn.getIconTextGap();
            w = ix+7;
            h = Math.max(h, 3);
        }
        else
        {
            w = 7;
            h = 3;
        }

        int iy = Math.max(0, (h-3))/2;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        
        Graphics2D g = img.createGraphics();
        try
        {
            if (icon != null)
                icon.paintIcon(btn, g, 0, 0);
            Polygon a = new Polygon();
            a.addPoint(ix+3, iy+3); // mid, top
            a.addPoint(ix, iy);
            a.addPoint(ix+6, iy);
            
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(Color.BLACK);
            g.fill(a);
            
        }
        finally
        {
            g.dispose();
        }
        
        ImageIcon arrowIcon = new ImageIcon(img);
        btn.setIcon(arrowIcon);
        
    }

    private void install()
    {
        btn.addMouseListener(this);
        btn.addActionListener(this);
    }
    
    private Timer timer;

    private Timer getTimer()
    {
        if (timer == null)
        {
            timer = new Timer(400, this);
            timer.setRepeats(false);
        }
        return timer;
    }

    public void mousePressed(MouseEvent e)
    {
        getTimer().restart();
    }
    
    public void mouseReleased(MouseEvent e)
    {
        getTimer().stop();
    }

    public void popup()
    {
        popup.show(btn, 0, btn.getHeight());
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() instanceof Timer)
        {
            popup();
        }
        else
        {
            if (defaultAction != null)
                defaultAction.actionPerformed(e);
        }
    }
    
}
