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
 * Created on Jan 17, 2006
 */
package org.nomad.xml.pull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class PropertyParser extends NomadPullParser {

	private HashMap<String, String> map;
	
	public static void load(HashMap<String,String> map, String file) {
		load(map, new File(file));
	}
	
	public static void load(HashMap<String,String> map, File file) {
		PropertyParser parser = new PropertyParser(map);
		try {
			parser.parse(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
	}
	
	public PropertyParser(HashMap<String, String> map) {
		this.map = map;
	}

	protected void parse(XmlPullParser parser) throws XmlPullParserException, IOException {
		event = parser.nextToken();
		while (event!=XmlPullParser.END_DOCUMENT) {
			
			switch (event) {
				case XmlPullParser.START_TAG:

					if (tag(parser, "properties"))
						; // all right
					else if (tag(parser, "property")) 
						property(parser);
					else
						errorNoSuchElement(parser);
					break;
			}
		
			event = parser.nextToken();
		}
	}

	private void property(XmlPullParser parser) throws XmlPullParserException, IOException {
		String attPropertyName = null;
		String attPropertyValue = null;

		for (int i=parser.getAttributeCount()-1;i>=0;i--) {
			if ("name".equals(parser.getAttributeName(i))) {
				attPropertyName = parser.getAttributeValue(i);
			} else if ("value".equals(parser.getAttributeName(i))) {
				attPropertyValue = parser.getAttributeValue(i);
			} else {
				warning(); // print error to System.err
				errorNoSuchAttribute(parser, i);
			}
		}

		assureAttributeExists(parser, "name", attPropertyName);
		assureAttributeExists(parser, "value", attPropertyValue);
		
		map.put(attPropertyName, attPropertyValue);

		boolean done = false;
		event = parser.nextToken();
		while (event!=XmlPullParser.END_DOCUMENT) {
			
			switch (event) {
				case XmlPullParser.START_TAG:

					errorNoSuchElement(parser); // has no children

					break;

				case XmlPullParser.END_TAG:
					if (tag(parser, "property"))
						done = true;
						
					break;
			}
		
			if (done)
				break;
			
			event = parser.nextToken();
		}
	}

}
