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
import org.nomad.theme.component.NomadControl;
import org.nomad.theme.property.editor.PropertyEditor;
import org.nomad.xml.dom.module.DCustom;
import org.nomad.xml.dom.module.DModule;
import org.nomad.xml.dom.module.DParameter;
import org.nomad.xml.dom.module.ModuleDescriptions;

public class ParameterProperty extends Property {

	public ParameterProperty(NomadComponent component) {
		super(component);
		setName("parameter#0");
		setHandler(null, new PropertyValueHandler(){
			public void writeValue(Object value) throws IllegalArgumentException {
				// we accept null values

				if (value!=null)
					throw new IllegalArgumentException("Illegal argument "+value+" in property "+this+".");
				
				setDParameter(null);
				
			}});
		setHandler(DParameter.class, new PropertyValueHandler(){
			public void writeValue(Object value) throws IllegalArgumentException {
				setDParameter((DParameter)value);
			}});
		setHandler(DCustom.class, new PropertyValueHandler(){
			public void writeValue(Object value) throws IllegalArgumentException {
				setDParameter((DParameter)value);
			}});
	}

	public Object getValue() {
		return getDParameter();
	}

	public void setValueFromString(String value) {
		setDParameter(decode(value));
	}
	
	private final static Pattern pattern = Pattern.compile("(\\d+)\\.(\\d+)\\.([cp])\\..*");
	
	public static DParameter decode(String value) {
		Matcher m = pattern.matcher(value);
		if (!m.matches()) {			
			System.err.println("Parameter pattern not matched by "+value);
			return null;
		}
		String[] pieces = value.split("\\.");		
		int moduleId = Integer.parseInt(pieces[0]);
		int paramId = Integer.parseInt(pieces[1]);
		String type = pieces[2];
		boolean isCustom = type.equals("c");

		DModule module = ModuleDescriptions.model.getModuleById(moduleId);
		if (module==null){
			System.err.println("In ParameterProperty.decode(): Module [id="+moduleId+"] not found");
			return null;
		}
		if (isCustom) return module.getCustomParamById(paramId);
		else return module.getParameterById(paramId);
	}

	public static String encode(DParameter p) {
		if (p==null) return "";
		return p.getParent().getModuleID()+"."+p.getId()+"."+ (p instanceof DCustom?"c":"p")+"."+p.getName();
	}
	
	public String getValueString() {
		return encode(getDParameter());
	}
	
	public void setDParameter(DParameter p) {
		if (getComponent() instanceof NomadControl)
			((NomadControl) getComponent()).setParameterInfo(p);
	}
	
	public DParameter getDParameter() {
		if (getComponent() instanceof NomadControl)
			return ((NomadControl) getComponent()).getParameterInfo();
		return null;
	}

	public DParameter[] findParameters() {
		DModule info = findModuleInfo();
		DParameter[] list = null;
		if (info != null) {
			list = new DParameter[info.getParameterCount()];
			for (int i=0;i<info.getParameterCount();i++)
				list[i] = info.getParameter(i);
		}
		return list;
	}
	
	public String[] findAndEncodeParameters() {
		DParameter[] params = findParameters();
		if (params!=null) {
			String[] list = new String[params.length];
			for (int i=params.length-1;i>=0;i--)
				list[i]=encode(params[i]);
			return list;
		}
		return null;
	}

	public PropertyEditor getEditor() {
		String[] list = findAndEncodeParameters();
		if (list==null)
			return super.getEditor();
		else
			return new PropertyEditor.ComboBoxEditor(this, list, getValueString());
	}
	
}
