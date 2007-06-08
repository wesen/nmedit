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

import java.awt.Image;
import java.io.StringWriter;

import javax.swing.ImageIcon;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.JTException;
import net.sf.nmedit.jtheme.component.JTImage;
import net.sf.nmedit.jtheme.image.AbstractImageResource;
import net.sf.nmedit.jtheme.image.ImageResource;
import net.sf.nmedit.jtheme.image.SVGImageResource;
import net.sf.nmedit.jtheme.image.SVGStringRessource;
import net.sf.nmedit.jtheme.image.ToolkitImageResource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.transform.JDOMSource;

public class ImageStore extends DefaultStore
{
    
    private static transient Log logger;
    
    private static Log getLogger()
    {
        if (logger == null)
            logger = LogFactory.getLog(ImageStore.class);
        return logger;
    }
    
    public static ImageResource getImageResource(StorageContext context, Element element)
    {
        String href = getXlinkHref(element);
        if (href == null) return null;
        if (href.endsWith("svg"))
            return new SVGImageResource(href, context.getContextClassLoader());
        else
            return new ToolkitImageResource(href, context.getContextClassLoader());
    }

    private AbstractImageResource createResource(String src)
    {
        if (src.endsWith(".svg"))
            return new SVGImageResource(src, context.getContextClassLoader());   
        else
            return new ToolkitImageResource(src, context.getContextClassLoader());
    }
    
    private StorageContext context;
    private String src;
    private ImageResource imageResource;
    private int width = -1;
    private int height = -1;

    protected ImageStore(StorageContext context, Element e)
    {
        super(e);
        this.context = context;
        src = getXlinkHref(e);
        
        if (src != null)
        {
            width = getIntAtt("width", -1);
            height = getIntAtt("height", -1);
            
            if (context instanceof DefaultStorageContext)
            {
                DefaultStorageContext dsc = (DefaultStorageContext) context;
                imageResource = dsc.getCachedImage(src);

                if (src.startsWith("url(#") && src.endsWith(")"))
                {
                    String id = src.substring(5, src.length()-1);
                    imageResource = dsc.getImageResourceById(id);
                }
                else if (imageResource == null)
                {
                    AbstractImageResource air = createResource(src);
                    imageResource = air;
                    dsc.putImage(air.getResolvedURL(), air);
                }
            }
            else
            {
                imageResource = createResource(src);
            }
        }
        else
        {
            Element svg = e.getChild("svg", svgns());
            if (svg != null)
            {
                width = getIntAtt(svg, "width", -1);
                height = getIntAtt(svg, "height", -1);
                imageResource = new SVGStringRessource(element2txt(svg));
            }
        }
    }
    
    public StorageContext getContext()
    {
        return context;
    }
    
    public ClassLoader getContextClassLoader()
    {
        return getContext().getContextClassLoader();
    }
    
    public static ImageStore create(StorageContext context, Element element)
    {
        ImageStore is = new ImageStore(context, element);
        
        return is;
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

    @Override
    public JTImage createComponent(JTContext context) throws JTException
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
        
        JTImage jtimg = (JTImage) context.createComponent(JTContext.TYPE_IMAGE);
        applyName(jtimg);
        setReducible(jtimg);
        
        jtimg.setIcon(new ImageIcon(image));
        applyLocation(jtimg);
        if (width>=0 && height>=0)
        {
            jtimg.setSize(width, height);
        }
        return jtimg;
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
}

