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
 * Created on Apr 6, 2006
 */
package net.sf.nmedit.patch.parser;


public abstract class WrappedPatchParser extends PatchParser
{
    
    protected PatchParser parser;

    public WrappedPatchParser(PatchParser parser)
    {
        if (parser == null)
            throw new NullPointerException();
        this.parser = parser;
    }

    @Override
    public int getSectionID()
    {
        return parser.getSectionID();
    }

    @Override
    public int getValueCount()
    {
        return parser.getValueCount();
    }

    @Override
    public int getValue( int index )
    {
        return parser.getValue( index );
    }

    @Override
    public String getString()
    {
        return parser.getString();
    }

    @Override
    public void setSource( Object source ) throws PatchParserException
    {
        parser.setSource( source );
    }

    @Override
    public int getTokenType()
    {
        return parser.getTokenType();
    }

    @Override
    public int getRecordIndex()
    {
        return parser.getRecordIndex();
    }

}
