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
package net.sf.nmedit.nomad.core.helpers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sf.nmedit.nomad.core.menulayout.MLEntry;
import net.sf.nmedit.nomad.core.menulayout.MenuLayout;
import net.sf.nmedit.nomad.core.swing.document.Document;
import net.sf.nmedit.nomad.core.swing.document.DocumentEvent;
import net.sf.nmedit.nomad.core.swing.document.DocumentListener;
import net.sf.nmedit.nomad.core.swing.document.DocumentManager;
import net.sf.nmedit.nomad.core.swing.document.HistoryFeature;

public class DocumentActionActivator
    implements DocumentListener, ChangeListener, ActionListener
{

    private Document selected;
    private MenuLayout menu;
    private DocumentManager documents;
    private HistoryFeature history;
    
    private MLEntry undoEntry;
    private MLEntry redoEntry;
    private MLEntry close;
    private MLEntry closeall;
    
    public DocumentActionActivator(DocumentManager documents,
            MenuLayout menu)
    {
        this.documents = documents;
        this.menu = menu;
        undoEntry = menu.getEntry("nomad.menu.edit.history.undo");
        redoEntry = menu.getEntry("nomad.menu.edit.history.redo");
        final String prefix1 = "nomad.menu.file.";
        close = menu.getEntry(prefix1+"close.close");
        closeall = menu.getEntry(prefix1+"close.closeall");
        
        close.addActionListener(this);
        closeall.addActionListener(this);

        undoEntry.addActionListener(this);
        redoEntry.addActionListener(this);
        
        documents.addListener(this);
        setSelected(documents.getSelection());
    }
    
    public void documentAdded(DocumentEvent e)
    {/*
        if (selected == null)
            setSelected(e.getDocument());*/
    }

    public void documentRemoved(DocumentEvent e)
    {/*
        if (selected == e.getDocument())
            setSelected(null);*/
    }

    public void documentSelected(DocumentEvent e)
    {
        setSelected(e.getDocument());
    }
    
    private void setSelected(Document d)
    {
        if (this.selected == d)
            return;
        this.selected = d;

        if (history != null)
        {
            history.removeChangeListener(this);
            history = null;
        }

        if (d != null)
        {
            history = d.getFeature(HistoryFeature.class);

            if (history != null)
            {
                history.addChangeListener(this);
            }
        }

        updateMenu();
    }

    private void updateMenu()
    {
        boolean enabled = selected != null;

        update(close, enabled);
        update(closeall, enabled);
        
        updateHistoryMenu();
    }

    private void update(MLEntry entry, boolean enabled)
    {
        if (entry != null) 
            entry.setEnabled(enabled);
    }

    public void stateChanged(ChangeEvent e)
    {
        updateHistoryMenu();
    }

    private void updateHistoryMenu()
    {
        if (history == null)
        {
            undoEntry.setEnabled(false);
            redoEntry.setEnabled(false);
        }
        else
        {
            undoEntry.setEnabled(history.canUndo());
            redoEntry.setEnabled(history.canRedo());
        }
    }
    
    public void actionPerformed(ActionEvent e)
    {
        Object src = e.getSource();
        if (src instanceof JMenuItem)
        {
            JMenuItem item = (JMenuItem) src;
            if (close.isInstalled(item))
            {
                if (close.isEnabled())
                    close();
            }
            else if (closeall.isInstalled(item))
            {
                if (closeall.isEnabled())
                    closeAll();
            }
            else if (undoEntry.isInstalled(item))
            {
                if (undoEntry.isEnabled() && history != null)
                    history.undo();
            }
            else if (redoEntry.isInstalled(item))
            {
                if (redoEntry.isEnabled() && history != null)
                    history.redo();
            }
        }
    }
    
    private void close()
    {
        Document d = documents.getSelection();
        if (d != null)
        {
            documents.remove(d);
            d.dispose();
        }
    }
    
    private void closeAll()
    {
        for (Document d: documents.getDocuments())
        {
            documents.remove(d);
            d.dispose();
        }
    }

}
