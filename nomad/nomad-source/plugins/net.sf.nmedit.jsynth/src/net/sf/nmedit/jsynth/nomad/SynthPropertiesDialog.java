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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.sound.midi.MidiDevice;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import net.sf.nmedit.jsynth.DefaultMidiPorts;
import net.sf.nmedit.jsynth.Synthesizer;
import net.sf.nmedit.jsynth.midi.MidiPlug;
import net.sf.nmedit.nomad.core.forms.ExceptionDialog;
import net.sf.nmedit.nomad.core.forms.NomadMidiDialogFrmHandler;
import net.sf.nmedit.nomad.core.forms.PropertyDialogForm;

public class SynthPropertiesDialog extends PropertyDialogForm
{
    
    private Synthesizer synth;
    
    public SynthPropertiesDialog(Synthesizer synth)
    {
        this.synth = synth;
    }
    
    public Synthesizer getSynth()
    {
        return synth;
    }
    
    public void addSynthInfo()
    {
        createSynthInfo().install(this);
    }

    public void addSynthSettings()
    {
        //addEntry("synth/settings", "Settings");
    }
    
    public void addPortSettings()
    {
        createPortSettings().install(this);
    }

    protected DialogPane createSynthInfo()
    {
        return new SynthInfo(synth);
    }

    protected DialogPane createPortSettings()
    {
        return new PortSettingsDialog(synth);
    }
    
    public abstract static class DialogPane implements ActionListener, Runnable
    {
        
        protected Synthesizer synth;
        protected String path;
        protected String title;
        protected JComponent component;
        protected PropertyDialogForm dialog;
        
        public DialogPane(Synthesizer synth, String path, String title)
        {
            this.synth = synth;
            this.path = path;
            this.title = title;
        }
        
        public void install(PropertyDialogForm dialog)
        {
            this.dialog = dialog;
            dialog.addEntry(path, title);
            dialog.addActionListener(this);
        }
        
        public void actionPerformed(ActionEvent e)
        {
            if (path == e.getActionCommand() || path.equals(e.getActionCommand()))
            {
                setDialogComponentLater();
            }
        }

        private void setDialogComponentLater()
        {
            SwingUtilities.invokeLater(this);
        }

        private void setDialogComponent()
        {
            if (component == null)
                component = createDialogComponent();
            if (component != null)
                dialog.setEditor(component);
        }

        public void run()
        {
            setDialogComponent();
        }
        
        protected abstract JComponent createDialogComponent();
        
    }
    
    protected static class PortSettingsDialog extends DialogPane implements PropertyChangeListener
    {

        public static final String ACTION_APPLY = "Apply";
        protected NomadMidiDialogFrmHandler midiDialogFrmHandler;
        protected PortAction applyAction;
        
        public PortSettingsDialog(Synthesizer synth)
        {
            super(synth, "connection", "Connection Settings");
        }
        
        protected boolean isSynthSupported()
        {
            return synth instanceof DefaultMidiPorts;
        }
        
        public void install(PropertyDialogForm dialog)
        {
            if (!isSynthSupported())
                return;
            super.install(dialog);
        }
        
        protected class PortAction extends AbstractAction implements Runnable
        {
            public PortAction(String command)
            {
                if (command == ACTION_APPLY)
                {
                    setEnabled(midiDialogFrmHandler.isSelectionDifferent());
                    putValue(NAME, "Apply");
                    putValue(ACTION_COMMAND_KEY, command);
                }
            }

            public void actionPerformed(ActionEvent e)
            {
                if (!isEnabled())
                    return;
                SwingUtilities.invokeLater(this);
            }

            public void run()
            {
                if (!isEnabled())
                    return;
                if (getValue(ACTION_COMMAND_KEY) == ACTION_APPLY)
                    applyPortSettings();
            }
        }

        protected void applyPortSettings()
        {
            if (!applyAction.isEnabled())
                return;
            applyAction.setEnabled(false);
            
            boolean reconnect = synth.isConnected();
            DefaultMidiPorts mdp = (DefaultMidiPorts) synth;

            try
            {
                if (synth.isConnected())
                    synth.setConnected(false);
                
                MidiDevice.Info info = midiDialogFrmHandler.getSelectedInput();
                mdp.getDefaultMidiInPort().setPlug(info != null ? new MidiPlug(info) : null);
                info = midiDialogFrmHandler.getSelectedOutput();
                mdp.getDefaultMidiOutPort().setPlug(info != null ? new MidiPlug(info) : null);
                
                
                
                if (reconnect)
                    synth.setConnected(true);
            }
            catch (Throwable e)
            {
                ExceptionDialog.showErrorDialog(dialog, e, 
                        synth.getDeviceName()+", "+title, e);

                applyAction.setEnabled(true);
            }
        }

        public void propertyChange(PropertyChangeEvent evt)
        {
            if (applyAction != null)
                applyAction.setEnabled(true);
        }
        
        @Override
        protected JComponent createDialogComponent()
        {
            DefaultMidiPorts mdp = (DefaultMidiPorts) synth;
            
            MidiPlug inPlug = mdp.getDefaultMidiInPort().getPlug();
            MidiDevice.Info in = inPlug != null ? inPlug.getDeviceInfo() : null;

            MidiPlug outPlug = mdp.getDefaultMidiOutPort().getPlug();
            MidiDevice.Info out = outPlug != null ? outPlug.getDeviceInfo() : null;
            
            midiDialogFrmHandler = new NomadMidiDialogFrmHandler();
            midiDialogFrmHandler.setPreviousInput(in);
            midiDialogFrmHandler.setPreviousOutput(out);
            midiDialogFrmHandler.addPropertyChangeListener(NomadMidiDialogFrmHandler.INPUT_DEVICE_PROPERTY, this);
            midiDialogFrmHandler.addPropertyChangeListener(NomadMidiDialogFrmHandler.OUTPUT_DEVICE_PROPERTY, this);
            
            JPanel btnPane = new JPanel();
            btnPane.setLayout(new BoxLayout(btnPane, BoxLayout.LINE_AXIS));
            btnPane.add(Box.createHorizontalGlue());
            btnPane.add(new JButton(applyAction = new PortAction(ACTION_APPLY)));;

            JPanel pan = new JPanel();
            pan.setLayout(new BorderLayout());
            pan.add(midiDialogFrmHandler, BorderLayout.CENTER);
            pan.add(btnPane, BorderLayout.SOUTH);

            JScrollPane sp = new JScrollPane(pan);
            sp.setBorder(null);
            
            return sp;
            
            /*
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
            
            return false;*/
        }
        
    }
    
    protected static class SynthInfo extends DialogPane
    {

        public SynthInfo(Synthesizer synth)
        {
            super(synth, "info", "Info");
        }

        @Override
        protected JComponent createDialogComponent()
        {
            JPanel p = new JPanel();
            p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
            p.add(new JLabel("Device: "+synth.getDeviceName()));
            p.add(new JLabel("Synth: "+synth.getName()));
            p.add(new JLabel("Vendor: "+synth.getVendor()));
            return new JScrollPane(p);
        }
        
    }

}
