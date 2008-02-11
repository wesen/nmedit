/* Copyright (C) 2007 Christian Schneider
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
package net.sf.nmedit.nomad.core.swing;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class URIListDropHandler implements DropTargetListener
{

    public static final String MIME_TEXT_URI_LIST = "text/uri-list";
    public static final String MIME_TEXT_PLAIN = "text/plain";
    
    public boolean isDFSupported(DataFlavor flavor)
    {
        return String.class.equals(flavor.getRepresentationClass())
        && (
                flavor.isMimeTypeEqual(MIME_TEXT_URI_LIST)
                || flavor.isMimeTypeEqual(MIME_TEXT_PLAIN)
        );
    }
    
    public DataFlavor getSupportedFlavor(DataFlavor[] list)
    {
        DataFlavor plainFlavor = null;
        for (DataFlavor flavor: list)
        {   
            // prefer URI List
            if (flavor.isMimeTypeEqual(MIME_TEXT_URI_LIST))
                return flavor;
            else if (flavor.isMimeTypeEqual(MIME_TEXT_PLAIN))
                plainFlavor = flavor;
        }
     
        return plainFlavor;
    }
    
    public void checkEvent(DropTargetDragEvent dtde)
    {
        if (getSupportedFlavor(dtde.getCurrentDataFlavors())!=null)
            dtde.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
        else
            dtde.rejectDrag();
    }
    
    public void dragEnter(DropTargetDragEvent dtde)
    {
        checkEvent(dtde);
    }

    public void dropActionChanged(DropTargetDragEvent dtde)
    {
        checkEvent(dtde);
    }
    
    public void dragOver(DropTargetDragEvent dtde)
    {
        // no op
    }

    public void dragExit(DropTargetEvent dte)
    {
        // no op
    }

    public void drop(DropTargetDropEvent dtde)
    {
        DataFlavor flavor = getSupportedFlavor(dtde.getCurrentDataFlavors());
        if (flavor == null)
        {
            dtde.rejectDrop();
            return;
        }
        dtde.acceptDrop(dtde.getDropAction());
        List<URI> uris;
        try
        {
            Reader reader = flavor.getReaderForText(dtde.getTransferable());

            BufferedReader lineReader = new BufferedReader(reader);
            uris = new ArrayList<URI>();
            String line;
            while ((line=lineReader.readLine())!=null)
            {
                try
                {
                    uris.add(new URI(line));
                }
                catch (URISyntaxException e)
                {
                    // not a URI => unknown data => abort
                    dtde.dropComplete(false);
                    return;
                }   
            }
        }
        catch (UnsupportedFlavorException e1)
        {
            throw new RuntimeException(e1);
        }
        catch (IOException e1)
        {
            throw new RuntimeException(e1);
        }
        if (uris.size()>0)
            uriListDropped(uris.toArray(new URI[uris.size()]));
        
        dtde.dropComplete(true);
    }

    public void uriListDropped(URI[] uriList)
    {
        // no op
    }
    
}