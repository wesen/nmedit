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
package net.sf.nmedit.jsynth;

import javax.sound.midi.MidiDevice;

import net.sf.nmedit.jsynth.event.SynthStateListener;

public interface Synthesizer 
{

    /**
     * Sets the input midi device info.
     * 
     * This method can only be used when the synthesizer
     * is not connected.
     * 
     * @param in the input midi device info
     * @throws IllegalStateException the
     * synthesizer is already connected
     */
    void setMidiIn(MidiDevice.Info in);

    /**
     * Sets the output midi device info.
     * 
     * This method can only be used when the synthesizer
     * is not connected.
     * 
     * @param in the output midi device info
     * @throws IllegalStateException the
     * synthesizer is already connected
     */
    void setMidiOut(MidiDevice.Info out);
    
    /**
     * Returns the input midi device info.
     * The operation returns <code>null</code>
     * when the property was not set before.
     * @return the input midi device info
     */
    MidiDevice.Info getMidiIn();

    /**
     * Returns the output midi device info.
     * The operation returns <code>null</code>
     * when the property was not set before.
     * @return the input midi device info
     */
    MidiDevice.Info getMidiOut();
    
    /**
     * Connects or disconnects the synthesizer.
     * 
     * <p>Note: It might require some time until
     * the synthesizer is connected/disconnected.
     * The operation might block or might be realized
     * asynchronously.</p>
     * 
     * <p>When connecting and a connection is already 
     * established nothing happens.</p>
     * 
     * <p>When disconnecting and no connection exists
     * nothing happens.</p>
     * 
     * @param connect <code>true</code> 
     * connects with synthesizer, <code>false</code>
     * disconnects from the synthesizer
     * 
     * @throws SynthException when the operation fails
     */
    void setConnected(boolean connect) throws SynthException;
    
    /**
     * Returns <code>true</code> when the synthesizer
     * is connected.
     * @return <code>true</code> when the synthesizer
     * is connected
     */
    boolean isConnected();

    void addSynthStateListener(SynthStateListener l);
    void removeSynthStateListener(SynthStateListener l);
    
}
