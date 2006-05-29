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
 * Created on Feb 14, 2006
 */
package net.sf.nmedit.nomad.main.dialog.decoration;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;

import net.sf.nmedit.nomad.main.dialog.NomadDialog;

import com.jgoodies.forms.builder.ButtonBarBuilder;

public class ButtonPane extends DialogPane implements ActionListener
{

    private NomadDialog dialog;

    public ButtonPane( NomadDialog dialog )
    {
        this.dialog = dialog;
        final int i = 10;

        setBorder( BorderFactory.createEmptyBorder( i, i, i, i ) );
    }

    private class OptionButton extends JButton
    {
        public OptionButton( String option )
        {
            setAlignmentY( 0 );
            addActionListener( ButtonPane.this );

            if (option != null && option.startsWith( ":" ))
            {
                setDefaultCapable( true );
                option = option.substring( 1 );
            }
            else
            {
                setDefaultCapable( false );
            }
            setText( option );
        }
    }

    public void actionPerformed( ActionEvent event )
    {
        if (event.getSource() instanceof OptionButton)
        {
            OptionButton ob = (OptionButton) event.getSource();

            if (dialog.setResult( ob.getText() ))
            {
                dialog.close();
            }
        }
    }

    public void setOptions( String[] options )
    {
        JButton[] buttons = new JButton[options.length];
        for (int i = 0; i < options.length; i++)
        {
            buttons[i] = new OptionButton( options[i] );
        }

        ButtonBarBuilder builder = new ButtonBarBuilder( this );
        builder.addGlue();
        builder.addGriddedButtons( buttons );
    }

}
