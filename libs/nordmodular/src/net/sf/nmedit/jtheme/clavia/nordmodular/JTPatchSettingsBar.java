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
package net.sf.nmedit.jtheme.clavia.nordmodular;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractSpinnerModel;
import javax.swing.Action;
import javax.swing.BorderFactory;
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

import net.sf.nmedit.jpatch.clavia.nordmodular.NMPatch;
import net.sf.nmedit.jpatch.clavia.nordmodular.event.PPatchSettingsEvent;
import net.sf.nmedit.jpatch.clavia.nordmodular.event.PPatchSettingsListener;
import net.sf.nmedit.jpatch.event.PModuleContainerEvent;
import net.sf.nmedit.jpatch.event.PModuleContainerListener;
import net.sf.nmedit.jsynth.clavia.nordmodular.NmSlot;
import net.sf.nmedit.jtheme.clavia.nordmodular.misc.GradientProgressBar;
import net.sf.nmedit.nmutils.math.Math2;
import net.sf.nmedit.nmutils.swing.LimitedText;

public class JTPatchSettingsBar extends JPanel implements PModuleContainerListener, PropertyChangeListener, PPatchSettingsListener 

/*implements PatchListener */
{

    /**
     * 
     */
    private static final long serialVersionUID = 7266333456740910212L;

    private JTextField
        pName;

    private JSpinner
        pVoices;
    
    private VoicesNumberModel voices;
    
    private NMPatch patch = null;
   // private JPanel dspPane;
    private JProgressBar dspTotal;
    private JProgressBar dspPoly;
    
    //   private CableToggler cableToggler = new CableToggler();
    
    private JTNMPatch pui;
    
    public JTPatchSettingsBar(JTNMPatch patchUI)
    {
        final int STRUT = 3;
        this.pui = patchUI;
        this.patch = patchUI.getPatch();
         
        // panel
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setMinimumSize(new Dimension(100,28));
       // setPreferredSize(new Dimension(100,28));
        setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
        
        patch.getPolyVoiceArea().addModuleContainerListener(this);
        patch.getCommonVoiceArea().addModuleContainerListener(this);

        patch.addPropertyChangeListener(NMPatch.NAME, 
                new PropertyChangeListener()
        {

            public void propertyChange(PropertyChangeEvent evt)
            {
                pName.setText((String) evt.getNewValue());
            }
            
        }
        );
        
        
        pName = new JTextField(new LimitedText(16), "Name", 16);
        pName.setToolTipText("Patch Name");
        pName.setMaximumSize(new Dimension(160,28));
        pName.addKeyListener(new PatchNameKeyAdapter());
        pName.addActionListener(new ActionListener() {
            public void actionPerformed( ActionEvent e )
            {
                if (patch!=null) patch.setName(pName.getText());
            }});
        
        pVoices = new JSpinner(); 
        voices = new VoicesNumberModel(1, 1, 32);
        pVoices.setMaximumSize(new Dimension(90,28));
        pVoices.setToolTipText("requested voices / available voices");
        pVoices.setModel(voices);
        pVoices.setValue(1);//voices.getMinimum());
        
        voices.addChangeListener(new ChangeListener() {
            public void stateChanged( ChangeEvent e )
            {
                if (patch!=null) patch.getHeader().setRequestedVoices(voices.getRequestedVoices());
            }
            
        });

        JLabel l;
        l = new JLabel("Patch:");
        l.setLabelFor(pName);
        add(l);
        add(pName);
        //pane.add(Box.createHorizontalGlue());
        
        add(Box.createHorizontalStrut(STRUT));
        
        l = new JLabel("Voices:");
        l.setLabelFor(pVoices);
        add(l);
        Box b = Box.createVerticalBox();
        b.add(Box.createVerticalGlue());
        b.add(pVoices);
        b.add(Box.createVerticalGlue());
        add(b);
        l = null;
        add(Box.createHorizontalStrut(STRUT));
        
        add (new JLabel("Load:"));
        add(Box.createHorizontalStrut(STRUT));
        
        dspPoly = createBar();
        dspTotal = createBar();
        dspPoly.setToolTipText("dsp load: poly voice area");
        dspTotal.setToolTipText("dsp load: total");

        add(new JLabel("PVA:"));
        b = Box.createVerticalBox();
        b.add(Box.createVerticalGlue());
        b.add(dspPoly);
        b.add(Box.createVerticalGlue());
        
        add(b);
        add(Box.createHorizontalStrut(STRUT));
        add(new JLabel("E:"));
        b = Box.createVerticalBox();
        b.add(Box.createVerticalGlue());
        b.add(dspTotal);
        b.add(Box.createVerticalGlue());
        add(b);
        add(Box.createHorizontalStrut(STRUT));
        
        Action showNoteDialogA = null;
        
        add(new JButton(showNoteDialogA));
        //pane.add(cableToggler);

        // morphs
        JTMorphModule morphModule = new JTMorphModule(patchUI.getContext());
        morphModule.setModule(patch.getMorphSection().getMorphModule());
        add(morphModule);

        add(Box.createHorizontalGlue());
        
        updateValues();
        installListeners();
    } 
    
    private class PatchNameKeyAdapter extends KeyAdapter
    {
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
        }
    }
    
    private JProgressBar createBar()
    {
        GradientProgressBar gp = new GradientProgressBar();
        // gp.setBackground(NomadClassicColors.TEXT_DISPLAY_BACKGROUND);
        gp.setForeground(Color.GREEN);
        gp.setGradient(Color.RED);
        gp.setEnabled(true);
        gp.setMinimum(0);
        gp.setMaximum(100);/*
        gp.setPreferredSize(new Dimension(40,4));
        gp.setMaximumSize(new Dimension(40,10));*/
        gp.setMinimumSize(new Dimension(40, 12));
        gp.setPreferredSize(new Dimension(40, 14));
        gp.setMaximumSize(new Dimension(40, Short.MAX_VALUE));
        gp.setAlignmentY(JComponent.CENTER_ALIGNMENT);
        gp.setStringPainted(true);
        return gp;
    }
    
    private void updateValues()
    {
        /*
        if (patch!=null)
        {
            patch.removeListener(this);
        }*/

        patch = pui.getPatch();
            
        pVoices.setEnabled(true);
        voices.setValue(patch.getHeader().getRequestedVoices());
        pName.setEnabled(true);
        String n = patch.getName();
        pName.setText(n == null ? "" : n);

        NmSlot slot = (NmSlot) patch.getSlot();
        voices.setVoiceCount(slot==null?null:slot.getVoiceCount());
        /*
        if (patch!=null)
            patch.addPatchListener(this);
*/
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
            double pva = patch.getPolyVoiceArea().getTotalCycles();
            double cva = patch.getCommonVoiceArea().getTotalCycles();
            double total = Math2.roundTo(pva+cva, -2);
            pva = Math2.roundTo(pva, -2);

            dspPoly.setValue((int)pva);
            dspTotal.setValue((int)total);

            percentString(dspPoly);
            percentString(dspTotal);
        }
    }
    
    private static void percentString(JProgressBar bar)
    {
        int d = (bar.getMaximum()-bar.getMinimum());
        if (d <= 0)
        {
            bar.setString(null);
        }
        int f = (int) Math.round((100*(bar.getValue()-bar.getMinimum()))/(double)d);
        bar.setString(f+"%");
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

    public void moduleAdded(PModuleContainerEvent e)
    {
        updateCyclesInfo();
    }

    public void moduleRemoved(PModuleContainerEvent e)
    {
        updateCyclesInfo();
    }

    public void dispose()
    {
        uninstallListeners(); 
    }

    private void uninstallListeners()
    {
        patch.removePropertyChangeListener(NMPatch.SLOT_PROPERTY, this);   
        patch.removePatchSettingsListener(this);
        NmSlot slot = (NmSlot) patch.getSlot();
        if (slot != null) slot.removePropertyChangeListener(this);
    }
    
    private void installListeners()
    {
        patch.addPropertyChangeListener(NMPatch.SLOT_PROPERTY, this);   
        patch.addPatchSettingsListener(this);
        NmSlot slot = (NmSlot) patch.getSlot();
        if (slot != null) slot.addPropertyChangeListener(this);
    }

    public void propertyChange(PropertyChangeEvent evt)
    {
        if (NMPatch.SLOT_PROPERTY == evt.getPropertyName())
        {
            NmSlot slot = (NmSlot) evt.getOldValue();
            if (slot != null) slot.removePropertyChangeListener(this);
            slot = (NmSlot) evt.getNewValue();
            if (slot != null) slot.addPropertyChangeListener(this);
            updateValues();
        }
        else if (NmSlot.PROPERTY_VOICECOUNT == evt.getPropertyName())
        {
            updateValues();
        }
    }

    public void patchSettingsChanged(PPatchSettingsEvent e)
    {
        updateValues();
    }

/*
    public void patchHeaderChanged( Event e )
    { }

    public void patchPropertyChanged( Event e )
    {
        if (Patch.VAPOLY_CYCLES.equals(e.getPropertyName())
                ||Patch.VACOMMON_CYCLES.equals(e.getPropertyName()))
        {
            updateCyclesInfo();
        }
    }*/
    
}
