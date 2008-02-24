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

import net.sf.nmedit.jpatch.PConnection;
import net.sf.nmedit.jpatch.PConnector;
import net.sf.nmedit.jpatch.InvalidDescriptorException;
import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleContainer;
import net.sf.nmedit.jpatch.clavia.nordmodular.Format;
import net.sf.nmedit.jpatch.clavia.nordmodular.Knob;
import net.sf.nmedit.jpatch.clavia.nordmodular.MidiController;
import net.sf.nmedit.jpatch.clavia.nordmodular.NMPatch;
import net.sf.nmedit.jpatch.clavia.nordmodular.Note;
import net.sf.nmedit.jpatch.clavia.nordmodular.NoteSet;
import net.sf.nmedit.jpatch.clavia.nordmodular.PNMMorphSection;
import net.sf.nmedit.jpatch.clavia.nordmodular.VoiceArea;
import net.sf.nmedit.jpatch.clavia.nordmodular.PNMMorphSection.Assignments;
import net.sf.nmedit.jpatch.PParameter;

public class PatchExporter
{
    
    private final boolean MorphMapDumpBugEnabled = true;//false;
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
            PNMMorphSection morphs = p.getMorphSection();
            
            int size = 0;
            if (MorphMapDumpBugEnabled)
            {
                for (int i=0;i<morphs.getMorphCount();i++)
                {
                    size+= morphs.getAssignments(i).size();
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
                    Assignments assignments = morphs.getAssignments(i);
                    PParameter morph = morphs.getMorph(i);
                    
                    if (assignments.size()>0)
                    {
                        
                        for (PParameter pp : assignments)
                        {
                        	// TODO: in the presence of custo; parameter helper.index does not return
                        	// the same value as getDescriptorindex
                            int pindex = Helper.index(pp); //pp.getDescriptor().getDescriptorIndex();
                            //System.out.println(pp.getDescriptor().getDescriptorIndex()+" "+pindex);
                            PModule m = pp.getParentComponent();
                            PParameter morphRange;
                            try
                            {
                                morphRange = Helper.getParameter(m, "morph", Helper.index(pp));
                            }
                            catch (InvalidDescriptorException e)
                            {
                                throw new ParseException(e);
                            }
                            
                            record[0] = m.getParentComponent().getComponentIndex();
                            record[1] = m.getComponentIndex();
                            record[2] = pindex;
                            record[3] = i;
                            record[4] = morphRange.getValue();
                            handler.morphMapDump(record);
                        }
                    }
                }
                handler.endSection(PParser.IMORPHMAPDUMP);
            }
        }

        {   // KeyboardAssignment
            PNMMorphSection morphs = p.getMorphSection();
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
                    
                    String pclass = Helper.pclass(k.getParameter());
                    
                    if ("parameter".equals(pclass))
                    {
                        PParameter pp = k.getParameter();
                        PModule m = pp.getParentComponent();
                        PModuleContainer va = m.getParentComponent();
                        record[0] = va.getComponentIndex();
                        record[1] = m.getComponentIndex();
                        record[2] = Helper.index(pp);
                        record[3] = k.getID();
                        handler.knobMapDump(record);
                    }
                    else if ("morph".equals(pclass))
                    {
                        PParameter  morph = k.getParameter();
                        record[0] = Format.VALUE_SECTION_MORPH;
                        record[1] = 1; // module index = const(1)
                        record[2] = Helper.index(morph);
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

                    String pclass = Helper.pclass(mc.getParameter());
                    
                    if ("parameter".equals(pclass))
                    {
                        PParameter pp = mc.getParameter();
                        PModule m = pp.getParentComponent();
                        
                        PModuleContainer va = m.getParentComponent();

                        record[0] = va.getComponentIndex(); // voice area id
                        record[1] = m.getComponentIndex(); // index in module container
                        record[2] = Helper.index(pp);
                        record[3] = mc.getControlId();
                        
                        handler.ctrlMapDump(record);
                    }
                    else if ("morph".equals(pclass))
                    {
                        PParameter morph = mc.getParameter();

                        record[0] = Format.VALUE_SECTION_MORPH;
                        record[1] = 1; // module index = const(1)
                        record[2] = Helper.index(morph); // morph index [0..3]
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
        for (PModule m : voiceArea)
        {
            PModule nm = (PModule) m; 
            if (m.getParameterCount()>0)
            {
                int pcount = Helper.getParameterClassCount(nm, "parameter");
                int[] record = getRecord(3+pcount);
                record[0] = nm.getComponentIndex(); // index in module container
                record[1] = Helper.index(nm); // module id
                record[2] = pcount;
                
                for (int i=0;i<pcount;i++)
                    record[3+i]=Helper.getParameter(nm, "parameter", i).getValue();
                handler.parameterDump(record);
            }
        }
        handler.endSection(PParser.IPARAMETERDUMP);
    }
    
    private void customDump( PContentHandler handler, VoiceArea voiceArea ) throws ParseException
    {
        handler.beginSection(PParser.ICUSTOMDUMP, vaId(voiceArea));
        for (PModule m : voiceArea)
        {
            PModule nm = (PModule) m;
            int ccount = Helper.getParameterClassCount(nm, "custom");
            if (ccount>0)
            {
                int[] record = getRecord(2+ccount);
                record[0] = nm.getComponentIndex(); // index in module container
                record[1] = ccount;
                
                for (int i=0;i<ccount;i++)
                    record[2+i]=Helper.getParameter(nm, "custom", i).getValue();
                handler.customDump(record);
            }
        }
        handler.endSection(PParser.ICUSTOMDUMP);
    }
    
    private void moduleDump( PContentHandler handler, VoiceArea voiceArea ) throws ParseException
    {
        handler.beginSection(PParser.IMODULEDUMP, vaId(voiceArea));
        int[] record = getRecord(4);
        for (PModule mm : voiceArea)
        {
            PModule m = (PModule)mm;
            record[0] = m.getComponentIndex();
            record[1] = Helper.index(m);
            record[2] = m.getInternalX();
            record[3] = m.getInternalY();
            handler.moduleDump(record);
        }
        handler.endSection(PParser.IMODULEDUMP);
    }

    private void nameDump( PContentHandler handler, VoiceArea voiceArea ) throws ParseException
    {
        handler.beginSection(PParser.INAMEDUMP, vaId(voiceArea));
        for (PModule mm : voiceArea)
        {
            PModule m = (PModule)mm;
            handler.moduleNameDump(m.getComponentIndex(), m.getTitle());
        }
        handler.endSection(PParser.INAMEDUMP);
    }
    
    private void getConnectionRecord(int[] record, PConnection c)
    {
        PConnector dst = c.getA();
        PConnector src = c.getB();
        
        // check order
        if (dst.isOutput())
        {
            PConnector tmp = dst;
            dst = src;
            src = tmp;
        }
        
        record[0] = src.getSignalType().getId();
        record[1] = dst.getParentComponent().getComponentIndex();
        record[2] = Helper.index(dst);
        record[3] = 0;
        record[4] = src.getParentComponent().getComponentIndex();
        record[5] = Helper.index(src);
        record[6] = src.isOutput()?1:0;
    }

    private void cableDump( PContentHandler handler, VoiceArea voiceArea ) throws ParseException
    {
        handler.beginSection(PParser.ICABLEDUMP, vaId(voiceArea));
        
        final int rsize = 7;

        if (!sortCables)
        {
            int[] record = getRecord(rsize);
            
            for(PConnection c: voiceArea.getConnectionManager())
            {
                getConnectionRecord(record, c);
                handler.cableDump(record);   
            }
        }
        else
        {
            List<int[]> cables = new ArrayList<int[]>(100);

            {
                for (PConnection c: voiceArea.getConnectionManager())
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
