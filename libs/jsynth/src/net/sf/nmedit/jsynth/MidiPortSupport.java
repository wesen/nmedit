package net.sf.nmedit.jsynth;

import net.sf.nmedit.jsynth.midi.MidiPlug;
import net.sf.nmedit.jsynth.midi.MidiPort;

public class MidiPortSupport
{

    private MidiPort inPort;
    private MidiPort outPort;
    private MidiPort[] ports;

    public MidiPortSupport(Synthesizer synth, String inName, String outName)
    {
        this(new MidiPort(synth, MidiPort.Type.RECEIVER, inName),
                new MidiPort(synth, MidiPort.Type.TRANSMITTER, outName));
    }
    
    public MidiPortSupport(MidiPort in, MidiPort out)
    {
        this.inPort = in;
        this.outPort = out;
        this.ports = new MidiPort[] {in, out};
    }

    public MidiPort getInPort()
    {
        return inPort;
    }
    
    public MidiPort getOutPort()
    {
        return outPort;
    }

    public MidiPlug getInPlug()
    {
        return inPort.getPlug();
    }
    
    public MidiPlug getOutPlug()
    {
        return outPort.getPlug();
    }
    
    public MidiPort[] toArray()
    {
        return ports.clone();
    }

    public MidiPort getPort(int index)
    {
        return ports[index];
    }

    public int getPortCount()
    {
        return ports.length;
    }
    
    public boolean arePlugsAvailable()
    {
        return getInPlug() != null && getOutPlug() != null;
    }
    
    public void validatePlugs() throws SynthException
    {
        if (getInPlug() == null)
            throw new SynthException("port not assigned: "+inPort);
        if (getOutPlug() == null)
            throw new SynthException("port not assigned: "+outPort);
    }
    
}
