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
 * Created on May 24, 2006
 */
package net.sf.nmedit.nomad.main.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.sound.midi.MidiDevice;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Patch;
import net.sf.nmedit.jsynth.SynthException;
import net.sf.nmedit.jsynth.clavia.nordmodular.v3_03.NordModular;
import net.sf.nmedit.jsynth.clavia.nordmodular.v3_03.Slot;
import net.sf.nmedit.jsynth.clavia.nordmodular.v3_03.SlotListener;
import net.sf.nmedit.jsynth.event.SynthStateChangeEvent;
import net.sf.nmedit.jsynth.event.SynthStateListener;
import net.sf.nmedit.nomad.core.application.Const;
import net.sf.nmedit.nomad.core.nomad.NomadEnvironment;
import net.sf.nmedit.nomad.main.Nomad;
import net.sf.nmedit.nomad.main.dialog.NomadMidiDialog;
import net.sf.nmedit.nomad.patch.ui.PatchDocument;
import net.sf.nmedit.nomad.patch.ui.PatchUI;
import net.sf.nmedit.nomad.util.document.Document;
import net.sf.nmedit.nomad.util.document.DocumentListener;

public class SynthDeviceActions implements SlotListener, SynthStateListener, DocumentListener
{

    private final NordModular device;
    private final Nomad nomad;
    private final PatchDocument[] slotDocs;
    private Action[] downloadToSlotActions;
    private Action[] uploadFromSlotActions;
    private Collection<Action> actions = new ArrayList<Action>();
    private JMenuItem menuSynthConnectionMenuItem = null;
    private Action updloadCurrentSlotAction = new UploadPatchFromCurrentSlotAction();
    private Action downloadToCurrentSlotAction = new DownloadToCurrentSlotAction();

    public SynthDeviceActions(NordModular device, Nomad nomad)
    {
        this.device = device;
        this.nomad = nomad;
        nomad.getDocumentContainer().addListener(this);
        slotDocs = new PatchDocument[device.getSlotCount()];
        Arrays.fill( slotDocs, null );

        downloadToSlotActions = new Action[device.getSlotCount()];
        uploadFromSlotActions = new Action[device.getSlotCount()];
        for (int s=0;s<device.getSlotCount();s++)
        {
            downloadToSlotActions[s] = new DownloadToSlotAction(device.getSlot(s));
            actions.add(downloadToSlotActions[s]);
            uploadFromSlotActions[s] = new UploadFromSlotAction(device.getSlot(s));
            actions.add(uploadFromSlotActions[s]);
        }
        actions.add(updloadCurrentSlotAction);
        actions.add(downloadToCurrentSlotAction);
        
        device.addSlotListener(this);
        device.addSynthStateListener(this);
        nomad.addWindowListener( new AutomaticSynthShutdownAction() );
        
        updateActions();
    }

    public boolean setupSynth()
    {
        return showMidiConfigurationDialog(device);
    }

    public static boolean showMidiConfigurationDialog(NordModular dev)
    {

        final String KEY_MIDI_IN_DEVICE =  Const.CUSTOM_PROPERTY_PREFIX_STRING+"synth.midi.in";
        final String KEY_MIDI_OUT_DEVICE =  Const.CUSTOM_PROPERTY_PREFIX_STRING+"synth.midi.out";

        MidiDevice.Info midiIn = dev.getMidiIn();
        MidiDevice.Info midiOut = dev.getMidiOut();
        
        NomadMidiDialog dlg = new NomadMidiDialog(midiIn, midiOut);
            
        if (midiIn==null) dlg.setInputDevice(NomadEnvironment.getProperty(KEY_MIDI_IN_DEVICE));
        if (midiOut==null) dlg.setOutputDevice(NomadEnvironment.getProperty(KEY_MIDI_OUT_DEVICE));

        dlg.invoke();
        
        if (dlg.isOkResult()) {
            midiIn = dlg.getInputDevice();
            midiOut = dlg.getOutputDevice();

            if (midiIn!=null && midiOut!=null)
            {
                dev.setMidiIn(midiIn);
                dev.setMidiOut(midiOut);
                
                NomadEnvironment.setProperty(KEY_MIDI_IN_DEVICE, midiIn.getName());
                NomadEnvironment.setProperty(KEY_MIDI_OUT_DEVICE, midiOut.getName());
                return true;
            }
        }
        
        return false;
    }
    public void newPatchInSlot( Slot slot )
    {
        int index = slot.getID();
        if (slotDocs[index] != null)
        {
            nomad.getDocumentContainer().remove((Document) slotDocs[index] );
            slotDocs[index] = null;
        }
        Patch p = slot.getPatch();
        if (p != null)
        {
            PatchDocument doc = new PatchDocument(PatchUI.newInstance( p ));
            doc.setSlot(slot);
            nomad.addPatchDocument(doc);
            nomad.getDocumentContainer().setSelection( doc );
            slotDocs[index] = doc;
        }
    }

    public void slotSelected( Slot slot )
    {
        PatchDocument doc = slotDocs[slot.getID()];
        if (doc != null) nomad.getDocumentContainer().setSelection( doc );
    }

    public void synthStateChanged(SynthStateChangeEvent e)
    {
        updateActions();
    }
    
    private void updateActions()
    {
        boolean connected = device.isConnected();
        if (menuSynthConnectionMenuItem!=null)
            this.menuSynthConnectionMenuItem.setText( connected ? "Disconnect" : "Connect" );
        for (Action action:actions)
        {
            action.setEnabled(connected);
        } 
    }

    class SynthConnectionMenuItemListener implements ActionListener
    {
        public void actionPerformed( ActionEvent arg0 )
        {
            try
            {
                if (!device.isConnected())
                {
                    if (device.getMidiIn()==null || device.getMidiOut()==null)
                       if (!setupSynth())
                           return;

                    device.setConnected(true);
                }
                else
                {
                    device.setConnected(false);
                }
            }
            catch (SynthException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    abstract class AbstractDownloadToSlotAction extends AbstractAction
    {
        public void download(Slot slot)
        {
            PatchDocument doc = nomad.getActivePatch();
            if (doc!=null)
            {
                if (doc.getSlot()!=slot)
                {
                    slot.setPatch(doc.getPatch());
                }
                slot.sendPatchMessage();
            }
        }
        
    }

    class DownloadToCurrentSlotAction extends AbstractDownloadToSlotAction
    {
        public DownloadToCurrentSlotAction()
        {
            putValue(NAME, "Download To Current Slot");
        }

        public void actionPerformed( ActionEvent e )
        {
            PatchDocument doc = nomad.getActivePatch();
            if (doc!=null)
            {
               download(device.getSlot(device.getActiveSlotID()));
            }
        }
    }

    class DownloadToSlotAction extends AbstractDownloadToSlotAction
    {
        private Slot slot;

        public DownloadToSlotAction(Slot slot)
        {
            this.slot = slot;
            putValue(NAME, "Download To "+slot.getName());
        }
        
        public void actionPerformed( ActionEvent e )
        {
            download(slot);
        }
    }
    
    abstract class AbstractUploadPatchFromSlotAction extends AbstractAction
    {
        public void actionPerformed( ActionEvent e )
        {
            device.getSlot(getSlot().getID()).sendGetPatchMessage();
        }
        
        public abstract Slot getSlot();
    }

    class UploadPatchFromCurrentSlotAction extends AbstractUploadPatchFromSlotAction
    {
        public UploadPatchFromCurrentSlotAction()
        {
            putValue(NAME, "Upload Active Slot");
        }
        
        @Override
        public Slot getSlot()
        {
            return device.getSlot(device.getActiveSlotID());
        }
    }

    class UploadFromSlotAction extends AbstractUploadPatchFromSlotAction
    {
        private Slot slot;

        public UploadFromSlotAction(Slot slot)
        {
            this.slot = slot;
            putValue(NAME, "Upload "+slot.getName());
        }
        
        @Override
        public Slot getSlot()
        {
            return slot;
        }
    }

    public void createSynthMenuItems(JMenu mnMenu)
    {
        mnMenu.add( new SynthSetupAction( this ) );
        menuSynthConnectionMenuItem = mnMenu.add( "Connect" );
        menuSynthConnectionMenuItem
                .addActionListener( new SynthConnectionMenuItemListener() );
        mnMenu.addSeparator();
        for (Action action : uploadFromSlotActions)
        {
            mnMenu.add(action);
        }
        mnMenu.addSeparator();
        mnMenu.add( updloadCurrentSlotAction );
    }

    public void createPatchMenuItems(JMenu mnMenu)
    {
        for (Action action : downloadToSlotActions)
        {
            mnMenu.add(action);
        }
        mnMenu.addSeparator();
        mnMenu.add(downloadToCurrentSlotAction);
    }

    public Nomad getNomad()
    {
        return nomad;
    }

    public void documentSelected( Document document )
    {
        // TODO Auto-generated method stub
        
    }

    public void documentRemoved( Document document )
    {
        // TODO Auto-generated method stub
        
    }

    public void documentAdded( Document document )
    {
        // TODO Auto-generated method stub
        
    }

    class AutomaticSynthShutdownAction extends WindowAdapter
    {
        public void windowClosing( WindowEvent event )
        {
            if (device.isConnected())
            {
                System.out.println( "Closing midi connection." );
                try
                {
                    device.setConnected(false);
                }
                catch (SynthException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

}
