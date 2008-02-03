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

import java.awt.Event;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.NoSuchElementException;

import javax.swing.Icon;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import net.sf.nmedit.nmutils.FileSort;
import net.sf.nmedit.nmutils.Platform;
import net.sf.nmedit.nomad.core.Nomad;

public class FileNode implements ETreeNode, MouseListener,
    Transferable
{

    private final static TreeNode[] EMPTY = new TreeNode[0];
    private File file;
    private TreeNode[] children = null;
    private TreeNode parent;
    
    public FileNode(TreeNode parent, File file)
    {
        this.parent = parent;
        this.file = file;
    }
    
    public File getFile()
    {
        return file;
    }

    public void processEvent(Event event)
    {
        if (parent!=null && parent instanceof ETreeNode)
            ((ETreeNode) parent).processEvent(event);
    }
    
    public FileFilter getFileFilter()
    {
        if (parent != null &&  parent instanceof FileNode)
        {
            return ((FileNode) parent).getFileFilter();
        }
        else
        {
            return null;
        }
    }

    public void notifyDropChildren()
    {
        children = null;
    }
    
    private TreeNode[] getChildren()
    {
        if (children == null)
        {
            FileFilter filter = getFileFilter();
            File[] files = filter == null ? this.file.listFiles() : this.file.listFiles(filter);
            if (files.length>0)
            {
                FileSort.sortDirectoriesFiles(files);
                children = new FileNode[files.length];
                for (int i=files.length-1;i>=0;i--)
                    children[i] = new FileNode(this, files[i]);
            }
            else
            {
                children = EMPTY;
            }
        }
        return children;
    }

    public TreeNode getChildAt( int childIndex )
    {
        return getChildren()[childIndex];
    }

    public int getChildCount()
    {
        return file.isFile() ? 0 : getChildren().length; 
    }

    public TreeNode getParent()
    {
        return parent;
    }

    public int getIndex( TreeNode node )
    {
        TreeNode[] children = getChildren();
        for (int i=children.length-1;i>=0;i--)
            if (children[i]==node)
                return i;
        return -1;
    }

    public boolean getAllowsChildren()
    {
        return file.isDirectory();
    }

    public boolean isLeaf()
    {
        return getChildCount()>0;
    }

    public Enumeration children()
    {
        
        if (getChildCount()==0)
            return DefaultMutableTreeNode.EMPTY_ENUMERATION;
        else
            return new Enumeration()
        {
            
            TreeNode[] items = getChildren();
            int index = 0;

            public boolean hasMoreElements()
            {
                return index<items.length;
            }

            public Object nextElement()
            {
                if (!hasMoreElements())
                    throw new NoSuchElementException();
                return items[index++];
            }
            
        };
    }

    public String toString()
    {
        return file.getName();
    }

    public Icon getIcon()
    {
        return file.isFile() ? ExplorerTreeUI.DefaultFileIcon : ExplorerTreeUI.DefaultFolderIcon; 
    }

    public String getToolTipText()
    {
        File f = getFile();
        return f.getAbsolutePath();
    }
    
    public void processEvent(MouseEvent e)
    {
        EventDispatcher.dispatchEvent(this, e);
    }

    public void mouseClicked(MouseEvent e)
    {
        // no op
    }

    public void mouseEntered(MouseEvent e)
    {
        // no op
    }

    public void mouseExited(MouseEvent e)
    {
        // no op
    }

    private void openAction(MouseEvent e)
    {     
        if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount()==2)
        {
            Nomad.sharedInstance().openOrSelect(file);
        }
    }
    
    public void mousePressed(MouseEvent e)
    {
        if (!Platform.isFlavor(Platform.OS.MacOSFlavor))
            openAction(e);
    }

    public void mouseReleased(MouseEvent e)
    {
        if (Platform.isFlavor(Platform.OS.MacOSFlavor))
            openAction(e);
    }

    private static DataFlavor fileFlavor = new DataFlavor(File.class, "File");
    private static DataFlavor uriFlavor =
        new DataFlavor("text/uri-list; charset=utf-16", "uri list");
 
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
    {
        if (fileFlavor.match(flavor))
            return file;

        if (uriFlavor.match(flavor))
        {
            String path = file == null ? null : 
                file.getAbsoluteFile().toURI().toString();
            return new ByteArrayInputStream(path.getBytes("utf-16"));
        }
        throw new UnsupportedFlavorException(flavor); 
    }

    public DataFlavor[] getTransferDataFlavors()
    {
        DataFlavor[] flavors = {fileFlavor, uriFlavor};
        return flavors;
    }

    public boolean isDataFlavorSupported(DataFlavor flavor)
    {
        for (DataFlavor f: getTransferDataFlavors())
            if (f.equals(flavor))
                return true;
        return false;
    }

}
