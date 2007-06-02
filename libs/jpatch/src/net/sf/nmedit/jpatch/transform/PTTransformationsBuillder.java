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
package net.sf.nmedit.jpatch.transform;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import net.sf.nmedit.jpatch.ModuleDescriptions;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Creates the {@link PTTransformations} from a XML Transformations v1.1 file.
 * 
 * @author Christian Schneider
 */
public class PTTransformationsBuillder
{

    /**
     * Creates the {@link PTTransformations} from a XML Transformations v1.1 file.
     * @param is the xml file source
     * @param moduleDescriptions the module descriptors
     * @return the transformations
     */
    public static PTTransformations build(InputSource is, ModuleDescriptions moduleDescriptions) 
        throws ParserConfigurationException, SAXException, IOException 
    {

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        DocumentHandler handler = new DocumentHandler(moduleDescriptions);
       
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

        return new PTBasicTransformations(handler.builder.getSelectors());
    }

        private static class DocumentHandler extends DefaultHandler
        {
            // !!! order is important
            static final int eltransformations = 0;
            static final int elgroup = 1;
            static final int elmodule = 2;
            static final int elparameter = 3;
            static final int elconnector = 4;

            Locator locator = null;
            
            static String[] elements = new String[]
            {
              "transformations",
              "group",
              "module",
              "parameter",
              "connector"
            };
            
            static Map<String,Integer> elementMap = new HashMap<String,Integer>();
            
            static
            {
                configMap(elements, elementMap);
            }
            
            int currentElement = -1;
            
            PTBuilder builder;
            
            public DocumentHandler( ModuleDescriptions modules )
            {
                builder = new PTBuilder(modules);
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
                    case eltransformations:
                    {
                        String version = attributes.getValue("version");
                        if (!"1.1".equals(version))
                            throw new  SAXException("incompatible version "+version);
                    }
                    break ;
                    case elgroup:
                    {
                        builder.beginGroup();
                    }
                    break;
                    case elmodule:
                    {
                        String componentId = attributes.getValue("component-id");
                        if (componentId == null)
                            throw saxexception("attribute 'component-id' missing");
                        builder.beginModule(componentId);
                    }
                    break;
                    case elparameter:
                    {
                        String componentId = attributes.getValue("component-id");  
                        if (componentId == null)
                            throw saxexception("attribute 'component-id' missing");
                        String selector = attributes.getValue("selector");
                        if (selector == null)
                            throw saxexception("attribute 'selector' missing");
                        
                        builder.parameter(componentId, selector);
                    }
                    break;
                    case elconnector:
                    {
                        String componentId = attributes.getValue("component-id");  
                        if (componentId == null)
                            throw saxexception("attribute 'component-id' missing");
                        String selector = attributes.getValue("selector");
                        if (selector == null)
                            throw saxexception("attribute 'selector' missing");
                        
                        builder.connector(componentId, selector);
                    }
                    break;
                    default: throw saxexception("unknown element "+qName);
                }
            }

            public void endElement (String uri, String localName, String qName)
            throws SAXException
            {
                switch (getElementId(qName))
                {
                    case eltransformations:
                        builder.done();
                        break;
                    case elparameter:
                        break;
                    case elconnector:
                        break;
                    case elgroup:
                        builder.endGroup();
                        break;
                    case elmodule:
                        builder.endModule();
                        break;
                    default: throw saxexception("unknown element "+qName);
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
                // no op
            }
            
            public void ignorableWhitespace (char ch[], int start, int length)
            throws SAXException
            {
                // no op
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
