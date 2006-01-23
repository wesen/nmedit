package org.nomad.theme;

import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Enumeration;

import javax.swing.JDesktopPane;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

import org.nomad.main.ModuleGroupsMenu;
import org.nomad.main.ModuleToolbarButton;
import org.nomad.patch.Module;
import org.nomad.patch.ModuleSection;
import org.nomad.xml.dom.module.DModule;


public class ModuleSectionGUI extends JDesktopPane implements DropTargetListener
{
	ModuleSection moduleSection = null;
	
//	private DropTarget dropTarget = null;
	private int dropAction = DnDConstants.ACTION_COPY;

	public static final DataFlavor ModuleSectionGUIFlavor = new DataFlavor("nomad/ModuleSectionGUIFlavor", "Nomad ModuleSectionGUI");

	private JPopupMenu popup = null;
	
    public ModuleSectionGUI(ModuleSection moduleSection) {
        super();
        this.moduleSection = moduleSection;
//      dropTarget = new DropTarget(this, dropAction, this, true);
        new DropTarget(this, dropAction, this, true);
        
        addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void mousePressed(MouseEvent event) {
				if (event.isPopupTrigger()) {
					JMenu moduleSubMenu = ModuleGroupsMenu.getMenu();
					JPopupMenu popup = new JPopupMenu();
					popup.add(new JMenuItem("Cut")).setEnabled(false);
					popup.add(new JMenuItem("Copy")).setEnabled(false);
					popup.add(new JMenuItem("Paste")).setEnabled(false);
					popup.add(new JSeparator());
					popup.add(moduleSubMenu);
					popup.show(event.getComponent(),event.getX(),event.getY());
				}
			}

			public void mouseReleased(MouseEvent event) {
				if (popup!=null) {
		            if (event.isPopupTrigger()) {
		            	popup.show(event.getComponent(), event.getX(), event.getY());
		            	popup = null;
		            }
				}
			}

			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}});
        
    }

	public void dragEnter(DropTargetDragEvent dtde) {
	}

	public void dragOver(DropTargetDragEvent dtde) {
		// We will only accept the ModuleToolbarButton.ModuleToolbarButtonFlavor
		if (dtde.getCurrentDataFlavorsAsList().contains(ModuleToolbarButton.ModuleToolbarButtonFlavor)) {
			dtde.acceptDrag(DnDConstants.ACTION_COPY);
		}
	}

	public void dropActionChanged(DropTargetDragEvent dtde) {
		// Depends on Ctrl or Shift
	}

	public void drop(DropTargetDropEvent dtde) {
		DataFlavor chosen = null;
		Object data = null;

		// We will only accept the ModuleToolbarButton.ModuleToolbarButtonFlavor
		if (dtde.isDataFlavorSupported(ModuleToolbarButton.ModuleToolbarButtonFlavor) 
				&& dtde.isLocalTransfer()) {
			
			// If there were more sourceFlavors, specify which one you like
			chosen = ModuleToolbarButton.ModuleToolbarButtonFlavor;
			
			try {
				// Get the data
				dtde.acceptDrop(dropAction);
				data = dtde.getTransferable().getTransferData(chosen);
		  	}
		  	catch (Throwable t) {
				t.printStackTrace();
				dtde.dropComplete(false);
				return;
		  	}
		  	
			if (data instanceof ModuleToolbarButton) {
				// Cast the data and create a nice module.
				DModule dModule = ((ModuleToolbarButton)data).getModuleDescription();
				moduleSection.addModuleAfterDrop(dModule, dtde.getLocation().x, dtde.getLocation().y);
			}
			dtde.dropComplete(true);
		} else {
			dtde.rejectDrop();      
			dtde.dropComplete(false);
		}
	}

	public void dragExit(DropTargetEvent dte) {
	}

	public void rebuildUI() {
		removeModuleDisplays();
		populate();
	}
	
	public void populate() {
		Module module = null;
        for (Enumeration e = moduleSection.getModules().keys(); e.hasMoreElements();) {
            module = moduleSection.getModule(((Integer) e.nextElement()).intValue());
            add(module.createModuleGUI(this), JLayeredPane.DEFAULT_LAYER.intValue());
        }

	}
	
	public void removeModuleDisplays() {

		while (getComponentCount()>0) {
			Component c = getComponent(0); remove(0);
			if (c instanceof ModuleGUI) {
				((ModuleGUI) c).unlink();
			}
		}

	}

	/*public void paint(Graphics g) {
		// 
	}*/
	
}
