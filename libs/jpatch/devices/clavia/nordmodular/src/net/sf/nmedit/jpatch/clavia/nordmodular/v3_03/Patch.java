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
 * Created on Apr 10, 2006
 */
package net.sf.nmedit.jpatch.clavia.nordmodular.v3_03;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.Event;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.EventBuilder;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.EventChain;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.PatchListener;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.features.History;
import net.sf.nmedit.jpatch.history.PatchHistory;
import net.sf.nmedit.jpatch.spi.PatchImplementation;

/**
 * Implementation of the (virtual) patch according to the patch file format 3.0 specification.
 * 
 * @author Christian Schneider
 * TODO handle Micro Modular
 */
public class Patch implements net.sf.nmedit.jpatch.Patch
{

    //   setPolyphonie(1-32)
    
    /**
     * @see Header
     */
    private Header header;
    
    /**
     * the poly voice area
     * @see VoiceArea
     */
    private VoiceArea polyVoiceArea;
    
    /**
     * the common voice area
     * @see VoiceArea
     */
    private VoiceArea commonVoiceArea;
    
    /**
     * midi controller settings
     * @see MidiControllerSet
     */
    private MidiControllerSet midiControllerSet;
    
    /**
     * knob assignments
     * @see KnobSet
     */
    private KnobSet knobs;
    
    /**
     * morph groups
     * @see MorphSet
     */
    private List<Morph> morphs;
    
    /**
     * A set of notes. The hashcode of the note class is equal to the note number.
     * This assures that no duplicate notes are in the set.
     * @see NoteSet
     */
    private NoteSet noteSet;

    public final static String MODIFIED = "patch.modified";
    public final static String NAME = "patch.name";
    public final static String NOTE = "patch.note";
    public final static String UI = "patch.ui";
    public final static String VERSION = "patch.version";
    public final static String SLOT = "patch.slot";
    public final static String HISTORY = "patch.history";
    public final static String VAPOLY_CYCLES = "patch.va.poly.cycles";
    public final static String VACOMMON_CYCLES = "patch.va.common.cycles";
    private Map<String, Object> properties = new HashMap<String,Object>();

    private final PatchImplementation impl;
    
    /**
     * Creates a new patch.
     */
    public Patch(PatchImplementation impl)
    {
        this.impl = impl;
        header = new Header(this);
        noteSet = new NoteSet();
        
        morphs = new ArrayList<Morph>();
        for (int i=0;i<4;i++)
            morphs.add(new Morph(this,i));
        morphs = Collections.unmodifiableList(morphs);
        midiControllerSet = new MidiControllerSet(this);
        knobs = new KnobSet(this);
        
        polyVoiceArea = new VoiceArea(this);
        commonVoiceArea = new VoiceArea(this);

        // TODO allow installing/uninstalling history
        // TODO default: don't install history
        History h =  new History(this);
        setProperty(HISTORY, h);
    }
    
    /**
     * Returns the header section of the patch.
     * @return the header section of the patch
     */
    public Header getHeader()
    {
        return this.header;
    }
    
    /**
     * Returns the poly voice area.
     * @return the poly voice area
     */
    public VoiceArea getPolyVoiceArea()
    {
        return this.polyVoiceArea;
    }
    
    /**
     * Returns the common voice area.
     * @return the common voice area
     */
    public VoiceArea getCommonVoiceArea()
    {
        return this.commonVoiceArea;
    }
    
    /**
     * Returns the note set. 
     * @return the note set
     * @see NoteSet
     */
    public NoteSet getNoteSet()
    {
        return noteSet;
    }

    /**
     * Returns the midi controller settings.
     * @return the midi controller settings
     * @see MidiControllerSet
     */
    public MidiControllerSet getMidiControllers()
    {
        return midiControllerSet;
    }
    
    /**
     * Returns the knob assignments.
     * @return the knob assignments.
     * @see KnobSet
     */
    public KnobSet getKnobs()
    {
        return knobs;
    }

    /**
     * Returns the morph group.
     * @return the morph group
     * @see MorphSet
     */
    public List<Morph> getMorphs()
    {
        return morphs;
    }

    public PatchImplementation getPatchImplementation()
    {
        return impl;
    }

    public PatchHistory getHistory()
    {
        return (PatchHistory) getProperty(HISTORY);
    }

    public Object getProperty(String name)
    {
        return properties.get(name);
    }
    
    public void setProperty(String name, Object value)
    {
        Object oldValue = properties.get(name);
        
        if (oldValue!=value)
        {
            if (value==null)
            {
                properties.remove(name);
                firePropertyChanged(name, oldValue, value);
            }
            else if (!value.equals(oldValue))
            {
                properties.put(name, value);
                firePropertyChanged(name, oldValue, value);
            }
        }
    }
    
    public void setModified(boolean changed)
    {
        setProperty(MODIFIED, changed ? Boolean.TRUE : Boolean.FALSE);
    }
    
    public boolean isModified()
    {
        Boolean b = (Boolean) getProperty(MODIFIED);
        return b != null ? b : false;
    }
    
    public String getName()
    {
        return (String) getProperty(NAME);
    }
    
    public void setName(String name)
    {
        setProperty(NAME, name);
    }

    public String getNote()
    {
        return (String) getProperty(NOTE);
    }
    
    public void setNote(String note)
    {
        setProperty(NOTE, note);
    }
    
    private EventChain<PatchListener> listenerList = null; 
    
    private void firePropertyChanged(String propertyName, Object oldValue, Object newValue)
    {
        if (listenerList!=null)
        {
            Event e = EventBuilder.patchPropertyChanged(this, propertyName, oldValue, newValue);
            EventChain<PatchListener> l = listenerList;
            do
            {
                l.getListener().patchPropertyChanged(e);
                l = l.getChain();
            }
            while (l!=null);
        }
    }
    
    public void addPatchListener(PatchListener l)
    {
        listenerList = new EventChain<PatchListener> (l, listenerList);
    }
    
    public void removeListener(PatchListener l)
    {
        if (listenerList!=null)
            listenerList = listenerList.remove(l);
    }

}
