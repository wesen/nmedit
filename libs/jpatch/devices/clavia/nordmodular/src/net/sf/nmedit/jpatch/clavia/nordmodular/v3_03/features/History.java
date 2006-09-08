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
 * Created on Sep 8, 2006
 */
package net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.features;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Module;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Morph;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Patch;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.VoiceArea;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.AssignmentChangeListener;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.Event;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.HeaderListener;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.ModuleListener;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.MorphListener;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.ParameterListener;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.PatchListener;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.VoiceAreaListener;
import net.sf.nmedit.jpatch.history.PatchHistory;

public class History implements PatchHistory
{
    
    private int limit = 0;
    private Patch patch = null;
    private Listener l = new Listener(this);

    public History(Patch patch)
    {
        this(patch, 100);
    }

    public History(Patch patch, int undoRedoLimit)
    {
        this.patch = patch;
        this.limit = undoRedoLimit;
        install();
    }
    
    private void install()
    {
        l.install(patch);
    }
    
    public void uninstall()
    {
        l.uninstall(patch);
    }

    public int getUndoCount()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public int getRedoCount()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public void clearUndo()
    {
        // TODO Auto-generated method stub
        
    }

    public void clearRedo()
    {
        // TODO Auto-generated method stub
        
    }

    public void clear()
    {
        clearUndo();
        clearRedo();
    }

    public void setUndoLimit( int limit )
    {
        throw new UnsupportedOperationException();
    }

    public void undo()
    {
        // TODO Auto-generated method stub
        
    }

    public void redo()
    {
        // TODO Auto-generated method stub
        
    }

    public boolean canUndo()
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean canRedo()
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isModified()
    {
        return patch.isModified();
    }

    public void setModified( boolean modified )
    {
        patch.setModified(modified);
    }

    private static class Listener implements 
        HeaderListener, ModuleListener, MorphListener,
        ParameterListener, PatchListener, VoiceAreaListener,
        AssignmentChangeListener
    {

        private History history;

        public Listener(History history)
        {
            this.history = history;
        }

        public void install( Patch patch )
        {
            patch.addPatchListener(this);
            patch.getHeader().addHeaderListener(this);
            for (Morph m : patch.getMorphs())
                m.addMorphListener(this);
            patch.getMidiControllers().addAssignmentChangeListener(this);
            patch.getKnobs().addAssignmentChangeListener(this);
            install(patch.getPolyVoiceArea());
            install(patch.getCommonVoiceArea());
        }

        public void uninstall( Patch patch )
        {
            patch.removeListener(this);
            patch.getHeader().removeListener(this);
            for (Morph m : patch.getMorphs())
                m.removeMorphListener(this);
            patch.getMidiControllers().removeAssignmentChangeListener(this);
            patch.getKnobs().removeAssignmentChangeListener(this);
            uninstall(patch.getPolyVoiceArea());
            uninstall(patch.getCommonVoiceArea());
        }

        private void install( VoiceArea va )
        {
            va.addVoiceAreaListener(this);
            for (Module m : va)
                install(m);
        }

        private void uninstall( VoiceArea va )
        {
            va.removeVoiceAreaListener(this);
            for (Module m : va)
                uninstall(m);
        }
        
        private void install( Module m )
        {
            m.addModuleListener(this);
            for (int i=0;i<m.getParameterCount();i++)
                m.getParameter(i).addParameterListener(this);
            for (int i=0;i<m.getCustomCount();i++)
                m.getParameter(i).addParameterListener(this);
        }

        private void uninstall( Module m )
        {
            m.removeModuleListener(this);
            for (int i=0;i<m.getParameterCount();i++)
                m.getParameter(i).removeParameterListener(this);
            for (int i=0;i<m.getCustomCount();i++)
                m.getParameter(i).removeParameterListener(this);
        }

        /*public void connectorStateChanged( Event e )
        {
            history.setChanged(true);
        }*/

        public void headerValueChanged( Event e )
        {
            history.setModified(true);
        }

        public void moduleRenamed( Event e )
        {
            history.setModified(true);
        }

        public void moduleMoved( Event e )
        {
            history.setModified(true);
        }

        public void morphAssigned( Event e )
        {
            history.setModified(true);
        }

        public void morphDeassigned( Event e )
        {
            history.setModified(true);
        }

        public void morphKeyboardAssignmentChanged( Event e )
        {
            history.setModified(true);
        }

        public void morphValueChanged( Event e )
        {
            history.setModified(true);
        }

        public void parameterValueChanged( Event e )
        {
            history.setModified(true);
        }

        public void parameterMorphValueChanged( Event e )
        {
            history.setModified(true);
        }

        public void parameterKnobAssignmentChanged( Event e )
        {
            history.setModified(true);
        }

        public void parameterMorphAssignmentChanged( Event e )
        {
            history.setModified(true);
        }

        public void parameterMidiCtrlAssignmentChanged( Event e )
        {
            history.setModified(true);
        }

        public void patchHeaderChanged( Event e )
        {
            history.setModified(true);
        }

        public void patchPropertyChanged( Event e )
        {
            if (Patch.NAME.equals(e.getPropertyName())
                    ||Patch.NOTE.equals(e.getPropertyName()))
                history.setModified(true);
        }

        public void moduleAdded( Event e )
        {
            history.setModified(true);
            install(e.getModule());
        }

        public void moduleRemoved( Event e )
        {
            history.setModified(true);
            uninstall(e.getModule());
        }

        public void voiceAreaResized( Event e )
        {
            history.setModified(true);
        }

        public void cablesAdded( Event e )
        {
            history.setModified(true);
        }

        public void cablesRemoved( Event e )
        {
            history.setModified(true);
        }

        public void assignmentChanged( Event e )
        {
            history.setModified(true);
        }

        public void cableGraphUpdated( Event e )
        {
            history.setModified(true);
        }
        
    }

}
