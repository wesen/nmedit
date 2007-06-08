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
package net.sf.nmedit.nomad.core.swing;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

public class FileTransferHandler extends TransferHandler
{

    public boolean canImport(JComponent c, DataFlavor[] flavors) 
    {
        for(int i = 0; i < flavors.length; i++)
            if (flavors[i].equals(DataFlavor.javaFileListFlavor))
                return true;
        return false;
    }

    public boolean importData(JComponent c, Transferable t) 
    {
        if (t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) 
        {
            try 
            {
                List<File> filelist = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
                return importFiles(c, filelist);
            }
            catch(UnsupportedFlavorException e) 
            {
                return false;
            }
            catch(IOException e) 
            {
                return false;
            }
        }
            
        return false;
    }

    protected boolean importFiles(JComponent c, List<File> filelist)
    {
        return false;
    }

}
