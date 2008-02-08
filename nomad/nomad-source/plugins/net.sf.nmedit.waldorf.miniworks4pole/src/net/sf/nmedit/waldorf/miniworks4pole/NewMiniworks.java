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
package net.sf.nmedit.waldorf.miniworks4pole;

import net.sf.nmedit.nomad.core.Nomad;
import net.sf.nmedit.nomad.core.service.Service;
import net.sf.nmedit.nomad.core.service.synthService.NewSynthService;
import net.sf.nmedit.nomad.core.swing.explorer.ExplorerTree;
import net.waldorf.miniworks4pole.jsynth.Miniworks4Pole;

public class NewMiniworks
    implements NewSynthService
{

    public String getSynthDescription()
    {
        return "http://www.waldorfmusic.de/";
    }

    public String getSynthName()
    {
        return "Miniworks 4 Pole";
    }

    public String getSynthVendor()
    {
        return "Waldorf";
    }

    public String getSynthVersion()
    {
        return "?";
    }

    public boolean isNewSynthAvailable()
    {
        return true;
    }

    public void newSynth()
    {/*
        ExplorerTree etree = Nomad.sharedInstance().getExplorer();
        Miniworks4Pole synth = MWData.createSynth();
        etree.addRootNode(new WMSynthDeviceContext(etree, synth, getSynthName()));*/
    }

    public Class<? extends Service> getServiceClass()
    {
        return NewSynthService.class;
    }

    
}
