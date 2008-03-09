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
package net.sf.nmedit.nomad.core.swing;

import java.awt.Component;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.Border;

public class ButtonBarBuilder
{

    private JComponent container;
    
    public ButtonBarBuilder(JComponent container)
    {
        this.container = container;
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
        
        Border insetBorder = BorderFactory.createEmptyBorder(1, 2, 1, 2);
        Border border = container.getBorder();
        if (border != null)
            BorderFactory.createCompoundBorder(border, insetBorder);
        else
            border = insetBorder;
        container.setBorder(insetBorder);
    }
    
    public ButtonBarBuilder()
    {
        this(new JPanel());
    }
    
    public JComponent getContainer()
    {
        return container;
    }
    
    public void addSeparator()
    {
        container.add(new JSeparator(JSeparator.VERTICAL));
    }
    
    public void add(Component c)
    {
        container.add(c);
    }

    public void add(Action action)
    {
        addButton(action);
    }

    public void addButton(Action action)
    {
        JButton btn = new JButton(action);
        btn.putClientProperty("hideActionText", Boolean.TRUE); 
        add(btn);
    }

    public void addFlatButton(Action action)
    {
        JButton btn = new JButton(action);
        flat(btn);
        btn.putClientProperty("hideActionText", Boolean.TRUE);
        add(btn);
    }

    private void flat(JButton btn)
    {
       // btn.setContentAreaFilled(false);
       // btn.setBorderPainted(false);
    }

    public void addBox()
    {
        add(Box.createHorizontalGlue());
    }

    public void addSpace()
    {
        add(Box.createHorizontalStrut(2));
    }
    
}
