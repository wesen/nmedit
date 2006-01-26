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

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.nomad.patch.Connector;
import org.nomad.patch.Module;
import org.nomad.theme.NomadClassicColors;
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
		switch (signal) {
			case DConnector.SIGNAL_AUDIO: setBackground(NomadClassicColors.MORPH_RED); break;
			case DConnector.SIGNAL_CONTROL: setBackground(NomadClassicColors.MORPH_BLUE); break;
			case DConnector.SIGNAL_LOGIC: setBackground(NomadClassicColors.MORPH_YELLOW); break;
			case DConnector.SIGNAL_SLAVE: setBackground(NomadClassicColors.MORPH_GRAY); break;
		}
		repaint();
	}
	
	public void setConnectedState(boolean isConnected) {
		setConnectedState(isConnected, true);
	}
	
	public void setConnectedState(boolean isConnected, boolean fireEvent) {
		if (flagConnected!=isConnected) {
			flagConnected=isConnected;
			if (fireEvent) fireConnectorChangeEvent();
		}
	}
	
	public void setConnectorType(boolean isInput) {
		setConnectorType(isInput, true);
	}
	
	public void setConnectorType(boolean isInput, boolean fireEvent) {
		if (flagIsInput!=isInput) {
			flagIsInput=isInput;
			if (fireEvent) fireConnectorChangeEvent();
		}
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

	public void link() {
		Module module = getModule();
		if (module!=null) {
			connector = module.findConnector(getConnectorInfo());
			if (connector!=null) {
				// TODO link connector <-> ui
			}
		}
	}

}
