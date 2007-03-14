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
package net.sf.nmedit.jpatch.clavia.nordmodular;

import java.util.HashMap;
import java.util.Map;

import net.sf.nmedit.jpatch.ModuleContainer;
import net.sf.nmedit.jpatch.Patch;
import net.sf.nmedit.jpatch.spec.ModuleDescriptions;
import net.sf.nmedit.jsynth.Slot;

/**
 * Implementation of the (virtual) patch according to the patch file format 3.0 specification.
 * 
 * @author Christian Schneider
 * TODO handle Micro Modular
 */
public class NMPatch implements Patch
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
    private MorphSection morphSection;
    
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
    public final static String VERSION = "version";
    public final static String SLOT = "patch.slot";
    public final static String HISTORY = "patch.history";
    public final static String VAPOLY_CYCLES = "patch.va.poly.cycles";
    public final static String VACOMMON_CYCLES = "patch.va.common.cycles";
    private Map<String, Object> properties = new HashMap<String,Object>();
    private ModuleDescriptions moduleDescriptions ;
    private Slot slot;

    /**
     * Creates a new patch.
     */
    public NMPatch(ModuleDescriptions modules)
    {
        this.moduleDescriptions = modules;
        header = new Header(this);
        noteSet = new NoteSet();
        
        morphSection = new MorphSection(this);
        midiControllerSet = new MidiControllerSet(this);
        knobs = new KnobSet(this);
        
        polyVoiceArea = new VoiceArea(this);
        commonVoiceArea = new VoiceArea(this);
        
        setProperty(VERSION, "Nord Modular patch 3.0");
    }

    public Slot getSlot()
    {
        return slot;
    }
    
    public void setSlot(Slot slot)
    {
        this.slot = slot;
    }
    
    public ModuleDescriptions getModules()
    {
        return moduleDescriptions;
    }
    
    public MorphSection getMorphSection()
    {
        return morphSection;
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
        menuLayout.getEntry("nomad.menu.file.open")
        .addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e)
            {
                
                JFileChooser chooser = new JFileChooser(new File("/home/christian/Programme/nomad/data/patch/"));
                chooser.setMultiSelectionEnabled(true);
                FileServiceTool.addChoosableFileFilters(chooser);
                if (!(chooser.showOpenDialog(mainWindow)==JFileChooser.APPROVE_OPTION))
                return;
                
                FileService service =
                FileServiceTool.lookupFileService(chooser);
                
                if (service != null)
                {
                    for (File file:chooser.getSelectedFiles())
                    {
                        service.open(file);
                    }
                }
                
            }});
     * Returns the common voice area.
     * @return the common voice area
    public PatchImplementation getPatchImplementation()
    {
        return impl;
    }

    public PatchHistory getHistory()
    {
    public Module createModule( Object moduleID )
    {
        throw new UnsupportedOperationException();
    }

    public Connection createConnection( Connector a, Connector b )
    {
        return null;
    }

        return (PatchHistory) getProperty(HISTORY);
    }

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
         //       firePropertyChanged(name, oldValue, value);
            }
            else if (!value.equals(oldValue))
            {
                properties.put(name, value);
         //       firePropertyChanged(name, oldValue, value);
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

    public String getVersion()
    {
        return (String) getProperty(VERSION);
    }

    public ModuleContainer getModuleContainer()
    {
        return null;
    }

    public String toString()
    {
        return getClass().getName()
        +"[name="+getName()+",version="+getVersion()+"]";
    }
    
}
