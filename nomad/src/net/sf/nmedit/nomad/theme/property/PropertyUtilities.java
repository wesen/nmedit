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
 * Created on Mar 9, 2006
 */
package net.sf.nmedit.nomad.theme.property;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;

import net.sf.nmedit.jmisc.parsing.LexerException;
import net.sf.nmedit.jmisc.parsing.SimpleLexer;
import net.sf.nmedit.jmisc.parsing.StringLexer;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.DConnector;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.DCustom;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.DModule;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.DParameter;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.ModuleDescriptions;


public class PropertyUtilities
{

    private static void decoderFailed( String s, int pos, String message )
    {
        throw new DecoderException( s, pos, message );
    }

    /**
     * Returns the string <code>x,y</code> where x and y are replaced by their
     * value.
     * 
     * @param x
     * @param y
     * @return the encoded value
     */
    public static String encodeIntegerPair( int x, int y )
    {
        return x + "," + y;
    }

    /**
     * Encodes dimension
     * 
     * @param size
     *            dimension to encode
     * @see #encode(int, int)
     * @return the encoded value
     */
    public static String encodeSize( Dimension size )
    {
        return encodeIntegerPair( size.width, size.height );
    }

    /**
     * Encodes point
     * 
     * @param p
     *            coordinates to encode
     * @see #encode(int, int)
     * @return the encoded value
     */
    public static String encodeLocation( Point p )
    {
        return encodeIntegerPair( p.x, p.y );
    }

    public static Point decodeLocationE( String representation ) throws LexerException
    {
        SimpleLexer lex = new StringLexer(representation);
        Point p = new Point();
        p.x = lex.tkInteger();
        lex.tkCharacter(',');
        p.y = lex.tkInteger();
        
        return p;
    }

    public static Point decodeLocation( String representation )
    {
        try
        {
            return decodeLocationE(representation);
        }
        catch (LexerException e)
        {
            e.printStackTrace();
            return new Point(0,0);
        }
        
        /*
        Record r= new Record();
        r.slen=representation.length();
        r.spos=0;
        
        Point p = new Point();
        p.x = Parser.parseDigits(representation, r);
        r.spos++;
        p.y = Parser.parseDigits(representation, r);
        return p;
        */
    }


    public static Dimension decodeSizeE( String representation ) throws LexerException
    {
        SimpleLexer lex = new StringLexer(representation);
        Dimension d = new Dimension();
        d.width = lex.tkInteger();
        lex.tkCharacter(',');
        d.height = lex.tkInteger();
        
        return d;
    }
    
    
    public static Dimension decodeSize( String representation )
    {
        try
        {
            return decodeSizeE(representation);
        }
        catch (LexerException e)
        {
            e.printStackTrace();
            return new Dimension(0,0);
        }
        
        /*
        Record r= new Record();
        r.slen=representation.length();
        r.spos=0;
        
        Dimension d = new Dimension();
        d.width = Parser.parseDigits(representation, r);
        r.spos++;
        d.height = Parser.parseDigits(representation, r);
        return d;*/
    }

    /**
     * Encodes an integer number.
     * 
     * @param value
     * @return the encoded value
     */
    public static String encodeInteger( int value )
    {
        return Integer.toString( value );
    }

    public static int decodeInteger( String representation )
    {
        return Integer.parseInt(representation);
    }
    
    /**
     * Encodes a string
     * 
     * @param value
     * @return the encoded value TODO make string xml conform
     */
    public static String encodeString( String value )
    {
        return value;
    }

    /**
     * Encodes a boolean value
     * 
     * @param value
     * @return the encoded value
     */
    public static String encodeBoolean( boolean value )
    {
        return Boolean.toString( value );
    }

    public static boolean decodeBoolean( String representation )
    {
        return Boolean.parseBoolean(representation);
    }

    /*
     * i=italic b=bold Example: 8,i,Arial means Font Arial , size 8 , bold
     */
    // private final static Pattern p =
    // Pattern.compile("(\\d+),(([ibIB]),)?(([ibIB]),)?([^,]+)");
    /**
     * Encodes font name, size, bold and italic properties. For example the
     * encoded string for Fot Arial, Size 8, Bold is <code>8,b,Arial</code>
     * 
     * @param value
     * @return the encoded value
     */
    public static String encodeFont( Font f )
    {
        return f.getSize() + "," + ( f.isBold() ? "b," : "" )
                + ( f.isItalic() ? "i," : "" ) + f.getName();
    }

    /*
     * Syntax: size := \d+ bold := (b|B)? italic := (i|I)? name := .+ font :=
     * size((,bold)?|(,italic)?),name
     */

    public static Font decodeFont( String s )
    {
        final int STATE_ERROR = -1;
        final int STATE_DONE = 0;
        final int STATE_NONE = 1;
        final int STATE_SIZE = 2;
        final int STATE_SIZE_FINISHED = 3;

        int pos = 0;
        int len = s.length();
        char c;
        int state = STATE_NONE;

        int size = 0;
        boolean bold = false;
        boolean italic = false;
        String name = null;

        while (state >= STATE_NONE && pos < len)
        {
            c = s.charAt( pos++ );
            switch (state)
            {
                case STATE_ERROR:
                    break;// will never be reached
                case STATE_DONE:
                    break;// will never be reached
                case STATE_NONE:
                    if (Character.isDigit( c ))
                    {
                        size = c - '0';
                        state = STATE_SIZE;
                    }
                    else
                    {
                        state = STATE_ERROR;
                        decoderFailed( s, pos, "character " + c
                                + " not in set [0..9] (size)" );
                    }
                    break;
                case STATE_SIZE:
                    switch (c)
                    {
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            size = ( size * 10 ) + ( c - '0' );
                            break;
                        case ',':
                            state = STATE_SIZE_FINISHED;
                            break;
                        default:
                            state = STATE_ERROR;
                            decoderFailed( s, pos, "character " + c
                                    + " not in set [,0..9] (size)" );
                            break;
                    }
                    break;
                case STATE_SIZE_FINISHED:
                    switch (c)
                    {
                        case 'b':
                        case 'B':
                            if (pos + 1 < len && s.charAt( pos + 1 ) == ',')
                            {
                                bold = true;
                            }
                            else
                            {
                                name = s.substring( pos );
                                state = STATE_DONE;
                            }

                            break;
                        case 'i':
                        case 'I':
                            if (pos + 1 < len && s.charAt( pos + 1 ) == ',')
                            {
                                italic = true;
                            }
                            else
                            {
                                name = s.substring( pos );
                                state = STATE_DONE;
                            }

                            break;
                        default:
                            name = s.substring( pos );
                            state = STATE_DONE;
                            break;
                    }
                    break;
            }
        }

        if (state != STATE_DONE)
        {
            decoderFailed( s, pos, "unknown format" );
            return null;
        }

        int atts = Font.PLAIN;
        if (bold)
        {
            atts = Font.BOLD;
            if (italic)
            {
                atts |= Font.ITALIC;
            }
        }
        else if (italic)
        {
            atts = Font.ITALIC;
        }

        Font f = new Font( name, atts, size );

        if (f == null) decoderFailed( s, pos, "font not found" );

        return f;
    }

    /*
     * moduleid := \d+ connectorid := \d+ type := (in|out) any := .* connector :=
     * moduleid . connectorid . type (. any)?
     */

    public static DConnector decodeConnector( String s )
    {
        final int STATE_ERROR = -1;
        final int STATE_DONE = 0;
        final int STATE_NONE = 1;
        final int STATE_MODULE_ID = 2;
        final int STATE_CONNECTOR_ID = 3;
        final int STATE_TYPE = 4;

        int pos = 0;
        int len = s.length();
        char c;

        int state = STATE_NONE;
        int moduleId = 0;
        int connectorId = 0;
        boolean isInput = false;

        while (pos < len)
        {
            c = s.charAt( pos++ );

            switch (state)
            {
                case STATE_ERROR:
                    break;// will never be reached
                case STATE_DONE:
                    break;// will never be reached
                case STATE_NONE:
                    if (Character.isDigit( c ))
                    {
                        moduleId = c - '0';
                        state = STATE_MODULE_ID;
                    }
                    else
                    {
                        state = STATE_ERROR;
                        decoderFailed( s, pos, "character " + c
                                + " not in set [,0..9] (module id)" );
                    }
                    break;
                case STATE_MODULE_ID:
                    switch (c)
                    {
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            moduleId = ( moduleId * 10 ) + ( c - '0' );
                            break;
                        case '.':
                            state = STATE_CONNECTOR_ID;
                            break;
                        default:
                            state = STATE_ERROR;
                            decoderFailed( s, pos, "character " + c
                                    + " not in set [.0..9] (module id)" );
                            break;
                    }
                    break;
                case STATE_CONNECTOR_ID:
                    switch (c)
                    {
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            connectorId = ( connectorId * 10 ) + ( c - '0' );
                            break;
                        case '.':
                            state = STATE_TYPE;
                            break;
                        default:
                            state = STATE_ERROR;
                            decoderFailed( s, pos, "character " + c
                                    + " not in set [.0..9] (connector id)" );
                            break;
                    }
                    break;
                case STATE_TYPE:
                    switch (c)
                    {
                        case 'i':
                            if (s.startsWith( "in.", pos-1 ) || s.endsWith( "in" ))
                            {
                                isInput = true;
                                state = STATE_DONE;
                            }
                            else
                            {
                                state = STATE_ERROR;
                                decoderFailed( s, pos,
                                        "expected character sequence 'in'" );
                            }
                            break;
                        case 'o':
                            if (s.startsWith( "out.", pos-1 ) || s.endsWith( "out" ))
                            {
                                isInput = false;
                                state = STATE_DONE;
                            }
                            else
                            {
                                state = STATE_ERROR;
                                decoderFailed( s, pos,
                                        "expected character sequence 'out'" );
                            }
                            break;
                        default:
                            state = STATE_ERROR;
                            decoderFailed( s, pos,
                                    "expected character sequence (in|out)" );
                            break;
                    }
                    break;
            }
        }

        DModule module = ModuleDescriptions.sharedInstance().getModuleById(
                moduleId );
        if (module == null)
        {
            decoderFailed( s, pos, "Module [id=" + moduleId + "] not found." );
            return null;
        }

        DConnector connector = module.getConnectorById( connectorId, isInput );
        if (connector == null)
        {
            decoderFailed( s, pos, "Connector [module[id=" + moduleId + "]/id="
                    + connectorId + "] not found." );
        }

        return connector;
    }

    public static String encodeConnector( DConnector p )
    {
        return ( p == null ) ? "" : p.getParent().getModuleID() + "."
                + p.getId() + "." + ( p.isInput() ? "in" : "out" ) + "."
                + p.getName();
    }

    /*
     * moduleid := \d+ parameterid := \d+ type := [cp] ; c == custom , p =
     * parameter any := .* parameter := moduleid . parameterid . type (. any)?
     */

    public static DParameter decodeParameter( String s )
    {
        final int STATE_ERROR = -1;
        final int STATE_DONE = 0;
        final int STATE_NONE = 1;
        final int STATE_MODULE_ID = 2;
        final int STATE_PARAMETER_ID = 3;
        final int STATE_TYPE = 4;

        int pos = 0;
        int len = s.length();
        char c;

        int state = STATE_NONE;
        int moduleId = 0;
        int parameterId = 0;
        boolean isCustom = false;

        while (pos < len)
        {
            c = s.charAt( pos++ );

            switch (state)
            {
                case STATE_ERROR:
                    break;// will never be reached
                case STATE_DONE:
                    break;// will never be reached
                case STATE_NONE:
                    if (Character.isDigit( c ))
                    {
                        moduleId = c - '0';
                        state = STATE_MODULE_ID;
                    }
                    else
                    {
                        state = STATE_ERROR;
                        decoderFailed( s, pos, "character " + c
                                + " not in set [,0..9] (module id)" );
                    }
                    break;
                case STATE_MODULE_ID:
                    switch (c)
                    {
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            moduleId = ( moduleId * 10 ) + ( c - '0' );
                            break;
                        case '.':
                            state = STATE_PARAMETER_ID;
                            break;
                        default:
                            state = STATE_ERROR;
                            decoderFailed( s, pos, "character " + c
                                    + " not in set [.0..9] (module id)" );
                            break;
                    }
                    break;
                case STATE_PARAMETER_ID:
                    switch (c)
                    {
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            parameterId = ( parameterId * 10 ) + ( c - '0' );
                            break;
                        case '.':
                            state = STATE_TYPE;
                            break;
                        default:
                            state = STATE_ERROR;
                            decoderFailed( s, pos, "character " + c
                                    + " not in set [.0..9] (connector id)" );
                            break;
                    }
                    break;
                case STATE_TYPE:
                    switch (c)
                    {
                        case 'c':
                            if (( ( pos == len ) && s.endsWith( "c" ) )
                                    || s.startsWith( "c.", pos-1 ))
                            {
                                isCustom = true;
                                state = STATE_DONE;
                            }
                            else
                            {
                                state = STATE_ERROR;
                                decoderFailed( s, pos,
                                        "expected character sequence 'c'" );
                            }
                            break;
                        case 'p':
                            if (( ( pos == len ) && s.endsWith( "p" ) )
                                    || s.startsWith( "p.", pos-1 ))
                            {
                                isCustom = false;
                                state = STATE_DONE;
                            }
                            else
                            {
                                state = STATE_ERROR;
                                decoderFailed( s, pos,
                                        "expected character sequence 'p'" );
                            }
                            break;
                        default:
                            state = STATE_ERROR;
                            decoderFailed( s, pos,
                                    "expected character from set [cp]" );
                            break;
                    }
                    break;
            }
        }

        DModule module = ModuleDescriptions.sharedInstance().getModuleById(
                moduleId );
        if (module == null)
        {
            decoderFailed( s, pos, "Module [id=" + moduleId + "] not found." );
            return null;
        }

        DParameter p = isCustom ? module.getCustomParamById( parameterId )
                : module.getParameter( parameterId );
        if (p == null)
        {
            decoderFailed( s, pos, ( isCustom ? "Custom" : "Parameter" )
                    + "[module[id=" + moduleId + "]/id=" + parameterId
                    + "] not found." );
        }

        return p;
    }

    public static String encodeParameter( DParameter p )
    {
        return ( p == null ) ? "" : p.getParent().getModuleID() + "."
                + p.getId() + "." + ( p instanceof DCustom ? "c" : "p" ) + "."
                + p.getName();
    }
}
