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

import java.io.IOException;

import org.nomad.xml.dom.theme.NomadDOM;
import org.nomad.xml.dom.theme.NomadDOMComponent;
import org.nomad.xml.dom.theme.NomadDOMModule;
import org.nomad.xml.dom.theme.NomadDOMProperty;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class UIParser extends NomadPullParser {

	private NomadDOM dom = null;
	
	public UIParser(NomadDOM dom) {
		this.dom = dom;
	}

	protected void parse(XmlPullParser parser) throws XmlPullParserException, IOException {
		event = parser.nextToken();
		while (event!=XmlPullParser.END_DOCUMENT) {
			
			switch (event) {
				case XmlPullParser.START_TAG:

					if (tag(parser, "ui-description"))
						; // all right
					else if (tag(parser, "module")) 
						module(parser);
					else
						errorNoSuchElement(parser);
					break;
			}
		
			event = parser.nextToken();
		}
	}

	private void module(XmlPullParser parser) throws XmlPullParserException, IOException {
		String attModuleId = null;

		for (int i=parser.getAttributeCount()-1;i>=0;i--) {
			if ("id".equals(parser.getAttributeName(i))) {
				attModuleId = parser.getAttributeValue(i);
			} else {
				warning(); // print error to System.err
				errorNoSuchAttribute(parser, i);
			}
		}

		assureAttributeExists(parser, "id", attModuleId);
		NomadDOMModule domModule = dom.createModuleNode(convertInteger(parser, "id", attModuleId));

		boolean done = false;
		event = parser.nextToken();
		while (event!=XmlPullParser.END_DOCUMENT) {
			
			switch (event) {
				case XmlPullParser.START_TAG:

					if (tag(parser, "component")) {
						component(parser, domModule);
					} else
						errorNoSuchElement(parser);

					break;

				case XmlPullParser.END_TAG:
					if (tag(parser, "component"))
						; // ok
					else if (tag(parser, "module"))
						done = true;
						
					break;
			}
		
			if (done)
				break;
			
			event = parser.nextToken();
		}
		
	}

	private void component(XmlPullParser parser, NomadDOMModule domModule) throws XmlPullParserException, IOException {
		String attComponentName = null;

		for (int i=parser.getAttributeCount()-1;i>=0;i--) {
			if ("name".equals(parser.getAttributeName(i))) {
				attComponentName = parser.getAttributeValue(i);
			} else {
				warning(); // print error to System.err
				errorNoSuchAttribute(parser, i);
			}
		}

		assureAttributeExists(parser, "name", attComponentName);
		NomadDOMComponent domComponent = domModule.createComponentNode(attComponentName);


		boolean done = false;
		event = parser.nextToken();
		while (event!=XmlPullParser.END_DOCUMENT) {
			
			switch (event) {
				case XmlPullParser.START_TAG:

					if (tag(parser, "property")) {
						property(parser, domComponent);
					} else
						errorNoSuchElement(parser);

					break;

				case XmlPullParser.END_TAG:
					if (tag(parser, "property"))
						; // ok
					else if (tag(parser, "component"))
						done = true;
						
					break;
			}
		
			if (done)
				break;
			
			event = parser.nextToken();
		}
	}

	private void property(XmlPullParser parser, NomadDOMComponent domComponent) throws XmlPullParserException, IOException {
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
		NomadDOMProperty domProperty = domComponent.createPropertyNode(attPropertyName);
		domProperty.setValue(attPropertyValue);

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
