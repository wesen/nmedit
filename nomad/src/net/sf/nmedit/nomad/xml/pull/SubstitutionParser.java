package net.sf.nmedit.nomad.xml.pull;
import java.io.IOException;
import java.util.ArrayList;

import net.sf.nmedit.nomad.xml.dom.substitution.ListSubstitution;
import net.sf.nmedit.nomad.xml.dom.substitution.Substitution;
import net.sf.nmedit.nomad.xml.dom.substitution.Substitutions;
import net.sf.nmedit.nomad.xml.dom.substitution.TransformationSubstitution;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

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

public class SubstitutionParser extends NomadPullParser {
	
	private Substitutions container = null;
	
	public SubstitutionParser() {
		this(new Substitutions());
	}
	
	public SubstitutionParser(Substitutions container) {
		this.container = container;
	}
	
	public Substitutions getSubstitutions() {
		return container;
	}
	
	protected void substitution(Substitution subs, String subsId) {
		container.putSubstitution(subsId, subs);
	}
	
	protected void parse(XmlPullParser parser) throws XmlPullParserException, IOException {
		
		event = parser.nextToken();
		while (event!=XmlPullParser.END_DOCUMENT) {
			
			switch (event) {
				case XmlPullParser.START_TAG:

					if (tag(parser, "substitutions"))
						; // all right
					else if (tag(parser, "list")) 
						list(parser);
					else if (tag(parser, "use-implementation")) 
						useImplementation(parser);
					else if (tag(parser, "transform")) 
						transform(parser);
					else
						errorNoSuchElement(parser);
					break;
			}
		
			event = parser.nextToken();
		}
	}


	private void transform(XmlPullParser parser) throws XmlPullParserException, IOException {
		// read attributes
		String attId = null;
		String attFactor="1.0";
		String attOffset="0.0";
		String attPrefix="";
		String attSuffix="";
		
		for (int i=parser.getAttributeCount()-1;i>=0;i--) {
			if ("id".equals(parser.getAttributeName(i))) {
				attId = parser.getAttributeValue(i);
			} else if ("factor".equals(parser.getAttributeName(i))) {
				attFactor = parser.getAttributeValue(i);
			} else if ("offset".equals(parser.getAttributeName(i))) {
				attOffset = parser.getAttributeValue(i);
			} else if ("praefix".equals(parser.getAttributeName(i))) {
				attPrefix = parser.getAttributeValue(i);
			} else if ("suffix".equals(parser.getAttributeName(i))) {
				attSuffix = parser.getAttributeValue(i);
			} else {
				warning(); // print error to System.err
				errorNoSuchAttribute(parser, i);
			}
		}

		assureAttributeExists(parser, "id", attId);

		TransformationSubstitution subs = new TransformationSubstitution();
		subs.setPraefix(attPrefix);
		subs.setSuffix(attSuffix);

		try {
			subs.setFactor(Double.parseDouble(attFactor));
			subs.setOffset(Double.parseDouble(attOffset));
		} catch (NumberFormatException e) {
			error(parser, e);
		}
		
		boolean done = false;
		event = parser.nextToken();
		while (event!=XmlPullParser.END_DOCUMENT) {
			
			switch (event) {
				case XmlPullParser.START_TAG:

					if (tag(parser, "if")) {
						// read attributes
						String attComparator = null;
						String attReplacement = null;

						for (int i=parser.getAttributeCount()-1;i>=0;i--) {
							if ("value-is".equals(parser.getAttributeName(i))) {
								attComparator = parser.getAttributeValue(i);
							} else if ("replace-with".equals(parser.getAttributeName(i))) {
								attReplacement = parser.getAttributeValue(i);
							} else {
								warning(); // print error to System.err
								errorNoSuchAttribute(parser, i);
							}
						}

						if (attComparator==null)
							errorMissingAttribute(parser, "value-is");
						if (attReplacement==null)
							errorMissingAttribute(parser, "replace-with");
						
						try {
							subs.setReplacement( Integer.parseInt(attComparator), attReplacement );
						} catch (NumberFormatException e) {
							error(parser, e);
						}
						done = true;
						
					} else
						errorNoSuchElement(parser);

					break;

				case XmlPullParser.END_TAG:
					if (tag(parser, "if"))
						; // ok
					else if (tag(parser, "transform"))
						done = true;
						
					break;
			}
		
			if (done)
				break;
			
			event = parser.nextToken();
		}
		
		substitution(subs, attId);
	}

	private void useImplementation(XmlPullParser parser) throws XmlPullParserException, IOException {
		// read attributes
		String attId = null;
		String attClass = null;
		for (int i=parser.getAttributeCount()-1;i>=0;i--) {
			if ("id".equals(parser.getAttributeName(i))) {
				attId = parser.getAttributeValue(i);
			} else if ("class".equals(parser.getAttributeName(i))) {
				attClass = parser.getAttributeValue(i);
			} else {
				warning(); // print error to System.err
				errorNoSuchAttribute(parser, i);
			}
		}

		assureAttributeExists(parser, "id", attId);
		assureAttributeExists(parser, "class", attClass);
		
		boolean done = false;
		event = parser.nextToken();
		while (event!=XmlPullParser.END_DOCUMENT) {
			
			switch (event) {
				case XmlPullParser.START_TAG:

					// element has no children
					errorNoSuchElement(parser);
					
					break;

				case XmlPullParser.END_TAG:
					if (tag(parser, "use-implementation"))
						done = true;

					break;

			}
		
			if (done)
				break;
			
			event = parser.nextToken();
		}
		
		try {
			Class subsClass = ClassLoader.getSystemClassLoader().loadClass(attClass);
			Substitution subs = (Substitution) subsClass.newInstance();
			substitution(subs, attId);
		} catch (Throwable e) {
			warning();
			error(parser, e);
		}
	}

	private void list(XmlPullParser parser) throws XmlPullParserException, IOException {
		// read attributes
		String attId = null;
		for (int i=parser.getAttributeCount()-1;i>=0;i--) {
			if ("id".equals(parser.getAttributeName(i))) {
				attId = parser.getAttributeValue(i);
			} else {
				warning(); // print error to System.err
				errorNoSuchAttribute(parser, i);
			}
		}
		
		assureAttributeExists(parser, "id", attId);
		
		ArrayList<String> items = new ArrayList<String>();
		boolean done = false;
		event = parser.nextToken();
		while (event!=XmlPullParser.END_DOCUMENT) {
			
			switch (event) {
				case XmlPullParser.START_TAG:

					if (tag(parser, "item")) {
						try {
							String itemText = parser.nextText();
							items.add(itemText.trim());
						} catch (XmlPullParserException e) {
							// no text -> empty string
							items.add("");
						}
					} else
						errorNoSuchElement(parser);
					
					break;

				case XmlPullParser.END_TAG:
					if (tag(parser, "item"))
						; // thats ok
					else if (tag(parser, "list"))
						done = true;
						
					break;

			}
		
			if (done)
				break;
			
			event = parser.nextToken();
		}

		ListSubstitution subs = new ListSubstitution(items.toArray(new String[items.size()]));
		substitution(subs, attId);
	}

}
