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
 * Created on Mar 2, 2006
 */
package net.sf.nmedit.nomad.theme.property;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.DModule;
import net.sf.nmedit.nomad.theme.ModuleComponent;
import net.sf.nmedit.nomad.theme.component.NomadComponent;
import net.sf.nmedit.nomad.theme.property.editor.Editor;


/**
 * The Property class creates the properties value by a given value
 * representation string or by extracting the value from a component.
 * 
 * @see net.sf.nmedit.nomad.theme.property.Value
 * @author Christian Schneider
 */
public abstract class Property
{
    private final String name;

    /**
     * Creates a new property.
     * 
     * @param name
     *            Name of the property.
     */
    public Property( String name )
    {
        this.name = name;
    }

    /**
     * Returns the property name
     * 
     * @return the property name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Creates a value object from a given value representation string
     * 
     * @param value
     *            representation string for the given value
     * @return a value object created from a given value representation string
     */
    public abstract Value decode( String value );

    /**
     * Extracts the current value from the given component
     * 
     * @param component
     *            the component from which the value is read
     * @return value extracted from the given component
     */
    public abstract Value encode( NomadComponent component );

    /**
     * Creates an editor for the component's property.
     * 
     * @param component
     *            editing component
     * @return editor for the component's property
     */
    public abstract Editor newEditor( NomadComponent component );

    public boolean equals( Object obj )
    {
        return ( obj != null && getClass().equals( obj.getClass() ) );
    }

    public DModule findModuleInfo(NomadComponent c)
    {
        DModule info = null;

        if (c instanceof ModuleComponent)
            return ((ModuleComponent) c).getModuleInfo();

        if (c.getParent() instanceof ModuleComponent)
            return ((ModuleComponent) c.getParent()).getModuleInfo();

        return info;
    }
}
