package net.sf.nmedit.nomad.core.swing.explorer;

import java.io.File;
import java.io.FileFilter;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
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
				File newFile =  new File(fNode.getFile().getParentFile(), 
						node.getUserObject().toString());
				File realNewFile = FileUtils.getNameWithExtension(fNode.getFile(), newFile);
				if (realNewFile.exists()) {
					startEditingAtPath(new TreePath(node.getPath()));
					return;
				} else if (fNode.getFile().renameTo(realNewFile)) {
					fNode.setFile(realNewFile);
				}
				fNode.updateChildrenNodes();
			}
			
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
