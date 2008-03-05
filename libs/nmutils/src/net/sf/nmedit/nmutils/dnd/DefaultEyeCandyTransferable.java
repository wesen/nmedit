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

import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;


public class DefaultEyeCandyTransferable extends WrappedTransferable implements EyeCandyTransferable
{
    
    private Image transferImage;

    public static Image getTransferImage(Transferable t)
    {
        if (t.isDataFlavorSupported(transferImageFlavor))
        {
            try
            {
                return (Image) t.getTransferData(transferImageFlavor);
            }
            catch (UnsupportedFlavorException e)
            {
                // ignore
            }
            catch (ClassCastException e)
            {
                // ignore
            }
            catch (IOException e)
            {
                // ignore
            }
        }
        return null;
    }
    
    public DefaultEyeCandyTransferable(Transferable t)
    {
        super(t);
    }

    public DefaultEyeCandyTransferable(Transferable t, Image transferImage)
    {
        super(t);
        this.transferImage = transferImage;
    }

    public Image getTransferImage()
    {
        return transferImage;
    }
    
    public void setTransferImage(Image transferImage)
    {
        this.transferImage = transferImage;
    }

    public Object getTransferData(DataFlavor flavor)
            throws UnsupportedFlavorException, IOException
    {
        if (transferImageFlavor.equals(flavor) && transferImage != null)
        {
            return transferImage;
        }
        return super.getTransferData(flavor);
    }

    public DataFlavor[] getTransferDataFlavors()
    {
        DataFlavor[] flavors = super.getTransferDataFlavors();
        DataFlavor[] eyecandyFlavors;
        if (transferImage == null)
        {
            eyecandyFlavors = flavors;
        }
        else
        {
            eyecandyFlavors = new DataFlavor[flavors.length+1];
            System.arraycopy(flavors, 0, eyecandyFlavors, 0, flavors.length);
            eyecandyFlavors[eyecandyFlavors.length-1] = transferImageFlavor;
        }
        return eyecandyFlavors;
    }

    public boolean isDataFlavorSupported(DataFlavor flavor)
    {
        if (transferImageFlavor.equals(flavor) && transferImage != null)
            return true;
        else
            return super.isDataFlavorSupported(flavor);
    }

}
