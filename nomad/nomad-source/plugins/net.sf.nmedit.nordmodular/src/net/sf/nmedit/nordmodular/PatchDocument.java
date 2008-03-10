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
package net.sf.nmedit.nordmodular;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEditSupport;

import net.sf.nmedit.jpatch.clavia.nordmodular.NMPatch;
import net.sf.nmedit.jsynth.Slot;
import net.sf.nmedit.jtheme.clavia.nordmodular.JTNMPatch;
import net.sf.nmedit.nomad.core.Nomad;
import net.sf.nmedit.nomad.core.swing.document.DefaultDocumentManager;
import net.sf.nmedit.nomad.core.swing.document.Document;

public class PatchDocument implements Document, 
    PropertyChangeListener, Transferable
{
    
    public static Icon pchIcon = new ImageIcon(
            PatchDocument.class.getResource("/icons/patch_file_icon16.png"));
    
    private JTNMPatch jtpatch;
    private NMPatch nmpatch;
    private URI uri;

    public PatchDocument(JTNMPatch patch)
    {
        this.jtpatch = patch;
        this.nmpatch = jtpatch.getPatch();
        if (nmpatch.getName() == null)
        	nmpatch.setName("Untitled");
        nmpatch.addPropertyChangeListener(NMPatch.NAME, this);
        nmpatch.addPropertyChangeListener("slot", this);
        nmpatch.addPropertyChangeListener(NMPatch.MODIFIED, this);
    	nmpatch.installModifiedListener();
    }
    
    public NMPatch getPatch()
    {
        return nmpatch;
    }
    
    public void setURI(File file)
    {
        setURI(file == null ? null : file.toURI());
    }
    
    public void setURI(URI uri)
    {
        this.uri = uri;
    }
    
    public URI getURI()
    {
        return uri;
    }

    public JTNMPatch getComponent()
    {
        return jtpatch;
    }

    public String getTitleExtended() {
        String name = nmpatch.getName();
        Slot slot = nmpatch.getSlot();
        if (slot != null)
        {
            name+=" ("+slot.getName()+")";
        }
        if (isModified())
        	name += " *";
        return name;
    }
    public String getTitle()
    {
    	return nmpatch.getName();
    }

    public Icon getIcon()
    {
        return pchIcon;
    }

    public void dispose()
    {
        if (nmpatch != null)
        {
            nmpatch.removePropertyChangeListener(this);
            nmpatch = null;
        }
        if (jtpatch != null)
            jtpatch.dispose();
    }

    @SuppressWarnings("unchecked")
    public <T> T getFeature(Class<T> featureClass)
    {
        if (UndoManager.class.equals(featureClass))
        {
            return (T)jtpatch.getPatch().getUndoManager();
        }
        else if (UndoableEditSupport.class.equals(featureClass))
        {
            return (T)jtpatch.getPatch().getEditSupport();
        }
        
        return null;
    }

    public File getFile()
    {
        return jtpatch != null ? jtpatch.getPatch().getFile() : null;
    }

    public void propertyChange(PropertyChangeEvent evt)
    {
        if (evt.getPropertyName() == NMPatch.NAME)
        {
            DefaultDocumentManager m = Nomad.sharedInstance().getDocumentManager();
            int index = m.indexOf(this);
            if (index>=0)
                m.setTitleAt(index, getTitleExtended());
        }
        else if ("slot".equals(evt.getPropertyName()))
        {
            DefaultDocumentManager dm = Nomad.sharedInstance().getDocumentManager();
            if (dm.contains(this))
            {
                dm.updateTitle(this);
            }
        } else if (NMPatch.MODIFIED.equals(evt.getPropertyName()))
            {
                DefaultDocumentManager dm = Nomad.sharedInstance().getDocumentManager();
                if (dm.contains(this))
                {
                    dm.updateTitle(this);
                }
            }
    }

    public Object getProperty(String name)
    {
        return "patch".equals(name) ? jtpatch.getPatch() : null;
    }

    // **** transferable
    
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
    {
        return jtpatch.getTransferData(flavor);
    }

    public DataFlavor[] getTransferDataFlavors()
    {
        return jtpatch.getTransferDataFlavors();
    }

    public boolean isDataFlavorSupported(DataFlavor flavor)
    {
        return jtpatch.isDataFlavorSupported(flavor);
    }

    public boolean isModified() {
    	return nmpatch.isModified();
    }
}

