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

public class StringLexer implements SimpleLexer, CharSequence
{
    
    private CharSequence seq;
    private int pos;

    StringLexer()
    {
        setSource(null);
    }

    public StringLexer(CharSequence seq)
    {
        setSource(seq);
    }
    
    public void setSource(CharSequence source)
    {
        this.seq = source;
        this.pos = 0;
    }

    public int getPosition() 
    {
        return pos;
    }

    public String getCurrentState()
    {
        return "'"+seq.toString()+"';[length="+length()+",position="+getPosition()+"]";
    }

    public char tkCharacter() throws LexerException
    {
        expectRemaining();
        return seq.charAt(pos++); 
    }
    
    public void tkCharacter(char expect) throws LexerException 
    {
        char c = tkCharacter();
        if (c!=expect)
            throw new LexerException(this, "expected character '"+expect+"'");
    }

    public int tkDigit() throws LexerException
    {
        int digit = tkCharacter()-'0';
        if (digit<0 || digit>=10)
            throw new LexerException(this, "expected character [0-9]");
        
        return digit;
    }

    public int tkDigits() throws LexerException
    {
        int number = tkDigit();
        
        while (hasRemainingCharacters())
        {
            char c = seq.charAt(pos);
            if (Character.isDigit(c))
            {
                number = (number*10)+(c-'0');
                pos++;
            }
            else
            {
                break;
            }
        }
        
        return number;
    }

    public int tkInteger() throws LexerException
    {
        expectRemaining();
        boolean neg;
        switch (seq.charAt(pos))
        {
            case '-':{ neg = true;  pos++; break; }
            case '+':{ neg = false; pos++; break; }
            default :{ neg = false; break; }
        }

        int abs = tkDigits();
        return neg ? -abs : abs;
    }

    public int ignore( int count ) throws LexerException
    {
        if (count<0)
            throw new IllegalArgumentException("argument must be >=0");
        
        int ignored = Math.min(count, seq.length()-pos);
        pos += ignored;
        return ignored;
    }

    public int length()
    {
        return seq.length();
    }

    public char charAt( int index )
    {
        return seq.charAt(index);
    }

    public CharSequence subSequence( int start, int end )
    {
        return seq.subSequence(start, end);
    }
    
    public String toString()
    {
        return seq.toString();
    }

    public boolean hasRemainingCharacters()
    {
        return pos<length();
    }
    
    private void expectRemaining() throws LexerException
    {
        if (!hasRemainingCharacters())
        {
            throw new LexerException(this, "Unexpected end of sequence");
        }
    }

}
