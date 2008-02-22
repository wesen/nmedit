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

import java.util.List;

/**
 * A packet.
 */
public interface PDLPacket 
{

    int getVariable(String name);
    int getVariable(String name, int defaultValue);

    boolean hasVariable(String name);

    boolean hasPacket(String name);
    boolean hasVariableList(String name);
    boolean hasPacketList(String name);
    boolean hasString(String name);

    int[] getVariableList(String name);
    
    PDLPacket getPacket(String name);
    PDLPacket[] getPacketList(String name);
 
    String getName();
    
    String getBinding();
    
    String getString(String name);
    
    boolean containsPacket(String name);
    List<String> getAllVariables();
    List<String> getAllVariableLists();
    List<String> getAllPackets();
    List<String> getAllPacketLists();
    List<String> getAllStrings();
    
}
