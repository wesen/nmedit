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
 * Created on Dec 29, 2006
 */
package net.sf.nmedit.jsynth;

import java.beans.PropertyChangeListener;

import net.sf.nmedit.jsynth.event.ComStatusListener;
import net.sf.nmedit.jsynth.event.SynthesizerStateListener;
import net.sf.nmedit.jsynth.worker.StorePatchWorker;

/**
 * Synthesizer is the base interface for remote synthesizers.
 * 
 * The interface exposes following components of a synthesizer:
 * 
 * <p>
 *   <strong>Port</strong>s describe the different input and output
 *   ports such as MIDI IN or MIDI OUT ports.
 * </p>
 * <p>
 *   <strong>Bank</strong>s are the locations where patches
 *   can be stored permanently.
 * </p>
 * <p>
 *   <strong>Slot</strong>s are the locations where patches can
 *   be loaded and remote controlled.
 * </p>
 * 
 * @author Christian Schneider
 */
public interface Synthesizer
{
    
    // global dsp usage, double value
    public static final String DSP_GLOBAL = "synth.dsp.global";
    public static final String PROPERTY_NAME = "synth.name";
    
    /*
    PortManager getPortManager();
    BankManager getBankManager();*/
    SlotManager getSlotManager();
    
    Bank getBank(int index);
    int getBankCount();

    Slot getSlot(int index);
    int getSlotCount();
    
    Port getPort(int index);
    int getPortCount();
    
    Bank[] getBanks();
    Port[] getPorts();

    String getVendor();
    
    String getName();
    String getDeviceName();

    StorePatchWorker createStorePatchWorker();
    
    boolean hasProperty(String propertyName);
    
    double getDoubleProperty(String propertyName);
    
    Object getProperty(String propertyName);
    
    void putClientProperty(Object key, Object value);
    Object getClientProperty(Object key);

    ComStatus getComStatus();

    void setConnected(boolean connected) throws SynthException;
    boolean isConnected();

    void addSynthesizerStateListener(SynthesizerStateListener l);
    void removeSynthesizerStateListener(SynthesizerStateListener l);

    void addPropertyChangeListener(PropertyChangeListener l);
    void removePropertyChangeListener(PropertyChangeListener l);

    void addPropertyChangeListener(String propertyName, PropertyChangeListener l);
    void removePropertyChangeListener(String propertyName, PropertyChangeListener l);

    void addComStatusListener(ComStatusListener l);
    void removeComStatusListener(ComStatusListener l);
    
}
