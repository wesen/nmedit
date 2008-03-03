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
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sf.nmedit.jpatch.clavia.nordmodular.NMPatch;
import net.sf.nmedit.jsynth.Slot;
import net.sf.nmedit.jsynth.SynthException;
import net.sf.nmedit.jsynth.clavia.nordmodular.NmSlot;
import net.sf.nmedit.jsynth.clavia.nordmodular.NordModular;
import net.sf.nmedit.jsynth.clavia.nordmodular.worker.StorePatchInSlotWorker;
import net.sf.nmedit.jsynth.event.SlotEvent;
import net.sf.nmedit.jsynth.event.SlotListener;
import net.sf.nmedit.jsynth.event.SlotManagerListener;
import net.sf.nmedit.jsynth.event.SynthesizerEvent;
import net.sf.nmedit.jsynth.event.SynthesizerStateListener;
import net.sf.nmedit.jsynth.nomad.forms.SynthObjectForm;
import net.sf.nmedit.jsynth.nomad.forms.SynthPropertiesDialog;
import net.sf.nmedit.jsynth.nomad.forms.SynthPropertiesDialog.DialogPane;
import net.sf.nmedit.jsynth.worker.RequestPatchWorker;
import net.sf.nmedit.jtheme.clavia.nordmodular.JTNMPatch;
import net.sf.nmedit.nmutils.swing.WorkIndicator;
import net.sf.nmedit.nomad.core.Nomad;
import net.sf.nmedit.nomad.core.forms.ExceptionDialog;
import net.sf.nmedit.nomad.core.swing.document.DefaultDocumentManager;
import net.sf.nmedit.nomad.core.swing.document.Document;
import net.sf.nmedit.nomad.core.swing.document.DocumentManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class NMSynthDeviceContext extends SynthObjectForm<NordModular>
{

    /**
     * 
     */
    private static final long serialVersionUID = 6337542133829436194L;


    //private Collection<Action> SpecialActions = null;
    
    public NMSynthDeviceContext(NordModular synth)
    {
        super(synth);
        (new PatchOpener(synth)).install();
    }


    protected NmEventHandler createEventHandler()
    {
        return new NmEventHandler();
    }
    
    public void openOrSelectPatch(Slot slot)
    {
        NmSlot nmslot = (NmSlot) slot;
        
        NMPatch patch = nmslot.getPatch();
        
        if (patch != null)
        {
            // find out if patch is open
            
            DefaultDocumentManager dm = Nomad.sharedInstance().getDocumentManager();
            
            for (Document d: dm.getDocuments())
            {
                if ( d instanceof PatchDocument )
                {
                    PatchDocument pd = (PatchDocument) d;
                    if (pd.getPatch() == patch)
                    {
                        // found -> select
                        
                        dm.setSelection(d);
                        
                        return ;
                    }
                }
            }
            
            // not found -> create document

            try
            {
                final PatchDocument pd = NmFileService.createPatchDoc(patch);
    
                SwingUtilities.invokeLater(new Runnable(){
                    public void run()
                    {
                        DocumentManager dm = 
                        Nomad.sharedInstance()
                        .getDocumentManager();
                        dm.add(pd);
                        dm.setSelection(pd);
                        
                    }
                });

            }
            catch (Exception e)
            {
                
                Log log = LogFactory.getLog(getClass());
                if (log.isWarnEnabled())
                {
                    log.warn(e);
                }
                

                ExceptionDialog.showErrorDialog(
                        Nomad.sharedInstance().getWindow().getRootPane(),
                        
                        e.getMessage(), "could not open patch", e);
                
                
                return;
            }
            
            return;
        }
        
        try
        {
            slot.createRequestPatchWorker().requestPatch();
        }
        catch (SynthException e1)
        {
            e1.printStackTrace();
        }
    }

    protected class NmEventHandler extends EventHandler
    {
        public void install()
        {
            super.install();
            //getSynthesizer().addPropertyChangeListener(this);
        }
/*
        public void propertyChange(PropertyChangeEvent evt)
        {
            super.propertyChange(evt);
            s
            
            
        }*/
    }

    protected SlotObject<NordModular> createSlotObject(Slot slot)
    {
        return new NmSlotObject(this, slot);
    }
    
    protected static class NmSlotObject extends SlotObject<NordModular>
    {

        public NmSlotObject(NMSynthDeviceContext sdc, Slot slot)
        {
            super(sdc, slot);
        }
        
        public NmSlot getSlot()
        {
            return (NmSlot) super.getSlot();
        }

    }

    protected void dropTransfer(SlotObject<NordModular> s, DropTargetDropEvent dtde)
    {
        if (!acceptsDropData(s, dtde.getCurrentDataFlavors()))
        {
            dtde.rejectDrop();
            return;
        }
        
        if (!dtde.isLocalTransfer())
        {
            dtde.rejectDrop();
            return;
        }
        
        try
        {
        NMPatch patch = (NMPatch) dtde
            .getTransferable()
            .getTransferData(JTNMPatch.nmPatchFlavor);
        
        if (patch.getSlot() != null)
            patch.setSlot(null);
        
          (new StorePatchInSlotWorker((NmSlot)s.getSlot(), patch)).store();
        }
        catch (IOException e)
        {
            dtde.rejectDrop();
        }
        catch (UnsupportedFlavorException e)
        {
            dtde.rejectDrop();
        }
    }
    
    protected boolean acceptsDropData(SlotObject<NordModular> s, DataFlavor[] flavors)
    {
        for (int i=0;i<flavors.length;i++)
            if (JTNMPatch.nmPatchFlavor.equals(flavors[i]))
                return true;
        return false;
    }

    
    /*
    protected EventHandler createEventHandler()
    {
        return new NMEventHandler(this);
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
  */  

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
        
        public SlotAction(SlotObject leaf, String command)
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
                slot.setEnabled(!slot.isEnabled());
            else if (cmd == SELECT_SLOT)
                slot.setSelected(true);
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
            //NMPatch oldpatch = (NMPatch) e.getOldPatch();
            final NMPatch newpatch = (NMPatch) e.getNewPatch(); 
    
            if (newpatch == null)
                return;
                
            Runnable run = new Runnable()
            {
                public void run()
                {
                        NmFileService.selectOrOpen(newpatch);
                }
            };

            run = WorkIndicator.create(Nomad.sharedInstance().getWindow(), run);
            SwingUtilities.invokeLater(run);
        }
    }

    protected void addForms(final SynthPropertiesDialog<NordModular> spd)
    {
        super.addForms(spd);
        DialogPane<NordModular> dp = new SSF(getSynthesizer());
        dp.install(spd);
    }
    
    protected static class SSF extends DialogPane<NordModular> 
        implements PropertyChangeListener, ChangeListener, SynthesizerStateListener
    {

        private SynthSettingsFrm frm;
        
        public SSF(NordModular synth)
        {
            super(synth, "synthsettings", "Settings");
        }
        

        public void dispose()
        {
            super.dispose();
            synth.removePropertyChangeListener(this);
            synth.removeSynthesizerStateListener(this);
        }

        private SynthSettingsFrm getForm(boolean create)
        {
            if (frm == null && create)
            {
                frm = new SynthSettingsFrm();
                setFormControlOptions();
                updateForm();
                synth.addPropertyChangeListener(this);
                synth.addSynthesizerStateListener(this);
                installFormListeners();
                updateFormEnabledState();
            }
            return frm;
        }
        
        private SpinnerNumberModel numberModel(int min, int max, int value)
        {
            return new SpinnerNumberModel(value, min, max, 1);
        }

        private void setFormControlOptions()
        {
            // change listener
            frm.spMIDIClockRate.setModel(numberModel(31, 239, 120));
            frm.spMIDIVelScaleMin.setModel(numberModel(0, 127, 0));
            frm.spMIDIVelScaleMax.setModel(numberModel(0, 127, 127));
            frm.spGlobalSyncBeats.setModel(numberModel(1, 32, 4));
            frm.spMasterTune.setModel(numberModel(-127, 127, 0));
            frm.spChannelSlotA.setModel(numberModel(1, 16, 1)); 
            frm.spChannelSlotB.setModel(numberModel(1, 16, 2));
            frm.spChannelSlotC.setModel(numberModel(1, 16, 3));
            frm.spChannelSlotD.setModel(numberModel(1, 16, 4));
        }

        private void installFormListeners()
        {
            // action listener
            frm.jtSynthName.addActionListener(this);
            frm.cbLEDsActive.addActionListener(this);
            frm.cbLocalOff.addActionListener(this);
            frm.cbProgramChangeReceive.addActionListener(this);
            frm.cbProgramChangeSend.addActionListener(this);
            frm.rbKBModeSelectedSlots.addActionListener(this);
            frm.rbKBModeActiveSlot.addActionListener(this);
            frm.rbKnobModeHook.addActionListener(this);
            frm.rbKnobModeImmediate.addActionListener(this);
            frm.rbMIDIClockInternal.addActionListener(this);
            frm.rbMIDIClockExternal.addActionListener(this);
            frm.rbPedalPolarityInverted.addActionListener(this);
            frm.rbPedalPolarityNormal.addActionListener(this);
            // change listener
            frm.spMIDIClockRate.addChangeListener(this);
            frm.spMIDIVelScaleMin.addChangeListener(this);
            frm.spMIDIVelScaleMax.addChangeListener(this);
            frm.spGlobalSyncBeats.addChangeListener(this);
            frm.spMasterTune.addChangeListener(this);
            frm.spActiveSlotA.addChangeListener(this); 
            frm.spActiveSlotB.addChangeListener(this);
            frm.spActiveSlotC.addChangeListener(this);
            frm.spActiveSlotD.addChangeListener(this);
            frm.spChannelSlotA.addChangeListener(this); 
            frm.spChannelSlotB.addChangeListener(this);
            frm.spChannelSlotC.addChangeListener(this);
            frm.spChannelSlotD.addChangeListener(this);
        }

        public void actionPerformed(ActionEvent e)
        {
            if (frm == null)
            {
                // forward event to super class
                super.actionPerformed(e);
                return ;
            }
            Object src = e.getSource();
            if (src == frm.jtSynthName)
            {
                String text = frm.jtSynthName.getText();
                if (text == null) text = "";
                if (text.length()>16)
                {
                    frm.jtSynthName.setText(text.substring(0, 16));
                    return;
                }
                
                synth.setName(text);
                synth.syncSettings();
            }
            else if (src==frm.cbLEDsActive)
            {
                synth.setLEDsActive(frm.cbLEDsActive.isSelected());
            }
            else if (src==frm.cbLocalOff)
            {
                synth.setLocalOn(!frm.cbLocalOff.isSelected());
            }
            else if (src==frm.cbProgramChangeReceive)
            {
                synth.setProgramChangeReceive(frm.cbProgramChangeReceive.isSelected());
            }
            else if (src==frm.cbProgramChangeSend)
            {
                synth.setProgramChangeSend(frm.cbProgramChangeSend.isSelected());
            }
            else if (src==frm.rbKBModeSelectedSlots||src==frm.rbKBModeActiveSlot)
            {
                boolean selectedSlots = frm.rbKBModeSelectedSlots.isSelected();
                boolean activeSlot = frm.rbKBModeActiveSlot.isSelected();
                if (selectedSlots == activeSlot)
                {
                    // intermediate state, another event will occure immediatelly
                    return;
                }
                
                synth.setKeyboardMode(selectedSlots);
            }
            else if (src==frm.rbKnobModeHook||src==frm.rbKnobModeImmediate)
            {
                boolean hook = frm.rbKnobModeHook.isSelected();
                boolean immediate = frm.rbKnobModeImmediate.isSelected();
                if (hook == immediate)
                {
                    // intermediate state, another event will occure immediatelly
                    return;
                }
                synth.setKnobMode(hook);
            }
            else if (src==frm.rbMIDIClockInternal||src==frm.rbMIDIClockExternal)
            {
                boolean intern = frm.rbMIDIClockInternal.isSelected();
                boolean extern = frm.rbMIDIClockExternal.isSelected();
                if (intern == extern)
                {
                    // intermediate state, another event will occure immediatelly
                    return;
                }
                synth.setMidiClockSource(intern);
            }
            else if (src==frm.rbPedalPolarityInverted||src==frm.rbPedalPolarityNormal)
            {
                boolean inverted = frm.rbPedalPolarityInverted.isSelected();
                boolean normal = frm.rbPedalPolarityNormal.isSelected();
                if (inverted == normal)
                {
                    // intermediate state, another event will occure immediatelly
                    return;
                }
                synth.setPedalPolarity(inverted);
            }
            else
            {
                // forward event to super class
                super.actionPerformed(e);
            }
            synth.syncSettings();
        }
        
        private int val(JSpinner spinner)
        {
            return ((Integer)(spinner.getValue())).intValue();
        }

        private boolean slotExists(int index)
        {
            return index>=0 && index<synth.getSlotCount();
        }

        public void stateChanged(ChangeEvent e)
        {
            if (frm == null)
                return ;

            Object src = e.getSource();
            
            if (src==frm.spMIDIClockRate)
            {
                synth.setMidiClockBPM(val(frm.spMIDIClockRate));
            }
            else if (src==frm.spMIDIVelScaleMin)
            {
                synth.setMidiVelScaleMin(val(frm.spMIDIVelScaleMin));
            }
            else if (src==frm.spMIDIVelScaleMax)
            {
                synth.setMidiVelScaleMax(val(frm.spMIDIVelScaleMax));
            }
            else if (src==frm.spGlobalSyncBeats)
            {
                synth.setGlobalSync(val(frm.spGlobalSyncBeats)-1);
            }
            else if (src==frm.spMasterTune)
            {
                synth.setMasterTune(val(frm.spMasterTune));
            }
            else if (src==frm.spActiveSlotA || src == frm.spChannelSlotA) 
            {
                if (slotExists(0))
                {
                    boolean selected = frm.spActiveSlotA.isSelected();
                    frm.spChannelSlotA.setEnabled(selected);
                    if (frm.spChannelSlotA==src&&(!selected))
                        return;
                    int number = val(frm.spChannelSlotA)-1;
                    synth.setMidiChannel(0, selected ? number : 16);
                }
            }
            else if (src==frm.spActiveSlotB || src == frm.spChannelSlotB) 
            {
                if (slotExists(1))
                {
                    boolean selected = frm.spActiveSlotB.isSelected();
                    frm.spChannelSlotB.setEnabled(selected);
                    if (frm.spChannelSlotB==src&&(!selected))
                        return;
                    int number = val(frm.spChannelSlotB)-1;
                    synth.setMidiChannel(1, selected ? number : 16);
                }
            }
            else if (src==frm.spActiveSlotC || src == frm.spChannelSlotC) 
            {
                if (slotExists(2))
                {
                    boolean selected = frm.spActiveSlotC.isSelected();
                    frm.spChannelSlotC.setEnabled(selected);
                    if (frm.spChannelSlotC==src&&(!selected))
                        return;
                    int number = val(frm.spChannelSlotC)-1;
                    synth.setMidiChannel(2, selected ? number : 16);
                }
            }
            else if (src==frm.spActiveSlotD || src == frm.spChannelSlotD) 
            {
                if (slotExists(3))
                {
                    boolean selected = frm.spActiveSlotD.isSelected();
                    frm.spChannelSlotD.setEnabled(selected);
                    if (frm.spChannelSlotD==src&&(!selected))
                        return;
                    int number = val(frm.spChannelSlotD)-1;
                    synth.setMidiChannel(3, selected ? number : 16);
                } 
            }
            synth.syncSettings();
        }

        public void propertyChange(PropertyChangeEvent evt)
        {
            String name = evt.getPropertyName();
            if ("settings".equals(name))
            {
                // settings changed at synth
                updateForm();
                return;
            }
        }
        
        private void updateForm()
        {
            frm.jtSynthName.setText(synth.getName());
            frm.cbLEDsActive.setSelected(synth.isLEDsActive());
            frm.cbLocalOff.setSelected(!synth.isLocalOn());

            frm.cbProgramChangeReceive.setSelected(synth.getProgramChangeReceive());
            frm.cbProgramChangeSend.setSelected(synth.getProgramChangeSend());

            frm.rbKBModeSelectedSlots.setSelected(synth.getKeyboardMode());
            frm.rbKBModeActiveSlot.setSelected(!synth.getKeyboardMode());
           
            frm.rbKnobModeHook.setSelected(synth.getKnobMode());
            frm.rbKnobModeImmediate.setSelected(!synth.getKnobMode());

            frm.rbMIDIClockInternal.setSelected(synth.getMidiClockSource());
            frm.rbMIDIClockExternal.setSelected(!synth.getMidiClockSource());

            frm.rbPedalPolarityInverted.setSelected(synth.getPedalPolarity());
            frm.rbPedalPolarityNormal.setSelected(!synth.getPedalPolarity());

            frm.spMIDIClockRate.setValue(synth.getMidiClockBPM());
            
            frm.spMIDIVelScaleMin.setValue(synth.getMidiVelScaleMin());
            frm.spMIDIVelScaleMax.setValue(synth.getMidiVelScaleMax());

            frm.spGlobalSyncBeats.setValue(1+synth.getGlobalSync());
            frm.spMasterTune.setValue(synth.getMasterTune());

            JCheckBox[] active = {frm.spActiveSlotA, frm.spActiveSlotB, frm.spActiveSlotC, frm.spActiveSlotD};
            JSpinner[] chan = {frm.spChannelSlotA, frm.spChannelSlotB, frm.spChannelSlotC, frm.spChannelSlotD};

            for (int i=0;i<4;i++)
            {
                boolean slotExists = i<synth.getSlotCount();
              
                if (!slotExists)
                {
                    active[i].setEnabled(false);
                    chan[i].setEnabled(false);
                }
                else
                {
                    int channel = 1+synth.getMidiChannel(i);
                    boolean validChannel = channel <= 16;
                    chan[i].setEnabled(validChannel);
                    active[i].setEnabled(true);
                    if (active[i].isSelected()!=validChannel)
                        active[i].setSelected(validChannel);
                    if (validChannel)
                        chan[i].setValue(channel);
                }
            }
        }

        @Override
        protected JComponent createDialogComponent()
        {
            JComponent frm = getForm(true);
            JScrollPane sc = new JScrollPane(frm);
            sc.setBorder(null);
            return sc;
        }


        public void synthConnectionStateChanged(SynthesizerEvent e)
        {
            updateFormEnabledState();
        }
        
        public void updateFormEnabledState()
        {
            if (frm == null)
                return ;
            
            boolean enabled = synth.isConnected();
            frm.setEnabled(enabled);
            frm.cbLEDsActive.setEnabled(enabled);
            frm.cbLocalOff.setEnabled(enabled);
            frm.cbProgramChangeReceive.setEnabled(enabled);
            frm.cbProgramChangeSend.setEnabled(enabled);
            frm.rbKBModeActiveSlot.setEnabled(enabled);
            frm.rbKBModeSelectedSlots.setEnabled(enabled);
            frm.rbKnobModeHook.setEnabled(enabled);
            frm.rbKnobModeImmediate.setEnabled(enabled);
            frm.rbMIDIClockExternal.setEnabled(enabled);
            frm.rbMIDIClockInternal.setEnabled(enabled);
            frm.rbPedalPolarityInverted.setEnabled(enabled);
            frm.rbPedalPolarityNormal.setEnabled(enabled);
            frm.jtSynthName.setEnabled(enabled);
            frm.spActiveSlotA.setEnabled(enabled);
            frm.spActiveSlotB.setEnabled(enabled);
            frm.spActiveSlotC.setEnabled(enabled);
            frm.spActiveSlotD.setEnabled(enabled);
            frm.spChannelSlotA.setEnabled(enabled);
            frm.spChannelSlotB.setEnabled(enabled);
            frm.spChannelSlotC.setEnabled(enabled);
            frm.spChannelSlotD.setEnabled(enabled);
            frm.spGlobalSyncBeats.setEnabled(enabled);
            frm.spMasterTune.setEnabled(enabled);
            frm.spMIDIClockRate.setEnabled(enabled);
            frm.spMIDIVelScaleMax.setEnabled(enabled);
            frm.spMIDIVelScaleMin.setEnabled(enabled);
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
        NmSlot nmslot = (NmSlot) slot;
        NMPatch patch = nmslot.getPatch();
        if (patch==null)
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
        else
        {
            NmFileService.selectOrOpen(patch);
        }
    }

}

