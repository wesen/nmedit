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

import net.sf.nmedit.nomad.theme.component.NomadComponent;

/**
 * Represents the value of a component's property.
 * 
 * @see net.sf.nmedit.nomad.theme.property.Property
 * @author Christian Schneider
 */
public abstract class Value
{

    private final String   representation;

    private final Property property;

    private boolean        inDefaultState;

    private int hashCode;
    
    /**
     * Creates a new value object for the given property.
     * 
     * @param property
     *            the value's property
     * @param representation
     *            string representation of the value
     */
    public Value( Property property, String representation )
    {
        inDefaultState = false;
        this.property = property;
        this.representation = representation;
        hashCode = representation == null ? 0 : representation.hashCode();
    }

    /**
     * Returns the property this value belongs to
     * 
     * @return the property this value belongs to
     */
    public Property getProperty()
    {
        return property;
    }

    /**
     * Returns the value's representation string
     * 
     * @return the value's representation string
     */
    public String getRepresentation()
    {
        return representation;
    }

    /**
     * Sets the default state property.
     * 
     * @param inDefaultState
     * @see #isInDefaultState()
     */
    protected void setDefaultState( boolean inDefaultState )
    {
        this.inDefaultState = inDefaultState;
    }

    /**
     * Returns true if this is the property's default value.
     * 
     * @return true if this is the property's default value
     */
    public boolean isInDefaultState()
    {
        return inDefaultState;
    }

    /**
     * Sets the component's property to this value.
     * 
     * @param component
     *            the modified component
     */
    public abstract void assignTo( NomadComponent component );

    public String toString()
    {
        return getRepresentation();
    }
    
    public int hashCode()
    {
        return hashCode;
    }

    public boolean equals( Object obj )
    {
        if (!super.equals( obj ))
        {
            if (obj instanceof Value)
            {
                Value v = (Value) obj;
                if (getProperty().equals( v.getProperty() ))
                {
                    return getRepresentation() == v.getRepresentation()
                            || getRepresentation().equals(
                                    v.getRepresentation() );
                }
            }
            return false;
        }
        else return true;
    }

}
