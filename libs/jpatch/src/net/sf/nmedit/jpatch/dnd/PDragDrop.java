package net.sf.nmedit.jpatch.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleDescriptor;

public class PDragDrop {
    
    // note: flavors should be final
    
	public static final DataFlavor ModuleDescriptorDataFlavor 
	= new DataFlavor(PModuleDescriptor.class, "module descriptor");

	public static final DataFlavor ModuleDataFlavor 
	= new DataFlavor(PModule.class, "module instance");

	public static final DataFlavor ModuleArrayDataFlavor 
	= new DataFlavor(PModule[].class, "PModule instances");

	public static final DataFlavor ModuleSelectionFlavor 
	= new DataFlavor(PModuleTransferData.class, "Nomad PModuleSelectionFlavor");

	private static final String charset = "ISO-8859-1";
	public static final DataFlavor PatchFileFlavor =
		new DataFlavor("text/patch; charset="+charset+"", "Nord Modular patch 3.0");
	
	public static final DataFlavor PatchStringFlavor = 
		new DataFlavor(java.lang.String.class, "patch string");

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
