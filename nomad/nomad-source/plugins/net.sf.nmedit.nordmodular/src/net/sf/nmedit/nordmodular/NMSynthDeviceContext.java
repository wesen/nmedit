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

import java.awt.Event;
import java.util.HashMap;
import java.util.Map;

import javax.sound.midi.MidiDevice.Info;

import net.sf.nmedit.jnmprotocol.utils.NmLookup;
import net.sf.nmedit.jsynth.SynthException;
import net.sf.nmedit.jsynth.clavia.nordmodular.NmSlot;
import net.sf.nmedit.jsynth.clavia.nordmodular.NordModular;
import net.sf.nmedit.jsynth.event.SlotEvent;
import net.sf.nmedit.jsynth.event.SlotListener;
import net.sf.nmedit.jsynth.event.SlotManagerListener;
import net.sf.nmedit.jsynth.midi.MidiPlug;
import net.sf.nmedit.jsynth.nomad.SynthDeviceContext;
import net.sf.nmedit.nomad.core.Nomad;
import net.sf.nmedit.nomad.core.swing.explorer.ExplorerTree;

public class NMSynthDeviceContext extends SynthDeviceContext 
{

    public NMSynthDeviceContext(ExplorerTree etree, NordModular synth,
            String title)
    {
        super(etree, title);

        setSynth(synth);
        (new PatchOpener(synth)).install();

    }

    public NordModular getSynthesizer()
    {
        return (NordModular) super.getSynth();
    }

    private void initPorts() throws SynthException
    {
        
        Info[] info = NmLookup.lookup(NmLookup.getHardwareDevices(), 1, 1000);

        if (info.length<2)
            return;
        getSynthesizer().getPCInPort().setPlug(new MidiPlug(info[0]));
        getSynthesizer().getPCOutPort().setPlug(new MidiPlug(info[1]));
    }
    
    public void processEvent(Event event)
    {
        
        if (!getSynthesizer().isConnected())
        {
            try
            {
                initPorts();
                getSynthesizer().setConnected(true);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return;
            }
        }
        
        System.out.println(event);
        
        /*
        if (event instanceof ContextEvent)
        {
            ContextEvent ce = (ContextEvent) event;
          
            if (ce.getNode() == this )
            {
                switch (ce.getId())
                {
                    case ExplorerTree.ACTION_OPEN:
                        setState(State.RUNNING);
                        break;
                }
            }
            
        }*/
    }
    
    private static class PatchOpener implements SlotManagerListener,
        SlotListener
    {
        private NordModular synth;

        private PatchOpener(NordModular synth)
        {
            this.synth = synth;
        }

        public void install()
        {
            synth.getSlotManager().addSlotManagerListener(this);
            
            for (int i=0;i<synth.getSlotCount();i++)
            {
                installSlot(synth.getSlot(i));
            }
        }

        public void slotAdded(SlotEvent e)
        {
            installSlot((NmSlot) e.getSlot());
        }

        public void slotRemoved(SlotEvent e)
        {
            uninstallSlot((NmSlot) e.getSlot());
        }

        public void installSlot(NmSlot slot)
        {
            slot.addSlotListener(this);
        }
        
        public void uninstallSlot(NmSlot slot)
        {
            slot.removeSlotListener(this);
        }

        public void newPatchInSlot(SlotEvent e)
        {
            NmSlot slot = (NmSlot) e.getSlot();
            uninstallDoc(slot);
            
            if (slot.getPatch()!= null)
                installDoc(slot);
        }

        private void uninstallDoc(NmSlot slot)
        {
            PatchDocument doc = docMap.get(slot.getSlotId());
            if (doc == null)
                return ;
            
            Nomad.sharedInstance()
            .getDocumentManager().remove(doc);
        }

        private void installDoc(NmSlot slot)
        {
            PatchDocument doc;
            try
            {
                doc = NmFileService.createPatchDoc(slot.getPatch());
            }
            catch(Exception e)
            {
                e.printStackTrace();
                return;
            }
            docMap.put(slot.getSlotId(), doc);
         
            Nomad.sharedInstance()
            .getDocumentManager().add(doc);
        }

        private Map<Integer, PatchDocument> docMap = new HashMap<Integer, PatchDocument>();
        
    }

}

