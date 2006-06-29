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
 * Created on Jun 18, 2006
 */
package net.sf.nmedit.jsynth.clavia.nordmodular.v3_03;

import java.util.Arrays;

import javax.swing.SwingUtilities;

import net.sf.nmedit.jnmprotocol.AckMessage;
import net.sf.nmedit.jnmprotocol.ErrorMessage;
import net.sf.nmedit.jnmprotocol.IAmMessage;
import net.sf.nmedit.jnmprotocol.LightMessage;
import net.sf.nmedit.jnmprotocol.NewPatchInSlotMessage;
import net.sf.nmedit.jnmprotocol.NmProtocolListener;
import net.sf.nmedit.jnmprotocol.ParameterMessage;
import net.sf.nmedit.jnmprotocol.PatchListMessage;
import net.sf.nmedit.jnmprotocol.PatchMessage;
import net.sf.nmedit.jnmprotocol.SlotActivatedMessage;
import net.sf.nmedit.jnmprotocol.SlotsSelectedMessage;
import net.sf.nmedit.jnmprotocol.VoiceCountMessage;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Format;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Module;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Patch;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.VoiceArea;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.io.BitstreamTranscoder;
import net.sf.nmedit.jpatch.io.PatchDecoder;
import net.sf.nmedit.jpatch.io.PatchDecoderException;
import net.sf.nmedit.jsynth.clavia.nordmodular.v3_03.io.BitStreamSource;
import net.sf.nmedit.jsynth.event.SynthStateChangeEvent;
import net.sf.nmedit.jsynth.event.SynthStateListener;

public class SynthMessageHandler extends NmProtocolListener implements SynthStateListener
{

    static 
    {
        BitstreamTranscoder.init();
    }

    private NordModular device;
    private BitStreamSource[] incomplete;

    public SynthMessageHandler(NordModular synth)
    {
        this.device = synth;
        synth.addSynthStateListener(this);
        
        incomplete = new BitStreamSource[device.getSlotCount()];
        Arrays.fill(incomplete, null);
    }
    
    public NordModular getNordModular()
    {
        return device;
    }

    public void synthStateChanged( SynthStateChangeEvent e )
    {
        // 
    }

    /*
        public void messageReceived(SlotsSelectedMessage m) {}
        public void messageReceived(SlotActivatedMessage m) {}
        public void messageReceived(NewPatchInSlotMessage m) {}
        public void messageReceived(ParameterMessage m) {}
        public void messageReceived(PatchMessage m) {}*/

        
    public void messageReceived(LightMessage message)
    {/*
        System.out.println("LightMessage: " +
                   "slot:" + message.get("slot") + " " +    
                   "pid:" + message.get("pid") + " " +
                   message.get("light0") +...+message.get("light19"));*/
    }

    public void messageReceived(VoiceCountMessage message)
    {
        for (int i=0;i<device.getSlotCount();i++)
            device.getSlot(i).setVoiceCount(message.get("voiceCount"+i));        
    }

    public void messageReceived(IAmMessage message)
    {
        System.out.println("IAmMessage: " +
                   "sender:" + message.get("sender") + " " +
                   "versionHigh:" + message.get("versionHigh") + " " +
                   "versionLow:" + message.get("versionLow"));
        if (message.get("sender") == IAmMessage.MODULAR) {
        System.out.println("IAmMessage: " +
                   "unknown1:" + message.get("unknown1") + " " +
                   "unknown2:" + message.get("unknown2") + " " +
                   "unknown3:" + message.get("unknown3") + " " +
                   "unknown4:" + message.get("unknown4"));
        }
        
        requestSlots();
    }

    
    private void requestSlot(int slotID) 
    {
        Slot slot = device.getSlot(slotID);
        slot.synchRequestPatch();
        
            /*
        task[slot] = null;
        RequestPatchMessage requestPatchMessage;
        try 
        {
            requestPatchMessage = new RequestPatchMessage();
            requestPatchMessage.set("slot", slot);
            device.send(requestPatchMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    private void requestSlots() {
        for (int i=0;i<device.getSlotCount();i++)
            requestSlot(i);
    }
 

    public NordModular getDevice() {
        return device;
    }
    
    public void messageReceived(PatchMessage message)
    {
        
        //System.out.println("messageReceived(PatchMessage)");
        final Slot slot = device.getSlot(message.get("slot"));

        PatchDecoder decoder;
        try
        {
            decoder = device.getPatchImplementation().createPatchDecoder(BitStreamSource.class);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return;
        }
        
        BitStreamSource source = incomplete[slot.getID()];
        if (source == null)
        {
            source = new BitStreamSource();
            incomplete[slot.getID()] = source;
        }
        
        source.setBitStream(message.getPatchStream());
        
        try
        {
            decoder.decode(source);
        }
        catch (Exception e)
        {
            incomplete[slot.getID()] = null;
            e.printStackTrace();
        }
        
        if (source.isComplete())
        {
            incomplete[slot.getID()] = null;
            
            try
            {
                slot.setPatch((Patch)decoder.getPatch());
                
                SwingUtilities.invokeLater(new Runnable(){public void run() {
                device.fireNewPatchInSlot(slot);
                }});
            }
            catch (PatchDecoderException e)
            {
                e.printStackTrace();
            }
        }
        
        /*
        if (!slot.canUpdateSlot())
            return ;
        
        try
        {
            slot.signalPacketReceived();
            NordUtilities.parsePatchMessage(message, slot.getPatch());
        }
        catch (TranscoderException e)
        {
            e.printStackTrace();
        }*/
    }
    
    public void messageReceived(AckMessage message)
    {
        int slotID = message.get("slot");
        int pid1 = message.get("pid1");
        //int type = message.get("type");
        //int pid2 = message.get("pid2");
        
        Slot slot = device.getSlot(slotID);
        
        if (pid1 != slot.getPID()) 
        {
            slot.setPID(pid1);
            slot.synchGetPatch();
        }
    }
    
    public void messageReceived(PatchListMessage message)
    {
        System.out.println("PatchListMessage"+message);
    }
    
    public void messageReceived(NewPatchInSlotMessage message)
    {
        System.out.println("NewPatchInSlotMessage: " +
                   "slot:" + message.get("slot") + " " +    
                   "pid:" + message.get("pid"));
        //int slot = message.get("slot");
       // requestSlot(slot);
    }
    
    public void messageReceived(SlotsSelectedMessage message)
    {
        /*
        System.out.println("SlotsSelectedMessage: " +
                   "slot0Selected:" + message.get("slot0Selected") + " " +
                   "slot1Selected:" + message.get("slot1Selected") + " " +
                   "slot2Selected:" + message.get("slot2Selected") + " " +
                   "slot3Selected:" + message.get("slot3Selected"));
        */
        for (int i=0;i<device.getSlotCount();i++)
        {
            boolean selected = message.get("slot"+i+"Selected")==1;
            final Slot slot = device.getSlot(i);
            slot.setSelected(selected);
            if (selected)
            {
                device.fireSlotSelectedMessage(slot);
            }
        }
    }
    
    public void messageReceived(SlotActivatedMessage message)
    {
        device.setActiveSlotID(message.get("activeSlot"));
    }
    
    public void messageReceived(ParameterMessage message)
    {
        Slot slot = device.getSlot(message.get("slot"));
        //int pid = message.get("pid");
        int section = message.get("section");
        
        if (section==Format.VALUE_SECTION_VOICE_AREA_POLY||section==Format.VALUE_SECTION_VOICE_AREA_COMMON) 
        {
            Patch patch = slot.getPatch();
            VoiceArea msection = section==Format.VALUE_SECTION_VOICE_AREA_POLY 
                ? patch.getPolyVoiceArea() : patch.getCommonVoiceArea();

            Module module = msection.get(message.get("module"));
            module.getParameter(message.get("parameter")).setValue(message.get("value"));
        }
    }

    public void messageReceived(ErrorMessage message) 
    {
        System.err.println("Received error message: code "+message.get("code"));
    }

}
