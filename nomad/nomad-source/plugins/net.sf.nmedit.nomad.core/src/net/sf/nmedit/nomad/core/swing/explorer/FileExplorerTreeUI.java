package net.sf.nmedit.nomad.core.swing.explorer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.Timer;
import javax.swing.plaf.TreeUI;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import net.sf.nmedit.nmutils.dnd.FileDnd;
import net.sf.nmedit.nmutils.io.FileUtils;

public class FileExplorerTreeUI extends ExplorerTreeUI {
	public void installUI(JComponent c) {
		super.installUI(c);
		DND dnd = new DND();
		dnd.dragSource = DragSource.getDefaultDragSource();
		DragGestureRecognizer dgr = 
			dnd.dragSource.createDefaultDragGestureRecognizer(tree, 
					DnDConstants.ACTION_COPY_OR_MOVE, dnd);

		dnd.dropTarget = new DropTarget(tree, DnDConstants.ACTION_COPY_OR_MOVE
				| DnDConstants.ACTION_LINK,
				dnd);

	}

	public static final DataFlavor FileSelectionFlavor = new DataFlavor(FileSelectionTransferable.class, "Nomad FileSelectionFlavor");
	protected static DataFlavor uriFlavor =
		new DataFlavor("text/uri-list; charset=utf-8", "uri list");
	protected static DataFlavor patchFlavor = new DataFlavor("text/patch", "patch text file");

	protected class FileSelectionTransferable implements Transferable {

		TreePath[] getSelectedPaths() {
			return tree.getSelectionPaths();
		}

		Transferable[] getSelectedTransferables() {
			TreePath paths[] = tree.getSelectionPaths();
			ArrayList<Transferable> result = new ArrayList<Transferable>();
			for (TreePath path : paths) {
				Object node = path.getLastPathComponent();
				if (node instanceof Transferable)
					result.add((Transferable)node);
			}

			Transferable returnValue[] = new Transferable[result.size()];
			return result.toArray(returnValue);
		}

		FileNode[] getSelectedFileNodes() {
			TreePath paths[] = tree.getSelectionPaths();
			ArrayList<FileNode> result = new ArrayList<FileNode>();
			for (TreePath path : paths) {
				Object node = path.getLastPathComponent();
				if (node instanceof FileNode)
					result.add((FileNode)node);
			}

			FileNode returnValue[] = new FileNode[result.size()];
			return result.toArray(returnValue);
		}

		public Object getTransferData(DataFlavor flavor)
		throws UnsupportedFlavorException, IOException {
			if (FileSelectionFlavor.match(flavor)) {
				return this;
			} else if (uriFlavor.match(flavor)) {
				ByteArrayOutputStream os = new ByteArrayOutputStream();

				for (Transferable t : getSelectedTransferables()) {
					Object o = t.getTransferData(FileNode.fileFlavor);
					if (o instanceof File) {
						File f = (File)o;
						String path = f.getAbsoluteFile().toURI().toString() + "\n";
						os.write(path.getBytes("utf-8"));
					}
				}
				return new ByteArrayInputStream(os.toByteArray());
			} else {
				return null;
			}
		}

		public DataFlavor[] getTransferDataFlavors() {
			DataFlavor[] flavors = {FileSelectionFlavor, uriFlavor};
			return flavors;
		}

		public boolean isDataFlavorSupported(DataFlavor flavor) {
			for (DataFlavor f: getTransferDataFlavors())
				if (f.equals(flavor))
					return true;
			return false;
		}
	}

	private class DND implements 
	DragGestureListener,
	DragSourceListener,
	DropTargetListener
	{

		DragSource dragSource;
		DropTarget dropTarget;

		private Rectangle 	cueLine		= new Rectangle();
		private Color		colorCueLine;
		private Timer		timerHover;
		private TreePath		pathLast		= null;
		private TreePath pathLastExpanded = null;

		protected DND() {
			colorCueLine = new Color(
					SystemColor.controlShadow.getRed(),
					SystemColor.controlShadow.getGreen(),
					SystemColor.controlShadow.getBlue(),
					64
			);

			timerHover = new Timer(1000, new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					if (isRootPath(pathLast))
						return;	// Do nothing if we are hovering over the root node
					if (!tree.isExpanded(pathLast)) {
						tree.expandPath(pathLast);
						if (pathLastExpanded == null || !pathLast.isDescendant(pathLastExpanded)) {
							pathLastExpanded = pathLast;
						}
					}
				}
			});
			timerHover.setRepeats(false);	// Set timer to one-shot mode

		}

		public boolean isRootPath(TreePath path)
		{
			return isRootVisible() &&  tree.getRowForPath(path) == 0;
		}


		public void dragGestureRecognized(DragGestureEvent dge)
		{
			Point p = dge.getDragOrigin();
			TreePath path = getClosestPathForLocation(tree, p.x, p.y);
			if (path == null)
				return;
			if (!tree.isPathSelected(path) && (tree.getSelectionPaths() == null || (tree.getSelectionPaths().length == 0))) {
				tree.setSelectionPath(path);
			}

			Transferable transferable = new FileSelectionTransferable();

			dragSource.startDrag(dge, 
					DragSource.DefaultMoveNoDrop,
					transferable,
					this);
		}

		public void dragDropEnd(DragSourceDropEvent dsde)
		{
			// no op
		}

		public void dragEnter(DragSourceDragEvent dsde)
		{
			// no op
		}

		public void dragExit(DragSourceEvent dse)
		{
			// no op
		}

		public void dragOver(DragSourceDragEvent dsde)
		{
			// no op
		}

		public void dropActionChanged(DragSourceDragEvent dsde)
		{
			// no op
		}

		private boolean isDropOnExplorer(Point location) {
			
			TreePath path = tree.getPathForLocation(tree.getWidth()-1, location.y);
			return (path == null);
		}

		private boolean isDropFilesOnDir(Point location, Transferable t) {
			File f = getFileAtLocation(location);
			if ((f == null) || !f.isDirectory())
				return false;
			if (!FileDnd.testFileFlavor(t.getTransferDataFlavors()))
				return false;

			return true;
		}
		
		private boolean isDropNewFile(Point location, Transferable t) {
			File f = getFileAtLocation(location);
			if ((f == null) || !f.isDirectory())
				return false;
			
			if (FileDnd.isPatchStringFlavor(t.getTransferDataFlavors()))
				return true;
			
			return false;
		}

		private TreeNode getNodeAtLocation(Point location) {
			TreePath path = tree.getPathForLocation(location.x, location.y);
			if (path == null)
				return null;

			Object c = path.getLastPathComponent();
			if (c instanceof TreeNode)
				return (TreeNode)c;
			else
				return null;
		}

		private File getFileAtLocation(Point location) {
			TreeNode c = getNodeAtLocation(location);
			if (c instanceof FileNode) {
				// System.out.println("c " + c + " class " + c.getClass());
				FileNode node = (FileNode)c;
				return node.getFile();
			} else {
				return null;
			}
		}

		protected boolean updateDropAction(DropTargetDragEvent dtde) {
			if (isDropOnExplorer(dtde.getLocation())) 
				dtde.acceptDrag(DnDConstants.ACTION_LINK);
			else if (isDropFilesOnDir(dtde.getLocation(), dtde.getTransferable()))
				dtde.acceptDrag(dtde.getDropAction() & (DnDConstants.ACTION_MOVE | DnDConstants.ACTION_COPY));
			else if (isDropNewFile(dtde.getLocation(), dtde.getTransferable())) 
				dtde.acceptDrag(DnDConstants.ACTION_COPY);
			else {
				dtde.rejectDrag();
				return false;
			}

			return true;
		}

		protected void dumpTransferable(Transferable t) {
			/*System.out.println("dump");
			for (DataFlavor f : t.getTransferDataFlavors()) {
				System.out.println("flavor " + f.getMimeType() + f.getPrimaryType() + " " + f.getSubType());
			}*/
		}

		public void dragEnter(DropTargetDragEvent dtde)
		{
			updateDropAction(dtde);
		}

		public void dragExit(DropTargetEvent dte)
		{
			tree.repaint(cueLine.getBounds());
			timerHover.stop();
			if (pathLastExpanded != null) {
				// tree.collapsePath(pathLastExpanded);
				pathLastExpanded = null;
			}
			pathLast = null;
		}

		public void dropActionChanged(DropTargetDragEvent dtde)
		{
			updateDropAction(dtde);
		}



		public void dragOver(DropTargetDragEvent dtde)
		{
			Point location = dtde.getLocation();
			TreeUI treeUI = tree.getUI();
			if (!(treeUI instanceof ExplorerTreeUI)) return;
			ExplorerTreeUI etUI = (ExplorerTreeUI) treeUI;
			etUI.updateScrollPosition(location);

			TreePath path = tree.getPathForLocation(location.x, location.y);
			if (!updateDropAction(dtde)) {
				tree.paintImmediately(cueLine.getBounds());				
				timerHover.stop();
				return;
			} else {
				if (pathLastExpanded != null && pathLastExpanded != path && !pathLastExpanded.isDescendant(path)) {
					// tree.collapsePath(pathLastExpanded);
					// pathLastExpanded = null;
				}

				TreePath path2 = tree.getClosestPathForLocation(location.x, location.y);
				if (!(path2 == pathLast))			
				{
					pathLast = path2;
					timerHover.restart();
				}

				if (path != null) {
					Rectangle raPath = tree.getPathBounds(path);
					if (raPath != null) {
						// Cue line bounds (2 pixels beneath the drop target)
						Rectangle rect = new Rectangle(0,  raPath.y+(int)raPath.getHeight(), tree.getWidth(), 2);
						if (!cueLine.contains(rect)) {
							tree.paintImmediately(cueLine.getBounds());				
						}
						cueLine.setRect(rect);

						Graphics2D g2 = (Graphics2D) tree.getGraphics();
						g2.setColor(colorCueLine); // The cue line color
						g2.fill(cueLine);         // Draw the cue line
					}
				}
			}


		}

		public void drop(DropTargetDropEvent dtde)
		{
			tree.repaint(cueLine.getBounds());
			timerHover.stop();

			Point location = dtde.getLocation();

			if (isDropOnExplorer(location)) { 
				dtde.acceptDrop(DnDConstants.ACTION_LINK);
				createNewDirectoryLink(dtde);
			} else if (isDropFilesOnDir(location, dtde.getTransferable())) {
				// check copy, move
				if ((dtde.getDropAction() & DnDConstants.ACTION_MOVE) != 0) {
					dtde.acceptDrop(DnDConstants.ACTION_MOVE);
					dropFilesInDir(dtde, dtde.isLocalTransfer());
				} else if  ((dtde.getDropAction() & DnDConstants.ACTION_COPY) != 0) {
					dtde.acceptDrop(DnDConstants.ACTION_COPY);
					dropFilesInDir(dtde, false);
				}
				dtde.dropComplete(true);
				return;
			} else if (isDropNewFile(location, dtde.getTransferable())) {
				dtde.acceptDrop(DnDConstants.ACTION_COPY);
				dropNewFileInDir(dtde);
				dtde.dropComplete(true);
				return;
			} else {
				dtde.rejectDrop();
				return;
			}

		}
		
		private void dropNewFileInDir(DropTargetDropEvent dtde) {
			Transferable t = dtde.getTransferable();
			Point location = dtde.getLocation();

			File dest = getFileAtLocation(location);
			TreeNode c = getNodeAtLocation(location);
			TreePath destPath = getClosestPathForLocation(tree, location.x, location.y);

			FileNode parNode = null;
			if (c instanceof FileNode) {
				parNode = (FileNode)c;
			} else {
				return;
			}

			if ((dest == null) || !dest.isDirectory())
				return; // never too sure
			
			DataFlavor flavor = FileDnd.getPatchStringFlavor(t.getTransferDataFlavors());
			if (flavor == null)
				return;
			try {
				String data = FileDnd.getPatchString(t);
				File newFile = FileUtils.newFileWithPrefix(dest, "macro", ".pch");
				FileWriter out = new FileWriter(newFile);
				out.write(data);
				out.close();
				parNode.updateChildrenNodes(true);
				((ExplorerTree)tree).updateParentRootNodes(parNode);
				tree.expandPath(destPath);
				((ExplorerTree)tree).fireNodeStructureChanged(parNode);

				for (TreeNode child : parNode.getChildren()) {
					if (child instanceof FileNode && ((FileNode)child).getFile().getCanonicalPath().equals(newFile.getCanonicalPath())) {
		            	tree.startEditingAtPath(new TreePath(((FileNode)child).getPath()));
		            	break;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
 
		private void dropFilesInDir(DropTargetDropEvent dtde, boolean move) {
			DataFlavor flavor = FileDnd.getFileFlavor(dtde.getCurrentDataFlavors());
			if (flavor == null)
				return;
			Point location = dtde.getLocation();

			File dest = getFileAtLocation(location);
			if ((dest == null) || !dest.isDirectory())
				return; // never too sure
			TreePath destPath = getClosestPathForLocation(tree, location.x, location.y);
			TreeNode tdestNode = getNodeAtLocation(location);
			if (!(tdestNode instanceof FileNode))
				return;
			FileNode destNode = (FileNode)tdestNode;

			Transferable t = dtde.getTransferable();
			List<File> files = FileDnd.getTransferableFiles(flavor, t);
			FileFilter filter = destNode.getFileFilter();

			if (files == null)
				return;
			for (File f : files) {
				if (f.exists()) {
					try {
						if (move) {
							boolean success = f.renameTo(new File(dest, f.getName()));
						} else {
							// recursively copy directory with only filtered files
							FileUtils.copy(f, new File(dest, f.getName()), filter);

						}
					} catch (Throwable e) {
						// XXX catch errors here
						// System.out.println("rename " + f + " to " + new File(dest, f.getName()));
						e.printStackTrace();
					}
				}
			}

			try {
				if (t.isDataFlavorSupported(FileSelectionFlavor)) {
					Object o = t.getTransferData(FileSelectionFlavor);
					if (o instanceof FileSelectionTransferable) {
						for (FileNode node : ((FileSelectionTransferable)o).getSelectedFileNodes()) {
							TreeNode parent = node.getParent();

							if (parent instanceof FileNode) {
								FileNode parNode = (FileNode)parent;
								File f1 = parNode.getFile();
								if (parNode.updateChildrenNodes(true)) {
									((ExplorerTree)tree).updateParentRootNodes(parNode);
									((ExplorerTree)tree).fireNodeStructureChanged(parNode);
								}
							}
						}
					}
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
			((FileNode)destNode).updateChildrenNodes(true);
			tree.expandPath(destPath);
			((ExplorerTree)tree).updateParentRootNodes(destNode);
			((ExplorerTree)tree).fireNodeStructureChanged(destNode);
		}

		private void createNewDirectoryLink(DropTargetDropEvent dtde) {
			Transferable t = dtde.getTransferable();
			try {
				if (t.isDataFlavorSupported(FileSelectionFlavor)) {
					Object o = t.getTransferData(FileSelectionFlavor);
					if (o instanceof FileSelectionTransferable) {
						Transferable ts[] = ((FileSelectionTransferable)o).getSelectedTransferables();
						for (Transferable t2: ts) {
							Object o2 = t2.getTransferData(FileNode.fileNodeFlavor);
							FileNode node = (FileNode)o2;
							DataFlavor fileFlavor = FileDnd.getFileFlavor(t2.getTransferDataFlavors());
							List<File> files = FileDnd.getTransferableFiles(fileFlavor, t2);

							for (File file: files) {
								if (file.isDirectory()) {
									addNewRootEntry(file, node == null ? null : node.getFileFilter());
								}
							}
						}
					}
				} else {
					DataFlavor fileFlavor = FileDnd.getFileFlavor(t.getTransferDataFlavors());
					List<File> files = FileDnd.getTransferableFiles(fileFlavor, t);

					for (File file: files) {
						if (file.isDirectory()) {
							addNewRootEntry(file, null);
						}
					}
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}

		protected void addNewRootEntry(File file, FileFilter filter) {
			ExplorerTree eTree = (ExplorerTree)tree;
			FileContext fc = new FileContext(eTree, filter, file);
			eTree.addRootNode(fc);
			eTree.fireRootChanged();
		}
	}
}
