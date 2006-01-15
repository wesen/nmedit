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

import org.nomad.theme.NomadClassicColors;
import org.nomad.theme.property.BooleanProperty;
import org.nomad.theme.property.ConnectorProperty;
import org.nomad.theme.property.Property;
import org.nomad.theme.property.PropertyValueHandler;
import org.nomad.theme.property.editor.PropertyEditor;
import org.nomad.xml.dom.module.DConnector;

public class NomadConnector extends NomadComponent {

	private ArrayList connectorChangeListenerList = new ArrayList();
	private boolean flagConnected = false;
	private boolean flagIsInput = false;
	private DConnector connectorInfo = null;
	private ConnectorColorProperty colorProperty = null;
	
	public NomadConnector() {
		super();
		
		addConnectorChangeListener(
				new ChangeListener() {
					public void stateChanged(ChangeEvent event) 
					{	deleteOnScreenBuffer();
						repaint(); }
				}
		);
		
		setDynamicOverlay(true);

		setPreferredSize(new Dimension(13,13));
		setMinimumSize(new Dimension(13,13));
		setMaximumSize(new Dimension(13,13));
		setSize(new Dimension(13,13));
		
		getAccessibleProperties().add(new ConnectedStateProperty(this));
		getAccessibleProperties().add(new ConnectorTypeProperty(this));
		getAccessibleProperties().add(colorProperty=new ConnectorColorProperty(this));
		getAccessibleProperties().add(new ConnectorProperty(this));
	}
	
	public void setConnectorInfo(DConnector connectorInfo) {
		this.connectorInfo = connectorInfo;
		if (connectorInfo!=null) {
			setConnectorType(connectorInfo.isInput());
			colorProperty.setValueFromSignal(connectorInfo.getSignal());
			deleteOnScreenBuffer();
		}
	}
	
	public DConnector getConnectorInfo() {
		return connectorInfo;
	}
	
	private class ConnectorColorProperty extends Property {
		public SignalType type = null;
		public ConnectorColorProperty(NomadComponent component) {
			super(component);
			setValueFromSignal(new SignalType(DConnector.SIGNAL_AUDIO));
			setName("type");
			
			setHandler(null, new PropertyValueHandler() {

				public void writeValue(Object value) throws IllegalArgumentException {
					if (value!=null)
						throw new IllegalArgumentException("Property "+this+" does not handle "+value);
				}});
			setHandler(SignalType.class, new PropertyValueHandler(){
				public void writeValue(Object value) throws IllegalArgumentException {
					setValueFromSignal((SignalType)value);
				}});
		}

		public Object getValue() {
			return type;
		}

		public void setValueFromSignal(int signal) {
			setValueFromSignal(new SignalType(signal));
		}

		public void setValueFromSignal(SignalType type) {
			this.type = type;
			
			switch (type.sig) {
				case DConnector.SIGNAL_AUDIO: setBackground(NomadClassicColors.MORPH_RED); break;
				case DConnector.SIGNAL_CONTROL: setBackground(NomadClassicColors.MORPH_BLUE); break;
				case DConnector.SIGNAL_LOGIC: setBackground(NomadClassicColors.MORPH_YELLOW); break;
				case DConnector.SIGNAL_SLAVE: setBackground(NomadClassicColors.MORPH_GRAY); break;
			}
			repaint();
		}
		
		public void setValueFromString(String value) {
			if (value.equals(DConnector.getSignalName(DConnector.SIGNAL_LOGIC))) setValueFromSignal(new SignalType(DConnector.SIGNAL_LOGIC));
			else if (value.equals(DConnector.getSignalName(DConnector.SIGNAL_AUDIO))) setValueFromSignal(new SignalType(DConnector.SIGNAL_AUDIO));
			else if (value.equals(DConnector.getSignalName(DConnector.SIGNAL_SLAVE))) setValueFromSignal(new SignalType(DConnector.SIGNAL_SLAVE));
			else if (value.equals(DConnector.getSignalName(DConnector.SIGNAL_CONTROL))) setValueFromSignal(new SignalType(DConnector.SIGNAL_CONTROL));
		}

		public PropertyEditor getEditor() {
			return new PropertyEditor.ComboBoxEditor(this, new SignalType[]{
				new SignalType(DConnector.SIGNAL_AUDIO),
				new SignalType(DConnector.SIGNAL_CONTROL),
				new SignalType(DConnector.SIGNAL_LOGIC),
				new SignalType(DConnector.SIGNAL_SLAVE),	
			});
		}
		
		private final class SignalType {
			String name;
			int sig;
			public SignalType(int sig) {
				name=DConnector.getSignalName(this.sig=sig);
			}
			
			public String toString() {
				return name;
			}
			
			public boolean equals(Object obj) {
				if (obj==null) return false;
				if (obj instanceof SignalType)
					return ((SignalType)obj).name.equals(name);
				return false;
			}
		}
		
	}
	
	private class ConnectedStateProperty extends BooleanProperty {
		public ConnectedStateProperty(NomadComponent component) {
			super(component);
			setName("isConnected");
		}
		public void setBooleanValue(boolean value) { setConnectedState(value); }
		public boolean getBoolean() { return isConnected(); }
	}
	
	private class ConnectorTypeProperty extends BooleanProperty {
		public ConnectorTypeProperty(NomadComponent component) {
			super(component); setName("isInput");
		}
		public void setBooleanValue(boolean value) { setConnectorType(value); }
		public boolean getBoolean() { return isInputConnector(); }
	}
	
	public void setConnectedState(boolean isConnected) {
		if (flagConnected!=isConnected) {
			flagConnected=isConnected;
			fireConnectorChangeEvent();
		}
	}
	
	public void setConnectorType(boolean isInput) {
		if (flagIsInput!=isInput) {
			flagIsInput=isInput;
			fireConnectorChangeEvent();
		}
	}
	
	public boolean isInputConnector() {
		return flagIsInput;
	}
	
	public boolean isConnected() {
		return flagConnected;
	}

	public void addConnectorChangeListener(ChangeListener l) {
		if (!connectorChangeListenerList.contains(l))
			connectorChangeListenerList.add(l);
	}
	
	public void removeConnectorChangeListener(ChangeListener l) {
		connectorChangeListenerList.remove(l);
	}
	
	public void fireConnectorChangeEvent(ChangeEvent event) {
		for (int i=connectorChangeListenerList.size()-1;i>=0;i--) {
			((ChangeListener)connectorChangeListenerList.get(i)).stateChanged(event);
		}
	}
	
	public void fireConnectorChangeEvent() {
		fireConnectorChangeEvent(new ChangeEvent(this));
	}
	
}
