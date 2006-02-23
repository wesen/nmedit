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

import org.nomad.theme.component.NomadConnector;
import org.nomad.theme.property.editor.PropertyEditor;
import org.nomad.xml.dom.module.DConnector;
import org.nomad.xml.dom.module.DModule;
import org.nomad.xml.dom.module.ModuleDescriptions;

public class ConnectorProperty extends Property {

	public ConnectorProperty() {
		setName("connector#0");
	}

	public void setValue(String value) {
		setDConnector(decode(value));
	}
	
	//private final static Pattern pattern = Pattern.compile("(\\d+)\\.(\\d+)\\.((in)|(out))\\..*");
	
	public static DConnector decode(String value) {
		int moduleId = 0;
		int connectorId = 0;
		int input = -1; // 0 = output, 1 = input
		
		char[] str = value.toCharArray();

		int i = 0;
		// module id
		for(;i<str.length;i++) {
			if (Character.isDigit(str[i])) {
				moduleId *= 10;
				moduleId += (str[i]-'0');
			} else if (str[i]=='.') {
				i++;
				break;
			} else {
				System.err.println("Invalid DConnector encoding: '"+value+"'.");
				return null;
			}
		}

		// connector id
		for(;i<str.length;i++) {
			if (Character.isDigit(str[i])) {
				connectorId *= 10;
				connectorId += (str[i]-'0');
			} else if (str[i]=='.') {
				i++;
				break;
			} else {
				System.err.println("Invalid DConnector encoding: '"+value+"'.");
				return null;
			}
		}
		
		// (in | out)
		String sub = value.substring(i);
		if (sub.startsWith("in")) input = 1;
		else if (sub.startsWith("out")) input = 0;
		
		
		// validation TODO check that id's part of string has not length 0
		if (moduleId<0 || connectorId<0 || input<0) {
			System.err.println("Invalid DConnector encoding: '"+value+"'.");
			return null;
		}

		DModule module = ModuleDescriptions.sharedInstance().getModuleById(moduleId);
		if (module==null) {
			System.err.println("In ConnectorProperty.decode(): Module [id="+moduleId+"] not found");
			return null;
		}
		
		DConnector connector = module.getConnectorById(connectorId, input==1);
		if (connector == null){
			System.err.println("In ConnectorProperty.decode(): Module [id="+moduleId+"] has no Connector [ id="+connectorId+"].");
			return null;
		}

		return connector;
	}

	public static String encode(DConnector p) {
		if (p==null) return "";
		return p.getParent().getModuleID()+"."+p.getId()+"."+(p.isInput()?"in":"out")+"."+p.getName();
	}
	
	public String getValue() {
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
			return new PropertyEditor.ComboBoxEditor(this, list, getValue());
	}
}
