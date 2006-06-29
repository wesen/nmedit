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
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Receiver;
import javax.swing.SwingUtilities;

import net.sf.nmedit.jnmprotocol.IAmMessage;
import net.sf.nmedit.jnmprotocol.MidiDriver;
import net.sf.nmedit.jnmprotocol.MidiMessage;
import net.sf.nmedit.jnmprotocol.NmProtocol;
import net.sf.nmedit.jnmprotocol.NmProtocolListener;
import net.sf.nmedit.jpatch.spi.PatchImplementation;
import net.sf.nmedit.jsynth.SynthException;
import net.sf.nmedit.jsynth.Synthesizer;
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
    
    public static Synthesizer.Info DEVICE_INFO = new Synthesizer.Info( "Nord Modular", "Clavia", "3.03" );
    private List<NmProtocolListener> protocolListenerList = new ArrayList<NmProtocolListener>();
    
    public NordModular()
    {
        super(DEVICE_INFO);

        patchImplementation = PatchImplementation.getImplementation("Clavia Nord Modular Patch", "3.03");
        
        slots = new Slot[getSlotCount()];
        for (int i=0;i<slots.length;i++)
            slots[i] = new Slot(i, this);

        this.protocol = null;
        this.driver = new MidiDriver();
        this.synthMessageHandler = new SynthMessageHandler(this);
        this.messageProcessor = null;
    }
    
    public void addProtocolListener(NmProtocolListener l)
    {
        if (!protocolListenerList.contains(l))
        {
            protocolListenerList.add(l);
            if (isConnected() && protocol!=null)
                protocol.addListener(l);
        }
    }
    
    public void removeProtocolListener(NmProtocolListener l)
    {
        if (protocolListenerList.remove(l))
        {
            if (isConnected() && protocol!=null)
                protocol.removeListener(l);
        }
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

                messageProcessor = new MessageProcessor();
                try
                {
                    MidiDevice inDevice = MidiSystem.getMidiDevice(getMidiIn());
                    inDevice.getTransmitter().setReceiver(messageProcessor);
                    
                    // create protocol
                    protocol = new NmProcotolExtension(driver);
                    protocol.send(new IAmMessage());
                }
                catch (Exception e)
                {
                    messageProcessor = null;
                    driver.disconnect();
                    protocol = null;
                    throw new SynthException(e);
                }

                protocol.addListener(synthMessageHandler);
                
                for (NmProtocolListener l:protocolListenerList)
                    protocol.addListener(l);
                
                messageProcessor.start();
                
                fireSynthStateEvent();
            }
            else
            {
                messageProcessor.dieAndWait(800);

                if (messageProcessor.isAlive())
                {
                    messageProcessor.setExitMessage("message thread: has stopped");
                    System.err.println("message thread: not stopped");
                }
                protocol.removeListener(synthMessageHandler);
                for (NmProtocolListener l:protocolListenerList)
                    protocol.removeListener(l);
                
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

    private class NmProcotolExtension extends NmProtocol implements Runnable
    {
        
        private Queue<MidiMessage> incoming = new ConcurrentLinkedQueue<MidiMessage>();
        private AtomicBoolean isBroadcasting = new AtomicBoolean(false);
        
        public NmProcotolExtension( MidiDriver midiDriver )
        {
            super( midiDriver );
        }
        
        public void heartbeat() throws Exception
        {
            super.heartbeat();

            if ((!isBroadcasting.get()) && !incoming.isEmpty())
            {
                SwingUtilities.invokeLater(this);
            }
        }

        protected void notifyListeners(MidiMessage midiMessage)
        {
            incoming.offer(midiMessage);
        }

        public void run()
        {
            isBroadcasting.getAndSet(true);
            MidiMessage message;
            while ((message=incoming.poll())!=null)
            {
                try
                {
                    super.notifyListeners(message);
                }
                catch (Exception e)
                {
                    // TODO handle error
                    e.printStackTrace();
                }
            }
            isBroadcasting.getAndSet(false);
        }
    }

    private class MessageProcessor extends SelfRegulatingThread
        implements Receiver
    {
        
        private final AtomicBoolean hasWork = new AtomicBoolean(true);
        private String exitMessage = null;
        private final Object messageLock = new Object(); 

        public MessageProcessor( )
        {
            super( 5000, 10 );
            setDaemon(true);
        }

        public void run()
        {
            super.run();
            synchronized (messageLock)
            {
                if (exitMessage!=null)
                {
                    System.out.println(exitMessage);
                    exitMessage = null;
                }
            }
        }
        
        public void setExitMessage( String message )
        {
            synchronized (messageLock)
            {
                exitMessage = message;
            }
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
