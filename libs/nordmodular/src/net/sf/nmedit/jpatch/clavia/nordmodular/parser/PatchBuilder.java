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

import java.util.NoSuchElementException;

import net.sf.nmedit.jpatch.ConnectorDescriptor;
import net.sf.nmedit.jpatch.InvalidDescriptorException;
import net.sf.nmedit.jpatch.Parameter;
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
import net.sf.nmedit.jpatch.clavia.nordmodular.Note;
import net.sf.nmedit.jpatch.clavia.nordmodular.Signal;
import net.sf.nmedit.jpatch.clavia.nordmodular.VoiceArea;
import net.sf.nmedit.jpatch.spec.ModuleDescriptions;

public class PatchBuilder implements PContentHandler
{
    
    private NMPatch patch;
    private VoiceArea voiceArea;
    private ModuleDescriptions modules;
    private ErrorHandler errorHandler;
    private String patchName = null;

    public PatchBuilder(ErrorHandler errorHandler, ModuleDescriptions modules)
    {
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
        NMModule module;
        int mindex = record[1];
        try
        {
            module = voiceArea.createModule( mindex );
        }
        catch (InvalidDescriptorException e)
        {
            throw new ParseException(e);
        }
        module.setIndex(record[0]);
        module.setLocation(record[2], record[3]);
        
        if (!voiceArea.add(module))
        {
            String e = module+" rejected in "+voiceArea;
            
            NMModule prev = voiceArea.getModule(mindex);
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

    private NMConnector getConnector(int mod, int cindex, int ctype) throws ParseException
    {
        NMModule module = voiceArea.getModule(mod);
        if (module == null)
        {
            emiterror("module[index="+mod+"] does not exist");
            return null;
        }
        boolean output = intToOutput(ctype);
        ConnectorDescriptor cd = module.getDescriptor().getConnector(cindex, output);
        if (cd == null)
        {
            for (int i=module.getDescriptor().getConnectorCount()-1;i>=0;i--)
            {
                System.out.println(module.getDescriptor().getConnector(i));
            }
            
            emiterror("Connector[index="+cindex
                    +",output="+output+"("+ctype+")] does not exist in "+module);
            return null;
        }
        
        NMConnector c = null;
        try
        {
            c = module.getConnector(cd);
        }
        catch (InvalidDescriptorException e)
        {
            emiterror(e.getMessage());
        }
        return c;
    }

    public void cableDump( int[] record ) throws ParseException
    {
        Signal signal = Signal.bySignalID(record[0]);
        NMConnector cdst = getConnector(record[1], record[2], record[3]);
        NMConnector csrc = getConnector(record[4], record[5], record[6]);
        
        if (cdst == null || csrc == null)
        {
            return;
        }

        if (cdst.isConnectedWith(csrc))
            emitwarning("Already connected: "+csrc+", "+cdst);
        else
        {
            csrc.connectWith(cdst, signal);
            if (!cdst.isConnectedWith(csrc))
                emiterror("Could not connect: "+csrc+", "+cdst);
        }
    }
    
    public void parameterDump( int[] record ) throws ParseException
    {
        NMModule module = voiceArea.getModule(record[0]);
        
        if (module.getModuleId()!=record[1])
            emiterror(module+" has different id than "+record[1]+" in ParameterDump");
        
        if (record[2]!=module.getParamCount())
            emiterror("invalid number of parameters "+record[2]+" expected "+module.getParamCount());
        
        for (int i=0;i<module.getParamCount();i++)
            module.getParameter(i).setValue(record[3+i]);
    }

    public void customDump( int[] record ) throws ParseException
    {
        NMModule module = voiceArea.getModule(record[0]);
        
        if (record[1]!=module.getCustomCount())
            emiterror("invalid number of custom-parameters "+record[2]+" expected "+module.getParamCount());
        
        for (int i=0;i<module.getCustomCount();i++)
            module.getCustom(i).setValue(record[2+i]);
    }

    public void keyboardAssignment( int[] record ) throws ParseException
    {
        MorphSection morphs = patch.getMorphSection();
        
        for (int i=0;i<4;i++)
        {
            int value = record[i];

            Parameter m = morphs.getMorph(i);
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
        Parameter p;
        if (va == null)
        {
            p = patch.getMorphSection().getMorph(paramIndex);
        }
        else
        {
            NMModule module = va.getModule(modIndex);
            p = module.getParameter(paramIndex);
        }
        Knob knob = patch.getKnobs().getByID(record[3]);
        knob.setParameter(p);
    }

    public void morphMapDumpProlog( int[] record ) throws ParseException
    {
        MorphSection morphs = patch.getMorphSection();
        
        for (int i=0;i<4;i++)
        {
            int value = record[i];
            
            Parameter m = morphs.getMorph(i);
            
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
        NMModule module = voiceArea.getModule(record[1]);
        NMParameter p = module.getParameter(record[2]);
        Parameter morphRange = module.getParameter(record[2]);
        Morph morph = patch.getMorphSection().getMorph(record[3]);
        morph.add(p);
        morphRange.setValue(record[4]);
    }

    public void ctrlMapDump( int[] record ) throws ParseException
    {
        MidiControllerSet mcset  = patch.getMidiControllers();
        
        if (!MidiController.isValidCC(record[3]))
            emiterror("invalid cc number "+record[3]);
        else
        {
            MidiController cc = mcset.getByMC(record[3]);
            
            Parameter p = null;
            
            int section = record[0];
            int pindex = record[2];
            if (section == 0)
            {
                try
                {
                NMModule module =  patch.getCommonVoiceArea().getModule(record[1]);
                
                if (pindex<0 || pindex>=module.getParamCount())
                    emiterror(module+" has no parameter[index="+pindex+"]");
                else
                    p = module.getParameter(pindex);
                }
                catch (NoSuchElementException e)
                {
                    emiterror("Module[index="+record[1]+"] does not exist in "+patch.getCommonVoiceArea());
                }
            }
            else if (section==1)
            {
                try
                {
                NMModule module = patch.getPolyVoiceArea().getModule(record[1]);
                if (pindex<0 || pindex>=module.getParamCount())
                    emiterror(module+" has no parameter[index="+pindex+"]");
                else
                    p = module.getParameter(pindex);
                }
                catch (NoSuchElementException e)
                {
                    emiterror("Module[index="+record[1]+"] does not exist in "+patch.getPolyVoiceArea());
                }
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
        NMModule module = null;
        
        try
        { 
            module = voiceArea.getModule(moduleIndex);
        }
        catch (NoSuchElementException e)
        {}
        if (module != null)
        {
            module.setName(moduleName);
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
