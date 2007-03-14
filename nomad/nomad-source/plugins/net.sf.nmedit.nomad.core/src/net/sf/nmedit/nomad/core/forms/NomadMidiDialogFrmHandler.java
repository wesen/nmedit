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
package net.sf.nmedit.nomad.core.forms;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import net.sf.nmedit.nmutils.midi.MidiUtils;

public class NomadMidiDialogFrmHandler extends NomadMidiDialogFrm implements ItemListener, ActionListener
{

    public static final String INPUT_DEVICE_PROPERTY = "midi.input";
    public static final String OUTPUT_DEVICE_PROPERTY = "midi.output";
    
    private NomadMidiDialogFrm form;

    private boolean listHardwareDevices = true;
    private boolean listSoftwareDevices = false;
    private boolean listAvailableDevices = false;

    private MidiDevice.Info previousInput =  null;
    private MidiDevice.Info previousOutput =  null;

    private boolean internalSelectedInputSet = false;
    private boolean internalSelectedOutputSet = false;
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
        
        if ((!internalSelectedInputSet) && (internalSelectedInput==null))
        {
            internalSelectedInputSet = true;
            setInternalSelectedInput(previousInput);
        }
    }
    
    public void setPreviousOutput(MidiDevice.Info previousOutput)
    {
        this.previousOutput = previousOutput;

        if ((!internalSelectedOutputSet) && (internalSelectedOutput==null))
        {
            internalSelectedOutputSet = true;
            setInternalSelectedOutput(previousInput);
        }
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
    public void refresh()
    {
        updateData();
    }

    public MidiDevice.Info getSelectedInput()
    {
        return getSelectedInfo(form.m_cbInDevices);
    }
    
    public MidiDevice.Info getSelectedOutput()
    {
        return getSelectedInfo(form.m_cbOutDevices);   
    }

    public void setSelectedInput(MidiDevice.Info info)
    {
        setSelectedInfo(form.m_cbInDevices, info);
    }

    public void setSelectedOutput(MidiDevice.Info info)
    {
        setSelectedInfo(form.m_cbOutDevices, info);
    }
    
    private void initializeForm()
    {
        form.m_cbInDevices.addItemListener(this);
        form.m_cbOutDevices.addItemListener(this);
        form.m_btnRefresh.addActionListener(this);

        form.m_cbInDevices.setRenderer(new MidiInfoRenderer(form.m_cbInDevices));
        form.m_cbOutDevices.setRenderer(new MidiInfoRenderer(form.m_cbOutDevices));

        updateData();
    }
    
    private void updateData()
    {
        form.m_cbInDevices.setModel(new DefaultComboBoxModel(createDeviceList(true)));
        form.m_cbOutDevices.setModel(new DefaultComboBoxModel(createDeviceList(false)));

        if (form.m_cbInDevices.getItemCount()>0)
            form.m_cbInDevices.setSelectedIndex(0);
        if (form.m_cbOutDevices.getItemCount()>0)    
            form.m_cbOutDevices.setSelectedIndex(0);

        if (internalSelectedInput != null)
            setSelectedInput(internalSelectedInput);
        if (internalSelectedOutput != null)
            setSelectedOutput(internalSelectedOutput);

        checkItemCount(form.m_cbInDevices);
        checkItemCount(form.m_cbOutDevices);
        updateLabels(true);
        updateLabels(false);
    }
    
    private MidiDevice.Info getSelectedInfo(JComboBox cb)
    {
        return (MidiDevice.Info) cb.getSelectedItem();
    }

    private void setSelectedInfo(JComboBox cb, MidiDevice.Info info)
    {
        if (defaultModel(cb).getIndexOf(info)>=0)
        {
            // element exists
            cb.setSelectedItem(info);
        }
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
            checkAdd(c, previousInput);
        }
        else
        {
            checkAdd(c, previousOutput);
        }
        
        return c;
    }

    private void checkAdd(Vector<MidiDevice.Info> c, MidiDevice.Info info)
    {
        // info is null or already in the list
        if (info == null || c.contains(info))
            return;
        
        // info is not in list
        if (listPreviousIfUnavailable)
        {
            try
            {
                if (!MidiUtils.isInputDeviceAvailable(info))
                {
                    // not available
                    c.add(info);
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
        if (e.getSource() == form.m_cbInDevices)
        {
            setInternalSelectedInput((MidiDevice.Info)form.m_cbInDevices.getSelectedItem());
            
            updateLabels(true);
        }
        else if (e.getSource() == form.m_cbOutDevices)
        {
            setInternalSelectedOutput((MidiDevice.Info)form.m_cbOutDevices.getSelectedItem());
            
            updateLabels(false);
        }
    }
    
    private void updateLabels(boolean forInputDevice)
    {
        if (forInputDevice)
        {
            updateLabels(form.m_cbInDevices, form.m_lblInVendor, form.m_lblInVersion, form.m_lblInDescription);
        }
        else
        {
            updateLabels(form.m_cbOutDevices, form.m_lblOutVendor, form.m_lblOutVersion, form.m_lblOutDescription);
        }
    }
    
    private void checkItemCount(JComboBox cb)
    {
        cb.setEnabled(cb.getItemCount()>0);
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
    
    /**
     * Handles the refresh button action.
     */
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == form.m_btnRefresh)
        {
            // disable components, indicating that something happens
            form.m_btnRefresh.setEnabled(false);
            
            // we reactivate it later ...
            SwingUtilities.invokeLater(new Refresher());
        }
    }
    
    private class Refresher implements Runnable
    {

        public void run()
        {
            try
            {
                // refresh list
                refresh();
                
                // wait a few milliseconds so that
                // the user can see the change of
                // the button enabled/disabled state
                Thread.sleep(500);
            }
            catch (InterruptedException e)
            {
                // ignore
            }
            finally
            {
                // enable components
                form.m_btnRefresh.setEnabled(true);
            }
        }
        
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
