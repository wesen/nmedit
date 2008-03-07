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
 * Created on Dec 19, 2006
 */
package net.sf.nmedit.jpatch.clavia.nordmodular.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;

/**
 * Lexer for Nord Modular 3 Patch files.
 * 
 * @author Christian Schneider
 */
public final class PScanner
{
    
    // white space characters
    
    // HORIZONTAL TABULATION
    public static final int C_WS_HT = '\u0009';
    // LINE FEED \u000A
    public static final int C_WS_LF = 10;
    // VERTICAL TABULATION
    public static final int C_WS_VT = '\u000B';
    // FORM FEED
    public static final int C_WS_FF = '\u000C'; 
    // CARRIAGE RETURN \u000D
    public static final int C_WS_CR = 13;
    // FILE SEPARATOR
    public static final int C_WS_FS = '\u001C';
    // GROUP SEPARATOR
    public static final int C_WS_GS = '\u001D';
    // RECORD SEPARATOR
    public static final int C_WS_RS = '\u001E';
    // UNIT SEPARATOR
    public static final int C_WS_US = '\u001F';
    // SPACE
    public static final int C_WS_SP = '\u0020'; 

    // end of file token / internal end of file character
    public static final int EOF = -1;
    // open bracket token
    public static final int BROPEN  = '[';
    // close bracket token
    public static final int BRCLOSE = ']';
    // slash token
    public static final int SLASH   = '/';
    // equal token
    public static final int EQ      = '=';

    // token classes
    public static final int BASE      = 1000;
    // inline whitespace 
    public static final int INLINEWS  = BASE+0;
    // newline characters \n,\r
    public static final int NEWLINEWS = BASE+1;
    // number token '0' | ('-'? [1..9] [0..9]+ )
    public static final int NUMBER    = BASE+2;
    // anything else - string token
    public static final int ANY       = BASE+3;
    
    private final static int TAKEN = EOF;
    
    // the reader
    private Reader reader;
    // buffer for string tokens
    private StringBuilder sbuffer;
    // buffer for numbers
    private int ibuffer;
    // current character
    private int cbuf;
    // current line
    private int line;
    // current position
    private int position;
    // start of the newline
    private int newlineposition;

    public PScanner()
    {
        sbuffer = new StringBuilder();
    }
    
    /**
     * Creates a new lexer that reads from the specfied input stream.
     * @param stream the source
     */
    public PScanner(InputStream stream)
    {
        this();
        setSource(stream);
    }
    
    /**
     * A lexer that reads from the specified reader
     * @param reader the source
     */
    public PScanner(Reader reader)
    {
        this();
        setSource(reader);
    }
    
    public void setSource(InputStream stream)
    {
        try
        {
            Charset c = Charset.forName("ISO-8859-1");
            setSource(new InputStreamReader(stream, c));
            return;
        }
        catch (IllegalCharsetNameException e)
        {
            
        }
        catch (UnsupportedCharsetException e)
        {
            
        }
        
        setSource(new InputStreamReader(stream));
    }
       
    public void setSource(Reader reader)
    {
        this.reader = reader;
        sbuffer.setLength(0);
        line = 1;
        newlineposition = 0;
        position = 0;
        ibuffer = 0;
        take();
    }

    public final int getPosition()
    {
        return position;
    }
    
    /**
     * Returns the current line number.
     * @return the current line number
     */
    public final int getLineNumber()
    {
        return line;
    }
    
    /**
     * Returns the current column number
     * @return the current column number
     */
    public final int getColumn()
    {
        return position-newlineposition;
    }
    
    /**
     * Returns the current string token.
     * 
     * Any token except the number token
     * and the newline whitespace token
     * have a string value. For other tokens
     * the return value is undefined.
     * 
     * @return the current string token
     */
    public final String getString()
    {
        return sbuffer.toString();
    }
    
    /**
     * Returns the current number token or
     * the number of newlines if the current token
     * is a newline whitespace token.
     * 
     * For any other token the return value is undefined.
     * 
     * @return the current number token or the number of newlines
     */
    public final int getNumber()
    {
        return ibuffer;
    }

    /**
     * Returns the next token in the stream.
     * 
     * Defined tokens are
     * <ul>
     * <li>EOF - if the end of the file was reached</li>
     * <li>BROPEN - the '[' character was read</li>
     * <li>BRCLOSE - the ']' character was read</li>
     * <li>SLASH - the '/' character was read</li>
     * <li>EQ - the '=' character was read</li>
     * <li>INLINEWS - whitespace characters except of '\r' '\n' were read</li>
     * <li>NEWLINE - the expression ('\r'|'\n')+ was read</li>
     * <li>NUMBER - a number was read</li>
     * <li>ANY - any other character(s) / string token</li>
     * </ul>
     *
     * The value of any token except of NUMBER and NEWLINE tokens
     * is returned by {@link #getString()}.
     * 
     * The number in a NUMBER token is returned by {@link #getNumber()}
     * 
     * The number of newlines in a NEWLINE token is
     * returned by {@link #getNumber()}. 
     * 
     * @return
     * @throws IOException
     */
    public final int nextToken() throws IOException
    {
        sbuffer.setLength(0);
        ibuffer = 0;
        int tmpToken;

        switch (next())
        {
            case'-':
            case'0':case'1':case'2':case'3':case'4':
            case'5':case'6':case'7':case'8':case '9':
                return number();
            case'[':case']':case'/':case'=':
                tmpToken = cbuf;
                appendAndTake();
                return tmpToken;
            case C_WS_HT:case C_WS_VT:case C_WS_FF:case C_WS_FS:
            case C_WS_GS:case C_WS_RS:case C_WS_US:case C_WS_SP:
                for(;;) {
                    appendAndTake();
                    switch (next())
                    {
                        case C_WS_HT:case C_WS_VT:case C_WS_FF:case C_WS_FS:
                        case C_WS_GS:case C_WS_RS:case C_WS_US:case C_WS_SP: 
                            break;
                        default:
                            return INLINEWS;
                    }
                }
            case C_WS_CR:case C_WS_LF: 
                return newlinews();
            case EOF:
                return EOF;
            default:
                return any();
        }
    }
    
    /**
     * marks the current character as read
     */
    private final void take()
    {
        cbuf = TAKEN;
    }
    
    /**
     * Returns the next character.
     * 
     * A new character is only read from the stream if {@link #take()}
     * has been called before.
     *  
     * @return the next character
     * @throws IOException reading from the stream failed
     */
    private final int next() throws IOException
    {
        if (cbuf!=TAKEN)
            return cbuf;

        cbuf = reader.read();
        if (cbuf!=TAKEN) position++;
        return cbuf;
    }
    
    /**
     * Appends the current character to the string buffer and 
     * marks the character as read by calling {@link #take()}
     */
    private final void appendAndTake() 
    {
        sbuffer.append((char)cbuf);
        cbuf = TAKEN;
    }
    
    /**
     * Reads (\r|\n)+
     * 
     * @return NEWLINEWS token
     * @throws IOException
     */
    private final int newlinews() throws IOException
    {
        loop: 
        for (boolean skipLF = false;;take())
        {
            if (skipLF)
            {
                skipLF = false;
                if (next() == C_WS_LF) 
                    continue;
            }
            switch (next())
            {
                case C_WS_CR: skipLF = true;   // fall through
                case C_WS_LF: ibuffer++; break;
                default:      break loop;
            }
        } 
        line+=ibuffer;
        newlineposition = position;
        return NEWLINEWS;
    }
    
    private final int abortNumber(int charCount, boolean sign) throws IOException
    {
        if (charCount == 0)
            return any();
        
        if (sign)
        {
            sbuffer.append('-');
            if (charCount == 1)
                return any();
        }
        sbuffer.append(Integer.toString(ibuffer));
        return any();
    }
    
    /**
     * Reads the next number or string.
     * 
     * If the next characters match the regular expression
     * <code>'0' | ('-'? [1..9] [0..9]+ )</code> and the
     * expression is followed by a whitespace or end of file
     * character then the NUMBER token is returned. Otherwise
     * the ANY token is returned.
     *  
     * @return the next number or string
     * @throws IOException
     */
    private final int number() throws IOException
    {
        /*
         * for testing
         *   0 1 -0 -1
         *   -00 -01 -10 -11
         *   0-0 0-1 1-0 1-1
         *   00- 01- 10- 11-
         *   0--0 0--1 1--0 1--1
         */
        
        boolean sign = false;
        final int first = getPosition();
        final int second = first+1;

        // read the number
        loop:for(;;)
        {
            switch (next())
            {
                case'0':
                    if (getPosition() == second)
                    {
                        if (sign)
                        {
                            // -0
                            take();
                            return abortNumber(getPosition()-first, sign);
                        }
                        if (ibuffer==0)
                        {
                            // 00
                            return abortNumber(getPosition()-first, sign);
                        }
                    }
                    
                    
                    // fall down
                case'1':case'2':case'3':case'4':
                case'5':case'6':case'7':case'8':case '9':
                    
                    if (getPosition()>first && ibuffer==0 && !sign)
                    {
                        return abortNumber(getPosition()-first, sign);
                    }
                    
                    int newbuffer = (ibuffer*10)+(cbuf-'0');
                    // check for overflow (not a number)
                    if (newbuffer<ibuffer) return abortNumber(getPosition()-first, sign);
                    // no overflow
                    ibuffer = newbuffer;
                    break;
                case '[': case EOF: case C_WS_CR:case C_WS_LF:
                case C_WS_HT:case C_WS_VT:case C_WS_FF:case C_WS_FS:
                case C_WS_GS:case C_WS_RS:case C_WS_US:case C_WS_SP:
                    // whitespace|eof|'['
                    break loop;
                case '-':
                    // _, --, [0-9]-
                    if (getPosition() == first)
                    {
                        sign = true;
                        break;
                    }
                    // fall down
                default:
                    // not a number
                    return abortNumber(getPosition()-first, sign);
                    
            }
            take();
        }
        
        if (getPosition()-first == 1 && sign)
        {
            return abortNumber(getPosition()-first, sign);
        }
        
        if (sign) ibuffer = -ibuffer;
        return NUMBER;
    }

    /**
     * Matches any character except whitespace, end of file
     * or one of the special characters '[', '], '/', '='
     * 
     * @return ANY
     * @throws IOException
     */
    private final int any() throws IOException
    {
        for(;;)
        {
            switch (next())
            {
                case EOF:
                case C_WS_CR:case C_WS_LF:
                case C_WS_HT:case C_WS_VT:case C_WS_FF:case C_WS_FS:
                case C_WS_GS:case C_WS_RS:case C_WS_US:case C_WS_SP:
                case '[':case ']':case '/':case '=':
                    return ANY;
                default:
                    appendAndTake();
                    // split strings which become too long
                    if (sbuffer.length()>=100)
                        return ANY;
                    break;
            }
        }
    }
    
}
