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
 * Created on Dec 10, 2006
 */
package net.sf.nmedit.jpatch;

import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import net.sf.nmedit.jpatch.PConnectorDescriptor;
import net.sf.nmedit.jpatch.PParameterDescriptor;
import net.sf.nmedit.jpatch.impl.PBasicConnectorDescriptor;
import net.sf.nmedit.jpatch.impl.PBasicDescriptor;
import net.sf.nmedit.jpatch.impl.PBasicLightDescriptor;
import net.sf.nmedit.jpatch.impl.PBasicModuleDescriptor;
import net.sf.nmedit.jpatch.impl.PBasicParameterDescriptor;
import net.sf.nmedit.jpatch.impl.PBasicRoles;
import net.sf.nmedit.jpatch.js.JSContext;
import net.sf.nmedit.jpatch.js.JSFormatter;
import net.sf.nmedit.nmutils.Hex;

import org.mozilla.javascript.Function;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class ModuleDescriptionsParser
{

    private ModuleDescriptionsParser()
    {
        super();
    }
    
    public static void parse(ModuleDescriptions descriptions, InputStream in) 
        throws ParserConfigurationException, SAXException, IOException
    {
        if (!(in instanceof BufferedInputStream))
            in = new BufferedInputStream(in);
        
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        
        DocumentHandler handler = new DocumentHandler(descriptions);
        
        try
        {
            parser.parse(in, handler);
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
    }
    private static class DocumentHandler extends DefaultHandler 
    {
        // !!! order is important
        static final int ModuleDescriptions = 0;
        static final int header = 1;
        static final int defs = 2;
        static final int annotation = 3;
        static final int body = 4;
        static final int module = 5;
        static final int comment = 6;
        static final int parameter = 7;
        static final int connector = 8;
        static final int attribute = 9;
        static final int vendor = 10;
        static final int property = 11;
        static final int device = 12;
        static final int model = 13;
        static final int version = 14;
        static final int defsignal = 15;
        static final int deftype = 16;
        static final int signal = 17;
        static final int enumeration = 18;
        static final int image = 19;
        static final int section = 20;
        static final int title = 21;
        static final int p = 22;
        static final int pre = 23;
        static final int code = 24;
        static final int list = 25;
        static final int item = 26;
        static final int link = 27;
        static final int mail = 28;
        static final int light = 29;
        static final int doc = 30;
        static final int script = 31;
        static final int documentation = 32;

        Locator locator = null;
        
        static String[] elements = new String[]
        {
          "ModuleDescriptions",
          "header",
          "defs",
          "annotation",
          "body",
          "module",
          "comment",
          "parameter",
          "connector",
          "attribute",
          "vendor",
          "property",
          "device",
          "model",
          "version",
          "def-signal",
          "def-type",
          "signal",
          "enumeration",
          "image",
          "section",
          "title",
          "p",
          "pre",
          "code",
          "list",
          "item",
          "link",
          "mail",
          "light",
          "doc",
          "script",
          "documentation"
        };
        
        static String[] attributes = new String[]
        {
            "id",
            "version",
            "url"
        };

        
        static final int attString = 0;
        static final int attBoolean = 1;
        static final int attInteger = 2;
        static final int attFloat = 3;
        static final int attDouble = 4;
        static final int attColor = 5;
        static String[] attTypes = new String[]
        {
           "string",
           "boolean",
           "integer",
           "float",
           "double",
           "color"
        };
                
        static Map<String,Integer> elementMap = new HashMap<String,Integer>();
        static Map<String,Integer> attributeMap = new HashMap<String,Integer>();
        static Map<String,Integer> attTypeMap = new HashMap<String,Integer>();
        
        static Map<String, Integer> ledTypes = new HashMap<String, Integer>();
        
        Map<PBasicParameterDescriptor, String> extensions = new HashMap<PBasicParameterDescriptor, String>();
        
        static
        {
            configMap(elements, elementMap);
            configMap(attributes, attributeMap);
            configMap(attTypes, attTypeMap);

            ledTypes.put("led", PLightDescriptor.TYPE_LED);
            ledTypes.put("led-array", PLightDescriptor.TYPE_LED_ARRAY);
            ledTypes.put("meter", PLightDescriptor.TYPE_METER);
        }
        
        private Map<String,String> strCache = new HashMap<String, String>(1000);
        
        private int instcount = 0;
        
        private String str(String stringId)
        {
            String s;
            if (stringId == null)
            {
                s = null;
            }
            else
            {
                s = strCache.get(stringId);
                if (s == null)
                    strCache.put(s = stringId, stringId);
                
             //   System.out.println("strings: "+(++instcount)+"/"+(strCache.size()));*/
            }
            return s;
        }

        private static final String INDEX = "index";
        private static final String CLASS = "class";
        private static final String TYPE = "type";
        private static final String KEY = "key";
        private static final String VALUE = "value";
        private static final String NAME = "name";
        private static final String ROLE = "role";
        private static final String SIGNAL = "signal";
        private static final String COMPONENTID = "component-id";
        
        private int getLEDType(String typeValue)
        {
            if (typeValue == null)
                return PLightDescriptor.TYPE_UNKNOWN;
            Integer t = ledTypes.get(typeValue);
            if (t == null)
                return PLightDescriptor.TYPE_UNKNOWN;
            else
                return t.intValue();
        }
        
        int currentElement = -1;
        
        public DocumentHandler( ModuleDescriptions descriptions )
        {
            this.moduleDescriptions = descriptions;
        }

        private static void configMap( String[] names, Map<String, Integer> map )
        {
            for (int i=0;i<names.length;i++)
                map.put(names[i], i);
        }
        
        public final int getAttTypeId(String element)
        {
            return getIdFromMap(element, attTypeMap);
        }
        
        public final int getElementId(String element)
        {
            return getIdFromMap(element, elementMap);
        }
        
        public final int getAttributeId(String attribute)
        {
            return getIdFromMap(attribute, attributeMap);
        }

        private PBasicModuleDescriptor moduled = null;
        private PBasicParameterDescriptor parameterd = null;
        private PBasicConnectorDescriptor connectord = null;
        private PBasicLightDescriptor lightd = null;

        private List<PParameterDescriptor> parameterList = new ArrayList<PParameterDescriptor>(100);
        private List<PConnectorDescriptor> connectorList = new ArrayList<PConnectorDescriptor>(100);
        private List<PLightDescriptor> lightList = new ArrayList<PLightDescriptor>(100);
        
        ModuleDescriptions moduleDescriptions ;
        
        PSignalTypes signalTypes;
        PSimpleTypes typedef;
        StringBuffer docText = new StringBuffer();
        private boolean docElement = false;
        
        private Map<Function, Formatter> formatterMap = new HashMap<Function, Formatter>();
        
        private transient JSContext jsc;
        
        private Formatter getFormatter(String src) throws SAXException
        {
            if (jsc == null)
                jsc = moduleDescriptions.getJSContext();
            
            Function jsFunction = jsc.getFormatterFunction(src);

            if (jsFunction == null)
                throw new SAXException("invalid function: "+src);
            
            Formatter formatter = formatterMap.get(jsFunction);
            if (formatter == null)
            {
                formatter = new JSFormatter(jsc, jsFunction); 
                formatterMap.put(jsFunction, formatter);
            }
            return formatter;
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

        public void startElement (String uri, String localName,
                      String qName, Attributes attributes)
        throws SAXException
        {
            final int eid = getElementId(qName);
            
            if (eid < 0)
                throw new SAXException("unknown element "+qName);
            
            switch (eid)
            {
                case ModuleDescriptions:
                    {
                        String version = attributes.getValue("version");
                        if (!"1.4".equals(version))
                            throw new  SAXException("incompatible version "+version);
                    }
                    break ;

                case doc:
                    docElement = true;
                    docText.setLength(0);
                    break;
                
                case documentation:
                {
                    String lang = attributes.getValue("lang");
                    String src = attributes.getValue("src");
                    moduleDescriptions.setDocumentationSrc(lang, src);
                    break;
                }
                    
                case script:
                {
                    
                    String type = attributes.getValue("type");
                    if (!"text/javascript".equals(type))
                        throw new SAXException("unsupported script type:"+type);
                    
                    String src = attributes.getValue("src");
                    
                    URL script = src == null ? null : moduleDescriptions.getResourceClassLoader().getResource(src);
                    if (script == null)
                        throw new SAXException("script not found: "+src);
                    
                    try
                    {
                        moduleDescriptions.getJSContext().addScript(script);
                    }
                    catch (IOException e)
                    {
                        throw new SAXException("error parsing script: "+src, e);
                    }
                }
                break;
                    
                case header:
                    break;
                case body:
                    break;
                case module:
                    {
                        String name = attributes.getValue(NAME);
                        if (name == null)
                            throw new SAXException("module name must not be null");
                        
                        String componentId = attributes.getValue(COMPONENTID);
                        if (componentId == null)
                            throw new SAXException("component-id missing in module: "+name);
                        
                        moduled = new PBasicModuleDescriptor(moduleDescriptions, name, str(componentId), true);

                        initRoles(moduled, attributes);
                        
                        String index = attributes.getValue(INDEX);
                        if (index != null)
                            moduled.setAttribute(INDEX, Integer.parseInt(index));
                        
                        moduled.setAttribute("fullname", str(attributes.getValue("fullname")));
                        moduled.setAttribute("category", str(attributes.getValue("category")));
                        moduled.setAttribute(CLASS, str(attributes.getValue(CLASS)));
                        
                        parameterList.clear();
                        connectorList.clear();
                        lightList.clear();
                        extensions.clear();
                    }
                    break;
                case defsignal:
                    {
                        if (signalTypes == null)
                        {
                            signalTypes = new PSignalTypes("signals");
                            moduleDescriptions.setSignals(signalTypes);
                        }
                    }
                    break;
                case signal:
                    {
                        if (signalTypes == null)
                            throw new SAXException("internal error signal(def=null)");
                        
                        int key = Integer.parseInt(attributes.getValue(KEY));
                        String type = attributes.getValue(TYPE);
                        
                        if (type == null)
                            throw new SAXException("signal type name not specified");

                        boolean noSignal = "true".equals(attributes.getValue("nosignal"));
                        String scolor = attributes.getValue("color");
                        Color color = null;
                        if (scolor != null)
                            color = Hex.htmlHexColor(scolor);
                        if (color == null)
                            color = noSignal ? Color.WHITE : Color.BLACK;

                        PSignal signal = signalTypes.create(key, str(type), color, noSignal);
                        //System.out.println(signal);
                    }
                    break;
                case deftype:
                    {
                        String name = attributes.getValue(NAME);
                        if (name == null)
                            throw new SAXException("type name not specified");
                    
                        typedef = new PSimpleTypes(str(name));
                        moduleDescriptions.addType(typedef);
                    }
                    break;
                case enumeration:
                    {
                        if (typedef == null)
                            throw new SAXException("internal error type(def=null)");
                    
                        int key = Integer.parseInt(attributes.getValue(KEY));
                        String value = attributes.getValue(VALUE);
                        
                        if (value == null)
                            throw new SAXException("value[key="+key+"] not specified in type "+typedef);
                        
                        typedef.putValue(key, value);
                    }
                    break;
                case image:
                    {
                        if (moduled == null)
                            throw new SAXException("no module associated with image");

                        String src = attributes.getValue("src");
                        if (src==null)
                            throw new SAXException("image has no such attribute: 'src'");
                        
                        String type = attributes.getValue(TYPE);
                        if (type==null)
                            throw new SAXException("image has no such attribute: 'type'");
                        
                        int width = Integer.parseInt(attributes.getValue("width"));
                        int height = Integer.parseInt(attributes.getValue("height"));

                        if (width<=0)
                            throw new SAXException("invalid image width: "+width);
                        if (height<=0)
                            throw new SAXException("invalid image height: "+height);
                        
                        ImageSource is = new ImageSource(src, width, height);
                        moduled.setAttribute(str(type), is);
                    }
                    break ;
                case light:
                    {
                        String name = attributes.getValue(NAME);
                        if (name == null)
                            throw new SAXParseException("light name must not be null", locator); 

                        String componentId = attributes.getValue(COMPONENTID);
                        if (componentId == null)
                            throw new SAXException("component-id missing in light: "+name);

                        String minValue = attributes.getValue("minValue");
                        String maxValue = attributes.getValue("maxValue");
                        String defaultValue = attributes.getValue("defaultValue");

                        lightd = new PBasicLightDescriptor(moduled, name, str(componentId));

                        initRoles(lightd, attributes);
                        
                        String index = attributes.getValue(INDEX);
                        if (index != null)
                            lightd.setAttribute(INDEX, Integer.parseInt(index));
                        
                        lightd.setType(getLEDType(attributes.getValue(TYPE)));
                        
                        if (minValue != null)
                            lightd.setMinValue(Integer.parseInt(minValue));
                        if (maxValue != null)
                            lightd.setMaxValue(Integer.parseInt(maxValue));
                        if (defaultValue != null)
                            lightd.setDefaultValue(Integer.parseInt(defaultValue));
                        
                        lightd.setAttribute(CLASS, attributes.getValue(CLASS));
                        lightList.add(lightd);
                    }
                    break;
                case parameter:
                    {
                        if (moduled == null)
                            throw new SAXException("no module associated with parameter");

                        String name = attributes.getValue(NAME);
                        if (name == null)
                            throw new SAXParseException("parameter name must not be null", locator); 

                        String componentId = attributes.getValue(COMPONENTID);
                        if (componentId == null)
                            throw new SAXException("component-id missing in parameter: "+name);
                        
                        String minValue = attributes.getValue("minValue");
                        String maxValue = attributes.getValue("maxValue");
                        String defaultValue = attributes.getValue("defaultValue");
                        
                        parameterd = new PBasicParameterDescriptor(moduled, str(name), componentId);

                        initRoles(parameterd, attributes);
                        
                        String extension = attributes.getValue("extension");
                        if (extension != null)
                            extensions.put(parameterd, extension);
                        
                        String index = attributes.getValue(INDEX);
                        if (index != null)
                            parameterd.setAttribute(INDEX, Integer.parseInt(index));
                        
                        if (minValue != null)
                            parameterd.setMinValue(Integer.parseInt(minValue));
                        if (maxValue != null)
                            parameterd.setMaxValue(Integer.parseInt(maxValue));
                        if (defaultValue != null)
                            parameterd.setDefaultValue(Integer.parseInt(defaultValue));
                        
                        parameterd.setAttribute(CLASS, str(attributes.getValue(CLASS)));
                   
                        String fmt = attributes.getValue("formatter");
                        
                        if (fmt != null)
                        {
                            Formatter formatter = getFormatter(fmt);
                            parameterd.setFormatter(formatter);
                        }
                        parameterList.add(parameterd);
                    }
                    break;
                case attribute:
                {
                    String name = attributes.getValue(NAME);
                    String type = attributes.getValue(TYPE);
                    String svalue = attributes.getValue(VALUE);
                    
                    int typeid = -1;
                    if (type != null)
                    {
                        typeid = getAttTypeId(type);
                        if (typeid <0 )
                            throw new SAXException("unknown attribute type "+type);
                    }
                    
                    Object value;
                    switch (typeid)
                    {
                        case attBoolean:
                            if (svalue.equals("yes")||svalue.equals("true"))
                                value = Boolean.TRUE;
                            else if (svalue.equals("no")||svalue.equals("false"))
                                value = Boolean.FALSE;
                            else
                                throw new SAXException("not a boolean value "+svalue);
                            break;
                        case attDouble:
                            value = Double.parseDouble(svalue);
                            break;
                        case attFloat:
                            value = Float.parseFloat(svalue);
                            break;
                        case attInteger:
                            value = Integer.parseInt(svalue);
                            break;
                        case attColor:
                            value = Hex.htmlHexColor(svalue);
                            break;
                        default:
                            value = str(svalue);
                            break;
                    }
                    
                    if (parameterd != null)
                        parameterd.setAttribute(name, value);
                    else if (connectord != null)
                        connectord.setAttribute(name, value);
                    else if (lightd != null)
                        lightd.setAttribute(name, value);
                    else if (moduled != null)
                        moduled.setAttribute(name, value);
                    else
                    {
                        throw new SAXException("attribute not associated with (module|parameter|connector)");
                    }
                }
                break;
                case connector:
                    {
                        if (moduled == null)
                            throw new SAXException("no module associated with connector");

                        String name = attributes.getValue(NAME);
                        if (name == null)
                            throw new SAXException("connector name must not be null"); 

                        String componentId = attributes.getValue(COMPONENTID);
                        if (componentId == null)
                            throw new SAXException("component-id missing in connector: "+name);
                        
                        String type = attributes.getValue(TYPE);
                        if (type == null)
                            throw new SAXException("connector type (input|output) not specified");

                        boolean isOutput;
                        
                        if ("input".equals(type))
                            isOutput = false;
                        else if ("output".equals(type))
                            isOutput = true;
                        else
                            throw new SAXException("connector must be one of (input|output): "+type);

                        PSignal sig = null;
                        String signalName = attributes.getValue(SIGNAL);
                        if (signalName != null)
                        {
                            if (signalTypes == null)
                                throw new SAXException("signal definitions missing");
                            sig = signalTypes.getTypeByName(signalName);
                            if (sig==null)
                                throw new SAXException("signal '"+signalName+"' not defined in "+signalTypes);
                        }
                        
                        connectord = new PBasicConnectorDescriptor(moduled, str(name), str(componentId), sig, isOutput);

                        initRoles(connectord, attributes);
                        
                        String index = attributes.getValue(INDEX);
                        if (index != null)
                            connectord.setAttribute(INDEX, Integer.parseInt(index));
                        
                        connectorList.add(connectord);
                    }
                    break;
            }
            
            currentElement = eid;
        }

        
        private void initRoles(PBasicDescriptor descriptor, Attributes attributes)
        {
            String roleString = attributes.getValue(ROLE);
            if (roleString!=null)
            {
                PRoles roles = PBasicRoles.parseRoles(roleString);
                if (!roles.isEmpty())
                    descriptor.setRoles(roles);
            }
        }

        public void endElement (String uri, String localName, String qName)
        throws SAXException
        {

            final int eid = getElementId(qName);
            
            switch (eid)
            {
                case doc:
                {
                    if (docText.length()>0)
                    {
                        PDescriptor descriptor;

                        if (parameterd != null)
                            descriptor = parameterd;
                        else if (connectord != null)
                            descriptor = connectord;
                        else if (lightd != null)
                            descriptor = lightd;
                        else if (moduled != null)
                            descriptor = moduled;
                        else
                            descriptor = null;
                         
                        if (descriptor != null)
                            descriptor.setAttribute("doc", docText.toString());
                    }
                    docElement = false;
                    break;
                }
                case parameter:
                {
                    parameterd = null;
                }
                break;
                case connector:
                {
                    connectord = null;
                }
                break;
                case light:
                {
                    lightd = null;
                }
                break;
                case module:
                {
                    if (moduled==null)
                        throw new SAXException("internal error endElement(module)");
                    
                    moduled.setParameters(parameterList);
                    moduled.setConnectors(connectorList);
                    moduled.setLights(lightList);
                    

                    // set extensions
                    if (!extensions.isEmpty())
                    {
                        for (PBasicParameterDescriptor dst: extensions.keySet())
                        {
                            String id = extensions.get(dst);
                            PParameterDescriptor ext = moduled.getParameterByComponentId(id);
                            if (ext == null)
                                throw new PRuntimeException("extension [component-id='"+id+"'] not found for parameter: "+dst);
                            dst.setExtensionDescriptor(ext);
                        }
                    }
                    
                    
                    moduleDescriptions.add(moduled);
                    moduled = null;
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
            if (docElement)
                docText.append(ch, start, length);
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
        

        public ModuleDescriptions getResult()
        {
            // TODO Auto-generated method stub
            return null;
        }

    }
    
}
