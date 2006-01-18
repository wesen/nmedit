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

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import org.nomad.editor.ComponentAlignmentToolbar;
import org.nomad.editor.views.property.NomadPropertyEditor;
import org.nomad.theme.ModuleComponent;
import org.nomad.theme.component.NomadComponent;
import org.nomad.theme.property.PropertySetListener;
import org.nomad.xml.dom.module.DModule;

public class VisualEditor extends NomadComponent implements ModuleComponent {

	private VEHotSpotManager spots= null;
	private ArrayList selection = new ArrayList(); // NomadComponents
	private VEPainter painter = null;
	private VEPropertySetChangeHandler propertySetListener = null;
	private VEMouseEventHandler mouseHandler = null;
	private DModule info = null;
	private NomadPropertyEditor propertyEditor = null;
	private Rectangle selectionRect = new Rectangle(0,0,0,0);
	private ComponentAlignmentToolbar tbAlignment = null;
	
	public VisualEditor(DModule info) {
		setLayout(null);
		this.info = info;
		new VEComponentDropAction(this);
		spots = new VEHotSpotManager(this);
		new VEComponentManager(this);
		painter = new VEPainter();
		propertySetListener = new VEPropertySetChangeHandler(this);
		mouseHandler = new VEMouseEventHandler(this);
		
		// remove unwanted properties
		getAccessibleProperties().clear();
	}
	
	public PropertySetListener getPropertySetListener() {
		return propertySetListener;
	}
	
	public VEMouseEventHandler getMouseEventHandler() {
		return mouseHandler;
	}

	public VEHotSpotManager getHotSpotManager() {
		return spots;
	}

	public void newComponenentDropped(NomadComponent component) {
		add(component);
	}

	public DModule getModuleInfo() {
		return info;
	}

	public void setComponentPropertyEditor(NomadPropertyEditor propertyEditor) {
		this.propertyEditor = propertyEditor;
	}

	public NomadPropertyEditor getPropertyEditor() {
		return propertyEditor;
	}
	
	public void paintComponent(Graphics g) {
		g.setColor(getBackground());
		g.fillRect(0,0,getWidth(), getHeight());
		
		painter.setGraphics((Graphics2D)g);
		painter.paintComponents(this);
		painter.paintHotSpots(this);
		painter.paintSelectionRect(selectionRect);
	}
	
	public Rectangle getSelectionRect() {
		return new Rectangle(selectionRect);
	}
	
	public void setSelectionRect(Rectangle r) {
		if (r==null) {
			if (!selectionRect.isEmpty())
				selectionRect = new Rectangle(0,0,0,0);
		} else {
			selectionRect = new Rectangle(r);
		}
		repaint();
	}

	public void rebuildSelection() {
		// check selected components
		clearSelection();
		if (!selectionRect.isEmpty()) {
			
			for (int i=getComponentCount()-1;i>=0;i--) {
				NomadComponent c = (NomadComponent) getComponent(i);
				
				if (selectionRect.intersects(c.getBounds()))
					selection.add(c);
				
			}
		}
		
		tbAlignment.setComponents(selection.toArray());
		
		repaint();
	}
	
	public void clearSelection() {
		if (selection.size()==1) {
			NomadComponent c = getFirstSelection();
			c.setVisible(false);
		}

		selection.clear();
		getPropertyEditor().setEditingPropertySet(getAccessibleProperties());
	}
	
	public NomadComponent getFirstSelection() {
		if (selection.size()>0)
			return (NomadComponent) selection.get(0);
		return null;
	}
	
	public void setSelection(NomadComponent component) {
		if (selection.size()!=1 || getFirstSelection()!=component) {
			clearSelection();
			if (component!=null) {
				getPropertyEditor().setEditingPropertySet(component.getAccessibleProperties());
				selection.add(component);
				component.setVisible(true);
			}
			
			tbAlignment.setComponents(selection.toArray());
			repaint();
		}
	}

	public boolean isSelected(NomadComponent component) {
		return selection.contains(component);
	}

	public ComponentAlignmentToolbar getAlignmentToolbar() {
		return tbAlignment;
	}

	public void setAlignmentToolbar(ComponentAlignmentToolbar tbAlignment) {
		this.tbAlignment = tbAlignment;
	}

	public ArrayList getSelection() {
		return new ArrayList(selection);
	}
}
