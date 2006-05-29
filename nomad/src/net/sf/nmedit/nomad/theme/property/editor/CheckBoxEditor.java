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
 * Created on Mar 9, 2006
 */
package net.sf.nmedit.nomad.theme.property.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

import net.sf.nmedit.nomad.theme.component.NomadComponent;
import net.sf.nmedit.nomad.theme.property.Property;
import net.sf.nmedit.nomad.theme.property.Value;
import net.sf.nmedit.nomad.theme.property.editor.EditorEvent.EventId;


public class CheckBoxEditor extends Editor implements ActionListener
{

    private Value checkedValue   = null;

    private Value uncheckedValue = null;

    public CheckBoxEditor( Property property, NomadComponent component )
    {
        super( property, component, false );

        JCheckBox cb = new JCheckBox( property.getName() );

        cb.addKeyListener( new DefaultEditorKeyListener( this ) );
        cb.addActionListener(this);

        setEditorComponent( cb );
    }

    public boolean isSelected()
    {
        return getCheckBox().isSelected();
    }

    public void setSelected( boolean s )
    {
        getCheckBox().setSelected( s );
    }

    public void setCheckedValue( Value value )
    {
        this.checkedValue = value;
    }

    public void setUncheckedValue( Value value )
    {
        this.uncheckedValue = value;
    }

    public Value getCheckedValue()
    {
        return checkedValue;
    }

    public Value getUncheckedValue()
    {
        return uncheckedValue;
    }

    public JCheckBox getCheckBox()
    {
        return (JCheckBox) getEditorComponent();
    }

    public Value getValue()
    {
        return getCheckBox().isSelected() ? getCheckedValue()
                : getUncheckedValue();
    }

    public void actionPerformed( ActionEvent event )
    {
        fireEditorEvent(EventId.EDITING_STOPPED);
    }
    
}
