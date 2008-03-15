package net.sf.nmedit.nmutils.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.BufferedReader;
import java.io.File;
import java.io.Reader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.nmedit.nmutils.Platform;

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
					
					if (Platform.isFlavor(Platform.OS.MacOSFlavor)) {
						if (uri.getAuthority() == null && uri.getScheme().equals("file")) {
							uri = new URI(uri.getScheme(), uri.getPath(), uri.getFragment());
						}
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
		    Log log = LogFactory.getLog(FileDnd.class);
		    if (log.isTraceEnabled())
		    {
		        log.trace("getTransferableFiles(DataFlavor "+flavor+", Transferable "
		                +transferable+") failed", e);
		    }
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
	public static boolean isPatchStringFlavor(DataFlavor f) {
		if (f.isFlavorTextType() && (f.getHumanPresentableName().contains("patch") ||
				f.getHumanPresentableName().contains("Patch"))) {
			return true;
		}
		if (f.getRepresentationClass().equals(java.lang.String.class) &&
				f.getHumanPresentableName().equals("patch string"))
			return true;
		return false;
	}

	public static boolean isPatchStringFlavor(DataFlavor[] transferDataFlavors) {
		for (DataFlavor f : transferDataFlavors) {
			if (isPatchStringFlavor(f))
				return true;
		}
		
		return false;
	}
	
	public static String getPatchString(Transferable t) {
		DataFlavor f = getPatchStringFlavor(t.getTransferDataFlavors());
		if (f == null)
			return null;
		Reader r;
		try {
			r = f.getReaderForText(t);
	        StringBuffer buf = new StringBuffer();
	        int c;
	        while ((c = r.read()) != -1) {
	            buf.append((char)c);
	        }
	        r.close();
	        
	        return buf.toString();
		} catch (Exception e) {
            Log log = LogFactory.getLog(FileDnd.class);
            if (log.isTraceEnabled())
            {
                log.trace("getPatchString(Transferable "+t+") failed", e);
            }
		}
		return null;
	}

	public static DataFlavor getPatchStringFlavor(DataFlavor[] flavors) {
		for (DataFlavor f: flavors) {
			if (isPatchStringFlavor(f))
				return f;
		}
		return null;
	}


}
