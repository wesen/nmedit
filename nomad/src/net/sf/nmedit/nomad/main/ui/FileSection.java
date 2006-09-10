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
 * Created on Apr 29, 2006
 */
package net.sf.nmedit.nomad.main.ui;

import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;

import net.sf.nmedit.nomad.main.Nomad;
import net.sf.nmedit.nomad.main.NomadActionControl;
import net.sf.nmedit.nomad.util.document.Document;
import net.sf.nmedit.nomad.util.document.DocumentListener;

public class FileSection extends HeaderSection implements DocumentListener
{

    public FileSection( Nomad nomad, NomadActionControl nc, String title )
    {
        super( title );
        nomad.getDocumentContainer().addListener(this);
        JComponent pane = getContentPane();
        pane.setLayout(new BoxLayout(pane, BoxLayout.X_AXIS));//new GridLayout(1, 0));
        pane.add(createButton(nc.fileNewAction));
        pane.add(createButton(nc.fileOpenAction));
        pane.add(createButton(nc.fileSaveAction));
        
        validate();
    }
    
    private JButton createButton(Action a)
    {
        JButton btn = new JButton(a);
        btn.setText(null);
        return btn;
    }

    public void documentSelected( Document document )
    {
        // TODO Auto-generated method stub
        
    }

    public void documentRemoved( Document document )
    {
        // TODO Auto-generated method stub
        
    }

    public void documentAdded( Document document )
    {
        // TODO Auto-generated method stub
        
    } 

}
