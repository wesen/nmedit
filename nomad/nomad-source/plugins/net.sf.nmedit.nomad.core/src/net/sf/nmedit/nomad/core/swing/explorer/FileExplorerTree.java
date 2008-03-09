package net.sf.nmedit.nomad.core.swing.explorer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import net.sf.nmedit.nmutils.io.FileUtils;
import net.sf.nmedit.nomad.core.menulayout.MenuBuilder;
import net.sf.nmedit.nomad.core.menulayout.MenuLayout;

public class FileExplorerTree extends ExplorerTree {
	/**
     * 
     */
    private static final long serialVersionUID = -1065473531878285689L;
    private boolean popupVisible = false;
    private FETPopupMenuListener popupMenuListener = new FETPopupMenuListener();
    
    public FileExplorerTree() {
    	super();
    	setEditable(true);
    	getModel().addTreeModelListener(new FileExplorerModelListener(this));
        enableEvents(MouseEvent.MOUSE_EVENT_MASK);
    }
    
    protected void processMouseEvent(MouseEvent e)
    {
        // update the cursor location
        switch (e.getID())
        {
            case MouseEvent.MOUSE_PRESSED:
            case MouseEvent.MOUSE_RELEASED:
            case MouseEvent.MOUSE_CLICKED:
                if (popupVisible)
                    updateSelectionAndPopupForLocation(e.getX(), e.getY());
                break;
            default:
                break;
        }        
        super.processMouseEvent(e);
    }
    
    private void updateSelectionAndPopupForLocation(int x, int y)
    {
        // select node at cursor
        int row = getClosestRowForLocation(x, y);
        if (!isRowSelected(row))
            setSelectionRow(row);
        updatePopupForSelection(row<0);
    }
    
    private void updatePopupForSelection(boolean setDisabled)
    {
        FileExplorerTree tree = FileExplorerTree.this;
        Object last = tree.getLastSelectedPathComponent();
        if (last != null && last instanceof ETreeNode && (!setDisabled))
        {
            ETreeNode node = (ETreeNode) last;
            
            enableAction(node, ACTION_REFRESH);     
            enableAction(node, ACTION_ENTRY_REMOVE);
            enableAction(node, ACTION_RENAME);
            enableAction(node, ACTION_DIR_NEWDIR);
            enableAction(node, ACTION_ITEM_DELETE);
            enableAction(node, ACTION_ITEM_OPEN);
            return;
        }
        else
        {
            disableAction(ACTION_REFRESH);     
            disableAction(ACTION_ENTRY_REMOVE);
            disableAction(ACTION_RENAME);
            disableAction(ACTION_DIR_NEWDIR);
            disableAction(ACTION_ITEM_DELETE);
            disableAction(ACTION_ITEM_OPEN);
        }
    }

    private MenuLayout menuLayout;

    public void createPopup(MenuBuilder menuBuilder)
    {
        // TODO this currently supports only a single FileExplorer
        // we have to clone the layout entry point
        MenuLayout l = menuBuilder.getLayout();
        this.menuLayout = l;
        JPopupMenu popup = menuBuilder.createPopup("nomad.fileexplorer");     
        createAction(l, ACTION_REFRESH);     
        createAction(l, ACTION_ENTRY_REMOVE);     
        createAction(l, ACTION_RENAME);     
        createAction(l, ACTION_DIR_NEWDIR);     
        createAction(l, ACTION_ITEM_DELETE);     
        createAction(l, ACTION_ITEM_OPEN);
        popup.addPopupMenuListener(popupMenuListener);
        setComponentPopupMenu(popup);
    }

    private void createAction(MenuLayout l, String entryPoint)
    {
        l.getEntry(entryPoint).addActionListener(new ETActionListener(entryPoint));
    }

    public static final String ACTION_REFRESH = 
        "nomad.fileexplorer.entry.refresh";
    public static final String ACTION_ENTRY_REMOVE = 
        "nomad.fileexplorer.entry.remove";
    public static final String ACTION_RENAME = 
        "nomad.fileexplorer.rename";
    public static final String ACTION_DIR_NEWDIR = 
        "nomad.fileexplorer.dir.newdir";
    public static final String ACTION_ITEM_DELETE = 
        "nomad.fileexplorer.item.delete";
    public static final String ACTION_ITEM_OPEN = 
        "nomad.fileexplorer.item.open";
    
    private class FETPopupMenuListener implements PopupMenuListener
    {

        public void popupMenuCanceled(PopupMenuEvent e)
        {
            popupVisible = false;
        }

        public void popupMenuWillBecomeInvisible(PopupMenuEvent e)
        {
            popupVisible = false;
        }

        public void popupMenuWillBecomeVisible(PopupMenuEvent e)
        {
            popupVisible = true;
            updatePopupForSelection(false);

        }

    }

    private void disableAction(String entryPoint)
    {
        menuLayout.getEntry(entryPoint).setEnabled(false);
    }

    private void enableAction(ETreeNode node, String entryPoint)
    {
        boolean enable = node.isActionCommandPossible(FileExplorerTree.this,
                entryPoint);
        menuLayout.getEntry(entryPoint).setEnabled(enable);
    }
    
    private class ETActionListener implements ActionListener
    {
        
        private String command;

        public ETActionListener(String command)
        {
            this.command = command;
        }

        public void actionPerformed(ActionEvent e)
        {
            FileExplorerTree tree = FileExplorerTree.this;
            Object last = tree.getLastSelectedPathComponent();
            if (last == null) return;
            if (last instanceof ETreeNode)
            {
                ((ETreeNode)last).actionCommandPerformed(FileExplorerTree.this,
                        command);
            }
        }
        
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
            Object node;
			node = (e.getTreePath().getLastPathComponent());

			/*
			 * If the event lists children, then the changed
			 * node is the child of the node we have already
			 * gotten.  Otherwise, the changed node and the
			 * specified node are the same.
			 */
			try {
				int index = e.getChildIndices()[0];
                
                node = ((FileNode)node).getChildAt(index);
			} catch (NullPointerException exc) {}

			if (node instanceof FileNode) {
				FileNode fNode = (FileNode)node;
				File oldFile = fNode.getFile();
				File newFile =  new File(fNode.getFile().getParentFile(),
                        fNode.getFile().getName());
				File realNewFile = FileUtils.getNameWithExtension(fNode.getFile(), newFile);
				if (realNewFile.exists()) {
					startEditingAtPath(new TreePath(fNode.getPath()));
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
                
                TreeNode parNode = fNode.getParent();
                /*if (parNode instanceof FileNode) {
                    FileNode fNode = (FileNode)parNode;
                    fNode.updateChildrenNodes();
                    ((ExplorerTree)tree).updateParentRootNodes(fNode);
                }
                */
                tree.fireNodeStructureChanged(parNode);
           }
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
