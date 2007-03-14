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
 * Created on Nov 19, 2006
 */
package net.sf.nmedit.nomad.core.xml;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class ApplicationXMLReaderFactory
{

    public final static String xmlReaderClassName = "org.apache.xerces.parsers.SAXParser";
    
    public static XMLReader createXMLReader() throws SAXException
    {
        return createXMLReader(false);
    }

    public static XMLReader createXMLReader(boolean validation) throws SAXException
    {
        Thread.currentThread().setContextClassLoader(ApplicationXMLReaderFactory.class.getClassLoader());
        
        // XMLReaderFactory not working in plugin
        XMLReader xmlReader = XMLReaderFactory.createXMLReader(xmlReaderClassName);
        xmlReader.setFeature("http://xml.org/sax/features/validation", validation);
        return xmlReader;
    }

}
