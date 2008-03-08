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
package net.sf.nmedit.nomad.core.swing.explorer;

import java.awt.Event;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.tree.TreeNode;

public class ContainerNode implements ETreeNode
{
    
    private TreeNode parent;
    private String title;
    private Icon icon;
    
    private Vector<TreeNode> children;

    public ContainerNode(TreeNode parent, String title)
    {
        this.parent = parent;
        this.title = title;
        this.children = new Vector<TreeNode>();
    }
    
    public void addChild(TreeNode node)
    {
        children.add(node);
    }
    
    public void addChild(int index, TreeNode node)
    {
        children.add(index, node);
    }
    
    public void setTitle(String title)
    {
        this.title = title;
    }
    
    public String getTitle()
    {
        return title;
    }
    
    public void setIcon(Icon icon)
    {
        this.icon = icon;
    }

    public Icon getIcon()
    {
        return icon;
    }

    public void notifyDropChildren()
    {
        
    }

    public void processEvent(Event event)
    {
        if (parent!=null && parent instanceof ETreeNode)
            ((ETreeNode) parent).processEvent(event);
    }
    
    public Enumeration<TreeNode> children()
    { 
        return children.elements();
    }

    public boolean getAllowsChildren()
    {
        return children.size()>0;
    }

    public TreeNode getChildAt(int childIndex)
    {
        return children.get(childIndex);
    }

    public int getChildCount()
    {
        return children.size();
    }

    public int getIndex(TreeNode node)
    {   
        return children.indexOf(node);
    }

    public TreeNode getParent()
    {
        return parent;
    }

    public boolean isLeaf()
    {
        return children.isEmpty();
    }
    
    public String toString()
    {
        return getTitle();
    }

    public TreeNode remove(int index)
    {
        return children.remove(index);
    }
    
    public void remove(TreeNode node)
    {
        children.remove(node);
    }
    
    public void clear()
    {
        children.clear();
    }

    public boolean contains(Object node)
    {
        return children.contains(node);
    }

    public String getToolTipText()
    {
        return null;
    }

    public void processEvent(MouseEvent e)
    {
        // TODO Auto-generated method stub
        
    }

    public void actionCommandPerformed(ExplorerTree tree, String command)
    {
        // TODO Auto-generated method stub
        
    }

    public boolean isActionCommandPossible(ExplorerTree tree, String command)
    {
        // TODO Auto-generated method stub
        return false;
    }

}

