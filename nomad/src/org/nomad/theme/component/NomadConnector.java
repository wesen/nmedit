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
import org.nomad.theme.property.BooleanProperty;
import org.nomad.theme.property.ConnectorProperty;
import org.nomad.theme.property.Property;
import org.nomad.theme.property.PropertySet;
import org.nomad.theme.property.PropertyValueHandler;
import org.nomad.theme.property.editor.PropertyEditor;
import org.nomad.xml.dom.module.DConnector;

public class NomadConnector extends NomadComponent {

	private ArrayList<ChangeListener> connectorChangeListenerList = null;
	private boolean flagConnected = false;
	private boolean flagIsInput = false;
	private DConnector connectorInfo = null;
	private Connector connector = null;

	public NomadConnector() {
		super();
		setDynamicOverlay(true);
		Dimension d = new Dimension(13,13);
		setPreferredSize(d);
		setMinimumSize(d);
		setMaximumSize(d);
		setSize(d);
	}

	protected void createProperties(PropertySet set) {
		super.createProperties(set);
		
		set.add(new ConnectedStateProperty(this));
		set.add(new ConnectorTypeProperty(this));
		set.add(new ConnectorColorProperty(this));
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
	
	private class ConnectorColorProperty extends Property {
		public SignalType type = null;
		public ConnectorColorProperty(NomadComponent component) {
			super(component);
			setValueFromSignal(new SignalType(DConnector.SIGNAL_AUDIO));
			setName("type");
		}

		public Object getValue() {
			return type;
		}

		public void setValueFromSignal(int signal) {
			setValueFromSignal(new SignalType(signal));
		}

		public void setValueFromSignal(SignalType type) {
			this.type = type;
			setColorFromSignal(type.sig);
		}
		
		public void setValueFromString(String value) {
			/**
			 * We check the strings ourselves.
			 * 
			 * logic
			 * audio
			 * slave
			 * control
			 * 
			 */
			
			if (value.length()>0) {
				switch (value.charAt(0)) {
					case 'l': if (value.equals(DConnector.getSignalName(DConnector.SIGNAL_LOGIC))) {
						setValueFromSignal(new SignalType(DConnector.SIGNAL_LOGIC));
					} break;	
					case 'a': if (value.equals(DConnector.getSignalName(DConnector.SIGNAL_AUDIO))) {
						setValueFromSignal(new SignalType(DConnector.SIGNAL_AUDIO));
					} break;
					case 's':if (value.equals(DConnector.getSignalName(DConnector.SIGNAL_SLAVE))) {
						setValueFromSignal(new SignalType(DConnector.SIGNAL_SLAVE));
					} break;
					case 'c':if (value.equals(DConnector.getSignalName(DConnector.SIGNAL_CONTROL))) {
						setValueFromSignal(new SignalType(DConnector.SIGNAL_CONTROL));
					} break;
				}
			}
		}
		
		public void setupForEditing() {
			super.setupForEditing();
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
		public void setBooleanValue(boolean value) { 
			setConnectedState(value); 
		}
		public boolean getBoolean() { return ((NomadConnector)getComponent()).isConnected(); }
		public boolean isExportable() {
			// we only export this property if no connector info is present
			return connectorInfo==null;
		}
	}
	
	private class ConnectorTypeProperty extends BooleanProperty {
		public ConnectorTypeProperty(NomadComponent component) {
			super(component); setName("isInput");
		}
		public void setBooleanValue(boolean value) { 
			setConnectorType(value); 
		}
		public boolean getBoolean() { return ((NomadConnector)getComponent()).isInputConnector(); }
		public boolean isExportable() {
			// we only export this property if no connector info is present
			return connectorInfo==null;
		}
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
