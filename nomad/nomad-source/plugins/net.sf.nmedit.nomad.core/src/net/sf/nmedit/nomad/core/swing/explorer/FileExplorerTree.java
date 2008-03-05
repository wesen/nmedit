package net.sf.nmedit.nomad.core.swing.explorer;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import net.sf.nmedit.nmutils.io.FileUtils;
import net.sf.nmedit.nomad.core.swing.ExtensionFilter;

public class FileExplorerTree extends ExplorerTree {
	/**
     * 
     */
    private static final long serialVersionUID = -1065473531878285689L;
    
    public FileExplorerTree() {
    	super();
    	setEditable(true);
    	getModel().addTreeModelListener(new FileExplorerModelListener(this));
    }

    public void installUI() {
    	setUI(new FileExplorerTreeUI());
	}
    
    class FileExplorerModelListener implements TreeModelListener {

    	protected FileExplorerTree tree;
		public FileExplorerModelListener(FileExplorerTree fileExplorerTree) {
			this.tree = fileExplorerTree;
		}

		public void treeNodesChanged(TreeModelEvent e) {
			DefaultMutableTreeNode node;
			node = (DefaultMutableTreeNode)(e.getTreePath().getLastPathComponent());

			/*
			 * If the event lists children, then the changed
			 * node is the child of the node we have already
			 * gotten.  Otherwise, the changed node and the
			 * specified node are the same.
			 */
			try {
				int index = e.getChildIndices()[0];
				node = (DefaultMutableTreeNode)
				(node.getChildAt(index));
			} catch (NullPointerException exc) {}

			if (node instanceof FileNode) {
				FileNode fNode = (FileNode)node;
				File oldFile = fNode.getFile();
				File newFile =  new File(fNode.getFile().getParentFile(), 
						node.getUserObject().toString());
				File realNewFile = FileUtils.getNameWithExtension(fNode.getFile(), newFile);
				if (realNewFile.exists()) {
					startEditingAtPath(new TreePath(node.getPath()));
					return;
				} else if (oldFile.renameTo(realNewFile)) {
					fNode.setFile(realNewFile);

					boolean rootChanged = false;
					for (FileNode rNode : getRootFileNodes()) {
						File rFile = rNode.getFile();
						if (FileUtils.isFileParent(oldFile, rFile)) {
							try {
								String oldPath = oldFile.getCanonicalPath();
								String newPath = newFile.getCanonicalPath();
								String renPath = rFile.getCanonicalPath();
								String newRenPath = newPath + renPath.substring(0, oldPath.length());
								rNode.setFile(new File(newRenPath));
								rNode.updateChildrenNodes();
								((ExplorerTree)tree).updateParentRootNodes(rNode);
								tree.fireNodeStructureChanged(rNode.getParent());
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							rootChanged = true;
						}
					}
					if (rootChanged)
						fireRootChanged();
				}
			}
			FileNode parNode = (FileNode)node.getParent();
			parNode.updateChildrenNodes();
			((ExplorerTree)tree).updateParentRootNodes(parNode);
			
			tree.fireNodeStructureChanged(node.getParent());
		}

		public void treeNodesInserted(TreeModelEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void treeNodesRemoved(TreeModelEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void treeStructureChanged(TreeModelEvent e) {
			// TODO Auto-generated method stub
			
		}
    	
    }
}
