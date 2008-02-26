/*
    Protocol Definition Language
    Copyright (C) 2003-2006 Marcus Andersson

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/
package net.sf.nmedit.jpdl2.dom;

/**
 * Defines the multiplicity for some elements.
 */
public interface PDLMultiplicity
{

    /**
     * Identifies the type of a multiplicity declaration.
     */
    public enum Type
    {
        /**
         * constant multiplicity
         */
        Constant,
        
        /**
         * variable multiplicity
         */
        Variable
        /*
        NoneOrMany,
        OneOrMany */
        
        
        /**
         * 5*packet
         * v*packet
         * {5,v}*packet
         * (+packet)
         * (*packet)
         */
        
    }
    
    /**
     * Returns the type of this multiplicity declaration.
     * @return the type of this multiplicity declaration
     */
    Type getType();

    /**
     * Returns the constant-multiplicity value.
     * If the {@link #getType() type} is not
     * {@link Type#Constant Constant} then the return value is undefined.
     * @return returns the constant-multiplicity value
     */
    int getConstant();

    /**
     * Returns the variable name which contains the multiplicity value.
     * If the {@link #getType() type} is not
     * {@link Type#Variable Variable} then the return value is undefined.
     * @return returns the variable name
     */
    String getVariable();
    
}
