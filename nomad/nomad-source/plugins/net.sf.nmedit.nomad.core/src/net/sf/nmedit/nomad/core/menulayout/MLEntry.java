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
 * Created on Nov 19, 2006
 */
package net.sf.nmedit.nomad.core.menulayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.event.EventListenerList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.nmedit.nmutils.iterator.BFSIterator;

/**
 * A node in the menu layout. 
 * 
 * @author Christian Schneider
 */
public class MLEntry extends AbstractAction
{

    /**
     * 
     */
    private static final long serialVersionUID = -3695182855344600135L;

    // the enabled icon source uri
    private String enabledIconSrc = null;

    // the disabled icon source uri
    private String disabledIconSrc = null;
    
    // the enabled icon at the specified source uri
    private ImageIcon enabledIcon = null;
    
    // the disabled icon at the specified source uri
    private ImageIcon disabledIcon = null;
    
    // the local identifier of this menu layout entry
    private String localEntryPoint;

    // the global identifier of this menu layout entry
    private String globalEntryPoint = null;
    
    // a list of child entries
    private List<MLEntry> entryList = null;
    
    // the parent entry
    private MLEntry parent = null;
    
    // the flat option - if this is flat==true and this entry
    // also has children, then it is not treated as a sub menu owner
    // but as a list of menu entries surrounded by menu separators.
    private boolean flat = false;
    
    // event listeners
    private EventListenerList listenerList = null;

    // additional key to identify the disabled icon
    public final static String SMALL_DISABLED_ICON = "SmallIcon$disabled";

    /**
     * Creates a new menu layout entry with the specified (local) entry point.
     * @param localEntryPoint the local entry point
     */
    public MLEntry(String localEntryPoint)
    {
        this.localEntryPoint = localEntryPoint;
    }
    
    /**
     * Returns the parent entry
     * @return the parent entry
     */
    public MLEntry getParent()
    {
        return parent;
    }
    
    public boolean isInstalled(JMenuItem item)
    {
        return item.getAction() == this;
    }
    
    /**
     * Returns the global entryPoint
     * @return
     */
    public String getGlobalEntryPoint()
    {
        if (globalEntryPoint == null)
        {
            if (parent == null)
            {
                globalEntryPoint = (localEntryPoint == null ? "" : localEntryPoint);
            }
            else
            {
                globalEntryPoint = parent.getGlobalEntryPoint()+"."+localEntryPoint;    
            }
        }
        return globalEntryPoint;
    }
    
    public String getLocalEntryPoint()
    {
        return localEntryPoint;
    }
    
    public void setEnabledIconSrc(String src)
    {
        if (this.enabledIconSrc != src)
        {
            this.enabledIconSrc = src;
            
            enabledIcon = null;
            putValue(SMALL_ICON, getEnabledIcon());
        }
    }
    
    public void setDisabledIconSrc(String src)
    {
        if (this.disabledIconSrc != src)
        {
            this.disabledIconSrc = src;
            
            disabledIcon = null;
            putValue(SMALL_DISABLED_ICON, getDisabledIcon());
        }
    }
    
    private URL getIconURL(String iconName)
    {
        return getClass().getClassLoader().getResource(iconName);
    }

    private static Log log = LogFactory.getLog(MLEntry.class);
    
    private ImageIcon getImageIcon(String src)
    {
        URL url = getIconURL(src);
        if (url == null)
        {
            if (log.isWarnEnabled())
            {
                log.debug("MLEntry:getImageIcon, could not find '"+src+"'.");
            }
            
            return null;
        }
        return new ImageIcon(url);
    }
    
    public ImageIcon getEnabledIcon()
    {
        if (enabledIcon == null)
        {
            if (enabledIconSrc != null)
                enabledIcon = getImageIcon(enabledIconSrc);
        }
        return enabledIcon;
    }

    public ImageIcon getDisabledIcon()
    {
        if (disabledIcon == null)
        {
            if (disabledIconSrc != null)
                disabledIcon = getImageIcon(disabledIconSrc);
        }
        return disabledIcon;
    }

    public String getEnabledIconSrc()
    {
        return this.enabledIconSrc;
    }
    
    public String getDisabledIconSrc()
    {
        return this.disabledIconSrc;
    }
    
    private void checkAdd(MLEntry entry)
    {
        if (entry.parent != null)
            throw new RuntimeException("cannot reparent entry: "+entry);
        
        if (entryList == null)
            entryList = new ArrayList<MLEntry>(2);
    }
 
    public void add(MLEntry entry)
    {
        checkAdd(entry);
        entryList.add(entry);
        entry.parent = this;
    }
    
    public void add(int index, MLEntry entry)
    {
        checkAdd(entry);
        entryList.add(index, entry);
        entry.parent = this;
    }
    
    public int size()
    {
        return entryList == null ? 0 : entryList.size();
    }

    public MLEntry getEntryAt(int index)
    {
        if (entryList == null)
            throw new IndexOutOfBoundsException("invalid index: "+index);
        return entryList.get(index);
    }

    /*
    public void printKeys(PrintStream out)
    {
        Iterator<MLEntry> i = bfsIterator();
        while (i.hasNext())
        {
            System.out.println(i.next());
        }
    }*/

    public void printXML(PrintStream out)
    {
        printXML(out, "");
    }
    
    private void printXML(PrintStream out, String ident)
    {
        String p = "<entry ";
        if (parent == null)
        {
            out.println("<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>");
            p += "xmlns=\"http://nmedit.sf.net/MenuLayout\"\n  ";
        }
        
        if (flat)
        {
            p+= "flat=\"true\" ";
        }
        
        if ((enabledIconSrc == null) && (disabledIconSrc == null) && (entryList == null))
        {        
            out.println(ident+p+"entryPoint=\""+localEntryPoint+"\" />");   
        }
        else
        {        
            out.println(ident+p+"entryPoint=\""+localEntryPoint+"\">");
    
            if (enabledIconSrc != null)
                out.println(ident+"  <icon src=\""+enabledIconSrc+"\" />");
            if (disabledIconSrc != null)
                out.println(ident+"  <icon src=\""+disabledIconSrc+"\" type=\"disabledIcon\" />");
            
            if (entryList!=null)
            {
                for (int i=0;i<entryList.size();i++)
                    entryList.get(i).printXML(out, ident+"  ");
            }
            
            out.println(ident+"</entry>");
        }
    }
    
    public Iterator<MLEntry> bfsIterator()
    {
        return new BFSIterator<MLEntry>(this)
        {
            @Override
            protected void enqueueChildren( Queue<MLEntry> queue, MLEntry parent )
            {
                List<MLEntry> l = parent.entryList;
                if (l!=null)
                    queue.addAll(l);
            }
            
        };
    }
    
    public String toString()
    {
        return getGlobalEntryPoint();
    }

    /**
     * Sets the flat option.
     * @param flat
     */
    public void setIsFlat( boolean flat )
    {
        this.flat = flat;
    }
    
    /**
     * Returns the flat option.
     * @return
     */
    public boolean isFlat()
    {
        return flat;
    }
    
    public int getListenerCount()
    {
        return listenerList == null ? 0 : listenerList.getListenerCount();
    }
    
    /**
     * Adds the specified ActionListener. 
     * @param l
     */
    public void addActionListener(ActionListener l)
    {
        if (listenerList == null)
            listenerList = new EventListenerList();
        listenerList.add(ActionListener.class, l);
    }
    

    /**
     * Removes the specified ActionListener. 
     * @param l
     */
    public void removeActionListener(ActionListener l)
    {
        if (listenerList != null)
            listenerList.remove(ActionListener.class, l);
    }
    
    /**
     * notifies all listeners
     */
    public void actionPerformed( ActionEvent e )
    {
        if (listenerList != null)
        {
            // Guaranteed to return a non-null array
            Object[] listeners = listenerList.getListenerList();
            // Process the listeners last to first, notifying
            // those that are interested in this event
            for (int i = listeners.length-2; i>=0; i-=2) 
            {
                if (listeners[i]==ActionListener.class)
                {
                    ActionListener al = (ActionListener)listeners[i+1];
                    if (al != this) // for savety
                    {
                        al.actionPerformed(e);
                    }
                }
            }
        }
    }

    public MLEntry cloneTree()
    {
        MLEntry clone = new MLEntry(getGlobalEntryPoint());
        
        cloneTree(clone, this);
        
        return clone;
    }

    private static void cloneTree(MLEntry clone, MLEntry entry)
    {
        cloneSettings(clone, entry);
        
        List<MLEntry> l = entry.entryList;
        
        if (l == null)
            return;
        
        for (int i=0;i<l.size();i++)
        {
            MLEntry child = l.get(i);
            MLEntry childClone = new MLEntry(child.getLocalEntryPoint());
            
            clone.add(childClone);
            cloneTree(childClone, child);
        }
    }

    public static final String SELECTED_KEY = "SwingSelectedKey";
    public static final String DISPLAYED_MNEMONIC_INDEX_KEY = "SwingDisplayedMnemonicIndexKey";
    public static final String LARGE_ICON_KEY = "SwingLargeIconKey";
    
    private static void cloneSettings(MLEntry clone, MLEntry entry)
    {        
        clone.disabledIcon = entry.disabledIcon;
        clone.enabledIcon = entry.enabledIcon;
        clone.disabledIconSrc = entry.enabledIconSrc;
        clone.enabled = entry.enabled;
        clone.enabledIcon = entry.enabledIcon;
        clone.enabledIconSrc = entry.enabledIconSrc;
        clone.flat = entry.flat;
        // auto: clone.globalEntryPoint = entry.globalEntryPoint;
        clone.putValue(NAME, entry.getValue(NAME));
        clone.putValue(SHORT_DESCRIPTION, entry.getValue(SHORT_DESCRIPTION));
        clone.putValue(LONG_DESCRIPTION, entry.getValue(LONG_DESCRIPTION));
        clone.putValue(SMALL_ICON, entry.getValue(SMALL_ICON));
        clone.putValue(ACTION_COMMAND_KEY, entry.getValue(ACTION_COMMAND_KEY));
        clone.putValue(ACCELERATOR_KEY, entry.getValue(ACCELERATOR_KEY));
        clone.putValue(MNEMONIC_KEY, entry.getValue(MNEMONIC_KEY));
        clone.putValue(SELECTED_KEY, entry.getValue(SELECTED_KEY));
        clone.putValue(DISPLAYED_MNEMONIC_INDEX_KEY, entry.getValue(DISPLAYED_MNEMONIC_INDEX_KEY));
        clone.putValue(LARGE_ICON_KEY, entry.getValue(LARGE_ICON_KEY));
    }

}
