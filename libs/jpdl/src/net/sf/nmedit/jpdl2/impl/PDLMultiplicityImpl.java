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

import net.sf.nmedit.jpdl2.PDLMultiplicity;

public class PDLMultiplicityImpl implements PDLMultiplicity
{

    public static final PDLMultiplicityImpl ONE = new PDLMultiplicityImpl(1);
    
    private String variable = null;
    private int constant = -1;

    public PDLMultiplicityImpl(String variable)
    {
        this.variable = variable;
    }
    
    public PDLMultiplicityImpl(int constant)
    {
        this.constant = constant;
    }

    public int getConstant()
    {
        return constant;
    }

    public String getVariable()
    {
        return variable;
    }

}
