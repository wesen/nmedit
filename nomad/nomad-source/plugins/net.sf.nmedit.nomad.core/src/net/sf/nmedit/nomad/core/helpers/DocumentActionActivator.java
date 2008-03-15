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

import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEditSupport;

import net.sf.nmedit.nomad.core.menulayout.MLEntry;
import net.sf.nmedit.nomad.core.menulayout.MenuLayout;
import net.sf.nmedit.nomad.core.swing.document.Document;
import net.sf.nmedit.nomad.core.swing.document.DocumentEvent;
import net.sf.nmedit.nomad.core.swing.document.DocumentListener;
import net.sf.nmedit.nomad.core.swing.document.DocumentManager;

public class DocumentActionActivator
    implements DocumentListener, ActionListener, UndoableEditListener
{

    private Document selected;
    private MenuLayout menu;
    private DocumentManager documents;
    private UndoManager undoManager;
    private UndoableEditSupport ues;
    
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

        UndoManager um = d == null ? null : d.getFeature(UndoManager.class);
        UndoableEditSupport ues = d==null ? null : d.getFeature(UndoableEditSupport.class);
        
        setUndoManager(um, ues);
        updateMenu();
    }

    private void setUndoManager(UndoManager undoManager, UndoableEditSupport ues)
    {
        UndoManager oldManager = this.undoManager;
        UndoManager newManager = undoManager;
        UndoableEditSupport oldues = this.ues;
        UndoableEditSupport newues = ues;
        if (oldManager != newManager || oldues != newues)
        {
            if (oldues != null) oldues.removeUndoableEditListener(this);
            
            this.undoManager = newManager;
            this.ues = newues;

            if (newues != null) newues.addUndoableEditListener(this);
            
            updateHistoryMenu();
        }
    }

    private void updateMenu()
    {
        boolean enabled = selected != null;

        update(close, enabled);
        update(closeall, enabled);
    }

    private void update(MLEntry entry, boolean enabled)
    {
        if (entry != null) 
            entry.setEnabled(enabled);
    }

    private void updateHistoryMenu()
    {
        if (undoManager == null)
        {
            undoEntry.setEnabled(false);
            redoEntry.setEnabled(false);
        }
        else
        {
            undoEntry.setEnabled(undoManager.canUndo());
            redoEntry.setEnabled(undoManager.canRedo());
            undoEntry.putValue(Action.NAME, undoManager.getUndoPresentationName());
            redoEntry.putValue(Action.NAME, undoManager.getRedoPresentationName());
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
                if (undoEntry.isEnabled() && undoManager != null && undoManager.canUndo())
                { 
                    undoManager.undo();
                    updateHistoryMenu();
                }
            }
            else if (redoEntry.isInstalled(item))
            {
                if (redoEntry.isEnabled() && undoManager != null && undoManager.canRedo())
                {
                    undoManager.redo();
                    updateHistoryMenu();
                }
            }
        }
    }
    
    private boolean close()
    {
        Document d = documents.getSelection();
        return documents.closeDocument(d);
    }

    private boolean closeAll()
    {
        for (Document d: documents.getDocuments())
        {
        	if (!documents.closeDocument(d))
        		return false;
        }
        return true;
    }

    public void undoableEditHappened(UndoableEditEvent e)
    {
        updateHistoryMenu();
    }

}
