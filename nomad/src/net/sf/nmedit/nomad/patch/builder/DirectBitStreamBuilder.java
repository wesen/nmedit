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
package net.sf.nmedit.nomad.patch.builder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.nmedit.jnmprotocol.PatchMessage;
import net.sf.nmedit.jpdl.BitStream;
import net.sf.nmedit.jpdl.IntStream;
import net.sf.nmedit.jpdl.PacketParser;
import net.sf.nmedit.nomad.patch.Format;
import net.sf.nmedit.nomad.patch.transcoder.BitstreamTranscoder;
import net.sf.nmedit.nomad.patch.virtual.Connector;
import net.sf.nmedit.nomad.patch.virtual.Header;
import net.sf.nmedit.nomad.patch.virtual.Knob;
import net.sf.nmedit.nomad.patch.virtual.MidiController;
import net.sf.nmedit.nomad.patch.virtual.Module;
import net.sf.nmedit.nomad.patch.virtual.Morph;
import net.sf.nmedit.nomad.patch.virtual.Parameter;
import net.sf.nmedit.nomad.patch.virtual.Patch;
import net.sf.nmedit.nomad.patch.virtual.VoiceArea;
import net.sf.nmedit.nomad.patch.virtual.misc.Assignment;

public class DirectBitStreamBuilder
{

    private IntStream intStream;
    private BitStream bitStream = null;
    private PacketParser patchParser =  BitstreamTranscoder.getPatchParser();
    private List<Integer> sectionEndPositions = new ArrayList<Integer>();

    private final Patch patch;

    public DirectBitStreamBuilder( Patch patch )
    {
        this.patch = patch;
        intStream = new IntStream();
    }

    private void append(int value)
    {
        System.out.print (value+" ");
        intStream.append(value);
    }
    
    private void append(int ... values)
    {
        for (int i=0;i<values.length;i++)
        {
            System.out.print (values[i]+" ");
            intStream.append(values[i]);
        }
    }
    
    public List<Integer> getSectionEndPositions()
    {
        return sectionEndPositions;
    }


    private BitStream getBitStream()
    {
        if (bitStream==null)
        {
            bitStream = new BitStream();
            intStream.setPosition(0);
            patchParser.generate(intStream, bitStream);
        }
        return bitStream;
    }
    
    public PatchMessage generateMessage( int slotID ) throws Exception
    {
        System.out.print("<section-end-positions>");
        for (int i:sectionEndPositions)
            System.out.print( i+" ");
        System.out.println("</section-end-positions>");
        
        return new PatchMessage(getBitStream(), getSectionEndPositions(), slotID);
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
        boolean result = patchParser.generate(isCopy, bs);
        
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
        System.out.print("<section ID=\""+ID+"\" name=\""+Format.getSectionName2(ID)+"\">");
        intStream.append(ID);
    }
    
    private void endSection()
    {
        System.out.println("</section>");
        storeEndPosition(intStream, sectionEndPositions);
    }

    public void generate()
    {
        
      // Create patch bitstream
        

      // Name section
      beginSection(Format.S_NAME_1 /*55*/);
      appendName(patch.getName());
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
      append(h.getRequestedVoices() - 1 /*-1 ??? */);
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
          for (Morph m : patch.getMorphs()) // are ordered morph 1-4
          {
              append(m.getValue());
              nknobs+=m.size();
          }
          for (Morph m : patch.getMorphs())
          {
              append(m.getKeyboardAssignment().getID());
          }
          append(nknobs);
          for (Morph m : patch.getMorphs())
          {
              for (Parameter p : m)
              {
                  append(Format.getVoiceAreaID(p.getModule().getVoiceArea().isPolyVoiceArea()));
                  append(p.getModule().getIndex());
                  append(p.getID());
                  append(m.getID());
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
              if (k.getID()==i && k.getAssignment()!=null) 
              {                
                  found = true;
                  append(1);
                  Assignment a = k.getAssignment();
                  if (a instanceof Morph)
                  {
                      append(Format.VALUE_SECTION_MORPH);
                      append(1);
                      append(((Morph)a).getID());
                  }
                  else
                  {
                      Parameter p = (Parameter) a;
                      append(Format.getVoiceAreaID(p.getModule().getVoiceArea().isPolyVoiceArea()));
                      append(p.getModule().getIndex());
                      append(p.getID());
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
          int size = 0;
          for (Iterator i=patch.getMidiControllers().getAssignedMidiControllers();i.hasNext();)
          {
              size++;
              i.next();
          }
          append(size);
          for (Iterator<MidiController> i=patch.getMidiControllers().getAssignedMidiControllers();i.hasNext();)
          {
              MidiController mc = i.next();

              append(mc.getID()); // CC
              Assignment a = mc.getAssignment();
              if (a instanceof Morph)
              {
                  append(Format.VALUE_SECTION_MORPH);
                  append(1);
                  append(((Morph)a).getID());
              }
              else
              {
                  Parameter p = (Parameter) a;
                  append(Format.getVoiceAreaID(p.getModule().getVoiceArea().isPolyVoiceArea()));
                  append(p.getModule().getIndex());
                  append(p.getID());
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
       
       append(va.size());
       for (Module m : va)
       {
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
                    intStream2.append(//Format.CABLE_DUMP_COLOR 
                            src.getConnectionColor().getSignalID());

                    intStream2.append(//Format.CABLE_DUMP_MODULE_INDEX_SOURCE, 
                            src.getModule().getIndex());
                    intStream2.append(//Format.CABLE_DUMP_CONNECTOR_INDEX_SOURCE, 
                            src.getDefinition().getId());
                    intStream2.append(//Format.CABLE_DUMP_CONNECTOR_TYPE_SOURCE, 
                            Format.getOutputID(src.isOutput()));

                    intStream2.append(//Format.CABLE_DUMP_MODULE_INDEX_DESTINATION, 
                            dst.getModule().getIndex());
                    intStream2.append(//Format.CABLE_DUMP_CONNECTOR_INDEX_DESTINATION
                            dst.getDefinition().getId());
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
        
        for (Module m : va)
        {
            if (m.getParameterCount()>0)
                nmodules++;
        }
        
        append(nmodules);
      
        for (Module m : va)
        {
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
        
        for (Module m : va)
        {
            if (m.getCustomCount()>0)
                nmodules++;
        }
        
        append(nmodules);
      
        for (Module m : va)
        {
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
        append(va.size());
        for (Module m : va)
        {
            append(m.getIndex());
            appendName(m.getName());
        }
        endSection();
    }

}
