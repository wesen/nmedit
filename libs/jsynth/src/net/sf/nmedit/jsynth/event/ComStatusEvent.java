/* Copyright (C) 2008 Christian Schneider
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
package net.sf.nmedit.jsynth.event;

import net.sf.nmedit.jsynth.ComStatus;
import net.sf.nmedit.jsynth.Synthesizer;

public class ComStatusEvent extends SynthesizerEvent
{

    private static final long serialVersionUID = 5712107834925411980L;

    private ComStatus status;

    public ComStatusEvent(Synthesizer synth, ComStatus status)
    {
        super(synth, SYNTH_COM_STATUS_CHANGED, null);
        this.status = status;
    }

    public ComStatus getStatus()
    {
        return status;
    }
    
}
