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
 * Created on Apr 6, 2006
 */
package net.sf.nmedit.patch.test;

import java.io.File;
import java.io.FileReader;

import net.sf.nmedit.patch.FactoryException;
import net.sf.nmedit.patch.parser.PatchParser;
import net.sf.nmedit.patch.parser.PatchParserFactory;

public class FileParserTest
{

    public static void main( String[] args ) throws FactoryException
    {
        System.out.println( "Testing File Parser" );

        PatchParserFactory factory = PatchParserFactory.newInstance();

        factory.setValidationEnabled( true );
        factory.setParser( PatchParserFactory.DEFAULT_FILE_PARSER );

        PatchParser parser = factory.newPatchParser();

        test( parser, "src/data/patch/" );
    }
    
    private static void test( PatchParser parser, String file )
    {
        
        File f = new File(file);
        
        if (f.isFile())
        {
            test(parser, f);
        }
        else
        {
            File[] files = f.listFiles();
            for (int i=0;i<files.length;i++)
            {
                test(parser, files[i]);
            }
        }
    }
    
    private static void test( PatchParser parser, File file ) 
    {
        System.out.print(file);
        try
        {
            FileReader reader = new FileReader( file );
            try
            {
                parser.setSource( reader );
                while ( parser.nextToken() >= 0);
            }
            finally
            {
                reader.close();
            }
            System.out.println(" [Ok]");
        }
        catch (Exception e) 
        {
            System.out.println(" [Failed]");
            System.err.println();
            e.printStackTrace();
        }
    }

}
