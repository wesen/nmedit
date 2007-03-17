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
 * Created on Jan 2, 2007
 */
package net.sf.nmedit.jsynth.clavia.nordmodular;

import java.awt.EventQueue;

import javax.sound.midi.MidiUnavailableException;

import net.sf.nmedit.jnmprotocol.DebugProtocol;
import net.sf.nmedit.jnmprotocol.IAmMessage;
import net.sf.nmedit.jnmprotocol.MessageMulticaster;
import net.sf.nmedit.jnmprotocol.MidiDriver;
import net.sf.nmedit.jnmprotocol.MidiException;
import net.sf.nmedit.jnmprotocol.NmProtocol;
import net.sf.nmedit.jnmprotocol.NmProtocolListener;
import net.sf.nmedit.jnmprotocol.NmProtocolMT;
import net.sf.nmedit.jnmprotocol.NmProtocolST;
import net.sf.nmedit.jnmprotocol.utils.ProtocolRunner;
import net.sf.nmedit.jnmprotocol.utils.ProtocolThreadExecutionPolicy;
import net.sf.nmedit.jnmprotocol.utils.StoppableThread;
import net.sf.nmedit.jnmprotocol.utils.ProtocolRunner.ProtocolErrorHandler;
import net.sf.nmedit.jpatch.clavia.nordmodular.NM1ModuleDescriptions;
import net.sf.nmedit.jsynth.AbstractSynthesizer;
import net.sf.nmedit.jsynth.Bank;
import net.sf.nmedit.jsynth.MidiPortSupport;
import net.sf.nmedit.jsynth.SlotManager;
import net.sf.nmedit.jsynth.SynthException;
import net.sf.nmedit.jsynth.Synthesizer;
import net.sf.nmedit.jsynth.clavia.nordmodular.worker.Scheduler;
import net.sf.nmedit.jsynth.midi.MidiPort;

public class NordModular extends AbstractSynthesizer implements Synthesizer
{

    private NmProtocol protocol;
    private StoppableThread protocolThread;
    private boolean connected = false;
    private MessageMulticaster multicaster;
    private MidiDriver midiDriver;
    private MidiPortSupport midiports;
    private boolean ignoreErrors = false;
    private Scheduler scheduler;
    private NmMessageHandler messageHander;
    private NM1ModuleDescriptions moduleDescriptions;

    private NmSlotManager slotManager;
    private int maxSlotCount = 4;
    
    public NordModular(NM1ModuleDescriptions moduleDescriptions)
    {
        this(moduleDescriptions, false);
    }
    
    public int getMaxSlotCount()
    {
        return maxSlotCount;
    }
    
    public void setMaxSlotCount(int slotCount)
    {
        this.maxSlotCount = Math.max(1, Math.min(4, slotCount));
    }
    
    public NM1ModuleDescriptions getModuleDescriptions()
    {
        return moduleDescriptions;
    }
    
    public Scheduler getScheduler()
    {
        return scheduler;
    }
    
    public boolean isMicroModular()
    {
        return getSlotCount() == 1;
    }
    
    public NordModular(NM1ModuleDescriptions moduleDescriptions, boolean debug)
    {
        this.moduleDescriptions = moduleDescriptions;
        slotManager = new NmSlotManager(this);
        
        midiports = new MidiPortSupport(this, "pc-in", "pc-out");
        
        multicaster = new MessageMulticaster();
        protocol = new SchedulingProtocolMT(new NmProtocolST());
        
        if (debug)
            protocol = new DebugProtocol(protocol);
        
        protocol.setMessageHandler(multicaster);

        messageHander = new NmMessageHandler(this);
        addProtocolListener(messageHander);
        
        scheduler = new Scheduler(protocol);
        
        protocolThread = new StoppableThread(new ProtocolThreadExecutionPolicy(protocol), 
                new ProtocolRunner(protocol, new Nm1ProtocolErrorHandler(this)));
    }

    private class SchedulingProtocolMT extends NmProtocolMT
    {
        public SchedulingProtocolMT(NmProtocol protocol)
        {
            super(protocol);
        }

        public void heartbeat() throws MidiException
        {
            
            try
            {
            scheduler.schedule();
            }
            catch (SynthException e)
            {
                MidiException me = new MidiException(e.getMessage(), -1);
                me.initCause(e);
                
                throw me;
            }
            super.heartbeat();
        }
    }
    
    public String getVendor()
    {
        return "Clavia";
    }
    
    public String getName()
    {
        return getDeviceName();
    }

    public String getDeviceName()
    {
        return "Nord Modular";
    }

    private MidiDriver createMidiDriver() throws SynthException
    {
        midiports.validatePlugs();
        
        return new MidiDriver(midiports.getInPlug().getDeviceInfo(),
                midiports.getOutPlug().getDeviceInfo());
    }
    
    private void connect() throws SynthException
    {
        midiDriver = createMidiDriver();
        try
        {
            midiDriver.connect();
        }
        catch (MidiUnavailableException e)
        {
            throw new SynthException(e);
        }
        
        try
        {
            midiDriver.getTransmitter().setReceiver(protocol.getReceiver());
            protocol.getTransmitter().setReceiver(midiDriver.getReceiver());
        }
        catch (Throwable t)
        {
            throw new SynthException(t);
        }

        protocol.reset();
        
        IAMAcceptor acceptor = new IAMAcceptor();
        try
        {
            multicaster.addProtocolListener(acceptor);

            try
            {
                protocol.send(new IAmMessage());
            }
            catch (Exception e)
            {
                throw new SynthException(e);
            }
            
            final long timeout = 3000;
            final long timeoutThreshold = System.currentTimeMillis()+timeout;
            
            while (!acceptor.IAMMessageReceived())
            {
                if (System.currentTimeMillis()>timeoutThreshold)
                {
                    throw new SynthException("time out: "+timeout+"ms");
                }
                
                try
                {
                    // Important: heartbeat must be executed here rather than in the
                    // separate thread. The NmProtocolMT implementation posts
                    // messages in the same thread like this code is executed.
                    // Thus the IAmMessage will only arrive after this method
                    // has returned with the timeout exception.
                    protocol.heartbeat();
                }
                catch (Exception e1)
                {
                    e1.printStackTrace();
                }
                
                try
                {
                    protocol.awaitWorkSignal(100);
                }
                catch (InterruptedException e)
                {
                    // no op
                }
            }
            
            final IAmMessage iam = acceptor.getIAMMessage();
            final int vlow = iam.get("versionLow");
            final int vhigh = iam.get("versionHigh");
            if (vhigh!=3 || vlow!=3)
                throw new SynthException("Unsupported firmware version: "+vhigh+"."+vlow+" ("+iam+")");

            setConnectedFlag(true);
        }
        catch (SynthException e)
        {
            disconnect();
            throw e;
        }
        finally
        {
            multicaster.removeProtocolListener(acceptor);
        }
        
        // now everything is fine - start the protocol thread
        protocolThread.start();
    }
    
    private void disconnect()
    {
        protocolThread.stop();
        midiDriver.disconnect();
        protocol.reset();
        setConnectedFlag(false);
    }
    
    public NmProtocol getProtocol()
    {
        return protocol;
    }

    private void setConnectedFlag(boolean connected)
    {
        if (this.connected != connected)
        {
            this.connected = connected;
            fireSynthesizerStateChanged();
        }
    }
    
    protected void fireSynthesizerStateChanged()
    {
        if (isConnected())
            connected();
        else
            disconnected();
        
        super.fireSynthesizerStateChanged();
    }
    
    private void connected()
    {
        scheduler.clear();
        
        // TODO check if synthesizer is Micro Modular (1 slot) or not (4 slots)
        NmSlot[] slots = new NmSlot[maxSlotCount];
        for (int i=0;i<slots.length;i++)
            slots[i] = new NmSlot(this, i);
        slotManager.setSlots(slots);
    }

    private void disconnected()
    {
        scheduler.clear();
        
        slotManager.setSlots(new NmSlot[0]);
    }

    public void setConnected( boolean connected ) throws SynthException
    {
        if (this.connected != connected)
        {
            if (!connected)
            {
                disconnect();
            }
            else
            {
                connect();
            }
        }
    }

    public boolean isConnected()
    {
        return connected;
    }

    private static class IAMAcceptor extends NmProtocolListener
    {

        private IAmMessage iamMessage = null;
        
        public boolean IAMMessageReceived()
        {
            return getIAMMessage() != null;
        }
        
        public IAmMessage getIAMMessage()
        {
            return iamMessage;
        }
        
        public void messageReceived(IAmMessage message) 
        {
            this.iamMessage = message;
        }        
        
    }
    
    private static class Nm1ProtocolErrorHandler extends ProtocolErrorHandler implements Runnable
    {
        private NordModular nm1;

        public Nm1ProtocolErrorHandler( NordModular nm1 )
        {
            this.nm1 = nm1;
        }

        public void handleError(Throwable t) throws Throwable
        {
            if (nm1.isIgnoreErrorsEnabled())
                t.printStackTrace();
            else
            {
                EventQueue.invokeLater(this);
                throw t;
            }
        }
        
        public void run()
        {
            try
            {
                nm1.setConnected(false);
            }
            catch (SynthException e)
            {
                // no op
            }
        }
    }

    public void addProtocolListener( NmProtocolListener l )
    {
        multicaster.addProtocolListener(l);
    }

    public void removeProtocolListener( NmProtocolListener l )
    {
        multicaster.removeProtocolListener(l);
    }

    public void setIgnoreErrorsEnabled( boolean ignoreErrors )
    {
        this.ignoreErrors = ignoreErrors;
    }

    public boolean isIgnoreErrorsEnabled()
    {
        return ignoreErrors;
    }

    public MidiPort[] getPorts()
    {
        return midiports.toArray();
    }
    
    public MidiPort getPCInPort()
    {
        return midiports.getInPort();
    }
    
    public MidiPort getPCOutPort()
    {
        return midiports.getOutPort();
    }
    
    public Bank[] getBanks()
    {
        return new Bank[0];
    }

    public MidiPort getPort( int index )
    {
        return midiports.getPort(index);
    }

    public Bank getBank( int index )
    {
        throw new IndexOutOfBoundsException();
    }

    public NmSlot getSlot( int index )
    {
        return slotManager.getSlot(index);
    }

    public int getPortCount()
    {
        return midiports.getPortCount();
    }

    public int getBankCount()
    {
        return 0;
    }

    public int getSlotCount()
    {
        return slotManager.getSlotCount();
    }

    public SlotManager getSlotManager()
    {
        return slotManager;
    }

    NmSlotManager getNmSlotManager()
    {
        return slotManager;
    }

}


