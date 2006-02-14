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
 * Created on Feb 12, 2006
 */
package org.nomad.patch.format;

import org.nomad.patch.CtrlMap;


public interface PatchFileCallback303 {

	void header_version	(String version) throws PatchFileException;
	void header_data	(int[] data) throws PatchFileException;
	void moduleDump		(boolean isPolySection, int[] data) throws PatchFileException;
	void currentNoteDump(int[] data) throws PatchFileException;
	void cableDump		(boolean isPolySection, int[] data) throws PatchFileException;
	void parameterDump	(boolean isPolySection, int[] data) throws PatchFileException;
	void customDump		(boolean isPolySection, int[] data) throws PatchFileException;
	void morphMapDumpMorphKnobValues(int[] morphKnobValues) throws PatchFileException;
	void morphMapDump	(int[] morph) throws PatchFileException;
	void keyboardAssignment(int[] data) throws PatchFileException;
	void knobMapDump	(int[] data) throws PatchFileException;
	void ctrlMapDump	(int[] data) throws PatchFileException;
	void nameDump		(boolean isPolySection, int moduleIndex, String moduleName) throws PatchFileException; 
	void notes			(String notes) throws PatchFileException;
	void unrecognizedSection(String header, String content) throws PatchFileException;

	public class Adapter implements PatchFileCallback303 {

		private String versionName = "";
		private String notes = "";
		private int[] versionNumber = new int[] {-1, -1};

		public String getVersionName()	{ return versionName; }
		public String getNotes() 	{ return notes; }
		public int getMinorVersion()	{ return versionNumber[0]; }
		public int getMajorVersion()	{ return versionNumber[1]; }
		
		public void header_version(String version) 
			throws PatchFileException {
			if (version!=null) {
				PatchFile303.extractVersionNumber(versionNumber,   version);
				this.versionName = PatchFile303.extractVersionName(version);
			}
		}

		public void notes(String note) throws PatchFileException {
			this.notes = note;
		}

		public void header_data(int[] data)								throws PatchFileException { }
		public void moduleDump(boolean isPolySection, int[] data)		throws PatchFileException { }
		public void currentNoteDump(int[] data)							throws PatchFileException { }
		public void cableDump(boolean isPolySection, int[] data)		throws PatchFileException { }
		public void parameterDump(boolean isPolySection, int[] data)	throws PatchFileException { }
		public void customDump(boolean isPolySection, int[] data)		throws PatchFileException { }
		public void morphMapDumpMorphKnobValues(int[] morphKnobValues)	throws PatchFileException { }
		public void morphMapDump(int[] morph)							throws PatchFileException { }
		public void keyboardAssignment(int[] data)						throws PatchFileException { }
		public void knobMapDump(int[] data)								throws PatchFileException { }
		public void ctrlMapDump(int[] data)								throws PatchFileException { }
		public void unrecognizedSection(String header, String content)	throws PatchFileException { }
		public void nameDump(boolean isPolySection, int moduleIndex, String moduleName)	
																		throws PatchFileException { }

	}

	public abstract class Validating extends Adapter {

		private boolean validating = true;
		
		public boolean isValidating() { return validating; }
		public void setValidatingEnabled(boolean enable) {	this.validating = enable; }
		
		protected void validateRange(int min, int max, int value, String name)	throws PatchFileException {
			if (min>value || value>max) 
				throw new PatchFileException("out of range: '"+name+"'");
		}

		protected void validateMin(int min, int value, String name) throws PatchFileException {
			if (min>value)
				throw new PatchFileException("out of range: '"+name+"'");
		}

		protected void validateEq(int eq, int value, String name) throws PatchFileException {
			if (eq>value)
				throw new PatchFileException("not equal: '"+name+"'");
		}
		
		public void header_data(int[] data) throws PatchFileException {
			
			if (isValidating())
			{
				validateRange( 0, 127, data[PatchFile303.HEADER_KEYBOARD_RANGE_MIN], "keyboard range min");
				validateRange( data[PatchFile303.HEADER_KEYBOARD_RANGE_MIN], 127, 
					data[PatchFile303.HEADER_KEYBOARD_RANGE_MAX], "keyboard range max");
				validateRange( 0, 24, data[PatchFile303.HEADER_BEND_RANGE], "bend range");
				validateRange( 0, 127, data[PatchFile303.HEADER_PORTAMENTO_TIME], "portamento time");
				validateRange( 0, 1, data[PatchFile303.HEADER_PORTAMENTO], "portamento");
				validateRange( 0, 32, data[PatchFile303.HEADER_REQUESTED_VOICES], "requested voices");
				validateRange( 0, 4000, data[PatchFile303.HEADER_SECTION_SEPARATOR_POSITION], "section separator");
				validateRange( 0, 4, data[PatchFile303.HEADER_OCTAVE_SHIFT], "octave shift");
				validateRange( 0, 1, data[PatchFile303.HEADER_VOICE_RETRIGGER_POLY], "voice retrigger poly");
				validateRange( 0, 1, data[PatchFile303.HEADER_VOICE_RETRIGGER_COMMON], "voice retrigger common");
				validateRange( 0, 1, data[PatchFile303.HEADER_CABLE_VISIBILITY_RED], "cable visibility red");
				validateRange( 0, 1, data[PatchFile303.HEADER_CABLE_VISIBILITY_BLUE], "cable visibility blue");
				validateRange( 0, 1, data[PatchFile303.HEADER_CABLE_VISIBILITY_YELLOW], "cable visibility yellow");			
				validateRange( 0, 1, data[PatchFile303.HEADER_CABLE_VISIBILITY_GRAY], "cable visibility gray");
				validateRange( 0, 1, data[PatchFile303.HEADER_CABLE_VISIBILITY_GREEN], "cable visibility green");
				validateRange( 0, 1, data[PatchFile303.HEADER_CABLE_VISIBILITY_PURPLE], "cable visibility purple");
				validateRange( 0, 1, data[PatchFile303.HEADER_CABLE_VISIBILITY_WHITE], "cable visibility white");
			}
			
			header_keyboard_range( data[PatchFile303.HEADER_KEYBOARD_RANGE_MIN],
					data[PatchFile303.HEADER_KEYBOARD_RANGE_MAX]);
			header_velocity_range(data[PatchFile303.HEADER_VELOCITY_RANGE_MIN],
					data[PatchFile303.HEADER_VELOCITY_RANGE_MAX] );
			header_portamento( data[PatchFile303.HEADER_PORTAMENTO_TIME],
					data[PatchFile303.HEADER_PORTAMENTO] == 1);

			header_cable_visibility(
				data[PatchFile303.HEADER_CABLE_VISIBILITY_RED]==1,
				data[PatchFile303.HEADER_CABLE_VISIBILITY_BLUE]==1,
				data[PatchFile303.HEADER_CABLE_VISIBILITY_YELLOW]==1,
				data[PatchFile303.HEADER_CABLE_VISIBILITY_GRAY]==1,
				data[PatchFile303.HEADER_CABLE_VISIBILITY_GREEN]==1,
				data[PatchFile303.HEADER_CABLE_VISIBILITY_PURPLE]==1,
				data[PatchFile303.HEADER_CABLE_VISIBILITY_WHITE]==1
			);

			header_voice_regtrigger(
				data[PatchFile303.HEADER_VOICE_RETRIGGER_POLY]==1,
				data[PatchFile303.HEADER_VOICE_RETRIGGER_COMMON]==1
			);

			header_bend (data[PatchFile303.HEADER_BEND_RANGE]);
			header_requested_voices (data[PatchFile303.HEADER_REQUESTED_VOICES]);
			header_separator (data[PatchFile303.HEADER_SECTION_SEPARATOR_POSITION]);
			header_octave_shift (data[PatchFile303.HEADER_OCTAVE_SHIFT]);
		}

		public abstract void header_keyboard_range ( int min, int max ) throws PatchFileException;
		public abstract void header_velocity_range ( int min, int max ) throws PatchFileException;
		public abstract void header_portamento ( int portamento_time, boolean portamento_auto ) throws PatchFileException;
		public abstract void header_voice_regtrigger ( boolean poly_active, boolean common_active ) throws PatchFileException;

		public abstract void header_cable_visibility (
			boolean cable_red_visible,
			boolean cable_blue_visible,
			boolean cable_yellow_visible,
			boolean cable_gray_visible,
			boolean cable_green_visible,
			boolean cable_purple_visible,
			boolean cable_white_visible
		) throws PatchFileException;

		public abstract void header_bend(int range) throws PatchFileException;
		public abstract void header_requested_voices(int count) throws PatchFileException;
		public abstract void header_separator(int position) throws PatchFileException;
		public abstract void header_octave_shift(int position) throws PatchFileException;
		
		public void moduleDump(boolean isPolySection, int[] data) throws PatchFileException { 
			if (isValidating())
			{
				validateMin  (1, data[PatchFile303.MODULE_DUMP_MODULE_INDEX], "module index");
				validateRange(0, 127, data[PatchFile303.MODULE_DUMP_MODULE_TYPE], "module type");
				validateMin  (0, data[PatchFile303.MODULE_DUMP_MODULE_XPOS], "module x-position");
				validateMin  (0, data[PatchFile303.MODULE_DUMP_MODULE_YPOS], "module y-position");
			}
			moduleDump(
				isPolySection,
				data[PatchFile303.MODULE_DUMP_MODULE_INDEX],
				data[PatchFile303.MODULE_DUMP_MODULE_TYPE],
				data[PatchFile303.MODULE_DUMP_MODULE_XPOS],
				data[PatchFile303.MODULE_DUMP_MODULE_YPOS]
			);
		}
		
		public abstract void moduleDump(boolean isPolySection, 
			int moduleIndex, int moduleType, int xpos, int ypos ) ;
		
		public void currentNoteDump(int[] data) throws PatchFileException { 
			if (isValidating())
			{
				validateRange(0, 127, data[PatchFile303.CURRENT_NOTE_DUMP_NOTE], "note");
				validateRange(0, 127, data[PatchFile303.CURRENT_NOTE_DUMP_ATTACK_VELOCITY], "attack velocity");
				validateRange(0, 127, data[PatchFile303.CURRENT_NOTE_DUMP_RELEASE_VELOCITY], "release velocity");
			}
			
			currentNoteDump(
				data[PatchFile303.CURRENT_NOTE_DUMP_NOTE],
				data[PatchFile303.CURRENT_NOTE_DUMP_ATTACK_VELOCITY],
				data[PatchFile303.CURRENT_NOTE_DUMP_RELEASE_VELOCITY]
			);
		}
		
		public abstract void currentNoteDump(
			int note, int attack_velocity, int release_velocity
		) ;
		
		public void cableDump(boolean isPolySection, int[] data) throws PatchFileException { 
			if (isValidating())
			{
				validateRange(0, 6, data[PatchFile303.CABLE_DUMP_COLOR], "color");
				validateMin(1, data[PatchFile303.CABLE_DUMP_MODULE_INDEX_DESTINATION], "destination module index");
				validateMin(0, data[PatchFile303.CABLE_DUMP_CONNECTOR_INDEX_DESTINATION], "destination connector index");
				// data[PatchFile303.CABLE_DUMP_CONNECTOR_TYPE_DESTINATION ??? always 0
				validateRange(0, 1, data[PatchFile303.CABLE_DUMP_CONNECTOR_TYPE_DESTINATION], "destination connector type");
				validateMin(1, data[PatchFile303.CABLE_DUMP_MODULE_INDEX_SOURCE], "source module index");
				validateMin(0, data[PatchFile303.CABLE_DUMP_CONNECTOR_INDEX_SOURCE], "source connector index");
				validateRange(0, 1, data[PatchFile303.CABLE_DUMP_CONNECTOR_TYPE_SOURCE], "source connector type");
			}
			
			cableDump( isPolySection,
				data[PatchFile303.CABLE_DUMP_COLOR],
				data[PatchFile303.CABLE_DUMP_MODULE_INDEX_DESTINATION],
				data[PatchFile303.CABLE_DUMP_CONNECTOR_INDEX_DESTINATION],
				data[PatchFile303.CABLE_DUMP_CONNECTOR_TYPE_DESTINATION] == 0,
				data[PatchFile303.CABLE_DUMP_MODULE_INDEX_SOURCE],
				data[PatchFile303.CABLE_DUMP_CONNECTOR_INDEX_SOURCE],
				data[PatchFile303.CABLE_DUMP_CONNECTOR_TYPE_SOURCE] == 0
			);
		}

		
		public abstract void cableDump(boolean isPolySection, 
			int color, int dst_module_index, int dst_connector_index, boolean dst_connector_isInput,
			int src_module_index, int src_connector_index, boolean src_connector_isInput
		) throws PatchFileException;
		
		public void parameterDump(boolean isPolySection, int[] data) throws PatchFileException {
			if (isValidating())
			{
				validateMin(1, data[PatchFile303.PARAMETER_DUMP_MODULE_INDEX], "module index");
				validateRange(1, 127, data[PatchFile303.PARAMETER_DUMP_MODULE_TYPE], "module type"); 
				validateMin(1, data[PatchFile303.PARAMETER_DUMP_PARAMETER_COUNT], "parameter count");
				validateEq(data[PatchFile303.PARAMETER_DUMP_PARAMETER_COUNT], 
						data.length - PatchFile303.PARAMETER_DUMP_PARAMETER_BASE,
						"parameter count"
				);
			}
			
			int[] parameters = new int [data[PatchFile303.PARAMETER_DUMP_PARAMETER_COUNT]];
			
			for (int i=0;i<parameters.length;i++)
				parameters[i] = data[PatchFile303.PARAMETER_DUMP_PARAMETER_BASE+i];

			parameterDump(isPolySection, data[PatchFile303.PARAMETER_DUMP_MODULE_INDEX],
				data[PatchFile303.PARAMETER_DUMP_MODULE_TYPE], parameters );
		}

		public abstract void parameterDump(boolean isPolySection, 
			int module_index, int module_typem, int[] parameters) throws PatchFileException;

		public void customDump(boolean isPolySection, int[] data) throws PatchFileException {
			if (isValidating())
			{
				validateMin(1, data[PatchFile303.CUSTOM_DUMP_MODULE_INDEX], "module index");
				validateMin(1, data[PatchFile303.CUSTOM_DUMP_PARAMETER_COUNT], "custom count");
				validateEq(data[PatchFile303.CUSTOM_DUMP_PARAMETER_COUNT], 
					data.length - PatchFile303.CUSTOM_DUMP_PARAMETER_BASE,
					"custom count"
				);
			}
			int[] customs = new int [data[PatchFile303.CUSTOM_DUMP_PARAMETER_COUNT]];
			
			for (int i=0;i<customs.length;i++)
				customs[i] = data[PatchFile303.CUSTOM_DUMP_PARAMETER_BASE+i];

			customDump(isPolySection, data[PatchFile303.CUSTOM_DUMP_MODULE_INDEX], customs );
		}

		public abstract void customDump(boolean isPolySection, 
			int module_index, int[] customs) throws PatchFileException;

		public void morphMapDumpMorphKnobValues(int[] morphKnobValues) throws PatchFileException {
			if (isValidating())
			{ 
				validateRange(0, 127, morphKnobValues[PatchFile303.MORPH_MAP_DUMP_VALUES_MORPH1], "morph 1");
				validateRange(0, 127, morphKnobValues[PatchFile303.MORPH_MAP_DUMP_VALUES_MORPH2], "morph 2");
				validateRange(0, 127, morphKnobValues[PatchFile303.MORPH_MAP_DUMP_VALUES_MORPH3], "morph 3");
				validateRange(0, 127, morphKnobValues[PatchFile303.MORPH_MAP_DUMP_VALUES_MORPH4], "morph 4");
			}

			morphMapDumpMorphKnobValues(morphKnobValues[PatchFile303.MORPH_MAP_DUMP_VALUES_MORPH1],
					morphKnobValues[PatchFile303.MORPH_MAP_DUMP_VALUES_MORPH2],
					morphKnobValues[PatchFile303.MORPH_MAP_DUMP_VALUES_MORPH3],
					morphKnobValues[PatchFile303.MORPH_MAP_DUMP_VALUES_MORPH4]) ;
		}

		public abstract void morphMapDumpMorphKnobValues(int morph1, int morph2,
			int morph3, int morph4) throws PatchFileException;
		                
		
		public void morphMapDump(int[] data) throws PatchFileException {
			if (isValidating())
			{
				validateRange(0, 1, data[PatchFile303.MORPH_MAP_DUMP_SECTION], "section index");
				validateMin(1, data[PatchFile303.MORPH_MAP_DUMP_MODULE_INDEX], "module index");
				validateMin(0, data[PatchFile303.MORPH_MAP_DUMP_PARAMETER_INDEX], "parameter index");
				validateRange(0, 3, data[PatchFile303.MORPH_MAP_DUMP_MORPH_INDEX], "morph index");
				validateRange(-127, 127, data[PatchFile303.MORPH_MAP_DUMP_MORPH_RANGE], "morph range");
			}
			morphMapDump(
				data[PatchFile303.MORPH_MAP_DUMP_SECTION]==1,
				data[PatchFile303.MORPH_MAP_DUMP_MODULE_INDEX],
				data[PatchFile303.MORPH_MAP_DUMP_PARAMETER_INDEX],
				data[PatchFile303.MORPH_MAP_DUMP_MORPH_INDEX],
				data[PatchFile303.MORPH_MAP_DUMP_MORPH_RANGE]
			);
		}

		public abstract void morphMapDump(boolean isPolySection, int module_index,
				int parameter_index, int morph_index, int morph_range) throws PatchFileException;
		
		public void keyboardAssignment(int[] data) throws PatchFileException { 
			if (isValidating())
			{
				validateRange(0, 2, data[PatchFile303.KEYBOARD_ASSIGNMENT_MORPH1], "morph 1");
				validateRange(0, 2, data[PatchFile303.KEYBOARD_ASSIGNMENT_MORPH2], "morph 2");
				validateRange(0, 2, data[PatchFile303.KEYBOARD_ASSIGNMENT_MORPH3], "morph 3");
				validateRange(0, 2, data[PatchFile303.KEYBOARD_ASSIGNMENT_MORPH4], "morph 4");
			}
			keyboardAssignment(data[PatchFile303.KEYBOARD_ASSIGNMENT_MORPH1],
					data[PatchFile303.KEYBOARD_ASSIGNMENT_MORPH2],
					data[PatchFile303.KEYBOARD_ASSIGNMENT_MORPH3],
					data[PatchFile303.KEYBOARD_ASSIGNMENT_MORPH4] );
		}

		public abstract void keyboardAssignment(int morph1, int morph2, int morph3, int morph4) throws PatchFileException;
		
		public void knobMapDump(int[] data) throws PatchFileException {
			if (isValidating())
			{
				validateRange(0, 2, data[PatchFile303.KNOB_MAP_DUMP_SECTION_INDEX], "section index");
				validateMin(1, data[PatchFile303.KNOB_MAP_DUMP_MODULE_INDEX], "module index");
				// validateMin(1, ... described in format is incorrect
				validateMin(0, data[PatchFile303.KNOB_MAP_DUMP_PARAMETER_INDEX], "parameter index");
				if (!(((0<=data[PatchFile303.KNOB_MAP_DUMP_KNOB_INDEX])
				      &&(data[PatchFile303.KNOB_MAP_DUMP_KNOB_INDEX]<=17))
				      ||(data[PatchFile303.KNOB_MAP_DUMP_KNOB_INDEX]==19)
				      ||(data[PatchFile303.KNOB_MAP_DUMP_KNOB_INDEX]==20)
				      ||(data[PatchFile303.KNOB_MAP_DUMP_KNOB_INDEX]==22))
				) throw new PatchFileException("out of range: 'knob index'");
			}
			
			knobMapDump(
				data[PatchFile303.KNOB_MAP_DUMP_SECTION_INDEX],
				data[PatchFile303.KNOB_MAP_DUMP_MODULE_INDEX],
				data[PatchFile303.KNOB_MAP_DUMP_PARAMETER_INDEX],
				data[PatchFile303.KNOB_MAP_DUMP_KNOB_INDEX]
			);
		}

		public abstract void knobMapDump(int section, int module_index, int parameter_index,
			int knob_index) throws PatchFileException;
		
		public void ctrlMapDump(int[] data) throws PatchFileException {
			if (isValidating())
			{
				validateRange(0, 2, data[PatchFile303.CTRL_MAP_DUMP_SECTION_INDEX], "section index");
				validateMin(1, data[PatchFile303.CTRL_MAP_DUMP_MODULE_INDEX], "module index");
				validateMin(0, data[PatchFile303.CTRL_MAP_DUMP_PARAMETER_INDEX], "parameter index");
				
				if (!CtrlMap.isValidCC(data[PatchFile303.CTRL_MAP_DUMP_CC_INDEX]))
					throw new PatchFileException("out of range: 'cc index'");
			}
			
			ctrlMapDump(
				data[PatchFile303.CTRL_MAP_DUMP_SECTION_INDEX],
				data[PatchFile303.CTRL_MAP_DUMP_MODULE_INDEX],
				data[PatchFile303.CTRL_MAP_DUMP_PARAMETER_INDEX],
				data[PatchFile303.CTRL_MAP_DUMP_CC_INDEX]
			);
			
		}

		public abstract void ctrlMapDump(int section,
			int module_index, int parameter_index, int cc_index) throws PatchFileException ;
		
		public abstract void nameDump(boolean isPolySection, int moduleIndex, String moduleName);
		
	}

}
