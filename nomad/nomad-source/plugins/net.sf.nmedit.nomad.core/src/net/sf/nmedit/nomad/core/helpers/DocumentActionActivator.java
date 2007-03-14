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
import net.sf.nmedit.nomad.core.util.document.Document;
import net.sf.nmedit.nomad.core.util.document.DocumentListener;
import net.sf.nmedit.nomad.core.util.document.DocumentManager;

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
    
    public void documentAdded(Document document)
    {
        if (selected == null)
            setSelected(document);
    }

    public void documentRemoved(Document document)
    {
        if (selected == document)
            setSelected(null);
    }

    public void documentSelected(Document document)
    {
        setSelected(document);
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
