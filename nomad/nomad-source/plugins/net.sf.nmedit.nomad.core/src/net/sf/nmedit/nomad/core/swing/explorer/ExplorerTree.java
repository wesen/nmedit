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
 * Created on Oct 29, 2006
 */
package net.sf.nmedit.nomad.core.swing.explorer;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class ExplorerTree extends JTree
{
    
    public final static int ACTION_OPEN = 1;
    
    private RootNode root = new RootNode();
    
    public ExplorerTree()
    {
        setModel(new DefaultTreeModel(root, true));
        setUI(new ExplorerTreeUI());
    }

    public void addRootNode(TreeNode node)
    {
        getRoot().add(node);
        fireRootChanged();
    }
    
    public RootNode getRoot()
    {
        return root;
    }
    
    public DefaultTreeModel getModel()
    {
        return (DefaultTreeModel) super.getModel();
    }
    
   /* 
    public ExplorerTreeModel getModel()
    {
        return (ExplorerTreeModel) super.getModel();
    }
 */
    public void updateUI() 
    {
        // ignore - we use custom TreeUI
    }
    
    public void fireNodeStructureChanged(TreeNode node)
    {
        getModel().nodeStructureChanged(node);
    }
    
    public void fireNodeChanged(TreeNode node)
    {
        getModel().nodeChanged(node);
    }
    
    public void fireNodeStructureChanged(TreePath path)
    {
        fireNodeStructureChanged((TreeNode) path.getLastPathComponent());
    }
    
    public void fireNodeChanged(TreePath path)
    {
        fireNodeChanged((TreeNode) path.getLastPathComponent());
    }

    public void fireRootChanged()
    {
        fireNodeStructureChanged(getRoot());
    }
    
    
    
  /*
    public void addAsRoot(Entry entry)
    {
        addRootEntry(entry);
    }
    
    public void addRootEntry(Entry entry)
    {
        globalRoot.add(entry);
        ((DefaultTreeModel)getModel())
        .nodeStructureChanged(globalRoot);
    }


    public void updateRootNode( Treec entry )
    {
        ((DefaultTreeModel)getModel())
        .nodeStructureChanged(entry);
    }
    
    public void updatePath(TreePath path)
    {
        ((DefaultTreeModel)getModel())
        .nodeStructureChanged((TreeNode) path.getLastPathComponent());
    }
    */
    /*
    public void updateNode(TreePath path)
    {
        ((DefaultTreeModel)getModel())
        .nodeStructureChanged((TreeNode)path.getLastPathComponent());

        repaint();
    }*/
    
    /*
    public Entry getSelectedEntry()
    {
        TreePath path = getSelectionPath();
        if (path != null)
        {
            Entry en = (Entry) path.getLastPathComponent();
            if (en!=null)
            {
                return en.getUserObject();
            }
        }
        return null;
    }*/
    
    
}
