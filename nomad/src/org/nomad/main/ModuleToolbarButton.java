package org.nomad.main;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
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

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.Border;

import net.sf.nmedit.nomad.core.nomad.NomadEnvironment;

import org.nomad.patch.ui.ModuleUI;
import org.nomad.theme.ModuleBuilder;
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

	private static final Border defaultBorder = new ToolbarButtonBorder(BorderFactory.createEtchedBorder(), 3);
	
	public ModuleToolbarButton(ModuleToolbar toolbar, DModule module) {
		super(new ImageIcon(module.getIcon()));
		this.toolbar = toolbar;
		this.module = module;
		//this.setToolTipText(module.getName() + " (" + MathRound.round(module.getCycles(), -2) + "%)");

		dragSource = DragSource.getDefaultDragSource();
		dragSource.createDefaultDragGestureRecognizer(this, dragAction, this);
		
		setBorder(defaultBorder);
		
		addMouseListener(this);
	}

	private static class ToolbarButtonBorder implements Border {

		Border border;
		int padding = 2;
		
		public ToolbarButtonBorder(Border border, int padding) {
			this.border = border;
			this.padding = padding;
		}
		
		public void paintBorder(Component component, Graphics g, int x, int y, int w, int h) {
			border.paintBorder(component, g, x, y, w, h);
		}

		public Insets getBorderInsets(Component c) {
			Insets i = border.getBorderInsets(c);
			i.set(i.left+padding, i.top+padding, i.right+padding, i.bottom+padding);
			return i;
		}

		public boolean isBorderOpaque() {
			// TODO Auto-generated method stub
			return border.isBorderOpaque();
		}
		
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
			ModuleBuilder builder = NomadEnvironment.sharedInstance().getBuilder();

			PermanentToolTip tip = new PermanentToolTip(this);
			if (builder != null) {
				ModuleUI gui = builder.compose(module, null);
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
