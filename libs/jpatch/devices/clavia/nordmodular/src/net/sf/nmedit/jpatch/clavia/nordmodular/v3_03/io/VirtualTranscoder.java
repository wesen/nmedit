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

import java.util.Iterator;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Connector;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Format;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Header;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Knob;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.MidiController;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Module;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Morph;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.MorphSet;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Note;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.NoteSet;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Parameter;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Patch;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.VoiceArea;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.misc.StandaloneRecord;

/**
 * Uses a {@link net.sf.nmedit.nomad.patch.virtual.Patch} as source and
 * feeds the {@link net.sf.nmedit.nomad.patch.builder.PatchDecoder} with the data. 
 * 
 * @author Christian Schneider
 */
public class VirtualTranscoder extends Transcoder<Patch, PatchBuilder>
{
    
    private StandaloneRecord r;

    public VirtualTranscoder()
    {
        r = new StandaloneRecord();
    }

    public void transcode(Patch patch, PatchBuilder callback) throws TranscoderException
    {
        r.reset();
        
        try
        {
            transcodeInternal(patch, callback);
        }
        catch (Exception e)
        {
            throw new TranscoderException(e);
        }
    }

    private void transcodeInternal( Patch patch, PatchBuilder callback )
    {        
        {
            r.reset();
            r.setSectionID(Format.SEC_DUMMY_PATCH_NAME);
            r.setString(patch.getName());
            callback.beginSection(r.getSectionID());
            callback.record(r);
            callback.endSection(r.getSectionID());
            r.reset();
        }
        
        {            
            r.setSectionID(Format.SEC_HEADER);
            callback.beginSection(r.getSectionID());

            // version
            r.setString(Format.VERSION_NORD_MODULAR_PATCH_3_0);
            callback.record(r);
            r.reset();
            
            // patch settings
            Header h = patch.getHeader();
            r.setSize(Format.VALUE_COUNT_HEADER);
            for (int i=0;i<Format.VALUE_COUNT_HEADER;i++)
            {
                r.setValue(i, h.getValue(i));
            }
            callback.record(r);
            r.reset();
            
            callback.endSection(r.getSectionID());
        }
        
        {   // module dump
            moduleDump(patch.getPolyVoiceArea(), callback);
            moduleDump(patch.getCommonVoiceArea(), callback);
        }
        
        {   // current note dump
            NoteSet noteSet = patch.getNoteSet();


            if (noteSet.size()>0)
            {
                r.setSize(3);
                
                // for ()
                
                r.setSectionID(Format.SEC_CURRENTNOTE_DUMP);
                callback.beginSection(r.getSectionID());
    
                r.setSize(noteSet.size()*3);
                int index = 0;
                for (Note n : noteSet)
                {
                    r.setValue(Format.CURRENT_NOTE_DUMP_NOTE+index, n.getNoteNumber());
                    r.setValue(Format.CURRENT_NOTE_DUMP_ATTACK_VELOCITY+index, n.getAttackVelocity());
                    r.setValue(Format.CURRENT_NOTE_DUMP_RELEASE_VELOCITY+index, n.getReleaseVelocity());
                    index+=3;
                }
                callback.record(r);
                r.reset();
    
                callback.endSection(r.getSectionID());
            }
        }
        
        {   // cable dump
            cableDump(patch.getPolyVoiceArea(),   callback);
            cableDump(patch.getCommonVoiceArea(),   callback);
        }
        
        {   // ParameterDump
            parameterDump(patch.getPolyVoiceArea(),  callback);
            parameterDump(patch.getCommonVoiceArea(),  callback);
        }
        
        {  // MorphMapDump

            // first : morph group
            MorphSet morphs = patch.getMorphs();

            int size = 0;
            for (int i=0;i<morphs.size();i++)
            {
                size+= morphs.get(i).size();
            }

            if (size>0)
            {
                
                // this reproduces a bug were morph values are not written
                // when no knob is assigned to any morph

                r.setSectionID(Format.SEC_MORPHMAP_DUMP);
                callback.beginSection(r.getSectionID());
                
                r.setSize(morphs.size());
                for (int i=0;i<morphs.size();i++)
                    r.setValue(i, morphs.get(i).getValue());
                callback.record(r);
    
                r.setSize(Format.VALUE_COUNT_MORPH_MAP_DUMP*size);
                int offset = 0;
                for (int i=0;i<morphs.size();i++)
                {
                    Morph morph = morphs.get(i);
                    if (morph.size()>0)
                    {
                        for (Parameter p : morph)
                        {
                            Module m = p.getModule();
                            
                            r.setValue(offset+Format.MORPH_MAP_DUMP_SECTION, Format.getVoiceAreaID(m.getVoiceArea().isPolyVoiceArea()));
                            r.setValue(offset+Format.MORPH_MAP_DUMP_MODULE_INDEX, m.getIndex());
                            r.setValue(offset+Format.MORPH_MAP_DUMP_PARAMETER_INDEX, p.getID());
                            r.setValue(offset+Format.MORPH_MAP_DUMP_MORPH_RANGE, p.getMorphRange());
                            r.setValue(offset+Format.MORPH_MAP_DUMP_MORPH_INDEX, morph.getID());
                            offset+=Format.VALUE_COUNT_MORPH_MAP_DUMP;
                        }
                    }
                }
                callback.record(r);
    
                // second: list
                r.reset();
                callback.endSection(r.getSectionID());
            }
        }
        
        {   // KeyboardAssignment
            MorphSet morphs = patch.getMorphs();
            int [] values = new int[morphs.size()];
            boolean writeKA = false;
            for (int i=0;i<morphs.size();i++)
            {
                int v = morphs.get(i).getKeyboardAssignment().getID();
                writeKA |= (v!=0); // write section only when at least one is != 0 
                values[i]=v;
            }
            
            if (writeKA)
            {
                r.setSectionID(Format.SEC_KEYBOARDASSIGNMENT);
                callback.beginSection(r.getSectionID());
    
                r.setSize(4);
                r.setValues(values);
                callback.record(r);
                r.reset();
                callback.endSection(r.getSectionID());
            }
        }
        
        {   // KnobMapDump
            
            Iterator<Knob> itAssigned = patch.getKnobs().getAssignedKnobs();
            if (itAssigned.hasNext())
            {
                r.setSectionID(Format.SEC_KNOBMAP_DUMP);
                callback.beginSection(r.getSectionID());

                r.setSize(Format.VALUE_COUNT_KNOBMAP_DUMP);
                
                for (Knob k;itAssigned.hasNext();)
                {
                    k = itAssigned.next();
                    if (k.getAssignment() instanceof Parameter)
                    {
                        Parameter p = (Parameter) k.getAssignment();
                        Module m = p.getModule();

                        r.setValue(Format.KNOB_MAP_DUMP_SECTION_INDEX, Format.getVoiceAreaID(m.getVoiceArea().isPolyVoiceArea()));
                        r.setValue(Format.KNOB_MAP_DUMP_MODULE_INDEX, m.getIndex());
                        r.setValue(Format.KNOB_MAP_DUMP_PARAMETER_INDEX, p.getID());
                    }
                    else
                    {
                        Morph morph = (Morph) k.getAssignment();

                        r.setValue(Format.KNOB_MAP_DUMP_SECTION_INDEX, Format.VALUE_SECTION_MORPH);
                        r.setValue(Format.KNOB_MAP_DUMP_MODULE_INDEX, 1);
                        r.setValue(Format.KNOB_MAP_DUMP_PARAMETER_INDEX, morph.getID());
                    }
                    r.setValue(Format.KNOB_MAP_DUMP_KNOB_INDEX, k.getID());
                    callback.record(r);
                }
                
                r.reset();
                callback.endSection(r.getSectionID());
            }
        }
        
        {   // CtrlMapDump
            
            Iterator<MidiController> itAssigned = 
                patch.getMidiControllers().getAssignedMidiControllers();
            
            if (itAssigned.hasNext())
            {
                r.setSectionID(Format.SEC_CTRLMAP_DUMP);
                callback.beginSection(r.getSectionID());

                r.setSize(Format.VALUE_COUNT_CTRL_MAP_DUMP);
                
                for (MidiController mc;itAssigned.hasNext();)
                {
                    mc = itAssigned.next();
                    
                    if (mc.getAssignment() instanceof Parameter)
                    {
                        Parameter p = (Parameter) mc.getAssignment();
                        Module m = p.getModule();
                        
                        // TODO
                        r.setValue(Format.CTRL_MAP_DUMP_MODULE_INDEX, m.getIndex());
                        r.setValue(Format.CTRL_MAP_DUMP_PARAMETER_INDEX, p.getID());
                        r.setValue(Format.CTRL_MAP_DUMP_SECTION_INDEX, Format.getVoiceAreaID(m.getVoiceArea().isPolyVoiceArea()));
                    }
                    else 
                    {
                        Morph morph = (Morph) mc.getAssignment();
    
                        r.setValue(Format.CTRL_MAP_DUMP_MODULE_INDEX, 1);
                        r.setValue(Format.CTRL_MAP_DUMP_PARAMETER_INDEX, morph.getID());
                        r.setValue(Format.CTRL_MAP_DUMP_SECTION_INDEX, Format.VALUE_SECTION_MORPH);
                    }
    
                    r.setValue(Format.CTRL_MAP_DUMP_CC_INDEX, mc.getID());
                    callback.record(r);
                }
                r.reset();
                callback.endSection(r.getSectionID());
            }
        }
        
        {   // CustomDump
            customDump(patch.getPolyVoiceArea(),  callback);
            customDump(patch.getCommonVoiceArea(),  callback);
        }
        
        {   // NameDump
            nameDump(patch.getPolyVoiceArea(),  callback);
            nameDump(patch.getCommonVoiceArea(),  callback);
        }
        
        {   // notes
            
            String notes = patch.getNote();
            
            if (notes==null) notes = "";
            else notes = Format.getEscapedNote(notes);

            if (notes.length()>0)
            {
                
                notes = notes.replaceAll("\r", "");
                
                r.setSectionID(Format.SEC_NOTE);
                callback.beginSection(r.getSectionID());
                
                for (String line : notes.split("\n"))
                {
                    r.setString(line);
                    callback.record(r);
                }
                
                callback.endSection(r.getSectionID());
                r.reset();
            }
        }
        
        r.reset();
    }

    private void parameterDump( VoiceArea va, PatchBuilder callback )
    {
        r.setSectionID(Format.SEC_PARAMETER_DUMP);
        callback.beginSection(r.getSectionID());

        r.setSize(1);
        r.setValue(0, Format.getVoiceAreaID(va.isPolyVoiceArea()));
        callback.record(r);
        r.reset();
        
        for (Module m : va)
        {
            if (m.getParameterCount()>0)
            {
                r.setSize(3+m.getParameterCount());
                r.setValue(Format.PARAMETER_DUMP_MODULE_INDEX,m.getIndex());
                r.setValue(Format.PARAMETER_DUMP_MODULE_TYPE, m.getID());
                r.setValue(Format.PARAMETER_DUMP_PARAMETER_COUNT, m.getParameterCount());
                
                for (int j=0;j<m.getParameterCount();j++)
                {
                    r.setValue(Format.PARAMETER_DUMP_PARAMETER_BASE+j, m.getParameter(j).getValue());
                }
                
                callback.record(r);
            }
        }

        callback.endSection(r.getSectionID());
    }
    
    private void customDump( VoiceArea va, PatchBuilder callback )
    {
        r.setSectionID(Format.SEC_CUSTOM_DUMP);
        callback.beginSection(r.getSectionID());

        r.setSize(1);
        r.setValue(0, Format.getVoiceAreaID(va.isPolyVoiceArea()));
        callback.record(r);
        r.reset();
        
        for (Module m : va)
        {
            if (m.getCustomCount()>0)
            {
                r.setSize(2+m.getCustomCount());
                r.setValue(Format.CUSTOM_DUMP_MODULE_INDEX, m.getIndex());
                r.setValue(Format.CUSTOM_DUMP_PARAMETER_COUNT, m.getCustomCount());
                
                for (int j=0;j<m.getCustomCount();j++)
                {
                    r.setValue(Format.CUSTOM_DUMP_PARAMETER_BASE+j, m.getCustom(j).getValue());
                }
                
                callback.record(r);
            }
        }

        r.reset();
        callback.endSection(r.getSectionID());
    }
    
    private void nameDump( VoiceArea va, PatchBuilder callback )
    {
        r.setSectionID(Format.SEC_NAME_DUMP);
        callback.beginSection(r.getSectionID());

        r.setSize(1);
        r.setValue(0, Format.getVoiceAreaID(va.isPolyVoiceArea()));
        callback.record(r);
        r.reset();
        
        r.setSize(1);
        for (Module m : va)
        {
            r.setValue(Format.NAME_DUMP_MODULE_INDEX, m.getIndex());
            r.setString(m.getName());
            callback.record(r);
        }
        r.reset();
        callback.endSection(r.getSectionID());
    }
    
    private void cableDump( VoiceArea va, PatchBuilder callback )
    {
        r.setSectionID(Format.SEC_CABLE_DUMP);
        callback.beginSection(r.getSectionID());

        r.setSize(1);
        r.setValue(0, Format.getVoiceAreaID(va.isPolyVoiceArea()));
        callback.record(r);
        r.reset();

        r.setSize(Format.VALUE_COUNT_CABLE_DUMP);
        
        for (Module m : va)
        {
            for (int j=m.getConnectorCount()-1;j>=0;j--)
            {
                // first connector (dst) triple is always an input
                // the second (src) triple is either in, or output
                
                Connector dst = m.getConnector(j);
                Connector src = dst.getParent();
                if (src!=null)
                {
                    // only src can be an output
                    r.setValue(Format.CABLE_DUMP_COLOR, src.getConnectionColor().getSignalID());

                    r.setValue(Format.CABLE_DUMP_CONNECTOR_INDEX_DESTINATION, dst.getDefinition().getId());
                    r.setValue(Format.CABLE_DUMP_CONNECTOR_TYPE_DESTINATION, Format.getOutputID(dst.isOutput()));
                    r.setValue(Format.CABLE_DUMP_MODULE_INDEX_DESTINATION, dst.getModule().getIndex());
                    
                    r.setValue(Format.CABLE_DUMP_CONNECTOR_INDEX_SOURCE, src.getDefinition().getId());
                    r.setValue(Format.CABLE_DUMP_CONNECTOR_TYPE_SOURCE, Format.getOutputID(src.isOutput()));
                    r.setValue(Format.CABLE_DUMP_MODULE_INDEX_SOURCE, src.getModule().getIndex());
                    callback.record(r);
                }
            }   
        }
        
        r.reset();
        callback.endSection(r.getSectionID());
    }

    private void moduleDump(VoiceArea va, PatchBuilder callback)
    {           
        r.setSectionID(Format.SEC_MODULE_DUMP);
        callback.beginSection(r.getSectionID());
        
        // voice area ID
        r.setSize(1);
        r.setValue(0, Format.getVoiceAreaID(va.isPolyVoiceArea()));
        callback.record(r);
        r.reset();
        
        // module dump
        r.setSize(4);
        
        for (Module m : va)
        {
            r.setValue(Format.MODULE_DUMP_MODULE_INDEX, m.getIndex());
            r.setValue(Format.MODULE_DUMP_MODULE_TYPE, m.getID());
            r.setValue(Format.MODULE_DUMP_MODULE_XPOS, m.getX());
            r.setValue(Format.MODULE_DUMP_MODULE_YPOS, m.getY());
            callback.record(r);
        }
        r.reset();
        
        callback.endSection(r.getSectionID());

    }
    
}
