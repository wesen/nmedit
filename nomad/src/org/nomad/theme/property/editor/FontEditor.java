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
package org.nomad.theme.property.editor;

import org.nomad.dialog.JFontChooser;
import org.nomad.theme.component.NomadComponent;
import org.nomad.theme.property.FontProperty;
import org.nomad.theme.property.Value;

public class FontEditor extends Editor
{
    private JFontChooser fontChooser = null;

    public FontEditor( FontProperty property, NomadComponent component )
    {
        super( property, component, true );
        fontChooser = new JFontChooser( property.encodeFont( component )
                .getFontValue() );
    }

    public JFontChooser getFontChooser()
    {
        return fontChooser;
    }

    public FontProperty getFontProperty()
    {
        return (FontProperty) getProperty();
    }

    @Override
    public Value getValue()
    {
        return getFontProperty().encodeFont( fontChooser.getFont() );
    }

}
