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
package net.sf.nmedit.jpdl2.impl;

import net.sf.nmedit.jpdl2.dom.PDLFunction;
import net.sf.nmedit.jpdl2.dom.PDLItemType;
import net.sf.nmedit.jpdl2.dom.PDLMultiplicity;
import net.sf.nmedit.jpdl2.dom.PDLVariable;
import net.sf.nmedit.jpdl2.utils.PDLUtils;

public class PDLVariableImpl extends PDLItemImpl implements PDLVariable
{
    
    private PDLItemType type;
    private String name;
    private int size;
    
    // implicit variable
    private PDLFunction function;
    
    // variable list
    private PDLMultiplicity multiplicity;
    private int terminal;
    private boolean hasTerminal = false;
    
    private PDLVariableImpl(PDLItemType type, String name, int size)
    {
        this.type = type;
        if (type == null)
            throw new NullPointerException("type must not be null");
        if (name == null)
            throw new NullPointerException("name must not be null");
        this.name = name;
        this.size = size;
    }
    
    public void setAnonym()
    {
        if (type == PDLItemType.AnonymousVariable)
            return;
        if (type == PDLItemType.ImplicitVariable)
        {
            this.type = PDLItemType.AnonymousVariable;
            return;
        }
        throw new IllegalStateException("no anonym equivalent for type: "+type);
    }
    
    public static PDLVariable create(String name, int size)
    {
        return new PDLVariableImpl(PDLItemType.Variable, name, size);
    }
    
    public static PDLVariable createVariableList(PDLVariable variable, PDLMultiplicity multiplicity)
    {
        return createVariableList(variable.getName(), variable.getSize(), multiplicity);
    }

    public static PDLVariable createVariableList(String name, int size, PDLMultiplicity multiplicity)
    {
        if (multiplicity == null)
            throw new NullPointerException();
        PDLVariableImpl v = new PDLVariableImpl(PDLItemType.VariableList, name, size);
        v.multiplicity = multiplicity;
        return v;
    }
    
    public static PDLVariable createImplicit(PDLVariable variable, PDLFunction function, boolean anonym)
    {
        return createImplicit(variable.getName(), variable.getSize(), function, anonym);
    }

    public static PDLVariable createImplicit(String name, int size, PDLFunction function, boolean anonym)
    {
        if (function == null)
            throw new NullPointerException();
        PDLVariableImpl v = new PDLVariableImpl(
                anonym ? PDLItemType.AnonymousVariable
                       : PDLItemType.ImplicitVariable, name, size);
        v.function = function;
        return v;
    }
    
    public PDLFunction getFunction()
    {
        return function;
    }

    public PDLMultiplicity getMultiplicity()
    {
        return multiplicity;
    }

    public String getName()
    {
        return name;
    }

    public int getSize()
    {
        return size;
    }

    public PDLVariable asVariable()
    {
        return this;
    }

    public PDLItemType getType()
    {
        return type;
    }

    public int getMinimumSize()
    {
        if (type == PDLItemType.VariableList)
        {
            int m = PDLUtils.getMinMultiplicity(multiplicity);
            if (m!=0 && hasTerminal) return size;
            return m * size;
        }
        else if (type == PDLItemType.AnonymousVariable)
            return 0;
        else
        {
            return size;
        }
    }
    
    public String toString()
    {
        String s = name+":"+size;
        if (type == PDLItemType.VariableList)
            return String.valueOf(multiplicity)+"*"+s;
        else if (type == PDLItemType.ImplicitVariable)
            return s+"=("+function+")";
        else if (type == PDLItemType.AnonymousVariable)
            return "%"+s+"=("+function+")";
        else
            return s;
    }

    public int getMinimumCount()
    {
        if (type == PDLItemType.VariableList)
        {
            int m = PDLUtils.getMinMultiplicity(multiplicity);
            if (m != 0 && hasTerminal) return 1;
            return m;
        }
        else if (type == PDLItemType.ImplicitVariable)
            return 0;
        else if (type == PDLItemType.AnonymousVariable)
            return 0;
        else if (type == PDLItemType.Variable)
            return 1;
        else
        {
            PDLUtils.unknownItemTypeError(this);
            return -1;
        }
    }

    public void setTerminal(Integer terminal)
    {
        if (terminal != null)
        {
            this.terminal = terminal;
            this.hasTerminal = true;
        }
        else
        {
            this.terminal = -1;
            this.hasTerminal = false;
        }
    }

    public boolean hasTerminal()
    {
        return hasTerminal;
    }
    
    public int getTerminal()
    {
        return terminal;
    }

}
