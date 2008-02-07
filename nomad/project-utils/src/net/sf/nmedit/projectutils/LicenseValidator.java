/* Copyright (C) 2007 Christian Schneider
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
package net.sf.nmedit.projectutils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Validates that the .java files license statement exist. 
 */
public class LicenseValidator
{
    
    private static Set<File> notFiles = new HashSet<File>();
    
    public static void main(String[] args) throws IOException
    {
        System.out.println("Validating .java source license statements.");
        // add not files
        boolean not = false;
        for (String name: args)
        {
            if (not)
            {
                File notfile = new File(name);
                System.out.println("Skipping: "+notfile.getAbsolutePath());
                if (notfile.exists())
                {
                    notFiles.add(notfile);
                }
                else
                {
                    System.err.println("excluded file/directory does not exist: "+notfile.getAbsolutePath());
                }
                not = false;
            }
            else not = "-not".equals(name);
        }
        not = false;
        for (String name: args)
        {
            if (not) 
            {
                not = false;
                continue;
            }

            if ("-not".equals(name))
            {
                not = true;
                continue;
            }
            
            File f = new File(name);
            
            if (skip(f))
            {
                continue;
            }
            
            if (!f.exists())
            {
                System.err.println("File not found: "+name);
                System.exit(1);
            }
            if (f.isDirectory())
            {
                System.out.println("******************************************");
                System.out.println("processing directory: "+f.getAbsolutePath());
                validateDir(f);
            }
            else
            {
                validateFile(f);
            }
        }

        System.err.println("Done.");
        System.out.println("directories: "+dirCount);
        System.out.println(".java-files: "+javaFileCount);
        System.out.println("missing license statements: "+missingLicenseCount);
        
        if (missingLicenseCount>0)
        {
            System.err.println("Validation failed, some .java-files have no license statement.");
            System.exit(1);
        }
    }

    private static boolean skip(File f)
    {
        return notFiles.contains(f);
    }

    private static int javaFileCount = 0;
    private static int missingLicenseCount = 0;
    private static int dirCount = 0;

    private static void validateDir(File d) throws IOException
    {
        if (skip(d))
            return;
        dirCount++;
        for (File f:d.listFiles())
        {
            if (f.isDirectory())
                validateDir(f);
            else validateFile(f);
        }
    }

    private static void validateFile(File f) throws IOException
    {
        if (!f.getName().toLowerCase().endsWith(".java"))
            return;
        if (skip(f))
            return;
        
        javaFileCount ++;

        String text = toString(f);
        
        if (!hasCopyrightStatement(text))
        {
            System.out.println("license missing: "+f.getAbsolutePath());
            missingLicenseCount++;
        }
        
    }

    private static boolean hasCopyrightStatement(String text) throws IOException
    {
        return text.contains("Copyright")
        && text.contains("GNU General Public License");
    }
    
    private static String toString(File f) throws IOException
    {
        BufferedReader in = new BufferedReader(new FileReader(f));
        
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = in.readLine())!=null)
        {
            sb.append(line);
            sb.append("\n");
        }
        
        return sb.toString();
    }
    
}
