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
 * Created on Apr 29, 2006
 */
package net.sf.nmedit.nomad.main.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.AbstractSpinnerModel;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sf.nmedit.jmisc.math.Math2;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Patch;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.Event;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.PatchListener;
import net.sf.nmedit.jsynth.clavia.nordmodular.v3_03.Slot;
import net.sf.nmedit.nomad.main.Nomad;
import net.sf.nmedit.nomad.main.action.ShowNoteDialogAction;
import net.sf.nmedit.nomad.patch.ui.PatchDocument;
import net.sf.nmedit.nomad.theme.NomadClassicColors;
import net.sf.nmedit.nomad.util.document.Document;
import net.sf.nmedit.nomad.util.document.DocumentListener;
import net.sf.nmedit.nomad.util.document.DocumentManager;

public class PatchSection extends HeaderSection implements DocumentListener,
PatchListener
{

    private JTextField
        pName;

    private JSpinner
        pVoices;
    
    private VoicesNumberModel voices;
    
    private DocumentManager documentManager = null;
    
    private Patch patch = null;
    private Document document = null;
    private Nomad nomad;
    private JPanel dspPane;
    private JProgressBar dspTotal;
    private JProgressBar dspPoly;
    
    private CableToggler cableToggler = new CableToggler();
    
    public PatchSection( Nomad nomad, String title, ShowNoteDialogAction action )
    {
        super( title );
        this.nomad = nomad;
        JComponent pane = getContentPane();
        pane.setLayout(new BoxLayout(pane, BoxLayout.X_AXIS));//new GridLayout(1, 0, 2, 2));

        pName = new JTextField(new LimitedText(16), "Name", 16);
        pName.setMinimumSize(new Dimension(80, 0));
        pName.addKeyListener(new KeyAdapter(){
            public void keyPressed( KeyEvent e )
            {
                if (e.getKeyCode()==KeyEvent.VK_ESCAPE)
                {
                    if (patch!=null)
                    {
                        pName.setBackground(Color.WHITE);
                        pName.setText(patch.getName());
                    }
                }
            }

            public void keyReleased( KeyEvent e )
            {
                if (patch!=null)
                {
                    String name = patch.getName();
                    if (name == null) name = "";
                    pName.setBackground
                    (
                            name.equals(pName.getText()) ?
                            Color.WHITE:Color.RED
                    );
                }   
            }});
        pName.addActionListener(new ActionListener() {
            public void actionPerformed( ActionEvent e )
            {
                if (patch!=null)
                {
                    patch.setName(pName.getText());
                    PatchSection.this.nomad.updateTitle(patch);
                }
            }});
        
        pVoices = new JSpinner(); 
        voices = new VoicesNumberModel(1, 1, 32);
        pVoices.setModel(voices);
        pVoices.setValue(1/*voices.getMinimum()*/);
        
        voices.addChangeListener(new ChangeListener() {
            public void stateChanged( ChangeEvent e )
            {
                if (patch!=null)
                {
                    patch.getHeader().setRequestedVoices(voices.getRequestedVoices());
                }
            }
            
        });

        pane.add(new JLabel("Name:"));
        pane.add(pName);
        pane.add(Box.createHorizontalStrut(5));
        
        pane.add(new JLabel("Voices:"));
        pane.add(pVoices);

        pane.add(Box.createHorizontalStrut(5));
        
        dspPane = new JPanel(new GridLayout(2, 1));
        dspPane.setMinimumSize(new Dimension(40, 0));
        dspPoly = createBar();
        dspTotal = createBar();

        dspPane.add(dspPoly);
        dspPane.add(dspTotal);
        
        pane.add(dspPane);
        pane.add(Box.createHorizontalStrut(5));
        
        pane.add(new JButton(action));
        pane.add(Box.createHorizontalStrut(5));
        pane.add(cableToggler);
        
        updateValues();
    } 
    
    private JProgressBar createBar()
    {
        GradientProgressBar gp = new GradientProgressBar();
        gp.setBackground(NomadClassicColors.TEXT_DISPLAY_BACKGROUND);
        gp.setForeground(Color.GREEN);
        gp.setGradient(Color.RED);
        gp.setEnabled(true);
        gp.setMinimum(0);
        gp.setMaximum(100);
        return gp;
    }
    
    public void setDocumentManager(DocumentManager dm)
    {
        if (this.documentManager!=dm)
        {
            if (this.documentManager!=null)
                this.documentManager.removeListener(this);
            this.documentManager = dm;
            if (this.documentManager!=null)
                this.documentManager.addListener(this);
        }
    }

    public void documentSelected( Document document )
    {
        updateValues();
        
    }

    public void documentRemoved( Document document )
    { }

    public void documentAdded( Document document )
    { }

    private void updateValues()
    {
        
        if (patch!=null)
        {
            patch.removeListener(this);
        }
        
        patch = null;
        document = null;
        
        if (documentManager==null)
        {
            cableToggler.setPatch(null);
            disableView();
            return;
        }
        
        document = documentManager.getSelection();
        PatchDocument doc = (PatchDocument) document;
        if (doc==null)
        {
            cableToggler.setPatch(null);
            disableView();
            return;
        }
        patch = doc.getPatch();
            
        pVoices.setEnabled(true);
        voices.setValue(patch.getHeader().getRequestedVoices());
        pName.setEnabled(true);
        String n = patch.getName();
        pName.setText(n == null ? "" : n);
        
        Slot slot = doc.getSlot();
        voices.setVoiceCount(slot==null?null:slot.getVoiceCount());
        
        if (patch!=null)
            patch.addPatchListener(this);

        cableToggler.setPatch(patch);
        updateCyclesInfo();
    }
    
    private void updateCyclesInfo()
    {
        if (patch==null)
        {
            dspPoly.setValue(0);
            dspTotal.setValue(0);
        }
        else
        {
            double pva = patch.getPolyVoiceArea().getCyclesTotal();
            double cva = patch.getCommonVoiceArea().getCyclesTotal();
            double total = Math2.roundTo(pva+cva, -2);
            pva = Math2.roundTo(pva, -2);

            dspPoly.setValue((int)pva);
            dspTotal.setValue((int)total);
        }
    }
    
    private void disableView()
    {
        pVoices.setEnabled(false);
        voices.setValue(32);
        pName.setEnabled(false);
        pName.setText("");
        updateCyclesInfo();
    }

    public void updateView()
    {
        updateValues();
    }
    
    private static class VoicesNumberModel extends AbstractSpinnerModel
    {
        
        private class Voices 
        {
            final int value;
            
            public Voices(int value)
            {
                this.value = value;
            }

            public String toString()
            {
                StringBuffer sb = new StringBuffer();
                if (value<10) sb.append(' ');
                sb.append(Integer.toString(value));
                sb.append("/");
                if (voiceCount==null) sb.append("- ");
                else
                {
                    if (voiceCount.intValue()<10) sb.append(' ');
                    sb.append(voiceCount);
                }
                return sb.toString();
            }
        }
        
        
        private Integer voiceCount = null;
        private Voices voices;
        private Number minimum;
        private Number maximum;
        
        public VoicesNumberModel(Number value, Number minimum, Number maximum) 
        {
            voices = new Voices(value.intValue());
            this.minimum = minimum;
            this.maximum = maximum;
        }
        
        public int getRequestedVoices()
        {
            return voices.value;
        }

        public void setVoiceCount(Integer i)
        {
            if (!(voiceCount==null?i==null:voiceCount.equals(i)))
            {
                this.voiceCount = i;
                fireStateChanged();
            }
        }

        public Object getValue()
        {
            return voices;
        }

        public void setValue( Object value )
        {
            if (value==null||(!(value instanceof Number||value instanceof Voices)))
                throw new IllegalArgumentException();
            
            int n = value instanceof Number ? ((Number)value).intValue() : ((Voices)value).value;
            if (n!=voices.value)
            {
                voices = new Voices(n);
                fireStateChanged();
            }
        }

        public Object getNextValue()
        {
            return (maximum.intValue()<=voices.value)?null: new Voices(voices.value+1);
        }

        public Object getPreviousValue()
        {
            return (minimum.intValue()>=voices.value)?null: new Voices(voices.value-1);
        }

        public String toString()
        {
            return voices.toString();
        }
        
    }


    public void patchHeaderChanged( Event e )
    { }

    public void patchPropertyChanged( Event e )
    {
        if (Patch.VAPOLY_CYCLES.equals(e.getPropertyName())
                ||Patch.VACOMMON_CYCLES.equals(e.getPropertyName()))
        {
            updateCyclesInfo();
        }
    }
    
}
