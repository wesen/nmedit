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

import net.sf.nmedit.jnmprotocol2.ActivePidListener;
import net.sf.nmedit.jnmprotocol2.ErrorMessage;
import net.sf.nmedit.jnmprotocol2.IAmMessage;
import net.sf.nmedit.jnmprotocol2.MessageMulticaster;
import net.sf.nmedit.jnmprotocol2.MidiDriver;
import net.sf.nmedit.jnmprotocol2.MidiException;
import net.sf.nmedit.jnmprotocol2.MidiMessage;
import net.sf.nmedit.jnmprotocol2.NmMessageAcceptor;
import net.sf.nmedit.jnmprotocol2.NmProtocol;
import net.sf.nmedit.jnmprotocol2.NmProtocolListener;
import net.sf.nmedit.jnmprotocol2.RequestSynthSettingsMessage;
import net.sf.nmedit.jnmprotocol2.SlotActivatedMessage;
import net.sf.nmedit.jnmprotocol2.SynthSettingsMessage;
import net.sf.nmedit.jnmprotocol2.utils.ProtocolRunner;
import net.sf.nmedit.jnmprotocol2.utils.ProtocolThreadExecutionPolicy;
import net.sf.nmedit.jnmprotocol2.utils.QueueBuffer;
import net.sf.nmedit.jnmprotocol2.utils.StoppableThread;
import net.sf.nmedit.jnmprotocol2.utils.ProtocolRunner.ProtocolErrorHandler;
import net.sf.nmedit.jpatch.clavia.nordmodular.NM1ModuleDescriptions;
import net.sf.nmedit.jsynth.AbstractSynthesizer;
import net.sf.nmedit.jsynth.ComStatus;
import net.sf.nmedit.jsynth.DefaultMidiPorts;
import net.sf.nmedit.jsynth.MidiPortSupport;
import net.sf.nmedit.jsynth.SlotManager;
import net.sf.nmedit.jsynth.SynthException;
import net.sf.nmedit.jsynth.Synthesizer;
import net.sf.nmedit.jsynth.clavia.nordmodular.worker.NMStorePatchWorker;
import net.sf.nmedit.jsynth.clavia.nordmodular.worker.ScheduledMessage;
import net.sf.nmedit.jsynth.clavia.nordmodular.worker.Scheduler;
import net.sf.nmedit.jsynth.midi.MidiPort;
import net.sf.nmedit.jsynth.worker.StorePatchWorker;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class NordModular extends AbstractSynthesizer implements Synthesizer, DefaultMidiPorts
{
    
    private final static Log log = LogFactory.getLog(NordModular.class);

    private double dspGlobal = 0;
    
    private NmProtocol protocol;
    private StoppableThread protocolThread;
    private boolean connected = false;
    private ComStatus comStatus = ComStatus.Offline;
    private MessageMulticaster multicaster;
    private MidiDriver midiDriver;
    private MidiPortSupport midiports;
    private boolean ignoreErrors = false;
    private Scheduler scheduler;
    private NmMessageHandler messageHander;
    private NM1ModuleDescriptions moduleDescriptions;

    private NmSlotManager slotManager;
 //   private int maxSlotCount = 4;
    
    private int deviceId = -1;
    private int serial = -1;

    private final static String DEFAULT_DEVICE_NAME = "Nord Modular";
    private final static String DEVICE_NAME_KEYBOARD = "Nord Modular Keyboard";
    private final static String DEVICE_NAME_RACK = "Nord Modular Rack";
    private final static String DEVICE_NAME_MICRO = "Micro Modular";
    private String name = DEFAULT_DEVICE_NAME;

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
    
    private Property[] slotVoiceCount = 
    {
        new Property("slot0VoiceCount", 0, 255, 0),
        new Property("slot1VoiceCount", 0, 255, 0),
        new Property("slot2VoiceCount", 0, 255, 0),
        new Property("slot3VoiceCount", 0, 255, 0) 
    };
    private Property activeSlot = new Property("activeSlot", 0, 3, 0);

    private NmBank[] banks;
    
    
    
    public int getMaxSlotCount()
    {
        if (!connected)
            return 0;
        if (deviceId == IAmMessage.MICRO_MODULAR)
            return 1; // 1 slot
        return 4; // default: 4 slots
    }
    
    public int getMaxBankCount()
    {
        if (!connected)
            return 0;
        if (deviceId == IAmMessage.MICRO_MODULAR)
            return 1; // 1 bank
        return 9; // default: 9 banks
    }
    
    public int getPId(int slot)
    {
        return multicaster.getActivePid(slot);
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
        return deviceId == IAmMessage.MICRO_MODULAR;
    }
    
    public int getDeviceId()
    {
        return deviceId;
    }
    
    public int getSerial()
    {
        return serial;
    }
    
    private class NMActivePidListener extends ActivePidListener
    {
        protected void pidChanged(int slotId, int pid)
        {
            // no op
        }
    }
    
    public NordModular(NM1ModuleDescriptions moduleDescriptions)
    {
        banks = new NmBank[0];
        
        this.moduleDescriptions = moduleDescriptions;
        slotManager = new NmSlotManager(this);
        
        midiports = new MidiPortSupport(this, "pc-in", "pc-out");
        
        multicaster = new MessageMulticaster(new NMActivePidListener());
        multicaster.addProtocolListener(new NmProtocolListener(){
           public void messageReceived(ErrorMessage m)
           {
               try
            {
                setConnected(false);
            }
            catch (SynthException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
           }
        });
        protocol = new SchedulingProtocol();
        
        protocol.setMessageHandler(multicaster);

        messageHander = new NmMessageHandler(this);
        addProtocolListener(messageHander);
        
        scheduler = new Scheduler(protocol);
        
        protocolThread = new StoppableThread(new ProtocolThreadExecutionPolicy(protocol), 
                new ProtocolRunner(protocol, new Nm1ProtocolErrorHandler(this)));
    }

    private class SchedulingProtocol extends NmProtocol
    {
        protected void heartbeatImpl() throws MidiException
        {
            try
            {
            scheduler.schedule();
            }
            catch (SynthException e)
            {
                if (e.getCause() != null && e.getCause() instanceof MidiException)
                    throw (MidiException)e.getCause();
                
                MidiException me = new MidiException(e.getMessage(), -1);
                me.initCause(e);
                
                throw me;
            }
            super.heartbeatImpl();
            comUpdateStatus();
        }
        

        protected void send(javax.sound.midi.MidiMessage message)
        {
            super.send(message);
            comTransmit();
        }

        public void send(MidiMessage midiMessage) throws MidiException
        {
            super.send(midiMessage);
            comTransmit();
        }

        protected void received(byte[] data)
        {
            super.received(data);
            comReceive();
        }

        protected void dispatchEvents(QueueBuffer<MidiMessage> events)
        {
            super.dispatchEvents(events);
            comReceive();
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
        switch (deviceId)
        {
            case IAmMessage.MICRO_MODULAR:
                return DEVICE_NAME_MICRO;
            case IAmMessage.NORD_MODULAR_RACK:
                return DEVICE_NAME_RACK;
            case IAmMessage.NORD_MODULAR_KEYBOARD:
                return DEVICE_NAME_KEYBOARD;
            default:
                return DEFAULT_DEVICE_NAME;
        }
    }

    private MidiDriver createMidiDriver() throws SynthException
    {
        midiports.validatePlugs();
        
        return new MidiDriver(midiports.getInPlug().getDeviceInfo(),
                midiports.getOutPlug().getDeviceInfo());
    }
    
    private void connect() throws SynthException
    {
        if (log.isInfoEnabled())
        {
            log.info("connect()");
        }
        
        midiDriver = createMidiDriver();
        try
        {
            midiDriver.connect();
        }
        catch (MidiUnavailableException e)
        {
            if (log.isWarnEnabled())
            {
                log.warn("mididriver.connect() failed", e);
            }
            throw new SynthException(e);
        }
        
        try
        {
            midiDriver.getTransmitter().setReceiver(protocol.getReceiver());
            protocol.getTransmitter().setReceiver(midiDriver.getReceiver());
        }
        catch (Throwable t)
        {
            if (log.isWarnEnabled())
            {
                log.warn("setting receiver/transmitter failed", t);
            }
            throw new SynthException(t);
        }

        protocol.reset();
        
        NmMessageAcceptor<IAmMessage> iamAcceptor = new NmMessageAcceptor<IAmMessage>(IAmMessage.class);

        NmMessageAcceptor<SynthSettingsMessage> settingsAcceptor = 
            new NmMessageAcceptor<SynthSettingsMessage>(SynthSettingsMessage.class);
        
        try
        {
            multicaster.addProtocolListener(iamAcceptor);


            if (log.isInfoEnabled())
            {
                log.info("sending "+IAmMessage.class.getName()+", expecting reply...");
            }
            
            try
            {
                protocol.send(new IAmMessage());
            }
            catch (Exception e)
            {
                if (log.isWarnEnabled())
                {
                    log.warn("sending "+IAmMessage.class.getName()+" failed", e);
                }
                throw new SynthException(e);
            }

            final long timeout = 10000; // 10 seconds
            iamAcceptor.waitForReply(protocol, timeout);
            
            IAmMessage iam = iamAcceptor.getFirstMessage();

            if (log.isInfoEnabled())
            {
                log.info("received "+IAmMessage.class.getName()+": "+iam);
            }

            validateVersion(iam, 3, 3);
            
            deviceId = iam.getDeviceId();
            serial = iam.getSerial();
            
            switch (deviceId)
            {
                case IAmMessage.NORD_MODULAR_RACK:
                    break;
                case IAmMessage.NORD_MODULAR_KEYBOARD:
                    break;
                case IAmMessage.MICRO_MODULAR:
                    break;
                default:
                {
                    log.warn("unknown deviceId: "+deviceId+" ("+iam+")"+", assume device is 'Nord Modular Keyboard'");
                    // assume keyboard
                    deviceId = IAmMessage.NORD_MODULAR_KEYBOARD;
                    break;
                }
            }
            
            setConnectedFlag(true);

            // request synth settings

            if (log.isInfoEnabled())
            {
                log.info("requesting synth settings");
            }
            multicaster.addProtocolListener(settingsAcceptor);
            try
            {
                protocol.send(new RequestSynthSettingsMessage());
            }
            catch (Exception e)
            {
                if (log.isWarnEnabled())
                {
                    log.warn("sending "+RequestSynthSettingsMessage.class.getName()+" failed", e);
                }
                throw new SynthException("Request synth settings failed.", e);
            }
            
            settingsAcceptor.waitForReply(protocol, timeout);

            if (log.isInfoEnabled())
            {
                log.info("synth settings received");
            }
            setSettings(settingsAcceptor.getFirstMessage());
            if (log.isInfoEnabled())
            {
                log.info("adapted properties to received synth settings");
            }
        }
        catch (SynthException e)
        {
            if (log.isWarnEnabled())
            {
                log.warn("connect() failed.", e);
            }
            disconnect();
            throw e;
        }
        catch (Exception e)
        {
            if (log.isWarnEnabled())
            {
                log.warn("connect() failed.", e);
            }
            disconnect();
            throw new SynthException(e);
        }
        finally
        {
            multicaster.removeProtocolListener(settingsAcceptor);
            multicaster.removeProtocolListener(iamAcceptor);
        }

        if (log.isInfoEnabled())
        {
            log.info("starting protocol thread...");
        }
        // now everything is fine - start the protocol thread
        protocolThread.start();

        // request patches 

        if (log.isInfoEnabled())
        {
            log.info("requesting patches...");
        }
        for (int i=0;i<slotManager.getSlotCount();i++)
        {
            NmSlot slot = slotManager.getSlot(i);
            
            if (slot.isEnabled())
                slot.requestPatch();
        }
        if (log.isInfoEnabled())
        {
            log.info("connect() successfull.");
        }
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
    
    private boolean isValidSlot(int slot)
    {
        return slot>=0 && slot<slotManager.getSlotCount();
    }
    
    private void checkSlot(int slot)
    {
        if (!isValidSlot(slot))
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
        return slotManager.getSlot(slot).isEnabled();
    }
    
    public void setSlotEnabled(int slot, boolean enable)
    {
        checkSlot(slot);
        slotManager.getSlot(slot).setEnabled(enable);
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

    public void setActiveSlot(int selectSlot)
    {
        checkSlot(selectSlot);
        int oldValue = activeSlot.getValue();
        activeSlot.setValue(selectSlot);

        getScheduler().offer(new ScheduledMessage(this,new SlotActivatedMessage(selectSlot)));
        
        slotManager.getSlot(oldValue).fireSelectedSlotChange(true, false);
        slotManager.getSlot(selectSlot).fireSelectedSlotChange(false, true);
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
            firePropertyChange(PROPERTY_NAME, oldName, name);
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
                
                int value = 0;
                
                if (!disabled)
                {
                    Object e = settings.get(slotEnabledPropertyName(i));
                    try
                    {
                        value = Math.max(0, Math.min(1, ((Integer)e).intValue()));
                    }
                    catch (ClassCastException cce)
                    {
                        // ignore
                    }
                }
                slotManager.getSlot(i).setEnabledValue(value>0);
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
    
    private String slotEnabledPropertyName(int slotIndex)
    {
        return "slot"+slotIndex+"Selected";
    }
    
    public Object getClientProperty(Object key)
    {
        if (!"icon".equals(key))
            return super.getClientProperty(key);

        Object icon = super.getClientProperty("icon");
        if (icon != null)
            return icon;
        
        switch (deviceId)
        {
            case IAmMessage.MICRO_MODULAR:
                icon = super.getClientProperty("icon.nm.micro");
                break;
            case IAmMessage.NORD_MODULAR_RACK:
                icon = super.getClientProperty("icon.nm.rack");
                break;
        }
        if (icon == null)
            icon = super.getClientProperty("icon.nm.keyboard");
        
        return icon;
    }

    private void disconnect()
    {
        if (log.isInfoEnabled())
        {
            log.info("disconnect()");
        }
        this.serial = -1;
        this.deviceId = -1;
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
            
            updateBanks();
            
            fireSynthesizerStateChanged();
            
            if (connected)
                setComStatus(ComStatus.Idle);
            else
                setComStatus(ComStatus.Offline);
        }
    }
    
    private void updateBanks()
    {
        if (!connected)
        {
            banks = new NmBank[0];
            return;
        }
        
        int cnt = getMaxBankCount();
        banks = new NmBank[cnt];
        for (int i=0;i<cnt;i++)
            banks[i] = new NmBank(this, i);
    }

    private SynthSettingsMessage createSettingsMessage() throws MidiException
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

        NmSlot[] slots = new NmSlot[getMaxSlotCount()];
        for (int i=0;i<slots.length;i++)
            slots[i] = new NmSlot(this, i);
        slotManager.setSlots(slots);
    }

    private void disconnected()
    {
        scheduler.clear();
        
        for (NmSlot slot: slotManager)
        {
            // unregister patch
            slot.setPatch(null);
        }
        
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
            if (t instanceof MidiException)
            {
                MidiException me = (MidiException) t;
                switch (me.getError())
                {
                    case MidiException.INVALID_MIDI_DATA:
                    case MidiException.MIDI_PARSE_ERROR:
                    case MidiException.UNKNOWN_MIDI_MESSAGE:
                    {
                        me.printStackTrace();
                        // ignore
                        // TODO log error
                        return;
                    }
                    case MidiException.TIMEOUT:
                    {
                        
                        // go on: 
                        EventQueue.invokeLater(this);
                        throw me;
                    }
                }
            }
            
            if (nm1.isIgnoreErrorsEnabled())
            {                
                t.printStackTrace();
            }
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
    
    public NmBank[] getBanks()
    {
        NmBank[] copy = new NmBank[banks.length];
        for (int i=0;i<banks.length;i++)
            copy[i] = banks[i];
        return copy;
    }

    public MidiPort getPort( int index )
    {
        return midiports.getPort(index);
    }

    public NmBank getBank( int index )
    {
        return banks[index];
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
        return banks.length;
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

    protected void fireSlotEnabledChange(int slotIndex, boolean oldEnabled, boolean newEnabled)
    {
        firePropertyChange(slotEnabledPropertyName(slotIndex), oldEnabled, newEnabled);
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

    public StorePatchWorker createStorePatchWorker()
    {
        return new NMStorePatchWorker(this);
    }

    public double getDoubleProperty(String propertyName)
    {
        if (DSP_GLOBAL.equals(propertyName))
        {
            return dspGlobal;
        }
        
        throw new IllegalArgumentException("no such property: "+propertyName);
    }

    public Object getProperty(String propertyName)
    {
        if (DSP_GLOBAL.equals(propertyName))
        {
            return dspGlobal;
        }
        return null;
    }

    public boolean hasProperty(String propertyName)
    {
        return false;// DSP_GLOBAL.equals(propertyName);
    }

    public ComStatus getComStatus()
    {
        return comStatus;
    }
    
    protected void setComStatus(ComStatus status)
    {
        ComStatus newValue = connected ? status : ComStatus.Offline;
        ComStatus oldValue = this.comStatus;
        
        if (oldValue != newValue)
        {
            this.comStatus = newValue;
            fireComStatusChanged(newValue);
        }
    }
    
    long comLastReceiveDisableAt = 0;
    long comLastTransmitDisableAt = 0;
    static final long COM_THRESHOLD = 75; // milliseconds
    
    protected void comReceive()
    {
        comLastReceiveDisableAt = System.currentTimeMillis()+COM_THRESHOLD;
        comUpdateStatus();
    }
    
    protected void comTransmit()
    {
        comLastTransmitDisableAt = System.currentTimeMillis()+COM_THRESHOLD;
        comUpdateStatus();
    }

    public void comUpdateStatus()
    {
        if (EventQueue.isDispatchThread())
        {
            __comUpdateStatusImpl();
        }
        else
        {
            EventQueue.invokeLater(new Runnable(){
                public void run()
                {
                    __comUpdateStatusImpl();
                }});
        }
    }
    
    private void __comUpdateStatusImpl()
    {
        long t = System.currentTimeMillis();
        boolean receive = comLastReceiveDisableAt>t;
        boolean transmit = comLastTransmitDisableAt>t;
        ComStatus newStatus;
        if (transmit)
        {
            if (receive)
            {
                newStatus = ComStatus.TransmitReceive;
            }
            else
            {
                newStatus = ComStatus.Transmit;
            }
        }
        else
        {
            if (receive)
            {
                newStatus = ComStatus.Receive;
            }
            else
            {
                newStatus = ComStatus.Idle;
            }
        }
        
        setComStatus(newStatus);
    }
    
}


