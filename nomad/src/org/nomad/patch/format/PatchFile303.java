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
 * Created on Feb 12, 2006
 */
package org.nomad.patch.format;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

import org.nomad.util.misc.NomadUtilities;

public class PatchFile303 {
	
	public final static int SEC_CABLE_DUMP 				= 1 <<  0;
	public final static int SEC_CTRLMAP_DUMP 			= 1 <<  1;
	public final static int SEC_CURRENTNOTE_DUMP 		= 1 <<  2;
	public final static int SEC_CUSTOM_DUMP 			= 1 <<  3;
	public final static int SEC_HEADER 					= 1 <<  4;
	public final static int SEC_KEYBOARDASSIGNMENT 		= 1 <<  5;
	public final static int SEC_KNOBMAP_DUMP 			= 1 <<  6;
	public final static int SEC_MODULE_DUMP 			= 1 <<  7;
	public final static int SEC_MORPHMAP_DUMP 			= 1 <<  8;
	public final static int SEC_NAME_DUMP 				= 1 <<  9;
	public final static int SEC_NOTES 					= 1 << 10;
	public final static int SEC_PARAMETER_DUMP 			= 1 << 11;

	public final static int SEC_ALL =	
		SEC_CABLE_DUMP 			|
		SEC_CTRLMAP_DUMP 		|
		SEC_CURRENTNOTE_DUMP 	|
		SEC_CUSTOM_DUMP 		|
		SEC_HEADER 				|
		SEC_KEYBOARDASSIGNMENT 	|
		SEC_KNOBMAP_DUMP 		|
		SEC_MODULE_DUMP 		|
		SEC_MORPHMAP_DUMP 		|
		SEC_NAME_DUMP 			|
		SEC_NOTES 				|
		SEC_PARAMETER_DUMP
	;
	
	public final static int SEC_INFORMATION =
		SEC_HEADER 				|
		SEC_NOTES
	;

	public final static String NAME_CABLE_DUMP 			= "CableDump";
	public final static String NAME_CTRLMAP_DUMP 		= "CtrlMapDump";
	public final static String NAME_CURRENTNOTE_DUMP	= "CurrentNoteDump";
	public final static String NAME_CUSTOM_DUMP 		= "CustomDump";
	public final static String NAME_HEADER 				= "Header";
	public final static String NAME_KEYBOARDASSIGNMENT 	= "KeyboardAssignment";
	public final static String NAME_KNOBMAP_DUMP 		= "KnobMapDump";
	public final static String NAME_MODULE_DUMP 		= "ModuleDump";
	public final static String NAME_MORPHMAP_DUMP 		= "MorphMapDump";
	public final static String NAME_NAME_DUMP 			= "NameDump";
	public final static String NAME_NOTES 				= "Notes";
	public final static String NAME_PARAMETER_DUMP 		= "ParameterDump";

	// header_data
	public final static int HEADER_KEYBOARD_RANGE_MIN 				= 0;
	public final static int HEADER_KEYBOARD_RANGE_MAX 				= 1;
	public final static int HEADER_VELOCITY_RANGE_MIN 				= 2;
	public final static int HEADER_VELOCITY_RANGE_MAX 				= 3;
	public final static int HEADER_BEND_RANGE 						= 4;
	public final static int HEADER_PORTAMENTO_TIME 					= 5;
	public final static int HEADER_PORTAMENTO 						= 6;
	public final static int HEADER_REQUESTED_VOICES 				= 7;
	public final static int HEADER_SECTION_SEPARATOR_POSITION 		= 8;
	public final static int HEADER_OCTAVE_SHIFT 					= 9;
	public final static int HEADER_VOICE_RETRIGGER_POLY 			= 10;
	public final static int HEADER_VOICE_RETRIGGER_COMMON 			= 11;
	public final static int HEADER_UNKNOWN1 						= 12;
	public final static int HEADER_UNKNOWN2 						= 13;
	public final static int HEADER_UNKNOWN3 						= 14;
	public final static int HEADER_UNKNOWN4 						= 15;
	public final static int HEADER_CABLE_VISIBILITY_RED 			= 16;
	public final static int HEADER_CABLE_VISIBILITY_BLUE 			= 17;
	public final static int HEADER_CABLE_VISIBILITY_YELLOW 			= 18;
	public final static int HEADER_CABLE_VISIBILITY_GRAY 			= 19;
	public final static int HEADER_CABLE_VISIBILITY_GREEN 			= 20;
	public final static int HEADER_CABLE_VISIBILITY_PURPLE 			= 21;
	public final static int HEADER_CABLE_VISIBILITY_WHITE 			= 22;

	//moduleDump
	public final static int MODULE_DUMP_MODULE_INDEX 				= 0;
	public final static int MODULE_DUMP_MODULE_TYPE  				= 1;
	public final static int MODULE_DUMP_MODULE_XPOS  				= 2;
	public final static int MODULE_DUMP_MODULE_YPOS					= 3;
	
	// currentNoteDump
	public final static int CURRENT_NOTE_DUMP_NOTE 					= 0;
	public final static int CURRENT_NOTE_DUMP_ATTACK_VELOCITY 		= 1;
	public final static int CURRENT_NOTE_DUMP_RELEASE_VELOCITY 		= 2;
	
	// cableDump
	public final static int CABLE_DUMP_COLOR 						= 0;
	public final static int CABLE_DUMP_MODULE_INDEX_DESTINATION 	= 1;
	public final static int CABLE_DUMP_CONNECTOR_INDEX_DESTINATION	= 2;
	public final static int CABLE_DUMP_CONNECTOR_TYPE_DESTINATION	= 3;
	public final static int CABLE_DUMP_MODULE_INDEX_SOURCE 			= 4;
	public final static int CABLE_DUMP_CONNECTOR_INDEX_SOURCE		= 5;
	public final static int CABLE_DUMP_CONNECTOR_TYPE_SOURCE		= 6;

	// parameterDump
	public final static int PARAMETER_DUMP_MODULE_INDEX				= 0;
	public final static int PARAMETER_DUMP_MODULE_TYPE				= 1;
	public final static int PARAMETER_DUMP_PARAMETER_COUNT			= 2;
	public final static int PARAMETER_DUMP_PARAMETER_BASE			= 3; // index of first parameter

	// customDump
	public final static int CUSTOM_DUMP_MODULE_INDEX				= 0;
	public final static int CUSTOM_DUMP_PARAMETER_COUNT				= 1;
	public final static int CUSTOM_DUMP_PARAMETER_BASE				= 2; // index of first parameter
	
	// morphMapDump - knob values
	public final static int MORPH_MAP_DUMP_VALUES_MORPH1			= 0;
	public final static int MORPH_MAP_DUMP_VALUES_MORPH2			= 1;
	public final static int MORPH_MAP_DUMP_VALUES_MORPH3			= 2;
	public final static int MORPH_MAP_DUMP_VALUES_MORPH4			= 3;

	// morphMapDump
	public final static int MORPH_MAP_DUMP_SECTION					= 0;
	public final static int MORPH_MAP_DUMP_MODULE_INDEX				= 1;
	public final static int MORPH_MAP_DUMP_PARAMETER_INDEX			= 2;
	public final static int MORPH_MAP_DUMP_MORPH_INDEX				= 3;
	public final static int MORPH_MAP_DUMP_MORPH_RANGE				= 4;
	
	// keyboardAssignment
	public final static int KEYBOARD_ASSIGNMENT_MORPH1				= 0;
	public final static int KEYBOARD_ASSIGNMENT_MORPH2				= 1;
	public final static int KEYBOARD_ASSIGNMENT_MORPH3				= 2;
	public final static int KEYBOARD_ASSIGNMENT_MORPH4				= 3;

	// knobMapDump
	public final static int KNOB_MAP_DUMP_SECTION_INDEX				= 0;
	public final static int KNOB_MAP_DUMP_MODULE_INDEX				= 1;
	public final static int KNOB_MAP_DUMP_PARAMETER_INDEX			= 2;
	public final static int KNOB_MAP_DUMP_KNOB_INDEX				= 3;
	
	// ctrlMapDump
	public final static int CTRL_MAP_DUMP_SECTION_INDEX				= 0;
	public final static int CTRL_MAP_DUMP_MODULE_INDEX				= 1;
	public final static int CTRL_MAP_DUMP_PARAMETER_INDEX			= 2;
	public final static int CTRL_MAP_DUMP_CC_INDEX					= 3;

	// nameDump
	public final static int NAME_DUMP_MODULE_INDEX					= 0;
	public final static int NAME_DUMP_MODULE_NAME					= 1;
	
	public static String extractVersionName(String versionString)
	{
		int[] version = new int[2];
		extractVersionNumber(version, versionString);
		
		if (version[0]<0 || version[1]<0)
			return null;
		
		versionString = versionString.trim();
		String rem = version[0]+"."+version[1];
		versionString = versionString.substring(0, versionString.length()-rem.length());
		versionString = versionString.trim();
		return versionString.length()>0 ? versionString : null;
	}
	
	public static void extractVersionNumber(int[] version, String versionString)
	{
		version[0] = -1;
		version[1] = -1;
		
		if (versionString!=null)
		{
			versionString 	= versionString.trim();
			String[] tmp 	= versionString.split("\\s");
			if (tmp.length>1)
			{
				versionString 	= tmp[tmp.length-1];
				tmp = versionString.split("\\.");
				if (tmp.length==2)
				{
					if (tmp[0].matches("\\d+") && tmp[1].matches("\\d+"))
					{
						version[0] = Integer.parseInt(tmp[1]); // minor
						version[1] = Integer.parseInt(tmp[0]); // major
					}
				}
			}
		}
	}
	
	/**
	 * Returns the name of the given section or null if the section id is invalid.
	 * @param sectionId id of the section
	 * @return the name of the given section or null if the section id is invalid
	 */
	public final static String getName(int sectionId)
	{
		switch (sectionId) {
			case SEC_CABLE_DUMP:			return NAME_CABLE_DUMP;
			case SEC_CTRLMAP_DUMP:			return NAME_CTRLMAP_DUMP;
			case SEC_CURRENTNOTE_DUMP:		return NAME_CURRENTNOTE_DUMP;
			case SEC_CUSTOM_DUMP:			return NAME_CUSTOM_DUMP;
			case SEC_HEADER:				return NAME_HEADER;
			case SEC_KEYBOARDASSIGNMENT: 	return NAME_KEYBOARDASSIGNMENT;
			case SEC_KNOBMAP_DUMP:			return NAME_KNOBMAP_DUMP;
			case SEC_MODULE_DUMP:			return NAME_MODULE_DUMP;
			case SEC_MORPHMAP_DUMP:			return NAME_MORPHMAP_DUMP;
			case SEC_NAME_DUMP:				return NAME_NAME_DUMP;
			case SEC_NOTES:					return NAME_NOTES;
			case SEC_PARAMETER_DUMP:		return NAME_PARAMETER_DUMP;
			default:						return null;
		}
	}
	
	/**
	 * Returns the id of the section name. If the name is invalid a negative value is returned
	 * @param name name of the section
	 * @return the id of the section
	 */
	public final static int getSection(String name) {
		
		final int MIN_LEN = 	 3;
		final int INVALID =		-1;
		
		if (name.length()<MIN_LEN) return INVALID;
		
		switch (Character.toUpperCase(name.charAt(0)))
		{

			case 'H':					return name.equals(NAME_HEADER) 			? SEC_HEADER 			: INVALID;
			case 'P':					return name.equals(NAME_PARAMETER_DUMP) ? SEC_PARAMETER_DUMP 		: INVALID;
			case 'C':
				
				switch (Character.toUpperCase(name.charAt(1))) 
				{
					case 'A':			return name.equals(NAME_CABLE_DUMP) 		? SEC_CABLE_DUMP 		: INVALID;
					case 'T':			return name.equals(NAME_CTRLMAP_DUMP) 		? SEC_CTRLMAP_DUMP 		: INVALID;
					case 'U':	

						switch (Character.toUpperCase(name.charAt(2))) 
						{
							case 'R':	return name.equals(NAME_CURRENTNOTE_DUMP) 	? SEC_CURRENTNOTE_DUMP	: INVALID;
							case 'S':	return name.equals(NAME_CUSTOM_DUMP) 		? SEC_CUSTOM_DUMP		: INVALID;
							default:	return INVALID;
						}
						
					default: 	return INVALID;
				}

			case 'K':

				switch (Character.toUpperCase(name.charAt(1))) // char(1) == o
				{
					case 'E':			return name.equals(NAME_KEYBOARDASSIGNMENT) ? SEC_KEYBOARDASSIGNMENT: INVALID;
					case 'N':			return name.equals(NAME_KNOBMAP_DUMP) 		? SEC_KNOBMAP_DUMP		: INVALID;
					default: 			return INVALID;
				}
				
			case 'M':
				
				switch (Character.toUpperCase(name.charAt(2))) // char(1) == o
				{
					case 'D':			return name.equals(NAME_MODULE_DUMP) 		? SEC_MODULE_DUMP 		: INVALID;
					case 'R':			return name.equals(NAME_MORPHMAP_DUMP) 		? SEC_MORPHMAP_DUMP		: INVALID;
					default: 			return INVALID;
				}

			case 'N':
				
				switch (Character.toUpperCase(name.charAt(1)))
				{
					case 'A':			return name.equals(NAME_NAME_DUMP) 		? SEC_NAME_DUMP 			: INVALID;
					case 'O':			return name.equals(NAME_NOTES) 			? SEC_NOTES 				: INVALID;
					default: 			return INVALID;
				}
				
			default : 					return INVALID;
		}
	}

	private BufferedReader in;
	private int lineNumber = 0;
	private String currentLine = null;
	private PatchConstructorCallback303 cb;
	private int currentSection = -1;
	private String patch_name;

	public PatchFile303(String file, PatchConstructorCallback303 callBack) throws FileNotFoundException {
		this(new File(file), callBack);
	}
	
	public PatchFile303(File file, PatchConstructorCallback303 callBack) throws FileNotFoundException {
		this(new FileReader(file), callBack, NomadUtilities.removeFileExtension(file.getName()));
	}
	
	public PatchFile303(FileReader in, PatchConstructorCallback303 callBack, String patch_name) {
		this(new BufferedReader(in), callBack, patch_name);
	}
	
	public PatchFile303(BufferedReader in, PatchConstructorCallback303 callBack, String patch_name) {
		this.in = in;
		this.cb = callBack;
		this.patch_name=patch_name;
	}
	
	protected String readln() throws IOException {
		currentLine = in.readLine();
		lineNumber ++;
		return currentLine;
	}
	
	public void readAll() throws PatchConstructionException {
		read(SEC_ALL);
	}
	
	public static PatchConstructorCallback303.Adapter info(File file) {

		PatchConstructorCallback303.Adapter info = new PatchConstructorCallback303.Adapter();
		try {

			PatchFile303 pfile = new PatchFile303(file, info);

			if (pfile.readInformation())
				return info;

		} catch (FileNotFoundException e) {
			// ignore
		}
		
		return null;
	}

	public boolean readInformation() {
		try {
			read(SEC_INFORMATION);
			return true;
		} catch (Throwable t) {
			// t.printStackTrace();
			return false;
		}
	}

	public void read(int sections) throws PatchConstructionException {
		
		try {
			cb.patch_name(patch_name);
			readE(sections);
		} catch (PatchConstructionException e) {
			
			if (!e.isFormatted()) {
				PatchConstructionException converted = exception(e.getMessage());
				converted.setStackTrace(e.getStackTrace());
				e = converted;
			}
			
			throw e; // pass exception
		} catch (Throwable t) {
			throw exception(t);
		}
		
	}
	
	private void readE(int sections) throws IOException, PatchConstructionException {
		
		String line ;
		
		while ((line = readln())!=null)
		{
			// line = line.trim();
			
			if (line.length() > 0)
			{
				// read header name
				
				currentSection = sectionHeader(line, true/*expect begin header*/, -1/*nothing expected*/, true/* fail on syntax error */);
	
				if (currentSection< 0) {
					
					// unknown section
					
					String header = extractHeaderName( line, true );
					String end = "[/"+header+"]";
					String content = "";
					boolean isNotFirstLine = false;
	
					while ((line = readln())!=null)
					{	
						// line = line.trim();
						
						
						if (!line.equals(end)) {
	
							
							if (isNotFirstLine) {
								
								content += "\n";
								
							} else {
								isNotFirstLine = true;
							}
	
							content += line;
						}
						else {
							break;
						}					
					}
					
					if (!line.equals(end))
					{
						throw exception("Non standard header '"+header+"' was not closed.");
					}
					
					cb.unrecognizedSection(header, content);
					
				}
				else if ((currentSection & sections) > 0)
				{	// callback wants to be notified by content
					
					switch (currentSection)
					{
						case SEC_CABLE_DUMP:		readCableDump();		break;
						case SEC_CTRLMAP_DUMP:		readCtrlMapDump();		break;
						case SEC_CURRENTNOTE_DUMP:	readCurrentNoteDump();	break;
						case SEC_CUSTOM_DUMP:		readCustomDump();		break;
						case SEC_HEADER:			readHeaderDump();		break;
						case SEC_KEYBOARDASSIGNMENT:readKeyboardAssignment();break;
						case SEC_KNOBMAP_DUMP:		readKnobmapDump();		break;
						case SEC_MODULE_DUMP:		readModuleDump();		break;
						case SEC_MORPHMAP_DUMP:		readMorphMapDump();		break;
						case SEC_NAME_DUMP:			readNameDump();			break;
						case SEC_NOTES:				readNotesDump();		break;
						case SEC_PARAMETER_DUMP:	readParameterDump();	break;
					}
				} 
				else // ignore section 
				{
	
					while ((line = readln())!=null)
					{	
						// line = line.trim();
						
						
						if (	currentSection == sectionHeader(line, false/*expect end header*/, currentSection/* expexted */, false/* do not fail on syntax error */) )
	
							break; // done
						
					}
				}
			}
		}		
	}

	private final Pattern PT_NUMBER = Pattern.compile("\\d+");
	private final Pattern PT_SEPARATED_NUMBERS = Pattern.compile("(\\d+\\s+)*\\d+");

	private final String d  = "\\d+";
	private final String ds  = d+"\\s+";
	private final String neg = "(\\+|-)?";
	private final Pattern PT_MORPHMAP = Pattern.compile("("+ds+ds+ds+ds+neg+ds+")*"+ds+ds+ds+ds+neg+d); 
	private final Pattern PT_SPACES = Pattern.compile("\\s+"); 

	private boolean isNumber(String s) {
		return PT_NUMBER.matcher(s).matches();
	}
	
	/**
	 * Parses the string str which should be a list of numbers separated by whitespace.
	 * 
	 * @param dst
	 * @param str
	 * @return number of numbers that were parsed
	 */
	/*
	public static int parseNumberList(int[] dst, String str) {
		final int dlen = dst.length;
		final int slen = str.length();
		
		int dpos = 0; // array pos
		int spos = 0; // string pos
		
		char c;
		while (spos<slen) {
			c = str.charAt(spos);
			if (Character.isDigit(c)) {
				dst[dpos] = 0;
				do {
					dst[dpos] = (dst[dpos]*10)+(c-'0');
					spos ++;
				} while (spos<slen && Character.isDigit(c = str.charAt(spos)));
				dpos ++;
				if (dpos>=dlen) {
					for (;spos<slen;spos++) {
						c = str.charAt(spos);
						if (!(Character.isWhitespace(c) || Character.isDigit(c)))
							break;
					}
					break;
				}
			} else if (Character.isWhitespace(c)) {
				do {
					spos ++;
				} while (spos<slen && Character.isWhitespace(str.charAt(spos)));
			} else {
				break;
			}
		}

		return dpos;
	}*/
	
	/*
	private boolean isNumberList(String line) {
		return PT_SEPARATED_NUMBERS.matcher(line).matches();
	}*/

	private boolean isMorphMap(String line) {
		if (isNumberList(line)) {
			// every fifth number may be negative
			
			for (int i=0;i<tmpDataSize;i++)
				if (tmpData[i]<0 && i%4!=0)
					return false;
			
			return true;
		} else {
			return false;
		}
		
		//PT_MORPHMAP.matcher(line).matches();
	}
	
	public static int parseIntList(int[] dst, String src) {
		return parseIntList(dst, dst.length, src);
	}

	/**
	 * Reads string containing a whitespace separated list of numbers into an integer array.
	 * The string can contain whitespaces at the begining or at the end. Numbers can have
	 * a negative sign prefix, but not the positive sign prefix.
	 * 
	 * @param dst
	 * @param maxCount limits the number of digits that are read
	 * @param src
	 * @return returns a negative value if a parsing error occured.
	 * Otherwise the number of digits that were parsed are returned. The
	 * return value r will fullfill the condition 0&lt;= r &lt;= max(maxCount, dst.length())
	 */
	public static int parseIntList(int[] dst, int maxCount, String src) {
		
		final int STATE_WHITESPACE = 0; // \s  => STATE_WHITESPACE, '-' => STATE_NEGATIVE, '\d' => STATE_DIGIT
		final int STATE_NEGATIVE   = 1; // \d  => STATE_DIGIT
		final int STATE_DIGIT      = 2; // \s  => STATE_WHITESPACE, '\d' => STATE_DIGIT
		final int STATE_ERROR 	   =-1; // other transition
		final int STATE_DONE 	   =-2; // other transition
		
		int state = STATE_WHITESPACE;
		
		int slen = src.length();
		int spos = 0;
		char c;
		boolean negative = false;
		int number = 0 ;
		int index = 0;
		
		maxCount = Math.min(maxCount, dst.length);
		
		while ((state!=STATE_DONE) && (state!=STATE_ERROR)) {
			
			if (spos>=slen) {
				switch (state) {
					case STATE_WHITESPACE:
						state = STATE_DONE;
						break;
						
					case STATE_DIGIT:
						dst[index++] = negative ? -number : number;
						state = STATE_DONE;
						break;
	
					default:
						state = STATE_ERROR;
						break;
				}
				break;
			}
			
			c = src.charAt(spos++);
			
			switch (c) {
				case ' ':
				case '\t':
				case '\n':
				case  0x0B:
				case '\f':
				case '\r': // ' '\t\n\x0B\f\r (whitespace)

					switch (state) {
						case STATE_WHITESPACE: 
							break;
						case STATE_DIGIT: 
							// store number
							dst[index++] = negative ? -number : number;
							state = (index>=maxCount) ? state = STATE_DONE : STATE_WHITESPACE; 
							break;
						default:
							state = STATE_ERROR;
							break;
					}
					break;
					
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9': // '\d' (digit)
					
					switch (state) {
						case STATE_WHITESPACE:
							number = c - '0';
							state = STATE_DIGIT;
							negative = false;
							break;
							
						case STATE_NEGATIVE: 
							number = c - '0';
							state = STATE_DIGIT; 
							negative = true;
							break ;
							
						case STATE_DIGIT:
							number = (number*10)+(c-'0');
							break;

						default:
							state = STATE_ERROR;
							break;
					}
					
					break;
					
				case '-': // negative
					if (state == STATE_WHITESPACE)  {
						state = STATE_NEGATIVE;
					} else {
						state = STATE_ERROR;
					}
					break;

			}
		}
		
		return state == STATE_DONE ? index : STATE_ERROR; // index==count(parsed numbers)
	}
	
	/*
	private int[] parseIntegers(String[] numbers) {
		int[] data = new int[numbers.length];
		for (int i=numbers.length-1;i>=0;i--)
			data[i] = Integer.parseInt(numbers[i]);
		return data;
	}
	
	private int[] parseIntegers(String numberLine) {
		return parseIntegers( PT_SPACES.split(numberLine) );
	}*/
	
	private int[] tmpData = new int[100];
	private int tmpDataSize = 0;
	
	/**
	 * checks if str is a list containing only numbers and returns true if this is true
	 * and at least one number was found. The numbers are accessible through the
	 * tmpData array. tmpDataSize contains the number of valid fields.
	 * 
	 * @param str
	 * @return
	 */
	private boolean isNumberList(String str) {
		tmpDataSize = parseIntList(tmpData, str);
		return tmpDataSize>0;
	}
	
	protected String[] split(String numberLine) {
		return PT_SPACES.split(numberLine);
	}
	
	protected String[] split(String numberLine, int limit) {
		return PT_SPACES.split(numberLine, limit);
	}

	private void readHeaderDump() throws IOException, PatchConstructionException {
		String line;
		
		boolean versionLineFound = false;
		boolean dataLineFound = false;
		
		while ((line = readln())!=null)
		{
			// line = line.trim();
			
			
			if (line.startsWith("Version"))
			{
				if (versionLineFound)
					throw exception("more then one version entry found");
				
				versionLineFound = true;
				
				int eqIndex = line.indexOf('=');
				
				if (eqIndex<0)
					throw exception("version entry format error in section '"+SEC_HEADER+"'");
				
				String version = line.substring(eqIndex+1);
				cb.header_version(version);
				
			}
			else if (isNumberList(line))
			{
				if (dataLineFound)
					throw exception("more then one data entry found");

				dataLineFound = true;

				final int EXPECTED = 23;
				
				if (tmpDataSize != EXPECTED)
					throw exception("data format error in section '"+SEC_HEADER+"' (found "+tmpDataSize+" entries instead of "+EXPECTED+" )");	
				
				cb.header_data(tmpData);
				
			}
			else {
				
				break;

			}
			
		}
		
		// read closing header

		sectionHeader(line, false/*expect end header*/, SEC_HEADER/* expexted */, true/* fail on syntax error */);

		
	}

	private void readCustomDump() throws IOException, PatchConstructionException {
		String line;
		
		line = readln();
		// line = line.trim();
		
		
		boolean isPolySection = isPolySection(line);
		
		while ((line = readln())!=null)
		{

			// line = line.trim();
			
			
			if (isNumberList(line)) {

				final int EXPECTED = 3;
				
				if (tmpDataSize < EXPECTED) 
					throw exception("data format error in section '"+SEC_CUSTOM_DUMP+"' (found "+tmpDataSize+" entries instead of "+EXPECTED+" )");	
				
				cb.customDump(isPolySection, tmpData);
				
			} else {
				break;
			}
			
		}

		sectionHeader(line, false/*expect end header*/, SEC_CUSTOM_DUMP /* expexted */, true/* fail on syntax error */);

		
	}

	private void readParameterDump() throws IOException, PatchConstructionException {
		String line;
		
		line = readln();
		// line = line.trim();
		
		
		boolean isPolySection = isPolySection(line);
		
		while ((line = readln())!=null)
		{

			// line = line.trim();
			
			
			if (isNumberList(line)) {

				final int EXPECTED = 4;
				
				if (tmpDataSize < EXPECTED) 
					throw exception("data format error in section '"+SEC_PARAMETER_DUMP+"' (found "+tmpDataSize+" entries instead of "+EXPECTED+" )");	
				
				cb.parameterDump(isPolySection, tmpData);
				
			} else {
				break;
			}
			
		}

		sectionHeader(line, false/*expect end header*/, SEC_PARAMETER_DUMP /* expexted */, true/* fail on syntax error */);

		
	}
	
	private void readNotesDump() throws IOException, PatchConstructionException {
		String line;
		
		String notes = "";
		boolean isNotFirstLine = false;
		
		while ((line = readln())!=null)
		{
			if ( SEC_NOTES == sectionHeader(line.trim(), false/*expect end header*/, SEC_NOTES/* expexted */, false/* do not fail on syntax error */) ) {

				cb.notes(notes);
				
				return;  // done
			}
			else {
				
				if (isNotFirstLine) {
					
					notes += "\n";
					
				} else {
					isNotFirstLine = true;
				}

				notes += line;
			}
			
		}

		cb.notes(notes);
		throw exception("Expected end of section "+NAME_NOTES);
	}

	private void readMorphMapDump() throws IOException, PatchConstructionException {
		String line;
		
		line = readln();
		// line = line.trim();
		
		
		if (line == null || !isNumberList(line))
			throw exception("Expected morph knob settings in section "+NAME_MORPHMAP_DUMP);
		{
			final int EXPECTED = 4;
			
			if (tmpDataSize != EXPECTED)
				throw exception("data format error in section '"+NAME_MORPHMAP_DUMP+"' (found "+tmpDataSize+" entries instead of "+EXPECTED+" )");	
			
			cb.morphMapDumpMorphKnobValues(tmpData);
		}
		
		while ((line = readln())!=null)
		{

			// line = line.trim();
			
			if (isNumberList(line)) { // TODO only each 5th can be negative, not all

				final int EXPECTED = 5;
				
				if (tmpDataSize % EXPECTED != 0)
					throw exception("data format error in section '"+NAME_MORPHMAP_DUMP+"' (found "+tmpDataSize+" entries instead of "+EXPECTED+" )");	
				
				int [] morphE = new int[EXPECTED];
				
				for (int i=0;i<tmpDataSize;i+=EXPECTED) {
					for (int j=0;j<EXPECTED;j++)
						morphE[j] = tmpData[i+j];
					cb.morphMapDump(morphE);
				}
				
			} else {
				break;
			}
			
		}

		sectionHeader(line, false/*expect end header*/, SEC_MORPHMAP_DUMP/* expexted */, true/* fail on syntax error */);

		
	}
	
	private boolean isPolySection(String line) throws PatchConstructionException {

		if (isNumberList(line)) {

			if (tmpDataSize==1) {
				if (tmpData[0]==1) return true;
				else if (tmpData[0]==0) return false;
			}
			
		}

		throw exception(	"section "+NAME_MODULE_DUMP +" must start with '1' or '0'");
	}

	private void readModuleDump() throws IOException, PatchConstructionException {
		String line;

		// first line

		line = readln();
		// line = line.trim();
		

		boolean isPolySection = isPolySection(line);
		
		while ((line = readln())!=null)
		{

			// line = line.trim();
			
			
			if (isNumberList(line)) {

				final int EXPECTED = 4;
				
				if (tmpDataSize != EXPECTED)
					throw exception("data format error in section '"+SEC_MODULE_DUMP+"' (found "+tmpDataSize+" entries instead of "+EXPECTED+" )");	
				
				cb.moduleDump(isPolySection, tmpData);
				
			} else {
				break;
			}
			
		}

		sectionHeader(line, false/*expect end header*/, SEC_MODULE_DUMP/* expexted */, true/* fail on syntax error */);

		
	}

	private void readKnobmapDump() throws IOException, PatchConstructionException {
		String line;
		
		while ((line = readln())!=null)
		{

			// line = line.trim();
			

			if (isNumberList(line)) {
				final int EXPECTED = 4;
				
				if (tmpDataSize != EXPECTED)
					throw exception("data format error in section '"+NAME_KNOBMAP_DUMP+"' (found "+tmpDataSize+" entries instead of "+EXPECTED+" )");	
				
				cb.knobMapDump(tmpData);
			} else {
				break;
			}

		}
		
		sectionHeader(line, false/*expect end header*/, SEC_KNOBMAP_DUMP/* expexted */, true/* fail on syntax error */);

		
		
	}

	private void readKeyboardAssignment() throws IOException, PatchConstructionException {
		String line;
		
		line = readln();
		// line = line.trim();
		
		
		if (line == null || !isNumberList(line))
			throw exception("missing data in section "+NAME_KEYBOARDASSIGNMENT);
		{
			final int EXPECTED = 4;
			
			if (tmpDataSize != EXPECTED)
				throw exception("data format error in section '"+NAME_KEYBOARDASSIGNMENT+"' (found "+tmpDataSize+" entries instead of "+EXPECTED+" )");	
			
			cb.keyboardAssignment(tmpData);
		}

		line = readln();
		// line = line.trim();
		
		
		sectionHeader(line, false/*expect end header*/, SEC_KEYBOARDASSIGNMENT/* expexted */, true/* fail on syntax error */);

		
	}

	private void readNameDump() throws IOException, PatchConstructionException {
		String line;
		
		line = readln();
		// line = line.trim();
		
		
		boolean isPolySection = isPolySection(line);

		while ((line = readln())!=null)
		{
			// line = line.trim();
			
			
			if (line.length()>0 && line.charAt(0)!='[') {
				
				String[] data = split(line, 2);

				final int MIN_EXPECTED = 1;
				
				if (data.length < MIN_EXPECTED)
					throw exception("data format error in section '"+NAME_NAME_DUMP+"' (found "+data.length+" entries instead of "+MIN_EXPECTED+" )");	
				
				if (!isNumber(data[0]))
					throw exception("data format error in section '"+NAME_NAME_DUMP+"'. module index not a number");
				
				int moduleIndex = Integer.parseInt(data[0]);
				String moduleName = data.length>MIN_EXPECTED ? data[1] : "";
				
				cb.nameDump(isPolySection, moduleIndex, moduleName);
				
			} else {
				break;
			}
			
		}
		
		sectionHeader(line, false/*expect end header*/, SEC_NAME_DUMP/* expexted */, true/* fail on syntax error */);

		
	}

	private void readCurrentNoteDump() throws IOException, PatchConstructionException {
		String line;

		while ((line = readln())!=null)
		{

			// line = line.trim();
			
			
			if (isNumberList(line)) {

				final int EXPECTED = 3;
				
				if (tmpDataSize < EXPECTED) // < instead of != due to bug 
					throw exception("data format error in section '"+SEC_CURRENTNOTE_DUMP+"' (found "+tmpDataSize+" entries instead of "+EXPECTED+" )");	
				
				cb.currentNoteDump(tmpData);
				
			} else {
				break;
			}
			
		}

		sectionHeader(line, false/*expect end header*/, SEC_CURRENTNOTE_DUMP /* expexted */, true/* fail on syntax error */);

		
	}

	private void readCtrlMapDump() throws IOException, PatchConstructionException {
		String line;
		
		while ((line = readln())!=null)
		{

			// line = line.trim();
			

			if (isNumberList(line)) {
				final int EXPECTED = 4;
				
				if (tmpDataSize != EXPECTED)
					throw exception("data format error in section '"+NAME_CTRLMAP_DUMP+"' (found "+tmpDataSize+" entries instead of "+EXPECTED+" )");	
				
				cb.ctrlMapDump(tmpData);
			} else {
				break;
			}

		}
		
		sectionHeader(line, false/*expect end header*/, SEC_CTRLMAP_DUMP/* expexted */, true/* fail on syntax error */);

		
	}

	private void readCableDump() throws IOException, PatchConstructionException {
		String line;
		
		line = readln();
		// line = line.trim();
		
		
		boolean isPolySection = isPolySection(line);
		
		while ((line = readln())!=null)
		{

			// line = line.trim();
			
			
			if (isNumberList(line)) {

				final int EXPECTED = 7;
				
				if (tmpDataSize != EXPECTED) 
					throw exception("data format error in section '"+SEC_CABLE_DUMP+"' (found "+tmpDataSize+" entries instead of "+EXPECTED+" )");	
				
				cb.cableDump(isPolySection, tmpData);
				
			} else {
				break;
			}
			
		}

		sectionHeader(line, false/*expect end header*/, SEC_CABLE_DUMP /* expexted */, true/* fail on syntax error */);

		
	}

	private int sectionHeader(String line, boolean expectBegin, int expectedSection, boolean failOnSyntaxError) throws PatchConstructionException
	{

		final int MIN_LEN = 3;
		
		if (	line.length() <MIN_LEN)
			
			if(	failOnSyntaxError)	
				throw exception("header format error");
			else
				return -1;
		
		if (	line.charAt(0)	!='['	)

			if(	failOnSyntaxError)	
				throw exception("header must start with '['");
			else
				return -1;
		
		if (	line.charAt(line.length()-1)!=']'	)	
			
			if(	failOnSyntaxError)
				throw exception("header must end with ']'");
			else
				return -1;
		
		boolean isBeginSection = line.charAt(1) != '/';
		
		if (	isBeginSection	!=	expectBegin		)	throw exception("expected "+(expectBegin?"opening":"closing")+" header");
		
		String name = extractHeaderName( line, isBeginSection );
		
		int section = getSection(	name	);

		if (	(expectedSection	>=	0)	&&	(section	!=  expectedSection)	)
			throw exception("expected header name '"+getName(expectedSection)+"', but found '"+name+"'");

		return section;
		
	}
	
	private String extractHeaderName(String line, boolean isBeginSection) {

		return line.substring(isBeginSection ? 1 : 2, line.length()-1);
		
	}
	
	private PatchConstructionException exception(String message)
	{
		return new PatchConstructionException(
			"An exception occured at line:'"+lineNumber+"',section:'"+getName(currentSection)+"';"+message+"; line --> '"+currentLine+"'",
			true
		);
	}
	
	private PatchConstructionException exception(Throwable t)
	{
		return new PatchConstructionException(
			"An exception occured at line:'"+lineNumber+"',section:'"+getName(currentSection)+"'; line --> '"+currentLine+"'", t, true
		);
	}
	
}
