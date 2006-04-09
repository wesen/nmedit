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
package net.sf.nmedit.patch.parser;

import net.sf.nmedit.patch.FactoryException;

public class PatchParserFactory
{

    private final static String BASE_PACKAGE           = "net.sf.nmedit.patch.parser";

    public final static String  DEFAULT_FILE_PARSER    = BASE_PACKAGE+".StreamParser";
    public final static String  DEFAULT_SYNTH_PARSER   = BASE_PACKAGE+".SynthParser";
    public final static String  DEFAULT_VIRTUAL_PARSER = BASE_PACKAGE+".VirtualParser";

    /**
     * true when a validating parser should be created
     */
    private boolean     validationEnabled      = false;

    /**
     * parser class name 
     */
    private String              parserName             = null;

    /**
     * Creates a new factory instance.
     * @return a new factory instance
     */
    public static PatchParserFactory newInstance()
    {
        return new PatchParserFactory();
    }

    /**
     * Creates a new parser.
     * 
     * @return
     * @throws FactoryException
     * @throws PatchParserException
     */
    public PatchParser newPatchParser() throws FactoryException
    {
        PatchParser parser = createInstance();
        if (!validationEnabled)
        {
            return parser;
        }
        else
        {
            return new Validator( parser );
        }
    }

    /**
     * Sets the favoured parser.
     * 
     * @param parserName class name of the parser
     */
    public void setParser( String parserName )
    {
        this.parserName = parserName;
    }

    /**
     * Returns the favoured parser's name.
     * If the name was not set with {@link #setParser(String)} <code>null</code>
     * is returned. 
     * 
     * @return the favoured parser's name
     */
    public String getParserName()
    {
        return this.parserName;
    }

    /**
     * Sets the validation flag.
     * 
     * @param enable
     */
    public void setValidationEnabled( boolean enable )
    {
        validationEnabled = enable;
    }
    
    /**
     * 
     * 
     * @return
     */
    public boolean isValidationEnabled()
    {
        return validationEnabled;
    }

    protected PatchParser createInstance() throws FactoryException
    {
        Class parserClass;
        try
        {
            parserClass = Class.forName( parserName );
        }
        catch (Exception e)
        {
            throw new FactoryException( "Parser class not found:" + parserName,
                    e );
        }

        PatchParser instance;
        try
        {
            instance = (PatchParser) parserClass.newInstance();
        }
        catch (Exception e)
        {
            throw new FactoryException( "Parser could not be instantiated:"
                    + parserName, e );
        }

        return instance;
    }

}
