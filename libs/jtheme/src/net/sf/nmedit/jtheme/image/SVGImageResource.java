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
package net.sf.nmedit.jtheme.image;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.net.URL;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SVGImageResource extends AbstractImageResource implements Serializable
{
    
    /**
     * 
     */
    private static final long serialVersionUID = 9179004169003176461L;
    private String svgData;
    private transient boolean svgDataInitialized;
    
    public SVGImageResource(URL imageURL)
    {
        super(imageURL);
    }
    
    public SVGImageResource(String srcURI)
    {
        super(srcURI, null);
    }
    
    public SVGImageResource(String srcURI, ClassLoader customClassLoader)
    {
        super(srcURI, customClassLoader);
    }

    protected void initState()
    {
        super.initState();
        svgDataInitialized = false;
    }

    private void readData()
    {
        InputStream source = null; 
        try
        {
            byte[] tmp = new byte[1024]; 
            
            source = new BufferedInputStream(createInputStream());
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int size;
            while ((size = source.read(tmp)) != -1)
            {
                buffer.write(tmp, 0, size);
            }
            svgData = new String(buffer.toByteArray());
        }
        catch (IOException e)
        {
            Log log = LogFactory.getLog(getClass());
            if (log.isErrorEnabled())
                log.error(getClass().getName()+".readData() failed", e);
        }
        finally
        {
            try
            {
                if (source != null)
                    source.close();
            }
            catch (IOException e)
            {
                // ignore
            }
        }   
    }

    @Override
    public Image getImage(int width, int height)
    {        
        if (!svgDataInitialized)
        {
            svgDataInitialized = true;
            readData();
        }
        if (svgData == null)
            return null;

        return getSVGImage(getImageCache(), svgData, width, height);
    }

    @Override
    public int getType()
    {
        return SCALABLE_IMAGE;
    }

    public static Image getSVGImage(ImageCache cache, String svgData, int width, int height)
    {
        if (cache != null)
        {
            Image image = cache.getImage(svgData, width, height);
            if (image != null)
                return image;
        }

        Image renderedImage = renderSVGImage(svgData, width, height);
        
        //Dimension size = GraphicsToolkit.getImageSize(renderedImage);

        if (cache != null && renderedImage != null)
        {
            cache.putImage(svgData, width, height, renderedImage);
        }
        
        return renderedImage;
    }
    
    private static Image renderSVGImage(Reader source, int width, int height)
    {

        ImageTranscoder pngt = new PNGTranscoder();
        TranscoderInput input = new TranscoderInput(source);
        
        ByteArrayOutputStream out = new ByteArrayOutputStream(8096);
        
        try
        {
            TranscoderOutput output = new TranscoderOutput(out);
            
            if (width>0 && height>0)
            {
                Float w = new Float(width);
                
                pngt.addTranscodingHint(PNGTranscoder.KEY_WIDTH, w);
                pngt.addTranscodingHint(PNGTranscoder.KEY_MAX_WIDTH, w);
                Float h = new Float(height);
                pngt.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, h);
                pngt.addTranscodingHint(PNGTranscoder.KEY_MAX_HEIGHT, h);
            }
            
            // TODO other solution for SAX parser / class loader issue ?
            Thread.currentThread().setContextClassLoader(ImageResource.class.getClassLoader());
            
            // Save the image.
            try
            {
                pngt.transcode(input, output);
            }
            catch (TranscoderException e)
            {
                Log log = LogFactory.getLog(ImageResource.class);
                if (log.isWarnEnabled())
                {
                    log.warn("could not render image", e);
                }
                return null;
            }
        }
        finally
        {
            try
            {
                source.close();
            }
            catch (IOException e)
            {
                Log log = LogFactory.getLog(ImageResource.class);
                if (log.isWarnEnabled())
                {
                    log.warn("could not close stream", e);
                }
            }
        }
        
        return Toolkit.getDefaultToolkit().createImage(out.toByteArray());
        
    }

    private static Image renderSVGImage(String svg, int width, int height)
    {
        return renderSVGImage(new StringReader(svg), width, height);
    }
    
    public void flush()
    {
        super.flush();
        svgDataInitialized = false;
        svgData = null;
    }

    private void writeObject(java.io.ObjectOutputStream out)
        throws IOException
    {
        if (!svgDataInitialized)
        {
            svgDataInitialized = true;
            readData();
        }
        out.defaultWriteObject();
    }
    
    private void readObject(java.io.ObjectInputStream in)
        throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
        initState();
    }
    
}
