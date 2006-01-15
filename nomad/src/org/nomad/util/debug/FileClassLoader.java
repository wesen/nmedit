package org.nomad.util.debug;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileClassLoader extends ClassLoader {

	public FileClassLoader() {
		super();
	}

	public FileClassLoader(ClassLoader classLoader) {
		super(classLoader);
	}
	
	public Class loadClass(File file) throws ClassNotFoundException {
		return loadClass(file.getAbsoluteFile().toString());
	}
	
	public Class findClass(String file) throws ClassNotFoundException {
		try {
			String className = PackageNameExtractor.getFullClassName(file);
			return getSystemClassLoader().loadClass(className);
		} catch (IOException cause) {
			throw new ClassNotFoundException();
		}
	}
	
	public static byte[] loadFileData(File file) throws IOException {
		FileInputStream in = new FileInputStream(file);
		byte[] b = new byte[in.available()];
		int index = 0;
		while (in.available()>0)
			b[index++] = (byte) in.read();

		return b;
	} 
	
}
