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
 * Created on Feb 20, 2006
 */
package org.nomad.synth;

import net.sf.nmedit.jnmprotocol.AckMessage;
import net.sf.nmedit.jnmprotocol.GetPatchMessage;
import net.sf.nmedit.jnmprotocol.IAmMessage;
import net.sf.nmedit.jnmprotocol.LightMessage;
import net.sf.nmedit.jnmprotocol.NewPatchInSlotMessage;
import net.sf.nmedit.jnmprotocol.NmProtocolListener;
import net.sf.nmedit.jnmprotocol.ParameterMessage;
import net.sf.nmedit.jnmprotocol.PatchListMessage;
import net.sf.nmedit.jnmprotocol.PatchMessage;
import net.sf.nmedit.jnmprotocol.RequestPatchMessage;
import net.sf.nmedit.jnmprotocol.SlotActivatedMessage;
import net.sf.nmedit.jnmprotocol.SlotsSelectedMessage;
import net.sf.nmedit.jnmprotocol.VoiceCountMessage;

import org.nomad.patch.Module;
import org.nomad.patch.ModuleSection;
import org.nomad.patch.Patch;
import org.nomad.patch.Section;
import org.nomad.patch.format.PatchBuilder;
import org.nomad.patch.format.PatchConstructionException;
import org.nomad.patch.format.PatchMessageDecoder;

public class ProtocolListener extends NmProtocolListener {

	private SynthDevice device;
	private Synth synth;

	private PatchBuilder[] task = {null,null,null,null};

	public ProtocolListener(SynthDevice device) {
		this.device = device;
		this.synth  = device.getSynth();
	}
	
	public SynthDevice getDevice() {
		return device;
	}
	
	private void requestSlots() {
		for (int i=0;i<4;i++)
			requestSlot(i);
	}
	
	private void requestSlot(int slot) {
	    task[slot] = null;
	    RequestPatchMessage requestPatchMessage;
		try {
			System.out.println("requestSlot(slot="+slot+")");
			requestPatchMessage = new RequestPatchMessage();
			requestPatchMessage.set("slot", slot);
			getDevice().send(requestPatchMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	
	public void messageReceived(LightMessage message)
	{/*
	    System.out.println("LightMessage: " +
			       "slot:" + message.get("slot") + " " +	
			       "pid:" + message.get("pid") + " " +
			       message.get("light0") +...+message.get("light19"));*/
	}
	
	public void messageReceived(PatchMessage message)
	{
		int slot = message.get("slot");
		
		PatchBuilder cons;
		
		if (task[slot]!=null) {
			cons = task[slot];
		} else {
			System.out.println("decodePatch(slot="+slot+")");
			cons = new PatchBuilder();
		}
		
		try {
			PatchMessageDecoder.decode(message, cons);
		} catch (PatchConstructionException e) {
			e.printStackTrace();
		}
		
		if (task[slot]==null) {
			synth.setSlot(slot, cons.getPatch());
			task[slot] = cons;
		}
	}
	
	public void messageReceived(AckMessage message)
	{
	    System.out.println("AckMessage: " +
			       "slot:" + message.get("slot") + " " +
			       "pid1:" + message.get("pid1") + " " +
			       "type:" + message.get("type") + " " +
			       "pid2:" + message.get("pid2"));

	    int slot = message.get("slot");
	    int pid =  message.get("pid1");
	    
	    if (pid != synth.getPId(slot)) {
	    	synth.setPId(slot, pid);
	    	
	    	GetPatchMessage msg;
			try {
				msg = new GetPatchMessage();
		    	msg.set("slot", slot);
		    	msg.set("pid", pid);
		    	// synth.setSlot(slot, new Patch());
		    	getDevice().send(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
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
	    int slot = message.get("slot");
	    requestSlot(slot);
	}
	
	public void messageReceived(VoiceCountMessage message)
	{
	    System.out.println("VoiceCountMessage: " +
			       "voiceCount0:" + message.get("voiceCount0") + " " +
			       "voiceCount1:" + message.get("voiceCount1") + " " +
			       "voiceCount2:" + message.get("voiceCount2") + " " +
			       "voiceCount3:" + message.get("voiceCount3"));	    
	}

	public void messageReceived(SlotsSelectedMessage message)
	{
	    System.out.println("SlotsSelectedMessage: " +
			       "slot0Selected:" + message.get("slot0Selected") + " " +
			       "slot1Selected:" + message.get("slot1Selected") + " " +
			       "slot2Selected:" + message.get("slot2Selected") + " " +
			       "slot3Selected:" + message.get("slot3Selected"));
	    
	    for (int i=0;i<4;i++)
	    	if (message.get("slot"+i+"Selected")==1) {
	    		synth.fireSlotSelectedMessage(synth.getSlotInfo(i));
	    		break;
	    	}
	    
	}
	
	public void messageReceived(SlotActivatedMessage message)
	{
	    synth.setActiveSlot(message.get("activeSlot"));
	}
	
	public void messageReceived(ParameterMessage message)
	{
	    int slot = message.get("slot");
	    //int pid = message.get("pid");
	    int section = message.get("section");
	    
	    if (section==Section.POLY||section==Section.COMMON) {
		    Patch patch = synth.getSlot(slot);
		    ModuleSection msection = section==Section.POLY ? patch.getPolySection() : patch.getCommonSection();
		    Module module = msection.get(message.get("module"));
		    module.getParameter(message.get("parameter"))
		    	.setValue(message.get("value"));
	    }
	}
	
	
	
}
