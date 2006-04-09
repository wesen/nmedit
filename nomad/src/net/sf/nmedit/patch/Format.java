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
package net.sf.nmedit.patch;

import java.util.HashMap;
import java.util.Map;

public class Format
{

    // value (1) must not be set 
    
    public final static int SEC_UNKOWN_SECTION          = 1 <<  1;
    public final static int SEC_CABLE_DUMP              = 1 <<  2;
    public final static int SEC_CTRLMAP_DUMP            = 1 <<  3;
    public final static int SEC_CURRENTNOTE_DUMP        = 1 <<  4;
    public final static int SEC_CUSTOM_DUMP             = 1 <<  5;
    public final static int SEC_HEADER                  = 1 <<  6;
    public final static int SEC_KEYBOARDASSIGNMENT      = 1 <<  7;
    public final static int SEC_KNOBMAP_DUMP            = 1 <<  8;
    public final static int SEC_MODULE_DUMP             = 1 <<  9;
    public final static int SEC_MORPHMAP_DUMP           = 1 << 10;
    public final static int SEC_NAME_DUMP               = 1 << 11;
    public final static int SEC_NOTES                   = 1 << 12;
    public final static int SEC_PARAMETER_DUMP          = 1 << 13;
    
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

    public final static String NAME_CABLE_DUMP          = "CableDump";
    public final static String NAME_CTRLMAP_DUMP        = "CtrlMapDump";
    public final static String NAME_CURRENTNOTE_DUMP    = "CurrentNoteDump";
    public final static String NAME_CUSTOM_DUMP         = "CustomDump";
    public final static String NAME_HEADER              = "Header";
    public final static String NAME_KEYBOARDASSIGNMENT  = "KeyboardAssignment";
    public final static String NAME_KNOBMAP_DUMP        = "KnobMapDump";
    public final static String NAME_MODULE_DUMP         = "ModuleDump";
    public final static String NAME_MORPHMAP_DUMP       = "MorphMapDump";
    public final static String NAME_NAME_DUMP           = "NameDump";
    public final static String NAME_NOTES               = "Notes";
    public final static String NAME_PARAMETER_DUMP      = "ParameterDump";

    // header_data
    public final static int HEADER_KEYBOARD_RANGE_MIN               = 0;
    public final static int HEADER_KEYBOARD_RANGE_MAX               = 1;
    public final static int HEADER_VELOCITY_RANGE_MIN               = 2;
    public final static int HEADER_VELOCITY_RANGE_MAX               = 3;
    public final static int HEADER_BEND_RANGE                       = 4;
    public final static int HEADER_PORTAMENTO_TIME                  = 5;
    public final static int HEADER_PORTAMENTO                       = 6;
    public final static int HEADER_REQUESTED_VOICES                 = 7;
    public final static int HEADER_SECTION_SEPARATOR_POSITION       = 8;
    public final static int HEADER_OCTAVE_SHIFT                     = 9;
    public final static int HEADER_VOICE_RETRIGGER_POLY             = 10;
    public final static int HEADER_VOICE_RETRIGGER_COMMON           = 11;
    public final static int HEADER_UNKNOWN1                         = 12;
    public final static int HEADER_UNKNOWN2                         = 13;
    public final static int HEADER_UNKNOWN3                         = 14;
    public final static int HEADER_UNKNOWN4                         = 15;
    public final static int HEADER_CABLE_VISIBILITY_RED             = 16;
    public final static int HEADER_CABLE_VISIBILITY_BLUE            = 17;
    public final static int HEADER_CABLE_VISIBILITY_YELLOW          = 18;
    public final static int HEADER_CABLE_VISIBILITY_GRAY            = 19;
    public final static int HEADER_CABLE_VISIBILITY_GREEN           = 20;
    public final static int HEADER_CABLE_VISIBILITY_PURPLE          = 21;
    public final static int HEADER_CABLE_VISIBILITY_WHITE           = 22;
    public final static int VALUE_COUNT_HEADER = 23;
    
    //moduleDump
    public final static int MODULE_DUMP_MODULE_INDEX                = 0;
    public final static int MODULE_DUMP_MODULE_TYPE                 = 1;
    public final static int MODULE_DUMP_MODULE_XPOS                 = 2;
    public final static int MODULE_DUMP_MODULE_YPOS                 = 3;
    
    // currentNoteDump
    public final static int CURRENT_NOTE_DUMP_NOTE                  = 0;
    public final static int CURRENT_NOTE_DUMP_ATTACK_VELOCITY       = 1;
    public final static int CURRENT_NOTE_DUMP_RELEASE_VELOCITY      = 2;
    
    // cableDump
    public final static int CABLE_DUMP_COLOR                        = 0;
    public final static int CABLE_DUMP_MODULE_INDEX_DESTINATION     = 1;
    public final static int CABLE_DUMP_CONNECTOR_INDEX_DESTINATION  = 2;
    public final static int CABLE_DUMP_CONNECTOR_TYPE_DESTINATION   = 3;
    public final static int CABLE_DUMP_MODULE_INDEX_SOURCE          = 4;
    public final static int CABLE_DUMP_CONNECTOR_INDEX_SOURCE       = 5;
    public final static int CABLE_DUMP_CONNECTOR_TYPE_SOURCE        = 6;

    // parameterDump
    public final static int PARAMETER_DUMP_MODULE_INDEX             = 0;
    public final static int PARAMETER_DUMP_MODULE_TYPE              = 1;
    public final static int PARAMETER_DUMP_PARAMETER_COUNT          = 2;
    public final static int PARAMETER_DUMP_PARAMETER_BASE           = 3; // index of first parameter

    // customDump
    public final static int CUSTOM_DUMP_MODULE_INDEX                = 0;
    public final static int CUSTOM_DUMP_PARAMETER_COUNT             = 1;
    public final static int CUSTOM_DUMP_PARAMETER_BASE              = 2; // index of first parameter
    
    // morphMapDump - knob values
    public final static int MORPH_MAP_DUMP_VALUES_MORPH1            = 0;
    public final static int MORPH_MAP_DUMP_VALUES_MORPH2            = 1;
    public final static int MORPH_MAP_DUMP_VALUES_MORPH3            = 2;
    public final static int MORPH_MAP_DUMP_VALUES_MORPH4            = 3;

    // morphMapDump
    public final static int MORPH_MAP_DUMP_SECTION                  = 0;
    public final static int MORPH_MAP_DUMP_MODULE_INDEX             = 1;
    public final static int MORPH_MAP_DUMP_PARAMETER_INDEX          = 2;
    public final static int MORPH_MAP_DUMP_MORPH_INDEX              = 3;
    public final static int MORPH_MAP_DUMP_MORPH_RANGE              = 4;
    
    // keyboardAssignment
    public final static int KEYBOARD_ASSIGNMENT_MORPH1              = 0;
    public final static int KEYBOARD_ASSIGNMENT_MORPH2              = 1;
    public final static int KEYBOARD_ASSIGNMENT_MORPH3              = 2;
    public final static int KEYBOARD_ASSIGNMENT_MORPH4              = 3;

    // knobMapDump
    public final static int KNOB_MAP_DUMP_SECTION_INDEX             = 0;
    public final static int KNOB_MAP_DUMP_MODULE_INDEX              = 1;
    public final static int KNOB_MAP_DUMP_PARAMETER_INDEX           = 2;
    public final static int KNOB_MAP_DUMP_KNOB_INDEX                = 3;
    
    // ctrlMapDump
    public final static int CTRL_MAP_DUMP_SECTION_INDEX             = 0;
    public final static int CTRL_MAP_DUMP_MODULE_INDEX              = 1;
    public final static int CTRL_MAP_DUMP_PARAMETER_INDEX           = 2;
    public final static int CTRL_MAP_DUMP_CC_INDEX                  = 3;

    // nameDump
    public final static int NAME_DUMP_MODULE_INDEX                  = 0;
    public final static int NAME_DUMP_MODULE_NAME                   = 1;

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
        sectionIDMap.put(NAME_NOTES               , SEC_NOTES);
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
            case SEC_NOTES                   : return NAME_NOTES;
            case SEC_PARAMETER_DUMP          : return NAME_PARAMETER_DUMP;
            default:
                return null;
        }
    }
    


    
}
