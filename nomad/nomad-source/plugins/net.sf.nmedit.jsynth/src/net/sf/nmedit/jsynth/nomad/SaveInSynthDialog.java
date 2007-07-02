
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
package net.sf.nmedit.jsynth.nomad;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;

import net.sf.nmedit.jsynth.Slot;
import net.sf.nmedit.jsynth.Synthesizer;

public class SaveInSynthDialog extends JDialog 
{

    /**
     * 
     */
    private static final long serialVersionUID = 4854099017201533301L;
    private Collection<Synthesizer> synths;
    private SaveInSynthFrm form;
    
    public SaveInSynthDialog(Collection<Synthesizer> synths)
    {
        this.synths = synths;
        setTitle("Save In Synthesizer");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        createComponents();
    }
    
    private void createComponents()
    {
        form = new SaveInSynthFrm();
        
        form.cbSynth.setModel(new DefaultComboBoxModel(synths.toArray()));

        form.cbSaveInSlot.setSelected(true);
        form.cbSaveInBank.setSelected(false);
        form.cbSlot.setRenderer(new CustomRenderer());
        form.cbSynth.setRenderer(new CustomRenderer());
        form.cbBank.setRenderer(new CustomRenderer());

        getRootPane().setDefaultButton(form.btnSave);
        
        ActionHandler actionHandler = new ActionHandler();

        form.cbSynth.setActionCommand(ActionHandler.SELECT_SYNTH);
        form.cbSlot.setActionCommand(ActionHandler.SELECT_BANK_SLOT);
        form.cbBank.setActionCommand(ActionHandler.SELECT_BANK_SLOT);
        form.btnSave.setActionCommand(ActionHandler.SAVE);
        form.btnCancel.setActionCommand(ActionHandler.CANCEL);
        form.cbSaveInSlot.setActionCommand(ActionHandler.TOGGLE_SLOT_SELECT);
        form.cbSaveInBank.setActionCommand(ActionHandler.TOGGLE_BANK_SELECT);
        
        form.cbSynth.addActionListener(actionHandler);
        form.cbSlot.addActionListener(actionHandler);
        form.cbBank.addActionListener(actionHandler);
        form.btnSave.addActionListener(actionHandler);
        form.btnCancel.addActionListener(actionHandler);
        form.cbSaveInSlot.addActionListener(actionHandler);
        form.cbSaveInBank.addActionListener(actionHandler);

        JPanel cp = new JPanel();
        
        setContentPane(cp);
        cp.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(form);
        
        updateData();
        updateState();
        pack();
    }
    
    public void invoke()
    {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screen.width-getWidth())/2, (screen.height-getHeight())/2);
        setModal(true);
        setVisible(true);
    }
    
    private class ActionHandler implements ActionListener
    {
        public static final String SELECT_SYNTH = "select.synth";
        public static final String SELECT_BANK_SLOT = "select.bankslot";
        public static final String SAVE = "save";
        public static final String CANCEL = "cancel";
        public static final String TOGGLE_SLOT_SELECT = "toggle.slot.select";
        public static final String TOGGLE_BANK_SELECT = "toggle.bank.select";
        
        public void actionPerformed(ActionEvent e)
        {
            String cmd = e.getActionCommand();
            if (cmd == SELECT_SYNTH)
                synthSelected();
            else if (cmd == SELECT_BANK_SLOT)
                updateState();
            else if (cmd == TOGGLE_BANK_SELECT)
                updateState();
            else if (cmd == TOGGLE_SLOT_SELECT)
                updateState();
            else if (cmd == SAVE)
                save();
            else if (cmd == CANCEL)
                cancel();
        }
        
    }

    private void synthSelected()
    {
        updateData();
        updateState();
    }

    private void updateData()
    {
        form.cbSlot.removeAllItems();
        form.cbBank.removeAllItems();
        Synthesizer synth = (Synthesizer) form.cbSynth.getSelectedItem();
        
        ComboBoxModel model = getSlotModel(synth);
        if (model != null)
            form.cbSlot.setModel(model);
    }
    
    private void updateState()
    {
        boolean dataToSaveInSlot =
            form.cbSlot.getItemCount()>0;
        boolean dataToSaveInBank =
            form.cbBank.getItemCount()>0;
        
        boolean canSaveInSlot =
            dataToSaveInSlot &&
            form.cbSaveInSlot.isSelected();
        boolean canSaveInBank = 
            dataToSaveInBank &&
            form.cbSaveInBank.isSelected();

        form.cbSaveInSlot.setEnabled(dataToSaveInSlot);
        form.cbSlot.setEnabled(canSaveInSlot);

        form.cbSaveInBank.setEnabled(dataToSaveInBank);
        form.cbSaveInBank.setEnabled(false); // TODO not implemented yet
        form.cbBank.setEnabled(canSaveInBank);

        form.btnSave.setEnabled(canSaveInSlot||canSaveInBank);
    }
    
    private ComboBoxModel getSlotModel(Synthesizer synth)
    {
        if (synth != null)
        {
            List<Slot> slots = new ArrayList<Slot>(synth.getSlotCount());
            for (int i=0;i<synth.getSlotCount();i++)
            {
                slots.add(synth.getSlot(i));
            }
            return new DefaultComboBoxModel(slots.toArray());
        }
        return null;
    }

    private Object action = ActionHandler.CANCEL;
    
    private void cancel()
    {
        action = ActionHandler.CANCEL;
        setVisible(false);
        dispose();
    }

    private void save()
    {
        action = ActionHandler.SAVE;
        setVisible(false);
        dispose();
    }

    public Synthesizer getSelectedSynthesizer()
    {
        if (isSaveOption())
        {
            return (Synthesizer) form.cbSynth.getSelectedItem();
        }
        return null;
    }

    public Slot getSelectedSlot()
    {
        if (getSelectedSynthesizer() != null)
        {
            return (Slot) form.cbSlot.getSelectedItem();
        }
        return null;
    }

    public boolean isSaveOption()
    {
        return action == ActionHandler.SAVE;
    }
    


    private static class CustomRenderer extends DefaultListCellRenderer
    {
        /**
         * 
         */
        private static final long serialVersionUID = 5574547544934341852L;

        public Component getListCellRendererComponent(JList list, Object value,
                int index, boolean isSelected, boolean cellHasFocus)
        {
            String text = null;
            
            if (value != null)
            {
                if (value instanceof Synthesizer)
                {
                    Synthesizer synth = (Synthesizer) value;
                    
                    String n = synth.getName();
                    String dv = synth.getDeviceName();
                    String v = synth.getVendor();
                    
                    if (n == null)
                        text = dv+" ("+v+")";
                    else
                        text = n+" ("+v+" "+dv+")";
                }
                else if (value instanceof Slot)
                {
                    Slot slot = (Slot) value;
                    text = slot.getName()+" (Slot "+(1+slot.getSlotIndex())+")";
                }
                else 
                {
                    text = String.valueOf(value);
                }
            }
            
            return super.getListCellRendererComponent(list, text, index, isSelected, cellHasFocus);
        }
    }
    
}
