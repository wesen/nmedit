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
package net.sf.nmedit.nordmodular;

import net.sf.nmedit.jsynth.SynthException;
import net.sf.nmedit.jsynth.Synthesizer;
import net.sf.nmedit.jsynth.clavia.nordmodular.NordModular;
import net.sf.nmedit.jsynth.midi.MidiPlug;
import net.sf.nmedit.jsynth.nomad.SynthDeviceContext;
import net.sf.nmedit.nomad.core.Nomad;
import net.sf.nmedit.nomad.core.registry.GlobalRegistry;
import net.sf.nmedit.nomad.core.registry.Registry;
import net.sf.nmedit.nomad.core.service.Service;
import net.sf.nmedit.nomad.core.service.synthService.NewSynthService;
import net.sf.nmedit.nomad.core.swing.explorer.ExplorerTree;

public class NewNordModularService
    implements NewSynthService
{

    public String getSynthDescription()
    {
        return "http://nmedit.sf.net";
    }

    public String getSynthName()
    {
        return "Nord Modular";
    }

    public String getSynthVendor()
    {
        return "Clavia (http://www.clavia.se)";
    }

    public String getSynthVersion()
    {
        return "OS 3.03";
    }

    public boolean isNewSynthAvailable()
    {
        return true;
    }

    public void newSynth()
    {
        NordModular nm = new NordModular(NMData.sharedInstance().getModuleDescriptions());
        newContext(nm);
    }

    public Class<? extends Service> getServiceClass()
    {
        return NewSynthService.class;
    }
    
    private void newContext(NordModular nm)
    {
        ExplorerTree et =
            Nomad.sharedInstance().getExplorer();

        SynthDeviceContext sdc = new NMSynthDeviceContext(et, nm,
               getSynthName());
        
        et.getRoot().add(sdc);
        et.fireRootChanged();
    }

    public static void newSynth(String name, MidiPlug input, MidiPlug output)
    {
        ExplorerTree etree = Nomad.sharedInstance().getExplorer();  
        NordModular synth = new NordModular(NMData.sharedInstance().getModuleDescriptions());

        synth.putClientProperty("icon.nm.micro", NMData.sharedInstance().getMicroIcon());
        synth.putClientProperty("icon.nm.keyboard", NMData.sharedInstance().getModularIcon());
        synth.putClientProperty("icon.nm.rack", NMData.sharedInstance().getModularRackIcon());

        try
        {
            synth.getPCInPort().setPlug(input);
            synth.getPCOutPort().setPlug(output);
        }
        catch (SynthException e)
        {
            // ignore - should not happen
        }

        Registry<Synthesizer> sreg = GlobalRegistry.getInstance().getRegistry(Synthesizer.class);
        sreg.add(synth);
        
        etree.addRootNode(new NMSynthDeviceContext(etree, synth, name));
    }
    
}
