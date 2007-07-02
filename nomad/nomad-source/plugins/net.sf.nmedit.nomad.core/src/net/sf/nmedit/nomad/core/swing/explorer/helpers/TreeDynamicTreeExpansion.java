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

import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.TreePath;

import net.sf.nmedit.nomad.core.swing.explorer.ETreeNode;

public class TreeDynamicTreeExpansion implements TreeExpansionListener
{

    //private JTree tree;

    public TreeDynamicTreeExpansion( JTree tree )
    {
        //this.tree = tree;
    }

    public void treeExpanded( TreeExpansionEvent event )
    {
        TreePath path = event.getPath();
        expandData(path.getLastPathComponent());
    }

    public void treeCollapsed( TreeExpansionEvent event )
    {
        TreePath path = event.getPath();
        collapseData(path.getLastPathComponent());
    }

    private void expandData( Object en )
    {
        /*
        if (!en.getHasChildren()) 
        {
            en.setAllowsChildren(false);
            return;
        }
        for (Entry entry : en.getEntries())
        {
            en.add(entry);
        }
        en.setAllowsChildren(true);
        ((DefaultTreeModel)tree.getModel())
        .nodeStructureChanged(en);*/
    }

    private void collapseData( Object en )
    {
        if (en instanceof ETreeNode)
        {
            ((ETreeNode)en).notifyDropChildren();
        }
        /*
        if (en.getChildCount()>0)
        {
            en.removeAllChildren();
            ((DefaultTreeModel)tree.getModel())
            .nodeStructureChanged(en);
        }*/
    }

}
