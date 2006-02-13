package org.nomad.theme.curve;

import java.awt.Component;
import java.awt.Container;
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
import java.util.Iterator;

import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;

import org.nomad.patch.Connector;
import org.nomad.patch.Cables;
import org.nomad.patch.ui.ModuleUI;
import org.nomad.theme.component.NomadConnector;
import org.nomad.util.array.TransitionChangeListener;
import org.nomad.util.array.TransitionMatrix;
import org.nomad.util.iterate.ComponentIterator;

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
 * Created on Feb 1, 2006
 */

public class CurvePanel extends CurvePaintPanel implements TransitionChangeListener<Connector, CCurve> {

	private JLayeredPane root ;
	private Cables table = null;
	private RootContainerListener rcl = new RootContainerListener();
	private ConnectorListener connectorListener = new ConnectorListener();
	private ModuleListener moduleListener = new ModuleListener();
	private ArrayList<CurvePopupListener> curvePopupListenerList = new ArrayList<CurvePopupListener>();

	public CurvePanel(JLayeredPane root) {
		super(root);
		this.root = root;
		root.addContainerListener(rcl);
	}
	
	public void addCurvePopupListener(CurvePopupListener l) {
		if (!curvePopupListenerList.contains(l))
			curvePopupListenerList.add(l);
	}
	
	public void removeCurvePopupListener(CurvePopupListener l) {
		curvePopupListenerList.remove(l);
	}

	public void fireCurvePopupListenerEvent(MouseEvent mouseEvent, Connector connector) {
		CurvePopupEvent event = new CurvePopupEvent(mouseEvent, this, connector);
		for (int i=curvePopupListenerList.size()-1;i>=0;i--)
			curvePopupListenerList.get(i).popup(event);
	}
	
	public void transitionChanged(TransitionMatrix<Connector, CCurve> matrix, Connector a, Connector b, CCurve told, CCurve tnew) {
		if (told!=null) removeCurve( told );
		if (tnew!=null) addCurve( tnew ); // updateCurve(newCurve);
	}
	
	public Cables getTransitions() {
		return table;
	}

	public void setTable(Cables table) {
		if (this.table!=table) {
			// TODO clear current curves
			
			if (this.table!=null)
				this.table.removeChangeListener(this);
			this.table = table;
			if (table!=null) {
				table.addChangeListener(this);
				//newTable(table);
				for (Curve t : getTransitions()) addCurve(t);
			}
		}
	}
	
	public JLayeredPane getRoot() {
		return root;
	}

	// returns the location of the connector relative to the root origin
	protected Point getLocation(NomadConnector connector) {
		Point p = new Point(connector.getWidth()/2, connector.getHeight()/2);
		return SwingUtilities.convertPoint(connector, p, getRoot());
	}
	
	// find a NomadConnector at given location
	protected NomadConnector findConnectorAt(Point location) {
		return findConnectorAt(location.x, location.y);
	}
	
	protected NomadConnector findConnectorAt(int x, int y) {
		for (ModuleUI m : new ModuleIterator(getRoot())) 
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

	// add/remove listeners to ModuleGUIs 
	private class RootContainerListener implements ContainerListener {
		public void componentAdded(ContainerEvent event) {
			if (event.getChild() instanceof ModuleUI) {
				ModuleUI m = (ModuleUI) event.getChild();
				
				m.addComponentListener(moduleListener);
				m.addMouseListener(moduleListener);
				
				for (NomadConnector c : new ConnectorIterator(m)) {
					c.addMouseListener(connectorListener);
					c.addMouseMotionListener(connectorListener);
				}
			}
		}

		public void componentRemoved(ContainerEvent event) {
			if (event.getChild() instanceof ModuleUI) {
				ModuleUI m = (ModuleUI) event.getChild();

				m.removeComponentListener(moduleListener);
				m.removeMouseListener(moduleListener);
				
				for (NomadConnector c : new ConnectorIterator(m)) {
					c.removeMouseListener(connectorListener);
					c.removeMouseMotionListener(connectorListener);
				}
			}
		}
	}
	
	// create new cables
	private class ConnectorListener extends MouseAdapter implements MouseMotionListener {
		
		private NomadConnector start;
		
		boolean isDragging() {
			return start!=null;
		}
		
		void abortDragging() {
			setDraggedCurve(null);
			start = null;
		}
		
		void startDragging(NomadConnector start) {
			if (isDragging()) abortDragging();
			
			this.start = start;
			Point location = getLocation(start);
			Curve drag = new Curve(location, location);
			drag.setColor(getTransitions().determineColor(start.getConnector(), null));
			setDraggedCurve(drag);
		}
		
		void stopDragging(NomadConnector stop) {
			// isDragging() must be true
			NomadConnector start = this.start;
			abortDragging();
			CCurve curve = new CCurve(start, stop);
			curve.setCurve(getLocation(start), getLocation(stop));
			getTransitions().addTransition(curve);
		}
		
		Point getRootLocation(MouseEvent event) {
			return SwingUtilities.convertPoint(event.getComponent(), event.getPoint(), getRoot());
		}

		public void mousePressed(MouseEvent event) {
			abortDragging();
			
			if (event.isPopupTrigger())
			{
				NomadConnector connector = findConnectorAt(getRootLocation(event));
				if (connector!=null && connector.getConnector()!=null) {
					fireCurvePopupListenerEvent(event, connector.getConnector());
				}
				return;
			}
			
			if (!SwingUtilities.isLeftMouseButton(event)) 
				return;
			
			if (event.getClickCount()==2)
				// TODO dbl-click drag cable
				;
			
			NomadConnector start = findConnectorAt(getRootLocation(event));
			if (start!=null) 
				startDragging(start);
		}

		public void mouseReleased(MouseEvent event) {
			if (isDragging()) 
			{
				NomadConnector stop = findConnectorAt(getRootLocation(event));
				if (stop==null)
					abortDragging();
				else
					stopDragging(stop);
			}
		}

		public void mouseDragged(MouseEvent event) {
			if (isDragging())
			{
				Curve drag = getDraggedCurve();
				drag.setP2(getRootLocation(event));
			}
		}

		public void mouseMoved(MouseEvent event) { }
	}

	public void updateCurves()
	{
		updateCurves(getTransitions());
	}
	
	public void updateCurves(Iterable<CCurve> curves)
	{
		for (Curve transition : curves)
		{			
			CCurve t = (CCurve) transition;
			
			if (t.getC1()!=null && t.getC2()!=null)
			{
				NomadConnector c1 = t.getC1().getUI();
				NomadConnector c2 = t.getC2().getUI();
				if (c1!=null && c2!=null) {
					t.setCurve(getLocation(c1), getLocation(c2));
				}
			}
		}
	}
	
	// if module location changes cables will be adjusted accordingly
	private class ModuleListener extends ComponentAdapter implements MouseListener {

		private boolean dragging = false;
		private ArrayList<CCurve> affected = new ArrayList<CCurve>();

		void setAffected(ModuleUI m)
		{
			affected.clear();
			for (NomadConnector a : new ConnectorIterator(m)) 
			{	// for each connector
				Connector c = a.getConnector();
				if (c!=null) {
					for (Iterator<Connector> iter=getTransitions().traverseDirect(c);iter.hasNext();)
					{
						CCurve t = getTransitions().getTransition(c, iter.next());
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
		
	}
	
	private class ConnectorIterator extends ComponentIterator<NomadConnector> {
		public ConnectorIterator(Container c) {
			super(NomadConnector.class, c);
		}	
	}
	
	private class ModuleIterator extends ComponentIterator<ModuleUI> {
		public ModuleIterator(Container c) {
			super(ModuleUI.class, c);
		}	
	}

}
