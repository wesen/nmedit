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

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JOptionPane;

import net.sf.nmedit.jpatch.clavia.nordmodular.NMData;
import net.sf.nmedit.jsynth.SynthException;
import net.sf.nmedit.jsynth.Synthesizer;
import net.sf.nmedit.jsynth.clavia.nordmodular.NordModular;
import net.sf.nmedit.jsynth.midi.MidiPlug;
import net.sf.nmedit.jsynth.nomad.SynthRegistry;
import net.sf.nmedit.jsynth.nomad.forms.SynthObjectForm;
import net.sf.nmedit.nomad.core.Nomad;
import net.sf.nmedit.nomad.core.forms.ExceptionDialog;
import net.sf.nmedit.nomad.core.registry.GlobalRegistry;
import net.sf.nmedit.nomad.core.registry.Registry;
import net.sf.nmedit.nomad.core.service.Service;
import net.sf.nmedit.nomad.core.service.synthService.NewSynthService;
import net.sf.nmedit.nomad.core.swing.tabs.JTabbedPane2;

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
        newContext(createSynth());
    }

    public Class<? extends Service> getServiceClass()
    {
        return NewSynthService.class;
    }
    
    private static void newContext(NordModular nm)
    {
        SynthObjectForm<NordModular> sof = new NMSynthDeviceContext(nm);
        JTabbedPane2 dst = Nomad.sharedInstance().getSynthTabbedPane();
        dst.addTab("Nord Modular", sof);
        dst.addAskRemoveListener(new RemoveChecker(sof));
    }
    
    private static NordModular createSynth()
    {
        NordModular synth = new NordModular(NMData.sharedInstance().getModuleDescriptions());

    synth.putClientProperty("icon.nm.micro", NMContextData.sharedInstance().getMicroIcon());
    synth.putClientProperty("icon.nm.keyboard", NMContextData.sharedInstance().getModularIcon());
    synth.putClientProperty("icon.nm.rack", NMContextData.sharedInstance().getModularRackIcon());
    

    Registry<Synthesizer> sreg = GlobalRegistry.getInstance().getRegistry(Synthesizer.class);
    sreg.add(synth);
    
    return synth;
    }

    public static void newSynth(String name, MidiPlug input, MidiPlug output)
    {
        NordModular synth = createSynth();

        try
        {
            synth.getPCInPort().setPlug(input);
            synth.getPCOutPort().setPlug(output);
        }
        catch (SynthException e)
        {
            // ignore - should not happen
        }

        newContext(synth);
    }
    
    private static class RemoveChecker implements PropertyChangeListener
    {

        private SynthObjectForm<NordModular> sof;

        public RemoveChecker(SynthObjectForm<NordModular> sof)
        {
            this.sof = sof;
        }

        public void propertyChange(PropertyChangeEvent evt)
        {
            if (!(evt.getNewValue() != null && evt.getNewValue() instanceof Integer))
                return;
            int index = (Integer) evt.getNewValue();

            JTabbedPane2 dst = Nomad.sharedInstance().getSynthTabbedPane();
            Component c = dst.getComponentAt(index);
            if (c != sof) return;
            
            if (sof.getSynthesizer().isConnected())
            {
                if (
                JOptionPane.showConfirmDialog(sof, 
                  "The device '"+sof.getSynthesizer().getName()+"' is connected. "+
                  "Do you want to disconnect and remove the device ?",
                  "Removing device",
                  JOptionPane.YES_NO_OPTION
                ) == JOptionPane.NO_OPTION )
                {
                    return;
                }
                
                // disconnect
                try
                {
                    sof.getSynthesizer().setConnected(false);
                } 
                catch (SynthException e)
                {
                    ExceptionDialog.showErrorDialog(Nomad.sharedInstance().getWindow().getRootPane(), 
                      "Could not disconnect device '"+sof.getSynthesizer().getName()+"'.",
                      "Disconnect",
                      e);
                    return;
                }
            }
            
            // remove tab
            dst.removeTabAt(index);
            
            // uninstall listener
            dst.removeAskRemoveListener(this);
            
            // uninstall synthesizer
            SynthRegistry.sharedInstance().remove(sof.getSynthesizer());
        }
    }
    
}
