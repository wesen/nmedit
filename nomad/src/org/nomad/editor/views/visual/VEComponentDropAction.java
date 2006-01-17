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
 * Created on Jan 16, 2006
 */
package org.nomad.editor.views.visual;

import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;

import org.nomad.editor.views.classes.NomadClassesView;
import org.nomad.theme.component.NomadComponent;

public class VEComponentDropAction extends VEManager implements DropTargetListener {

	private int dropAction = DnDConstants.ACTION_COPY;
	
	public VEComponentDropAction(VisualEditor editor) {
		super(editor);
        new DropTarget(editor, dropAction, this, true);        
	}

	public void dragOver(DropTargetDragEvent event) {
		if (event.getCurrentDataFlavorsAsList().contains(NomadClassesView.NomadComponentClassFlavor)) {
			event.acceptDrag(DnDConstants.ACTION_COPY);
		} 
	}

	public void drop(DropTargetDropEvent event) {
		if (event.isDataFlavorSupported(NomadClassesView.NomadComponentClassFlavor)
				&& event.isLocalTransfer()) {
			DataFlavor chosenDataFlavor = NomadClassesView.NomadComponentClassFlavor;
			Object data;
			try {
				event.acceptDrop(dropAction);
				data = event.getTransferable().getTransferData(chosenDataFlavor);
			}
		  	catch (Throwable t) {
				t.printStackTrace();
				event.dropComplete(false);
				return;
		  	}

			if (data instanceof Class) {
				Class dataClass = (Class) data;
				
				
				if (NomadComponent.class.isAssignableFrom(dataClass)) {
					NomadComponent component = null;
					try {
						component = (NomadComponent) dataClass.newInstance();
						component.setLocation(event.getLocation());
						
						getEditor().newComponenentDropped(component);
						
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
			event.dropComplete(true);
		  	
		} else {
			event.rejectDrop();      
			event.dropComplete(false);
		}
	}

	public void dragEnter(DropTargetDragEvent event) { }
	public void dropActionChanged(DropTargetDragEvent event) { }
	public void dragExit(DropTargetEvent event) { }
}
