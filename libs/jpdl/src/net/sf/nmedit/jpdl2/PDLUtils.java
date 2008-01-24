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
package net.sf.nmedit.jpdl2;

public class PDLUtils
{
    
    public static String getBinding(PDLPacketRef packetReference)
    {
        String binding = packetReference.getBinding();
        if (binding == null)
            binding = packetReference.getPacketName();
        return binding;
    }

    public static int getMinMultiplicity(PDLMultiplicity multiplicity)
    {
        if (multiplicity.getConstant()>=0)
            return multiplicity.getConstant();
        else
            return 0;
    }

    public static int getMultiplicity(PDLPacket context, PDLMultiplicity multiplicity)
    {
        if (multiplicity == null)
            return 1;
        if (multiplicity.getConstant()>=0)
            return multiplicity.getConstant();
        else
            return context.getVariable(multiplicity.getVariable());
    }
    
}
