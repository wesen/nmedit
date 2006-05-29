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
 * Created on May 15, 2006
 */
package net.sf.nmedit.nomad.theme.laf;

import java.awt.event.ActionListener;

import javax.swing.Timer;

public class HoldTimer extends Timer
{

    public final static int DEFAULT_DELAY = 120;
    public final static int DEFAULT_INITIAL_DELAY = 300;
    
    public HoldTimer( ActionListener listener )
    {
        this(DEFAULT_DELAY, listener);
        setRepeats(true);
    }

    public HoldTimer( int delay, ActionListener listener )
    {
        super( delay, listener );
        setInitialDelay(DEFAULT_INITIAL_DELAY);
        setCoalesce(true);
    }

}
