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

import java.awt.BorderLayout;
import java.awt.Container;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;

import net.sf.nmedit.nomad.core.swing.JTab;
import net.sf.nmedit.nomad.core.swing.JTabComponent;

public class DefaultDocumentManager extends JPanel implements DocumentManager, PropertyChangeListener
{

    private List<DocumentListener> listenerList = new ArrayList<DocumentListener>();
    //private List<Document> documentList = new ArrayList<Document>();
    final JPanel container = this;
    private JTabComponent tabs;
    private Document currentDoc = null;
    
    public DefaultDocumentManager()
    {
        super(new BorderLayout());
        
        tabs = new JTabComponent();
        tabs.addPropertyChangeListener(this);
        
        container.setOpaque(true);
        //container.addChangeListener(new TabChangeListener());
        container.setBorder(null);
    }
    
    private boolean tabsVisible = false;
    private void componentAddedRemoved()
    {
        if (tabs.getTabCount()>1 && ! tabsVisible)
        {
            add(tabs, BorderLayout.NORTH);
            tabsVisible = true;
            revalidate();
        }
        else if (tabs.getTabCount()<=1 &&  tabsVisible)
        {
         
            remove(tabs);
            tabsVisible=false;
            revalidate();
        }
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
        addToContainer(d);
        componentAddedRemoved();
        return true;
    }

    public boolean remove( Document d )
    {
        removeFromContainer(d);
        componentAddedRemoved();
        return true;
    }
    
    private void addToContainer(Document d)
    {
        tabs.add(new TabDocument(d));
        fireevent(new DocumentEvent(DocumentEvent.DOCUMENT_ADDED, d));
    }
    
    private void removeFromContainer(Document d)
    {
        for (int i=0;i<tabs.getTabCount();i++)
        {
            if (d==((TabDocument)tabs.getTab(i)).document)
            {
                tabs.closeTabAt(i);

                fireevent(new DocumentEvent(DocumentEvent.DOCUMENT_REMOVED, d));
                return;
            }
        }
        
    }
;
    public int getDocumentCount()
    {
        return tabs.getTabCount();
    }

    public Document[] getDocuments()
    {
        
        Document[] d = new Document[tabs.getTabCount()];
        for (int i=0;i<tabs.getTabCount();i++)
        {
            d[i] = ((TabDocument)tabs.getTab(i)).document;
        }
        return d;
    }

    public boolean contains( Document d )
    {
        
        for (int i=tabs.getTabCount()-1;i>=0;i--)
            if (((TabDocument)tabs.getTab(i)).document==d)
                return true;
        return false;
    }

    public Document getSelection()
    {
        TabDocument td = (TabDocument) tabs.getTabSelectedTab();
        return td != null ? td .document : null;
    }

    public void setSelection( Document d )
    {
        for (int i=tabs.getTabCount()-1;i>=0;i--)
        {
            TabDocument td = (TabDocument)tabs.getTab(i);
            if (d == td.document)
            {
                tabs.setSelectedTab(td);
                return;
            }
        }
    }

    public void updateTitle( Document document )
    {
        for (int i=tabs.getTabCount()-1;i>=0;i--)
        {
            TabDocument td = (TabDocument)tabs.getTab(i);
            if (document == td.document)
            {
                td.setTitle(document.getTitle());
                tabs.repaint();
                return;
            }
        }
    }
    
    private static class TabDocument extends JTab
    {
        private Document document;

        public TabDocument(Document d)
        {
            super(null, d.getTitle());
            this.document = d;
        }
    }

    public void propertyChange( PropertyChangeEvent evt )
    {
        if ("selection".equals(evt.getPropertyName()))
        {            
            selectionChanged();
        }
        else if ("tab.removed".equals(evt.getPropertyName()))
        {
            tabRemoved((TabDocument)evt.getOldValue());
        }   
    }
    
    private void tabRemoved( TabDocument document )
    {
        componentAddedRemoved();

        if (document==currentDoc)
            selectionChanged();

        fireevent(new DocumentEvent(DocumentEvent.DOCUMENT_REMOVED, document.document));
    }

    private void selectionChanged()
    {

        
        Document d = getSelection();
        
        if (d!=currentDoc)
        {
             
            if (currentDoc!=null)
                remove(currentDoc.getComponent());
            currentDoc = d;
            if (d!=null)
            {
                JComponent c = d.getComponent();
                add(c, BorderLayout.CENTER);
                revalidate();
                c.repaint();
            }
            else
            {
                repaint();
            }
            
            if (d!=null)
                fireevent(new DocumentEvent(DocumentEvent.DOCUMENT_SELECTED, d));
            
        }
    }

}
