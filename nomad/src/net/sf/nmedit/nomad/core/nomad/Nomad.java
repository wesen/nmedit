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
 * Created on Mar 31, 2006
 */
package net.sf.nmedit.nomad.core.nomad;

import net.sf.nmedit.nomad.core.application.Application;
import net.sf.nmedit.nomad.core.application.ApplicationInstantiationException;
import net.sf.nmedit.nomad.core.application.Const;

public class Nomad
{

    public static void main( String[] args )
            throws ApplicationInstantiationException
    {
        Application.setProperty(Const.APPLICATION_CLASS_NAME, "net.sf.nmedit.nomad.core.nomad.NomadApplication");
        Application.setProperty(Const.APPLICATION_NAME, "Nomad");
        Application.setProperty(Const.APPLICATION_VERSION, "0.2");
        Application.setProperty(Const.APPLICATION_LOGFILE, "nomad.log");
        Application.setProperty(Const.APPLICATION_PROPERTYFILE, "conf/application.xml");

        Application.create( args );
    }

}
