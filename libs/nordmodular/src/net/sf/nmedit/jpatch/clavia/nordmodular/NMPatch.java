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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.swing.event.EventListenerList;

import net.sf.nmedit.jpatch.ModuleDescriptions;
import net.sf.nmedit.jpatch.PModuleContainer;
import net.sf.nmedit.jpatch.PPatch;
import net.sf.nmedit.jpatch.clavia.nordmodular.event.ModifiedListener;
import net.sf.nmedit.jpatch.clavia.nordmodular.event.PAssignmentEvent;
import net.sf.nmedit.jpatch.clavia.nordmodular.event.PAssignmentListener;
import net.sf.nmedit.jpatch.clavia.nordmodular.event.PPatchSettingsEvent;
import net.sf.nmedit.jpatch.clavia.nordmodular.event.PPatchSettingsListener;
import net.sf.nmedit.jpatch.clavia.nordmodular.parser.ParseException;
import net.sf.nmedit.jpatch.clavia.nordmodular.parser.PatchExporter;
import net.sf.nmedit.jpatch.clavia.nordmodular.parser.PatchFileWriter;
import net.sf.nmedit.jpatch.impl.PBasicPatch;
import net.sf.nmedit.jsynth.Slot;
import net.sf.nmedit.jsynth.clavia.nordmodular.NmSlot;
import net.sf.nmedit.jsynth.clavia.nordmodular.utils.NmUtils;

/**
 * Implementation of the (virtual) patch according to the patch file format 3.0 specification.
 * 
 * @author Christian Schneider
 * TODO handle Micro Modular
 */
public class NMPatch extends PBasicPatch implements PPatch
{
    
    public static final String SLOT_PROPERTY = "slot";

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
    private PNMMorphSection morphSection;
    
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
    private Slot slot;

    private PModuleContainer[] containers;
    
    private LightProcessor lightProcessor;
    
    private EventListenerList listenerList = new EventListenerList();
    
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    
    private ModifiedListener modifiedListener;
    
    private MorphAutoAssigner morphAutoAssigner;
    
    /**
     * Creates a new patch.
     */
    public NMPatch(ModuleDescriptions modules)
    {
        super(modules);
        
        lightProcessor = new LightProcessor();
        
        header = new Header(this);
        noteSet = new NoteSet();
        
        midiControllerSet = new MidiControllerSet(this);
        knobs = new KnobSet(this);
        polyVoiceArea = new VoiceArea(this, true, lightProcessor, "PolyVoiceArea", Format.VALUE_SECTION_VOICE_AREA_POLY);
        commonVoiceArea = new VoiceArea(this, false, lightProcessor, "CommonVoiceArea", Format.VALUE_SECTION_VOICE_AREA_COMMON);
        morphSection = new PNMMorphSection(this);
        
        containers = new PModuleContainer[]{commonVoiceArea, polyVoiceArea, morphSection};
        
        modifiedListener = new ModifiedListener(this);

        morphAutoAssigner = new MorphAutoAssigner(this);
        morphAutoAssigner.installAt(polyVoiceArea);
        morphAutoAssigner.installAt(commonVoiceArea);
        
        setProperty(VERSION, "Nord Modular patch 3.0");
        setProperty(MODIFIED, Boolean.FALSE);
    }
    
    public void installModifiedListener() {
        modifiedListener.install();
    }

    public void uninstallModifiedListener() {
        modifiedListener.uninstall();
    }

    public File getFile()
    {
        try
        {
            return (File) getProperty("file");
        }
        catch (ClassCastException e)
        {
            return null;
        }
    }
    
    public void addAssignmentListener(PAssignmentListener l)
    {
        listenerList.add(PAssignmentListener.class, l);
    }

    public void removeAssignmentListener(PAssignmentListener l)
    {
        listenerList.remove(PAssignmentListener.class, l);
    }
    
    protected void fireAssignmentEvent(PAssignmentEvent event)
    {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        
        if (event.isAssignmentEvent())
        {
            for (int i = listeners.length-2; i>=0; i-=2) 
            {
                if (listeners[i]==PAssignmentListener.class) 
                {
                    // Lazily create the event:
                    ((PAssignmentListener)listeners[i+1]).parameterAssigned(event);
                }
            }
        }
        else
        {
            for (int i = listeners.length-2; i>=0; i-=2) 
            {
                if (listeners[i]==PAssignmentListener.class) 
                {
                    // Lazily create the event:
                    ((PAssignmentListener)listeners[i+1]).parameterDeassigned(event);
                }
            }
        }
    }

    public void addPatchSettingsListener(PPatchSettingsListener l)
    {
        listenerList.add(PPatchSettingsListener.class, l);
    }

    public void removePatchSettingsListener(PPatchSettingsListener l)
    {
        listenerList.remove(PPatchSettingsListener.class, l);
    }
    
    public void firePatchSettingsChanged(boolean ignoreModified)
    {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event

        PPatchSettingsEvent event = null;
        
        for (int i = listeners.length-2; i>=0; i-=2) 
        {
            if (listeners[i]==PPatchSettingsListener.class) 
            {
                if (event == null)
                    event = new PPatchSettingsEvent(this);
                
                if (ignoreModified && listeners[i+1]==modifiedListener)
                    continue;
                
                // Lazily create the event:
                ((PPatchSettingsListener)listeners[i+1]).patchSettingsChanged(event);
            }
        }
    }
    
    public PModuleContainer getModuleContainer(int index)
    {
        return containers[index];
    }

    public int getModuleContainerCount()
    {
        return containers.length;
    }

    public Slot getSlot()
    {
        return slot;
    }
    
    public void setSlot(Slot slot)
    {
        Slot old = this.slot;
        if (old != slot)
        {
            this.slot = slot;
            if (old != null)
            {
                ((NmSlot)old).setPatch(null);
            }
            firePropertyChanged(SLOT_PROPERTY, old, slot);
        }
    }
    
    public PNMMorphSection getMorphSection()
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
        if (NAME.equals(name))
            return getName();
        
        return properties.get(name);
    }
    
    public void setProperty(String name, Object value)
    {
        Object oldValue = properties.get(name);
        
        if (oldValue!=value)
        {
            if (NAME.equals(name))
            {
                setName((String) value);
                return;
            }
            if (value==null)
            {
                properties.remove(name);
                firePropertyChanged(name, oldValue, null);
            }
            else if (!value.equals(oldValue))
            {
                properties.put(name, value);
                firePropertyChanged(name, oldValue, value);
                
                if (name.equals("file") && (value instanceof File))
                {
                    setName(NmUtils.getPatchNameFromfileName((File) value));
                }
            }
        }
    }

    protected void updateName(String oldname, String newname)
    {
        // called by setName(String)
        super.updateName(oldname, newname);
        firePropertyChanged(NAME, oldname, newname);
    }
    
    protected void firePropertyChanged(String name, Object oldValue, Object newValue)
    {
        pcs.firePropertyChange(name, oldValue, newValue);
    }
    
    public void addPropertyChangeListener(PropertyChangeListener l)
    {
        pcs.addPropertyChangeListener(l);
    }
    
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener l)
    {
        pcs.addPropertyChangeListener(propertyName, l);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener l)
    {
        pcs.removePropertyChangeListener(l);
    }
    
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener l)
    {
        pcs.removePropertyChangeListener(propertyName, l);
    }

    public void setModified(boolean changed)
    {
    	boolean oldValue = (Boolean)getProperty(MODIFIED);
        setProperty(MODIFIED, changed ? Boolean.TRUE : Boolean.FALSE);
        firePropertyChanged(MODIFIED, oldValue, changed ? Boolean.TRUE : Boolean.FALSE);
    }
    
    public boolean isModified()
    {
        Boolean b = (Boolean) getProperty(MODIFIED);
        return b != null ? b : false;
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

    public String toString()
    {
        return getClass().getName()
        +"[name="+getName()+",version="+getVersion()+"]";
    }

    public LightProcessor getLightProcessor()
    {
        return lightProcessor;
    }
    
    public String patchFileString() {
    	ByteArrayOutputStream os = new ByteArrayOutputStream();
    	PatchFileWriter fileWriter = new PatchFileWriter(os);
    	PatchExporter export = new PatchExporter();
    	try {
			export.export(this, fileWriter);
			return os.toString();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
    
    public static NMPatch createPatchFromFile(File file) 
    throws ParseException, IOException {
    	NMData data = NMData.sharedInstance();

    	NMPatch patch;
	    	InputStream in = new FileInputStream(file);
			patch = NmUtils.parsePatch(data.getModuleDescriptions(), in);
	    	in.close();

    	patch.setProperty("file", null);
    	patch.setModified(false);
//    	patch.installModifiedListener();

        // enable history
        patch.setEditSupportEnabled(true);
        
    	return patch;
    }
    
    
    public NMPatch createFromFile(File file) throws ParseException, IOException  {
    	return NMPatch.createPatchFromFile(file);
    }

    
    public PPatch newPatchFromFile(File file) {
    	return null;	
    }
    
    public NMPatch createEmptyPatch() {
    	return new NMPatch(getModuleDescriptions());
    }


}
