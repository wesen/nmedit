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
 * Created on Jun 27, 2006
 */
package net.sf.nmedit.nomad.main.ui.fix;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

public class StripeEnabledListCellRenderer extends DefaultListCellRenderer
{

    private TreeStripes stripes;

    public StripeEnabledListCellRenderer(TreeStripes stripes)
    {
        this.stripes = stripes;
    }

    public Component getListCellRendererComponent(
            JList list, 
            Object value,  
            int index, 
            boolean selected,
            boolean hasFocus )
    {
        JLabel component = (JLabel) super.getListCellRendererComponent(list, value, index, selected, hasFocus);

        if (!(selected))
        {
            component.setOpaque(true);
            component.setBackground(stripes.getColorAt(index));
        }
        
        return component;
    }
}
