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
 * Created on Dec 21, 2006
 */
package net.sf.nmedit.jpatch.clavia.nordmodular.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import net.sf.nmedit.jpatch.Connection;
import net.sf.nmedit.jpatch.Connector;
import net.sf.nmedit.jpatch.InvalidDescriptorException;
import net.sf.nmedit.jpatch.Module;
import net.sf.nmedit.jpatch.Parameter;
import net.sf.nmedit.jpatch.clavia.nordmodular.Knob;
import net.sf.nmedit.jpatch.clavia.nordmodular.MidiController;
import net.sf.nmedit.jpatch.clavia.nordmodular.Morph;
import net.sf.nmedit.jpatch.clavia.nordmodular.MorphSection;
import net.sf.nmedit.jpatch.clavia.nordmodular.NMConnector;
import net.sf.nmedit.jpatch.clavia.nordmodular.NMModule;
import net.sf.nmedit.jpatch.clavia.nordmodular.NMParameter;
import net.sf.nmedit.jpatch.clavia.nordmodular.NMPatch;
import net.sf.nmedit.jpatch.clavia.nordmodular.Note;
import net.sf.nmedit.jpatch.clavia.nordmodular.NoteSet;
import net.sf.nmedit.jpatch.clavia.nordmodular.VoiceArea;

public class PatchExporter
{
    
    private final boolean MorphMapDumpBugEnabled = false;
    private final boolean sortCables = true;

    public void export (NMPatch p, PContentHandler handler) throws ParseException
    {
        handler.beginDocument();
        
        // header
        handler.beginSection(PParser.IHEADER,-1);
        handler.header("Version", "Nord Modular patch 3.0");
        handler.header(p.getHeader().getData());
        handler.endSection(PParser.IHEADER);
        //

        {   // module dump
            moduleDump(handler, p.getPolyVoiceArea());
            moduleDump(handler, p.getCommonVoiceArea());
        }

        {   // current note dump
            NoteSet noteSet = p.getNoteSet();
            if (noteSet.size()>0)
            {
                handler.beginSection(PParser.ICURRENTNOTEDUMP,-1);
                int[] record = getRecord(3);
                for (Note n : noteSet)
                {
                    record[0] = n.getNoteNumber();
                    record[1] = n.getAttackVelocity();
                    record[2] = n.getReleaseVelocity();
                    handler.currentNoteDump(record);
                }
                handler.endSection(PParser.ICURRENTNOTEDUMP);
            }
        }

        {   // cable dump
            cableDump(handler, p.getPolyVoiceArea());
            cableDump(handler, p.getCommonVoiceArea());
        }
        
        {   // ParameterDump
            parameterDump(handler, p.getPolyVoiceArea());
            parameterDump(handler, p.getCommonVoiceArea());
        }

        {  // MorphMapDump

            // first : morph group
            MorphSection morphs = p.getMorphSection();
            
            int size = 0;
            if (MorphMapDumpBugEnabled)
            {
                for (int i=0;i<morphs.getMorphCount();i++)
                {
                    size+= morphs.getMorph(i).getAssignmentsCount();
                }
            }
            else
            {
                size = 1; // >0
            }

            if (size>0)
            {
                
                // this reproduces a bug were morph values are not written
                // when no knob is assigned to any morph

                handler.beginSection(PParser.IMORPHMAPDUMP, -1);
                {
                    int[] record = getRecord(4);
                    for (int i=0;i<morphs.getMorphCount();i++)
                    {
                        record[i] = morphs.getMorph(i).getValue();
                    }
                    handler.morphMapDumpProlog(record);
                }

                int[] record = getRecord(5);
                for (int i=0;i<morphs.getMorphCount();i++)
                {
                    Morph morph = morphs.getMorph(i);
                    if (morph.getAssignmentsCount()>0)
                    {
                        
                        for (NMParameter pp : morph.getAssignments())
                        {
                            int pindex = pp.getDescriptor().getIndex();
                            NMModule m = pp.getOwner();
                            Parameter morphRange;
                            try
                            {
                                morphRange = m.getParameter(m.getDescriptor().getParameter( pindex, "morph" ));
                            }
                            catch (InvalidDescriptorException e)
                            {
                                throw new ParseException(e);
                            }
                            
                            record[0] = vaId(m.getParent());
                            record[1] = m.getIndex();
                            record[2] = pindex;
                            record[3] = morph.getDescriptor().getMorphId();
                            record[4] = morphRange.getValue();
                        }
                    }
                }
                handler.endSection(PParser.IMORPHMAPDUMP);
            }
        }

        {   // KeyboardAssignment
            MorphSection morphs = p.getMorphSection();
            int [] record = getRecord(morphs.getMorphCount());
            boolean writeKA = false;
            for (int i=0;i<morphs.getMorphCount();i++)
            {
                int v = morphs.getKeyboardAssignment(i).getValue();
                writeKA |= (v!=0); // write section only when at least one is != 0 
                record[i]=v;
            }
            
            if (writeKA)
            {
                handler.beginSection(PParser.IKEYBOARDASSIGNMENT, -1);
                handler.keyboardAssignment(record);
                handler.endSection(PParser.IKEYBOARDASSIGNMENT);
            }
        }
        
        {   // KnobMapDump
            
            Iterator<Knob> iter = p.getKnobs().iterator();
            
            boolean hasAssignedKnobs = false;
            
            int[] record = getRecord(4);
            
            while (iter.hasNext())
            {
                Knob k = iter.next();
                
                if (k.getParameter() != null)
                {
                    if (!hasAssignedKnobs)
                    {    
                        hasAssignedKnobs = true;
                        handler.beginSection(PParser.IKNOBMAPDUMP, -1);
                    }
                    
                    String pclass = k.getParameter().getDescriptor().getParameterClass();
                    
                    if ("parameter".equals(pclass))
                    {
                        NMParameter pp = (NMParameter) k.getParameter();
                        NMModule m = pp.getOwner();
                        record[0] = vaId(m.getParent());
                        record[1] = m.getIndex();
                        record[2] = pp.getDescriptor().getIndex();
                        record[3] = k.getID();
                        handler.knobMapDump(record);
                    }
                    else if ("morph".equals(pclass))
                    {
                        Morph morph = (Morph) k.getParameter();
                        record[0] = 2;
                        record[1] = 1; // module index = const(1)
                        record[2] = morph.getDescriptor().getMorphId();
                        record[3] = k.getID();
                        handler.knobMapDump(record);
                    }
                }
            }
            if (hasAssignedKnobs)
            {
                handler.endSection(PParser.IKNOBMAPDUMP);
            }
        }
        
        {   // CtrlMapDump
            
            Iterator<MidiController> iter = p.getMidiControllers().iterator();
            
            boolean hasAssignedMCtrl = false;

            int[] record = getRecord(4);
            
            while (iter.hasNext())
            {
                MidiController mc = iter.next();
                if (mc.getParameter() != null)
                {
                    if (!hasAssignedMCtrl)
                    {
                        hasAssignedMCtrl = true;
                        handler.beginSection(PParser.ICTRLMAPDUMP, -1);
                    }

                    String pclass = mc.getParameter().getDescriptor().getParameterClass();
                    
                    if ("parameter".equals(pclass))
                    {
                        NMParameter pp = (NMParameter) mc.getParameter();
                        NMModule m = pp.getOwner();

                        record[0] = vaId(m.getParent());
                        record[1] = m.getIndex();
                        record[2] = pp.getDescriptor().getIndex();
                        record[3] = mc.getControlId();
                        
                        handler.ctrlMapDump(record);
                    }
                    else if ("morph".equals(pclass))
                    {
                        Morph morph = (Morph) mc.getParameter();

                        record[0] = 2;
                        record[1] = 1; // module index = const(1)
                        record[2] = morph.getDescriptor().getMorphId(); // morph index [0..3]
                        record[3] = mc.getControlId();
                        
                        handler.ctrlMapDump(record);
                    }
                }
            }
            
            if (hasAssignedMCtrl)
                handler.endSection(PParser.ICTRLMAPDUMP);
        }

        {   // CustomDump
            customDump(handler, p.getPolyVoiceArea());
            customDump(handler, p.getCommonVoiceArea());
        }
        
        {   // NameDump
            nameDump(handler, p.getPolyVoiceArea());
            nameDump(handler, p.getCommonVoiceArea());
        }
        
        // notes
        handler.beginSection(PParser.INOTES, -1);
        String note = p.getNote();
        if (note == null)
            note = "";
        // fix newline characters
        note = note.replaceAll("\\r", "").replaceAll("\\n","\r\n");
        handler.notes(note);
        handler.endSection(PParser.INOTES);
        
        handler.endDocument();
    }
    
    private int vaId(VoiceArea va)
    {
        if (va == null) return 2;
        else return va.isPolyVoiceArea() ? 1 : 0;
    }

    private void parameterDump( PContentHandler handler, VoiceArea voiceArea ) throws ParseException
    {
        handler.beginSection(PParser.IPARAMETERDUMP, vaId(voiceArea));
        for (Module m : voiceArea)
        {
            NMModule nm = (NMModule) m; 
            if (m.getParameterCount()>0)
            {
                int[] record = getRecord(3+nm.getParamCount());
                record[0] = nm.getIndex();
                record[1] = nm.getModuleId();
                record[2] = nm.getParamCount();
                
                for (int i=0;i<nm.getParamCount();i++)
                    record[3+i]=nm.getParameter(i).getValue();
                handler.parameterDump(record);
            }
        }
        handler.endSection(PParser.IPARAMETERDUMP);
    }
    
    private void customDump( PContentHandler handler, VoiceArea voiceArea ) throws ParseException
    {
        handler.beginSection(PParser.ICUSTOMDUMP, vaId(voiceArea));
        for (Module m : voiceArea)
        {
            NMModule nm = (NMModule) m;
            if (nm.getCustomCount()>0)
            {
                int[] record = getRecord(2+nm.getCustomCount());
                record[0] = nm.getIndex();
                record[1] = nm.getCustomCount();
                
                for (int i=0;i<nm.getCustomCount();i++)
                    record[2+i]=nm.getCustom(i).getValue();
                handler.customDump(record);
            }
        }
        handler.endSection(PParser.ICUSTOMDUMP);
    }
    
    private void moduleDump( PContentHandler handler, VoiceArea voiceArea ) throws ParseException
    {
        handler.beginSection(PParser.IMODULEDUMP, vaId(voiceArea));
        int[] record = getRecord(4);
        for (Module mm : voiceArea)
        {
            NMModule m = (NMModule)mm;
            record[0] = m.getIndex();
            record[1] = m.getID();
            record[2] = m.getX();
            record[3] = m.getY();
            handler.moduleDump(record);
        }
        handler.endSection(PParser.IMODULEDUMP);
    }

    private void nameDump( PContentHandler handler, VoiceArea voiceArea ) throws ParseException
    {
        handler.beginSection(PParser.INAMEDUMP, vaId(voiceArea));
        for (Module mm : voiceArea)
        {
            NMModule m = (NMModule)mm;
            handler.moduleNameDump(m.getIndex(), m.getTitle());
        }
        handler.endSection(PParser.INAMEDUMP);
    }
    
    private void getConnectionRecord(int[] record, Connection c)
    {
        Connector dst = c.getDestination();
        Connector src = c.getSource();
        
        // check order
        if (dst.isOutput())
        {
            Connector tmp = dst;
            dst = src;
            src = tmp;
        }
        
        record[0] = ((NMConnector)src).getConnectionColor().getSignalID();
        record[1] = ((NMModule)dst.getOwner()).getIndex();
        record[2] = dst.getDescriptor().getIndex();
        record[3] = 0;
        record[4] = ((NMModule)src.getOwner()).getIndex();
        record[5] = src.getDescriptor().getIndex();
        record[6] = src.isOutput()?1:0;
    }

    private void cableDump( PContentHandler handler, VoiceArea voiceArea ) throws ParseException
    {
        handler.beginSection(PParser.ICABLEDUMP, vaId(voiceArea));
        
        final int rsize = 7;

        if (!sortCables)
        {
            int[] record = getRecord(rsize);
            
            for(Connection c: voiceArea.getConnectionManager())
            {
                getConnectionRecord(record, c);
                handler.cableDump(record);   
            }
        }
        else
        {
            List<int[]> cables = new ArrayList<int[]>(100);

            {
                for (Connection c: voiceArea.getConnectionManager())
                {
                    int[] r = new int[rsize];
                    getConnectionRecord(r, c);
                    
                    // if both connectors are inputs (0) 
                    // then we assure that the first connector in the record
                    // has a module index less or equal than the second one
                    if (r[3] == 0 && r[6] == 0 && (r[1]>r[4] || (r[1]==r[4] && r[2]>r[5])))
                    {
                        for (int i=1;i<3+1;i++)
                        { 
                            int t = r[i];
                            r[i]  = r[i+3];
                            r[i+3]= t;
                            
                        }
                    }
                    cables.add(r);
                }
            }
            
            Collections.sort(cables, new CableSort());
            Iterator<int[]> iter = cables.iterator();
            while (iter.hasNext())
            {
                handler.cableDump(iter.next());   
            }
            
        }
        handler.endSection(PParser.ICABLEDUMP);
    }
    
    private int[] cachedRecord = new int[10];
    
    private int[] getRecord(int size)
    {
        if (cachedRecord.length<size)
            cachedRecord = new int[size];
        return cachedRecord;
    }
    
    private static class CableSort implements Comparator<int[]>
    {
        // sorting order
        final static int[] permutation = new int[] { 0, 1, 3, 2, 4, 6, 5 };

        public int compare( int[] o1, int[] o2 )
        {
            int p;
            int r;
            for (int i=0;i<permutation.length;i++)
            {
                p = permutation[i];
                r = o1[p]-o2[p];
                if (r!=0)
                    return r;
            }
            return 0;
        }
        
    }

}
