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
 * Created on May 15, 2006
 */
package net.sf.nmedit.nomad.patch.ui.popup;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Knob;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.MidiController;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.MidiControllerSet;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Module;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Morph;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Parameter;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Patch;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.VoiceArea;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.misc.Assignment;
import net.sf.nmedit.nomad.main.dialog.NomadDialog;
import net.sf.nmedit.nomad.theme.component.NomadControl;

public class ControlPopup extends JPopupMenu
{

    //private NomadControl control;
    private Parameter parameter;
    private Module module;
    private VoiceArea voiceArea;
    private Patch patch ;

    public ControlPopup(NomadControl control)
    {
        super(control.getParameterInfo().getName());
        //this.control = control;
        parameter = control.getParameter();
        module = parameter.getModule();
        voiceArea = module.getVoiceArea();
        patch = voiceArea.getPatch();
        
        addMenuItems();
    }

    private void addMenuItems()
    {
        // quick access
        add(new DefaultValueAction());
        add(new ZeroMorphAction());
        addSeparator();
        
        // knobs
        JMenu mnKnob = new JMenu("Knob");
        int cnt = 1;
        
        for (Knob knob : patch.getKnobs())
        {
            mnKnob.add(new JRadioButtonMenuItem(new KnobAction(knob))).setSelected(knob.isAssignedTo(parameter));
            
            switch (cnt)
            {
                case 6:
                case 12:
                case 15:
                case 18:
                    mnKnob.addSeparator();
            }
            
            cnt ++;
            
        }
        mnKnob.addSeparator();
        mnKnob.add(new KnobAction(null)).setEnabled(parameter.getAssignedKnob()!=null);
        
        // morph
        JMenu mnMorph = new JMenu("Morph");
        
        for (Morph morph : patch.getMorphs())
        {
            mnMorph.add(new JRadioButtonMenuItem(new MorphAction(morph))).setSelected(morph.contains(parameter));
        }
        mnMorph.addSeparator();
        mnMorph.add(new MorphAction(null)).setEnabled(parameter.getAssignedMorph()!=null);
        
        // midi
        JMenu mnMidi = new JMenu("MIDI Controller");

        MidiController other = getAssignedMC();
        for (MidiController mc : patch.getMidiControllers().getPrimaryMidicontrollers())
        {
            mnMidi.add(new JRadioButtonMenuItem(new MCAction(mc))).setSelected(mc.isAssignedTo(parameter));
            if (other==mc)
                other=null;
        }
        
        mnMidi.addSeparator();
        JMenuItem item = mnMidi.add(new JRadioButtonMenuItem(new MCChooseFromList()));
        if (other!=null)
            item.setSelected(true);
        
        mnMidi.addSeparator();
        mnMidi.add(new MCAction(null)).setEnabled(getAssignedMC()!=null);
        
        add(mnKnob);
        add(mnMorph);
        add(mnMidi);
    }

    private class DefaultValueAction extends AbstractAction
    {
        public DefaultValueAction()
        {
            putValue(NAME, "Default Value");
        }

        public void actionPerformed( ActionEvent e )
        {
            parameter.setValue(parameter.getDefaultValue());
        }
        
    }

    private class ZeroMorphAction extends AbstractAction
    {
        public ZeroMorphAction()
        {
            putValue(NAME, "Zero Morph");
            setEnabled(parameter.getAssignedMorph()!=null);
        }

        public void actionPerformed( ActionEvent e )
        {
            parameter.setMorphRange(0);
        }
        
    }
    
    private class KnobAction extends AbstractAction
    {
        private Knob knob;

        public KnobAction(Knob knob)
        {
            this.knob = knob;
            putValue(NAME, knob == null ? "Disable" : knob.getName());
        }

        public void actionPerformed( ActionEvent e )
        {
            if (knob!=null)
                knob.assignTo(parameter);
            else
            {
                parameter.getAssignedKnob().deAssign();
            }
        }
    }
    
    private class MorphAction extends AbstractAction
    {
        private Morph morph;

        public MorphAction(Morph morph)
        {
            this.morph = morph;
            putValue(NAME, morph == null ? "Disable" : morph.getName());
        }

        public void actionPerformed( ActionEvent e )
        {
            if (morph!=null)
                morph.add(parameter);
            else
            {
                parameter.getAssignedMorph().remove(parameter);
            }
        }
    }
    
    private class MCAction extends AbstractAction
    {
        private MidiController mc;

        public MCAction(MidiController mc)
        {
            this.mc = mc;
            putValue(NAME, mc == null ? "Disable" : mc.getName());
        }

        public void actionPerformed( ActionEvent e )
        {
            if (mc!=null)
            {
                MidiController prevAssignment = getAssignedMC();
                if (prevAssignment!=mc)
                {
                    if (prevAssignment!=null)
                        prevAssignment.deAssign();
                    mc.assignTo(parameter);
                }
            }
            else
            {
                getAssignedMC().deAssign();
            }
        }
    }
    
    private class MCChooseFromList extends AbstractAction
    {
        public MCChooseFromList()
        {
            putValue(NAME, "Other");
        }

        public void actionPerformed( ActionEvent e )
        {
            (new MCDialog()).invoke();
        }
    }
    

    public class MCDialog extends NomadDialog implements TableModel {
        
        MidiControllerSet mcSet = patch.getMidiControllers();
        JTable table = new JTable();

        public MCDialog() 
        {
            setTitle("MIDI Controller");
            setPreferredSize(new Dimension(320,240));
            setScrollbarEnabled(false);
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBorder(null);
            table.setModel(this);
            table.getColumnModel().getColumn(0)
                .setPreferredWidth(30);
            table.getTableHeader().setMinimumSize(new Dimension(0, 20));
            table.getTableHeader().setPreferredSize(new Dimension(320, 20));
            table.getTableHeader().setReorderingAllowed(false);
            add(table.getTableHeader());
            add(new JScrollPane(table));
        }
        
        public void invoke() {
            super.invoke(new String[]{RESULT_OK, RESULT_CANCEL, "Remove"});
            
            if (isOkResult())
            {
                int index = table.getSelectedRow();
                if (index>=0 && index<mcSet.size())
                {
                    MidiController mc = mcSet.get(index);
                    mc.assignTo(parameter);
                }
            }
            else if (getResult()!=null && "Remove".equals(getResult()))
            {
                MidiController mc = getAssignedMC();
                if (mc!=null)
                    mc.deAssign();
            }
        }

        public int getRowCount()
        {
            return mcSet.size();
        }

        public int getColumnCount()
        {
            return 4;
        }

        public String getColumnName( int columnIndex )
        {
            switch (columnIndex)
            {
                case 0: return "ID";
                case 1: return "MIDI Ctrl";
                case 2: return "Module";
                case 3: return "Parameter";
                default: return "";
            }
        }

        public Class<?> getColumnClass( int columnIndex )
        {
            return String.class;
        }

        public boolean isCellEditable( int rowIndex, int columnIndex )
        {
            return false;
        }

        public Object getValueAt( int rowIndex, int columnIndex )
        {
            MidiController mc = mcSet.get(rowIndex);
            
            Assignment a = mc.getAssignment();
            
            switch (columnIndex)
            {
                case 0: return Integer.toString(mc.getID());
                case 1: return mc.getName();
                case 2:  
                    if (a instanceof Parameter)
                    {
                        return ((Parameter)a).getModule().getName();
                    }
                    return "";
                case 3: 
                    if (a instanceof Parameter)
                    {
                        return ((Parameter)a).getName();
                    }
                    else if (a instanceof Morph)
                    {
                        return ((Morph)a).getName();
                    }
                    return "";
                default: return "";
            }
        }

        public void setValueAt( Object aValue, int rowIndex, int columnIndex )
        { }

        public void addTableModelListener( TableModelListener l )
        { }

        public void removeTableModelListener( TableModelListener l )
        { }
        
    }

    
    
    private MidiController getAssignedMC()
    {
        for (Iterator<MidiController> i = patch.getMidiControllers().getAssignedMidiControllers(); i.hasNext();)
        {
            MidiController mc = i.next();
            if (mc.isAssignedTo(parameter))
                return mc;
        }
        return null;
    }
    
}
