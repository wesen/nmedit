package net.sf.nmedit.nmutils.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.BufferedReader;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class FileDnd {
	public static List<File> getTransferableFiles(DataFlavor flavor,
			Transferable transferable) {

		flavor = getFileFlavor(transferable.getTransferDataFlavors());
		List<File> files = new ArrayList<File>();

		if (flavor == null)
			return files;
		try
		{
			BufferedReader r = new BufferedReader(flavor.getReaderForText(transferable));
			String line;
			while ((line=r.readLine())!=null)
			{
				if (line.startsWith("file:"))
				{
					URI uri = URI.create(line);
					if (uri.getAuthority() != null && uri.getAuthority().equals("localhost") && 
							uri.getScheme() != null && uri.getScheme().equals("file")) {
						// OSX has file://localhost/bla uris, transform them for java.io.File
						uri = new URI(uri.getScheme(), uri.getPath(), uri.getFragment());
					}

					File file = new File(uri);
					if (file.exists())
					{
						files.add(file);
					} else {
					}
				}
			}
            r.close(); // always close stream
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			return null;
		}

		return files;
	}
	
	public static DataFlavor getFileFlavor(DataFlavor[] flavors) {
		for (DataFlavor f: flavors) {
			if (f.isMimeTypeEqual("text/uri-list") && f.isFlavorTextType())
			{
				return f;
			}
		}
		return null;
	}

	public static boolean testFileFlavor(DataFlavor[] list)
	{
		for (DataFlavor f: list)
			if(f.isMimeTypeEqual("text/uri-list") && f.isFlavorTextType())
				return true;
		return false;
	}

	public static boolean isPatchStringFlavor(DataFlavor[] transferDataFlavors) {
		for (DataFlavor f : transferDataFlavors) {
			if (f.getRepresentationClass().equals(java.lang.String.class) &&
					f.getHumanPresentableName().equals("patch string"))
				return true;
		}
		
		return false;
	}

	public static DataFlavor getPatchStringFlavor(DataFlavor[] flavors) {
		for (DataFlavor f: flavors) {
			if (f.getRepresentationClass().equals(java.lang.String.class) &&
					f.getHumanPresentableName().equals("patch string"))
			{
				return f;
			}
		}
		return null;
	}


}
