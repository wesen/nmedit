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
 * Created on Jul 6, 2006
 */
package net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.history.impl;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Module;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Patch;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.VoiceArea;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.CustomEvent;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.EventListener;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.HeaderEvent;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.ModuleEvent;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.ParameterEvent;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.PatchEventS;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.VoiceAreaEvent;

public class PatchEventTracer
{

    private final PatchHistoryImpl history;
    private final Patch patch;
    
    private VAEventTracer VAEventTracer = new VAEventTracer();
    private ModuleEventTracer ModuleEventTracer = new ModuleEventTracer();
    private ParameterEventTracer ParameterEventTracer = new ParameterEventTracer(); 
    private CustomEventTracer CustomEventTracer = new CustomEventTracer();
    private PEventTracer PEventTracer = new PEventTracer();
    private HeaderEventTracer HeaderEventTracer = new HeaderEventTracer();

    public PatchEventTracer(PatchHistoryImpl history)
    {
        this.history = history;
        this.patch = history.getPatch();
        install();
    }
    
    private void install()
    {
        patch.addListener(PEventTracer);
        patch.getHeader().addListener(HeaderEventTracer);
        install(patch.getPolyVoiceArea());
        install(patch.getCommonVoiceArea());
    }

    public void uninstall()
    {
        patch.removeListener(PEventTracer);
        patch.getHeader().removeListener(HeaderEventTracer);
        uninstall(patch.getPolyVoiceArea());
        uninstall(patch.getCommonVoiceArea());
    }

    private void install(VoiceArea va)
    {
        va.addListener(VAEventTracer);
        for (Module m : va)
            install(m);
    }

    private void uninstall(VoiceArea va)
    {
        va.removeListener(VAEventTracer);
        for (Module m : va)
            uninstall(m);
    }
    
    private void install( Module m )
    {
        m.addListener(ModuleEventTracer);
        for (int i=0;i<m.getParameterCount();i++)
            m.getParameter(i).addListener(ParameterEventTracer);
        for (int i=0;i<m.getCustomCount();i++)
            m.getCustom(i).addListener(CustomEventTracer);
    }
    
    private void uninstall( Module m )
    {
        m.removeListener(ModuleEventTracer);
        for (int i=0;i<m.getParameterCount();i++)
            m.getParameter(i).removeListener(ParameterEventTracer);
        for (int i=0;i<m.getCustomCount();i++)
            m.getCustom(i).removeListener(CustomEventTracer);
    }
    
    private class PEventTracer implements EventListener<PatchEventS>
    {
        public void event( PatchEventS event )
        {
            history.setModified(true);
        }
    }
    
    private class HeaderEventTracer implements EventListener<HeaderEvent>
    {
        public void event( HeaderEvent event )
        {
            history.setModified(true);
        }
    }

    private class VAEventTracer implements EventListener<VoiceAreaEvent>
    {
        public void event( VoiceAreaEvent event )
        {
            history.setModified(true);
            switch (event.getID())
            {
                case VoiceAreaEvent.VA_MODULE_ADDED:
                    install(event.getModule());
                    history.addToUndo(new ModuleAddRecord(history, null, event.getModule()));
                    break;
                case VoiceAreaEvent.VA_MODULE_REMOVED:
                    uninstall(event.getModule());
                    history.addToUndo(new ModuleRemoveRecord(history, null, event.getModule()));
                    break;
            }
        }
    }

    private class ModuleEventTracer implements EventListener<ModuleEvent>
    {
        public void event( ModuleEvent event )
        {
            history.setModified(true);
        }
    }

    private class ParameterEventTracer implements EventListener<ParameterEvent>
    {
        public void event( ParameterEvent event )
        {
            history.setModified(true);
        }
    }
    
    private class CustomEventTracer implements EventListener<CustomEvent>
    {
        public void event( CustomEvent event )
        {
            history.setModified(true);
        }
    }
    
}
