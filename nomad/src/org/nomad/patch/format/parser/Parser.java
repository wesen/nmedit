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
 * Created on Feb 28, 2006
 */
package org.nomad.patch.format.parser;

public class Parser {

	/**
	 * starting from pos, pos is incremented while any whitespace character is found.
	 * the position of the next non whitespace character is returned.
	 * 
	 * @param str
	 * @param pos position in str (pos<len)
	 * @param len length of str
	 * @return position of next non whitespace character. pos might be greater or equal than len 
	 */
	public final static int ignoreWhitespace(String str, int pos, int len)
	{
		while (pos<len)
		{
			if (Character.isWhitespace(str.charAt( pos ))) 
			{
				pos ++;
			} 
			else break ;

		}
		return pos ;
	}

	// same as above, but reads from pos to 0 (right to left), may return -1
	public final static int ignoreWhitespaceRewind(String str, int pos)
	{
		while (pos>=0)
		{
			if (Character.isWhitespace(str.charAt( pos ))) 
			{
				pos --;
			} 
			else break ;

		}
		return pos ;
	}

	/**
	 * reads digits from left to right and returns the integer number
	 * 
	 * @param str
	 * @param record
	 * @return -1 if no digit was found
	 */
	public final static int parseDigits(String str, Record record)
	{
		int number = 0;
		int spos = record.spos ;
		char c;
		while (spos < record.slen) 
		{
			c = str.charAt( spos );
			if (Character.isDigit(c))
			{
				number = (number*10)+(c-'0');
				spos ++;
			} 
			else break ;
		}
		
		if (record.spos != spos) 
		{
			record.spos = spos ;
			return number ;
		} 
		else return -1 ;
	}
	
	public final static int parseDigitsRewind(String str, Record record)
	{
		int number = 0;
		int spos = record.spos ;
		int shift = 1;
		char c;
		while (spos > 0) 
		{
			c = str.charAt( spos );
			if (Character.isDigit(c))
			{
				number = number + ((c-'0') * shift);
				shift ++ ;
				spos --;
			} 
			else break ;
		}
		
		if (record.spos != spos) 
		{
			record.spos = spos ;
			return number ;
		} 
		else return -1 ;
	}

	public final static boolean parseCharacterRewind(String str, char c, Record record)
	{
		if (record.spos < 0)
			return false;
		
		if (str.charAt(record.spos)!=c)
			return false;
		
		record.spos --;
		return true ;
	}
	
}
