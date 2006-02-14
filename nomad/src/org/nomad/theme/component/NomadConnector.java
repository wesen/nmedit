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
 * Created on Jan 11, 2006
 */
package org.nomad.theme.component;

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.nomad.patch.Connector;
import org.nomad.patch.Module;
import org.nomad.patch.Cables;
import org.nomad.patch.ui.ModuleSectionUI;
import org.nomad.theme.NomadClassicColors;
import org.nomad.theme.curve.CurvePanel;
import org.nomad.theme.property.ConnectorProperty;
import org.nomad.theme.property.PropertySet;
import org.nomad.xml.dom.module.DConnector;

public class NomadConnector extends NomadComponent {

	private ArrayList<ChangeListener> connectorChangeListenerList = null;
	private boolean flagConnected = false;
	private boolean flagIsInput = false;
	private DConnector connectorInfo = null;
	private Connector connector = null;

	public NomadConnector() {
		super();
		setColorFromSignal(DConnector.SIGNAL_AUDIO);
		setDynamicOverlay(true);
		Dimension d = new Dimension(13,13);
		setPreferredSize(d);
		setMinimumSize(d);
		setMaximumSize(d);
		setSize(d);
	}
	
	protected CurvePanel getCurvePanel() {
		Component c = getParent();
		if (c!=null) {
			c = c.getParent();
			if (c!=null && c instanceof ModuleSectionUI) {
				return ((ModuleSectionUI) c).getCurvePanel();
			}
		}
		return null;
	}

	protected void createProperties(PropertySet set) {
		super.createProperties(set);
		set.add(new ConnectorProperty(this));
		//getAccessibleProperties().rewriteDefaults();
	}
	
	public void setConnectorInfo(DConnector connectorInfo) {
		this.connectorInfo = connectorInfo;
		if (connectorInfo!=null) {
			setConnectorType(connectorInfo.isInput(), false);
			setColorFromSignal(connectorInfo.getSignal());
			fireConnectorChangeEvent();
		}
	}
	
	public DConnector getConnectorInfo() {
		return connectorInfo;
	}
	
	protected void setColorFromSignal(int signal) {
		setBackground(NomadClassicColors.getConnectorColor(signal));
		repaint();
	}
	
	public void setConnectedState(boolean isConnected) {
		setConnectedState(isConnected, true);
	}
	
	public void setConnectedState(boolean isConnected, boolean fireEvent) {
		flagConnected=isConnected;
		if (fireEvent) fireConnectorChangeEvent();
	}
	
	public void setConnectorType(boolean isInput) {
		setConnectorType(isInput, true);
	}
	
	public void setConnectorType(boolean isInput, boolean fireEvent) {
		flagIsInput=isInput;
		if (fireEvent) fireConnectorChangeEvent();
	}
	
	public boolean isInputConnector() {
		return flagIsInput;
	}
	
	public boolean isConnected() {
		return flagConnected;
	}

	public void addConnectorChangeListener(ChangeListener l) {
		if (connectorChangeListenerList==null)
			connectorChangeListenerList = new ArrayList<ChangeListener>();
		
		if (!connectorChangeListenerList.contains(l))
			connectorChangeListenerList.add(l);
	}
	
	public void removeConnectorChangeListener(ChangeListener l) {
		if (connectorChangeListenerList.remove(l))
			if (connectorChangeListenerList.size()==0)
				connectorChangeListenerList=null;
	}
	
	public void fireConnectorChangeEvent(ChangeEvent event) {
		if (connectorChangeListenerList==null) return;
		
		for (ChangeListener l : connectorChangeListenerList ) {
			l.stateChanged(event);
		}
	}
	
	public void fireConnectorChangeEvent() {
		fireConnectorChangeEvent(new ChangeEvent(this));
	}

	public void addTransition(NomadConnector c) {
		getTransitionTable().addTransition(connector, c.connector);
	}

	public ArrayList<Connector> getComposite() {
		if (connector!=null) {
			Cables t = getTransitionTable();
			if (t!=null) {
				return t.getLinked(connector);
			}
		}
		return new ArrayList<Connector>();
	}

	public boolean hasTransition(NomadConnector stop) {
		if (connector!=null) {
			Cables t = getTransitionTable();
			if (t!=null) {
				return t.hasTransition(this.connector, stop.connector);
			}
		}
		return false;
	}
	
	public Connector getCableCompositeOutput() {
		Cables t = getTransitionTable();
		return t == null ? null : t.getOutput(connector);
	}
	
	protected Cables getTransitionTable() {
		Module m = getModule();
		return (m==null) ? null : m.getModuleSection().getCables();
	}
	
	public void link(Module module) {
		connector = module.findConnector(getConnectorInfo());
		if (connector!=null) {
			connector.setUI(this);
			// TODO link connector <-> ui
		}
	}
	
	public void unlink() {
		if (connector!=null) {
			connector.setUI(this);
			connector = null;
		}
	}

	public Connector getConnector() {
		return connector;
	}
	
}
