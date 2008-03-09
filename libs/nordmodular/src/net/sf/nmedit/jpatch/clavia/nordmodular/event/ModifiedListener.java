package net.sf.nmedit.jpatch.clavia.nordmodular.event;

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
import net.sf.nmedit.jpatch.AllEventsListener;
import net.sf.nmedit.jpatch.clavia.nordmodular.NMPatch;
import net.sf.nmedit.jpatch.event.PConnectionEvent;
import net.sf.nmedit.jpatch.event.PModuleContainerEvent;
import net.sf.nmedit.jpatch.event.PModuleEvent;
import net.sf.nmedit.jpatch.event.PParameterEvent;

public class ModifiedListener extends AllEventsListener 
    implements PAssignmentListener, PPatchSettingsListener
{

    private NMPatch patch;
    private boolean installed;

    public ModifiedListener(NMPatch patch)
    {
        listenConnections = true;
        listenModules = true;
        listenParameters = true;
        
        this.patch = patch;
    }
    
    public NMPatch getPatch()
    {
        return patch;
    }
    
    public boolean isInstalled()
    {
        return installed;
    }
    
    public void uninstall()
    {
        if (!installed)
            return;
        
        patch.removeAssignmentListener(this);
        patch.removePatchSettingsListener(this);

        uninstallModuleContainer(patch.getPolyVoiceArea());
        uninstallModuleContainer(patch.getCommonVoiceArea());
        
        uninstallParameters(patch.getMorphSection().getMorphModule());
    }
    
    public void install()
    {
        if (installed)
            return;

        patch.addAssignmentListener(this);
        patch.addPatchSettingsListener(this);
        
        installModuleContainer(patch.getPolyVoiceArea());
        installModuleContainer(patch.getCommonVoiceArea());
        
        installParameters(patch.getMorphSection().getMorphModule());
    }

    public void moduleAdded(PModuleContainerEvent e)
    {
    	patch.setModified(true);
    }

    public void moduleRemoved(PModuleContainerEvent e)
    {
    	patch.setModified(true);
    }

    public void moduleRenamed(PModuleEvent e)
    {
    	patch.setModified(true);
    }

    public void moduleMoved(PModuleEvent e)
    {
    	patch.setModified(true);
    }

    public void focusRequested(PParameterEvent e)
    {
    	patch.setModified(true);
    }
    
    public void parameterValueChanged(PParameterEvent e)
    {
    	patch.setModified(true);
    }

    public void connectionAdded(PConnectionEvent e)
    {
    	patch.setModified(true);
    }

    public void connectionRemoved(PConnectionEvent e)
    {
    	patch.setModified(true);
    }

    public void parameterAssigned(PAssignmentEvent e)
    {
    	patch.setModified(true);
    }

    public void parameterDeassigned(PAssignmentEvent e)
    {
    	patch.setModified(true);
    }

        public void patchSettingsChanged(PPatchSettingsEvent e)
    {
    	patch.setModified(true);
    }
}
