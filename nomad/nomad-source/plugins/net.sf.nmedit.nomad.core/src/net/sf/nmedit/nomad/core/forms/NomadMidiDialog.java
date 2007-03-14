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
package net.sf.nmedit.nomad.core.forms;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JWindow;

public class NomadMidiDialog extends NomadJDialog
{
    
    private NomadMidiDialogFrmHandler form;

    public NomadMidiDialog()
    {
        super();
        initDialog();
    }

    public NomadMidiDialog(Frame owner)
    {
        super(owner);
        initDialog();
    }

    public NomadMidiDialog(Dialog owner)
    {
        super(owner);
        initDialog();
    }

    public NomadMidiDialog(Window owner)
    {
        super(owner);
        initDialog();
    }

    public NomadMidiDialog(Frame owner, boolean modal)
    {
        super(owner, modal);
        initDialog();
    }

    public NomadMidiDialog(Frame owner, String title)
    {
        super(owner, title);
        initDialog();
    }

    public NomadMidiDialog(Dialog owner, boolean modal)
    {
        super(owner, modal);
        initDialog();
    }

    public NomadMidiDialog(Dialog owner, String title)
    {
        super(owner, title);
        initDialog();
    }

    public NomadMidiDialog(Window owner, ModalityType modalityType)
    {
        super(owner, modalityType);
        initDialog();
    }

    public NomadMidiDialog(Window owner, String title)
    {
        super(owner, title);
        initDialog();
    }

    public NomadMidiDialog(Frame owner, String title, boolean modal)
    {
        super(owner, title, modal);
        initDialog();
    }

    public NomadMidiDialog(Dialog owner, String title, boolean modal)
    {
        super(owner, title, modal);
        initDialog();
    }

    public NomadMidiDialog(Window owner, String title, ModalityType modalityType)
    {
        super(owner, title, modalityType);
        initDialog();
    }

    public NomadMidiDialog(Frame owner, String title, boolean modal,
            GraphicsConfiguration gc)
    {
        super(owner, title, modal, gc);
        initDialog();
    }

    public NomadMidiDialog(Dialog owner, String title, boolean modal,
            GraphicsConfiguration gc)
    {
        super(owner, title, modal, gc);
        initDialog();
    }

    public NomadMidiDialog(Window owner, String title, ModalityType modalityType,
            GraphicsConfiguration gc)
    {
        super(owner, title, modalityType, gc);
        initDialog();
    }

    private void initDialog()
    {
        form = new NomadMidiDialogFrmHandler();
        form.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        
        getContentPane().setLayout(new BorderLayout());
        createTitlePane("MIDI", loadIcon("midi-ill.png"));
        getContentPane().add(form, BorderLayout.CENTER);
    }

    private Icon loadIcon(String source)
    {
        URL url = getClass().getResource(source);
        
        if (url == null)
            return null;
        
        Image image = Toolkit.getDefaultToolkit().getImage(url);
        if (image == null)
            return null;
        return new ImageIcon(image);
    }

    public static void main(String[] args)
    {
        NomadMidiDialog.showMidiDialog(null, "Midi");
    }

    public static int showMidiDialog(Window owner, String title)
    {
        NomadMidiDialog dlg = new NomadMidiDialog(owner, title);
        /*
        JOptionPane.showConfirmDialog(parentComponent, message)
        */
        dlg.setVisible(true);
        return 0;
    }
    
}
