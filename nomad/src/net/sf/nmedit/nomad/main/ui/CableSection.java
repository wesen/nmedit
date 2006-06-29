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
 * Created on Apr 29, 2006
 */
package net.sf.nmedit.nomad.main.ui;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JCheckBox;
import javax.swing.JComponent;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Signal;

public class CableSection extends HeaderSection
{

    public CableSection( String title )
    {
        super( title );

        JComponent pane =
        getContentPane();

        pane.add(create(Signal.AUDIO));
        pane.add(create(Signal.CONTROL));
        pane.add(create(Signal.LOGIC));
        pane.add(create(Signal.SLAVE));
        
        pane.add(create(Signal.USER1));
        pane.add(create(Signal.USER2));
        pane.add(create(Signal.NONE));
        
        /*
        pane.setLayout(new BoxLayout(pane, BoxLayout.LINE_AXIS));
        
        Box box = Box.createHorizontalBox();
        
        box.add(create(Signal.AUDIO));
        box.add(create(Signal.CONTROL));
        box.add(create(Signal.LOGIC));
        box.add(create(Signal.SLAVE));
        box.add(Box.createHorizontalGlue());
        pane.add(box);

        box = Box.createHorizontalBox();
        box.add(create(Signal.USER1));
        box.add(create(Signal.USER2));
        box.add(create(Signal.NONE));
        box.add(Box.createHorizontalGlue());
        pane.add(box);*/
    }

    private JComponent create (Signal s)
    {
        JCheckBox cbox = new ColorCheckBox() ;
        cbox.setBackground(s.getDefaultColor());
        cbox.setSelected(true);
        Dimension sz = new Dimension(14,14);

        cbox.setMinimumSize(sz);
        cbox.setMaximumSize(sz);
        cbox.setPreferredSize(sz);
        
       // btn.setBorder(BorderFactory.createLineBorder(Color.black));
        return cbox;
    }
    
    private class ColorCheckBox extends JCheckBox
    {
        protected void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            
            Graphics2D g2 = (Graphics2D) g;
            Composite prevComposite = g2.getComposite();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            g.setColor(getBackground());
            g.fillRect(0,0,getWidth(), getHeight());
            g2.setComposite(prevComposite);
            
        }
    }
    
}
