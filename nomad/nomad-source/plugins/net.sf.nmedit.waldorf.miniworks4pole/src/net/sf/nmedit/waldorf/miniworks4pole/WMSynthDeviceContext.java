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
package net.sf.nmedit.waldorf.miniworks4pole;

import java.awt.Event;

import javax.sound.midi.MidiDevice;

import net.sf.nmedit.jsynth.Slot;
import net.sf.nmedit.jsynth.SynthException;
import net.sf.nmedit.jsynth.event.SlotEvent;
import net.sf.nmedit.jsynth.event.SlotListener;
import net.sf.nmedit.jsynth.event.SlotManagerListener;
import net.sf.nmedit.jsynth.midi.MidiPlug;
import net.sf.nmedit.jsynth.worker.RequestPatchWorker;
import net.sf.nmedit.nomad.core.Nomad;
import net.sf.nmedit.nomad.core.forms.NomadMidiDialog;
import net.sf.nmedit.nomad.core.forms.NomadMidiDialogFrmHandler;
import net.sf.nmedit.nomad.core.swing.explorer.ExplorerTree;
import net.waldorf.miniworks4pole.jpatch.MWPatch;
import net.waldorf.miniworks4pole.jsynth.MWSlot;
import net.waldorf.miniworks4pole.jsynth.Miniworks4Pole;

public class WMSynthDeviceContext //extends SynthDeviceContext 
{
    /*
    public WMSynthDeviceContext(ExplorerTree etree, Miniworks4Pole synth,
            String title)
    {
        super(etree, title);

        setSynth(synth);
        (new PatchOpener(synth)).install();
    }

    public Miniworks4Pole getSynthesizer()
    {
        return (Miniworks4Pole) super.getSynth();
    }
*/

    protected void connect()
    {/*
        super.connect();
        
        if (getSynth().isConnected())
        {
            
          //  ((MWSlot)getSynth().getSlot(0))
        //    .setPatch(MWData.createPatch());
            
     //       System.out.println(((MWSlot)getSynth().getSlot(0)).getPatch());
        }*/
    }
    /*
    private static class PatchOpener implements SlotManagerListener,
        SlotListener
    {
        private Miniworks4Pole synth;

        private MWPatchDoc patchDoc;

        private PatchOpener(Miniworks4Pole synth)
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
            installSlot((MWSlot) e.getSlot());
        }

        public void slotRemoved(SlotEvent e)
        {
            uninstallSlot((MWSlot) e.getSlot());
        }

        public void installSlot(MWSlot slot)
        {
            slot.addSlotListener(this);
        }
        
        public void uninstallSlot(MWSlot slot)
        {
            slot.removeSlotListener(this);
        }

        public void newPatchInSlot(SlotEvent e)
        {
            if (patchDoc != null)
                uninstallDoc(patchDoc);
            patchDoc = null;
            
            MWSlot slot = (MWSlot) e.getSlot();
            MWPatch patch = slot.getPatch();
            if (patch != null)
            {
                installDoc(new MWPatchDoc(patch));
            }
            /uninstallDoc(slot);
            
            if (slot.getPatch()!= null)
                installDoc(slot);
        }

        public void uninstallDoc(MWPatchDoc doc)
        {
            Nomad.sharedInstance()
            .getDocumentManager().remove(doc);
        }

        public void installDoc(MWPatchDoc doc)
        {
            Nomad.sharedInstance()
            .getDocumentManager().add(doc);
        }
        
    }*/
/*
    protected boolean showSettings()
    {
        Miniworks4Pole synth = getSynthesizer();

        MidiPlug inPlug = synth.getInPort().getPlug();
        MidiDevice.Info in = inPlug != null ? inPlug.getDeviceInfo() : null;

        MidiPlug outPlug = synth.getOutPort().getPlug();
        MidiDevice.Info out = outPlug != null ? outPlug.getDeviceInfo() : null;
        
        NomadMidiDialog dialog = new NomadMidiDialog(Nomad.sharedInstance().getWindow(), "MIDI");
        NomadMidiDialogFrmHandler form = dialog.getForm();
        form.setPreviousInput(in);
        form.setPreviousOutput(out);
        
        dialog.setModal(true);
        if (dialog.showDialog() == NomadMidiDialog.APPROVE_OPTION)
        {

            in = form.getSelectedInput();
            out = form.getSelectedOutput();

            if (in == null || out == null)
                return false;
            
            try
            {
                synth.getInPort().setPlug(new MidiPlug(in));
                synth.getOutPort().setPlug(new MidiPlug(out));
            }
            catch (SynthException e)
            {
                // TODO Auto-generated catch block
                
                return false;
            }
            return true;
        }
        
        return false;
    }
    

    protected void processEvent(Event event, Slot slot)
    {
        RequestPatchWorker worker = slot.createRequestPatchWorker(); 
        try
        {
            worker.requestPatch();
        }
        catch (SynthException e)
        {
            e.printStackTrace();
        }
    }

  */  
}

