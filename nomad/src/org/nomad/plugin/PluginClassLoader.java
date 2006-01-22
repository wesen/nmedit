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

/*
 * Created on Jan 21, 2006
 */
package org.nomad.plugin;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.jar.JarFile;

public class PluginClassLoader extends URLClassLoader {

	public final static String pluginDirectory = "plugin";
	public final static String pluginClassName = "NomadPlugin";

	public PluginClassLoader() {
		this( getSystemClassLoader() );
	}

	public PluginClassLoader(ClassLoader classLoader) {
		super(new URL[]{}, classLoader);
	}

	public File[] listFiles() {
		return (new File(pluginDirectory)).listFiles();
	}

	// final static String jarPrefix = "jar:";
	
	protected Class findClass(String name) throws ClassNotFoundException {
		// final String jarSuffix = "jar!/";
		String classRessourceName = name.replaceAll("\\.","/")+".class";
		URL classRessource = findResource(classRessourceName);
		String urlString = classRessource.toString();
		// boolean inJar = urlString.startsWith(jarPrefix);
		try {
			URL url = new URL(urlString.substring(0, urlString.length()-classRessourceName.length()));
			addURL(url);
			propagate(url);
		} catch (MalformedURLException e) 
		{ /* ignore */ }
		return super.findClass(name);
	}

	private static final Class[] addURLParamList = new Class[] { URL.class };

	private static void propagate(URL url) {
		URLClassLoader sloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
		Class sclass = URLClassLoader.class;
		try {
			Method method = sclass.getDeclaredMethod("addURL", addURLParamList);
			method.setAccessible(true);
			method.invoke(sloader, new Object[]{ url });
		} catch (Throwable t) {
			t.printStackTrace();
			// throw new IOException("Error, could not add URL to system classloader");
		}
	}

    public static byte[] getBytesFromFile(File file) throws IOException {
    	BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
    	byte[] data = new byte[in.available()];
    	in.read(data);
    	in.close();
    	return data;
    }

	public URL findResource(String name) {
		/**
		 * handle resources plugin/{plugin name}/{resource}
		 */
		
		String[] pieces = name.split("/");
		
		if ((pieces.length>=3) && (pieces[0].equals("plugin"))) {
			String pluginName = pieces[1];
			// plugin/{plugin name}
			String pluginLocation = name.substring(0, pieces[0].length()+1+pieces[1].length());
			File pluginDir = new File(pluginLocation);
			// {resource name}
			String resourceName = name.substring(pieces[0].length()+1+pieces[1].length()+1);
			
			if (pluginDir.exists() && pluginDir.isDirectory()) {
				File resourceFile = new File(pluginDir.getPath()+File.separatorChar+resourceName);
				if (resourceFile.exists() && resourceFile.isFile())
					try {
						return resourceFile.toURL();
					} catch (MalformedURLException e) {
						// Ignore is ok
					}
			}
			
			
			// not found jet, maybe a jar ?
			pluginLocation += ".jar";
			pluginDir = new File(pluginLocation);
			
			if (pluginDir.exists() && pluginDir.isFile()) {
				try {
				JarFile jar = new JarFile(pluginDir);
				String jarEntryName =  "plugin"+File.separatorChar+pluginName+File.separatorChar+resourceName ;
					if (jar.getJarEntry(jarEntryName)!=null) {
						URL url = new URL ( "jar:file:"
								+ pluginDir.getAbsolutePath()
								+ "!/"
								+ jarEntryName);
	
						return url;
					}
				} catch (IOException e) {
					// Ignore is ok
				}
			}
		}
		
		return super.findResource(name);
	}

	public String[] listPossiblePlugins() {
		return listPossiblePlugins(pluginDirectory);
	}

	public String[] listPossiblePlugins(String path) {
		File pluginPath = new File(path);
		if ((!pluginPath.exists()) || pluginPath.isFile()) return new String[0];

		File[] plugins = pluginPath.listFiles();
		ArrayList items = new ArrayList();
		for (int i=0;i<plugins.length;i++) {
			String pluginName = checkPlugin(plugins[i]);
			if (pluginName!=null) {
				items.add(pluginPath.getName()+"."+pluginName+".NomadPlugin");
			}
		}
		
		String[] candidates = new String[items.size()];
		for (int i=0;i<items.size();i++) {
			candidates[i] = (String) items.get(i);
		}
		return candidates;
	}
	
	private String checkPlugin(File file) {
		
		if (file.getPath().toString().split("[^\\w\\./]",2).length>1) {
			// file name contains illegal characters, so it can not be name of a package
			return null;
		}
		
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i=0;i<files.length;i++) {
				if (files[i].getName().equals("NomadPlugin.class"))
					return file.getName();
			}
		} else {
			if (file.getName().endsWith(".jar")) {
				try {
					JarFile jar = new JarFile (file);
					String pluginName = file.getName().substring(0, file.getName().length()-4);
					String jarEntry = jarEntryName(file, pluginName);
					if (jar.getJarEntry(jarEntry)!=null) {
						return pluginName;
					} else {
					}
				} catch (Throwable e) {
					// not a jar, so no further checking
				}
			}
		}
		
		return null;
	}
	
	private String jarEntryName(File jar, String pluginName) {
		return "plugin"+File.separatorChar+pluginName+File.separatorChar+"NomadPlugin.class";
	}
	
}
