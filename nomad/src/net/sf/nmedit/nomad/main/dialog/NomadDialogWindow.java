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
package net.sf.nmedit.nomad.main.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import net.sf.nmedit.nomad.main.dialog.decoration.ButtonPane;
import net.sf.nmedit.nomad.main.dialog.decoration.InfoPane;
import net.sf.nmedit.nomad.main.dialog.decoration.TitlePane;
import net.sf.nmedit.nomad.util.NomadUtilities;

public class NomadDialogWindow extends JDialog
{

    public static void invoke( NomadDialog dialog, String[] options )
    {

        NomadDialogWindow window = new NomadDialogWindow( dialog, options );

        NomadUtilities.center( window );

        SwingUtilities.invokeLater( dialog );
        window.setModal( true );
        window.setVisible( true );

    }

    private NomadDialog dialog;

    public NomadDialogWindow( NomadDialog dialog, String[] options )
    {
        super( NomadDialogWindow.findActiveFrame() );

        setTitle( dialog.getTitle() );

        this.dialog = dialog;

        Dimension sz1 = dialog.getPreferredSize();
        Dimension size = new Dimension( 640 * 3 / 4, 480 * 3 / 4 );
        size.setSize( Math.max( sz1.width, size.width ), Math.max( sz1.height,
                size.height ) );

        setDefaultCloseOperation( DISPOSE_ON_CLOSE );

        getContentPane().setLayout( new BorderLayout() );

        if (dialog.getTitle() != null)
        {
            TitlePane titlePane = new TitlePane();
            titlePane.setTitle( dialog.getTitle() );
            titlePane.setImage( dialog.getImage() );
            getContentPane().add( titlePane, BorderLayout.NORTH );
            size.width = Math.max( size.width,
                    titlePane.getPreferredSize().width );
            size.height += titlePane.getPreferredSize().height;
        }

        if (dialog.isScrollbarEnabled())
        {
            JScrollPane scroll = new JScrollPane( dialog );
            scroll.setBorder( BorderFactory.createEmptyBorder() );
            getContentPane().add( scroll, BorderLayout.CENTER );
            dialog.setPreferredSize( null );
        }
        else
        {
            getContentPane().add( dialog, BorderLayout.CENTER );
        }

        if (dialog.getInfoText() != null && dialog.getInfoTitle() != null)
        {
            InfoPane info = new InfoPane();
            info.setTitle( dialog.getInfoTitle() );
            info.setText( dialog.getInfoText() );
            getContentPane().add( info, BorderLayout.WEST );
        }

        if (options != null)
        {
            ButtonPane btnPane = new ButtonPane( dialog );
            btnPane.setOptions( options );
            getContentPane().add( btnPane, BorderLayout.SOUTH );
        }

        dialog.window = this;

        setSize( size );
        invalidate();

        if (( !dialog.isScrollbarEnabled() ) && ( dialog.isPackingEnabled() ))
        {
            pack();
        }
    }

    public void done()
    {
        setVisible( false );
        while (getContentPane().getComponentCount() > 0)
            getContentPane().remove( getContentPane().getComponent( 0 ) );
        setModal( false );
        dialog.window = null;
    }

    private static Frame findActiveFrame()
    {
        Frame[] frames = JFrame.getFrames();
        for (int i = 0; i < frames.length; i++)
        {
            if (frames[i].isVisible())
            {
                return frames[i];
            }
        }
        return null;
    }
}
