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
package net.waldorf.miniworks4pole.jsynth;

import java.util.HashMap;
import java.util.Map;

import net.sf.nmedit.jpatch.InvalidDescriptorException;
import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleDescriptor;
import net.sf.nmedit.jpatch.PParameterDescriptor;
import net.sf.nmedit.jpatch.PPatch;
import net.sf.nmedit.jpatch.event.PModuleContainerEvent;
import net.sf.nmedit.jpatch.event.PModuleContainerListener;
import net.sf.nmedit.jpatch.event.PParameterEvent;
import net.sf.nmedit.jpatch.event.PParameterListener;
import net.sf.nmedit.jpatch.PParameter;
import net.sf.nmedit.jpdl.Packet;
import net.sf.nmedit.jsynth.Slot;
import net.sf.nmedit.jsynth.event.SlotEvent;
import net.sf.nmedit.jsynth.event.SlotListener;
import net.sf.nmedit.jsynth.event.SlotManagerListener;
import net.waldorf.miniworks4pole.jpatch.MWPatch;
import net.waldorf.miniworks4pole.jprotocol.MWListenerSupport;
import net.waldorf.miniworks4pole.jprotocol.MWMidiListener;
import net.waldorf.miniworks4pole.jprotocol.MiniworksMidiMessage;

public class MWEventHandler extends MWMidiListener 
    implements PModuleContainerListener, PParameterListener, SlotManagerListener, SlotListener
{

    private Miniworks4Pole synth;
    private PPatch patch = null;

    private Map<PParameter, Integer> p2c = new HashMap<PParameter, Integer>();
    private Map<Integer, PParameter> c2p = new HashMap<Integer, PParameter>();


    public MWEventHandler(Miniworks4Pole synth, MWListenerSupport listenerSupport)
    {
        this.synth = synth;
        synth.getSlotManager().addSlotManagerListener(this);
        listenerSupport.addListener(this);
        listenerSupport.addParameterListener(this);
        
        for (int i=synth.getSlotCount()-1;i>=0;i--)
            install(synth.getSlot(i));
    }
    
    public void setPatch(PPatch patch)
    {
        if (this.patch != patch)
        {
            p2c.clear();
            c2p.clear();
            if (this.patch != null)
                uninstall(this.patch);
            this.patch = patch;
            if (this.patch != null)
                install(this.patch);
        }
    }

    private void install(PPatch pch)
    {
        pch.getModuleContainer(0).addModuleContainerListener(this);
        for (PModule m : pch.getModuleContainer(0))
            install(m);
    }

    private void uninstall(PPatch pch)
    {
        pch.getModuleContainer(0).removeModuleContainerListener(this);
        for (PModule m : pch.getModuleContainer(0))
            uninstall(m);
    }

    public void moduleAdded(PModuleContainerEvent e)
    {
        install(e.getModule());
    }

    public void moduleRemoved(PModuleContainerEvent e)
    {
        uninstall(e.getModule());
    }

    private void install(PModule module)
    {
        for (int i=module.getParameterCount()-1;i>=0;i--)
        {
            install(module.getParameter(i));
        }
    }

    private void uninstall(PModule module)
    {
        for (int i=module.getParameterCount()-1;i>=0;i--)
        {
            uninstall(module.getParameter(i));
        }
    }

    private void install(PParameter parameter)
    { 
        parameter.addParameterListener(this);
     
        PParameterDescriptor pd = parameter.getDescriptor();
        Integer controller = ((Integer)pd.getAttribute("controller"));

        p2c.put(parameter, controller);
        c2p.put(controller, parameter);
    }

    private void uninstall(PParameter parameter)
    {
        parameter.removeParameterListener(this);

        Integer controller = p2c.get(parameter);
        if (controller != null)
            c2p.remove(controller);
        p2c.remove(parameter);
    }

    public void parameterValueChanged(PParameterEvent e)
    {   
        PParameter p = e.getParameter();
        Integer controller = p2c.get(p);
        if (controller == null)
            return;
        
        send(MiniworksMidiMessage.createControllerMessage(controller, p.getValue()));
    }

    private void send(MiniworksMidiMessage message)
    { 
        synth.send(message);
    }

    public void parameterMessage(MiniworksMidiMessage message) 
    {
        PParameter p = c2p.get(message.getController());
        if (p == null)
            return;
        
        p.setValue(message.getControllerValue());
    };
    
    public void bankChangeMessage(MiniworksMidiMessage message) 
    {
        //System.out.println("bankChangeMessage(bank="+message.getBank());
        synth.send(MiniworksMidiMessage.createProgramDumpRequestMessage(1, message.getBank()));
    };
    
    public void programDumpMessage(MiniworksMidiMessage message)
    {
        //System.out.println("programDumpMessage");
        Packet pack = message.getPacket().getPacket("data");
        //System.out.println(pack.getName());
        
        int programNumber = pack.getVariable("ProgramNumber");
        
        /*for (Object o: pack.getAllVariables())
            System.out.println(o);*/

        MWPatch patch = new MWPatch(synth.getModuleDescriptions());
        
        patch.setProgramNumber(programNumber);
        
        byte[] data = message.getData();
        System.out.println(data);
        
        PModule module = patch.getMiniworksModule();
        for (int i=0;i<module.getParameterCount();i++)
        {
            PParameter par = module.getParameter(i); 
            PParameterDescriptor pd = par.getDescriptor();
            int value = pack.getVariable((String) pd.getAttribute("message-id"));
            par.setValue(value);
        }
        
        synth.getSlot(0).setPatch(patch);
        // TODO
        
    };
    public void programBulkDumpMessage(MiniworksMidiMessage message) {
        
        System.out.println("programBulkDumpMessage");
        
    };
    public void allDumpMessage(MiniworksMidiMessage message) {
        System.out.println("allDumpMessage");
        }

    public void slotAdded(SlotEvent e)
    {
        install(e.getSlot());
    }

    public void slotRemoved(SlotEvent e)
    {
        uninstall(e.getSlot());
    }

    private void install(Slot slot)
    {
        slot.addSlotListener(this);
    }

    private void uninstall(Slot slot)
    {
        slot.removeSlotListener(this);
    }

    public void newPatchInSlot(SlotEvent e)
    {
        setPatch(((MWSlot) e.getSlot()).getPatch());
    }

    public void focusRequested(PParameterEvent e)
    {
        // TODO Auto-generated method stub
        
    };

}
