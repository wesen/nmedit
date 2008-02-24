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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Validates the java .class files major version number. 
 */
public class ClassFileVersionValidator
{

    public static final int MAJ_J2SE6 = 50;
    public static final int MAJ_J2SE5 = 49;
    public static final int MAJ_JDK14 = 48;
    public static final int MAJ_JDK13 = 47;
    public static final int MAJ_JDK12 = 46;

    public static final String MAJ_J2SE6_NAME = "J2SE_6.0";
    public static final String MAJ_J2SE5_NAME = "J2SE_5.0";
    public static final String MAJ_JDK14_NAME = "JDK_1.4";
    public static final String MAJ_JDK13_NAME = "JDK_1.3";
    public static final String MAJ_JDK12_NAME = "JDK_1.2";

    public static final String majorVersionToString(int majorVersion)
    {
        switch (majorVersion)
        {
            case MAJ_J2SE6: return "J2SE 6.0";
            case MAJ_J2SE5: return "J2SE 5.0";
            case MAJ_JDK14: return "JDK 1.4";
            case MAJ_JDK13: return "JDK 1.3";
            case MAJ_JDK12: return "JDK 1.2";
            default: return null;
        }
    }
    
    public static final int stringToMajorVersion(String version)
    {
        if (version.equals(MAJ_J2SE6_NAME))
            return MAJ_J2SE6;
        if (version.equals(MAJ_J2SE5_NAME))
            return MAJ_J2SE5;
        if (version.equals(MAJ_JDK14_NAME))
            return MAJ_JDK14;
        if (version.equals(MAJ_JDK13_NAME))
            return MAJ_JDK13;
        if (version.equals(MAJ_JDK12_NAME))
            return MAJ_JDK12;
        return -1;
    }
    
    public static class ClassFileInfo
    {
        String name;
        int minorVersion;
        int majorVersion;
        
        public ClassFileInfo(String name, int minorVersion, int majorVersion)
        {
            this.name = name;
            this.minorVersion = minorVersion;
            this.majorVersion = majorVersion;
        }
        
        public String toString()
        {
            return name + " major:" + majorVersion +" ("+ majorName() + ") minor:"+minorVersion;
        }

        private String majorName()
        {
            String name =  majorVersionToString(majorVersion);
            if (name == null)
            {
                name = "undefined";
            }
            return name;
        }
    }
    
    public static ClassFileInfo getInfo(String name, InputStream in) throws IOException
    {
        
        byte[] b = new byte[8];
        
        byte[] magick = new byte[] {(byte)0xCA, (byte)0xFE, (byte)0xBA, (byte)0xBE};

        in.read(b, 0, magick.length);
        
        for (int i=0;i<magick.length;i++)
            if (magick[i]!=b[i])
                return null; // magick number wrong => not a class file

        // byte 4-5: minor version number of the class file format being used
        // byte 6-7: major version number of the class file format being used. J2SE 6.0=50, J2SE 5.0=49, JDK 1.4=48, JDK 1.3=47, JDK 1.2=46. For details of earlier version numbers see footnote 1 at The JavaTM Virtual Machine Specification 2nd edition


        in.read(b, magick.length, 4);
        
        int minor = (b(b[4])<<8)|b(b[5]);
        int major = (b(b[6])<<8)|b(b[7]);
        
        return new ClassFileInfo(name, minor, major);
    }
    
    private static int b(byte b)
    {
        return b&0xFF;
    }
    
    private static int maxVersionId; 
    
    private static int classesCount = 0;
    private static int jarCount = 0;
    
    public static void main(String[] args) throws IOException
    {
        if (args.length == 1 && args[0].equals("-help"))
        {
            System.out.println("usage: -version <version> [<file>|<directory>]+");
            System.out.println("<version>-strings: "
                    +MAJ_J2SE6_NAME+", "
                    +MAJ_J2SE5_NAME+", "
                    +MAJ_JDK14_NAME+", "
                    +MAJ_JDK13_NAME+", "
                    +MAJ_JDK12_NAME);
            System.exit(0);
        }
        
        if (args.length<3)
        {
            System.err.println("missing arguments");
            System.exit(1);
        }
        
        if (!args[0].equals("-version"))
        {
            System.err.println("'-version' not specified");
            System.exit(1);
        }
        
        String version = args[1].toUpperCase();
        maxVersionId = stringToMajorVersion(version);
        if (maxVersionId<0)
        {
            System.err.println("unknown version string: "+args[1]);
            System.exit(1);
        }
        
        System.out.println("Checking java .class files major version: <="+majorVersionToString(maxVersionId));
        
        try
        {
            for (int i=2;i<args.length;i++)
                validate(args[i]);
        }
        catch (FileNotFoundException e)
        {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        catch (VersionError e)
        {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        System.out.println("Done.");
        System.out.println(".classes-files: "+classesCount);
        System.out.println(".jar-files: "+jarCount);
        System.out.println("All class files are "+majorVersionToString(maxVersionId)+" compliant.");
        System.out.println("No nested jar files found.");
    }

    private static void validate(String fileName) throws IOException, VersionError
    {
        File file = new File(fileName);
        
        if (!file.exists())
            throw new FileNotFoundException("file not found: "+fileName);
        
        if (file.isDirectory())
            validateDir(file);
        else if (file.isFile())
            validateFile(file);
    }

    private static void validateDir(File file) throws IOException, VersionError
    {
        boolean first = false;
        for (File f: file.listFiles())
        {
            if (f.isFile())
            {
                String n = file.getName();
                if (first && (n.endsWith(".class")||n.endsWith(".jar")))
                {
                    System.out.println(file.getAbsolutePath());
                    first = false;
                }
                validateFile(f);
            }
            else
                validateDir(f);
        }
        
    }
    
    
    private static void validateJar(File f) throws IOException, VersionError
    {   
        System.out.println("jar: "+f.getAbsolutePath());
        JarFile jar = new JarFile(f);
        validateJar(jar);
    }
    
    // TODO check jar files inside jar files
    private static void validateJar(JarFile jar) throws IOException, VersionError
    {
        jarCount++;
        for (Enumeration<JarEntry> en=jar.entries();en.hasMoreElements();)
        {
            JarEntry e = en.nextElement();
            if (e.getName().endsWith(".class"))
            {
                InputStream in = jar.getInputStream(e);
                try
                {
                    validateFile(e.getName(), in);
                }
                finally
                {
                    in.close();
                }
            }
            else if (e.getName().endsWith(".jar"))
            {
                throw new RuntimeException("not implemented: checking nested jar files");
            }
        }
    }
    private static void validateFile(String name, InputStream in) throws IOException, VersionError
    {
        ClassFileInfo info = getInfo(name, in);
        
        if (info == null)
        {
            System.out.println("not a class file: "+name+" (magick number wrong or missing)");
        }
        else
        {
            classesCount ++;
            // System.out.println(info);
            if (info.majorVersion>maxVersionId)
                throw new VersionError("invalid major version: "+info);
            
        }
    }

    private static boolean validateFile(File file) throws IOException, VersionError
    {
        String n = file.getName();
        
        if (n.toLowerCase().endsWith(".jar"))
        {
            validateJar(file);
            return false;
        }
        if (!n.endsWith(".class"))
            return false;
        
        InputStream in = new FileInputStream(file);
        try
        {
            validateFile(file.getAbsolutePath(), in);
            return true;
        }
        finally
        {
            in.close();
        }
    }
    
    private static class VersionError extends Exception
    {
        /**
         * 
         */
        private static final long serialVersionUID = -8031013124643544471L;

        public VersionError(String message)
        {
            super(message);
        }
    }
    
}
