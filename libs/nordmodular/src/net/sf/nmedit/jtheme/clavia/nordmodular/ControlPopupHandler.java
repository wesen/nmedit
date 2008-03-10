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

import net.sf.nmedit.jnmprotocol2.MorphKeyboardAssignmentMessage;
import net.sf.nmedit.jpatch.PParameter;
import net.sf.nmedit.jpatch.clavia.nordmodular.Knob;
import net.sf.nmedit.jpatch.clavia.nordmodular.MidiController;
import net.sf.nmedit.jpatch.clavia.nordmodular.NMPatch;
import net.sf.nmedit.jpatch.clavia.nordmodular.PNMMorphSection;
import net.sf.nmedit.jpatch.clavia.nordmodular.PNMMorphSection.Assignments;
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
                ControlPopup popup = new ControlPopup(control,parameter);
                popup.show(e, component);
            }
        }
    }
    
    // used for multiparameters components e.g. seq editor
    public void showPopup(MouseEvent e, JTComponent component, int parameterIndex)
    {
        if (component instanceof JTControl)
        {
            JTControl control = (JTControl) component;
            JTControlAdapter adapter = control.getControlAdapter(parameterIndex);
            PParameter parameter = adapter == null ? null : adapter.getParameter();
            
            if (parameter != null)
            {
                ControlPopup popup = new ControlPopup(control,parameter);
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
        private JMenu submenuKeyboard;
        
        private PParameter parameter;
        
        private NMPatch patch;

		private JTControl control;
        
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
        
        public ControlPopup(JTControl control, PParameter parameter)
        {
            this.parameter = parameter;
            this.control = control;
            AbstractAction tmpa;
            int tmp;
            
            popup = new JPopupMenu();
            popup.add(new ParameterAction(this, ParameterAction.DEFAULTVALUE));
            if (!(control.getParent() instanceof JTMorphModule)) {
            	popup.add(new ParameterAction(this, ParameterAction.ZEROMORPH));
            }

            // Knob
            submenuKnob = new JMenu("Knob");
            
            tmp = getPatch().getKnobs().getKnobIndex(parameter);
            if (tmp >= 0) {
            	popup.add(new ParameterAction(this, ParameterAction.DISABLE_KNOB, tmp));
            }
            int tmpM = getPatch().getMorphSection().getAssignedMorph(getParameter());
            if (tmpM >= 0) {
            	popup.add(new ParameterAction(this, ParameterAction.DISABLE_MORPH, tmpM));
            }
            for (int i = 0; i < 120; i++) {
            	if (getPatch().getMidiControllers().get(i).getParameter() == getParameter()) {
            		popup.add(new ParameterAction(this, ParameterAction.DISABLE_MIDI, i));
            	}
            }

            popup.addSeparator();

            for (int i=0;i<=20;i++)
            {
                tmpa = new ParameterAction(this, ParameterAction.KNOB, i);
                if (tmp==i)
                {
                    tmpa.setEnabled(false);
                    //tmpa.putValue(mleAbstractAction.SELECTED_KEY, Boolean.TRUE);
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
            
            if (!(control.getParent() instanceof JTMorphModule)) {
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
            } else {
            	JTMorphModule morphModule = (JTMorphModule)control.getParent();
            	int idx = morphModule.getMorphIndex(control);
            	if (idx >= 0) {
            		Assignments assignments = getPatch().getMorphSection().getAssignments(idx);
            		if (assignments.size() > 0) {
            			popup.addSeparator();
            			for (PParameter p : assignments) {
            				popup.add(new ParameterAction(this, ParameterAction.DISABLE_FROM_MORPH, p));
            			}
            			popup.addSeparator();
            		}
            	}
            }
            
            // MIDI Controller
            submenuMIDI = new JMenu("MIDI Controller");
            submenuMIDI.add(new ParameterAction(this, ParameterAction.MIDI, MidiController.MODULATION_WHEEL));
            submenuMIDI.add(new ParameterAction(this, ParameterAction.MIDI, MidiController.FootPedal));
            submenuMIDI.add(new ParameterAction(this, ParameterAction.MIDI, MidiController.VOLUME));
            submenuMIDI.addSeparator();
            for (int i = 0; i < 120; i+=10) {
            	JMenu subMenu = new JMenu("CC " + i + " to " + (i  + 9 ));
            	for (int j = i ; j < (i + 10) && j <= 120; j++) {
            		if (MidiController.isValidCC(j))
            			subMenu.add(new ParameterAction(this, ParameterAction.MIDI, j));
            	}
            	submenuMIDI.add(subMenu);
            }
            submenuMIDI.addSeparator();
            submenuMIDI.add(new ParameterAction(this, ParameterAction.MIDI, -2));
            submenuMIDI.addSeparator();
            submenuMIDI.add(new ParameterAction(this, ParameterAction.MIDI, -1));
            
            popup.add(submenuMIDI);
            
            if (parameter.getParentComponent().getParentComponent() == getPatch().getMorphSection())
            {
                popup.addSeparator();
                submenuKeyboard = new JMenu("Keyboard");
                submenuKeyboard.add(new ParameterAction(this, ParameterAction.KB, MorphKeyboardAssignmentMessage.KEYBOARD_VELOCITY));
                submenuKeyboard.add(new ParameterAction(this, ParameterAction.KB, MorphKeyboardAssignmentMessage.KEYBOARD_NOTE));
                submenuKeyboard.addSeparator();
                submenuKeyboard.add(new ParameterAction(this, ParameterAction.KB, MorphKeyboardAssignmentMessage.KEYBOARD_DISABLE));
                popup.add(submenuKeyboard);
            }
            
            /*
            popup.add(new AbstractAction(){

                {
                    putValue(NAME, "TEST");
                }
                
                public void actionPerformed(ActionEvent arg0)
                {
                 
                    testSettingsMessage();
                    
                }});
            */
            
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
            // ----
            // Keyboard > (only in morph section!)
            //          Velocity
            //          Note
            //          ----
            //          Disable
            
        }

        /*
        void testSettingsMessage()
        {
            
            NMPatch patch = getPatch();
            
            NmSlot slot = (NmSlot) patch.getSlot();
            NordModular nm = (NordModular) slot.getSynthesizer();
            
            
            PatchSettingsMessage msg = new PatchSettingsMessage();
            msg.setDefaults(slot.getSlotIndex(), slot.getPatchId(), 0x0);
            
            Header h = patch.getHeader();
            
            msg.set("kbrangeMin1", 0);
            msg.set("kbrangeMin2", 0);
            msg.set("kbrangeMax1", 1);
            msg.set("kbrangeMax2", 0);
            msg.set("velocityRangeMin1", 0);
            msg.set("velocityRangeMin2", 0);
            msg.set("velocityRangeMax1", 1);
            msg.set("velocityRangeMax2", 0);
            msg.set("bendRange", h.getBendRange());
            msg.set("portamentoTime1", 0);
            msg.set("portamentoTime2", 1);
            msg.set("portamentoType", 0);
            msg.set("pedalMode", 0);
            msg.set("voiceCount", 1);
            msg.set("dividerBar1", 0);
            msg.set("dividerBar2", 0);
            msg.set("octaveShift1", 0);
            msg.set("octaveShift2", 0);
            msg.set("red", 0);
            msg.set("blue", 0);
            msg.set("yellow", 0);
            msg.set("gray", 0);
            msg.set("green", 0);
            msg.set("purple", 0);
            msg.set("white", 0);
            msg.set("voiceRetriggerCommon", 0);
            msg.set("voiceRetriggerPoly", 0);

            System.out.println(msg);
            try
            {
            nm.getProtocol().send(msg);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }*/
    }
    
    private static class ParameterAction extends AbstractAction
    {

        /**
         * 
         */
        private static final long serialVersionUID = -6529948398460343031L;
        public static final String DEFAULTVALUE = "Default value";
        public static final String ZEROMORPH = "Zero morph";
        public static final String KNOB = "Knob";
        public static final String MORPH = "Morph";
        public static final String DISABLE_KNOB = "Disable Knob";
        public static final String DISABLE_MORPH = "Disable Morph";
        public static final String DISABLE_FROM_MORPH = "Disable From Morph";
        public static final String DISABLE_MIDI = "Disable Midi";
        public static final String MIDI = "MIDI";
        public static final String KB = "KB";
        
        private ControlPopup parent;
        private int index;
        private PParameter parameter = null;
        
        public PParameter getMorphParameter()
        {
            return (getParameter() == null) ? null : getParameter().getExtensionParameter();
        }
        
        public ParameterAction(ControlPopup parent, String actionCommand)
        {
            this(parent, actionCommand, -1);
        }

        public ParameterAction(ControlPopup parent, String actionCommand, PParameter parameter)
        {
            this(parent, actionCommand, -1, parameter);
        }
        
        public ParameterAction(ControlPopup parent, String actionCommand, int index) {
        	this(parent, actionCommand, index, null);
        }

        public ParameterAction(ControlPopup parent, String actionCommand, int index, PParameter parameter)
        {
        	this.parameter = parameter;
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
                setEnabled(getMorphParameter() != null /*&& morph enabled*/);
                name = ZEROMORPH;
            }
            else if (actionCommand == DISABLE_MORPH) {
            	name = "Disable Morph " + (index + 1);
            	setEnabled(true);
            }
            else if (actionCommand == DISABLE_FROM_MORPH) {
            	name = "Remove Morph from " + getParameter().getParentComponent().getTitle() + extraInfo(getParameter());
            	setEnabled(getMorphParameter() != null);
            }
            else if (actionCommand == MORPH)
            { 
                if (index>4) throw new IllegalArgumentException("invalid morph group:"+index);
                boolean MorphAssignmentSupported = getMorphParameter() != null;
                if (index>=0)
                {
                    name = "Group "+(index+1);
                    if (MorphAssignmentSupported && index>=0)
                    {
                        PNMMorphSection ms = getPatch().getMorphSection();
                        int assignedToMorph = ms.getAssignedMorph(getParameter());
                        
                        if (index == assignedToMorph)
                        {
                            name += extraInfo(getParameter());
                        }
                        else if (!ms.getAssignments(index).isFull())
                        {
                            setEnabled(true);
                        }
                    }
                }
                else // disable case
                {
                    name = "Disable";
                    boolean assignedToMorph = getPatch().getMorphSection().getAssignedMorph(getParameter())>=0;
                    setEnabled(assignedToMorph);
                }
            }
            else if (actionCommand == DISABLE_KNOB) {
            	name = "Disable ";
            	Knob k = getPatch().getKnobs().get(index);
            	name += k.getName();
            	setEnabled(true);
            } else if (actionCommand == KNOB)
            {
                setEnabled(true);
                if (index<0)
                {
                    name = "Disable";
                }
                else
                {
                    Knob k = getPatch().getKnobs().get(index);
                    name = k.getName();
                    
                    if (k.getParameter() != null)
                    {
                        name += extraInfo(k.getParameter());
                    }
                    
                }
            }
            else if (actionCommand == DISABLE_MIDI) {
            	name = "Disable ";
                name += MidiController.getDefaultName(index);
            	setEnabled(true);
            }
            else if (actionCommand == MIDI)
            {   
                boolean assigned = false;
                if (index>=0)
                {
                    assigned = getPatch().getMidiControllers().get(index)
                    .getParameter() == getParameter();
                }
                
                if (MidiController.isValidCC(index))
                {
                    setEnabled(!assigned);
                    MidiController mc = getPatch().getMidiControllers().get(index);
                    name = MidiController.getDefaultName(index)+extraInfo(mc.getParameter());
                }
                else
                {
                    if (index==-1)
                    {                    
                        setEnabled(getPatch().getMidiControllers().getMidiControllerIndex(getParameter())>=0);
                        name = "Disable";
                    }
                    else if (index==-2)
                    {
                        setEnabled(true);
                        name = "Other...";
                    }
                    else
                        throw new IllegalArgumentException("invalid midi controller ID: "+index);
                }
            }
            else if (actionCommand == KB && getParameter().getParentComponent().getParentComponent()==getPatch().getMorphSection())
            {
                PNMMorphSection ms = 
                    (PNMMorphSection)getParameter().getParentComponent().getParentComponent();
                
                setEnabled(ms.getKeyboardAssignment(getParameter().getComponentIndex()).getValue()!=index);
                
                if (index == MorphKeyboardAssignmentMessage.KEYBOARD_VELOCITY)
                    name = "Velocity";
                else if (index == MorphKeyboardAssignmentMessage.KEYBOARD_NOTE)
                    name = "Note";
                else
                    name = "Disable";
            }
            else
            {
                throw new IllegalArgumentException("invalid action command:"+actionCommand);
            }
            
            putValue(NAME, name);
            
        }
        
        private String extraInfo(PParameter p)
        {
            if (p == null) return "";
            String moduleName = p.getParentComponent().getName();
            String paramName = p.getName();
            return " ("+moduleName+"/"+paramName+")";
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
            else if (command == DISABLE_KNOB){
            	deassignKnob();
            } else if (command == DISABLE_MORPH) {
            	deassignMorph();
            } else if (command == DISABLE_FROM_MORPH) {
            	deassignMorph();
            } else if (command == DISABLE_MIDI) {
            	deassignMidiCtrl();
            }
            else if (command == MORPH)
            {
                if (index<0)
                    deassignMorph();
                else assignMorph();
            }
            else if (command == ZEROMORPH)
            {
            	getMorphParameter().setValue(0);
            }
            else if (command == DEFAULTVALUE)
            {
                PParameter p = getParameter();
                p.setValue(p.getDefaultValue());
            }
            else if (command == MIDI)
            {
                if (index>=0) assignMidiCtrl();
                else if (index==-1) deassignMidiCtrl();
                else if (index==-2) MidiCtrlAssignmentDialog();
            }
        }
        
        private void MidiCtrlAssignmentDialog()
        {
            MidiCtrlFrm.showDialog(parent.control, getPatch().getMidiControllers());
        }

        PParameter getParameter()
        {
        	if (parameter == null)
        		return parent.getParameter();
        	else
        		return parameter;
        }
        
        NMPatch getPatch()
        {
            return parent.getPatch();
        }

        private void deassignMidiCtrl()
        {
            getPatch().getMidiControllers().deassign(getParameter());
        }

        public void assignMidiCtrl()
        {
            MidiController cc = getPatch().getMidiControllers().get(index);
            cc.setParameter(getParameter());
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
            getMorphParameter().setValue(0);

            PNMMorphSection m = getPatch().getMorphSection();
            int group = m.getAssignedMorph(getParameter());
            if (group>=0)
                m.getAssignments(group).remove(getParameter());
        }

        public void assignMorph()
        {
        	
        	PParameter parameter = getParameter();
            PNMMorphSection m = getPatch().getMorphSection();
            int group = m.getAssignedMorph(parameter);
            if (group>=0)
                m.getAssignments(group).remove(parameter);
            m.getAssignments(index).add(parameter);
              
        }
        
    }

}
