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
package net.sf.nmedit.nomad.patch.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.SwingUtilities;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Connector;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Format;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Module;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.VoiceArea;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.Event;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.HeaderListener;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.VoiceAreaListener;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.DModule;
import net.sf.nmedit.nomad.core.nomad.NomadEnvironment;
import net.sf.nmedit.nomad.main.background.BackgroundFactory;
import net.sf.nmedit.nomad.main.ui.NComponent;
import net.sf.nmedit.nomad.patch.ui.action.ModuleDragSource;
import net.sf.nmedit.nomad.patch.ui.action.ModuleSelectionSource;
import net.sf.nmedit.nomad.patch.ui.action.ModuleSelectionSource.ModuleTransferData;
import net.sf.nmedit.nomad.patch.ui.drag.CableDragAction;
import net.sf.nmedit.nomad.patch.ui.drag.ModuleSelectionTool;
import net.sf.nmedit.nomad.patch.ui.drag.PaintAbleDragAction;
import net.sf.nmedit.nomad.patch.ui.drag.SelectingDragAction;
import net.sf.nmedit.nomad.theme.component.NomadConnector;
import net.sf.nmedit.nomad.util.graphics.shape.RenderOp;

public class ModuleSectionUI extends NComponent implements 
//Scrollable,
VoiceAreaListener, HeaderListener{

	private VoiceArea moduleSection;
	
	public VoiceArea getModuleSection() {
		return moduleSection;
	}

    private final static int moduleDropAction = DnDConstants.ACTION_COPY;
	private CableDisplay curvePanel = null;
    private DragDropAction ddAction = new DragDropAction();

    /*
     * TODO optimizations
    //Overridden for performance reasons.

     */
    public void invalidate() {
        
    }
    public void validate() {
    }

    public void revalidate() {
    }
    
    public void update(Graphics g) {
        
    }

    
	public ModuleSectionUI(VoiceArea moduleSection) {
        setAutoscrolls(true);
        this.moduleSection = moduleSection;
        moduleSection.getPatch().getHeader().addHeaderListener(this);
        moduleSection.addVoiceAreaListener(this);
        curvePanel = new CableDisplay(this);
      //  patchUI = null;
        
        setOpaque(true);
        setDoubleBuffered(true);
  
        addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent event) {
                // TODO implement popup
				/*if (event.isPopupTrigger()) {
					//JMenu moduleSubMenu = ModuleGroupsMenu.getMenu();
					JPopupMenu popup = new JPopupMenu();
					popup.add(new JMenuItem("Cut",AppIcons.IC_CUT)).setEnabled(false);
					popup.add(new JMenuItem("Copy")).setEnabled(false);
					popup.add(new JMenuItem("Paste",AppIcons.IC_PASTE)).setEnabled(false);
					popup.add(new JSeparator());
					//popup.add(moduleSubMenu);
                    
                    ModuleGroupsMenu.build(popup);
                    
					popup.show(event.getComponent(),event.getX(),event.getY());
				}
                else*/ if (SwingUtilities.isLeftMouseButton(event))
                {
                    createSelectionAction(event);
                }
			}
            /*
			public void mouseReleased(MouseEvent event) {
				if (popup!=null) {
		            if (event.isPopupTrigger()) {
		            	popup.show(event.getComponent(), event.getX(), event.getY());
		            	popup = null;
		            }
				}
			}*/
            
        });

        setBackgroundB(BackgroundFactory.createMetallicBackground());
        moduleSectionResized();
        

        new DropTarget(this, moduleDropAction, ddAction, true);
    }

	private boolean adjustSize = true;

	private boolean cablesVisible = true;

	public void unlink() {
		moduleSection.removeVoiceAreaListener(this);
		//getCableDisplay().setTable(null);
		removeModuleDisplays();
        curvePanel.clear();
	}
	
	void populate() {
		adjustSize = false;
      //  curvePanel.setTable(null); // will disable updates

		for (Module module : getModuleSection())
        {
            ModuleUI ui = NomadEnvironment.sharedInstance().
                getTheme().buildModule(module);
            module.setUI(ui);
            ui.setModuleSectionUI(this);
            add(ui);
        };

        curvePanel.populate();
	    
        adjustSize = true;
        updateSize();
	}
	
	void removeModuleDisplays() {
        for (Component c : getComponents())
        {
            if (c instanceof ModuleUI)
            {
                ((ModuleUI)c).unlink();
                remove(c);
            }
        }
	}
    
    public void paint(Graphics g)
    {
        super.paint(g);

        if (cablesVisible)
        {
            curvePanel.paint(g);
        }
        
        if (dragBounds!=null)
        {
            g.setColor(Color.BLACK);
            g.drawRect(dragBounds.x,dragBounds.y,dragBounds.width-1,dragBounds.height-1);
        }

        if (dragAction!=null)
            dragAction.paint(g);
    }

    public void printCables( Graphics g )
    {
        curvePanel.paint(g);
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

    public void updateSize() {
        Dimension d = new Dimension( ModuleUI.Metrics.getPixelX(moduleSection.getImpliedWidth()),
                ModuleUI.Metrics.getPixelY(moduleSection.getImpliedHeight()) );
        d.width+=ModuleUI.Metrics.getPixelX(1);
        d.height+=ModuleUI.Metrics.getPixelY(5);
        setPreferredSize(d);
        setSize(d);
        //revalidate();
        
    }

    public void moduleSectionResized() {
        if (adjustSize) {
            updateSize();
        }
    }
    
    private Rectangle dragBounds = null;
    
    private class DragDropAction extends DropTargetAdapter {
        
        public void dragExit(DropTargetEvent e)
        {
            deleteRect();
        }
        
        private void deleteRect()
        {
            if (dragBounds!=null) 
            {
                repaint(dragBounds.x, dragBounds.y, dragBounds.width, dragBounds.height);
                dragBounds=null;
            }   
        }

        public void dragOver(DropTargetDragEvent dtde) 
        {
         
            // We will only accept the ModuleToolbarButton.ModuleToolbarButtonFlavor
            if (dtde.getCurrentDataFlavorsAsList().contains(ModuleDragSource.ModuleInfoFlavor)) 
            {
                dtde.acceptDrag(DnDConstants.ACTION_COPY);
            }
            else if (dtde.getCurrentDataFlavorsAsList().contains(ModuleSelectionSource.ModuleSelectionFlavor)) 
            {
                dtde.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
                
                Object data;
                try
                {
                    data = dtde.getTransferable().getTransferData(ModuleSelectionSource.ModuleSelectionFlavor);
                }
                catch (Throwable e)
                {
                    return ;
                }
                if (data!=null && data instanceof ModuleTransferData)
                {
                    ModuleTransferData mtd = ((ModuleTransferData) data);
                    Rectangle r = mtd.getData().getBounds(null);
                   // Point tl = mtd.getData().getTopLeftPX();
                    Point p = dtde.getLocation();
                    Point o = mtd.getOrigin();
                    
                    /*
                    o.x+=mtd.getModuleUI().getX();
                    o.y+=mtd.getModuleUI().getY();*/
                    p.x =p.x-o.x;
                    p.y =p.y-o.y;

                    r.x= p.x;
                    r.y= p.y;

                    if (dragBounds!=null) 
                        repaint(dragBounds.x, dragBounds.y, dragBounds.width, dragBounds.height);
                    dragBounds = r; 
                    repaint(dragBounds.x, dragBounds.y, dragBounds.width, dragBounds.height);
                    
                }
            }
            else
            {
                dtde.rejectDrag();
            }
        }

        public void drop(DropTargetDropEvent dtde) {
            DataFlavor chosen = null;
            Object data = null;
            // We will only accept the ModuleToolbarButton.ModuleToolbarButtonFlavor
            if (dtde.isDataFlavorSupported(ModuleDragSource.ModuleInfoFlavor) 
                    && dtde.isLocalTransfer()) {
                
                // If there were more sourceFlavors, specify which one you like
                chosen = ModuleDragSource.ModuleInfoFlavor;
                
                try {
                    // Get the data
                    dtde.acceptDrop(moduleDropAction);
                    data = dtde.getTransferable().getTransferData(chosen);
                }
                catch (Throwable t) {
                    t.printStackTrace();
                    dtde.dropComplete(false);
                    return;
                }
                
                if (data!=null && data instanceof DModule) {
                    // Cast the data and create a nice module.
                    DModule info = ((DModule)data);
                    Point p = dtde.getLocation();
                    Module mod = new Module(info);
                    mod.setLocation(ModuleUI.Metrics.getGridX(p.x),ModuleUI.Metrics.getGridY((p.y - ModuleUI.Metrics.HEIGHT)) );

                    moduleSection.add(mod);             
                }
                dtde.dropComplete(true);
            } else if (dtde.isDataFlavorSupported(ModuleSelectionSource.ModuleSelectionFlavor) 
                    && dtde.isLocalTransfer()) 
            {
                chosen = ModuleSelectionSource.ModuleSelectionFlavor;

                
                Transferable transfer = dtde.getTransferable();
                try {
                    // Get the data
                    dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                    data = transfer.getTransferData(chosen);
                }
                catch (Throwable t) {
                    t.printStackTrace();
                    dtde.dropComplete(false);
                    return;
                }
                
                if (data!=null && data instanceof ModuleTransferData) 
                {
                    // Cast the data and create a nice module.
                    ModuleTransferData tdata = ((ModuleTransferData)data);
                    ModuleSelectionTool tool = tdata.getData();
                    Point p = dtde.getLocation();
                    
                    int action = dtde.getDropAction();
                    
                    if (tool==selection && ((action&DnDConstants.ACTION_MOVE)!=0))
                    { 
                        Point o = tdata.getOrigin();
                        ModuleUI start = tdata.getModuleUI();
                        o.x+=start.getX();
                        o.y+=start.getY();
                        
                        selection.moveSelection(p.x-o.x, p.y-o.y);
                    }
                    else
                    {
                        tool.copyTo(moduleSection, ModuleUI.Metrics.getGridX(p.x), ModuleUI.Metrics.getGridY(p.y));
                    }
                    
                }
                dtde.dropComplete(true);
                
            } else {
                dtde.rejectDrop();      
                dtde.dropComplete(false);
            }
            deleteRect();
        }

    }
    
    private ModuleSelectionTool selection = new ModuleSelectionTool();

    private PaintAbleDragAction dragAction = null;
    
    public void createDragAction( NomadConnector nc, MouseEvent event )
    {
        if (nc.getConnector()==null)
            return;
        
        if (event.getClickCount()==2)
            moveCableAction(nc, event);
        else
            createCableAction(nc, event);
    }
    
    public void createCableAction( NomadConnector nc, MouseEvent event )
    {
        CableDragAction cda = new CableDragAction(nc, event.getX(), event.getY(), 
                curvePanel, RenderOp.OPTIMIZE_SPEED)
        {
            Curve curve = new Curve();
            
            {
                curve.setCurve(getStartConnectorLocation(), getStartConnectorLocation());
                add(curve);
            }

            public void dragged()
            {
                super.dragged();
                curve.setP2(getDeltaStartConnectorLocation());
                repaintDirtyRegion();
            }
            
            public void stop()
            {
                NomadConnector du = getDownUnder();
                if (du!=null && du.getConnector()!=null && getStart().getConnector()!=null)
                {
                    du.getConnector().connect(getStart().getConnector());
                }
                
                dragAction = null;
                repaintDirtyRegion();
                super.stop();
            }

            private void repaintDirtyRegion()
            {
                Rectangle bounds = getDirtyRegion();
                repaint(bounds.x-2, bounds.y-2, bounds.width+5, bounds.height+5);
            }
        };
        
        dragAction = cda;
    }
    
    public void moveCableAction( NomadConnector nc, MouseEvent event )
    {
        CableDragAction cda = new CableDragAction(nc, event.getX(), event.getY(), 
                curvePanel, RenderOp.OPTIMIZE_SPEED)
        {
            {
                Connector c = getStart().getConnector();
                curvePanel.beginUpdate();
                for(Cable cable : getCables(c)) {
                    if (cable.getC1()!=c)
                        cable.swapConnectors();
                    add(cable);
                    curvePanel.remove(cable);
                }
                curvePanel.endUpdate();
                c.disconnectCables();
            }

            protected void escape() 
            {
                curvePanel.beginUpdate();
                for (Curve curve : curveList)
                {
                    Cable cable = (Cable) curve;        
                    cable.getC1().connect(cable.getC2()); // put cables back
                }
                curvePanel.endUpdate();
                
                dragAction = null;
                uninstall();
                super.escape();
            }
            
            public void dragged()
            {
                // before super.dragged so that the dirty bounds are exact

                int ax = getAbsoluteX();
                int ay = getAbsoluteY();
                for (Curve curve:curveList) 
                {
                    curve.setP1(ax,ay);
                }
                
                super.dragged();
                repaintDirtyRegion();
            }
            
            public void stop()
            {
                NomadConnector stop = getDownUnder();

                if (stop==null || stop.getConnector()==null) {
                    // remove all : below
                } else {
                    for (Curve curve : curveList) {
                        Cable cable = (Cable) curve;
                        cable.getC2().connect(stop.getConnector());
                    }
                }
                
                dragAction = null;
                repaintDirtyRegion();
                super.stop();
            }

            private void repaintDirtyRegion()
            {
                Rectangle bounds = getDirtyRegion();
                repaint(bounds.x-2, bounds.y-2, bounds.width+5, bounds.height+5);
            }
        };
        
        dragAction = cda;
    }

    public Collection<ModuleUI> getSelectedModules()
    {
        return selection.getModuleUIs();
    }
    
    /*
    public Collection<ModuleUI> getSelection()
    {
        Collection<ModuleUI> selection = new ArrayList<ModuleUI>();

        for (int i=getComponentCount()-1;i>=0;i--)
        {
            Component c = getComponent(i);
            if (c instanceof ModuleUI)
            {
                ModuleUI m = (ModuleUI)c;
                if (m.isSelected())
                    selection.add(m);
            }
        }
        
        return selection;
    }*/
    
    private void createSelectionAction(MouseEvent event)
    {
        dragAction = new SelectingDragAction(this, event.getX(), event.getY())
        {
            {
                selection.clear();
            }
            
            public void stop()
            {
                if (getSelection().isEmpty())
                    selection.clear();
                dragAction = null;
                repaintDirtyRegion();
                super.stop();
            }
            public void dragged()
            {
                super.dragged();
                
                Rectangle selRect = getSelection();
                
                for (Iterator<ModuleUI> iter = selection.iterator();iter.hasNext();)
                {
                    ModuleUI m = iter.next();
                    if (!selRect.intersects(m.getX(), m.getY(), m.getWidth(), m.getHeight()))
                    {
                        m.setSelected(false);
                        iter.remove();
                    }
                }
                for (Component c : getComponents())
                {
                    if (c instanceof ModuleUI)
                    { 
                        ModuleUI m  = (ModuleUI)c;
                        if (selRect.intersects(m.getX(), m.getY(), m.getWidth(), m.getHeight()))
                        {
                            if (!selection.contains(m))
                                selection.add(m);
                        }
                    }
                }
                repaintDirtyRegion();
            }

            void repaintDirtyRegion()
            {
                Rectangle dirty = getDirtyRegion();
                repaint(dirty.x, dirty.y, dirty.width, dirty.height);
            }
        };
    }
    
    public void moduleAdded( Event e )
    {
        ModuleUI ui = NomadEnvironment.sharedInstance().getTheme().buildModule(e.getModule());
        ui.setModuleSectionUI(this);
        e.getModule().setUI(ui);
        add(ui);
        
        repaint(50, ui.getX(), ui.getY(), ui.getWidth(), ui.getHeight());
        /*
            Rectangle r = ui.getBounds();
            RepaintManager.currentManager(this).addDirtyRegion(this, r.x, r.y, r.width, r.height);
*/            
    }

    public void moduleRemoved( Event e )
    {
        ModuleUI m = (ModuleUI) e.getModule().getUI();
        selection.remove(m); // if selected, remove from selection
        m.setModuleSectionUI(null);
        e.getModule().setUI(null);
        m.unlink();
        remove(m);
        repaint(50, m.getX(), m.getY(), m.getWidth(), m.getHeight());
        /*
        if (isDisplayable())
            RepaintManager.currentManager(this).addDirtyRegion(this,
                bounds.x, bounds.y, bounds.width, bounds.height
            );*/
    }

    public void voiceAreaResized( Event e )
    {
        moduleSectionResized();
    }

    public void cablesAdded( Event e )
    {
        curvePanel.add(e.getConnector1(), e.getConnector2());
    }

    public void cablesRemoved( Event e )
    {
        curvePanel.remove(e.getConnector1(), e.getConnector2());
    }

    public void cableGraphUpdated( Event e )
    {
        Connector src = e.getConnector();
        curvePanel.beginUpdate();
        for (Iterator<Connector> i=src.breadthFirstSearch();i.hasNext();)
        {
            src = i.next();
            if (src.getChildCount()>0)
            {
                for (Iterator<Connector> iter = src.childIterator(); iter.hasNext(); )
                {
                    curvePanel.update(src, iter.next());
                }
            }
        }
        curvePanel.endUpdate();
    }
    public ModuleSelectionTool getSelected()
    {
        return selection;
    }
    
    public void headerValueChanged( Event e )
    {
        switch (e.getIndex())
        {
            case Format.HEADER_CABLE_VISIBILITY_BLUE:
            case Format.HEADER_CABLE_VISIBILITY_RED:
            case Format.HEADER_CABLE_VISIBILITY_YELLOW:
            case Format.HEADER_CABLE_VISIBILITY_GRAY:
            case Format.HEADER_CABLE_VISIBILITY_GREEN:
            case Format.HEADER_CABLE_VISIBILITY_PURPLE:
            case Format.HEADER_CABLE_VISIBILITY_WHITE:
                repaint();
        }
    }

}
