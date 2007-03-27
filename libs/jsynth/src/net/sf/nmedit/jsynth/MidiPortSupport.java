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
