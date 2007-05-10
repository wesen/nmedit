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
package net.sf.nmedit.jprotocol.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import net.sf.nmedit.jprotocol.PRMessageDescriptor;
import net.sf.nmedit.jprotocol.PRMessages;
import net.sf.nmedit.jprotocol.PRParameterDescriptor;
import net.sf.nmedit.jprotocol.PRParseException;
import net.sf.nmedit.jprotocol.PRParser;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class PRParserImpl implements PRParser
{

    public PRParserImpl()
    {
        
    }

    public PRMessages parseMessages(InputSource is) throws PRParseException, IOException
    {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser;
        try
        {
            parser = factory.newSAXParser();
        }
        catch (Exception e)
        {
            throw new PRParseException(e);
        }
        
        PRMessagesImpl messages = new PRMessagesImpl();
        DocumentHandler handler = new DocumentHandler(messages);
        
        try
        {
            parser.parse(is, handler);
            return messages;
        }
        catch (SAXException e)
        {
            Locator l = handler.locator;
            if (l == null)
                throw new PRParseException(e);
            
            SAXException se = new SAXException("error in line:col="+l.getLineNumber()+":"+l.getColumnNumber());
            se.initCause(e);
            
            throw new PRParseException(se);
        }
    }
    
    private static class DocumentHandler extends DefaultHandler
    {
        
        private PRMessagesImpl messages;
        private PRMessageDescriptorImpl message;
        private Locator locator;
        
        private static Map<String, Integer> elementIdMap;
        private static final int EL_MESSAGES = 0;
        private static final int EL_MESSAGE = 1;
        private static final int EL_PARAMETER = 2;
        private int currentElement = -1;
        
        static
        {
            elementIdMap = new HashMap<String, Integer>();
            elementIdMap.put("messages", EL_MESSAGES);
            elementIdMap.put("message", EL_MESSAGE);
            elementIdMap.put("parameter", EL_PARAMETER);
        }
        
        protected int getElementId(String name)
        {
            Integer id = elementIdMap.get(name);
            return id == null ? -1 : id.intValue();
        }

        public DocumentHandler(PRMessagesImpl messages)
        {
            this.messages = messages;
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

        public void startElement (String uri, String localName,
                      String qName, Attributes attributes)
        throws SAXException
        {
            final int eid = getElementId(qName);
            if (eid < 0)
                throw new SAXException("unknown element "+qName);
            
            switch (eid)
            {
                case EL_MESSAGES:
                    {   
                        String version = attributes.getValue("version");
                        if (!"1.0".equals(version))
                            throw new  SAXException("incompatible version "+version);
                    }
                    break ;
                case EL_MESSAGE:
                    {
                        String name = attributes.getValue("name");
                        int id = Integer.parseInt(attributes.getValue("message-id"));
                        message = new PRMessageDescriptorImpl(id, name);
                        messages.add(message);
                    }
                    break;
                case EL_PARAMETER:
                    {
                        String name = attributes.getValue("name");
                        String path = attributes.getValue("path");
                        String defaultValueString = attributes.getValue("default");

                        if (name == null) throw new SAXException("parameter name missing");
                        if (path == null) throw new SAXException("parameter path missing");
                        
                        PRParameterDescriptor parameter;
                        
                        if (defaultValueString != null)
                        {
                            int value = parseDefaultValue(defaultValueString);
                            parameter = new PRParameterDescriptorImpl(name, path, value);
                        }
                        else
                        {
                            parameter = new PRParameterDescriptorImpl(name, path);
                        }
                        message.addParameter(parameter);
                    }
                    break;
            }
        }
        
        private int hex2int(char c)
        {
            if ('0'<=c && c<='9')
                return c-'0';
            c = Character.toLowerCase(c);
            if ('a'<=c && c<='f')
                return c-'a'+10;
            else
                throw new NumberFormatException("invalid hexadecimal character: "+c);
        }

        private int parseDefaultValue(String s)
        {
            if (s.startsWith("0x"))
            {
                if (s.length()==2)
                    throw new NumberFormatException("invalid hexadecimal number: "+s);
                
                int value = 0;
                
                for (int i=2;i<s.length();i++)
                    value = (value*16)+hex2int(s.charAt(i));
                
                return value;
            }
            else
            {
                return Integer.parseInt(s);
            }
        }

        public void endElement (String uri, String localName, String qName)
        throws SAXException
        {
            if (getElementId(qName)==EL_MESSAGE)
                message = null;
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

    public static void main(String[] args)
        throws Exception
    {
        PRParser parser = new PRParserImpl();
        
        InputStream in = new FileInputStream("/home/christian/CVS-Arbeitsbereich/nmedit/libs/jnmprotocol/src/net/sf/nmedit/jnmprotocol3/dom/example.messages.xml");
        InputSource is = new InputSource(in);
        PRMessages messages = parser.parseMessages(is);
        
        for (PRMessageDescriptor m:messages.getMessages())
        {
            System.out.println("message "+m+":");
            for (PRParameterDescriptor p: m.getParameters())
            {
                System.out.println("\t"+p);
            }
        }
        System.out.println("done");
        
        PRMessageFactoryImpl f = new PRMessageFactoryImpl(messages);   
        System.out.println(f.createMessage(1));
        
    }

}
