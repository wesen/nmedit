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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.clavia.nordmodular.Format;
import net.sf.nmedit.jpatch.clavia.nordmodular.Header;
import net.sf.nmedit.jpatch.clavia.nordmodular.NMPatch;
import net.sf.nmedit.jpatch.clavia.nordmodular.VoiceArea;
import net.sf.nmedit.jsynth.Slot;
import net.sf.nmedit.jsynth.Synthesizer;
import net.sf.nmedit.nomad.core.Nomad;
import net.sf.nmedit.nomad.core.forms.PropertyDialogForm;
import net.sf.nmedit.nomad.core.service.fileService.FileService;

public class PatchSettingsDialog 
{
    
    private NMPatch patch;
    private PropertyDialogForm dialog;
    private DialogPane settingsDlgPane;
    private PatchSettingsFrm frm;
    
    private FileService service;
    JTextArea notesTextArea = new JTextArea();
    
    private PatchSettingsDialog(NMPatch patch, FileService service)
    {
        this.service = service;
        this.patch = patch ;
        dialog = new PropertyDialogForm();
        
        String path;

        path = "info";
        DialogPane fileInfo = new DialogPane(patch, path, "Info")
        {
            @Override
            protected JComponent createDialogComponent()
            {
                return new JScrollPane(createInfoDialog());
            }
        };
        
        fileInfo.install(dialog);
        
        path = "settings";
        
        settingsDlgPane = new DialogPane(patch, path, "Settings")
        {
            @Override
            protected JComponent createDialogComponent()
            {
                return new JScrollPane(createSettingsDialog());
            }
        };
        
        settingsDlgPane.install(dialog);
        dialog.setSelectedPath(path);
        

        path = "notes";
        DialogPane notesPane = new DialogPane(patch, path, "Notes")
        {
            @Override
            protected JComponent createDialogComponent()
            {
                return new JScrollPane(createNotesDialog());
            }
        };
        
        notesPane.install(dialog);
        
        
    }

    protected JComponent createNotesDialog()
    {
        notesTextArea.setFont(new Font("monospaced", Font.PLAIN, 11));
        notesTextArea.setText(patch.getNote());
        
        return notesTextArea;
    }
    
    private JComponent text(String text)
    {
        JTextField tf = new JTextField(text);
        tf.setEditable(false);
        tf.setBorder(null);
        return tf;
    }

    protected JComponent createInfoDialog()
    {
        File file = patch.getFile();
        Slot slot = patch.getSlot();
        Synthesizer synth = slot == null ? null : slot.getSynthesizer();
        
        JPanel cont = new JPanel();
        cont.setLayout(new BoxLayout(cont, BoxLayout.Y_AXIS));
        
        Box box;

        // TODO fix layout
        
        // path
        box = Box.createHorizontalBox();
        box.add(new JLabel("Path:"));
        box.add(text(file == null ? "-" : file.getAbsolutePath()));
        cont.add(box);
        // file type
        box = Box.createHorizontalBox();
        box.add(new JLabel("Type:"));
        box.add(text(service == null ? "-" : service.getFileFilter().getDescription()));
        cont.add(box);
        // synth
        box = Box.createHorizontalBox();
        box.add(new JLabel("Synthesizer:"));
        box.add(text(synth == null ? "-" : (synth.getVendor()+" "+synth.getName())));
        cont.add(box);
        // slot
        box = Box.createHorizontalBox();
        box.add(new JLabel("Slot:"));
        box.add(text(slot == null ? "-" : slot.getName()));
        cont.add(box);
        
        return cont;
    }

    private JComponent createSettingsDialog()
    {
        frm = new PatchSettingsFrm();
        
        setValues(frm, patch.getHeader());
        
        return frm;
    }

    private SpinnerNumberModel model(int value)
    {
        return model(0, 127, value);
    }
    
    private SpinnerNumberModel model(int min, int max, int value)
    {
        return new SpinnerNumberModel(value, min, max, 1);
    }
    
    private void setValues(PatchSettingsFrm frm, Header header)
    {
        frm.btnGetCurrentNotes.setEnabled(false);
        
        frm.cbOctaveShift.setSelectedIndex(header.getOctaveShift());
        frm.cbVoiceReCommon.setSelected(header.isVoiceRetriggerCommonActive());
        frm.cbVoiceRePoly.setSelected(header.isVoiceRetriggerPolyActive());

        frm.rbPedalModeOnOff.setEnabled(false);
        frm.rbPedalModeSustain.setEnabled(false);
        
        frm.rbPortaAuto.setSelected(header.isPortamentoAutoEnabled());
        frm.spPortaTime.setModel(model(header.getPortamentoTime()));
        
        frm.spBendRange.setModel(model(Format.HEADER_BEND_RANGE_MIN, Format.HEADER_BEND_RANGE_MAX, header.getBendRange()));
        
        // TODO min<=max
        frm.spKbRangeMin.setModel(model(header.getKeyboardRangeMin())); 
        frm.spKbRangeMax.setModel(model(header.getKeyboardRangeMax())); 

        // TODO min<=max
        frm.spVelRangeMin.setModel(model(header.getVelocityRangeMin()));
        frm.spVelRangeMax.setModel(model(header.getVelocityRangeMax()));
        
        frm.spRequestedVoices.setModel(model(0, 32, header.getRequestedVoices()));

        double[] svp = stats(patch.getPolyVoiceArea());
        double[] svc = stats(patch.getCommonVoiceArea());
        
        frm.lblCycles.setText(svp[0]+"/"+svc[0]);
        frm.lblXmem.setText(svp[1]+"/"+svc[1]);
        frm.lblYmem.setText(svp[2]+"/"+svc[2]);
        frm.lblProgMem.setText(svp[3]+"/"+svc[3]);
        frm.lblDynMem.setText(svp[4]+"/"+svc[4]);
        frm.lblZeroPage.setText(svp[5]+"/"+svc[5]);
    }
    
    private void apply()
    {
        Header header = patch.getHeader();
        
        header.setValueWithoutNotification(Format.HEADER_OCTAVE_SHIFT, frm.cbOctaveShift.getSelectedIndex());
        
        header.setValueWithoutNotification(Format.HEADER_VOICE_RETRIGGER_COMMON, frm.cbVoiceReCommon.isSelected()?1:0);
        header.setValueWithoutNotification(Format.HEADER_VOICE_RETRIGGER_POLY, frm.cbVoiceRePoly.isSelected()?1:0);

        // frm.rbPedalModeOnOff.setEnabled(false);
        // frm.rbPedalModeSustain.setEnabled(false);
        
        header.setValueWithoutNotification(Format.HEADER_PORTAMENTO, frm.rbPortaAuto.isSelected()?1:0);
        header.setValueWithoutNotification(Format.HEADER_PORTAMENTO_TIME, (Integer) frm.spPortaTime.getValue());

        header.setValueWithoutNotification(Format.HEADER_BEND_RANGE, (Integer) frm.spBendRange.getValue());

        header.setValueWithoutNotification(Format.HEADER_KEYBOARD_RANGE_MIN, (Integer) frm.spKbRangeMin.getValue());
        header.setValueWithoutNotification(Format.HEADER_KEYBOARD_RANGE_MAX, (Integer) frm.spKbRangeMax.getValue());

        header.setValueWithoutNotification(Format.HEADER_VELOCITY_RANGE_MIN, (Integer) frm.spVelRangeMin.getValue());
        header.setValueWithoutNotification(Format.HEADER_VELOCITY_RANGE_MAX, (Integer) frm.spVelRangeMax.getValue());

        header.setValueWithoutNotification(Format.HEADER_REQUESTED_VOICES, (Integer) frm.spRequestedVoices.getValue());
        
        header.firePatchSettingsChanged(false);
        
        patch.setNote(notesTextArea.getText());
    }
    
    double[] stats(VoiceArea va)
    {
        double[] stats = new double[6];
        Arrays.fill(stats, 0);
        stats[0] = va.getTotalCycles();
        for (PModule m: va)
        {
            stats[1] += m.getDoubleAttribute("x-mem", 0);
            stats[2] += m.getDoubleAttribute("y-mem", 0);
            stats[3] += m.getDoubleAttribute("prog-mem", 0);
            stats[4] += m.getDoubleAttribute("dyn-mem", 0);
            stats[5] += m.getDoubleAttribute("zero-page", 0);
        }
        return stats;
    }

    public static void invoke(FileService service, NMPatch patch)
    {       
        
        PatchSettingsDialog dlg = new PatchSettingsDialog(patch, service);
        dlg.invoke();
        
    }

    private void invoke()
    {
        final JDialog d = new JDialog(Nomad.sharedInstance().getWindow(), "Properties for "+patch.getName());
        d.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension ds = new Dimension(ss.width/2, ss.height*2/5);
        
        d.getContentPane().setLayout(new BorderLayout());
        d.getContentPane().add(dialog, BorderLayout.CENTER);
        d.setBounds((ss.width-ds.width)/2, (ss.height-ds.height)/2, ds.width, ds.height);

        dialog.addButton(new AbstractAction(){
            /**
             * 
             */
            private static final long serialVersionUID = 3024495863362168958L;
            {
                putValue(NAME, "Apply");
            }
            public void actionPerformed(ActionEvent e)
            {
                apply();
            }
            });        

        dialog.addButton(new AbstractAction(){
            /**
             * 
             */
            private static final long serialVersionUID = 1653037659655851257L;
            {
                putValue(NAME, "Close");
            }
            public void actionPerformed(ActionEvent e)
            {
                d.dispose();
            }});        


        d.setVisible(true);
    }

    public abstract static class DialogPane implements ActionListener, Runnable
    {
        
        protected NMPatch patch;
        protected String path;
        protected String title;
        protected JComponent component;
        protected PropertyDialogForm dialog;
        
        public DialogPane(NMPatch patch, String path, String title)
        {
            this.patch = patch;
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
    
}
