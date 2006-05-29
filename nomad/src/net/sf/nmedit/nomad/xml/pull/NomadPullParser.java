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
package net.sf.nmedit.nomad.xml.pull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public abstract class NomadPullParser {
	
	/**
	 * The parser's current event
	 */
	protected int event = 0;
	
	/**
	 * Indicates whether an error should cause an exception to be thrown,
	 * or if the error should be ignored and a notification will be printed
	 * to System.err
	 */
	private boolean isWarning = false;

	public boolean isWarning() {
		return isWarning;
	}
	
	private boolean flagWarningIfAttributes = true;
	
	/**
	 * Sets the warning flag. After calling error() the flag is set back to false.
	 * This should only be called direct before calling error().
	 */
	protected void warning() {
		warning(true);
	}

	protected void warning(boolean warning) {
		isWarning=warning;
	}

	protected void error(XmlPullParser parser, String message) throws XmlPullParserException {
		String msg = info(parser)+": "+message;
		if (isWarning) {
			System.err.println("** "+msg);
			isWarning = false;
			return;
		}
		throw new XmlPullParserException(msg);
	}

	protected void error(XmlPullParser parser, Throwable e) throws XmlPullParserException {
		if (isWarning) {
			error(parser, e.getMessage());
			e.printStackTrace();
			return;
		}
		throw new XmlPullParserException("An exception occured", parser, e);
	}

	protected void errorNoSuchElement(XmlPullParser parser) throws XmlPullParserException {
		error(parser, "No such element '"+parser.getName()+"'");
	}

	protected void errorNoSuchAttribute(XmlPullParser parser, int attribute) throws XmlPullParserException {
		error(parser, "No such attribute '"+parser.getAttributeName(attribute)+"'" +
				" in Element '"+parser.getName()+"'.");
	}

	protected void errorMissingAttribute(XmlPullParser parser, String attribute) throws XmlPullParserException {
		error(parser, "No such attribute '"+attribute+"'" +" in Element '"+parser.getName()+"'.");
	}

	protected String info(XmlPullParser parser) {
		return "@"+ parser.getLineNumber()+","+parser.getColumnNumber();
	}
	
	protected void assureNoAttributes(XmlPullParser parser) throws XmlPullParserException {
		assureNoAttributes(parser, flagWarningIfAttributes);
	}
	
	protected void assureNoAttributes(XmlPullParser parser, boolean isWarning) throws XmlPullParserException {
		if (parser.getAttributeCount()>0) {
			warning(isWarning);
			error(parser, "Element '"+parser.getName()+"' has not attributes");
		}
	}

	protected void assureAttributeExists(XmlPullParser parser, String attName, String attValue) throws XmlPullParserException {
		if (attValue==null) {
			errorMissingAttribute(parser, attName);
		}
	}
	
	protected void conversionError(XmlPullParser parser, String attName, String attValue, String typeName) throws XmlPullParserException {
		error(parser, "Cannot convert attribute '"+attName+"'='"+attValue+"' to type '"+typeName+"' in element '"+parser.getName()+"'.");
	}
	
	protected void conversionError(XmlPullParser parser, String attName, String attValue, Class type) throws XmlPullParserException {
		conversionError(parser, attName, attValue, type.getName());
	}

	protected double convertDouble(XmlPullParser parser, int attribute) throws XmlPullParserException {
		return convertDouble(parser, parser.getAttributeName(attribute), parser.getAttributeValue(attribute) );
	}

	protected double convertDouble(XmlPullParser parser, int attribute, double defaultValue) {
		return convertDouble(parser, parser.getAttributeValue(attribute), defaultValue );
	}
	
	protected double convertDouble(XmlPullParser parser, String attValue, double defaultValue) {
		if (attValue!=null) {
			try {
				return Double.parseDouble(attValue);
			} catch (NumberFormatException e) {
			}
		}
		return defaultValue;
	}
	
	protected double convertDouble(XmlPullParser parser, String attName, String attValue) throws XmlPullParserException {
		assureAttributeExists(parser, attName, attValue);
		try {
			return Double.parseDouble(attValue);
		} catch (NumberFormatException e) {
			conversionError(parser, attName, attValue, Double.class);
		}
		return 0;
	}

	protected int convertInteger(XmlPullParser parser, int attribute) throws XmlPullParserException {
		return convertInteger(parser, parser.getAttributeName(attribute), parser.getAttributeValue(attribute) );
	}

	protected int convertInteger(XmlPullParser parser, int attribute, int defaultValue) {
		return convertInteger(parser, parser.getAttributeValue(attribute), defaultValue );
	}
	
	protected int convertInteger(XmlPullParser parser, String attValue, int defaultValue) {
		if (attValue!=null) {
			try {
				return Integer.parseInt(attValue);
			} catch (NumberFormatException e) {
			}
		}
		return defaultValue;
	}
	
	protected int convertInteger(XmlPullParser parser, String attName, String attValue) throws XmlPullParserException {
		assureAttributeExists(parser, attName, attValue);
		try {
			return Integer.parseInt(attValue);
		} catch (NumberFormatException e) {
			conversionError(parser, attName, attValue, Integer.class);
		}
		return 0;
	}

	protected boolean convertBoolean(XmlPullParser parser, int attribute) throws XmlPullParserException {
		return convertBoolean(parser, parser.getAttributeName(attribute), parser.getAttributeValue(attribute) );
	}

	protected boolean convertBoolean(XmlPullParser parser, int attribute, boolean defaultValue) {
		return convertBoolean(parser, parser.getAttributeValue(attribute), defaultValue );
	}
	
	protected boolean convertBoolean(XmlPullParser parser, String attValue, boolean defaultValue) {
		if (attValue!=null) {
			try {
				return Boolean.parseBoolean(attValue);
			} catch (NumberFormatException e) {
			}
		}
		return defaultValue;
	}
	
	protected boolean convertBoolean(XmlPullParser parser, String attName, String attValue) throws XmlPullParserException {
		assureAttributeExists(parser, attName, attValue);
		try {
			return Boolean.parseBoolean(attValue);
		} catch (NumberFormatException e) {
			conversionError(parser, attName, attValue, Boolean.class);
		}
		return false;
	}
	
	/**
	 * Creates a parser instance
	 * @return the parser
	 * @throws XmlPullParserException
	 */

	protected XmlPullParser getParser() throws XmlPullParserException {
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(true);
		factory.setValidating(false);
		XmlPullParser parser = factory.newPullParser();
		return parser;
	}
	
	public void parse(String fileName) throws FileNotFoundException, XmlPullParserException {
		parse(new FileReader(fileName));
	}
	
	public void parse(File file) throws FileNotFoundException, XmlPullParserException {
		parse(new FileReader(file));
	}
	
	public void parse(InputStream in) throws XmlPullParserException {
		parse(new InputStreamReader(in));
	}
	
	public void parse(Reader reader) throws XmlPullParserException {
		XmlPullParser parser = getParser();
		parser.setInput(reader);
		try {
			parse(parser);
		} catch (Throwable e) {
			if (e instanceof XmlPullParserException)
				throw (XmlPullParserException) e;
			else
				error(parser, e);
		}
	}
	
	protected abstract void parse(XmlPullParser parser) throws XmlPullParserException, IOException;
	
	/**
	 * Returns true if the parsers tag name property is equals to the given string name
	 * @param parser the parser
	 * @param name the name
	 * @return true if the parsers tag name property is equals to the given string name
	 */
	protected boolean tag(XmlPullParser parser, String name) {
		return name.equals(parser.getName());
	}
	
}
