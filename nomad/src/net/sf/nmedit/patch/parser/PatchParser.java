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
 * Created on Apr 5, 2006
 */
package net.sf.nmedit.patch.parser;

import net.sf.nmedit.patch.Format;

public abstract class PatchParser
{

    public final static int TK_END_OF_DOCUMENT = -1;
    public final static int TK_SECTION_START = 0;
    public final static int TK_SECTION_END   = 1;
    public final static int TK_RECORD        = 2;

    /**
     * Reads the next token and returns its ID.
     * 
     * The return value is 
     * 
     * <ul>
     * <li>{@link #TK_SECTION_START} when a section header was parsed</li>
     * <li>{@link #TK_SECTION_STOP} when a closing section header was parsed</li>
     * <li>{@link #TK_SECTION_END} when a record of the current section was parsed</li>
     * <li>{@link #TK_END_OF_DOCUMENT} when the document is completed</li>
     * </ul>
     * 
     * @return ID of the next token
     */
    public abstract int nextToken() throws PatchParserException;

    /**
     * Returns the ID of the current section. If no section has
     * been parsed a negative value will be returned.
     * 
     * @return the ID of the current section
     */
    public abstract int getSectionID();

    /**
     * Returns the number of available integers in the current record. 
     * 
     * @return number of available integers in the current record
     */
    public abstract int getValueCount();

    /**
     * Returns a value of the current record. The value
     * is selected by it's index. Possible indices depend
     * on the current section ID and are declared
     * in {@link Format}..
     * 
     * @param index index of the requested value
     * @return a value of the current record
     */
    public abstract int getValue(int index);

    /**
     * Returns the string value of a record if available or otherwise an empty string.
     * @return the string value of a record if available or otherwise an empty string
     */
    public abstract String getString();
    
    public abstract void setSource(Object source)
        throws PatchParserException;
    
    /**
     * Returns the token type returned by the last call to {@link #nextToken()}.
     * If {@link #nextToken()} was not called, {@link #TK_END_OF_DOCUMENT}
     * is returned.
     * @return the token type returned by the last call to {@link #nextToken()}
     */
    public abstract int getTokenType();
    
    /**
     * Returns the index of the current record.
     * @return the index of the current record
     */
    public abstract int getRecordIndex();
 
    public String toString()
    {
        return getClass().getSimpleName();
    }

}
