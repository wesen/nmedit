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

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.ListenableAdapter;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.PatchEventS;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.history.impl.PatchHistoryImpl;
import net.sf.nmedit.jpatch.history.PatchHistory;
import net.sf.nmedit.jpatch.spi.PatchImplementation;

/**
 * Implementation of the (virtual) patch according to the patch file format 3.0 specification.
 * 
 * @author Christian Schneider
 * TODO handle Micro Modular
 */
public class Patch extends ListenableAdapter<PatchEventS> implements net.sf.nmedit.jpatch.Patch
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
     * name of the patch
     */
    private String name;
    
    /**
     * user notes
     */
    private String note;
    
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
    private MorphSet morphSet;
    
    /**
     * A set of notes. The hashcode of the note class is equal to the note number.
     * This assures that no duplicate notes are in the set.
     * @see NoteSet
     */
    private NoteSet noteSet;

    private final PatchImplementation impl;
    private PatchHistory history;
    private PatchEventS event = new PatchEventS(this);
    
    /**
     * Creates a new patch.
     */
    public Patch(PatchImplementation impl)
    {
        this.impl = impl;
        header = new Header();
        noteSet = new NoteSet();
        morphSet = new MorphSet();
        midiControllerSet = new MidiControllerSet();
        knobs = new KnobSet();
        
        polyVoiceArea = new VoiceArea(this);
        commonVoiceArea = new VoiceArea(this);
        name = null;
        note = "";
        
        // TODO allow installing/uninstalling history
        // TODO default: don't install history
        this.history = new PatchHistoryImpl(this);
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
     * Returns the patch's name.
     * @return the patch's name
     */
    public String getName()
    {
        return name;
    }
    
    private boolean eqString(String a, String b)
    {
        if (a==null) a="";
        if (b==null) b="";
        return a.equals(b);
    }
    
    /**
     * Sets the patch's name.
     * @param name the new name
     */
    public void setName(String name)
    {
        if (!eqString(this.name, name))
        {
            event.nameChanged(this.name, name);
            
            this.name = name;
            
            fireEvent(event);
        }
    }
    
    /**
     * Returns the user's notes.
     * @return the user's notes
     */
    public String getNote()
    {
        return note;
    }
    
    /**
     * Sets the user's notes property.
     * @param text the text
     */
    public void setNote(String text)
    {
        if (!eqString(this.note, text))
        {
            event.noteChanged(this.note, text);
            
            this.note = text == null ? "" : text;
    
            fireEvent(event);
        }
    }
    
    /**
     * Returns the morph group.
     * @return the morph group
     * @see MorphSet
     */
    public MorphSet getMorphs()
    {
        return morphSet;
    }

    public PatchImplementation getPatchImplementation()
    {
        return impl;
    }

    public PatchHistory getHistory()
    {
        return history;
    }

}
