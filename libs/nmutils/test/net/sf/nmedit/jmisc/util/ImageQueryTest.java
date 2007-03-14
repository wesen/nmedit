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
 * Created on Aug 29, 2006
 */
package net.sf.nmedit.jmisc.util;

import static org.junit.Assert.assertTrue;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.sf.nmedit.nmutils.graphics.ImageQuery;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * junit test for {@link net.sf.nmedit.nmutils.graphics.ImageQuery} 
 * @author Christian Schneider
 */
public class ImageQueryTest 
{
    // width/height of image
    private final static int DIM = 1000;
    // temporary file
    private static File tempFile = null;

    /**
     * Creates a temporary jpg image and lets point {@link #tempFile} to it.
     */
    @BeforeClass
    public static void createTemporaryImage() throws IOException
    {
        tempFile = File.createTempFile(ImageQuery.class.getSimpleName(), ".jpg");
        tempFile.deleteOnExit();   
        ImageIO.write(new BufferedImage(DIM,DIM,BufferedImage.TYPE_INT_ARGB), "jpg", tempFile);
    }

    /**
     * Deletes the temporary image.
     */
    @AfterClass
    public static void deleteTemporaryImage()
    {
        if (tempFile!=null)
        {
            tempFile.delete();
            tempFile = null;
        }
    }

    /**
     * Tests {@link ImageQuery#waitForDimensions(Image)} using the temporary image.
     * The test fails when the image dimensions are available before <code>waitForDimensions</code>
     * was called. 
     */
    @Test
    public void testWaitForDelayed()
    {
        assertTrue("temporary image not available", (tempFile!=null) && (tempFile.exists()));

        // do not use ImageIO.read() since it reads the image at once
        Image img = Toolkit.getDefaultToolkit().getImage(tempFile.getPath());

        assertTrue("image could not be read", img!=null);
        assertTrue("image already loaded", !checksz(img));
        boolean available = ImageQuery.waitForDimensions(img);
        assertTrue("function failed waiting for dimensions", available);
        assertTrue("image not loaded", checksz(img));
    }

    /**
     * Tests {@link ImageQuery#waitForDimensions(Image)} using a image that is already loaded.
     */
    @Test
    public void testWaitForNotDelayed()
    {
        // do not use ImageIO.read() since it reads the image at once
        Image img = new BufferedImage(DIM, DIM, BufferedImage.TYPE_INT_ARGB);

        assertTrue("image could not be read", img!=null);
        boolean available = ImageQuery.waitForDimensions(img);
        assertTrue("function failed waiting for dimensions", available);
        assertTrue("image not loaded", checksz(img));
    }

    /**
     * @return true when the image dimensions are available
     */
    private static boolean checksz(Image img)
    {
        return (img.getWidth(null)==DIM)&&(img.getHeight(null)==DIM);
    }
    
}
