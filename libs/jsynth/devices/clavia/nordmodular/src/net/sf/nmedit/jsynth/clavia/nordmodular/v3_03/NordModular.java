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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.sound.midi.Receiver;

import net.sf.nmedit.jnmprotocol.IAmMessage;
import net.sf.nmedit.jnmprotocol.MidiDriver;
import net.sf.nmedit.jnmprotocol.MidiMessage;
import net.sf.nmedit.jnmprotocol.NmProtocol;
import net.sf.nmedit.jpatch.spi.PatchImplementation;
import net.sf.nmedit.jsynth.SynthException;
import net.sf.nmedit.jsynth.generic.AbstractSynthesizer;
import net.sf.nmedit.jsynth.util.SelfRegulatingThread;

public class NordModular extends AbstractSynthesizer
{
    
    private final Object lock = new Object();

    private NmProtocol protocol;
    private final MidiDriver driver;
    private final SynthMessageHandler synthMessageHandler;
    private MessageProcessor messageProcessor;
    
    private Slot[] slots ;
    private int activeSlotID = 0;

    private List<SlotListener> slotListenerList
        = new ArrayList<SlotListener>();
    
    private PatchImplementation patchImplementation;
    
    public NordModular()
    {
        super( "Nord Modular", "Clavia", "3.03" );

        patchImplementation = PatchImplementation.getImplementation("Clavia Nord Modular Patch", "3.03");
        
        slots = new Slot[getSlotCount()];
        for (int i=0;i<slots.length;i++)
            slots[i] = new Slot(i, this);

        this.protocol = null;
        this.driver = new MidiDriver();
        this.synthMessageHandler = new SynthMessageHandler(this);
        this.messageProcessor = null;
    }
    
    public int getSlotCount()
    {
        return 4;
    }

    public Slot getSlot(int slotID)
    {
        return slots[slotID];
    }
    
    public int getActiveSlotID()
    {
        return activeSlotID;
    }
    
    void setActiveSlotID(int slotID)
    {
        if (slotID<0||slotID>=slots.length)
            throw new IllegalArgumentException("Slot ID out of range: "+slotID);
        
        this.activeSlotID = slotID;
    }

    public void addSlotListener( SlotListener l )
    {
        if (!slotListenerList.contains(l))
            slotListenerList.add(l);
    }
    
    public void removeSlotListener( SlotListener l )
    {   
        slotListenerList.remove(l);
    }

    public void fireSlotSelectedMessage( Slot slot )
    {
        for (int i=slotListenerList.size()-1;i>=0;i--)
            slotListenerList.get(i).slotSelected(slot);
    }

    public void fireNewPatchInSlot( Slot slot )
    {
        for (int i=slotListenerList.size()-1;i>=0;i--)
            slotListenerList.get(i).newPatchInSlot(slot);
    }
    
    public void setConnected( boolean connect ) throws SynthException
    {
        synchronized (lock)
        {
            checkPreconditions(connect);
            if (isConnected()==connect) return;
            
            if (connect)
            {
                // connect
                try
                {
                    driver.connect(getMidiIn(), getMidiOut());
                }
                catch (Exception e)
                {
                    throw new SynthException(e);
                }
                
                // create message thread 
                
                // create protocol
                protocol = new NmProtocol(driver);
                try
                {
                    protocol.send(new IAmMessage());
                }
                catch (Exception e)
                {
                    driver.disconnect();
                    protocol = null;
                    throw new SynthException(e);
                }

                protocol.addListener(synthMessageHandler);
                messageProcessor = new MessageProcessor();
                messageProcessor.start();
                
                fireSynthStateEvent();
            }
            else
            {
                messageProcessor.dieAndWait(800);

                if (messageProcessor.isAlive())
                {
                    System.err.println("thread does not stop");
                }
                
                protocol.removeListener(synthMessageHandler);
                protocol = null;
                driver.disconnect();

                fireSynthStateEvent();
            }
        }
    }

    public boolean isConnected()
    {
        return protocol != null;
    }

    public void send(MidiMessage m) throws SynthException
    {
        if (!isConnected())
        {
            throw new SynthException("Not connected");
        }
        
        synchronized(protocol)
        {
            try
            {
                protocol.send(m);
            }
            catch (Exception e)
            {
                throw new SynthException(e);
            }
            
            messageProcessor.signalPendingWork();
        }
    }
    

    private class MessageProcessor extends SelfRegulatingThread
        implements Receiver
    {
        
        private final AtomicBoolean hasWork = new AtomicBoolean(true);

        public MessageProcessor( )
        {
            super( 5000, 10 );
            setDaemon(true);
        }

        @Override
        protected boolean processMessages()
        {
            boolean hadWork = hasWork.getAndSet(false);
            
            if (protocol==null) return false;
            
            synchronized(protocol)
            {
                hadWork |= !protocol.sendQueueIsEmpty();

                try
                {
                    protocol.heartbeat();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            return hadWork;
        }

        public void send( javax.sound.midi.MidiMessage message, long timeStamp )
        {
            // wake up the thread when sleeping
            hasWork.getAndSet(true);
            signalPendingWork();
        }

        public void close()
        {
            // ignore
        }

    }


    public PatchImplementation getPatchImplementation()
    {
        return patchImplementation;
    }

    
}
