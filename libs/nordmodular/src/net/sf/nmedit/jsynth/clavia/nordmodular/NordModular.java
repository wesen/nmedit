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
import java.util.HashMap;
import java.util.Map;

import javax.sound.midi.MidiUnavailableException;

import net.sf.nmedit.jnmprotocol.DebugProtocol;
import net.sf.nmedit.jnmprotocol.IAmMessage;
import net.sf.nmedit.jnmprotocol.MessageMulticaster;
import net.sf.nmedit.jnmprotocol.MidiDriver;
import net.sf.nmedit.jnmprotocol.MidiException;
import net.sf.nmedit.jnmprotocol.NmMessageAcceptor;
import net.sf.nmedit.jnmprotocol.NmProtocol;
import net.sf.nmedit.jnmprotocol.NmProtocolListener;
import net.sf.nmedit.jnmprotocol.NmProtocolMT;
import net.sf.nmedit.jnmprotocol.NmProtocolST;
import net.sf.nmedit.jnmprotocol.RequestSynthSettingsMessage;
import net.sf.nmedit.jnmprotocol.SynthSettingsMessage;
import net.sf.nmedit.jnmprotocol.utils.ProtocolRunner;
import net.sf.nmedit.jnmprotocol.utils.ProtocolThreadExecutionPolicy;
import net.sf.nmedit.jnmprotocol.utils.StoppableThread;
import net.sf.nmedit.jnmprotocol.utils.ProtocolRunner.ProtocolErrorHandler;
import net.sf.nmedit.jpatch.clavia.nordmodular.NM1ModuleDescriptions;
import net.sf.nmedit.jsynth.AbstractSynthesizer;
import net.sf.nmedit.jsynth.Bank;
import net.sf.nmedit.jsynth.DefaultMidiPorts;
import net.sf.nmedit.jsynth.MidiPortSupport;
import net.sf.nmedit.jsynth.SlotManager;
import net.sf.nmedit.jsynth.SynthException;
import net.sf.nmedit.jsynth.Synthesizer;
import net.sf.nmedit.jsynth.clavia.nordmodular.worker.Scheduler;
import net.sf.nmedit.jsynth.midi.MidiPort;

public class NordModular extends AbstractSynthesizer implements Synthesizer, DefaultMidiPorts
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
    
    private final static String DEVICE_NAME = "Nord Modular";
    private String name = DEVICE_NAME;

    private boolean settingsChangedFlag = false;
    private boolean settingsInSync = true;

    // false, true = external, internal
    private Property midiClockSource = new Property("midiClockSource", true);
    // false, true = active, inactive
    private Property ledsActive = new Property("ledsActive", true);
    // false, true = local on, local off
    private Property localOn = new Property("localOn", false);
    // false, true = active slot, selected slots
    private Property keyboardMode = new Property("keyboardMode", false);
    // false, true = normal, inverted
    private Property pedalPolarity = new Property("pedalPolarity", false);
    // print value = (value+1)
    private Property globalSync = new Property("globalSync", 0, 31, 0);
    // value range: -127..0..127
    private Property masterTune = new Property("masterTune", -127, 127, 0, true);
    // false, true = immediate, hook 
    private Property knobMode = new Property("knobMode", false);
    // other properties
    private Property programChangeReceive = new Property("programChangeReceive", true);
    private Property programChangeSend = new Property("programChangeSend", true);
    private Property midiVelScaleMin = new Property("midiVelScaleMin", 0, 127, 0);
    private Property midiVelScaleMax = new Property("midiVelScaleMax", 0, 127, 127);
    private Property midiClockBpm = new Property("midiClockBpm", 31, 239, 120);
    private Property[] slotMidiChannels = 
    {
        new Property("midiChannelSlot0", 0, 16, 0),
        new Property("midiChannelSlot1", 0, 16, 1),
        new Property("midiChannelSlot2", 0, 16, 2),
        new Property("midiChannelSlot3", 0, 16, 3)      
    };
    private Property[] slotEnabled = 
    {
        new Property("slot0Selected", false),
        new Property("slot1Selected", false),
        new Property("slot2Selected", false),
        new Property("slot3Selected", false)      
    };
    private Property[] slotVoiceCount = 
    {
        new Property("slot0VoiceCount", 0, 255, 0),
        new Property("slot1VoiceCount", 0, 255, 0),
        new Property("slot2VoiceCount", 0, 255, 0),
        new Property("slot3VoiceCount", 0, 255, 0) 
    };
    private Property activeSlot = new Property("activeSlot", 0, 3, 0);

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
        return name;
    }

    public String getDeviceName()
    {
        return DEVICE_NAME;
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
        
        NmMessageAcceptor<IAmMessage> iamAcceptor = new NmMessageAcceptor<IAmMessage>(IAmMessage.class);

        NmMessageAcceptor<SynthSettingsMessage> settingsAcceptor = 
            new NmMessageAcceptor<SynthSettingsMessage>(SynthSettingsMessage.class);
        
        try
        {
            multicaster.addProtocolListener(iamAcceptor);

            try
            {
                protocol.send(new IAmMessage());
            }
            catch (Exception e)
            {
                throw new SynthException(e);
            }

            final long timeout = 3000;
            iamAcceptor.waitForReply(protocol, timeout);
            
            validateVersion(iamAcceptor.getFirstMessage(), 3, 3);
            setConnectedFlag(true);

            // request synth settings

            multicaster.addProtocolListener(settingsAcceptor);
            try
            {
                protocol.send(new RequestSynthSettingsMessage());
            }
            catch (Exception e)
            {
                throw new SynthException("Request synth settings failed.", e);
            }
            
            settingsAcceptor.waitForReply(protocol, timeout);
            setSettings(settingsAcceptor.getFirstMessage());
        }
        catch (SynthException e)
        {
            disconnect();
            throw e;
        }
        catch (Exception e)
        {
            disconnect();
            throw new SynthException(e);
        }
        finally
        {
            multicaster.removeProtocolListener(settingsAcceptor);
            multicaster.removeProtocolListener(iamAcceptor);
        }
        
        // now everything is fine - start the protocol thread
        protocolThread.start();
    }
    
    public boolean getMidiClockSource()
    {
        return midiClockSource.getBooleanValue();
    }

    public void setMidiClockSource(boolean internal)
    {
        midiClockSource.setValue(internal);
    }

    public int getMidiVelScaleMin()
    {
        return midiVelScaleMin.getValue();
    }

    public void setMidiVelScaleMin(int value)
    {
        midiVelScaleMin.setValue(value);
    }

    public int getMidiVelScaleMax()
    {
        return midiVelScaleMax.getValue();
    }

    public void setMidiVelScaleMax(int value)
    {
        midiVelScaleMax.setValue(value);
    }

    public boolean isLEDsActive()
    {
        return ledsActive.getBooleanValue();
    }

    public void setLEDsActive(boolean value)
    {
        ledsActive.setValue(value);
    }

    public int getMidiClockBPM()
    {
        return midiClockBpm.getValue();
    }

    public void setMidiClockBPM(int bpm)
    {
        midiClockBpm.setValue(bpm);
    }

    public boolean isLocalOn()
    {
        return localOn.getBooleanValue();
    }

    public void setLocalOn(boolean on)
    {
        this.localOn.setValue(on);
    }

    public boolean getKeyboardMode()
    {
        return keyboardMode.getBooleanValue();
    }

    public void setKeyboardMode(boolean selectedSlots)
    {
        keyboardMode.setValue(selectedSlots);
    }

    public boolean getPedalPolarity()
    {
        return pedalPolarity.getBooleanValue();
    }

    public void setPedalPolarity(boolean inverted)
    {
        pedalPolarity.setValue(inverted);
    }
    
    public int getGlobalSync()
    {
        return globalSync.getValue();
    }

    public void setGlobalSync(int value)
    {
        globalSync.setValue(value);
    }

    public int getMasterTune()
    {
        return masterTune.getValue();
    }

    public void setMasterTune(int value)
    {
        masterTune.setValue(value);
    }

    public boolean getProgramChangeSend()
    {
        return programChangeSend.getBooleanValue();
    }

    public void setProgramChangeSend(boolean enabled)
    {
        programChangeSend.setValue(enabled);
    }

    public boolean getProgramChangeReceive()
    {
        return programChangeReceive.getBooleanValue();
    }

    public void setProgramChangeReceive(boolean enabled)
    {
        programChangeReceive.setValue(enabled);
    }

    public boolean getKnobMode()
    {
        return knobMode.getBooleanValue();
    }

    public void setKnobMode(boolean hook)
    {
        knobMode.setValue(hook);
    }
    
    private void checkSlot(int slot)
    {
        if (slot>slotManager.getSlotCount() || slot<0)
            throw new IndexOutOfBoundsException("invalid slot index: "+slot);
    }

    public int getMidiChannel(int slot)
    {
        checkSlot(slot);
        return slotMidiChannels[slot].getValue();
    }
    
    public void setMidiChannel(int slot, int channel)
    {
        checkSlot(slot);
        slotMidiChannels[slot].setValue(channel);
    }

    public boolean isSlotEnabled(int slot)
    {
        checkSlot(slot);
        return slotEnabled[slot].getBooleanValue();
    }
    
    public void setSlotEnabled(int slot, boolean enable)
    {
        checkSlot(slot);
        slotEnabled[slot].setValue(enable);
    }
    
    public int getVoiceCount(int slot)
    {
        checkSlot(slot);
        return slotVoiceCount[slot].getValue();
    }
    
    public void setVoiceCount(int slot, int voiceCount)
    {
        checkSlot(slot);
        slotVoiceCount[slot].setValue(voiceCount);
    }

    public int getActiveSlot()
    {
        return activeSlot.getValue();
    }

    public void getActiveSlot(int value)
    {
        activeSlot.setValue(value);
    }
    
    public void setName(String name)
    {
        String oldName = this.name;
        if (name == null)
        {
            name = "";
        }
        else if (name.length()>16)
            name = name.substring(0, 16);
        
        if (oldName == null || (!name.equals(oldName)))
        {
            this.name = name;
            settingsChangedFlag = true;
            firePropertyChange("name", oldName, name);
        }
    }
    
    public void setSettings(SynthSettingsMessage message)
    {
        Map<String, Object> settings = message.getParamMap();
        setName((String) settings.get("name"));
        midiClockSource.readValue(settings);
        midiVelScaleMin.readValue(settings);
        midiVelScaleMax.readValue(settings);
        ledsActive.readValue(settings);
        midiClockBpm.readValue(settings);
        localOn.readValue(settings);
        keyboardMode.readValue(settings);
        pedalPolarity.readValue(settings);
        globalSync.readValue(settings);
        masterTune.readValue(settings);
        programChangeSend.readValue(settings);
        programChangeReceive.readValue(settings);
        knobMode.readValue(settings);

        for (int i=0;i<4;i++)
        {
            slotMidiChannels[i].readValue(settings);
        }

        if (message.containsExtendedSettings())
        {
            for (int i=0;i<slotManager.getSlotCount();i++)
            {
                boolean disabled = i>=slotManager.getSlotCount();
                slotEnabled[i].readValue(settings, disabled);
                slotVoiceCount[i].readValue(settings, disabled);
            }
            // TODO check if slot is available
            activeSlot.readValue(settings);
        }
        
        if (settingsChangedFlag)
        {
            settingsChangedFlag = false;
            firePropertyChange("settings", null, "settings");
        }
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
    
    private SynthSettingsMessage createSettingsMessage() throws Exception
    {
        Map<String, Object> settings = new HashMap<String, Object>();

        settings.put("name", getName());
        midiClockSource.putValue(settings);
        midiVelScaleMin.putValue(settings);
        midiVelScaleMax.putValue(settings);
        ledsActive.putValue(settings);
        midiClockBpm.putValue(settings);
        localOn.putValue(settings);
        keyboardMode.putValue(settings);
        pedalPolarity.putValue(settings);
        globalSync.putValue(settings);
        masterTune.putValue(settings);
        programChangeSend.putValue(settings);
        programChangeReceive.putValue(settings);
        knobMode.putValue(settings);

        for (int i=0;i<4;i++)
        {
            slotMidiChannels[i].putValue(settings);
        }

        return new SynthSettingsMessage(settings);
    }
    
    public void sendSettings()
    {
        if (isConnected())
        {
            try
            {
                protocol.send(createSettingsMessage());
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
            settingsInSync = true;
        }
    }

    public void syncSettings()
    {
        EventQueue.invokeLater(new Runnable(){
            public void run()
            {
                syncSettingsImmediatelly();  
            }
        });
    }

    public void syncSettingsImmediatelly()
    {
        if ((!settingsInSync) && isConnected())
        {
            sendSettings();
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
    
    private void validateVersion(IAmMessage msg, int versionLow, int versionHigh)
        throws SynthException
    {
        int msgVersionLow = msg.get("versionLow");
        int msgVersionHigh = msg.get("versionHigh");
        
        if (msgVersionLow != versionLow || msgVersionHigh != versionHigh)
        {
            throw new SynthException("Unsupported OS version: "
                    +msgVersionHigh+"."+msgVersionLow
                    +" (expected "
                    +versionHigh+"."+versionLow
                    +")");
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

    public SlotManager<NmSlot> getSlotManager()
    {
        return slotManager;
    }

    NmSlotManager getNmSlotManager()
    {
        return slotManager;
    }

    public MidiPort getDefaultMidiInPort()
    {
        return getPCInPort();
    }

    public MidiPort getDefaultMidiOutPort()
    {
        return getPCOutPort();
    }
    
    private class Property
    {
        private String propertyName;
        private int minValue;
        private int maxValue;
        private int value;
        private boolean isBooleanProperty = false;
        private boolean signedByte = false;

        public Property(String propertyName, boolean defaultValue)
        {
            this(propertyName, 0, 1, defaultValue ? 1 : 0);
            this.isBooleanProperty = true;
        }

        public Property(String propertyName, int minValue, int maxValue, int defaultValue, boolean signedByte)
        {
            this(propertyName, minValue, maxValue, defaultValue);
            this.signedByte = signedByte;
        }
        
        public Property(String propertyName, int minValue, int maxValue, int defaultValue)
        {
            this.propertyName = propertyName;
            this.minValue = minValue;
            this.maxValue = maxValue;
            this.value = defaultValue;
        }

        public void setValue(boolean value)
        {
            setValue(value ? 1 : 0);
        }
        
        public boolean getBooleanValue()
        {
            return value > 0;
        }
        
        public void setValue(int value)
        {
            value = Math.max(minValue, Math.min(value, maxValue));
            int oldValue = this.value;
            
            if (oldValue != value)
            {
                this.value = value;
                
                NordModular.this.settingsChangedFlag = true;
                NordModular.this.settingsInSync = false;
                
                if (isBooleanProperty)
                {
                    NordModular.this.firePropertyChange(propertyName, oldValue>0, value>0);
                }
                else
                {
                    NordModular.this.firePropertyChange(propertyName, oldValue, value);
                }
            }
        }

        public int getValue()
        {
            return value;
        }
        
        protected void putValue(Map<String, Object> settings)
        {
            int internal = (signedByte) ? (((byte)value)&0xFF) : value;
            settings.put(propertyName, internal);
        }
        
        protected void readValue(Map<String, Object> settings)
        {
            readValue(settings, false);
        }
        
        protected void readValue(Map<String, Object> settings, boolean zero)
        {
            try
            {
                int newValue = 0;
                
                if (!zero)
                {
                    newValue = ((Integer) settings.get(propertyName)).intValue();
                    if (signedByte)
                    { 
                        // cast to signed byte, then back to int
                        newValue = (byte) newValue;
                    }
                }
                
                setValue( newValue );
            }
            catch (NullPointerException e)
            {
                // ignore
            }
            catch (ClassCastException e)
            {
                // ignore
            }
        }
        
    }

}


