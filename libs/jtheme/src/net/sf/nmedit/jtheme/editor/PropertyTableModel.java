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

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Arrays;

import javax.swing.JComponent;
import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.reflect.PropertyAccess;
import net.sf.nmedit.jtheme.reflect.PropertyAccessGroup;
import net.sf.nmedit.jtheme.reflect.PropertyDatabase;

public class PropertyTableModel implements TableModel
{

    private Object edited;
    private JTContext context;
    private PropertyAccessGroup accessGroup;
    private String[] properties;
    private ComponentListener componentListener = new ComponentListener();
    
    public PropertyTableModel()
    {
        recreateProperties();
    }
    
    public Class getPropertyClassForRow(int rowIndex)
    {
        String p = properties[rowIndex];
        PropertyAccess pa = accessGroup.getPropertyAccess(p);
        return pa != null ? pa.getPropertyClass() : null;
    }
    
    private void recreateProperties()
    {
        if (accessGroup == null)
        {
            if (properties == null || properties.length>0)
                properties = new String[0];
        }
        else
        {
            properties = new String[accessGroup.getParameterCount()];
            int index = 0;
            for (PropertyAccess pa : accessGroup)
                properties[index++] = pa.getPropertyName();
            Arrays.sort(properties);
        }
    }
    
    public void setEditedObject(Object edited)
    {
        Object oldValue = this.edited;
        if (oldValue != edited)
        {
            if (this.edited != null && this.edited instanceof JComponent)
                uninstallComponentListener((JComponent)this.edited);
            
            this.edited = edited;

            if (this.edited != null && this.edited instanceof JComponent)
                installComponentListener((JComponent)this.edited);
            recreateTable();
        }
    }
    
    protected void installComponentListener(JComponent c)
    {
        c.addComponentListener(componentListener);
    }
        
    protected void uninstallComponentListener(JComponent c)
    {
        c.removeComponentListener(componentListener);
    }
    
    protected void updateProperty(String propertyName)
    {
        int index = indexForProperty(propertyName);
        if (index<=0) return;
        
        fireTableValueChangedEvent(index, 1);
    }
    
    private int indexForProperty(String propertyName)
    {
        for (int i=0;i<properties.length;i++)
            if (propertyName.equals(properties[i]))
                return i;
        return -1;
    }

    private class ComponentListener extends ComponentAdapter
    {
        public void componentResized(ComponentEvent e) 
        {
            updateProperty("size");
        }   
        public void componentMoved(ComponentEvent e) 
        {
            updateProperty("location");
        }
    }
    
    public Object getEditedObject()
    {
        return edited;
    }
    
    public void setContext(JTContext context)
    {
        this.context = context;
    }
    
    public JTContext getContext()
    {
        return context;
    }
    
    protected void recreateTable()
    {
        clearTable();
        if (edited != null && context != null)
        {
            PropertyDatabase db = context.getBuilder().getPropertyDatabase();
            accessGroup = db.getPropertyAccessGroup(edited.getClass());
            // fire event
        }
        else
        {
            accessGroup = null;
        }
        recreateProperties();
        
        fireTableRecreatedEvent();
    }
    
    public void clearTable()
    {
        // 
    }

    public Class<?> getColumnClass(int columnIndex)
    {
        return columnIndex == 0 ? String.class : Object.class;
    }

    public int getColumnCount()
    {
        return 2;
    }

    public String getColumnName(int columnIndex)
    {
        return columnIndex == 0 ? "property" : "value";
    }

    public int getRowCount()
    {
        return properties.length;
    }

    public Object getValueAt(int rowIndex, int columnIndex)
    {
        if (accessGroup != null && edited != null && 0<= rowIndex && rowIndex<properties.length)
        {
            String p = properties[rowIndex];
            
            if(columnIndex == 0)
                return p;
            
            PropertyAccess pa = accessGroup.getPropertyAccess(p);
            if (pa == null)
                return null;
            
            return pa.get(edited);
        }
        return null;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return columnIndex == 1;
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        if (columnIndex == 1)
        {
            if (accessGroup != null && edited != null && 0<= rowIndex && rowIndex<properties.length)
            {
                String p = properties[rowIndex];
                
                PropertyAccess pa = accessGroup.getPropertyAccess(p);
                if (pa == null)
                    return ;
                
                pa.set(edited, aValue);
                fireTableValueChangedEvent(rowIndex, columnIndex);
            }
        }
    }
    
    private EventListenerList listenerList = new EventListenerList();

    public void addTableModelListener(TableModelListener l)
    {
        listenerList.add(TableModelListener.class, l);
    }

    public void removeTableModelListener(TableModelListener l)
    {
        listenerList.remove(TableModelListener.class, l);
    }
    

    public void fireTableRecreatedEvent()
    {
        fireChangedEvent(new TableModelEvent(this));
    }
    
    public void fireTableValueChangedEvent(int row, int col)
    {
        fireChangedEvent(new TableModelEvent(this, row, row+1, col));
    }
    
    public void fireChangedEvent(TableModelEvent e)
    {
        TableModelListener[] list = listenerList.getListeners(TableModelListener.class);

        for (int i=0;i<list.length;i++)
        {
            list[i].tableChanged(e);
        }
    }
    
}

