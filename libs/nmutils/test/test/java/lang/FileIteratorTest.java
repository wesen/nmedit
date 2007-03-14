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
 * Created on Sep 5, 2006
 */
package test.java.lang;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.sf.nmedit.nmutils.iterator.FileIterator;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test for the FileIterator
 * @author Christian Schneider
 */
public class FileIteratorTest
{
    
    static Set<File> tempFiles = new HashSet<File>();
    static File root = null;
    
    static String getTempDir()
    {
        String d = System.getProperty("java.io.tmpdir");
        // add trailing separator if missing
        if ( !(d.endsWith("/") || d.endsWith("\\")) )
           d += System.getProperty("file.separator");
        return d;
    }

    static File createTempDir()
    {
        String dir = getTempDir();
        for (int i=0;i<40;i++)
        {
            String suffix = "FITest"+(int)(Math.random()*999);
            File tempDir = new File(dir+suffix);
            if ((!tempDir.exists()) && tempDir.mkdir())
                return tempDir;
        }
        // failed
        return null;
    }
    
    @BeforeClass
    public static void createTestDirectory() throws IOException
    {
        root = createTempDir();
        Assert.assertTrue("could not create temporary directory", root!=null);
        tempFiles.add(root);

        // creates the directory structure
        // sys-temp-dir /
        //    FITestXXX /
        //      a
        //      b
        //      emptyDir /
        //      nonEmptyDir /
        //        c
        //        d
        
        
        File f;
        f = new File(root, "a");
        Assert.assertTrue(f.createNewFile());
        tempFiles.add(f);
        f = new File(root, "b");
        Assert.assertTrue(f.createNewFile());
        tempFiles.add(f);

        f = new File(root, "emptyDir");
        Assert.assertTrue("could not create empty dir", f.mkdir());
        tempFiles.add(f);

        File subdir = f = new File(root, "nonEmptyDir");
        Assert.assertTrue("could not create non-empty dir", f.mkdir());
        tempFiles.add(f);

        f = new File(subdir, "c");
        Assert.assertTrue(f.createNewFile());
        tempFiles.add(f);
        f = new File(subdir, "d");
        Assert.assertTrue(f.createNewFile());
        tempFiles.add(f);
    }
    
    @AfterClass
    public static void removeTestDirectory()
    {
        // first delete files
        for (File f : tempFiles)
            if (f.isFile())
                Assert.assertTrue(f.delete());
        
        // then delete directories (would fail when containing files/dirs)
        for (File f : root.listFiles())
            Assert.assertTrue(f.delete());
        Assert.assertTrue(root.delete());
        
        // clear
        tempFiles.clear();
        root = null;
    }

    /**
     * Tests whether the files iterator finds all finds and directories.
     */
    @Test
    public void testFindsAll()
    {
        Assert.assertTrue(root!=null);
        
        Iterator<File> i = new FileIterator(root);
        
        int cnt = 0;
        int found = 0;
        while (i.hasNext())
        {
            File f = i.next();
            if (tempFiles.contains(f))
                found ++;
            cnt ++;
        }

        Assert.assertTrue(cnt==found);
        Assert.assertTrue(cnt==tempFiles.size());
    }

    /**
     * Tests whether the files iterator works correctly when only one normal file is specified.
     * @throws IOException 
     */
    @Test
    public void testFindsOne() throws IOException
    {
        File f = File.createTempFile("FITest", "tmp");
        f.deleteOnExit();
        Assert.assertTrue(f!=null);
        
        Iterator<File> i = new FileIterator(f);
        
        int cnt = 0;
        while (i.hasNext())
        {
            File found = i.next();
            Assert.assertTrue(f.equals(found));
            cnt ++;
        }

        Assert.assertTrue(cnt==1);
        Assert.assertTrue("could not remove temporary file", f.delete());
    }

    /**
     * Tests whether the files iterator works correctly when no file is specified.
     * @throws IOException 
     */
    @Test
    public void testFindsNone()
    {
        Assert.assertTrue(!(new FileIterator(null)).hasNext());
    }
    
}
