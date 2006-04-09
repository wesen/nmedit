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

public class Assignable {

	private Parameter parameter;
	private Morph morph ;
	
	public Assignable() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Parameter getParameter() {
		return parameter;
	}
	
	public Morph getMorph() {
		return morph;
	}
	
	public boolean isAssignedToParameter() {
		return getParameter()!=null;
	}
	
	public boolean isAssignedToMorph() {
		return getMorph()!=null;
	}
	
	public boolean isAssigned() {
		return isAssignedToParameter() || isAssignedToMorph();
	}

	public void assignTo(Parameter parameter) {
		if (this.parameter != parameter) {
			this.parameter = parameter;
			if (parameter!=null) morph = null;
		}
	}

	public void assignTo(Morph morph) {
		if (this.morph != morph) {
			this.morph = morph;
			if (morph!=null) parameter = null;
		}
	}
	
	public void removeAssignment() {
		morph = null;
		parameter = null;
	}
	
	public ModuleSectionType getSectionIndex() {
		if (isAssignedToParameter()) {
			return getParameter().getModule().getModuleSection().getType();
		} else if (isAssignedToMorph()) { 
			return ModuleSectionType.MORPH;
		}
		return null;
	}
	
	public int getModuleIndex() {
		if (isAssignedToParameter()) {
			return getParameter().getModule().getIndex();
		} else if (isAssignedToMorph()) { 
			return 1;
		}
		return 0;
	}
	
	public int getParameterIndex() {
		if (isAssignedToParameter()) {
			return getParameter().getIndex();
		} else if (isAssignedToMorph()) { 
			return getMorph().getIndex();
		}
		return -1;
	}
}
