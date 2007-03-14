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
package net.sf.nmedit.nomad.core.swing.explorer.helpers;

import java.util.Enumeration;
import java.util.NoSuchElementException;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

public class TreeNodeEnumeration implements Enumeration<TreeNode>
{

    public static Enumeration<TreeNode> enumeration(TreeNode[] children)
    {
        if (children.length == 0)
            return DefaultMutableTreeNode.EMPTY_ENUMERATION;
        else
            return new TreeNodeEnumeration(children);
    }
    
    private int index = 0;
    private TreeNode[] children;

    public TreeNodeEnumeration(TreeNode[] children)
    {
        this.children = children;
    }
    
    public boolean hasMoreElements()
    {
        return index<children.length;
    }

    public TreeNode nextElement()
    {
        if (!hasMoreElements())
            throw new NoSuchElementException();
        
        return children[index++];
    }

}

