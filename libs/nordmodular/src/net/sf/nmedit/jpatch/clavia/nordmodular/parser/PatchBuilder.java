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
 * Created on Dec 20, 2006
 */
package net.sf.nmedit.jpatch.clavia.nordmodular.parser;

import java.util.List;
import java.util.NoSuchElementException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.nmedit.jpatch.InvalidDescriptorException;
import net.sf.nmedit.jpatch.ModuleDescriptions;
import net.sf.nmedit.jpatch.PConnector;
import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PParameter;
import net.sf.nmedit.jpatch.clavia.nordmodular.Format;
import net.sf.nmedit.jpatch.clavia.nordmodular.Header;
import net.sf.nmedit.jpatch.clavia.nordmodular.Knob;
import net.sf.nmedit.jpatch.clavia.nordmodular.MidiController;
import net.sf.nmedit.jpatch.clavia.nordmodular.MidiControllerSet;
import net.sf.nmedit.jpatch.clavia.nordmodular.NMPatch;
import net.sf.nmedit.jpatch.clavia.nordmodular.Note;
import net.sf.nmedit.jpatch.clavia.nordmodular.PNMMorphSection;
import net.sf.nmedit.jpatch.clavia.nordmodular.Signal;
import net.sf.nmedit.jpatch.clavia.nordmodular.VoiceArea;

public class PatchBuilder implements PContentHandler
{
    
    private static Log log = LogFactory.getLog(PatchBuilder.class);
    
    private NMPatch patch;
    private VoiceArea voiceArea;
    private ModuleDescriptions modules;
    private ErrorHandler errorHandler;
    private String patchName = null;


    public PatchBuilder(ErrorHandler errorHandler, ModuleDescriptions modules)
    {
        this(null, errorHandler, modules);
    }
    
    
    public PatchBuilder(NMPatch patch, ErrorHandler errorHandler, ModuleDescriptions modules)
    {
        this.patch = patch;
        this.errorHandler = errorHandler;
        this.modules = modules;
        reset();
    }
    
    public String getPatchName()
    {
        return patchName;
    }
    
    public void setPatchName(String name)
    {
        this.patchName = name;
        if (this.patch != null)
            patch.setName(name);
    }
    
    private void emitwarning(String message) throws ParseException
    {
        errorHandler.warning(new ParseException(message));
    }
    
    private void emiterror(String message) throws ParseException
    {
        errorHandler.error(new ParseException(message));   
    }
    
    private void emitfatal(String message) throws ParseException
    {
        errorHandler.fatal(new ParseException(message));   
    }
    
    public void reset()
    {
        patch = new NMPatch(modules);
        patch.setName(getPatchName());
        patch.setEditSupportEnabled(false); // disable history
    }
    
    public NMPatch getPatch()
    {
        return patch;
    }

    public void notes( String notes ) throws ParseException
    {
        patch.setNote(notes);
    }
    
    private VoiceArea getVoiceArea(int voiceAreaId)
    {
        switch (voiceAreaId)
        {
            case 1: return patch.getPolyVoiceArea();
            case 0: return patch.getCommonVoiceArea();
            default: return null;
        }
    }

    public void beginSection( int section, int voiceAreaId )
            throws ParseException
    {
        voiceArea = getVoiceArea(voiceAreaId);
    }

    public void endSection( int section ) throws ParseException
    {
        voiceArea = null;
    }

    public void header( int[] record ) throws ParseException
    {
        Header header = patch.getHeader();
        for (int i=0;i<HEADER_RSIZE;i++)
            header.setValue(i, record[i]);
    }
    
    public void header( String property, String value ) throws ParseException
    {
        if ("version".equals(property))
        {
            if (!value.contains("3"))
                emitfatal("unsupported version "+value);
        }
        else
        {
            emitwarning("unknown property "+property+"="+value);
        }
    }

    public void moduleDump( int[] record ) throws ParseException
    {
        PModule module;
        int mindex = record[1];
        try
        {
            module = voiceArea.createModule(modules.getModuleById("m"+mindex));
        }
        catch (InvalidDescriptorException e)
        {
            throw new ParseException(e);
        }
        module.setInternalLocation(record[2], record[3]);
        
        if (!voiceArea.add(record[0], module))
        {
            String e = module+" rejected in "+voiceArea;
            
            PModule prev = voiceArea.getModule(mindex);
            if (prev != null)
                e+=" index reserved by "+prev;
             
            emitwarning(e);
        }
    }

    public void currentNoteDump( int[] record ) throws ParseException
    {
        patch.getNoteSet().add(new Note(record[0], record[1], record[2]));
    }
    
    private boolean intToOutput(int connectorType) throws ParseException
    {
        // 0 ~ input, 1 ~ output
        switch (connectorType)
        {
            case 0: return false;
            case 1: return true;
            default: throw new ParseException("invalid connector type:"+connectorType);
        }
    }

    private PConnector getConnector(int mod, int cindex, int ctype) throws ParseException
    {
        PModule module = voiceArea.getModule(mod);
        if (module == null)
        {
            emiterror("module[index="+mod+"] does not exist");
            return null;
        }
        boolean output = intToOutput(ctype);
        
        for (int i=module.getConnectorCount()-1;i>=0;i--)
        {
            PConnector tmp = module.getConnector(i);
            if (cindex==tmp.getIntAttribute("index",-1) && tmp.isOutput()==output)
            {
                return tmp;
            }
        }
        emiterror("Connector[index="+cindex
                +",output="+output+"("+ctype+")] does not exist in "+module);
        return null;
    }

    public void cableDump( int[] record ) throws ParseException
    {
        Signal signal = Signal.bySignalID(record[0]);
        PConnector cdst = getConnector(record[1], record[2], record[3]);
        PConnector csrc = getConnector(record[4], record[5], record[6]);
        
        if (cdst == null || csrc == null)
        {
            return;
        }

        if (cdst.isConnected(csrc))
            emitwarning("Already connected: "+csrc+", "+cdst);
        else
        {
            if (!csrc.connect(cdst/*, signal*/)) // TODO signal
                emiterror("Could not connect: "+csrc+", "+cdst);
        }
    }
    
    public void parameterDump( int[] record ) throws ParseException
    {
        PModule module = voiceArea.getModule(record[0]);
        
        if (module.getIntAttribute("index", -1)!=record[1])
            emiterror(module+" has different id than "+record[1]+" in ParameterDump");

        List<PParameter> plist =  Helper.getParametersByClass(module, "parameter");
        int paramClassCount = plist.size();
        
        if (record[2]!=paramClassCount)
            emiterror("invalid number of parameters[class='parameter'] "+record[2]+" expected "+paramClassCount);
        
        for (int i=0;i<paramClassCount;i++)
        {
            plist.get(i).setValue(record[3+i]);
        }
    }

    public void customDump( int[] record ) throws ParseException
    {
        PModule module = voiceArea.getModule(record[0]);

        List<PParameter> plist =  Helper.getParametersByClass(module, "custom");
        int customCount = plist.size();
        
        if (record[1]!=customCount)
            emiterror("invalid number of parameters[class=custom] "+record[2]+" expected "+customCount);
        
        for (int i=0;i<customCount;i++)
        {
            plist.get(i).setValue(record[2+i]);
        }
    }

    public void keyboardAssignment( int[] record ) throws ParseException
    {
        PNMMorphSection morphs = patch.getMorphSection();
        
        for (int i=0;i<4;i++)
        {
            int value = record[i];
            PParameter m = morphs.getKeyboardAssignment(i);
            if (value>=0 && value<=2)
                m.setValue(value);
            else
                emiterror("morph "+i
                        +" keyboard assignment value out of range ["
                        +m.getMinValue()+".."+m.getMaxValue()+"]: "+value);
        }
    }

    public void knobMapDump( int[] record ) throws ParseException
    {
        VoiceArea va = getVoiceArea(record[0]);
        int modIndex = record[1];
        int paramIndex = record[2];
        PParameter p;
        if (va == null)
        {
            p = patch.getMorphSection().getMorph(paramIndex);
        }
        else
        {
            PModule module = va.getModule(modIndex);
            if (module == null)
                throw new ParseException("[KnobMapDump] Module does not exist at index:"+modIndex);
            
            p = Helper.getParameter(module, "parameter", paramIndex);
            if (p == null)
                throw new ParseException("[KnobMapDump] Parameter does not exist at index:"+paramIndex+" (module index: "+modIndex+")");
            
        }
        Knob knob = patch.getKnobs().getByID(record[3]);
        knob.setParameter(p);
    }

    public void morphMapDumpProlog( int[] record ) throws ParseException
    {
        PNMMorphSection morphs = patch.getMorphSection();
        
        for (int i=0;i<4;i++)
        {
            int value = record[i];
            
            PParameter m = morphs.getMorph(i);
            
            if (value>=m.getMinValue() && value<=m.getMaxValue())
                m.setValue(value);
            else
                emiterror("morph "+i
                        +" value out of range ["+m.getMinValue()+".."+m.getMaxValue()+"]: "+value);
        }
    }
   
    public void morphMapDump( int[] record ) throws ParseException
    {
        VoiceArea voiceArea = getVoiceArea(record[0]);
        PModule module = voiceArea.getModule(record[1]);
        PParameter p = Helper.getParameter(module, "parameter", record[2]); 
        PParameter morphRange = p.getExtensionParameter();
        if (morphRange == null)
        {
            if (log.isDebugEnabled())
                log.debug("morphMapDump, morph parameter not found for parameter "+p);
        }
        else
        {
            patch.getMorphSection().assign(record[3], p);
            morphRange.setValue(record[4]);
        }
    }

    public void ctrlMapDump( int[] record ) throws ParseException
    {
        MidiControllerSet mcset  = patch.getMidiControllers();
        int ccId = record[Format.CTRL_MAP_DUMP_CC_INDEX];
        
        if (!MidiController.isValidCC(ccId))
            emiterror("invalid cc number "+ccId);
        else
        {
            MidiController cc = mcset.getByMC(ccId);
            
            PParameter p = null;
            
            int section = record[Format.CTRL_MAP_DUMP_SECTION_INDEX];
            int moduleIndex = record[Format.CTRL_MAP_DUMP_MODULE_INDEX];
            int pindex = record[Format.CTRL_MAP_DUMP_PARAMETER_INDEX];
            if (section == 0 || section == 1)
            {
                VoiceArea va = section == 0 ? patch.getCommonVoiceArea() : patch.getPolyVoiceArea();
                
                PModule module =  va.getModule(moduleIndex);
                if (module == null)
                    emiterror("Module[index="+moduleIndex+"] does not exist in "+patch.getCommonVoiceArea());
                
                if (pindex<0 || pindex>=Helper.getParameterClassCount(module, "parameter"))                    
                    emiterror(module+" has no parameter[index="+pindex+"]");
                else
                    p = Helper.getParameter(module, "parameter", pindex);
            }
            else if (section==2)
                p = patch.getMorphSection().getMorph(record[2]);
            else
                emiterror("invalid section-id "+section);
            
            cc.setParameter(p);
        }
    }

    public void moduleNameDump( int moduleIndex, String moduleName )
            throws ParseException
    {
        PModule module = null;
        
        try
        { 
            module = voiceArea.getModule(moduleIndex);
        }
        catch (NoSuchElementException e)
        {}
        if (module != null)
        {
            module.setTitle(moduleName);
        }
        else
        {
            emiterror("namedump: module[index="
                    +moduleIndex+",name='"+moduleName+"'] does not exist");
        }
    }

    public void beginDocument() throws ParseException
    {
        // no op
    }

    public void endDocument() throws ParseException
    {
        // no op
    }

}
