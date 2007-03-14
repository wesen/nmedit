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
 * Created on Jun 11, 2006
 */
package net.sf.nmedit.jmisc.parsing;

public interface SimpleLexer
{

    /**
     * Returns the absolute position.
     * @return the absolute position
     * @throws LexerException
     */
    int getPosition();

    /**
     * Returns information on the lexer's state.
     * @return information on the lexer's state
     */
    String getCurrentState();

    /**
     * Reads a single digit.
     * Regular expression: <code>[0-9]</code>
     * @return a single digit.
     * @throws LexerException
     */
    int tkDigit() throws LexerException ;

    /**
     * Reads a sequence of digits containing at least one digit.
     * Regular expression: <code>[0-9]+</code>
     * @return Returns the value of the digit sequence
     * @throws LexerException
     */
    int tkDigits() throws LexerException ;
    
    /**
     * Reads optionally a sign and a sequence of digits containing at least one digit.
     * Regular expression: <code>(+|-)?[0-9]+</code>
     * @return number
     * @throws LexerException
     */
    int tkInteger() throws LexerException ;
    
    /**
     * Moves the position behind the specified number of characters.
     * The operation may ignore less than the specified number of charactars.
     * 
     * @param count
     * @return number of ignored characters
     * @throws LexerException
     */
    int ignore(int count) throws LexerException ;

    /**
     * Returns <code>true</code> when some characters remain.
     * @return <code>true</code> when some characters remain.
     */
    boolean hasRemainingCharacters();
    
    /**
     * Reads the next character.
     * @return the next character
     * @throws LexerException
     */
    char tkCharacter() throws LexerException ;

    /**
     * Reads the next character.
     * The operation fails when the next character is different than the specified character.
     * @param expect
     * @throws LexerException 
     */
    void tkCharacter(char expect) throws LexerException ;
    
}
