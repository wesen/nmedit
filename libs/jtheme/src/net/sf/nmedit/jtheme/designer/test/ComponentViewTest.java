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
package net.sf.nmedit.jtheme.designer.test;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.sf.nmedit.jtheme.designer.ComponentView;

/**
 * Test for the class {@link ComponentView}.
 * 
 * @author Christian Schneider
 */
public class ComponentViewTest
{

    public static void main(String[] args)
    {
        JFrame f = new JFrame(ComponentViewTest.class.getName());
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(400, 300);
        Dimension s = Toolkit.getDefaultToolkit().getScreenSize();
        f.setLocation((s.width-f.getWidth())/2, (s.height-f.getHeight())/2);
        f.getContentPane().setLayout(new BorderLayout());
        
        
        JPanel view = new JPanel();
        view.setLayout(null);
        view.setSize(new Dimension(300, 200));
        {
            JLabel viewComponent = new JLabel("label 1");
            viewComponent.setLocation(10, 10);
            viewComponent.setSize(100, 20);
            view.add(viewComponent);
        }
        {
            JButton viewComponent = new JButton("click");
            viewComponent.setLocation(20, 40);
            viewComponent.setSize(100, 20);
            view.add(viewComponent);
        }
        ComponentView cv = new ComponentView();
        cv.setView(view);
        
        f.getContentPane().add(cv, BorderLayout.CENTER);
        f.setVisible(true);
        
    }
    
}
