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
 * Created on Jun 27, 2006
 */
package net.sf.nmedit.nomad.patch.ui.action;

import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceContext;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;

import net.sf.nmedit.nomad.patch.ui.ModuleSectionUI;
import net.sf.nmedit.nomad.patch.ui.ModuleUI;
import net.sf.nmedit.nomad.patch.ui.drag.ModuleSelectionTool;

public class ModuleSelectionSource implements DragGestureListener, DragSourceListener, Transferable
{

    private final static int dragAction = DnDConstants.ACTION_COPY_OR_MOVE;
    
    public static final DataFlavor ModuleSelectionFlavor = new DataFlavor("nomad/ModuleSelectionFlavor", "Nomad ModuleSelectionFlavor");

    private static final ModuleSelectionSource mss = new ModuleSelectionSource();
    
    private ModuleTransferData currentData = null;
    
    public static void install(ModuleUI source)
    {
        DragSource dragSource = DragSource.getDefaultDragSource();
        dragSource.createDefaultDragGestureRecognizer(source, dragAction, mss);
    }
    
    public void dragGestureRecognized(DragGestureEvent dge) 
    {
        currentData = null;
        ModuleUI m = (ModuleUI) dge.getComponent();
        if (m.isEnabled())
        {
            ModuleSectionUI section = m.getModuleSection();
            
            if (section.getSelected().isEmpty())
            {
                section.getSelected().add(m);
            }
            else if (section.getSelected().contains(m))
            {
                // thats ok
            }
            else
            {
                section.getSelected().clear();
                section.getSelected().add(m);
            }
            
            if (!section.getSelected().isEmpty())
            {
                currentData = new ModuleTransferData(m, dge.getDragOrigin(), section.getSelected());
                dge.startDrag(DragSource.DefaultMoveDrop, this, this);
            }
        }
    }

    public void dragEnter(DragSourceDragEvent e)
    {
      DragSourceContext context = e.getDragSourceContext();
      //intersection of the users selected action, and the source and target actions
      int myaction = e.getDropAction();
      
      if( (myaction & DnDConstants.ACTION_COPY) != 0) {
          context.setCursor(DragSource.DefaultCopyDrop);
      } else if( (myaction & DnDConstants.ACTION_MOVE) != 0) {
              context.setCursor(DragSource.DefaultMoveDrop);
      } else {
          context.setCursor(DragSource.DefaultCopyNoDrop);
      }
    }
    public void dragOver(DragSourceDragEvent dsde) {
    }
    public void dropActionChanged(DragSourceDragEvent dsde) { }
    public void dragExit(DragSourceEvent dse){}
    public void dragDropEnd(DragSourceDropEvent dsde) 
    {
        currentData = null;
    }

    public DataFlavor[] getTransferDataFlavors() {
        DataFlavor[] flavors = {ModuleSelectionFlavor};
        return flavors;
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {
        // DropTarget gives his flavors. DropSource looks if it can be dropped on the target.
        // System.out.println("Plop");
        return flavor != null && flavor.equals(ModuleSelectionFlavor);
    }

    public ModuleTransferData getTransferData(DataFlavor flavor) {
        return currentData;
    }

    public final static class ModuleTransferData
    {
        private Point origin;
        private ModuleSelectionTool data;
        private ModuleUI moduleUI;

        public ModuleTransferData(ModuleUI moduleUI, Point origin, ModuleSelectionTool data)
        {
            this.moduleUI = moduleUI;
            this.origin = new Point(origin);
            this.data = data;
        }

        public ModuleSelectionTool getData()
        {
            return data;
        }

        public Point getOrigin()
        {
            return new Point(origin);
        }

        public ModuleUI getModuleUI()
        {
            return moduleUI;
        }
    }
    
}
