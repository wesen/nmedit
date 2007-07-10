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

import net.sf.nmedit.jpatch.clavia.nordmodular.NMPatch;
import net.sf.nmedit.jtheme.clavia.nordmodular.JTNMPatch;
import net.sf.nmedit.nomad.core.Nomad;
import net.sf.nmedit.nomad.core.swing.document.DefaultDocumentManager;
import net.sf.nmedit.nomad.core.swing.document.Document;
import net.sf.nmedit.nomad.core.swing.document.HistoryFeature;

public class PatchDocument implements Document, 
    PropertyChangeListener, Transferable
{
    
    public static Icon pchIcon = new ImageIcon(
            PatchDocument.class.getResource("/icons/patch_file_icon16.png"));
    
    private JTNMPatch jtpatch;
    private NMPatch nmpatch;
    private URI uri;
    private HistoryFeature historyFeature;

    public PatchDocument(JTNMPatch patch)
    {
        this.jtpatch = patch;
        this.nmpatch = jtpatch.getPatch();
        nmpatch.addPropertyChangeListener(NMPatch.NAME, this);
    }
    
    public void setURI(File file)
    {
        setURI(file.toURI());
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

    public String getTitle()
    {
        return jtpatch.getPatch().getName();
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

    public <T> T getFeature(Class<T> featureClass)
    {
        if (HistoryFeature.class.equals(featureClass))
        {
            if (historyFeature == null)
                historyFeature = new PatchHistoryFeature(this, jtpatch.getPatch().getHistory());
            
            return featureClass.cast(historyFeature);
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
                m.setTitleAt(index, getTitle());
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

}

