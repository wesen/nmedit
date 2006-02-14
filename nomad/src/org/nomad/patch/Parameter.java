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

import java.util.ArrayList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.nomad.xml.dom.module.DParameter;

public class Parameter {

	private ArrayList<ChangeListener> changeListenerList ;
	private DParameter info;
	private int value =  0;
	private int morph = -1;
	private int morph_range = 0;
	private int knob_assignment = -1;
	private Module module;

	public Parameter(DParameter info, Module module) {
		changeListenerList = new ArrayList<ChangeListener>();
		this.info = info;
		this.module = module;
	}
	
	public Module getModule() {
		return module;
	}

	public DParameter getInfo() {
		return info;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setMorph(int morph) {
		this.morph = morph;
	}

	public int getMorph() {
		return morph;
	}

	public boolean isMorphAssigned() {
		return getMorph()>=0;
	}
	
	public void setMorphRange(int morph_range) {
		this.morph_range = morph_range;
	}

	public int setMorphRange() {
		return morph_range;
	}
	
	public void setMorph(int morph, int morph_range) {
		setMorph(morph);
		setMorphRange(morph_range);
	}
	
	public int getKnobAssignment() {
		return knob_assignment;
	}
	
	public void setKnobAssignment(int knob) {
		this.knob_assignment = knob;
	}

	public int getIndex() {
		return getInfo().getId();
	}

	public void fireChangeEvent() {
		fireChangeEvent(new ChangeEvent(this));
	}

	public void fireChangeEvent(ChangeEvent event) {
		for (ChangeListener l : changeListenerList) {
			l.stateChanged(event);
		}
	}
	
	public void addChangeListener(ChangeListener l) {
		if (!changeListenerList.contains(l))
			changeListenerList.add(l);
	}
	
	public void removeChangeListener(ChangeListener l) {
		changeListenerList.remove(l);
	}
	
}
