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
 * Created on Jan 9, 2007
 */
package net.sf.nmedit.jsynth.clavia.nordmodular.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import net.sf.nmedit.jnmprotocol.DeleteCableMessage;
import net.sf.nmedit.jnmprotocol.DeleteModuleMessage;
import net.sf.nmedit.jnmprotocol.GetPatchMessage;
import net.sf.nmedit.jnmprotocol.IAmMessage;
import net.sf.nmedit.jnmprotocol.MidiMessage;
import net.sf.nmedit.jnmprotocol.MoveModuleMessage;
import net.sf.nmedit.jnmprotocol.NewCableMessage;
import net.sf.nmedit.jnmprotocol.NewModuleMessage;
import net.sf.nmedit.jnmprotocol.ParameterMessage;
import net.sf.nmedit.jnmprotocol.PatchListEntry;
import net.sf.nmedit.jnmprotocol.PatchListMessage;
import net.sf.nmedit.jnmprotocol.PatchMessage;
import net.sf.nmedit.jnmprotocol.RequestPatchMessage;
import net.sf.nmedit.jnmprotocol.SlotActivatedMessage;
import net.sf.nmedit.jnmprotocol.SlotsSelectedMessage;
import net.sf.nmedit.jpatch.clavia.nordmodular.Format;
import net.sf.nmedit.jpatch.clavia.nordmodular.NM1ModuleDescriptions;
import net.sf.nmedit.jpatch.clavia.nordmodular.NMConnector;
import net.sf.nmedit.jpatch.clavia.nordmodular.NMModule;
import net.sf.nmedit.jpatch.clavia.nordmodular.NMParameter;
import net.sf.nmedit.jpatch.clavia.nordmodular.NMPatch;
import net.sf.nmedit.jpatch.clavia.nordmodular.VoiceArea;
import net.sf.nmedit.jpatch.clavia.nordmodular.parser.ErrorHandler;
import net.sf.nmedit.jpatch.clavia.nordmodular.parser.PParser;
import net.sf.nmedit.jpatch.clavia.nordmodular.parser.ParseException;
import net.sf.nmedit.jpatch.clavia.nordmodular.parser.PatchBuilder;
import net.sf.nmedit.jsynth.SynthException;

public class NmUtils
{
    
    public static NM1ModuleDescriptions parseModuleDescriptions() 
        throws ParserConfigurationException, SAXException, IOException
    {
        NmUtils instance = new NmUtils();

        final String file = "/module-descriptions/modules.xml"; 

        NM1ModuleDescriptions descriptions;
        
        InputStream in = new BufferedInputStream(instance.getClass().getResourceAsStream(file));
        try
        {
        descriptions = NM1ModuleDescriptions.parse(in);
        }
        finally
        {
            in.close();
        }
        
        return descriptions;
    }

    public static SynthException transformException(Throwable e)
    {
        if (e instanceof SynthException)
            return (SynthException) e;
        SynthException se = new SynthException();
        se.initCause(e);
        return se;
    }
    
    public static String getPatchBankLocation(int section, int position)
    {        
        return Integer.toString(((section+1)*100)+(position+1));
    }
    public static SlotActivatedMessage 
        createSlotsActivatedMessage(int slot)
    {
        SlotActivatedMessage message = new SlotActivatedMessage();
        message.set("activeSlot", slot);
        return message;
    }
    
    public static SlotsSelectedMessage createSlotsSelectedMessage(
            boolean slot0, boolean slot1, boolean slot2, boolean slot3)
    {
        SlotsSelectedMessage message = new SlotsSelectedMessage();
        message.set("slot0Selected", slot0?1:0);
        message.set("slot1Selected", slot1?1:0);
        message.set("slot2Selected", slot2?1:0);
        message.set("slot3Selected", slot3?1:0);
        return message;
    }
    
    public static MidiMessage createRequestPatchMessage(int slotId) 
    {
        RequestPatchMessage msg = new RequestPatchMessage();
        msg.set("slot", slotId);    
        return msg;
    }

    public static MidiMessage createGetPatchMessage(int slot, int pid) throws Exception
    {
        GetPatchMessage msg = new GetPatchMessage();
        msg.set("slot", slot);
        msg.set("pid", pid);
        return msg;
    }
    
    public static MidiMessage createNewModuleMessage(int pid, NMModule module) throws Exception
    {
        NewModuleMessage msg = new NewModuleMessage();
        msg.set("pid", pid);

        // get data
        int section = Format.getVoiceAreaID(module.getParent().isPolyVoiceArea());
        
        // set data
        msg.newModule 
        (
            module.getID(),
            section, 
            module.getIndex(),
            module.getX(), 
            module.getY(),
            "",// module.getName(),
            module.getParameterValues(),
            module.getCustomValues()
        );
        
        return msg;
    }
    
    public static MidiMessage createDeleteModuleMessage( int pid, NMModule module ) throws Exception
    {    
        return createDeleteModuleMessage(pid, module.getParent().isPolyVoiceArea(), module.getIndex());
    }
    
    public static MidiMessage createDeleteModuleMessage( int pid, boolean polyVoiceArea, int moduleIndex ) throws Exception
    {
        DeleteModuleMessage msg = new DeleteModuleMessage();
        // get data
        int section = Format.getVoiceAreaID(polyVoiceArea);
        // set data
        msg.deleteModule( section, moduleIndex );
        return msg;
    }

    public static MidiMessage createDeleteCableMessage( VoiceArea va, NMConnector a, NMConnector b, int slotId, int pId ) throws Exception
    {
        // get message instance
        DeleteCableMessage msg = new DeleteCableMessage();
        msg.set("slot", slotId);
        msg.set("pid", pId);

        // get data
        NMConnector src = a;
        NMConnector dst = b;

        if (dst.isOutput())
        {
            src = b; // swap
            dst = a;
        }
        
        // set data
        msg.deleteCable
        (
            getVoiceAreaId(va),
                
            dst.getModule().getIndex(), 
            Format.getOutputID(dst.isOutput()),
            dst.getDescriptor().getIndex(),
            
            src.getModule().getIndex(), 
            Format.getOutputID(src.isOutput()),
            src.getDescriptor().getIndex()
        );
        
        return msg;
    }

    public static MidiMessage createNewCableMessage( VoiceArea va, NMConnector a, NMConnector b, int slotId, int pId ) throws Exception
    {
        // get message instance
        NewCableMessage msg = new NewCableMessage();
        msg.set("slot", slotId);
        msg.set("pid", pId);

        // get data
        NMConnector src = a;
        NMConnector dst = b;
        
        if (dst.isOutput())
        {
            src = b; // swap
            dst = a;
        }
        
        int color = src.getConnectionColor().getSignalID();
        
        // set data
        msg.newCable
        (
            getVoiceAreaId(va),
            color, 
            
            dst.getModule().getIndex(), 
            Format.getOutputID(dst.isOutput()),
            dst.getDescriptor().getIndex(),
            
            src.getModule().getIndex(), 
            Format.getOutputID(src.isOutput()),
            src.getDescriptor().getIndex()
        );
        
        return msg;
    }

    public static MidiMessage createMoveModuleMessage( NMModule module, int slotId, int pId ) throws Exception
    {
        // get message instance
        MoveModuleMessage msg = new MoveModuleMessage();
        msg.set("slot", slotId);
        msg.set("pid", pId);
        
        // set data
        msg.moveModule
        (
            getVoiceAreaId(module),
            module.getIndex(), 
            module.getX(), 
            module.getY()
        );

        return msg;
    }
    
    public static MidiMessage createParameterChangedMessage( NMParameter parameter, int slotId, int pId ) throws Exception
    {
        // get message instance
        ParameterMessage msg = new ParameterMessage();
        msg.set("slot", slotId);
        msg.set("pid", pId);
        
        // get data
        NMModule module = parameter.getOwner();
        
        // set data
        msg.set("module", module.getIndex());
        msg.set("section", getVoiceAreaId(module));
        msg.set("parameter", parameter.getDescriptor().getIndex());
        msg.set("value", parameter.getValue());

        return msg;
    }

    public static MidiMessage createIAmMessage() throws Exception
    {
        return new IAmMessage();
    }
    
    public static int getVoiceAreaId(VoiceArea voiceArea)
    {
        return Format.getVoiceAreaID(voiceArea.isPolyVoiceArea());
    }
    
    public static int getVoiceAreaId(NMModule module)
    {
        return getVoiceAreaId(module.getParent());
    }


    public static String[] getPatchNames(PatchListMessage msg)
    {
        Collection<PatchListEntry> entries = msg.getEntries();
        String[] names = new String[entries.size()];
        int index = 0;
        for (PatchListEntry e : entries)
            names[index++] = e.getName();
        return names;
    }
    
    public static MidiMessage createPatchMessage(NMPatch patch, int slotId) throws Exception
    {
        Patch2BitstreamBuilder builder = new Patch2BitstreamBuilder(patch);
        builder.generate();
        return new PatchMessage(builder.getBitStream(), builder.getSectionEndPositions(), slotId);
    }
    
    public static NMPatch parsePatch(NM1ModuleDescriptions modules, InputStream source) 
        throws ParseException
    {
        PParser parser = new PParser(new ParserErrorHandler());
        parser.setSource(source);
        PatchBuilder builder = new PatchBuilder(parser, modules);
        parser.setContentHandler(builder);
        parser.parse();
        
        NMPatch patch = builder.getPatch();
        patch.getHistory().setEnabled(true);
        return patch;
    }

    public static PatchBuilder parsePatchMessage(PatchMessage message, NM1ModuleDescriptions modules) throws ParseException
    {
        PatchBuilder patchBuilder = new PatchBuilder(new ParserErrorHandler(), modules);
        patchBuilder.beginDocument();
        parsePatchMessage(message, patchBuilder);
        return patchBuilder;
    }
    
    public static void parsePatchMessage(PatchMessage message, PatchBuilder patchBuilder) throws ParseException
    {
        BitstreamPatchParser bsParser = new BitstreamPatchParser();
        bsParser.transcode(message.getPatchStream(), patchBuilder);
    }

    /*
    private String int2str(int[] array)
    {
        StringBuffer sb = new StringBuffer("{");
        for (int i=0;i<array.length;i++)
        {
            sb.append(Integer.toString(array[i]));
            if (i<array.length-1)
                sb.append(',');
        }
        sb.append('}');
        return sb.toString();
    }*/
    
    private static class ParserErrorHandler implements ErrorHandler
    {

        public void warning( ParseException e ) throws ParseException
        {
            // ignore
        }

        public void error( ParseException e ) throws ParseException
        {
            throw e;
        }

        public void fatal( ParseException e ) throws ParseException
        {
            throw e;
        }
        
    }

    public static String getPatchNameFromfileName(File file)
    {
        return getPatchNameFromfileName(file.getName());
    }

    public static String getPatchNameFromfileName(String fileName)
    {
        String s = fileName;
        
        int pos = s.lastIndexOf(File.separatorChar);
        if (pos >= 0)
            s = s.substring(pos+1);
        
        pos = s.lastIndexOf('.');
        if (pos>=0 && s.toLowerCase().endsWith(".pch"))
            s = s.substring(0, pos);

        return s;
    }

}
