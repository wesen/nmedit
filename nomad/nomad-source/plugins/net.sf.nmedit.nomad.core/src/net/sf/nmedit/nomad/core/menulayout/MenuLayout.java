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
package net.sf.nmedit.nomad.core.menulayout;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.nmedit.nomad.core.xml.ApplicationXMLReaderFactory;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class MenuLayout implements Iterable<MLEntry>
{

    private MLEntry root;
    private Map<String, MLEntry> entryMap = null;

    public MenuLayout(MLEntry root)
    {
        this.root = root;
    }
    
    public Iterator<MLEntry> entries()
    {
        return getRoot().bfsIterator();
    }
    
    public Iterator<MLEntry> iterator()
    {
        return entries();
    }

    public MLEntry getRoot()
    {
        return root;
    }

    public MLEntry getEntry(String key)
    {
        assureMapCreated();
        return entryMap.get(key);
    }
    
    public int size()
    {
        assureMapCreated();
        return entryMap.size();
    }
    
    private void assureMapCreated()
    {
        if (entryMap == null)
            entryMap = createEntryMap();
    }
    
    private Map<String, MLEntry> createEntryMap()
    {
        Map<String, MLEntry> map = new HashMap<String, MLEntry>();
        
        Iterator<MLEntry> i = getRoot().bfsIterator();
        while (i.hasNext())
        {
            MLEntry e = i.next();
            map.put(e.getGlobalEntryPoint(), e);
        }
        
        return map;
    }

    public static MenuLayout getLayout(InputStream in) throws Exception
    {
        XMLReader xmlReader = ApplicationXMLReaderFactory.createXMLReader();
        
        InputSource is = new InputSource(in);
      
        MLContentHandler ch = new MLContentHandler();
        xmlReader.setContentHandler(ch);
        xmlReader.parse(is);

        return new MenuLayout(ch.getRoot());
    }
    
    private static class MLContentHandler implements ContentHandler
    {
        
        private static final Map<String, Integer> toInt = new HashMap<String, Integer>();
        
        private MLEntry currentEntry = null;
        private MLEntry root = null;

        public final static int ID_ENTRY = 1;
        public final static int ID_ICON = 2;
        public final static int ID_ATT_SRC = 3;
        public final static int ID_ATT_TYPE = 4;
        public final static int ID_ATT_ENTRYPOINT = 5;
        public final static int ID_ATT_FLAT = 5;
        public final static int ID_VAL_ENABLEDICON = 6;
        public final static int ID_VAL_DISABLEDICON = 7;
        public final static int ID_VAL_TRUE = 8;
        public final static int ID_VAL_FALSE = 9;

        static {
            toInt.put("entry", ID_ENTRY);
            toInt.put("icon", ID_ICON);
            toInt.put("src", ID_ATT_SRC);
            toInt.put("type", ID_ATT_TYPE);
            toInt.put("flat", ID_ATT_FLAT);
            toInt.put("entryPoint", ID_ATT_ENTRYPOINT);
            toInt.put("enabledIcon", ID_VAL_ENABLEDICON);
            toInt.put("disabledIcon", ID_VAL_DISABLEDICON);
            toInt.put("true", ID_VAL_TRUE);
            toInt.put("1", ID_VAL_TRUE);
            toInt.put("false", ID_VAL_FALSE);
            toInt.put("0", ID_VAL_FALSE);
        }

        public MLEntry getRoot()
        {
            return root;
        }
        
        private int choice(String key)
        {
            Integer id = toInt.get(key);
            return id == null ? 0 : id.intValue();
        }
        
        public void setDocumentLocator( Locator locator )
        { }

        public void startDocument() throws SAXException
        { }

        public void endDocument() throws SAXException
        { }

        public void startPrefixMapping( String prefix, String uri ) throws SAXException
        { }

        public void endPrefixMapping( String prefix ) throws SAXException
        { }

        public void startElement( String uri, String localName, String qName, Attributes atts ) throws SAXException
        {
            switch (choice(localName)) // localName == qName
            {
                case ID_ENTRY:
                    
                    MLEntry parent = currentEntry;
                    currentEntry = new MLEntry(atts.getValue("entryPoint"));
                    if (root == null)
                        root = currentEntry;
                    else
                        parent.add(currentEntry);
                    
                    if (choice(atts.getValue("flat"))==ID_VAL_TRUE)
                        currentEntry.setIsFlat(true);
                    
                    break;
                case ID_ICON:
                    
                    boolean isEnabledIconSrc = choice(atts.getValue("type")) == ID_VAL_ENABLEDICON;
                    String src = atts.getValue("src");
                    
                    if (isEnabledIconSrc)
                        currentEntry.setEnabledIconSrc(src);
                    else
                        currentEntry.setDisabledIconSrc(src);

                    break;
                    
            }
        }

        public void endElement( String uri, String localName, String qName ) throws SAXException
        {
            if (choice(localName) == ID_ENTRY)
            {
                currentEntry = currentEntry.getParent();
            }
        }

        public void characters( char[] ch, int start, int length ) throws SAXException
        { }

        public void ignorableWhitespace( char[] ch, int start, int length ) throws SAXException
        { }

        public void processingInstruction( String target, String data ) throws SAXException
        { }

        public void skippedEntity( String name ) throws SAXException
        { }
        
    }
    
    public static void disableGhosts(MenuLayout layout)
    {
        for (MLEntry e : layout)
        {
            if (e.getListenerCount() == 0 && e.size() == 0)
            {
                e.setEnabled(false);
            }
        }
    }
    
}
