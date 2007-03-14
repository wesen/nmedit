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
 * Created on Dec 11, 2006
 */
package net.sf.nmedit.jpatch.clavia.nordmodular;

import net.sf.nmedit.jpatch.Parameter;
import net.sf.nmedit.jpatch.ParameterDescriptor;

public class MorphKBAssignmentDescriptor implements ParameterDescriptor
{

    public static final int NONE = 0;
    public static final int VELOCITY = 1;
    public static final int NOTE = 2;
    public static final String SNONE = "None";
    public static final String SVELOCITY = "Velocity";
    public static final String SNOTE = "Note";

    public static final String PARAMETER_CLASS = "morphkb";
    
    private int morphID;
    private MorphSectionDescriptor module;

    public MorphKBAssignmentDescriptor(MorphSectionDescriptor module, int morphID)
    {
        this.morphID = morphID;
        this.module = module;
    }
    
    public int getMorphId()    
    {
        return morphID;
    }

    public int getDefaultValue()
    {
        return NONE;
    }

    public int getRange()
    {
        return getMaxValue()-getMinValue()+1;
    }

    public int getMinValue()
    {
        return 0;
    }

    public int getMaxValue()
    {
        return 2;
    }

    public ParameterDescriptor getSourceDescriptor()
    {
        return null;
    }

    public String getParameterClass()
    {
        return PARAMETER_CLASS;
    }

    public MorphSectionDescriptor getModuleDescriptor()
    {
        return module;
    }

    public String getComponentName()
    {
        return "Keyboard Assignment "+morphID;
    }

    public Object getAttribute( String name )
    {
        return null;
    }

    public Object[] getAttributes()
    {
        return MorphDescriptor.EMPTY;
    }

    public int getAttributeCount()
    {
        return 0;
    }

    public String getName()
    {
        return getClass().getName();
    }

    public String getFormattedValue( int value )
    {
        return getFormattedValue(null, value);
    }

    public String getFormattedValue( Parameter parameter, int value )
    {
        switch (value)
        {
            case NONE: return SNONE;  
            case VELOCITY: return SVELOCITY;
            case NOTE: return SNOTE;
            default: return "error";
        }
    }

    public int getIndex()
    {
        return morphID;
    }

}
