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
package org.nomad.theme.property.editor;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import org.nomad.theme.component.NomadComponent;
import org.nomad.theme.property.Value;
import org.nomad.theme.property.editor.EditorEvent.EventId;

public class ComboBoxEditor extends Editor implements ActionListener
{

    public ComboBoxEditor( org.nomad.theme.property.Property property,
            NomadComponent component, Value[] values )
    {
        super( property, component, false );

        JComboBox comboBox = new JComboBox( values );
        
        Value currentValue = property.encode(component);
        comboBox.setSelectedItem(currentValue);
        
        if (comboBox.getSelectedIndex()<0)
            comboBox.setSelectedIndex( 0 );

        comboBox.setFont( new Font( "SansSerif", Font.PLAIN, 11 ) );
        comboBox.setForeground( Color.BLUE );

        comboBox.addActionListener( this );
        comboBox.addKeyListener( new DefaultEditorKeyListener( this ) );

        setEditorComponent( comboBox );
    }

    public void setSelectedValue( Value value )
    {
        getComboBox().setSelectedItem( value );
    }

    public JComboBox getComboBox()
    {
        return (JComboBox) getEditorComponent();
    }

    public Value getValue()
    {
        return (Value) getComboBox().getSelectedItem();
    }

    public void actionPerformed( ActionEvent event )
    {
        fireEditorEvent( EventId.EDITING_STOPPED );
    }

}
