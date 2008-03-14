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
 * Created on Nov 1, 2006
 */
package net.sf.nmedit.nomad.core.swing.explorer;

import java.awt.Event;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

import javax.swing.Icon;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

public class LeafNode implements ETreeNode
{

    private TreeNode parent;
    private Icon icon;
    private String text;
    private String toolTipText;

    public LeafNode(TreeNode parent)
    {
        this.parent = parent; 
    }

    public LeafNode(TreeNode parent, String text)
    {
        this(parent, null, text);   
    }

    public void processEvent(Event event)
    {
        if (parent!=null && parent instanceof ETreeNode)
            ((ETreeNode) parent).processEvent(event);
    }
    
    public LeafNode(TreeNode parent, Icon icon, String text)
    {
        this.parent = parent;
        this.icon = icon;
        this.text = text;
    }

    public void setText(String t)
    {
        this.text = t;
    }
    
    public String getText()
    {
        return this.text;
    }
    
    public Icon getIcon()
    {
        return icon;
    }

    public void notifyDropChildren()
    {
        // ignore
    }

    public TreeNode getChildAt( int childIndex )
    {
        throw new IndexOutOfBoundsException();
    }

    public int getChildCount()
    {
        return 0;
    }

    public TreeNode getParent()
    {
        return parent;
    }

    public int getIndex( TreeNode node )
    {
        return -1;
    }

    public boolean getAllowsChildren()
    {
        return false;
    }

    public boolean isLeaf()
    {
        return true;
    }

    public Enumeration children()
    {
        return DefaultMutableTreeNode.EMPTY_ENUMERATION;
    }

    public String toString()
    {
        return text;
    }

    public void setToolTipText(String toolTipText)
    {
        // TODO Auto-generated method stub
        this.toolTipText = toolTipText;
    }
    
    public String getToolTipText()
    {
        // TODO Auto-generated method stub
        return this.toolTipText==null?this.text:this.toolTipText;
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
