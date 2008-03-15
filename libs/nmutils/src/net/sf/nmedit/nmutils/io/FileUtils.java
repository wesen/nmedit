package net.sf.nmedit.nmutils.io;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileUtils {
	public static boolean copy(File from, File to, String what) {
		return copy(new File(from, what), new File(to, what));
	}

	public static boolean copy(String from, String to) {
		return copy(new File(from), new File(to));
	}

	public static boolean USE_NIO = true;

	public static boolean copy(File from, File to) {
		return FileUtils.copy(from, to, (FileFilter)null);
	}

	public static boolean copy(File from, File to, FileFilter filter) {
		if (from.isDirectory()) {
			for (File f : Arrays.asList(filter == null ? from.listFiles() : from.listFiles(filter))) {
				String name = f.getName();
				if (!copy(from, to, name)){
					return false;
				}
			}
		} else {
			try {
				FileInputStream  is = new FileInputStream(from);
				FileChannel ifc = is.getChannel();
				File parent = to.getParentFile();
				if (parent != null) {
					boolean ok = parent.mkdirs();
                    // TODO handle ok == false
				}
				FileOutputStream os = new FileOutputStream(to);
				if (USE_NIO) {
					FileChannel ofc = os.getChannel();
					ofc.transferFrom(ifc, 0, from.length());
				} else {
					byte[] buf = new byte[1024];
					int len;
					while ((len = is.read(buf)) > 0) {
						os.write(buf, 0, len);
					}
				}
				is.close();
				os.close();
			} catch (IOException ex) {
			    Log log = LogFactory.getLog(FileUtils.class);
			    if (log.isTraceEnabled())
			    {
			        log.trace("copy from "+from+" to "+to+" failed", ex);
			    }
			}
		}
		
		return true;
	}

	public static  void copyFile(File src, File dst) throws IOException {
		copy(src, dst);
	}
	
	static public boolean deleteDirectory(File path) {
        boolean ok = true;
		if( path.exists() ) {
			File[] files = path.listFiles();
			for(int i=0; i<files.length; i++) {
				if(files[i].isDirectory()) {
					ok &= deleteDirectory(files[i]);
				}
				else {
					ok &= files[i].delete();
				}
			}
		}
        ok &= path.delete();
        return ok;
	}
	
	static public String getFileExtension(File file) {
		String name = file.getName();
		int dotIndex = name.lastIndexOf(".");
		if (dotIndex > 0) { 
			return name.substring(dotIndex + 1);
		} else {
			return null;
		}
	}

	static public String getPathnameWithoutExtension(File file) {
		String name = file.getAbsolutePath();
		int dotIndex = name.lastIndexOf(".");
		if (dotIndex > 0) { 
			return name.substring(0, dotIndex - 1);
		} else {
			return name;
		}
	}
	
	static public File getNameWithExtension(File oldFile, File newFile) {
		String oldExtension = getFileExtension(oldFile);
		String newExtension = getFileExtension(newFile);
		
		if (!(oldExtension == null || newExtension != null)) { 
			newFile = new File(getPathnameWithoutExtension(newFile) + "." + oldExtension);
		}
		
		return newFile;
	}
	
	static public File newFileWithPrefix(File parent, String prefix, String suffix) {
		File newFile = null;
		
		for (int i = 0; true; i++) {
			newFile = new File(parent, prefix + " (" + i + ")" + suffix);
			if (!newFile.exists())
				return newFile;
		}
	}
	
	static public boolean isFileParent(File f1, File f2) {
		try {
			String p1 = f1.getCanonicalPath();
			String p2 = f2.getCanonicalPath();
			if ((p2.length() >= p1.length()) && p2.substring(0, p1.length()).equals(p1))
				return true;
			else
				return false;
		} catch (IOException e) {
            Log log = LogFactory.getLog(FileUtils.class);
            if (log.isTraceEnabled())
            {
                log.trace("isFileParent "+f1+", "+f2+" failed", e);
            }
			return false;
		}
	}
}
