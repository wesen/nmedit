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
 * Created on Apr 21, 2006
 */
package net.sf.nmedit.nomad.patch.virtual.event;

import net.sf.nmedit.nomad.patch.virtual.Connector;
import net.sf.nmedit.nomad.patch.virtual.Module;
import net.sf.nmedit.nomad.patch.virtual.VoiceArea;

public class VoiceAreaEvent extends PatchEvent
{

    private VoiceArea va;
    private Module m;
    private Connector src;
    private Connector dst;
    
    public VoiceAreaEvent()
    {
        va = null;
        m = null;
    }

    public VoiceArea getVoiceArea()
    {
        return va;
    }
    
    public Module getModule()
    {
        return m;
    }
    
    public void moduleAdded(VoiceArea va, Module m)
    {
        setID(VA_MODULE_ADDED);
        this.va = va;
        this.m = m;
    }
    
    public void moduleRemoved(VoiceArea va, Module m)
    {
        setID(VA_MODULE_REMOVED);
        this.va = va;
        this.m = m;
    }

    public void connected( VoiceArea area, Connector src, Connector dst )
    {
        setID(VA_CONNECTED);
        this.src = src;
        this.dst = dst;
    }

    public void disconnected( VoiceArea area, Connector src, Connector dst )
    {
        setID(VA_DISCONNECTED);
        this.src = src;
        this.dst = dst;
    }

    public Connector getDst()
    {
        return dst;
    }

    public Connector getSrc()
    {
        return src;
    }

    public void resized( VoiceArea area )
    {
        setID(VA_RESIZED);
        this.va = area;
    }

    public void updateConnection( Connector c )
    {
        setID(VA_UPDATE_CONNECTION);
        this.src = c;
        this.dst = null;
    }

    public void beginUpdate( VoiceArea va )
    {
        setID(VA_BEGIN_UPDATE);
        this.va = va;
    }
    
    public void endUpdate( VoiceArea va )
    {
        setID(VA_END_UPDATE);
        this.va = va;
    }
    
}
