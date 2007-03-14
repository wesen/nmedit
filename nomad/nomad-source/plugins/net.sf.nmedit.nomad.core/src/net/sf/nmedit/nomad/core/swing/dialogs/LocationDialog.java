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
 * Created on Nov 1, 2006
 */
package net.sf.nmedit.nomad.core.swing.dialogs;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileFilter;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.sf.nmedit.nomad.core.swing.ExtensionFilter;
import net.sf.nmedit.nomad.core.swing.dialog.AbstractWizard;

public class LocationDialog extends AbstractWizard
{
    
    private JFileChooser fileChooser;
    private JComponent filterPane = null;
    private JTextField tf = null;

    private final int ST_FILECHOOSER = 0;
    private final int ST_FILEFILTER= 1;
    private int state = ST_FILECHOOSER;
    
    public LocationDialog()
    {
        fileChooser = new JFileChooser();
        fileChooser.setFileHidingEnabled(true);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setControlButtonsAreShown(false);
        fileChooser.addPropertyChangeListener(new PropertyChangeListener(){
            public void propertyChange( PropertyChangeEvent evt )
            {
                if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(evt.getPropertyName()))
                {
                    fireChangeEvent();
                }
            }});
    }

    public JComponent createCurrentUI()
    {
        return state == ST_FILECHOOSER ? fileChooser : (filterPane!=null?filterPane :
        (filterPane=createFileFilterConfiguration()));
    }

    public String getCurrentDescription()
    {
        return state == ST_FILECHOOSER ? "Location" : "File Filter";
    }

    public void next()
    {
        state ++;
    }

    public void back()
    {
        state --;
    }

    public boolean canGoBack()
    {
        return state>ST_FILECHOOSER;
    }

    public boolean canGoNext()
    {
        return state<ST_FILEFILTER;
    }

    public boolean canFinish()
    {
        return fileChooser.getSelectedFile()!=null;
    }

    public File getLocation()
    {
        return fileChooser.getSelectedFile();
    }

    public void setLocation(File location)
    {
        fileChooser.setSelectedFile(location);
    }
    
    public FileFilter getFileFilter()
    {
        if (tf == null)
            return null;
        String text = tf.getText().trim();
        if (text.startsWith("*.") && text.length()>2)
        {
            text = text.substring(2);
        }
        else
            if (text.startsWith(".") && text.length()>1)
            {
                text = text.substring(1);
            }
            else return null;
        if (!text.equals("*"))
           return new ExtensionFilter(text, true);
        
        return null;
    }
    
    private JComponent createFileFilterConfiguration()
    {
        JComponent panel = new JPanel();
        panel.setLayout(new BorderLayout(4, 4));
        panel.add(new JLabel("File Filter: "), BorderLayout.WEST);
        tf  = new JTextField("*.*");
        
        panel.add(tf, BorderLayout.CENTER);
        return panel;
    }
    
}
