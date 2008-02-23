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
package net.sf.nmedit.jsynth.event;

import java.awt.Event;

import net.sf.nmedit.jsynth.Synthesizer;

public class SynthesizerEvent extends Event
{

    /**
     * 
     */
    private static final long serialVersionUID = -4003614134438383460L;
    public static final int SYNTH_CONNECTION_STATE_CHANGED = 0;
    public static final int SYNTH_BANK_ADDED = 1;
    public static final int SYNTH_BANK_REMOVED = 2;
    public static final int SYNTH_PORT_ADDED = 3;
    public static final int SYNTH_PORT_REMOVED = 4;
    public static final int SYNTH_SLOT_ADDED = 5;
    public static final int SYNTH_SLOT_REMOVED = 6;
    public static final int SYNTH_SLOT_NEWPATCH = 7;
    public static final int SYNTH_BANK_UPDATE = 8;
    public static final int SYNTH_COM_STATUS_CHANGED = 9;

    public SynthesizerEvent( Synthesizer synth )
    {
        super( synth, 0, null );
    }
    
    protected SynthesizerEvent(Synthesizer synth, int id, Object arg)
    {
        super(synth, id, arg);
    }
    
    public Synthesizer getSynthesizer()
    {
        return (Synthesizer) target;
    }

    public int getId()
    {
        return id;
    }
    
}
