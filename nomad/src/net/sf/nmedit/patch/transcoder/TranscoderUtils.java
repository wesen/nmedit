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
 * Created on Apr 9, 2006
 */
package net.sf.nmedit.patch.transcoder;

import java.util.List;

import net.sf.nmedit.jpdl.BitStream;
import net.sf.nmedit.jpdl.Packet;
import net.sf.nmedit.jpdl.PacketParser;
import net.sf.nmedit.jpdl.Protocol;
import net.sf.nmedit.jpdl.Tracer;
import net.sf.nmedit.patch.Format;
import net.sf.nmedit.patch.PatchBuilder;
import net.sf.nmedit.patch.Record;
import net.sf.nmedit.patch.parser.PatchParser;
import net.sf.nmedit.patch.parser.PatchParserException;

public class TranscoderUtils
{

    private static PacketParser patchParser = null;
    private static Protocol patchProtocol = null;
    private static String patchPdlFile = null;

    public static void init() {
        
        try {
            usePDLFile("/patch.pdl", null);
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

    public static void transcode( PatchParser parser, PatchBuilder callback ) throws TranscoderException
    {
        try
        {
            transcodeInternal(parser, callback);
        }
        catch (PatchParserException e)
        {
            throw new TranscoderException(e);
        }
    }

    protected static void transcodeInternal( PatchParser parser, PatchBuilder callback ) throws PatchParserException
    {
        Record record = new Record();
        
        while (parser.nextToken()>=0)
        {
            switch (parser.getTokenType())
            {
                case PatchParser.TK_SECTION_START:
                    
                    callback.beginSection(parser.getSectionID());
                    record.setSectionID(parser.getSectionID());
                    break;
                    
                case PatchParser.TK_SECTION_END:
                    
                    callback.endSection(parser.getSectionID());
                    break;
                    
                case PatchParser.TK_RECORD:
                    
                    record.setString(parser.getString());
                    record.setSize(parser.getValueCount());

                    for (int i=parser.getValueCount()-1;i>=0;i--)
                    {
                        record.setValue(i, parser.getValue(i));
                    }
                    
                    callback.record(record);
                    break;
                default:
                    throw new IllegalStateException("unknown token type:"+parser.getTokenType());
            }
        }
    }

    public void transcode(BitStream stream, PatchBuilder callback) throws PatchParserException
    {
        Packet packet = new Packet();
        Record record = new Record();
    
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
            throw new PatchParserException("Illegal patch format.");
        }
    }

    private static void transcodeSection(int section, Packet sectionData, PatchBuilder callback, Record r)
    {

        switch (section)
        {
            // Name section
            case Format.S_NAME_1:
            case Format.S_NAME_2:
                // patch name
                //enqueue(0, extractName(sectionData.getPacket("name")));
                //cb.patch_name();
                break;

            // Header section
            case Format.S_HEADER: 
            {
                callback.beginSection(Format.SEC_HEADER);
                
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

                r.setValues(data);
                
                callback.record(r);
                callback.endSection(Format.SEC_HEADER);
                
            } 
            break;
            // Module section
            case Format.S_MODULE: 
            {
                callback.beginSection(Format.SEC_MODULE_DUMP);
                
                //  == ModuleSectionType.POLY.SectionId ?
                r.setSize(1);
                r.setValue(0, sectionData.getVariable("section"));
                callback.record(r);
                
                int[] data = new int[4];
                
                for (Packet p : (List<Packet>) sectionData.getPacketList("modules")) 
                {
                    data[Format.MODULE_DUMP_MODULE_INDEX] = p.getVariable("index");
                    data[Format.MODULE_DUMP_MODULE_TYPE] = p.getVariable("type");
                    data[Format.MODULE_DUMP_MODULE_XPOS] = p.getVariable("xpos");
                    data[Format.MODULE_DUMP_MODULE_YPOS] = p.getVariable("ypos");
                    
                    r.setValues(data);
                    callback.record(r);
                }

                callback.endSection(Format.SEC_MODULE_DUMP);
                
            }
            break;

            // Note section
            case Format.S_NOTE: 
            {
                callback.beginSection(Format.SEC_CURRENTNOTE_DUMP);

                int[] data = new int[3];
                
                for (int i=1;i<=2;i++) 
                {
                    Packet note = sectionData.getPacket("note"+i);
                    data[Format.CURRENT_NOTE_DUMP_NOTE]=note.getVariable("value");
                    data[Format.CURRENT_NOTE_DUMP_ATTACK_VELOCITY]=note.getVariable("attack");
                    data[Format.CURRENT_NOTE_DUMP_RELEASE_VELOCITY]=note.getVariable("release");
                    r.setValues(data);
                    callback.record(r);
                }
                
                for (Packet note: (List<Packet>) sectionData.getPacketList("notes")) 
                {
                    data[Format.CURRENT_NOTE_DUMP_NOTE]=note.getVariable("value");
                    data[Format.CURRENT_NOTE_DUMP_ATTACK_VELOCITY]=note.getVariable("attack");
                    data[Format.CURRENT_NOTE_DUMP_RELEASE_VELOCITY]=note.getVariable("release");
                    r.setValues(data);
                    callback.record(r);
                }

                callback.endSection(Format.SEC_CURRENTNOTE_DUMP);
            } 
            break;

            // Cable section
            case Format.S_CABLE: 
            {
                callback.beginSection(Format.SEC_CABLE_DUMP);
                
                r.setSize(1);
                r.setValue(0, sectionData.getVariable("section"));
                callback.record(r);

                for (Packet note: (List<Packet>) sectionData.getPacketList("cables")) {
                    int[] data = new int[7];
                    data[Format.CABLE_DUMP_COLOR]=note.getVariable("color");
                    data[Format.CABLE_DUMP_MODULE_INDEX_DESTINATION]=note.getVariable("destination");
                    data[Format.CABLE_DUMP_CONNECTOR_INDEX_DESTINATION]=note.getVariable("input");
                    data[Format.CABLE_DUMP_MODULE_INDEX_SOURCE]=note.getVariable("source");
                    data[Format.CABLE_DUMP_CONNECTOR_INDEX_SOURCE]=note.getVariable("inputOutput");
                    data[Format.CABLE_DUMP_CONNECTOR_TYPE_SOURCE]=note.getVariable("type");
                    r.setValues(data);
                    callback.record(r);
                }

                callback.endSection(Format.SEC_CABLE_DUMP);
            } 
            break;
            
            // Parameter section
            case Format.S_PARAMETER: 
            {
                callback.beginSection(Format.SEC_PARAMETER_DUMP);
                
                r.setSize(1);
                r.setValue(0, sectionData.getVariable("section"));
                callback.record(r);

                for (Packet modules: (List<Packet>) sectionData.getPacketList("parameters")) { 
                    int module_index = modules.getVariable("index");
                    
                    Packet parameters = modules.getPacket("parameters");
                    List<String> param = parameters.getAllVariables();
                    
                    int[] data = new int[3+param.size()];
                    data[Format.PARAMETER_DUMP_MODULE_INDEX] = module_index;
                    data[Format.PARAMETER_DUMP_MODULE_TYPE] = 1; // TODO read variable
                    data[Format.PARAMETER_DUMP_PARAMETER_COUNT] = param.size();
                    
                        for (int i=0;i<param.size();i++)
                        data[Format.PARAMETER_DUMP_PARAMETER_BASE+i] = parameters.getVariable(param.get(i));// .intValue();//.getVariable("value");
                    
                    r.setValues(data);
                    callback.record(r);
            
                    param.clear();
                }
                callback.endSection(Format.SEC_PARAMETER_DUMP);
            } break;
            
            // Morphmap section
            case Format.S_MORPHMAP: {
                int[] data = new int[4];

                callback.beginSection(Format.SEC_MORPHMAP_DUMP);
                
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

                callback.endSection(Format.SEC_MORPHMAP_DUMP);
            } break;
            // Knobmap section
            case Format.S_KNOBMAP: 
            {
                callback.beginSection(Format.SEC_KNOBMAP_DUMP);
                

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

                callback.endSection(Format.SEC_KNOBMAP_DUMP);
            } break;
            
            // Controlmap section
            case Format.S_CTRLMAP: {
                callback.beginSection(Format.SEC_CTRLMAP_DUMP);
                int[] data = new int[4];
                for (Packet p : (List<Packet>) sectionData.getPacketList("controls")) {

                    data[Format.CTRL_MAP_DUMP_SECTION_INDEX] = p.getVariable("section");
                    data[Format.CTRL_MAP_DUMP_MODULE_INDEX] = p.getVariable("module");
                    data[Format.CTRL_MAP_DUMP_PARAMETER_INDEX] = p.getVariable("parameter");
                    data[Format.CTRL_MAP_DUMP_CC_INDEX] = p.getVariable("control");
                    r.setValues(data);
                    callback.record(r);
              }   
                callback.endSection(Format.SEC_CTRLMAP_DUMP);
            } break;
            
            // Custom section
            case Format.S_CUSTOM: {
                callback.beginSection(Format.SEC_CUSTOM_DUMP);

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
                callback.endSection(Format.SEC_CUSTOM_DUMP);
            } break;
            
            // Namedump section
            case Format.S_NAMEDUMP: {
                callback.beginSection(Format.SEC_NAME_DUMP);

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
                callback.beginSection(Format.SEC_NAME_DUMP);
            }
            break;
        }
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
    
}
