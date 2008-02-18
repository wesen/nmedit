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
 * Created on Apr 5, 2006
 */
package net.sf.nmedit.jpatch.clavia.nordmodular;

import java.util.HashMap;
import java.util.Map;

/**
 * Constants of the Clavia Nord Modular patch file format 3.0
 * 
 * TODO defined section constants are different than the   
 *  {@link net.sf.nmedit.jnmprotocol2.PatchMessage} constants
 * 
 * @author Christian Schneider
 */
public class Format
{

    /**
     * Patch version string
     */
    public static final String VERSION_NORD_MODULAR_PATCH_3_0 = "Version=Nord Modular patch 3.0";

    
    /**
     * section id - for unknown sections
     */
    public final static int SEC_UNKOWN_SECTION          = 1 <<  1;
    
    /**
     * section id - cable dump
     */
    public final static int SEC_CABLE_DUMP              = 1 <<  2;
    
    /**
     * section id - control map dump
     */
    public final static int SEC_CTRLMAP_DUMP            = 1 <<  3;
    
    /**
     * section id - current note dump
     */
    public final static int SEC_CURRENTNOTE_DUMP        = 1 <<  4;
    
    /**
     * section id - custom dump
     */
    public final static int SEC_CUSTOM_DUMP             = 1 <<  5;
    
    /**
     * section id - header
     */
    public final static int SEC_HEADER                  = 1 <<  6;
    
    /**
     * section id - keyboard assignment
     */
    public final static int SEC_KEYBOARDASSIGNMENT      = 1 <<  7;
    
    /**
     * section id - knobmap dump
     */
    public final static int SEC_KNOBMAP_DUMP            = 1 <<  8;
    
    /**
     * section id - module dump
     */
    public final static int SEC_MODULE_DUMP             = 1 <<  9;
    
    /**
     * section id - morphmap dump
     */
    public final static int SEC_MORPHMAP_DUMP           = 1 << 10;
    
    /**
     * section id - name dump
     */
    public final static int SEC_NAME_DUMP               = 1 << 11;
    
    /**
     * section id - user notes
     */
    public final static int SEC_NOTE                   = 1 << 12;
    
    /**
     * section id - parameter dump
     */
    public final static int SEC_PARAMETER_DUMP          = 1 << 13;
    
    /**
     * Dummy section id for the patch name.
     * Some sources do not have this section - for example
     * a patch file has no patch name in it.
     */
    public final static int SEC_DUMMY_PATCH_NAME        = 1 << 14;

    // PatchMessage section IDs
    public final static int S_NAME_1 = 55;
    public final static int S_NAME_2 = 39;
    public final static int S_HEADER = 33;
    public final static int S_MODULE = 74;
    public final static int S_NOTE = 105;
    public final static int S_CABLE = 82;
    public final static int S_MORPHMAP = 101;
    public final static int S_KNOBMAP = 98;
    public final static int S_CTRLMAP = 96;
    public final static int S_CUSTOM = 91;
    public final static int S_NAMEDUMP = 90;
    public final static int S_PARAMETER = 77;

    /**
     *  section name
     */
    public final static String NAME_CABLE_DUMP          = "CableDump";
    
    /**
     *  section name
     */
    public final static String NAME_CTRLMAP_DUMP        = "CtrlMapDump";
    
    /**
     *  section name
     */
    public final static String NAME_CURRENTNOTE_DUMP    = "CurrentNoteDump";
    
    /**
     *  section name
     */
    public final static String NAME_CUSTOM_DUMP         = "CustomDump";
    
    /**
     *  section name
     */
    public final static String NAME_HEADER              = "Header";
    
    /**
     *  section name
     */
    public final static String NAME_KEYBOARDASSIGNMENT  = "KeyboardAssignment";
    
    /**
     *  section name
     */
    public final static String NAME_KNOBMAP_DUMP        = "KnobMapDump";
    
    /**
     *  section name
     */
    public final static String NAME_MODULE_DUMP         = "ModuleDump";
    
    /**
     *  section name
     */
    public final static String NAME_MORPHMAP_DUMP       = "MorphMapDump";
    
    /**
     *  section name
     */
    public final static String NAME_NAME_DUMP           = "NameDump";
    
    /**
     *  section name
     */
    public final static String NAME_NOTE               = "Notes";
    
    /**
     *  section name
     */
    public final static String NAME_PARAMETER_DUMP      = "ParameterDump";

    /**
     * constant: common voice area
     * @see #VALUE_SECTION_VOICE_AREA_POLY
     * @see #VALUE_SECTION_MORPH
     */
    public final static int VALUE_SECTION_VOICE_AREA_COMMON = 0;
    
    /**
     * constant: poly voice area
     * @see #VALUE_SECTION_VOICE_AREA_COMMON
     * @see #VALUE_SECTION_MORPH
     */
    public final static int VALUE_SECTION_VOICE_AREA_POLY   = 1;
    
    /**
     * constant: morph section
     * @see #VALUE_SECTION_VOICE_AREA_POLY
     * @see #VALUE_SECTION_VOICE_AREA_COMMON
     */
    public final static int VALUE_SECTION_MORPH             = 2;
    
    /**
     * header-value index 
     */
    public final static int HEADER_KEYBOARD_RANGE_MIN               = 0;
    public final static int HEADER_KEYBOARD_RANGE_MIN_DEFAULT       = 0;
    
    /**
     * header-value index 
     */
    public final static int HEADER_KEYBOARD_RANGE_MAX               = 1;
    public final static int HEADER_KEYBOARD_RANGE_MAX_DEFAULT       = 127;
    
    /**
     * header-value index 
     */
    public final static int HEADER_VELOCITY_RANGE_MIN               = 2;
    public final static int HEADER_VELOCITY_RANGE_MIN_DEFAULT       = 0;
    
    /**
     * header-value index 
     */
    public final static int HEADER_VELOCITY_RANGE_MAX               = 3;
    public final static int HEADER_VELOCITY_RANGE_MAX_DEFAULT       = 127;
    
    /**
     * header-value index 
     */
    public final static int HEADER_BEND_RANGE                       = 4;
    public final static int HEADER_BEND_RANGE_MIN                   = 0;
    public final static int HEADER_BEND_RANGE_MAX                   = 24;
    public final static int HEADER_BEND_RANGE_DEFAULT               = 2;
    
    /**
     * header-value index 
     */
    public final static int HEADER_PORTAMENTO_TIME                  = 5;
    public final static int HEADER_PORTAMENTO_TIME_DEFAULT          = 0;
    
    /**
     * header-value index 
     */
    public final static int HEADER_PORTAMENTO                       = 6;
    public final static int HEADER_PORTAMENTO_DEFAULT               = 0;
    
    /**
     * header-value index 
     */
    public final static int HEADER_REQUESTED_VOICES                 = 7;
    public final static int HEADER_REQUESTED_VOICES_DEFAULT         = 1-1;
    
    /**
     * header-value index 
     */
    public final static int HEADER_SECTION_SEPARATOR_POSITION       = 8;
    public final static int HEADER_SECTION_SEPARATOR_POSITION_TOP_MOST = 0; 
    public final static int HEADER_SECTION_SEPARATOR_POSITION_BOTTOM_MOST = 4000; 
    public final static int HEADER_SECTION_SEPARATOR_POSITION_DEFAULT = 
        HEADER_SECTION_SEPARATOR_POSITION_BOTTOM_MOST; 
    
    /**
     * header-value index 
     */
    public final static int HEADER_OCTAVE_SHIFT                     = 9;
    public final static int HEADER_OCTAVE_SHIFT_DEFAULT             = 2;
    
    /**
     * header-value index 
     */
    public final static int HEADER_VOICE_RETRIGGER_POLY             = 10;
    public final static int HEADER_VOICE_RETRIGGER_POLY_DEFAULT     = 1;
    
    /**
     * header-value index 
     */
    public final static int HEADER_VOICE_RETRIGGER_COMMON           = 11;
    public final static int HEADER_VOICE_RETRIGGER_COMMON_DEFAULT   = 1;
    
    /**
     * header-value index 
     */
    public final static int HEADER_UNKNOWN1                         = 12;
    public final static int HEADER_UNKNOWN1_DEFAULT                 = 0; // TODO right value
    
    /**
     * header-value index 
     */
    public final static int HEADER_UNKNOWN2                         = 13;
    public final static int HEADER_UNKNOWN2_DEFAULT                 = 0; // TODO right value
    
    /**
     * header-value index 
     */
    public final static int HEADER_UNKNOWN3                         = 14;
    public final static int HEADER_UNKNOWN3_DEFAULT                 = 0; // TODO right value
    
    /**
     * header-value index 
     */
    public final static int HEADER_UNKNOWN4                         = 15;
    public final static int HEADER_UNKNOWN4_DEFAULT                 = 0; // TODO right value

    public final static int HEADER_CABLE_VISIBLE                     = 1;
    public final static int HEADER_CABLE_INVISIBLE                   = 0;
    /**
     * header-value index 
     */
    public final static int HEADER_CABLE_VISIBILITY_RED             = 16;
    
    /**
     * header-value index 
     */
    public final static int HEADER_CABLE_VISIBILITY_BLUE            = 17;
    
    /**
     * header-value index 
     */
    public final static int HEADER_CABLE_VISIBILITY_YELLOW          = 18;
    
    /**
     * header-value index 
     */
    public final static int HEADER_CABLE_VISIBILITY_GRAY            = 19;
    
    /**
     * header-value index 
     */
    public final static int HEADER_CABLE_VISIBILITY_GREEN           = 20;
    
    /**
     * header-value index 
     */
    public final static int HEADER_CABLE_VISIBILITY_PURPLE          = 21;
    
    /**
     * header-value index 
     */
    public final static int HEADER_CABLE_VISIBILITY_WHITE           = 22;
    
    /**
     * number of values in the header 
     */
    public final static int VALUE_COUNT_HEADER = 23;
    

    /**
     * module dump value index 
     */
    public final static int MODULE_DUMP_MODULE_INDEX                = 0;
    
    /**
     * module dump value index 
     */
    public final static int MODULE_DUMP_MODULE_TYPE                 = 1;
    
    /**
     * module dump value index 
     */
    public final static int MODULE_DUMP_MODULE_XPOS                 = 2;
    
    /**
     * module dump value index 
     */
    public final static int MODULE_DUMP_MODULE_YPOS                 = 3;
    
    /**
     * current note dump value index 
     */
    public final static int CURRENT_NOTE_DUMP_NOTE                  = 0;
    
    /**
     * current note dump value index 
     */
    public final static int CURRENT_NOTE_DUMP_ATTACK_VELOCITY       = 1;
    
    /**
     * current note dump value index 
     */
    public final static int CURRENT_NOTE_DUMP_RELEASE_VELOCITY      = 2;
    
    /**
     * cable dump value index 
     */
    public final static int CABLE_DUMP_COLOR                        = 0;
    
    /**
     * cable dump value index 
     */
    public final static int CABLE_DUMP_MODULE_INDEX_DESTINATION     = 1;
    
    /**
     * cable dump value index 
     */
    public final static int CABLE_DUMP_CONNECTOR_INDEX_DESTINATION  = 2;
    
    /**
     * cable dump value index 
     */
    public final static int CABLE_DUMP_CONNECTOR_TYPE_DESTINATION   = 3;
    
    /**
     * cable dump value index 
     */
    public final static int CABLE_DUMP_MODULE_INDEX_SOURCE          = 4;
    
    /**
     * cable dump value index 
     */
    public final static int CABLE_DUMP_CONNECTOR_INDEX_SOURCE       = 5;
    
    /**
     * cable dump value index 
     */
    public final static int CABLE_DUMP_CONNECTOR_TYPE_SOURCE        = 6;
    
    /**
     * cable dump value index 
     */
    public final static int VALUE_COUNT_CABLE_DUMP = 7;
    
    /**
     * cable dump value index 
     */
    public final static int VALUE_CABLE_DUMP_INPUT = 0;
    
    /**
     * cable dump value index 
     */
    public final static int VALUE_CABLE_DUMP_OUTPUT = 1;

    /**
     * Returns the output ID of a connector.
     * 
     * <ul>
     *  <li>{@link #VALUE_CABLE_DUMP_INPUT} - if the connector is an input</li>
     *  <li>{@link #VALUE_CABLE_DUMP_OUTPUT} - if the connector is an output</li>
     * </ul>
     * 
     * @param isOutput if the connector is an output
     * @return connector output ID
     * 
     * @see #VALUE_CABLE_DUMP_INPUT
     * @see #VALUE_CABLE_DUMP_OUTPUT
     */
    public final static int getOutputID(boolean isOutput)
    {
        return isOutput ? VALUE_CABLE_DUMP_OUTPUT : VALUE_CABLE_DUMP_INPUT;
    }
    
    /**
     * parameter dump value index 
     */
    public final static int PARAMETER_DUMP_MODULE_INDEX             = 0;
    
    /**
     * parameter dump value index 
     */
    public final static int PARAMETER_DUMP_MODULE_TYPE              = 1;
    
    /**
     * parameter dump value index 
     */
    public final static int PARAMETER_DUMP_PARAMETER_COUNT          = 2;
    
    /**
     * Offset of the parameter values in the <b>parameter dump</b>
     * section. For example the <code>n</code>-th parameter value has the index
     * <code>PARAMETER_DUMP_BASE+n</code>.
     */
    public final static int PARAMETER_DUMP_PARAMETER_BASE           = 3; // index of first parameter

    /**
     * custom dump value index 
     */
    public final static int CUSTOM_DUMP_MODULE_INDEX                = 0;
    
    /**
     * custom dump value index 
     */
    public final static int CUSTOM_DUMP_PARAMETER_COUNT             = 1;
    
    /**
     * Offset of the custom values in the <b>custom dump</b>
     * section. For example the <code>n</code>-th custom value has the index
     * <code>CUSTOM_DUMP_PARAMETER_BASE+n</code>.
     */
    public final static int CUSTOM_DUMP_PARAMETER_BASE              = 2; // index of first parameter
    

    /**
     * morphmap dump / morph values 
     */
    public final static int MORPH_MAP_DUMP_VALUES_MORPH1            = 0;
    
    /**
     * morphmap dump / morph values 
     */
    public final static int MORPH_MAP_DUMP_VALUES_MORPH2            = 1;
    
    /**
     * morphmap dump / morph values 
     */
    public final static int MORPH_MAP_DUMP_VALUES_MORPH3            = 2;
    
    /**
     * morphmap dump / morph values 
     */
    public final static int MORPH_MAP_DUMP_VALUES_MORPH4            = 3;

    /**
     * morphmap dump / morph settings 
     */
    public final static int MORPH_MAP_DUMP_SECTION                  = 0;
    
    /**
     * morphmap dump / morph settings 
     */
    public final static int MORPH_MAP_DUMP_MODULE_INDEX             = 1;
    
    /**
     * morphmap dump / morph settings 
     */
    public final static int MORPH_MAP_DUMP_PARAMETER_INDEX          = 2;
    
    /**
     * morphmap dump / morph settings 
     */
    public final static int MORPH_MAP_DUMP_MORPH_INDEX              = 3;
    
    /**
     * morphmap dump / morph settings 
     */
    public final static int MORPH_MAP_DUMP_MORPH_RANGE              = 4;
    
    /**
     * morphmap dump / morph values : number of values
     * @see #VALUE_COUNT_MORPH_MAP_DUMP 
     */
    public final static int VALUE_COUNT_MORPH_MAP_DUMP_VALUES = 4;
    
    /**
     * morphmap dump / morph settings : number of values
     * @see #VALUE_COUNT_MORPH_MAP_DUMP_VALUES 
     */
    public final static int VALUE_COUNT_MORPH_MAP_DUMP = 5;
    
    /**
     * keyboard assignment - value index 
     * @see #VALUE_KEYBOARD_ASSIGNMENT_NONE
     * @see #VALUE_KEYBOARD_ASSIGNMENT_NOTE
     * @see #VALUE_KEYBOARD_ASSIGNMENT_VELOCITY
     */
    public final static int KEYBOARD_ASSIGNMENT_MORPH1              = 0;
    
    /**
     * keyboard assignment - value index 
     * @see #VALUE_KEYBOARD_ASSIGNMENT_NONE
     * @see #VALUE_KEYBOARD_ASSIGNMENT_NOTE
     * @see #VALUE_KEYBOARD_ASSIGNMENT_VELOCITY
     */
    public final static int KEYBOARD_ASSIGNMENT_MORPH2              = 1;
    
    /**
     * keyboard assignment - value index 
     * @see #VALUE_KEYBOARD_ASSIGNMENT_NONE
     * @see #VALUE_KEYBOARD_ASSIGNMENT_NOTE
     * @see #VALUE_KEYBOARD_ASSIGNMENT_VELOCITY
     */
    public final static int KEYBOARD_ASSIGNMENT_MORPH3              = 2;
    
    /**
     * keyboard assignment - value index
     * @see #VALUE_KEYBOARD_ASSIGNMENT_NONE
     * @see #VALUE_KEYBOARD_ASSIGNMENT_NOTE
     * @see #VALUE_KEYBOARD_ASSIGNMENT_VELOCITY 
     */
    public final static int KEYBOARD_ASSIGNMENT_MORPH4              = 3;

    /**
     * keyboard assignment value
     */
    public final static int VALUE_KEYBOARD_ASSIGNMENT_NONE          = 0;
    
    /**
     * keyboard assignment value
     */
    public final static int VALUE_KEYBOARD_ASSIGNMENT_VELOCITY      = 1;
    
    /**
     * keyboard assignment value
     */
    public final static int VALUE_KEYBOARD_ASSIGNMENT_NOTE          = 2;
    
    // knobMapDump
    
    /**
     * knob map dump - value index
     */
    public final static int KNOB_MAP_DUMP_SECTION_INDEX             = 0;
    
    /**
     * knob map dump - value index
     */
    public final static int KNOB_MAP_DUMP_MODULE_INDEX              = 1;
    
    /**
     * knob map dump - value index
     */
    public final static int KNOB_MAP_DUMP_PARAMETER_INDEX           = 2;
    
    /**
     * knob map dump - value index
     */
    public final static int KNOB_MAP_DUMP_KNOB_INDEX                = 3;
    
    /**
     * knob map dump - number of values
     */
    public final static int VALUE_COUNT_KNOBMAP_DUMP = 4;
    
    /**
     * control map dump - value index
     */
    public final static int CTRL_MAP_DUMP_SECTION_INDEX             = 0;
    
    /**
     * control map dump - value index
     */
    public final static int CTRL_MAP_DUMP_MODULE_INDEX              = 1;
    
    /**
     * control map dump - value index
     */
    public final static int CTRL_MAP_DUMP_PARAMETER_INDEX           = 2;
    
    /**
     * control map dump - value index
     */
    public final static int CTRL_MAP_DUMP_CC_INDEX                  = 3;
    
    /**
     * control map dump - number of values
     */
    public static final int VALUE_COUNT_CTRL_MAP_DUMP = 4;

    /**
     * name dump - value index
     */
    public final static int NAME_DUMP_MODULE_INDEX                  = 0;
    
    /**
     * name dump - value index; note that this value is a string
     */
    public final static int NAME_DUMP_MODULE_NAME                   = 1;

    /**
     * map containing the pairs (name, ID) of a section
     */
    private final static Map<String, Integer> sectionIDMap;
    
    static 
    {
        sectionIDMap = new HashMap<String, Integer>();
        sectionIDMap.put(NAME_CABLE_DUMP          , SEC_CABLE_DUMP);
        sectionIDMap.put(NAME_CTRLMAP_DUMP        , SEC_CTRLMAP_DUMP);
        sectionIDMap.put(NAME_CURRENTNOTE_DUMP    , SEC_CURRENTNOTE_DUMP);
        sectionIDMap.put(NAME_CUSTOM_DUMP         , SEC_CUSTOM_DUMP);
        sectionIDMap.put(NAME_HEADER              , SEC_HEADER);
        sectionIDMap.put(NAME_KEYBOARDASSIGNMENT  , SEC_KEYBOARDASSIGNMENT);
        sectionIDMap.put(NAME_KNOBMAP_DUMP        , SEC_KNOBMAP_DUMP);
        sectionIDMap.put(NAME_MODULE_DUMP         , SEC_MODULE_DUMP);
        sectionIDMap.put(NAME_MORPHMAP_DUMP       , SEC_MORPHMAP_DUMP);
        sectionIDMap.put(NAME_NAME_DUMP           , SEC_NAME_DUMP);
        sectionIDMap.put(NAME_NOTE               , SEC_NOTE);
        sectionIDMap.put(NAME_PARAMETER_DUMP      , SEC_PARAMETER_DUMP);
    }
    
    /**
     * Returns the ID of the section.
     * @param name name of the section
     * @return the ID of the section
     */
    public static int getSectionID(String name)
    {
        Integer id = sectionIDMap.get(name);
        if (id != null)
        {
            return id.intValue();
        }
        else
        {
            return -1;
        }
    }

    /**
     * Returns the name of the section.
     * @param ID ID of the section
     * @return the name of the section
     */
    public static String getSectionName(int ID)
    {
        switch (ID)
        {
            case SEC_CABLE_DUMP              : return NAME_CABLE_DUMP;
            case SEC_CTRLMAP_DUMP            : return NAME_CTRLMAP_DUMP;
            case SEC_CURRENTNOTE_DUMP        : return NAME_CURRENTNOTE_DUMP;
            case SEC_CUSTOM_DUMP             : return NAME_CUSTOM_DUMP;
            case SEC_HEADER                  : return NAME_HEADER;
            case SEC_KEYBOARDASSIGNMENT      : return NAME_KEYBOARDASSIGNMENT;
            case SEC_KNOBMAP_DUMP            : return NAME_KNOBMAP_DUMP;
            case SEC_MODULE_DUMP             : return NAME_MODULE_DUMP;
            case SEC_MORPHMAP_DUMP           : return NAME_MORPHMAP_DUMP;
            case SEC_NAME_DUMP               : return NAME_NAME_DUMP;
            case SEC_NOTE                   : return NAME_NOTE;
            case SEC_PARAMETER_DUMP          : return NAME_PARAMETER_DUMP;
            default:
                throw new RuntimeException("unknown section[ID="+ID+"]");
                //return null;
        }
    }

    public static String getSectionName2(int ID)
    {
        switch (ID)
        {
            // PatchMessage section IDs
            case S_NAME_1: return "Name 1";
            case S_NAME_2: return "Name 2";
            case S_HEADER: return NAME_HEADER;
            case S_MODULE: return NAME_MODULE_DUMP;
            case S_NOTE: return NAME_CURRENTNOTE_DUMP;
            case S_CABLE: return NAME_CABLE_DUMP;
            case S_MORPHMAP: return NAME_MORPHMAP_DUMP;
            case S_KNOBMAP: return NAME_KNOBMAP_DUMP;
            case S_CTRLMAP: return NAME_CTRLMAP_DUMP;
            case S_CUSTOM: return NAME_CUSTOM_DUMP;
            case S_NAMEDUMP: return NAME_NAME_DUMP;
            case S_PARAMETER: return NAME_PARAMETER_DUMP;
            default:
                return null;
        }
    }
    
    /**
     * Returns the voice area ID.
     * 
     * @param polyVoiceArea true if the ID of the poly voice area is expected and
     *                      false if the ID of the common voice area is expected
     * @return the voice area ID
     */
    public static int getVoiceAreaID( boolean polyVoiceArea )
    {
        return polyVoiceArea ? VALUE_SECTION_VOICE_AREA_POLY : VALUE_SECTION_VOICE_AREA_COMMON;
    }


    /*
    public static String getUnescapedNote(String s)
    {
        // replaces '\[' with '['
        
        if (s.startsWith("["))
            return null;
        
        return s.replaceAll("\\\\\\[", "[");
    }
    
    public static String getEscapedNote(String s)
    {
        // replaces '[' with '\['
        return s.replaceAll("\\[", "\\\\[");
    }*/
    

    public static String getUnescapedNote(String s)
    {
        // replaces '\[/' with '[/'
        return s.replaceAll("\\\\\\[/", "[/");
    }
    
    public static String getEscapedNote(String s)
    {
        // replaces '[/' with '\[/'
        return s.replaceAll("\\[/", "\\\\[/");
    }
    
}
