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

import java.awt.Component;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.NoSuchElementException;

import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import net.sf.nmedit.nmutils.FileSort;
import net.sf.nmedit.nmutils.Platform;
import net.sf.nmedit.nmutils.io.FileUtils;
import net.sf.nmedit.nmutils.swing.WorkIndicator;
import net.sf.nmedit.nomad.core.Nomad;

public class FileNode implements ETreeNode, MouseListener,
    Transferable
{
    private final static FileNode[] EMPTY = new FileNode[0];
    private File file;
    private FileNode[] children = null;
    private TreeNode parent;
    private boolean nailed = false;
    
    public FileNode(TreeNode parent, File file)
    {
        this.parent = parent;
        this.file = file;
    }
    
    public File getFile()
    {
        return file;
    }

    public void setFile(File file)
    {
        this.file = file;
        updateChildrenNodes(true);
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

    public boolean updateChildrenNodes(boolean deep) {
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
    		
            if (deep)
            {
    		for (FileNode child : children) {
    			if (child.getChildCount() > 0) {
    				if (child.updateChildrenNodes(deep)) {
                        updated = true;
    				}
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
    
    private TreeNode[] getChildrenArray()
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
    
    public Collection<? extends TreeNode> getChildren() {
    	ArrayList<TreeNode> result  = new ArrayList<TreeNode>();
    	for (TreeNode child : getChildrenArray()) {
    		result.add(child);
    	}
    	return result;
    }
    
    public TreeNode getChildAt( int childIndex )
    {
        return getChildrenArray()[childIndex];
    }

    public int getChildCount()
    {
        return file.isFile() ? 0 : getChildrenArray().length; 
    }

    public TreeNode getParent()
    {
        return parent;
    }

    public int getIndex( TreeNode node )
    {
        TreeNode[] children = getChildrenArray();
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
            
            TreeNode[] items = getChildrenArray();
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
        return file.isFile() ? 
        		ExplorerTreeUI.DefaultFileIcon : null; 
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
        if (Platform.isLeftMouseButtonOnly(e) && e.getClickCount()==2)
        {
            justOpenIt(e.getComponent());
        }
    }
    
    private void justOpenIt(Component c)
    {
        Runnable run = new Runnable() 
        {
            public void run()
            {
                Nomad.sharedInstance().openOrSelect(file);
            }
        };
        run = WorkIndicator.create(c, run);
        SwingUtilities.invokeLater(run);
    }
    
    public void mousePressed(MouseEvent e)
    {
        ExplorerTree et = (ExplorerTree) e.getComponent();
        
        /*
        if (handlePopupTrigger(e))
            return;*/
        if (!Platform.isFlavor(Platform.OS.MacOSFlavor))
            openAction(e);
    }

    public void mouseReleased(MouseEvent e)
    {
        if (Platform.isFlavor(Platform.OS.MacOSFlavor))
            openAction(e);
        /*
        if (Platform.couldBePopupTrigger(e) && filePopup != null)
        {
            ((ExplorerTree)e.getComponent()).cancelEditing();
        	e.consume();
        }
        
        if (Platform.isFlavor(Platform.OS.MacOSFlavor) && Platform.couldBePopupTrigger(e))
        	e.consume();*/
    }

    public boolean isActionCommandPossible(ExplorerTree tree, String command)
    {
    	if (nailed && ((command == FileExplorerTree.ACTION_ENTRY_REMOVE) ||
    			(command == FileExplorerTree.ACTION_ITEM_DELETE) ||
    			(command == FileExplorerTree.ACTION_RENAME)))
    		return false;
    	
        if (this instanceof FileContext)
        {
            return command == FileExplorerTree.ACTION_DIR_NEWDIR
            || command == FileExplorerTree.ACTION_ITEM_DELETE
            || command == FileExplorerTree.ACTION_REFRESH
            || command == FileExplorerTree.ACTION_RENAME
            || command == FileExplorerTree.ACTION_ENTRY_REMOVE;   
        }
        if (file.isDirectory())
        {
            return command == FileExplorerTree.ACTION_DIR_NEWDIR
            || command == FileExplorerTree.ACTION_ITEM_DELETE
            || command == FileExplorerTree.ACTION_REFRESH
            || command == FileExplorerTree.ACTION_RENAME;
        }
        else if (file.isFile())
        {
            return command == FileExplorerTree.ACTION_ITEM_OPEN
            || command == FileExplorerTree.ACTION_ITEM_DELETE
            || command == FileExplorerTree.ACTION_RENAME;
        }
        return false;
    }
    
    public void actionCommandPerformed(ExplorerTree tree, String command)
    {
        ExplorerTree et = tree;
        FileNode node = this;
        if (command == FileExplorerTree.ACTION_ITEM_OPEN)
        {
            justOpenIt(tree);
        }
        else if (command == FileExplorerTree.ACTION_REFRESH) {
            if (node.updateChildrenNodes(true)) {
                et.fireNodeStructureChanged(node);
            }
        } else if (command == FileExplorerTree.ACTION_ITEM_DELETE) {
            Nomad n = Nomad.sharedInstance();
            File f = node.getFile();
            if (f.isFile()) {
                int result = JOptionPane.showConfirmDialog(n.getWindow().getRootPane(), 
                        "Are you sure you want to delete " + f.getName() + " ?", "", JOptionPane.OK_CANCEL_OPTION
                );
                if (result == JOptionPane.OK_OPTION) {
                    if (f.delete())
                    {
                        if (node.getParent() instanceof FileNode)
                            ((FileNode)node.getParent()).notifyChildFilesRemoved(et);
                    }
                }
            } else if (f.isDirectory())
            {
                int result = JOptionPane.showConfirmDialog(n.getWindow().getRootPane(), 
                        "Are you sure you want to delete " + f.getName() + " and all its contents ?", "", JOptionPane.OK_CANCEL_OPTION
                );
                if (result == JOptionPane.OK_OPTION) {
                    FileUtils.deleteDirectory(f);
                    boolean rootChanged = false;
                    for (FileNode rNode : et.getRootFileNodes()) {
                        File rFile = rNode.getFile();
                        if (FileUtils.isFileParent(f, rFile)) {
                            et.getRoot().remove(rNode);
                            rootChanged = true;
                        }
                    }
                    if (node.getParent() instanceof FileNode)
                        ((FileNode)node.getParent()).notifyChildFilesRemoved(et);
                    if (rootChanged)
                        et.fireRootChanged();
                }
            }
        }
        else if (command == FileExplorerTree.ACTION_ENTRY_REMOVE)
        {
            if (node.getParent() == et.getRoot())
            {
                Nomad n = Nomad.sharedInstance();
                File f = node.getFile();
                int result = JOptionPane.showConfirmDialog(n.getWindow().getRootPane(), 
                        "Are you sure you want to remove " + f.getName() + " from the tree ?", "", JOptionPane.OK_CANCEL_OPTION
                );
                if (result == JOptionPane.OK_OPTION) {
                    et.getRoot().remove(node);
                    et.fireRootChanged();
                }
            }
        } else if (command == FileExplorerTree.ACTION_RENAME) {
            et.startEditingAtPath(new TreePath(node.getPath()));
        } else if (command == FileExplorerTree.ACTION_DIR_NEWDIR) {
            File f = node.getFile();
            try {
                File newDir = FileUtils.newFileWithPrefix(f, "dir", "");
                newDir.mkdir();
                node.updateChildrenNodes(true);
                ((ExplorerTree)et).updateParentRootNodes(node);
                et.expandPath(new TreePath(node.getPath()));
                ((ExplorerTree)et).fireNodeStructureChanged(node);

                for (TreeNode child : node.getChildren()) {
                    if (child instanceof FileNode && ((FileNode)child).getFile().getCanonicalPath().equals(newDir.getCanonicalPath())) {
                        et.startEditingAtPath(new TreePath(((FileNode)child).getPath()));
                        break;
                    }
                }

            } catch (IOException e1) {
                // no op
                e1.printStackTrace();
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
        	String path;
        	if (file == null) {
        		path = null;
        	} else {
        		path = file.getAbsoluteFile().toURI().toString();
        	}
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

	public void setNailed(boolean nailed) {
		this.nailed = nailed;
	}

	public boolean isNailed() {
		return nailed;
	}
	
	public boolean renameTo(ExplorerTree etree, File realNewFile) {
		File oldFile = getFile();
		if (oldFile.renameTo(realNewFile)) {
			setFile(realNewFile);
			
			boolean rootChanged = false;
			for (FileNode rNode : etree.getRootFileNodes()) {
				File rFile = rNode.getFile();
				if (FileUtils.isFileParent(oldFile, rFile)) {
					try {
						String oldPath = oldFile.getCanonicalPath();
						String newPath = realNewFile.getCanonicalPath();
						String renPath = rFile.getCanonicalPath();
						String newRenPath = newPath + renPath.substring(oldPath.length());
						rNode.setFile(new File(newRenPath));
						etree.updateParentRootNodes(rNode);
						etree.fireNodeStructureChanged(rNode.getParent());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					rootChanged = true;
				} else if (FileUtils.isFileParent(rFile, oldFile)) {
					rNode.updateChildrenNodes(true);
				}
			}
			if (rootChanged)
				etree.fireRootChanged();
        }

		return true;
	}

}
