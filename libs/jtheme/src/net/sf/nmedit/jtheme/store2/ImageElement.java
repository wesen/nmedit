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
package net.sf.nmedit.jtheme.store2;

import java.awt.Image;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;

import javax.swing.ImageIcon;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleDescriptor;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.JTException;
import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.JTImage;
import net.sf.nmedit.jtheme.image.AbstractImageResource;
import net.sf.nmedit.jtheme.image.ImageResource;
import net.sf.nmedit.jtheme.image.SVGImageResource;
import net.sf.nmedit.jtheme.image.SVGStringRessource;
import net.sf.nmedit.jtheme.image.ToolkitImageResource;
import net.sf.nmedit.jtheme.store.DefaultStorageContext;
import net.sf.nmedit.jtheme.store.StorageContext;
import net.sf.nmedit.nmutils.graphics.GraphicsToolkit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.transform.JDOMSource;

public class ImageElement extends AbstractElement implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1462274961082257283L;
    private String src;
    private String id;
    private transient ImageResource imageResource; // TODO serialization
    protected boolean reducible = false;
    
    private static Log getLogger()
    {
        return LogFactory.getLog(ImageElement.class);
    }

    public void initializeElement(StorageContext context)
    {
        if (id != null)
        {
            imageResource = context.getCachedImage(id);
        }
    }
    
    public static ImageResource getImageResource(StorageContext context, Element element)
    {
        String href = getXlinkHref(element);
        if (href == null) return null;
        
        ImageResource ir;
        
        if (href.endsWith("svg"))
            ir = new SVGImageResource(href, context.getContextClassLoader());
        else
            ir = new ToolkitImageResource(href, context.getContextClassLoader());
        
        ir.setImageCache(context.getImageCache());
        
        return ir;
    }

    private static final String SVG_NS = "http://www.w3.org/2000/svg";
    private transient Namespace cachedSvgNameSpace;
    
    private Namespace svgns()
    {
        if (cachedSvgNameSpace == null)
            cachedSvgNameSpace = Namespace.getNamespace(SVG_NS);
        return cachedSvgNameSpace;
    }
    
    private static final String XLINK_NS = "http://www.w3.org/1999/xlink";
    private static Namespace xlinkns = Namespace.getNamespace(XLINK_NS);
    
    public static String getXlinkHref(Element e)
    {
        return e.getAttributeValue("href", xlinkns);
    }

    public void renderImage(StorageContext context)
    {
        render();
    }

    private Image render()
    {
        if (imageResource == null)
            return null;

        Image image = imageResource.getImage(width, height);
        
        if (image == null)
        {
            Log log = LogFactory.getLog(getClass());
            if (log.isWarnEnabled())
                log.warn("Could not open image [width="+width+",height="+height+"] from:"+imageResource);
            
            return null;
        }
        
        return image;
    }
    
    @Override
    public JTComponent createComponent(JTContext context,
            PModuleDescriptor descriptor, PModule module) throws JTException
    {
        Image image = render();
        if (image == null)
            return null;
        
        JTImage jtimg = (JTImage) context.createComponent(JTContext.TYPE_IMAGE);
        setName(jtimg);
        this.reducible = jtimg.isReducible();

        jtimg.setIcon(new ImageIcon(image));
        
        setLocation(jtimg);
        if (width>=0 && height>=0)
        {
            setSize(jtimg);
        }
        else
        {
            jtimg.setSize(GraphicsToolkit.getImageSize(image));
        }

        return jtimg;
    }

    @Override
    public boolean isReducible()
    {
        return reducible;
    }
    
    public static AbstractElement createElement(StorageContext context, Element element)
    {
        ImageElement e = new ImageElement();
        e.initElement(context, element);
        e.lookupImage(context, element);
        return e;
    }

    private AbstractImageResource createResource(StorageContext context, String src)
    {
        AbstractImageResource ir;
        if (src.endsWith(".svg"))
            ir = new SVGImageResource(src, context.getContextClassLoader());   
        else
            ir = new ToolkitImageResource(src, context.getContextClassLoader());
        
        ir.setImageCache(context.getImageCache());
        return ir;
    }

    @Override
    protected void initAttributes(StorageContext context, Attribute att)
    {
        if ("href".equals(att.getName()) && xlinkns.equals(att.getNamespace()))
        {
            src = att.getValue();
        }
        else
        {
            super.initAttributes(context, att);
        }
    }
    
    protected void lookupImage(StorageContext context, Element e)
    {
        if (imageResource != null)
            return;
        
        if (src != null)
        {
            if (context instanceof DefaultStorageContext)
            {
                DefaultStorageContext dsc = (DefaultStorageContext) context;
                imageResource = dsc.getCachedImage(src);

                if (src.startsWith("url(#") && src.endsWith(")"))
                {                    
                    String id = src.substring(5, src.length()-1);
                    imageResource = dsc.getImageResourceById(id);
                    this.id = id;
                }
                else if (imageResource == null)
                {
                    AbstractImageResource air = createResource(context, src);
                    imageResource = air;
                    imageResource.setImageCache(context.getImageCache());
                    this.id = air.getResolvedURL().toString();
                    dsc.putImage(id, air);
                }
            }
            else
            {
                imageResource = createResource(context, src);
            }
        }
        else
        {
            Element svg = e.getChild("svg", svgns());
            if (svg != null)
            {
                imageResource = new SVGStringRessource(element2txt(svg));
                imageResource.setImageCache(context.getImageCache());

                if (context instanceof DefaultStorageContext)
                {
                    DefaultStorageContext dsc = (DefaultStorageContext) context;
                    this.id = context.generateId();
                    dsc.putImage(id, imageResource);
                }
            }
        }
    }

    public static Image getImage(String href, StorageContext context)
    {
        if (href.startsWith("url(#") && href.endsWith(")"))
        {
            if (context instanceof DefaultStorageContext)
            {
                DefaultStorageContext c = (DefaultStorageContext) context;
                String id = href.substring(5, href.length()-1);
                
                ImageResource ir = c.getImageResourceById(id);
                return ir == null ? null : ir.getImage(-1, -1);
            }
            return null;
        }
        else return context.getImage(href);
    }
    
    public static ImageResource svgElement2img(Element element)
    {
        String svg = element2txt(element);
        if (svg == null) return null;
        return new SVGStringRessource(svg);
    }


    public static String element2txt(Element el)
    {
        TransformerFactory tf = TransformerFactory.newInstance();
        // set all necessary features for your transformer -> see OutputKeys
        Transformer t;
        try
        {
            t = tf.newTransformer();
        }
        catch (TransformerConfigurationException e)
        {
            return null;
        }

        StringWriter sw = new StringWriter();
        try
        {
            t.transform(new JDOMSource(el), new StreamResult(sw));
        }
        catch (TransformerException e)
        {
            Log log = getLogger();
            if (log.isWarnEnabled())
            {
                log.warn("element2txt failed", e);
            }
            return null;
        }

        return sw.toString();
    }

    private void writeObject(java.io.ObjectOutputStream out)
        throws IOException
    {
        out.defaultWriteObject();
    }
    
    private void readObject(java.io.ObjectInputStream in)
        throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
    }
    
}
