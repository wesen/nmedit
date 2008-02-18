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
 * Created on Jan 9, 2007
 */
package net.sf.nmedit.jsynth.clavia.nordmodular.utils;

import net.sf.nmedit.jnmprotocol2.GetPatchMessage;
import net.sf.nmedit.jnmprotocol2.MidiException;
import net.sf.nmedit.jnmprotocol2.NmProtocol;
import net.sf.nmedit.jnmprotocol2.NmProtocolListener;
import net.sf.nmedit.jnmprotocol2.PatchMessage;
import net.sf.nmedit.jpatch.clavia.nordmodular.NM1ModuleDescriptions;
import net.sf.nmedit.jpatch.clavia.nordmodular.NMPatch;
import net.sf.nmedit.jpatch.clavia.nordmodular.parser.ParseException;
import net.sf.nmedit.jpatch.clavia.nordmodular.parser.PatchBuilder;
import net.sf.nmedit.jsynth.clavia.nordmodular.NordModular;

public class ReceivePatchWorker extends NmProtocolListener
{
    
    private PatchBuilder builder = null;
    private int sections = 0;
    private NM1ModuleDescriptions modules;
    private int errorcount = 0;
    private int pid;
    private int slot;

    public ReceivePatchWorker(int slot, int pid, NM1ModuleDescriptions modules)
    {
        this.slot = slot;
        this.pid = pid;
        this.modules = modules;
    }
    
    public void getPatch(NmProtocol protocol) throws MidiException
    {
        for (GetPatchMessage m: GetPatchMessage.forAllParts(slot, pid))
            protocol.send(m);
    }
    
    public void getPatch(NordModular nm1) throws MidiException
    {
        getPatch(nm1.getProtocol());
    }

    public boolean available()
    {
        return builder != null;
    }
    
    public synchronized boolean complete()
    {
        return sections >=13;
    }
    
    public synchronized boolean hasError()
    {
        return errorcount > 0;
    }

    public NMPatch getPatch()
    {
        return builder != null ? builder.getPatch() : null;
    }
    
    public synchronized void processMessage(PatchMessage message) throws ParseException
    {
        errorcount ++;
        if (sections == 0)
            builder = NmUtils.parsePatchMessage(message, modules);
        else
            NmUtils.parsePatchMessage(message, builder);
        if (++sections == 13)
            builder.endDocument();
        errorcount--;
    }

    public void messageReceived(PatchMessage message) 
    {
        if (pid == message.get("pid"))
        {
            try
            {
                processMessage(message);
            }
            catch (ParseException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    public int getSections()
    {
        return sections;
    }
}
