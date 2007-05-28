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
package net.sf.nmedit.jtheme.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleDescriptor;

public final class JTDragDrop
{
    
    private JTDragDrop()
    {
        // no instances possible
    }

    public static final DataFlavor ModuleDescriptorDataFlavor 
        = new DataFlavor(PModuleDescriptor.class, "module descriptor");

    public static final DataFlavor ModuleDataFlavor 
        = new DataFlavor(PModule.class, "module instance");

    public static final DataFlavor ModuleArrayDataFlavor 
        = new DataFlavor(PModule[].class, "module instances");
    
    public static boolean isModuleDescriptorFlavorSupported(Transferable t)
    {
        return t.isDataFlavorSupported(ModuleDescriptorDataFlavor);
    }
    
    public static Transferable createTransferable(PModuleDescriptor descriptor)
    {
        return new ModuleDescriptorTransferable(descriptor);
    }

    public static PModuleDescriptor getModuleDescriptor(Transferable t)
    {
        if (isModuleDescriptorFlavorSupported(t))
        {
            try
            {
                return (PModuleDescriptor) t.getTransferData(ModuleDescriptorDataFlavor);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static class ModuleDescriptorTransferable implements Transferable
    {
        
        private PModuleDescriptor descriptor;

        public ModuleDescriptorTransferable(PModuleDescriptor descriptor)
        {
            this.descriptor = descriptor;
        }

        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
        {
            if (!isDataFlavorSupported(flavor))
                throw new UnsupportedFlavorException(flavor);
            return descriptor;
        }

        public DataFlavor[] getTransferDataFlavors()
        {
            return new DataFlavor[] {ModuleDescriptorDataFlavor};
        }

        public boolean isDataFlavorSupported(DataFlavor flavor)
        {
            return ModuleDescriptorDataFlavor.equals(flavor);
        }
        
    }
    
}
