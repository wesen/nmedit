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

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import net.sf.nmedit.nomad.core.application.ApplicationInstantiationException;
import net.sf.nmedit.nomad.core.application.ProgressMeter;
import net.sf.nmedit.nomad.main.Nomad;


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
        
        mainFrame = new Nomad(getName()+" "+getVersion());
        // TODO add config... again but send an event rather than closing immediately
        //configureMainFrame(mainFrame);

        //NomadUtilities.setupAndShow(mainFrame, 0.75, 0.75);
        //mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        

        
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
       
        mainFrame.addWindowListener(
          new WindowAdapter()
          {
              public void windowClosing( WindowEvent event )
              {
                  mainFrame.exitNomad();
              }
          }
        );
        
        mainFrame.setVisible(true);
    }

    @Override
    protected void stopInternal()
    {
        mainFrame.shutdown();
        mainFrame.dispose();
        super.stopInternal();
    }

}
