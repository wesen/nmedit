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

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import net.sf.nmedit.nomad.main.Nomad;
import net.sf.nmedit.nomad.patch.ui.PatchUI;
import net.sf.nmedit.nomad.patch.virtual.Patch;
import net.sf.nmedit.nomad.synth.DeviceIOException;
import net.sf.nmedit.nomad.synth.SynthDevice;
import net.sf.nmedit.nomad.synth.SynthDeviceStateListener;
import net.sf.nmedit.nomad.synth.nord.AbstractNordModularDevice;
import net.sf.nmedit.nomad.synth.nord.NordUtilities;
import net.sf.nmedit.nomad.synth.nord.Slot;
import net.sf.nmedit.nomad.synth.nord.SlotListener;
import net.sf.nmedit.nomad.util.document.Document;
import net.sf.nmedit.nomad.util.document.DocumentListener;

public class SynthDeviceActions implements SlotListener, SynthDeviceStateListener, DocumentListener
{

    private final AbstractNordModularDevice device;
    private final Nomad nomad;
    private final PatchUI[] slotDocs;
    private Action[] downloadToSlotActions;
    private Action[] uploadFromSlotActions;
    private Collection<Action> actions = new ArrayList<Action>();
    private JMenuItem menuSynthConnectionMenuItem = null;
    private Action updloadCurrentSlotAction = new UploadPatchFromCurrentSlotAction();
    private Action downloadToCurrentSlotAction = new DownloadToCurrentSlotAction();

    public SynthDeviceActions(AbstractNordModularDevice device, Nomad nomad)
    {
        this.device = device;
        this.nomad = nomad;
        nomad.getDocumentContainer().addListener(this);
        slotDocs = new PatchUI[device.getSlotCount()];
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
        device.addStateListener(this);
        nomad.addWindowListener( new AutomaticSynthShutdownAction() );
        
        updateActions();
    }

    public boolean setupSynth()
    {
        return NordUtilities.showMidiConfigurationDialog(device);
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
            PatchUI tab = PatchUI.newInstance( p );
            nomad.addPatchUI(tab.getPatch().getName() + " (slot:" + index + ")", tab );
           // if (nomad.getDocumentContainer().getSelection()==null)
                nomad.getDocumentContainer().setSelection( tab );
            /* documents.getSelectedDocument() .setName(documents.getTitleAt( documents
                                    .getSelectedDocumentIndex() ) ); */
            slotDocs[index] = tab;
        }
    }

    public void slotSelected( Slot slot )
    {
        PatchUI doc = slotDocs[slot.getID()];
        if (doc != null) nomad.getDocumentContainer().setSelection( doc );
    }

    public void synthConnectionStateChanged( SynthDevice synth )
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
                    if (device.getIn()==null || device.getOut()==null)
                       if (!setupSynth())
                           return;

                    device.setConnected(true);
                }
                else
                {
                    device.setConnected(false);
                }
            }
            catch (DeviceIOException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    abstract class AbstractDownloadToSlotAction extends AbstractAction
    {
        public void download(Slot slot)
        {
            PatchUI patch = nomad.getActivePatch();
            if (patch!=null)
            {
                slot.setPatch(patch.getPatch());
                try
                {
                    slot.synchronize();
                }
                catch (DeviceIOException e1)
                {
                    e1.printStackTrace();
                }
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
            PatchUI patch = nomad.getActivePatch();
            if (patch!=null)
                download(device.getSlot(device.getActiveSlotID()));
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
            device.getSlot(getSlot().getID()).synchGetPatch();

            /*
            Patch p = device.getSlot(device.getActiveSlotID())
            .getPatch();

            if (p == null)
            {
                System.err.println( "no patch data" );
                return;
            }

            PatchUI tab = PatchUI.newInstance( p );
            nomad.addPatchUI( tab.getPatch().getName(), tab );
            nomad.getDocumentContainer().setSelection( tab );*/
            /*
            documents.getSelectedDocument()
                    .setName(
                            documents.getTitleAt( documents
                                    .getSelectedDocumentIndex() ) );
            */
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
                catch (DeviceIOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

}
