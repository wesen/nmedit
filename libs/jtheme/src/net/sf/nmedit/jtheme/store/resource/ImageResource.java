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
package net.sf.nmedit.jtheme.store.resource;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.net.URL;

import net.sf.nmedit.jtheme.store.StorageContext;
import net.sf.nmedit.jtheme.store.helpers.ByteBuffer;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class ImageResource implements Resource
{

    public Image getImage(Dimension preferredSize)
    {
        return getImage(preferredSize.width, preferredSize.height);
    }
    
    public abstract Image getImage(int prefWidth, int prefHeight);
    
    public static class ImageImageResource extends ImageResource 
    {

        private Image image;
        private URL url;

        public ImageImageResource(Image image, URL url)
        {
            this.image = image;
            this.url = url;
        }

        public Image getImage(Dimension preferredSize)
        {
            return image;
        }

        public Image getImage(int prefWidth, int prefHeight)
        {
            return image;
        }

        public URL getURL()
        {
            return url;
        }
        
    }

    public static class RasterImageResource extends ImageResource
    {

        private StorageContext context;
        private String uri;
        private transient URL url;
        
        public RasterImageResource(StorageContext context, String uri)
        {
            this.context = context;
            this.uri = uri;
        }
        
        public Image getImage(int prefWidth, int prefHeight)
        {
            Image image = context.getImage(uri);
            if (image != null) return image;
            
            
            Log log = LogFactory.getLog(getClass());
            if (log.isWarnEnabled())
            {
                log.warn((getClass()+", uri: '"+ uri +"' image not found"));
            }
            return null;
        }

        public URL getURL()
        {
            if (url != null)
                return url;
            
            return url = context.getContextClassLoader().getResource(uri);
        }
        
    }
    
    public static class SVGImageResource extends ImageResource
    {

        private String uri;
        private ClassLoader loader;
        private transient byte[] bytes;
        private transient URL url;
        private transient Image cachedImage;
        private transient int ciw = 0;
        private transient int cih = 0;
        
        public SVGImageResource(URL url)
        {
            this.url = url;
        }
        
        public SVGImageResource(ClassLoader loader, String uri)
        {
            this.loader = loader;
            this.uri = uri;
        }

        public URL getURL()
        {
            if (url != null)
                return url;
            
            return url = loader.getResource(uri);
        }
        
        protected InputStream getInputStream() throws FileNotFoundException, URISyntaxException
        {
            if (url != null)
                return new FileInputStream(new File(url.toURI()));
            else
                return loader.getResourceAsStream(uri);
        }
        
        protected boolean ensureBytesAvailable()
        {
            if (bytes != null)
                return true;

            InputStream source = null; 
            try
            {
                source = new BufferedInputStream(getInputStream()); 
                ByteBuffer bos = new ByteBuffer();
                int data;
                while ((data = source.read()) != -1)
                {
                    bos.write(data);
                }
                bytes = bos.getBytes();
            }
            catch (Exception e)
            {
                Log log = LogFactory.getLog(getClass());
                if (log.isWarnEnabled())
                {
                    log.warn((getClass()+",uri='"+ uri +"',url='"+url+"' "+ e), e);
                }
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
            return false;
        }
        
        public Image getImage(int prefWidth, int prefHeight)
        {
            if (cachedImage != null && ciw == prefWidth && cih == prefHeight)
            {
                return cachedImage;
            }
            
            if (!ensureBytesAvailable())
                return null;
            
            cachedImage = svg2image(new InputStreamReader(new ByteArrayInputStream(bytes)), prefWidth, prefHeight);
            
            ciw = prefWidth;
            cih = prefHeight;
            
            return cachedImage;
        }
        
    }

    public static Image svg2image(Reader source, int width, int height)
    {

        ImageTranscoder pngt = new PNGTranscoder();
        TranscoderInput input = new TranscoderInput(source);
        
        ByteBuffer ostream = new ByteBuffer();
        
        try
        {
            TranscoderOutput output = new TranscoderOutput(ostream);
            
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
                    log.warn("could not load image", e);
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
                e.printStackTrace();
            }
        }
        
        return Toolkit.getDefaultToolkit().createImage(ostream.getBytes());
        
    }

    public static Image svg2image(String svg, int width, int height)
    {
        if (svg == null) return null;

        return svg2image(new StringReader(svg), width, height);
    }
    
}
