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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sf.nmedit.nomad.patch.ui.PatchUI;
import net.sf.nmedit.nomad.patch.virtual.Patch;
import net.sf.nmedit.nomad.util.document.Document;
import net.sf.nmedit.nomad.util.document.DocumentListener;
import net.sf.nmedit.nomad.util.document.DocumentManager;

public class PatchSection extends HeaderSection implements DocumentListener
{

    private JProgressBar
        load;
    
    private JTextField
        pName;

    private JSpinner
        pVoices;
    
    private SpinnerNumberModel voices;
    
    private DocumentManager documentManager = null;
    
    private Patch patch = null;
    private Document document = null;
    
    public PatchSection( String title )
    {
        super( title );
        
        JComponent pane = getContentPane();
        
        pane.setLayout(new GridLayout(0, 2, 2, 2));
        
        load = new JProgressBar();
        load.setMinimum(0);
        load.setMaximum(100);
        load.setValue(40);

        pName = new JTextField("Name");
        pName.setMinimumSize(new Dimension(80, 20));
        pName.addKeyListener(new KeyListener(){
            public void keyTyped( KeyEvent e )
            {
                // TODO Auto-generated method stub
                
            }

            public void keyPressed( KeyEvent e )
            {
                if (e.getKeyCode()==KeyEvent.VK_ESCAPE)
                {
                    if (patch!=null)
                    {
                        pName.setBackground(Color.WHITE);
                        pName.setText(patch.getName());
                    }
                }
            }

            public void keyReleased( KeyEvent e )
            {
                if (patch!=null)
                {
                    pName.setBackground(
                            patch.getName().equals(pName.getText()) ?
                            Color.WHITE:Color.RED
                    );
                }   
            }});
        pName.addActionListener(new ActionListener() {
            public void actionPerformed( ActionEvent e )
            {
                if (patch!=null)
                {
                    patch.setName(pName.getText());
                }
            }});
        
        pVoices = new JSpinner(); 
        voices = new SpinnerNumberModel(32, 1, 32, 1);
        pVoices.setModel(voices);
        
        voices.addChangeListener(new ChangeListener() {
            public void stateChanged( ChangeEvent e )
            {
                if (patch!=null)
                {
                    patch.getHeader().setRequestedVoices((Integer)voices.getNumber());
                }
            }
            
        });
        
        pane.add(new JLabel("PVA:"));        
        pane.add(load);

        pane.add(new JLabel("Name:"));
        pane.add(pName);
        
        pane.add(new JLabel("Voices:"));
        pane.add(pVoices);
        
        updateValues();
    }

    public void setDocumentManager(DocumentManager dm)
    {
        if (this.documentManager!=dm)
        {
            if (this.documentManager!=null)
                this.documentManager.removeListener(this);
            this.documentManager = dm;
            if (this.documentManager!=null)
                this.documentManager.addListener(this);
        }
    }

    public void documentSelected( Document document )
    {
        updateValues();
    }

    public void documentRemoved( Document document )
    { }

    public void documentAdded( Document document )
    { }
    
    private void updateValues()
    {
        patch = null;
        document = null;
        
        if (documentManager==null)
        {
            disableView();
            return;
        }
        
        document = documentManager.getSelection();
        PatchUI pui = (PatchUI) document;
        if (pui==null)
        {
            disableView();
            return;
        }
        patch = pui.getPatch();
            
        pVoices.setEnabled(true);
        voices.setValue(patch.getHeader().getRequestedVoices());
        pName.setEnabled(true);
        pName.setText(patch.getName());
        load.setEnabled(true);
        load.setValue(0); // TODO accurate load
    }
    
    private void disableView()
    {
        pVoices.setEnabled(false);
        voices.setValue(32);
        pName.setEnabled(false);
        pName.setText("-");
        load.setEnabled(false);
        load.setValue(0);
    }
    
}
