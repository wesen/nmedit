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
 * Created on May 26, 2006
 */
package net.sf.nmedit.nomad.main.dialog;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.MidiDevice.Info;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.Timer;

import net.sf.nmedit.nomad.main.resources.AppIcons;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

public class NomadMidiDialog extends NomadDialog
{

    private final MidiDeviceChooser inDeviceChooser;
    private final MidiDeviceChooser outDeviceChooser;
    private MidiDevice.Info defaultIn;
    private MidiDevice.Info defaultOut;
    private Timer observerTask;
    private MidiDevice.Info[] inDeviceList;
    private MidiDevice.Info[] outDeviceList;

    public NomadMidiDialog(Info midiIn, Info midiOut)
    {
        setTitle("MIDI Setup");
        setImage(AppIcons.IC_NORDMODULAR);
        setLayout(null);
        setScrollbarEnabled(true);
        setPackingEnabled(false);
        this.defaultIn = midiIn;
        this.defaultOut= midiOut;
        inDeviceList = getMidiDevicesList(true);
        outDeviceList = getMidiDevicesList(false);
        
        inDeviceChooser = new MidiDeviceChooser(true);
        outDeviceChooser = new MidiDeviceChooser(false);

        FormLayout layout = new FormLayout(
                "pref, 20px, pref:grow",
                ""); // add rows dynamically
        //, 80dlu
        DefaultFormBuilder builder = new DefaultFormBuilder(layout, this);
        builder.setDefaultDialogBorder();

        builder.appendSeparator("Input");
        build(builder, inDeviceChooser);
        
        builder.appendSeparator("Output");
        build(builder, outDeviceChooser);
        
        observerTask = new Timer(500, new MidiDeviceObserver());
        observerTask.setCoalesce(true);
        observerTask.setInitialDelay(2000);
        observerTask.setRepeats(true);
        observerTask.start();
    }

    private void build(DefaultFormBuilder builder, MidiDeviceChooser deviceChooser)
    {
        builder.append("Device",    deviceChooser);
        builder.nextLine();
        builder.append("Description", deviceChooser.lblDescription);
        builder.nextLine();
        builder.append("Vendor",    deviceChooser.lblVendor);
        builder.nextLine();
        builder.append("Version",   deviceChooser.lblVersion);
        builder.nextLine();
    }
    
    public void invoke() 
    {
        try
        {
            super.invoke(new String[]{RESULT_OK, ":"+RESULT_CANCEL});
        }
        finally
        {
            // dialog was closed, stop timer
            observerTask.stop();
        }
    }

    public void setInputDevice(String name) 
    {
        inDeviceChooser.setSelectedDevice(name);     
    }

    public void setInputDevice(MidiDevice.Info dev) {
        inDeviceChooser.setSelectedDevice(dev);
    }
    
    public void setOutputDevice(String name) {
        outDeviceChooser.setSelectedDevice(name);        
    }

    public void setOutputDevice(MidiDevice.Info dev) {
        outDeviceChooser.setSelectedDevice(dev);
    }
    
    public MidiDevice.Info getInputDevice() 
    {
        if (getResult().equals(RESULT_OK))
        {
            MidiDevice.Info info = inDeviceChooser.getSelectedDevice();
            if (info!=null) return info;
        }
        return defaultIn;
    }
    
    public MidiDevice.Info getOutputDevice()
    {
        if (getResult().equals(RESULT_OK))
        {
            MidiDevice.Info info = outDeviceChooser.getSelectedDevice();
            if (info!=null) return info;
        }
        return defaultOut;
    }

    public static NomadMidiDialog invokeDialog(Info midiIn, Info midiOut) 
    {
        NomadMidiDialog dlg = new NomadMidiDialog(midiIn, midiOut);
        dlg.setVisible(true);
        return dlg;
    }

    private class MidiDeviceChooser extends JComboBox implements ListCellRenderer
    {
        public final static String NONE = "-none-";

        public JLabel lblDescription = new JLabel(NONE);
        public JLabel lblVendor = new JLabel(NONE);
        public JLabel lblVersion = new JLabel(NONE);

        private final boolean inputs;
        private Info lastSelection = null;
        
        public MidiDeviceChooser(boolean inputs)
        {
            this.inputs = inputs;
            lastSelection = inputs ? defaultIn : defaultOut;
            regenerate();
            updateLabels();
            setRenderer(this);
            addActionListener(this);
        }
        
        public void regenerate()
        {
            Info updateSelection = getSelectedDevice();
            if (updateSelection!=null)
                lastSelection = updateSelection;
            
            removeAllItems();
            for (Info info : inputs ? inDeviceList : outDeviceList)
            {
                addItem(info);
            }
            if (lastSelection==null && getItemCount()>0)
            {
                lastSelection = (Info) getItemAt(0);
            }
            
            setEnabled(getItemCount()>0);
            setSelectedDevice(lastSelection);
        }
        
        public MidiDevice.Info getSelectedDevice()
        {
            return (MidiDevice.Info) getSelectedItem();
        }
        
        public void setSelectedDevice(String name)
        {
            for (int i=0;i<getItemCount();i++) 
            {
                MidiDevice.Info deviceInfo = (MidiDevice.Info) getItemAt(i);
                if (deviceInfo.getName().equals(name)) 
                {
                    setSelectedDevice(deviceInfo);
                    break;
                }
            }
        }
        
        public void setSelectedDevice(MidiDevice.Info deviceInfo)
        {
            setSelectedItem(deviceInfo);
        }
        
        public void actionPerformed(ActionEvent e)
        {
            updateLabels();
        }
        
        private void updateLabels()
        {
            Info info = getSelectedDevice();

            if (info == null)
            {
                lblDescription.setText(NONE);
                lblVendor.setText(NONE);
                lblVersion.setText(NONE);
            }
            else
            {
                lblDescription.setText(info.getDescription());
                lblVendor.setText(info.getVendor());
                lblVersion.setText(info.getVersion());
            }
        }

        public Component getListCellRendererComponent( 
                JList list, Object value, int index, boolean isSelected, boolean cellHasFocus )
        {
            if (value==null)
                return new JLabel(NONE);
            
            JLabel label = new JLabel(((MidiDevice.Info) value).getName());
            if (isSelected)
            {
                label.setFont(new Font(getFont().getName(), Font.BOLD, getFont().getSize()));
            }
            return label;
        }
        
    }

    public static boolean isHardwareMidiDevice(MidiDevice device)
    {
        return ( ! (device instanceof Sequencer) && ! (device instanceof Synthesizer));
    }
    
    // BE AWARE THAT MidiDevice d : isInputDevice(d) <=/=> isOutputDevice(d) 
    
    public static boolean isInputDevice(MidiDevice device)
    {
        return device.getMaxTransmitters()>0
        || device.getMaxTransmitters()==-1; // unlimited
    }
    
    public static boolean isOutputDevice(MidiDevice device)
    {
        return device.getMaxReceivers()>0
        || device.getMaxReceivers()==-1; // unlimited
    }
    
    public static MidiDevice.Info[] getMidiDevicesList(boolean inputs)
    {
        Collection<MidiDevice.Info> devices 
            = new ArrayList<MidiDevice.Info>();

        for (MidiDevice.Info deviceInfo : MidiSystem.getMidiDeviceInfo()) 
        {
            MidiDevice device;
            
            try
            {
                device = MidiSystem.getMidiDevice(deviceInfo);
            }
            catch (MidiUnavailableException e)
            {
                device = null;
            }

            if ( (device != null) && isHardwareMidiDevice(device))
            {
                if ((inputs && isInputDevice(device)) || ((!inputs) && isOutputDevice(device)))
                {
                    devices.add(deviceInfo);
                }
            }
        }
        
        return devices.toArray(new MidiDevice.Info[devices.size()]);
    }

    private class MidiDeviceObserver implements ActionListener
    {
        public void actionPerformed( ActionEvent e )
        {
            check(inDeviceChooser, inDeviceList);
            check(outDeviceChooser, outDeviceList);
        }

        private void check( MidiDeviceChooser deviceChooser, Info[] deviceList )
        {
            Info[] updatedList = getMidiDevicesList(deviceChooser.inputs);
            boolean changed = false;
            if (updatedList.length==deviceList.length)
            {
                // same size, thus not changed
                // check if elements are equal
                for (int i=0;i<deviceList.length;i++)
                {
                    boolean found = false;
                    for (int j=0;j<updatedList.length;j++)
                    {
                        if (deviceList[i].equals(updatedList[j]))
                        {
                            found = true;
                            break;
                        }
                    }
                    if (!found)
                    {
                        changed = true;
                        break;
                    }
                }
            }
            else
            {
                changed = true;
            }

            if (changed)
            {
                if (deviceChooser.inputs)
                    inDeviceList = updatedList;
                else
                    outDeviceList = updatedList;
                deviceChooser.regenerate();
            }
            
        }
        
    }
    
}
