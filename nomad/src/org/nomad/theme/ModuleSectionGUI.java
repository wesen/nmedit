package org.nomad.theme;

import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;

import javax.swing.JDesktopPane;

import org.nomad.main.ModuleToolbarButton;
import org.nomad.patch.ModuleSection;
import org.nomad.xml.dom.module.DModule;


public class ModuleSectionGUI extends JDesktopPane implements DropTargetListener
{
	ModuleSection moduleSection = null;
	
//	private DropTarget dropTarget = null;
	private int dropAction = DnDConstants.ACTION_COPY;

	public static final DataFlavor ModuleSectionGUIFlavor = new DataFlavor("nomad/ModuleSectionGUIFlavor", "Nomad ModuleSectionGUI");

    public ModuleSectionGUI(ModuleSection moduleSection) {
        super();
        this.moduleSection = moduleSection;
//      dropTarget = new DropTarget(this, dropAction, this, true);
        new DropTarget(this, dropAction, this, true);
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
}
