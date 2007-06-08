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
package net.sf.nmedit.jtheme.clavia.nordmodular;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;

import net.sf.nmedit.jpatch.PParameter;
import net.sf.nmedit.jpatch.clavia.nordmodular.Knob;
import net.sf.nmedit.jpatch.clavia.nordmodular.MidiController;
import net.sf.nmedit.jpatch.clavia.nordmodular.NMPatch;
import net.sf.nmedit.jpatch.clavia.nordmodular.PNMMorphSection;
import net.sf.nmedit.jtheme.JTPopupHandler;
import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.JTControl;
import net.sf.nmedit.jtheme.component.JTControlAdapter;

public class ControlPopupHandler implements JTPopupHandler
{

    public void showPopup(MouseEvent e, JTComponent component)
    {
        if (component instanceof JTControl)
        {
            JTControl control = (JTControl) component;
            JTControlAdapter adapter = control.getControlAdapter();
            PParameter parameter = adapter == null ? null : adapter.getParameter();
            
            if (parameter != null)
            {
                ControlPopup popup = new ControlPopup(parameter);
                popup.show(e, component);
            }
        }
    }
    
    private static class ControlPopup
    {
        
        private JPopupMenu popup;

        private JMenu submenuKnob;
        private JMenu submenuMorph;
        private JMenu submenuMIDI;
        
        private PParameter parameter;
        
        private NMPatch patch;
        
        NMPatch getPatch()
        {
            if (patch == null)
                patch = (NMPatch) parameter.getParentComponent()
                    .getParentComponent().getPatch();
            return patch;
        }
        
        public PParameter getParameter()
        {
            return parameter;
        }
        
        public void show(MouseEvent e, JTComponent component)
        {
            popup.show(component, e.getX(), e.getY());
        }

        public PParameter getMorphParameter()
        {
            return (parameter == null) ? null : parameter.getExtensionParameter();
        }
        
        public ControlPopup(PParameter parameter)
        {
            this.parameter = parameter;
            AbstractAction tmpa;
            int tmp;
            
            popup = new JPopupMenu();
            popup.add(new ParameterAction(this, ParameterAction.DEFAULTVALUE));
            popup.add(new ParameterAction(this, ParameterAction.ZEROMORPH));
            popup.addSeparator();
            // Knob
            submenuKnob = new JMenu("Knob");
            
            tmp = getPatch().getKnobs().getKnobIndex(parameter);
            
            
            for (int i=0;i<=20;i++)
            {
                tmpa = new ParameterAction(this, ParameterAction.KNOB, i);
                if (tmp==i)
                {
                    tmpa.setEnabled(false);
                    tmpa.putValue(AbstractAction.SELECTED_KEY, Boolean.TRUE);
                }
                submenuKnob.add(tmpa);
                if (i==5||i==11||i==14||i==17)
                    submenuKnob.addSeparator();
            }
            submenuKnob.addSeparator();

            tmpa = new ParameterAction(this, ParameterAction.KNOB);
            if (tmp<0)
                tmpa.setEnabled(false);
            submenuKnob.add(tmpa);
            popup.add(submenuKnob);
            // Morph
            submenuMorph = new JMenu("Morph");
            
            tmp = getPatch().getMorphSection().getAssignedMorph(getParameter());
            
            for (int i=0;i<4;i++)
            {
                tmpa = new ParameterAction(this, ParameterAction.MORPH, i);
                if (i==tmp)
                    tmpa.setEnabled(false);
                submenuMorph.add(tmpa);
            }
            submenuMorph.addSeparator();
            tmpa = new ParameterAction(this, ParameterAction.MORPH);
            if (tmp<0)
                tmpa.setEnabled(false);
            submenuMorph.add(tmpa);
            popup.add(submenuMorph);
            // MIDI Controller
            submenuMIDI = new JMenu("MIDI Controller");
            submenuMIDI.add(new ParameterAction(this, ParameterAction.MIDI, MidiController.MODULATION_WHEEL));
            submenuMIDI.add(new ParameterAction(this, ParameterAction.MIDI, MidiController.ExpressionPedal));
            submenuMIDI.add(new ParameterAction(this, ParameterAction.MIDI, MidiController.VOLUME));
            submenuMIDI.addSeparator();
            submenuMIDI.add(new ParameterAction(this, ParameterAction.MIDI, -2));
            submenuMIDI.addSeparator();
            submenuMIDI.add(new ParameterAction(this, ParameterAction.MIDI, -1));
            
            popup.add(submenuMIDI);
            
            // Default value
            // Zero morph
            // ----
            // Knob >
            //          Knob [1-6]
            //          ----
            //          Knob [7-12]
            //          ----
            //          Knob [13-15]
            //          ----
            //          Knob [16-18]
            //          ----
            //          Pedal
            //          After touch
            //          On/Off switch
            //          ----
            //          Disable
            // Morph >
            //          Group [1-4]
            //          ----
            //          Disable
            // MIDI Controller >
            //          Modulation Wheel
            //          Expression Pedal
            //          Main Volume
            //          ----
            //          Other ...
            //          ----
            //          Disable
            
        }
        
    }
    
    private static class ParameterAction extends AbstractAction
    {

        public static final String DEFAULTVALUE = "Default value";
        public static final String ZEROMORPH = "Zero morph";
        public static final String KNOB = "Knob";
        public static final String MORPH = "Morph";
        public static final String MIDI = "MIDI";
        
        private ControlPopup parent;
        private int index;
        

        public ParameterAction(ControlPopup parent, String actionCommand)
        {
            this(parent, actionCommand, -1);
        }
        
        public ParameterAction(ControlPopup parent, String actionCommand, int index)
        {
            putValue(ACTION_COMMAND_KEY, actionCommand);
            this.parent = parent;
            setEnabled(false);
            this.index = index;

            String name;
            
            if (actionCommand == DEFAULTVALUE)
            {
                name = DEFAULTVALUE;
                PParameter p = getParameter();
                setEnabled(p.getValue()!=p.getDefaultValue());
            }
            else if (actionCommand == ZEROMORPH)
            {
                // TODO check if morph is enabled
                setEnabled(parent.getMorphParameter() != null /*&& morph enabled*/);
                name = ZEROMORPH;
            }
            else if (actionCommand == MORPH)
            {
                // TODO check if morph is enabled
                setEnabled(parent.getMorphParameter() != null /*&& morph enabled*/);
                
                if (index>4)
                    throw new IllegalArgumentException("invalid morph group:"+index);
                
                name = (index<0) ? "Disable" : ("Group "+(index+1));
            }
            else if (actionCommand == KNOB)
            {
                setEnabled(true);
                if (index<0)
                    name = "Disable";
                else
                    name = getPatch().getKnobs().get(index).getName();
            }
            else if (actionCommand == MIDI)
            {
                if (MidiController.isValidCC(index))
                {
                    name = MidiController.getDefaultName(index);
                }
                else
                {
                    if (index==-1)
                        name = "Disable";
                    else if (index==-2)
                        name = "Other...";
                    else
                        throw new IllegalArgumentException("invalid midi controller ID: "+index);
                }
            }
            else
            {
                throw new IllegalArgumentException("invalid action command:"+actionCommand);
            }
            
            putValue(NAME, name);
            
        }

        public void actionPerformed(ActionEvent e)
        {
            if (!isEnabled()) return;
            
            final String command = e.getActionCommand();

            if (command == KNOB)
            {
                if (index<0)
                    deassignKnob();
                else assignKnob();
            }
            else if (command == MORPH)
            {
                if (index<0)
                    deassignMorph();
                else assignMorph();
            }
            else if (command == DEFAULTVALUE)
            {
                PParameter p = getParameter();
                p.setValue(p.getDefaultValue());
            }
        }
        
        PParameter getParameter()
        {
            return parent.getParameter();
        }
        
        NMPatch getPatch()
        {
            return parent.getPatch();
        }

        private void deassignKnob()
        {
            getPatch().getKnobs().deassign(getParameter());
        }

        public void assignKnob()
        {
            Knob k = getPatch().getKnobs().get(index);
            k.setParameter(getParameter());
        }

        public void deassignMorph()
        {
            PNMMorphSection m = getPatch().getMorphSection();
            int group = m.getAssignedMorph(getParameter());
            if (group>=0)
                m.getAssignments(group).remove(getParameter());
        }

        public void assignMorph()
        {
            PNMMorphSection m = getPatch().getMorphSection();
            m.getAssignments(index).add(getParameter());
        }
        
    }

}
