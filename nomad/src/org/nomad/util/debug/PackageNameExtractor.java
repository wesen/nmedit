package org.nomad.util.debug;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

public class PackageNameExtractor {

	private final static String cm1= "//[^\\r\\f\\n]*[\\r\\f\\n]"; // one line comment
	private final static String cm2= "/\\*((\\*[^\\/])|[^\\*])*\\*/"; // multi line comment
	private final static Pattern p = Pattern.compile("(\\s|("+cm1+")|("+cm2+"))*");

	public static String getFullClassName(String file) throws IOException {
		return PackageNameExtractor.getFullClassName(new File(file));
	}

	public static String getFullClassName(File file) throws IOException {
		return PackageNameExtractor.getFullClassName(file, 
			FileClassLoader.loadFileData(file)
		);
	}

	public static String getFullClassName(File file, byte[] b) {
		String cname = file.getName();
		
		String[] piecewise = cname.split("\\.");
		if (piecewise.length>=2)
			cname = piecewise[piecewise.length-2];

		String packageName = extractPackageName(b);
		if (packageName!=null)
			return packageName+"."+cname;
		else
			return cname;
	}
	
	public static String extractPackageName(File file) throws IOException {
		return extractPackageName(FileClassLoader.loadFileData(file));
	}

	public static String extractPackageName(byte[] b) {
		return extractPackageName(new String(b));
	}

	public static String extractPackageName(String source) {
		/*
		 * format:
		 * <comment>
		 * 'package' <comment> <pname> <comment> ';' 
		 */

		source = align(source); // remove comments before 'package'

		// check if there is a package declaration
		if (!source.startsWith("package"))
			return null; // no package decl.

		// extract package name
		source = source.substring("package".length()); // remove 'package' keyword
		source = align(source); // remove comments after 'package'
		int semicolon = source.indexOf(';');
		if (semicolon<0)
			return null; // invalid format

		source = source.substring(0, semicolon); // remove everything after semicolon
		source = align(source); // remove comments between packagename and semicolon

		return source; // return package name
	}
	
	private static String align(String source) {
		return p.matcher(source).replaceFirst("");
	}
	
}
