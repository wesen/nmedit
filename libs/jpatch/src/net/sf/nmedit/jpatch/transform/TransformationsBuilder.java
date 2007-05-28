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
import net.sf.nmedit.jpatch.PConnectorDescriptor;
import net.sf.nmedit.jpatch.PModuleDescriptor;
import net.sf.nmedit.jpatch.PParameterDescriptor;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class TransformationsBuilder
{
    

    public static PTTransformations build(InputSource is, ModuleDescriptions moduleDescriptions) 
        throws ParserConfigurationException, SAXException, IOException
    {

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        
        PTTransformations transformations = new PTTransformations();
        
        DocumentHandler handler = new DocumentHandler(moduleDescriptions, transformations);
        /*
        try
        {*/
            parser.parse(is, handler);
        /*}
        catch (SAXException e)
        {
            Locator l = handler.locator;
            if (l == null)
                throw e;
            
            SAXException se = new SAXException("error in line:col="+l.getLineNumber()+":"+l.getColumnNumber());
            se.initCause(e);
            
            throw se;
        }*/
        
        return transformations;
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
            
            public DocumentHandler( ModuleDescriptions descriptions, PTTransformations transformations )
            {
                this.moduleDescriptions = descriptions;
                this.transformations = transformations;
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
            
            private PTGroup currentGroup;
            private PTModule currentModule;

            private ModuleDescriptions moduleDescriptions ;
            private PTTransformations transformations;
            
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
            
            private int integer(Attributes a, String attName) throws SAXException
            {
                String v = a.getValue(attName);
                if (v == null)
                    throw saxexception("attribute '"+attName+"' missing");
                
                try
                {
                    return Integer.parseInt(v);
                }
                catch (NumberFormatException e)
                {
                    throw saxexception("attribute '"+attName+"'", e);
                }
            }

            public void startElement (String uri, String localName,
                          String qName, Attributes attributes)
            throws SAXException
            {
                final int eid = getElementId(qName);
                
                if (eid < 0)
                    throw saxexception("unknown element "+qName);
                
                switch (eid)
                {
                    case eltransformations:
                    {
                        String version = attributes.getValue("version");
                        if (!"1.0".equals(version))
                            throw new  SAXException("incompatible version "+version);
                    }
                    break ;
                    case elgroup:
                    {
                        if (currentGroup == null)
                            currentGroup = new PTGroup();
                    }
                    break;
                    case elmodule:
                    {
                        String componentid = attributes.getValue("component-id");
                        PModuleDescriptor md = moduleDescriptions.getModuleById(componentid);
                        if (md == null)
                            throw saxexception("module [component-id="+componentid+"] does not exist");
                        
                        currentModule = new PTModule(md);
                        currentGroup.add(currentModule);
                    }
                    break;
                    case elparameter:
                    {
                        if (currentModule == null)
                            throw saxexception("no module associated with parameter");
                        
                        String componentid = attributes.getValue("component-id");                     
                        String id = attributes.getValue("variable");
                        if (componentid == null) throw saxexception("attribute 'component-id' missing");   
                        if (id == null) throw saxexception("attribute 'variable' missing");
                        
                        PModuleDescriptor md = currentModule.getTarget();
                        
                        PParameterDescriptor pd = md.getParameterByComponentId(componentid);
                        
                        if (pd == null)
                            throw saxexception("parameter [component-id="+componentid+"]" +
                                    " not found in "+currentModule.getTarget());
                                
                        currentModule.add(new PTTransformable<PParameterDescriptor>(id, pd));
                    }
                    break;
                    case elconnector:
                    {
                        if (currentModule == null)
                            throw saxexception("no module associated with connector");


                        String componentid = attributes.getValue("component-id");                     
                        String id = attributes.getValue("variable");
                        if (componentid == null) throw saxexception("attribute 'component-id' missing");   
                        if (id == null) throw saxexception("attribute 'variable' missing");
                        PModuleDescriptor md = currentModule.getTarget();
                        PConnectorDescriptor cd = md.getConnectorByComponentId(componentid);

                        if (cd == null)
                            throw saxexception("connector [component-id="+componentid+"]" +
                                    " not found in "+currentModule.getTarget());
                        currentModule.add(new PTTransformable<PConnectorDescriptor>(id, cd));
                    }
                    break;
                }
                
                currentElement = eid;
            }

            public void endElement (String uri, String localName, String qName)
            throws SAXException
            {

                final int eid = getElementId(qName);
                
                switch (eid)
                {
                    case elparameter:
                    case elconnector:
                    break;
                    case elgroup:
                    {
                        if (currentGroup != null)
                        {
                            transformations.add(currentGroup);
                            currentGroup = null;
                        }
                    }
                    break;
                    case elmodule:
                    {
                        currentModule = null;
                    }
                    break;
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
