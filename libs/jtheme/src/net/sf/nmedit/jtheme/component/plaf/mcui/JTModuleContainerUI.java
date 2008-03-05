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
package net.sf.nmedit.jtheme.component.plaf.mcui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceContext;
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
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.plaf.ComponentUI;

import net.sf.nmedit.jpatch.CopyOperation;
import net.sf.nmedit.jpatch.InvalidDescriptorException;
import net.sf.nmedit.jpatch.MoveOperation;
import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleContainer;
import net.sf.nmedit.jpatch.PModuleDescriptor;
import net.sf.nmedit.jpatch.PPatch;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.cable.Cable;
import net.sf.nmedit.jtheme.cable.JTCableManager;
import net.sf.nmedit.jtheme.component.JTModule;
import net.sf.nmedit.jtheme.component.JTModuleContainer;
import net.sf.nmedit.jtheme.component.plaf.PaintableSelection;
import net.sf.nmedit.jtheme.component.plaf.SelectionPainter;
import net.sf.nmedit.jpatch.dnd.ModulesBoundingBox;
import net.sf.nmedit.jpatch.dnd.PDragDrop;
import net.sf.nmedit.jpatch.dnd.PModuleTransferData;
import net.sf.nmedit.jpatch.dnd.PModuleTransferDataWrapper;
import net.sf.nmedit.jpatch.history.PUndoableEditSupport;
import net.sf.nmedit.nmutils.Platform;
import net.sf.nmedit.nmutils.dnd.FileDnd;
import net.sf.nmedit.nmutils.swing.NmSwingUtilities;

import net.sf.nmedit.jtheme.component.plaf.mcui.ContainerAction;
import net.sf.nmedit.jtheme.util.ModuleImageRenderer;

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
        popup.add(new ContainerAction(this.getModuleContainer(), ContainerAction.DELETE_UNUSED));
        popup.show(mc, e.getX(), e.getY());
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
        // nothing happens here
    }

    public void paintChildrenHack(Graphics g)
    {
        if (hasPaintableSelection()) 
        {
            paintableSelection.paint(jtc, g);
            /*
        	Rectangle r = selectRectangle;
            SelectionPainter.paintSelectionBox(g, 
                    r.x, r.y, r.width, r.height);
                    */
        }    

        Rectangle box = dndBox;
        if (box != null)
        {
                ModulesBoundingBox transfer = getCurrentTransfer();
                if (transfer != null && !transfer.getModules().isEmpty())
                {
                    Image transferImage = transfer.getTransferImage();
                	Rectangle bbox = transfer.getBoundingBox();

                    if (transferImage != null)
                    {
                        g.drawImage(transferImage, 
                            box.x, box.y, null);
                    }
                    else
                    {
                        SelectionPainter.paintPModuleSelectionBox(g, 
                                transfer.getModules(), 
                                box.x - bbox.x, box.y - bbox.y);
                    }
                }
        }
    }
    
    private transient Rectangle dndBox;
    private transient Point dndInitialScrollLocation;
	protected EventHandler eventHandler;
	private ModulesBoundingBox currentTransferModules;
    
    private PaintableSelection paintableSelection = null;
    
    private boolean hasPaintableSelection()
    {
        return paintableSelection != null;
    }
    
    private void setPaintableSelection(PaintableSelection ps)
    {
        if (ps != paintableSelection)
        {
            if (ps == null)
            {
                // repaint and remove
                paintableSelection.repaint(jtc);
                paintableSelection = null;
            }
            else
            {
                paintableSelection = ps;
            }
        }
    }
    
    
	/*public boolean selectBoxActive;
	public Point selectStartPoint;
	public Rectangle selectRectangle;*/
    
	private void setCurrentTransfer(Collection<? extends PModule> modules, Image transferImage) {
		setCurrentTransfer(modules, new Point(5, 5), transferImage);
	}
    
    private void setCurrentTransfer(Collection<? extends PModule> modules, Point dragPoint, Image transferImage)
    {
    	if (modules != null) {
    		this.currentTransferModules = new ModulesBoundingBox(modules, dragPoint);
            this.currentTransferModules.setTransferImage(transferImage);
    	} else {
    		this.currentTransferModules = null;
    	}
        updateDnDBoundingBox(null);
    }
    
    private ModulesBoundingBox getCurrentTransfer()
    {
        return currentTransferModules;
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
                
            }
            
            getModuleContainer().setPreferredSize(null);            
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

//            if (dndBox.x<0) dndBox.x = 0;
//            if (dndBox.y<0) dndBox.y = 0;

            int r = dndBox.x + dndBox.width +140;
            int b = dndBox.y + dndBox.height+140;
            
            if (r>getModuleContainer().getWidth()
            || b>getModuleContainer().getHeight()
            ) 
            {
                getModuleContainer().setPreferredSize(new Dimension(r, b));
                getModuleContainer().invalidate();
            }
            
//            getModuleContainer().scrollRectToVisible(dndBox);
            
            //getModuleContainer().scrollRectToVisible(dndBox);
        }

    }
    
    private void installEventHandler(JTModuleContainer jtc, boolean dndAllowed)
    {
        eventHandler = createEventHandler(jtc, dndAllowed);
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
    
    public static class EventHandler
      implements ContainerListener, 
      DropTargetListener, DragGestureListener, DragSourceListener,
      MouseListener, MouseMotionListener
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
        
        private transient InputMap inputMapWhenFocused ;	 
        protected InputMap createInputMapWhenFocused()	 
        {	 
            if (inputMapWhenFocused == null)	 
            {	 
                inputMapWhenFocused = new InputMap();	 
                fillInputMap(inputMapWhenFocused);	 
            }	 
            return inputMapWhenFocused;	 
        }	 
	 
        protected void fillInputMap(InputMap map)	 
        {	 
            int vk_delete = KeyEvent.VK_DELETE;	 
	 
            if (Platform.flavor() == Platform.OS.MacOSFlavor)	 
                vk_delete = KeyEvent.VK_BACK_SPACE;	 
	 
            KeyStroke deleteModules = KeyStroke.getKeyStroke(vk_delete, 0);	 
            map.put(deleteModules, ContainerAction.DELETE);
            
            KeyStroke selectAllKey;
            if (Platform.isFlavor(Platform.OS.MacOSFlavor)) {
            	selectAllKey = KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.META_DOWN_MASK);
            } else {
            	selectAllKey = KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK);
            }
            map.put(selectAllKey, ContainerAction.SELECT_ALL);
        }	 
	 
        public void installKeyboardActions( JTModuleContainer mc)	 
        {	 
//            NMLazyActionMap.installLazyActionMap(module.getContext().getUIDefaults(),	 
//                    module, BasicEventHandler.class, moduleActionMapKey);	 

        	mc.getActionMap().put(ContainerAction.DELETE, new ContainerAction(mc, ContainerAction.DELETE));
        	mc.getActionMap().put(ContainerAction.SELECT_ALL, new ContainerAction(mc, ContainerAction.SELECT_ALL));
            InputMap im = createInputMapWhenFocused();	 
            SwingUtilities.replaceUIInputMap(mc, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, im);	 
        }	 
	 
        public void uninstallKeyboardActions(JTModuleContainer mc)	 
        {	 
            SwingUtilities.replaceUIInputMap(mc, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, null);	 
	 
            // TODO this line shouldn't be necessary, but if setUI() was called twice	 
            // each time with a new ui instance then the input map will cause a StackOverflowError	 
            // if a key was pressed	 
            mc.setInputMap(JComponent.WHEN_FOCUSED, new InputMap());	 
	 
            SwingUtilities.replaceUIActionMap(mc, null);	 
        }	 

        public void install()
        {
            JTModuleContainer jtc = getModuleContainer();
            installAtModuleContainer(jtc);
                        
            installKeyboardActions(jtc);
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
            uninstallKeyboardActions(jtc);

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
            jtc.addMouseMotionListener(this);
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
            
            DragGestureRecognizer dgr =
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
            && PDragDrop.isModuleDescriptorFlavorSupported(t);
        }
        
        public void dragEnter(DropTargetDragEvent dtde)
        {
        	DataFlavor flavors[] = dtde.getTransferable().getTransferDataFlavors();
        	
            ModulesBoundingBox currentTransfer = jtcUI.getCurrentTransfer();
    		Transferable t = dtde.getTransferable();
            
        	if (currentTransfer == null) {
        		if (t.isDataFlavorSupported(PDragDrop.ModuleSelectionFlavor)) {
					try {
                        PModuleTransferDataWrapper transfer = (PModuleTransferDataWrapper) t.getTransferData(PDragDrop.ModuleSelectionFlavor);
                        jtcUI.setCurrentTransfer(transfer.getModules(), transfer.getDragStartLocation(), transfer.getTransferImage());
					} catch (Throwable e) {
                        jtcUI.setCurrentTransfer(null, null);
					}
        		} else if (FileDnd.testFileFlavor(t.getTransferDataFlavors())) {
                	PPatch patch = getModuleContainer().getPatchContainer().getPatch();
    				DataFlavor fileFlavor = FileDnd.getFileFlavor(t.getTransferDataFlavors());
    				List<File> files = FileDnd.getTransferableFiles(fileFlavor, t);
    				if (files.size() == 1) {
    					PPatch newPatch = patch.createFromFile(files.get(0));
    					if (newPatch != null) {
    						PModuleContainer newMc = null;
    						
    						for (int i = 0; i < newPatch.getModuleContainerCount(); i++) {
    							newMc = newPatch.getModuleContainer(i);
    							if (newMc.getModuleCount() > 0)
    								break;
    						}
    						if (newMc != null) {
    							jtcUI.setCurrentTransfer(newMc.getModules(), null);
    						} else {
    							dtde.rejectDrag();
    							return;
    						}
    					}
    				}
        		} else {
        			// System.out.println("flavor not supported");
        		}
        	}
        	
        	if (isMDDropOk(dtde.getDropAction(), t)) {
        		dtde.acceptDrag(DnDConstants.ACTION_COPY);
        	} else if (t.isDataFlavorSupported(PDragDrop.ModuleSelectionFlavor)) {
        		dtde.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
        	} else if (FileDnd.testFileFlavor(t.getTransferDataFlavors())) {
        		dtde.acceptDrag(DnDConstants.ACTION_COPY);
        	} else {
        		dtde.rejectDrag();
            }
        }

        public void dragExit(DropTargetEvent dte)
        {
            jtcUI.setCurrentTransfer(null, null);
        }

        public void dragOver(DropTargetDragEvent dtde)
        {
        	if (isMDDropOk(dtde.getDropAction(), dtde.getTransferable())) {
                dtde.acceptDrag(DnDConstants.ACTION_COPY);
                return;
            }
            
        	jtcUI.updateScrollPosition(dtde.getLocation());
            
            /*if (dtde.getCurrentDataFlavorsAsList().contains(ModuleDragSource.ModuleInfoFlavor))
            {
                dtde.acceptDrag(DnDConstants.ACTION_COPY);
            }
            else*/
            if (dtde.getCurrentDataFlavorsAsList().contains(PDragDrop.ModuleSelectionFlavor))
            {
                PModuleTransferData data;
                try
                {
               
                    data = (PModuleTransferData) dtde.getTransferable().getTransferData(PDragDrop.ModuleSelectionFlavor);
                    if (data.getSourcePatch() == getModuleContainer().getPatchContainer().getPatch()) {
                    	dtde.acceptDrag(dtde.getDropAction() & (DnDConstants.ACTION_MOVE | DnDConstants.ACTION_COPY));
                    } else {
                    	dtde.acceptDrag(DnDConstants.ACTION_COPY);
                    }

                    if (data!=null)
                    {
                        paintDragOver(jtcUI.getCurrentTransfer(), dtde);
                        return ;
                    }
                }
                catch (Throwable e)
                {
                    ;
                }
            }
            
            if (FileDnd.testFileFlavor(dtde.getTransferable().getTransferDataFlavors())) {
            	dtde.acceptDrag(DnDConstants.ACTION_COPY);
            	if (jtcUI.getCurrentTransfer() != null) { 
            		paintDragOver(jtcUI.getCurrentTransfer(), dtde);
            		return;
            	}
            }
    	

            jtcUI.updateDnDBoundingBox(null);
            
            dtde.rejectDrag();       
        }

        private transient Rectangle cachedRectangle;
        
        private void paintDragOver(ModulesBoundingBox modulesBoundingBox, DropTargetDragEvent dtde)
        {
            Rectangle box = (cachedRectangle = modulesBoundingBox.getBoundingBox(cachedRectangle));
            
            Point o = modulesBoundingBox.getDragStartLocation();
            Point p = new Point(dtde.getLocation());
                     
            p.x =p.x-o.x;
            p.y =p.y-o.y;

            box.x += p.x;
            box.y += p.y;

            jtcUI.updateDnDBoundingBox(box);
        }

        public void drop(DropTargetDropEvent dtde)
        {
            jtcUI.setCurrentTransfer(null, null);
            
            DataFlavor chosen = null;
            Object data = null;

            Transferable transfer = dtde.getTransferable();
            
            if (isMDDropOk(dtde.getDropAction(), dtde.getTransferable()))
            {
                PModuleContainer mc = getModuleContainer().getModuleContainer();
                PModuleDescriptor md = PDragDrop.getModuleDescriptor(dtde.getTransferable());
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
                PModuleContainer parent = module.getParentComponent();
                if (parent != null) {
                    JTCableManager cm = jtcUI.jtc.getCableManager();
                    try
                    {
                        cm.setAutoRepaintDisabled();
                    	MoveOperation move = parent.createMoveOperation();
                    	move.setScreenOffset(0, 0);
                    	move.add(module);
                    	move.move();
                    }
                    finally
                    {
                        cm.clearAutoRepaintDisabled();
                    }
                } else {
                	// XXX concurrency problems probably ?!
                	throw new RuntimeException("Drop problem on illegal modules: for example 2 midi globals");
                }
       
                dtde.acceptDrop(DnDConstants.ACTION_COPY);
         
                // compute dimensions of container
                jtcUI.jtc.revalidate();
                jtcUI.jtc.repaint();
                dtde.dropComplete(true);
            } else if (dtde.isDataFlavorSupported(PDragDrop.ModuleSelectionFlavor)
                    && dtde.isLocalTransfer())
            {
                chosen = PDragDrop.ModuleSelectionFlavor;

                try {
                    // Get the data
                    dtde.acceptDrop(dtde.getDropAction() & (DnDConstants.ACTION_MOVE | DnDConstants.ACTION_COPY));
                    data = transfer.getTransferData(chosen);
                } catch (Throwable t) {
                    t.printStackTrace();
                    dtde.dropComplete(false);
                    return;
                }

                if (data!=null && data instanceof PModuleTransferData)
                {
                    // Cast the data and create a nice module.
                    PModuleTransferData tdata = ((PModuleTransferData)data);
                    boolean isSamePatch = false;
                    if (tdata.getSourcePatch() == getModuleContainer().getPatchContainer().getPatch())
                    	isSamePatch = true;
                    
                    //Point p = dtde.getLocation();

                    int action = dtde.getDropAction();

                    if ((action&DnDConstants.ACTION_MOVE)!=0 && isSamePatch)
                    {
                    	MoveOperation op = tdata.getSourceModuleContainer().createMoveOperation();
                    	op.setDestination(getModuleContainer().getModuleContainer());
                        executeOperationOnSelection(tdata, dtde, op);
                    }
                    else
                    {
                    	MoveOperation op = tdata.getSourceModuleContainer().createCopyOperation();
                    	// check for shift pressed to create links XXX
                    	op.setDestination(getModuleContainer().getModuleContainer());
                        executeOperationOnSelection(tdata, dtde, op);
                    }

                }
                dtde.dropComplete(true);

            } else if (FileDnd.testFileFlavor(transfer.getTransferDataFlavors())) {
            	PPatch patch = getModuleContainer().getPatchContainer().getPatch();
				DataFlavor fileFlavor = FileDnd.getFileFlavor(transfer.getTransferDataFlavors());
				List<File> files = FileDnd.getTransferableFiles(fileFlavor, transfer);
				if (files.size() == 1) {
					PPatch newPatch = patch.createFromFile(files.get(0));
					if (newPatch != null) {
						PModuleContainer newMc = null;
						
						for (int i = 0; i < newPatch.getModuleContainerCount(); i++) {
							newMc = newPatch.getModuleContainer(i);
							if (newMc.getModuleCount() > 0)
								break;
						}
						
						if (newMc == null) {
							dtde.rejectDrop();
							dtde.dropComplete(false);
	                        jtcUI.updateDnDBoundingBox(null);
							return;
						}
                    	CopyOperation op = newMc.createCopyOperation();
                    	op.setDestination(getModuleContainer().getModuleContainer());
                    	for (int i = 0; i < newMc.getModuleCount(); i++) {
                    		op.add(newMc.getModule(i + 1));
                    	}
                        Point p = new Point(dtde.getLocation());
                        op.setScreenOffset(p.x, p.y);
                    	op.copy();
                        dtde.dropComplete(true);
                        jtcUI.updateDnDBoundingBox(null);
                        return;
					}
				}
				dtde.rejectDrop();
				dtde.dropComplete(false);
            } else {
                dtde.rejectDrop();
                dtde.dropComplete(false);
            }
            jtcUI.updateDnDBoundingBox(null);
        }

        private void executeOperationOnSelection(PModuleTransferData tdata, DropTargetDropEvent dtde, MoveOperation op)
        {
            Point o = tdata.getDragStartLocation();
            Point p = new Point(dtde.getLocation());

            p.x = p.x-o.x;
            p.y = p.y-o.y;
            
            JTModuleContainer jtmc = getModuleContainer();
            PUndoableEditSupport ues = jtmc.getModuleContainer().getEditSupport();
            JTCableManager cm = jtmc.getCableManager();
            PModuleContainer mc = jtmc.getModuleContainer();
            
            for (PModule module: tdata.getModules()) {
                op.add(module);
            }
            
            op.setScreenOffset(p.x, p.y);
            
            try
            {
            	cm.setAutoRepaintDisabled();
            	String name = (op instanceof CopyOperation ? "copy modules" : "move modules");
                ues.beginUpdate(name);
                try {
                	op.move();
                } finally {
                    ues.endUpdate();
                }
                
                Collection<? extends PModule> moved = op.getMovedModules();
                            
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
                
                Collection<Cable> cables = new ArrayList<Cable>(20); 
                cm.getCables(cables, moved);
                for (Cable cable: cables)
                {
                    cm.update(cable);
                    cable.updateEndPoints();
                    cm.update(cable);
                }

                jtmc.revalidate();
                jtmc.repaint();
            }
            finally
            {
                cm.clearAutoRepaintDisabled();
            }
        }

        public void dropActionChanged(DropTargetDragEvent dtde)
        {
            // no op
            if (isMDDropOk(dtde.getDropAction(), dtde.getTransferable()))
            {
                dtde.acceptDrag(DnDConstants.ACTION_COPY);
                return;
            }
            
            if (dtde.getTransferable().isDataFlavorSupported(PDragDrop.ModuleSelectionFlavor))
            {
                dtde.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
                return;
            }
            
            dtde.rejectDrag();
        }


        public void dragGestureRecognized(DragGestureEvent dge)
        {
        	JTModuleContainer jtc = getModuleContainer();
            JTModule module = (JTModule) dge.getComponent();
            if (!module.isEnabled()) return;
          
            if (jtc.isSelectionEmpty())
            {
                jtc.addSelection(module);
            }
            else if (jtc.isInSelection(module))
            {
                // thats ok
            }
            else
            {
                if (!jtc.isInSelection(module))
                {
                	jtc.clearSelection();
                	jtc.addSelection(module);
                }
            }

            if (!jtc.isSelectionEmpty())
            {
                Component c = dge.getComponent();
                Point dndOrigin = dge.getDragOrigin();
                // if (c instanceof JTModule) // always true
                {
                    dndOrigin = SwingUtilities.convertPoint(c, dndOrigin, getModuleContainer());
                }
                
                Collection<? extends JTModule> jtmodules = jtc.getSelectedModules();
                Collection<? extends PModule> collection = jtc.getSelectedPModules();

                PModuleTransferDataWrapper transfer =
                    new PModuleTransferDataWrapper(this.getModuleContainer().getModuleContainer(), 
                            collection, dndOrigin);
                
                ModuleImageRenderer mir = new ModuleImageRenderer(jtmodules);
                mir.setPaintExtraBorder(true);
                transfer.setTransferImage(mir.render());
                
                jtcUI.setCurrentTransfer(transfer.getModules(), transfer.getDragStartLocation(), transfer.getTransferImage());
                
                // DragSource.isDragImageSupported() returns false on platforms
                // which support this feature due to some kind of bug in the JRE.
                // http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4874070
                if (DragSource.isDragImageSupported() & false) // disable feature until we are sure it works
                {
//                    Image dragImage = renderDragImage( collection );
//                    Point imageOffset = new Point( dndOrigin );
//                    dge.startDrag(DragSource.DefaultMoveDrop, dragImage, imageOffset, transfer, this);    
                }
                else
                {
                    dge.startDrag(DragSource.DefaultMoveDrop, transfer, this);
                }
            }
        }
        
        private static <T extends JComponent> Image renderDragImage(Collection<T> collection)
        {
            // get boundaries
            Rectangle bounds = new Rectangle(0, 0, 0, 0);
            Rectangle tmp = new Rectangle();
            boolean firstComponent = true;
            boolean nonOpaque = false;
            for (T component: collection)
            {
                // get component bounds
                tmp = component.getBounds(tmp);
                // check if at least one component is not opaque
                nonOpaque |= !component.isOpaque();
                // compute union
                if (firstComponent)
                {
                    bounds.setBounds(tmp); // otherwise x,y is always 0,0
                }
                else
                {
                    SwingUtilities.computeUnion(
                            tmp.x, tmp.y, 
                            tmp.width, tmp.height, 
                            bounds);
                }
            }
            BufferedImage image = new BufferedImage( 
                    bounds.width, bounds.height, // size
                    collection.size() > 1 || nonOpaque
                    ? BufferedImage.TYPE_INT_ARGB  // image has alpha
                    : BufferedImage.TYPE_INT_RGB   // alpha not necessary for single opaque module 
            );
            
            Graphics2D g2 = image.createGraphics();

            try
            {
                
                // translation to image 0,0
                int tx = -bounds.x;
                int ty = -bounds.y;
                
                for (T component: collection)
                {
                    // always use a new graphics instance because
                    // some components cause an illegal state
                    
                    Graphics2D g2c = (Graphics2D) g2.create();
                    try
                    {   
                        g2c.translate(tx+component.getX(), ty+component.getY());
                        synchronized (component.getTreeLock())
                        {
                            g2c.setFont(component.getFont());
                            g2c.setColor(component.getBackground());
                            component.paint(g2c);
                        }
                    }
                    finally
                    {
                        g2c.dispose();
                    }
                }
                
            }
            finally
            {
                g2.dispose();
            }
            return image;
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
            DragSourceContext context = dsde.getDragSourceContext();

            switch (getSourceForEvent(dsde))
            {
                case DRAG_MODULES_CREATE:
                    context.setCursor(DragSource.DefaultLinkDrop);
                    break;
                case DRAG_MODULES_FROM_THIS_CONTAINER:
                    context.setCursor(DragSource.DefaultMoveDrop);
                    break;
                case DRAG_MODULES_FROM_OTHER_CONTAINER:
                    context.setCursor(DragSource.DefaultCopyDrop);
                    break;
                // reject ...
                case DRAG_INVALID: break; 
                default: break;
            }
        }

        static final int DRAG_INVALID = -1; // test for < 0
        static final int DRAG_MODULES_FROM_THIS_CONTAINER = 0;
        static final int DRAG_MODULES_FROM_OTHER_CONTAINER = 1;
        static final int DRAG_MODULES_CREATE = 2;
        protected int getSourceForEvent(DragSourceEvent e)
        {
            DragSourceContext context = e.getDragSourceContext();
            Transferable transfer = context.getTransferable();
            if (transfer instanceof PModuleTransferDataWrapper)
            {
                if (jtcUI.jtc == context.getComponent())
                    return DRAG_MODULES_FROM_THIS_CONTAINER;
                else
                    return DRAG_MODULES_FROM_OTHER_CONTAINER;
            }
            
            if (PDragDrop.isModuleDescriptorFlavorSupported(transfer))
                return DRAG_MODULES_CREATE;
            
            return DRAG_INVALID;
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
            if (Platform.isLeftMouseButtonOnly(e) && !jtcUI.hasPaintableSelection())
                getModuleContainer().clearSelection();
        }
        
        public void mouseClickedAtModule(MouseEvent e)
        {
        	// this is a real dilemma here. THe good way would be to do this through mouseClicked, 
        	// but at least under OSX< this is not reliable.
        	// on mouseReleased is the better way to go, because mousePressed starts a drag and we don't want to deselect on that
        	// so we need to check by hand if a popup menu was opened by this click, because isPopupTrigger is on mousePressed
        	if (Platform.couldBePopupTrigger(e))
                return;
        	
        	JTModuleContainer jtc = getModuleContainer();
        	JTModule module = (JTModule) e.getComponent();
            
            boolean shift = e.isShiftDown();
            if (Platform.isFlavor(Platform.OS.MacOSFlavor)) {
            	// apple key has a different behaviour under osx
            	boolean meta = e.isMetaDown();
            	if (meta && shift)
            		return;
            	
            	if (meta) {
            		if (jtc.isInSelection(module)) {
            			jtc.removeSelection(module);
            		} else {
            			jtc.addSelection(module);
            		}
            		return;
            	}
            } else {
            	boolean ctrl = e.isControlDown();
                if (shift && ctrl) return;
                
            	if (ctrl) { 
            		jtc.removeSelection(module);
            		return;
            	}
            }
            
            if (shift)
            	jtc.addSelection(module);
            else // !(shift||ctrl)
            {
            	jtc.selectOnly(module);
            }
        }
        
        public void mouseClicked(MouseEvent e)
        {
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
            if (e.getComponent() == mc) // do not rob focus from module
            {
                getModuleContainer().requestFocusInWindow();
            }
            if (Platform.isPopupTrigger(e) && e.getComponent() == mc)
            {
                jtcUI.createPopupMenu(mc, e);
            }
            
        }

        public void mouseReleased(MouseEvent e)
        {
            if (dndAllowed)
            {
                if (e.getComponent() == getModuleContainer())
                    mouseClickedAtModuleContainer(e);
            }

            if (e.getComponent() instanceof JTModule)
                mouseClickedAtModule(e);

            JTModuleContainer mc = jtcUI.getModuleContainer();
            if (Platform.isLeftMouseButtonOnly(e) && e.getComponent() == mc && jtcUI.hasPaintableSelection()) {
            	jtcUI.setPaintableSelection(null);
            }
        }
        
        public void mouseDragged(MouseEvent e) {
        	jtcUI.updateScrollPosition(e.getPoint());
        
        	JTModuleContainer mc = jtcUI.getModuleContainer();
        	if (Platform.isLeftMouseButtonOnly(e) && e.getComponent() == mc) {
        		if (!jtcUI.hasPaintableSelection()) {
        			startNewSelectionRectangle(e);
        		}
        		
        		updateSelectionRectangle(e);
        	}
		}

        HashSet<JTModule> oldSelection = null;
        

        
        private void startNewSelectionRectangle(MouseEvent e) {
        	JTModuleContainer mc = jtcUI.getModuleContainer();
            
            PaintableSelection ps = new PaintableSelection();
            ps.start.setLocation(e.getPoint());
            ps.bounds.setBounds(e.getX(), e.getY(), e.getX(), e.getY());
			oldSelection = new HashSet<JTModule>(mc.getSelectedModules());
            jtcUI.setPaintableSelection(ps);
            ps.repaint(jtcUI.jtc);
        }

        private void updateSelectionRectangle(MouseEvent e) {
			Point point = e.getPoint();
            PaintableSelection ps = jtcUI.paintableSelection;
            if (ps == null) return;
            
			Rectangle select = ps.bounds;
    		Point start = ps.start;
    		JTModuleContainer jtc = jtcUI.getModuleContainer();
        	jtc.repaint(select);
    		int x1, x2, y1, y2;
    		x1 = Math.min(start.x, point.x);
    		x2 = Math.max(start.x, point.x);
    		y1 = Math.min(start.y, point.y);
    		y2 = Math.max(start.y, point.y);
    		select.setRect(x1, y1, x2-x1, y2-y1);
    		jtc.repaint(select);
            boolean shift = e.isShiftDown();
            boolean meta = false;
            boolean ctrl = false;
            
            if (Platform.isFlavor(Platform.OS.MacOSFlavor)) {
            	// apple key has a different behaviour under osx
            	meta = e.isMetaDown();
            	if (meta && shift)
            		return;
            } else {
            	ctrl = e.isControlDown();
                if (shift && ctrl) return;
            }
            
    		if (!meta && !shift && !ctrl) {
    			jtc.clearSelection();
    			oldSelection.clear();
    		}
    		Component[] components = jtc.getComponents();
    		for (int i=components.length-1;i>=0;i--)
    		{
    			Component c = components[i];
    			if (c instanceof JTModule)
    			{
    				JTModule mui = (JTModule) c;
    				if (select.intersects(mui.getBounds())) {
    					if (meta) {
    						if (oldSelection.contains(mui)) {
    							jtc.removeSelection(mui);
    						} else {
    							jtc.addSelection(mui);
    						}
    					} else if (ctrl) {
    						if (oldSelection.contains(mui))
    							jtc.removeSelection(mui);
    					} else {
    						jtc.addSelection(mui);
    					}
    				} else {
    		    		if (meta || shift || ctrl) {

    		    			if (oldSelection.contains(mui)) {
    		    				jtc.addSelection(mui);
    		    			} else {
    		    				jtc.removeSelection(mui);
    		    			}
    		    		}
    				}
    			}
    		}

		}

		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub
		}
        
    }

	public void updateScrollPosition(Point location) {
		Rectangle visible = getModuleContainer().getVisibleRect();
		Dimension dim = getModuleContainer().getSize();
//		System.out.println("visible " + visible.width + "x" + visible.height + " dim " + dim.width + "x" + dim.height);
        int wScroll = Math.min(visible.width / 3, 40);
        int hScroll = Math.min(visible.height / 3, 40);
        // System.out.println("w " + wScroll + " h " + hScroll);
        
        Rectangle scrollTo = new Rectangle(location);
        if (location.x < (visible.x + wScroll))
        	scrollTo.x = Math.max(location.x - wScroll, 0);
        if (location.x > (visible.x + visible.width - wScroll))
        	scrollTo.x = location.x + wScroll;
        scrollTo.x = Math.min(scrollTo.x, dim.width);
        
        if (location.y < (visible.y + hScroll))
        	scrollTo.y = Math.max(location.y - hScroll, 0);
        if (location.y > (visible.y + visible.height - hScroll))
        	scrollTo.y = location.y + hScroll;
        scrollTo.y = Math.min(scrollTo.y, dim.height);
        
//         System.out.println("location " + location.x + " " + location.y + " visible " + visible.x + " " + visible.y + " scrollto " + scrollTo.x + " " + scrollTo.y);
        
        getModuleContainer().scrollRectToVisible(scrollTo);
	}
    
}
