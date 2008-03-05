/* Copyright (C) 2008 Christian Schneider
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
package net.sf.nmedit.nmutils.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class WrappedTransferable implements Transferable
{
    
    protected Transferable t; 
    
    public WrappedTransferable(Transferable t)
    {
        this.t = t;
    }

    public Object getTransferData(DataFlavor flavor)
            throws UnsupportedFlavorException, IOException
    {
        return t.getTransferData(flavor);
    }

    public DataFlavor[] getTransferDataFlavors()
    {
        return t.getTransferDataFlavors();
    }

    public boolean isDataFlavorSupported(DataFlavor flavor)
    {
        return t.isDataFlavorSupported(flavor);
    }

}
