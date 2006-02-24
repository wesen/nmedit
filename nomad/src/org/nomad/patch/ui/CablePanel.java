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
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import org.nomad.patch.Cables;
import org.nomad.patch.Connector;
import org.nomad.theme.component.NomadConnector;
import org.nomad.util.array.TransitionChangeListener;
import org.nomad.util.iterate.ComponentIterator;
import org.nomad.util.shape.ShapeDisplay;
import org.nomad.util.shape.ShapeDraggingTool;

public class CablePanel extends ShapeDisplay<Curve> implements TransitionChangeListener<Cable> {

	private ModuleSectionUI moduleSectionUI;
	private CurvePainter painter = new CurvePainter();
	private Cables table = null;
	private RootContainerListener rcl ;
	private ConnectorListener connectorListener ;
	private ModuleListener moduleListener ;

	public CablePanel(ModuleSectionUI sectionUI) {
		this.moduleSectionUI = sectionUI;
		moduleListener = new ModuleListener();
		connectorListener = new ConnectorListener();
		rcl = new RootContainerListener();
		moduleSectionUI.addContainerListener(rcl);
		
		setSize(sectionUI.getSize());
		sectionUI.addComponentListener(new ComponentAdapter(){

			public void componentResized(ComponentEvent event) {
				setSize(event.getComponent().getSize());
			}});
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
				
				m.addComponentListener(moduleListener);
				m.addMouseListener(moduleListener);
				for (Connector cc:m.getModule().getConnectors()) {
					NomadConnector c = cc.getUI();
					if(c!=null){
						c.addMouseListener(connectorListener);
						c.addMouseMotionListener(connectorListener);
					}
				}
			}
		}

		public void componentRemoved(ContainerEvent event) {
			if (event.getChild() instanceof ModuleUI) {
				ModuleUI m = (ModuleUI) event.getChild();

				m.removeComponentListener(moduleListener);
				m.removeMouseListener(moduleListener);
				
				for (Connector cc:m.getModule().getConnectors()) {
					NomadConnector c = cc.getUI();
					if(c!=null){
						c.removeMouseListener(connectorListener);
						c.removeMouseMotionListener(connectorListener);
					}
				}
			}
		}
	}


	// returns the location of the connector relative to the root origin
	protected Point getLocation(NomadConnector connector) {
		Point p = new Point(connector.getWidth()/2, connector.getHeight()/2);
		
		Container c = connector;
		p.translate(c.getX(), c.getY());
		c = c.getParent();
		p.translate(c.getX(), c.getY());
		
		return p;
		//return SwingUtilities.convertPoint(connector, p, getRoot());
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
	private class ConnectorListener extends MouseAdapter implements MouseMotionListener {

		ShapeDraggingTool<Curve> tool = null;
		NomadConnector start = null;
		Point startLocation = null;

		public void mousePressed(MouseEvent event) {
			if (tool!=null) {
				tool.removeAll();
				tool = null;
			}
			
			if (SwingUtilities.isLeftMouseButton(event)) {
				
				if (event.getClickCount()==2)
					// TODO dbl-click drag cable
					;
				
				start = (NomadConnector) event.getComponent();
				startLocation = getLocation(start);
				Curve curve = new Curve(startLocation, startLocation);
				curve.setColor(Cable.getColorByColorCode(getTransitions().determineColor(start.getConnector(), null)));
				tool = newShapeDraggingTool(curve);
			}
		}
		
		public void mouseReleased(MouseEvent event) {
			if (isDragging()) {
				beginUpdate();
				try {
					tool.removeAll();
					tool = null;
					NomadConnector stop = findConnectorAt(getRootLocation(event));
					
					if (stop!=null) {
						Cable curve = new Cable(start, stop);
						curve.setCurve(startLocation, getLocation(stop));
						getTransitions().addTransition(curve);
					}
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
				Curve curve = tool.shapes().next(); // there is at least one shape
				curve.setP2(getRootLocation(event));
				update(curve);
			}
		}

		public void mouseMoved(MouseEvent event) { }
		
		Point getRootLocation(MouseEvent event) {
			return SwingUtilities.convertPoint(event.getComponent(), event.getPoint(), getModuleSectionUI());
		}
	}
/*
	public void updateCurves()
	{
		updateCurves(getTransitions());
	}
	
	public void addCurve(Curve curve) {
		if (curve instanceof Cable) {
			
			// make sure that new curves have the correct location 
			
			Cable t = (Cable) curve;

			NomadConnector c1 = t.getC1().getUI();
			NomadConnector c2 = t.getC2().getUI();
			if (c1!=null && c2!=null) {
				t.setCurve(getLocation(c1), getLocation(c2));
			}
		}
		
		super.addCurve(curve);
	}
	
	public void updateCurves(Iterable<Cable> curves)
	{
		for (Curve transition : curves)
		{			
			Cable t = (Cable) transition;
			NomadConnector c1 = t.getC1().getUI();
			NomadConnector c2 = t.getC2().getUI();
			if (c1!=null && c2!=null) {
				t.setCurve(getLocation(c1), getLocation(c2));
			}
		}
	}*/
	
	public void remove(Curve t) {
		if (t instanceof Cable) {
			Cable c = (Cable) t;
			c.setCablePanel(null);
		}
		super.remove(t);
	}
	
	public void add(Curve t) {
		if (t instanceof Cable) {
			Cable c = (Cable) t;
			c.setCablePanel(this);
			updateCableLocation(c);
		}
		super.add(t);
	}
	
	public void updateCableLocation(Cable t) {

		NomadConnector c1 = t.getC1().getUI();
		NomadConnector c2 = t.getC2().getUI();
		if (c1!=null && c2!=null) {
			t.setCurve(getLocation(c1), getLocation(c2));
		}
		
		//update(t);
	}
	
	// if module location changes cables will be adjusted accordingly
	private class ModuleListener extends ComponentAdapter implements MouseListener {

		ArrayList<Cable> cableList = new ArrayList<Cable>();
		
		void updateConnectedCables(ModuleUI m) {
			beginUpdate();
			try {
				for (NomadConnector a : new ComponentIterator<NomadConnector>(NomadConnector.class, m)) {
					Connector c = a.getConnector();
					if (c!=null) {
						for ( Cable t : getTransitions().getTransitions(c)) {
							updateCableLocation(t);
						}
					}
				}
			} catch (RuntimeException r) {
				endUpdate();
				throw r;
			}
			endUpdate();
		}
		
		public void componentMoved(ComponentEvent event) {
			updateConnectedCables ((ModuleUI)event.getComponent());
		}
		
		public void mouseClicked(MouseEvent event) {
			// TODO Auto-generated method stub
			
		}

		public void mousePressed(MouseEvent event) {
			beginUpdate();
			try {
				for (NomadConnector a : new ComponentIterator<NomadConnector>(NomadConnector.class, (ModuleUI)event.getComponent())) {
					Connector c = a.getConnector();
					if (c!=null) {
						for ( Cable t : getTransitions().getTransitions(c))
							if (!cableList.contains(t)) {
								cableList.add(t);
								setDirectRenderingEnabled(t, true);
							}
					}
				}
			} catch (RuntimeException r) {
				endUpdate();
				throw r;
			}
			endUpdate();
		}

		public void mouseReleased(MouseEvent event) {
			//endUpdate();
			if (!cableList.isEmpty()) {
				beginUpdate();
				try {
					for (Cable c : cableList) {
						setDirectRenderingEnabled(c, false);
					}
				} catch (RuntimeException r) {
					endUpdate();
					throw r;
				}
				endUpdate();
				cableList.clear();
			}
		}

		public void mouseEntered(MouseEvent event) {
			// TODO Auto-generated method stub
			
		}

		public void mouseExited(MouseEvent event) {
			// TODO Auto-generated method stub
			
		}

		/*
		
		private boolean dragging = false;
		private ArrayList<Cable> affected = new ArrayList<Cable>();

		void setAffected(ModuleUI m)
		{
			affected.clear();
			for (NomadConnector a : new ConnectorIterator(m)) 
			{	// for each connector
				Connector c = a.getConnector();
				if (c!=null) {
					for ( Cable t : getTransitions().getTransitions(c)) {
						if (!affected.contains(t)) affected.add(t);
					}
				}
			}
		}
		
		void startDragging(ModuleUI m) {
			if (isDragging()) stopDragging();
			dragging = true;
			setAffected(m);
			setUnmanaged(affected);
		}
		
		void stopDragging() {
			if (isDragging()) {
				dragging = false;
				affected.clear();
				resetUnmanaged();
			}
		}
		
		boolean isDragging() {
			return dragging;
		}

		void updateAffected() 
		{
			updateCurves(affected);
		}
		
		public void componentMoved(ComponentEvent event) {
			if (isDragging()) {
				updateAffected();
			} else if (event.getComponent() instanceof ModuleUI) {
				stopDragging();
				setAffected((ModuleUI) event.getComponent());
				updateAffected();
				affected.clear();
			}
		}

		public void mousePressed(MouseEvent event) {
			if (SwingUtilities.isLeftMouseButton(event) && event.getComponent() instanceof ModuleUI)
			{
				startDragging((ModuleUI)event.getComponent());
			}
		}

		public void mouseReleased(MouseEvent event) {
			stopDragging();
		}

		public void mouseClicked(MouseEvent event) { }
		public void mouseEntered(MouseEvent event) { }
		public void mouseExited(MouseEvent event) { }
		*/
	}
}
