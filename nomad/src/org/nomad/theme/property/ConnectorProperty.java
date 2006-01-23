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
 * Created on Jan 12, 2006
 */
package org.nomad.theme.property;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.nomad.theme.component.NomadComponent;
import org.nomad.theme.component.NomadConnector;
import org.nomad.theme.property.editor.PropertyEditor;
import org.nomad.xml.dom.module.DConnector;
import org.nomad.xml.dom.module.DModule;
import org.nomad.xml.dom.module.ModuleDescriptions;

public class ConnectorProperty extends Property {

	public ConnectorProperty(NomadComponent component) {
		super(component);
		setName("connector#0");
	}
	public void setupForEditing() {
		super.setupForEditing();
		setHandler(null, new PropertyValueHandler(){
			public void writeValue(Object value) throws IllegalArgumentException {
				// we accept null values
				
				if (value!=null)
					throw new IllegalArgumentException("Illegal argument "+value+" in property "+this+".");
				
				setDConnector(null);
				
			}});
		setHandler(DConnector.class, new PropertyValueHandler(){
			public void writeValue(Object value) throws IllegalArgumentException {
				setDConnector((DConnector)value);
			}});
	}

	public Object getValue() {
		return getDConnector();
	}

	public void setValueFromString(String value) {
		setDConnector(decode(value));
	}
	
	private final static Pattern pattern = Pattern.compile("(\\d+)\\.(\\d+)\\.((in)|(out))\\..*");
	
	public static DConnector decode(String value) {
		Matcher m = pattern.matcher(value);
		if (!m.matches()) {
			System.err.println("Connector pattern not matched by "+value);
			return null;
		}

		int moduleId = Integer.parseInt(m.group(1));
		int connectorId = Integer.parseInt(m.group(2));
		String inout = m.group(3);

		boolean isOut = inout.equals("out");

		DModule module = ModuleDescriptions.sharedInstance().getModuleById(moduleId);
		if (module==null) {
			System.err.println("In ConnectorProperty.decode(): Module [id="+moduleId+"] not found");
			return null;
		}

		return module.getConnectorById(connectorId, !isOut);
	}

	public static String encode(DConnector p) {
		if (p==null) return "";
		return p.getParent().getModuleID()+"."+p.getId()+"."+(p.isInput()?"in":"out")+"."+p.getName();
	}
	
	public String getValueString() {
		return encode(getDConnector());
	}
	
	public void setDConnector(DConnector p) {
		if (getComponent() instanceof NomadConnector)
			((NomadConnector) getComponent()).setConnectorInfo(p);
	}
	
	public DConnector getDConnector() {
		if (getComponent() instanceof NomadConnector)
			return ((NomadConnector) getComponent()).getConnectorInfo();
		return null;
	}

	public DConnector[] findConnectors() {
		DModule info = findModuleInfo();
		DConnector[] list = null;
		if (info != null) {
			list = new DConnector[info.getConnectorCount()];
			for (int i=0;i<info.getConnectorCount();i++)
				list[i] = info.getConnector(i);
		}
		return list;
	}

	public String[] findAndEncodeConnectors() {
		DConnector[] conn = findConnectors();
		if (conn!=null) {
			String[] list = new String[conn.length];
			for (int i=conn.length-1;i>=0;i--)
				list[i]=encode(conn[i]);
			return list;
		}
		return null;
	}

	public PropertyEditor getEditor() {
		String[] list = findAndEncodeConnectors();
		if (list==null)
			return super.getEditor();
		else
			return new PropertyEditor.ComboBoxEditor(this, list, getValueString());
	}
}
