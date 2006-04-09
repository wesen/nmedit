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
package org.nomad.patch.format;

import org.nomad.patch.Assignable;
import org.nomad.patch.CableColor;
import org.nomad.patch.Cables;
import org.nomad.patch.Connector;
import org.nomad.patch.Header;
import org.nomad.patch.Module;
import org.nomad.patch.ModuleSection;
import org.nomad.patch.ModuleSectionType;
import org.nomad.patch.Morph;
import org.nomad.patch.Note;
import org.nomad.patch.Parameter;
import org.nomad.patch.Patch;
import org.nomad.patch.format.PatchConstructorCallback303.Validating;
import org.nomad.patch.ui.Cable;
import org.nomad.xml.dom.module.DConnector;
import org.nomad.xml.dom.module.DModule;
import org.nomad.xml.dom.module.ModuleDescriptions;

public class PatchBuilder extends Validating {

	private Patch patch;
	
	public PatchBuilder() {
		this.patch = new Patch();
	}

	public void patch_name(String name) throws PatchConstructionException {
		super.patch_name(name);
		patch.setName(name);
	}
	
	public Patch getPatch() {
		return patch;
	}
	
	public void notes(String note) throws PatchConstructionException {
		super.notes(note);
		getPatch().setNote(note);
	}

	public void header_data(int[] data) throws PatchConstructionException	{
		super.header_data(data);
		Header h = getPatch().getHeader();
		h.setUnknown1(data[PatchFile303.HEADER_UNKNOWN1]);
		h.setUnknown2(data[PatchFile303.HEADER_UNKNOWN2]);
		h.setUnknown3(data[PatchFile303.HEADER_UNKNOWN3]);
		h.setUnknown4(data[PatchFile303.HEADER_UNKNOWN4]);
	}

	public void header_keyboard_range(int min, int max) {
		getPatch().getHeader().setKeyboardRange(min, max);
	}

	public void header_velocity_range(int min, int max) {
		getPatch().getHeader().setVelocityRange(min, max);
	}

	public void header_portamento(int portamento_time, boolean portamento_auto) {
		getPatch().getHeader().setPortamento(portamento_time, portamento_auto);
	}

	public void header_voice_regtrigger(boolean poly_active, boolean common_active) {
		getPatch().getHeader().setVoiceRetriggerActive(poly_active, common_active);
	}

	public void header_cable_visibility(boolean cable_red_visible,
			boolean cable_blue_visible, boolean cable_yellow_visible,
			boolean cable_gray_visible, boolean cable_green_visible,
			boolean cable_purple_visible, boolean cable_white_visible) {
		Header h = getPatch().getHeader();
		h.setCableVisible(CableColor.RED, 	cable_red_visible);
		h.setCableVisible(CableColor.BLUE, 	cable_blue_visible);
		h.setCableVisible(CableColor.YELLOW,cable_yellow_visible);
		h.setCableVisible(CableColor.GRAY, 	cable_gray_visible);
		h.setCableVisible(CableColor.GREEN, cable_green_visible);
		h.setCableVisible(CableColor.PURPLE,cable_purple_visible);
		h.setCableVisible(CableColor.WHITE, cable_white_visible);
	}

	public void header_bend(int range) {
		getPatch().getHeader().setBendRange(range);
	}

	public void header_requested_voices(int count) {
		getPatch().getHeader().setRequestedVoices(count);
	}

	public void header_separator(int position) {
		getPatch().getHeader().setSeparatorPosition(position);
	}

	public void header_octave_shift(int position) {
		getPatch().getHeader().setOctaveShift(position);
	}

	public void moduleDump(boolean isPolySection, int moduleIndex,
			int moduleType, int xpos, int ypos) {
		DModule info = ModuleDescriptions.sharedInstance().getModuleById(moduleType);
		if (info==null) {
			System.err.println("Module Id "+moduleType+" unknown.");
			return;
		}
		Module module = new Module(info);
		module.setIndex(moduleIndex);
		module.setLocation(xpos, ypos);
		ModuleSection ms = 	(isPolySection ? getPatch().getPolySection() : getPatch().getCommonSection());
		ms.setRearangingEnabled(false);
		ms.add(module);
		ms.setRearangingEnabled(true);
	}

	public void morphMapDumpMorphKnobValues(int morph1, int morph2, int morph3, int morph4) {
		getPatch().getMorphList().setMorphValues(morph1, morph2, morph3, morph4);
	}

	public void nameDump(boolean isPolySection, int moduleIndex, String moduleName) {
		(isPolySection ? getPatch().getPolySection() : getPatch().getCommonSection())
			.get(moduleIndex)
			.setName(moduleName);
	}

	public void parameterDump(boolean isPolySection, int module_index, int module_type, int[] parameters) throws PatchConstructionException {
		(isPolySection ? getPatch().getPolySection() : getPatch().getCommonSection())
			.get(module_index)
			.setParameterValues(parameters);
	}

	public void customDump(boolean isPolySection, int module_index, int[] customs) throws PatchConstructionException {
		(isPolySection ? getPatch().getPolySection() : getPatch().getCommonSection())
			.get(module_index)
			.setCustomValues(customs);
	}

	public void keyboardAssignment(int morph1, int morph2, int morph3, int morph4) {
		getPatch().getMorphList().setKeyboardAssignments(morph1, morph2, morph3, morph4);
	}

	public void cableDump(boolean isPolySection, int color,
			int dst_module_index, int dst_connector_index,
			boolean dst_connector_isInput, int src_module_index,
			int src_connector_index, boolean src_connector_isInput) {
		ModuleSection section = isPolySection ? getPatch().getPolySection() : getPatch().getCommonSection();
		
		Module mdst = section.get(dst_module_index);
		Module msrc = section.get(src_module_index);

		if (mdst==null) {
			System.err.println("Module[index="+dst_module_index+"] not found.");
			return ;
		}
		
		if (msrc==null) {
			System.err.println("Module[index="+src_module_index+"] not found.");
			return ;
		}
		
		DConnector idst = mdst.getInfo().getConnectorById(dst_connector_index, dst_connector_isInput);
		DConnector isrc = msrc.getInfo().getConnectorById(src_connector_index, src_connector_isInput);

		if (idst==null||isrc==null) {
			System.err.println("Error in CableDump");
			return;
		}
		
		// TODO (input | output) connector ?
		Connector cdst = mdst.findConnector(idst);
		Connector csrc = msrc.findConnector(isrc);

		if (cdst==null||csrc==null) {
			System.err.println("Error in CableDump");
			return;
		}
		
		Cable cable = new Cable(cdst, csrc);
		CableColor c = CableColor.byColorId(color);
		cable.setColor(c==null?CableColor.WHITE : CableColor.byColorId(color));
        Cables d = section.getCables();
		d.setColoringEnabled(false);
		d.addValidTransition(cable);
		d.setColoringEnabled(true);
	}

	public void morphMapDump(boolean isPolySection, int module_index,
			int parameter_index, int morph_index, int morph_range)
			throws PatchConstructionException {
		
		(isPolySection ? getPatch().getPolySection() : getPatch().getCommonSection())
			.get(module_index)
			.getParameter(parameter_index)
			.setMorph(morph_index, morph_range);
	}

	public void currentNoteDump(int noteNumber, int attack_velocity, int release_velocity) {
		getPatch().getNotes().add(new Note(noteNumber, attack_velocity, release_velocity));
	}

	public void knobMapDump(int section, int module_index, int parameter_index, int knob_index) {
		assign(getPatch().getKnobMapList().getKnobMap(knob_index), section, module_index, parameter_index);
	}

	public void ctrlMapDump(int section, int module_index, int parameter_index, int cc_index) {
		assign(getPatch().getCtrlMapList().getCtrl(cc_index), section, module_index, parameter_index);
	}

	protected boolean assign(Assignable assignable, int section, int module_index, int parameter_index) {
		switch (ModuleSectionType.bySectionId(section)) {
			case COMMON: {
				Module module = getPatch().getCommonSection().get(module_index);
				Parameter parameter = module.getParameter(parameter_index);
				assignable.assignTo(parameter);
				return true;
			} 
			case POLY: {
				Module module = getPatch().getPolySection().get(module_index);
				Parameter parameter = module.getParameter(parameter_index);
				assignable.assignTo(parameter);
				return true;
			} 
			case MORPH: {
				Morph morph = getPatch().getMorphList().get(parameter_index);
				assignable.assignTo(morph);
				return true;
			} 
			default: return false;
		}
	}
	
}
