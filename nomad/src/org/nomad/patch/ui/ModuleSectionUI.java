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
 * Created on Feb 13, 2006
 */
package org.nomad.patch.ui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JDesktopPane;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

import org.nomad.main.ModuleGroupsMenu;
import org.nomad.main.ModuleToolbarButton;
import org.nomad.patch.Cables;
import org.nomad.patch.Connector;
import org.nomad.patch.Module;
import org.nomad.patch.ModuleSection;
import org.nomad.theme.curve.CCurve;
import org.nomad.theme.curve.CurvePanel;
import org.nomad.theme.curve.CurvePopupEvent;
import org.nomad.theme.curve.CurvePopupListener;
import org.nomad.util.iterate.ComponentIterator;
import org.nomad.xml.dom.module.DModule;

public class ModuleSectionUI extends JDesktopPane implements DropTargetListener, ModuleSectionListener {

	private ModuleSection moduleSection;

	public ModuleSection getModuleSection() {
		return moduleSection;
	}
	
//	private DropTarget dropTarget = null;
	private int dropAction = DnDConstants.ACTION_COPY;

	public static final DataFlavor ModuleSectionGUIFlavor = new DataFlavor("nomad/ModuleSectionGUIFlavor", "Nomad ModuleSectionGUI");

	private JPopupMenu popup = null;
	private CurvePanel curvePanel = null;


	public ModuleSectionUI(ModuleSection moduleSection) {
        this.moduleSection = moduleSection;
        
//      dropTarget = new DropTarget(this, dropAction, this, true);
        new DropTarget(this, dropAction, this, true);
        
        addMouseListener(new MouseAdapter() {
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
			}});

        
        curvePanel = new CurvePanel(this);
        curvePanel.setTable(moduleSection.getCables());
        curvePanel.addCurvePopupListener(new CurvePopupListener(){
			public void popup(CurvePopupEvent event) {
				JPopupMenu popup = new JPopupMenu();
				popup.add(new JDisconnectorMenuItem(getCurvePanel().getTransitions(), event.getConnector()));
				event.show(popup);
			}});
        
        /*
        addComponentListener(new ComponentAdapter() {

			public void componentResized(ComponentEvent event) {
				curvePanel.setSize(getSize());
			}}); */
        
        // +1 : cables are in front of dragged module, -1 : behind
        add(curvePanel, new Integer(JLayeredPane.DRAG_LAYER.intValue()-1));
        
        setPreferredSize(
        	new Dimension(
        		ModuleUI.Metrics.getPixelX(moduleSection.getMaxGridX()),
        		ModuleUI.Metrics.getPixelY(moduleSection.getMaxGridY())
        	)	
        );       
        setSize(getPreferredSize());
        
        moduleSection.addSectionListener(this);
    }

	public void moduleAdded(Module module) {
		add(module.newUI(this));
	}
	
	public void moduleRemoved(Module module) {
		remove(module.getUI());
	}


	public void unlink() {
		moduleSection.removeSectionListener(this);
		getCurvePanel().setTable(null);
		removeModuleDisplays();
	}
	
    private class JDisconnectorMenuItem extends JMenuItem {
    	private Connector connector;
    	private ArrayList<CCurve> cables = new ArrayList<CCurve>();
		private Cables transitions;

		public JDisconnectorMenuItem(Cables t, Connector c) {
			this.transitions = t;
    		this.connector = c;
    		setText("Disconnect");
    		loadCables(cables);
    		setEnabled(cables.size()>0);
    		addActionListener(new ActionListener() {
    			public void actionPerformed(ActionEvent event) {
					transitions.remove(cables);
    			}
    		});
    	}
		
		protected void loadCables(ArrayList<CCurve> cables) {
			cables.addAll(transitions.getDirectTransitions(connector));
		}
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
				DModule info = ((ModuleToolbarButton)data).getModuleDescription();
				Point p = dtde.getLocation();
		        Module mod = new Module(info);
		        mod.setLocation(ModuleUI.Metrics.getGridX(p.x),ModuleUI.Metrics.getGridY((p.y - ModuleUI.Metrics.HEIGHT)) );

		        moduleSection.add(mod);				
			}
			dtde.dropComplete(true);
		} else {
			dtde.rejectDrop();      
			dtde.dropComplete(false);
		}
	}

	public void dragExit(DropTargetEvent dte) {
	}

	/*public void rebuildUI() {
		removeModuleDisplays();
		populate();
	}*/
	
	void populate() {
		for (Module module : getModuleSection()) {
            //module = moduleSection.getModule(((Integer) e.nextElement()).intValue());
			
            add(module.newUI(this), JLayeredPane.DEFAULT_LAYER.intValue());
        }
		
		getCurvePanel().updateCurves();
	}
	
	void removeModuleDisplays() {
		for (ModuleUIIterator iter = new ModuleUIIterator(this); iter.hasNext();) {
			iter.next().unlink();
			iter.remove();
		}
	}

	public CurvePanel getCurvePanel() {
		return curvePanel;
	}

	private class ModuleUIIterator extends ComponentIterator<ModuleUI> {

		public ModuleUIIterator(Container container) {
			super(ModuleUI.class, container);
		}

	}
	
}
