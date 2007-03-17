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
 * Created on Jan 21, 2007
 */
package net.sf.nmedit.jtheme.component.plaf;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
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
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.plaf.ComponentUI;

import net.sf.nmedit.jpatch.ConnectionManager;
import net.sf.nmedit.jpatch.Module;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.cable.Cable;
import net.sf.nmedit.jtheme.cable.JTCableManager;
import net.sf.nmedit.jtheme.component.JTModule;
import net.sf.nmedit.jtheme.component.JTModuleContainer;

public class JTModuleContainerUI extends ComponentUI
{
    
    public static final String backgroundKey = "ModuleContainerUI.background";
    public static final String DnDAllowedKey = "ModuleContainerUI.DnDAllowed";
    
    public static final String baseFontKey = "ModuleContainer.font";
    
    protected JTModuleContainer jtc;
    
    public JTModuleContainerUI(JTModuleContainer jtc)
    {
        this.jtc = jtc;
    }

    public static JTModuleContainerUI createUI(JComponent c)
    {
        return new JTModuleContainerUI((JTModuleContainer)c);
    }

    public JTModuleContainer getModuleContainer()
    {
        return jtc;
    }
    
    public void installUI(JComponent c) 
    {
        JTModuleContainer jtc = (JTModuleContainer) c;
        JTContext context = jtc.getContext();
        UIDefaults defaults = context.getUIDefaults();
        
        Color background = defaults.getColor(backgroundKey);
        if (background != null)
            c.setBackground(background);
        
        Font baseFont = defaults.getFont(baseFontKey);
        if (baseFont != null)
            c.setFont(baseFont);
        
        // System.out.println(DnDAllowedKey+": "+defaults.getBoolean(DnDAllowedKey));
        
        if (defaults.getBoolean(DnDAllowedKey))
        {
            installDnDHandler(jtc);
        }
    }

    public void uninstallUI(JComponent c) 
    {
        JTModuleContainer jtc = (JTModuleContainer) c;
        JTContext context = jtc.getContext();
        UIDefaults defaults = context.getUIDefaults();
        
        if (defaults.getBoolean(DnDAllowedKey))
        {
            uninstallDnDHandler(jtc);
        }
    }

    public void update(Graphics g, JComponent c) 
    {
        if (c.isOpaque()) 
        {
            g.setColor(c.getBackground());
            g.fillRect(0, 0, c.getWidth(),c.getHeight());
        }
        paint(g, c);
    }
    
    public void paint(Graphics g, JComponent c) 
    {
        //
    }

    public void paintChildrenHack(Graphics g)
    {
        Rectangle box = dndBox;
        if (box == null) return;
        
        g.setColor(Color.BLUE);
        g.drawRect(box.x, box.y, box.width-1, box.height-1);
        // System.out.println("paintbox: "+box);
    }
    
    private transient Rectangle dndBox;
    private transient Point dndInitialScrollLocation;
    
    private void revalidateParent()
    {
        JComponent parent = (JComponent) getModuleContainer().getParent();
        if (parent != null)
            parent.revalidate();
    }
    
    public void updateDnDBoundingBox(Rectangle box)
    {
        Rectangle repaint;
        if (dndBox != null)
        {
            repaint = dndBox;
            if (box != null)
                SwingUtilities.computeUnion(box.x, box.y, box.width, box.height, repaint);
        }
        else
        {
            repaint = box;
        }
        
        if (repaint != null)
        {
            /*
            int enlarge = 5;
            repaint.x-= enlarge;
            repaint.y-= enlarge;
            repaint.width+= enlarge*2;
            repaint.height+= enlarge*2;*/
            getModuleContainer().repaint(repaint);
        }
        
        if (box == null)
        {
            dndBox = null;
            
            if (dndInitialScrollLocation != null)
            {
                Rectangle r = 
                getModuleContainer()
                .getVisibleRect();

                r.x = dndInitialScrollLocation.x;
                r.y = dndInitialScrollLocation.y;
                
                getModuleContainer().scrollRectToVisible(r);
            }
            
            getModuleContainer().setPreferredSize(
                getModuleContainer().computePreferredSize(null)        
            );            
            dndInitialScrollLocation = null;
            revalidateParent();
        }
        else
        {
            if (dndInitialScrollLocation == null)
            {
                Rectangle r = 
                    getModuleContainer()
                    .getVisibleRect();
                
                dndInitialScrollLocation
                = r.getLocation();
                
            }
            
            dndBox = new Rectangle(box);

            if (dndBox.x<0) dndBox.x = 0;
            if (dndBox.y<0) dndBox.y = 0;

            int r = dndBox.x + dndBox.width +140;
            int b = dndBox.y + dndBox.height+140;
            
            if (r>getModuleContainer().getWidth()
            || b>getModuleContainer().getHeight()
            ) 
            {
                getModuleContainer()
                .setPreferredSize(new Dimension(r, b));
                revalidateParent();
            }
            
            getModuleContainer().scrollRectToVisible(dndBox);
        }
    }
    
    private void installDnDHandler(JTModuleContainer jtc)
    {
        createDndHandler(jtc);
    }

    private void uninstallDnDHandler(JTModuleContainer jtc)
    {
        DnDHandler handler = lookupDndHandler(jtc);
        if (handler != null) 
            handler.uninstall();
    }
    
    protected Object DndHandlerKey()
    {
        return "JTModuleContainerDnDHandler";
    }

    protected DnDHandler createDndHandler(JTModuleContainer jtc)
    {
        DnDHandler handler = new DnDHandler(this);
        jtc.putClientProperty(DndHandlerKey(), handler);
        return handler;
    }
    
    protected DnDHandler lookupDndHandler(JTModuleContainer jtc)
    {
        Object obj = jtc.getClientProperty(DndHandlerKey());
        if (obj != null && (obj instanceof DnDHandler))
            return (DnDHandler) obj;
        return null;
    }
    
    public static interface ModuleTransferData extends Transferable
    {
        JTModuleContainer getSource();
        JTModule[] getModules();
        Point getDragStartLocation();
        Rectangle getBoundingBox();
        Rectangle getBoundingBox(Rectangle r);
    }
    
    protected static class ModuleTransferDataWrapper implements ModuleTransferData
    {
        private DnDHandler delegate;
        private Point dragStartLocation;

        public ModuleTransferDataWrapper(DnDHandler delegate, Point dragStartLocation)
        {
            this.delegate = delegate;
            this.dragStartLocation = dragStartLocation;
        }

        public Point getDragStartLocation()
        {
            return new Point(dragStartLocation);
        }

        public JTModule[] getModules()
        {
            return delegate.getModules();
        }
        
        private transient Rectangle boundingBox;

        public Rectangle getBoundingBox()
        {
            return getBoundingBox(null);
        }
        
        public Rectangle getBoundingBox(Rectangle r)
        {
            if (boundingBox == null)
            {
                boundingBox = new Rectangle(0,0,0,0);
                
                JTModule[] modules = getModules();
                
                if (modules.length>0)
                {
                    JTModule m = modules[0];
                    boundingBox = m.getBounds(boundingBox);
                    for (int i=modules.length-1;i>=1;i--)
                    {
                        m = modules[i];
                        SwingUtilities.computeUnion(
                                m.getX(), m.getY(), 
                                m.getWidth(), m.getHeight(), 
                                boundingBox);
                    }
                }
            }
            
            if (r == null)
            {
                return new Rectangle(boundingBox);
            }
            else
            {
                r.setRect(boundingBox);
                return r;
            }
        }
        
        public JTModuleContainer getSource()
        {
            return delegate.getSource();
        }

        public ModuleTransferData getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
        {
            if (!isDataFlavorSupported(flavor))
                throw new UnsupportedFlavorException(flavor);
            return this;
        }

        public DataFlavor[] getTransferDataFlavors()
        {
            DataFlavor[] flavors = {DnDHandler.ModuleSelectionFlavor};
            return flavors;
        }

        public boolean isDataFlavorSupported(DataFlavor flavor)
        {
            // DropTarget gives his flavors. DropSource looks if it can be dropped on the target.
            return flavor != null && flavor.equals(DnDHandler.ModuleSelectionFlavor);
        }
    }
    
    public static class DnDHandler
      implements ContainerListener, 
      DropTargetListener, DragGestureListener, DragSourceListener,
      MouseListener
    {

        private JTModuleContainerUI jtcUI;

        public DnDHandler(JTModuleContainerUI jtcUI)
        {
            this.jtcUI = jtcUI;
            
            if (jtcUI.getModuleContainer().isDnDAllowed())
                install();
        }
        
        public JTModuleContainer getModuleContainer()
        {
            return jtcUI.getModuleContainer();
        }
        
        public void install()
        {
            JTModuleContainer jtc = getModuleContainer();
            installAtModuleContainer(jtc);
            
            for (int i=jtc.getComponentCount()-1;i>=0;i--)
            {
                Component component = jtc.getComponent(i);
                if (installsAtChild(component))
                    installAtChild(component);
            }
        }
        
        protected boolean installsAtChild(Component component)
        {
            return component instanceof JTModule; 
        }
        
        public void uninstall()
        {
            JTModuleContainer jtc = getModuleContainer();
            uninstallAtModuleContainer(jtc);
            
            for (int i=jtc.getComponentCount()-1;i>=0;i--)
            {
                Component component = jtc.getComponent(i);
                if (installsAtChild(component))
                    uninstallAtChild(component);
            }
        }

        public void componentAdded(ContainerEvent e)
        {
            Component component = e.getChild();
            if (installsAtChild(component))
                installAtChild(component);
        }

        public void componentRemoved(ContainerEvent e)
        {
            Component component = e.getChild();
            if (installsAtChild(component))
                uninstallAtChild(component);
        }

        protected DropTarget moduleContainerDropTarget;
        protected int dndActions =  DnDConstants.ACTION_COPY_OR_MOVE;
        protected DropTargetListener dropTargetListener;

        public static final DataFlavor ModuleSelectionFlavor = new DataFlavor("nomad/ModuleSelectionFlavor", "Nomad ModuleSelectionFlavor");
        
        protected DropTargetListener createDropTargetListener()
        {
            return this;
        }
        
        protected void installAtModuleContainer(JTModuleContainer jtc)
        {
            jtc.addContainerListener(this);
            jtc.addMouseListener(this);
            dropTargetListener = createDropTargetListener();
            moduleContainerDropTarget = new DropTarget(jtc, dndActions, dropTargetListener, true);
        }

        protected void uninstallAtModuleContainer(JTModuleContainer jtc)
        {
            // TODO
            jtc.removeContainerListener(this);
            jtc.removeMouseListener(this);
        }
        
        protected void installAtChild(Component component)
        {
            component.addMouseListener(this);
            // JTModule module = (JTModule) component;
            DragSource dragSource = DragSource.getDefaultDragSource();
            dragSource.createDefaultDragGestureRecognizer(component, dndActions, this);
        }

        protected void uninstallAtChild(Component component)
        {
            // TODO JTModule module = (JTModule) component;

            component.removeMouseListener(this);
        }

        public void dragEnter(DropTargetDragEvent dtde)
        {
            // no op
            //dragOver(dtde);
        }

        public void dragExit(DropTargetEvent dte)
        {
            jtcUI.updateDnDBoundingBox(null);
        }

        public void dragOver(DropTargetDragEvent dtde)
        {
            /*if (dtde.getCurrentDataFlavorsAsList().contains(ModuleDragSource.ModuleInfoFlavor))
            {
                dtde.acceptDrag(DnDConstants.ACTION_COPY);
            }
            else*/
            if (dtde.getCurrentDataFlavorsAsList().contains(DnDHandler.ModuleSelectionFlavor))
            {
                dtde.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);

                ModuleTransferData data;
                try
                {
                    data = (ModuleTransferData) dtde.getTransferable().getTransferData(DnDHandler.ModuleSelectionFlavor);

                    if (data!=null)
                    {
                        paintDragOver(data, dtde);
                        return ;
                    }
                }
                catch (Throwable e)
                {
                    ;
                }
            }
            
            jtcUI.updateDnDBoundingBox(null);
            
            dtde.rejectDrag();       
        }

        private transient Rectangle cachedRectangle;
        
        private void paintDragOver(ModuleTransferData data, DropTargetDragEvent dtde)
        {
            Rectangle box = (cachedRectangle = data.getBoundingBox(cachedRectangle));
            
            Point o = data.getDragStartLocation();
            Point p = new Point(dtde.getLocation());
                     
            p.x =p.x-o.x;
            p.y =p.y-o.y;

            box.x += p.x;
            box.y += p.y;

            jtcUI.updateDnDBoundingBox(box);
        }

        public void drop(DropTargetDropEvent dtde)
        {
            jtcUI.updateDnDBoundingBox(null);
            
            
            DataFlavor chosen = null;
            Object data = null;
            
            /*
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
            } else  */
            
            if (dtde.isDataFlavorSupported(DnDHandler.ModuleSelectionFlavor)
                    && dtde.isLocalTransfer())
            {
                chosen = DnDHandler.ModuleSelectionFlavor;


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
                    
                    //Point p = dtde.getLocation();

                    int action = dtde.getDropAction();

                    if (tdata.getSource() == getModuleContainer() && ((action&DnDConstants.ACTION_MOVE)!=0))
                    {
                        moveSelection(tdata, dtde);
                        //jtcUI.updateDnDBoundingBox(null);
                    }
                    else
                    {
                        copySelection(tdata, dtde);
                    }

                }
                dtde.dropComplete(true);

            } else {
                dtde.rejectDrop();
                dtde.dropComplete(false);
            }
            jtcUI.updateDnDBoundingBox(null);
        }

        private void copySelection(ModuleTransferData tdata, DropTargetDropEvent dtde)
        {
          //  JTModuleContainer jtcontainer = tdata.getSource();
            
        }
        
        /*
        public void copyTo(ModuleContainer v, int dx, int dy)
        {
    
            Point tl = getTopLeft();
    
            Set<Module> modules = modules();
    
            if (modules.isEmpty())
                return ;
    
            Map<Module,Module> clonemap = new HashMap<Module,Module>();
            for (Module m : modules)
            {
                Module clone = m.clone();
                clone.setIndex(-1);
    
                clone.setLocation(clone.getX()-tl.x+dx, clone.getY()-tl.y+dy);
                clonemap.put(m, clone);
    
                v.add(clone);
            }
    
            for (Module m : modules)
            {
                Module clone = clonemap.get(m);
                for (int i=m.getConnectorCount()-1;i>=0;i--)
                {
                    Connector c1 = m.getConnector(i);
                    Iterator<Connector> i2 = c1.childIterator();
                    while (i2.hasNext())
                    {
                        Connector c2 = i2.next();
                        Module m2 = c2.getModule();
                        Module clone2 = clonemap.get(m2);
                        if (clone2!=null)
                        {
                            Connector c1Clone = clone.getConnector(i);
                            Connector c2Clone = clone2.getConnector(c2.getDefinition().getContextId());
                            Connector.connect(c1Clone, c2Clone, null);
                        }
                    }
                }
            }
    
        }*/
       
        private void moveSelection(ModuleTransferData tdata, DropTargetDropEvent dtde)
        {
            Point o = tdata.getDragStartLocation();
            Point p = new Point(dtde.getLocation());

            p.x = p.x-o.x;
            p.y = p.y-o.y;
            
            JTModule[] modules = tdata.getModules();
            for (JTModule module: modules)
            {
                module.setLocation(module.getX()+p.x, module.getY()+p.y);
            }
            
            JTCableManager cman =
            getModuleContainer().getCableManager();
            
            ConnectionManager cm = null;
            
            // TODO faster search of cables 
            for (Iterator<Cable> i=cman.getVisible(); i.hasNext();)
            {
                Cable cable = i.next();
                
                for (int j=0;j<modules.length;j++)
                {
                    Module module = modules[j].getModule();
                    if (cm == null)
                        cm = module.getParent().getConnectionManager();
                    
                    if (cm.isConnected(cable.getSource()) || cm.isConnected(cable.getDestination()))
                    {
                        cman.update(cable);
                        break;
                    }
                }
            }
            
            cman.notifyRepaintManager();
        }

        public void dropActionChanged(DropTargetDragEvent dtde)
        {
            // no op
        }

        protected Set<JTModule> selectionSet = new HashSet<JTModule>();
        
        protected int getSelectionSize()
        {
            return selectionSet.size();
        }

        protected boolean isOnlyThisSelected(Object module)
        {
            return getSelectionSize() == 1 && isInSelection(module);
        }

        protected void selectOnly(JTModule module)
        {
            if (!selectionSet.isEmpty())
            {
                for (JTModule dif : selectionSet.toArray(new JTModule[selectionSet.size()]))
                    if (dif != module)
                        removeSelection(dif);
            }
            
            if (selectionSet.isEmpty())
                addSelection(module);
        }
        
        protected void addSelection(JTModule module)
        {
            selectionSet.add(module);
            module.setSelected(true);
        }

        protected void removeSelection(JTModule module)
        {
            selectionSet.remove(module);
            module.setSelected(false);
        }

        protected boolean isInSelection(Object module)
        {
            return selectionSet.contains(module);
        }
        
        protected boolean isSelectionEmpty()
        {
            return selectionSet.isEmpty();
        }
        
        protected void clearSelection()
        {
            if (selectionSet.isEmpty())
                return;
            
            for (JTModule module: selectionSet)
                module.setSelected(false);
            
            selectionSet.clear();
        }
        
        public void dragGestureRecognized(DragGestureEvent dge)
        {
            JTModule module = (JTModule) dge.getComponent();
            if (!module.isEnabled()) return;
          
            if (isSelectionEmpty())
            {
                addSelection(module);
            }
            else if (isInSelection(module))
            {
                // thats ok
            }
            else
            {
                if (!isInSelection(module))
                {
                    clearSelection();
                    addSelection(module);
                }
            }

            if (!isSelectionEmpty())
            {
                Component c = dge.getComponent();
                Point dndOrigin = dge.getDragOrigin();
                // if (c instanceof JTModule) // always true
                {
                    dndOrigin = SwingUtilities.convertPoint(c, dndOrigin, getModuleContainer());
                }
                
                ModuleTransferData transferData = new ModuleTransferDataWrapper(this, dndOrigin);
                dge.startDrag(DragSource.DefaultMoveDrop, transferData, this);
            }
        }

        public JTModule[] getModules()
        {
            return selectionSet.toArray(new JTModule[selectionSet.size()]);
        }

        public JTModuleContainer getSource()
        {
            return getModuleContainer();
        }

        public Point getDragStartLocation()
        {
            throw new UnsupportedOperationException();
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

        public void mouseClickedAtModuleContainer(MouseEvent e)
        {
            if (SwingUtilities.isLeftMouseButton(e))
                clearSelection();
        }
        
        public void mouseClickedAtModule(MouseEvent e)
        {
            if (!SwingUtilities.isLeftMouseButton(e))
                return;
            
            boolean shift = e.isShiftDown();
            boolean ctrl = e.isControlDown();
            
            if (shift && ctrl) return;
            
            JTModule module = (JTModule) e.getComponent();
            
            if (shift)
                addSelection(module);
            else if (ctrl)
                removeSelection(module);
            else // !(shift||ctrl)
            {
                selectOnly(module);
            }
        }
        
        public void mouseClicked(MouseEvent e)
        {
            if (e.getComponent() == getModuleContainer())
                mouseClickedAtModuleContainer(e);
            else if (e.getComponent() instanceof JTModule)
                mouseClickedAtModule(e);
        }

        public void mouseEntered(MouseEvent e)
        {
            // no op
        }

        public void mouseExited(MouseEvent e)
        {
            // no op
        }

        public void mousePressed(MouseEvent e)
        {
            // no op
        }

        public void mouseReleased(MouseEvent e)
        {
            // no op
        }
        
        /*
         * 
        private final static int moduleDropAction = DnDConstants.ACTION_COPY;
        private CableDisplay curvePanel = null;
        private DragDropAction ddAction = new DragDropAction();
        {
            this = container
             new DropTarget(this, moduleDropAction, ddAction, true);
        }
        private class DragDropAction extends DropTargetAdapter {
            247 
            248         public void dragExit(DropTargetEvent e)
            249         {
            250             deleteRect();
            251         }
            252 
            253         private void deleteRect()
            254         {
            255             if (dragBounds!=null)
            256             {
            257                 repaint(dragBounds.x, dragBounds.y, dragBounds.width, dragBounds.height);
            258                 dragBounds=null;
            259             }
            260         }
            261 
            262         public void dragOver(DropTargetDragEvent dtde)
            263         {
            264 
            265             // We will only accept the ModuleToolbarButton.ModuleToolbarButtonFlavor
            266             if (dtde.getCurrentDataFlavorsAsList().contains(ModuleDragSource.ModuleInfoFlavor))
            267             {
            268                 dtde.acceptDrag(DnDConstants.ACTION_COPY);
            269             }
            270             else if (dtde.getCurrentDataFlavorsAsList().contains(ModuleSelectionSource.ModuleSelectionFlavor))
            271             {
            272                 dtde.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
            273 
            274                 Object data;
            275                 try
            276                 {
            277                     data = dtde.getTransferable().getTransferData(ModuleSelectionSource.ModuleSelectionFlavor);
            278                 }
            279                 catch (Throwable e)
            280                 {
            281                     return ;
            282                 }
            283                 if (data!=null && data instanceof ModuleTransferData)
            284                 {
            285                     ModuleTransferData mtd = ((ModuleTransferData) data);
            286                     Rectangle r = mtd.getData().getBounds(null);
            287                    // Point tl = mtd.getData().getTopLeftPX();
            288                     Point p = dtde.getLocation();
            289                     Point o = mtd.getOrigin();
            290 
            291                     
            292                 //    o.x+=mtd.getModuleUI().getX();
            293                   //  o.y+=mtd.getModuleUI().getY();
            294                     p.x =p.x-o.x;
            295                     p.y =p.y-o.y;
            296 
            297                     r.x= p.x;
            298                     r.y= p.y;
            299 
            300                     if (dragBounds!=null)
            301                         repaint(dragBounds.x, dragBounds.y, dragBounds.width, dragBounds.height);
            302                     dragBounds = r;
            303                     repaint(dragBounds.x, dragBounds.y, dragBounds.width, dragBounds.height);
            304 
            305                 }
            306             }
            307             else
            308             {
            309                 dtde.rejectDrag();
            310             }
            311         }
            312 
            313         public void drop(DropTargetDropEvent dtde) {
            314             DataFlavor chosen = null;
            315             Object data = null;
            316             // We will only accept the ModuleToolbarButton.ModuleToolbarButtonFlavor
            317             if (dtde.isDataFlavorSupported(ModuleDragSource.ModuleInfoFlavor)
            318                     && dtde.isLocalTransfer()) {
            319 
            320                 // If there were more sourceFlavors, specify which one you like
            321                 chosen = ModuleDragSource.ModuleInfoFlavor;
            322 
            323                 try {
            324                     // Get the data
            325                     dtde.acceptDrop(moduleDropAction);
            326                     data = dtde.getTransferable().getTransferData(chosen);
            327                 }
            328                 catch (Throwable t) {
            329                     t.printStackTrace();
            330                     dtde.dropComplete(false);
            331                     return;
            332                 }
            333 
            334                 if (data!=null && data instanceof DModule) {
            335                     // Cast the data and create a nice module.
            336                     DModule info = ((DModule)data);
            337                     Point p = dtde.getLocation();
            338                     Module mod = new Module(info);
            339                     mod.setLocation(ModuleUI.Metrics.getGridX(p.x),ModuleUI.Metrics.getGridY((p.y - ModuleUI.Metrics.HEIGHT)) );
            340 
            341                     moduleSection.add(mod);
            342                 }
            343                 dtde.dropComplete(true);
            344             } else if (dtde.isDataFlavorSupported(ModuleSelectionSource.ModuleSelectionFlavor)
            345                     && dtde.isLocalTransfer())
            346             {
            347                 chosen = ModuleSelectionSource.ModuleSelectionFlavor;
            348 
            349 
            350                 Transferable transfer = dtde.getTransferable();
            351                 try {
            352                     // Get the data
            353                     dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
            354                     data = transfer.getTransferData(chosen);
            355                 }
            356                 catch (Throwable t) {
            357                     t.printStackTrace();
            358                     dtde.dropComplete(false);
            359                     return;
            360                 }
            361 
            362                 if (data!=null && data instanceof ModuleTransferData)
            363                 {
            364                     // Cast the data and create a nice module.
            365                     ModuleTransferData tdata = ((ModuleTransferData)data);
            366                     ModuleSelectionTool tool = tdata.getData();
            367                     Point p = dtde.getLocation();
            368 
            369                     int action = dtde.getDropAction();
            370 
            371                     if (tool==selection && ((action&DnDConstants.ACTION_MOVE)!=0))
            372                     {
            373                         Point o = tdata.getOrigin();
            374                         ModuleUI start = tdata.getModuleUI();
            375                         o.x+=start.getX();
            376                         o.y+=start.getY();
            377 
            378                         selection.moveSelection(p.x-o.x, p.y-o.y);
            379                     }
            380                     else
            381                     {
            382                         tool.copyTo(moduleSection, ModuleUI.Metrics.getGridX(p.x), ModuleUI.Metrics.getGridY(p.y));
            383                     }
            384 
            385                 }
            386                 dtde.dropComplete(true);
            387 
            388             } else {
            389                 dtde.rejectDrop();
            390                 dtde.dropComplete(false);
            391             }
            392             deleteRect();
            393         }
            394 
            395     }
            396 
            397     private ModuleSelectionTool selection = new ModuleSelectionTool();
            398 
            399     private PaintAbleDragAction dragAction = null;
            400 
            401     public void createDragAction( NomadConnector nc, MouseEvent event )
            402     {
            403         if (nc.getConnector()==null)
            404             return;
            405 
            406         if (event.getClickCount()==2)
            407             moveCableAction(nc, event);
            408         else
            409             createCableAction(nc, event);
            410     }
            411 
            412     public void createCableAction( NomadConnector nc, MouseEvent event )
            413     {
            414         CableDragAction cda = new CableDragAction(nc, event.getX(), event.getY(),
            415                 curvePanel, RenderOp.OPTIMIZE_SPEED)
            416         {
            417             Curve curve = new Curve();
            418 
            419             {
            420                 curve.setCurve(getStartConnectorLocation(), getStartConnectorLocation());
            421                 add(curve);
            422             }
            423 
            424             public void dragged()
            425             {
            426                 super.dragged();
            427                 curve.setP2(getDeltaStartConnectorLocation());
            428                 repaintDirtyRegion();
            429             }
            430 
            431             public void stop()
            432             {
            433                 NomadConnector du = getDownUnder();
            434                 if (du!=null && du.getConnector()!=null && getStart().getConnector()!=null)
            435                 {
            436                     du.getConnector().connect(getStart().getConnector());
            437                 }
            438 
            439                 dragAction = null;
            440                 repaintDirtyRegion();
            441                 super.stop();
            442             }
            443 
            444             private void repaintDirtyRegion()
            445             {
            446                 Rectangle bounds = getDirtyRegion();
            447                 repaint(bounds.x-2, bounds.y-2, bounds.width+5, bounds.height+5);
            448             }
            449         };
            450 
            451         dragAction = cda;
            452     }
            453 
            454     public void moveCableAction( NomadConnector nc, MouseEvent event )
            455     {
            456         CableDragAction cda = new CableDragAction(nc, event.getX(), event.getY(),
            457                 curvePanel, RenderOp.OPTIMIZE_SPEED)
            458         {
            459             {
            460                 Connector c = getStart().getConnector();
            461                 curvePanel.beginUpdate();
            462                 for(Cable cable : getCables(c)) {
            463                     if (cable.getC1()!=c)
            464                         cable.swapConnectors();
            465                     add(cable);
            466                     curvePanel.remove(cable);
            467                 }
            468                 curvePanel.endUpdate();
            469                 c.disconnectCables();
            470             }
            471 
            472             protected void escape()
            473             {
            474                 curvePanel.beginUpdate();
            475                 for (Curve curve : curveList)
            476                 {
            477                     Cable cable = (Cable) curve;
            478                     cable.getC1().connect(cable.getC2()); // put cables back
            479                 }
            480                 curvePanel.endUpdate();
            481 
            482                 dragAction = null;
            483                 uninstall();
            484                 super.escape();
            485             }
            486 
            487             public void dragged()
            488             {
            489                 // before super.dragged so that the dirty bounds are exact
            490 
            491                 int ax = getAbsoluteX();
            492                 int ay = getAbsoluteY();
            493                 for (Curve curve:curveList)
            494                 {
            495                     curve.setP1(ax,ay);
            496                 }
            497 
            498                 super.dragged();
            499                 repaintDirtyRegion();
            500             }
            501 
            502             public void stop()
            503             {
            504                 NomadConnector stop = getDownUnder();
            505 
            506                 if (stop==null || stop.getConnector()==null) {
            507                     // remove all : below
            508                 } else {
            509                     for (Curve curve : curveList) {
            510                         Cable cable = (Cable) curve;
            511                         cable.getC2().connect(stop.getConnector());
            512                     }
            513                 }
            514 
            515                 dragAction = null;
            516                 repaintDirtyRegion();
            517                 super.stop();
            518             }
            519 
            520             private void repaintDirtyRegion()
            521             {
            522                 Rectangle bounds = getDirtyRegion();
            523                 repaint(bounds.x-2, bounds.y-2, bounds.width+5, bounds.height+5);
            524             }
            525         };
            526 
            527         dragAction = cda;
            528     }
            529 
            530     public Collection<ModuleUI> getSelectedModules()
            531     {
            532         return selection.getModuleUIs();
            533     }
            534 
            554     private void createSelectionAction(MouseEvent event)
            555     {
            556         dragAction = new SelectingDragAction(this, event.getX(), event.getY())
            557         {
            558             {
            559                 selection.clear();
            560             }
            561 
            562             public void stop()
            563             {
            564                 if (getSelection().isEmpty())
            565                     selection.clear();
            566                 dragAction = null;
            567                 repaintDirtyRegion();
            568                 super.stop();
            569             }
            570             public void dragged()
            571             {
            572                 super.dragged();
            573 
            574                 Rectangle selRect = getSelection();
            575 
            576                 for (Iterator<ModuleUI> iter = selection.iterator();iter.hasNext();)
            577                 {
            578                     ModuleUI m = iter.next();
            579                     if (!selRect.intersects(m.getX(), m.getY(), m.getWidth(), m.getHeight()))
            580                     {
            581                         m.setSelected(false);
            582                         iter.remove();
            583                     }
            584                 }
            585                 for (Component c : getComponents())
            586                 {
            587                     if (c instanceof ModuleUI)
            588                     {
            589                         ModuleUI m  = (ModuleUI)c;
            590                         if (selRect.intersects(m.getX(), m.getY(), m.getWidth(), m.getHeight()))
            591                         {
            592                             if (!selection.contains(m))
            593                                 selection.add(m);
            594                         }
            595                     }
            596                 }
            597                 repaintDirtyRegion();
            598             }
            599 
            600             void repaintDirtyRegion()
            601             {
            602                 Rectangle dirty = getDirtyRegion();
            603                 repaint(dirty.x, dirty.y, dirty.width, dirty.height);
            604             }
            605         };
            606     }*/
    }
    
}
