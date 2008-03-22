/* Copyright (C) 2008 Christian Schneider
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
package net.sf.nmedit.jpatch;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Christian Schneider
 */
public class PModuleDocumentationParser
{

    public static Map<String, String> build(InputSource is) 
        throws ParserConfigurationException, SAXException, IOException 
    {

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        
        Map<String, String> documentation = new HashMap<String, String>();
        DocumentHandler handler = new DocumentHandler(documentation);
       
        try
        {
            parser.parse(is, handler);
        }
        catch (SAXException e)
        {
            Locator l = handler.locator;
            if (l == null)
                throw e;
            
            SAXException se = new SAXException("error in line:col="+l.getLineNumber()+":"+l.getColumnNumber());
            se.initCause(e);
            
            throw se;
        }

        return documentation;
    }

        private static class DocumentHandler extends DefaultHandler
        {
            // !!! order is important
            static final int elroot = 0;
            static final int elmodule = 1;

            Locator locator = null;
            
            static String[] elements = new String[]
            {
              "module-documentation",
              "module"
            };
            
            static Map<String,Integer> elementMap = new HashMap<String,Integer>();
            
            static
            {
                configMap(elements, elementMap);
            }
            
            int currentElement = -1;
            
            Map<String, String> documentation;
            String currentId;
            StringBuilder currentDoc = new StringBuilder();
            
            public DocumentHandler( Map<String, String> documentation )
            {
                this.documentation = documentation;
            }

            private static void configMap( String[] names, Map<String, Integer> map )
            {
                for (int i=0;i<names.length;i++)
                    map.put(names[i], i);
            }
            
            public final int getElementId(String element)
            {
                return getIdFromMap(element, elementMap);
            }
            
            public final int getIdFromMap(String name, Map<String,Integer> map)
            {
                Integer id = map.get(name);
                return id == null ? -1 : id.intValue();
            }
                                                  
            public void startDocument ()
            throws SAXException
            {
                // no op
            }

            public void endDocument ()
            throws SAXException
            {
                // no op
            }

            public InputSource resolveEntity (String publicId, String systemId)
            throws IOException, SAXException
            {
                return null;
            }
            
            public void notationDecl (String name, String publicId, String systemId)
            throws SAXException
            {
                // no op
            }
            
            public void unparsedEntityDecl (String name, String publicId,
                            String systemId, String notationName)
            throws SAXException
            {
                // no op
            }
            
            private SAXException saxexception(String message, Exception t)
            {
                message = "@"+locator.getLineNumber()
                +":"+locator.getColumnNumber()+": "
                +message;
                
                if (t == null)
                    return new SAXException(message);
                else
                    return new SAXException(message, t);
            }
            
            private SAXException saxexception(String message) 
            {
                return saxexception(message, null);
            }
            
            public void startElement (String uri, String localName,
                          String qName, Attributes attributes)
            throws SAXException
            {
                switch (getElementId(qName))
                {
                    case elroot:
                        break;
                    case elmodule:
                    {
                        if (currentId != null)
                            throw saxexception("invalid element <module/>");
                        
                        String componentId = attributes.getValue("component-id");
                        if (componentId == null)
                            throw saxexception("attribute 'component-id' missing");
                        currentId = componentId;
                        currentDoc.setLength(0);
                        break;
                    }
                    default:
                    {
                        currentDoc.append("<"+qName);
                        for (int i=0;i<attributes.getLength();i++)
                        {
                            currentDoc.append(" "+attributes.getQName(i)+"=\""+
                                    attributes.getValue(i)+"\"");
                        }
                        currentDoc.append(">");
                        break;
                    }
                }
            }

            public void endElement (String uri, String localName, String qName)
            throws SAXException
            {
                switch (getElementId(qName))
                {
                    case elroot:
                        break;
                    case elmodule:
                        documentation.put(currentId, currentDoc.toString());
                        currentId = null;
                        currentDoc.setLength(0);
                        break;
                    default:
                    {
                        currentDoc.append("</"+qName+">");
                        break;
                    }
                }
            }
            
            public void setDocumentLocator (Locator locator)
            {
                this.locator = locator;
            }

            public void startPrefixMapping (String prefix, String uri)
            throws SAXException
            {
                // no op
            }

            public void endPrefixMapping (String prefix)
            throws SAXException
            {
                // no op
            }
            
            public void characters (char ch[], int start, int length)
            throws SAXException
            {
                maybeText(ch, start, length);
            }

            public void ignorableWhitespace (char ch[], int start, int length)
            throws SAXException
            {
                maybeText(ch, start, length);
            }
            
            public void processingInstruction (String target, String data)
            throws SAXException
            {
                // no op
            }

            public void skippedEntity (String name)
            throws SAXException
            {
                // no op
            }
            
            private void maybeText(char[] ch, int start, int length)
            {
                if (currentId != null)
                    currentDoc.append(ch, start, length);
            }
            
            /*
            public void warning (SAXParseException e)
            throws SAXException
            {
                super.warning(e);
            }
            
            public void error (SAXParseException e)
            throws SAXException
            {
                super.error(e);
            }
            
            public void fatalError (SAXParseException e)
            throws SAXException
            {
                super.fatalError(e);
            }*/

        }
    
}
