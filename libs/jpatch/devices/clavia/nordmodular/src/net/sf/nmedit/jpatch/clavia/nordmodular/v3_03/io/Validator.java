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
 * Created on Apr 6, 2006
 */
package net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.io;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Format;

public class Validator extends WrappedPatchParser
{
    
    public PatchParser create(PatchParser parser)
    {
        return new Validator(parser);
    }

    public Validator( PatchParser parser )
    {
        super(parser);
    }

    @Override
    public int nextToken() throws PatchParserException
    {
        if (parser.nextToken() == TK_RECORD)
        {
            switch (parser.getSectionID())
            {

                case Format.SEC_NOTES:
                    // nothing to check here
                    break;

                case Format.SEC_DUMMY_PATCH_NAME:
                    // nothing to check here
                    break;
                    
                case Format.SEC_UNKOWN_SECTION:
                    // nothing to check here
                    break;
                
                case Format.SEC_HEADER:
                    validateHeader();
                    break;

                case Format.SEC_CABLE_DUMP:
                    validateCableDump();
                    break;

                case Format.SEC_CTRLMAP_DUMP:
                    validateCtrlMapDump();
                    break;

                case Format.SEC_CURRENTNOTE_DUMP:
                    validateCurrentNoteDump();
                    break;

                case Format.SEC_CUSTOM_DUMP:
                    validateCustomDump();
                    break;

                case Format.SEC_KEYBOARDASSIGNMENT:
                    validateKeyboardAssignment();
                    break;

                case Format.SEC_KNOBMAP_DUMP:
                    validateKnobMapDump();
                    break;

                case Format.SEC_MODULE_DUMP:
                    validateModuleDump();
                    break;

                case Format.SEC_MORPHMAP_DUMP:
                    validateMorphMapDump();
                    break;

                case Format.SEC_NAME_DUMP:
                    validateNameDump();
                    break;

                case Format.SEC_PARAMETER_DUMP:
                    validateParameterDump();
                    break;
            }
        }

        return parser.getTokenType();
    }

    private void validateHeader() throws PatchParserException
    {
        // we check only the header entry containing numbers
        if (parser.getValueCount() > 0)
        {
            if (parser.getValueCount() != Format.VALUE_COUNT_HEADER)
            {
                throw new PatchParserException( "incomplete header" );
            }

            vRan( 0, 127, Format.HEADER_KEYBOARD_RANGE_MIN,
                    "keyboard range min" );
            vRan( parser.getValue( Format.HEADER_KEYBOARD_RANGE_MIN ), 127,
                    Format.HEADER_KEYBOARD_RANGE_MAX, "keyboard range max" );
            vRan( 0, 24, Format.HEADER_BEND_RANGE, "bend range" );
            vRan( 0, 127, Format.HEADER_PORTAMENTO_TIME, "portamento time" );
            vRan( 0, 1, Format.HEADER_PORTAMENTO, "portamento" );
            vRan( 0, 32, Format.HEADER_REQUESTED_VOICES, "requested voices" );
            vRan( 0, 4000, Format.HEADER_SECTION_SEPARATOR_POSITION,
                    "section separator" );
            vRan( 0, 4, Format.HEADER_OCTAVE_SHIFT, "octave shift" );
            vRan( 0, 1, Format.HEADER_VOICE_RETRIGGER_POLY,
                    "voice retrigger poly" );
            vRan( 0, 1, Format.HEADER_VOICE_RETRIGGER_COMMON,
                    "voice retrigger common" );
            vRan( 0, 1, Format.HEADER_CABLE_VISIBILITY_RED,
                    "cable visibility red" );
            vRan( 0, 1, Format.HEADER_CABLE_VISIBILITY_BLUE,
                    "cable visibility blue" );
            vRan( 0, 1, Format.HEADER_CABLE_VISIBILITY_YELLOW,
                    "cable visibility yellow" );
            vRan( 0, 1, Format.HEADER_CABLE_VISIBILITY_GRAY,
                    "cable visibility gray" );
            vRan( 0, 1, Format.HEADER_CABLE_VISIBILITY_GREEN,
                    "cable visibility green" );
            vRan( 0, 1, Format.HEADER_CABLE_VISIBILITY_PURPLE,
                    "cable visibility purple" );
            vRan( 0, 1, Format.HEADER_CABLE_VISIBILITY_WHITE,
                    "cable visibility white" );
        }
    }

    private void validateCableDump() throws PatchParserException
    {
        if (parser.getValueCount() > 1)
        {
            vRan( 0, 6, Format.CABLE_DUMP_COLOR, "color" );
            vMin( 1, Format.CABLE_DUMP_MODULE_INDEX_DESTINATION,
                    "destination module index" );
            vMin( 0, Format.CABLE_DUMP_CONNECTOR_INDEX_DESTINATION,
                    "destination connector index" );
            // data[Constants.CABLE_DUMP_CONNECTOR_TYPE_DESTINATION ??? always 0
            vRan( 0, 1, Format.CABLE_DUMP_CONNECTOR_TYPE_DESTINATION,
                    "destination connector type" );
            vMin( 1, Format.CABLE_DUMP_MODULE_INDEX_SOURCE,
                    "source module index" );
            vMin( 0, Format.CABLE_DUMP_CONNECTOR_INDEX_SOURCE,
                    "source connector index" );
            vRan( 0, 1, Format.CABLE_DUMP_CONNECTOR_TYPE_SOURCE,
                    "source connector type" );
        }
    }

    private void validateCtrlMapDump() throws PatchParserException
    {
        vRan( 0, 2, Format.CTRL_MAP_DUMP_SECTION_INDEX, "section index" );
        vMin( 1, Format.CTRL_MAP_DUMP_MODULE_INDEX, "module index" );
        vMin( 0, Format.CTRL_MAP_DUMP_PARAMETER_INDEX, "parameter index" );

        int cc = parser.getValue( Format.CTRL_MAP_DUMP_CC_INDEX );

        if (!( ( 0 <= cc && cc <= 120 ) && ( cc != 32 ) ))
            throw new PatchParserException( "out of range: 'cc index'" );

    }

    private void validateCurrentNoteDump() throws PatchParserException
    {
        vRan( 0, 127, Format.CURRENT_NOTE_DUMP_NOTE, "note" );
        vRan( 0, 127, Format.CURRENT_NOTE_DUMP_ATTACK_VELOCITY,
                "attack velocity" );
        vRan( 0, 127, Format.CURRENT_NOTE_DUMP_RELEASE_VELOCITY,
                "release velocity" );
    }

    private void validateCustomDump() throws PatchParserException
    {
        if (parser.getValueCount()>1)
        {
            vMin( 1, Format.CUSTOM_DUMP_MODULE_INDEX, "module index" );
            vMin( 1, Format.CUSTOM_DUMP_PARAMETER_COUNT, "custom count" );
            vEq( parser.getValueCount() - Format.CUSTOM_DUMP_PARAMETER_BASE,
                    Format.CUSTOM_DUMP_PARAMETER_COUNT, "custom count" );
        }
    }

    private void validateKeyboardAssignment() throws PatchParserException
    {
        vRan( 0, 2, Format.KEYBOARD_ASSIGNMENT_MORPH1, "morph 1" );
        vRan( 0, 2, Format.KEYBOARD_ASSIGNMENT_MORPH2, "morph 2" );
        vRan( 0, 2, Format.KEYBOARD_ASSIGNMENT_MORPH3, "morph 3" );
        vRan( 0, 2, Format.KEYBOARD_ASSIGNMENT_MORPH4, "morph 4" );
    }

    private void validateKnobMapDump() throws PatchParserException
    {
        vRan( 0, 2, Format.KNOB_MAP_DUMP_SECTION_INDEX, "section index" );
        vMin( 1, Format.KNOB_MAP_DUMP_MODULE_INDEX, "module index" );
        // vMin(1, ... described in format is incorrect
        vMin( 0, Format.KNOB_MAP_DUMP_PARAMETER_INDEX, "parameter index" );

        int kindex = parser.getValue( Format.KNOB_MAP_DUMP_KNOB_INDEX );

        if (!( ( ( 0 <= kindex ) && ( kindex <= 17 ) ) || ( kindex == 19 )
                || ( kindex == 20 ) || ( kindex == 22 ) ))
            throw new PatchParserException( "out of range: 'knob index'" );
    }

    private void validateModuleDump() throws PatchParserException
    {
        if (parser.getValueCount() > 1)
        {
            vMin( 1, Format.MODULE_DUMP_MODULE_INDEX, "module index" );
            vRan( 0, 127, Format.MODULE_DUMP_MODULE_TYPE, "module type" );
            vMin( 0, Format.MODULE_DUMP_MODULE_XPOS, "module x-position" );
            vMin( 0, Format.MODULE_DUMP_MODULE_YPOS, "module y-position" );
        }
    }

    private void validateMorphMapDump() throws PatchParserException
    {
        if (parser.getValueCount() ==  4)
        {
            // morph values
            vRan( 0, 127, Format.MORPH_MAP_DUMP_VALUES_MORPH1, "morph 1" );
            vRan( 0, 127, Format.MORPH_MAP_DUMP_VALUES_MORPH2, "morph 2" );
            vRan( 0, 127, Format.MORPH_MAP_DUMP_VALUES_MORPH3, "morph 3" );
            vRan( 0, 127, Format.MORPH_MAP_DUMP_VALUES_MORPH4, "morph 4" );
        }
        else if (parser.getValueCount()>1) // and != 4
        {
            // knob values
    
            vRan( 0, 1, Format.MORPH_MAP_DUMP_SECTION, "section index" );
            vMin( 1, Format.MORPH_MAP_DUMP_MODULE_INDEX, "module index" );
            vMin( 0, Format.MORPH_MAP_DUMP_PARAMETER_INDEX, "parameter index" );
            vRan( 0, 3, Format.MORPH_MAP_DUMP_MORPH_INDEX, "morph index" );
            vRan( -127, 127, Format.MORPH_MAP_DUMP_MORPH_RANGE, "morph range" );
        }
    }

    private void validateNameDump() throws PatchParserException
    {

    }

    private void validateParameterDump() throws PatchParserException
    {
        if (parser.getValueCount() > 1)
        {
            vMin( 1, Format.PARAMETER_DUMP_MODULE_INDEX, "module index" );
            vRan( 1, 127, Format.PARAMETER_DUMP_MODULE_TYPE, "module type" );
            vMin( 1, Format.PARAMETER_DUMP_PARAMETER_COUNT,
                    "parameter count" );

            vEq( parser.getValueCount()
                    - Format.PARAMETER_DUMP_PARAMETER_BASE,
                    Format.PARAMETER_DUMP_PARAMETER_COUNT, "parameter count" );
        }
    }

    private void validateIndex( int index, String name )
            throws PatchParserException
    {
        if (index >= parser.getValueCount())
        {
            throw new PatchParserException( "missing value [index=" + index
                    + "<" + parser.getValueCount() + "]:" + name );
        }
    }

    // range
    private void vRan( int min, int max, int index, String name )
            throws PatchParserException
    {
        validateIndex( index, name );
        int value = parser.getValue( index );
        if (min > value || value > max)
            throw new PatchParserException( "out of range:" + name );
    }

    private void vMin( int min, int index, String name )
            throws PatchParserException
    {
        validateIndex( index, name );
        int value = parser.getValue( index );
        if (min > value)
            throw new PatchParserException( "out of range:" + name );
    }

    private void vEq( int eq, int index, String name )
            throws PatchParserException
    {
        validateIndex( index, name );
        int value = parser.getValue( index );
        if (eq > value)
            throw new PatchParserException( "not equal: '" + name + "'" );
    }
 
    public String toString()
    {
        return parser.toString() + "[validating=true]";
    }

}
