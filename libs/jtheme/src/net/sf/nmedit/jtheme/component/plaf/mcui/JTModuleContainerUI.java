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
import java.awt.Image;
import java.awt.MouseInfo;
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
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.plaf.ComponentUI;

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleContainer;
import net.sf.nmedit.jpatch.PModuleDescriptor;
import net.sf.nmedit.jpatch.PPatch;
import net.sf.nmedit.jpatch.dnd.ModulesBoundingBox;
import net.sf.nmedit.jpatch.dnd.PDragDrop;
import net.sf.nmedit.jpatch.dnd.PModuleTransferData;
import net.sf.nmedit.jpatch.dnd.PModuleTransferDataWrapper;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.JTException;
import net.sf.nmedit.jtheme.component.JTLayerRoot;
import net.sf.nmedit.jtheme.component.JTModule;
import net.sf.nmedit.jtheme.component.JTModuleContainer;
import net.sf.nmedit.jtheme.component.plaf.PaintableSelection;
import net.sf.nmedit.jtheme.component.plaf.PaintableTransfer;
import net.sf.nmedit.jtheme.component.plaf.SelectionPainter;
import net.sf.nmedit.jtheme.util.ModuleImageRenderer;
import net.sf.nmedit.nmutils.Platform;
import net.sf.nmedit.nmutils.dnd.FileDnd;
import net.sf.nmedit.nmutils.swing.ApplicationClipboard;

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
        popup.addSeparator();
        getModuleContainer().installModulesMenu(popup);
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
                PaintableSelection old = paintableSelection;
                paintableSelection = null;
                old.repaint(jtc);
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

            KeyStroke escape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);	 
            map.put(escape, ContainerAction.ABORT_PASTE);

            KeyStroke selectAllKey;
            if (Platform.isFlavor(Platform.OS.MacOSFlavor)) {
            	selectAllKey = KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.META_DOWN_MASK);
            } else {
            	selectAllKey = KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK);
            }
            map.put(selectAllKey, ContainerAction.SELECT_ALL);
            
            if (Platform.isFlavor(Platform.OS.MacOSFlavor)) {
            	map.put(KeyStroke.getKeyStroke("meta X"), ContainerAction.CUT);
            	map.put(KeyStroke.getKeyStroke("meta C"), ContainerAction.COPY);
            	map.put(KeyStroke.getKeyStroke("meta V"), ContainerAction.PASTE);
            } else {
            	map.put(KeyStroke.getKeyStroke("ctrl X"), ContainerAction.CUT);
            	map.put(KeyStroke.getKeyStroke("ctrl C"), ContainerAction.COPY);
            	map.put(KeyStroke.getKeyStroke("ctrl V"), ContainerAction.PASTE);
            }
        }	 
	 
        public void installKeyboardActions( JTModuleContainer mc)	 
        {	 
//            NMLazyActionMap.installLazyActionMap(module.getContext().getUIDefaults(),	 
//                    module, BasicEventHandler.class, moduleActionMapKey);	 

        	ActionMap map = mc.getActionMap();
        	map.put(ContainerAction.DELETE, new ContainerAction(mc, ContainerAction.DELETE));
        	map.put(ContainerAction.SELECT_ALL, new ContainerAction(mc, ContainerAction.SELECT_ALL));
        	map.put(ContainerAction.COPY, new ContainerAction(mc, ContainerAction.COPY, ApplicationClipboard.getApplicationClipboard()));
        	map.put(ContainerAction.CUT, new ContainerAction(mc, ContainerAction.CUT, ApplicationClipboard.getApplicationClipboard()));
        	map.put(ContainerAction.PASTE,
        	        new ContainerAction(mc, ContainerAction.PASTE, ApplicationClipboard.getApplicationClipboard()));
        	map.put(ContainerAction.ABORT_PASTE, new ContainerAction(mc, ContainerAction.ABORT_PASTE));
        	
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
            PModuleContainer pmc = getContainer().getModuleContainer();
            boolean isOKPass1 =
            (action & DnDConstants.ACTION_COPY) >0
            && pmc != null
            && PDragDrop.isModuleDescriptorFlavorSupported(t);
            
            if (isOKPass1)
            {
                PModuleDescriptor md = PDragDrop.getModuleDescriptor(t);
                if (pmc.canAdd(md))
                    return true;
            }
            
            return false;
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
                                zeroAlign(newMc);
                                Image transferImage = null;
                                try
                                {
                                    transferImage = ModuleImageRenderer.createImage(
                                        jtcUI.jtc, newMc, true, false, true);
                                }
                                catch (JTException jte)
                                {
                                   transferImage = null; 
                                }
                                
    			                jtcUI.setCurrentTransfer(newMc.getModules(), transferImage);
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
        	
        	if (isMDDropOk(dtde.getDropAction(), t)) 
            {
                jtcUI.setPaintableSelection(PaintableTransfer.get(t)); // get might return null
        		dtde.acceptDrag(DnDConstants.ACTION_COPY);
        	} 
            else if (t.isDataFlavorSupported(PDragDrop.ModuleSelectionFlavor)) {
        		dtde.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
        	} else if (FileDnd.testFileFlavor(t.getTransferDataFlavors())) {
        		dtde.acceptDrag(DnDConstants.ACTION_COPY);
        	} else {
                dtde.rejectDrag();
            }
        }

        private void zeroAlign(PModuleContainer mc)
        {
            // moves all modules to origin (0, 0) without changing the location relative to each other
            List<PModule> slist = new ArrayList<PModule>(mc.getModuleCount());
            Point min = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
            for (PModule module: mc)
            {
                min.x = Math.min(min.x, module.getScreenX());
                min.y = Math.min(min.y, module.getScreenY());
                slist.add(module);
            }
            // sort modules ascending so moving them does not cause a revalidation of the layout
            Collections.sort(slist, new ZeroAlignOrder());
            for (PModule module: slist)
            {
                module.setScreenLocation(
                        module.getScreenX()-min.x,
                        module.getScreenY()-min.y
                );
            }
        }
        
        private static class ZeroAlignOrder implements Comparator<PModule>
        {

            public int compare(PModule a, PModule b)
            {
                int c;
                // first compare by column
                c = b.getScreenX()-a.getScreenX();
                if (c != 0) return Integer.signum(c);
                // then compare by row
                c = b.getScreenY()-a.getScreenY();
                return Integer.signum(c);
            }
            
        }

        public void dragExit(DropTargetEvent dte)
        {
            jtcUI.setPaintableSelection(null);
            jtcUI.setCurrentTransfer(null, null);
        }

        public void dragOver(DropTargetDragEvent dtde)
        {
        	if (isMDDropOk(dtde.getDropAction(), dtde.getTransferable())) {
                
                PaintableSelection ps = jtcUI.paintableSelection;
                if (ps != null)
                {
                    ps.repaint(jtcUI.jtc); // old location
                    ps.bounds.setLocation(dtde.getLocation());
                    ps.repaint(jtcUI.jtc); // new location
                }
                
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
                        jtcUI.paintDragOver(jtcUI.getCurrentTransfer(), dtde.getLocation());
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
            		jtcUI.paintDragOver(jtcUI.getCurrentTransfer(), dtde.getLocation());
            		return;
            	}
            }
    	

            jtcUI.updateDnDBoundingBox(null);

            dtde.rejectDrag();       
        }

        public void drop(DropTargetDropEvent dtde)
        {
            jtcUI.setCurrentTransfer(null, null);
            jtcUI.setPaintableSelection(null);
            
            JTModuleContainer jtmc = jtcUI.getModuleContainer();
            
            Transferable transfer = dtde.getTransferable();

            if (isMDDropOk(dtde.getDropAction(), dtde.getTransferable()))
            {
            	jtmc.dropNewModule(dtde);
            } else if (dtde.isDataFlavorSupported(PDragDrop.ModuleSelectionFlavor)
                    && dtde.isLocalTransfer())
            {
            	jtmc.copyMoveModules(dtde);
            } else if (FileDnd.testFileFlavor(transfer.getTransferDataFlavors())) {
            	jtmc.dropPatchFile(dtde);
            } else {
                dtde.rejectDrop();
                dtde.dropComplete(false);
            }
            jtcUI.updateDnDBoundingBox(null);
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
        	Component src = dge.getComponent();
        	if (src instanceof JTModule) {
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
                mir.setForDragAndDrop(true);
                mir.setPaintExtraBorder(true);
                transfer.setTransferImage(mir.render());
                
                jtcUI.setCurrentTransfer(transfer.getModules(), transfer.getDragStartLocation(), transfer.getTransferImage());
                dge.startDrag(DragSource.DefaultMoveDrop, transfer, this);
            }
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

            boolean invalid = false;
            
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
                case DRAG_INVALID: invalid = true; break;
                default: break;
            }
            
            if (invalid)
            {
                if ((context.getSourceActions() & DnDConstants.ACTION_COPY)>0)
                    context.setCursor(DragSource.DefaultCopyNoDrop);
                else if ((context.getSourceActions() & DnDConstants.ACTION_MOVE)>0)
                    context.setCursor(DragSource.DefaultMoveNoDrop);
                else if ((context.getSourceActions() & DnDConstants.ACTION_LINK)>0)
                    context.setCursor(DragSource.DefaultLinkNoDrop);
                else
                    context.setCursor(DragSource.DefaultCopyNoDrop);
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
            //jtcUI.setPaintableSelection(null); // 
        }

        public void dragOver(DragSourceDragEvent dsde)
        {/*
            PaintableSelection ps = jtcUI.paintableSelection;
            if (ps != null)
            {
                ps.repaint(jtcUI.jtc);
                ps.bounds.setLocation(dsde.getLocation());
                ps.repaint(jtcUI.jtc);
            }
            */
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
        	if (jtcUI.isPasting) {
        		jtcUI.abortPaste();
        		return;
        	}
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
//			if (jtcUI.isPasting) {
//	        	jtcUI.updateScrollPosition(e.getPoint());
//	        	paintDragOver(jtcUI.getCurrentTransfer(), e.getPoint());
//			}
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

    private transient Rectangle cachedRectangle;
    
    private void paintDragOver(ModulesBoundingBox modulesBoundingBox, Point p)
    {
        Rectangle box = (cachedRectangle = modulesBoundingBox.getBoundingBox(cachedRectangle));
        Point o = modulesBoundingBox.getDragStartLocation();
//        Point p = new Point(dtde.getLocation());
                 
        p.x =p.x-o.x;
        p.y =p.y-o.y;

        box.x += p.x;
        box.y += p.y;

        updateDnDBoundingBox(box);
    }


	protected class PasteEventHandler implements MouseListener, MouseMotionListener, FocusListener {
		protected JTModuleContainerUI jtcUI;
		
		public PasteEventHandler(JTModuleContainerUI ui) {
			this.jtcUI = ui;
		}
		
		public void mouseClicked(MouseEvent e) {
			jtcUI.pasteAt(e.getPoint());
		}

		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void mouseMoved(MouseEvent e) {
            jtcUI.paintDragOver(jtcUI.getCurrentTransfer(), e.getPoint());
		}

		Object previousOwner = null;


		public void focusGained(FocusEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void focusLost(FocusEvent e) {
    		jtcUI.abortPaste();
			
		}

	}

    private boolean isPasting = false;
    PasteEventHandler handler = null;
    PModuleTransferDataWrapper pasteTransfer = null;

    public void pasteAt(Point p) {
    	getModuleContainer().copyModules(pasteTransfer, p);
    	abortPaste();
    }
    
	public void abortPaste() {
		if (isPasting) {
            updateDnDBoundingBox(null);

			JTLayerRoot root = getModuleContainer().getLayerRoot();
			if (handler != null) {
				root.removeMouseListener(handler);
				root.removeMouseMotionListener(handler);
			}
			getModuleContainer().getLayerRoot().ignoreMouseEvents();
			isPasting = false;
			pasteTransfer = null;
		}
		
	}
	
	public void startPaste(PModuleTransferDataWrapper transfer) {
		isPasting = true;
		if (handler == null) {
			handler = new PasteEventHandler(this);
			getModuleContainer().addFocusListener(handler);
		}
		pasteTransfer = transfer;

//		FocusManager focusManager = FocusManager.getCurrentManager();
//        focusManager.addPropertyChangeListener(handler);
        
        getModuleContainer().requestFocus();
        
		JTLayerRoot root = getModuleContainer().getLayerRoot();
		root.addMouseListener(handler);
		root.addMouseMotionListener(handler);
		root.captureMouseEvents();
        
		
		Point onScreen = MouseInfo.getPointerInfo().getLocation();
		SwingUtilities.convertPointFromScreen(onScreen, getModuleContainer());
        setCurrentTransfer(transfer.getModules(), new Point(0, 0), transfer.getTransferImage());
		paintDragOver(getCurrentTransfer(), onScreen);
	}
}
