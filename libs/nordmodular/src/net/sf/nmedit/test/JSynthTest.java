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
 */package net.sf.nmedit.test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.sound.midi.MidiDevice.Info;

import net.sf.nmedit.jnmprotocol2.AckMessage;
import net.sf.nmedit.jnmprotocol2.ErrorMessage;
import net.sf.nmedit.jnmprotocol2.GetPatchListMessage;
import net.sf.nmedit.jnmprotocol2.IAmMessage;
import net.sf.nmedit.jnmprotocol2.LightMessage;
import net.sf.nmedit.jnmprotocol2.MeterMessage;
import net.sf.nmedit.jnmprotocol2.MidiMessage;
import net.sf.nmedit.jnmprotocol2.NewPatchInSlotMessage;
import net.sf.nmedit.jnmprotocol2.NmProtocolListener;
import net.sf.nmedit.jnmprotocol2.ParameterMessage;
import net.sf.nmedit.jnmprotocol2.PatchListEntry;
import net.sf.nmedit.jnmprotocol2.PatchListMessage;
import net.sf.nmedit.jnmprotocol2.PatchMessage;
import net.sf.nmedit.jnmprotocol2.SlotActivatedMessage;
import net.sf.nmedit.jnmprotocol2.SlotsSelectedMessage;
import net.sf.nmedit.jnmprotocol2.VoiceCountMessage;
import net.sf.nmedit.jnmprotocol2.utils.NmLookup;
import net.sf.nmedit.jpatch.clavia.nordmodular.NM1ModuleDescriptions;
import net.sf.nmedit.jpatch.clavia.nordmodular.NMPatch;
import net.sf.nmedit.jsynth.Slot;
import net.sf.nmedit.jsynth.SynthException;
import net.sf.nmedit.jsynth.clavia.nordmodular.NmSlot;
import net.sf.nmedit.jsynth.clavia.nordmodular.NordModular;
import net.sf.nmedit.jsynth.clavia.nordmodular.utils.NmUtils;
import net.sf.nmedit.jsynth.event.SlotEvent;
import net.sf.nmedit.jsynth.event.SlotListener;
import net.sf.nmedit.jsynth.event.SlotManagerListener;
import net.sf.nmedit.jsynth.midi.MidiDescription;
import net.sf.nmedit.jsynth.midi.MidiPlug;
import net.sf.nmedit.jsynth.worker.PatchLocation;
import net.sf.nmedit.jsynth.worker.StorePatchWorker;

public class JSynthTest
{

    public static void main(String[] args) throws Exception
    {
        (new JSynthTest()).run();
    }

    private App app;
    private NordModular synth;
    
    private void run() throws Exception
    {
        app = new App();
        (new Thread(app)).start();
        
        
        NM1ModuleDescriptions md = NmUtils.parseModuleDescriptions();
        
        synth = new NordModular(md);
        synth.getSlotManager().addSlotManagerListener(new SlotManagerInfo());
        synth.addProtocolListener(new ProtocolInfo());
        
        initPorts();

        System.out.println("Using ports:");
        System.out.println("\t-"+synth.getPCInPort());
        System.out.println("\t-"+synth.getPCOutPort());
        
        synth.setConnected(true);
        
        if (!waitForSlots())
        {
            System.err.println("timeout: slots not available");
            shutdown();
        }
        
        
        Slot slot = synth.getSlot(0);
        
        NMPatch patch = new NMPatch(null);
        patch.setName("Hallo123");
        
        StorePatchWorker worker = synth.createStorePatchWorker();
        worker.setSource(patch);
        worker.setDestination(new PatchLocation(slot.getSlotIndex()));
        
        worker.store();
        
        //listBanks();
    }
    
    private boolean waitForSlots()
    {
        long timeout = System.currentTimeMillis() + 1000;
        
        while(synth.isConnected() && synth.getSlotCount()<=0)
        {
            if (System.currentTimeMillis()>timeout)
                break;
            
            try
            {
                Thread.sleep(50);
            }
            catch (InterruptedException e)
            {
                // no op
            }
        }
        
        return synth.isConnected() && synth.getSlotCount()>0;
    }
   
    void listBanks() throws Exception
    {
        BankListTest test = new BankListTest();
        
        synth.addProtocolListener(test);
        test.start();
    }
    
    private static class ProtocolInfo extends NmProtocolListener
    {
        public void messageReceived(IAmMessage m) { msg(m); }
        public void messageReceived(LightMessage m) { /*msg(m);*/ }
        public void messageReceived(MeterMessage m) { msg(m); }
        public void messageReceived(PatchMessage m) { /*msg(m);*/ }
        public void messageReceived(AckMessage m) { msg(m); }
        public void messageReceived(PatchListMessage m) { msg(m); }
        public void messageReceived(NewPatchInSlotMessage m) { msg(m); }
        public void messageReceived(VoiceCountMessage m) { msg(m); }
        public void messageReceived(SlotsSelectedMessage m) { msg(m); }
        public void messageReceived(SlotActivatedMessage m) { msg(m); }
        public void messageReceived(ParameterMessage m) { msg(m); }
        public void messageReceived(ErrorMessage m) { msg(m); }
        public void msg(MidiMessage m) { System.out.println(m); }
    }
    
    private class BankListTest extends NmProtocolListener
    {
        public void start() throws Exception
        {
            request(0,0);
        }
        
        protected void request(int section, int position) throws Exception
        {
            if (section<0 || position<0)
            {
                synth.removeProtocolListener(this);
                return;
            }
            
            System.out.println("requesting patch list: section="+section+", position="+position);

            synth.getProtocol().send( new GetPatchListMessage(section, position) );
        }
        
        public void messageReceived(PatchListMessage message) 
        {
            for (PatchListEntry e: message.getEntries())
                System.out.println(e);

            System.out.println("next section="+message.getNextSection()+", position="+message.getNextPosition());
            try
            {
                request(message.getNextSection(), message.getNextPosition());
            }
            catch (Exception e1)
            {
                e1.printStackTrace();
            }
        }
    }

    private void initPorts() throws SynthException
    {
        Info[] info = NmLookup.lookup(NmLookup.getHardwareDevices(), 1, 1000);

        MidiDescription descIn = new MidiDescription(info[0], 1);
        MidiDescription descOut = new MidiDescription(info[1], 0);
        synth.getPCInPort().setPlug(new MidiPlug(descIn));
        synth.getPCOutPort().setPlug(new MidiPlug(descOut));
    }
    
    private static class SlotManagerInfo implements SlotManagerListener,
        SlotListener, PropertyChangeListener
    {

        public void slotAdded(SlotEvent e)
        {
            System.out.println("slot added: "+e.getSlot());
            install((NmSlot) e.getSlot());
        }

        public void slotRemoved(SlotEvent e)
        {
            System.out.println("slot removed: "+e.getSlot());
            uninstall((NmSlot) e.getSlot());
        }
        
        private void install(NmSlot slot)
        {
            slot.addSlotListener(this);
            slot.addPropertyChangeListener(this);
        }
        
        private void uninstall(NmSlot slot)
        {
            slot.removeSlotListener(this);
            slot.removePropertyChangeListener(this);
        }

        public void newPatchInSlot(SlotEvent e)
        {
            NmSlot slot = (NmSlot) e.getSlot();
            
            System.out.println("new patch in slot "+slot.getName()+": "+slot.getPatch());
        }

        public void propertyChange(PropertyChangeEvent evt)
        {
            NmSlot slot = (NmSlot) evt.getSource();
            System.out.println(slot.getName()+": "+evt.getPropertyName()+"="+evt.getNewValue()+" (old:"+evt.getOldValue()+")");
        }
        
    }
    
    private void shutdown()
    {
        System.out.println("Shutting down...");
        try
        {
            Thread.sleep(500);
        }
        catch (InterruptedException e1)
        {
        }
        
        try
        {
            synth.setConnected(false);
        }
        catch (SynthException e)
        {
            e.printStackTrace();
        }
        
        System.exit(0);
    }
    
    private class App implements Runnable
    {
        private boolean stopped = false;
        private long timeout = System.currentTimeMillis()+30000;
        
        public void run()
        {
            try
            {
            loop:for (;;)
            {
                synchronized (this)
                {
                    if (stopped)
                        break loop;
                }
                
                if (timeout<System.currentTimeMillis())
                    break loop;
                
                delay();
            }
            }
            finally
            {
                shutdown();
            }
        }
        
        private void delay()
        {
            try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
                // ignore
            }
        }
        
        public void stop()
        {
            synchronized (this)
            {
                stopped = true;
            }
        }
        
    }
    
}
