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
 * Created on Feb 15, 2006
 */
package net.sf.nmedit.nomad.main.dialog;

import java.awt.GridLayout;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import net.sf.nmedit.nomad.main.resources.AppIcons;
import net.sf.nmedit.nomad.patch.Format;
import net.sf.nmedit.nomad.patch.virtual.Header;
import net.sf.nmedit.nomad.patch.virtual.Patch;

import com.jgoodies.forms.factories.ComponentFactory;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class PatchSettingsDialog extends NomadDialog implements SwingConstants {

    private SpinnerNumberModel modelKeyboardRangeMin;
    private SpinnerNumberModel modelKeyboardRangeMax;
    private SpinnerNumberModel modelVelocityRangeMin;
    private SpinnerNumberModel modelVelocityRangeMax;
    private SpinnerNumberModel modelBendRange;
    private SpinnerNumberModel modelRequestedVoices;
    private SpinnerNumberModel modelPortamentoTime;
    private ChoosableModel modelOctaveShift;
    private ChoosableModel modelPortamento;
    private ChoosableModel modelPedal;
    private JCheckBox modelVoiceRetriggerCommonActive;
    private JCheckBox modelVoiceRetriggerPolyActive;
    
    public int getBendRange()
    {
        return (Integer) modelBendRange.getValue();
    }

    public void setBendRange( int bend_range )
    {
        modelBendRange.setValue(bend_range);
    }

    public int getKeyboardRangeMax()
    {
        return (Integer) modelKeyboardRangeMax.getValue();
    }

    public void setKeyboardRangeMax( int keyboard_range_max )
    {
        modelKeyboardRangeMax.setValue(keyboard_range_max);
    }

    public int getKeyboardRangeMin()
    {
        return (Integer) modelKeyboardRangeMin.getValue();
    }

    public void setKeyboardRangeMin( int keyboard_range_min )
    {
        modelKeyboardRangeMin.setValue(keyboard_range_min);
    }

    public int getOctaveShift()
    {
        return modelOctaveShift.getValue();
    }

    public void setOctaveShift( int octave_shift )
    {
        modelOctaveShift.setValue( octave_shift );
    }

    public boolean isPortamentoAutoEnabled()
    {
        return ((Integer) modelPortamento.getValue())==1;
    }

    public void setPortamentoAutoEnabled( boolean enable )
    {
        modelPortamento.setValue(enable?1:0);
    }

    public int getPortamentoTime()
    {
        return (Integer) modelPortamentoTime.getValue();
    }

    public void setPortamentoTime( int portamento_time )
    {
        modelPortamentoTime.setValue(portamento_time);
    }

    public int getRequestedVoices()
    {
        return ((Integer)modelRequestedVoices.getValue());
    }

    public void setRequestedVoices( int requested_voices )
    {
        modelRequestedVoices.setValue((requested_voices));
    }

    public int getVelocityRangeMax()
    {
        return (Integer) modelVelocityRangeMax.getValue();
    }

    public void setVelocityRangeMax( int velocity_range_max )
    {
        modelVelocityRangeMax.setValue(velocity_range_max);
    }

    public int getVelocityRangeMin()
    {
        return (Integer) modelVelocityRangeMin.getValue();
    }

    public void setVelocityRangeMin( int velocity_range_min )
    {
        modelVelocityRangeMin.setValue(velocity_range_min);
    }

    public boolean isVoiceRetriggerCommonActive()
    {
        return modelVoiceRetriggerCommonActive.isSelected();
    }

    public void setVoiceRetriggerCommonActive( boolean active )
    {
        modelVoiceRetriggerCommonActive.setSelected(active);
    }

    public boolean isVoiceRetriggerPolyActive()
    {
        return modelVoiceRetriggerPolyActive.isSelected();
    }

    public void setVoiceRetriggerPolyActive( boolean active )
    {
        modelVoiceRetriggerPolyActive.setSelected(active);
    }
    
    private void buildDialog()
    {
        setTitle("Patch Settings");
        setImage(AppIcons.IC_PATCH);
        setPackingEnabled(true);
        setScrollbarEnabled(false);

        final String vspace = "10px";
        final String hspace = "4px";
        FormLayout layout = new FormLayout(
                "min,"+vspace+", right:pref,30dlu, 10px, pref,"+vspace+", right:pref,30dlu",
                 "pref,"+hspace+", pref,"+hspace+", pref,"+hspace+", pref,"+hspace+", pref,"+hspace+", pref,"+hspace+", pref,"+hspace+", pref,"+hspace+"," +
                 "pref,"+hspace+", pref,"+hspace+", pref,"+hspace+", pref,"+hspace+", pref,"+hspace+", pref,"+hspace+", pref,"+hspace+", pref");
        setLayout(layout);
        
        final int headerw = 4; // header width
        int row = 1;
        CellConstraints cc = new CellConstraints();
        ComponentFactory cf = DefaultComponentFactory.getInstance();
        
        // Create Components
        modelKeyboardRangeMin = numberModel(Format.HEADER_KEYBOARD_RANGE_MIN_DEFAULT, 0, 127);
        modelKeyboardRangeMax = numberModel(Format.HEADER_KEYBOARD_RANGE_MAX_DEFAULT, 0, 127);
        modelVelocityRangeMin = numberModel(Format.HEADER_VELOCITY_RANGE_MIN_DEFAULT, 0, 127);
        modelVelocityRangeMax = numberModel(Format.HEADER_VELOCITY_RANGE_MAX_DEFAULT, 0, 127);
        modelBendRange = numberModel(Format.HEADER_BEND_RANGE_DEFAULT, 2, 24);
        modelRequestedVoices = numberModel(16, 1, 32);
        modelPortamentoTime = numberModel(Format.HEADER_PORTAMENTO_TIME_DEFAULT, 0, 127);
        
        JRadioButton[] octaveShift = new JRadioButton[] {
                new JRadioButton("-2"), new JRadioButton("-1"), new JRadioButton("0"),
                new JRadioButton("+1"), new JRadioButton("+2") };
        modelOctaveShift = new ChoosableModel(octaveShift);
        
        JRadioButton rbtnSustain = new JRadioButton("Sustain");
        JRadioButton rbtnOnOff = new JRadioButton("On/Off");
        JRadioButton[] pedal = new JRadioButton[] { rbtnSustain, rbtnOnOff };
        modelPedal = new ChoosableModel(pedal);
        rbtnSustain.setEnabled(false);
        rbtnOnOff.setEnabled(false);

        JRadioButton rbtnPortaNormal = new JRadioButton("Normal");
        JRadioButton rbtnPortaAuto = new JRadioButton("Auto");
        JRadioButton[] portamento = new JRadioButton[] { rbtnPortaNormal, rbtnPortaAuto };
        modelPortamento = new ChoosableModel(portamento);
        
        add(cf.createSeparator("Voices",LEFT), cc.xyw(1,row, headerw));
        row+=2;//2
        add(lbl("Requested"), cc.xy(1,row));
        add(spinner(modelRequestedVoices), cc.xy(3,row));
        row+=2;//3

        add(cf.createSeparator("Velocity Range",LEFT), cc.xyw(1,row, headerw));
        add(cf.createSeparator("Keyboard Range",LEFT), cc.xyw(6,row, headerw));
        row+=2;//4
        
        add(lbl("Max"), cc.xy(1,row));add( spinner(modelVelocityRangeMax), cc.xy(3,row));
        add(lbl("Max"), cc.xy(6,row));add( spinner(modelKeyboardRangeMax), cc.xy(8,row));
        row+=2;//5
        
        add(lbl("Min"), cc.xy(1,row));add( spinner(modelVelocityRangeMin), cc.xy(3,row));
        add(lbl("Min"), cc.xy(6,row));add( spinner(modelKeyboardRangeMin), cc.xy(8,row));
        row+=2;//6
        
        add(cf.createSeparator("Pedal Mode",LEFT), cc.xyw(1,row, headerw));
        add(cf.createSeparator("Bend Range",LEFT), cc.xyw(6,row, headerw));
        row+=2;//7
        
        add(rbtnSustain, cc.xy(1,row));
        add(lbl("Semitones"), cc.xy(6,row));add( spinner(modelBendRange), cc.xy(8,row));
        row+=2;//8
        add(rbtnOnOff, cc.xy(1,row));
        row+=2;//9
        
        add(cf.createSeparator("Portamento",LEFT), cc.xyw(1,row, headerw*2+1));
        row+=2;
        add(rbtnPortaNormal, cc.xy(1,row));
        add(rbtnPortaAuto, cc.xy(3,row));
        add(lbl("Time"), cc.xy(6,row));add( spinner(modelPortamentoTime), cc.xy(8,row));
        row+=2;
        
        add(cf.createSeparator("Octave shift",LEFT), cc.xyw(1,row, headerw));
        add(cf.createSeparator("Voice Retrigger",LEFT), cc.xyw(6,row, headerw));
        row+=2;
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 5));
        
        for (int i=0;i<octaveShift.length;i++) 
        {
            panel.add(octaveShift[i]);
        }
        add( panel, cc.xyw(1,row, 4));

        modelVoiceRetriggerCommonActive = new JCheckBox("Common");
        modelVoiceRetriggerPolyActive = new JCheckBox("Poly");
        
        add(modelVoiceRetriggerCommonActive, cc.xy(6,row));
        add(modelVoiceRetriggerPolyActive, cc.xy(8,row));
    }

// ------------------------------------------------------------------------
    
	private final Patch patch;
    private final Header header;

    public PatchSettingsDialog(Patch p)
    {
        buildDialog();
        this.patch = p;
        this.header = p.getHeader();
        restoreSettings();
    }
    
    public void restoreSettings()
    {
        setBendRange( header.getBendRange() );
        setKeyboardRangeMax( header.getKeyboardRangeMax() );
        setKeyboardRangeMin( header.getKeyboardRangeMin() );
        setOctaveShift( header.getOctaveShift() );
        setPortamentoAutoEnabled( header.isPortamentoAutoEnabled() );
        setPortamentoTime( header.getPortamentoTime() );
        setRequestedVoices( header.getRequestedVoices() );
        setVelocityRangeMax( header.getVelocityRangeMax() );
        setVelocityRangeMin( header.getVelocityRangeMin() );
        setVoiceRetriggerCommonActive( header.isVoiceRetriggerCommonActive() );
        setVoiceRetriggerPolyActive( header.isVoiceRetriggerPolyActive() );
        
    }
    
    private JSpinner spinner(SpinnerModel model)
    {
        return new WheelSpinner(model);
    }
    
    private static class WheelSpinner extends JSpinner implements MouseWheelListener
    {

        public WheelSpinner( SpinnerModel model )
        {
            super(model);
            addMouseWheelListener(this);
        }

        public void mouseWheelMoved( MouseWheelEvent e )
        {
            SpinnerModel model = getModel();
            boolean increase = e.getWheelRotation()<0;
            Object value = increase ? model.getNextValue() : model.getPreviousValue();
            if (value!=null)
                model.setValue(value);
            
        }
        
    }
    
    private JLabel lbl(String text)
    {
        return new JLabel(text);
    }
	
	public Patch getPatch() {
		return patch;
	}
	
	public void invoke() {
		invoke(new String[]{RESULT_OK, ":"+RESULT_CANCEL});
        
        if (isOkResult())
        {
            apply();
        }
	}
    
    public void apply()
    {
        // write modifications

        header.setBendRange(getBendRange());
        header.setKeyboardRangeMax(getKeyboardRangeMax());
        header.setKeyboardRangeMin(getKeyboardRangeMin());
        header.setOctaveShift(getOctaveShift());
        header.setPortamentoAutoEnabled(isPortamentoAutoEnabled());
        header.setPortamentoTime(getPortamentoTime());
        header.setRequestedVoices(getRequestedVoices());
        header.setVelocityRangeMax(getVelocityRangeMax());
        header.setVelocityRangeMin(getVelocityRangeMin());
        header.setVoiceRetriggerCommonActive(isVoiceRetriggerCommonActive());
        header.setVoiceRetriggerPolyActive(isVoiceRetriggerPolyActive());
    }

    private SpinnerNumberModel numberModel (int currentValue, int minValue, int maxValue)
    {
        SpinnerNumberModel model = new SpinnerNumberModel();
        model.setMinimum(minValue);
        model.setMaximum(maxValue);
        model.setStepSize(1);
        model.setValue(currentValue);
        return model;
    }

    private class ChoosableModel extends ButtonGroup 
    {
        private final JRadioButton[] rbuttons;
        
        public ChoosableModel(JRadioButton[] rbuttons)
        {
            this.rbuttons = rbuttons;
            for (JRadioButton btn : rbuttons)
            {
                add(btn);
            }
        }
        
        public int getValue()
        {
            for (int i=0;i<rbuttons.length;i++)
                if (rbuttons[i].isSelected())
                    return i;
            return -1;
        }
        
        public void setValue(int value)
        {
            if (value>=0 && value<rbuttons.length)
            {
                rbuttons[value].setSelected(true);
            }
        }
        
    }
    
}
