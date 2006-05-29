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
 * Created on Apr 17, 2006
 */
package net.sf.nmedit.nomad.patch.builder;

import net.sf.nmedit.nomad.patch.Format;
import net.sf.nmedit.nomad.patch.misc.Record;
import net.sf.nmedit.nomad.patch.virtual.Connector;
import net.sf.nmedit.nomad.patch.virtual.Header;
import net.sf.nmedit.nomad.patch.virtual.KeyboardAssignment;
import net.sf.nmedit.nomad.patch.virtual.Knob;
import net.sf.nmedit.nomad.patch.virtual.MidiController;
import net.sf.nmedit.nomad.patch.virtual.Module;
import net.sf.nmedit.nomad.patch.virtual.Morph;
import net.sf.nmedit.nomad.patch.virtual.MorphSet;
import net.sf.nmedit.nomad.patch.virtual.Note;
import net.sf.nmedit.nomad.patch.virtual.NoteSet;
import net.sf.nmedit.nomad.patch.virtual.Parameter;
import net.sf.nmedit.nomad.patch.virtual.Patch;
import net.sf.nmedit.nomad.patch.virtual.VoiceArea;
import net.sf.nmedit.nomad.xml.dom.module.DConnector;
import net.sf.nmedit.nomad.xml.dom.module.ModuleDescriptions;

/**
 * Builder that builds the virtial patch target.
 * 
 * @author Christian Schneider
 */
public class VirtualBuilder implements PatchBuilder
{

    /**
     * the virtual patch
     */
    private Patch patch;

    /**
     * the user's notes buffer
     */
    private StringBuffer notes;

    /**
     * the selected voice area
     */
    private VoiceArea va;

    /**
     * true, if a new section is going to be read and the current record is the
     * first record in the section
     */
    private boolean firstRecord;

    /**
     * Creates the builder for a new target.
     */
    public VirtualBuilder()
    {
        this( new Patch() );
    }

    /**
     * Creates the builder using an existing patch as target. This is usefull,
     * if the target is not built completely
     * 
     * @param patch the target
     */
    public VirtualBuilder( Patch patch )
    {
        super();
        this.patch = patch;
        notes = new StringBuffer();
        va = null;
    }

    /**
     * Returns the target
     * 
     * @return the target
     */
    public Patch getPatch()
    {
        return patch;
    }

    public void beginSection( int ID )
    {
        switch (ID)
        {
            case Format.SEC_NOTES:
            {
                notes.replace( 0, notes.length(), "" );
            }
                break;
        }
        firstRecord = true;
    }

    public void endSection( int ID )
    {
        switch (ID)
        {
            case Format.SEC_NOTES:
            {
                // remove last \n because it does not belong there
                notes.replace( notes.length() - 1, notes.length() - 1, "" );
                patch.setNote( notes.toString() );
            }
                break;
        }
        va = null;
    }

    /**
     * Set's the {@link #va} field according to the voice area ID
     * 
     * @param ID voice area ID
     */
    private void selectVoiceArea( int ID )
    {
        switch (ID)
        {
            case Format.VALUE_SECTION_VOICE_AREA_POLY:
                va = patch.getPolyVoiceArea();
                break;
            case Format.VALUE_SECTION_VOICE_AREA_COMMON:
                va = patch.getCommonVoiceArea();
                break;
            default:
                va = null;
                break;
        }
    }

    /**
     * The voice area ID is in most cases the first record of a section. If r is
     * the first record it reads the value of the record and sets the
     * {@link #va} field according to the value.
     * 
     * @param r current record
     * @return true when r is the first record and the voice area was set.
     */
    private boolean selectVoiceArea( Record r )
    {
        if (firstRecord)
        {
            // if (r.getValueCount()!=1) exception
            selectVoiceArea( r.getValue( 0 ) );
            return true;
        }
        else
        {
            return false;
        }

    }

    public void record( Record r )
    {
        switch (r.getSectionID())
        {
            case Format.SEC_DUMMY_PATCH_NAME:
            {
                patch.setName( r.getString() );
            }
                break;
            case Format.SEC_HEADER:
            {
                if (r.getValueCount() == Format.VALUE_COUNT_HEADER)
                {
                    Header h = patch.getHeader();
                    for (int i = r.getValueCount() - 1; i >= 0; i--)
                    {
                        h.setValue( i, r.getValue( i ) );
                    }
                }
            }
                break;
            case Format.SEC_NOTES:
            {
                notes.append( r.getString() + "\n" );
            }
                break;
            case Format.SEC_MODULE_DUMP:
            {
                if (!selectVoiceArea( r ))
                {
                    Module module = new Module(
                            ModuleDescriptions
                                    .sharedInstance()
                                    .getModuleById(
                                            r
                                                    .getValue( Format.MODULE_DUMP_MODULE_TYPE ) ) );
                    module.setIndex( r
                            .getValue( Format.MODULE_DUMP_MODULE_INDEX ) );
                    module.setLocation( r
                            .getValue( Format.MODULE_DUMP_MODULE_XPOS ), r
                            .getValue( Format.MODULE_DUMP_MODULE_YPOS ) );
                    va.add( module );
                }
            }
                break;
            case Format.SEC_CURRENTNOTE_DUMP:
            {
                // TODO note set removes duplicates - see patch format bug

                NoteSet noteSet = patch.getNoteSet();
                for (int i = 0; i < r.getValueCount(); i += 3)
                {
                    Note note = new Note(
                            r.getValue( Format.CURRENT_NOTE_DUMP_NOTE + i ),
                            r
                                    .getValue( Format.CURRENT_NOTE_DUMP_ATTACK_VELOCITY
                                            + i ),
                            r
                                    .getValue( Format.CURRENT_NOTE_DUMP_RELEASE_VELOCITY
                                            + i ) );
                    noteSet.add( note );
                }
            }
                break;
            // case SEC_UNKOWN_SECTION: ;
            case Format.SEC_CABLE_DUMP:
                ;
                {
                    if (!selectVoiceArea( r ))
                    {
                        // color not required
                        // r.getValue(Format.CABLE_DUMP_COLOR);

                        Module msrc = va
                                .get( r
                                        .getValue( Format.CABLE_DUMP_MODULE_INDEX_SOURCE ) );
                        Module mdst = va
                                .get( r
                                        .getValue( Format.CABLE_DUMP_MODULE_INDEX_DESTINATION ) );

                        DConnector isrc = msrc
                                .getDefinition()
                                .getConnectorById(
                                        r
                                                .getValue( Format.CABLE_DUMP_CONNECTOR_INDEX_SOURCE ),
                                        r
                                                .getValue( Format.CABLE_DUMP_CONNECTOR_TYPE_SOURCE ) == 0 );

                        DConnector idst = mdst
                                .getDefinition()
                                .getConnectorById(
                                        r
                                                .getValue( Format.CABLE_DUMP_CONNECTOR_INDEX_DESTINATION ),
                                        r
                                                .getValue( Format.CABLE_DUMP_CONNECTOR_TYPE_DESTINATION ) == 0 );

                        if (idst == null || isrc == null)
                        {
                            System.err.println( "Connector not found" );
                        }
                        else
                        {
                            Connector csrc = msrc.getConnector( isrc
                                    .getContextId() );
                            Connector cdst = mdst.getConnector( idst
                                    .getContextId() );
                            csrc.connect( cdst );
                        }
                    }
                }
                break;
            case Format.SEC_MORPHMAP_DUMP:
            {
                MorphSet morphs = patch.getMorphs();
                if (r.getValueCount() == 4)
                {
                    morphs.get( 0 ).setValue(
                            r.getValue( Format.MORPH_MAP_DUMP_VALUES_MORPH1 ) );
                    morphs.get( 1 ).setValue(
                            r.getValue( Format.MORPH_MAP_DUMP_VALUES_MORPH2 ) );
                    morphs.get( 2 ).setValue(
                            r.getValue( Format.MORPH_MAP_DUMP_VALUES_MORPH3 ) );
                    morphs.get( 3 ).setValue(
                            r.getValue( Format.MORPH_MAP_DUMP_VALUES_MORPH4 ) );
                }
                else
                {
                    for (int i = 0; i < r.getValueCount(); i += 5)
                    {
                        selectVoiceArea( r
                                .getValue( Format.MORPH_MAP_DUMP_SECTION + i ) );

                        Module module = va.get( r
                                .getValue( Format.MORPH_MAP_DUMP_MODULE_INDEX
                                        + i ) );
                        Parameter p = module
                                .getParameter( r
                                        .getValue( Format.MORPH_MAP_DUMP_PARAMETER_INDEX
                                                + i ) );

                        p.setMorphRange( r
                                .getValue( Format.MORPH_MAP_DUMP_MORPH_RANGE
                                        + i ) );

                        morphs.get(
                                r.getValue( Format.MORPH_MAP_DUMP_MORPH_INDEX
                                        + i ) ).add( p );
                    }
                }
            }
                break;
            case Format.SEC_KEYBOARDASSIGNMENT:
            {
                MorphSet morphs = patch.getMorphs();
                morphs
                        .get( 0 )
                        .setKeyboardAssignment(
                                KeyboardAssignment
                                        .byID( r
                                                .getValue( Format.KEYBOARD_ASSIGNMENT_MORPH1 ) ) );
                morphs
                        .get( 1 )
                        .setKeyboardAssignment(
                                KeyboardAssignment
                                        .byID( r
                                                .getValue( Format.KEYBOARD_ASSIGNMENT_MORPH2 ) ) );
                morphs
                        .get( 2 )
                        .setKeyboardAssignment(
                                KeyboardAssignment
                                        .byID( r
                                                .getValue( Format.KEYBOARD_ASSIGNMENT_MORPH3 ) ) );
                morphs
                        .get( 3 )
                        .setKeyboardAssignment(
                                KeyboardAssignment
                                        .byID( r
                                                .getValue( Format.KEYBOARD_ASSIGNMENT_MORPH4 ) ) );
            }
                break;
            case Format.SEC_KNOBMAP_DUMP:
            {
                switch (r.getValue( 0 ))
                {
                    case Format.VALUE_SECTION_MORPH:
                    {
                        Morph morph = patch
                                .getMorphs()
                                .get(
                                        r
                                                .getValue( Format.KNOB_MAP_DUMP_PARAMETER_INDEX ) );
                        Knob k = patch.getKnobs().getByID(
                                r.getValue( Format.KNOB_MAP_DUMP_KNOB_INDEX ) );
                        k.assignTo( morph );
                    }
                        break;

                    case Format.VALUE_SECTION_VOICE_AREA_COMMON:
                    case Format.VALUE_SECTION_VOICE_AREA_POLY:
                    {
                        selectVoiceArea( r.getValue( 0 ) );
                        Module module = va.get( r
                                .getValue( Format.KNOB_MAP_DUMP_MODULE_INDEX ) );
                        Parameter p = module
                                .getParameter( r
                                        .getValue( Format.KNOB_MAP_DUMP_PARAMETER_INDEX ) );

                        Knob k = patch.getKnobs().getByID(
                                r.getValue( Format.KNOB_MAP_DUMP_KNOB_INDEX ) );
                        k.assignTo( p );
                    }
                        break;
                }
            }
                break;
            case Format.SEC_NAME_DUMP:
            {
                if (!selectVoiceArea( r ))
                {
                    Module module = va.get( r
                            .getValue( Format.NAME_DUMP_MODULE_INDEX ) );
                    module.setName( r.getString() );
                }
            }
                break;
            case Format.SEC_CTRLMAP_DUMP:
            {

                switch (r.getValue( Format.CTRL_MAP_DUMP_SECTION_INDEX ))
                {
                    case Format.VALUE_SECTION_MORPH:
                    {
                        Morph morph = patch
                                .getMorphs()
                                .get(
                                        r
                                                .getValue( Format.CTRL_MAP_DUMP_PARAMETER_INDEX ) );
                        MidiController mc = patch.getMidiControllers().getByMC(
                                r.getValue( Format.CTRL_MAP_DUMP_CC_INDEX ) );
                        mc.assignTo( morph );
                    }
                        break;

                    case Format.VALUE_SECTION_VOICE_AREA_POLY:
                    case Format.VALUE_SECTION_VOICE_AREA_COMMON:
                    {
                        selectVoiceArea( r.getValue( 0 ) );
                        Module module = va.get( r
                                .getValue( Format.CTRL_MAP_DUMP_MODULE_INDEX ) );
                        Parameter p = module
                                .getParameter( r
                                        .getValue( Format.CTRL_MAP_DUMP_PARAMETER_INDEX ) );
                        MidiController mc = patch.getMidiControllers().getByMC(
                                r.getValue( Format.CTRL_MAP_DUMP_CC_INDEX ) );
                        mc.assignTo( p );
                    }
                        break;

                    default:
                        System.err
                                .println( "Unrecognized section CTRLMAP_DUMP: "
                                        + r
                                                .getValue( Format.CTRL_MAP_DUMP_SECTION_INDEX ) );
                        break;
                }
            }
                break;
            case Format.SEC_PARAMETER_DUMP:
            {
                if (!selectVoiceArea( r ))
                {
                    Module module = va.get( r
                            .getValue( Format.PARAMETER_DUMP_MODULE_INDEX ) );

                    if (module.getParameterCount() != r
                            .getValue( Format.PARAMETER_DUMP_PARAMETER_COUNT ))
                    {
                        // TODO better error handling
                        System.err
                                .println( "Module has different number of parameters: "
                                        + module
                                        + ": paramCount(module)="
                                        + module.getParameterCount()
                                        + ", paramCount(record)="
                                        + r
                                                .getValue( Format.PARAMETER_DUMP_PARAMETER_COUNT ) );
                    }

                    else

                    for (int i = module.getParameterCount() - 1; i >= 0; i--)
                    {
                        module
                                .getParameter( i )
                                .setValue(
                                        r
                                                .getValue( Format.PARAMETER_DUMP_PARAMETER_BASE
                                                        + i ) );
                    }
                }
            }
                break;
            case Format.SEC_CUSTOM_DUMP:
            {
                if (!selectVoiceArea( r ))
                {
                    Module module = va.get( r
                            .getValue( Format.CUSTOM_DUMP_MODULE_INDEX ) );
                    for (int i = module.getCustomCount() - 1; i >= 0; i--)
                    {
                        module.getCustom( i ).setValue(
                                r.getValue( Format.CUSTOM_DUMP_PARAMETER_BASE
                                        + i ) );
                    }
                }
            }
                break;
        }

        firstRecord = false;
    }
}
