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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MidiDevice;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import net.sf.nmedit.jsynth.DefaultMidiPorts;
import net.sf.nmedit.jsynth.Synthesizer;
import net.sf.nmedit.jsynth.midi.MidiDescription;
import net.sf.nmedit.jsynth.midi.MidiPlug;
import net.sf.nmedit.nomad.core.forms.ExceptionDialog;
import net.sf.nmedit.nomad.core.forms.PropertyDialogForm;

public class SynthPropertiesDialog<S extends Synthesizer> extends PropertyDialogForm
{
    
    /**
     * 
     */
    private static final long serialVersionUID = 6817919267677037724L;
    private S synth;
    private List<DialogPane<?>> disposeList = new ArrayList<DialogPane<?>>();
    
    public SynthPropertiesDialog(S synth)
    {
        this.synth = synth;
    }
    
    public void addToDisposeList(DialogPane<?> dp)
    {
        disposeList.add(dp);
    }
    
    public S getSynth()
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

    protected DialogPane<S> createSynthInfo()
    {
        return new SynthInfo<S>(synth);
    }

    protected DialogPane<S> createPortSettings()
    {
        return new PortSettingsDialog<S>(synth);
    }
    
    public void dispose()
    {
        for (DialogPane<?> dp: disposeList)
            dp.dispose();
    }

    public abstract static class DialogPane<S extends Synthesizer> implements ActionListener, Runnable
    {
        
        protected S synth;
        protected String path;
        protected String title;
        protected JComponent component;
        protected PropertyDialogForm dialog;
        
        public DialogPane(S synth, String path, String title)
        {
            this.synth = synth;
            this.path = path;
            this.title = title;
        }
        
        public void dispose()
        {
            // no op
        }
        
        public void install(SynthPropertiesDialog<S> dialog)
        {
            this.dialog = dialog;
            dialog.addEntry(path, title);
            dialog.addActionListener(this);
            dialog.addToDisposeList(this);
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
    
    protected static class PortSettingsDialog<S extends Synthesizer> extends DialogPane<S> implements PropertyChangeListener
    {

        public static final String ACTION_APPLY = "Apply";
        public static final String ACTION_REFRESH = "Refresh";
        protected NomadMidiDialogFrmHandler midiDialogFrmHandler;
        protected MyAction applyAction;
        
        public PortSettingsDialog(S synth)
        {
            super(synth, "connection", "Connection Settings");
        }
        
        protected boolean isSynthSupported()
        {
            return synth instanceof DefaultMidiPorts;
        }
        
        public void install(SynthPropertiesDialog<S> dialog)
        {
            if (!isSynthSupported())
                return;
            super.install(dialog);
        }
        
        protected class MyAction extends AbstractAction implements Runnable
        {
            /**
             * 
             */
            private static final long serialVersionUID = 4966069655840174685L;

            public MyAction(String command)
            {
                if (command == ACTION_APPLY)
                {
                    setEnabled(midiDialogFrmHandler.isSelectionDifferent());
                    putValue(NAME, "Apply");
                }
                else if (command == ACTION_REFRESH)
                {
                    setEnabled(true);
                    putValue(NAME, "Refresh");
                }
                putValue(ACTION_COMMAND_KEY, command);
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
                else if (getValue(ACTION_COMMAND_KEY) == ACTION_REFRESH)
                    midiDialogFrmHandler.refresh();
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

                MidiDevice.Info info1 = midiDialogFrmHandler.getSelectedInput();
                MidiDevice.Info info2 = midiDialogFrmHandler.getSelectedOutput();
                MidiDescription descIn = new MidiDescription(info1, 1);
                MidiDescription descOut = new MidiDescription(info2, 0);
                mdp.getDefaultMidiInPort().setPlug(info1 != null ? new MidiPlug(descIn) : null);
                mdp.getDefaultMidiOutPort().setPlug(info2 != null ? new MidiPlug(descOut) : null);

                formSetPreviousPorts();
                
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
        
        private void formSetPreviousPorts()
        {
            DefaultMidiPorts mdp = (DefaultMidiPorts) synth;
            
            MidiPlug inPlug = mdp.getDefaultMidiInPort().getPlug();
            MidiDevice.Info in = inPlug != null ? inPlug.getDeviceInfo() : null;

            MidiPlug outPlug = mdp.getDefaultMidiOutPort().getPlug();
            MidiDevice.Info out = outPlug != null ? outPlug.getDeviceInfo() : null;

            midiDialogFrmHandler.setPreviousInput(in);
            midiDialogFrmHandler.setPreviousOutput(out);
          //  midiDialogFrmHandler.updateForm();
        }
        
        @Override
        protected JComponent createDialogComponent()
        {
            midiDialogFrmHandler = new NomadMidiDialogFrmHandler();
            formSetPreviousPorts();
            midiDialogFrmHandler.addPropertyChangeListener(NomadMidiDialogFrmHandler.INPUT_DEVICE_PROPERTY, this);
            midiDialogFrmHandler.addPropertyChangeListener(NomadMidiDialogFrmHandler.OUTPUT_DEVICE_PROPERTY, this);
            
            JPanel btnPane = new JPanel();
            btnPane.setLayout(new BoxLayout(btnPane, BoxLayout.LINE_AXIS));
            btnPane.add(Box.createHorizontalGlue());
            btnPane.add(new JButton(new MyAction(ACTION_REFRESH)));
            btnPane.add(new JButton(applyAction = new MyAction(ACTION_APPLY)));

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
    
    protected static class SynthInfo<S extends Synthesizer> extends DialogPane<S>
    {

        public SynthInfo(S synth)
        {
            super(synth, "info", "Info");
        }

        @Override
        protected JComponent createDialogComponent()
        {
            SynthInfoFrm frm = new SynthInfoFrm();
            frm.lblSynthName.setText(synth.getName());
            frm.lblDeviceName.setText(synth.getDeviceName());
            frm.lblSlotCount.setText(Integer.toString(synth.getSlotCount()));
            frm.lblVendor.setText(synth.getVendor());
            frm.lblSynthIcon.setText(null);
            frm.lblSynthIcon.setIcon(null);
            Object icon = synth.getClientProperty("icon");
            if (icon instanceof Icon)
                frm.lblSynthIcon.setIcon((Icon) icon);
            JScrollPane sc = new JScrollPane(frm);
            sc.setBorder(null);
            return sc;
        }
        
    }

}
