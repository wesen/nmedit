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
package net.sf.nmedit.jsynth.clavia.nordmodular.utils;

import java.util.Collection;
import java.util.Iterator;

import net.sf.nmedit.jnmprotocol2.PDLData;
import net.sf.nmedit.jnmprotocol2.utils.NmCharacter;
import net.sf.nmedit.jpatch.clavia.nordmodular.Format;
import net.sf.nmedit.jpatch.clavia.nordmodular.parser.PContentHandler;
import net.sf.nmedit.jpatch.clavia.nordmodular.parser.PParser;
import net.sf.nmedit.jpatch.clavia.nordmodular.parser.ParseException;
import net.sf.nmedit.jpatch.clavia.nordmodular.parser.PatchBuilder;
import net.sf.nmedit.jpdl2.stream.BitStream;
import net.sf.nmedit.jpdl2.PDLException;
import net.sf.nmedit.jpdl2.PDLPacket;
import net.sf.nmedit.jpdl2.PDLPacketParser;

/**
 * Uses a {@link net.sf.nmedit.jpdl.BitStream} as source and
 * feeds the {@link net.sf.nmedit.jmisc.nomad.patch.builder.PatchDecoder} with the data. 
 * 
 * @author Christian Schneider
 */
@SuppressWarnings("unchecked")
public class BitstreamPatchParser 
{

    private int[] recognizeSections = null; 
    
    public BitstreamPatchParser()
    {
        super();
    }
    
    public void setRecognizedSections(int ...sections)
    {
        this.recognizeSections = sections;
    }
    
    private boolean isSectionRecognized(int id)
    {
        if (recognizeSections == null) return true;
        
        for (int i=0;i<recognizeSections.length;i++)
            if (recognizeSections[i]==id)
                return true;
        return false;
    }

    private int[] data = new int[100];
    
    private int[] getData(int size)
    {
        if (data.length<size)
            data = new int[size];
        return data;
    }

    public void transcode(BitStream stream, PatchBuilder callback) throws ParseException
    {
        PDLPacketParser parser = new PDLPacketParser( PDLData.getPatchDoc() );
        PDLPacket packet;
    
        try
        {
            packet = parser.parse(stream);
        }
        catch (PDLException e)
        {
            throw new ParseException("Illegal patch format.", e);
        }

        do 
        {
            PDLPacket section = packet.getPacket("section");
            PDLPacket sectionData = section.getPacket("data");
            transcodeSection(section.getVariable("type"), sectionData, callback);
            packet = packet.getPacket("next");
        } 
        while (packet != null);
    }

    private void transcodeSection(int section, PDLPacket sectionData, PatchBuilder callback) throws ParseException
    {
        if (!isSectionRecognized(section))
            return;
        
        switch (section)
        {
            // Name section
            case Format.S_NAME_1:
            case Format.S_NAME_2:

                // patch name
                String patchName = NmCharacter.extractName(sectionData.getPacket("name"));
                callback.setPatchName(patchName);
                break;

            // Header section
            case Format.S_HEADER: 
            {
                callback.beginSection(PParser.IHEADER, -1);
                int[] data = getData(PContentHandler.HEADER_RSIZE);
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
                data[Format.HEADER_REQUESTED_VOICES]=sectionData.getVariable("voices")+1;
                data[Format.HEADER_SECTION_SEPARATOR_POSITION]=sectionData.getVariable("sspos");
                data[Format.HEADER_OCTAVE_SHIFT]=sectionData.getVariable("octave");

                data[Format.HEADER_UNKNOWN1] = Format.HEADER_UNKNOWN1_DEFAULT;
                data[Format.HEADER_UNKNOWN2] = Format.HEADER_UNKNOWN2_DEFAULT;
                data[Format.HEADER_UNKNOWN3] = Format.HEADER_UNKNOWN3_DEFAULT;
                data[Format.HEADER_UNKNOWN4] = Format.HEADER_UNKNOWN4_DEFAULT;
                
                callback.header(data);
                callback.endSection(PParser.IHEADER);
            } 
            break;
            // Module section
            case Format.S_MODULE: 
            {
                int va = sectionData.getVariable("section");
                callback.beginSection(PParser.IMODULEDUMP, va);
                
                int[] record = getData(4);
                for (PDLPacket p : sectionData.getPacketList("modules")) 
                {
                    record[Format.MODULE_DUMP_MODULE_INDEX] = p.getVariable("index");
                    record[Format.MODULE_DUMP_MODULE_TYPE] = p.getVariable("type");
                    record[Format.MODULE_DUMP_MODULE_XPOS] = p.getVariable("xpos");
                    record[Format.MODULE_DUMP_MODULE_YPOS] = p.getVariable("ypos");
                    callback.moduleDump(record);
                }
                callback.endSection(PParser.IMODULEDUMP);
            }
            break;

            // Note section
            case Format.S_NOTE: 
            {
                callback.beginSection(PParser.ICURRENTNOTEDUMP, -1);

                int[] record = getData(3);
                
                for (int i=1;i<=2;i++) 
                {
                    PDLPacket note = sectionData.getPacket("note"+i);
                    record[Format.CURRENT_NOTE_DUMP_NOTE] = note.getVariable("value");
                    record[Format.CURRENT_NOTE_DUMP_ATTACK_VELOCITY] = note.getVariable("attack");
                    record[Format.CURRENT_NOTE_DUMP_RELEASE_VELOCITY] = note.getVariable("release");
                    callback.currentNoteDump(record);
                }
                
                for (PDLPacket note: sectionData.getPacketList("notes")) 
                {
                    record[Format.CURRENT_NOTE_DUMP_NOTE] = note.getVariable("value");
                    record[Format.CURRENT_NOTE_DUMP_ATTACK_VELOCITY] = note.getVariable("attack");
                    record[Format.CURRENT_NOTE_DUMP_RELEASE_VELOCITY] = note.getVariable("release");
                    callback.currentNoteDump(record);
                }

                callback.endSection(PParser.ICURRENTNOTEDUMP);
            } 
            break;

            // Cable section
            case Format.S_CABLE: 
            {
                int va = sectionData.getVariable("section");
                callback.beginSection(PParser.ICABLEDUMP, va);

                int[] record = getData(Format.VALUE_COUNT_CABLE_DUMP);
                for (PDLPacket note: sectionData.getPacketList("cables")) {
                    record[Format.CABLE_DUMP_COLOR]=note.getVariable("color");
                    record[Format.CABLE_DUMP_MODULE_INDEX_DESTINATION]=note.getVariable("destination");
                    record[Format.CABLE_DUMP_CONNECTOR_INDEX_DESTINATION]=note.getVariable("input");
                    record[Format.CABLE_DUMP_CONNECTOR_TYPE_DESTINATION]=Format.VALUE_CABLE_DUMP_INPUT; 
                    record[Format.CABLE_DUMP_MODULE_INDEX_SOURCE]=note.getVariable("source");
                    record[Format.CABLE_DUMP_CONNECTOR_INDEX_SOURCE]=note.getVariable("inputOutput");
                    record[Format.CABLE_DUMP_CONNECTOR_TYPE_SOURCE]=note.getVariable("type");
                    callback.cableDump(record);
                }

                callback.endSection(PParser.ICABLEDUMP);
            } 
            break;
            
            // Parameter section
            case Format.S_PARAMETER: 
            {
                int va = sectionData.getVariable("section");

                // TODO handle morph section ???
                if (va!=Format.VALUE_SECTION_MORPH)
                {
                    callback.beginSection(PParser.IPARAMETERDUMP, va);

                    for (PDLPacket modules: sectionData.getPacketList("parameters")) { 
                        int module_index = modules.getVariable("index"); 
                        int module_type = modules.getVariable("type");
                        if (section==Format.VALUE_SECTION_MORPH)
                        {
                            //module_type = 0;
                            module_index = 0;
                        }
                        PDLPacket parameters = modules.getPacket("parameters");
                        Collection<String> param = parameters.getAllVariables();
                        
                        int [] record = getData(3+param.size());
                        record[Format.PARAMETER_DUMP_MODULE_INDEX]= module_index;
                        record[Format.PARAMETER_DUMP_MODULE_TYPE]= module_type; // TODO read variable
                        record[Format.PARAMETER_DUMP_PARAMETER_COUNT]= param.size(); // implied
                        
                        Iterator<String> iter = param.iterator();
                        for (int i=0;i<param.size();i++)
                        {
                            record[Format.PARAMETER_DUMP_PARAMETER_BASE+i] = parameters.getVariable(iter.next());
                        }
                        
                        callback.parameterDump(record);
                
                        //param.clear();
                    }
                    callback.endSection(PParser.IPARAMETERDUMP);
                }
            } break;
            
            // Morphmap section
            case Format.S_MORPHMAP: {
                int[] data = getData(4);

                // keyboardAssignment
                callback.beginSection(PParser.IKEYBOARDASSIGNMENT, -1);
                data[Format.KEYBOARD_ASSIGNMENT_MORPH1] = sectionData.getVariable("keyboard1");
                data[Format.KEYBOARD_ASSIGNMENT_MORPH2] = sectionData.getVariable("keyboard2");
                data[Format.KEYBOARD_ASSIGNMENT_MORPH3] = sectionData.getVariable("keyboard3");
                data[Format.KEYBOARD_ASSIGNMENT_MORPH4] = sectionData.getVariable("keyboard4");
                callback.keyboardAssignment(data);
                callback.endSection(PParser.IKEYBOARDASSIGNMENT);

                // knob values
                callback.beginSection(PParser.IMORPHMAPDUMP, -1);
                data[Format.MORPH_MAP_DUMP_VALUES_MORPH1] = sectionData.getVariable("morph1");
                data[Format.MORPH_MAP_DUMP_VALUES_MORPH2] = sectionData.getVariable("morph2");
                data[Format.MORPH_MAP_DUMP_VALUES_MORPH3] = sectionData.getVariable("morph3");
                data[Format.MORPH_MAP_DUMP_VALUES_MORPH4] = sectionData.getVariable("morph4");
                callback.morphMapDumpProlog(data);

                // parameter assignments
                data = getData(5);
                for (PDLPacket p : sectionData.getPacketList("morphs")) 
                {
                    data[Format.MORPH_MAP_DUMP_MORPH_INDEX] = p.getVariable("morph");
                    data[Format.MORPH_MAP_DUMP_SECTION] = p.getVariable("section");
                    data[Format.MORPH_MAP_DUMP_MODULE_INDEX] = p.getVariable("module");
                    data[Format.MORPH_MAP_DUMP_MORPH_RANGE] = p.getVariable("range"); //TODO out of range??? X-127 ???
                    data[Format.MORPH_MAP_DUMP_PARAMETER_INDEX] = p.getVariable("parameter");

                    callback.morphMapDump(data);
                }
                callback.endSection(PParser.IMORPHMAPDUMP);
            } break;
            // Knobmap section
            case Format.S_KNOBMAP: 
            {
                callback.beginSection(PParser.IKNOBMAPDUMP, -1);
             
                int[] data = getData(4); //new int[5];
                for (int i = 0; i < 23; i++) {
                    PDLPacket knob = sectionData.getPacket("knob"+i);
                    boolean assigned = knob.getVariable("assigned")==1;
                    if (assigned) {
                        PDLPacket assignment = knob.getPacketList("assignment")[0]; // .front()
                        data[Format.KNOB_MAP_DUMP_SECTION_INDEX] = assignment.getVariable("section");
                        data[Format.KNOB_MAP_DUMP_KNOB_INDEX] = i;
                        data[Format.KNOB_MAP_DUMP_MODULE_INDEX] = assignment.getVariable("module"); 
                        data[Format.KNOB_MAP_DUMP_PARAMETER_INDEX] = assignment.getVariable("parameter");
                        callback.knobMapDump(data);
                    }
                }

                callback.endSection(PParser.IKNOBMAPDUMP);
            } break;
            
            // Controlmap section
            case Format.S_CTRLMAP: {
                callback.beginSection(PParser.ICTRLMAPDUMP, -1);
                int[] data = getData(4);
                for (PDLPacket p : sectionData.getPacketList("controls")) {

                    data[Format.CTRL_MAP_DUMP_SECTION_INDEX] = p.getVariable("section");
                    data[Format.CTRL_MAP_DUMP_MODULE_INDEX] = p.getVariable("module");
                    data[Format.CTRL_MAP_DUMP_PARAMETER_INDEX] = p.getVariable("parameter");
                    data[Format.CTRL_MAP_DUMP_CC_INDEX] = p.getVariable("control");
                    callback.ctrlMapDump(data);
              }   
                callback.endSection(PParser.ICTRLMAPDUMP);
            } break;
            
            // Custom section
            case Format.S_CUSTOM: {
                int va = sectionData.getVariable("section");
                callback.beginSection(PParser.ICUSTOMDUMP, va);

                for (PDLPacket modules: sectionData.getPacketList("customModules")) { 
                    int module_index = modules.getVariable("index");

                    PDLPacket parameters = modules.getPacket("customValues");
                    Collection<String> param = parameters.getAllVariables();
                    
                    int[] data = getData(2+param.size());
                    data[Format.CUSTOM_DUMP_MODULE_INDEX] = module_index;
                    data[Format.CUSTOM_DUMP_PARAMETER_COUNT] = param.size();
                    Iterator<String> iter = param.iterator();
                    for (int i=0;i<param.size();i++)
                        data[Format.CUSTOM_DUMP_PARAMETER_BASE+i] = parameters.getVariable(iter.next());

                    callback.customDump(data);
                }
                callback.endSection(PParser.ICUSTOMDUMP);
            } break;
            
            // Namedump section
            case Format.S_NAMEDUMP: {
                int va = sectionData.getVariable("section");
                callback.beginSection(PParser.INAMEDUMP, va);

                for (PDLPacket p : sectionData.getPacketList("moduleNames")) 
                {
                    int moduleIndex = p.getVariable("index");
                    String moduleName = NmCharacter.extractName(p.getPacket("name"));
                    callback.moduleNameDump(moduleIndex, moduleName);
                }
                callback.endSection(PParser.INAMEDUMP);
            }
            break;
        }
    }

}
