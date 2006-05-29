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
package net.sf.nmedit.nomad.patch.misc;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;


public class PatchFilePreviewComponent extends JScrollPane implements PropertyChangeListener {

    File file = null;
    JEditorPane edPane ;

    public PatchFilePreviewComponent(JFileChooser fc) {
    	super(new JEditorPane());
        setPreferredSize(new Dimension(160, 50));
        fc.addPropertyChangeListener(this);
        edPane = (JEditorPane)getViewport().getView();
        
        edPane.setEditable(false);
        edPane.setContentType("text/html");
    }
    
    public void loadData() {
        String text = "";
        if (file != null) {
        	final String P = "<p style=\"margin:2px;\">"; 
        	text+="<html><body style=\"font-family:sans-serif;font-size:9px;padding:2px;margin:0;\">";
        	text+=P+"<b>File:</b> "+file.getName()+"</p>";
            PatchUtils.PatchInfo info = PatchUtils.getPatchInfo(file);
	        if (info!=null)
	        {
	        	String v = ""+info.getVersion();
                
	        	text+=P+"<b>Version:</b> "+(v.length()==0?"-":v)+"</p>";
	        	text+=P+"<b>Notes:</b> ";
	        	text+="";
	        	text+=notes(info);
	        	text+="</p>";
	        }
	        text+="</body></html>";
        }
        edPane.setText(text);
        // edPane.scrollRectToVisible(new Rectangle(0,0,1,1));
    }
    
    private String notes(PatchUtils.PatchInfo info) {
    	String notes = info.getNotes();
    	if (notes != null && (notes = notes.trim()).length()>0) {
    		notes = notes.replaceAll("\\s\\s+", "...");
    		notes = notes.replaceAll("\\n"," ");
    		//notes = notes.substring(0, Math.min(100, notes.length()-1));
    		return "<br>"+notes;	
    	} else {
    		return "-";
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
