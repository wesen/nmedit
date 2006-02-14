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
 * Created on Feb 12, 2006
 */
package org.nomad.patch.format;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PatchPreview extends JPanel implements PropertyChangeListener {

    final static String preFileName		= "<b>File:</b>";
    final static String preVersionName 	= "<b>Version:</b>";
    final static String preNotes  		= "<b>Notes:</b>";
    File file = null;
    JLabel lblFileName		= lbl();
    JLabel lblVersionName 	= lbl();
    JLabel lblNotes  		= lbl();

    private JLabel lbl() {
    	JLabel label = new JLabel();
    	label.setFont(new Font("SansSerif", Font.PLAIN, 11));
    	return label;
    }
    
    public PatchPreview(JFileChooser fc) {
        setPreferredSize(new Dimension(160, 50));
        fc.addPropertyChangeListener(this);
        
        setBorder(BorderFactory.createEtchedBorder());
        
        JPanel top = new JPanel();
        top.setLayout(new GridLayout(0, 1));
        top.add(lblFileName);
        top.add(lblVersionName);
        setLayout(new BorderLayout());
        add(top, BorderLayout.NORTH);
        add(lblNotes, BorderLayout.CENTER);
        clearData();
    }

    void clearData() {
    	html(lblFileName, preFileName+"-");
    	html(lblVersionName, preVersionName+"-");
    	html(lblNotes, preNotes+"-");
    }
    
    void html(JLabel lbl, String text) {
    	lbl.setText("<html>"+text+"</html>");
    }
    
    public void loadData() {
    	clearData();
    	
        if (file == null) return;
        
        html(lblFileName, preFileName+"<br>"+file.getName());
        
        PatchFileCallback303.Adapter info = PatchFile303.info(file);
        if (info!=null)
        {
        	String v = "";
        	
        	if (info.getVersionName()!=null)
        		v += "<br>"+ info.getVersionName()+" ";
        	if (info.getMinorVersion()>=0&&info.getMajorVersion()>=0)
        		v += "<br>"+ info.getMajorVersion()+"."+info.getMinorVersion();

        	html(lblVersionName, preVersionName+(v.length()==0 ? "-" : v));
        	
        	html(lblNotes, notes(info));
        }
    }
    
    private String notes(PatchFileCallback303.Adapter info) {
    	String notes = info.getNotes();
    	if (notes != null && (notes = notes.trim()).length()>0) {
    		notes = notes.replaceAll("\\s\\s+", "...");
    		notes = notes.replaceAll("\\n"," ");
    		notes = notes.substring(0, Math.min(100, notes.length()-1));
    		return preNotes+"<br>"+notes;	
    	} else {
    		return preNotes+"-";
    	}
    }

    public void propertyChange(PropertyChangeEvent e) {
        boolean update = false;
        String prop = e.getPropertyName();

        //If the directory changed, don't show an image.
        if (JFileChooser.DIRECTORY_CHANGED_PROPERTY.equals(prop)) {
            file = null;
            update = true;

        //If a file became selected, find out which one.
        } else if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(prop)) {
            file = (File) e.getNewValue();
            update = true;
        }

        //Update the preview accordingly.
        if (update) {
            if (isShowing()) {
                loadData();
                repaint();
            }
        }
    }

}
