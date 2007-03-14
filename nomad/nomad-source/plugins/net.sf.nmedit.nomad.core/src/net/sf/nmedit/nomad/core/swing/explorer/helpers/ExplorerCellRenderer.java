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
 * Created on Sep 12, 2006
 */
package net.sf.nmedit.nomad.core.swing.explorer.helpers;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import net.sf.nmedit.nomad.core.swing.explorer.ETreeNode;
import net.sf.nmedit.nomad.core.swing.explorer.ExplorerTreeUI;

public class ExplorerCellRenderer extends DefaultTreeCellRenderer
{

    public ExplorerCellRenderer()
    {
        setBackgroundSelectionColor(ExplorerTreeUI.defaultSelectionBackground);
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value,
                          boolean sel,
                          boolean expanded,
                          boolean leaf, int row,
                          boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, 
                expanded, leaf, row, false&hasFocus);
           if (value instanceof ETreeNode)
           {
               ETreeNode entry = (ETreeNode) value;
                if (entry != null)
                {
                    Icon icon = entry.getIcon();
                    if (icon != null)
                    {
                        setIcon(icon);
                    }
                }
           }
        //setText(entry == null ? null : entry.getName());
        return this;
    }

}
