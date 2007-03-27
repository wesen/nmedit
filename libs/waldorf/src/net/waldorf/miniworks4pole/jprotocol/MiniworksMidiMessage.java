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
package net.waldorf.miniworks4pole.jprotocol;

import java.util.Arrays;

import javax.sound.midi.MidiMessage;

import net.sf.nmedit.jpdl.BitStream;
import net.sf.nmedit.jpdl.Packet;

public class MiniworksMidiMessage
{

    // message types
    public static final int MESSAGE_TYPE_BANKCHANGE = 0xC0;
    public static final int MESSAGE_TYPE_CONTROLCHANGE = 0xB0;
    public static final int MESSAGE_TYPE_SYSEX = 0xF0;
    public static final int MESSAGE_TYPE_ALIVE = 0xFE;
    public static final int NOTE_OFF = 0x80;
    public static final int NOTE_ON = 0x90;

    // message type SYSEX: dump types
    public static final int DUMP_TYPE_PROGRAM_DUMP = 0x00;
    public static final int DUMP_TYPE_PROGRAM_BULK_DUMP = 0x01;
    public static final int DUMP_TYPE_ALL_DUMP = 0x08;

    public static final int DUMP_TYPE_PROGRAM_DUMP_REQUEST = 0x40;
    public static final int DUMP_TYPE_PROGRAM_BULK_DUMP_REQUEST = 0x41;
    public static final int DUMP_TYPE_ALL_DUMP_REQUEST = 0x48;

    // controller numbers
    public static final int CN_MODWHEEL = 0x01;
    public static final int CN_BREATH_CONTROLLER = 0x02;
    public static final int CN_SIGNAL_ENVELOPE = CN_BREATH_CONTROLLER;
    
    public static final int CN_VOLUME_PARAMETER = 0x09;
    public static final int CN_PANNING_PARAMETER = 0x0A;
    public static final int CN_VCF_ENV_ATTACK = 0x0E;
    public static final int CN_VCF_ENV_DECAY = 0x0F;
    public static final int CN_VCF_ENV_SUSTAIN = 0x10;
    public static final int CN_VCF_ENV_RELEASE = 0x11;

    public static final int CN_VCA_ENV_ATTACK = 0x12;
    public static final int CN_VCA_ENV_DECAY = 0x13;
    public static final int CN_VCA_ENV_SUSTAIN = 0x14;
    public static final int CN_VCA_ENV_RELEASE = 0x15;
    public static final int CN_VCF_ENV_CUTOFF_AMOUNT = 0x16;
    public static final int CN_VCA_ENV_VOLUME_AMOUNT = 0x17;
    public static final int CN_LFO_SPEED = 0x18;
    public static final int CN_LFO_SHAPE = 0x19;
    public static final int CN_LFO_SPEED_MOD_AMOUNT = 0x1A;
    public static final int CN_LFO_SPEED_MOD_SOURCE = 0x1B;
    public static final int CN_SUSTAIN_SWITCH = 0x40;
    public static final int CN_CUTOFF_MOD_AMOUNT = 0x46;
    public static final int CN_CUTOFF_MOD_SOURCE = 0x47;
    public static final int CN_RESONANCE_MOD_AMOUNT = 0x48;
    public static final int CN_RESONANCE_MOD_SOURCE = 0x49;
    public static final int CN_VOLUME_MOD_AMOUNT = 0x4A;
    public static final int CN_VOLUME_MOD_SOURCE = 0x2B;
    public static final int CN_PANNING_MOD_AMOUNT = 0x4C;
    public static final int CN_PANNING_MOD_SOURCE = 0x2D;
    public static final int CN_CUTOFF_FREQUENCY = 0x4E;
    public static final int CN_RESONANCE = 0x4F;
    public static final int CN_GATE_TIME = 0x50;
    public static final int CN_TRIGGER_SOURCE = 0x51;
    public static final int CN_TRIGGER_MODE = 0x52;
    public static final int CN_RESET_ALL_CONTROLLERS = 0x79;
    public static final int CN_ALL_NOTES_OFF = 0x7B;
    
    private Packet packet;
    private int messageType;
    private int dumpType = -1;
    private byte[] data;

    MiniworksMidiMessage(Packet packet, int messageType, int dumpType, byte[] bdata)
    {
        this.packet = packet;
        this.messageType = messageType;
        this.dumpType = dumpType;
        this.data = bdata;
    }

    public int getController()
    {
        return getData()[1];
    }

    public int getControllerValue()
    {
        return getData()[2];
    }

    public int getBank()
    {
        return getData()[1];
    }
    
    public Packet getPacket()
    {
        return packet;
    }
    
    protected MiniworksMidiMessage(int messageType)
    {
        this.messageType = messageType;
    }

    protected MiniworksMidiMessage(int messageType, int dumpType)
    {
        this.messageType = messageType;
        this.dumpType = dumpType;
    }

    public final int getMessageType()
    {
        return messageType;
    }

    public final int getDumpType()
    {
        return dumpType;
    }
    
    public static MiniworksMidiMessage createAllDumpRequestMessage(int deviceId)
    {
        return new Request(DUMP_TYPE_ALL_DUMP_REQUEST, deviceId, -1);
    }
    
    public static MiniworksMidiMessage createProgramDumpRequestMessage(int deviceId, int program)
    {
        return new Request(DUMP_TYPE_PROGRAM_DUMP_REQUEST, deviceId, program);
    }
    
    public static MiniworksMidiMessage createProgramBulkDumpRequestMessage(int deviceId, int program)
    {
        return new Request(DUMP_TYPE_PROGRAM_BULK_DUMP_REQUEST, deviceId, program);
    }
    
    public static MiniworksMidiMessage createControllerMessage(int controller, int value)
    {
        return new ConstMessage(MESSAGE_TYPE_CONTROLCHANGE, controller, value);
    }
    
    public static MiniworksMidiMessage createBankChangeMessage(int bank)
    {
        return new ConstMessage(MESSAGE_TYPE_BANKCHANGE, bank);
    }
    
    public byte[] getData()
    {
        return data;
    }
    
    public MidiMessage createMidiMessage()
    {
        return new SimpleMidiMessage(getData());
    }
    
    private static class ConstMessage extends MiniworksMidiMessage
    {

        private int[] message;

        protected ConstMessage(int ... message)
        {
            super(message[0]);
            this.message = message;
        }

        @Override
        public byte[] getData()
        {
            BitStream bs = new BitStream();
            for (int i=0;i<message.length;i++)
                bs.append(message[i], 8);
            return bs.toByteArray();
        }
        
    }
    
    private static class Request extends MiniworksMidiMessage
    {

        private int programNo;
        private int deviceId;
        private int dumpType;

        protected Request(int dumpType, int deviceId, int programNo)
        {
            super(MESSAGE_TYPE_SYSEX, dumpType);
            this.deviceId = deviceId;
            this.programNo = programNo;
        }

        @Override
        public byte[] getData()
        {
            BitStream bs = new BitStream(7);
            bs.append(0xF0, 8);
            bs.append(0x3E, 8);
            bs.append(0x04, 8);
            bs.append(deviceId, 8);
            bs.append(dumpType, 8);
            if (dumpType != DUMP_TYPE_ALL_DUMP)
                bs.append(programNo, 8);
            bs.append(0xF7, 8);
            return bs.toByteArray();
        }
        
    }
    
    public String toString()
    {
        return getClass().getName()+" "+Arrays.toString(getData());
    }

    public MidiMessage getMidiMessage()
    {
        return new SimpleMidiMessage(getData());
    }
    
}

