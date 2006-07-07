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
 * Created on Jul 5, 2006
 */
package net.sf.nmedit.nomad.main.dialog;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Patch;
import net.sf.nmedit.nomad.main.resources.AppIcons;

public class PatchNoteDialog extends NomadDialog
{

    private Patch patch;
    private JTextArea textArea;

    public PatchNoteDialog(Patch patch)
    {
        this.patch = patch;
        
        setTitle("Note");
        setImage(AppIcons.IC_NOTE_BIG);
        setPackingEnabled(false);
        setScrollbarEnabled(false);
        
        textArea = new JTextArea();
        textArea.setFont(new Font("monospaced", Font.PLAIN, getFont().getSize()));
        String note = patch.getNote();
        if (note==null) note = "";
        textArea.setText( note );
        
        setLayout(new BorderLayout());
        add(new JScrollPane(textArea), BorderLayout.CENTER);
    }

    public void invoke() 
    {
        invoke(new String[]{RESULT_OK, ":"+RESULT_CANCEL});
        
        if (isOkResult())
        {
            apply();
        }
    }

    private void apply()
    {
        String text = textArea.getText();
        patch.setNote(text==null?"":text);
    }
}
