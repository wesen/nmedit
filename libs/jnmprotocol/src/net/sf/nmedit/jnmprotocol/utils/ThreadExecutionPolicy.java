/*
    Nord Modular Midi Protocol 3.03 Library
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

package net.sf.nmedit.jnmprotocol.utils;

/**
 * Controls the execution of a {@link net.sf.nmedit.jnmprotocol.utils.StoppableThread}
 */
public interface ThreadExecutionPolicy
{

    /**
     * Delays the execution of the runnable each time after it was called in the thread loop. 
     */
    void delay()
        throws InterruptedException;

    /**
     * Interrups the blocking delay() method.
     */
    void interruptDelay();
    
    /**
     * If true this indicates that the thread shouln't be executed anymore.
     */
    boolean exitRequested();
    
    /**
     * Returns true if the runnable should be called in the current thread loop cycle.
     */
    boolean executionRequested();
    
}
