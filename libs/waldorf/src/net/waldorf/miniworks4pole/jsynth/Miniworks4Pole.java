package net.waldorf.miniworks4pole.jsynth;

import javax.sound.midi.MidiUnavailableException;

import net.sf.nmedit.jnmprotocol.MidiDriver;
import net.sf.nmedit.jpatch.spec.ModuleDescriptions;
import net.sf.nmedit.jsynth.AbstractSynthesizer;
import net.sf.nmedit.jsynth.Bank;
import net.sf.nmedit.jsynth.DefaultSlotManager;
import net.sf.nmedit.jsynth.MidiPortSupport;
import net.sf.nmedit.jsynth.Port;
import net.sf.nmedit.jsynth.SlotManager;
import net.sf.nmedit.jsynth.SynthException;
import net.sf.nmedit.jsynth.clavia.nordmodular.utils.NmUtils;
import net.sf.nmedit.jsynth.midi.MidiPort;
import net.waldorf.miniworks4pole.jprotocol.MWListenerSupport;
import net.waldorf.miniworks4pole.jprotocol.MiniworksMidiMessage;
import net.waldorf.miniworks4pole.jprotocol.WProtocol;

public class Miniworks4Pole extends AbstractSynthesizer
{
    
    private DefaultSlotManager<Miniworks4Pole, MWSlot> slotManager;
    private MidiPortSupport midiports;
    private MidiDriver driver;
    private boolean connected = false;
    private WProtocol protocol;
    private MWEventHandler eventHandler;
    private MWListenerSupport midiListenerSupport;
    private ModuleDescriptions moduleDescriptions;

    public Miniworks4Pole(ModuleDescriptions moduleDescriptions)
    {
        this.moduleDescriptions = moduleDescriptions;
        
        slotManager = new DefaultSlotManager<Miniworks4Pole, MWSlot>(this);
        midiports = new MidiPortSupport(this, "in", "out");
        
        protocol = new WProtocol();
        midiListenerSupport = new MWListenerSupport(protocol);
        eventHandler = new MWEventHandler(this, midiListenerSupport);
    }
    
    public MidiPort getInPort()
    {
        return midiports.getInPort();
    }
    
    public MidiPort getOutPort()
    {
        return midiports.getOutPort();
    }

    public Bank getBank(int index)
    {
        throw new IndexOutOfBoundsException();
    }

    public int getBankCount()
    {
        return 0;
    }

    public Bank[] getBanks()
    {
        return new Bank[0];
    }

    public String getDeviceName()
    {
        return "Miniworks 4 Pole";
    }

    public String getName()
    {
        return getDeviceName();
    }

    public Port getPort(int index)
    {
        return midiports.getPort(index);
    }

    public int getPortCount()
    {
        return midiports.getPortCount();
    }

    public Port[] getPorts()
    {
        return midiports.toArray();
    }

    public MWSlot getSlot(int index)
    {
        return slotManager.getSlot(index);
    }

    public int getSlotCount()
    {
        return slotManager.getSlotCount();
    }

    public SlotManager getSlotManager()
    {
        return slotManager;
    }

    public String getVendor()
    {
        return "Waldorf Music";
    }

    public boolean isConnected()
    {
        return connected;
    }
    
    protected MidiDriver createDriver() throws SynthException
    {
        midiports.validatePlugs();
     
        return new MidiDriver(midiports.getInPlug().getDeviceInfo(), midiports.getOutPlug().getDeviceInfo());
    }

    public void setConnected(boolean connected) throws SynthException
    {
        if (this.connected != connected)
        {
            // this.connected = ...
            if (connected)
                connect();
            else
                disconnect();            
        }
    }
    
    private void connect() throws SynthException
    {
        driver = createDriver();
        try
        {
            driver.connect();
        }
        catch (MidiUnavailableException e)
        {
            NmUtils.transformException(e);
        }
        
        driver.getTransmitter().setReceiver(midiListenerSupport);
        
        connected = true;
        
        fireSynthesizerStateChanged();
        slotManager.setSlots(new MWSlot[]{new MWSlot(this)});
    }
    
    private void disconnect()
    {
        slotManager.setSlots(new MWSlot[0]);
        connected = false;
        driver.disconnect();
        fireSynthesizerStateChanged();
    }

    public WProtocol getProtocol()
    {
        return protocol;
    }
    
    public void send(MiniworksMidiMessage message)
    {
        if (!isConnected()) return;
        driver.getReceiver().send(message.getMidiMessage(), -1);
    }

    public ModuleDescriptions getModuleDescriptions()
    {
        return moduleDescriptions;
    }
    
}
