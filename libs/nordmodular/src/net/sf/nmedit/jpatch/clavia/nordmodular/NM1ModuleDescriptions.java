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
 * Created on Dec 10, 2006
 */
package net.sf.nmedit.jpatch.clavia.nordmodular;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import net.sf.nmedit.jpatch.ModuleDescriptions;
import net.sf.nmedit.jpatch.ModuleDescriptionsParser;
import net.sf.nmedit.jpatch.impl.PBasicModuleMetrics;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class NM1ModuleDescriptions extends ModuleDescriptions
{

    public static NM1ModuleDescriptions parse(ClassLoader loader, InputStream stream) throws ParserConfigurationException, SAXException, IOException
    {
        NM1ModuleDescriptions mod = new NM1ModuleDescriptions(loader);
        
        try
        {
            ModuleDescriptionsParser.parse(mod, stream);
        }
        catch (SAXParseException spe)
        {
            Log log = LogFactory.getLog(NM1ModuleDescriptions.class);
            if (log.isErrorEnabled())
            {
                log.error("error in parse("+loader+","+stream+"); " +
                		"@"+spe.getLineNumber()+":"+spe.getColumnNumber(), spe);
            }
        }
        return mod;
    }

    public NM1ModuleDescriptions(ClassLoader loader)
    {
        
        super(loader);
        this.metrics = new PBasicModuleMetrics(255, 15, 4000, 4000);
    }
    
}
