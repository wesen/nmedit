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

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

public class RootNode extends DefaultMutableTreeNode implements TreeNode
{

    /** array of children, may be null if this node has no children */
    protected Vector<TreeNode> children;

    public RootNode()
    {
        super();
    }

    public TreeNode getParent()
    {
        return null;
    }
    
    //
    //  Primitives
    //

    /**
     * Removes <code>newChild</code> from its present parent (if it has a
     * parent), sets the child's parent to this node, and then adds the child
     * to this node's child array at index <code>childIndex</code>.
     * <code>newChild</code> must not be null and must not be an ancestor of
     * this node.
     *
     * @param   newChild    the MutableTreeNode to insert under this node
     * @param   childIndex  the index in this node's child array
     *              where this node is to be inserted
     * @exception   ArrayIndexOutOfBoundsException  if
     *              <code>childIndex</code> is out of bounds
     * @exception   IllegalArgumentException    if
     *              <code>newChild</code> is null or is an
     *              ancestor of this node
     * @exception   IllegalStateException   if this node does not allow
     *                      children
     * @see #isNodeDescendant
     */
    public void insert(TreeNode newChild, int childIndex) {
    if (newChild == null) {
        throw new IllegalArgumentException("new child is null");
    } else if (isNodeAncestor(newChild)) {
        throw new IllegalArgumentException("new child is an ancestor");
    }
    else if (newChild.getParent()!=this) {
        if (newChild.getParent()==null) {
            throw new IllegalArgumentException("parent not set");
        }
        else
            throw new IllegalArgumentException("parent must be the root node");
    }
        if (children == null) {
        children = new Vector<TreeNode>();
        }
        children.insertElementAt(newChild, childIndex);
    }

    /**
     * Removes the child at the specified index from this node's children
     * and sets that node's parent to null. The child node to remove
     * must be a <code>MutableTreeNode</code>.
     *
     * @param   childIndex  the index in this node's child array
     *              of the child to remove
     * @exception   ArrayIndexOutOfBoundsException  if
     *              <code>childIndex</code> is out of bounds
     */
    public void remove(int childIndex) {
    	if (childIndex < 0) 
    		return;
        TreeNode child = getChildAt(childIndex);
        children.removeElementAt(childIndex);
        if (child instanceof MutableTreeNode)
            ((MutableTreeNode) child).setParent(null);
    }


    /**
     * Returns the child at the specified index in this node's child array.
     *
     * @param   index   an index into this node's child array
     * @exception   ArrayIndexOutOfBoundsException  if <code>index</code>
     *                      is out of bounds
     * @return  the TreeNode in this node's child array at  the specified index
     */
    public TreeNode getChildAt(int index) {
    if (children == null) {
        throw new ArrayIndexOutOfBoundsException("node has no children");
    }
    return (TreeNode)children.elementAt(index);
    }

    /**
     * Returns the number of children of this node.
     *
     * @return  an int giving the number of children of this node
     */
    public int getChildCount() {
    if (children == null) {
        return 0;
    } else {
        return children.size();
    }
    }

    /**
     * Returns the index of the specified child in this node's child array.
     * If the specified node is not a child of this node, returns
     * <code>-1</code>.  This method performs a linear search and is O(n)
     * where n is the number of children.
     *
     * @param   aChild  the TreeNode to search for among this node's children
     * @exception   IllegalArgumentException    if <code>aChild</code>
     *                          is null
     * @return  an int giving the index of the node in this node's child 
     *          array, or <code>-1</code> if the specified node is a not
     *          a child of this node
     */
    public int getIndex(TreeNode aChild) {
    if (aChild == null) {
        throw new IllegalArgumentException("argument is null");
    }

    if (!isNodeChild(aChild)) {
        return -1;
    }
    return children.indexOf(aChild);    // linear search
    }

    /**
     * Creates and returns a forward-order enumeration of this node's
     * children.  Modifying this node's child array invalidates any child
     * enumerations created before the modification.
     *
     * @return  an Enumeration of this node's children
     */
    public Enumeration<TreeNode> children() {
    if (children == null) {
        return DefaultMutableTreeNode.EMPTY_ENUMERATION;
    } else {
        return children.elements();
    }
    }

    public boolean getAllowsChildren() {
    return true;
    }

    //
    //  Derived methods
    //

    /**
     * Removes <code>aChild</code> from this node's child array, giving it a
     * null parent.
     *
     * @param   aChild  a child of this node to remove
     * @exception   IllegalArgumentException    if <code>aChild</code>
     *                  is null or is not a child of this node
     */
    public void remove(TreeNode aChild) {
    if (aChild == null) {
        throw new IllegalArgumentException("argument is null");
    }

    if (!isNodeChild(aChild)) {
        throw new IllegalArgumentException("argument is not a child");
    }
    remove(getIndex(aChild));   // linear search
    }

    /**
     * Removes all of this node's children, setting their parents to null.
     * If this node has no children, this method does nothing.
     */
    public void removeAllChildren() {
    for (int i = getChildCount()-1; i >= 0; i--) {
        remove(i);
    }
    }

    /**
     * Removes <code>newChild</code> from its parent and makes it a child of
     * this node by adding it to the end of this node's child array.
     *
     * @see     #insert
     * @param   newChild    node to add as a child of this node
     * @exception   IllegalArgumentException    if <code>newChild</code>
     *                      is null
     * @exception   IllegalStateException   if this node does not allow
     *                      children
     */
    public void add(TreeNode newChild) {
        insert(newChild, getChildCount());
    }

    public void add(TreeNode newChild, int i) {
        insert(newChild, i);
    }

    //
    //  Tree Queries
    //

    /**
     * Returns true if <code>anotherNode</code> is an ancestor of this node
     * -- if it is this node, this node's parent, or an ancestor of this
     * node's parent.  (Note that a node is considered an ancestor of itself.)
     * If <code>anotherNode</code> is null, this method returns false.  This
     * operation is at worst O(h) where h is the distance from the root to
     * this node.
     *
     * @see     #isNodeDescendant
     * @see     #getSharedAncestor
     * @param   anotherNode node to test as an ancestor of this node
     * @return  true if this node is a descendant of <code>anotherNode</code>
     */
    public boolean isNodeAncestor(TreeNode anotherNode) {
    if (anotherNode == null) {
        return false;
    }

    TreeNode ancestor = this;

    do {
        if (ancestor == anotherNode) {
        return true;
        }
    } while((ancestor = ancestor.getParent()) != null);

    return false;
    }

    //
    //  Child Queries
    //

    /**
     * Returns true if <code>aNode</code> is a child of this node.  If
     * <code>aNode</code> is null, this method returns false.
     *
     * @return  true if <code>aNode</code> is a child of this node; false if 
     *          <code>aNode</code> is null
     */
    public boolean isNodeChild(TreeNode aNode) {
    boolean retval;

    if (aNode == null) {
        retval = false;
    } else {
        if (getChildCount() == 0) {
        retval = false;
        } else {
        retval = (aNode.getParent() == this);
        }
    }

    return retval;
    }


    //
    //  Leaf Queries
    //

    /**
     * Returns true if this node has no children.  To distinguish between
     * nodes that have no children and nodes that <i>cannot</i> have
     * children (e.g. to distinguish files from empty directories), use this
     * method in conjunction with <code>getAllowsChildren</code>
     *
     * @see #getAllowsChildren
     * @return  true if this node has no children
     */
    public boolean isLeaf() {
    return (getChildCount() == 0);
    }

}
