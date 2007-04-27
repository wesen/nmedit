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
package net.sf.nmedit.jtheme.editor;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.JTException;
import net.sf.nmedit.jtheme.component.JTComponent;

public class ComponentDragDrop
{

    private static final int AcceptedActions = DnDConstants.ACTION_COPY;
    
    private static DataFlavor cachedDataFlavor;
    public static DataFlavor getDataFlavor()
    {
        if (cachedDataFlavor == null)
        {
            try
            {
                cachedDataFlavor = 
                    new DataFlavor( 
                      DataFlavor.javaJVMLocalObjectMimeType
                      +";class="+Class.class.getName()
                    );
            }
            catch (ClassNotFoundException e)
            {
                // no op
            }
        }
        
        return cachedDataFlavor;
    }

    private ComponentView componentView;
    private JComponent target;
    private JList source;
    private Point dropLocation = new Point(0,0);

    public ComponentDragDrop(JList compList, ComponentView componentView)
    {
        this.source = compList;
        this.componentView = componentView;
        this.target = componentView.getSink();
        
        install();
    }
    
    public JComponent getSink()
    {
        return componentView.getSink();
    }
    
    public boolean isDropAllowed()
    {
        return componentView.getView() != null;
    }
    
    public JComponent getView()
    {
        return componentView.getView();
    }
    
    public JTContext getContext()
    {
        JComponent v = componentView.getView();
        if (v == null || (!(v instanceof JTComponent)))
        {
            return null;
        }
        return ((JTComponent)v).getContext();
    }
    
    private void install()
    {
        DropTargetListener dtListener = new DTListener(this, target);
        DropTarget dropTarget = new DropTarget(target, 
                AcceptedActions,
                dtListener,
                true);
        
        target.setDropTarget(dropTarget);
        target.setTransferHandler(new ClassTransferHandler(this));
        source.setDragEnabled(true);
        source.setTransferHandler(new ClassTransferHandler(this));
    }
    
    private static class DTListener implements DropTargetListener 
    {
        private ComponentDragDrop dd;
        private JComponent target;

        public DTListener(ComponentDragDrop dd, JComponent target)
        {
            this.dd = dd;
            this.target = target;
        }

        public void dragEnter(DropTargetDragEvent e) 
        {
          if(!isDragOk(e)) 
          {
              e.rejectDrag();      
              return;
          }
          /*DropLabel.this.borderColor=Color.green;      
          showBorder(true);*/
          e.acceptDrag(AcceptedActions);
        }

        public void dragOver(DropTargetDragEvent e) 
        {
          if(!isDragOk(e)) 
          {
              e.rejectDrag();      
              return;
          }
          e.acceptDrag(AcceptedActions);      
        }
        
        public void dropActionChanged(DropTargetDragEvent e) 
        {
          if(!isDragOk(e)) 
          {
              e.rejectDrag();      
              return;
          }
          e.acceptDrag(AcceptedActions);      
        }

        public void dragExit(DropTargetEvent e) 
        {
            // no op
        }

        private boolean isDragOk(DropTargetDragEvent e) 
        {
            if (!dd.isDropAllowed())
                return false;
            
            if (!e.isDataFlavorSupported(ComponentDragDrop.getDataFlavor()))
                return false;

            return ((e.getSourceActions() & AcceptedActions) != 0);
          }

        public void drop(DropTargetDropEvent dtde)
        {
            if ((!dd.isDropAllowed()) || ((dtde.getDropAction()&AcceptedActions)==0))
            {
                dtde.rejectDrop();
                return;
            }

            dd.setDropLocation(dtde.getLocation());
            
            TransferHandler transferHandler = target.getTransferHandler();
            if (!transferHandler.importData(target, dtde.getTransferable()))
            {
                dtde.rejectDrop();
                return;
            }
            
            dtde.acceptDrop(AcceptedActions);
        }
    }
    
    private static class ClassTransferable implements Transferable
    {
        
        private Class<?> transferData;

        public ClassTransferable()
        {
            this(null);
        }

        public ClassTransferable(Class<?> transferData)
        {
            this.transferData = transferData;
        }

        public Class getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
        {
            if (!isDataFlavorSupported(flavor))
                throw new UnsupportedFlavorException(flavor);
            
            return transferData;
        }

        public DataFlavor[] getTransferDataFlavors()
        {
            return new DataFlavor[] {ComponentDragDrop.getDataFlavor()};
        }

        public boolean isDataFlavorSupported(DataFlavor flavor)
        {
            return ComponentDragDrop.getDataFlavor().equals(flavor);
        }
        
    }
    
    private static class ClassTransferHandler extends TransferHandler
    {
        private ComponentDragDrop dd;

        public ClassTransferHandler(ComponentDragDrop dd)
        {
            this.dd = dd;
        }
        
        public int getSourceActions(JComponent c) 
        {
            return AcceptedActions;
        }
        
        protected Transferable createTransferable(JComponent c) 
        {
            if (!(c instanceof JList))
                return null;
            JList list = (JList) c;
            Object oSelection = list.getSelectedValue();
            
            if (!(oSelection instanceof Class))
                return null;
            
            Class<?> transferClass = (Class) oSelection;
            return new ClassTransferable(transferClass);
        }
        
        public boolean importData(JComponent comp, Transferable t) 
        {
            final DataFlavor dataFlavor = ComponentDragDrop.getDataFlavor();
            
            if (!t.isDataFlavorSupported(dataFlavor))
                return false;
            
            try
            {
                Object classObj = t.getTransferData(dataFlavor);
                
                if (!(classObj instanceof Class))
                {
                    assert false : "not a class: "+classObj;
                    return false;
                }
                Class clazz = (Class) classObj;
                
                if (!JComponent.class.isAssignableFrom(clazz))
                {
                    return false;
                }
                
                return importClass(comp, clazz);
            }
            catch (UnsupportedFlavorException e)
            {
                assert false : e;
            }
            catch (IOException e)
            {
                assert false : e;
            }
            return false;
        }

        public <T extends JTComponent> boolean importClass(JComponent comp, Class<T> clazz)
        {
            JTContext context = dd.getContext();
            if (context == null)
                return false;
            
            JTComponent component;
            try
            {
                component = context.createComponentInstance(clazz);
            }
            catch (JTException e)
            {
                e.printStackTrace();
                return false;
            }
            
            setPreferredSize(component);
            JComponent target = dd.getView();
            target.add(component, 0);
            ComponentView.setLocation(component, dd.getDropLocation());
            
            dd.getSink().repaint(component.getX(), component.getY(), 
                    component.getWidth(), component.getHeight());
            return true;
        }
    }

    private static void setPreferredSize(JTComponent component)
    {
        Dimension d = component.getPreferredSize();
        if (d != null && d.width>0 && d.height>0)
            component.setSize(d);
        else
            component.setSize(20, 20);
    }
    
    public void setDropLocation(Point location)
    {
        dropLocation.setLocation(location);
    }

    public Point getDropLocation()
    {
        return dropLocation;
    }
    
}

