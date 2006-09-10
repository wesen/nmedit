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

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;

import javax.swing.JComponent;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.DModule;

public abstract class ModuleDragSource implements DragGestureListener, DragSourceListener, Transferable
{

    private DragSource dragSource = null;
    private int dragAction = DnDConstants.ACTION_COPY;
    
    public static final DataFlavor ModuleInfoFlavor = new DataFlavor("nomad/ModuleInfoFlavor", "Nomad ModuleInfoFlavor");

    private DModule source = null;
    private JComponent component;
    
    public ModuleDragSource(JComponent source)
    {
        this.component = source;
        dragSource = DragSource.getDefaultDragSource();
        dragSource.createDefaultDragGestureRecognizer(source, dragAction, this);
    }

    public void dragGestureRecognized(DragGestureEvent dge) 
    {
        if (component.isEnabled())
        {
            source = locateModuleInfo(dge);
            if (source!=null) dge.startDrag(DragSource.DefaultCopyDrop, this, this);
        }
    }

    public void dragEnter(DragSourceDragEvent dsde) { }
    public void dragOver(DragSourceDragEvent dsde) {
       
    }
    public void dropActionChanged(DragSourceDragEvent dsde) { }
    public void dragExit(DragSourceEvent dse){}
    public void dragDropEnd(DragSourceDropEvent dsde) {}

    public DataFlavor[] getTransferDataFlavors() {
        DataFlavor[] flavors = {ModuleInfoFlavor};
        return flavors;
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {
        // DropTarget gives his flavors. DropSource looks if it can be dropped on the target.
        // System.out.println("Plop");
        return flavor != null && flavor.equals(ModuleInfoFlavor);
    }

    public Object getTransferData(DataFlavor flavor) {
        return source;
    }
    
    public abstract DModule locateModuleInfo(DragGestureEvent dge);

}
