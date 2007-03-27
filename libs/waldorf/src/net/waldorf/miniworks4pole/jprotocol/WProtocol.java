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
 * Created on Jan 15, 2007
 */
package net.waldorf.miniworks4pole.jprotocol;

import javax.sound.midi.MidiMessage;

import net.sf.nmedit.jpdl.BitStream;
import net.sf.nmedit.jpdl.Packet;
import net.sf.nmedit.jpdl.PacketParser;
import net.sf.nmedit.jpdl.Protocol;
import net.sf.nmedit.jsynth.SynthException;

public class WProtocol //extends NmProtocolST
{

    public static void setProtocol(Protocol protocol)
    {
        WProtocol.protocol = protocol;
        packetParser = protocol.getPacketParser("Message");
    }

    private static Protocol protocol;
    private static PacketParser packetParser;
    
    public PacketParser getPacketParser()
    {
        return packetParser;
    }

    public Packet parseMessage(MidiMessage message)
    {
        return parseMessage(message.getMessage());
    }

    public Packet parseMessage(byte[] message)
    {

        Packet packet = new Packet();
        boolean success = packetParser.parse(BitStream.wrap(message), packet);
        
        return success ? packet : null;
    }

    public MiniworksMidiMessage createMessage(MidiMessage message) throws SynthException
    {
        byte[] data = message.getMessage();
        Packet packet = parseMessage(data);
        
        if (packet == null)
            throw new SynthException("could not parse message: "+message+" ("+toString(message.getMessage())+")");
        
        return createMessage(packet, data);
    }
    
    private static String toString(byte[] b)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        
        if (b.length>0)
            sb.append(byte2hex(b[0]));
        
        for(int i=1;i<b.length;i++)
        {
            sb.append(",");
            sb.append(byte2hex(b[i]));
        }
        
        sb.append("]");
        return sb.toString();
    }

    private static CharSequence byte2hex(byte b)
    {
        int i = (int) (b&0xFF);
        return Integer.toHexString(i).toUpperCase();
    }

    public MiniworksMidiMessage createMessage(Packet packet, byte[] bdata) throws SynthException
    {
        int messageType = packet.getVariable("messageType");
        Packet data = packet.getPacket("data");
        if (data == null) throw new SynthException("no such packet: data");
        
        switch (messageType)
        {
            case MiniworksMidiMessage.MESSAGE_TYPE_BANKCHANGE:
            {
                int bank = data.getVariable("bank");
                return MiniworksMidiMessage.createBankChangeMessage(bank);
            }
            case MiniworksMidiMessage.MESSAGE_TYPE_CONTROLCHANGE:
            {
                int controller = data.getVariable("controller");
                int value = data.getVariable("value");
                return MiniworksMidiMessage.createControllerMessage(controller, value);
            }
            case MiniworksMidiMessage.MESSAGE_TYPE_SYSEX:
            {
                int dumpType = data.getVariable("dumpType");

                return new MiniworksMidiMessage(data, MiniworksMidiMessage.MESSAGE_TYPE_SYSEX,
                        dumpType, bdata);
            }
            case MiniworksMidiMessage.MESSAGE_TYPE_ALIVE:
            {
                return new MiniworksMidiMessage(data, MiniworksMidiMessage.MESSAGE_TYPE_ALIVE, -1,bdata);
            }
            case MiniworksMidiMessage.NOTE_ON:
                // no op TODO trigger LEDS
                return null;
            case MiniworksMidiMessage.NOTE_OFF:
                // no op
                return null;
            default:
                throw new SynthException("unknown packet: "+data+" (name="+data.getName()+")");
        }
    }

    public static void parse(MidiMessage message)
    {
        parse(message.getMessage());
    }

    private static void parse( byte[] message )
    {
        Packet packet = new Packet();
        boolean success = packetParser.parse(BitStream.wrap(message), packet);
        
        print(success, packet);
    }


    private static void print( boolean success, Packet packet )
    {
        //if (success)
        System.out.println("success="+success);
        do
        {
            for (Object v : packet.getAllVariables())
            {
                System.out.println(v+"="+packet.getVariable((String)v));
            }

            packet = packet.getPacket("data");
        } while(packet!=null);
        
    }
    
    
}
