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

import java.awt.Color;
import java.awt.Font;

import javax.swing.JTextField;

import net.sf.nmedit.nomad.theme.component.NomadComponent;
import net.sf.nmedit.nomad.theme.property.Property;
import net.sf.nmedit.nomad.theme.property.Value;


public class TextEditor extends Editor
{
    public TextEditor( Property property, NomadComponent component )
    {
        super( property, component, false );

        JTextField textField = new JTextField();
        textField.addKeyListener( new DefaultEditorKeyListener( this ) );

        textField.setFont( new Font( "SansSerif", Font.PLAIN, 11 ) );
        textField.setForeground( Color.BLUE );
        textField.setBorder( null );

        textField.setText( property.encode( component ).getRepresentation() );
        textField.setSelectionStart( 0 );
        textField.setSelectionEnd( textField.getText().length() - 1 );

        setEditorComponent( textField );
    }

    public JTextField getTextField()
    {
        return (JTextField) getEditorComponent();
    }

    public Value getValue()
    {
        return getProperty().decode( getTextField().getText() );
    }

}
