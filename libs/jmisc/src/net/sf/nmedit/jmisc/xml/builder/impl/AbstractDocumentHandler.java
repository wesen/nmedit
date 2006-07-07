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
package net.sf.nmedit.jmisc.xml.builder.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import net.sf.nmedit.jmisc.xml.builder.DocumentHandler;
import net.sf.nmedit.jmisc.xml.builder.ElementHandler;
import net.sf.nmedit.jmisc.xml.builder.XMLProcessingException;

import org.xmlpull.mxp1.MXParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public abstract class AbstractDocumentHandler implements DocumentHandler
{
    
    private ElementHandler location = null;

    public void processDocument( XmlPullParser parser )
        throws XmlPullParserException, IOException, XMLProcessingException
    {
        location = new Document(getRootElement());
        
        int event = parser.getEventType();
        
        do
        {    
            switch (event)
            {
                case XmlPullParser.START_DOCUMENT:
                {
                    startDocument(parser);
                    break;
                }
                case XmlPullParser.END_DOCUMENT:
                    {
                    endDocument(parser);
                    break;
                }    
                case XmlPullParser.START_TAG:
                    {
                    startTag(parser);
                    break;
                }
                case XmlPullParser.END_TAG:{
                    endTag(parser);
                    break;
                }
                case XmlPullParser.TEXT:{
                    text(parser);
                    break;
                }
            }
            
            event = parser.next();
        } 
        while (event != XmlPullParser.END_DOCUMENT);
    }
    
    protected void text( XmlPullParser parser ) throws XMLProcessingException
    { }

    protected void endTag( XmlPullParser parser ) throws XMLProcessingException
    {
        if (location == null)
        {
            // error
        }
        location = location.getParent();
    }

    protected void startTag( XmlPullParser parser ) throws XMLProcessingException
    {
        ElementHandler e = location.getElementHandler(parser.getName());
        if (e==null) undefinedElement(parser);
        location = e;

        e.element(parser);
    }

    protected void endDocument( XmlPullParser parser ) throws XMLProcessingException
    { }

    protected void startDocument( XmlPullParser parser ) throws XMLProcessingException
    { }

    private void undefinedElement( XmlPullParser parser ) throws XMLProcessingException
    { 
        throw new XMLProcessingException(
          parser, "undefined element '"+parser.getName()+"'" 
        );
    }

    public void processDocument(File file) 
        throws XmlPullParserException, 
        XMLProcessingException, IOException, FileNotFoundException
    {
        XmlPullParser parser = getParser();
        parser.setInput(new BufferedReader(new FileReader(file)));
        processDocument(parser);
    }

    public void processDocument( InputStream in )
    throws XmlPullParserException, 
    XMLProcessingException, IOException, FileNotFoundException
    {
        XmlPullParser parser = getParser();
        parser.setInput(new InputStreamReader(in));
        processDocument(parser);
    }
    
    public XmlPullParser getParser() throws XmlPullParserException
    {
        /*XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(false);
        XmlPullParser parser = factory.newPullParser();*/
        MXParser parser = new MXParser();
        // unsupported
        //parser.setFeature(XmlPullParser.FEATURE_VALIDATION, true);
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
        
        return parser;
    }
    
}
