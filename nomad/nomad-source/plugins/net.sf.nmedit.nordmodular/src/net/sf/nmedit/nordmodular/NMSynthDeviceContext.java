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
package net.sf.nmedit.nordmodular;

import java.awt.Event;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;

import net.sf.nmedit.jsynth.Slot;
import net.sf.nmedit.jsynth.SynthException;
import net.sf.nmedit.jsynth.clavia.nordmodular.NmSlot;
import net.sf.nmedit.jsynth.clavia.nordmodular.NordModular;
import net.sf.nmedit.jsynth.event.SlotEvent;
import net.sf.nmedit.jsynth.event.SlotListener;
import net.sf.nmedit.jsynth.event.SlotManagerListener;
import net.sf.nmedit.jsynth.nomad.SynthDeviceContext;
import net.sf.nmedit.jsynth.nomad.SynthPropertiesDialog;
import net.sf.nmedit.jsynth.nomad.SynthPropertiesDialog.DialogPane;
import net.sf.nmedit.jsynth.worker.RequestPatchWorker;
import net.sf.nmedit.nomad.core.Nomad;
import net.sf.nmedit.nomad.core.swing.explorer.ExplorerTree;

public class NMSynthDeviceContext extends SynthDeviceContext 
{

    public NMSynthDeviceContext(ExplorerTree etree, NordModular synth,
            String title)
    {
        super(etree, title);

        setSynth(synth);
        (new PatchOpener(synth)).install();

    }

    public NordModular getSynthesizer()
    {
        return (NordModular) super.getSynth();
    }

    protected EventHandler createEventHandler()
    {
        return new NMEventHandler(this);
    }

    private static class NMEventHandler extends EventHandler
    {

        public NMEventHandler(SynthDeviceContext context)
        {
            super(context);
        }
        
        public JPopupMenu getSlotPopup(SlotLeaf leaf)
        {
            JPopupMenu menu = new JPopupMenu();

            menu.add(new SlotAction(leaf, SlotAction.OPEN_PATCH));
            menu.add(new SlotAction(leaf, SlotAction.NEW_PATCH));
            menu.addSeparator();
            menu.add(new SlotAction(leaf, SlotAction.ENABLE_DISABLE_SLOT));
            menu.add(new SlotAction(leaf, SlotAction.SELECT_SLOT));
            
            return menu;
            
        }
        
    }
    

    private static class SlotAction extends AbstractAction
    {

        /**
         * 
         */
        private static final long serialVersionUID = -8238875184398804108L;
        public static final String OPEN_PATCH = "open.patch";
        public static final String NEW_PATCH = "new.patch";
        public static final String ENABLE_DISABLE_SLOT = "EnableDisable";
        public static final String SELECT_SLOT = "select";
        
        //private SlotLeaf leaf;
        private NmSlot slot;
        
        public SlotAction(SlotLeaf leaf, String command)
        {
            //this.leaf = leaf;
            this.slot = (NmSlot) leaf.getSlot();
            
            putValue(ACTION_COMMAND_KEY, command);
            if (command == OPEN_PATCH)
            {
                putValue(NAME, "Open Patch");
            }
            else if (command == NEW_PATCH)
            {
                putValue(NAME, "New Patch");
                setEnabled(false);
            }
            else if (command == ENABLE_DISABLE_SLOT)
            {
                putValue(NAME, slot.isEnabled() ? "Disable Slot" : "Enable Slot");
            }
            else if (command == SELECT_SLOT)
            {
                putValue(NAME, "Select");
                setEnabled(slot.isEnabled());
            }
        }

        public void actionPerformed(ActionEvent e)
        {
            if (!isEnabled()) return;
            
            String cmd = e.getActionCommand();
            if (cmd == OPEN_PATCH)
            {
                try
                {
                    slot.createRequestPatchWorker().requestPatch();
                }
                catch (SynthException e1)
                {
                    e1.printStackTrace();
                }
            }
            else if (cmd == NEW_PATCH);
            else if (cmd == ENABLE_DISABLE_SLOT)
                slot.requestEnableSlot(!slot.isEnabled());
            else if (cmd == SELECT_SLOT)
                slot.requestSelectSlot();
        }
        
    }
    
    
    private static class PatchOpener implements SlotManagerListener,
        SlotListener
    {
        private NordModular synth;

        private PatchOpener(NordModular synth)
        {
            this.synth = synth;
        }

        public void install()
        {
            synth.getSlotManager().addSlotManagerListener(this);
            
            for (int i=0;i<synth.getSlotCount();i++)
            {
                installSlot(synth.getSlot(i));
            }
        }

        public void slotAdded(SlotEvent e)
        {
            installSlot((NmSlot) e.getSlot());
        }

        public void slotRemoved(SlotEvent e)
        {
            uninstallSlot((NmSlot) e.getSlot());
        }

        public void installSlot(NmSlot slot)
        {
            slot.addSlotListener(this);
        }
        
        public void uninstallSlot(NmSlot slot)
        {
            slot.removeSlotListener(this);
        }

        public void newPatchInSlot(SlotEvent e)
        {
            NmSlot slot = (NmSlot) e.getSlot();
            uninstallDoc(slot);
            
            if (slot.getPatch()!= null)
                installDoc(slot);
        }

        private void uninstallDoc(NmSlot slot)
        {
            PatchDocument doc = docMap.get(slot.getSlotId());
            if (doc == null)
                return ;
            
            Nomad.sharedInstance()
            .getDocumentManager().remove(doc);
        }

        private void installDoc(NmSlot slot)
        {
            PatchDocument doc;
            try
            {
                doc = NmFileService.createPatchDoc(slot.getPatch());
            }
            catch(Exception e)
            {
                e.printStackTrace();
                return;
            }
            docMap.put(slot.getSlotId(), doc);
         
            Nomad.sharedInstance()
            .getDocumentManager().add(doc);
        }

        private Map<Integer, PatchDocument> docMap = new HashMap<Integer, PatchDocument>();
        
    }

    protected void addForms(final SynthPropertiesDialog spd)
    {
        super.addForms(spd);
        DialogPane dp = new SSF(getSynthesizer());
        dp.install(spd);
    }
    
    protected static class SSF extends DialogPane
    {

        public SSF(NordModular synth)
        {
            super(synth, "synthsettings", "Settings");
        }

        @Override
        protected JComponent createDialogComponent()
        {
            return new SynthSettingsFrm();
        }

    }

    /*
    private boolean showSettings2()
    {
        NordModular synth = getSynthesizer();

        MidiPlug inPlug = synth.getPCInPort().getPlug();
        MidiDevice.Info in = inPlug != null ? inPlug.getDeviceInfo() : null;

        MidiPlug outPlug = synth.getPCOutPort().getPlug();
        MidiDevice.Info out = outPlug != null ? outPlug.getDeviceInfo() : null;
        
        NomadMidiDialog dialog = new NomadMidiDialog(Nomad.sharedInstance().getWindow(), "MIDI");
        NomadMidiDialogFrmHandler form = dialog.getForm();
        form.setPreviousInput(in);
        form.setPreviousOutput(out);
        
        dialog.setModal(true);
        if (dialog.showDialog() == NomadMidiDialog.APPROVE_OPTION)
        {

            in = form.getSelectedInput();
            out = form.getSelectedOutput();

            if (in == null || out == null)
                return false;
            
            try
            {
                synth.getPCInPort().setPlug(new MidiPlug(in));
                synth.getPCOutPort().setPlug(new MidiPlug(out));
            }
            catch (SynthException e)
            {
                // TODO Auto-generated catch block
                
                return false;
            }
            return true;
        }
        
        return false;
    }*/
    

    protected void processEvent(Event event, Slot slot)
    {
        RequestPatchWorker worker = slot.createRequestPatchWorker(); 
        try
        {
            worker.requestPatch();
        }
        catch (SynthException e)
        {
            e.printStackTrace();
        }
    }

    
}

