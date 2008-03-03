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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collection;

import javax.xml.parsers.ParserConfigurationException;

import net.sf.nmedit.jnmprotocol2.DeleteCableMessage;
import net.sf.nmedit.jnmprotocol2.DeleteModuleMessage;
import net.sf.nmedit.jnmprotocol2.KnobAssignmentMessage;
import net.sf.nmedit.jnmprotocol2.MidiCtrlAssignmentMessage;
import net.sf.nmedit.jnmprotocol2.MidiException;
import net.sf.nmedit.jnmprotocol2.MidiMessage;
import net.sf.nmedit.jnmprotocol2.MorphAssignmentMessage;
import net.sf.nmedit.jnmprotocol2.MorphRangeChangeMessage;
import net.sf.nmedit.jnmprotocol2.MoveModuleMessage;
import net.sf.nmedit.jnmprotocol2.NewCableMessage;
import net.sf.nmedit.jnmprotocol2.NewModuleMessage;
import net.sf.nmedit.jnmprotocol2.ParameterMessage;
import net.sf.nmedit.jnmprotocol2.ParameterSelectMessage;
import net.sf.nmedit.jnmprotocol2.PatchListEntry;
import net.sf.nmedit.jnmprotocol2.PatchListMessage;
import net.sf.nmedit.jnmprotocol2.PatchMessage;
import net.sf.nmedit.jnmprotocol2.SetModuleTitleMessage;
import net.sf.nmedit.jpatch.PConnector;
import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleContainer;
import net.sf.nmedit.jpatch.PParameter;
import net.sf.nmedit.jpatch.clavia.nordmodular.Format;
import net.sf.nmedit.jpatch.clavia.nordmodular.NM1ModuleDescriptions;
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

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
    /*
    public static String getPatchBankLocation(int section, int position)
    {        
        return Integer.toString(((section+1)*100)+(position+1));
    }*/
    
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
    
    public static MidiMessage createNewModuleMessage(int pid, PModule module, int slotId) throws MidiException
    {
        NewModuleMessage msg = new NewModuleMessage();
        msg.setPid(pid);
        msg.setSlot(slotId); 
        // get data
        int section = module.getParentComponent().getComponentIndex();
        
        String name = module.getName();
        if (name == null)
            name = "";
        
        // set data
        msg.newModule 
        (
            Helper.index(module), // module id
            section, 
            module.getComponentIndex(),
            module.getInternalX(), 
            module.getInternalY(),
            name,
            Helper.paramValues(module, "parameter"),
            Helper.paramValues(module, "custom")
        );

       /* System.out.println(msg);

        System.out.println(Helper.index(module));
        System.out.println(section);
        System.out.println(module.getComponentIndex());
        System.out.println(module.getInternalX());
        System.out.println(module.getInternalY());
        System.out.println(name);
        */
        return msg;
    }

    public static MidiMessage createDeleteModuleMessage( int pid, PModule module, int moduleIndex )
    {    
        return createDeleteModuleMessage(pid, module.getParentComponent().getComponentIndex(), moduleIndex);
    }
    
    public static MidiMessage createDeleteModuleMessage( int pid, int polyVoiceArea, int moduleIndex ) 
    {
        DeleteModuleMessage msg = new DeleteModuleMessage();
        // get data
        int section = polyVoiceArea;
        // set data
        msg.deleteModule( section, moduleIndex );
        return msg;
    }

    public static MidiMessage createDeleteCableMessage( VoiceArea va, PConnector a, PConnector b, int slotId, int pId )
    {
        // get message instance
        DeleteCableMessage msg = new DeleteCableMessage();
        msg.setSlot(slotId);
        msg.setPid(pId);
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

    public static MidiMessage createNewCableMessage( VoiceArea va, PConnector a, PConnector b, int slotId, int pId ) 
    {
        // get message instance
        NewCableMessage msg = new NewCableMessage();
        msg.setSlot(slotId);
        msg.setPid(pId);

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

    public static MidiMessage createMoveModuleMessage( PModule module, int slotId, int pId ) 
    {
        // get message instance
        MoveModuleMessage msg = new MoveModuleMessage();
        msg.setSlot(slotId);
        msg.setPid(pId);
        
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
    
    
    public static MidiMessage createSelectParameterMessage( PParameter parameter, int slotId, int pId )
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
    
    public static MidiMessage createParameterChangedMessage( PParameter parameter, int slotId, int pId ) 
    {
        // get message instance
        ParameterMessage msg = new ParameterMessage();
        msg.setSlot(slotId);
        msg.setPid(pId);
        
        // get data
        PModule module = parameter.getParentComponent();
        
        // set data
        msg.parameterChanged(
                getVoiceAreaId(module),
                module.getComponentIndex(),
                Helper.index(parameter),
                parameter.getValue());

        return msg;
    }
    
    public static MidiMessage createMorphRangeChangeMessage( PParameter parameter, int slotId, int pId )
    {
        // get message instance
        MorphRangeChangeMessage msg = new MorphRangeChangeMessage();
        
        // get data
        PModule module = parameter.getParentComponent();
        
        // set data
        msg.setMorphRange(slotId, pId,
                getVoiceAreaId(module),
                module.getComponentIndex(),
                Helper.index(parameter),
                Math.abs(parameter.getValue()),
                parameter.getValue()<0?0:1);

        return msg;
    }

    public static MidiMessage createSetModuleTitleMessage(PModule module, String title, int slot, int pid)
    {
        SetModuleTitleMessage msg = new SetModuleTitleMessage();
        msg.setTitle(slot, pid, getVoiceAreaId(module), module.getComponentIndex(), title);
        return msg;
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

    public static PatchMessage[] createPatchMessages(NMPatch patch, int slotId) throws MidiException 
    {
        Patch2BitstreamBuilder builder = new Patch2BitstreamBuilder(patch);
        return builder.createMessages(slotId);
    }

    public static Charset getPatchFileCharset()
    {
        return Charset.forName("ISO-8859-1");
    }
    
    public static PatchMessage[] createPatchSettingsMessages(NMPatch patch, int slotId) throws MidiException
    {
        Patch2BitstreamBuilder builder = new Patch2BitstreamBuilder(patch);
        builder.setHeaderOnly(true);
        // TODO PatchMessage not working yet, sectionsEnded must be != 1
        return builder.createMessages(slotId);
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
        // todo enable history
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
        FileOutputStream out = new FileOutputStream(file);
        try
        {
            writePatch(patch, out);
        }
        finally
        {
            out.flush();
            out.close();
        }
    }
    
    public static void writePatch(NMPatch patch, OutputStream out)
    throws IOException, ParseException
    {
        Writer writer =
            new BufferedWriter(new OutputStreamWriter(out, getPatchFileCharset()));
        try
        {
            (new PatchExporter()).export(patch, new PatchFileWriter(writer));
        }
        finally
        {
            writer.flush();
            writer.close();
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
    
    public static class ParserErrorHandler implements ErrorHandler
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
