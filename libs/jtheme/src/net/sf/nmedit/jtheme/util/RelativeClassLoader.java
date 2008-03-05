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
package net.sf.nmedit.jtheme.util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.NoSuchElementException;

public class RelativeClassLoader extends ClassLoader
{
    
    private String absPathPrefix;

    public static ClassLoader fromPath(final ClassLoader parent, URL path) throws URISyntaxException
    {
        File file = new File(path.toURI());
        
        if (file.isFile())
        {
            file = file.getParentFile();
        }
        String s = file.getAbsolutePath();
        if (!s.endsWith(File.separator))
            s = s + File.separatorChar;
        
        final String ss = s;
        
        ClassLoader loader = AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
            public RelativeClassLoader run() {
                return new RelativeClassLoader(ss, parent);
            }
        }
        );
        return loader;
    }

    public RelativeClassLoader(File path, ClassLoader parent)
    {
        super(parent == null ? RelativeClassLoader.class.getClassLoader() : parent);
        absPathPrefix = path.getAbsolutePath() + File.separatorChar;
    }
    
    public RelativeClassLoader(String path, ClassLoader parent)
    {
        this(new File(path), parent);
    }

    
    public RelativeClassLoader(URL path, ClassLoader parent)    
    {
        this(url2str(path), parent);
    }
    
    private static String url2str(URL path)
    {
        return path.getFile();
    }

    public RelativeClassLoader(File path)
    {
        this(path, null);
    }
    
    public RelativeClassLoader(String path)
    {
        this(path, null);
    }
    
    public RelativeClassLoader(URL path)
    {
        this(path, null);
    }
    
    protected Class<?> findClass(String name) throws ClassNotFoundException 
    {
        // TODO
        throw new ClassNotFoundException(name);
    }
    

    public URL getResource(String name) {
        
        URL res = super.getResource(name);
        return res;
    }
    
    protected URL findResource(String name) 
    {
        //return getParent().getResource(absPathPrefix+name);
        
        File file = new File(absPathPrefix+name);
        
        if (file.exists())
        {
            try
            {
                return file.toURI().toURL();
            }
            catch (MalformedURLException e)
            { }
        }
        
        return null;
    }
    
    protected Enumeration<URL> findResources(String name) throws IOException 
    {
        return new OneOrNoneCE(findResource(name));    
    }
    
    private static class OneOrNoneCE implements Enumeration<URL>
    {

        private URL url;

        public OneOrNoneCE(URL url)
        {
            this.url = url;
        }
        
        public boolean hasMoreElements()
        {
            return url != null;
        }

        public URL nextElement()
        {
            if (!hasMoreElements())
                throw new NoSuchElementException();
            
            URL res = url;
            url = null;
            return res;
        }
        
    }
    
    public String toString()
    {
        return getClass().getName()+"[prefix="+absPathPrefix+",loader="+getParent()+"]";
    }
    
}

