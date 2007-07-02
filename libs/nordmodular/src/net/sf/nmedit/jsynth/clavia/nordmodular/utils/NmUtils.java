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
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

import net.sf.nmedit.jnmprotocol.DeleteCableMessage;
import net.sf.nmedit.jnmprotocol.DeleteModuleMessage;
import net.sf.nmedit.jnmprotocol.GetPatchMessage;
import net.sf.nmedit.jnmprotocol.IAmMessage;
import net.sf.nmedit.jnmprotocol.KnobAssignmentMessage;
import net.sf.nmedit.jnmprotocol.MidiCtrlAssignmentMessage;
import net.sf.nmedit.jnmprotocol.MidiMessage;
import net.sf.nmedit.jnmprotocol.MorphAssignmentMessage;
import net.sf.nmedit.jnmprotocol.MorphRangeChangeMessage;
import net.sf.nmedit.jnmprotocol.MoveModuleMessage;
import net.sf.nmedit.jnmprotocol.NewCableMessage;
import net.sf.nmedit.jnmprotocol.NewModuleMessage;
import net.sf.nmedit.jnmprotocol.ParameterMessage;
import net.sf.nmedit.jnmprotocol.ParameterSelectMessage;
import net.sf.nmedit.jnmprotocol.PatchListEntry;
import net.sf.nmedit.jnmprotocol.PatchListMessage;
import net.sf.nmedit.jnmprotocol.PatchMessage;
import net.sf.nmedit.jnmprotocol.RequestPatchMessage;
import net.sf.nmedit.jnmprotocol.SetModuleTitleMessage;
import net.sf.nmedit.jnmprotocol.SetPatchTitleMessage;
import net.sf.nmedit.jnmprotocol.SlotActivatedMessage;
import net.sf.nmedit.jnmprotocol.SlotsSelectedMessage;
import net.sf.nmedit.jpatch.clavia.nordmodular.Format;
import net.sf.nmedit.jpatch.clavia.nordmodular.NM1ModuleDescriptions;
import net.sf.nmedit.jpatch.PConnector;
import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleContainer;
import net.sf.nmedit.jpatch.PParameter;
import net.sf.nmedit.jpatch.clavia.nordmodular.NMPatch;
import net.sf.nmedit.jpatch.clavia.nordmodular.VoiceArea;
import net.sf.nmedit.jpatch.clavia.nordmodular.parser.ErrorHandler;
import net.sf.nmedit.jpatch.clavia.nordmodular.parser.Helper;
import net.sf.nmedit.jpatch.clavia.nordmodular.parser.PParser;
import net.sf.nmedit.jpatch.clavia.nordmodular.parser.ParseException;
import net.sf.nmedit.jpatch.clavia.nordmodular.parser.PatchBuilder;
import net.sf.nmedit.jpatch.clavia.nordmodular.parser.PatchExporter;
import net.sf.nmedit.jpatch.clavia.nordmodular.parser.PatchFileWriter;
import net.sf.nmedit.jsynth.SynthException;
import net.sf.nmedit.jtheme.util.RelativeClassLoader;

public class NmUtils
{
    
    public static NM1ModuleDescriptions parseModuleDescriptions() 
        throws ParserConfigurationException, SAXException, IOException, URISyntaxException
    {
        NmUtils instance = new NmUtils();

        final String file = "/module-descriptions/modules.xml"; 

        NM1ModuleDescriptions descriptions;
        
        
        URL resource = instance.getClass().getResource(file);
        
        InputStream in = new BufferedInputStream(resource.openStream());
        try
        {
        descriptions = NM1ModuleDescriptions.parse(RelativeClassLoader.fromPath(NmUtils.class.getClassLoader(), 
                resource),in);
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
    /*
    public static MidiMessage createMorphRangeMessage(PParameter parameter, int span, int direction)
    {
        MorphRangeMessage msg = new MorphRangeMessage();
        
        PModule module = parameter.getParentComponent();
        PModuleContainer va = module.getParentComponent();
        
        int section = va.getComponentIndex();
        int moduleIndex = module.getComponentIndex();
        int parameterIndex = parameter.getComponentIndex();
        
        msg.setMessage(section, moduleIndex, parameterIndex, span, direction);
        return msg;
    }
    */
    
    public static MidiMessage createMidiCtrlAssignmentMessage(PParameter parameter, int prevMidiCtrl, int midiCtrl,
            int slot, int pid)
    {
        MidiCtrlAssignmentMessage msg = new MidiCtrlAssignmentMessage();

        PModule module = parameter.getParentComponent();
        PModuleContainer va = module.getParentComponent();
        
        int section = va.getComponentIndex();
        int moduleIndex = module.getComponentIndex();
        int parameterIndex = Helper.index(parameter);
        
        msg.assign(slot, pid, prevMidiCtrl, midiCtrl, section, moduleIndex, parameterIndex);
        return msg;
    }
    
    public static MidiMessage createMorphAssignmentMessage(PParameter parameter, int morph,
            int slot, int pid)
    {
        MorphAssignmentMessage msg = new MorphAssignmentMessage();

        PModule module = parameter.getParentComponent();
        PModuleContainer va = module.getParentComponent();
        
        int section = va.getComponentIndex();
        int moduleIndex = module.getComponentIndex();
        int parameterIndex = Helper.index(parameter);
        
        msg.setMorphAssignment(slot, pid, section, moduleIndex, parameterIndex, morph);
        return msg;
    }
    
    public static MidiMessage createKnobAssignmentMessage(PParameter parameter, int prevKnob, int knob,
            int slot, int pid)
    {
        KnobAssignmentMessage msg = new KnobAssignmentMessage();

        PModule module = parameter.getParentComponent();
        PModuleContainer va = module.getParentComponent();
        
        int section = va.getComponentIndex();
        int moduleIndex = module.getComponentIndex();
        int parameterIndex = Helper.index(parameter);
        
        msg.assign(slot, pid, prevKnob, knob, section, moduleIndex, parameterIndex);
        return msg;
    }
    
    public static MidiMessage createMidiCtrlDeAssignmentMessage(int cc, int slot, int pid)
    {
        MidiCtrlAssignmentMessage msg = new MidiCtrlAssignmentMessage();
        msg.deassign(slot, pid, cc);
        return msg;
    }
    
    public static MidiMessage createKnobDeAssignmentMessage(int knob, int slot, int pid)
    {
        KnobAssignmentMessage msg = new KnobAssignmentMessage();
        msg.deassign(slot, pid, knob);
        return msg;
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
    
    public static MidiMessage createNewModuleMessage(int pid, PModule module) throws Exception
    {
        NewModuleMessage msg = new NewModuleMessage();
        msg.set("pid", pid);

        // get data
        int section = module.getParentComponent().getComponentIndex();
        
        // set data
        msg.newModule 
        (
            Helper.index(module), // module id
            section, 
            module.getComponentIndex(),
            module.getInternalX(), 
            module.getInternalY(),
            "",// module.getName(),
            Helper.paramValues(module, "parameter"),
            Helper.paramValues(module, "custom")
        );
        
        return msg;
    }
    
    public static MidiMessage createDeleteModuleMessage( int pid, PModule module, int moduleIndex ) throws Exception
    {    
        return createDeleteModuleMessage(pid, module.getParentComponent().getComponentIndex(), moduleIndex);
    }
    
    public static MidiMessage createDeleteModuleMessage( int pid, int polyVoiceArea, int moduleIndex ) throws Exception
    {
        DeleteModuleMessage msg = new DeleteModuleMessage();
        // get data
        int section = polyVoiceArea;
        // set data
        msg.deleteModule( section, moduleIndex );
        return msg;
    }

    public static MidiMessage createDeleteCableMessage( VoiceArea va, PConnector a, PConnector b, int slotId, int pId ) throws Exception
    {
        // get message instance
        DeleteCableMessage msg = new DeleteCableMessage();
        msg.set("slot", slotId);
        msg.set("pid", pId);

        // get data
        PConnector src = a;
        PConnector dst = b;

        if (dst.isOutput())
        {
            src = b; // swap
            dst = a;
        }
        
        // set data
        msg.deleteCable
        (
            getVoiceAreaId(va),
                
            dst.getParentComponent().getComponentIndex(), 
            Format.getOutputID(dst.isOutput()),
            Helper.index(dst),
            
            src.getParentComponent().getComponentIndex(), 
            Format.getOutputID(src.isOutput()),
            Helper.index(src)
        );
        
        return msg;
    }

    public static MidiMessage createNewCableMessage( VoiceArea va, PConnector a, PConnector b, int slotId, int pId ) throws Exception
    {
        // get message instance
        NewCableMessage msg = new NewCableMessage();
        msg.set("slot", slotId);
        msg.set("pid", pId);

        // get data
        PConnector src = a;
        PConnector dst = b;
        
        if (dst.isOutput())
        {
            src = b; // swap
            dst = a;
        }
        
        int color = src.getSignalType().getId();
        
        // set data
        msg.newCable
        (
            getVoiceAreaId(va),
            color, 
            
            dst.getParentComponent().getComponentIndex(), 
            Format.getOutputID(dst.isOutput()),
            Helper.index(dst),
            
            src.getParentComponent().getComponentIndex(), 
            Format.getOutputID(src.isOutput()),
            Helper.index(src)
        );
        
        return msg;
    }

    public static MidiMessage createMoveModuleMessage( PModule module, int slotId, int pId ) throws Exception
    {
        // get message instance
        MoveModuleMessage msg = new MoveModuleMessage();
        msg.set("slot", slotId);
        msg.set("pid", pId);
        
        // set data
        msg.moveModule
        (
            getVoiceAreaId(module),
            module.getComponentIndex(), 
            module.getInternalX(), 
            module.getInternalY()
        );

        return msg;
    }
    
    
    public static MidiMessage createSelectParameterMessage( PParameter parameter, int slotId, int pId ) throws Exception
    {
        // get message instance
        ParameterSelectMessage msg = new ParameterSelectMessage();

        // get data
        PModule module = parameter.getParentComponent();
        
        // set data
        msg.select(slotId, pId, 
                getVoiceAreaId(module),
                module.getComponentIndex(), Helper.index(parameter));

        return msg;
    }
    
    public static MidiMessage createParameterChangedMessage( PParameter parameter, int slotId, int pId ) throws Exception
    {
        // get message instance
        ParameterMessage msg = new ParameterMessage();
        msg.set("slot", slotId);
        msg.set("pid", pId);
        
        // get data
        PModule module = parameter.getParentComponent();
        
        // set data
        msg.set("module", module.getComponentIndex());
        msg.set("section", getVoiceAreaId(module));
        msg.set("parameter", Helper.index(parameter));
        msg.set("value", parameter.getValue());

        return msg;
    }
    
    public static MidiMessage createMorphRangeChangeMessage( PParameter parameter, int slotId, int pId ) throws Exception
    {
        // get message instance
        MorphRangeChangeMessage msg = new MorphRangeChangeMessage();
        msg.set("slot", slotId);
        msg.set("pid", pId);
        
        // get data
        PModule module = parameter.getParentComponent();
        
        // set data
        msg.set("module", module.getComponentIndex());
        msg.set("section", getVoiceAreaId(module));
        msg.set("parameter", Helper.index(parameter));
        msg.set("span", Math.abs(parameter.getValue()));
        msg.set("direction", parameter.getValue()<0?0:1);

        return msg;
    }

    public static MidiMessage createSetModuleTitleMessage(PModule module, String title, int slot, int pid)
    {
        SetModuleTitleMessage msg = new SetModuleTitleMessage();
        msg.setTitle(slot, pid, getVoiceAreaId(module), module.getComponentIndex(), title);
        return msg;
    }

    public static MidiMessage createSetPatchTitleMessage(String title, int slot, int pid)
    {
        SetPatchTitleMessage msg = new SetPatchTitleMessage();
        msg.setTitle(slot, pid, title);
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
    
    public static int getVoiceAreaId(PModule module)
    {
        return module.getParentComponent().getComponentIndex();
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

    public static MidiMessage createPatchSettingsMessage(NMPatch patch, int slotId) throws Exception
    {
        Patch2BitstreamBuilder builder = new Patch2BitstreamBuilder(patch);
        builder.setHeaderOnly(true);
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

    public static boolean writePatchSavely(NMPatch patch, File file)
    {
        try
        {
            writePatch(patch, file);
            return true;
        }
        catch (Exception e)
        {
            Log log = LogFactory.getLog(NmUtils.class);
            if (log.isErrorEnabled())
                log.error("could not write patch "+patch+" to file "+file, e);
            return false;
        }
    }
    
    public static void writePatch(NMPatch patch, File file)
        throws IOException, ParseException
    {
        OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
        try
        {
            PatchExporter export = new PatchExporter();
            PatchFileWriter writer = new PatchFileWriter(out);
            export.export(patch, writer);
        }
        finally
        {
            out.flush();
            out.close();
        }
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
