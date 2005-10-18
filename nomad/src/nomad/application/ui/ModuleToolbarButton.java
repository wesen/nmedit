package nomad.application.ui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.ImageIcon;

import nomad.model.descriptive.DModule;
import nomad.util.MathRound;

/**
 * @author Christian Schneider
 */
public class ModuleToolbarButton extends JButton implements DragGestureListener, DragSourceListener, Transferable {
	private DModule module;

	private DragSource dragSource = null;
	private int dragAction = DnDConstants.ACTION_COPY;
	
	public static final DataFlavor ModuleToolbarButtonFlavor = new DataFlavor("nomad/ModuleToolbarButtonFlavor", "Nomad ModuleToolbarButton");

	public ModuleToolbarButton(DModule module) {
		super(new ImageIcon(module.getIcon()));
		this.module = module;
		this.setToolTipText(module.getName() + " (" + MathRound.round(module.getCycles(), -2) + "%)");

		dragSource = DragSource.getDefaultDragSource();
		dragSource.createDefaultDragGestureRecognizer(this, dragAction, this);
	}

	public DModule getModuleDescription() {
		return module;
	}

	public void dragGestureRecognized(DragGestureEvent dge) {
        dge.startDrag(DragSource.DefaultCopyDrop, this, this);
	}

	public void dragEnter(DragSourceDragEvent dsde) {
	}

	public void dragOver(DragSourceDragEvent dsde) {
	}

	public void dropActionChanged(DragSourceDragEvent dsde) {
	}

	public void dragDropEnd(DragSourceDropEvent dsde) {
		doClick();
	}

	public void dragExit(DragSourceEvent dse) {
	}

	public DataFlavor[] getTransferDataFlavors() {
		DataFlavor[] flavors = {ModuleToolbarButtonFlavor};
		return flavors;
	}

	public boolean isDataFlavorSupported(DataFlavor flavor) {
		// DropTarget gives his flavors. DropSource looks if it can be dropped on the target.
		System.out.println("Plop");
		return true;
	}

	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
		return this;
	}

}
