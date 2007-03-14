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
package net.sf.nmedit.jtheme.editor;

import java.awt.Component;
import java.text.ParseException;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;
import javax.swing.table.TableCellEditor;

import net.sf.nmedit.jtheme.xml.DefaultPropertyStringHandler;

public class PropertyTableCellEditor implements TableCellEditor, CellEditorListener
{
    
    private Map<Class, TableCellEditor> editorMap = new HashMap<Class, TableCellEditor>();
    private MyDefaultEditor defaultEditor = new MyDefaultEditor();
    
    private EventListenerList eventListeners = new EventListenerList();

    private TableCellEditor currentEditor = null;
    private DefaultEditor editor;
    
    public PropertyTableCellEditor(DefaultEditor editor)
    {
        this.editor = editor;
    }
    
    public PropertyTableModel getModel()
    {
        return editor.getPropertyTableModel();
    }
    
    protected DefaultPropertyStringHandler getDefaultPropertyStringHandler()
    {
        return editor.getDefaultPropertyStringHandler();
    }
    
    public void setEditor(Class<?> target, TableCellEditor editor)
    {
        editorMap.put(target, editor);
    }
    
    public void addCellEditorListener(CellEditorListener l)
    {
        eventListeners.add(CellEditorListener.class, l);
    }
    
    public void removeCellEditorListener(CellEditorListener l)
    {
        eventListeners.remove(CellEditorListener.class, l);
    }
    
    protected void setCurrentEditor(TableCellEditor editor)
    {
        if (this.currentEditor != null)
            uninstall(this.currentEditor);
        this.currentEditor = editor;
        if (this.currentEditor != null)
            install(this.currentEditor);
    }

    protected void install(TableCellEditor editor)
    {
        editor.addCellEditorListener(this);
    }
    
    protected void uninstall(TableCellEditor editor)
    {
        editor.removeCellEditorListener(this);
    }
    
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column)
    {
        {
            Class pclass = getModel().getPropertyClassForRow(row);
            TableCellEditor editor = editorMap.get(pclass);
            if (editor == null)
            {
                editor = defaultEditor;
                defaultEditor.propertyClass = pclass;
                defaultEditor.stringHandler = getDefaultPropertyStringHandler(); 
            }
            setCurrentEditor(editor);
        }
        
        return currentEditor.getTableCellEditorComponent(table, value, isSelected, row, column);
    }
    
    public boolean isCellEditable(EventObject anEvent)
    {
        return true;
    }

    public boolean shouldSelectCell(EventObject anEvent)
    {
        return true;
    }

    public void cancelCellEditing()
    {
        if (currentEditor != null)
            currentEditor.cancelCellEditing();
    }

    public Object getCellEditorValue()
    {
        return (currentEditor != null) ? currentEditor.getCellEditorValue() : null;
    }

    public boolean stopCellEditing()
    {
        if (currentEditor != null)
            return currentEditor.stopCellEditing();
        return true;
    }

    public void editingCanceled(ChangeEvent e)
    {
        // received from current editor
        
        e = new ChangeEvent(this);
        for (CellEditorListener l : eventListeners.getListeners(CellEditorListener.class))
            l.editingCanceled(e);
    }

    public void editingStopped(ChangeEvent e)
    {
        e = new ChangeEvent(this);
        for (CellEditorListener l : eventListeners.getListeners(CellEditorListener.class))
            l.editingStopped(e);        
    }

     
    protected class MyDefaultEditor extends DefaultCellEditor
    {
        
        DefaultPropertyStringHandler stringHandler;
        Class<?> propertyClass;

        public MyDefaultEditor()
        {
            super(new JTextField());

            ((JTextField) editorComponent).removeActionListener(delegate);
            
            delegate = new EditorDelegate() 
            {
                public void setValue(Object value) 
                {
                    if (propertyClass == String.class)
                        setText((String)value);
                    else
                        setText(value == null ? null : stringHandler.toString(propertyClass, value));
                }

                public Object getCellEditorValue() 
                {
                    String s = getText();
                    
                    if(propertyClass == String.class)
                        return s;
                    
                    try
                    {
                        return stringHandler.parseString(propertyClass, getText());
                    }
                    catch (ParseException e)
                    {
                        return null;
                    }
                }
            };
            
            ((JTextField) editorComponent).addActionListener(delegate);
        }
        
        protected String getText()
        {
            return ((JTextField) editorComponent).getText();
        }
        
        protected void setText(String text)
        {
            ((JTextField) editorComponent).setText(text);
        }
        
    }
    
}

