package net.waldorf.miniworks4pole.jsynth;

import java.util.HashMap;
import java.util.Map;

import net.sf.nmedit.jpatch.InvalidDescriptorException;
import net.sf.nmedit.jpatch.Module;
import net.sf.nmedit.jpatch.ModuleDescriptor;
import net.sf.nmedit.jpatch.Parameter;
import net.sf.nmedit.jpatch.ParameterDescriptor;
import net.sf.nmedit.jpatch.Patch;
import net.sf.nmedit.jpatch.event.ModuleContainerEvent;
import net.sf.nmedit.jpatch.event.ModuleContainerListener;
import net.sf.nmedit.jpatch.event.ParameterEvent;
import net.sf.nmedit.jpatch.event.ParameterValueChangeListener;
import net.sf.nmedit.jsynth.Slot;
import net.sf.nmedit.jsynth.event.SlotEvent;
import net.sf.nmedit.jsynth.event.SlotListener;
import net.sf.nmedit.jsynth.event.SlotManagerListener;
import net.waldorf.miniworks4pole.jpatch.MWPatch;
import net.waldorf.miniworks4pole.jprotocol.MWListenerSupport;
import net.waldorf.miniworks4pole.jprotocol.MWMidiListener;
import net.waldorf.miniworks4pole.jprotocol.MiniworksMidiMessage;

public class MWEventHandler extends MWMidiListener 
    implements ModuleContainerListener, ParameterValueChangeListener, SlotManagerListener, SlotListener
{

    private Miniworks4Pole synth;
    private Patch patch = null;

    private Map<Parameter, Integer> p2c = new HashMap<Parameter, Integer>();
    private Map<Integer, Parameter> c2p = new HashMap<Integer, Parameter>();


    public MWEventHandler(Miniworks4Pole synth, MWListenerSupport listenerSupport)
    {
        this.synth = synth;
        synth.getSlotManager().addSlotManagerListener(this);
        listenerSupport.addListener(this);
        listenerSupport.addParameterListener(this);
        
        for (int i=synth.getSlotCount()-1;i>=0;i--)
            install(synth.getSlot(i));
    }
    
    public void setPatch(Patch patch)
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

    private void install(Patch pch)
    {
        pch.getModuleContainer().addModuleContainerListener(this);
        for (Module m : pch.getModuleContainer())
            install(m);
    }

    private void uninstall(Patch pch)
    {
        pch.getModuleContainer().removeModuleContainerListener(this);
        for (Module m : pch.getModuleContainer())
            uninstall(m);
    }

    public void moduleAdded(ModuleContainerEvent e)
    {
        install(e.getModule());
    }

    public void moduleRemoved(ModuleContainerEvent e)
    {
        uninstall(e.getModule());
    }

    private void install(Module module)
    {
        ModuleDescriptor md = module.getDescriptor();
        
        for (int i=md.getParameterCount()-1;i>=0;i--)
        {
            ParameterDescriptor pd = md.getParameter(i);
          
            try
            {
                install(module.getParameter(pd));
            }
            catch (InvalidDescriptorException e)
            {
                // ignore
            }
        }
    }

    private void uninstall(Module module)
    {
        ModuleDescriptor md = module.getDescriptor();
        
        for (int i=md.getParameterCount()-1;i>=0;i--)
        {
            ParameterDescriptor pd = md.getParameter(i);
            
            try
            {
                uninstall(module.getParameter(pd));
            }
            catch (InvalidDescriptorException e)
            {
                // ignore
            }
        }
    }

    private void install(Parameter parameter)
    { 
        parameter.addParameterValueChangeListener(this);
     
        ParameterDescriptor pd = parameter.getDescriptor();
        Integer controller = ((Integer)pd.getAttribute("controller"));

        p2c.put(parameter, controller);
        c2p.put(controller, parameter);
    }

    private void uninstall(Parameter parameter)
    {
        parameter.removeParameterValueChangeListener(this);

        Integer controller = p2c.get(parameter);
        if (controller != null)
            c2p.remove(controller);
        p2c.remove(parameter);
    }

    public void parameterValueChanged(ParameterEvent e)
    {   
        Parameter p = e.getParameter();
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
        Parameter p = c2p.get(message.getController());
        if (p == null)
            return;
        
        p.setValue(message.getControllerValue());
    };
    
    public void bankChangeMessage(MiniworksMidiMessage message) 
    {
        synth.send(MiniworksMidiMessage.createProgramDumpRequestMessage(1, message.getBank()));
    };
    
    public void programDumpMessage(MiniworksMidiMessage message)
    {
       // MWPatch patch = new MWPatch(synth.getModuleDescriptions());
      // synth.getSlot(0).setPatch(patch);
        // TODO
        
    };
    public void programBulkDumpMessage(MiniworksMidiMessage message) {};
    public void allDumpMessage(MiniworksMidiMessage message) {}

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
    };

}
