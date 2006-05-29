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
 * Created on Mar 9, 2006
 */
package net.sf.nmedit.nomad.theme.property;

import java.awt.Font;

public abstract class FontValue extends Value
{
    private Font fontValue;

    public FontValue( Property property, Font font )
    {
        super( property, PropertyUtilities.encodeFont( font ) );
        this.fontValue = font;
    }

    public FontValue( Property property, String representation )
    {
        super( property, representation );
        fontValue = PropertyUtilities.decodeFont( representation );
    }

    public Font getFontValue()
    {
        return fontValue;
    }

}
