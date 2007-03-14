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
 * Created on May 13, 2006
 */
package net.sf.nmedit.nomad.core.util.document;

import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class JTabbedPaneDocumentManager extends JTabbedPane implements DocumentManager
{

    private List<DocumentListener> listenerList = new ArrayList<DocumentListener>();
    private List<Document> documentList = new ArrayList<Document>();
    final JTabbedPane container = this;
    
    public JTabbedPaneDocumentManager()
    {
        container.setOpaque(true);
        container.addChangeListener(new TabChangeListener());
        container.setBorder(null);
    }
    
    protected void fireevent(DocumentEvent e)
    {
        // TODO better implementation
        switch (e.getID())
        {
            case DocumentEvent.DOCUMENT_ADDED:
                for (int i=listenerList.size()-1;i>=0;i--)
                    listenerList.get(i).documentAdded(e.getDocument());
                break;
            case DocumentEvent.DOCUMENT_REMOVED:
                for (int i=listenerList.size()-1;i>=0;i--)
                    listenerList.get(i).documentRemoved(e.getDocument());
                break;
            case DocumentEvent.DOCUMENT_SELECTED:
                for (int i=listenerList.size()-1;i>=0;i--)
                    listenerList.get(i).documentSelected(e.getDocument());
                break;
        }
    }
    
    public void addListener(DocumentListener l)
    {
        if (!listenerList.contains(l))
            listenerList.add(l);
    }
    
    public void removeListener(DocumentListener l)
    {
        listenerList.remove(l);
    }
    
    public Container getContainer()
    {
        return this;
    }

    public boolean add( Document d )
    {
        if (documentList.add(d))
        {
            addToContainer(d);
            return true;
        }
        return false;
    }

    public boolean remove( Document d )
    {
        if (documentList.remove(d))
        {
            removeFromContainer(d);
            return true;
        }
        return false;
    }
    
    private void addToContainer(Document d)
    {
        container.addTab(d.getTitle(), d.getComponent());
        fireevent(new DocumentEvent(DocumentEvent.DOCUMENT_ADDED, d));
    }
    
    private void removeFromContainer(Document d)
    {
        container.remove(d.getComponent());
        fireevent(new DocumentEvent(DocumentEvent.DOCUMENT_REMOVED, d));
    }
;
    public int getDocumentCount()
    {
        return documentList.size();
    }

    public Document[] getDocuments()
    {
        return documentList.toArray(new Document[documentList.size()]);
    }

    public boolean contains( Document d )
    {
        return documentList.contains(d);
    }

    public Document getSelection()
    {
        Component sel = container.getSelectedComponent(); 
        for (Document d : documentList)
            if (d.getComponent()==sel)
                return d;
        return null ;
    }

    public void setSelection( Document d )
    {
        container.setSelectedComponent(d.getComponent());
    }

    private class TabChangeListener implements ChangeListener
    {
        public void stateChanged( ChangeEvent e )
        {
            fireevent(new DocumentEvent(DocumentEvent.DOCUMENT_SELECTED, getSelection()));
        }
    }

    public void updateTitle( Document document )
    {
        for (int i=getTabCount()-1;i>=0;i--)
        {
            if (document.getComponent() == getComponentAt(i))
            {
                setTitleAt( i, document.getTitle() );
                return;
            }
        }
    }

}
