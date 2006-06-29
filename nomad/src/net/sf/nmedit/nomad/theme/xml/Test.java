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
 * Created on Jun 16, 2006
 */
package net.sf.nmedit.nomad.theme.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import net.sf.nmedit.jmisc.xml.builder.DocumentHandler;
import net.sf.nmedit.jmisc.xml.builder.XMLProcessingException;
import net.sf.nmedit.nomad.theme.xml.dom.ComponentNode;
import net.sf.nmedit.nomad.theme.xml.dom.ModuleNode;
import net.sf.nmedit.nomad.theme.xml.dom.ThemeNode;

import org.xmlpull.v1.XmlPullParserException;

public class Test
{

    public static void main(String[] args) 
        throws FileNotFoundException, XmlPullParserException, XMLProcessingException, IOException
    {
        ThemeNode theme = new ThemeNode();
        
        DocumentHandler builder = new ThemeBuilder(theme);
        
        //String abs = (new File("")).getAbsolutePath()+"/src/net/sf/nmedit/nomad";
        
        File f = new File("src/plugin/classictheme/theme.xml");
        long time = System.currentTimeMillis();
        builder.processDocument(f);
        
        time = System.currentTimeMillis()-time; 
        
        System.out.println("success (time:"+(time/1000.0d)+"s)");
        if(false)
        for (ModuleNode mn : theme)
        {
            System.out.println("<module id=\""+mn.getId()+"\">");
            for (ComponentNode cn: mn)
            {
                System.out.println("<component name=\""+cn.getName()+"\">");
                for (int i=0;i<cn.getPropertyCount();i++)
                {
                    String name = cn.getPropertyName(i);
                    System.out.println(name+"="+cn.getProperty(name));
                }
                System.out.println("</component>");
            }
            System.out.println("</module>");
        }
    }

}
