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
 * Created on May 17, 2006
 */
package net.sf.nmedit.nomad.synth.nord;

import javax.sound.midi.MidiDevice;

import net.sf.nmedit.jnmprotocol.PatchMessage;
import net.sf.nmedit.nomad.core.application.Const;
import net.sf.nmedit.nomad.core.nomad.NomadEnvironment;
import net.sf.nmedit.nomad.main.dialog.NomadMidiDialog;
import net.sf.nmedit.nomad.patch.builder.DirectBitStreamBuilder;
import net.sf.nmedit.nomad.patch.builder.VirtualBuilder;
import net.sf.nmedit.nomad.patch.transcoder.BitstreamTranscoder;
import net.sf.nmedit.nomad.patch.transcoder.TranscoderException;
import net.sf.nmedit.nomad.patch.virtual.Patch;

public class NordUtilities
{

    public static PatchMessage generatePatchMessage(Patch patch, int slotID) throws Exception
    {
        DirectBitStreamBuilder builder = new 
            DirectBitStreamBuilder(patch);
        builder.generate();

        PatchMessage patchMessage =
            builder.generateMessage(slotID);
        
        return patchMessage; 
        
        
        /*
        VirtualTranscoder transcoder = new VirtualTranscoder();
        BitStreamBuilder bitStreamBuilder = new BitStreamBuilder();
        transcoder.transcode(patch, bitStreamBuilder);
        
        PatchMessage patchMessage =
            bitStreamBuilder.generateMessage(slotID);
        
        return patchMessage;*/
    }

    public static void parsePatchMessage(PatchMessage message, Patch patch) throws TranscoderException
    {
        BitstreamTranscoder transcoder = new BitstreamTranscoder();
        VirtualBuilder patchBuilder = new VirtualBuilder(patch);
        transcoder.transcode(message.getPatchStream(), patchBuilder);
    }

    /*
    public static void dispatchMessage(MidiMessage m, NmProtocolListener l)
    {
        
    }*/
    
    public static boolean showMidiConfigurationDialog(AbstractNordModularDevice dev)
    {

        final String KEY_MIDI_IN_DEVICE =  Const.CUSTOM_PROPERTY_PREFIX_STRING+"synth.midi.in";
        final String KEY_MIDI_OUT_DEVICE =  Const.CUSTOM_PROPERTY_PREFIX_STRING+"synth.midi.out";

        MidiDevice.Info midiIn = dev.getIn();
        MidiDevice.Info midiOut = dev.getOut();
        
        NomadMidiDialog dlg = new NomadMidiDialog(midiIn, midiOut);
            
        if (midiIn==null) dlg.setInputDevice(NomadEnvironment.getProperty(KEY_MIDI_IN_DEVICE));
        if (midiOut==null) dlg.setOutputDevice(NomadEnvironment.getProperty(KEY_MIDI_OUT_DEVICE));

        dlg.invoke();
        
        if (dlg.isOkResult()) {
            midiIn = dlg.getInputDevice();
            midiOut = dlg.getOutputDevice();

            if (midiIn!=null && midiOut!=null)
            {
                dev.setIn(midiIn);
                dev.setOut(midiOut);
                
                NomadEnvironment.setProperty(KEY_MIDI_IN_DEVICE, midiIn.getName());
                NomadEnvironment.setProperty(KEY_MIDI_OUT_DEVICE, midiOut.getName());
                return true;
            }
        }
        
        return false;
    }
    
}
