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
import java.util.Map;

import net.sf.nmedit.jnmprotocol.PDLData;
import net.sf.nmedit.jpatch.PConnector;
import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.clavia.nordmodular.Format;
import net.sf.nmedit.jpatch.clavia.nordmodular.Header;
import net.sf.nmedit.jpatch.clavia.nordmodular.Knob;
import net.sf.nmedit.jpatch.clavia.nordmodular.MidiController;
import net.sf.nmedit.jpatch.clavia.nordmodular.MidiControllerSet;
import net.sf.nmedit.jpatch.clavia.nordmodular.NMPatch;
import net.sf.nmedit.jpatch.clavia.nordmodular.PNMMorphSection;
import net.sf.nmedit.jpatch.clavia.nordmodular.VoiceArea;
import net.sf.nmedit.jpatch.clavia.nordmodular.parser.Helper;
import net.sf.nmedit.jpatch.PParameter;
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
    
    private boolean headerOnly = false;
    
    public void setHeaderOnly(boolean enable)
    {
        this.headerOnly = enable;
    }

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
     
      if (headerOnly)
      {
          generateHeader();
          
          return;
      }
        

      // Name section
      beginSection(Format.S_NAME_1);
      {
          String name = patch.getName();
          if (name == null) name = "";
          appendName(name);
      }
      endSection();
      
      // Header section
      generateHeader();

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
          
          final PNMMorphSection ms = patch.getMorphSection();
          for (int i=0;i<ms.getMorphCount();i++)
          {
              PParameter m = ms.getMorph(i);
              append(m.getValue());
              nknobs+=ms.getAssignments(i).size();
          }
          for (int i=0;i<ms.getMorphCount();i++)
          {
              PParameter m = ms.getMorph(i);
              append(m.getValue());
          }
          append(nknobs);
          for (int i=0;i<ms.getMorphCount();i++)
          {
              PParameter m = ms.getMorph(i);
              for (PParameter p : ms.getAssignments(i))
              {
                  append(p.getParentComponent().getParentComponent().getComponentIndex());
                  append(p.getParentComponent().getComponentIndex());
                  append(Helper.index(p));
                  append(Helper.index(m));
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
                  PParameter a = k.getParameter();
                  int moduleIndex
                      = patch.getMorphSection().isMorph(a) 
                      ? 1 : a.getParentComponent().getComponentIndex();
    
                  append(a.getParentComponent().getParentComponent().getComponentIndex());
                  append(moduleIndex);
                  append(Helper.index(a));
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
              PParameter a = mc.getParameter();
              
              int moduleIndex
                  = patch.getMorphSection().isMorph(a) 
                  ? 1 : a.getParentComponent().getComponentIndex();

              append(a.getParentComponent().getParentComponent().getComponentIndex());
              append(moduleIndex);
              append(Helper.index(a));
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

    protected void generateHeader()
    {
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
    }
    
    private void moduleSection(VoiceArea va)
    {
        beginSection(Format.S_MODULE);
       append(Format.getVoiceAreaID(va.isPolyVoiceArea()));
       
       append(va.getModuleCount());
       for (PModule m : va)
       {
           append(Helper.index(m));
           append(m.getComponentIndex());
           append(m.getInternalX());
           append(m.getInternalY());   
       }
       endSection();
    }
    
    private void cableSection(VoiceArea va)
    {
        beginSection(Format.S_CABLE);
        append(Format.getVoiceAreaID(va.isPolyVoiceArea()));
        
        IntStream intStream2 = new IntStream();
        int cablecount = 0;
        
        for (PModule m : va)
        {
            for (int j=m.getConnectorCount()-1;j>=0;j--)
            {
                // first connector (dst) triple is always an input
                // the second (src) triple is either in, or output
                
                PConnector dst = m.getConnector(j);
                PConnector src = dst.getParentConnector();
                if (src!=null)
                {
                    // only src can be an output
                    intStream2.append(//Format.CABLE_DUMP_COLOR 
                            src.getSignalType().getId());

                    intStream2.append(//Format.CABLE_DUMP_MODULE_INDEX_SOURCE, 
                            src.getParentComponent().getComponentIndex());
                    intStream2.append(//Format.CABLE_DUMP_CONNECTOR_INDEX_SOURCE, 
                            Helper.index(src));
                    intStream2.append(//Format.CABLE_DUMP_CONNECTOR_TYPE_SOURCE, 
                            Format.getOutputID(src.isOutput()));

                    intStream2.append(//Format.CABLE_DUMP_MODULE_INDEX_DESTINATION, 
                            dst.getParentComponent().getComponentIndex());
                    intStream2.append(//Format.CABLE_DUMP_CONNECTOR_INDEX_DESTINATION
                            Helper.index(dst));
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
        
        for (PModule m : va)
        {
            if (Helper.getParameterClassCount(m, "parameter")>0)
                nmodules++;
        }
        
        append(nmodules);
      
        for (PModule m : va)
        {
            Map map = Helper.getParameterClassMap(m, "parameter");
            int size = map.size();
            if (size>0)
            {
                append(m.getComponentIndex());
                append(Helper.index(m));
                for (int i=0;i<size;i++)
                {
                    append(((PParameter)map.get(i)).getValue());
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
        
        for (PModule m : va)
        {
            if (Helper.getParameterClassCount(m, "custom")>0)
                nmodules++;
        }
        
        append(nmodules);

        for (PModule m : va)
        {
            Map map = Helper.getParameterClassMap(m, "custom");
            int size = map.size();
            if (size>0)
            {
                append(m.getComponentIndex());
                // not:append(module id);
                append(size);
                for (int i=0;i<size;i++)
                {
                    append(((PParameter)map.get(i)).getValue());
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
        for (PModule m : va)
        {
            append(m.getComponentIndex());
            String t = m.getTitle();
            appendName(t == null ? "" : t);
        }
        endSection();
    }

}
