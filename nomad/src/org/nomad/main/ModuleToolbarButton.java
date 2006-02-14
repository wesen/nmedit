package org.nomad.main;

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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import org.nomad.env.Environment;
import org.nomad.patch.ui.ModuleUI;
import org.nomad.theme.ModuleGUIBuilder;
import org.nomad.util.misc.MathRound;
import org.nomad.util.misc.PermanentToolTip;
import org.nomad.xml.dom.module.DModule;


/**
 * @author Christian Schneider
 */
public class ModuleToolbarButton extends JButton implements MouseListener, DragGestureListener, DragSourceListener, Transferable {
	private DModule module;

	private DragSource dragSource = null;
	private int dragAction = DnDConstants.ACTION_COPY;
	private ModuleToolbar toolbar = null;
	
	public static final DataFlavor ModuleToolbarButtonFlavor = new DataFlavor("nomad/ModuleToolbarButtonFlavor", "Nomad ModuleToolbarButton");

	public ModuleToolbarButton(ModuleToolbar toolbar, DModule module) {
		super(new ImageIcon(module.getIcon()));
		this.toolbar = toolbar;
		this.module = module;
		//this.setToolTipText(module.getName() + " (" + MathRound.round(module.getCycles(), -2) + "%)");

		dragSource = DragSource.getDefaultDragSource();
		dragSource.createDefaultDragGestureRecognizer(this, dragAction, this);
		
		addMouseListener(this);
	}

	public ModuleToolbar getToolbar() {
		return toolbar;
	}
	
	public DModule getModuleDescription() {
		return module;
	}
	
	public void dragGestureRecognized(DragGestureEvent dge) {
		if (toolbar.hasDraggingSupport())
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

	public void mouseClicked(MouseEvent event) { }
	public void mousePressed(MouseEvent event) {
		PermanentToolTip.removeTip();
	}
	public void mouseReleased(MouseEvent event) { }

	public void mouseEntered(MouseEvent event) {
		try {
			ModuleGUIBuilder builder = Environment.sharedInstance().getBuilder();

			PermanentToolTip tip = new PermanentToolTip(this);
			if (builder != null) {
				ModuleUI gui = builder.createGUI(module, null);
				if (gui!=null) {
					gui.setMinimumSize(gui.getSize());
					gui.setMaximumSize(gui.getSize());
					gui.setPreferredSize(gui.getSize());
					tip.addOneLine(gui);
				}
			}
			tip.addProperty("Name:", module.getName());
			tip.addProperty("Category:", module.getParent().getParent().getName());
			tip.addProperty("Cycles:", MathRound.round(module.getCycles(), -2) + "%");
			tip.showTip(500);
		} catch (NullPointerException e) {
			// TODO remove this catch block - workaround : exception is caused if a new theme/patch is loaded 
		}
	}

	public void mouseExited(MouseEvent event) {
		PermanentToolTip.removeTip();
	}

}
