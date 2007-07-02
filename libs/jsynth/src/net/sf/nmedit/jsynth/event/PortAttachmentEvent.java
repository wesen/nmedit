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
package net.sf.nmedit.jsynth.event;

import java.awt.Event;

import net.sf.nmedit.jsynth.Plug;
import net.sf.nmedit.jsynth.Port;
import net.sf.nmedit.jsynth.Synthesizer;

public class PortAttachmentEvent extends Event
{

    /**
     * 
     */
    private static final long serialVersionUID = 6310227728011508217L;
    private Plug oldPlug;
    private Plug newPlug;

    public PortAttachmentEvent( Port target, Plug oldPlug, Plug newPlug)
    {
        super( target, 0, null );
        this.oldPlug = oldPlug;
        this.newPlug = newPlug;
    }

    public Synthesizer getSynthesizer()
    {
        return getPort().getSynthesizer();
    }
    
    public Port getPort()
    {
        return (Port) target;
    }
    
    public Plug getOldPlug()
    {
        return oldPlug;
    }
    
    public Plug getNewPlug()
    {
        return newPlug;
    }
    
}
