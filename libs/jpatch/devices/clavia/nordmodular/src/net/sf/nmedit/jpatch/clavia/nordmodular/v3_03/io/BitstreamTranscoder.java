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
 * Created on Apr 18, 2006
 */
package net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.io;

import java.util.ArrayList;
import java.util.List;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Format;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.misc.StandaloneRecord;
import net.sf.nmedit.jpdl.BitStream;
import net.sf.nmedit.jpdl.Packet;
import net.sf.nmedit.jpdl.PacketParser;
import net.sf.nmedit.jpdl.Protocol;
import net.sf.nmedit.jpdl.Tracer;

/**
 * Uses a {@link net.sf.nmedit.jpdl.BitStream} as source and
 * feeds the {@link net.sf.nmedit.nomad.patch.builder.PatchDecoder} with the data. 
 * 
 * @author Christian Schneider
 */
@SuppressWarnings("unchecked")
public class BitstreamTranscoder extends Transcoder<BitStream, PatchBuilder>
{

    private static PacketParser patchParser = null;
    private static Protocol patchProtocol = null;
    private static String patchPdlFile = null;

    public static PacketParser getPatchParser()
    {
        if (patchParser==null)
            init();
        
        return patchParser;
    }
    
    private static boolean initialized = false;
    
    public static void init() {
        
        if (initialized)
            return;
        
        try {
            usePDLFile("/patch.pdl", null);
            initialized = true;
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

    public BitstreamTranscoder()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    private static String extractName(Packet name)
    {
        // module name limit:16<20 / notes may be longer
        StringBuffer buffer = new StringBuffer(20); 
        
        List list = name.getVariableList("chars");
        int value;
        
        for (int i=0;i<list.size();i++)
        {
            value = (Integer) list.get(i);
            if (value!=0)
            {
                buffer.append((char) value);
            }
        }

        return buffer.toString();

        /*String result="";
         List<Integer> chars = (List<Integer>) name.getVariableList("chars");
         
         for (Integer i : chars) {
             if (i!=0)
                 result+=(char)i.intValue();
         }
     
         return result;*/
    }

    public void transcode(BitStream stream, PatchBuilder callback) throws TranscoderException
    {
        Packet packet = new Packet();
        StandaloneRecord record = new StandaloneRecord();
    
        if (patchParser.parse(stream, packet)) 
        {
            while (packet != null) 
            {
                Packet section = packet.getPacket("section");
                Packet sectionData = section.getPacket("data");
                transcodeSection(section.getVariable("type"), sectionData, callback, record);
                packet = packet.getPacket("next");
                
                record.reset();
            } 
        } 
        else 
        {
            throw new TranscoderException("Illegal patch format.");
        }
    }

    private static void transcodeSection(int section, Packet sectionData, PatchBuilder callback, StandaloneRecord r)
    {

        switch (section)
        {
            // Name section
            case Format.S_NAME_1:
            case Format.S_NAME_2:

                // patch name
                r.setSectionID(Format.SEC_DUMMY_PATCH_NAME);
                callback.beginSection(r.getSectionID());
                r.setString(extractName(sectionData.getPacket("name")));
                callback.record(r);
                callback.endSection(r.getSectionID());
                
                break;

            // Header section
            case Format.S_HEADER: 
            {
                r.setSectionID(Format.SEC_HEADER);
                callback.beginSection(r.getSectionID());
                
                int[] data = new int[23];
                
                data[Format.HEADER_KEYBOARD_RANGE_MIN]=sectionData.getVariable("krangemin");
                data[Format.HEADER_KEYBOARD_RANGE_MAX]=sectionData.getVariable("krangemax");
                data[Format.HEADER_VELOCITY_RANGE_MIN]=sectionData.getVariable("vrangemin");
                data[Format.HEADER_VELOCITY_RANGE_MAX]=sectionData.getVariable("vrangemax");
                data[Format.HEADER_PORTAMENTO_TIME]=sectionData.getVariable("ptime");
                data[Format.HEADER_PORTAMENTO] =sectionData.getVariable("portamento");
                data[Format.HEADER_CABLE_VISIBILITY_RED]=sectionData.getVariable("red");
                data[Format.HEADER_CABLE_VISIBILITY_BLUE]=sectionData.getVariable("blue");
                data[Format.HEADER_CABLE_VISIBILITY_YELLOW]=sectionData.getVariable("yellow");
                data[Format.HEADER_CABLE_VISIBILITY_GRAY]=sectionData.getVariable("gray");
                data[Format.HEADER_CABLE_VISIBILITY_GREEN]=sectionData.getVariable("green");
                data[Format.HEADER_CABLE_VISIBILITY_PURPLE]=sectionData.getVariable("purple");
                data[Format.HEADER_CABLE_VISIBILITY_WHITE]=sectionData.getVariable("white");
                data[Format.HEADER_VOICE_RETRIGGER_POLY]=sectionData.getVariable("pretrigger");
                data[Format.HEADER_VOICE_RETRIGGER_COMMON]=sectionData.getVariable("cretrigger");
                data[Format.HEADER_BEND_RANGE]=sectionData.getVariable("brange");
                data[Format.HEADER_REQUESTED_VOICES]=sectionData.getVariable("voices");
                data[Format.HEADER_SECTION_SEPARATOR_POSITION]=sectionData.getVariable("sspos");
                data[Format.HEADER_OCTAVE_SHIFT]=sectionData.getVariable("octave");

                data[Format.HEADER_UNKNOWN1] = Format.HEADER_UNKNOWN1_DEFAULT;
                data[Format.HEADER_UNKNOWN2] = Format.HEADER_UNKNOWN2_DEFAULT;
                data[Format.HEADER_UNKNOWN3] = Format.HEADER_UNKNOWN3_DEFAULT;
                data[Format.HEADER_UNKNOWN4] = Format.HEADER_UNKNOWN4_DEFAULT;
                
                r.setValues(data);
                
                callback.record(r);
                callback.endSection(r.getSectionID());
                
            } 
            break;
            // Module section
            case Format.S_MODULE: 
            {
                r.setSectionID(Format.SEC_MODULE_DUMP);
                callback.beginSection(r.getSectionID());
                
                //  == ModuleSectionType.POLY.SectionId ?
                r.setSize(1);
                r.setValue(0, sectionData.getVariable("section"));
                callback.record(r);
                
                r.setSize(4);
                
                for (Packet p : (List<Packet>) sectionData.getPacketList("modules")) 
                {
                    r.setValue(Format.MODULE_DUMP_MODULE_INDEX, p.getVariable("index"));
                    r.setValue(Format.MODULE_DUMP_MODULE_TYPE, p.getVariable("type"));
                    r.setValue(Format.MODULE_DUMP_MODULE_XPOS, p.getVariable("xpos"));
                    r.setValue(Format.MODULE_DUMP_MODULE_YPOS, p.getVariable("ypos"));
                    callback.record(r);
                }

                callback.endSection(r.getSectionID());
                
            }
            break;

            // Note section
            case Format.S_NOTE: 
            {
                r.setSectionID(Format.SEC_CURRENTNOTE_DUMP);
                callback.beginSection(r.getSectionID());

                ArrayList<Integer> list = new ArrayList<Integer>();
                
                for (int i=1;i<=2;i++) 
                {
                    Packet note = sectionData.getPacket("note"+i);
                    list.add(note.getVariable("value"));
                    list.add(note.getVariable("attack"));
                    list.add(note.getVariable("release"));
                }
                
                for (Packet note: (List<Packet>) sectionData.getPacketList("notes")) 
                {
                    list.add(note.getVariable("value"));
                    list.add(note.getVariable("attack"));
                    list.add(note.getVariable("release"));
                }

                r.setSize(list.size());
                for (int i=0;i<list.size();i++)
                    r.setValue(i, list.get(i));
                
                callback.record(r);

                callback.endSection(r.getSectionID());
            } 
            break;

            // Cable section
            case Format.S_CABLE: 
            {
                r.setSectionID(Format.SEC_CABLE_DUMP);
                callback.beginSection(r.getSectionID());
                
                r.setSize(1);
                r.setValue(0, sectionData.getVariable("section"));
                callback.record(r);

                r.setSize(Format.VALUE_COUNT_CABLE_DUMP);
                for (Packet note: (List<Packet>) sectionData.getPacketList("cables")) {
                    r.setValue(Format.CABLE_DUMP_COLOR,note.getVariable("color"));
                    r.setValue(Format.CABLE_DUMP_MODULE_INDEX_DESTINATION,note.getVariable("destination"));
                    r.setValue(Format.CABLE_DUMP_CONNECTOR_INDEX_DESTINATION,note.getVariable("input"));
                    r.setValue(Format.CABLE_DUMP_CONNECTOR_TYPE_DESTINATION,Format.VALUE_CABLE_DUMP_INPUT); 
                    r.setValue(Format.CABLE_DUMP_MODULE_INDEX_SOURCE,note.getVariable("source"));
                    r.setValue(Format.CABLE_DUMP_CONNECTOR_INDEX_SOURCE,note.getVariable("inputOutput"));
                    r.setValue(Format.CABLE_DUMP_CONNECTOR_TYPE_SOURCE,note.getVariable("type"));
                    callback.record(r);
                }

                callback.endSection(r.getSectionID());
            } 
            break;
            
            // Parameter section
            case Format.S_PARAMETER: 
            {
                int va = sectionData.getVariable("section");

                // TODO handle morph section ???
                if (va!=Format.VALUE_SECTION_MORPH)
                {
                    r.setSectionID(Format.SEC_PARAMETER_DUMP);
                    callback.beginSection(r.getSectionID());
                    
                    r.setSize(1);
                    r.setValue(0, va);
                    callback.record(r);
    
                    for (Packet modules: (List<Packet>) sectionData.getPacketList("parameters")) { 
                        int module_index = modules.getVariable("index"); 
                        int module_type = modules.getVariable("type");
                        if (section==Format.VALUE_SECTION_MORPH)
                        {
                            //module_type = 0;
                            module_index = 0;
                        }
                        Packet parameters = modules.getPacket("parameters");
                        List<String> param = parameters.getAllVariables();
                        
                        r.setSize(3+param.size());
                        r.setValue(Format.PARAMETER_DUMP_MODULE_INDEX, module_index);
                        r.setValue(Format.PARAMETER_DUMP_MODULE_TYPE, module_type); // TODO read variable
                        r.setValue(Format.PARAMETER_DUMP_PARAMETER_COUNT, param.size()); // implied
                        
                        for (int i=0;i<param.size();i++)
                        {
                            r.setValue(Format.PARAMETER_DUMP_PARAMETER_BASE+i, parameters.getVariable(param.get(i)));
                        }
                            // .intValue();//.getVariable("value");
                        

                        /*
                        System.out.print("param.dump.in: ");
                        for (int i=0;i<r.getValueCount();i++)
                            System.out.print(r.getValue(i)+" ");
                        System.out.println();*/

                        callback.record(r);
                
                        //param.clear();
                    }
                    callback.endSection(r.getSectionID());
                }
            } break;
            
            // Morphmap section
            case Format.S_MORPHMAP: {
                int[] data = new int[4];

                r.setSectionID(Format.SEC_MORPHMAP_DUMP);
                callback.beginSection(r.getSectionID());
                
                data[Format.MORPH_MAP_DUMP_VALUES_MORPH1] = sectionData.getVariable("morph1");
                data[Format.MORPH_MAP_DUMP_VALUES_MORPH2] = sectionData.getVariable("morph2");
                data[Format.MORPH_MAP_DUMP_VALUES_MORPH3] = sectionData.getVariable("morph3");
                data[Format.MORPH_MAP_DUMP_VALUES_MORPH4] = sectionData.getVariable("morph4");
                
                // knob values
                r.setValues(data);
                callback.record(r);

                data[Format.KEYBOARD_ASSIGNMENT_MORPH1] = sectionData.getVariable("keyboard1");
                data[Format.KEYBOARD_ASSIGNMENT_MORPH2] = sectionData.getVariable("keyboard2");
                data[Format.KEYBOARD_ASSIGNMENT_MORPH3] = sectionData.getVariable("keyboard3");
                data[Format.KEYBOARD_ASSIGNMENT_MORPH4] = sectionData.getVariable("keyboard4");

                r.setValues(data);
                callback.record(r);
                
                // keyboardAssignment
                
                for (Packet p : (List<Packet>) sectionData.getPacketList("morphs")) {
                    data = new int[5];
                    data[Format.MORPH_MAP_DUMP_MORPH_INDEX] = p.getVariable("morph");
                    data[Format.MORPH_MAP_DUMP_SECTION] = p.getVariable("section");
                    data[Format.MORPH_MAP_DUMP_MODULE_INDEX] = p.getVariable("module");
                    data[Format.MORPH_MAP_DUMP_MORPH_RANGE] = p.getVariable("range"); //TODO out of range??? X-127 ???
                    data[Format.MORPH_MAP_DUMP_PARAMETER_INDEX] = p.getVariable("parameter");

                    r.setValues(data);
                    callback.record(r);
                }

                callback.endSection(r.getSectionID());
            } break;
            // Knobmap section
            case Format.S_KNOBMAP: 
            {
                r.setSectionID(Format.SEC_KNOBMAP_DUMP);
                callback.beginSection(r.getSectionID());
                

                int[] data = new int[4]; //new int[5];
                for (int i = 0; i < 23; i++) {
                    Packet knob = sectionData.getPacket("knob"+i);
                    boolean assigned = knob.getVariable("assigned")==1;
                    if (assigned) {
                        Packet assignment = (Packet) knob.getPacketList("assignment").get(0); // .front()
                        data[Format.KNOB_MAP_DUMP_SECTION_INDEX] = assignment.getVariable("section");
                        data[Format.KNOB_MAP_DUMP_KNOB_INDEX] = i;
                        data[Format.KNOB_MAP_DUMP_MODULE_INDEX] = assignment.getVariable("module"); 
                        data[Format.KNOB_MAP_DUMP_PARAMETER_INDEX] = assignment.getVariable("parameter");

                        r.setValues(data);
                        callback.record(r);
                    }
                }

                callback.endSection(r.getSectionID());
            } break;
            
            // Controlmap section
            case Format.S_CTRLMAP: {
                r.setSectionID(Format.SEC_CTRLMAP_DUMP);
                callback.beginSection(r.getSectionID());
                int[] data = new int[4];
                for (Packet p : (List<Packet>) sectionData.getPacketList("controls")) {

                    data[Format.CTRL_MAP_DUMP_SECTION_INDEX] = p.getVariable("section");
                    data[Format.CTRL_MAP_DUMP_MODULE_INDEX] = p.getVariable("module");
                    data[Format.CTRL_MAP_DUMP_PARAMETER_INDEX] = p.getVariable("parameter");
                    data[Format.CTRL_MAP_DUMP_CC_INDEX] = p.getVariable("control");
                    r.setValues(data);
                    callback.record(r);
              }   
                callback.endSection(r.getSectionID());
            } break;
            
            // Custom section
            case Format.S_CUSTOM: {
                r.setSectionID(Format.SEC_CUSTOM_DUMP);
                callback.beginSection(r.getSectionID());

                r.setSize(1);
                r.setValue(0, sectionData.getVariable("section"));
                callback.record(r);

                for (Packet modules: (List<Packet>) sectionData.getPacketList("customModules")) { 
                    int module_index = modules.getVariable("index");

                    Packet parameters = modules.getPacket("customValues");
                    List<String> param = parameters.getAllVariables();
                    
                    int[] data = new int[2+param.size()];
                    data[Format.CUSTOM_DUMP_MODULE_INDEX] = module_index;
                    data[Format.CUSTOM_DUMP_PARAMETER_COUNT] = param.size();
                    for (int i=0;i<param.size();i++)
                        data[Format.CUSTOM_DUMP_PARAMETER_BASE+i] = parameters.getVariable(param.get(i));

                    r.setValues(data);
                    callback.record(r);
                    
                    param.clear();
                }
                callback.endSection(r.getSectionID());
            } break;
            
            // Namedump section
            case Format.S_NAMEDUMP: {
                r.setSectionID(Format.SEC_NAME_DUMP);
                callback.beginSection(r.getSectionID());

                r.setSize(1);
                r.setValue(0, sectionData.getVariable("section"));
                callback.record(r);

                r.setSize(1);
                for (Packet p : (List<Packet>)sectionData.getPacketList("moduleNames")) 
                {
                    r.setValue(0, p.getVariable("index"));
                    r.setString(extractName(p.getPacket("name")));
                    callback.record(r);
                }
                callback.endSection(r.getSectionID());
            }
            break;
        }
    }

}
