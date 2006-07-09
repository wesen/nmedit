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
 * Created on Apr 5, 2006
 */
package net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.io;

import java.io.IOException;
import java.io.Reader;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Format;

/**
 * Patch parser implementation that parses the patch file
 * using the given {@link java.io.Reader}
 * 
 * @author Christian Schneider
 */
public class PatchFileParser extends PatchParser
{

    // default buffer size
    private static final int DEFAULT_BUFFER_SIZE        = 8192;

    // max value count
    private static final int DEFAULT_VALUE_COUNT        = 100;

    // default size of the string buffer
    private static final int DEFAULT_STRING_BUFFER_SIZE = 200;

    // source buffer
    private char[]           buffer;

    // number of valid characters in buffer
    private int              limit;

    // current position
    private int              pos;

    // character at current position
    private char             ch;

    // buffer for string value
    private StringBuffer     str;

    // source
    private Reader           source;

    // line number >= 1
    private int              line;

    // column of current line >= 0
    private int              col;

    // ID of the current section
    private int              sectionID;

    // list of parsed integers
    private int[]            values;

    // number of valid integers in values
    private int              valueCount;

    // current token type
    private int              tkType;

    // index of current record
    //private int              recordIndex;
    
    // storage for the header name of an unknown section
    private StringBuffer     unsupported;

    PatchFileParser()
    {
        this.buffer = new char[DEFAULT_BUFFER_SIZE];
        this.str = new StringBuffer( DEFAULT_STRING_BUFFER_SIZE );
        this.values = new int[DEFAULT_VALUE_COUNT];
        this.source = null;
        this.unsupported = new StringBuffer();
        reset();
    }
    

    public PatchFileParser(Reader source)
    {
       this();
       this.source = source;
    }

    /*public void setSource( Reader source )
    {
        this.source = source;
        reset();
    }*/

    private void reset()
    {
        this.pos = 0;
        this.limit = 0;
        this.line = 1;
        this.col = -1;
        this.sectionID = -1;
        //this.recordIndex = -1;
        tkType = TK_END_OF_DOCUMENT;
        clearBuffers();
    }

    private void clearBuffers()
    {
        valueCount = 0;
        if (str.length()>0)
        {
            str.delete( 0, str.length() );
        }
    }

    /**
     * Refills the buffer and returns true when the buffer is not empty.
     * 
     * @return true, when the refilled buffer is not empty
     * @throws IOException
     */
    private boolean refill() throws IOException
    {
        // buffer is empty

        limit = source.read( buffer );
        pos = 0;
        if (limit > 0)
        {
            return true;
        }
        else
        // limit == 0 should not occure, but is similar to limit == -1
        {
            // end of stream has been reached
            limit = 0;
            return false;
        }
    }

    /**
     * Returns true when at least one character is available. The character will
     * be stored in the field {@link #ch}.
     * 
     * @return true when at least one character is available
     * @throws IOException
     */
    private boolean align() throws IOException
    {
        if (pos < limit || refill())
        {
            ch = buffer[pos++];
            col++;
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * True when a newline character sequence was found. The character sequence
     * will be read when the current character is <code>'\r'</code> or
     * <code>'\n'</code>. If the sequence is not valid a
     * {@link PatchParserException} will be thrown. An exception might also be
     * thrown when the underlying reader threw an exception.
     * 
     * @return true when a newline character sequence was found.
     * @throws PatchParserException
     * @throws IOException
     */
    private boolean newLine() throws PatchParserException, IOException
    {
        if (pos < limit || refill())
        {
            switch (buffer[pos])
            {
                case '\r': // \r\n
                    align();

                    if (align() && ch == '\n')
                    {
                        line++;
                        col = 0;

                        return true;
                    }

                    throw expectedNewLine();

                case '\n':
                    align();
                    line++;
                    col = 0;

                    return true;
            }
        }
        return false;
    }
/*
    public int getRecordIndex()
    {
        return recordIndex;
    }*/

    public int nextToken() throws PatchParserException
    {
        clearBuffers();

        try
        {
            switch (tkType)
            {
                case TK_END_OF_DOCUMENT:
                case TK_SECTION_END:
                    // we expect the header token
                    return setTokenType( sectionHeader() ? TK_SECTION_START
                            : TK_END_OF_DOCUMENT );
                case TK_SECTION_START:
                case TK_RECORD:
                    switch (sectionID)
                    {
                        case Format.SEC_UNKOWN_SECTION:
                        {
                            return setTokenType( unknown() );
                        }
                        case Format.SEC_HEADER:
                            if (acceptClosingHeader())
                            {
                                return setTokenType( TK_SECTION_END );
                            }
                            else
                            {
                                if (( pos < limit || refill() )
                                        && Character.isDigit( buffer[pos] ))
                                {
                                    record();
                                }
                                else
                                {
                                    while (( !newLine() ) && align())
                                    {
                                        str.append( ch );
                                    }
                                }
                                return setTokenType( TK_RECORD );
                            }
                               
                        case Format.SEC_NOTE:
                            return setTokenType( notes() );

                        case Format.SEC_NAME_DUMP:
                            if (acceptClosingHeader())
                            {
                                return setTokenType( TK_SECTION_END );
                            }
                            else
                            {
                                nameDump();
                                return setTokenType( TK_RECORD );
                            }

                        default:
                            if (acceptClosingHeader())
                            {
                                return setTokenType( TK_SECTION_END );
                            }
                            else
                            {
                                record();
                                return setTokenType( TK_RECORD );
                            }
                    }
                // break;
                default:
                    throw error( "illegal state" );
            }
        }
        catch (IOException e)
        {
            throw new PatchParserException( e );
        }
    }

    private int setTokenType( int t )
    {
        /*
        if (t == TK_RECORD)
        {
            recordIndex++;
        }
        else
        {
            recordIndex = -1;
        }*/
        this.tkType = t;
        return t;
    }

    public int getTokenType()
    {
        return tkType;
    }

    /**
     * Returns true, when a valid header was found, or false when end of stream
     * is reached. When a header was found the {@link #sectionID} is set.
     * 
     * @return true, when a valid header was found, or false when end of stream
     * is reached
     * @throws PatchParserException
     * @throws IOException
     */
    private boolean sectionHeader() throws PatchParserException, IOException
    {
        while (newLine())
            ;

        if (!align())
        {
            return false;
        }

        if (ch != '[')
        {
            throw expected( "[" );
        }

        // we expect at least one character
        while (align() && ch != ']')
        {
            str.append( ch );
        }

        sectionID = Format.getSectionID( str.toString() );

        if (sectionID < 0)
        {
            sectionID = Format.SEC_UNKOWN_SECTION;

            // remember section closing header
            unsupported.replace( 0, unsupported.length(), str.toString() );
        }

        if (!newLine())
        {
            throw expectedNewLine();
        }

        return true;
    }

    private boolean acceptClosingHeader() throws PatchParserException,
            IOException
    {
        if (( !( pos < limit || refill() ) ) || buffer[pos] != '[')
        {
            return false;
        }

        align(); // '['
        if (align() && ch != '/')
        {
            throw expected( "/" );
        }

        StringBuffer name = new StringBuffer( 10 );

        while (align() && ch != ']')
        {
            name.append( ch );
        }

        if (ch != ']')
        {
            throw expected( "]" );
        }

        int ID = Format.getSectionID( name.toString() );

        if (ID < 0 || ID != sectionID)
        {
            throw error( "illegal closing-header name for "
                    + Format.getSectionName( sectionID ) + ":" + name );
        }

        if (!newLine())
        {
            if (pos < limit || refill())
            {
                // line has more characters -> illegal character
                throw expectedNewLine();
            }
            else
            {
                // We accept since we are at the end of file
                // and everything seems ok, although the
                // newline character sequence is missing.
            }
        }

        return true;
    }

    private int unknown() throws PatchParserException, IOException
    {
        // do simple readline
        while (!newLine())
        {
            if (!align())
            {
                throw unexpectedEOF();
            }
            str.append( ch );
        }

        if (str.toString().equals( "[/" + unsupported + "]" ))
        {
            return TK_SECTION_END;
        }
        else
        {
            return TK_RECORD;
        }
    }

    private void number( boolean noSign ) throws PatchParserException,
            IOException
    {
        boolean negative = false;

        // we use assureCharacter() because the sign is optional
        if (pos < limit || refill())
        {
            switch (buffer[pos])
            {
                case '-':
                    negative = true;
                // no break

                case '+':
                    if (noSign)
                    {
                        throw expected( "digit" );
                    }

                    if (!align())
                    {
                        throw expected( "digit" );
                    }
                    break;
            }
        }

        int number;

        if (( !align() ) || ( !Character.isDigit( ch ) ))
        {
            throw expected( "digit" );
        }

        number = ch - '0';

        while (( pos < limit || refill() ) && Character.isDigit( buffer[pos] ))
        {
            align();
            number = ( number * 10 ) + ( ch - '0' );
        }

        // apply the sign
        if (negative)
        {
            number = -number;
        }

        // TODO check valueCount limit

        if (valueCount == values.length)
        {
            // enlarge array
            int[] tmp = new int[values.length + DEFAULT_VALUE_COUNT];
            for (int i = valueCount - 1; i >= 0; i--)
            {
                tmp[i] = values[i];
            }
            values = tmp;
        }

        values[valueCount++] = number;
    }

    private void nameDump() throws PatchParserException, IOException
    {
        number( true ); // one number without sign

        if (( pos < limit || refill() ) && buffer[pos] == ' ')
        {
            align();

            // name dump
            // we check at the beginning of the loop for a newline
            // because an empty string is valid
            
            while (!newLine())
            {
                if (!align())
                {
                    throw unexpectedEOF();
                }
                str.append( ch ); 
            }
        }
        else if (!newLine())
        {
            throw expectedNewLine();
        }
    }

    private void record() throws PatchParserException, IOException
    {
        // \d(\w\d)*\w?\n

        // we accept any sign (+|-) or no sign
        number( false );

        boolean stop = false;

        while (( pos < limit || refill() ) && ( !stop ))
        {
            switch (buffer[pos])
            {
                case '\n':
                case '\r':
                    stop = true;
                    break;
                case ' ':

                    if (ch == ' ')
                    {
                        throw error( "illegal character: blank(' ')" );
                    }

                    align();
                    // else ok
                    break;
                default:

                    if (ch != ' ')
                    {
                        throw expected( "digit" );
                    }

                    number( false );

                    break;
            }

        }

        if (!newLine())
        {
            throw expectedNewLine();
        }

    }

    private final static String closingNoteHeader = "[/"
        + Format.getSectionName( Format.SEC_NOTE ) + "]";

    private int notes() throws PatchParserException, IOException
    {
        if (!(pos<limit || refill()))
        {
            throw unexpectedEOF();
        }
        
        char indicator = buffer[pos];
        boolean stop = false;
            
        while ((pos<limit||refill()) && (!stop))
        {
            switch (buffer[pos])
            {
                case '\n':
                case '\r':
                    stop = true;
                    break;
                default:
                    align();
                    str.append(ch);
            }
        }

        if ((indicator == '[') && (str.toString().compareTo( closingNoteHeader ) == 0))
        {
            if (!newLine())
            {
                if (!(pos<limit || refill()))
                {
                    throw expectedNewLine();
                }
            }
            return TK_SECTION_END;
        }
        
        if (!newLine())
        {
            throw expectedNewLine();
        }
        str.replace(0, str.length(), Format.getUnescapedNote(str.toString()));

        return TK_RECORD;
    }

    private PatchParserException error( String message )
    {
        return new PatchParserException( message + " " + location () );
    }

    private String location ()
    {
        String section;
        if (sectionID < 0)
        {
            section = "none";
        }
        else if (sectionID == Format.SEC_UNKOWN_SECTION)
        {
            section = "Unknown[name='" + unsupported + "']";
        }
        else
        {
            section = Format.getSectionName( sectionID );
        }
        return "@(line,column)=(" + line+ "," + col + ");section=" + section + ";";
    }

    private PatchParserException unexpectedEOF()
    {
        return error( "unexpected end of file" );
    }

    private PatchParserException expected( String what )
    {
        if (what.length() == 1)
        {
            return error( "expected character:'" + what + "'" );
        }
        else
        {
            return error( "expected:" + what );
        }
    }

    private PatchParserException expectedNewLine()
    {
        return error( "expected newline character sequence" );
    }

    @Override
    public int getSectionID()
    {
        return sectionID;
    }

    @Override
    public int getValueCount()
    {
        return valueCount;
    }

    @Override
    public int getValue( int index ) 
    {
        if (index >= valueCount) throw (new IndexOutOfBoundsException(location()+" index:"+index));

        return values[index];
    }

    @Override
    public String getString()
    {
        return str.toString();
    }

}
