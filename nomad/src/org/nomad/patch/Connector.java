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
 * Created on Feb 13, 2006
 */
package org.nomad.patch;

import org.nomad.theme.component.NomadConnector;
import org.nomad.xml.dom.module.DConnector;

public class Connector {

	private DConnector info;
	private Module module;
	private NomadConnector ui = null;
	private boolean stateConnected = false;

	public Connector(DConnector info, Module module) {
		this.info = info;
		this.module = module;
	}

	public void removeCables() {
		getModule().getModuleSection().getCables().removeNode(this);
	}

	public DConnector getInfo() {
		return info;
	}

	public Module getModule() {
		return module;
	}

	public NomadConnector getUI() {
		return ui;
	}

	public void setUI(NomadConnector connectorUI) {
		this.ui = connectorUI;
	}

	public boolean isConnected() {
		return stateConnected;
	}
	
	public void setConnected(boolean connected) {
		if (stateConnected!=connected) {
			this.stateConnected = connected;
			if (ui!=null)
				ui.setConnectedState(connected);
			getModule().fireConnectorChangeEvent(this);
		}
	}

}
