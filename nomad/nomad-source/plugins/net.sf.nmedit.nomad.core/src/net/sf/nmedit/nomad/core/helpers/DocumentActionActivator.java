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

import net.sf.nmedit.nomad.core.menulayout.MLEntry;
import net.sf.nmedit.nomad.core.menulayout.MenuLayout;
import net.sf.nmedit.nomad.core.swing.document.Document;
import net.sf.nmedit.nomad.core.swing.document.DocumentEvent;
import net.sf.nmedit.nomad.core.swing.document.DocumentListener;
import net.sf.nmedit.nomad.core.swing.document.DocumentManager;

public class DocumentActionActivator
    implements DocumentListener
{

    private Document selected;
    private MenuLayout menu;
    private DocumentManager documents;
    
    public DocumentActionActivator(DocumentManager documents,
            MenuLayout menu)
    {
        this.documents = documents;
        this.menu = menu;
        
        documents.addListener(this);
        setSelected(documents.getSelection());
    }
    
    public void documentAdded(DocumentEvent e)
    {
        if (selected == null)
            setSelected(e.getDocument());
    }

    public void documentRemoved(DocumentEvent e)
    {
        if (selected == e.getDocument())
            setSelected(null);
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
        
        updateMenu();
    }

    private void updateMenu()
    {
        final String prefix1 = "nomad.menu.file.";

        MLEntry close = menu.getEntry(prefix1+"close.close");
        MLEntry closeall = menu.getEntry(prefix1+"close.closeall");
        
        boolean enabled = selected != null;

        update(close, enabled);
        update(closeall, enabled);
    }

    private void update(MLEntry entry, boolean enabled)
    {
        if (entry != null) 
            entry.setEnabled(enabled);
    }

}
