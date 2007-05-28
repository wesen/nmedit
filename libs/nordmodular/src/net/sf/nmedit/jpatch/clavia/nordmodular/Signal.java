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
 * Created on Mar 1, 2006
 */
package net.sf.nmedit.jpatch.clavia.nordmodular;

import java.awt.Color;

public enum Signal {
	
    AUDIO   (0, Color.decode("#CB4F4F"), "Audio"),   // 0 ~red/audio
    CONTROL (1, Color.decode("#5A5FB3"), "Control"), // 1 ~blue/control
    LOGIC   (2, Color.decode("#E5DE45"), "Logic"),   // 2 ~yellow/logic
    SLAVE   (3, Color.decode("#A8A8A8"), "Slave"),   // 3 ~gray/slave
    USER1   (4, Color.decode("#9AC899"), "User1"),   // 4 ~green/user1
    USER2   (5, Color.decode("#BB00D7"), "User2"),   // 5 ~purple/user2
    NONE    (6, Color.WHITE, "Loose Wire");          // 6 ~white/loose wire
    
    private final Color color ;
	private final int signalID;
    private final String name;

	private Signal(int signalID, Color color, String name) 
    {
		this.signalID = signalID;
		this.color   = color;
        this.name = name;
	}
    
    public final int getSignalID()
    {
        return signalID;
    }
    
    public final Color getColor() 
    {
        return color ;
    }
	
	public final Color getDefaultColor() 
    {
		return color ;
	}
    
    public String toString()
    {
        return name;
    }
	
	public static Signal bySignalID( int ID )
	{
		switch ( ID ) 
		{
			case 0: return AUDIO;
			case 1: return CONTROL;
			case 2: return LOGIC;
			case 3: return SLAVE;
			case 4: return USER1;
			case 5: return USER2;
			case 6: return NONE;	
			//default:return null ;
            default:throw new IllegalArgumentException("illegal color id:"+ID);
		}
	}

}
