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
 * Created on Jan 7, 2007
 */
package net.sf.nmedit.jnmprotocol.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;

import net.sf.nmedit.jnmprotocol.IAmMessage;
import net.sf.nmedit.jnmprotocol.MessageHandler;
import net.sf.nmedit.jnmprotocol.MidiDriver;
import net.sf.nmedit.jnmprotocol.MidiMessage;
import net.sf.nmedit.jnmprotocol.NmProtocolST;
import net.sf.nmedit.jnmprotocol.ProtocolTesterHelper;

public class NmLookup
{
    
    public static void main(String[] args)
    {
        MidiDevice.Info[] devices = lookup(ProtocolTesterHelper.getHardwareDevices(), 0,
                600);

        if (devices.length>0)
        {
            System.out.println("Nord Modular detected at ");
            int index = 1;
            for (int i=0;i<devices.length;i+=2)
            {
                System.out.print(index+":");
                print(devices[i]);
                System.out.println();
                System.out.print(index+":");
                print(devices[i+1]);
                System.out.println();
                index++;
            }
        }
        else
        {
            System.out.println("Nord Modular not detected.");
        }
    }
    
    private static void print(MidiDevice.Info info)
    {
        System.out.print("[name="+info.getName()+",vendor"+info.getVendor()+",description="+info.getDescription()
                +",version="+info.getVersion());
    }
    
    private static boolean isAvailable(int max)
    {
        return max == -1 || max > 0;
    }

    public static MidiDevice.Info[] lookup(MidiDevice.Info[] devices, int maxDevices,
            long timeout)
    {
        List<MidiDevice.Info> receiverList = new ArrayList<MidiDevice.Info>(devices.length);
        List<MidiDevice.Info> transmitterList = new ArrayList<MidiDevice.Info>(devices.length);
        for (int i=0;i<devices.length;i++)
        {
            try
            {
                MidiDevice dev = MidiSystem.getMidiDevice(devices[i]);
                if (isAvailable(dev.getMaxReceivers()))
                    receiverList.add(devices[i]);
                if (isAvailable(dev.getMaxTransmitters()))
                    transmitterList.add(devices[i]);
            }
            catch (MidiUnavailableException e)
            {
                // ignore device
            }
        }
        
        if (receiverList.isEmpty() || transmitterList.isEmpty())
            return new MidiDevice.Info[0]; // no devices

        Iterator<MidiDevice.Info> iterR = receiverList.iterator();
        Iterator<MidiDevice.Info> iterT = transmitterList.iterator();

        MidiDevice.Info receiver = iterR.next();
        MidiDevice.Info transmitter = iterT.next();
        
        while (maxDevices > 0 || maxDevices <= 0)
        {
            
            MidiDriver driver = new MidiDriver(transmitter, receiver);
            
            try
            {
                if (isNordModular(driver, timeout))
                {
                    maxDevices --;
                    return new MidiDevice.Info[] {transmitter, receiver};
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            
            if (!iterT.hasNext())
            {
                iterT = transmitterList.iterator();
                if (iterR.hasNext())
                    receiver = iterR.next();
                else
                    break;
            }
            transmitter = iterT.next();
        }
        

        return new MidiDevice.Info[0]; // no devices
    }

    public static boolean isNordModular(MidiDriver driver, long timeout) throws Exception
    {
        boolean doDisconnect = false;
        if (!driver.isConnected())
        {
            driver.connect();
            doDisconnect = true;
        }

        NmProtocolST protocol = new NmProtocolST();
        IAmAcceptor acceptor = new IAmAcceptor();
        protocol.setMessageHandler(acceptor);
        protocol.getTransmitter().setReceiver(driver.getReceiver());
        driver.getTransmitter().setReceiver(protocol.getReceiver());
        
        try
        {
            protocol.send(new IAmMessage());
            
            long time = System.currentTimeMillis();
            
            while (timeout>0)
            {
                protocol.heartbeat();
                if (!acceptor.isAccepted())
                {
                    try
                    {
                        protocol.awaitWorkSignal(timeout);
                    }
                    catch (InterruptedException e)
                    {
                        // no op
                    }
                    
                    long t = System.currentTimeMillis()-time;
                    timeout-=t;
                    time = System.currentTimeMillis();
                    
                }
                else
                {
                    break;
                }
            } 
        }
        finally 
        {
            if (doDisconnect)
                driver.disconnect();
        }
        
        return acceptor.isAccepted();
    }

    private static class IAmAcceptor implements MessageHandler
    {
        
        private boolean accepted = false;

        public synchronized void processMessage( MidiMessage message )
        {
            accepted |= (message instanceof IAmMessage);
        }
        
        public synchronized boolean isAccepted()
        {
            return accepted;
        }
        
    }
    
}
