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
 * Created on Sep 9, 2006
 */
package net.sf.nmedit.jtheme.dom;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;

import net.sf.nmedit.jmisc.xml.XMLFileWriter;

public class ThemeDom extends HashMap<Integer,ContainerNode>
{

    public static void importDocument(ThemeDom dom, String file) 
    {
        ThemeBuilder builder = new ThemeBuilder(dom);
        try
        {
            builder.processDocument(new File(file));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void importDocument(ThemeDom dom, InputStream in) 
    {
        ThemeBuilder builder = new ThemeBuilder(dom);
        try
        {
            builder.processDocument(in);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public static void exportDocument(XMLFileWriter out, ThemeDom dom)
    {

        out.beginTag( "theme", true );

        for (ContainerNode mod : dom.values())
        {
            out.beginTagStart( "module" );
            out.addAttribute( "id", "" + mod.getID() );
            out.beginTagFinish( true );

            for (ComponentNode compNode : mod)
            {
                out.beginTagStart( "component" );
                out.addAttribute( "name", compNode.getComponentName() ); // TODO use
                // associations
                // i.e.
                // 'button',
                // 'knob'
                out.beginTagFinish( true );

                for (PropertyNode pn : compNode.values())
                {
                    out.beginTagStart( "property" );
                    out.addAttribute( "name", pn.getPropertyName() );
                    out.addAttribute( "value", pn.getPropertyValue() );
                    out.beginTagFinish( false );
                }

                out.endTag();
            }
            out.endTag();
        }
        out.endTag();
    }

    public void exportDocument( XMLFileWriter xml )
    {
        exportDocument(xml, this);
    }

}
