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
package net.sf.nmedit.jtheme.store;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.JTException;
import net.sf.nmedit.jtheme.image.ImageCache;
import net.sf.nmedit.jtheme.image.ImageResource;
import net.sf.nmedit.jtheme.store2.ButtonElement;
import net.sf.nmedit.jtheme.store2.ComponentElement;
import net.sf.nmedit.jtheme.store2.ConnectorElement;
import net.sf.nmedit.jtheme.store2.ImageElement;
import net.sf.nmedit.jtheme.store2.LabelElement;
import net.sf.nmedit.jtheme.store2.LightElement;
import net.sf.nmedit.jtheme.store2.ModuleElement;
import net.sf.nmedit.jtheme.store2.SliderElement;
import net.sf.nmedit.jtheme.store2.TextDisplayElement;
import net.sf.nmedit.nmutils.PluginObjectInputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.w3c.dom.css.CSSStyleSheet;
import org.xml.sax.InputSource;

import com.steadystate.css.parser.CSSOMParser;

public class DefaultStorageContext extends StorageContext
{
    
    private static final Log log = LogFactory.getLog(DefaultStorageContext.class);
    
    private static final boolean DEBUG = false;
    
    private Map<Object, ModuleElement> moduleStoreMap = new HashMap<Object, ModuleElement>();
    private ClassLoader classLoader;
    private CSSStyleSheet styleSheet;

    private Map<String, ImageResource> imageResourceMap = new HashMap<String, ImageResource>();
    
    private ImageCache imageCache = new ImageCache();

    private static transient Log _logger;
    
    private static Log getLogger()
    {
        if (_logger == null)
            _logger = LogFactory.getLog(DefaultStorageContext.class);
        return _logger;
    }

    public ImageCache getImageCache()
    {
        return imageCache;
    }
    
    public DefaultStorageContext(JTContext context, ClassLoader classLoader)
    {
        this.classLoader = classLoader;
        this.context = context;
        installDefaults();
    }

    private JTContext context;

    public JTContext getContext()
    {
        return context;
    }

    public ImageResource getCachedImage(URL source)
    {
        if (source != null)
        {
            return imageResourceMap.get(source.toString());
        }
        return null;
    }

    public ImageResource getCachedImage(String source)
    {
        ImageResource ir = imageResourceMap.get(source);
        if (ir == null)
        {
            ClassLoader loader = classLoader != null ? classLoader :getClass().getClassLoader();
            URL url = loader.getResource(source);
            ir = getCachedImage(url);
        }
        return ir;
    }
    /*
    public void putImage(URL url, ImageResource ir)
    {
        if (url != null)
        {
            if (ir.getImageCache() != null)
                ir.setImageCache(imageCache);
            imageResourceMap.put(url, ir);
        }
    }*/
    
    public void putImage(String key, ImageResource ir)
    {
        if (key != null)
        {
            if (ir.getImageCache() != null)
                ir.setImageCache(imageCache);
            imageResourceMap.put(key, ir);
        }
    }
    
    public CSSStyleSheet getStyleSheet()
    {
        return styleSheet;
    }
    
    public ClassLoader getContextClassLoader()
    {
        return classLoader;
    }
    
    protected void installDefaults()
    {
        installStore("module", ModuleElement.class);
        installStore("label", LabelElement.class);
        installStore("image", ImageElement.class);
        installStore("connector", ConnectorElement.class);
        installStore("button", ButtonElement.class);
        installStore("slider", SliderElement.class);
        installStore("textDisplay", TextDisplayElement.class);
        installStore("light", LightElement.class);
    }

    @Override
    public ModuleElement getModuleStoreById(Object id)
    {
        return moduleStoreMap.get(id);
    }
    
    private File cacheDir = null;

    private File getElementCacheFile()
    {
        if (cacheDir != null)
            return new File(cacheDir, "data.cache");
        return null;
    }

    private File getImageCacheFile()
    {
        if (cacheDir != null)
            return new File(cacheDir, "image.cache");
        return null;
    }

    public void setCacheDir(File cacheDir)
    {
        this.cacheDir = cacheDir;
        
        if (cacheDir != null && (!cacheDir.exists()))
        {
            boolean ok = cacheDir.mkdir();
            // TODO handle ok==false
        }
    }
    
    public File getCacheDir()
    {
        return cacheDir;
    }


    public void parseStore(InputSource source)
    throws JTException
    {
        parseStore(source, null);
    }

    public void parseStore(InputSource source, ClassLoader loader)
    throws JTException
    {
        final boolean DEBUG_DISABLE_CACHE = false;
        
        
        if (loader == null)
            loader = getClass().getClassLoader();
        
        long t = 0; // for timing stuff

        boolean imageCacheRead = false;
        boolean elementCacheRead = false;
        
        
        File imageCacheFile = getImageCacheFile();
        if (imageCacheFile != null && imageCacheFile.exists() && (!DEBUG_DISABLE_CACHE))
        {
            if (DEBUG) System.out.println(this+": image cache file "+imageCacheFile+" (exists)");
            
            try
            {
                if (DEBUG) t = System.currentTimeMillis();
                imageCache.readCacheFile(imageCacheFile);
                imageCacheRead = true;
                if (DEBUG) System.out.println(this+": image cache read in "+(System.currentTimeMillis()-t)+"ms");
            }
            catch (Throwable et)
            {
                if (log.isTraceEnabled())
                    log.trace("loading images from cache failed", et);
            }
        }
        
        File cacheFile = getElementCacheFile();

        try
        {

            if (cacheFile != null && (!DEBUG_DISABLE_CACHE))
            {
                if (DEBUG) System.out.println(this+": element cache file "+cacheFile+" (exists:"+cacheFile.exists()+")");
            
                if (cacheFile.exists())
                {     if (DEBUG) t = System.currentTimeMillis();
                    if (initializeFromCache(cacheFile, loader))
                    {
                        elementCacheRead = true;
                        if (DEBUG) System.out.println(this+": elements initialized from cache in "+(System.currentTimeMillis()-t)+"ms");
                    }
                    else
                    {
                        if (DEBUG) System.out.println(this+": initialization of elements from cache failed");
                    }
                }
            }
        }
        catch (Exception e)
        {
            if (log.isTraceEnabled())
                log.trace("loading elements from cache failed", e);
        }
        
        if (imageCacheRead && elementCacheRead)
            return;
        
        if (!elementCacheRead)
        {
            SAXBuilder saxBuilder = new SAXBuilder();
            try
            {    
                if (DEBUG) t = System.currentTimeMillis();
                Document document = saxBuilder.build(source);
                buildStore(document);
                if (DEBUG) System.out.println(this+": built store in "+(System.currentTimeMillis()-t)+"ms");
            }
            catch (JDOMException e)
            {
                throw new JTException(e);
            }
            catch (IOException e)
            {
                throw new JTException(e);
            }
            
            if (cacheFile != null && (!DEBUG_DISABLE_CACHE))
            {
                if (DEBUG) System.out.println(this+": writing element cache");
                writeCache(cacheFile);
            }
        }
        
        if (!imageCacheRead)
        {
            if (imageCacheFile != null)
            {
                // render images
                if (DEBUG) System.out.println(this+": render images...");

                if (DEBUG) t = System.currentTimeMillis();
                for (ModuleElement m: moduleStoreMap.values())
                {
                    for (ComponentElement e: m)
                    {
                        if (e instanceof ImageElement)
                            ((ImageElement) e).renderImage(this);
                    }
                }
                if (DEBUG) System.out.println(this+": rendered in "+(System.currentTimeMillis()-t)+"ms");
    
                try
                {
                    if (!DEBUG_DISABLE_CACHE)
                    {
                        if (DEBUG) System.out.println(this+": write image cache...");
                        imageCache.writeCacheFile(imageCacheFile);
                    }
                }
                catch (Exception e)
                {
                    if (log.isWarnEnabled())
                        log.warn("could not write image cache to: "+imageCacheFile, e);
                }
            }
        }
        
    }
  
    private void writeCache(File cacheFile)
    {
        try
        {
            __writeCache(cacheFile);
        }
        catch (Exception e)
        {
            if (log.isErrorEnabled())
                log.error("writeCache("+(cacheFile==null?null:cacheFile.getAbsolutePath())+") failed", e);
        }
    }

    private void __writeCache(File cacheFile) throws Exception
    {
        ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(cacheFile)));
        try
        {
            // css
            out.writeObject(cssStyleSheet);
            
            // defs
            out.writeInt(imageResourceMap.size());
            for (Object key: imageResourceMap.keySet())
            {
                out.writeObject(key);
                Object ir = imageResourceMap.get(key);
                out.writeObject(ir);
            }
            // modules
            out.writeInt(moduleStoreMap.size());
            for (ModuleElement e: moduleStoreMap.values())
            {
                out.writeObject(e);    
            }
        }
        finally
        {
            out.flush();
            out.close();
        }
    }

    private boolean initializeFromCache(File cacheFile, ClassLoader loader) throws Exception
    {
        ObjectInputStream in = new PluginObjectInputStream(loader,
                new BufferedInputStream(new FileInputStream(cacheFile)));
        try
        {
            {
                // css
                cssStyleSheet = (String) in.readObject();
                try
                {
                    buildCssFromString(cssStyleSheet);
                }
                finally
                {
                    cssStyleSheet = null;
                }
            }
            {
                // defs
                ClassLoader cl = getContextClassLoader();
                if (cl == null) cl = loader;
                int size = in.readInt();
                
                imageResourceMap = new HashMap<String, ImageResource>(size*2);
                for (int i=0;i<size;i++)
                {
                    String key = (String) in.readObject();
                    ImageResource ir = (ImageResource) in.readObject();
                    ir.setCustomClassLoader(cl);
                    ir.setImageCache(imageCache);

                    imageResourceMap.put(key, ir);
                }
            }
            {
                // module elements
                int size = in.readInt();
                moduleStoreMap = new HashMap<Object, ModuleElement>(size);
                for (int i=0;i<size;i++)
                {
                    ModuleElement e = (ModuleElement) in.readObject();
                    moduleStoreMap.put(e.getId(), e);
                    e.initializeElement(this);
                }
            }
        }
        finally
        {
            in.close();
        }
        return true;
    }

/*  
    public static DefaultStorageContext parseStore(ClassLoader classLoader, InputSource source)
      throws JTException
    {
        DefaultStorageContext dsc = new DefaultStorageContext(classLoader); 
        dsc.parseStore(source);
        return dsc;
    }
*/

    protected void buildStore(Document document) throws JTException
    {
        Element root = document.getRootElement();

        buildDefs(root);
        buildCss(root);
        buildModules(root);
    }

    private void buildDefs(Element root)
    {
        Element defs = root.getChild("defs");
        if (defs == null) return;
        
        List imageList = defs.getChildren("image");
        
        for (int i=imageList.size()-1;i>=0;i--)
        {
            buildDefsImage((Element)imageList.get(i));
        }
    }

    private void buildDefsImage(Element element)
    {
        String id = element.getAttributeValue("id");
        if (id.length() == 0) return;
        
        ImageResource is = ImageElement.getImageResource(this, element);
        if (is != null)
        {
            is.setImageCache(getImageCache());
            imageResourceMap.put(id, is);
        }
    }
    
    public ImageResource getImageResourceById(String id)
    {
        return imageResourceMap.get(id);
    }

    protected transient String cssStyleSheet;
    
    private void buildCss(Element root) throws JTException
    {
        Element styleElement = null;
        
        List<?> children = root.getChildren();
        for (int i=0;i<children.size();i++)
        {
            Element e = (Element) children.get(i);
            String name = e.getName();
            
            if (name.equals("style"))
            {
                styleElement = e;
                break;
            }
            else if (!name.equals("defs"))
            {
                break;
            }
        }

        cssStyleSheet = styleElement != null
            ? styleElement.getText() : "";
        buildCssFromString(cssStyleSheet);
    }
    
    private void buildCssFromString(String cssText) throws JTException
    {
        CSSOMParser cssParser = CSSUtils.getCSSOMParser();
        try
        {
            // TODO set uri ???
            //source.setURI(arg0)
            if (cssText == null)
                return;

            org.w3c.css.sac.InputSource source = new org.w3c.css.sac.InputSource(new StringReader(cssText));

            styleSheet = cssParser.parseStyleSheet(source, null, null);
        }
        catch (NullPointerException e) 
        {
            Log log = getLogger();
            if (log.isWarnEnabled())
            {
                log.warn("buildCssFromString", e);
            }
        }
        catch (IOException e)
        {
            Log log = getLogger();
            if (log.isWarnEnabled())
            {
                log.warn("buildCssFromString", e);
            }
            return;
            // throw new JTException(e);
        }        
    }

    private void buildModules(Element root) throws JTException
    {
        for (Object moduleElement : root.getChildren("module"))
        {
            buildModuleStore((Element)moduleElement);
        }
    }

    private void buildModuleStore(Element moduleElement) throws JTException
    {
        ModuleElement moduleStore = (ModuleElement) buildComponentStore(moduleElement);
        
        for (Object uchild : moduleElement.getChildren())
        {
            Element child = (Element) uchild;
            String name = child.getName();
            
            boolean dontLoad = name.equals("name") || name.startsWith("select-");
            
            if (!dontLoad)
            {
                ComponentElement childStore = tryBuildComponentStore(moduleElement, child);
                
                if (childStore != null)
                {
                    moduleStore.add(childStore);
                }
            }
        }

        moduleStoreMap.put(moduleStore.getId(), moduleStore);
    }
    
    private ComponentElement tryBuildComponentStore(Element moduleElement, Element element)
    {
        try
        {
            return buildComponentStore(element);
        }
        catch (JTException e)
        {
            Log log = getLogger();
            if (log.isWarnEnabled())
            {
                log.warn("could not create component store for element "+
                        element.getName()+
                        " (parent "+moduleElement.getAttributeValue("component-id")+")", e);
            }
            return null;
        }
    }
    
    private ComponentElement buildComponentStore(Element element)
      throws JTException
    {
        // TODO not all elements can be handled ????
        try
        {
            return createStore(element);
        }
        catch (JTException e)
        {
            // TODO find a better solution for unsupported elements
            if (e.getMessage().startsWith("No store for element"))
            {
                Log log = getLogger();
                if (log.isWarnEnabled())
                {
                    log.warn(this, e);
                }
                return null;
            }
            throw e;
        }
    }
    
}


