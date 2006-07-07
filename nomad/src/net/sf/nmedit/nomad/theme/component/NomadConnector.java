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
package net.sf.nmedit.nomad.theme.component;

import java.awt.event.MouseEvent;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Connector;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Module;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.ConnectorEvent;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.EventListener;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.DConnector;
import net.sf.nmedit.nomad.patch.ui.ModuleUI;
import net.sf.nmedit.nomad.theme.NomadClassicColors;
import net.sf.nmedit.nomad.theme.property.ConnectorProperty;
import net.sf.nmedit.nomad.theme.property.PropertySet;


public class NomadConnector extends NomadComponent implements EventListener<ConnectorEvent> {

	private boolean flagConnected = false;
	private boolean flagIsInput = false;
	private DConnector connectorInfo = null;
	private Connector connector = null;

	public NomadConnector() {
		super();
        enableEvents(MouseEvent.MOUSE_EVENT_MASK);
		//setColorFromSignal(DConnector.SIGNAL_AUDIO);
        setDefaultSize(13,13);
		setSize(13,13);
		setFocusable(true);
	}
    
    protected void processMouseEvent(MouseEvent event)
    {
        if (event.getID() == MouseEvent.MOUSE_PRESSED)
        {
            if (event.isPopupTrigger() && event.getComponent() == this) 
            {
                Connector c = getConnector();
                ModuleUI m = (ModuleUI) getParent();
                m.showConnectorPopup(event, c);
            }
        }
        
        super.processMouseEvent(event);
    }
    
	public void registerProperties(PropertySet set) {
		super.registerProperties(set);
		set.add(new ConnectorProperty());
	}
	
	public void setConnectorInfo(DConnector connectorInfo) {
		this.connectorInfo = connectorInfo;
		if (connectorInfo!=null) {
			setConnectorType(connectorInfo.isInput(), false);
			setColorFromSignal(connectorInfo.getSignal());
		}
	}
	
	public DConnector getConnectorInfo() {
		return connectorInfo;
	}
	
	private int sig=-1;
	
	protected void setColorFromSignal(int signal) {
		if (sig!=signal) {
			sig=signal;
			setBackground(NomadClassicColors.getConnectorColor(signal));
			repaint();
		}
	}
	
	public void setConnectedState(boolean connected) {
		if (flagConnected!=connected) {
			flagConnected=connected;
			repaint();
		}
	}
	
	public void setConnectorType(boolean isInput) {
		setConnectorType(isInput, true);
	}
	
	public void setConnectorType(boolean isInput, boolean fireEvent) {
		flagIsInput=isInput;
	}
	
	public boolean isInputConnector() {
		return flagIsInput;
	}
	
	public boolean isConnected() {
		return flagConnected;
	}

	public void link(Module module) {
		connector = module.getConnector(connectorInfo.getContextId());
		if (connector!=null) {
			this.setConnectedState(connector.isConnected());
            connector.setUI(this);
            connector.addListener(this);
		}
	}
	
	public void unlink() {
		if (connector!=null) {
            connector.removeListener(this);
            connector.setUI(null);
			connector = null;
		}
	}

	public Connector getConnector() {
		return connector;
	}

    public void event( ConnectorEvent event )
    {
        if (connector != null)
            setConnectedState(connector.isConnected());
    }
	
}
