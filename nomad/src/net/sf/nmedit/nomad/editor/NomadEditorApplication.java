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
package net.sf.nmedit.nomad.editor;

import net.sf.nmedit.nomad.core.application.ApplicationInstantiationException;
import net.sf.nmedit.nomad.core.application.ProgressMeter;
import net.sf.nmedit.nomad.core.nomad.NomadEnvironment;
import net.sf.nmedit.nomad.editor.app.UIEditor;
import net.sf.nmedit.nomad.util.NomadUtilities;


public class NomadEditorApplication extends NomadEnvironment
{
    
    public NomadEditorApplication( String[] args )
    {
        super( args );
    }

    private UIEditor mainFrame = null;

    @Override
    protected void startInternal(ProgressMeter progress) throws ApplicationInstantiationException
    {
        super.startInternal(progress);
        
        mainFrame = new UIEditor(getName());
        NomadUtilities.setupAndShow(mainFrame, 0.4, 0.5);
        configureMainFrame(mainFrame);
        //mainFrame.initialLoading();
    }

    @Override
    protected void stopInternal()
    {
        mainFrame.dispose();
        super.stopInternal();
    }

}
