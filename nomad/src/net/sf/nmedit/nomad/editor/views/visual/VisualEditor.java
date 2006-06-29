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
package net.sf.nmedit.nomad.editor.views.visual;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.DModule;
import net.sf.nmedit.nomad.core.nomad.NomadEnvironment;
import net.sf.nmedit.nomad.editor.ComponentAlignmentMenu;
import net.sf.nmedit.nomad.editor.views.ComponentZOrderMenuItem;
import net.sf.nmedit.nomad.editor.views.property.NomadPropertyEditor;
import net.sf.nmedit.nomad.theme.ModuleComponent;
import net.sf.nmedit.nomad.theme.NomadClassicColors;
import net.sf.nmedit.nomad.theme.component.NomadComponent;
import net.sf.nmedit.nomad.theme.property.Property;
import net.sf.nmedit.nomad.theme.property.PropertySet;
import net.sf.nmedit.nomad.theme.property.Value;


public class VisualEditor extends NomadComponent implements ModuleComponent {

	private VEHotSpotManager spots= null;
	private ArrayList<NomadComponent> selection = new ArrayList<NomadComponent>();
	private VEPainter painter = null;
	private VEMouseEventHandler mouseHandler = null;
	private DModule info = null;
	private NomadPropertyEditor propertyEditor = null;
	private Rectangle selectionRect = new Rectangle(0,0,0,0);
	private ComponentAlignmentMenu tbAlignment = new ComponentAlignmentMenu();
	private JPopupMenu popup = null;
	
	private final static ArrayList<NomadComponent> copyMemory
		= new ArrayList<NomadComponent>();
	
	private ComponentZOrderMenuItem mnSendToBack = null;
	
	public VisualEditor(DModule info) {
		setLayout(null);
		this.info = info;
		new VEComponentDropAction(this);
		spots = new VEHotSpotManager(this);
		new VEComponentManager(this);
		painter = new VEPainter();
		mouseHandler = new VEMouseEventHandler(this);
		setBackground(NomadClassicColors.MODULE_BACKGROUND);

		JMenuItem imageImage = new JMenuItem("Add images...");
		imageImage.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event) {
				ImageBrowserDialog.showDialog(VisualEditor.this);
			}});

		JMenuItem mnCopy  = new JMenuItem("Copy");
		mnCopy.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event) {
				copySelection();
			}});
		JMenuItem mnPaste = new JMenuItem("Paste");
		mnPaste.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event) {
				pasteCopy();
			}});
		JMenuItem mnDelete= new JMenuItem("Delete");
		mnDelete.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event) {
				deleteSelection();
			}});
		
		mnSendToBack = new ComponentZOrderMenuItem("Send To Back");
		mnSendToBack.addActionListener(new ComponentZOrderMenuItem.SendToBackAction());
		
		popup = new JPopupMenu();
		popup.add(mnCopy);
		popup.add(mnPaste);
		popup.addSeparator();
		popup.add(mnDelete);
		popup.addSeparator();
		popup.add(mnSendToBack);
		popup.addSeparator();
		popup.add(tbAlignment);
		popup.addSeparator();
		popup.add(imageImage);
		
		addMouseListener(new MouseAdapter(){
	        public void mousePressed(MouseEvent evt) {
	            if (evt.isPopupTrigger()) {
	            	mnSendToBack.setComponent(findAnyComponentAt(evt.getX(), evt.getY()));
	            	popup.show(evt.getComponent(), evt.getX(), evt.getY());
	            }
	        }
	        public void mouseReleased(MouseEvent evt) {
	            if (evt.isPopupTrigger()) {
	            	popup.show(evt.getComponent(), evt.getX(), evt.getY());
	            }
	        }	
		
		});
	}

	protected void createProperties(PropertySet set) {
		// we do not call super() because we don't want to create any properties
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
	
	protected Component findAnyComponentAt(int x, int y) {
		for (int i=getComponentCount()-1;i>=0;i--) {
			if (getComponent(i).getBounds().contains(x, y))
				return getComponent(i);
		}
		return null;
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
		getPropertyEditor().setEditingPropertySet(null, null);
	}
	
	public NomadComponent getFirstSelection() {
		if (selection.size()>0)
			return selection.get(0);
		return null;
	}
	
	public void setSelection(NomadComponent component) {
		if (selection.size()!=1 || getFirstSelection()!=component) {
			clearSelection();
			if (component!=null) {
				getPropertyEditor().setEditingPropertySet(
						component,
						NomadEnvironment.sharedInstance().getFactory().getProperties(component)
				);
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

	public ArrayList<NomadComponent> getSelection() {
		return new ArrayList<NomadComponent>(selection);
	}

	public void copySelection() {
		copyMemory.clear();
		for (NomadComponent c : selection) {
			NomadComponent clone = cloneComponent(c);
			if (clone!=null)
				copyMemory.add(clone);
		}
	}
	
	public void pasteCopy() {
		for (NomadComponent c : copyMemory) {
			add(c);
		}
		repaint();
	}

	public void deleteSelection() {
		for (NomadComponent c : selection) {
			remove(c);
		}
		setSelection(null);
	}
	
	public NomadComponent cloneComponent(NomadComponent c) {
		NomadComponent clone;
		try {
			clone = c.getClass().newInstance();
		} catch (Throwable e) {
			return null;
		}
		
		HashMap<String, Value> src = new HashMap<String,Value>();
		
		for (Property p : NomadEnvironment.sharedInstance().getFactory().getProperties(c)) {
			src.put(p.getName(), p.encode(c));
		}
		
		for (Value v : src.values()) {
            v.assignTo(c);
		}
		
		return clone;
	}
	
}
