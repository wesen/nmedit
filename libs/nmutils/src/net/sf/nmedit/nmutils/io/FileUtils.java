package net.sf.nmedit.nmutils.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.Arrays;

public class FileUtils {
	public static boolean copy(File from, File to, String what) {
		return copy(new File(from, what), new File(to, what));
	}

	public static boolean copy(String from, String to) {
		return copy(new File(from), new File(to));
	}

	public static boolean USE_NIO = true;

	public static boolean copy(File from, File to) {
		if (from.isDirectory()) {
			for (String name : Arrays.asList(from.list())) {
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
					parent.mkdirs();
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
				ex.printStackTrace();
			}
		}
		
		return true;
	}

	public static  void copyFile(File src, File dst) throws IOException {
		copy(src, dst);
	}
}
