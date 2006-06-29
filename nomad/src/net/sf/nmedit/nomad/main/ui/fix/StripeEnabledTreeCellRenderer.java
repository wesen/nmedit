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

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

public class StripeEnabledTreeCellRenderer extends DefaultTreeCellRenderer
{

    public StripeEnabledTreeCellRenderer()
    {
        setBackgroundNonSelectionColor(null);
    }

    public Color getBackground()
    {
        // required 
        return null;
    }
    
    public Component getTreeCellRendererComponent( 
            JTree tree, 
            Object value, 
            boolean selected, 
            boolean expanded, boolean leaf, int row, boolean hasFocus )
    {
        JLabel component = (JLabel) super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

        component.setOpaque(false);
        component.setBackground(null);
        
        return component;
    }
}
