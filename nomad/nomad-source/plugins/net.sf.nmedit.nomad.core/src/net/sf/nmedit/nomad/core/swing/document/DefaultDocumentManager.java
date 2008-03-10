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
package net.sf.nmedit.nomad.core.swing.document;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sf.nmedit.nomad.core.Nomad;
import net.sf.nmedit.nomad.core.swing.tabs.FFTabBarUI;
import net.sf.nmedit.nomad.core.swing.tabs.JTabbedPane2;

public class DefaultDocumentManager extends JTabbedPane2 implements DocumentManager, PropertyChangeListener
{

    /**
     * 
     */
    private static final long serialVersionUID = 8346940412067775776L;
    private List<Document> documentList = new ArrayList<Document>();
    final JTabbedPane2 container = this;
    
    public DefaultDocumentManager()
    {
        container.setOpaque(true);
        container.addTabBarChangeListener(new TabChangeListener());
        container.setBorder(null);
        container.getTabBar().addPropertyChangeListener(this);
    }

    public int indexOf(Document d)
    {
        return container.indexOfComponent(d.getComponent());
    }

    protected void fireDocumentAdded(Document doc)
    {
        if (getDocumentCount()==1)
            setSelection(doc);
        
        DocumentEvent documentEvent = null;
        
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==DocumentListener.class) {
                // Lazily create the event:
                if (documentEvent == null)
                    documentEvent = new DocumentEvent(DocumentEvent.DOCUMENT_ADDED, doc);
                ((DocumentListener)listeners[i+1]).documentAdded(documentEvent);
            }
        }
        
    }

    protected void fireDocumentRemoved(Document doc)
    {
        if (getDocumentCount()==0)
            setSelection(null);
        
        DocumentEvent documentEvent = null;
        
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==DocumentListener.class) {
                // Lazily create the event:
                if (documentEvent == null)
                    documentEvent = new DocumentEvent(DocumentEvent.DOCUMENT_REMOVED, doc);
                ((DocumentListener)listeners[i+1]).documentRemoved(documentEvent);
            }
        }
        
    }

    protected void fireDocumentSelected(Document doc)
    {
        DocumentEvent documentEvent = null;
        
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==DocumentListener.class) {
                // Lazily create the event:
                if (documentEvent == null)
                    documentEvent = new DocumentEvent(DocumentEvent.DOCUMENT_SELECTED, doc);
                ((DocumentListener)listeners[i+1]).documentSelected(documentEvent);
            }
        }
        
    }
    
    public void addListener(DocumentListener l)
    {
        listenerList.add(DocumentListener.class, l);
    }
    
    public void removeListener(DocumentListener l)
    {
        listenerList.remove(DocumentListener.class, l);
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
        Icon icon = d.getIcon();
        if (icon == null)
            icon = FFTabBarUI.defaultIcon;
        
        container.addTab(d.getTitleExtended(), icon, d.getComponent());
        fireDocumentAdded(d);
    }
    
    private void removeFromContainer(Document d)
    {
        container.removeTabAt(container.indexOfComponent(d.getComponent()));
        fireDocumentRemoved(d);
        d.dispose();
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
        container.setSelectedComponent(d==null?null:d.getComponent());
    }

    private class TabChangeListener implements ChangeListener
    {
        public void stateChanged( ChangeEvent e )
        {
            fireDocumentSelected(getSelection());
        }
    }

    public void updateTitle( Document document )
    {
        for (int i=getTabCount()-1;i>=0;i--)
        {
            if (document.getComponent() == getComponentAt(i))
            {
                setTitleAt( i, document.getTitleExtended() );
                return;
            }
        }
    }

    public void propertyChange(PropertyChangeEvent evt)
    {
        
        if ("ask-remove".equals(evt.getPropertyName()))
        {
            int index = (Integer) evt.getNewValue();
            closeDocument(documentList.get(index));
//            remove(documentList.get(index));
            return;
        }
        if ("show-context-menu".equals(evt.getPropertyName()))
        {
            MouseEvent e = (MouseEvent) evt.getOldValue();
            int tabIndex = (Integer) evt.getNewValue();
            
            JPopupMenu popup = new JPopupMenu();
            popup.add(new ContextMenuAction(container, tabIndex, ContextMenuAction.CLOSE));
            popup.show(container.getTabBar(), e.getX(), e.getY());
        }
        
    }
    
    public boolean closeDocument(Document d) {
    	if (d != null) {
            if (d.isModified()) {
            	Nomad n = Nomad.sharedInstance();
            	int result = JOptionPane.showConfirmDialog(n.getWindow().getRootPane(), "Are you sure you want to close " + d.getTitle() + " ?",
            			"", JOptionPane.OK_CANCEL_OPTION);
            	if (result != JOptionPane.OK_OPTION)
            		return false;
            	
            }
            remove(d);
            d.dispose();
    	}
    	return true;
    }
    


    private static class ContextMenuAction extends AbstractAction
    {
        
        /**
         * 
         */
        private static final long serialVersionUID = -5802320514699863654L;

        public static final String CLOSE = "Close Tab";
        
        private JTabbedPane2 tabs;
        private int tabIndex;
        
        public ContextMenuAction(JTabbedPane2 tabs, int tabIndex, String command)
        {
            putValue(NAME, command);
            putValue(ACTION_COMMAND_KEY, command);
            
            this.tabs = tabs;
            this.tabIndex = tabIndex;
        }

        public void actionPerformed(ActionEvent e)
        {
            if (!isEnabled())
                return;
            
            if (e.getActionCommand() == CLOSE)
            {
                tabs.getTabBar().askRemove(tabIndex);
                setEnabled(false);
            }
        }
        
    }
    
}
