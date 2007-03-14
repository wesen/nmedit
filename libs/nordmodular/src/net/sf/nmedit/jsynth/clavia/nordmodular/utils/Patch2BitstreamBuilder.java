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
 * Created on May 25, 2006
 */
package net.sf.nmedit.jsynth.clavia.nordmodular.utils;

import java.util.ArrayList;
import java.util.List;

import net.sf.nmedit.jnmprotocol.PDLData;
import net.sf.nmedit.jpatch.Module;
import net.sf.nmedit.jpatch.Parameter;
import net.sf.nmedit.jpatch.clavia.nordmodular.Format;
import net.sf.nmedit.jpatch.clavia.nordmodular.Header;
import net.sf.nmedit.jpatch.clavia.nordmodular.Knob;
import net.sf.nmedit.jpatch.clavia.nordmodular.MidiController;
import net.sf.nmedit.jpatch.clavia.nordmodular.MidiControllerSet;
import net.sf.nmedit.jpatch.clavia.nordmodular.Morph;
import net.sf.nmedit.jpatch.clavia.nordmodular.MorphSection;
import net.sf.nmedit.jpatch.clavia.nordmodular.NMConnector;
import net.sf.nmedit.jpatch.clavia.nordmodular.NMModule;
import net.sf.nmedit.jpatch.clavia.nordmodular.NMParameter;
import net.sf.nmedit.jpatch.clavia.nordmodular.NMPatch;
import net.sf.nmedit.jpatch.clavia.nordmodular.VoiceArea;
import net.sf.nmedit.jpdl.BitStream;
import net.sf.nmedit.jpdl.IntStream;
import net.sf.nmedit.jpdl.PacketParser;

public class Patch2BitstreamBuilder
{

    private IntStream intStream;
    private BitStream bitStream = null;
    private PacketParser patchParser =  PDLData.getPatchParser();
    private List<Integer> sectionEndPositions = new ArrayList<Integer>();

    private final NMPatch patch;

    public Patch2BitstreamBuilder( NMPatch patch )
    {
        this.patch = patch;
        intStream = new IntStream();
    }

    private void append(int value)
    {
        //System.out.print (value+" ");
        intStream.append(value);
    }
    
    private void append(int ... values)
    {
        for (int i=0;i<values.length;i++)
        {
            //System.out.print (values[i]+" ");
            intStream.append(values[i]);
        }
    }
    
    public List<Integer> getSectionEndPositions()
    {
        return sectionEndPositions;
    }


    public BitStream getBitStream()
    {
        if (bitStream==null)
        {
            bitStream = new BitStream();
            intStream.setPosition(0);
            patchParser.generate(intStream, bitStream);
        }
        return bitStream;
    }
    

    protected void storeEndPosition(IntStream intStream, List<Integer> sectionEndPositions)
    {        
        sep(intStream, patchParser, sectionEndPositions);
    }

    protected static void sep(IntStream is, 
            PacketParser patchParser,
            List<Integer> sep)
    {
        IntStream isCopy = new IntStream();
        while(is.isAvailable(1))
            isCopy.append(is.getInt());
        is.setPosition(0);
        
        BitStream bs = new BitStream();
        /*boolean result = */patchParser.generate(isCopy, bs);
        
        sep.add((bs.getSize()/8)-1);
    }
    
    public void appendName( String s )
    {
        int limit = Math.min(16, s.length());

        for (int i = 0; i < limit; i++) {
            append((int)s.charAt(i));
        }
        if (limit < 16) 
            append(0);
    }
    
    private void beginSection(int ID)
    {
        intStream.append(ID);
    }
    
    private void endSection()
    {
        storeEndPosition(intStream, sectionEndPositions);
    }

    public void generate()
    {
        
      // Create patch bitstream
        

      // Name section
      beginSection(Format.S_NAME_1);
      {
          String name = patch.getName();
          if (name == null) name = "";
          appendName(name);
      }
      endSection();
      
      // Header section
      Header h = patch.getHeader();
      beginSection(Format.S_HEADER);
      append(h.getKeyboardRangeMin());
      append(h.getKeyboardRangeMax());
      append(h.getVelocityRangeMin());
      append(h.getVelocityRangeMax());
      append(h.getBendRange());
      append(h.getPortamentoTime());
      append(h.getPortamento());
      append(1);
      append(h.getRequestedVoices() - 1);
      append(0);
      append(h.getSeparatorPosition());
      append(h.getOctaveShift());
      append(h.getValue(Format.HEADER_CABLE_VISIBILITY_RED));
      append(h.getValue(Format.HEADER_CABLE_VISIBILITY_BLUE));
      append(h.getValue(Format.HEADER_CABLE_VISIBILITY_YELLOW));
      append(h.getValue(Format.HEADER_CABLE_VISIBILITY_GRAY));
      append(h.getValue(Format.HEADER_CABLE_VISIBILITY_GREEN));
      append(h.getValue(Format.HEADER_CABLE_VISIBILITY_PURPLE));
      append(h.getValue(Format.HEADER_CABLE_VISIBILITY_WHITE));
      append(h.getValue(Format.HEADER_VOICE_RETRIGGER_COMMON));
      append(h.getValue(Format.HEADER_VOICE_RETRIGGER_POLY));
      append(0xF);
      append(0);
      endSection();

      // Module section
      moduleSection(patch.getPolyVoiceArea());
      moduleSection(patch.getCommonVoiceArea());

      // Note section
      beginSection(Format.S_NOTE);
      {
          append(64, 0, 0);

          append(0); // size
          append(64, 0, 0);/*
          Iterator<Note> nIter = patch.getNoteSet().iterator();
          Note n = nIter.next();
          // TODO 
          append(n.getNoteNumber(), n.getAttackVelocity(), n.getReleaseVelocity());
          append(patch.getNoteSet().size()-1);
          append(n.getNoteNumber(), n.getAttackVelocity(), n.getReleaseVelocity());
          while(nIter.hasNext())
          {
              n = nIter.next();
              append(n.getNoteNumber(), n.getAttackVelocity(), n.getReleaseVelocity());
          }*/
      }
      endSection();
      
      // Cable section
      cableSection(patch.getPolyVoiceArea());
      cableSection(patch.getCommonVoiceArea());

      // Parameter section
      parameterSection(patch.getPolyVoiceArea());
      parameterSection(patch.getCommonVoiceArea());

      // Morph section
      beginSection(Format.S_MORPHMAP);
      {
          int nknobs = 0;
          
          final MorphSection ms = patch.getMorphSection();
          for (int i=0;i<ms.getMorphCount();i++)
          {
              Morph m = ms.getMorph(i);
              append(m.getValue());
              nknobs+=m.getAssignmentsCount();
          }
          for (int i=0;i<ms.getMorphCount();i++)
          {
              Morph m = ms.getMorph(i);
              append(m.getValue());
          }
          append(nknobs);
          for (int i=0;i<ms.getMorphCount();i++)
          {
              Morph m = ms.getMorph(i);
              for (NMParameter p : m.getAssignments())
              {
                  append(Format.getVoiceAreaID(p.getOwner().getParent().isPolyVoiceArea()));
                  append(p.getOwner().getIndex());
                  append(p.getDescriptor().getIndex());
                  append(m.getDescriptor().getIndex());
                  append(m.getValue()); // ???? m.getRange() ???
              }
          }
      }
      endSection();

      // Knob section
      beginSection(Format.S_KNOBMAP);
      for (int i = 0; i <= 22; i++) 
      {
          boolean found = false;
          
          for (Knob k : patch.getKnobs()) 
          {
              if (k.getID()==i && k.getParameter()!=null) 
              {                
                  found = true;
                  append(1);
                  Parameter a = k.getParameter();
                  if (a instanceof Morph)
                  {
                      append(Format.VALUE_SECTION_MORPH);
                      append(1);
                      append(((Morph)a).getDescriptor().getMorphId());
                  }
                  else
                  {
                      NMParameter p = (NMParameter) a;
                      append(Format.getVoiceAreaID(p.getOwner().getParent().isPolyVoiceArea()));
                      append(p.getOwner().getIndex());
                      append(p.getDescriptor().getIndex());
                  }
                  break ;
              }
          }
          if (!found) 
          {
              append(0);
          }
      }
      endSection();

      // Control section
      beginSection(Format.S_CTRLMAP);
      {
          final MidiControllerSet ms = patch.getMidiControllers();
          MidiController[] msList = ms.getAssignedControllers();
          
          int size = msList.length;
          append(size);
          for (MidiController mc : msList)
          {
              append(mc.getControlId()); // CC
              Parameter a = mc.getParameter();
              if (a instanceof Morph)
              {
                  append(Format.VALUE_SECTION_MORPH);
                  append(1);
                  append(((Morph)a).getDescriptor().getMorphId());
              }
              else
              {
                  NMParameter p = (NMParameter) a;
                  append(Format.getVoiceAreaID(p.getOwner().getParent().isPolyVoiceArea()));
                  append(p.getOwner().getIndex());
                  append(p.getDescriptor().getIndex());
              }
          }
      }
      endSection();

      // Custom section
      customSection(patch.getPolyVoiceArea());
      customSection(patch.getCommonVoiceArea());

      // Module name section
      moduleNameSection(patch.getPolyVoiceArea());
      moduleNameSection(patch.getCommonVoiceArea());
    }
    
    private void moduleSection(VoiceArea va)
    {
        beginSection(Format.S_MODULE);
       append(Format.getVoiceAreaID(va.isPolyVoiceArea()));
       
       append(va.getModuleCount());
       for (Module mm : va)
       {
           NMModule m = (NMModule) mm;
           
           append(m.getID());
           append(m.getIndex());
           append(m.getX());
           append(m.getY());   
       }
       endSection();
    }
    
    private void cableSection(VoiceArea va)
    {
        beginSection(Format.S_CABLE);
        append(Format.getVoiceAreaID(va.isPolyVoiceArea()));
        
        IntStream intStream2 = new IntStream();
        int cablecount = 0;
        
        for (Module mm : va)
        {
            NMModule m = (NMModule) mm;
            for (int j=m.getConnectorCount()-1;j>=0;j--)
            {
                // first connector (dst) triple is always an input
                // the second (src) triple is either in, or output
                
                NMConnector dst = (NMConnector) m.getConnector(j);
                NMConnector src = dst.getSource();
                if (src!=null)
                {
                    // only src can be an output
                    intStream2.append(//Format.CABLE_DUMP_COLOR 
                            src.getConnectionColor().getSignalID());

                    intStream2.append(//Format.CABLE_DUMP_MODULE_INDEX_SOURCE, 
                            src.getModule().getIndex());
                    intStream2.append(//Format.CABLE_DUMP_CONNECTOR_INDEX_SOURCE, 
                            src.getDescriptor().getIndex());
                    intStream2.append(//Format.CABLE_DUMP_CONNECTOR_TYPE_SOURCE, 
                            Format.getOutputID(src.isOutput()));

                    intStream2.append(//Format.CABLE_DUMP_MODULE_INDEX_DESTINATION, 
                            dst.getModule().getIndex());
                    intStream2.append(//Format.CABLE_DUMP_CONNECTOR_INDEX_DESTINATION
                            dst.getDescriptor().getIndex());
                    // always input
                    //intStream2.append(//Format.CABLE_DUMP_CONNECTOR_TYPE_DESTINATION, 
                    //        Format.getOutputID(dst.isOutput()));
                    
                    cablecount++;
                }
            }   
        }
        
        append(cablecount);
        
        // append buffer
        while (intStream2.isAvailable(1))
            append(intStream2.getInt());
      
        endSection();
    }

    private void parameterSection(VoiceArea va)
    {
        beginSection(Format.S_PARAMETER);
        append(Format.getVoiceAreaID(va.isPolyVoiceArea()));
        
        int nmodules = 0;
        
        for (Module mm : va)
        {
            NMModule m = (NMModule) mm;
            if (m.getParameterCount()>0)
                nmodules++;
        }
        
        append(nmodules);
      
        for (Module mm : va)
        {
            NMModule m = (NMModule) mm;
            if (m.getParameterCount()>0)
            {
                append(m.getIndex());
                append(m.getID());
                for (int i=0;i<m.getParameterCount();i++)
                {
                    append(m.getParameter(i).getValue());
                }
            }
        }
        endSection();
    }  

    private void customSection(VoiceArea va)
    {
        beginSection(Format.S_CUSTOM);
        append(Format.getVoiceAreaID(va.isPolyVoiceArea()));
        
        int nmodules = 0;
        
        for (Module mm : va)
        {
            NMModule m = (NMModule) mm;
            if (m.getCustomCount()>0)
                nmodules++;
        }
        
        append(nmodules);
      
        for (Module mm : va)
        {
            NMModule m = (NMModule) mm;
            if (m.getCustomCount()>0)
            {
                append(m.getIndex());
                // not:append(m.getID());
                append(m.getCustomCount());
                for (int i=0;i<m.getCustomCount();i++)
                {
                    append(m.getCustom(i).getValue());
                }
            }
        }
        endSection();
    }

    private void moduleNameSection(VoiceArea va)
    {
        beginSection(Format.S_NAMEDUMP);
        append(Format.getVoiceAreaID(va.isPolyVoiceArea()));
        append(va.getModuleCount());
        for (Module mm : va)
        {
            NMModule m = (NMModule) mm;
            append(m.getIndex());
            appendName(m.getName());
        }
        endSection();
    }

}
