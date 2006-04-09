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
 * Created on Feb 24, 2006
 */
package org.nomad.patch.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;

import javax.swing.SwingUtilities;

import org.nomad.patch.CableColor;
import org.nomad.patch.Cables;
import org.nomad.patch.Connector;
import org.nomad.theme.component.NomadConnector;
import org.nomad.util.array.TransitionChangeListener;
import org.nomad.util.iterate.ComponentIterator;
import org.nomad.util.shape.ShapeDisplay;
import org.nomad.util.shape.ShapeDraggingTool;

public class CableDisplay extends ShapeDisplay<Curve> implements TransitionChangeListener<Cable> {

	private ModuleSectionUI moduleSectionUI;
	private CurvePainter painter = new CurvePainter();
	private Cables table = null;
	private RootContainerListener rcl ;
	private ConnectorListener connectorListener ;
	private ModuleListener moduleListener ;
	private EnumSet<CableColor> visibleCables = EnumSet.allOf(CableColor.class) ;

	public CableDisplay(ModuleSectionUI sectionUI) {
		this.moduleSectionUI = sectionUI;
		moduleListener = new ModuleListener();
		connectorListener = new ConnectorListener();
		rcl = new RootContainerListener();
		moduleSectionUI.addContainerListener(rcl);
	}
	
    public void addDirtyRegion(int x, int y, int w, int h)
    {
        moduleSectionUI.triggerRepaint(x, y, w, h);
    }
    
	public boolean areCablesVisible(CableColor cableColor) {
		return visibleCables.contains(cableColor) ;
	}
	
	public void setCablesVisible(CableColor cableColor, boolean visible) {
		boolean contains = visibleCables.contains(cableColor) ;
		if (contains ^ visible) {
			if (contains) {
				visibleCables.remove(cableColor);
			} else {
				visibleCables.add(cableColor);
			}
			
			beginUpdate();
			
			for (Iterator<Curve> iter = getBuffered(); iter.hasNext(); ) {
				Cable c = (Cable) iter.next() ;
				if (c.getColorCode().ColorID == cableColor.ColorID) {
					setShapeVisible(c, visible) ;
				}
			}

			endUpdate();
			
		}
	}
	
	public void updateCableLocations(ModuleUI moduleUI) {
		moduleListener.updateConnectedCables(moduleUI);
	}

	public ModuleSectionUI getModuleSectionUI() {
		return moduleSectionUI;
	}

	protected void renderShape(Graphics2D g2, Curve shape) {
		painter.paint(g2, shape);
	}

	protected void renderShapeDirect(Graphics2D g2, Curve shape) {
		painter.paint(g2, shape, false);
	}

	public void transitionChanged(Cable t, boolean transition_added) {
		if (transition_added) {
			add(t);
		} else {
			t.setCablePanel(null);
			remove(t);
		}
	}

	public Cables getTransitions() {
		return table;
	}

	public void setTable(Cables table) {
		if (this.table!=table) {
			beginUpdate();
			try {
				if (this.table!=null) {
					this.table.removeChangeListener(this);
					clear();
				}
				this.table = table;
				if (table!=null) {
					table.addChangeListener(this);
					//newTable(table);
					for (Cable t : getTransitions()) { 
						add(t);
					}
				}
			} catch (RuntimeException e) {
				endUpdate();
				throw e;
			}
			endUpdate();
		}
	}

	// add/remove listeners to ModuleGUIs 
	private class RootContainerListener implements ContainerListener {
		public void componentAdded(ContainerEvent event) {
			if (event.getChild() instanceof ModuleUI) {
				ModuleUI m = (ModuleUI) event.getChild();
				
				m.addMouseListener(moduleListener);
				for (Connector cc:m.getModule().getConnectors()) {
					NomadConnector c = cc.getUI();
					if(c!=null){
						c.addKeyListener(connectorListener);
						c.addMouseListener(connectorListener);
						c.addMouseMotionListener(connectorListener);
					}
				}
			}
		}

		public void componentRemoved(ContainerEvent event) {
			if (event.getChild() instanceof ModuleUI) {
				ModuleUI m = (ModuleUI) event.getChild();

				m.removeMouseListener(moduleListener);				
				for (Connector cc:m.getModule().getConnectors()) {
					NomadConnector c = cc.getUI();
					if(c!=null){
						c.removeKeyListener(connectorListener);
						c.removeMouseListener(connectorListener);
						c.removeMouseMotionListener(connectorListener);
					}
				}
			}
		}
	}


	// returns the location of the connector relative to the root origin
	protected Point getLocationX(NomadConnector connector) {
		Point p = new Point(connector.getWidth()/2, connector.getHeight()/2);
		
		Container c = connector;
		p.translate(c.getX(), c.getY());
		c = c.getParent();
		p.translate(c.getX(), c.getY());
		
		return p;
		//return SwingUtilities.convertPoint(connector, p, getRoot());
	}

    protected void readLocation(NomadConnector c, Point dst)
    {
        dst.setLocation(c.getX()+(c.getWidth()/2), c.getY()+(c.getHeight()/2));
        Container p = c.getParent();
        dst.translate(p.getX(), p.getY());
    }
    
	// find a NomadConnector at given location
	protected NomadConnector findConnectorAt(Point location) {
		return findConnectorAt(location.x, location.y);
	}
	
	protected NomadConnector findConnectorAt(int x, int y) {
		for (ModuleUI m : new ComponentIterator<ModuleUI>(ModuleUI.class, getModuleSectionUI())) 
		{
			if (m.getBounds().contains(x, y)) 
			{  	// module has been found
				
				x-= m.getX(); // translate to module origin
				y-= m.getY();
				
				Component candidate = m.findComponentAt(x, y);
				
				if (candidate instanceof NomadConnector)
					return (NomadConnector) candidate; // found
				
				break;
			}
		}
		
		return null; // not found
	}
	
	// create new cables
	private class ConnectorListener extends MouseAdapter implements MouseMotionListener, KeyListener {

		ShapeDraggingTool<? extends Curve> tool = null;
		NomadConnector start = null;
        Point startLocation = new Point();
        Point stopLocation = new Point();
		boolean moveCableMode = false;

		public void mousePressed(MouseEvent event) {
			if (tool!=null) {
				tool.removeAll();
				tool = null;
			}
			
			if (SwingUtilities.isLeftMouseButton(event)) {
				
				start = (NomadConnector) event.getComponent();
				start.requestFocus(); // we want to listen key events
				
				if (event.getClickCount()==2) {
					Connector c = start.getConnector();
					// TODO dbl-click drag cable
					if (c!=null) {
						ArrayList<Cable> dragged = new ArrayList<Cable>();
						for(Cable cable : getTransitions().getTransitions(c)) {
							if (cable.getC1()!=c)
								cable.swapConnectors();
							dragged.add(cable);
						}
						getTransitions().remove(dragged);
						tool = newShapeDraggingTool(dragged);
						moveCableMode = true;
					}
				} else {	
                    readLocation(start, startLocation);
					Curve curve = new Curve(startLocation, startLocation);
					curve.setColor(getTransitions().determineColor(start.getConnector(), null).getColor());
					tool = newShapeDraggingTool(curve);
					moveCableMode = false;
				}
			}
		}
		
		public void mouseReleased(MouseEvent event) {
			if (isDragging()) {
				beginUpdate();
				try {
					NomadConnector stop = findConnectorAt(getRootLocation(event));
					if (!moveCableMode) {
						if (stop!=null) {
							Cable curve = new Cable(start, stop);
                            
                            readLocation(stop, stopLocation);
							curve.setCurve(startLocation, stopLocation);
							getTransitions().addTransition(curve);
						}
					} else {
						if (stop==null || stop.getConnector()==null) {
							// remove all : below
						} else {
							for (Iterator<? extends Curve> iter=tool.shapes(); iter.hasNext(); ) {
								Cable cable = (Cable) iter.next();
								Cable newCable = new Cable(cable.getC2(), stop.getConnector());
								getTransitions().addTransition(newCable);
							}
						}
					}
					tool.removeAll();
					tool = null;
				} catch (RuntimeException e) {
					endUpdate();
					throw e;
				}
				endUpdate();
			}
		}
		
		private void abortMoveCableMode() {
			if (isDragging() && moveCableMode) {
				beginUpdate();
				try {
					tool.removeAll();
					for (Iterator<? extends Curve> iter=tool.shapes(); iter.hasNext(); ) {
						Cable cable = (Cable) iter.next();
						getTransitions().addTransition(cable); // put cables back
					}
					tool = null;
				} catch (RuntimeException e) {
					endUpdate();
					throw e;
				}
				endUpdate();
			}
		}
		
		private boolean isDragging() {
			return tool!=null;
		}


		public void mouseDragged(MouseEvent event) {
			if (tool!=null) {
				tool.updateShapes();
				for (Iterator<? extends Curve> iter=tool.shapes();iter.hasNext();) {
					Curve curve = iter.next();
					curve.setP1(getRootLocation(event));
					update(curve);
				}
			}
		}

		public void mouseMoved(MouseEvent event) { }
		
		Point getRootLocation(MouseEvent event) {
			return SwingUtilities.convertPoint(event.getComponent(), event.getPoint(), getModuleSectionUI());
		}

		public void keyPressed(KeyEvent event) {  
			System.out.println(event.getKeyCode()+", "+KeyEvent.VK_ESCAPE);
			if (event.getKeyCode()==KeyEvent.VK_ESCAPE && moveCableMode) {
				abortMoveCableMode();
			}
		}

		public void keyTyped(KeyEvent event) { }

		public void keyReleased(KeyEvent event) {
		}
	}

	public void remove(Curve t) {
		if (t instanceof Cable) {
			Cable c = (Cable) t;
			c.setCablePanel(null);
		}
		super.remove(t);
	}
	
	public void add(Curve t) {
		beginUpdate();
		boolean visible = true;
		if (t instanceof Cable) {
			Cable c = (Cable) t;
			c.setCablePanel(this);
			updateCableLocation(c);
			visible = areCablesVisible(c.getColorCode());
		}
		super.add(t);
		setShapeVisible(t, visible);
		endUpdate();
	}

    Point dummy1 = new Point();
    Point dummy2 = new Point();
    
	public void updateCableLocation(Cable t) {

		NomadConnector c1 = t.getC1().getUI();
		NomadConnector c2 = t.getC2().getUI();
		if (c1!=null && c2!=null) 
        {
            readLocation(c1, dummy1);
            readLocation(c2, dummy2);
			t.setCurve(dummy1, dummy2);
		}
		
		//update(t);
	}
	
	// if module location changes cables will be adjusted accordingly
	private class ModuleListener extends MouseAdapter {

		ArrayList<Cable> cableList = new ArrayList<Cable>();
		
		void updateConnectedCables(ModuleUI m) {

			if (getTransitions()==null)
				return;
			
			beginUpdate();
			try {
				for (Connector c : m.getModule().getConnectors()) {
					for ( Cable t : getTransitions().getTransitions(c)) {
						updateCableLocation(t);
					}
				}
			} catch (RuntimeException r) {
				endUpdate();
				throw r;
			}
			endUpdate();
		}
		
		public void mousePressed(MouseEvent event) {
			if (SwingUtilities.isLeftMouseButton(event)) {
				beginUpdate();
				try {
					for (Connector c : ((ModuleUI)event.getComponent()).getModule().getConnectors()) {
						for ( Cable t : getTransitions().getTransitions(c))
							if (!cableList.contains(t)) {
								cableList.add(t);
								setDirectRenderingEnabled(t, true);
							}
					}
					cableList.clear();
				} catch (RuntimeException r) {
					endUpdate();
					throw r;
				}
				endUpdate();
			}
		}

		public void mouseReleased(MouseEvent event) {
			transformDirectRendering();
		}

	}
}
