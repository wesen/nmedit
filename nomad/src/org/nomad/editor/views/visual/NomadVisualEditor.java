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
 * Created on Jan 10, 2006
 */
package org.nomad.editor.views.visual;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragSource;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import org.nomad.editor.views.classes.NomadClassesView;
import org.nomad.editor.views.property.NomadPropertyEditor;
import org.nomad.theme.ModuleComponent;
import org.nomad.theme.component.NomadComponent;
import org.nomad.theme.property.IntegerProperty;
import org.nomad.theme.property.PropertySet;
import org.nomad.xml.dom.module.DModule;

public class NomadVisualEditor extends NomadComponent implements ModuleComponent {

	private EventPolicyConfigurator eventPolicyConfigurator = new EventPolicyConfigurator();
	private NomadComponent selectedComponent = null;
	private EditorEventHandler editorEventHandler = new EditorEventHandler();
	private Rectangle recResize = null;
	private Rectangle recMove = null;
	private Rectangle recBounds = null;
	private NomadPropertyEditor propertyEditor = null;
	private int resize_grid = 5;
	private int move_grid = 5;
	private int dropAction = DnDConstants.ACTION_COPY;
	
	private ArrayList lostComponentsList = new ArrayList();
	private Rectangle recSelection = new Rectangle(0,0,0,0);
	
	private SelectedComponentEventHandler seh = new SelectedComponentEventHandler();
	
	private DModule moduleInfo = null;
	
	public NomadVisualEditor(DModule info) {
		super();
		this.moduleInfo = info;
		setLayout(null);
		addContainerListener(eventPolicyConfigurator);
		addComponentListener(eventPolicyConfigurator);
		addMouseListener(editorEventHandler);
		addMouseMotionListener(editorEventHandler);
		addKeyListener(editorEventHandler);

		DragSource.getDefaultDragSource();


		MyDTL dtl = new MyDTL();
		
        new DropTarget(this, dropAction, dtl, true);
        
        
        /**
         * First we remove all unnecessary properties
         */
        
        PropertySet pset = getAccessibleProperties();
        for (int i=pset.size()-1;i>=0;i--)
        	pset.remove(i);
        
        /**
         * Install custom properties
         */

        pset.add(new ResizeGridProperty(this));
        pset.add(new MoveGridProperty(this));
	}
	
	public DModule getModuleInfo() {
		return moduleInfo;
	}
	
	private class ResizeGridProperty extends IntegerProperty {

		public ResizeGridProperty(NomadComponent component) {
			super(component);
			setName("grid.resize");
		}

		public void setIntegerValue(int integer) {
			resize_grid = integer;
		}

		public int getIntegerValue() {
			return resize_grid;
		}
	}
	
	private class MoveGridProperty extends IntegerProperty {

		public MoveGridProperty(NomadComponent component) {
			super(component);
			setName("grid.move");
		}

		public void setIntegerValue(int integer) {
			move_grid = integer;
		}

		public int getIntegerValue() {
			return move_grid;
		}
	}
	
	private class MyDTL implements DropTargetListener {

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
						} catch (InstantiationException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
						if (component!=null) {
							add(component);
							if (propertyEditor!=null)
								propertyEditor.setEditingPropertySet(component.getAccessibleProperties());
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
	

	public void setComponentPropertyEditor(NomadPropertyEditor editor) {
		if (propertyEditor!=editor) {
			if (propertyEditor!=null) propertyEditor.setEditingPropertySet(null);
			propertyEditor = editor;
			if (propertyEditor!=null) {
				if(selectedComponent!=null) {
					propertyEditor.setEditingPropertySet(selectedComponent.getAccessibleProperties());
				} else {
					propertyEditor.setEditingPropertySet(getAccessibleProperties());
				}
			}
		}
	}
	
	public NomadPropertyEditor getComponentPropertyEditor() {
		return propertyEditor;
	}

	private class SelectedComponentEventHandler extends ComponentAdapter {

		public void componentResized(ComponentEvent event) {
			updateComponentGrip();
			repaint();
		}

		public void componentMoved(ComponentEvent event) {
			updateComponentGrip();
			repaint();
		}
	}
	
	private class EventPolicyConfigurator extends ComponentAdapter implements ContainerListener {
		public void componentAdded(ContainerEvent event) {
			if (!(event.getChild()instanceof NomadComponent))
				throw new IllegalStateException("Can not host a non NomadComponent component.");
			
			event.getChild().setVisible(false);
			event.getChild().addComponentListener(this);
		}

		public void componentRemoved(ContainerEvent event) {
			event.getChild().setVisible(true);
			event.getChild().removeComponentListener(this);
			
		}
		
		public synchronized void checkIfComponentIsLost(Rectangle bounds, Component c) {
			
			if (!bounds.contains(c.getBounds()) ) { // lost
			
				if (!lostComponentsList.contains(c)) {
					lostComponentsList.add(c);
				}
				
			} else { // not lost
				
				lostComponentsList.remove(c);
				
			}
		}
		public synchronized void checkIfComponentIsLost(Component c) {
			Rectangle bounds = new Rectangle(0, 0, getWidth(), getHeight());
			checkIfComponentIsLost(bounds, c);
		}
		
		public synchronized void rebuildLostComponentList() {

			Rectangle bounds = new Rectangle(0, 0, getWidth(), getHeight());
			
			lostComponentsList = new ArrayList();
			
			for (int i=getComponentCount()-1;i>=0;i--)
				checkIfComponentIsLost(bounds, getComponent(i));
			
			repaint();
		}
		
		// check for lost components

		public void componentResized(ComponentEvent event) {
			if (event.getSource()==NomadVisualEditor.this) {

				rebuildLostComponentList();
				
			} else {
				checkIfComponentIsLost(event.getComponent());
				repaint();
			}
		}

		public void componentMoved(ComponentEvent event) {
			if (event.getSource()==NomadVisualEditor.this) {
			
				rebuildLostComponentList();
				
			} else {
				checkIfComponentIsLost(event.getComponent());
				repaint();
			}
		}
	}
	
	public void setSelectedComponent(Component comp) {
		if (selectedComponent!=comp) {
			if (selectedComponent!=null) {
				selectedComponent.removeComponentListener(seh);
				selectedComponent.setPreferredSize(selectedComponent.getSize());
				selectedComponent.setVisible(false);
			}
			selectedComponent = (NomadComponent) comp;
			if (selectedComponent!=null) {
				selectedComponent.addComponentListener(seh);
				selectedComponent.setVisible(true);
				selectedComponent.requestFocus();
			}
			
			if (propertyEditor!=null)  {
				if (selectedComponent!=null)
					propertyEditor.setEditingPropertySet(selectedComponent.getAccessibleProperties());
				else
					propertyEditor.setEditingPropertySet(this.getAccessibleProperties());
			}
			
			updateComponentGrip();
			repaint();
		}
	}
	
	protected void updateComponentGrip() {
		recBounds = null; recMove = null; recResize = null;
		if (selectedComponent!=null) {
			int cx = selectedComponent.getX();
			int cy = selectedComponent.getY();
			int cw = selectedComponent.getWidth();
			int ch = selectedComponent.getHeight();
			recBounds = new Rectangle(cx-1, cy-1, cw+1, ch+1);
			recMove = new Rectangle(cx-1, cy-6, 6,6);
			recResize = new Rectangle(cx+cw, cy+ch, 6,6);
		}
	}

	protected Rectangle getComponentOutOfBoundsGrip(Rectangle bounds, Component c) {

		Rectangle r = new Rectangle(c.getX(),c.getY(),6,6);

		if (r.x+r.width>=bounds.width) r.x = bounds.width-r.width;
		else if (r.x<0) r.x=0;

		if (r.y+r.height>=bounds.height) r.y = bounds.height-r.height;
		else if (r.y<0) r.y=0;
		
		return r;
	}
	
	public void paintComponent(Graphics g) {
		//super.paintComponent(g);
		
		
		g.setColor(getBackground());
		g.fillRect(0,0,getWidth(), getHeight());
		
		
		Rectangle bounds = new Rectangle(0, 0, getWidth(), getHeight());
		
		for (int i=0;i<getComponentCount();i++) {
			NomadComponent c = (NomadComponent) getComponent(i);
			if (!c.isVisible()) {
				if (bounds.contains(c.getBounds())) {
					Graphics gg = g.create();
					gg.translate(c.getX(), c.getY());
					gg.setClip(0, 0, c.getWidth(), c.getHeight());
					c.paintComponent(gg);
					gg.dispose();
				} else { // they are outside
				}
			}
		}

		Graphics2D g2 = (Graphics2D) g.create();
		g2.setColor(Color.CYAN);
		for (int i=lostComponentsList.size()-1;i>=0;i--) {
			Component c = (Component) lostComponentsList.get(i);
			g2.fill(getComponentOutOfBoundsGrip(bounds,c));
		}
		if (recBounds!=null) {
			Stroke stroke = g2.getStroke();
			g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setRenderingHint( RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
			g2.setStroke(new BasicStroke( 1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1, new float[]{ 1, 2 }, 0 ));
			g2.setColor(Color.RED);

			g2.draw(recBounds);
			g2.setStroke(stroke);
		}
		
		if (recMove!=null) {
			g2.setColor(new Color(255,0,0,160)); // transparent red
			g2.fill(recMove);
		}

		if (recResize!=null) { 
			g2.setColor(new Color(0,0,255,160)); // transparent blue
			g2.fill(recResize);
		}

		if (!recSelection.isEmpty()) {
			g2.setColor(new Color(0,0,255,40));
			g2.fill(recSelection);
			g2.setColor(Color.BLUE);
			g2.draw(recSelection);
		}
		
		
		g2.dispose();
	}
	
	public Component findAnyComponentAt(Point p) {
		for (int i=0;i<getComponentCount();i++) {
			Component c = getComponent(i);
			if (c.contains(p.x-c.getX(), p.y-c.getY())) {
				return c;
			}
		}
		return null;
	}
	
	public int alignToGrid(int grid, int value) {
		return (grid<=0) ? value : value - (value%grid);
	}
	
	public int alignToMoveGrid(int value) {
		return alignToGrid(move_grid, value);
	}
	
	public int alignToResizeGrid(int value) {
		return alignToGrid(resize_grid, value);		
	}

	private class EditorEventHandler implements MouseListener, KeyListener, MouseMotionListener {
		
		int dragType = DRAG_INVALID;
		final static int DRAG_INVALID = 0;
		final static int DRAG_RESIZE = 1;
		final static int DRAG_MOVE = 2;
		Dimension start_size = null;
		Point start_loc = null;
		int startx = 0;
		int starty = 0;
		
		public void mouseClicked(MouseEvent event) {
			dragType = DRAG_INVALID;
			if (oldCursor!=null) {
				setCursor(oldCursor);
				oldCursor = null;
			}

			if (!recSelection.isEmpty()) {
				recSelection.width = 0;
				recSelection.height = 0;
				repaint();
			}
			
			// check if 'outside grip' was clicked
			Rectangle bounds = new Rectangle(0, 0, getWidth(), getHeight());
			for (int i=lostComponentsList.size()-1;i>=0;i--) {
				Component c = (Component) lostComponentsList.get(i);
				Rectangle grip = getComponentOutOfBoundsGrip(bounds, c);
				
				if (grip.contains(event.getPoint())) {
					// bring back
					
					int x = c.getX();
					int y = c.getY();

					if (x<0) x=10; else if (x+c.getWidth()>=bounds.width) x=bounds.width-c.getWidth()-10;
					if (y<0) y=10; else if (y+c.getHeight()>=bounds.height) y=bounds.height-c.getHeight()-10;
					
					c.setLocation(x, y);
					
					// in case the component is invisible because it is too small
					c.setSize(Math.max(6, c.getWidth()), Math.max(6,c.getHeight()));
					
					eventPolicyConfigurator.checkIfComponentIsLost(c);
					
					return;
				}
				
			}
			
			Component c = NomadVisualEditor.this.findAnyComponentAt(event.getPoint());
			setSelectedComponent((NomadComponent)c);
		}
		public void mousePressed(MouseEvent event) {
			startx = event.getX();
			starty = event.getY();

			recSelection.x = startx;
			recSelection.y = starty;
			
			if (!recSelection.isEmpty()) {
				recSelection.width = 0;
				recSelection.height = 0;
				repaint();
			}
			
			if (selectedComponent!=null) {
				if ((recResize!=null)&&recResize.contains(event.getPoint())) {
					start_size = selectedComponent.getSize();
					dragType = DRAG_RESIZE;
				} else if ((recMove!=null)&&recMove.contains(event.getPoint())) {
					start_loc = selectedComponent.getLocation();
					dragType = DRAG_MOVE;
				} else {
					dragType = DRAG_INVALID;
					setSelectedComponent(null);
				}
			} else {
				dragType = DRAG_INVALID;
				setSelectedComponent(null);
			}
		}

		public void mouseReleased(MouseEvent event) {			
			dragType = DRAG_INVALID;
		}

		public void mouseEntered(MouseEvent event) { }
		public void mouseExited(MouseEvent event) { }
		public void keyTyped(KeyEvent event) { }
		public void keyPressed(KeyEvent event) { }
		public void keyReleased(KeyEvent event) { }
		
		public void mouseDragged(MouseEvent event) {
			int dx = event.getX()-startx;
			int dy = event.getY()-starty;

			if (selectedComponent!=null) {
				
				if (dragType==DRAG_MOVE) {
					selectedComponent.setLocation(alignToMoveGrid(start_loc.x+dx), alignToMoveGrid(start_loc.y+dy));
					updateComponentGrip();
					repaint();
					
					return ;
				} else if (dragType==DRAG_RESIZE) {
					selectedComponent.setSize(alignToResizeGrid(start_size.width+dx), alignToResizeGrid(start_size.height+dy));
					updateComponentGrip();
					repaint();
					return ;
				}
			}

			recSelection.width = dx;
			recSelection.height = dy;

			repaint();
		}

		private Cursor oldCursor = null;
		
		public void mouseMoved(MouseEvent event) {
			if (dragType==DRAG_INVALID) {

				// check if 'outside grip' is hovered
				Rectangle bounds = new Rectangle(0, 0, getWidth(), getHeight());
				for (int i=lostComponentsList.size()-1;i>=0;i--) {
					Component c = (Component) lostComponentsList.get(i);
					Rectangle grip = getComponentOutOfBoundsGrip(bounds, c);
					
					if (grip.contains(event.getPoint())) {
						
						if (oldCursor==null) {

							oldCursor = getCursor();
							setCursor(new Cursor(Cursor.HAND_CURSOR));
						}
						
						return;
					}
					
				}
				
				Component c = findAnyComponentAt(event.getPoint());
				if ((c==null) && (oldCursor!=null)) {
					if (!(recResize!=null && recResize.contains(event.getPoint()))) {
						if (!(recMove!=null && recMove.contains(event.getPoint()))) {
							setCursor(oldCursor);
							oldCursor=null;
						}
					}
				} else if (c!=selectedComponent && oldCursor==null) {											
					if(c!=null) {
						oldCursor = getCursor();
						setCursor(new Cursor(Cursor.HAND_CURSOR));
					} else if (recResize!=null && recResize.contains(event.getPoint())) {
						oldCursor = getCursor();
						setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));
					} else if (recMove!=null && recMove.contains(event.getPoint())) {
						oldCursor = getCursor();
						setCursor(new Cursor(Cursor.MOVE_CURSOR));
					}
				}
				
			}
		}
	}
	
}
