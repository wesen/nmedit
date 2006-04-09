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
 * Created on Feb 28, 2006
 */
package org.nomad.patch.format;

import java.util.EnumSet;

import org.nomad.patch.format.parser.Parser;
import org.nomad.patch.format.parser.Record;

public class PatchFormat {
	
	public enum Section {

		CableDump			("CableDump"),
		CtrlMapDump			("CtrlMapDump"),
		CurrentNoteDump		("CurrentNoteDump"),
		CustomDump			("CustomDump"),
		Header				("Header"),
		KeyboardAssignment	("KeyboardAssignment"),
		KnobMapDump			("KnobMapDump"),
		ModuleDump			("ModuleDump"),
		MorphMapDump		("MorphMapDump"),
		NameDump			("NameDump"),
		Notes				("Notes"),
		ParameterDump		("ParameterDump");
		
		private final String name ;
		
		private Section(String name) {
			this.name = name ;
		}
		
		public String getName() {
			return name ;
		}

		public static EnumSet<Section> all() {
			return EnumSet.allOf(Section.class);
		}
		
		public static EnumSet<Section> info() {
			return EnumSet.of(Header, Notes)  ;
		}		
	}

	// header_data
	public final static int HEADER_KEYBOARD_RANGE_MIN 				= 0;
	public final static int HEADER_KEYBOARD_RANGE_MAX 				= 1;
	public final static int HEADER_VELOCITY_RANGE_MIN 				= 2;
	public final static int HEADER_VELOCITY_RANGE_MAX 				= 3;
	public final static int HEADER_BEND_RANGE 						= 4;
	public final static int HEADER_PORTAMENTO_TIME 					= 5;
	public final static int HEADER_PORTAMENTO 						= 6;
	public final static int HEADER_REQUESTED_VOICES 				= 7;
	public final static int HEADER_SECTION_SEPARATOR_POSITION 		= 8;
	public final static int HEADER_OCTAVE_SHIFT 					= 9;
	public final static int HEADER_VOICE_RETRIGGER_POLY 			= 10;
	public final static int HEADER_VOICE_RETRIGGER_COMMON 			= 11;
	public final static int HEADER_UNKNOWN1 						= 12;
	public final static int HEADER_UNKNOWN2 						= 13;
	public final static int HEADER_UNKNOWN3 						= 14;
	public final static int HEADER_UNKNOWN4 						= 15;
	public final static int HEADER_CABLE_VISIBILITY_RED 			= 16;
	public final static int HEADER_CABLE_VISIBILITY_BLUE 			= 17;
	public final static int HEADER_CABLE_VISIBILITY_YELLOW 			= 18;
	public final static int HEADER_CABLE_VISIBILITY_GRAY 			= 19;
	public final static int HEADER_CABLE_VISIBILITY_GREEN 			= 20;
	public final static int HEADER_CABLE_VISIBILITY_PURPLE 			= 21;
	public final static int HEADER_CABLE_VISIBILITY_WHITE 			= 22;

	//moduleDump
	public final static int MODULE_DUMP_MODULE_INDEX 				= 0;
	public final static int MODULE_DUMP_MODULE_TYPE  				= 1;
	public final static int MODULE_DUMP_MODULE_XPOS  				= 2;
	public final static int MODULE_DUMP_MODULE_YPOS					= 3;
	
	// currentNoteDump
	public final static int CURRENT_NOTE_DUMP_NOTE 					= 0;
	public final static int CURRENT_NOTE_DUMP_ATTACK_VELOCITY 		= 1;
	public final static int CURRENT_NOTE_DUMP_RELEASE_VELOCITY 		= 2;
	
	// cableDump
	public final static int CABLE_DUMP_COLOR 						= 0;
	public final static int CABLE_DUMP_MODULE_INDEX_DESTINATION 	= 1;
	public final static int CABLE_DUMP_CONNECTOR_INDEX_DESTINATION	= 2;
	public final static int CABLE_DUMP_CONNECTOR_TYPE_DESTINATION	= 3;
	public final static int CABLE_DUMP_MODULE_INDEX_SOURCE 			= 4;
	public final static int CABLE_DUMP_CONNECTOR_INDEX_SOURCE		= 5;
	public final static int CABLE_DUMP_CONNECTOR_TYPE_SOURCE		= 6;

	// parameterDump
	public final static int PARAMETER_DUMP_MODULE_INDEX				= 0;
	public final static int PARAMETER_DUMP_MODULE_TYPE				= 1;
	public final static int PARAMETER_DUMP_PARAMETER_COUNT			= 2;
	public final static int PARAMETER_DUMP_PARAMETER_BASE			= 3; // index of first parameter

	// customDump
	public final static int CUSTOM_DUMP_MODULE_INDEX				= 0;
	public final static int CUSTOM_DUMP_PARAMETER_COUNT				= 1;
	public final static int CUSTOM_DUMP_PARAMETER_BASE				= 2; // index of first parameter
	
	// morphMapDump - knob values
	public final static int MORPH_MAP_DUMP_VALUES_MORPH1			= 0;
	public final static int MORPH_MAP_DUMP_VALUES_MORPH2			= 1;
	public final static int MORPH_MAP_DUMP_VALUES_MORPH3			= 2;
	public final static int MORPH_MAP_DUMP_VALUES_MORPH4			= 3;

	// morphMapDump
	public final static int MORPH_MAP_DUMP_SECTION					= 0;
	public final static int MORPH_MAP_DUMP_MODULE_INDEX				= 1;
	public final static int MORPH_MAP_DUMP_PARAMETER_INDEX			= 2;
	public final static int MORPH_MAP_DUMP_MORPH_INDEX				= 3;
	public final static int MORPH_MAP_DUMP_MORPH_RANGE				= 4;
	
	// keyboardAssignment
	public final static int KEYBOARD_ASSIGNMENT_MORPH1				= 0;
	public final static int KEYBOARD_ASSIGNMENT_MORPH2				= 1;
	public final static int KEYBOARD_ASSIGNMENT_MORPH3				= 2;
	public final static int KEYBOARD_ASSIGNMENT_MORPH4				= 3;

	// knobMapDump
	public final static int KNOB_MAP_DUMP_SECTION_INDEX				= 0;
	public final static int KNOB_MAP_DUMP_MODULE_INDEX				= 1;
	public final static int KNOB_MAP_DUMP_PARAMETER_INDEX			= 2;
	public final static int KNOB_MAP_DUMP_KNOB_INDEX				= 3;
	
	// ctrlMapDump
	public final static int CTRL_MAP_DUMP_SECTION_INDEX				= 0;
	public final static int CTRL_MAP_DUMP_MODULE_INDEX				= 1;
	public final static int CTRL_MAP_DUMP_PARAMETER_INDEX			= 2;
	public final static int CTRL_MAP_DUMP_CC_INDEX					= 3;

	// nameDump
	public final static int NAME_DUMP_MODULE_INDEX					= 0;
	public final static int NAME_DUMP_MODULE_NAME					= 1;
	

	public final static int VERSION_LOW = 0;
	public final static int VERSION_HIGH = 0;
	
	public static void parseVersionString(Version version, String str) {
		// string length
		int len = str.length();
		
		// result values

		int fNameStart  = 0;
		int fNameStop   = 0;
		int versionHigh = 0;
		int versionLow  = 0;

		Record r = new Record ();
		r.slen = len ;
		r.spos = len -1 ;
		
		r.spos = Parser.ignoreWhitespaceRewind(str, r.spos) ; // ignore whitespace from right to left
		versionLow = Parser.parseDigitsRewind(str, r) ; // digit sequence
		// may be -1
		if (!Parser.parseCharacterRewind(str, '.', r)) {
			// error
		}
		versionHigh = Parser.parseDigitsRewind(str, r);
		r.spos = Parser.ignoreWhitespaceRewind(str, r.spos) ;

		
		fNameStart = Parser.ignoreWhitespace( str, 0, len );
		fNameStop = r.spos ;
		
		version.setVersion(versionHigh, versionLow);
		version.setFormatName(str.substring(fNameStart, fNameStop)) ;
	}
	
	public class Version {
		
		private int versionHigh;
		private int versionLow;
		private String formatName;

		public Version(Version version) {
			this (version.versionHigh, version.versionLow, version.formatName) ;
		}

		public Version(int versionHigh, int versionLow, String formatName) {
			this.versionHigh = versionHigh ;
			this.versionLow  = versionLow  ;
			this.formatName  = formatName  ;
		}

		public void setVersion(int versionHigh, int versionLow) {
			this.versionHigh = versionHigh;
			this.versionLow  = versionLow ; 
		}
		
		public String toString() {
			return formatName+" "+versionHigh+"."+versionLow ;
		}

		public String getFormatName() {
			return formatName;
		}

		public void setFormatName(String formatName) {
			this.formatName = formatName;
		}

		public int getVersionHigh() {
			return versionHigh;
		}

		public void setVersionHigh(int versionHigh) {
			this.versionHigh = versionHigh;
		}

		public int getVersionLow() {
			return versionLow;
		}

		public void setVersionLow(int versionLow) {
			this.versionLow = versionLow;
		}
		
	}
	
}
