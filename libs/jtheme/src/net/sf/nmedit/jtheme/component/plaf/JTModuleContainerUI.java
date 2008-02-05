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
import java.awt.event.ActionEvent;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.plaf.ComponentUI;

import net.sf.nmedit.jpatch.InvalidDescriptorException;
import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleContainer;
import net.sf.nmedit.jpatch.PModuleDescriptor;
import net.sf.nmedit.jpatch.MoveOperation;
import net.sf.nmedit.jpatch.PatchUtils;
import net.sf.nmedit.jpatch.history.History;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.cable.Cable;
import net.sf.nmedit.jtheme.cable.JTCableManager;
import net.sf.nmedit.jtheme.component.JTModule;
import net.sf.nmedit.jtheme.component.JTModuleContainer;
import net.sf.nmedit.jtheme.dnd.JTDragDrop;
import net.sf.nmedit.nmutils.swing.NmSwingUtilities;

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

    public void createPopupMenu(JTModuleContainer mc, MouseEvent e)
    {
        JPopupMenu popup = new JPopupMenu();
        popup.add(new ContainerAction(ContainerAction.DELETE_UNUSED));
        popup.show(mc, e.getX(), e.getY());
    }
    
    protected class ContainerAction extends AbstractAction
    {
        
        /**
         * 
         */
        private static final long serialVersionUID = 7135918324094843867L;
        public static final String DELETE_UNUSED = "delete.unused";

        public ContainerAction(String command)
        {
            if (command == DELETE_UNUSED)
            {
                putValue(NAME, "Delete Unused Modules");
                putValue(ACTION_COMMAND_KEY, command);
                PModuleContainer t = getTarget();
                setEnabled(t != null && PatchUtils.hasUnusedModules(t));
            }
        }
        
        protected PModuleContainer getTarget()
        {
            return getModuleContainer().getModuleContainer();
        }
        
        public void actionPerformed(ActionEvent e)
        {
            if (isEnabled())
            {
                if (getValue(ACTION_COMMAND_KEY)==DELETE_UNUSED)
                {
                    PModuleContainer mc = getTarget();
                    if (mc != null)
                    {
                        History history = mc.getPatch().getHistory();
                        try
                        {
                            if (history != null)
                                history.beginRecord();
                        
                            while (PatchUtils.removeUnusedModules(mc)>0);
                        } 
                        finally
                        {
                            if (history != null)
                                history.endRecord();
                        }
                    }
                }
            }
        }
        
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
        
        boolean dndAllowed = defaults.getBoolean(DnDAllowedKey); 
        installEventHandler(jtc, dndAllowed);
    }

    public void uninstallUI(JComponent c) 
    {
        JTModuleContainer jtc = (JTModuleContainer) c;
        //JTContext context = jtc.getContext();
        // UIDefaults defaults = context.getUIDefaults();
        
        uninstallEventHandler(jtc);
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
    }
    
    private transient Rectangle dndBox;
    private transient Point dndInitialScrollLocation;
    
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
        	getModuleContainer().repaintOverlay(repaint);
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
        }
        else
        {
            if (dndInitialScrollLocation == null)
            {
                Rectangle r = 
                    getModuleContainer()
                    .getVisibleRect();
                
                dndInitialScrollLocation = r.getLocation();
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
            }
            
            getModuleContainer().scrollRectToVisible(dndBox);
        }
    }
    
    private void installEventHandler(JTModuleContainer jtc, boolean dndAllowed)
    {
        createEventHandler(jtc, dndAllowed);
    }

    private void uninstallEventHandler(JTModuleContainer jtc)
    {
        EventHandler handler = lookupEventHandler(jtc);
        if (handler != null) 
            handler.uninstall();
    }
    
    protected Object EventHandlerKey()
    {
        return "JTModuleContainerDnDHandler";
    }

    protected EventHandler createEventHandler(JTModuleContainer jtc, boolean dndAllowed)
    {
        EventHandler handler = new EventHandler(this, dndAllowed);
        jtc.putClientProperty(EventHandlerKey(), handler);
        return handler;
    }
    
    protected EventHandler lookupEventHandler(JTModuleContainer jtc)
    {
        Object obj = jtc.getClientProperty(EventHandlerKey());
        if (obj != null && (obj instanceof EventHandler))
            return (EventHandler) obj;
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
        private EventHandler delegate;
        private Point dragStartLocation;

        public ModuleTransferDataWrapper(EventHandler delegate, Point dragStartLocation)
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
            DataFlavor[] flavors = {JTDragDrop.ModuleSelectionFlavor};
            return flavors;
        }

        public boolean isDataFlavorSupported(DataFlavor flavor)
        {
            // DropTarget gives his flavors. DropSource looks if it can be dropped on the target.
            return flavor != null && flavor.equals(JTDragDrop.ModuleSelectionFlavor);
        }
    }
    
    public static class EventHandler
      implements ContainerListener, 
      DropTargetListener, DragGestureListener, DragSourceListener,
      MouseListener
    {

        private JTModuleContainerUI jtcUI;
        private boolean dndAllowed;

        public EventHandler(JTModuleContainerUI jtcUI, boolean dndAllowed)
        {
            this.dndAllowed = dndAllowed;
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
            
            if (dndAllowed)
            {
                for (int i=jtc.getComponentCount()-1;i>=0;i--)
                {
                    Component component = jtc.getComponent(i);
                    if (installsAtChild(component))
                        installAtChild(component);
                }   
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

            if (dndAllowed)
            {
                for (int i=jtc.getComponentCount()-1;i>=0;i--)
                {
                    Component component = jtc.getComponent(i);
                    if (installsAtChild(component))
                        uninstallAtChild(component);
                }
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

        protected DropTargetListener createDropTargetListener()
        {
            return this;
        }
        
        protected void installAtModuleContainer(JTModuleContainer jtc)
        {
            jtc.addMouseListener(this);
            if (dndAllowed)
            {
                jtc.addContainerListener(this);
                dropTargetListener = createDropTargetListener();
                moduleContainerDropTarget = new DropTarget(jtc, dndActions, dropTargetListener, true);
            }
        }

        protected void uninstallAtModuleContainer(JTModuleContainer jtc)
        {
            jtc.removeMouseListener(this);
            if (dndAllowed)
            {
                jtc.removeContainerListener(this);
            }
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
        
        protected JTModuleContainer getContainer()
        {
            return getModuleContainer();
        }

        protected boolean isMDDropOk(int action, Transferable t)
        {
            return
            (action & DnDConstants.ACTION_COPY) >0
            && getContainer().getModuleContainer() != null
            && JTDragDrop.isModuleDescriptorFlavorSupported(t);
        }
        
        public void dragEnter(DropTargetDragEvent dtde)
        {
        	if (isMDDropOk(dtde.getDropAction(), dtde.getTransferable()))
            {
                dtde.acceptDrag(DnDConstants.ACTION_COPY);
                return;
            }
            
            if (dtde.getTransferable().isDataFlavorSupported(JTDragDrop.ModuleSelectionFlavor))
            {
                dtde.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
                return;
            }

            dtde.rejectDrag();
            
            // no op
            //dragOver(dtde);
        }

        public void dragExit(DropTargetEvent dte)
        {
            jtcUI.updateDnDBoundingBox(null);
        }

        public void dragOver(DropTargetDragEvent dtde)
        {
            if (isMDDropOk(dtde.getDropAction(), dtde.getTransferable()))
            {
                dtde.acceptDrag(DnDConstants.ACTION_COPY);
                return ;
            }
            
            /*if (dtde.getCurrentDataFlavorsAsList().contains(ModuleDragSource.ModuleInfoFlavor))
            {
                dtde.acceptDrag(DnDConstants.ACTION_COPY);
            }
            else*/
            if (dtde.getCurrentDataFlavorsAsList().contains(JTDragDrop.ModuleSelectionFlavor))
            {
                dtde.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);

                ModuleTransferData data;
                try
                {
                    data = (ModuleTransferData) dtde.getTransferable().getTransferData(JTDragDrop.ModuleSelectionFlavor);

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
            

            if (isMDDropOk(dtde.getDropAction(), dtde.getTransferable()))
            {
                PModuleContainer mc = getModuleContainer().getModuleContainer();
                PModuleDescriptor md = JTDragDrop.getModuleDescriptor(dtde.getTransferable());
                if (md == null || mc == null)
                {
                    dtde.rejectDrop();
                    return;
                }
                
                Point l = dtde.getLocation();
                
                PModule module;
                try
                {
                    module = mc.createModule(md);
                    module.setScreenLocation(l.x, l.y);
                }
                catch (InvalidDescriptorException e)
                {
                    e.printStackTrace();
                    dtde.rejectDrop();
                    return;
                }
                mc.add(module);
                // TODO short after dropping a new module and then moving it
                // causes a NullPointerException in the next line
                MoveOperation move = module.getParentComponent().createMoveOperation();
                move.setScreenOffset(0, 0);
                move.add(module);
                move.move();
       
                dtde.acceptDrop(DnDConstants.ACTION_COPY);
         
                // compute dimensions of container
                jtcUI.jtc.updateModuleContainerDimensions();
                
                return;
            }
            
            if (dtde.isDataFlavorSupported(JTDragDrop.ModuleSelectionFlavor)
                    && dtde.isLocalTransfer())
            {
                chosen = JTDragDrop.ModuleSelectionFlavor;


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
            
            JTModuleContainer jtmc = getModuleContainer();
            PModuleContainer mc = jtmc.getModuleContainer();
            
            MoveOperation moveop = mc.createMoveOperation();

            JTModule[] modules = tdata.getModules();
            for (JTModule jtmodule: modules)
                moveop.add(jtmodule.getModule());
            
            moveop.setScreenOffset(p.x, p.y);
            
            moveop.move();
            
            Collection<? extends PModule> moved = moveop.getMovedModules();
                        
            int maxx = 0;
            int maxy = 0;
            
            for (JTModule jtmodule: NmSwingUtilities.getChildren(JTModule.class, jtmc))
            {
                PModule module = jtmodule.getModule();
                if (moved.contains(module))
                {
                    jtmodule.setLocation(module.getScreenLocation());

                    maxx = Math.max(jtmodule.getX(), maxx)+jtmodule.getWidth();
                    maxy = Math.max(jtmodule.getY(), maxy)+jtmodule.getHeight();
                }
            }
            
            jtmc.updateModuleContainerDimensions();
        }

        public void dropActionChanged(DropTargetDragEvent dtde)
        {
            // no op
            if (isMDDropOk(dtde.getDropAction(), dtde.getTransferable()))
            {
                dtde.acceptDrag(DnDConstants.ACTION_COPY);
                return;
            }
            
            if (dtde.getTransferable().isDataFlavorSupported(JTDragDrop.ModuleSelectionFlavor))
            {
                dtde.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
                return;
            }
            
            dtde.rejectDrag();
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
        	if (dndAllowed)
            {
                if (e.getComponent() == getModuleContainer())
                    mouseClickedAtModuleContainer(e);
            }
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
        	JTModuleContainer mc = jtcUI.getModuleContainer();
            if (SwingUtilities.isRightMouseButton(e) && e.getComponent() == mc)
            {
                jtcUI.createPopupMenu(mc, e);
            }
        }

        public void mouseReleased(MouseEvent e)
        {
            if (e.getComponent() instanceof JTModule)
                mouseClickedAtModule(e);
        }
        
    }
    
}
