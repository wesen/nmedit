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
 * Created on Feb 16, 2006
 */
package org.nomad.patch.format;

import java.util.List;

import net.sf.nmedit.jnmprotocol.PatchMessage;
import net.sf.nmedit.jpdl.BitStream;
import net.sf.nmedit.jpdl.Packet;
import net.sf.nmedit.jpdl.PacketParser;
import net.sf.nmedit.jpdl.Protocol;
import net.sf.nmedit.jpdl.Tracer;

import org.nomad.patch.Section;

public class PatchMessageDecoder {
	
	private static PacketParser patchParser = null;
	private static Protocol patchProtocol = null;
	private static String patchPdlFile = null;

	public static void init() {
		try {
			usePDLFile("/usr/local/lib/nmpatch/patch.pdl", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void usePDLFile(String filename, Tracer tracer) throws Exception
	{
		patchPdlFile = filename;
		patchProtocol = new Protocol(patchPdlFile);
		patchParser = patchProtocol.getPacketParser("Patch");
		patchProtocol.useTracer(tracer);
	}

	public static void decode(PatchMessage message, PatchConstructorCallback303 cb) throws PatchConstructionException {
		decode(message.getPatchStream(), cb);
	}
	
	private static String extractName(Packet name)
	{
		  String result="";
		  List<Integer> chars = (List<Integer>) name.getVariableList("chars");
		  
		  for (Integer i : chars) {
			  if (i!=0)
				  result+=(char)i.intValue();
		  }
	  
		  return result;
	}

	//@SuppressWarnings("unchecked")
	public static void decode(BitStream patchStream, PatchConstructorCallback303 cb) throws PatchConstructionException {
		Packet packet = new Packet();
		
		if (patchParser.parse(patchStream, packet)) {
			while (packet != null) {
				Packet section = packet.getPacket("section");
				Packet sectionData = section.getPacket("data");
				switch (section.getVariable("type")) {

					// Name section
			      	case 55:
			      	case 39:
			      		cb.patch_name(extractName(sectionData.getPacket("name")));
			      		break;

			      	// Header section
			      	case 33: {
			      		int[] data = new int[23];

			      		data[PatchFile303.HEADER_KEYBOARD_RANGE_MIN]=sectionData.getVariable("krangemin");
						data[PatchFile303.HEADER_KEYBOARD_RANGE_MAX]=sectionData.getVariable("krangemax");
						data[PatchFile303.HEADER_VELOCITY_RANGE_MIN]=sectionData.getVariable("vrangemin");
						data[PatchFile303.HEADER_VELOCITY_RANGE_MAX]=sectionData.getVariable("vrangemax");
						data[PatchFile303.HEADER_PORTAMENTO_TIME]=sectionData.getVariable("ptime");
						data[PatchFile303.HEADER_PORTAMENTO] =sectionData.getVariable("portamento");
						data[PatchFile303.HEADER_CABLE_VISIBILITY_RED]=sectionData.getVariable("red");
						data[PatchFile303.HEADER_CABLE_VISIBILITY_BLUE]=sectionData.getVariable("blue");
						data[PatchFile303.HEADER_CABLE_VISIBILITY_YELLOW]=sectionData.getVariable("yellow");
						data[PatchFile303.HEADER_CABLE_VISIBILITY_GRAY]=sectionData.getVariable("gray");
						data[PatchFile303.HEADER_CABLE_VISIBILITY_GREEN]=sectionData.getVariable("green");
						data[PatchFile303.HEADER_CABLE_VISIBILITY_PURPLE]=sectionData.getVariable("purple");
						data[PatchFile303.HEADER_CABLE_VISIBILITY_WHITE]=sectionData.getVariable("white");
						data[PatchFile303.HEADER_VOICE_RETRIGGER_POLY]=sectionData.getVariable("pretrigger");
						data[PatchFile303.HEADER_VOICE_RETRIGGER_COMMON]=sectionData.getVariable("cretrigger");
						data[PatchFile303.HEADER_BEND_RANGE]=sectionData.getVariable("brange");
						data[PatchFile303.HEADER_REQUESTED_VOICES]=sectionData.getVariable("voices");
						data[PatchFile303.HEADER_SECTION_SEPARATOR_POSITION]=sectionData.getVariable("sspos");
						data[PatchFile303.HEADER_OCTAVE_SHIFT]=sectionData.getVariable("octave");

						cb.header_data(data);
			      	} break;

					// Module section
				    case 74: {
				    	int[] data = new int[4];
				    	boolean isPolySection = sectionData.getVariable("section") == Section.POLY;
				    	for (Packet p : (List<Packet>) sectionData.getPacketList("modules")) {
				    		data[PatchFile303.MODULE_DUMP_MODULE_INDEX] = p.getVariable("index");
					    	data[PatchFile303.MODULE_DUMP_MODULE_TYPE] = p.getVariable("type");
						    data[PatchFile303.MODULE_DUMP_MODULE_XPOS] = p.getVariable("xpos");
							data[PatchFile303.MODULE_DUMP_MODULE_YPOS] = p.getVariable("ypos");				    	
				    		cb.moduleDump(isPolySection, data);
				    	}
				    } break;

					// Note section
				    case 105: {
				    	int[] data = new int[3];
				    	for (int i=1;i<=2;i++) {
					    	Packet note = sectionData.getPacket("note"+i);
					    	data[PatchFile303.CURRENT_NOTE_DUMP_NOTE]=note.getVariable("value");
						    data[PatchFile303.CURRENT_NOTE_DUMP_ATTACK_VELOCITY]=note.getVariable("attack");
							data[PatchFile303.CURRENT_NOTE_DUMP_RELEASE_VELOCITY]=note.getVariable("release");
					    	cb.currentNoteDump(data);
				    	}
				    	
				    	for (Packet note: (List<Packet>) sectionData.getPacketList("notes")) {
				    		data[PatchFile303.CURRENT_NOTE_DUMP_NOTE]=note.getVariable("value");
						    data[PatchFile303.CURRENT_NOTE_DUMP_ATTACK_VELOCITY]=note.getVariable("attack");
							data[PatchFile303.CURRENT_NOTE_DUMP_RELEASE_VELOCITY]=note.getVariable("release");
					    	cb.currentNoteDump(data);
				    	}
					} break;

					// Cable section
				    case 82: {
				    	int[] data = new int[7];
				    	boolean isPolySection = sectionData.getVariable("section") == Section.POLY;

				    	for (Packet note: (List<Packet>) sectionData.getPacketList("cables")) {
					    	data[PatchFile303.CABLE_DUMP_COLOR]=note.getVariable("color");
					    	data[PatchFile303.CABLE_DUMP_MODULE_INDEX_DESTINATION]=note.getVariable("destination");
					    	data[PatchFile303.CABLE_DUMP_CONNECTOR_INDEX_DESTINATION]=note.getVariable("input");
					    	data[PatchFile303.CABLE_DUMP_MODULE_INDEX_SOURCE]=note.getVariable("source");
					    	data[PatchFile303.CABLE_DUMP_CONNECTOR_INDEX_SOURCE]=note.getVariable("inputOutput");
					    	data[PatchFile303.CABLE_DUMP_CONNECTOR_TYPE_SOURCE]=note.getVariable("type");
				    		
				    		cb.cableDump(isPolySection, data);
					  }
					} break;
					
					// Parameter section
				    case 77: {
					    boolean isPolySection = sectionData.getVariable("section") == Section.POLY;
				    	
				    	for (Packet modules: (List<Packet>) sectionData.getPacketList("parameters")) { 
				    		int module_index = modules.getVariable("index");
				    		
				    		Packet parameters = modules.getPacket("parameters");
				    		List<String> param = parameters.getAllVariables();
				    		
				    		int[] data = new int[3+param.size()];
				    		data[PatchFile303.PARAMETER_DUMP_MODULE_INDEX] = module_index;
				    		data[PatchFile303.PARAMETER_DUMP_MODULE_TYPE] = 1; // TODO read variable
				    		data[PatchFile303.PARAMETER_DUMP_PARAMETER_COUNT] = param.size();
				    		
 				    		for (int i=0;i<param.size();i++)
				    			data[PatchFile303.PARAMETER_DUMP_PARAMETER_BASE+i] = parameters.getVariable(param.get(i));// .intValue();//.getVariable("value");
			    			
				    		cb.parameterDump(isPolySection, data);
		    		
				    		param.clear();
				    	}
					} break;
					
					// Morphmap section
					case 101: {
						int[] data = new int[4];

						data[PatchFile303.MORPH_MAP_DUMP_VALUES_MORPH1] = sectionData.getVariable("morph1");
						data[PatchFile303.MORPH_MAP_DUMP_VALUES_MORPH2] = sectionData.getVariable("morph2");
						data[PatchFile303.MORPH_MAP_DUMP_VALUES_MORPH3] = sectionData.getVariable("morph3");
						data[PatchFile303.MORPH_MAP_DUMP_VALUES_MORPH4] = sectionData.getVariable("morph4");
						
						cb.morphMapDumpMorphKnobValues(data);

						data[PatchFile303.KEYBOARD_ASSIGNMENT_MORPH1] = sectionData.getVariable("keyboard1");
						data[PatchFile303.KEYBOARD_ASSIGNMENT_MORPH2] = sectionData.getVariable("keyboard2");
						data[PatchFile303.KEYBOARD_ASSIGNMENT_MORPH3] = sectionData.getVariable("keyboard3");
						data[PatchFile303.KEYBOARD_ASSIGNMENT_MORPH4] = sectionData.getVariable("keyboard4");
						
						cb.keyboardAssignment(data);
						
						data = new int[5];
						for (Packet p : (List<Packet>) sectionData.getPacketList("morphs")) {
							data[PatchFile303.MORPH_MAP_DUMP_MORPH_INDEX] = p.getVariable("morph");
							data[PatchFile303.MORPH_MAP_DUMP_SECTION] = p.getVariable("section");
							data[PatchFile303.MORPH_MAP_DUMP_MODULE_INDEX] = p.getVariable("module");
							data[PatchFile303.MORPH_MAP_DUMP_MORPH_RANGE] = p.getVariable("range"); //TODO out of range??? X-127 ???
							data[PatchFile303.MORPH_MAP_DUMP_PARAMETER_INDEX] = p.getVariable("parameter");
							
							cb.morphMapDump(data);
						}
						
					} break;
					// Knobmap section
				    case 98: {
				    	int[] data = new int[5];
				    	
				    	for (int i = 0; i < 23; i++) {
						    Packet knob = sectionData.getPacket("knob"+i);
						    boolean assigned = knob.getVariable("assigned")==1;
						    if (assigned) {
						    	Packet assignment = (Packet) knob.getPacketList("assignment").get(0); // .front()
								data[PatchFile303.KNOB_MAP_DUMP_SECTION_INDEX] = assignment.getVariable("section");
						    	data[PatchFile303.KNOB_MAP_DUMP_KNOB_INDEX] = i;
							    data[PatchFile303.KNOB_MAP_DUMP_MODULE_INDEX] = assignment.getVariable("module"); 
								data[PatchFile303.KNOB_MAP_DUMP_PARAMETER_INDEX] = assignment.getVariable("parameter");
						    	cb.knobMapDump(data);
						    }
				    	}
					} break;
					
					// Controlmap section
				    case 96: {
				    	int[] data = new int[4];
				    	for (Packet p : (List<Packet>) sectionData.getPacketList("controls")) {

				    		data[PatchFile303.CTRL_MAP_DUMP_SECTION_INDEX] = p.getVariable("section");
				    		data[PatchFile303.CTRL_MAP_DUMP_MODULE_INDEX] = p.getVariable("module");
				    		data[PatchFile303.CTRL_MAP_DUMP_PARAMETER_INDEX] = p.getVariable("parameter");
				    		data[PatchFile303.CTRL_MAP_DUMP_CC_INDEX] = p.getVariable("control");
				    		
				    		cb.ctrlMapDump(data);
					  }	  
					} break;
					
					// Custom section
				    case 91: {
					    boolean isPolySection = sectionData.getVariable("section") == Section.POLY;

				    	for (Packet modules: (List<Packet>) sectionData.getPacketList("customModules")) { 
				    		int module_index = modules.getVariable("index");

				    		Packet parameters = modules.getPacket("customValues");
				    		List<String> param = parameters.getAllVariables();
				    		
				    		int[] data = new int[2+param.size()];
				    		data[PatchFile303.CUSTOM_DUMP_MODULE_INDEX] = module_index;
				    		data[PatchFile303.CUSTOM_DUMP_PARAMETER_COUNT] = param.size();
				    		for (int i=0;i<param.size();i++)
				    			data[PatchFile303.CUSTOM_DUMP_PARAMETER_BASE+i] = parameters.getVariable(param.get(i));
				    			
				    		cb.customDump(isPolySection, data);
				    		
				    		param.clear();
				    	}
					} break;
					
					// Namedump section
				    case 90: {
					    boolean isPolySection = sectionData.getVariable("section") == Section.POLY;
					    for (Packet p : (List<Packet>)sectionData.getPacketList("moduleNames")) {
					    	cb.nameDump(isPolySection, p.getVariable("index"), extractName(p.getPacket("name")));
					    }
					}
					break;
				}

				packet = packet.getPacket("next");
		    } 
		} else {
		    throw new PatchConstructionException("Illegal patch format.");
	    }
	}
	
}
