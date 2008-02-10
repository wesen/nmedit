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
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.NoSuchElementException;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import net.sf.nmedit.nmutils.FileSort;
import net.sf.nmedit.nmutils.Platform;
import net.sf.nmedit.nmutils.io.FileUtils;
import net.sf.nmedit.nomad.core.Nomad;

public class FileNode implements ETreeNode, MouseListener,
    Transferable
{

    private final static FileNode[] EMPTY = new FileNode[0];
    private File file;
    private FileNode[] children = null;
    private TreeNode parent;
	private JPopupMenu filePopup;
    
    public FileNode(TreeNode parent, File file)
    {
        this.parent = parent;
        this.file = file;
    }
    
    public File getFile()
    {
        return file;
    }

    protected TreeNode[] getPathToRoot(TreeNode aNode, int depth) {
    	TreeNode[]              retNodes;

    	/* Check for null, in case someone passed in a null node, or
    	   they passed in an element that isn't rooted at root. */
    	if(aNode == null) {
    		if(depth == 0)
    			return null;
    		else
    			retNodes = new TreeNode[depth];
    	}
    	else {
    		depth++;
    		retNodes = getPathToRoot(aNode.getParent(), depth);
    		retNodes[retNodes.length - depth] = aNode;
    	}
    	return retNodes;
    }
    
    public TreeNode[] getPath() {
    	return getPathToRoot(this, 0);
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

    public boolean updateChildrenNodes() {
    	boolean updated = false;
    	
    	if (children != null) {
    		File files[] = getChildrenFiles();
    		Hashtable<String, File> fileNames = new Hashtable<String, File>();
    		Hashtable<String, FileNode> childrenNames = new Hashtable<String, FileNode>();
    		ArrayList<FileNode> newChildren = new ArrayList<FileNode>();

    		for (File fn : files) {
    			try {
    				fileNames.put(fn.getCanonicalPath(), fn);
    			} catch (Throwable e) {
    				updated = true;
    			}
    		}

    		for (FileNode child : children) {
                try {
                	String name = child.getFile().getCanonicalPath();
            		childrenNames.put(name, child);
                	if (fileNames.containsKey(name)) {
                		newChildren.add(child);
                	} else {
                		updated = true;
                	}
    			} catch (Throwable e) {
    				updated = true;
    			}
    		}
    		
    		for (File f: files) {
    			try {
    				String name = f.getCanonicalPath();
    				
    				if (!childrenNames.containsKey(name)) {
    					newChildren.add(new FileNode(this, f));
        				updated = true;
    				}
    			} catch (Throwable e) {
    				updated = true;
    			}
    		}

    		if (updated) {
    			Collections.sort(newChildren, new Comparator<FileNode>() {
    				public int compare(FileNode o1, FileNode o2) {
    					return o1.getFile().getName().compareTo(o2.getFile().getName());
    				}
    			});

    			children = new FileNode[newChildren.size()];
    			int i = 0;
    			for (FileNode child : newChildren) {
    				children[i++] = child;
    			}
    		}
    		
    		for (FileNode child : children) {
    			if (child.getChildCount() > 0) {
    				if (child.updateChildrenNodes()) {
    					updated = true;
    				}
    			}
    		}
    	}

    	return updated;
    }
    
    public void notifyDropChildren()
    {
        children = null;
    }
    
    private void updateChildrenArray(int removed) {
        FileNode[] a = new FileNode[children.length-removed];
        int ai = 0;
        for (int i=0;i<children.length;i++)
            if (children[i] != null)
                a[ai++] = children[i];
        children = a;
    }
    
    public void notifyChildFilesRemoved(ExplorerTree et)
    {
        if (children != null)
        {
            int removed = 0;
            for (int i=children.length-1;i>=0;i--)
            {
                FileNode fn = children[i];
                if (!fn.getFile().exists())
                {
                    children[i] = null;
                    removed ++;
                }
            }
            if (removed>0)
            {
            	updateChildrenArray(removed);
                et.fireNodeStructureChanged(this);
            }
        }
    }

    private File[] getChildrenFiles() {
        FileFilter filter = getFileFilter();
        File[] files = filter == null ? this.file.listFiles() : this.file.listFiles(filter);
        return files;
    }
    
    private TreeNode[] getChildren()
    {
        if (children == null)
        {
        	File[] files = getChildrenFiles();
            if (files != null && files.length>0)
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
    	return getChildCount() == 0;
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
        if (handlePopupTrigger(e))
            return;
        if (!Platform.isFlavor(Platform.OS.MacOSFlavor))
            openAction(e);
    }

    public void mouseReleased(MouseEvent e)
    {
        if (handlePopupTrigger(e))
            return;
        if (Platform.isFlavor(Platform.OS.MacOSFlavor))
            openAction(e);
    }

    private boolean handlePopupTrigger(MouseEvent e)
    {
        if ((e.getComponent() instanceof ExplorerTree))
        {
            ExplorerTree et = (ExplorerTree) e.getComponent();
            
            if (et.isPopupTrigger(e, this, true))
            {
            	if (filePopup != null && filePopup.getInvoker()== e.getComponent()) 
            	{
            	    // close popup
            		filePopup.setVisible(false);
            		filePopup = null;
            		return true;
            	} else { 
            	    createPopup(e, et);
                	return true;
            	}
            }
        }
        return false;
    }
    
    protected void createPopup(MouseEvent e, ExplorerTree et)
    {
        filePopup = new JPopupMenu();
        populatePopup(filePopup, e, et);
        filePopup.show(et, e.getX(), e.getY());
    }
    
    protected void populatePopup(JPopupMenu popup, MouseEvent e, ExplorerTree et)
    {
        if (file.isDirectory())
        {
            popup.add(new FileNodeAction(et, FileNodeAction.REFRESH));
            popup.add(new FileNodeAction(et, FileNodeAction.DELETE_PERMANENTLY));
        }
        else
        {
            popup.add(new FileNodeAction(et, FileNodeAction.OPEN));
            popup.add(new FileNodeAction(et, FileNodeAction.DELETE_PERMANENTLY));
        }
        
        if (getParent() == et.getRoot())
        {
            popup.addSeparator();
            popup.add(new FileNodeAction(et, FileNodeAction.REMOVE_EXPLORER_ENTRY));
        }
    }
    
    private class FileNodeAction extends AbstractAction
    {

        public static final String OPEN = "Open";
        public static final String REFRESH = "Refresh";
        public static final String DELETE_PERMANENTLY = "Delete (Permanently)";
        public static final String REMOVE_EXPLORER_ENTRY = "Remove Entry";

        private ExplorerTree et;
        
        public FileNodeAction(ExplorerTree et, String command){
            this.et = et;
            putValue(ACTION_COMMAND_KEY, command); 
            putValue(NAME, command); 
            if (command == OPEN)
                setEnabled(false); // not implemented yet
        }
        
        public void actionPerformed(ActionEvent e)
        {
            if (e.getActionCommand() == REFRESH)
            {
            	if (updateChildrenNodes()) {
                	et.fireNodeStructureChanged(FileNode.this);
                }
            }
            else if (e.getActionCommand() == DELETE_PERMANENTLY && getFile().isFile())
            {
                if (getFile().delete())
                {
                    if (FileNode.this.getParent() instanceof FileNode)
                        ((FileNode)FileNode.this.getParent()).notifyChildFilesRemoved(et);
                }
            }
            else if (e.getActionCommand() == DELETE_PERMANENTLY && getFile().isDirectory())
            {
            	FileUtils.deleteDirectory(getFile());
                if (FileNode.this.getParent() instanceof FileNode)
                    ((FileNode)FileNode.this.getParent()).notifyChildFilesRemoved(et);
            }
            else if (e.getActionCommand() == REMOVE_EXPLORER_ENTRY)
            {
                if (getParent() == et.getRoot())
                {
                    et.getRoot().remove(FileNode.this);
                    et.fireRootChanged();
                }
                
            }
        }
        
    }
    
    protected static DataFlavor fileFlavor = new DataFlavor(File.class, "File");
    protected static DataFlavor fileNodeFlavor = new DataFlavor(FileNode.class, "FileNode");
    protected static DataFlavor uriFlavor =
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
        
        if (fileNodeFlavor.match(flavor))
        	return this;
        throw new UnsupportedFlavorException(flavor); 
    }

    public DataFlavor[] getTransferDataFlavors()
    {
        DataFlavor[] flavors = {fileFlavor, uriFlavor, fileNodeFlavor};
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
