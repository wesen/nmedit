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

import net.sf.nmedit.nomad.core.application.ApplicationInstantiationException;
import net.sf.nmedit.nomad.core.application.ProgressMeter;

import org.nomad.main.Nomad;
import org.nomad.util.misc.NomadUtilities;

public class NomadApplication extends NomadEnvironment
{
    public NomadApplication( String[] args )
    {
        super( args );
    }

    private Nomad mainFrame = null;

    @Override
    protected void startInternal(ProgressMeter progress) throws ApplicationInstantiationException
    {
        super.startInternal(progress);
        progress.increment("Loading: Nomad");
        
        mainFrame = new Nomad(getName());
        configureMainFrame(mainFrame);

        NomadUtilities.setupAndShow(mainFrame, 0.75, 0.75);
    }

    @Override
    protected void stopInternal()
    {
        mainFrame.dispose();
        super.stopInternal();
    }

}
