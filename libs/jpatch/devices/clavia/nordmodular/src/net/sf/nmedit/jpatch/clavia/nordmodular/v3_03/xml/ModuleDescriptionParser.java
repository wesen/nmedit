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
package net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.xml;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.DConnector;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.DCustom;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.DGroup;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.DModule;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.DParameter;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.DSection;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.ModuleDescriptions;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.substitution.Substitution;

public class ModuleDescriptionParser extends NomadPullParser {

	private ModuleDescriptions dmodules = null;
	
	public ModuleDescriptionParser(ModuleDescriptions dmodules) {
		this.dmodules = dmodules;
	}
	
	public ModuleDescriptions getModuleDescriptions() {
		return dmodules;
	}

	protected void parse(XmlPullParser parser) throws XmlPullParserException, IOException {

		event = parser.nextToken();
		while (event!=XmlPullParser.END_DOCUMENT) {
			
			switch (event) {
				case XmlPullParser.START_TAG:

					if (tag(parser, "modules-definition"))
						; // all right (root element)
					else if (tag(parser, "structure")) 
						structure(parser);
					else if (tag(parser, "modules")) 
						modules(parser);
					else
						errorNoSuchElement(parser);
					break;
			}
		
			event = parser.nextToken();
		}
		
	}

	private void structure(XmlPullParser parser) throws XmlPullParserException, IOException {
		
		assureNoAttributes(parser);
		
		boolean done = false;
		event = parser.nextToken();
		while (event!=XmlPullParser.END_DOCUMENT) {
			
			switch (event) {
				case XmlPullParser.START_TAG:

					if (tag(parser, "group"))
						group(parser);
					else
						errorNoSuchElement(parser);
					break;
				
				case XmlPullParser.END_TAG:
					
					if (tag(parser, "group"))
						; // ok
					if (tag(parser, "structure"))
						done = true;
					break;
			}
			
			if (done)
				break;
		
			event = parser.nextToken();
		}
	}
	

	private void group(XmlPullParser parser) throws XmlPullParserException, IOException {
		/*
		<!ATTLIST group
			name 		CDATA		#REQUIRED
			short-name 	CDATA		#IMPLIED
		>*/
		
		String attGroupName = null;
		String attShortName = null;

		for (int i=parser.getAttributeCount()-1;i>=0;i--) {
			if ("name".equals(parser.getAttributeName(i))) {
				attGroupName = parser.getAttributeValue(i);
			} else if ("short-name".equals(parser.getAttributeName(i))) {
				attShortName = parser.getAttributeValue(i);
			} else {
				warning(); // print error to System.err
				errorNoSuchAttribute(parser, i);
			}
		}
		
		assureAttributeExists(parser, "name", attGroupName);
		

		DGroup group =
			attShortName==null ? new DGroup(attGroupName)
		                       : new DGroup(attGroupName, attShortName);
		dmodules.addToolbarGroup(group);

		boolean done = false;
		event = parser.nextToken();
		while (event!=XmlPullParser.END_DOCUMENT) {
			
			switch (event) {
				case XmlPullParser.START_TAG:

					if (tag(parser, "section"))
						section(parser, group);
					else
						errorNoSuchElement(parser);
					break;
				
				case XmlPullParser.END_TAG:
					
					if (tag(parser, "section"))
						; // ok
					if (tag(parser, "group"))
						done = true;
					break;
			}
			
			if (done)
				break;
		
			event = parser.nextToken();
		}
		
		// TODO assure group section count >0
		
	}

	private void section(XmlPullParser parser, DGroup group) throws XmlPullParserException, IOException {

		assureNoAttributes(parser);
		
		DSection section = new DSection(group);
		group.addSection(section);
		
		boolean done = false;
		event = parser.nextToken();
		while (event!=XmlPullParser.END_DOCUMENT) {
			
			switch (event) {
				case XmlPullParser.START_TAG:

					if (tag(parser, "insert"))
						insert(parser, section);
					else
						errorNoSuchElement(parser);
					break;
				
				case XmlPullParser.END_TAG:
					
					if (tag(parser, "insert"))
						; // ok
					if (tag(parser, "section"))
						done = true;
					break;
			}
			
			if (done)
				break;
		
			event = parser.nextToken();
		}
		
		// TODO assure section insert count >0
	}

	private void insert(XmlPullParser parser, DSection section) throws XmlPullParserException, IOException {

		String attModuleId = null;
		
		for (int i=parser.getAttributeCount()-1;i>=0;i--) {
			if ("module-id".equals(parser.getAttributeName(i))) {
				attModuleId = parser.getAttributeValue(i);
			} else {
				warning(); // print error to System.err
				errorNoSuchAttribute(parser, i);
			}
		}
		
		assureAttributeExists(parser, "module-id", attModuleId);

		// TODO ...
		
		DModule module = null;
		try {
			module = getModuleDescriptions().getModuleById(Integer.parseInt(attModuleId));
		} catch (NumberFormatException e) {
			// error is given later
		}
		
		if (module==null) {
			warning();
			error(parser, "Referenced module (key='"+attModuleId+"') does not exist. (Invalid element order?)");
		} else {
			module.setParent(section);
			section.addModule(module);
		}

		boolean done = false;
		event = parser.nextToken();
		while (event!=XmlPullParser.END_DOCUMENT) {
			
			switch (event) {
				case XmlPullParser.START_TAG:

					errorNoSuchElement(parser); // has no children
					break;

				case XmlPullParser.END_TAG:
					
					if (tag(parser, "insert"))
						done = true;
					break;
			}
			
			if (done)
				break;
		
			event = parser.nextToken();
		}
	}

	private void modules(XmlPullParser parser) throws XmlPullParserException, IOException {

		assureNoAttributes(parser);
		
		boolean done = false;
		event = parser.nextToken();
		while (event!=XmlPullParser.END_DOCUMENT) {
			
			switch (event) {
				case XmlPullParser.START_TAG:

					if (tag(parser, "module"))
						module(parser);
					else
						errorNoSuchElement(parser);
					break;
				
				case XmlPullParser.END_TAG:
					
					if (tag(parser, "module"))
						; // ok
					if (tag(parser, "modules"))
						done = true;
					break;
			}
			
			if (done)
				break;
		
			event = parser.nextToken();
		}
	}

	private void module(XmlPullParser parser) throws XmlPullParserException, IOException {
		String attModuleId = null;
		String attShortName= null;
		String attCVAonly  = "false";
		String attLimit    = null;
		String attCycles   = null;
		String attDynMem   = null;
		String attHeight   = null;
		String attProgMem  = null;
		String attXmem     = null;
		String attYmem     = null;
		String attZeroPage = null;
		
		for (int i=parser.getAttributeCount()-1;i>=0;i--) {
			if ("id".equals(parser.getAttributeName(i))) {
				attModuleId = parser.getAttributeValue(i);
			} else if ("short-name".equals(parser.getAttributeName(i))) {
				attShortName = parser.getAttributeValue(i);
			} else if ("limit".equals(parser.getAttributeName(i))) {
				attLimit = parser.getAttributeValue(i);
			} else if ("cycles".equals(parser.getAttributeName(i))) {
				attCycles = parser.getAttributeValue(i);
			} else if ("dyn-mem".equals(parser.getAttributeName(i))) {
				attDynMem = parser.getAttributeValue(i);
			} else if ("height".equals(parser.getAttributeName(i))) {
				attHeight = parser.getAttributeValue(i);
			} else if ("prog-mem".equals(parser.getAttributeName(i))) {
				attProgMem = parser.getAttributeValue(i);
			} else if ("x-mem".equals(parser.getAttributeName(i))) {
				attXmem = parser.getAttributeValue(i);
			} else if ("y-mem".equals(parser.getAttributeName(i))) {
				attYmem = parser.getAttributeValue(i);
			} else if ("zero-page".equals(parser.getAttributeName(i))) {
				attZeroPage = parser.getAttributeValue(i);
			} else if ("cva-only".equals(parser.getAttributeName(i))) {
				attCVAonly = parser.getAttributeValue(i);
			} else {
				warning(); // print error to System.err
				errorNoSuchAttribute(parser, i);
			}
		}

		assureAttributeExists(parser, "short-name", attShortName);
		DModule module = new DModule(null, attShortName);
		module.setShortName(attShortName);
		module.setModuleID(convertInteger(parser, "id", attModuleId));
		module.setHeight(convertInteger(parser, "height", attHeight));

		module.setCycles(convertDouble(parser, attCycles,0));
		module.setXmem(convertDouble(parser, attXmem,0));
		module.setYmem(convertDouble(parser,attYmem,0));
		module.setProgMem(convertDouble(parser, attProgMem,0));
		module.setDynMem(convertDouble(parser, attDynMem,0));
		module.setZeroPage(convertDouble(parser, attZeroPage,0));
		module.setCvaOnly(convertBoolean(parser, attCVAonly, false));
		module.setLimit(convertInteger(parser, attLimit,-1));
		

		boolean done = false;
		event = parser.nextToken();
		while (event!=XmlPullParser.END_DOCUMENT) {
			
			switch (event) {
				case XmlPullParser.START_TAG:

					if (tag(parser, "input"))
						connector(parser, module, DConnector.CONNECTOR_TYPE_INPUT); 
					else if (tag(parser, "output"))
						connector(parser, module, DConnector.CONNECTOR_TYPE_OUTPUT);
					else if (tag(parser, "parameter"))
						parameter(parser, module, false);
					else if (tag(parser, "custom"))
						parameter(parser, module, true);
					else
						errorNoSuchElement(parser);
					break;
				
				case XmlPullParser.END_TAG:
					
					if (tag(parser, "input"))
						; // ok
					else if (tag(parser, "output"))
						; // ok
					else if (tag(parser, "parameter"))
						; // ok
					else if (tag(parser, "custom"))
						; // ok
					else if (tag(parser, "module"))
						done = true;
					break;
			}
			
			if (done)
				break;
		
			event = parser.nextToken();
		}
		
		dmodules.add(module);
	}

	private void connector(XmlPullParser parser, DModule module, int connector_type) throws XmlPullParserException, IOException {
		String connectorId = null;
		String connectorSignal= null;
		String connectorName= null;
		for (int i=parser.getAttributeCount()-1;i>=0;i--) {
			if ("id".equals(parser.getAttributeName(i))) {
				connectorId = parser.getAttributeValue(i);
			} else if ("type".equals(parser.getAttributeName(i))) {
				connectorSignal = parser.getAttributeValue(i);
			} else if ("name".equals(parser.getAttributeName(i))) {
				connectorName = parser.getAttributeValue(i);
			} else {
				warning(); // print error to System.err
				errorNoSuchAttribute(parser, i);
			}
		} 

		assureAttributeExists(parser, "id", connectorId);
		assureAttributeExists(parser, "type", connectorSignal);
		assureAttributeExists(parser, "name", connectorName);
		
		DConnector connector = new DConnector(module, 
			convertInteger(parser, "id", connectorId),
			connector_type,
			DConnector.getSignalId(connectorSignal),
			connectorName
		);

		boolean done = false;
		event = parser.nextToken();
		while (event!=XmlPullParser.END_DOCUMENT) {
			
			switch (event) {
				case XmlPullParser.START_TAG:

					errorNoSuchElement(parser); // has no children
					break;

				case XmlPullParser.END_TAG:
					
					if (tag(parser, "input"))
						done = true;
					else if (tag(parser, "output"))
						done = true;
					break;
			}
			
			if (done)
				break;
		
			event = parser.nextToken();
		}

		module.addConnector(connector);
	}

	private void parameter(XmlPullParser parser, DModule module, boolean isCustomParameter) throws XmlPullParserException, IOException {
		String attId = null;
		String attName= null;
		String attMin = null;
		String attMax = null;
		String attBitCount="0";
		String attDefaultValue="0";
		String attSubstitutionKey=null;

		for (int i=parser.getAttributeCount()-1;i>=0;i--) {
			if ("id".equals(parser.getAttributeName(i))) {
				attId = parser.getAttributeValue(i);
			} else if ("name".equals(parser.getAttributeName(i))) {
				attName = parser.getAttributeValue(i);
			} else if ("min".equals(parser.getAttributeName(i))) {
				attMin = parser.getAttributeValue(i);
			} else if ("max".equals(parser.getAttributeName(i))) {
				attMax = parser.getAttributeValue(i);
			} else if ("bit-count".equals(parser.getAttributeName(i))) {
				attBitCount = parser.getAttributeValue(i);
			} else if ("default".equals(parser.getAttributeName(i))) {
				attDefaultValue = parser.getAttributeValue(i);
			} else if ("use-substitution".equals(parser.getAttributeName(i))) {
				attSubstitutionKey = parser.getAttributeValue(i);
			} else {
				warning(); // print error to System.err
				errorNoSuchAttribute(parser, i);
			}
		}

		assureAttributeExists(parser, "name", attName);		
		int pmId = convertInteger(parser, "id", attId);
		int pmMin= convertInteger(parser, "min", attMin);
		int pmMax= convertInteger(parser, "max", attMax);
		int pmBitC = convertInteger(parser, "bit-count", attBitCount);
		int pmDefault = convertInteger(parser, "default", attDefaultValue); 
		
		Substitution s;
		if (attSubstitutionKey==null) {
			s = Substitution.DEFAULT_SUBSTITUTION;
		} else {
			s = getModuleDescriptions().getSubstitutions().getSubstitution(attSubstitutionKey);
			if (s==null) {
				warning();
				error(parser, "Substitution [key="+attSubstitutionKey+"] does not exist.");
			}
		}
		
		boolean done = false;
		event = parser.nextToken();
		while (event!=XmlPullParser.END_DOCUMENT) {
			
			switch (event) {
				case XmlPullParser.START_TAG:

					errorNoSuchElement(parser); // has no children
					break;

				case XmlPullParser.END_TAG:
					
					if (tag(parser, "parameter"))
						done = true;
					else if (tag(parser, "custom"))
						done = true;
					break;
			}
			
			if (done)
				break;
		
			event = parser.nextToken();
		}

		if (!isCustomParameter) {
			DParameter pinfo = new DParameter(module, s, pmMin, pmMax, pmBitC, pmId, attName);
			pinfo.setDefaultValue(pmDefault);
			module.addParameter(pinfo);
		} else {
			DCustom pinfo = new DCustom(module, s, pmMin, pmMax, pmBitC, pmId, attName);
			pinfo.setDefaultValue(pmDefault);
			module.addCustomParam(pinfo);
		}
	}

}
