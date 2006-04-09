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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;

import org.nomad.main.ModuleGroupsMenu;
import org.nomad.main.ModuleToolbarButton;
import org.nomad.patch.Module;
import org.nomad.patch.ModuleSection;
import org.nomad.util.iterate.ComponentIterator;
import org.nomad.xml.dom.module.DModule;

public class ModuleSectionUI extends JComponent implements ModuleSectionListener {

	private ModuleSection moduleSection;
	
	private Component overlay = null;

	public ModuleSection getModuleSection() {
		return moduleSection;
	}
	
	private final static int dropAction = DnDConstants.ACTION_COPY;

	public static final DataFlavor ModuleSectionGUIFlavor = new DataFlavor("nomad/ModuleSectionGUIFlavor", "Nomad ModuleSectionGUI");

	private JPopupMenu popup = null;
	private CableDisplay curvePanel = null;

	private DragDropAction ddAction = new DragDropAction();
	
	private PatchUI patchui = null;

    /*public void update(Graphics g) {
        paint(g);
    }*/
    
	public ModuleSectionUI(ModuleSection moduleSection) {
        curvePanel = new CableDisplay(this);
        this.moduleSection = moduleSection;
        setOpaque(true);
        setDoubleBuffered(false);
        new DropTarget(this, dropAction, ddAction, true);
        
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

        
        /*
        curvePanel.addCurvePopupListener(new CurvePopupListener(){
			public void popup(CurvePopupEvent event) {
				JPopupMenu popup = new JPopupMenu();
				popup.add(new JDisconnectorMenuItem(getCurvePanel().getTransitions(), event.getConnector()));
				event.show(popup);
			}});*/
        
        moduleSectionResized();
        moduleSection.addSectionListener(this);
    }

	public void locationSet(ModuleUI moduleUI) {
		curvePanel.updateCableLocations(moduleUI);
	}

    public void triggerRepaint( int x, int y, int w, int h )
    {
        RepaintManager.currentManager(this).addDirtyRegion(this,x, y, w, h);  
    }
	
	public void setPatchUI(PatchUI patchui) {
		this.patchui = patchui;
	}
	
	public PatchUI getPatchUI() {
		return patchui;
	}

	public void moduleAdded(Module module) {
		Component c;
		add(c=module.newUI(this));
		Rectangle r = c.getBounds();
		RepaintManager.currentManager(this).addDirtyRegion(this, r.x, r.y, r.width, r.height);
	}
	
	public void moduleRemoved(Module module) {
		ModuleUI m = module.getUI();
		Rectangle bounds = m.getBounds(); 
		remove(module.getUI());
		if (isDisplayable())
			RepaintManager.currentManager(this).addDirtyRegion(this,
				bounds.x, bounds.y, bounds.width, bounds.height
			);
	}

    public void rearangingModules(boolean finished) {
        /*
        getCurvePanel().beginUpdate();
        getCurvePanel().endUpdate();*/
        
        if (!finished)
            getCableDisplay().beginUpdate();
        else SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                getCableDisplay().endUpdate();
                
            }});
    }
	
	private boolean adjustSize = false;

	private boolean cablesVisible = true;

	public void updateSize() {
        Dimension d = new Dimension( ModuleUI.Metrics.getPixelX(moduleSection.getMaxGridX()),
            	ModuleUI.Metrics.getPixelY(moduleSection.getMaxGridY()) );
        setPreferredSize(d);       
        setSize(d);
	}
	
	public void moduleSectionResized() {
		if (adjustSize) {
			updateSize();
		}
	}

	public void unlink() {
		moduleSection.removeSectionListener(this);
		getCableDisplay().setTable(null);
		removeModuleDisplays();
	}
	/*
    private class JDisconnectorMenuItem extends JMenuItem {
    	private Connector connector;
    	private ArrayList<Cable> cables = new ArrayList<Cable>();
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
		
		protected void loadCables(ArrayList<Cable> cables) {
			for (Cable t : transitions.getTransitions(connector))
				cables.add(t);
		}
    }*/
    
    private class DragDropAction extends DropTargetAdapter {

    	public void dragOver(DropTargetDragEvent dtde) {
    		// We will only accept the ModuleToolbarButton.ModuleToolbarButtonFlavor
    		if (dtde.getCurrentDataFlavorsAsList().contains(ModuleToolbarButton.ModuleToolbarButtonFlavor)) {
    			dtde.acceptDrag(DnDConstants.ACTION_COPY);
    		}
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

    }
    
	/*public void rebuildUI() {
		removeModuleDisplays();
		populate();
	}*/
	
	void populate() {
		adjustSize = false;
        curvePanel.setTable(null); // will disable updates

		for (Module module : getModuleSection()) 
            add(module.newUI(this));
		
        curvePanel.setTable(moduleSection.getCables());
		//getCurvePanel().updateCurves();
        
        adjustSize = true;
        updateSize();
	}
	
	void removeModuleDisplays() {
		for (Iterator<ModuleUI> iter = new ComponentIterator<ModuleUI>(ModuleUI.class, this); iter.hasNext(); ) {
			iter.next().unlink();
			iter.remove();
		}
	}

	public CableDisplay getCableDisplay() {
		return curvePanel;
	}
	
	public void setDraggedComponent(Component c) {
		if (this.overlay!=c) {
			this.overlay = c;
			if (c!=null)
				setComponentZOrder(c, 0);
		}
	}

    // do not use this instead of paintChildren()
    private Rectangle clip = new Rectangle();
    private Rectangle bounds = new Rectangle();
    
	protected void paintChildren(Graphics g) {
		if (overlay!=null) {
			super.paintChildren(g);
			
			if (cablesVisible)
				getCableDisplay().paint(g);
			
			if (clip.intersects(overlay.getBounds(bounds))) 
            {
				g.translate(bounds.x, bounds.y);
				overlay.paint(g);
				g.translate(-bounds.x, -bounds.y);
			}

			if (cablesVisible)
            {
				getCableDisplay().paintDirect(g);
            }
			
		} else {
			super.paintChildren(g);
			
			if (cablesVisible)
            {
				getCableDisplay().paint(g);
				getCableDisplay().paintDirect(g);
			}
		}
	}

	public boolean areCablesVisible() {
		return cablesVisible;
	}

	public void setCablesVisible(boolean visible) {
		if (cablesVisible!=visible) {
			cablesVisible = visible;
			repaint();
		}
	}

}
