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
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Patch;
import net.sf.nmedit.nomad.main.Nomad;
import net.sf.nmedit.nomad.main.action.ShowNoteDialogAction;
import net.sf.nmedit.nomad.patch.ui.PatchDocument;
import net.sf.nmedit.nomad.util.document.Document;
import net.sf.nmedit.nomad.util.document.DocumentListener;
import net.sf.nmedit.nomad.util.document.DocumentManager;

public class PatchSection extends HeaderSection implements DocumentListener
{

    private JTextField
        pName;

    private JSpinner
        pVoices;
    
    private SpinnerNumberModel voices;
    
    private DocumentManager documentManager = null;
    
    private Patch patch = null;
    private Document document = null;
    private Nomad nomad;
    
    public PatchSection( Nomad nomad, String title, ShowNoteDialogAction action )
    {
        super( title );
        this.nomad = nomad;
        JComponent pane = getContentPane();
        pane.setLayout(new BoxLayout(pane, BoxLayout.X_AXIS));//new GridLayout(1, 0, 2, 2));

        pName = new JTextField(new LimitedText(16), "Name", 16);
        pName.setMinimumSize(new Dimension(80, 20));
        pName.addKeyListener(new KeyAdapter(){
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
                    String name = patch.getName();
                    if (name == null) name = "";
                    pName.setBackground
                    (
                            name.equals(pName.getText()) ?
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
                    PatchSection.this.nomad.updateTitle(patch);
                }
            }});
        
        pVoices = new JSpinner(); 
        voices = new SpinnerNumberModel(1, 1, 32, 1);
        pVoices.setModel(voices);
        pVoices.setValue(voices.getMinimum());
        
        voices.addChangeListener(new ChangeListener() {
            public void stateChanged( ChangeEvent e )
            {
                if (patch!=null)
                {
                    patch.getHeader().setRequestedVoices((Integer)voices.getNumber());
                }
            }
            
        });

        pane.add(new JLabel("Name:"));
        pane.add(pName);
        
        pane.add(new JLabel("Voices:"));
        pane.add(pVoices);
        
        pane.add(new JButton(action));
        
        updateValues();
    } 
    
    private class LimitedText extends DefaultStyledDocument {
        
        private int maxCharacters;
     
        public LimitedText(int maxCharacters) 
        {
            this.maxCharacters = maxCharacters;
        }
     
        public void insertString(int offs, String str, AttributeSet a) 
            throws BadLocationException {
     
            //This rejects the entire insertion if it would make
            //the contents too long. Another option would be
            //to truncate the inserted string so the contents
            //would be exactly maxCharacters in length.
            if ((getLength() + str.length()) <= maxCharacters)
                super.insertString(offs, str, a);
            else
                Toolkit.getDefaultToolkit().beep();
        }
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
        PatchDocument doc = (PatchDocument) document;
        if (doc==null)
        {
            disableView();
            return;
        }
        patch = doc.getPatch();
            
        pVoices.setEnabled(true);
        voices.setValue(patch.getHeader().getRequestedVoices());
        pName.setEnabled(true);
        String n = patch.getName();
        pName.setText(n == null ? "" : n);
    }
    
    private void disableView()
    {
        pVoices.setEnabled(false);
        voices.setValue(32);
        pName.setEnabled(false);
        pName.setText("");
    }

    public void updateView()
    {
        updateValues();
    }
    
}
