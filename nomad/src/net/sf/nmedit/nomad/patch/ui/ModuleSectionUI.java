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
import java.awt.Graphics2D;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Connector;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Module;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.VoiceArea;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.Event;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.VoiceAreaListener;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.DModule;
import net.sf.nmedit.nomad.core.nomad.NomadEnvironment;
import net.sf.nmedit.nomad.main.background.BackgroundFactory;
import net.sf.nmedit.nomad.main.ui.ModuleDragSource;
import net.sf.nmedit.nomad.main.ui.NComponent;
import net.sf.nmedit.nomad.patch.ui.drag.CableDragAction;
import net.sf.nmedit.nomad.patch.ui.drag.ModuleDragAction;
import net.sf.nmedit.nomad.patch.ui.drag.PaintAbleDragAction;
import net.sf.nmedit.nomad.patch.ui.drag.SelectingDragAction;
import net.sf.nmedit.nomad.theme.component.NomadConnector;
import net.sf.nmedit.nomad.util.NomadUtilities;
import net.sf.nmedit.nomad.util.graphics.shape.RenderOp;


public class ModuleSectionUI extends NComponent implements
//Scrollable,
VoiceAreaListener{

	private VoiceArea moduleSection;
	
	public VoiceArea getModuleSection() {
		return moduleSection;
	}
    
	private final static int dropAction = DnDConstants.ACTION_COPY;

	//public static final DataFlavor ModuleSectionGUIFlavor = new DataFlavor("nomad/ModuleSectionGUIFlavor", "Nomad ModuleSectionGUI");

	//private JPopupMenu popup = null;
	private CableDisplay curvePanel = null;

	private DragDropAction ddAction = new DragDropAction();

    /*
     * TODO optimizations
    //Overridden for performance reasons.

     */
    public void validate() {
    }

    public void revalidate() {
    }
    
    public void update(Graphics g) {
        
    }


	public ModuleSectionUI(VoiceArea moduleSection) {
        setAutoscrolls(true);
        this.moduleSection = moduleSection;
        moduleSection.addVoiceAreaListener(this);
        curvePanel = new CableDisplay(this);
      //  patchUI = null;
        
        setOpaque(true);
        setDoubleBuffered(true);
        new DropTarget(this, dropAction, ddAction, true);
  
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
            ModuleUI ui = NomadEnvironment.sharedInstance().getBuilder().compose(module, this);
            module.setUI(ui);
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
        revalidate();
    }
    
    public void moduleSectionResized() {
        if (adjustSize) {
            updateSize();
        }
    }

    private class DragDropAction extends DropTargetAdapter {

        public void dragOver(DropTargetDragEvent dtde) {
            // We will only accept the ModuleToolbarButton.ModuleToolbarButtonFlavor
            if (dtde.getCurrentDataFlavorsAsList().contains(ModuleDragSource.ModuleInfoFlavor)) {
                dtde.acceptDrag(DnDConstants.ACTION_COPY);
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
                    dtde.acceptDrop(dropAction);
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
            } else {
                dtde.rejectDrop();      
                dtde.dropComplete(false);
            }
        }

    }

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
    
    public void createDragAction( ModuleUI moduleUI, MouseEvent event )
    {
        ModuleDragAction mda= new ModuleDragAction(moduleUI, event.getX(), event.getY())
        {
            public void dragged()
            {
                super.dragged();
                repaintDirtyRegion();

                Component c = getSource();
                Rectangle bounds = new Rectangle( 
                        SwingUtilities.convertPoint(getSource(), c.getX(),c.getY(),ModuleSectionUI.this),
                        c.getSize());
                bounds.x = Math.max( bounds.x, 0 );
                bounds.y = Math.max( bounds.y, 0 );
                NomadUtilities.enlarge( bounds, 20 );
                //TODO 
                //scrollRectToVisible( bounds );
                
            }
            void repaintDirtyRegion()
            {
                Rectangle dirty = getDirtyRegion();
                repaint(dirty.x, dirty.y, dirty.width, dirty.height);
            }
            public void stop()
            {
                moveModules();
                dragAction = null;
                repaintDirtyRegion();
                super.stop();
            }
            private void moveModules()
            {
                moduleSection.beginUpdate();
                
                /*
                for (Iterator<ModuleUI> iter = iterator();iter.hasNext();)
                {
                    ModuleUI m = iter.next();
                    m.setLocationEx(getDeltaX()+m.getX(),getDeltaY()+m.getY());
                }*/
                
                moveSelection(getModules(), getDeltaX(), getDeltaY());
                moduleSection.endUpdate();
            }
            public void paint(Graphics g)
            {
                Graphics2D g2 = (Graphics2D) g;

                g2.setColor(Color.BLUE);
                super.paint(g);
            }
        };
        mda.addModule(moduleUI);
        
        for (int i = getComponentCount()-1;i>=0;i--)
        {
            Component c = getComponent(i);
            if (c instanceof ModuleUI)
            {
                ModuleUI m = (ModuleUI) c;
                if (m.isSelected() && m!=moduleUI)
                    mda.addModule(m);
            }
        }
        
        dragAction = mda;
    }
    
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
    }
    
    private void createSelectionAction(MouseEvent event)
    {
        dragAction = new SelectingDragAction(this, event.getX(), event.getY())
        {
            Set<ModuleUI> all = new HashSet<ModuleUI>();
            Set<ModuleUI> selection = new HashSet<ModuleUI>();
            
            {
                for (int i=getComponentCount()-1;i>=0;i--)
                {
                    Component c = getComponent(i);
                    if (c instanceof ModuleUI)
                    {
                        ModuleUI m = (ModuleUI)c;
                        if (m.isSelected())
                            selection.add(m);
                        all.add(m);
                    }
                }
            }
            
            public void stop()
            {
                if (getSelection().isEmpty())
                {
                    for (ModuleUI m : selection)
                        m.setSelected(false);
                }
                
                all.clear();
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
                for (ModuleUI m : all)
                {
                    if (selRect.intersects(m.getX(), m.getY(), m.getWidth(), m.getHeight()))
                    {
                        if (!selection.contains(m))
                        {
                            m.setSelected(true);
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
    
    private void moveSelection(ModuleUI [] movedModules, int dx, int dy)
    {
        final List<ModuleUI> moved = Arrays.asList(movedModules);
        final List<Point> locations = new ArrayList<Point>(moved.size());
        
        Collections.sort(moved, new DescendingYOrder());
        
        for (ModuleUI module:moved)
        {
            locations.add(new Point(module.getX()+dx, module.getY()+dy));
        }
        for (int i=0;i<moved.size();i++)
        {
            Point dst = locations.get(i);
            moved.get(i).setLocationEx(dst.x, dst.y);
            
        }
    }

    private static class DescendingYOrder implements Comparator<Component>
    {
        public int compare( Component o1, Component o2 )
        {            
            return o1.getY()-o2.getY();
        }
        
    }

    public void moduleAdded( Event e )
    {
        ModuleUI ui = NomadEnvironment.sharedInstance().getBuilder().compose(e.getModule(), this);
        e.getModule().setUI(ui);
        add(ui);
        
            //Component c;
            add(ui/*c=event.getModule().newUI(this)*/);
            Rectangle r = ui.getBounds();
            RepaintManager.currentManager(this).addDirtyRegion(this, r.x, r.y, r.width, r.height);
    }

    public void moduleRemoved( Event e )
    {
        ModuleUI m = (ModuleUI) e.getModule().getUI();
        Rectangle bounds = m.getBounds(); 
        remove(m);
        if (isDisplayable())
            RepaintManager.currentManager(this).addDirtyRegion(this,
                bounds.x, bounds.y, bounds.width, bounds.height
            );
        e.getModule().setUI(null);
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
}
