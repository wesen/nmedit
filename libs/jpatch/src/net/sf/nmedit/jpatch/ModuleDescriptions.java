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
 * Created on Dec 10, 2006
 */
package net.sf.nmedit.jpatch;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.nmedit.jpatch.impl.PBasicModuleDescriptor;
import net.sf.nmedit.jpatch.js.JSContext;
import net.sf.nmedit.jpatch.transform.PTTransformations;
import net.sf.nmedit.nmutils.PluginObjectInputStream;

public class ModuleDescriptions implements Iterable<PModuleDescriptor>
{

    private PSignalTypes signalTypes;
    private Map<String,PTypes<PType>> types = new HashMap<String, PTypes<PType>>();
    private Map<Object, PModuleDescriptor> moduleMap = new HashMap<Object, PModuleDescriptor>(127);
    private ClassLoader loader;
    protected PTTransformations transformations;
    protected PModuleMetrics metrics; 
    
    private SoftReference<Map<ImageSource, Image>> imageCacheReference;
    private JSContext jscontext;
    
    public ModuleDescriptions(ClassLoader resourceClassLoader)
    {
        this.loader = resourceClassLoader;
        jscontext = new JSContext();
    }
    
    public JSContext getJSContext()
    {
        return jscontext;
    }
    
    private Map<ImageSource, Image> getImageCache()
    {
        Map<ImageSource, Image> cache = 
            (imageCacheReference != null) ? imageCacheReference.get() : null;
        if (cache == null)
        {
            cache = new HashMap<ImageSource, Image>();
            imageCacheReference = new SoftReference<Map<ImageSource,Image>>(cache);
        }
        return cache;
    }
    
    protected ClassLoader getResourceClassLoader()
    {
        return loader == null ? getClass().getClassLoader() : loader;
    }
    
    public Image getImage(ImageSource source)
    {
        Map<ImageSource, Image> cache = getImageCache();
        Image image = cache.get(source);
        if (image == null)
        {
            URL url = getResourceClassLoader().getResource(source.getSource());
            if (url != null)
            {
                image = Toolkit.getDefaultToolkit().getImage(url);
                cache.put(source, image);
            }
        }
        return image;
    }
    
    public void writeCache(ObjectOutputStream out) throws IOException
    {
        // signals
        out.writeObject(signalTypes);
        // types
        out.writeInt(types.size());
        for (Entry<String, PTypes<PType>> e: types.entrySet())
        {
            out.writeObject(e.getKey());
            out.writeObject(e.getValue());
        }
        // modules
        out.writeInt(moduleMap.size());
        for (PModuleDescriptor m : moduleMap.values())
        {
            out.writeObject(m);
        }
    }
    
    public void writeCache(File cache) throws FileNotFoundException, IOException
    {
        ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(cache)));
        try
        {
            writeCache(out);
        }
        finally
        {
            out.flush();
            out.close();
        }
    }

    @SuppressWarnings("unchecked")
    public void readCache(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        // signals
        signalTypes = (PSignalTypes) in.readObject();
        int size;
        // types
        size = in.readInt();
        for (int i=0;i<size;i++)
        {
            String key = (String) in.readObject();
            PTypes<PType> t = (PTypes) in.readObject();
            types.put(key, t);
        }
        // modules
        size = in.readInt();
        for (int i=0;i<size;i++)
        {
            PModuleDescriptor d = (PModuleDescriptor) in.readObject();
            ((PBasicModuleDescriptor)d).setModuleDescriptions(this);
            moduleMap.put(d.getComponentId(), d);
        }
    }

    public void readCache(File cache) throws FileNotFoundException, IOException, ClassNotFoundException
    {
        readCache(cache, getClass().getClassLoader());
    }
    
    public void readCache(File cache, ClassLoader loader) throws FileNotFoundException, IOException, ClassNotFoundException
    {
        ObjectInputStream in = new PluginObjectInputStream(loader, new BufferedInputStream(new FileInputStream(cache)));
        try
        {
            readCache(in);
        }
        finally
        {
            in.close();
        }
    }
    
    public PModuleMetrics getMetrics()
    {
        return metrics;
    }
    
    public PTTransformations getTransformations()
    {
        return transformations;
    }
    
    public ClassLoader getModuleDescriptionsClassLoader()
    {
        if (loader == null)
            return getClass().getClassLoader();
        
        return loader;
    }
    
    public PSignalTypes getDefinedSignals()
    {
        return signalTypes;
    }
    
    public int getModuleCount()
    {
        return moduleMap.size();
    }
    
    public void add( PModuleDescriptor module )
    {
        moduleMap.put(module.getComponentId(), module);
    }
    
    public PModuleDescriptor getModuleById(Object id)
    {
        return moduleMap.get(id);
    }
    
    public void setSignals( PSignalTypes signalTypes )
    {
        this.signalTypes = signalTypes;
    }

    public void addType( PTypes<PType> type )
    {
        types.put(type.getName(), type);
    }
    
    public PTypes<PType> getType( String name )
    {
        return types.get(name);
    }
    
    public PModuleDescriptor[] modulesByCategory(String categoryName)
    {
        List<PModuleDescriptor> modules = null;
        for (PModuleDescriptor mod : this)
        {
            if (categoryName.equals(mod.getCategory()))
            {
                if (modules == null)
                    modules = new LinkedList<PModuleDescriptor>();
                modules.add(mod);
            }
        }
        if (modules == null || modules.isEmpty())
            return new PModuleDescriptor[0];
        
        return modules.toArray(new PModuleDescriptor[modules.size()]);
    }

    public Iterator<PModuleDescriptor> iterator()
    {
        return moduleMap.values().iterator();
    }


    public void setTransformations(PTTransformations t)
    {
        this.transformations = t;
    }
    
}
