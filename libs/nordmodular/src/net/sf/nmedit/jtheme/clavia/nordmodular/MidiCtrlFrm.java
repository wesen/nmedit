/* Copyright (C) 2008 Christian Schneider
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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import net.sf.nmedit.jpatch.ImageSource;
import net.sf.nmedit.jpatch.ModuleDescriptions;
import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleDescriptor;
import net.sf.nmedit.jpatch.PParameter;
import net.sf.nmedit.jpatch.clavia.nordmodular.MidiController;
import net.sf.nmedit.jpatch.clavia.nordmodular.MidiControllerSet;
import net.sf.nmedit.jtheme.component.JTControl;

public class MidiCtrlFrm implements ListSelectionListener
{

    public final static String CLOSE = "close";
    private final static String ASSIGN_TO_OPTION = "assign to";
    private final static String DEASSIGN_FROM_OPTION = "deassign from";

    private JPanel contentPane;
    private JTable table;
    private MidiControllerTableModel tableModel;
    private MidiControllerSet mcset;
    private PParameter parameter;

    private FormAction AC_CLOSE = new FormAction(CLOSE);
    private FormAction AC_ASSIGN_TO = new FormAction(ASSIGN_TO_OPTION);
    private FormAction AC_DEASSIGN_FROM = new FormAction(DEASSIGN_FROM_OPTION);
    private JDialog dialog;
    
    public MidiCtrlFrm(PParameter parameter, MidiControllerSet mcset)
    {
        this.mcset = mcset;
        this.parameter = parameter;
        createLayout();
    }
    
    public static void showDialog(JTControl control, MidiControllerSet mcset)
    {
        MidiCtrlFrm frm = new MidiCtrlFrm(control.getControlAdapter().getParameter(), mcset);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int width = 400;
        int height = 300;
        Rectangle bounds = new Rectangle((screen.width-width)/2, (screen.height-height)/2, width, height);
        JDialog dialog = new JDialog();
        dialog.setTitle("Midi Controller");
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.getContentPane().add(frm.contentPane);
        dialog.setModal(true);
        dialog.setBounds(bounds);
        frm.dialog = dialog;
        dialog.validate();
        dialog.setVisible(true);
    }
    
    private void createLayout()
    {
        contentPane = new JPanel(new BorderLayout());
        
        // table
        tableModel = new MidiControllerTableModel();
       
        for (MidiController mc: mcset)
        {
            tableModel.setParameter(mc.getControlId(), mc.getParameter());
        }
        
        table = new JTable(tableModel);
        
        table.getSelectionModel().addListSelectionListener(this);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        table.getColumnModel().getColumn(2).setCellRenderer(new ModuleCellRenderer());
        table.getColumnModel().getColumn(3).setCellRenderer(new ParameterCellRenderer());
        table.setRowHeight(19);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(110);
        table.getColumnModel().getColumn(2).setPreferredWidth(110);
        table.getColumnModel().getColumn(3).setPreferredWidth(160);
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(false);
        contentPane.add(new JScrollPane(table), BorderLayout.CENTER);
        
        JPanel buttonPane = new JPanel();
        
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
        buttonPane.add(new JButton(AC_ASSIGN_TO));
        buttonPane.add(strut());
        
        if (parameter != null)
        {
            PModule module = parameter.getParentComponent();
            PModuleDescriptor descriptor = module.getDescriptor();
            ModuleDescriptions md = descriptor.getModules();
            Image image = md.getImage(descriptor.get16x16IconSource());
            
            JLabel moduleLabel = new JLabel(
                    module.getName() + " / "+parameter.getName(), 
                    new ImageIcon(image), JLabel.LEADING);
            buttonPane.add(moduleLabel);
            buttonPane.add(strut());
        }

        buttonPane.add(Box.createHorizontalGlue());
        buttonPane.add(new JButton(AC_DEASSIGN_FROM));
        buttonPane.add(Box.createRigidArea(new Dimension(14, 0)));
        buttonPane.add(new JButton(AC_CLOSE));
        buttonPane.add(strut());
        
        contentPane.add(buttonPane, BorderLayout.SOUTH);
        
        // update actions
        updateActions();
        
        // select parameter
        int selectIndex = tableModel.indexOf(this.parameter);
        if (selectIndex>=0)
            table.setRowSelectionInterval(selectIndex, selectIndex);
    }

    private void updateActions()
    {
        int index = table.getSelectedRow();
        PParameter parameter = index<0 ? null : tableModel.getParameter(index);
        AC_DEASSIGN_FROM.setEnabled(index != 32 && parameter != null);
        AC_ASSIGN_TO.setEnabled(index != 32 && this.parameter != null && this.parameter != parameter);
    }

    public void valueChanged(ListSelectionEvent e)
    {
        if (e.getValueIsAdjusting())
            return; // button not released yet
        updateActions(); // row selection changed
    }
    
    private Component strut()
    {
        return Box.createRigidArea(new Dimension(4, 0));
    }

    public static void main(String args)
    {
        
    }
    
    private class FormAction extends AbstractAction
    {
        public FormAction(String command)
        {
            putValue(ACTION_COMMAND_KEY, command);
            if (command == CLOSE)
            {
                putValue(NAME, "Close");
            }
            else if (command == ASSIGN_TO_OPTION)
            {
                putValue(NAME, "Assign To");
            }
            else if (command == DEASSIGN_FROM_OPTION)
            {
                putValue(NAME, "Deassign Selected");
            }
        }

        public void actionPerformed(ActionEvent e)
        {
            String command = e.getActionCommand();
            if (command == CLOSE)
            {
                dialog.setVisible(false);
                dialog.dispose();
            }
            else if (command == ASSIGN_TO_OPTION)
            {
                int index = table.getSelectedRow();
                mcset.get(index).setParameter(MidiCtrlFrm.this.parameter);
                tableModel.assignToParameter(index, MidiCtrlFrm.this.parameter);   
                updateActions();
            }
            else if (command == DEASSIGN_FROM_OPTION)
            {
                int index = table.getSelectedRow();
                mcset.get(index).setParameter(null);
                tableModel.assignToParameter(index, null);   
                updateActions();
            }
        }
    }

    private class ParameterCellRenderer extends DefaultTableCellRenderer
    {
        public Component getTableCellRendererComponent(JTable table, 
                Object value,
                boolean isSelected, boolean hasFocus, int row, int column) 

        {
            String name = value == null ? "" : ((PParameter)value).getName(); 
            
            return super.getTableCellRendererComponent(table, 
                    name, isSelected, hasFocus, row, column);       
        }
    }
    
    private class ModuleCellRenderer extends DefaultTableCellRenderer
    {
        public Component getTableCellRendererComponent(JTable table, 
                Object value,
                boolean isSelected, boolean hasFocus, int row, int column) 

        {
            String name = "";
            PModule module = null;
            
            if (value != null)
            {
                module = (PModule) value;
                name = module.getName();
            }
            
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, 
                    name, isSelected, hasFocus, row, column);
            label.setIcon(null);
            if (module != null)
            {
                PModuleDescriptor descriptor = module.getDescriptor();
                ModuleDescriptions md = descriptor.getModules();
                ImageSource source = descriptor.get16x16IconSource();
                Image img = md.getImage(source);
                if (img != null)
                {
                    label.setIcon(new ImageIcon(img));
                }
            }
            return label;
        }
    }
    
    private class MidiControllerTableModel extends AbstractTableModel
    {
        
        private PParameter[] assignments = new PParameter[getRowCount()];
        
        public int indexOf(PParameter p)
        {
            for (int i=0;i<assignments.length;i++)
                if (p==assignments[i])
                    return i;
            return -1;
        }

        public void assignToParameter(int index, PParameter param)
        {
            if (assignments[index] == param)
                return;
            
            if (param == null)
            {
                assignments[index] = null;
                fireTableRowsUpdated(index, index);
            }
            else
            {
                int oldindex = indexOf(param);
                if (oldindex>=0)
                {
                    assignments[oldindex] = null;
                    fireTableRowsUpdated(oldindex, oldindex);
                }
                assignments[index] = param;
                fireTableRowsUpdated(index, index);
            }
        }
        
        public void setParameter(int index, PParameter param)
        {
            assignments[index] = param;
        }
        
        public PParameter getParameter(int index)
        {
            return assignments[index];
        }
        
        public int getColumnCount()
        {
            // #cc | name | module | parameter
            return 4;
        }

        public int getRowCount()
        {
            return 121;
        }
        
        public String getColumnName(int column) 
        {
            switch (column)
            {
                case 0:
                    return "#";
                case 1:
                    return "Controller";
                case 2:
                    return "Module";
                case 3:
                    return "Parameter";
                default:
                    return "";
            }
        }
        
        public Class<?> getColumnClass(int columnIndex) 
        {
            switch (columnIndex)
            {
                case 0:
                    return Object.class;
                case 1:
                    return Object.class;
                case 2:
                    return PModule.class;
                case 3:
                    return PParameter.class;
                default:
                    return super.getColumnClass(columnIndex);
            }
        }

        public Object getValueAt(int rowIndex, int columnIndex)
        {
            if (rowIndex == 32)
            {
                switch (columnIndex)
                {
                    case 0:
                        return "-";
                    case 1:
                        return "-";
                    default:
                        return null;
                }
            }
                

            int cc = rowIndex;
            switch (columnIndex)
            {
                case 0:
                    return cc;
                case 1:
                    return MidiController.getDefaultName(cc);
                case 2:
                {
                    PParameter parameter = assignments[cc];
                    if (parameter != null)
                        return parameter.getParentComponent();
                    return null;
                }
                case 3:
                {
                    PParameter parameter = assignments[cc];
                    if (parameter != null)
                        return parameter;
                    return null;
                }
                default:
                    return null;
            }
        }
        
    }
    
}
