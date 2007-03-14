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

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.nmedit.jtheme.JTException;
import net.sf.nmedit.jtheme.store.resource.ImageResource;
import net.sf.nmedit.nmutils.Timer;

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
    
    private Map<String, ModuleStore> moduleStoreMap = new HashMap<String, ModuleStore>();
    private Document document;
    private ClassLoader classLoader;
    private CSSStyleSheet styleSheet;
    
    private Map<String, ImageResource> imageResourceMap = new HashMap<String, ImageResource>();

    private static transient Log _logger;
    
    private static Log getLogger()
    {
        if (_logger == null)
            _logger = LogFactory.getLog(DefaultStorageContext.class);
        return _logger;
    }
    
    public DefaultStorageContext(ClassLoader classLoader)
    {
        this.classLoader = classLoader;
        installDefaults();
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
        installStore("module", ModuleStore.class);
        installStore("knob", KnobStore.class);
        installStore("label", LabelStore.class);
        installStore("image", ImageStore.class);
        installStore("connector", ConnectorStore.class);
        installStore("button", ButtonStore.class);
        installStore("slider", SliderStore.class);
        installStore("textDisplay", TextDisplayStore.class);
    }

    @Override
    public ModuleStore getModuleStoreById(String id)
    {
        return moduleStoreMap.get(id);
    }
    

    
    public void parseStore(InputSource source)
    throws JTException
    {
        
        SAXBuilder saxBuilder = new SAXBuilder();
        try
        {
            document = saxBuilder.build(source);
            
            Timer t = new Timer();
            t.reset();
            
            buildStore();
            
            System.out.println("buildStore "+t);
        }
        catch (JDOMException e)
        {
            throw new JTException(e);
        }
        catch (IOException e)
        {
            throw new JTException(e);
        }
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
    protected Document getDocument()
    {
        return document;
    }
    
    protected void buildStore() throws JTException
    {
        Element root = document.getRootElement();
        
        buildCss(root);
        buildDefs(root);
        buildModules(root);
    }

    private void buildDefs(Element root)
    {
        Element defs = root.getChild("defs");
        if (defs == null) return;
        
        List<Element> imageList = (List<Element>) defs.getChildren("image");
        
        for (int i=imageList.size()-1;i>=0;i--)
        {
            buildDefsImage(imageList.get(i));
        }
    }

    private void buildDefsImage(Element element)
    {
        String id = element.getAttributeValue("id");
        if (id.length() == 0) return;
        
        ImageResource is =
        ImageStore.getImageResource(this, element);
        
        imageResourceMap.put(id, is);
    }
    
    public ImageResource getImageResourceById(String id)
    {
        return imageResourceMap.get(id);
    }

    private void buildCss(Element root) throws JTException
    {
        String cssText = "";
        Element styleElement = root.getChild("style");
        if (styleElement != null)
        {
             cssText = styleElement.getText();   
        }
        
        buildCssFromString(cssText);
        
    }
    
    private void buildCssFromString(String cssText) throws JTException
    {
        CSSOMParser cssParser = new CSSOMParser();
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
        List<Element> moduleElementList = (List<Element>) root.getChildren("module");
        
        for (Element moduleElement : moduleElementList)
        {
            buildModuleStore(moduleElement);
        }
    }

    private void buildModuleStore(Element moduleElement) throws JTException
    {
        ModuleStore moduleStore = (ModuleStore) buildComponentStore(moduleElement);
        
        for (Element child : (List<Element>) moduleElement.getChildren())
        {
            String name = child.getName();
            
            boolean dontLoad = name.equals("name") || name.startsWith("select-");
            
            if (!dontLoad)
            {
                Store childStore = tryBuildComponentStore(child);
                
                if (childStore != null)
                {
                    moduleStore.add(childStore);
                }
            }
        }

        moduleStoreMap.put(moduleStore.getId(), moduleStore);
    }
    
    private Store tryBuildComponentStore(Element element)
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
                log.warn(this, e);
            }
            return null;
        }
    }
    
    private Store buildComponentStore(Element element)
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


