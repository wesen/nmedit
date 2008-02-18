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
 */package net.sf.nmedit.jsynth.clavia.nordmodular.worker;

import net.sf.nmedit.jnmprotocol2.MidiMessage;
import net.sf.nmedit.jsynth.SynthException;
import net.sf.nmedit.jsynth.clavia.nordmodular.NordModular;
import net.sf.nmedit.jsynth.clavia.nordmodular.utils.NmUtils;

public class ScheduledMessage implements ScheduledWorker
{

    private NordModular synth;
    private MidiMessage message;
    private boolean messageSent = false;
    private boolean error = false;

    public ScheduledMessage(NordModular synth, MidiMessage message)
    {
        this.synth = synth;
        this.message = message;
    }

    public void aborted()
    {
        error = true; 
    }

    public boolean isWorkerFinished()
    {
        return (!synth.isConnected()) || messageSent || error;
    }

    public void runWorker() throws SynthException
    {
        if (isWorkerFinished())
            return;
        
        //System.out.println("scheduled: "+message);
        
        messageSent = true;
        try
        {
            synth.getProtocol().send(message);
        }
        catch (Exception e)
        {
            throw NmUtils.transformException(e);
        }
    }
    
}
