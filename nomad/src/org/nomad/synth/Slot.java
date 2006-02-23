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
 * Created on Feb 20, 2006
 */
package org.nomad.synth;

import net.sf.nmedit.jnmprotocol.MidiException;
import net.sf.nmedit.jnmprotocol.ParameterMessage;

import org.nomad.patch.Connector;
import org.nomad.patch.Custom;
import org.nomad.patch.Module;
import org.nomad.patch.Parameter;
import org.nomad.patch.Patch;
import org.nomad.patch.event.ModuleChangeListener;
import org.nomad.patch.ui.ModuleSectionListener;

public class Slot implements ModuleSectionListener, ModuleChangeListener {

	private Synth synth;
	private Patch patch = null;
	private int pid = -1;
	private int slotId ;
	
	private ParameterMessage parameterMessage = null;

	public Slot(Synth synth, int slotId) {
		this.synth = synth;
		setPatch(new Patch());
		this.slotId = slotId;

		try {
			parameterMessage = new ParameterMessage();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Synth getSynth() {
		return synth;
	}

	public int getPid() {
		return pid;
	}
	
	void setPid(int pid) {
		this.pid = pid;
	}

	public int getId() {
		return slotId;
	}

	public Patch getPatch() {
		return patch;
	}
	
	public void setPatch(Patch patch) {
		
		if (patch!=null) {
			for (Module m : patch.getPolySection())
				m.removeModuleListener(this);
			for (Module m : patch.getCommonSection())
				m.removeModuleListener(this);
			
			patch.getPolySection().removeSectionListener(this);
			patch.getCommonSection().removeSectionListener(this);
		}
		
		this.patch = patch;
		
		if (patch!=null) {
			for (Module m : patch.getPolySection())
				m.addModuleListener(this);
			for (Module m : patch.getCommonSection())
				m.addModuleListener(this);
			
			patch.getPolySection().addSectionListener(this);
			patch.getCommonSection().addSectionListener(this);
		}
		
		getSynth().fireNewPatchInSlot(this);
	}

	public void moduleAdded(Module module) {
		module.addModuleListener(this);
	}

	public void moduleRemoved(Module module) {
		module.removeModuleListener(this);
	}
	
	public void parameterChanged(Module module, Parameter parameter) {
		parameterMessage.set("slot", slotId);
		parameterMessage.set("pid", pid);
		parameterMessage.set("module", module.getIndex());
		parameterMessage.set("section", module.getModuleSection().getIndex());
		parameterMessage.set("parameter",parameter.getInfo().getContextId());
		parameterMessage.set("value",parameter.getValue());
		try {
			getSynth().getDevice().send(parameterMessage);
		} catch (Exception e) {
			//System.err.println(e);
			e.printStackTrace();
			
			if (e instanceof MidiException) {
				System.err.println("MidiException:code="+((MidiException)e).getError());
			}
			
		}
	}

	public void customChanged(Module module, Custom custom) {
		// TODO Auto-generated method stub
	}

	public void connectorChanged(Module module, Connector connector) {
		// TODO Auto-generated method stub
	}

	public void locationChanged(Module module) {
		// TODO Auto-generated method stub
	}

	public void moduleSectionResized() {
		// TODO Auto-generated method stub
		
	}

	public void rearangingModules(boolean finished) {
		// TODO Auto-generated method stub
		
	}
	
}
