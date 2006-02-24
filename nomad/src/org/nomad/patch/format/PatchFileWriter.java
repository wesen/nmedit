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
 * Created on Feb 15, 2006
 */
package org.nomad.patch.format;

import java.io.PrintWriter;
import java.util.ArrayList;

import org.nomad.patch.Connector;
import org.nomad.patch.Header;
import org.nomad.patch.Module;
import org.nomad.patch.ModuleSection;
import org.nomad.patch.MorphList;
import org.nomad.patch.Note;
import org.nomad.patch.Parameter;
import org.nomad.patch.Patch;
import org.nomad.patch.Section;
import org.nomad.patch.ui.Cable;

public class PatchFileWriter {

	protected Patch patch;
	protected PrintWriter out;
	protected String section="";

	public PatchFileWriter(PrintWriter out, Patch patch) {
		this.patch = patch;
		this.out = out;
	}
	
	public void write() {
		header();
		moduleDump(patch.getPolySection());
		moduleDump(patch.getCommonSection());
		currentNoteDump();
		cableDump(patch.getPolySection());
		cableDump(patch.getCommonSection());
		parameterDump(patch.getPolySection());
		parameterDump(patch.getCommonSection());
		customDump(patch.getPolySection());
		customDump(patch.getCommonSection());
		nameDump(patch.getPolySection());
		nameDump(patch.getCommonSection());
		morphMapDump();
		knobMapDump();
	}
	
	protected void morphMapDump() {
		beginSection(PatchFile303.NAME_MORPHMAP_DUMP);
		
		MorphList m=patch.getMorphList();
		println(m.getMorphValues());
		/*moduleDump(patch.getCommonSection());
		moduleDump(patch.getPolySection());*/
		println(Section.MORPH, m.getKeyboardAssignments());		
		endSection();
		
	}
	
	protected void morphMapDump(ModuleSection sec) {
		int[] data = new int[5];
		data[PatchFile303.MORPH_MAP_DUMP_SECTION]=sec.getIndex();
		for (Module m : sec.sortedModules()) {
			data[PatchFile303.MORPH_MAP_DUMP_MODULE_INDEX] = m.getIndex();
			for (int i=0;i<m.getParameterCount();i++) {
				Parameter p = m.getParameter(i);
				data[PatchFile303.MORPH_MAP_DUMP_PARAMETER_INDEX] = p.getIndex();
				data[PatchFile303.MORPH_MAP_DUMP_MORPH_INDEX] = p.getMorph();
				data[PatchFile303.MORPH_MAP_DUMP_MORPH_RANGE] = p.getMorphRange();
				println(data);
			}
		}
	}
	
	protected void knobMapDump() {
		beginSection(PatchFile303.NAME_KNOBMAP_DUMP);
		int[] data = new int[4];
		/*
		data[PatchFile303.KNOB_MAP_DUMP_SECTION_INDEX]=Section.MORPH;
		data[PatchFile303.KNOB_MAP_DUMP_MODULE_INDEX] = m.getIndex();
		data[PatchFile303.KNOB_MAP_DUMP_PARAMETER_INDEX] = p.getIndex();
		data[PatchFile303.KNOB_MAP_DUMP_KNOB_INDEX] = p.getKnobAssignment();
		*/
		endSection();
		
	}
	/*
	protected void knobMapDump(ModuleSection sec) {
		int[] data = new int[4];
		data[PatchFile303.KNOB_MAP_DUMP_SECTION_INDEX]=sec.getIndex();
		for (Module m : sec) {
			data[PatchFile303.KNOB_MAP_DUMP_MODULE_INDEX] = m.getIndex();
			for (int i=0;i<m.getParameterCount();i++) {
				Parameter p = m.getParameter(i);
				data[PatchFile303.KNOB_MAP_DUMP_PARAMETER_INDEX] = p.getIndex();
				data[PatchFile303.KNOB_MAP_DUMP_KNOB_INDEX] = p.getKnobAssignment();
				println(data);
			}
		}
	}
	*/
	protected void parameterDump(ModuleSection sec) {
		beginSection(PatchFile303.NAME_PARAMETER_DUMP);
		println(sec.getIndex());
		
		for (Module m : sec.sortedModules()) {
			
			int[] data = new int[m.getParameterCount()+3];
			data[0] = m.getIndex();
			data[1] = m.getInfo().getModuleID();
			data[2] = m.getParameterCount();
			
			for (int i=0;i<m.getParameterCount();i++)
				data[3+i]=m.getParameter(i).getValue();
			
			println(data);
		}
		
		endSection();
	}
	
	protected void customDump(ModuleSection sec) {
		beginSection(PatchFile303.NAME_CUSTOM_DUMP);
		println(sec.getIndex());
		
		for (Module m : sec.sortedModules()) {
			if (m.getCustomCount()>0) {
				int[] data = new int[m.getCustomCount()+PatchFile303.CUSTOM_DUMP_PARAMETER_BASE];
				data[PatchFile303.CUSTOM_DUMP_MODULE_INDEX] = m.getIndex();
				data[PatchFile303.CUSTOM_DUMP_PARAMETER_COUNT] = m.getCustomCount();
				
				for (int i=0;i<m.getCustomCount();i++)
					data[PatchFile303.CUSTOM_DUMP_PARAMETER_BASE+i]=m.getCustom(i).getValue();
				
				println(data);
			}
		}
		
		endSection();
	}

	protected void cableDump(ModuleSection sec) {
		beginSection(PatchFile303.NAME_CABLE_DUMP);
		println(sec.getIndex());
		
		int[] data = new int[7];
		for (Cable t : sec.getCables()) {
			
			Connector dst, src;
			if (t.getC1().getInfo().isInput()) {
				dst=t.getC1();
				src=t.getC2();
			} else {
				dst=t.getC2();
				src=t.getC1();
			}

			data[0] = t.getColorCode(); //t.getColor() TODO cable color
			data[1] = dst.getModule().getIndex();
			data[2] = dst.getInfo().getId();
			data[3] = bool(dst.getInfo().isOutput());
			
			data[4] = src.getModule().getIndex();
			data[5] = src.getInfo().getId();
			data[6] = bool(src.getInfo().isOutput());
			
			println(data);
		}
		
		endSection();
	}

	protected void currentNoteDump() {
		beginSection(PatchFile303.NAME_CURRENTNOTE_DUMP);
		
		// Todo a line contains 6 entries - see BUG description
		
		ArrayList<Note> notes = patch.getNotes(); 
		final int block=3;
		int[] data = new int[notes.size()*block]; 
		int index = 0;
		
		for (Note n:notes) {
			data[index+0]=n.getNoteNumber();
			data[index+1]=n.getAttackVelocity();
			data[index+2]=n.getReleaseVelocity();
			index+=block;
		}

		println(data);
		
		endSection();
	}
	
	protected void moduleDump(ModuleSection sec) {
		beginSection(PatchFile303.NAME_MODULE_DUMP);
		println(sec.getIndex());
		
		for (Module m : sec) {
			println(new int[]{
			m.getIndex(),
			m.getInfo().getModuleID(),
			m.getX(),
			m.getY()});
		}
		
		endSection();
	}
	
	protected void nameDump(ModuleSection sec) {
		beginSection(PatchFile303.NAME_NAME_DUMP);
		println(sec.getIndex());
		
		for (Module m : sec.sortedModules()) {
			println(m.getIndex()+" "+m.getName());
		}
		
		endSection();
	}
	
	private void header() {
		beginSection(PatchFile303.NAME_HEADER);
		version();
		headerDump();
		endSection();
	}
	
	protected void version(){
		println("Version=Nord Modular patch 3.0");
	}

	protected void headerDump() {
		int[] data = new int[23];
		Header h = patch.getHeader();
		
		data[PatchFile303.HEADER_KEYBOARD_RANGE_MIN]=h.getKeyboardRangeMin();
		data[PatchFile303.HEADER_KEYBOARD_RANGE_MAX]=h.getKeyboardRangeMax();
		data[PatchFile303.HEADER_VELOCITY_RANGE_MIN]=h.getVelocityRangeMin();
		data[PatchFile303.HEADER_VELOCITY_RANGE_MAX]=h.getVelocityRangeMax();
		data[PatchFile303.HEADER_BEND_RANGE]=h.getBendRange();
		data[PatchFile303.HEADER_PORTAMENTO_TIME]=h.getPortamentoTime();
		data[PatchFile303.HEADER_PORTAMENTO]=bool(h.isPortamentoAutoEnabled());
		data[PatchFile303.HEADER_REQUESTED_VOICES]=h.getRequestedVoices();
		data[PatchFile303.HEADER_SECTION_SEPARATOR_POSITION]=h.getSeparatorPosition();
		data[PatchFile303.HEADER_OCTAVE_SHIFT]=h.getOctaveShift();
		data[PatchFile303.HEADER_VOICE_RETRIGGER_POLY]  =bool(h.isVoiceRetriggerPolyActive());
		data[PatchFile303.HEADER_VOICE_RETRIGGER_COMMON]=bool(h.isVoiceRetriggerCommonActive());
		data[PatchFile303.HEADER_UNKNOWN1]=h.getUnknown1();
		data[PatchFile303.HEADER_UNKNOWN2]=h.getUnknown2();
		data[PatchFile303.HEADER_UNKNOWN3]=h.getUnknown3();
		data[PatchFile303.HEADER_UNKNOWN4]=h.getUnknown4();
		data[PatchFile303.HEADER_CABLE_VISIBILITY_RED]		=bool(h.isCableVisible(Header.CABLE_RED));
		data[PatchFile303.HEADER_CABLE_VISIBILITY_BLUE]		=bool(h.isCableVisible(Header.CABLE_BLUE));
		data[PatchFile303.HEADER_CABLE_VISIBILITY_YELLOW]	=bool(h.isCableVisible(Header.CABLE_YELLOW));
		data[PatchFile303.HEADER_CABLE_VISIBILITY_GRAY]		=bool(h.isCableVisible(Header.CABLE_GRAY));
		data[PatchFile303.HEADER_CABLE_VISIBILITY_GREEN]	=bool(h.isCableVisible(Header.CABLE_GREEN));
		data[PatchFile303.HEADER_CABLE_VISIBILITY_PURPLE]	=bool(h.isCableVisible(Header.CABLE_PURPLE));
		data[PatchFile303.HEADER_CABLE_VISIBILITY_WHITE]	=bool(h.isCableVisible(Header.CABLE_WHITE));
		
		println(data);
	}
	
	protected int bool(boolean b) {
		return b ? 1 : 0;
	}
	
	protected void println(String line) {
		out.println(line);
	}
	
	protected void beginSection(String title) {
		section = title;
		println("["+title+"]");
	}
	
	protected void endSection() {
		println("[/"+section+"]");
	}

	protected void println(int data) {
		println(new int[]{data});
	}

	protected void println(int leading, int[] data) {
		out.print(leading+" ");
		println(data);
	}
	
	protected void println(int[] data) {
		String line="";
		for(int i:data)
			line+=i+" ";
		println(line);
	}
	
}
