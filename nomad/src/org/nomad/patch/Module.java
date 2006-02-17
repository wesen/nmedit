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

import java.awt.Point;
import java.util.ArrayList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.nomad.env.Environment;
import org.nomad.patch.format.PatchConstructionException;
import org.nomad.patch.ui.ModuleSectionUI;
import org.nomad.patch.ui.ModuleUI;
import org.nomad.xml.dom.module.DConnector;
import org.nomad.xml.dom.module.DCustom;
import org.nomad.xml.dom.module.DModule;
import org.nomad.xml.dom.module.DParameter;

public class Module {

	private DModule info;
	private int index = -1;
	private ModuleUI ui;

	private Parameter[] parameters ;
	private Custom[] customs ;
	private Connector[] connectors ;
	private int x;
	private int y;
	private String name;
	private ModuleSection moduleSection = null;

	private ArrayList<ChangeListener> locationChangeListenerList ;
	
	public Module(DModule info) {
		this.info = info;
		
		name = info.getName();
		
		parameters = new Parameter[getInfo().getParameterCount()];
		for (int i=parameters.length-1;i>=0;i--)
			parameters[i] = new Parameter(getInfo().getParameter(i), this);

		
		customs = new Custom[getInfo().getCustomParamCount()];
		for (int i=customs.length-1;i>=0;i--)
			customs[i] = new Custom(getInfo().getCustomParam(i), this);

		
		connectors = new Connector[getInfo().getConnectorCount()];
		for (int i=connectors.length-1;i>=0;i--)
			connectors[i] = new Connector(getInfo().getConnector(i), this);
		
		locationChangeListenerList = new ArrayList<ChangeListener>(2);
	}

	public ModuleUI getUI() {
		return ui;
	}

	public ModuleUI newUI(ModuleSectionUI moduleSection) {
    	return ui = Environment.sharedInstance().getBuilder().createGUI(this, moduleSection);
	}
	
	void setModuleSection(ModuleSection moduleSection) {
		this.moduleSection = moduleSection;
	}
	
	public ModuleSection getModuleSection() {
		return moduleSection;
	}

	public int getParameterCount() {
		return getInfo().getParameterCount();
	}
	
	public int getCustomCount() {
		return getInfo().getCustomParamCount();
	}
	
	public int getConnectorCount() {
		return getInfo().getConnectorCount();
	}
	
	public Parameter getParameter(int index) {
		return Module.<Parameter>get(parameters, index);
	}
	
	public Custom getCustom(int index) {
		return Module.<Custom>get(customs, index);
	}
	
	public Parameter[] getParameters() {
		return parameters;
	}
	
	public Custom[] getCustoms() {
		return customs;
	}
	
	public Connector[] getConnectors() {
		return connectors;
	}
	
	protected static <T> T get(T[] array, int index) {
		if (index<0 || index>=array.length)
			return null;
		else
			return array[index];
	}
	
	public Connector getConnector(int index) {
		return Module.<Connector>get(connectors, index);
	}
	
	public DModule getInfo() { 
		return info;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setLocation(Point location) {
		setLocation(location.x, location.y);
	}

	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
		fireLocationChangedEvent();
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
		fireLocationChangedEvent();
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
		fireLocationChangedEvent();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setParameterValues(int[] parameter_values) throws PatchConstructionException {
		if (getParameterCount()!=parameter_values.length)
			throw new PatchConstructionException("Invalid number of parameter values");
		for (int i=parameter_values.length-1;i>=0;i--)
			getParameter(i).setValue(parameter_values[i]);
	}

	public void setCustomValues(int[] custom_values) throws PatchConstructionException {
		if (getCustomCount()!=custom_values.length) {
			throw new PatchConstructionException("Invalid number of custom values");
		} 
		for (int i=custom_values.length-1;i>=0;i--)
			getCustom(i).setValue(custom_values[i]);
	}

	public Connector findConnector(DConnector info) {
		return connectors[info.getContextId()];
	}

	public Parameter findParameter(DParameter info) {
		if (info instanceof DCustom) return null; // fix for the moment
		return parameters[info.getContextId()];
	}

	public void addLocationChangeListener(ChangeListener l) {
		if (!locationChangeListenerList.contains(l))
			locationChangeListenerList.add(l);
	}
	
	public void removeLocationChangeListener(ChangeListener l) {
		locationChangeListenerList.remove(l);
	}
	
	public void fireLocationChangedEvent() {
		if (!locationChangeListenerList.isEmpty()) {
			ChangeEvent event = new ChangeEvent(this);
			for (ChangeListener l : locationChangeListenerList)
				l.stateChanged(event);
		}
	}

}
