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
package net.sf.nmedit.jsynth.nomad.forms;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Vector;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import net.sf.nmedit.jsynth.midi.MidiUtils;

public class NomadMidiDialogFrmHandler extends NomadMidiDialogFrm implements ItemListener
{

    /**
     * 
     */
    private static final long serialVersionUID = 5654053362334343563L;
    public static final String INPUT_DEVICE_PROPERTY = "midi.input";
    public static final String OUTPUT_DEVICE_PROPERTY = "midi.output";
    
    private NomadMidiDialogFrm form;

    private boolean listHardwareDevices = true;
    private boolean listSoftwareDevices = false;
    private boolean listAvailableDevices = false;

    private MidiDevice.Info previousInput =  null;
    private MidiDevice.Info previousOutput =  null;

    private MidiDevice.Info internalSelectedInput =  null;
    private MidiDevice.Info internalSelectedOutput =  null;
    
    private boolean listPreviousIfUnavailable = true;
    
    private String currentText = "current";
    
    public NomadMidiDialogFrmHandler()
    {
        this.form = this;
        initializeForm();
    }

    private void setInternalSelectedInput(MidiDevice.Info info)
    {
        MidiDevice.Info oldValue = this.internalSelectedInput;
        MidiDevice.Info newValue = info;
        
        if (oldValue != newValue)
        {
            this.internalSelectedInput = info;
            firePropertyChange(INPUT_DEVICE_PROPERTY, oldValue, newValue);
        }
    }

    private void setInternalSelectedOutput(MidiDevice.Info info)
    {
        MidiDevice.Info oldValue = this.internalSelectedOutput;
        MidiDevice.Info newValue = info;
        
        if (oldValue != newValue)
        {
            this.internalSelectedOutput = info;
            firePropertyChange(OUTPUT_DEVICE_PROPERTY, oldValue, newValue);
        }
    }

    public void setCurrentText(String text)
    {
        this.currentText = text;
    }
    
    public String getCurrentText()
    {
        return currentText;
    }
    
    public boolean isSelectionDifferent()
    {
        return getPreviousOutput() != getSelectedOutput()
        || getPreviousInput() != getSelectedInput();
    }
    
    public void setListPreviousIfUnavailable(boolean enable)
    {
        this.listPreviousIfUnavailable = enable;
    }
    
    public boolean isListPreviousIfUnavailableEnabled()
    {
        return listPreviousIfUnavailable;
    }

    public MidiDevice.Info getPreviousInput()
    {
        return previousInput;
    }

    public MidiDevice.Info getPreviousOutput()
    {
        return previousOutput;
    }

    public void setPreviousInput(MidiDevice.Info previousInput)
    {
        this.previousInput = previousInput;
        setInternalSelectedInput(previousInput);
        updateForm();
    }

    public void setPreviousOutput(MidiDevice.Info previousOutput)
    {
        this.previousOutput = previousOutput;
        setInternalSelectedOutput(previousOutput);
        updateForm();
    }
    
    public boolean isListingAvailableDeviceDevicesOnly()
    {
        return listAvailableDevices;
    }
    
    public boolean isHardwareDeviceListingEnabled()
    {
        return listHardwareDevices;
    }

    public boolean isSoftwareDeviceListingEnabled()
    {
        return listHardwareDevices;
    }

    public void setListingAvailableDeviceDevicesOnly(boolean availableOnly)
    {
        listAvailableDevices = availableOnly;
    }

    public void setHardwareDeviceListingEnabled(boolean enable)
    {
        listHardwareDevices = enable;
    }

    public void setSoftwareDeviceListingEnabled(boolean enable)
    {
        listSoftwareDevices = enable;
    }

    /**
     * Updates the list of input/output devices.
     */
    public void refreshImmediatelly()
    {
        updateForm();
    }

    public MidiDevice.Info getSelectedInput()
    {
        return getSelectedInfo(form.cbInDevices);
    }
    
    public MidiDevice.Info getSelectedOutput()
    {
        return getSelectedInfo(form.cbOutDevices);   
    }

    public boolean setSelectedInput(MidiDevice.Info info)
    {
        return setSelectedInfo(form.cbInDevices, info);
    }

    public boolean setSelectedOutput(MidiDevice.Info info)
    {
        return setSelectedInfo(form.cbOutDevices, info);
    }
    
    private void initializeForm()
    {
        form.cbInDevices.addItemListener(this);
        form.cbOutDevices.addItemListener(this);

        form.cbInDevices.setRenderer(new MidiInfoRenderer(form.cbInDevices));
        form.cbOutDevices.setRenderer(new MidiInfoRenderer(form.cbOutDevices));

        updateForm();
    }
    
    private void updateForm()
    {
        form.cbInDevices.setModel(new DefaultComboBoxModel(createDeviceList(true)));
        form.cbOutDevices.setModel(new DefaultComboBoxModel(createDeviceList(false)));

        if (form.cbInDevices.getItemCount()>0)
        {
            if (internalSelectedInput == null && previousInput != null)
            {
                setInternalSelectedInput(previousInput);
            }
            if (!(internalSelectedInput != null && setSelectedInput(internalSelectedInput)))
                form.cbInDevices.setSelectedIndex(0);
        }
        else
        {
            setInternalSelectedInput(null);
        }

        if (form.cbOutDevices.getItemCount()>0)
        {
            if (internalSelectedOutput == null && previousOutput != null)
            {
                setInternalSelectedOutput(previousOutput);
            }
            if (!(internalSelectedOutput != null && setSelectedOutput(internalSelectedOutput)))
                form.cbOutDevices.setSelectedIndex(0);
        }
        else
        {
            setInternalSelectedOutput(null);
        }

        form.cbInDevices.setEnabled(form.cbInDevices.getItemCount()>0);
        form.cbOutDevices.setEnabled(form.cbOutDevices.getItemCount()>0);
        
        updateLabels(true);
        updateLabels(false);

        form.cbInDevices.repaint();
        form.cbOutDevices.repaint();

        if (getPreviousInput() == null ^ form.cbInDevices.getItemCount()==0)
            firePropertyChange(INPUT_DEVICE_PROPERTY, getPreviousInput(), null);
        if (getPreviousOutput() == null ^ form.cbOutDevices.getItemCount()==0)
            firePropertyChange(OUTPUT_DEVICE_PROPERTY, getPreviousOutput(), null);
    }
    
    private MidiDevice.Info getSelectedInfo(JComboBox cb)
    {
        return (MidiDevice.Info) cb.getSelectedItem();
    }

    private boolean setSelectedInfo(JComboBox cb, MidiDevice.Info info)
    {
        if (defaultModel(cb).getIndexOf(info)>=0)
        {
            // element exists
            cb.setSelectedItem(info);
            return true;
        }
        return false;
    }
    
    private DefaultComboBoxModel defaultModel(JComboBox cb)
    {
        return (DefaultComboBoxModel) cb.getModel();
    }

    private Vector<MidiDevice.Info> createDeviceList(boolean inputs)
    {
        final boolean hw = listHardwareDevices; // collect hardware device ?
        final boolean sw = listSoftwareDevices; // but software devices ?
        final boolean onlyAvailable = listAvailableDevices; // list unavailable ones
        
        Vector<MidiDevice.Info> c = new Vector<MidiDevice.Info>();
        MidiUtils.collectMidiDeviceInfo(c, hw, sw, inputs, !inputs, onlyAvailable);
        
        if (inputs)
        {
            checkAdd(c, previousInput, inputs);
        }
        else
        {
            checkAdd(c, previousOutput, !inputs);
        }
        
        return c;
    }

    private void checkAdd(Vector<MidiDevice.Info> c, MidiDevice.Info info, boolean input)
    {
        // info is null or already in the list
        if (info == null || c.contains(info))
            return;
        
        // info is not in list
        if (listPreviousIfUnavailable)
        {
            try
            {
                if (input)
                {
                    if (!MidiUtils.isInputDeviceAvailable(info))
                    {
                        // not available
                        c.add(info);
                    }
                }
                else
                {
                    if (!MidiUtils.isOutputDeviceAvailable(info))
                    {
                        // not available
                        c.add(info);
                    }
                }
            }
            catch (MidiUnavailableException e)
            {
                // the device does not exist anymore - we do not add it
            }
        }
    }

    public void itemStateChanged(ItemEvent e)
    {
        if (e.getSource() == form.cbInDevices)
        {
            setInternalSelectedInput((MidiDevice.Info)form.cbInDevices.getSelectedItem());
            
            updateLabels(true);
        }
        else if (e.getSource() == form.cbOutDevices)
        {
            setInternalSelectedOutput((MidiDevice.Info)form.cbOutDevices.getSelectedItem());
            
            updateLabels(false);
        }
    }
    
    private void updateLabels(boolean forInputDevice)
    {
        if (forInputDevice)
        {
            updateLabels(form.cbInDevices, form.lblInVendor, form.lblInVersion, form.lblInDescription);
        }
        else
        {
            updateLabels(form.cbOutDevices, form.lblOutVendor, form.lblOutVersion, form.lblOutDescription);
        }
    }
    
    private void updateLabels(JComboBox cb, JLabel lblVendor, JLabel lblVersion, JLabel lblDescription)
    {
        final String NO_VALUE = "-";
        MidiDevice.Info info = (MidiDevice.Info) cb.getSelectedItem();
        
        if (info == null)
        {
            lblVendor.setText(NO_VALUE);
            lblVersion.setText(NO_VALUE);
            lblDescription.setText(NO_VALUE);
        }
        else
        {
            lblVendor.setText(info.getVendor());
            lblVersion.setText(info.getVersion());
            lblDescription.setText(info.getDescription());
        }
    }

    public void refresh()
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                refreshImmediatelly();
            }
        });
    }
    
    /**
     * Renders the MidiDevice.Info list items of the JComboBox.
     * Instead of only showing the name of the device the renderer
     * appends the list index plus 1. Since this index is unique for each list
     * item it is possible to distinguish between devices which have
     * the equal names (usability).
     */
    private class MidiInfoRenderer extends DefaultListCellRenderer
    {

        /**
         * 
         */
        private static final long serialVersionUID = 4870999830473752949L;
        private JComboBox cb;

        public MidiInfoRenderer(JComboBox cb)
        {
            this.cb = cb;
        }
        
        private DefaultComboBoxModel getModel()
        {
            return defaultModel(cb);
        }
        
        public Component getListCellRendererComponent(
            JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
        {
            String text;
            
            if (value == null)
            {
                text = "-none-";
            }
            else
            {
                final MidiDevice.Info info = (MidiDevice.Info) value;
                
                // if the combobox is opened, the index is >= 0, but if the
                // combobox is closed, the displayed item has a negative index
                int id = (index<0 ? getModel().getIndexOf(info) : index)+1;
    
                text = info.getName()+" ("+(id)+")";
                
                if (info.equals(getPreviousInput()) || info.equals(getPreviousOutput()))
                    text += " ("+getCurrentText()+")";
                
                if (cb.getSelectedIndex() == index)
                {
                    text = "-> "+text;
                }
            }
            return super.getListCellRendererComponent(list, text, 
                    index, isSelected, cellHasFocus);
        }
    }

    // For testing.
    public static void main(String[] args)
    {
        NomadMidiDialogFrmHandler dialog = new NomadMidiDialogFrmHandler();

        dialog.setSoftwareDeviceListingEnabled(true);
        dialog.setPreviousInput(dialog.getSelectedInput());
        dialog.setPreviousOutput(null);
        
        PropertyChangeListener l = new PropertyChangeListener()
        {
            public void propertyChange(PropertyChangeEvent evt)
            {
                if (evt.getPropertyName() == INPUT_DEVICE_PROPERTY
                        || evt.getPropertyName() == OUTPUT_DEVICE_PROPERTY)
                
                System.out.println(evt.getPropertyName()+": oldValue="+evt.getOldValue()+", newValue="+evt.getNewValue()); 
            }            
        };

        dialog.refresh();
        
        dialog.addPropertyChangeListener(l);
        
        JFrame f = new JFrame("Midi Dialog");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setBounds(10, 10, 400, 300);
        f.getContentPane().setLayout(new BorderLayout());
        f.getContentPane().add(new JScrollPane(dialog));
        f.setVisible(true);
    }
    
}
