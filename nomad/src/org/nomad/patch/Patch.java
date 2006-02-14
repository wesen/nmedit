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

public class Patch {

	private Header header ;
	private ModuleSection common_section ;
	private ModuleSection poly_section ;
	private MorphList morphList ;
	private ArrayList<Note> noteList ;
	private KnobMapList knobMapList ;
	private CtrlMapList ctrlMapList ;
	private String note = "";
	
	public Patch() {
		header = new Header();
		common_section = new ModuleSection(Section.COMMON);
		poly_section = new ModuleSection(Section.POLY);
		morphList = new MorphList();
		noteList = new ArrayList<Note>();
		knobMapList = new KnobMapList();
		ctrlMapList = new CtrlMapList();
	}

	public String getNote() {
		return note;
	}
	
	public void setNote(String note) {
		this.note = note==null?"":note;
	}
	
	public Header getHeader() {
		return header;
	}

	public ModuleSection getCommonSection() {
		return common_section;
	}

	public ModuleSection getPolySection() {
		return poly_section;
	}

	public MorphList getMorphList() {
		return morphList;
	}
	
	public ArrayList<Note> getNotes() {
		return noteList;
	}
	
	public KnobMapList getKnobMapList() {
		return knobMapList;
	}
	
	public CtrlMapList getCtrlMapList() {
		return ctrlMapList;
	}
	
}
