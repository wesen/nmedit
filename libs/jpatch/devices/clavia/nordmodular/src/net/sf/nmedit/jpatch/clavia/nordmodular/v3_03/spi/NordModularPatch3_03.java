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
 * Created on Jun 20, 2006
 */
package net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spi;

import net.sf.nmedit.jpatch.Patch;
import net.sf.nmedit.jpatch.spi.PatchImplementation;

public class NordModularPatch3_03 extends PatchImplementation
{

    public final static String NAME = "Clavia Nord Modular Patch";
    public final static String VERSION = "3.03";
    
    public NordModularPatch3_03()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    public String getName()
    {
        return NAME;
    }

    @Override
    public String getVersion()
    {
        return VERSION;
    }

    @Override
    public String getVendor()
    {
        return "http://nmedit.sf.net";
    }

    @Override
    public Patch createPatch()
    {
        return new net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Patch(this);
    }

    @Override
    public String getDefaultFileExtension()
    {
        return "pch";
    }

}
